package org.spring.cdi.decorator.strategies;

import org.spring.cdi.decorator.model.DecoratorInfo;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


/**
 * {@link DelegateResolutionStrategy} to implement resolution logic to find delegate for a given decorator.
 * 
 * @author nschlimm
 * 
 */
public interface DelegateResolutionStrategy {
	
	/**
	 * Search the delegate for the given {@link DecoratorInfo}.
	 * 
	 * @param beanFactory bean factory that contains the delegate
	 * @param decoratorInfo decorator that holds the delegate field
	 * @return the name of the delegate bean
	 */
	String getRegisteredDelegate(ConfigurableListableBeanFactory beanFactory, DecoratorInfo decoratorInfo);

}
