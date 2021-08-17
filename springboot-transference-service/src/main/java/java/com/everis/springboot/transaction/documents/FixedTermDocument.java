package java.com.everis.springboot.transaction.documents;

import org.springframework.data.annotation.Id;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor

public class FixedTermDocument {
	
	@Id
	private String id;
	
	private Double saldo;
	
	private String fechaCreacion;
	
	private String idCliente;
	
	private Integer diaRetiro;
}
