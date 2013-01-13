package org.spring.cdi;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.web.context.WebApplicationContext;

/**
 * A <code>ScopeMetadataResolver</code> implementation which maps the CDI provided
 * scopes to their Spring counterparts.
 * 
 * <p>The Spring Framework supports the JSR-330 to declare components, however it does not
 * support the API, provided by the JSR-299
 * 
 * <p>This class allows Application developers to use the JSR-299 for scoping annotations. The
 * benefit is that a Spring bean can be completely defined by JSR-299 and JSR-330, like:
 * 
 * <p><pre>
 * &#064;javax.inject.Named("mySpringBean")
 * &#064;javax.enterprise.context.RequestScoped
 * public class MySpringBeanController{...}
 * </pre>
 * 
 * <p>To use this class you need to configure it within the Spring Context, like
 * <p><pre>
 * &lt;context:component-scan
 *    scope-resolver="net.wessendorf.spring.scopes.CdiScopeMetadataResolver"
 *    base-package="my.packages..." /&gt;
 * </pre>
 * 
 * @author Matthias Weßendorf
 */
public class CdiScopeMetadataResolver extends AnnotationScopeMetadataResolver
{

  /**
   * Checks if one of the following CDI scope annoations are used and maps
   * them to their matching Spring scopes:
   * 
   * <ul>
   * <li><code>&#064;javax.enterprise.context.RequestScoped</code></li>
   * <li><code>&#064;javax.enterprise.context.SessionScoped</code></li>
   * <li><code>&#064;javax.enterprise.context.ApplicationScoped</code></li>
   * </ul>
   * 
   * If none of them are found it delegates back to the original Spring
   * <code>AnnotationScopeMetadataResolver</code> class. 
   */
  @Override
  public ScopeMetadata resolveScopeMetadata(BeanDefinition definition)
  {
    ScopeMetadata metadata = new ScopeMetadata();
    if (definition instanceof AnnotatedBeanDefinition)
    {
      AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) definition;
      Set<String> annotationTypes = annDef.getMetadata().getAnnotationTypes();

      if (annotationTypes.contains(RequestScoped.class
          .getName()))
      {
        metadata.setScopeName(WebApplicationContext.SCOPE_REQUEST);
      }
      else if (annotationTypes
          .contains(SessionScoped.class.getName()))
      {
        metadata.setScopeName(WebApplicationContext.SCOPE_SESSION);
      }
      else if (annotationTypes
          .contains(ApplicationScoped.class.getName()))
      {
        metadata.setScopeName(WebApplicationContext.SCOPE_APPLICATION);
      }
      else
      {
        // do the regular Spring stuff..
        return super.resolveScopeMetadata(definition);
      }
    }
    return metadata;
  }
}
