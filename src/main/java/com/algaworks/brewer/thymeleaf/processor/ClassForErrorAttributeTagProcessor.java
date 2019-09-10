package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.util.FieldUtils;
import org.thymeleaf.templatemode.TemplateMode;

public class ClassForErrorAttributeTagProcessor extends AbstractAttributeTagProcessor {

	/*
	 * Criação de Constantes
	 */
	private static final String NOME_ATRIBUTO = "classforerror";
	private static final int PRECEDENCIA = 1000;
	
	/*
	 * Criação do Construtor
	 */
	public ClassForErrorAttributeTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, null, false, NOME_ATRIBUTO, true, PRECEDENCIA, true);
	}
	

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) {
		
		boolean temErro = FieldUtils.hasErrors(context, attributeValue);
		
		/*
		 * Verifica as Classes se possuem erro e implementam a Tag de Erro
		 */
		if (temErro) {
			String classesExistentes = tag.getAttributeValue("class");
			// Adiciona as classes o atributo has-error
			structureHandler.setAttribute("class", classesExistentes + " has-error");
		}
	}
}
