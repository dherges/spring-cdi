package org.spring.cdi.decorator.resolver.aop;

import org.junit.runner.RunWith;
import org.spring.cdi.decorator.resolver.longtwoqualified.LongTwoQualified_DecoratorAwareBeanFactoryPostProcessorTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(inheritLocations=true, locations="/test-context-decorator-resolver-aop.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class AOPEnabled_DecoratorAwareBeanFactoryPostProcessorTest extends LongTwoQualified_DecoratorAwareBeanFactoryPostProcessorTest{

}
