package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.Validator.CredentialsValidator;
import it.uniroma3.siw.controller.Validator.UtenteValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.BranoService;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class AuthenticationController {

	@Autowired
	private CredentialsService cs;

	@Autowired
	private CredentialsValidator cv;

	@Autowired
	private UtenteValidator uv;

	@Autowired
	private AutoreService chefs;

	@Autowired
	private BranoService bs;

	// vai alla pagina di login
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		return "login.html";
	}

	// vai alla pagina di logout
	@GetMapping("/logout")
	public String logout(Model model) {
		return "index.html";
	}

	// vai alla pagin index (o admin/dashboard) dopo il login
	@GetMapping("/default")
	public String defaultAfterLogin(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = cs.getCredentials(userDetails.getUsername());

		// superfluo perchè non uso una dashboard diversa per admin
		/*if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "home.html";
		}
		*/
		model.addAttribute("Autori", this.chefs.findAllAutore());
		model.addAttribute("BranoList", this.bs.listaBrani());

		return "home.html";
	}

	// vai alla pagin index (o admin/dashboard) dopo il login con OAuth 
	
	@GetMapping("/defaultOauth")
	public String oauthLogin(Model model) {
		OAuth2User userDetails = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return "home.html";
	}

	// vai alla pgina di registrazione di un utente
	@GetMapping("/register")
	public String getCredentials(Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credentials());
		return "register.html";
	}

	// configurazione form della pagina di registrazione di un utente
	@PostMapping("/register")
	public String addCredentials(@Valid @ModelAttribute("utente") Utente utente, BindingResult utenteBindingResult,
			@Valid @ModelAttribute("credenziali") Credentials credenziali, BindingResult credenzialiBindingResult,
			Model model) {

		utente.setCognome(utente.getCognome().toLowerCase());
		utente.setNome(utente.getNome().toLowerCase());

		this.uv.validate(utente, utenteBindingResult);
		this.cv.validate(credenziali, credenzialiBindingResult);

		if (!credenzialiBindingResult.hasErrors() && !utenteBindingResult.hasErrors()) {
			credenziali.setUtente(utente);
			cs.save(credenziali); // this also stores the User, thanks to Cascade.ALL policy
			return "login.html";
		}

		return "register.html";
	}
}