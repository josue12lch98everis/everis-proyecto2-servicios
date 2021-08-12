package com.everis.banca.app.savingAccount.services.interfaces;


import com.everis.banca.app.savingAccount.models.documents.SavingAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SavingAccountService {

	Flux<SavingAccount> getAllSavingAccount();
	
	Mono<SavingAccount> save(SavingAccount creditCard);
	

	Mono<SavingAccount> findById(String idSavingAccount);

	Mono<Void> deleteById(String idSavingAccount);
}
