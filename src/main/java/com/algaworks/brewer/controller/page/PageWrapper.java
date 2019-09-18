package com.algaworks.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {

	/* CONTROLE DE PAGINAÇÃO E DOS DADOS DAS PÁGINAS */

	/*
	 * Declaração de Variáveis
	 */
	private Page<T> page;
	private UriComponentsBuilder uriBuilder;

	/*
	 * Declaração de Construtor
	 */
	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		this.uriBuilder = ServletUriComponentsBuilder.fromRequest(httpServletRequest);
	}

	/*
	 * Retorna o conteúdo da página sem perder informações ao passar de uma página
	 * para outra
	 */
	public List<T> getConteudo() {
		return page.getContent();
	}

	/*
	 * Verifica de o conteúdo e a página estão vazios
	 */
	public boolean isVazia() {
		return page.getContent().isEmpty();
	}

	/*
	 * Verifica a Página atual e retorna o valor da mesma
	 */
	public int getAtual() {
		return page.getNumber();
	}
	
	/*
	 * Verifica a Primeira Página e retorna o valor da mesma
	 */
	public boolean isPrimeira() {
		return page.isFirst();
	}
	
	/*
	 * Verifica a Última Página e retorna o valor da mesma
	 */
	public boolean isUltima() {
		return page.isLast();
	}
	
	/*
	 * Verifica o Total de Páginas e retorna o valor da mesma
	 */
	public int getTotal() {
		return page.getTotalPages();
	}
	
	/*
	 * Pega a página da URL
	 */
	public String urlParaPagina(int pagina) {
		return uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
	}
	
	/*
	 * Cria o método para ordenar pelo atributo
	 */
	public String urlOrdenada(String propriedade) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
				.fromUriString(uriBuilder.build(true).encode().toUriString());
		
		String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade));
		
		return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
	}
	
	/*
	 * Inverte a direção do Filtro
	 */
	public String inverterDirecao(String propriedade) {
		String direcao = "asc";
		
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}
		
		return direcao;
	}
	
	/*
	 * Inverter de Descendente para Ascendente
	 */
	public boolean descendente(String propriedade) {
		return inverterDirecao(propriedade).equals("asc");
	}
	
	/*
	 * Saber se existe a ordenação na tabela
	 */
	public boolean ordenada(String propriedade) {
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null; 
		
		if (order == null) {
			return false;
		}
		
		return page.getSort().getOrderFor(propriedade) != null ? true : false;
	}
	

}
