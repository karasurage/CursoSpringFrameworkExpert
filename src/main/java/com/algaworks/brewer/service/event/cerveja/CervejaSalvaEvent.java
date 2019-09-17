package com.algaworks.brewer.service.event.cerveja;

import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;

public class CervejaSalvaEvent {

	/*
	 * Declaração de variáveis
	 */
	private Cerveja cerveja;

	/*
	 * Declaração de Construtor
	 */
	public CervejaSalvaEvent(Cerveja cerveja) {
		this.cerveja = cerveja;
	}

	/*
	 * Declaração de Getters
	 */
	public Cerveja getCerveja() {
		return cerveja;
	}
	
	/*
	 * Verifica se existe foto
	 */
	public boolean temFoto() {
		return !StringUtils.isEmpty(cerveja.getFoto());
	}
	
}
