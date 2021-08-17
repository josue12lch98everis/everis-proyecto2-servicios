package java.com.everis.springboot.transaction.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transactions")
public class TransactionDocument {
	
	@Id
	private String id;
	
	private Double amount;
	
	private String createAt;
	
	private String idClienteTransmitter;
	
	private String idAccountTransmitter;
	
	private String idAccountReceptor;
	private String idClienteReceptor;
	
	
	
}
