package Controle;

import Modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CadMesaDAO {

    Connection conexao;

    public CadMesaDAO() {
        conexao = new ConexaoDAO().conectaBancoDados();
    }

    // CREATE
    public void cadastrarMesa(CadMesa mesa) {
        try {
            String sql = "INSERT INTO mesa (acentos, cheia) VALUES (?, false)";
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(mesa.getAssentos()));
            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Mesa cadastrada com sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar mesa: " + erro.getMessage());
        }
    }

    // ADICIONAR CLIENTE À MESA
    public void adicionarClienteMesa(int idMesa, int idCliente) {
        try {
            String sql = "INSERT INTO mesa_cliente (id_mesa, id_cliente) VALUES (?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idMesa);
            stmt.setInt(2, idCliente);
            stmt.execute();
            stmt.close();

            atualizarStatusMesa(idMesa);

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar cliente à mesa: " + erro.getMessage());
        }
    }

    // ATUALIZA STATUS DA MESA 
    private void atualizarStatusMesa(int idMesa) {
        try {
            String sqlAssentos = "SELECT acentos FROM mesa WHERE id = ?";
            PreparedStatement stmtAssentos = conexao.prepareStatement(sqlAssentos);
            stmtAssentos.setInt(1, idMesa);
            ResultSet rsAssentos = stmtAssentos.executeQuery();

            int totalAssentos = 0;
            if (rsAssentos.next()) {
                totalAssentos = rsAssentos.getInt("acentos");
            }

            String sqlClientes = "SELECT COUNT(*) AS total FROM mesa_cliente WHERE id_mesa = ?";
            PreparedStatement stmtClientes = conexao.prepareStatement(sqlClientes);
            stmtClientes.setInt(1, idMesa);
            ResultSet rsClientes = stmtClientes.executeQuery();

            int totalClientes = 0;
            if (rsClientes.next()) {
                totalClientes = rsClientes.getInt("total");
            }

            boolean cheia = totalClientes >= totalAssentos;

            String sqlUpdate = "UPDATE mesa SET cheia = ? WHERE id = ?";
            PreparedStatement stmtUpdate = conexao.prepareStatement(sqlUpdate);
            stmtUpdate.setBoolean(1, cheia);
            stmtUpdate.setInt(2, idMesa);
            stmtUpdate.execute();

            stmtAssentos.close();
            stmtClientes.close();
            stmtUpdate.close();

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar status da mesa: " + erro.getMessage());
        }
    }

    // READ 
    public List<String[]> listarMesas() {

        List<String[]> lista = new ArrayList<>();

        try {
            String sql = """
                SELECT m.id, c.nome
                FROM mesa m
                LEFT JOIN mesa_cliente mc ON m.id = mc.id_mesa
                LEFT JOIN cliente c ON mc.id_cliente = c.id
                ORDER BY m.id
            """;

            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            int mesaAtual = -1;
            StringBuilder clientes = new StringBuilder();

            while (rs.next()) {
                int idMesa = rs.getInt("id");

                if (mesaAtual != idMesa && mesaAtual != -1) {
                    lista.add(new String[]{
                        String.valueOf(mesaAtual),
                        clientes.toString()
                    });
                    clientes = new StringBuilder();
                }

                mesaAtual = idMesa;

                String nome = rs.getString("nome");
                if (nome != null) {
                    clientes.append(nome).append(", ");
                }
            }

            if (mesaAtual != -1) {
                lista.add(new String[]{
                    String.valueOf(mesaAtual),
                    clientes.toString()
                });
            }

            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar mesas");
        }

        return lista;
    }
    
    //READ para ver clientes na mesa
    public String listarClientesMesaTexto(int idMesa) {

        StringBuilder clientes = new StringBuilder();

        try {
            String sql = """
                SELECT c.nome
                FROM mesa_cliente mc
                JOIN cliente c ON mc.id_cliente = c.id
                WHERE mc.id_mesa = ?
            """;

            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idMesa);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clientes.append(rs.getString("nome")).append(", ");
            }

            stmt.close();

        } catch (SQLException e) {
            return "";
        }

        if (clientes.length() > 2) {
            clientes.setLength(clientes.length() - 2); // remove última vírgula
        }

        return clientes.toString();
    }
    //Verificação 
    public boolean clienteJaEmOutraMesa(int idCliente) {

        String sql = """
            SELECT COUNT(*) 
            FROM mesa_cliente
            WHERE id_cliente = ?
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao verificar se cliente já está em outra mesa:\n" + e.getMessage());
        }

        return false;
    }

    
    public List<CadMesa> listarMesasTabela() {
        List<CadMesa> lista = new ArrayList<>();

        try {
            String sql = "SELECT id, acentos, cheia FROM mesa ORDER BY id";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CadMesa m = new CadMesa();
                m.setID(rs.getInt("id"));
                m.setAssentos(String.valueOf(rs.getInt("acentos")));
                m.setCheia(rs.getBoolean("cheia"));
                lista.add(m);
            }

            stmt.close();
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao listar mesas: " + erro.getMessage());
        }

        return lista;
    }
    
    //Verificação 
    public boolean clienteJaNaMesa(int idMesa, int idCliente) {
        try {
            String sql = """
                SELECT 1 FROM mesa_cliente 
                WHERE id_mesa = ? AND id_cliente = ?
            """;
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idMesa);
            stmt.setInt(2, idCliente);
            ResultSet rs = stmt.executeQuery();

            boolean existe = rs.next();
            stmt.close();
            return existe;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao validar cliente na mesa");
            return true;
        }
    }

    //Verificação
    public boolean mesaTemVaga(int idMesa) {
        try {
            String sql = """
                SELECT 
                    (SELECT COUNT(*) FROM mesa_cliente WHERE id_mesa = ?) AS ocupados,
                    acentos
                FROM mesa WHERE id = ?
            """;

            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idMesa);
            stmt.setInt(2, idMesa);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("ocupados") < rs.getInt("acentos");
            }

            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao validar vagas da mesa");
        }
        return false;
    }


    public List<CadUsuario> listarClientesMesa(int idMesa) {
        List<CadUsuario> lista = new ArrayList<>();

        try {
            String sql = """
                SELECT c.id, c.nome
                FROM cliente c
                JOIN mesa_cliente mc ON c.id = mc.id_cliente
                WHERE mc.id_mesa = ?
            """;

            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idMesa);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CadUsuario u = new CadUsuario();
                u.setId(rs.getInt("id"));
                u.setUsuario(rs.getString("nome"));
                lista.add(u);
            }
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar clientes da mesa");
        }
        return lista;
    }


    //DELETE Cliente da mesa
    public void removerClienteMesa(int idMesa, int idCliente) {
        try {
            String sql = "DELETE FROM mesa_cliente WHERE id_mesa = ? AND id_cliente = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idMesa);
            stmt.setInt(2, idCliente);
            stmt.execute();
            stmt.close();

            atualizarStatusMesa(idMesa);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover cliente da mesa");
        }
    }
    
    // DELETE
    public void excluirMesa(int id) {
        try {
            String sqlMesaCliente = "DELETE FROM mesa_cliente WHERE id_mesa = ?";
            PreparedStatement stmtRel = conexao.prepareStatement(sqlMesaCliente);
            stmtRel.setInt(1, id);
            stmtRel.execute();
            stmtRel.close();

            String sqlMesa = "DELETE FROM mesa WHERE id = ?";
            PreparedStatement stmtMesa = conexao.prepareStatement(sqlMesa);
            stmtMesa.setInt(1, id);
            stmtMesa.execute();
            stmtMesa.close();

            JOptionPane.showMessageDialog(null, "Mesa excluída com sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir mesa: " + erro.getMessage());
        }
    }
}
