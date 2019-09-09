package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Origem;
import com.algaworks.brewer.model.Sabor;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.service.CadastroCervejaService;

@Controller
public class CervejasController {

	@Autowired
	private Estilos estilos;
	
	@Autowired
	private CadastroCervejaService cadastroCervejaService;
	
	@RequestMapping("/cervejas/novo")
	public ModelAndView novo(Cerveja cerveja) {
		ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja");
		mv.addObject("sabores", Sabor.values()); // Inseri uma lista do ENUM para a View
		mv.addObject("estilos", estilos.findAll()); // Inseri uma lista do Banco de Dados para a View
		mv.addObject("origens", Origem.values()); // Inseri um array para a View
		
		return mv;
	}
	
	/*
	 * @Valid --> Faz a validação do Objeto Cerveja
	 * BindingResult --> Caso haja erro, ele vai validar no IF-ELSE
	 * Model --> Caso haja erro, o mesmo exibe a mensagem de erro definido no Controller para a View
	 * Redirect no return --> Serve para retornar ao formulário através da URL (RequestMapping) e não do do diretório dos arquivos
	 * RedirectAttributes --> Serve para que a mensagem não suma ao redirecionar para a página especificada (addFlashAttribute)
	 */
	@RequestMapping(value = "/cervejas/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes) { 
		if (result.hasErrors()) {
			return novo(cerveja);
		}
		
		cadastroCervejaService.salvar(cerveja); // Chama a regra de negócio para salvar o objeto cerveja no banco de dados
		attributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso!");
		return new ModelAndView("redirect:/cervejas/novo");
	}
	
}
