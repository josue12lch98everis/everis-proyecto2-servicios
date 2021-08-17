package java.com.everis.springboot.transaction.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CreateAccountDocument {
	
	@Id
	private String id;
	
	private String account_type;
	private String idAccount;
	private String client;
	
	private Double mount;
	
}
