package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListaDoadores {
	
public static void main(String[] args) throws SQLException {
		
		Connection conexao = ConnectionFactory.criarConexao();
		
		String sql = "SELECT * FROM Doador;";
		
		PreparedStatement comando = conexao.prepareStatement(sql);
		
		System.out.println("Executando comando...");
		ResultSet resultado = comando.executeQuery();
		
		List<Doador> lista = new ArrayList<Doador>();
		while(resultado.next()){ 
			Doador d = new Doador();
			d.setId(resultado.getLong("id"));
			d.setNome(resultado.getString("nome"));
			d.setValor(resultado.getInt("valor"));
			lista.add(d);
		}
	}
}
