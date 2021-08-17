package java.com.everis.springboot.transaction.controller;

import java.com.everis.springboot.transaction.documents.TransactionDocument;
import java.com.everis.springboot.transaction.service.TransactionService;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import reactor.core.publisher.Mono;

@RestController
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	

	
	@PostMapping("/transfer")
	public Mono<ResponseEntity<Map<String,Object>>> deposit(@RequestBody TransactionDocument transaction){
		System.out.println("Entro al metodo guardar cuenta");
		return transactionService.depositar(transaction);
	}
	
	@PostMapping("/retirement/{id}/{amount}/{idAccountReceptor}")
	public Mono<ResponseEntity<Map<String,Object>>> retirement(@PathVariable String id, @PathVariable Double amount){
		System.out.println("Entro al metodo guardar cuenta");
		return transactionService.retirar(id, amount);
	} 
	
	@GetMapping("/getBalance/{id}")
	public Mono<ResponseEntity<Map<String,Object>>> getBalance(@PathVariable("id") String id) {
		return transactionService.consultarSaldo(id);
	}

}
