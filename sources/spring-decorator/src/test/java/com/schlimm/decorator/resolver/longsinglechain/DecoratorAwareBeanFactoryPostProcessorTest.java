package com.schlimm.decorator.resolver.longsinglechain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.springcdi.decorator.DecoratorAwareBeanFactoryPostProcessor;
import com.schlimm.springcdi.decorator.resolver.DecoratorAwareAutowireCandidateResolver;
import com.schlimm.springcdi.decorator.resolver.rules.ResolverCDIAutowiringRules;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorOrderingStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDecoratorResolutionStrategy;
import com.schlimm.springcdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;



@ContextConfiguration("/test-context-decorator-resolver-long-single-chain.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class DecoratorAwareBeanFactoryPostProcessorTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorAwareBeanFactoryPostProcessor beanPostProcessor;
	
	@Before
	public void setUp() {
		beanPostProcessor = new DecoratorAwareBeanFactoryPostProcessor(new SimpleDecoratorResolutionStrategy(), new SimpleDelegateResolutionStrategy(), null, new SimpleDecoratorOrderingStrategy());
		beanPostProcessor.setMode("resolver");
	}
	
	@Test
	public void testChaining_MustBeOneChain() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().size()==1);
	}

	@Test
	public void testChaining_MyDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(0).getDelegateBeanDefinitionHolder().getBeanName().equals("myDelegate"));
	}
	
	@Test
	public void testChaining_MustBeThreeDecorators() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(0).getDecorators().size()==3);
	}
	
}
