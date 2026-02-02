package Visao;

import Controle.CadUsuarioDAO;
import Modelo.CadUsuario;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormCadUsuario extends JFrame {

    private JTextField txtId, txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnCadastrar, btnAtualizar, btnExcluir, btnLimpar, btnVoltar;
    private JTable tabela;

    private CadUsuarioDAO usuarioDAO = new CadUsuarioDAO();

    public FormCadUsuario() {
        initComponents();
        listarUsuarios();
    }

    private void initComponents() {

        JLabel lblId = new JLabel("ID:");
        JLabel lblUsuario = new JLabel("Usuário:");
        JLabel lblSenha = new JLabel("Senha:");

        txtId = new JTextField();
        txtUsuario = new JTextField();
        txtSenha = new JPasswordField();

        txtId.setEditable(false);

        btnCadastrar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        btnVoltar = new JButton("Voltar");

        tabela = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Usuário", "Senha"}
        ));

        JScrollPane scroll = new JScrollPane(tabela);

        //  AÇÕES 
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

        setTitle("Cadastro de Usuário");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup().addGap(20)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(lblId)
                        .addComponent(lblUsuario)
                        .addComponent(lblSenha))
                    .addGap(10)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(txtId, 60, 60, 60)
                        .addComponent(txtUsuario, 200, 200, 200)
                        .addComponent(txtSenha))
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
                    .addComponent(lblUsuario).addComponent(txtUsuario).addComponent(btnAtualizar))
                .addGap(8)
                .addGroup(layout.createParallelGroup()
                    .addComponent(lblSenha).addComponent(txtSenha).addComponent(btnExcluir))
                .addGap(8)
                .addComponent(btnLimpar)
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

        if (txtUsuario.getText().isEmpty() || txtSenha.getPassword().length == 0) {
            JOptionPane.showMessageDialog(null, "Preencha usuário e senha!");
            return;
        }

        CadUsuario u = new CadUsuario();
        u.setUsuario(txtUsuario.getText());
        u.setSenha(new String(txtSenha.getPassword()));

        usuarioDAO.cadastrarUsuario(u);
        listarUsuarios();
        limpar();
    }

    private void atualizar() {

        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário!");
            return;
        }

        CadUsuario u = new CadUsuario();
        u.setId(Integer.parseInt(txtId.getText()));
        u.setUsuario(txtUsuario.getText());
        u.setSenha(new String(txtSenha.getPassword()));

        usuarioDAO.atualizarUsuario(u);
        listarUsuarios();
        limpar();
    }

    private void excluir() {

        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário!");
            return;
        }

        usuarioDAO.excluirUsuario(Integer.parseInt(txtId.getText()));
        listarUsuarios();
        limpar();
    }

    private void listarUsuarios() {
        List<CadUsuario> lista = usuarioDAO.listarUsuarios();
        DefaultTableModel m = (DefaultTableModel) tabela.getModel();
        m.setRowCount(0);

        lista.forEach(u -> {
            m.addRow(new Object[]{
                u.getId(),
                u.getUsuario(),
                u.getSenha()
            });
        });
    }

    private void carregarCampos() {
        int l = tabela.getSelectedRow();
        txtId.setText(tabela.getValueAt(l, 0).toString());
        txtUsuario.setText(tabela.getValueAt(l, 1).toString());
        txtSenha.setText(tabela.getValueAt(l, 2).toString());
    }

    private void limpar() {
        txtId.setText("");
        txtUsuario.setText("");
        txtSenha.setText("");
    }

    private void voltar() {
        new FormTelaSelecao().setVisible(true);
        this.dispose();
    }
}
