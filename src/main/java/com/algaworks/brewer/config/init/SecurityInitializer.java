package com.algaworks.brewer.config.init;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.algaworks.brewer.BrewerApplication;

@ComponentScan(basePackageClasses = { BrewerApplication.class })
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	
}
