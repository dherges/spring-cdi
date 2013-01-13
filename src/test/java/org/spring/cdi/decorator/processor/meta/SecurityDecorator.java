package org.spring.cdi.decorator.processor.meta;

import java.lang.annotation.Retention;

import org.spring.cdi.decorator.meta.DecoratorAnnotation;


@DecoratorAnnotation
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SecurityDecorator { }
