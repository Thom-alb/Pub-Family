package Controle;

import Modelo.CadDono;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CadDonoDAO {

    private Connection conexao;

    public CadDonoDAO() {
        conexao = new ConexaoDAO().conectaBancoDados();
    }

    // CREATE
    public void cadastrarDono(CadDono dono) {
        try {
            String sql = "INSERT INTO gerente (nome, usuario, senha) VALUES (?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, dono.getNome());
            stmt.setString(2, dono.getUsuario());
            stmt.setString(3, dono.getSenha());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Gerente cadastrado com sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar gerente: " + erro.getMessage());
        }
    }

    // READ 
    public List<CadDono> listarDonos() {
        List<CadDono> lista = new ArrayList<>();

        try {
            String sql = "SELECT * FROM gerente";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CadDono d = new CadDono();

                d.setId(rs.getInt("id"));
                d.setNome(rs.getString("nome"));
                d.setUsuario(rs.getString("usuario"));
                d.setSenha(rs.getString("senha"));

                lista.add(d);
            }

            stmt.close();

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao listar gerentes: " + erro.getMessage());
        }

        return lista;
    }

    // UPDATE
    public void atualizarDono(CadDono dono) {
        try {
            String sql = "UPDATE gerente SET nome = ?, usuario = ?, senha = ? WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, dono.getNome());
            stmt.setString(2, dono.getUsuario());
            stmt.setString(3, dono.getSenha());
            stmt.setInt(4, dono.getId());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Gerente atualizado com sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar gerente: " + erro.getMessage());
        }
    }
    
    // LOGIN
    public boolean login(String usuario, String senha) {
        try {
            String sql = "SELECT id FROM gerente WHERE usuario = ? AND senha = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, usuario);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            boolean existe = rs.next(); // true se encontrou

            stmt.close();
            return existe;

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao realizar login: " + erro.getMessage());
            return false;
        }
    }


    // DELETE
    public void excluirDono(int id) {
        try {
            String sql = "DELETE FROM gerente WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Gerente excluído com sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir gerente: " + erro.getMessage());
        }
    }
}
