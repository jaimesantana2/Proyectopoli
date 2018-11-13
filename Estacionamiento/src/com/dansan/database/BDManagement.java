package com.dansan.database;

import com.dansan.bean.HistoricoBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dansan
 */
public class BDManagement {

    private String username = "root";
    private String password = "riverplate";
    private String db = "jdbc:mysql://localhost:3306/plaza_estacionamiento";
    private String driver = "com.mysql.jdbc.Driver";
    private Connection conexion;
    private Statement st;

    // Se crea la conexion a la base plaza_estacionamiento
    public BDManagement() {
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(db, username, password);
            st = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodos de la tabla tarifa
     */
    public double obtenerTarifa(char c) {
        double tarifa = 0;
        String pedido = "select * from tarifas where tipo_auto='" + c + "'";
        try {
            ResultSet rs = st.executeQuery(pedido);
            if (rs.next()) {
                tarifa = (double) rs.getObject("tarifa");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tarifa;
    }

    public ResultSet listarTarifas() {
        ResultSet rs = null;
        String pedido = "select * from tarifas";
        try {
            rs = st.executeQuery(pedido);
        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    /**
     * Metodos de la tabla cochera
     */
    public ResultSet listarCocheras() {
        ResultSet rs = null;
        String pedido = "select * from cocheras";
        try {
            rs = st.executeQuery(pedido);
        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public int numCochesEnCocheras(int piso) {
        int total = 0;
        ResultSet rs=null;
        String pedido = "select count(*) as total from cocheras where piso="+piso;
        try {
            rs = st.executeQuery(pedido);
            while(rs.next()){
                total = rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }
    
    
    public HistoricoBean obtenerCoche(String patente) {
        HistoricoBean bean = new HistoricoBean();
        try {
            String query = "select * from cocheras where patente='" + patente + "'";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                bean.setPatente(rs.getString("patente"));
                bean.setHora_ocupacion(rs.getDouble("hora_ocupacion"));
                String cad = (String) rs.getObject("tipo_auto");
                char c = cad.charAt(0);
                bean.setTipo_auto(c);
                bean.setPiso(rs.getInt("piso"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

    public ArrayList<HistoricoBean> obtenerCoche(int piso) {
        ArrayList<HistoricoBean> beanes = new ArrayList<HistoricoBean>();
        try {
            String query = "select * from cocheras where piso=" + piso;
            ResultSet rs = st.executeQuery(query);
            rs.beforeFirst();
            while (rs.next()) {
                HistoricoBean bean = new HistoricoBean();
                bean.setPatente(rs.getString("patente"));
                bean.setHora_ocupacion(rs.getDouble("hora_ocupacion"));
                String cad = (String) rs.getObject("tipo_auto");
                char c = cad.charAt(0);
                bean.setTipo_auto(c);
                bean.setPiso(rs.getInt("piso"));
                beanes.add(bean);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return beanes;
    }

    public void agregarCocheras(String patente, int piso, char tipo) {
        Date date = new Date();
        double fecha = date.getTime();
        String query = "insert into cocheras (patente,hora_ocupacion,tipo_auto,piso) ";
        query += "values('" + patente + "','" + fecha + "','" + tipo + "','" + piso + "')";
        try {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void borrarCocheras(String patente) {
        String query = "delete from cocheras where patente = '" + patente + "'";
        try {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodos de la tabla historico
     */
    public ResultSet listarHistorico() {
        ResultSet rs = null;
        String query = "select * from historico";
        try {
            rs = st.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public void grabarHistorico(String patente, double hora_ocupacion, char tipo, int piso, double importe) {
        Date date = new Date();
        double fecha = date.getTime();
        String query = "";
        query += "insert into historico (patente,hora_ocupacion,tipo_auto,piso,hora_salida,importe) ";
        query += "values('" + patente + "','" + hora_ocupacion + "','" + tipo + "','" + piso + "','" + fecha + "','" + importe + "')";
        try {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(BDManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
