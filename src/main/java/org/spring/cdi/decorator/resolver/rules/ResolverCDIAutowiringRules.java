package org.spring.cdi.decorator.resolver.rules;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.spring.cdi.decorator.DecoratorAwareBeanFactoryPostProcessor;
import org.spring.cdi.decorator.DecoratorModuleUtils;
import org.spring.cdi.decorator.model.DecoratorInfo;
import org.spring.cdi.decorator.model.QualifiedDecoratorChain;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.util.Assert;


/**
 * Class implements the wiring rules for autowiring CDI decorators. This rule set is used when
 * {@link DecoratorAwareBeanFactoryPostProcessor} mode is set to 'resolver'.
 * 
 * @author Niklas Schlimm
 * 
 */
public class ResolverCDIAutowiringRules implements DecoratorAutowiringRules {

	private List<QualifiedDecoratorChain> decoratorChains;

	private AutowireCandidateResolver resolver;

	private ConfigurableListableBeanFactory beanFactory;

	public ResolverCDIAutowiringRules() {
		super();
	}

	public ResolverCDIAutowiringRules(List<QualifiedDecoratorChain> decoratorChains, AutowireCandidateResolver resolver, ConfigurableListableBeanFactory beanFactory) {
		super();
		this.decoratorChains = decoratorChains;
		this.resolver = resolver;
		this.beanFactory = beanFactory;
	}

	/**
	 * Main method that is called to check if the given {@link BeanDefinitionHolder} is a candidate for the given injection point
	 * described by the {@link DependencyDescriptor}.
	 * 
	 * @param bdHolder
	 *            candidate bean
	 * @param descriptor
	 *            injection point
	 * @return true if candidate bean can be wired into injection point
	 * 
	 */
	public boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		boolean isDelegateDescriptor = false;
		if (DecoratorInfo.isDelegateField(descriptor.getField()))
			isDelegateDescriptor = true;
		if (descriptor instanceof IgnoreDecoratorAutowiringLogic || (!isDelegateDescriptor && !descriptor.getDependencyType().isInterface()))
			return true;
		boolean match = false;
		if (isDecoratedInjectionPoint(descriptor)) {
			// Descriptor refers to a decorated target bean => bdHolder bean name must match last decorator bean name of the
			// decorator chain
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratedInjectionPoint(descriptor);
			DecoratorInfo firstDecoratorInfo = chain.getDecorators().get(0);
			if (firstDecoratorInfo.getDecoratorBeanDefinitionHolder().getBeanName().equals(bdHolder.getBeanName())) {
				return true;
			}
		} else if (DecoratorInfo.isDecorator(descriptor)) {
			QualifiedDecoratorChain chain = getDecoratorChainForDecoratorDescriptor(descriptor);
			// descriptor must be predecessor decorator for bdHolder
			if (chain.areSequential(bdHolder, descriptor))
				return true;
		}
		return match;
	}

	/**
	 * Method checks if the given descriptor is a decorated injection point. That is, the injection point matches a delegate bean
	 * definition of a known {@link QualifiedDecoratorChain}.
	 * 
	 * @param descriptor
	 *            injection point to check
	 * @return true if injection point must be decorated
	 */
	public boolean isDecoratedInjectionPoint(DependencyDescriptor descriptor) {
		// Field is not in a decorator, but descriptor matches a target bean definition
		if (!DecoratorInfo.isDecorator(descriptor.getField().getDeclaringClass())) {
			// Now that we know, that we're aoutside a @Decorator:
			// is there a chain that contains a target delegate bean definition that matches the descriptor?
			for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
				String delegateName = decoratorChain.getDelegateBeanDefinitionHolder().getBeanName();
				// Check qualifiers and type of the chain's delegate vs. the descriptor
				if (resolver.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(),
						DecoratorModuleUtils.createRuleBasedDescriptor(descriptor.getField(), new Class[] { IgnoreDecoratorAutowiringLogic.class }))
						&& beanFactory.isTypeMatch(delegateName, descriptor.getDependencyType())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the {@link QualifiedDecoratorChain} for a decorated injection point
	 * 
	 * @param decoratedInjectionPoint
	 * @return {@link QualifiedDecoratorChain} that applies to the injection point
	 */
	public QualifiedDecoratorChain getDecoratorChainForDecoratedInjectionPoint(DependencyDescriptor decoratedInjectionPoint) {
		// Match: a chain contains a target bean definition that matches the target descriptor
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			String delegateName = decoratorChain.getDelegateBeanDefinitionHolder().getBeanName();
			// Check qualifiers and type of the chain's delegate and the descriptor
			if (resolver.isAutowireCandidate(decoratorChain.getDelegateBeanDefinitionHolder(),
					DecoratorModuleUtils.createRuleBasedDescriptor(decoratedInjectionPoint.getField(), new Class[] { IgnoreDecoratorAutowiringLogic.class }))
					&& beanFactory.isTypeMatch(delegateName, decoratedInjectionPoint.getDependencyType())) {
				return decoratorChain;
			}
		}
		return null;
	}

	public QualifiedDecoratorChain getDecoratorChainForDecoratorDescriptor(DependencyDescriptor decoratorDescriptor) {
		for (QualifiedDecoratorChain decoratorChain : decoratorChains) {
			Set<Field> fields = decoratorChain.getAllDeclaredDelegateFields();
			if (fields.contains(decoratorDescriptor.getField())) {
				return decoratorChain;
			}
		}
		return null;
	}

	public List<QualifiedDecoratorChain> getDecoratorChains() {
		return decoratorChains;
	}

	@Override
	public boolean executeLogic(Object... arguments) {
		Assert.isTrue(arguments.length == 2, "Expect two arguments!");
		Assert.isTrue(arguments[0] instanceof BeanDefinitionHolder);
		Assert.isTrue(arguments[1] instanceof DependencyDescriptor);
		return applyDecoratorAutowiringRules((BeanDefinitionHolder) arguments[0], (DependencyDescriptor) arguments[1]);
	}

}
