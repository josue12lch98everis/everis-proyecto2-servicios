package com.everis.banca.app.savingAccount.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.everis.banca.app.savingAccount.dao.SavingAccountDao;
import com.everis.banca.app.savingAccount.models.documents.SavingAccount;
import com.everis.banca.app.savingAccount.services.interfaces.SavingAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SavingAccountImpl implements SavingAccountService {

	@Autowired
	private SavingAccountDao savingAccountDao;
	
	@Override
	public Flux<SavingAccount> getAllSavingAccount() {
		// TODO Auto-generated method stub
		Flux<SavingAccount> lsavingAccount = savingAccountDao.findAll();
		return lsavingAccount;
	}

	@Override
	public Mono<SavingAccount> save(SavingAccount savingAccount) {
		if(savingAccount.getId() != null) {
			return Mono.error(new IllegalArgumentException("Id of New SavingAccount be null"));
		}
		return savingAccountDao.save(savingAccount);
	}

	@Override
	public Mono<SavingAccount> findById(String idSavingAccount) {
		// TODO Auto-generated method stub
		return savingAccountDao.findById(idSavingAccount);
	}

	@Override
	public Mono<Void> deleteById(String idSavingAccount) {
		// TODO Auto-generated method stub
		return savingAccountDao.deleteById(idSavingAccount);
	}

}
