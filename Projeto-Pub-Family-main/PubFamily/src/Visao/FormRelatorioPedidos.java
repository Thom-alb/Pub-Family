package Visao;

import Controle.CadPedidoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormRelatorioPedidos extends JFrame {

    private JTable tabela;
    private JLabel lblLucro;
    private JButton btnVoltar;
    private JButton btnExcluir;


    private CadPedidoDAO pedidoDAO = new CadPedidoDAO();

    public FormRelatorioPedidos() {
        initComponents();
        carregarRelatorio();
    }

    private void initComponents() {

        setTitle("Relatório de Pedidos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabela = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Pedido",
                    "Mesa",
                    "Produto",
                    "Quantidade",
                    "Subtotal",
                    "Total do Pedido"
                }
        ));

        JScrollPane scroll = new JScrollPane(tabela);

        lblLucro = new JLabel("Lucro Total: R$ 0.00");
        lblLucro.setFont(new Font("Arial", Font.BOLD, 14));

        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> voltar());
        
        btnExcluir = new JButton("Excluir Pedido");
        btnExcluir.addActionListener(e -> excluirPedido());


        JPanel rodape = new JPanel(new BorderLayout());
        JPanel botoes = new JPanel();
        botoes.add(btnExcluir);
        botoes.add(btnVoltar);

        rodape.add(lblLucro, BorderLayout.WEST);
        rodape.add(botoes, BorderLayout.EAST);


        add(scroll, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }
private void excluirPedido() {

    int linha = tabela.getSelectedRow();

    if (linha == -1) {
        JOptionPane.showMessageDialog(this,
                "Selecione um pedido na tabela para excluir.");
        return;
    }

    int idPedido = (int) tabela.getValueAt(linha, 0);

    int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir o pedido #" + idPedido + "?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
    );

    if (confirmacao == JOptionPane.YES_OPTION) {
        pedidoDAO.excluirPedido(idPedido);
        carregarRelatorio(); 
    }
}

    private void carregarRelatorio() {

        DefaultTableModel m = (DefaultTableModel) tabela.getModel();
        m.setRowCount(0);

        pedidoDAO.listarRelatorioPedidos()
                .forEach(m::addRow);

        double lucro = pedidoDAO.calcularLucroTotal();
        lblLucro.setText("Lucro Total: R$ " + String.format("%.2f", lucro));
    }

    private void voltar() {
        new FormTelaSelecao().setVisible(true);
        dispose();
    }
}
