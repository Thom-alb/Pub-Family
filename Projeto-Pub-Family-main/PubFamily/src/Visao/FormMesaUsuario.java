package Visao;

import Controle.CadMesaDAO;
import Controle.CadUsuarioDAO;
import Modelo.CadMesa;
import Modelo.CadUsuario;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormMesaUsuario extends JFrame {

  
    private JLabel lblMesas;
    private JLabel lblUsuarios;
    private JLabel lblClientesMesa;
    private JLabel lblIdMesa;
    private JLabel lblIdCliente;
    private JLabel lblNomeCliente;


    private JTable tabelaMesas;
    private JTable tabelaUsuarios;       
    private JTable tabelaClientesMesa;   

 
    private JTextField txtIdMesa;
    private JTextField txtIdUsuario;
    private JTextField txtNomeUsuario;


    private JButton btnAdicionar;
    private JButton btnRemover;
    private JButton btnVoltar;

  
    private CadMesaDAO mesaDAO = new CadMesaDAO();
    private CadUsuarioDAO usuarioDAO = new CadUsuarioDAO();



    public FormMesaUsuario() {
        initComponents();
        listarMesas();
        listarUsuarios();
    }

 

    private void initComponents() {

 
        lblMesas = new JLabel("Mesas");
        lblUsuarios = new JLabel("Usuários do Sistema");
        lblClientesMesa = new JLabel("Clientes da Mesa");

        lblIdMesa = new JLabel("ID Mesa");
        lblIdCliente = new JLabel("ID Cliente");
        lblNomeCliente = new JLabel("Nome Cliente");

  
        txtIdMesa = new JTextField(6);
        txtIdUsuario = new JTextField(6);
        txtNomeUsuario = new JTextField(15);

        txtIdMesa.setEditable(false);
        txtIdUsuario.setEditable(false);
        txtNomeUsuario.setEditable(false);

     
        btnAdicionar = new JButton("Adicionar à Mesa");
        btnRemover = new JButton("Remover da Mesa");
        btnVoltar = new JButton("Voltar");

    
        tabelaMesas = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Assentos", "Cheia"}
        ));

   
        tabelaUsuarios = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome"}
        ));

 
        tabelaClientesMesa = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome"}
        ));

        JScrollPane scrollMesas = new JScrollPane(tabelaMesas);
        JScrollPane scrollUsuarios = new JScrollPane(tabelaUsuarios);
        JScrollPane scrollClientesMesa = new JScrollPane(tabelaClientesMesa);

        //  AÇÕES

        tabelaMesas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selecionarMesa();
            }
        });

        tabelaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selecionarUsuarioSistema();
            }
        });

        tabelaClientesMesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selecionarClienteMesa();
            }
        });

        btnAdicionar.addActionListener(e -> adicionarUsuarioMesa());
        btnRemover.addActionListener(e -> removerUsuarioMesa());
        btnVoltar.addActionListener(e -> voltar());

        setTitle("Gerenciar Mesas e Usuários");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

    
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createSequentialGroup().addGap(15)

                .addGroup(layout.createParallelGroup()
                    .addComponent(lblMesas)
                    .addComponent(scrollMesas, 230, 230, 230))

                .addGap(15)

                .addGroup(layout.createParallelGroup()
                    .addComponent(lblClientesMesa)
                    .addComponent(scrollClientesMesa, 230, 230, 230))

                .addGap(15)

                .addGroup(layout.createParallelGroup()
                    .addComponent(lblUsuarios)
                    .addComponent(scrollUsuarios, 230, 230, 230)
                    .addGap(10)
                    .addComponent(lblIdMesa)
                    .addComponent(txtIdMesa)
                    .addComponent(lblIdCliente)
                    .addComponent(txtIdUsuario)
                    .addComponent(lblNomeCliente)
                    .addComponent(txtNomeUsuario)
                    .addGap(10)
                    .addComponent(btnAdicionar)
                    .addComponent(btnRemover)
                    .addComponent(btnVoltar))

                .addGap(15)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup().addGap(15)

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMesas)
                    .addComponent(lblClientesMesa)
                    .addComponent(lblUsuarios))

                .addGap(5)

                .addGroup(layout.createParallelGroup()
                    .addComponent(scrollMesas, 250, 250, 250)
                    .addComponent(scrollClientesMesa, 250, 250, 250)
                    .addComponent(scrollUsuarios, 250, 250, 250))

                .addGap(10)

                .addComponent(lblIdMesa)
                .addComponent(txtIdMesa)
                .addComponent(lblIdCliente)
                .addComponent(txtIdUsuario)
                .addComponent(lblNomeCliente)
                .addComponent(txtNomeUsuario)

                .addGap(10)

                .addComponent(btnAdicionar)
                .addComponent(btnRemover)
                .addComponent(btnVoltar)

                .addGap(15)
        );

        pack();
        setLocationRelativeTo(null);
    }


    // MÉTODOS


    private void listarMesas() {
        DefaultTableModel m = (DefaultTableModel) tabelaMesas.getModel();
        m.setRowCount(0);

        List<CadMesa> lista = mesaDAO.listarMesasTabela();
        lista.forEach(me ->
            m.addRow(new Object[]{
                me.getID(),
                me.getAssentos(),
                me.isCheia() ? "Sim" : "Não"
            })
        );
    }

    private void listarUsuarios() {
        DefaultTableModel m = (DefaultTableModel) tabelaUsuarios.getModel();
        m.setRowCount(0);

        usuarioDAO.listarUsuarios().forEach(u ->
            m.addRow(new Object[]{u.getId(), u.getUsuario()})
        );
    }

    private void listarClientesMesaSelecionada() {
        if (txtIdMesa.getText().isEmpty()) return;

        int idMesa = Integer.parseInt(txtIdMesa.getText());
        DefaultTableModel m = (DefaultTableModel) tabelaClientesMesa.getModel();
        m.setRowCount(0);

        mesaDAO.listarClientesMesa(idMesa).forEach(u ->
            m.addRow(new Object[]{u.getId(), u.getUsuario()})
        );
    }

    private void selecionarMesa() {
        int l = tabelaMesas.getSelectedRow();
        if (l == -1) return;

        txtIdMesa.setText(tabelaMesas.getValueAt(l, 0).toString());
        listarClientesMesaSelecionada();
    }

    private void selecionarUsuarioSistema() {
        int l = tabelaUsuarios.getSelectedRow();
        if (l == -1) return;

        txtIdUsuario.setText(tabelaUsuarios.getValueAt(l, 0).toString());
        txtNomeUsuario.setText(tabelaUsuarios.getValueAt(l, 1).toString());
    }

    private void selecionarClienteMesa() {
        int l = tabelaClientesMesa.getSelectedRow();
        if (l == -1) return;

        txtIdUsuario.setText(tabelaClientesMesa.getValueAt(l, 0).toString());
        txtNomeUsuario.setText(tabelaClientesMesa.getValueAt(l, 1).toString());
    }

    private void adicionarUsuarioMesa() {

        if (txtIdMesa.getText().isEmpty() || txtIdUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma mesa e um usuário.");
            return;
        }
        

        int idMesa = Integer.parseInt(txtIdMesa.getText());
        int idUsuario = Integer.parseInt(txtIdUsuario.getText());

           if (mesaDAO.clienteJaNaMesa(idMesa, idUsuario)) {
               JOptionPane.showMessageDialog(this, "Este cliente já está nesta mesa.");
               return;
           }

           if (mesaDAO.clienteJaEmOutraMesa(idUsuario)) {
               JOptionPane.showMessageDialog(this,
                       "Este cliente já está vinculado a outra mesa.\nRemova-o da mesa atual antes.");
               return;
           }


        if (mesaDAO.clienteJaNaMesa(idMesa, idUsuario)) {
            JOptionPane.showMessageDialog(this, "Este cliente já está nesta mesa.");
            return;
        }

        if (!mesaDAO.mesaTemVaga(idMesa)) {
            JOptionPane.showMessageDialog(this, "Esta mesa já está cheia.");
            return;
        }

        mesaDAO.adicionarClienteMesa(idMesa, idUsuario);

        listarClientesMesaSelecionada();
        listarMesas();
    }

    private void removerUsuarioMesa() {

        if (txtIdMesa.getText().isEmpty() || txtIdUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma mesa e um cliente da mesa.");
            return;
        }

        int idMesa = Integer.parseInt(txtIdMesa.getText());
        int idCliente = Integer.parseInt(txtIdUsuario.getText());

        mesaDAO.removerClienteMesa(idMesa, idCliente);

        listarClientesMesaSelecionada();
        listarMesas();
    }

    private void voltar() {
        new FormTelaSelecao().setVisible(true);
        dispose();
    }
}
