package com.senkiu.programa.dto;

import java.math.BigDecimal;

public class ProgramaSocialDTO {
	private Long id;
	private String nombre;
	private String descripcion;
	private String categoriaNombre;
	private BigDecimal objetivoMonto;
	private BigDecimal montoActual;
	private BigDecimal porcentajeAvance;
	private String estadoNombre;

	public ProgramaSocialDTO() {
		super();
	}

	public ProgramaSocialDTO(Long id, String nombre, String descripcion, String categoriaNombre,
			BigDecimal objetivoMonto, BigDecimal montoActual, BigDecimal porcentajeAvance, String estadoNombre) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.categoriaNombre = categoriaNombre;
		this.objetivoMonto = objetivoMonto;
		this.montoActual = montoActual;
		this.porcentajeAvance = porcentajeAvance;
		this.estadoNombre = estadoNombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCategoriaNombre() {
		return categoriaNombre;
	}

	public void setCategoriaNombre(String categoriaNombre) {
		this.categoriaNombre = categoriaNombre;
	}

	public BigDecimal getObjetivoMonto() {
		return objetivoMonto;
	}

	public void setObjetivoMonto(BigDecimal objetivoMonto) {
		this.objetivoMonto = objetivoMonto;
	}

	public BigDecimal getMontoActual() {
		return montoActual;
	}

	public void setMontoActual(BigDecimal montoActual) {
		this.montoActual = montoActual;
	}

	public BigDecimal getPorcentajeAvance() {
		return porcentajeAvance;
	}

	public void setPorcentajeAvance(BigDecimal porcentajeAvance) {
		this.porcentajeAvance = porcentajeAvance;
	}

	public String getEstadoNombre() {
		return estadoNombre;
	}

	public void setEstadoNombre(String estadoNombre) {
		this.estadoNombre = estadoNombre;
	}
}