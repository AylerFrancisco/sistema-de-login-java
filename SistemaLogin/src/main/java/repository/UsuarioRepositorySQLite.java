/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import adapter.LogAdapter;
import adapter.LogAdapterImpl;
import configSQLite.SQLiteConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import observer.UsuarioObserver;
import observer.UsuarioSubject;

/**
 *
 * @author Ayler
 */
public class UsuarioRepositorySQLite implements IUsuarioRepository, UsuarioSubject{

    
    private List<Usuario> usuarios = new ArrayList<>();
    private final List<UsuarioObserver> observers = new ArrayList<>();
    private static UsuarioRepositorySQLite instance;
    private LogAdapter logAdapter;
    
     // Construtor privado para garantir o Singleton
    public UsuarioRepositorySQLite() {
        this.usuarios = listarTodos();
        logAdapter = new LogAdapterImpl.Builder().build();
    }
    
    public static synchronized UsuarioRepositorySQLite getInstance() {
        if (instance == null) {
            instance = new UsuarioRepositorySQLite();
        }
        return instance;
    }

    @Override
    public boolean criar(Usuario usuario) {
        try (Connection conexao = new SQLiteConexao().getConnexao()) { // Obtém uma conexão nova
           
           
            String sql = "INSERT INTO usuario (usuario, senha, tipoCadastro, autorizado, dataCadastro) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {
                preparedStatement.setString(1, usuario.getUsuario());
                preparedStatement.setString(2, usuario.getSenha());
                preparedStatement.setString(3, usuario.getTipoCadastro());
                preparedStatement.setInt(4, usuario.getAutorizado());
                preparedStatement.setString(5, LocalDate.now().toString());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    this.usuarios = listarTodos();
                    notifyUsuarioObservers();
                    //logAdapter.log("create", usuario.getUsuario(), usuario.getEmail());
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir usuário: " + e.getMessage());
        }
    }

    @Override
    public Usuario consultar(String usuario) {
       Connection conexao = new SQLiteConexao().getConnexao();  //conexao do banco
        try {
            String sql = "SELECT * FROM usuario WHERE usuario = ? LIMIT 1";

            PreparedStatement preparedStatement = conexao.prepareStatement(sql);
            preparedStatement.setString(1, usuario);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            } else {
                Usuario usuarioEncontrado = new Usuario();
                usuarioEncontrado.setId(resultSet.getInt("id"));
                usuarioEncontrado.setUsuario(resultSet.getString("usuario"));
                usuarioEncontrado.setSenha(resultSet.getString("senha"));
                usuarioEncontrado.setTipoCadastro(resultSet.getString("tipoCadastro"));
                usuarioEncontrado.setAutorizado(resultSet.getInt("autorizado"));
                usuarioEncontrado.setDataCadastro(resultSet.getString("dataCadastro"));
                return usuarioEncontrado;
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
//            ConexaoService.closeConexao(conexao);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
       Connection conexao = new SQLiteConexao().getConnexao();

        try {
            String sql = "UPDATE usuario SET usuario = ?, senha = ? WHERE id = ?";
            PreparedStatement preparedStatement = conexao.prepareStatement(sql);

            preparedStatement.setString(1, usuario.getUsuario());
            preparedStatement.setString(2, usuario.getSenha());
            preparedStatement.setInt(3, usuario.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                this.usuarios = listarTodos();
                notifyUsuarioObservers();
                //logAdapter.log("update", usuario.getUsuario(), usuario.getEmail());
            } else {
                throw new RuntimeException("Nenhum usuário foi atualizado.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
//            ConexaoService.closeConexao(conexao);
        }
    }

    @Override
    public boolean deletar(String usuario) {
      Connection conexao = new SQLiteConexao().getConnexao();

        try {
            String sql = "DELETE FROM usuario WHERE usuario = ?";
            PreparedStatement preparedStatement = conexao.prepareStatement(sql);

            preparedStatement.setString(1, usuario);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                this.usuarios = listarTodos();
                notifyUsuarioObservers();
                logAdapter.log("delete", "usuario", usuario);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
//            ConexaoService.closeConexao(conexao);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();

       Connection conexao = new SQLiteConexao().getConnexao();


        try {
            String sql = "SELECT * FROM usuario";
            PreparedStatement preparaLista = conexao.prepareStatement(sql);

            try (ResultSet resultSet = preparaLista.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nome = resultSet.getString("usuario");
                    String senha = resultSet.getString("senha");
                    String tipoCadastro = resultSet.getString("tipoCadastro");
                    int autorizado = resultSet.getInt("autorizado");
                    String dataCadastro = resultSet.getString("dataCadastro");

                    Usuario usuario = new Usuario(id, nome, senha, tipoCadastro, autorizado, dataCadastro);
                    usuarios.add(usuario);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
//            ConexaoService.closeConexao(conexao);
        }

        return usuarios;
    }

//    @Override
//    public Usuario buscarPorEmail(String email) {
//        String sql = "SELECT id, nome, senha, email, dataCadastro FROM usuario WHERE email = ?";
//        Connection conexao = (Connection) new SqlLiteConexao();
//
//        try (PreparedStatement pstmt = conexao.prepareStatement(sql);) {
//            pstmt.setString(1, email);
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                Integer id = rs.getInt("id");
//                String nome = rs.getString("nome");
//                String senha = rs.getString("senha");
//                String dataCadastro = rs.getString("dataCadastro");
//
//                return new Usuario(id, nome, senha, dataCadastro, email);
//            }
//        } catch (SQLException e) {
//            throw new IllegalArgumentException("Não foi possível buscar o usuário", e);
//        }
//        return null;
//    }

    @Override
    public Usuario buscarPorUsuario(String usuario1) {
        String sql = "SELECT usuario, senha, tipoCadastro, autorizado, dataCadastro FROM usuario WHERE usuario = ?";
       Connection conexao = new SQLiteConexao().getConnexao();

        try (PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, usuario1);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String usuario = rs.getString("usuario");
                String senha = rs.getString("senha");
                 String tipoCadastro = rs.getString("tipoCadastro");
                    int autorizado = rs.getInt("autorizado");
                    String dataCadastro = rs.getString("dataCadastro");

                return new Usuario(usuario, senha, tipoCadastro, autorizado, dataCadastro);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Não foi possível buscar o usuário", e);
        }
        return null;
    }

    @Override
    public void addUsuarioObserver(UsuarioObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeUsuarioObserver(UsuarioObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyUsuarioObservers() {
        for (UsuarioObserver observer : observers) {
            observer.onUsuariosUpdated(usuarios);
        }
    }

    public boolean hasUsuarioObserver(UsuarioObserver observer) {
        return observers.contains(observer);
    }
    
    
    
    
    
}
