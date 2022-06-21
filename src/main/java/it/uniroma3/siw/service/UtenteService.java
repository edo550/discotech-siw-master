package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;

@Service
public class UtenteService {
	
	@Autowired
	private UtenteRepository utrepo;
	
    @Transactional
    public Utente save(Utente utente) {
        return this.utrepo.save(utente);
    }
    
    public Utente getUtenti(Long id) {
        Optional<Utente> result = this.utrepo.findById(id);
        return result.orElse(null);
    }
	
    public List<Utente> getAllUtenti() {
        List<Utente> result = new ArrayList<>();
        Iterable<Utente> iterable = this.utrepo.findAll();
        for(Utente user : iterable)
            result.add(user);
        return result;
    }
    
	public boolean alreadyExists(Utente u) {
		return utrepo.existsByNomeAndCognome(u.getNome(), u.getCognome());
	}
}
