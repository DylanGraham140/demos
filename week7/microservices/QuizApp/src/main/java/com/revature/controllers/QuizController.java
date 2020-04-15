package com.revature.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.clients.FlashcardClient;
import com.revature.models.Flashcard;
import com.revature.models.Quiz;
import com.revature.repositories.QuizRepository;

@RestController
public class QuizController {

	@Autowired
	private QuizRepository qd;
	
	@Autowired
	private FlashcardClient fclient;
	
//	@LoadBalanced
//	@Bean
//	RestTemplate restTemplate() {
//		return new RestTemplate();
//	}
//	@Autowired
//	RestTemplate rt;
// The above RestTemplate process is old, and we prefer to use FeignClients	
	
	@GetMapping("/ribbon/client")
	public String retrieve() {
//		String info = this.rt.getForObject("http://flashcard/server", String.class);
		String info = fclient.retrievePort();
		
		return info;
	}
	
	
	@GetMapping
	public ResponseEntity<List<Quiz>> findAll() {
		List<Quiz> all = qd.findAll();
		
		if(all.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(all);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Quiz> findById(@PathVariable("id") int id) {
		Optional<Quiz> optional = qd.findById(id);
		
		if(optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Quiz> insert(@RequestBody Quiz f) {
		int oldId = f.getId();
		int newId = qd.save(f).getId();
		
		if(newId == oldId) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.status(201).body(f);
	}
	
	@GetMapping("/cards")
	public ResponseEntity<List<Flashcard>> getCards() {
		List<Flashcard> all = fclient.findAll();
		
		if(all.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(all);
	}
}
