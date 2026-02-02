package Controle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexaoDAO {

    private static final String URL =
        "jdbc:mysql://localhost:3306/PUB?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public Connection conectaBancoDados() {
        try {
            
            Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
            return conexao;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao conectar ao banco:\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
