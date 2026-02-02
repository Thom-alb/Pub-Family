package Visao;

import Controle.*;
import Modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormCadPedido extends JFrame {

    //  COMPONENTES 
    private JTable tblMesas, tblProdutos, tblPedido;
    private JTextField txtQtd;

    private JButton btnAdicionar, btnAtualizar, btnFinalizar, btnVoltar;


    private CadMesaDAO mesaDAO = new CadMesaDAO();
    private CadProdutoDAO produtoDAO = new CadProdutoDAO();
    private CadPedidoDAO pedidoDAO = new CadPedidoDAO();

    //  ESTADO 
    private int idPedidoAtual = -1;
    private int idMesaSelecionada = -1;



    public FormCadPedido() {
        initComponents();
        carregarMesas();
        carregarProdutos();
    }



    private void initComponents() {

        setTitle("Cadastro de Pedido");
        setSize(1200, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //  TABELA MESAS 
        tblMesas = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mesa", "Clientes"}
        ));

        //  TABELA PRODUTOS 
        tblProdutos = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Produto", "Tipo", "Qtd", "Preço"}
        ));

        //  TABELA PEDIDO 
        tblPedido = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Produto", "Qtd", "Preço", "Subtotal"}
        ));

        txtQtd = new JTextField(5);

        btnAdicionar = new JButton("Adicionar Produto");
        btnAtualizar = new JButton("Atualizar Pedido");
        btnFinalizar = new JButton("Finalizar Pedido");
        btnVoltar = new JButton("Voltar");

      
        tblMesas.getSelectionModel().addListSelectionListener(e -> selecionarMesa());
        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnAtualizar.addActionListener(e -> atualizarPedido());
        btnFinalizar.addActionListener(e -> finalizarPedido());
        btnVoltar.addActionListener(e -> voltar());

      
        JPanel centro = new JPanel(new GridLayout(1, 3, 10, 10));
        centro.add(new JScrollPane(tblMesas));
        centro.add(new JScrollPane(tblProdutos));
        centro.add(new JScrollPane(tblPedido));

        JPanel rodape = new JPanel();
        rodape.add(new JLabel("Quantidade:"));
        rodape.add(txtQtd);
        rodape.add(btnAdicionar);
        rodape.add(btnAtualizar);
        rodape.add(btnFinalizar);
        rodape.add(btnVoltar);

        add(centro, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }



    private void carregarMesas() {

        DefaultTableModel m = (DefaultTableModel) tblMesas.getModel();
        m.setRowCount(0);

        mesaDAO.listarMesasTabela().forEach(mesa -> {

            String clientes = mesaDAO.listarClientesMesaTexto(mesa.getID());

            m.addRow(new Object[]{
                mesa.getID(),
                clientes.isEmpty() ? "Sem clientes" : clientes
            });
        });
    }



    private void selecionarMesa() {
        int l = tblMesas.getSelectedRow();
        if (l == -1) return;

        idMesaSelecionada = Integer.parseInt(
                tblMesas.getValueAt(l, 0).toString()
        );

       
        idPedidoAtual = -1;
        limparPedido();
    }

   

    private void carregarProdutos() {
        DefaultTableModel m = (DefaultTableModel) tblProdutos.getModel();
        m.setRowCount(0);

        produtoDAO.listarProdutos().forEach(p ->
            m.addRow(new Object[]{
                p.getID(),
                p.getNome(),
                p.getTipo(),
                p.getQuantidade(),
                p.getPreco()
            })
        );
    }

   

    private void criarPedidoSeNecessario() {

        if (idPedidoAtual != -1) return;

        if (idMesaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma mesa.");
            return;
        }

        List<CadUsuario> clientes = mesaDAO.listarClientesMesa(idMesaSelecionada);
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mesa sem clientes.");
            return;
        }

        CadPedido pedido = new CadPedido();
        pedido.setIdMesa(idMesaSelecionada);
        pedido.setIdCliente(clientes.get(0).getId()); // primeiro cliente

        idPedidoAtual = pedidoDAO.criarPedido(pedido);
    }

    private void adicionarProduto() {

        if (txtQtd.getText().isEmpty()) return;

        int l = tblProdutos.getSelectedRow();
        if (l == -1) return;

        criarPedidoSeNecessario();
        if (idPedidoAtual == -1) return;

        int idProduto = Integer.parseInt(tblProdutos.getValueAt(l, 0).toString());
        int qtd = Integer.parseInt(txtQtd.getText());
        double preco = Double.parseDouble(tblProdutos.getValueAt(l, 4).toString());

        PedidoItem item = new PedidoItem();
        item.setIdProduto(idProduto);
        item.setQuantidade(qtd);
        item.setSubtotal(qtd * preco);

        pedidoDAO.adicionarItem(idPedidoAtual, item);
        atualizarPedido();
    }

    private void atualizarPedido() {
        if (idPedidoAtual == -1) return;

        DefaultTableModel m = (DefaultTableModel) tblPedido.getModel();
        m.setRowCount(0);

        pedidoDAO.listarItensPedidoTabela(idPedidoAtual)
                .forEach(m::addRow);
    }
    private void limparPedido() {

        // Limpa tabela de itens do pedido
        DefaultTableModel m = (DefaultTableModel) tblPedido.getModel();
        m.setRowCount(0);

        // Limpa campo de quantidade
        txtQtd.setText("");

        // Reseta estado do pedido
        idPedidoAtual = -1;
    }

    private void finalizarPedido() {

        if (idPedidoAtual == -1) return;


        List<PedidoItem> itens = pedidoDAO.listarItensPedido(idPedidoAtual);
        for (PedidoItem item : itens) {
            produtoDAO.baixarEstoque(item.getIdProduto(), item.getQuantidade());
        }


        pedidoDAO.finalizarPedido(idPedidoAtual);


        carregarProdutos();


        limparPedido();
        idPedidoAtual = -1;
    }




    private void voltar() {
        new FormTelaSelecao().setVisible(true);
        dispose();
    }
}
