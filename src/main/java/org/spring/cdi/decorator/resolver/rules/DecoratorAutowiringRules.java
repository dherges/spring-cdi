package org.spring.cdi.decorator.resolver.rules;

import org.spring.cdi.SpringCDIPlugin;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;


/**
 * Interface that is implemented by decorator autowiring rule sets.
 * 
 * Clients can implement their own rule sets to enhance wiring logic.
 * 
 * @author Niklas Schlimm
 *
 */
public interface DecoratorAutowiringRules extends SpringCDIPlugin {

	boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor);

}
