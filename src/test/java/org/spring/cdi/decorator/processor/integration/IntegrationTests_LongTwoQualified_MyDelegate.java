package org.spring.cdi.decorator.processor.integration;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.cdi.decorator.resolver.longtwoqualified.LongTwoQualified_IntegrationTests_MyDelegate;
import org.spring.cdi.decorator.resolver.longtwoqualified.LongTwoQualified_MyDelegate;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(inheritLocations=false, locations="/test-context-decorator-processor-long-two-qualified-chains-integration.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class IntegrationTests_LongTwoQualified_MyDelegate extends LongTwoQualified_IntegrationTests_MyDelegate {

	@Test
	public void testInjectedObject() {
		Assert.assertTrue(LongTwoQualified_MyDelegate.class.isAssignableFrom(AopUtils.getTargetClass(decoratedInterface)));
	}
	
}
