package org.spring.cdi.decorator.processor;

import org.spring.cdi.decorator.model.DecoratorMetaDataBean;
import org.spring.cdi.decorator.model.QualifiedDecoratorChain;
import org.spring.cdi.decorator.strategies.DecoratorChainingStrategy;
import org.spring.cdi.decorator.strategies.impl.SimpleDecoratorChainingStrategy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SimpleBeanTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


/**
 * {@link BeanPostProcessor} that applies the JSR-299 decorator pattern to the Spring beans.
 * 
 * If the processed bean is a decorated bean, then this {@link BeanPostProcessor} returns a CGLIB proxy for that bean. Uses a
 * {@link DelegateMethodInterceptor} to delegate calls to that given delegate bean to the decorator chain.
 * 
 * @author Niklas Schlimm
 * 
 */
public class DecoratorAwareBeanPostProcessor implements BeanPostProcessor, InitializingBean {

	/**
	 * The decorator meta data bean that contains all {@link QualifiedDecoratorChain}
	 */
	@Autowired
	private DecoratorMetaDataBean metaData;

	@Autowired
	private final ConfigurableListableBeanFactory beanFactory = null;

	/**
	 * Chaining strategy in use, may be custom strategy
	 */
	private DecoratorChainingStrategy chainingStrategy;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		if (metaData.isDecoratedBean(beanName)) {
			return buildDelegateProxy(bean, beanName);
		} else {
			return bean;
		}

	}

	@SuppressWarnings("serial")
	public Object buildDelegateProxy(final Object bean, final String beanName) {
		final SimpleBeanTargetSource targetSource = new SimpleBeanTargetSource() {{setTargetBeanName(beanName); setTargetClass(bean.getClass()); setBeanFactory(beanFactory);}};
		ProxyFactory pf = new ProxyFactory() {{setTargetSource(targetSource); setProxyTargetClass(true);}};
		DelegateMethodInterceptor interceptor = new DelegateMethodInterceptor(chainingStrategy.getChainedDecorators(beanFactory, metaData.getQualifiedDecoratorChain(beanName), bean));
		pf.addAdvice(interceptor); pf.addInterface(DelegateProxyInspector.class);
		Object proxy = pf.getProxy();
		return proxy;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (chainingStrategy == null) {
			chainingStrategy = new SimpleDecoratorChainingStrategy();
		}
	}

}
