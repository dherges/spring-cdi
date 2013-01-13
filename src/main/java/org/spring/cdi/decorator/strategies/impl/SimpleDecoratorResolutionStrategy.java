package org.spring.cdi.decorator.strategies.impl;

import java.util.HashMap;
import java.util.Map;

import org.spring.cdi.decorator.DecoratorAwareBeanFactoryPostProcessorException;
import org.spring.cdi.decorator.model.DecoratorInfo;
import org.spring.cdi.decorator.strategies.DecoratorResolutionStrategy;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ClassUtils;


/**
 * Simple strategy that searches bean definitions for decorators.
 * 
 * @author Niklas Schlimm
 *
 */
@SuppressWarnings("rawtypes")
public class SimpleDecoratorResolutionStrategy implements DecoratorResolutionStrategy {

	private static final String SCOPED_TARGET = "scopedTarget.";
	
	private HashMap<String, Class> registeredDecoratorsCache;

	public Map<String, Class> getRegisteredDecorators(ConfigurableListableBeanFactory beanFactory) {
		Map<String, Class> definitions = new HashMap<String, Class>();
		if (registeredDecoratorsCache == null) {
			registeredDecoratorsCache = new HashMap<String, Class>();
			String[] bdNames = beanFactory.getBeanDefinitionNames();
			for (String bdName : bdNames) {
				BeanDefinition bd = beanFactory.getBeanDefinition(bdName);
				if (bd instanceof AnnotatedBeanDefinition) {
					AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) bd;
					if (DecoratorInfo.isDecorator(abd)) {
						Class decoratorClass = null;
						try {
							decoratorClass = ClassUtils.forName(abd.getBeanClassName(), this.getClass().getClassLoader());
						} catch (Exception e) {
							throw new DecoratorAwareBeanFactoryPostProcessorException("Could not find decorator class: " + abd.getBeanClassName(), e);
						} 
						if (bdName.startsWith(SCOPED_TARGET)) {
							bd = beanFactory.getBeanDefinition(bdName.replace(SCOPED_TARGET, ""));
						}
						if (bd.isAutowireCandidate()) {
							definitions.put(bdName.replace(SCOPED_TARGET, ""), decoratorClass);
						} 
					}
				}
			}
		} else {
			definitions = registeredDecoratorsCache;
		}

		return definitions;
	}

}
