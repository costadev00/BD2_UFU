package com.oracle.tutorial.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyQueries3 {

    Connection con;
    JDBCUtilities settings;

    public MyQueries3(Connection connArg, JDBCUtilities settingsArg) {
        this.con = connArg;
        this.settings = settingsArg;
    }

    public static void getMyData(Connection con) throws SQLException {
        Statement stmt = null;
        String query = "select c.nome_cliente as nome " +
                "from cliente c " +
                "where c.nome_cliente in (select d.nome_cliente from deposito d) " +
                "and c.nome_cliente in (select e.nome_cliente  from emprestimo e) ";

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Clientes");
            while (rs.next()) {
                String nome = rs.getString("nome");

                System.out.println(" Nome: " + nome);
            }
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }


    public static void main(String[] args) {
        JDBCUtilities myJDBCUtilities;
        Connection myConnection = null;
        if (args[0] == null) {
            System.err.println("Properties file not specified at command line");
            return;
        } else {
            try {
                myJDBCUtilities = new JDBCUtilities(args[0]);
            } catch (Exception e) {
                System.err.println("Problem reading properties file " + args[0]);
                e.printStackTrace();
                return;
            }
        }

        try {
            myConnection = myJDBCUtilities.getConnection();

            MyQueries3.getMyData(myConnection);

        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            JDBCUtilities.closeConnection(myConnection);
        }

    }
}
