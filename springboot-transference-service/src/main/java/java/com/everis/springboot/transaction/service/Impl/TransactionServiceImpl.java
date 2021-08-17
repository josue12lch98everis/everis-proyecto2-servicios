package java.com.everis.springboot.transaction.service.Impl;

import java.com.everis.springboot.transaction.dao.TransactionDao;
import java.com.everis.springboot.transaction.documents.CreateAccountDocument;
import java.com.everis.springboot.transaction.documents.CurrentAccount;
import java.com.everis.springboot.transaction.documents.FixedTermDocument;
import java.com.everis.springboot.transaction.documents.MovementDocument;
import java.com.everis.springboot.transaction.documents.SavingAccount;
import java.com.everis.springboot.transaction.documents.TransactionDocument;
import java.com.everis.springboot.transaction.service.TransactionService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Value("${everis.url.gateway}")
	private String urlGateway;


	@Override
	public Mono<ResponseEntity<Map<String,Object>>> depositar(TransactionDocument transactionDocument ) {
		Mono <Object> accountTransmiter =getAccountDetails(transactionDocument.getIdAccountTransmitter());
		Mono <Object>accountReceptor =		getAccountDetails(transactionDocument.getIdAccountReceptor());
		Map<String, Object> response = new HashMap<>();
		double amountToTransfer =transactionDocument.getAmount();
		if (accountTransmiter.equals(CurrentAccount.class)) {
CurrentAccount currentAccount= (CurrentAccount) accountTransmiter.block();
if (currentAccount.getAmountInAccount()- transactionDocument.getAmount()<0) {
	response.put("mensaje", "Saldo insuficiente para realizar la transferencia");
	return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
}else {
currentAccount.setAmountInAccount(currentAccount.getAmountInAccount()-amountToTransfer);
webClientBuilder.build().put()
.uri(urlGateway+"/api/currentAccount").syncBody(currentAccount)

.retrieve();
if (accountReceptor.block().getClass()==CurrentAccount.class) {
	CurrentAccount currentAccountReceptor=(CurrentAccount) accountReceptor.block();
	currentAccountReceptor.setAmountInAccount(amountToTransfer+currentAccountReceptor.getAmountInAccount());
	webClientBuilder.build().put()
    .uri(urlGateway+"/api/currentAccount").syncBody(currentAccountReceptor)
    
.retrieve();
	response.put("mensaje", "Operación realizada satisfactoriamente");
	
	return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
		
}else if (accountReceptor.block().getClass()==SavingAccount.class){
	SavingAccount account=(SavingAccount) accountReceptor.block();
	account.setAmountInAccount(amountToTransfer+account.getAmountInAccount());
	webClientBuilder.build().put()
    .uri(urlGateway+"/api/accountSavings").syncBody(account)
    
.retrieve();	
	response.put("mensaje", "Operación realizada satisfactoriamente");

return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));

}else {
	FixedTermDocument account=(FixedTermDocument) accountReceptor.block();
	account.setSaldo(amountToTransfer+account.getSaldo());
	webClientBuilder.build().post()
    .uri(urlGateway+"/api/fixed-term/saveAccount").syncBody(account).retrieve();
response.put("mensaje", "Operación realizada satisfactoriamente");
	
	return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
	
}
}	
			
		}else if (accountTransmiter.block().getClass()==SavingAccount.class){
			SavingAccount savingAccount= (SavingAccount) accountTransmiter.block();
			if (savingAccount.getAmountInAccount()- transactionDocument.getAmount()<0) {
				response.put("mensaje", "Saldo insuficiente para realizar la transferencia");
				return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
			}else {
				savingAccount.setAmountInAccount(savingAccount.getAmountInAccount()-amountToTransfer);

				webClientBuilder.build().put()
			    .uri(urlGateway+"/api/accountSavings").syncBody(savingAccount)
			    
		.retrieve();
				
				if (accountReceptor.block().getClass()==CurrentAccount.class) {
					CurrentAccount currentAccountReceptor=(CurrentAccount) accountReceptor.block();
					currentAccountReceptor.setAmountInAccount(amountToTransfer+currentAccountReceptor.getAmountInAccount());
					webClientBuilder.build().put()
				    .uri(urlGateway+"/api/currentAccount").syncBody(currentAccountReceptor)
				    
			.retrieve();
					response.put("mensaje", "Operación realizada satisfactoriamente");
					
					return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
						
				}else if (accountReceptor.block().getClass()==SavingAccount.class){
					SavingAccount account=(SavingAccount) accountReceptor.block();
					account.setAmountInAccount(amountToTransfer+account.getAmountInAccount());
					webClientBuilder.build().put()
				    .uri(urlGateway+"/api/accountSavings").syncBody(account)
				    
			.retrieve();	response.put("mensaje", "Operación realizada satisfactoriamente");
			
			return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
			
				}else {
					FixedTermDocument account=(FixedTermDocument) accountReceptor.block();
					account.setSaldo(amountToTransfer+account.getSaldo());
					webClientBuilder.build().post()
				    .uri(urlGateway+"/api/fixed-term/saveAccount").syncBody(account).retrieve();
response.put("mensaje", "Operación realizada satisfactoriamente");
					
					return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
					
				}
			}
			
		}else {
			FixedTermDocument fixedTermDocument= (FixedTermDocument) accountTransmiter.block();
			fixedTermDocument.setSaldo(amountToTransfer+fixedTermDocument.getSaldo());
			webClientBuilder.build().post()
		    .uri(urlGateway+"/api/fixed-term/saveAccount").syncBody(fixedTermDocument).retrieve();
			if (fixedTermDocument.getSaldo() - transactionDocument.getAmount()<0) {
				response.put("mensaje", "Saldo insuficiente para realizar la transferencia");
				
				return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
			}else {
				
				fixedTermDocument.setSaldo(fixedTermDocument.getSaldo()-amountToTransfer);

				if (accountReceptor.block().getClass()==CurrentAccount.class) {
					CurrentAccount currentAccountReceptor=(CurrentAccount) accountReceptor.block();
					currentAccountReceptor.setAmountInAccount(amountToTransfer+currentAccountReceptor.getAmountInAccount());
					webClientBuilder.build().put()
				    .uri(urlGateway+"/api/currentAccount").syncBody(currentAccountReceptor)
				    
			.retrieve();
					response.put("mensaje", "Operación realizada satisfactoriamente");
					
					return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
						
				}else if (accountReceptor.block().getClass()==SavingAccount.class){
					SavingAccount account=(SavingAccount) accountReceptor.block();
					account.setAmountInAccount(amountToTransfer+account.getAmountInAccount());
					webClientBuilder.build().put()
				    .uri(urlGateway+"/api/accountSavings").syncBody(account)
				    
			.retrieve();	response.put("mensaje", "Operación realizada satisfactoriamente");
			
			return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
			
				}else {
					FixedTermDocument account=(FixedTermDocument) accountReceptor.block();
					account.setSaldo(amountToTransfer+account.getSaldo());
					webClientBuilder.build().post()
				    .uri(urlGateway+"/api/fixed-term/saveAccount").syncBody(account).retrieve();
response.put("mensaje", "Operación realizada satisfactoriamente");
					
					return Mono.just(new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST));
					
				}
		}}
		
		
	/*	Map<String, Object> response = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	
		
		  return webClientBuilder.build().get()
				    .uri(urlGateway+"/api/movement/numberOfMovements?idCuenta="+transactionDocument.getIdAccountTransmitter())
					.retrieve().bodyToMono(Long.class)
		.flatMap(number->{
			transactionDao.findById().flatMap( c -> {
		
			c.setAmount(c.getAmount() + cantidad);
			
			
				
				return transactionDao.save(c).flatMap(acc -> {
					Date date = Calendar.getInstance().getTime();
					MovementDocument movement = MovementDocument.builder()
							.tipoMovimiento("Deposito")
							.tipoProducto("Cuenta Plazo Fijo")
							.fechaMovimiento(dateFormat.format(date))
							.idCuenta(idCuenta)
							.idCliente(acc.getIdClienteTransmitter())
							.build();
					
					webClientBuilder.build().post()
					.uri(urlGateway+"/api/movement/saveMovement")
					.body(Mono.just(movement), MovementDocument.class)
					.retrieve().bodyToMono(MovementDocument.class).subscribe();
					
					
					response.put("mensaje", "Se hizo el deposito exitosamente");
					response.put("cuenta", acc);
					return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
				});
		
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));});*/
	}

	@Override
	public Mono<ResponseEntity<Map<String,Object>>> retirar(String idCuenta,Double cantidad) {
		Map<String, Object> response = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		
		return transactionDao.findById(idCuenta).flatMap( c -> {
			
			
				if(c.getAmount() - cantidad < 0) {
					response.put("mensaje", "No puede realizar este retiro ya que no cuenta con el saldo suficiente");
					return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
				}else {
					
					
					
					c.setAmount(c.getAmount() - cantidad);
					return transactionDao.save(c).flatMap(acc -> {
						
						Date date = Calendar.getInstance().getTime();
						MovementDocument movement = MovementDocument.builder()
								.tipoMovimiento("Retiro")
								.tipoProducto("Cuenta Plazo Fijo")
								.fechaMovimiento(dateFormat.format(date))
								.idCuenta(idCuenta)
								.idCliente(acc.getIdClienteTransmitter())
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
	}

	@Override
	public Mono<ResponseEntity<Map<String, Object>>> consultarSaldo(String idCliente) {
		Map<String, Object> response = new HashMap<>();
		
		return transactionDao.findByIdCliente(idCliente).flatMap( c -> {
			
			
			response.put("mensaje", "El saldo de la cuenta es: S/."+c.getAmount());
			return Mono.just(new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK));
			
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@Override
	public Mono< Object> getAccountDetails(String idCuenta) {
		// TODO Auto-generated method stub
		Object obj ;
		
		CreateAccountDocument account	 = webClientBuilder.build().get()
		.uri(urlGateway+"/api/account/findAccount/"+idCuenta).retrieve().bodyToMono(CreateAccountDocument.class).toProcessor().block();
	 if (account.getAccount_type()=="Cuenta de Ahorro") {
		 		 
		 obj = webClientBuilder.build().get().uri(urlGateway+"/api/currentAccount/getAccount/"+idCuenta).retrieve().bodyToMono(SavingAccount.class);
	 }
	 else if (account.getAccount_type()=="Cuenta Corriente") {
		 obj = webClientBuilder.build().get().uri(urlGateway+"/api/currentAccount/getAccount/"+idCuenta).retrieve().bodyToMono(CurrentAccount.class);
	 }else {
		 obj = webClientBuilder.build().get().uri(urlGateway+"/api/fixed-term/getAccount/"+idCuenta).retrieve().bodyToMono(FixedTermDocument.class);	 
	 }
		return Mono.just(obj);
		
				
		   }
	

	
	}
	


