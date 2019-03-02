package com.algaworks.contatos.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.contatos.model.Contato;
import com.algaworks.contatos.repository.Contatos;

@RestController
@RequestMapping("/contatos")
public class ContatosResource {
	
	@Autowired
	private Contatos contatos;

	private EmployeeResourceAssembler assembler;

	ContatosResource(Contatos repository, EmployeeResourceAssembler assembler) {
		this.contatos = repository;
		this.assembler = assembler;
	}
	
	@GetMapping
	public List<Contato> listar() {
		return contatos.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Contato> buscar(@PathVariable Long id) {
		Contato contato = contatos.findOne(id);
		
		if (contato == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(contato);
	}

//	@PostMapping
//	public Contato adicionar(@Valid @RequestBody Contato contato) {
//
//		return contatos.save(contato);
//	}

	@PostMapping
	public ResponseEntity<?> adicionar(@Valid @RequestBody Contato contato) throws URISyntaxException {

		if (contato.getNome() != null && contatos.existsByNome(contato.getNome())){
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		Resource<Contato> resource = assembler.toResource(contatos.save(contato));
		contatos.save(contato);
//		return ResponseEntity.status(HttpStatus.CREATED).build();
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Contato> atualizar(@PathVariable Long id, 
			@Valid @RequestBody Contato contato) {
		Contato existente = contatos.findOne(id);
		
		if (existente == null) {
			return ResponseEntity.notFound().build();
		}
		
		BeanUtils.copyProperties(contato, existente, "id");
		
		existente = contatos.save(existente);
		
		return ResponseEntity.ok(existente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		Contato contato = contatos.findOne(id);
		
		if (contato == null) {
			return ResponseEntity.notFound().build();
		}
		
		contatos.delete(contato);
		
		return ResponseEntity.noContent().build();
	}
}











