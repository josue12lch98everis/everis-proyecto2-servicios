package com.everis.banca.app.cuentacorriente.models;

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
	
	private double comission;
	
	private String idCuenta;
	
	private String idCliente;
	
}
