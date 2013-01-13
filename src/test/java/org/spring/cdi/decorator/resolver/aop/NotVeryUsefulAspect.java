package org.spring.cdi.decorator.resolver.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class NotVeryUsefulAspect {

	@Pointcut("execution(* org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyServiceInterface.*(..))")
	public void myDummyPointCut() {}
	
	@Before("myDummyPointCut()")
	public void sayHello() {
//		System.out.println("Aspect Hello");
	}
}
