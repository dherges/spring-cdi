package org.spring.cdi.decorator.processor.aop;

import java.lang.reflect.Proxy;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.cdi.decorator.processor.DelegateProxyInspector;
import org.spring.cdi.decorator.processor.integration.IntegrationTest_SingleChain;
import org.spring.cdi.decorator.resolver.aop.NotVeryUsefulAspect;
import org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyDecorator;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;




/**
 * Test Spring AOP JDK Dynamic Proxies compatibility of Spring-CDI decorator module.
 * 
 * Single decorator chain, three decorators
 * 
 * @author Niklas Schlimm
 *
 */
@ContextConfiguration(inheritLocations=false, locations={"/test-context-decorator-processor-aop.xml", "/test-context-decorator-processor-long-single-chain.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOP_Enabled_IntegrationTest_SingleChain extends IntegrationTest_SingleChain { 
	
	/**
	 * {@link LongSingleChain_MyDecorator} must be AOP (JDK) proxied with {@link NotVeryUsefulAspect}
	 */
	@Test
	public void testProxyType() {
		DelegateProxyInspector inspector = (DelegateProxyInspector)decoratedInterface;
		Object decorator1 = inspector.getInterceptorTarget();
		Object decorator2 = decoratedInterface.getDelegateObject();
		Object decorator3 = decoratedInterface.getDelegateObject().getDelegateObject();
		if (Proxy.isProxyClass(decorator1.getClass())&&LongSingleChain_MyDecorator.class.isAssignableFrom(AopUtils.getTargetClass(decorator1))) {
			Assert.assertTrue(checkJDKProxy(decorator1)); return;
		}
		if (Proxy.isProxyClass(decorator2.getClass())&&LongSingleChain_MyDecorator.class.isAssignableFrom(AopUtils.getTargetClass(decorator2))) {
			Assert.assertTrue(checkJDKProxy(decorator2)); return;
		}
		if (Proxy.isProxyClass(decorator3.getClass())&&LongSingleChain_MyDecorator.class.isAssignableFrom(AopUtils.getTargetClass(decorator3))) {
			Assert.assertTrue(checkJDKProxy(decorator3)); return;
		}
		TestCase.fail();
	}
	
	public static boolean checkJDKProxy(Object decorator) {
		Advised advised = (Advised) decorator;
		AspectJMethodBeforeAdvice beforeAdvice = (AspectJMethodBeforeAdvice) advised.getAdvisors()[1].getAdvice();
		return AopUtils.isJdkDynamicProxy(decorator) && advised.getAdvisors()[1].getAdvice().getClass().equals(AspectJMethodBeforeAdvice.class)
		&& beforeAdvice.getAspectName().contains("NotVeryUsefulAspect");
	}
	
}