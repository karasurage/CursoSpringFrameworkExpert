package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class OrderElementTagProcessor extends AbstractElementTagProcessor {

	/*
	 * Criação de Constantes
	 */
	private static final String NOME_TAG = "order";
	private static final int PRECEDENCIA = 1000;;
	
	/*
	 * Criação do Construtor
	 */
	public OrderElementTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCIA);
	}
	
	
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		IModelFactory modelFactory = context.getModelFactory();
		
		IAttribute page = tag.getAttribute("page");
		IAttribute field = tag.getAttribute("field");
		IAttribute text = tag.getAttribute("text");
		
		/*
		 * Cria as informações para a tag brewer:message
		 */
		IModel model = modelFactory.createModel();
		model.add(modelFactory.createStandaloneElementTag("th:block" 
				, "th:replace"
				, String.format("fragments/Ordenacao :: order (%s, %s, %s)", page.getValue(), field.getValue(), text.getValue())));
		
		/*
		 * Inseri as informações para a tag brewer:message que serão processadas pelo thymeleaf
		 */
		structureHandler.replaceWith(model, true);
	}

}
