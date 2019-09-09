package com.algaworks.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.Cervejas;

@Service
public class CadastroCervejaService {

	/*
	 * Injeta a dependência de Cervejas para poder salvar a mesma
	 */
	@Autowired
	private Cervejas cervejas;

	/*
	 * Serve para salvar o objeto cerveja no banco de dados
	 */
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejas.save(cerveja);
	}
}
