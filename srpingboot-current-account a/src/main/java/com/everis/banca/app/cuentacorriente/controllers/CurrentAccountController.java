package com.everis.banca.app.cuentacorriente.controllers;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everis.banca.app.cuentacorriente.dao.CurrentAccountDao;
import com.everis.banca.app.cuentacorriente.models.documents.CurrentAccount;
import com.everis.banca.app.cuentacorriente.services.interfaces.ICurrentAccountService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping()
@Slf4j
public class CurrentAccountController {



		@Autowired
		private ICurrentAccountService currentAccountService;
		
		@GetMapping()
		public Flux<CurrentAccount> getAllCurrentAccount() {
			
			Flux<CurrentAccount> currentAccounts = currentAccountService.getAllCurrentAccount().map(currentAccount -> {
				return currentAccount;
			});
			return currentAccounts;
		}
		
		@PostMapping()
		public Mono<CurrentAccount> saveCurrentAccount(@RequestBody CurrentAccount currentAccount) {
			try {
				currentAccount.setCreateAt(new Date());
				Mono<CurrentAccount> save = currentAccountService.save(currentAccount);
				log.info("Se ingresó correctamente");
				
				
				
				return save;
			
			
			} catch (Exception e) {
				log.error("Error: " + e);
				return null;
			}}
			@PutMapping()
			public Mono<ResponseEntity<Mono<CurrentAccount>>> updateCurrentAccount(@RequestBody CurrentAccount currentAccount) {
				try {
					Mono<CurrentAccount> updatedCurrentAccount= currentAccountService.findById(currentAccount.getId()).flatMap(c ->{ 
						
						
						currentAccount.setModifiedAt(new Date ());
						
						
						return currentAccountService.save(currentAccount);})
							.cast(CurrentAccount.class) ;
					if ( updatedCurrentAccount ==null) {
						log.info("No se econtró el registro de la cuenta corriente");

						Map<String, Object> params = new HashMap<String, Object>();
						params.put("mensaje", "No existe la cuenta");
						
						return Mono.just(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
						}else {
							log.info("Se modificó correctamente");
							Map<String, Object> params = new HashMap<String, Object>();
							
							params.put("mensaje", "No existe la cuenta");
							
							return Mono.just(new ResponseEntity<>(updatedCurrentAccount, HttpStatus.NOT_FOUND));
								}
					
					
					
				}
				 catch (Exception e) {
					log.error("Error: " + e);
					return Mono.just(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
				}
				
				
				}
			
			@DeleteMapping()
			public Mono<ResponseEntity<Void>> deleteCurrentAccount(@RequestParam(name="idCurrentAccount",required = true) String idCurrentAccount) {
				
				return currentAccountService.findById(idCurrentAccount).flatMap(c ->{ 
					return currentAccountService.deleteById(idCurrentAccount).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))) ;	
				}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
			}
			
		}
	


