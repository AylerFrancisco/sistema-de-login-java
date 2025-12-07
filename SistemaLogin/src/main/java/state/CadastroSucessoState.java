/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package state;

import presenter.PrimeiroAcessoPresenter;
import repository.IUsuarioRepository;

/**
 *
 * @author lukian.borges
 */
public class CadastroSucessoState implements CadastroState {
    
    private final PrimeiroAcessoPresenter presenter;

    public CadastroSucessoState(PrimeiroAcessoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void handle() {
        IUsuarioRepository usuarioRepository = presenter.getUsuarioRepository();
        PrimeiroAcessoPresenter acessoPresenter = new PrimeiroAcessoPresenter(usuarioRepository);
        presenter.getPrimeiroAcessoViewSwing().dispose();
    }
    
}
