package org.spring.cdi.decorator.resolver.longsinglechain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.cdi.decorator.strategies.DecoratorResolutionStrategy;
import org.spring.cdi.decorator.strategies.impl.SimpleDecoratorResolutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;



@ContextConfiguration("/test-context-decorator-resolver-long-single-chain.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class LongSingleChain_SimpleDecoratorResolutionStrategyTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorResolutionStrategy decoratorResolutionStrategy;
	
	@Before
	public void setUp() {
		decoratorResolutionStrategy = new SimpleDecoratorResolutionStrategy();
	}
	
	@Test
	public void testChaining_MustBeThreeDecorators() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).size()==3);
	}

	@Test
	public void testChaining_ContainsMyDecorator() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longSingleChain_MyDecorator"));
	}
	
	@Test
	public void testChaining_ContainsMyDecorator2() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longSingleChain_MyDecorator2"));
	}
	
	@Test
	public void testChaining_ContainsMyDecorator3() {
		Assert.isTrue(decoratorResolutionStrategy.getRegisteredDecorators(beanFactory).containsKey("longSingleChain_MyDecorator3"));
	}
	
}
