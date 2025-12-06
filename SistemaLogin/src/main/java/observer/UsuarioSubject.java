/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package observer;

/**
 *
 * @author lukian.borges
 */
public interface UsuarioSubject {
    
    void addUsuarioObserver(UsuarioObserver observer);
    void removeUsuarioObserver(UsuarioObserver observer);
    void notifyUsuarioObservers();
    
}
