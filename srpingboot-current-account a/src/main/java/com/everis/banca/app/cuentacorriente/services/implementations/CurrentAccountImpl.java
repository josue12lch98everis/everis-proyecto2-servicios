package com.everis.banca.app.cuentacorriente.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everis.banca.app.cuentacorriente.dao.CurrentAccountDao;
import com.everis.banca.app.cuentacorriente.models.documents.CurrentAccount;
import com.everis.banca.app.cuentacorriente.services.interfaces.ICurrentAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrentAccountImpl implements ICurrentAccountService {

	@Autowired
	private CurrentAccountDao  currentAccountDao;
	
	@Override
	public Flux<CurrentAccount> getAllCurrentAccount() {
		// TODO Auto-generated method stub
		Flux<CurrentAccount> lcurrentAccount = currentAccountDao.findAll();
		return lcurrentAccount;
	}

	@Override
	public Mono<CurrentAccount> createCurrentAccount(CurrentAccount currentAccount) {
		if(currentAccount.getId() != null) {
			return Mono.error(new IllegalArgumentException("Id of New CurrentAccount be null"));
		}
		return currentAccountDao.save(currentAccount);
	}

	@Override
	public Flux<CurrentAccount> findByClientId(String clientId) {
		Flux<CurrentAccount> lcurrentAccount = currentAccountDao.findByClientId(clientId) ;
		return lcurrentAccount;
	}

	@Override
	public Mono<CurrentAccount> findById(String idCurrentAccount) {
		// TODO Auto-generated method stub
		
		return currentAccountDao.findById(idCurrentAccount);
	}

	@Override
	public Mono<CurrentAccount> save(CurrentAccount currentAccount) {
		// TODO Auto-generated method stub
		return currentAccountDao.save(currentAccount);
	}

	@Override
	public Mono<Void> deleteById(String idCurrentAccount) {
		return currentAccountDao.deleteById(idCurrentAccount);
	}

	
}
