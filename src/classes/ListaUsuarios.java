package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListaUsuarios {

	public static void main(String[] args) throws SQLException {
		
		Connection conexao = ConnectionFactory.criarConexao();
		
		String sql = "SELECT * FROM Usuario;";
		
		PreparedStatement comando = conexao.prepareStatement(sql);
		
		System.out.println("Executando comando...");
		ResultSet resultado = comando.executeQuery();
		
		List<Usuario> lista = new ArrayList<Usuario>();
		while(resultado.next()){ 
			Usuario u = new Usuario();
			u.setId(resultado.getLong("id"));
			u.setNome(resultado.getString("nome"));
			u.setEmail(resultado.getString("email"));
			lista.add(u);
		}
	}
}
