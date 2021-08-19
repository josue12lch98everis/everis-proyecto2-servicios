package com.everis.banca.app.cuentacorriente.services.implementations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.banca.app.cuentacorriente.dao.CurrentAccountDao;
import com.everis.banca.app.cuentacorriente.models.MovementDocument;
import com.everis.banca.app.cuentacorriente.models.documents.CurrentAccount;
import com.everis.banca.app.cuentacorriente.services.interfaces.ICurrentAccountService;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrentAccountImpl implements ICurrentAccountService {

	@Autowired
	private CurrentAccountDao  currentAccountDao;
	@Autowired
	private WebClient.Builder webClientBuilder;
	@Value("${everis.cantidad.movimientos}")
	private Integer amountOfMovements;
	@Value("${everis.comision.movimientos}")
	private double comissionPerMovement;
	@Value("${everis.url.gateway}")
	private String urlGateway;
	

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

	@Override
	public Mono<ResponseEntity<Map<String,Object>>> depositar(String idCuenta,Double cantidad) {
		Map<String, Object> response = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	
		return webClientBuilder.build().get()
			    .uri(urlGateway+"/api/movement/numberOfMovements?idCuenta="+idCuenta)
		.retrieve().bodyToMono(Long.class).flatMap(number->{
		//TODO: put movements per month static
			if (number<=amountOfMovements) {
		return currentAccountDao.findById(idCuenta).flatMap( c -> {
			c.setAmountInAccount(c.getAmountInAccount() + cantidad);
				return currentAccountDao.save(c).flatMap(acc -> {
					Date date = Calendar.getInstance().getTime();
					MovementDocument movement = MovementDocument.builder()
							.tipoMovimiento("Deposito")
							.tipoProducto("Cuenta Corriente")
							.fechaMovimiento(dateFormat.format(date))
							.idCuenta(idCuenta)
							.idCliente(acc.getClientId())
							.build();
					
					webClientBuilder.build().post()
					.uri(urlGateway+"/api/movement/saveMovement")
					.body(Mono.just(movement), MovementDocument.class)
					.retrieve().bodyToMono(MovementDocument.class).subscribe();
					
					
					response.put("mensaje", "Se hizo el deposito exitosamente");
					response.put("cuenta", acc);
					return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
				});
			
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}else {
		return currentAccountDao.findById(idCuenta).flatMap( c -> {
			c.setAmountInAccount(c.getAmountInAccount() + cantidad-comissionPerMovement);
				return currentAccountDao.save(c).flatMap(acc -> {
					Date date = Calendar.getInstance().getTime();
					MovementDocument movement = MovementDocument.builder()
							.tipoMovimiento("Deposito")
							.tipoProducto("Cuenta Corriente")
							.fechaMovimiento(dateFormat.format(date))
							.comission(comissionPerMovement)
							.idCuenta(idCuenta)
							.idCliente(acc.getClientId())
							.build();
					
					webClientBuilder.build().post()
					.uri(urlGateway+"/api/movement/saveMovement")
					.body(Mono.just(movement), MovementDocument.class)
					.retrieve().bodyToMono(MovementDocument.class).subscribe();
					
					
					response.put("mensaje", "Se hizo el deposito exitosamente");
					response.put("cuenta", acc);
					return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
				});
			
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}}
			);};


	@Override
	public Mono<ResponseEntity<Map<String, Object>>> retirar(String idCuenta, Double cantidad) {
		Map<String, Object> response = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		
		return webClientBuilder.build().get()
			    .uri(urlGateway+"/api/movement/numberOfMovements?idCuenta="+idCuenta)
		.retrieve().bodyToMono(Long.class).flatMap(number->{
		//TODO: put movements per month static
			if (number<=amountOfMovements) {
		
		return currentAccountDao.findById(idCuenta).flatMap( c -> {
			
			
				if(c.getAmountInAccount() - cantidad < 0) {
					response.put("mensaje", "No puede realizar este retiro ya que no cuenta con el saldo suficiente");
					return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
				}else {
					
					
					
					c.setAmountInAccount(c.getAmountInAccount() - cantidad);
					return currentAccountDao.save(c).flatMap(acc -> {
						
						Date date = Calendar.getInstance().getTime();
						MovementDocument movement = MovementDocument.builder()
								.tipoMovimiento("Retiro")
								.tipoProducto("Cuenta Corriente")
								.fechaMovimiento(dateFormat.format(date))
								.idCuenta(idCuenta)
								.idCliente(acc.getClientId())
								.build();
						
						webClientBuilder.build().post()
						.uri(urlGateway+"/api/movement/saveMovement")
						.body(Mono.just(movement), MovementDocument.class)
						.retrieve().bodyToMono(MovementDocument.class).subscribe();
						
						response.put("mensaje", "Se hizo el retiro exitosamente");
						response.put("cuenta", acc);
						return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
					});
				}
		
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}else {
		return currentAccountDao.findById(idCuenta).flatMap( c -> {
			
			
			if(c.getAmountInAccount() - cantidad -comissionPerMovement< 0) {
				response.put("mensaje", "No puede realizar este retiro ya que no cuenta con el saldo suficiente");
				return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
			}else {
				
				
				
				c.setAmountInAccount(c.getAmountInAccount() - cantidad-comissionPerMovement);
				return currentAccountDao.save(c).flatMap(acc -> {
					
					Date date = Calendar.getInstance().getTime();
					MovementDocument movement = MovementDocument.builder()
							.tipoMovimiento("Retiro")
							.tipoProducto("Cuenta Corriente")
							.comission(comissionPerMovement)
							.fechaMovimiento(dateFormat.format(date))
							.idCuenta(idCuenta)
							.idCliente(acc.getClientId())
							.build();
					
					webClientBuilder.build().post()
					.uri(urlGateway+"/api/movement/saveMovement")
					.body(Mono.just(movement), MovementDocument.class)
					.retrieve().bodyToMono(MovementDocument.class).subscribe();
					
					response.put("mensaje", "Se hizo el retiro exitosamente");
					response.put("cuenta", acc);
					return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
				});
			}
	
	}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	} });};

	

	@Override
	public Mono<ResponseEntity<Map<String, Object>>> consultarSaldo(String idAccount) {
		Map<String, Object> response = new HashMap<>();
		
		return currentAccountDao.findById(idAccount).flatMap( c -> {
			
			
			response.put("mensaje", "El saldo de la cuenta es: S/."+c.getAmountInAccount());
			return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
			
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	

	
}
