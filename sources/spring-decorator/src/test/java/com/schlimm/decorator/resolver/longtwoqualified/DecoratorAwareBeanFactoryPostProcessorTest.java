package com.schlimm.decorator.resolver.longtwoqualified;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.schlimm.decorator.DecoratorAwareBeanFactoryPostProcessor;
import com.schlimm.decorator.SimpleDecoratorResolutionStrategy;
import com.schlimm.decorator.SimpleDelegateResolutionStrategy;
import com.schlimm.decorator.resolver.DelegateAwareAutowireCandidateResolver;
import com.schlimm.decorator.resolver.SimpleCDIAutowiringRules;



@ContextConfiguration("/test-context-decorator-resolver-long-two-qualified-chains.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DecoratorAwareBeanFactoryPostProcessorTest {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private DecoratorAwareBeanFactoryPostProcessor beanPostProcessor;
	
	@Before
	public void setUp() {
		beanPostProcessor = new DecoratorAwareBeanFactoryPostProcessor(new SimpleDecoratorResolutionStrategy(), new SimpleDelegateResolutionStrategy());
	}
	
	@Test
	public void testChaining_MustBeTwoChains() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DelegateAwareAutowireCandidateResolver resolver = (DelegateAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getCdiAutowiringRules()).getDecoratorChains().size()==2);
	}

	@Test
	public void testChaining_MyDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DelegateAwareAutowireCandidateResolver resolver = (DelegateAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getCdiAutowiringRules()).getDecoratorChains().get(0).getDelegateBeanDefinitionHolder().getBeanName().equals("myDelegate"));
	}
	
	@Test
	public void testChaining_AnotherDelegateMustBeDelegate() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DelegateAwareAutowireCandidateResolver resolver = (DelegateAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getCdiAutowiringRules()).getDecoratorChains().get(1).getDelegateBeanDefinitionHolder().getBeanName().equals("anotherDelegate"));
	}
	
	@Test
	public void testChaining_MustBeThreeDecoratorsInBothChains() {
		beanPostProcessor.postProcessBeanFactory(beanFactory);
		DelegateAwareAutowireCandidateResolver resolver = (DelegateAwareAutowireCandidateResolver)((DefaultListableBeanFactory)beanFactory).getAutowireCandidateResolver();
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getCdiAutowiringRules()).getDecoratorChains().get(0).getDecorators().size()==2);
		Assert.isTrue(((SimpleCDIAutowiringRules)resolver.getCdiAutowiringRules()).getDecoratorChains().get(1).getDecorators().size()==2);
	}
	
}