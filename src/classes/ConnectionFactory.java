package classes;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public static Connection criarConexao() throws SQLException{
		
		String stringDeConexao = "jdbc:mysql://localhost:3306/Trabalho3B";
		String usuario = "root";
		String senha = "ifrnjc";
		
		System.out.println("Abrindo conexão...");
		Connection conexao = DriverManager.getConnection(stringDeConexao, usuario, senha);
		
		return conexao;
	}
	
	Connection con = null;
	
	public void closeConnection() throws SQLException {
		   
        con.close();
   }
	
}
