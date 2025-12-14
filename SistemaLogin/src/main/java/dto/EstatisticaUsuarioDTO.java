/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

public class EstatisticaUsuarioDTO {

    private String nomeUsuario;
    private int total;
    private int lidas;
    private int naoLidas;

    public EstatisticaUsuarioDTO(String nomeUsuario, int total, int lidas, int naoLidas) {
        this.nomeUsuario = nomeUsuario;
        this.total = total;
        this.lidas = lidas;
        this.naoLidas = naoLidas;
    }

    public String getNomeUsuario() { return nomeUsuario; }
    public int getTotal() { return total; }
    public int getLidas() { return lidas; }
    public int getNaoLidas() { return naoLidas; }
}
