package org.spring.cdi.decorator.resolver.longsinglechain;

import javax.decorator.Decorator;
import javax.decorator.Delegate;

@Decorator
public class LongSingleChain_MyDecorator3 implements LongSingleChain_MyServiceInterface {
	
	@Delegate 
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
