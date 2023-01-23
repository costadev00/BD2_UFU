import java.util.Properties; //Objeto genérico que armazena propriedades com usuário e senha
import java.sql.DriverManager; //Objeto que criará a conexão do sistema de banco de dados
import java.sql.Connection; //Objeto que armazenará o objeto de conexão ao banco de dados
import java.sql.Statement; //Objeto para disparar um comando para o SGBD
import java.sql.ResultSet; //Objeto que armazenará as tuplas resultantes de um comando SQL
import java.sql.SQLException; //Objeto para capturar eventos de erro no acesso ao banco de dados

public class StandAloneJDBCCode {
	public static Connection getConnection(){
	//code
		Connection con = null;
		String currentUrlString = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", "postgres");
		connectionProps.put("password", "1234");
		currentUrlString = "jdbc:postgresql://localhost:5432/IB2";
		//atenção para os símbolos de barra "/" na linha acima, não confunda com a letra l
		try {
			con = DriverManager.getConnection(currentUrlString, connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;


}
	public static void myquery(Connection con) throws SQLException {
	//code myQueries3
		Statement stmt = null;
    		String query =
    				"select cliente.nome_cliente, sum(deposito.saldo_deposito) as soma_deposito, sum(emprestimo.valor_emprestimo) as soma_emprestimo from cliente inner join deposito  on cliente.nome_cliente = deposito.nome_cliente left join emprestimo on cliente.nome_cliente = emprestimo.nome_cliente where deposito.nome_cliente is not null and emprestimo.nome_cliente is not null group by cliente.nome_cliente order by cliente.nome_cliente asc";
    		    try {      
		      
		      stmt = con.createStatement(); 
		      ResultSet rs = stmt.executeQuery(query);      
		      while (rs.next()) {
			String coffeeName = rs.getString(1);
			int sumd = rs.getInt(2);
			int sume = rs.getInt(3);
			System.out.println("Nome cliente: " +coffeeName + "Soma Depositos: " + sumd + "Soma Emprestimos: " + sume);       
		      }
		    } catch (SQLException e) { e.printStackTrace(); }
 		      finally { if (stmt != null) { stmt.close(); }
 		    }
}

	public static void closeConnection(Connection con) {
	//code
		
		try {
	 	if (con != null) {
	 	con.close(); con = null;
	 	}
	 	System.out.println("Released all database resources.");
	 	} catch (SQLException e) { e.printStackTrace(); } 
}	
	public static void main(String[] args) {
	//code
		if (args.length == 0) {
		System.err.println("No arguments.");
	 	}
	 	Connection myConnection = null;
	 	try {
		myConnection = StandAloneJDBCCode.getConnection();
		StandAloneJDBCCode.myquery(myConnection);
		} catch (SQLException e) {
		e.printStackTrace();
	 	} finally {
		StandAloneJDBCCode.closeConnection(myConnection);
   }
 } 
}


