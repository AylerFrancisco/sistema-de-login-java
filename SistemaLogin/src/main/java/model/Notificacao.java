/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author Ayler
 */
public class Notificacao {
    private Integer id;
    private Integer idRemetente;
    private Integer idDestinatario;
    private String mensagem;
    private String dataEnvio;
    private Integer lida;

    public Notificacao(Integer idRemetente, Integer idDestinatario, String mensagem) {
        this.idRemetente = idRemetente;
        this.idDestinatario = idDestinatario;
        this.mensagem = mensagem;
        this.dataEnvio = LocalDateTime.now().toString();
        this.lida = 0;
    }
    
    public Notificacao(){
       
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getMensagem(){
        return mensagem;
    }
    public void setMensagem(String mensagem){
        this.mensagem = mensagem;
    }

    public Integer getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(Integer idRemetente) {
        this.idRemetente = idRemetente;
    }

    public Integer getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(Integer idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Integer getLida() {
        return lida;
    }

    public void setLida(Integer lida) {
        this.lida = lida;
    }
    
    
   
    @Override
    public String toString(){
        return mensagem;
    }
    
    
    
}
