package org.spring.cdi.decorator.resolver.longtwoqualified;

import org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyServiceInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("session")
@Qualifier("my")
public class LongTwoQualified_MyDelegate implements LongSingleChain_MyServiceInterface {

	@Override
	public LongSingleChain_MyServiceInterface getDelegateObject() {
		return null;
	}

	@Override
	public String sayHello() {
		return "Hello";
	}

}
