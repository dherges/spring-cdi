package org.spring.cdi.decorator.resolver.rules;

import org.spring.cdi.decorator.resolver.DecoratorAwareAutowireCandidateResolver;
import org.springframework.beans.factory.config.DependencyDescriptor;


/**
 * Tagging interface to mark a {@link DependencyDescriptor} that was instantiated during decorator autowiring logic. The
 * {@link DecoratorAwareAutowireCandidateResolver} will ignore specific {@link DecoratorAutowiringRules} for
 * {@link DependencyDescriptor} that is marked with this interface.
 * 
 * @author Niklas Schlimm
 * @see DecoratorAwareAutowireCandidateResolver, SimpleDelegateResolutionStrategy
 * 
 */
public interface IgnoreDecoratorAutowiringLogic {}
