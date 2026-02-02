package Controle;

import Modelo.CadProduto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CadProdutoDAO {

    Connection conexao;

    public CadProdutoDAO() {
        conexao = new ConexaoDAO().conectaBancoDados();
    }

    // CREATE
    public void cadastrarProduto(CadProduto produto) {
        
        if (produtoExiste(produto.getNome(), produto.getTipo())) {
        JOptionPane.showMessageDialog(null,
                "Este produto já está cadastrado!");
        return;
    }
        try {
            String sql = "INSERT INTO produto (nome, tipo, quantidade, preco) VALUES (?, ?, ?, ?)";
            
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, produto.getNome());        // <-- adicionar no modelo depois
            stmt.setString(2, produto.getTipo());
            stmt.setInt(3, Integer.parseInt(produto.getQuantidade()));
            stmt.setDouble(4, Double.parseDouble(produto.getPreco()));

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + erro.getMessage());
        }
    }
    
    public boolean produtoExiste(String nome, String tipo) {
    try {
        String sql = "SELECT COUNT(*) FROM produto WHERE nome = ? AND tipo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, nome);
        stmt.setString(2, tipo);

        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getInt(1) > 0;

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Erro ao verificar produto: " + e.getMessage());
        return false;
    }
}


    // READ 
    public List<CadProduto> listarProdutos() {
        List<CadProduto> lista = new ArrayList<>();

        try {
            String sql = "SELECT * FROM produto";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CadProduto p = new CadProduto();
                p.setID(rs.getInt("id"));
                p.setNome(rs.getString("nome")); // <-- precisa existir no modelo
                p.setTipo(rs.getString("tipo"));
                p.setQuantidade(rs.getString("quantidade"));
                p.setPreco(rs.getString("preco"));

                lista.add(p);
            }

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + erro.getMessage());
        }

        return lista;
    }

    // UPDATE
    public void atualizarProduto(CadProduto produto) {
        try {
            String sql = "UPDATE produto SET nome = ?, tipo = ?, quantidade = ?, preco = ? WHERE id = ?";
            
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getTipo());
            stmt.setInt(3, Integer.parseInt(produto.getQuantidade()));
            stmt.setDouble(4, Double.parseDouble(produto.getPreco()));
            stmt.setInt(5, produto.getID());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar produto: " + erro.getMessage());
        }
    }
public void baixarEstoque(int idProduto, int quantidade) {

    String sql = """
        UPDATE produto
        SET quantidade = quantidade - ?
        WHERE id = ?
    """;

    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, quantidade);
        stmt.setInt(2, idProduto);
        stmt.executeUpdate();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Erro ao atualizar estoque do produto");
    }
}

    // DELETE
    public void excluirProduto(int id) {
        try {
            String sql = "DELETE FROM produto WHERE id = ?";
            
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir produto: " + erro.getMessage());
        }
    }
}
