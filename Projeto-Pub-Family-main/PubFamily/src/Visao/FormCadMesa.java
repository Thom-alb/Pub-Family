package Visao;

import Controle.CadMesaDAO;
import Modelo.CadMesa;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormCadMesa extends JFrame {

    CadMesaDAO mesaDAO = new CadMesaDAO();

    public FormCadMesa() {
        initComponents();
        listarMesas();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        JLabel lblId = new JLabel("ID:");
        JLabel lblAssentos = new JLabel("Assentos:");
        JLabel lblCheia = new JLabel("Cheia:");

        txtId = new JTextField();
        txtAssentos = new JTextField();
        txtCheia = new JTextField();

        txtId.setEditable(false);
        txtCheia.setEditable(false);

        btnCadastrar = new JButton("Cadastrar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        btnVoltar = new JButton("Voltar");

        tabela = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Assentos", "Cheia", "Clientes"}
        ));

        JScrollPane scroll = new JScrollPane(tabela);

        // Ações
        btnCadastrar.addActionListener(e -> cadastrar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        btnVoltar.addActionListener(e -> voltar());

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                carregarCampos();
            }
        });

        setTitle("Cadastro de Mesas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup().addGap(20)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblId)
                    .addComponent(lblAssentos)
                    .addComponent(lblCheia))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                    .addComponent(txtId, 60, 60, 60)
                    .addComponent(txtAssentos, 200, 200, 200)
                    .addComponent(txtCheia))
                .addGap(15)
                .addGroup(layout.createParallelGroup()
                    .addComponent(btnCadastrar)
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
                    .addComponent(lblAssentos).addComponent(txtAssentos).addComponent(btnExcluir))
                .addGap(8)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblCheia).addComponent(txtCheia).addComponent(btnLimpar))
                .addGap(8)
                .addComponent(btnVoltar)
                .addGap(15)
                .addComponent(scroll, 220, 220, 220)
        );

        pack();
        setLocationRelativeTo(null);
    }

    //  MÉTODOS 

    private void cadastrar() {
        if (txtAssentos.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Informe a quantidade de assentos!");
            return;
        }

        CadMesa m = new CadMesa();
        m.setAssentos(txtAssentos.getText());

        mesaDAO.cadastrarMesa(m);
        listarMesas();
        limpar();
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione uma mesa!");
            return;
        }

        mesaDAO.excluirMesa(Integer.parseInt(txtId.getText()));
        listarMesas();
        limpar();
    }

    private void listarMesas() {

    DefaultTableModel m = (DefaultTableModel) tabela.getModel();
    m.setRowCount(0);

    List<CadMesa> mesas = mesaDAO.listarMesasTabela();

    for (CadMesa mesa : mesas) {

        String clientes = mesaDAO.listarClientesMesaTexto(mesa.getID());

        m.addRow(new Object[]{
            mesa.getID(),
            mesa.getAssentos(),
            mesa.isCheia(),
            clientes.isEmpty() ? "Sem clientes" : clientes
        });
    }
}


    private void carregarCampos() {
        int l = tabela.getSelectedRow();
        txtId.setText(tabela.getValueAt(l, 0).toString());
        txtAssentos.setText(tabela.getValueAt(l, 1).toString());
        txtCheia.setText(tabela.getValueAt(l, 2).toString());
    }

    private void limpar() {
        txtId.setText("");
        txtAssentos.setText("");
        txtCheia.setText("");
    }

    private void voltar() {
        new FormTelaSelecao().setVisible(true);
        this.dispose();
    }

    //  VARIÁVEIS 
    private JTextField txtId, txtAssentos, txtCheia;
    private JButton btnCadastrar, btnExcluir, btnLimpar, btnVoltar;
    private JTable tabela;
}
