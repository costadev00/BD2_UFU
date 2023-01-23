package com.oracle.tutorial.jdbc;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.DatabaseMetaData;


public class MyQueries {

  Connection con;
  JDBCUtilities settings;

  public MyQueries(Connection connArg, JDBCUtilities settingsArg) {
    this.con = connArg;
    this.settings = settingsArg;
  }

  public static void getMyData3(Connection con) throws SQLException {
    Statement stmt = null;
    String query =
            "select dep.nome_cliente, dep.nome_agencia, dep.numero_conta, \n" +
                    "sum(dep.saldo_deposito) as soma_deposito, sum(emp.valor_emprestimo) as soma_emprestimo\n" +
                    "FROM deposito AS dep left Join emprestimo AS emp \n" +
                    "ON dep.nome_cliente = emp.nome_cliente AND dep.nome_agencia = emp.nome_agencia AND dep.numero_conta = emp.numero_conta\n" +
                    "WHERE dep.saldo_deposito > 0 and emp.valor_emprestimo > 0\n" +
                    "GROUP BY dep.nome_cliente, dep.nome_agencia, dep.numero_conta";

    try {
      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      System.out.println("Nome Cliente -- Nome Agência -- Número Conta -- Soma Depósitos -- Soma Empréstimos");
      while (rs.next()) {
//      1 index
        String clientName = rs.getString(1);
        String nomeAgencia = rs.getString(2);
        int numConta = rs.getInt(3);
        float sumDep = rs.getFloat(4);
        float sumEmp = rs.getFloat(5);
//        2 alias
//        String clientName = rs.getString("nome");
//        String nomeAgencia = rs.getString("agencia");
//        String conta = rs.getString("conta");
//        float sumDep = rs.getFloat("soma_deposito");
//        float sumEmp = rs.getFloat("soma_emprestimo");
//
//        3 column_name
//
//        String clientName = rs.getString("nome_cliente");
//        String nomeAgencia = rs.getString("nome_agencia");
//        String conta = rs.getString("numero_conta");
//        float sumDep = rs.getFloat("soma_deposito");
//        float sumEmp = rs.getFloat("soma_emprestimo");
//
        System.out.println(clientName + " - " + nomeAgencia + "-" + numConta + "-" + (float) sumDep + " - " + (float) sumEmp);
      }
    } catch (SQLException e) {
      JDBCUtilities.printSQLException(e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  public static void cursorHoldabilitySupport(Connection con)
          throws SQLException {
    DatabaseMetaData dbMetaData = con.getMetaData();
    System.out.println("ResultSet.HOLD_CURSORS_OVER_COMMIT = " +
            ResultSet.HOLD_CURSORS_OVER_COMMIT);
    System.out.println("ResultSet.CLOSE_CURSORS_AT_COMMIT = " +
            ResultSet.CLOSE_CURSORS_AT_COMMIT);
    System.out.println("Default cursor holdability: " +
            dbMetaData.getResultSetHoldability());
    System.out.println("Supports HOLD_CURSORS_OVER_COMMIT? " +
            dbMetaData.supportsResultSetHoldability(
                    ResultSet.HOLD_CURSORS_OVER_COMMIT));
    System.out.println("Supports ResultSet CONCUR_READ_ONLY? " +
            dbMetaData.supportsResultSetConcurrency(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));
    System.out.println("Supports ResultSet CONCUR_UPDATABLE? " +
            dbMetaData.supportsResultSetConcurrency(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE));
  }
  public static void modifyPrices(Connection conn, float rate, boolean custom) throws SQLException {
    Statement stmt = null;
    rate = custom ? rate : 0.5f;
    try {
      stmt = conn.createStatement();
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
      ResultSet uprs = stmt.executeQuery("SELECT * FROM DEPOSITO");
      while (uprs.next()) {
        float ratee = uprs.getFloat("SALDO_DEPOSITO");
        uprs.updateFloat("SALDO_DEPOSITO", ratee + rate);
        uprs.updateRow();
      }
    } catch (SQLException e ) {
      JDBCTutorialUtilities.printSQLException(e);
    } finally {
      if (stmt != null) { stmt.close(); }
    }
  }

  public static void insertRow(Connection con) throws SQLException {
    Statement stmt = null;
    try {
      stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
      ResultSet uprs = stmt.executeQuery("SELECT * FROM debito");
      uprs.moveToInsertRow();
      uprs.updateInt(1, 2000);
      uprs.updateFloat(2, 150);
      uprs.updateInt(3, 1);
      uprs.updateDate(4, Date.valueOf("2014-01-23"));
      uprs.updateInt(5, 46248);
      uprs.updateString(6, "UFU");
      uprs.updateString(7, "Carla Soares Sousa");
      uprs.insertRow();

      uprs.moveToInsertRow();
      uprs.updateInt(1, 2001);
      uprs.updateFloat(2, 200);
      uprs.updateInt(3, 2);
      uprs.updateDate(4, Date.valueOf("2014-01-23"));
      uprs.updateInt(5, 26892);
      uprs.updateString(6, "Glória");
      uprs.updateString(7, "Carolina Soares Souza");
      uprs.insertRow();

      uprs.moveToInsertRow();
      uprs.updateInt(1, 2002);
      uprs.updateFloat(2, 500);
      uprs.updateInt(3, 3);
      uprs.updateDate(4, Date.valueOf("2014-01-23"));
      uprs.updateInt(5, 70044);
      uprs.updateString(6, "Cidade Jardim");
      uprs.updateString(7, "Eurides Alves da Silva");
      uprs.insertRow();

      uprs.beforeFirst();

      System.out.println("Dados inseridos com suscesso");

    } catch (SQLException e) {
      JDBCTutorialUtilities.printSQLException(e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }


    public static void menu(Connection con){
    try{
      //para rodar só deixar descomentado a funço que quiser, está na ordem que o sr pediu

//      Scanner in = new Scanner(System.in);
//      float rate = 0;
//
//      MyQueries.getMyData3(con);
//
//      MyQueries.cursorHoldabilitySupport(con);
//
//      System.out.println("taxa: \n" );
//      rate = in.nextFloat();
//      MyQueries.modifyPrices(con,rate,true);

      insertRow(con);
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    JDBCUtilities myJDBCUtilities;
    Connection con = null;
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
      con = myJDBCUtilities.getConnection();

      MyQueries.menu(con);


    } catch (SQLException e) {
      JDBCUtilities.printSQLException(e);
    } finally {
      JDBCUtilities.closeConnection(con);
    }

  }
}
