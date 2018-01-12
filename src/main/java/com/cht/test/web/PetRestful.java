package com.cht.test.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/pet")
public class PetRestful {

    @Path("/show")
    @GET
    @Produces("text/plain")
    public String show() {
        Connection conn = null;
        StringBuffer out = new StringBuffer();
        try {
            Context initialContext = new InitialContext();
            DataSource datasource = (DataSource)initialContext.lookup("java:jboss/datasources/MySQLDS");
            conn = datasource.getConnection();
            Statement stmt = conn.createStatement() ;
            String query = "select * from pet;" ;
            ResultSet rs = stmt.executeQuery(query) ;
            while (rs.next()) {
                out.append(rs.getString(1) + " / " + rs.getString(2) + " / " + rs.getString(3) + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return out.toString();
    }
    
    @Path("/add/{name}")
    @GET
    @Produces("text/plain")
    public String add(@PathParam("name") String name) {
        System.out.println("add pet name:" + name);
        Connection conn = null;
        try {
            Context initialContext = new InitialContext();
            DataSource datasource = (DataSource)initialContext.lookup("java:jboss/datasources/MySQLDS");
            conn = datasource.getConnection();
            
            Statement stmt = conn.createStatement() ;
            String query = "select count(*) from pet;" ;
            ResultSet rs = stmt.executeQuery(query) ;
            int total = 0;
            while (rs.next()) {
                total = rs.getInt(1);
            }
            
            if (total > 10) {
                return "add fail! db full.";
            }
            
            String sql = "INSERT INTO test.pet ( name, owner, species, sex, birth, death ) VALUES (?,null,null,null,null,null);";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            
            int count = pstmt.executeUpdate();
            if (count == 1) {
                return "add pet name:" + name;
            } else {
                return "add fail!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "add fail!";
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Path("/del/{name}")
    @GET
    @Produces("text/plain")
    public String del(@PathParam("name") String name) {
        System.out.println("del pet name:" + name);
        Connection conn = null;
        try {
            Context initialContext = new InitialContext();
            DataSource datasource = (DataSource)initialContext.lookup("java:jboss/datasources/MySQLDS");
            conn = datasource.getConnection();
            String sql = "delete from test.pet where name=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            
            int count = stmt.executeUpdate();
            if (count > 0) {
                return "del pet name:" + name;
            } else {
                return "del fail!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "del fail!";
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
