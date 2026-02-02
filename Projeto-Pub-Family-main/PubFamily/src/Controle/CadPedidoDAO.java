package Controle;

import Modelo.CadPedido;
import Modelo.PedidoItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CadPedidoDAO {

    private Connection conexao;

    public CadPedidoDAO() {
        conexao = new ConexaoDAO().conectaBancoDados();
    }

    //CREATE 

    public int criarPedido(CadPedido pedido) {

        int idPedidoGerado = -1;

        String sql = """
            INSERT INTO pedido (id_mesa, id_cliente, preco_total)
            VALUES (?, ?, 0)
        """;

        try (PreparedStatement stmt =
                     conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pedido.getIdMesa());
            stmt.setInt(2, pedido.getIdCliente());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idPedidoGerado = rs.getInt(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao criar pedido: " + e.getMessage());
        }

        return idPedidoGerado;
    }

    public void adicionarItem(int idPedido, PedidoItem item) {

        String sql = """
            INSERT INTO pedido_itens
            (id_pedido, id_produto, quantidade, subtotal)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            stmt.setInt(2, item.getIdProduto());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getSubtotal());

            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao adicionar item ao pedido: " + e.getMessage());
        }
    }

    public void finalizarPedido(int idPedido) {

        String sql = """
            UPDATE pedido
            SET preco_total = (
                SELECT SUM(subtotal)
                FROM pedido_itens
                WHERE id_pedido = ?
            )
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            stmt.setInt(2, idPedido);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    null,
                    "Pedido finalizado com sucesso!\nConfira as estatísticas para ver os pedidos realizados"
            );

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao finalizar pedido: " + e.getMessage());
        }
    }

    //READ 

    public List<CadPedido> listarPedidos() {

        List<CadPedido> pedidos = new ArrayList<>();

        String sql = """
            SELECT 
                p.id AS pedido_id,
                p.id_mesa,
                p.id_cliente,
                p.preco_total,
                c.nome AS cliente_pedido
            FROM pedido p
            JOIN cliente c ON p.id_cliente = c.id
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                CadPedido pedido = new CadPedido();
                pedido.setId(rs.getInt("pedido_id"));
                pedido.setIdMesa(rs.getInt("id_mesa"));
                pedido.setIdCliente(rs.getInt("id_cliente"));
                pedido.setPrecoTotal(rs.getDouble("preco_total"));

                pedido.setItens(buscarItensDoPedido(pedido.getId()));

                List<String> clientesMesa =
                        buscarClientesDaMesa(pedido.getIdMesa());

                StringBuilder info = new StringBuilder();
                info.append("Pedido #").append(pedido.getId()).append("\n");
                info.append("Mesa: ").append(pedido.getIdMesa()).append("\n");
                info.append("Cliente que pediu: ")
                    .append(rs.getString("cliente_pedido")).append("\n");
                info.append("Clientes na mesa: ")
                    .append(clientesMesa).append("\n");
                info.append("Total: R$ ")
                    .append(pedido.getPrecoTotal()).append("\n");
                info.append("Itens:\n");

                for (PedidoItem item : pedido.getItens()) {
                    info.append("- Produto ID: ")
                        .append(item.getIdProduto())
                        .append(" | Qtd: ")
                        .append(item.getQuantidade())
                        .append(" | Subtotal: R$ ")
                        .append(item.getSubtotal())
                        .append("\n");
                }

                JOptionPane.showMessageDialog(null, info.toString());
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao listar pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    public List<Object[]> listarRelatorioPedidos() {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT
                p.id AS pedido,
                p.id_mesa,
                pr.nome AS produto,
                pi.quantidade,
                pi.subtotal,
                p.preco_total
            FROM pedido p
            JOIN pedido_itens pi ON p.id = pi.id_pedido
            JOIN produto pr ON pi.id_produto = pr.id
            ORDER BY p.id
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("pedido"),
                    rs.getInt("id_mesa"),
                    rs.getString("produto"),
                    rs.getInt("quantidade"),
                    rs.getDouble("subtotal"),
                    rs.getDouble("preco_total")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao listar relatório de pedidos:\n" + e.getMessage()
            );
        }

        return lista;
    }

    public double calcularLucroTotal() {

        String sql = "SELECT SUM(preco_total) AS total FROM pedido";
        double total = 0;

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao calcular lucro total");
        }

        return total;
    }

    public List<Object[]> listarItensPedidoTabela(int idPedido) {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT 
                p.nome AS produto,
                pi.quantidade,
                p.preco,
                pi.subtotal
            FROM pedido_itens pi
            JOIN produto p ON pi.id_produto = p.id
            WHERE pi.id_pedido = ?
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("produto"),
                    rs.getInt("quantidade"),
                    rs.getDouble("preco"),
                    rs.getDouble("subtotal")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao listar itens do pedido: " + e.getMessage());
        }

        return lista;
    }

    public List<PedidoItem> listarItensPedido(int idPedido) {

        List<PedidoItem> lista = new ArrayList<>();

        String sql = """
            SELECT id_produto, quantidade
            FROM pedido_itens
            WHERE id_pedido = ?
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PedidoItem item = new PedidoItem();
                item.setIdProduto(rs.getInt("id_produto"));
                item.setQuantidade(rs.getInt("quantidade"));
                lista.add(item);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao listar itens do pedido");
        }

        return lista;
    }

    //DELETE 

    public void excluirPedido(int idPedido) {

        try {
            conexao.setAutoCommit(false);

            PreparedStatement stmtItens =
                    conexao.prepareStatement(
                            "DELETE FROM pedido_itens WHERE id_pedido = ?"
                    );
            stmtItens.setInt(1, idPedido);
            stmtItens.executeUpdate();

            PreparedStatement stmtPedido =
                    conexao.prepareStatement(
                            "DELETE FROM pedido WHERE id = ?"
                    );
            stmtPedido.setInt(1, idPedido);
            stmtPedido.executeUpdate();

            conexao.commit();
            JOptionPane.showMessageDialog(null,
                    "Pedido excluído com sucesso!");

        } catch (SQLException e) {
            try { conexao.rollback(); } catch (SQLException ignored) {}

            JOptionPane.showMessageDialog(null,
                    "Erro ao excluir pedido: " + e.getMessage());
        }
    }

    //MÉTODOS AUXILIARES 

    private List<String> buscarClientesDaMesa(int idMesa) {

        List<String> clientes = new ArrayList<>();

        String sql = """
            SELECT c.nome
            FROM mesa_cliente mc
            JOIN cliente c ON mc.id_cliente = c.id
            WHERE mc.id_mesa = ?
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idMesa);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clientes.add(rs.getString("nome"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao buscar clientes da mesa: " + e.getMessage());
        }

        return clientes;
    }

    private List<PedidoItem> buscarItensDoPedido(int idPedido) {

        List<PedidoItem> itens = new ArrayList<>();

        String sql = """
            SELECT id_produto, quantidade, subtotal
            FROM pedido_itens
            WHERE id_pedido = ?
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PedidoItem item = new PedidoItem();
                item.setIdProduto(rs.getInt("id_produto"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setSubtotal(rs.getDouble("subtotal"));
                itens.add(item);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao buscar itens do pedido: " + e.getMessage());
        }

        return itens;
    }
}
