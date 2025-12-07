/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package state;

import presenter.CadastroPresenter;
import presenter.LoginPresenter;
import presenter.UsuarioCadastroHomePresenter;
import repository.IUsuarioRepository;

/**
 *
 * @author lukian.borges
 */
public class UsuarioCadastroHomeGoldenState implements CadastroState {

    private final UsuarioCadastroHomePresenter presenter;

    public UsuarioCadastroHomeGoldenState(UsuarioCadastroHomePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void handle() {
        IUsuarioRepository usuarioRepository = presenter.getUsuarioRepository();
        LoginPresenter loginPresenter = new LoginPresenter(usuarioRepository);
    }

}
