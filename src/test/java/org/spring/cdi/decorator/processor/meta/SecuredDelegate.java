package org.spring.cdi.decorator.processor.meta;

import java.lang.annotation.Retention;

import org.spring.cdi.decorator.meta.DelegateAnnotation;


@DelegateAnnotation
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SecuredDelegate { }
