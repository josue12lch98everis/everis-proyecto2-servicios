package java.com.everis.springboot.transaction.service;

import java.com.everis.springboot.transaction.documents.CreateAccountDocument;
import java.com.everis.springboot.transaction.documents.TransactionDocument;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;



import reactor.core.publisher.Mono;

public interface TransactionService {
	
	
	
	Mono<ResponseEntity<Map<String,Object>>> depositar(TransactionDocument transactionDocument);
	
	Mono<ResponseEntity<Map<String,Object>>> retirar(String idCuenta,Double cantidad);
	
	Mono<ResponseEntity<Map<String,Object>>> consultarSaldo(String idCliente);
	
	public Mono< Object> getAccountDetails(String idCuenta) ;
	
}
