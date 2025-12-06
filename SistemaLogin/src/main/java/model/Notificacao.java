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
    private int id;
    private int idRemetente;
    private int idDestinatario;
    private String mensagem;
    private LocalDateTime dataEnvio;
    private boolean lida;

    public Notificacao(int id, int idRemetente, int idDestinatario, String mensagem, LocalDateTime dataEnvio, boolean lida) {
        this.id = id;
        this.idRemetente = idRemetente;
        this.idDestinatario = idDestinatario;
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
        this.lida = lida;
    }
    
    public Notificacao(){
       
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getIdRemetente(){
        return idRemetente;
    }
    public void setIdRemetente(int idRemetente){
        this.idRemetente = idRemetente;
    }
    public int getIdDestinatario(){
        return idDestinatario;
    }
    public void setIdDestinatario(int idDestinatario){
        this.idDestinatario = idDestinatario;
    }
    public String getMensagem(){
        return mensagem;
    }
    public void setMensagem(String mensagem){
        this.mensagem = mensagem;
    }
    public LocalDateTime getDataEnvio(){
        return dataEnvio;
    }
    public void setDataEnvio(LocalDateTime dataEnvio){
        this.dataEnvio = dataEnvio;
    }
    public boolean isLida(){
        return lida;
    }
    public void setLida(boolean lida ){
        this.lida = lida;
    }
    @Override
    public String toString(){
        return mensagem;
    }
    
    
    
}
