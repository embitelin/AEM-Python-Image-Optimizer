package com.adobe.aem.python.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * PythonConfiguration file which store the consfiguration of instanceUrl,
 * userName and password . These configuration is configurable based on runmodes
 * @author sruthi.adabettu@embitel.com
 *
 */
@ObjectClassDefinition(
        name = "Python Configuration",
        description = "This configuration requires to call AEM"
)
public @interface ImageOptimizerConfiguration {

    @AttributeDefinition(name = "AEM Instance Url", description = "Provide AEM instance author URL")
    String instanceurl() default "http://localhost:4502";
    @AttributeDefinition(name = "User Name", description = "Provide the user name")
    String username() default "";
    @AttributeDefinition(name = "Password", description = "Provide the password")
    String password() default "";
}