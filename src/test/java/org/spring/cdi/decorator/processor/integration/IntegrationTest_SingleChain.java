package org.spring.cdi.decorator.processor.integration;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_IntegrationTests;
import org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyDelegate;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;




@ContextConfiguration(inheritLocations=false, locations="/test-context-decorator-processor-long-single-chain.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class IntegrationTest_SingleChain extends LongSingleChain_IntegrationTests {
	
	@Test
	public void testInjectedObject() {
		Assert.assertTrue(LongSingleChain_MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}

	
}