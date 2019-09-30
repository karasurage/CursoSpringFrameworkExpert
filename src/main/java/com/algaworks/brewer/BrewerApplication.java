package com.algaworks.brewer;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Component;
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
import com.algaworks.brewer.controller.converter.CidadeConverter;
import com.algaworks.brewer.controller.converter.EstadoConverter;
import com.algaworks.brewer.controller.converter.EstiloConverter;
import com.algaworks.brewer.service.CadastroCervejaService;
import com.algaworks.brewer.storage.FotoStorage;
import com.algaworks.brewer.storage.local.FotoStorageLocal;
import com.algaworks.brewer.thymeleaf.BrewerDialect;
import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import com.google.common.cache.CacheBuilder;

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
//  Habilita e Desabilita o Cache
@EnableCaching
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
		engine.addDialect(new BrewerDialect()); // Cria um novo Brewer Dialect 
		engine.addDialect(new DataAttributeDialect()); // Cria um novo Data Attribute Dialect
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
		conversionService.addConverter(new CidadeConverter());
		conversionService.addConverter(new EstadoConverter());
		
		// Formatando os padrões numéricos BigDecimal e Integer
		NumberStyleFormatter bigDecimalFormatter = new NumberStyleFormatter("#,##0.00");
		conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
		
		NumberStyleFormatter integerFormatter = new NumberStyleFormatter("#,##0");
		conversionService.addFormatterForFieldType(Integer.class, integerFormatter);
		
		// API de Datas do Java 8
		DateTimeFormatterRegistrar dateTimeFormatter = new DateTimeFormatterRegistrar();
		dateTimeFormatter.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		dateTimeFormatter.registerFormatters(conversionService);
		
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
	
	/*
	 * Classe criada para PaginacaoUtil para CervejasImpl.java e EstilosImpl.java
	 */
	@Component
	public class PaginacaoUtil {

		public void preparar(Criteria criteria, Pageable pageable) {
			
			int paginaAtual = pageable.getPageNumber();
			int totalRegistrosPorPagina = pageable.getPageSize();
			int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
			
			criteria.setFirstResult(primeiroRegistro);
			criteria.setMaxResults(totalRegistrosPorPagina);
			
			Sort sort = pageable.getSort();
			if (sort != null) {
				
				Sort.Order order = sort.iterator().next();
				String property = order.getProperty();
				criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property)); // Erro encontrado no criteria
				// URL que funciona: http://localhost:8080/cervejas?sort=sku,asc ou qualquer outra página de pesquisa
			}
		}
	}
	
	/*
	 * Implementação do Método para fazer o Cache da Aplicação
	 */
	@Bean
	public CacheManager cacheManager() {
		CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
				.maximumSize(3)
				.expireAfterAccess(20, TimeUnit.SECONDS);
		
		GuavaCacheManager cacheManager = new GuavaCacheManager();
		cacheManager.setCacheBuilder(cacheBuilder);
		return cacheManager;
	}
	
	/*
	 * Serve para setar o encondig das messagens de erro como UTF-8
	 * e seguir um padrão
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
		bundle.setBasename("classpath:/messages");
		bundle.setDefaultEncoding("UTF-8"); // https://www.utf8-chartable.de/
		return bundle;
	}
	
}
