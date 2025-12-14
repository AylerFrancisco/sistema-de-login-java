/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author lukian.borges
 */
public class EstatisticaRemetenteDTO {

    private String nomeRemetente;
    private int totalEnviadas;

    public EstatisticaRemetenteDTO(String nomeRemetente, int totalEnviadas) {
        this.nomeRemetente = nomeRemetente;
        this.totalEnviadas = totalEnviadas;
    }

    public String getNomeRemetente() {
        return nomeRemetente;
    }

    public int getTotalEnviadas() {
        return totalEnviadas;
    }
}