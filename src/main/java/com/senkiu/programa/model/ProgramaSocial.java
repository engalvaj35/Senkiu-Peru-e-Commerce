package com.senkiu.programa.model;

import java.math.BigDecimal;
import com.senkiu.categoria.model.Categoria;
import com.senkiu.estado.publicacion.model.EstadoPublicacion;
import com.senkiu.ong.model.Ong;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "programa_social")
public class ProgramaSocial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ong_id", nullable = false)
	private Ong ong;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@Column(name = "nombre", nullable = false, length = 150)
	private String nombre;

	@Column(name = "objetivo_monto", nullable = false, precision = 10, scale = 2)
	private BigDecimal objetivoMonto;

	@Column(name = "monto_actual", precision = 10, scale = 2)
	private BigDecimal montoActual = BigDecimal.ZERO;

	@Column(name = "descripcion", columnDefinition = "TEXT")
	private String descripcion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estado_id", nullable = false)
	private EstadoPublicacion estado;

	public ProgramaSocial() {
		super();
	}

	public ProgramaSocial(Long id, Ong ong, Categoria categoria, String nombre, BigDecimal objetivoMonto,
			BigDecimal montoActual, String descripcion, EstadoPublicacion estado) {
		super();
		this.id = id;
		this.ong = ong;
		this.categoria = categoria;
		this.nombre = nombre;
		this.objetivoMonto = objetivoMonto;
		this.montoActual = montoActual;
		this.descripcion = descripcion;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ong getOng() {
		return ong;
	}

	public void setOng(Ong ong) {
		this.ong = ong;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoPublicacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoPublicacion estado) {
		this.estado = estado;
	}
}
