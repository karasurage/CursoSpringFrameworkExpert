package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Cerveja;

@Controller
public class CervejasController {

//	@Autowired
//	private Cervejas cervejas;
	
	@RequestMapping("/cervejas/novo")
	public String novo(Cerveja cerveja) {
		return "cerveja/CadastroCerveja";
	}
	
	/*
	 * @Valid --> Faz a validação do Objeto Cerveja
	 * BindingResult --> Caso haja erro, ele vai validar no IF-ELSE
	 * Model --> Caso haja erro, o mesmo exibe a mensagem de erro definido no Controller para a View
	 * Redirect no return --> Serve para retornar ao formulário através da URL (RequestMapping) e não do do diretório dos arquivos
	 * RedirectAttributes --> Serve para que a mensagem não suma ao redirecionar para a página especificada (addFlashAttribute)
	 */
	@RequestMapping(value = "/cervejas/novo", method = RequestMethod.POST)
	public String cadastrar(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes) { 
		if (result.hasErrors()) {
//			cervejas.findAll(); // Apagar...
			
			return novo(cerveja);
		}
		
		attributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso!");
		return "redirect:/cervejas/novo";
	}
	
}
