package Visao;

import Controle.CadProdutoDAO;
import Modelo.CadProduto;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormCadProduto extends javax.swing.JFrame {

    CadProdutoDAO produtoDAO = new CadProdutoDAO();

    public FormCadProduto() {
        initComponents();
        listarProdutos();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        JLabel lblId = new JLabel("ID:");
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblTipo = new JLabel("Tipo:");
        JLabel lblQtd = new JLabel("Quantidade:");
        JLabel lblPreco = new JLabel("Preço:");

        txtId = new JTextField();
        txtNome = new JTextField();
        txtTipo = new JTextField();
        txtQuantidade = new JTextField();
        txtPreco = new JTextField();

        txtId.setEditable(false);

        btnCadastrar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        btnVoltar = new JButton("Voltar");

        tabela = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome", "Tipo", "Quantidade", "Preço"}
        ));

        JScrollPane scroll = new JScrollPane(tabela);

        // Ações
        btnCadastrar.addActionListener(e -> cadastrar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        btnVoltar.addActionListener(e -> voltar());

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                carregarCampos();
            }
        });

        setTitle("Cadastro de Produto");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup().addGap(20)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblId)
                    .addComponent(lblNome)
                    .addComponent(lblTipo)
                    .addComponent(lblQtd)
                    .addComponent(lblPreco))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                    .addComponent(txtId, 60, 60, 60)
                    .addComponent(txtNome, 200, 200, 200)
                    .addComponent(txtTipo)
                    .addComponent(txtQuantidade)
                    .addComponent(txtPreco))
                .addGap(15)
                .addGroup(layout.createParallelGroup()
                    .addComponent(btnCadastrar)
                    .addComponent(btnAtualizar)
                    .addComponent(btnExcluir)
                    .addComponent(btnLimpar)
                    .addComponent(btnVoltar))
                .addGap(20))
            .addComponent(scroll)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup().addGap(20)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblId).addComponent(txtId).addComponent(btnCadastrar))
                .addGap(8)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblNome).addComponent(txtNome).addComponent(btnAtualizar))
                .addGap(8)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblTipo).addComponent(txtTipo).addComponent(btnExcluir))
                .addGap(8)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblQtd).addComponent(txtQuantidade).addComponent(btnLimpar))
                .addGap(8)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblPreco).addComponent(txtPreco).addComponent(btnVoltar))
                .addGap(15)
                .addComponent(scroll, 220, 220, 220)
        );

        pack();
        setLocationRelativeTo(null);
    }

    //  MÉTODOS 

    private void cadastrar() {
        
        if (txtPreco.getText().contains(",")) {
            JOptionPane.showMessageDialog(null,
                    "Preço inválido!\nUse ponto (.) ao invés de vírgula (,).");
            return;
        }
        CadProduto p = new CadProduto();
        p.setNome(txtNome.getText());
        p.setTipo(txtTipo.getText());
        p.setQuantidade(txtQuantidade.getText());
        p.setPreco(txtPreco.getText());

        produtoDAO.cadastrarProduto(p);
        listarProdutos();
        limpar();
    }

    private void atualizar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um produto!");
            return;
        }
        if (txtPreco.getText().contains(",")) {
            JOptionPane.showMessageDialog(null,
                    "Preço inválido!\nUse ponto (.) ao invés de vírgula (,).");
            return;
        }
        CadProduto p = new CadProduto();
        p.setID(Integer.parseInt(txtId.getText()));
        p.setNome(txtNome.getText());
        p.setTipo(txtTipo.getText());
        p.setQuantidade(txtQuantidade.getText());
        p.setPreco(txtPreco.getText());

        produtoDAO.atualizarProduto(p);
        listarProdutos();
        limpar();
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um produto!");
            return;
        }

        produtoDAO.excluirProduto(Integer.parseInt(txtId.getText()));
        listarProdutos();
        limpar();
    }

    private void listarProdutos() {
        List<CadProduto> lista = produtoDAO.listarProdutos();
        DefaultTableModel m = (DefaultTableModel) tabela.getModel();
        m.setRowCount(0);

        lista.forEach(p -> {
            m.addRow(new Object[]{
                p.getID(),
                p.getNome(),
                p.getTipo(),
                p.getQuantidade(),
                p.getPreco()
            });
        });
    }

    private void carregarCampos() {
        int l = tabela.getSelectedRow();
        txtId.setText(tabela.getValueAt(l, 0).toString());
        txtNome.setText(tabela.getValueAt(l, 1).toString());
        txtTipo.setText(tabela.getValueAt(l, 2).toString());
        txtQuantidade.setText(tabela.getValueAt(l, 3).toString());
        txtPreco.setText(tabela.getValueAt(l, 4).toString());
    }

    private void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtTipo.setText("");
        txtQuantidade.setText("");
        txtPreco.setText("");
    }

    private void voltar() {
        new FormTelaSelecao().setVisible(true);
        this.dispose();
    }

    //  VARIÁVEIS 
    private JTextField txtId, txtNome, txtTipo, txtQuantidade, txtPreco;
    private JButton btnCadastrar, btnAtualizar, btnExcluir, btnLimpar, btnVoltar;
    private JTable tabela;
}
