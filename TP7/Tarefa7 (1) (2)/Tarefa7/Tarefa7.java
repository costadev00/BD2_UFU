package com.oracle.tutorial.jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MyQueries2 {

    Connection con;
    JDBCUtilities settings;

    public MyQueries2(Connection connArg, JDBCUtilities settingsArg) {
        this.con = connArg;
        this.settings = settingsArg;
    }

    public static void getMyData(Connection con) throws SQLException {
        Statement stmt = null;
        String query = "select c.nome_cliente as nome " +
                "from cliente c " +
                "where c.nome_cliente in (select d.nome_cliente from deposito d) " +
                "and c.nome_cliente not in (select e.nome_cliente  from emprestimo e) ";

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

    public static List<String> readTXT () throws IOException { 
        BufferedReader inputStream = null;
        Scanner scanned_line = null;
        String line;
        String[] value;
        value = new String[7];

        int countv;
        List<String> lista = new ArrayList<>();
        try { 
            inputStream = new BufferedReader(new FileReader("debito-populate-table.txt"));
            while ((line = inputStream.readLine()) != null) { 
                countv=0;
                
                // System.out.println("<<");
                // split fields separated by tab delimiters
                scanned_line = new Scanner(line);
                scanned_line.useDelimiter("\t");
                while (scanned_line.hasNext()) {
                    value[countv++]=scanned_line.next();
                } //while
                
                if (scanned_line != null) { scanned_line.close();}
                //System.out.println(">>");
                lista.add("insert into debito (numero_debito, valor_debito,  motivo_debito, data_debito, numero_conta, nome_agencia, nome_cliente) " 
                    + "values (" + value[0] +", "+ value[1] +", "+ value[2] +", '"+ value[3] +"', "+ value[4] +", '"+ value[5] +"', '"+ value[6] + "');" );
            } //while
        }
        finally { if (inputStream != null) {
            inputStream.close();
        }} //if & finally

        return lista;
    } //main

    public static void populateTable (Connection con)throws SQLException{
        Statement stmt = null;
        
        try{
            stmt = con.createStatement();
            System.out.println("Executando DDL/DML");
            stmt.executeUpdate("truncate table debito;");
            List <String> querys = readTXT();
            
            if(querys.size() > 0){
                for(String item : querys){
                    stmt.executeUpdate(item);
                }
            }
        }catch(SQLException e){
            JDBCUtilities.printSQLException(e);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(stmt != null) {stmt.close();}
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

            //MyQueries2.getMyData(myConnection);
            MyQueries2.populateTable(myConnection);

        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            JDBCUtilities.closeConnection(myConnection);
        }

    }
}
