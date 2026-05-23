package com.senkiu.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioPerfilRequest {
    @Size(max = 100)
    private String nombres;

    @Email(message = "Formato de email inválido")
    private String email;

    private String apellidoPaterno;
    private String apellidoMaterno;
    
    @Pattern(
        regexp = "^(|\\d{8})$",
        message = "El DNI debe tener 8 dígitos"
    )
    private String dni;

    @Pattern(
        regexp = "^(|\\d{9})$",
        message = "El celular debe tener 9 dígitos"
    )
    private String celular;
    
    private String direccion;
    private String distrito;
    private String provincia;
    private String departamento;

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}