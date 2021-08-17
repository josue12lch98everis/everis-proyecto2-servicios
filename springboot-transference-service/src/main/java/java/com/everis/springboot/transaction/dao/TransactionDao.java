package java.com.everis.springboot.transaction.dao;

import java.com.everis.springboot.transaction.documents.TransactionDocument;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import reactor.core.publisher.Mono;

public interface TransactionDao extends ReactiveMongoRepository<TransactionDocument, String>{
	
	Mono<TransactionDocument> findByIdCliente(String client);

}
