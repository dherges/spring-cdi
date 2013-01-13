package org.spring.cdi.decorator.springsamples;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/test-context-decorator-spring-samples.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Samples_IntegrationTests {

	@Autowired
	@Qualifier("my")
	private Samples_MyServiceInterface decoratedInterface;

	@Test
	public void testHelloWorld() {
		Assert.assertTrue(decoratedInterface.sayHello().equals("Hello"));
	}

}
