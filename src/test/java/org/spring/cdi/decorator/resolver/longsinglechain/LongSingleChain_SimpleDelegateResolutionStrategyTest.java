package org.spring.cdi.decorator.resolver.longsinglechain;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.cdi.decorator.model.DecoratorInfo;
import org.spring.cdi.decorator.strategies.DelegateResolutionStrategy;
import org.spring.cdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;



@ContextConfiguration("/test-context-decorator-resolver-long-single-chain.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class LongSingleChain_SimpleDelegateResolutionStrategyTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DelegateResolutionStrategy delegateResolutionStrategy;
	
	@Before
	public void setUp() {
		delegateResolutionStrategy = new SimpleDelegateResolutionStrategy();
	}
	
	@Test
	public void testChaining_MyDecoratorLeadsToMyDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("longSingleChain_MyDecorator", beanFactory.getBeanDefinition("longSingleChain_MyDecorator"), Class.forName("org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyDecorator"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("longSingleChain_MyDelegate"));
	}

	@Test
	public void testChaining_MyDecorator2LeadsToMyDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("longSingleChain_MyDecorator2", beanFactory.getBeanDefinition("longSingleChain_MyDecorator2"), Class.forName("org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyDecorator2"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("longSingleChain_MyDelegate"));
	}
	
	@Test
	public void testChaining_MyDecorator3LeadsToMyDelegate() {
		DecoratorInfo decoratorInfo = null;
		try {
			decoratorInfo = new DecoratorInfo("myDecorator3", beanFactory.getBeanDefinition("longSingleChain_MyDecorator3"), Class.forName("org.spring.cdi.decorator.resolver.longsinglechain.LongSingleChain_MyDecorator3"));
		} catch (NoSuchBeanDefinitionException e) {
			TestCase.fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
		Assert.isTrue(delegateResolutionStrategy.getRegisteredDelegate(beanFactory, decoratorInfo).equals("longSingleChain_MyDelegate"));
	}
	

}
