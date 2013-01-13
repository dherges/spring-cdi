package org.spring.cdi.decorator.resolver.longtwoqualified;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

import org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyServiceInterface;
import org.springframework.beans.factory.annotation.Qualifier;


@Decorator
@Qualifier("my")
public class LongTwoQualified_MyDecorator2 implements LongSingleChain_MyServiceInterface {
	
	@Delegate @Qualifier("my")
	private LongSingleChain_MyServiceInterface delegateInterface;

	@Override
	public LongSingleChain_MyServiceInterface getDelegateObject() {
		return delegateInterface;
	}

	@Override
	public String sayHello() {
		return delegateInterface.sayHello();
	}



}
