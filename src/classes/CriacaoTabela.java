package classes;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CriacaoTabela {

	public static void main(String[] args) throws SQLException {
		
		String stringDeConexao = "jdbc:mysql://localhost:3306/Trabalho3B";
		String usuario = "root";
		String senha = "ifrnjc";
		
		System.out.println("Abrindo conexão...");
		Connection conexao = DriverManager.getConnection(stringDeConexao, usuario, senha);
		
		System.out.println("Criando a tabela Usuario...");
		
		String sql = "CREATE TABLE usuario ("+"id BIGINT NOT NULL AUTO_INCREMENT," + 
					"nome VARCHAR (30) NOT NULL," +
					"email VARCHAR (255) NOT NULL," +
					"PRIMARY KEY (id)" +
					")" +
					"ENGINE = InnoDB";
		
		PreparedStatement comando = conexao.prepareStatement(sql);
		comando.execute();
		comando.close();
		
		System.out.println("Fechando conexão...");
		conexao.close();
	}
	
}
