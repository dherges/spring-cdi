package org.spring.cdi.decorator.strategies.impl;

import java.lang.reflect.Field;

import org.spring.cdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;
import org.spring.cdi.decorator.DecoratorModuleUtils;
import org.spring.cdi.decorator.model.QualifiedDecoratorChain;
import org.spring.cdi.decorator.strategies.DecoratorChainingStrategy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ReflectionUtils;


/**
 * Simple decorator chaining strategy that creates the decorator chain. Can deal with scoped proxies and AOP proxies.
 * 
 * @author Niklas Schlimm
 * 
 */
public class SimpleDecoratorChainingStrategy implements DecoratorChainingStrategy {

	private static final String SCOPED_TARGET = "scopedTarget.";

	public Object getChainedDecorators(ConfigurableListableBeanFactory beanFactory, QualifiedDecoratorChain chain, Object delegate) {
		for (int i = 0; i < chain.getDecorators().size(); i++) {
			// Predecessor must be the original target bean (if proxied)
			Object predecessor = getTargetBean(beanFactory, chain.getDecorators().get(i).getDecoratorBeanDefinitionHolder().getBeanName());
			Object successor = delegate;
			if (i < chain.getDecorators().size() - 1) {
				// successor is not the delegate, but a succeeding decorator
				successor = beanFactory.getBean(chain.getDecorators().get(i + 1).getDecoratorBeanDefinitionHolder().getBeanName());
			}
			ReflectionUtils.makeAccessible(chain.getDecorators().get(i).getDelegateFields().get(0).getDeclaredField());
			try {
				Field delegateField = chain.getDecorators().get(i).getDelegateFields().get(0).getDeclaredField();
				delegateField.set(predecessor, successor);
			} catch (Exception e) {
				throw new DecoratorAwareBeanFactoryPostProcessorException("Could not set decorator field!", e);
			}
		}
		return beanFactory.getBean(chain.getDecorators().get(0).getDecoratorBeanDefinitionHolder().getBeanName());
	}

	/**
	 * Retrieves target beans for scoped proxies and AOP proxies
	 * @param beanFactory the bean factory for bean lookup
	 * @param beanName the name of the bean under investigation
	 * @return the original target
	 */
	private Object getTargetBean(ConfigurableListableBeanFactory beanFactory, String beanName) {
		Object targetBean = null;
		if (beanFactory.containsBean(SCOPED_TARGET + beanName)) {
			targetBean = beanFactory.getBean(SCOPED_TARGET + beanName);
		} else {
			targetBean = beanFactory.getBean(beanName);
		}
		if (AopUtils.isAopProxy(targetBean)) {
			targetBean = DecoratorModuleUtils.locateAopTarget(beanName, targetBean);
		}
		return targetBean;
	}

}
