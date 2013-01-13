package org.spring.cdi.decorator.processor.aop;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.cdi.decorator.processor.DelegateProxyInspector;
import org.spring.cdi.decorator.processor.integration.IntegrationTests_LongTwoChains_MyDelegate;
import org.spring.cdi.decorator.resolver.aop.NotVeryUsefulAspect;
import org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyDecorator;
import org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyDelegate;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Test Spring AOP CGLIB Proxies compatibility of Spring-CDI decorator module.
 * 
 * Two decorator chains, three decorators each
 * 
 * @author Niklas Schlimm
 *
 */
@ContextConfiguration(inheritLocations=false, locations={"/test-context-decorator-processor-aop-cg.xml", "/test-context-decorator-processor-long-two-chains-integration.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOP_CGLIB_Enabled_IntegrationTests_LongTwoChains_MyDelegate extends IntegrationTests_LongTwoChains_MyDelegate {

	/**
	 * Injected object must be of type {@link LongSingleChain_MyDecorator}
	 */
	@Test
	public void testInjectedObject() {
		Assert.assertTrue(LongSingleChain_MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}

	/**
	 * {@link LongSingleChain_MyDecorator} must be AOP (CGI) proxied with {@link NotVeryUsefulAspect}
	 */
	@Test
	public void testProxyType() {
		DelegateProxyInspector inspector = (DelegateProxyInspector)decoratedInterface;
		Object decorator1 = inspector.getInterceptorTarget();
		Object decorator2 = decoratedInterface.getDelegateObject();
		Object decorator3 = decoratedInterface.getDelegateObject().getDelegateObject();
		if (LongSingleChain_MyDecorator.class.isAssignableFrom(decorator1.getClass())) {
			Assert.assertTrue(AOP_CGLIB_Enabled_IntegrationTest_SingleChain.checkCGLIBProxy(decorator1)); return;
		}
		if (LongSingleChain_MyDecorator.class.isAssignableFrom(decorator2.getClass())) {
			Assert.assertTrue(AOP_CGLIB_Enabled_IntegrationTest_SingleChain.checkCGLIBProxy(decorator2)); return;
		}
		if (LongSingleChain_MyDecorator.class.isAssignableFrom(decorator3.getClass())) {
			Assert.assertTrue(AOP_CGLIB_Enabled_IntegrationTest_SingleChain.checkCGLIBProxy(decorator3)); return;
		}
		TestCase.fail();
	}

}
