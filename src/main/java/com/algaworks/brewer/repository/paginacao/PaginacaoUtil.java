//package com.algaworks.brewer.repository.paginacao;

// Esse método só funciona dentro da classe principal

//@Component
//public class PaginacaoUtil {
//
//	public void preparar(Criteria criteria, Pageable pageable) {
//		
//		int paginaAtual = pageable.getPageNumber();
//		int totalRegistrosPorPagina = pageable.getPageSize();
//		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
//		
//		criteria.setFirstResult(primeiroRegistro);
//		criteria.setMaxResults(totalRegistrosPorPagina);
//		
//		Sort sort = pageable.getSort();
//		if (sort != null) {
//			Sort.Order order = sort.iterator().next();
//			String property = order.getProperty();
//			criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property)); // Erro encontrado no criteria
//			// URL que funciona: http://localhost:8080/cervejas?sort=sku,asc
//		}
//	}
//}
