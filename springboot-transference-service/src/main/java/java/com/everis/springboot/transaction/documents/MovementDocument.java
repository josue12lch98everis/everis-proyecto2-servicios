package java.com.everis.springboot.transaction.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementDocument {
	
	private String id;
	
	private String tipoMovimiento;
	
	private String tipoProducto;
	private String fechaMovimiento;
	private double amount;
	private String idCuenta;
	private double comission;
	private String idCliente;
	
	
}
