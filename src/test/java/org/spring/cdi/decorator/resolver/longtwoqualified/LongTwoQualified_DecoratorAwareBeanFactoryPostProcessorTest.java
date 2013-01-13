package org.spring.cdi.decorator.resolver.longtwoqualified;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.cdi.decorator.DecoratorAwareBeanFactoryPostProcessor;
import org.spring.cdi.decorator.resolver.DecoratorAwareAutowireCandidateResolver;
import org.spring.cdi.decorator.resolver.rules.ResolverCDIAutowiringRules;
import org.spring.cdi.decorator.strategies.impl.SimpleDecoratorOrderingStrategy;
import org.spring.cdi.decorator.strategies.impl.SimpleDecoratorResolutionStrategy;
import org.spring.cdi.decorator.strategies.impl.SimpleDelegateResolutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;




@ContextConfiguration("/test-context-decorator-resolver-long-two-qualified-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class LongTwoQualified_DecoratorAwareBeanFactoryPostProcessorTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorAwareBeanFactoryPostProcessor beanPostProcessor;
	
	@Before
	public void setUp() {
		beanPostProcessor = new DecoratorAwareBeanFactoryPostProcessor(new SimpleDecoratorResolutionStrategy(), new SimpleDelegateResolutionStrategy(), null, new SimpleDecoratorOrderingStrategy());
		beanPostProcessor.setMode("resolver");
	}
	
	@Test
	public void testChaining_MustBeTwoChains() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().size()==2);
	}

	@Test
	public void testChaining_MyDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(1).getDelegateBeanDefinitionHolder().getBeanName().equals("longTwoQualified_MyDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(0).getDelegateBeanDefinitionHolder().getBeanName().equals("longTwoQualified_AnotherDelegate"));
	}
	
	@Test
	public void testChaining_MustBeThreeDecoratorsInBothChains() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DecoratorAwareAutowireCandidateResolver resolver = (DecoratorAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(0).getDecorators().size()==2);
		Assert.isTrue(((ResolverCDIAutowiringRules)resolver.getPlugins().iterator().next()).getDecoratorChains().get(1).getDecorators().size()==2);
	}
	
}
