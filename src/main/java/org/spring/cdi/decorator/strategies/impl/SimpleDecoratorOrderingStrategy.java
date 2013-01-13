package org.spring.cdi.decorator.strategies.impl;

import org.spring.cdi.decorator.model.QualifiedDecoratorChain;
import org.spring.cdi.decorator.strategies.DecoratorOrderingStrategy;
import org.spring.cdi.decorator.strategies.DecoratorResolutionStrategy;

/**
 * Simple ordering strategy just orders decorators as found in the {@link DecoratorResolutionStrategy}.
 * 
 * @author Niklas Schlimm
 *
 */
public class SimpleDecoratorOrderingStrategy implements DecoratorOrderingStrategy {

	@Override
	public QualifiedDecoratorChain orderDecorators(QualifiedDecoratorChain chain) {
		return chain;
	}

}
