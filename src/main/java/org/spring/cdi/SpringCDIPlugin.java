package org.spring.cdi;

import org.spring.cdi.decorator.resolver.DecoratorAwareAutowireCandidateResolver;
import org.spring.cdi.decorator.resolver.rules.BeanPostProcessorCDIAutowiringRules;
import org.spring.cdi.decorator.resolver.rules.ResolverCDIAutowiringRules;

/**
 * Interface implemented by Spring-CDI plugins. Enables registry of rule set plugin with {@link DecoratorAwareAutowireCandidateResolver}.
 * 
 * @author Niklas Schlimm
 * @see {@link ResolverCDIAutowiringRules}, {@link DecoratorAwareAutowireCandidateResolver}, {@link BeanPostProcessorCDIAutowiringRules}
 *
 */
public interface SpringCDIPlugin {
	
	boolean executeLogic(Object... arguments);
	
}
