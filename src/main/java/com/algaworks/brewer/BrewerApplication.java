package com.algaworks.brewer;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.algaworks.brewer.controller.CervejasController;
import com.algaworks.brewer.controller.converter.EstiloConverter;
import com.algaworks.brewer.service.CadastroCervejaService;
import com.algaworks.brewer.storage.FotoStorage;
import com.algaworks.brewer.storage.local.FotoStorageLocal;
import com.algaworks.brewer.thymeleaf.BrewerDialect;

import nz.net.ultraq.thymeleaf.LayoutDialect;

//Anotação de uma Aplicação Spring Boot
@SpringBootApplication
//Anotação de Configuração
@Configuration
//Anotação de Configuração do Controller
@ComponentScan(basePackageClasses = { CervejasController.class, CadastroCervejaService.class })
//Anotação de Configuração para Aplicações Web MVC
@EnableWebMvc
// Anotação para suporte do Data para Web (Paginação e mais)
@EnableSpringDataWebSupport
public class BrewerApplication implements WebMvcConfigurer, ApplicationContextAware {

	public static void main(String[] args) {
		SpringApplication.run(BrewerApplication.class, args);
	}
	
	/* Os dados informados abaixo precisam estar no arquivo de inicialização do Spring Framework */ 
	
	/*
	 * WebConfig.java
	 */
	
	/*
	 * Criação do Atributo ApplicationContext
	 */
	private ApplicationContext applicationContext;

	/*
	 * Método do ApplicationContext
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/*
	 * Resolve o direcionamento do nome da requisição e passa o TemplateEngine --> TemplateResolver	
	 */
	@Bean
	public ViewResolver ViewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}
	
	/*
	 * Serve para executar as páginas do templateResolver
	 */
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		
		engine.addDialect(new LayoutDialect()); // Cria um novo Layout Dialect
		engine.addDialect(new BrewerDialect()); // Cria um novo Dialect Brewer
		return engine;
	}
	
	/*
	 * Resolve o problema do caminho dos templates (páginas HTML)
	 */
	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}

	/*
	 * Serve para procurar os recursos estáticos (CSS, JS, JQuery, etc)
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}
	
	/*
	 * Serve para converter determinado formato de String para outro tipo de Dado
	 * Usado quando se quer o ID do objeto ao invés do nome
	 */
	@Bean
	public FormattingConversionService mvcConversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		conversionService.addConverter(new EstiloConverter());
		
		// Formatando os padrões numéricos BigDecimal e Integer
		NumberStyleFormatter bigDecimalFormatter = new NumberStyleFormatter("#,##0.00");
		conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
		
		NumberStyleFormatter integerFormatter = new NumberStyleFormatter("#,##0");
		conversionService.addFormatterForFieldType(Integer.class, integerFormatter);
		
		return conversionService;
	}
	
	/*
	 * Trata as informações no padrão de linguagem do Brasil
	 * 
	 */
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}
	
	/*
	 * Em produção pode ser modificado apenas o retorno do método da interface
	 * --> "return new FotoStorageLocal()" para qualquer outro método implementado
	 */
	@Bean
	public FotoStorage fotoStorage() {
		return new FotoStorageLocal();
	}

}
