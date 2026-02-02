package Visao;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.UIManager;


public class FormTelaSelecao extends JFrame {

    private JButton btnCadUsuario;
    private JButton btnCadMesa;
    private JButton btnCadProduto;
    private JButton btnFazerPedido;
    private JButton btnEstatisticas;
    private JButton btnSelecionarMesaUsuario;

   
    public FormTelaSelecao() {
        initComponents();
        setLocationRelativeTo(null); 
    }

   
    @SuppressWarnings("unchecked")
    private void initComponents() {

        btnCadUsuario   = new JButton("Cadastrar Usuário");
        btnCadMesa      = new JButton("Cadastrar Mesa");
        btnCadProduto   = new JButton("Cadastrar Produto");
        btnFazerPedido  = new JButton("Realizar Pedido");
        btnEstatisticas = new JButton("Estatísticas");
        btnSelecionarMesaUsuario = new JButton("Selecionar mesa de usuário");

        btnSelecionarMesaUsuario.addActionListener(e -> {
            new FormMesaUsuario().setVisible(true);
            this.dispose();
        });

        
        btnCadProduto.addActionListener(e -> {
            new FormCadProduto().setVisible(true);
            this.dispose();
        });
        btnCadUsuario.addActionListener(e -> {
            new FormCadUsuario().setVisible(true);
            this.dispose();
        });
           btnCadMesa.addActionListener(e -> {
            new FormCadMesa().setVisible(true);
            this.dispose();
        });
           
        btnFazerPedido.addActionListener(e -> {
            new FormCadPedido().setVisible(true);
            this.dispose();
        });
        
        btnEstatisticas.addActionListener(e -> {
            new FormRelatorioPedidos().setVisible(true);
            this.dispose();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tela de Seleção");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGap(120)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnCadUsuario,   GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                        .addComponent(btnCadMesa)
                        .addComponent(btnCadProduto)
                        .addComponent(btnSelecionarMesaUsuario)
                        .addComponent(btnFazerPedido)
                        .addComponent(btnEstatisticas))
                    .addGap(120))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(30)
                .addComponent(btnCadUsuario)
                .addGap(15)
                .addComponent(btnCadMesa)
                .addGap(15)
                .addComponent(btnCadProduto)
                .addGap(15)
                .addComponent(btnSelecionarMesaUsuario)
                .addGap(15)
                .addComponent(btnFazerPedido)
                .addGap(15)
                .addComponent(btnEstatisticas)
                .addGap(30)
        );

        pack();
    }

   
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> {
            new FormTelaSelecao().setVisible(true);
        });
    }
}
