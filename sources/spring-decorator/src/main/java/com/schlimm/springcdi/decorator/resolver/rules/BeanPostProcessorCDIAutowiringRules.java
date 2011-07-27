package com.schlimm.springcdi.decorator.resolver.rules;

import java.util.List;

import javax.decorator.Decorator;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;
import com.schlimm.springcdi.decorator.model.DecoratorInfo;
import com.schlimm.springcdi.decorator.model.QualifiedDecoratorChain;

/**
 * Class implements the wiring rules for autowiring CDI decorators when {@link DecoratorAwareBeanFactoryPostProcessorException}
 * mode is set to 'processor'.
 * 
 * @author Niklas Schlimm
 * 
 */
@SuppressWarnings("unused")
public class BeanPostProcessorCDIAutowiringRules implements DecoratorAutowiringRules {

	private List<QualifiedDecoratorChain> decoratorChains;

	private AutowireCandidateResolver resolver;

	private ConfigurableListableBeanFactory beanFactory;

	public BeanPostProcessorCDIAutowiringRules() {
		super();
	}

	public BeanPostProcessorCDIAutowiringRules(List<QualifiedDecoratorChain> decoratorChains, AutowireCandidateResolver resolver, ConfigurableListableBeanFactory beanFactory) {
		super();
		this.decoratorChains = decoratorChains;
		this.resolver = resolver;
		this.beanFactory = beanFactory;
	}

	@Override
	public boolean executeLogic(Object... arguments) {
		Assert.isTrue(arguments.length == 2, "Expect two arguments!");
		Assert.isTrue(arguments[0] instanceof BeanDefinitionHolder);
		Assert.isTrue(arguments[1] instanceof DependencyDescriptor);
		return applyDecoratorAutowiringRules((BeanDefinitionHolder) arguments[0], (DependencyDescriptor) arguments[1]);
	}

	@Override
	public boolean applyDecoratorAutowiringRules(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		if (DecoratorInfo.isDecorator(beanFactory.getType(bdHolder.getBeanName()))) {
			return false;
		}
		return true;
	}
}
