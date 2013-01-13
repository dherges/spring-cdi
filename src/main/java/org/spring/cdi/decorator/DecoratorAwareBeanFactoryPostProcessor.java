package org.spring.cdi.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.cdi.SpringCDIInfrastructure;
import org.spring.cdi.decorator.model.DecoratorInfo;
import org.spring.cdi.decorator.model.DecoratorMetaDataBean;
import org.spring.cdi.decorator.model.QualifiedDecoratorChain;
import org.spring.cdi.decorator.processor.DecoratorAwareBeanPostProcessor;
import org.spring.cdi.decorator.resolver.DecoratorAwareAutowireCandidateResolver;
import org.spring.cdi.decorator.resolver.rules.BeanPostProcessorCDIAutowiringRules;
import org.spring.cdi.decorator.resolver.rules.DecoratorAutowiringRules;
import org.spring.cdi.decorator.resolver.rules.ResolverCDIAutowiringRules;
import org.spring.cdi.decorator.strategies.DecoratorOrderingStrategy;
import org.spring.cdi.decorator.strategies.DecoratorResolutionStrategy;
import org.spring.cdi.decorator.strategies.DelegateResolutionStrategy;
import org.spring.cdi.decorator.strategies.impl.SimpleDecoratorOrderingStrategy;
import org.spring.cdi.decorator.strategies.impl.SimpleDecoratorResolutionStrategy;
import org.spring.cdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;


/**
 * This {@link BeanFactoryPostProcessor} can deal with JSR-299 CDI @Decorator and @Delegate annotations.
 * 
 * It determines all registered decorarors in the given bean factory. For each delegate bean it will generate
 * {@link QualifiedDecoratorChain}. The {@link QualifiedDecoratorChain} holds all meta data required for autowiring in the
 * {@link DecoratorAwareAutowireCandidateResolver} or {@link DecoratorAwareBeanPostProcessor}.
 * 
 * @author Niklas Schlimm
 * 
 */
@SuppressWarnings("rawtypes")
public class DecoratorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered, InitializingBean {

	private static final String DECORATOR_POSTPROCESSOR_NAME = "_decoratorPostprocessor";

	private static final String RESOLVER = "resolver";

	private static final String PROCESSOR = "processor";

	protected final Log logger = LogFactory.getLog(getClass());

	private DecoratorResolutionStrategy decoratorResolutionStrategy;

	private DelegateResolutionStrategy delegateResolutionStrategy;

	private DecoratorAutowiringRules decoratorAutowiringRules;

	private DecoratorOrderingStrategy decoratorOrderingStrategy;

	private String mode = PROCESSOR;

	public DecoratorAwareBeanFactoryPostProcessor() {
		super();
	}

	public DecoratorAwareBeanFactoryPostProcessor(DecoratorResolutionStrategy decoratorResolutionStrategy, DelegateResolutionStrategy delegateResolutionStrategy,
			DecoratorAutowiringRules decoratorAutowiringRules, DecoratorOrderingStrategy decoratorOrderingStrategy) {
		super();
		this.decoratorResolutionStrategy = decoratorResolutionStrategy;
		this.delegateResolutionStrategy = delegateResolutionStrategy;
		this.decoratorAutowiringRules = decoratorAutowiringRules;
		this.decoratorOrderingStrategy = decoratorOrderingStrategy;
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		Map<String, Class> decorators = decoratorResolutionStrategy.getRegisteredDecorators(beanFactory);
		List<QualifiedDecoratorChain> chains = buildQualifiedDecoratorChains(beanFactory, decorators);
		registerAutowireCandidateResolver(beanFactory, chains);
		registerDecoratorMetadataBean(beanFactory, chains);

		if (PROCESSOR.equals(getMode())) {
			((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(DECORATOR_POSTPROCESSOR_NAME, BeanDefinitionBuilder.rootBeanDefinition(DecoratorAwareBeanPostProcessor.class)
					.getBeanDefinition());
			if (beanFactory.getBeanNamesForType(DecoratorAwareBeanPostProcessor.class) == null) {
				throw new DecoratorAwareBeanFactoryPostProcessorException("Mode 'processor' requires DecoratorAwareBeanPostProcessor registered!");
			}
		}

	}

	private void registerDecoratorMetadataBean(ConfigurableListableBeanFactory beanFactory, List<QualifiedDecoratorChain> chains) {
		if (beanFactory.containsBean("decoratorMetaData")) {
			DecoratorMetaDataBean metaData = (DecoratorMetaDataBean) beanFactory.getBean("decoratorMetaData");
			metaData.setDecoratorChains(chains);
		} else {
			DecoratorMetaDataBean metaData = new DecoratorMetaDataBean(chains);
			beanFactory.registerSingleton("decoratorMetaData", metaData);
		}
	}

	/**
	 * Registers the {@link AutowireCandidateResolver}, more specifically the {@link DecoratorAutowiringRules} with the resolver.
	 * If the bean is already configured with a {@link SpringCDIInfrastructure} candidate resolver, then this method just adds the
	 * {@link DecoratorAutowiringRules} plugin to that current resolver. Otherwise it will create the
	 * {@link DecoratorAwareAutowireCandidateResolver} and set it on the bean facrory given.
	 * 
	 * @param beanFactory
	 *            the factory to set the autowire candidate resolver
	 * @param chains
	 *            the qualified decorator chains
	 */
	private void registerAutowireCandidateResolver(ConfigurableListableBeanFactory beanFactory, List<QualifiedDecoratorChain> chains) {
		AutowireCandidateResolver resolver = ((DefaultListableBeanFactory) beanFactory).getAutowireCandidateResolver();
		if (resolver != null && resolver instanceof SpringCDIInfrastructure) {
			((SpringCDIInfrastructure) resolver).addPlugin(decoratorAutowiringRules == null ? new ResolverCDIAutowiringRules(chains, resolver, beanFactory) : decoratorAutowiringRules);
		} else {
			DecoratorAwareAutowireCandidateResolver newResolver = new DecoratorAwareAutowireCandidateResolver();
			newResolver.setBeanFactory(beanFactory);
			((DefaultListableBeanFactory) beanFactory).setAutowireCandidateResolver(newResolver);

			if (RESOLVER.equals(getMode()) && decoratorAutowiringRules == null) {
				decoratorAutowiringRules = new ResolverCDIAutowiringRules(chains, newResolver, beanFactory);
			}
			if (PROCESSOR.equals(getMode()) && decoratorAutowiringRules == null) {
				decoratorAutowiringRules = new BeanPostProcessorCDIAutowiringRules(new DecoratorMetaDataBean(chains), newResolver, beanFactory);
			}
			newResolver.addPlugin(decoratorAutowiringRules);
		}
	}

	/**
	 * Builds all {@link QualifiedDecoratorChain}. Every target delegate bean has exactly one decorator chain. Every decorator is
	 * exclusive for one delegate target bean.
	 * 
	 * @param beanFactory
	 *            the current bean factory
	 * @param decorators
	 *            the decorators registered in the given bean factory
	 * @return the qualified decorator chains
	 */
	private List<QualifiedDecoratorChain> buildQualifiedDecoratorChains(ConfigurableListableBeanFactory beanFactory, Map<String, Class> decorators) {
		List<DecoratorInfo> decoratorInfos = new ArrayList<DecoratorInfo>();
		List<QualifiedDecoratorChain> chains = new ArrayList<QualifiedDecoratorChain>();
		for (String bdName : decorators.keySet()) {
			DecoratorInfo newDecoratorInfo = new DecoratorInfo(bdName, beanFactory.getBeanDefinition(bdName), decorators.get(bdName));
			decoratorInfos.add(newDecoratorInfo);
			String delegate = delegateResolutionStrategy.getRegisteredDelegate(beanFactory, newDecoratorInfo);
			QualifiedDecoratorChain chain = null;
			// Is there already a chain for that delegate?
			for (QualifiedDecoratorChain qualifiedDecoratorChain : chains) {
				if (qualifiedDecoratorChain.getDelegateBeanDefinitionHolder().getBeanName().equals(delegate)) {
					chain = qualifiedDecoratorChain;
				}
			}
			if (chain == null) {
				chain = new QualifiedDecoratorChain(new BeanDefinitionHolder(beanFactory.getBeanDefinition(delegate), delegate));
				chains.add(chain);
			}
			chain.addDecoratorInfo(newDecoratorInfo);
			decoratorOrderingStrategy.orderDecorators(chain);
		}
		return chains;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (decoratorResolutionStrategy == null) {
			decoratorResolutionStrategy = new SimpleDecoratorResolutionStrategy();
		}
		if (delegateResolutionStrategy == null) {
			delegateResolutionStrategy = new SimpleDelegateResolutionStrategy();
		}
		if (decoratorOrderingStrategy == null) {
			decoratorOrderingStrategy = new SimpleDecoratorOrderingStrategy();
		}
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

}
