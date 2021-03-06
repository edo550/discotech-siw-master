package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Autore;

public interface AutoreRepository extends CrudRepository<Autore, Long> {
	
	public Autore findByCognome(String cognome);
	public Autore findByNomeAndCognome(String nome, String cognome);
	public List<Autore> findByNomeOrCognome(String nome, String cognome);
	public List<Autore> findByNazionalita(String nazionalit√†);
	public List<Autore> findAllByOrderByCognomeAsc();
	public List<Autore> findAllByOrderByCognomeDesc();
	public List<Autore> findAllByOrderByNazionalitaAsc();
	public List<Autore> findAllByOrderByNazionalitaDesc();
}