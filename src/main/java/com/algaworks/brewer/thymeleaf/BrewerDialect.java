package com.algaworks.brewer.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.algaworks.brewer.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import com.algaworks.brewer.thymeleaf.processor.MessageElementTagProcessor;

public class BrewerDialect extends AbstractProcessorDialect {
	
	/*
	 * Criando o construtor da classe
	 */
	public BrewerDialect() {
		super("AlgaWorks Brewer", "brewer", StandardDialect.PROCESSOR_PRECEDENCE);
	}

	/*
	 * Inserindo dados do Dialect para os Processadores
	 */
	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processadores = new HashSet<>();
		// Adiciona ao BrewerDialect a classe de Erro
		processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		
		// Adiciona ao BrewerDialect a classe de Mensagens
		processadores.add(new MessageElementTagProcessor(dialectPrefix));
		return processadores;
	}
	
}