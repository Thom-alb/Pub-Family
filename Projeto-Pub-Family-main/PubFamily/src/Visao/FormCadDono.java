package Visao;

import Controle.CadDonoDAO;
import Modelo.CadDono;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormCadDono extends javax.swing.JFrame {

    CadDonoDAO donoDAO = new CadDonoDAO();

    public FormCadDono() {
        initComponents();
        listarDonos();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        txtId = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        txtSenha = new javax.swing.JTextField();

        btnCadastrar = new javax.swing.JButton();
        btnAtualizar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnListar = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();


        
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaGerentes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cadastro de Gerente");
        setResizable(false);

        jLabel1.setText("ID:");
        jLabel2.setText("Nome:");
        jLabel3.setText("Usuário:");
        jLabel4.setText("Senha:");

        txtId.setEditable(false);

        btnCadastrar.setText("Cadastrar");
        btnCadastrar.addActionListener(evt -> cadastrar());

        btnAtualizar.setText("Atualizar");
        btnAtualizar.addActionListener(evt -> atualizar());

        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(evt -> excluir());

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(evt -> limparCampos());

        btnListar.setText("Listar");
        btnListar.addActionListener(evt -> listarDonos());
        
        btnLogin.setText("Login");
        btnLogin.addActionListener(evt -> login());

        tabelaGerentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Nome", "Usuário", "Senha" }
        ));
        tabelaGerentes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                carregarCampos();
            }
        });

        jScrollPane1.setViewportView(tabelaGerentes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNome)
                    .addComponent(txtUsuario)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCadastrar)
                        .addGap(10, 10, 10)
                        .addComponent(btnAtualizar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnExcluir)
                        .addGap(10, 10, 10)
                        .addComponent(btnLimpar))
                        .addGap(10, 10, 10)
                        .addComponent(btnLogin)
                    .addComponent(btnListar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCadastrar)
                    .addComponent(btnAtualizar))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluir)
                    .addComponent(btnLimpar))
                    .addComponent(btnLogin)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnListar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    //MÉTODOS

    private void cadastrar() {
        CadDono d = new CadDono();
        d.setNome(txtNome.getText());
        d.setUsuario(txtUsuario.getText());
        d.setSenha(txtSenha.getText());

        donoDAO.cadastrarDono(d);
        listarDonos();
        limparCampos();
    }

    private void atualizar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um gerente!");
            return;
        }

        CadDono d = new CadDono();
        d.setId(Integer.parseInt(txtId.getText()));
        d.setNome(txtNome.getText());
        d.setUsuario(txtUsuario.getText());
        d.setSenha(txtSenha.getText());

        donoDAO.atualizarDono(d);
        listarDonos();
        limparCampos();
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um gerente!");
            return;
        }

        donoDAO.excluirDono(Integer.parseInt(txtId.getText()));
        listarDonos();
        limparCampos();
    }

    private void listarDonos() {
        List<CadDono> lista = donoDAO.listarDonos();
        DefaultTableModel modelo = (DefaultTableModel) tabelaGerentes.getModel();
        modelo.setRowCount(0);

        lista.forEach(d -> {
            modelo.addRow(new Object[]{
                d.getId(),
                d.getNome(),
                d.getUsuario(),
                d.getSenha()
            });
        });
    }
    
    private void login() {

        if (txtUsuario.getText().isEmpty() || txtSenha.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Informe usuário e senha para login!");
            return;
        }

        boolean autenticado = donoDAO.login(
                txtUsuario.getText(),
                txtSenha.getText()
        );

        if (autenticado) {
            JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");

            // abre a próxima tela
            FormTelaSelecao tela = new FormTelaSelecao();
            tela.setVisible(true);

            // fecha a tela atual
            this.dispose();

        } else {
            JOptionPane.showMessageDialog(null,
                    "Gerente não encontrado!\nUsuário ou senha inválidos.");
        }
    }


    private void carregarCampos() {
        int linha = tabelaGerentes.getSelectedRow();
        txtId.setText(tabelaGerentes.getValueAt(linha, 0).toString());
        txtNome.setText(tabelaGerentes.getValueAt(linha, 1).toString());
        txtUsuario.setText(tabelaGerentes.getValueAt(linha, 2).toString());
        txtSenha.setText(tabelaGerentes.getValueAt(linha, 3).toString());
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtUsuario.setText("");
        txtSenha.setText("");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormCadDono().setVisible(true));
    }

    // VARIÁVEIS 
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnListar;
    private javax.swing.JButton btnLogin;


    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaGerentes;

    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JTextField txtSenha;
}
