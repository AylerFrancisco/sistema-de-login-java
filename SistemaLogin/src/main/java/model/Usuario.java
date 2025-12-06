/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author luluzinho da pomerana
 */
public class Usuario {
    private static int contadorId = 0;
    private int id;
    private String usuario;
    private String senha;
    private String tipoCadastro;
    private Integer autorizado;
    private String dataCadastro;

    public Usuario() {
    }

    public Usuario(String usuario, String senha, String tipoCadastro, Integer autorizado, String dataCadastro) {
        this.id = ++contadorId;
        this.usuario = usuario;
        this.senha = senha;
        this.tipoCadastro = tipoCadastro;
        this.autorizado = autorizado;
        this.dataCadastro = dataCadastro;
    }

    public Usuario(int id, String usuario, String senha, String tipoCadastro, Integer autorizado, String dataCadastro) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
        this.tipoCadastro = tipoCadastro;
        this.autorizado = autorizado;
        this.dataCadastro = dataCadastro;
    }

    public static int getContadorId() {
        return contadorId;
    }

    public static void setContadorId(int contadorId) {
        Usuario.contadorId = contadorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getTipoCadastro() {
        return tipoCadastro;
    }

    public void setTipoCadastro(String tipoCadastro) {
        this.tipoCadastro = tipoCadastro;
    }

    public Integer getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Integer autorizado) {
        this.autorizado = autorizado;
    }
}
