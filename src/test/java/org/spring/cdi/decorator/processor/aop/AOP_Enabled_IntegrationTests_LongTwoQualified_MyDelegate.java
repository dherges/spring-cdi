package org.spring.cdi.decorator.processor.aop;

import java.lang.reflect.Proxy;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.cdi.decorator.processor.DelegateProxyInspector;
import org.spring.cdi.decorator.processor.integration.IntegrationTests_LongTwoQualified_MyDelegate;
import org.spring.cdi.decorator.resolver.aop.NotVeryUsefulAspect;
import org.spring.cdi.decorator.resolver.longtwoqualified.LongTwoQualified_MyDecorator;
import org.spring.cdi.decorator.resolver.longtwoqualified.LongTwoQualified_MyDelegate;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Test Spring AOP JDK Dynamic Proxies compatibility of Spring-CDI decorator module.
 * 
 * Two decorator chains, two decorators with @Qualifier annotations each
 * 
 * @author Niklas Schlimm
 *
 */
@ContextConfiguration(inheritLocations=false, locations={"/test-context-decorator-processor-aop.xml", "/test-context-decorator-processor-long-two-qualified-chains-integration.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOP_Enabled_IntegrationTests_LongTwoQualified_MyDelegate extends IntegrationTests_LongTwoQualified_MyDelegate {

	@Test
	public void testInjectedObject() {
		Assert.assertTrue(LongTwoQualified_MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}
	/**
	 * {@link LongTwoQualified_MyDecorator} must be AOP (JDK) proxied with {@link NotVeryUsefulAspect}
	 */
	@Test
	public void testProxyType() {
		DelegateProxyInspector inspector = (DelegateProxyInspector)decoratedInterface;
		Object decorator1 = inspector.getInterceptorTarget();
		Object decorator2 = decoratedInterface.getDelegateObject();
		if (Proxy.isProxyClass(decorator1.getClass())&&LongTwoQualified_MyDecorator.class.isAssignableFrom(AopUtils.getTargetClass(decorator1))) {
			Assert.assertTrue(AOP_Enabled_IntegrationTest_SingleChain.checkJDKProxy(decorator1)); return;
		}
		if (Proxy.isProxyClass(decorator2.getClass())&&LongTwoQualified_MyDecorator.class.isAssignableFrom(AopUtils.getTargetClass(decorator2))) {
			Assert.assertTrue(AOP_Enabled_IntegrationTest_SingleChain.checkJDKProxy(decorator2)); return;
		}
		TestCase.fail();
	}

}
