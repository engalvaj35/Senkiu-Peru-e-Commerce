package com.senkiu.solicitud.programa.dto;
import java.math.BigDecimal;
public class SolicitudProgramaRequest {
 private String titulo;
 private String descripcion;
 private Long categoriaId;
 private BigDecimal objetivoMonto;
 public SolicitudProgramaRequest() {
 }
 public String getTitulo() {
 return titulo;
 }
 public void setTitulo(String titulo) {
 this.titulo = titulo;
 }
 public String getDescripcion() {
 return descripcion;
 }
 public void setDescripcion(String descripcion) {
 this.descripcion = descripcion;
 }
 public Long getCategoriaId() {
 return categoriaId;
 }
 public void setCategoriaId(Long categoriaId) {
 this.categoriaId = categoriaId;
 }
 public BigDecimal getObjetivoMonto() {
 return objetivoMonto;
 }
 public void setObjetivoMonto(BigDecimal objetivoMonto) {
 this.objetivoMonto = objetivoMonto;
 }
}