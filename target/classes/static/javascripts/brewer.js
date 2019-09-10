// Criando o objeto brewer
var Brewer = Brewer || {};

Brewer.MaskMoney = (function() {
	
	/*
	 *  Adicionando função construtora e Inicialização
	 */
	function MaskMoney() {
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}
	
	/*
	 * Protótipo das funções e Execucação dos protótipos
	 */
	MaskMoney.prototype.enable = function() {
		this.decimal.maskMoney({ decimal: ',', thousands: '.' });
		this.plain.maskMoney({ precision: 0, thousands: '.' });
	}
	
	// Retorno da função construtora
	return MaskMoney;
	
}());

//Execucação das funções
$(function() {
	var maskMoney = new Brewer.MaskMoney();
	maskMoney.enable();
});