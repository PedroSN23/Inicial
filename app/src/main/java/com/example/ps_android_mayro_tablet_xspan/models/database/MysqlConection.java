package com.example.ps_android_mayro_tablet_xspan.models.database;

import com.example.ps_android_mayro_tablet_xspan.models.items.Contenedores;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MysqlConection {
    private final Connection conn;

    public MysqlConection(String ip, String database, String user, String pass) throws  SQLException, ClassNotFoundException {
        String db_url = "jdbc:mysql://"+ip+"/"+database+"?autoreconect=true&maxReconnects=10";
        Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection(db_url, user, pass);
    }

    /**********************EMBARQUES***************************/
    public int getIdEmbarque(String epc) throws SQLException {
        int ret=-1;
        String query = "SELECT id_embarque FROM lista_pl WHERE epc = ?";
        PreparedStatement preparedStatement = (PreparedStatement) conn.prepareStatement(query);
        preparedStatement.setString(1, epc);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs != null) {
            if(rs.next()) {
                ret = rs.getInt(1);
            }
            rs.close();
        }
        return ret;
    }

    public List<Contenedores> obtenerPLRecibo(int id_embarque) throws SQLException {
        List<Contenedores>contenedoresList = new LinkedList<>();
        String query = "SELECT epc, estado, folio_contenedor, sku FROM lista_pl WHERE id_embarque = ?";
        PreparedStatement preparedStatement = (PreparedStatement) conn.prepareStatement(query);
        preparedStatement.setInt(1, id_embarque);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs != null) {
            while(rs.next()) {
                int folio = rs.getInt(3);
                String epc = rs.getString(1);
                String sku = rs.getString(4);
                boolean leido = rs.getInt(2)==2;
                boolean found = false;
                for(Contenedores c: contenedoresList) {
                    if(c.getFolio_contenedor()==folio) {
                        found = true;
                        c.addSkuFromBd(sku, epc, leido);
                        break;
                    }
                }
                if(!found) {
                    Contenedores c = new Contenedores(folio);
                    c.addSkuFromBd(sku, epc, leido);
                    contenedoresList.add(c);
                }
            }
            rs.close();
        }
        return contenedoresList;
    }

    public void modificarEstadoPallet(int folio) throws SQLException {
        String query = "UPDATE pallets SET estado = 3 WHERE id_pallet = ?";
        PreparedStatement preparedStatement = (PreparedStatement) conn.prepareStatement(query);
        preparedStatement.setInt(1, folio);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void modificarEtadoEmbarque(int id_embarque, int folio) throws SQLException {
        String query = "UPDATE lista_pl SET estado = 2 WHERE folio_contenedor = ? AND id_embarque = ?";
        PreparedStatement preparedStatement = (PreparedStatement) conn.prepareStatement(query);
        preparedStatement.setInt(1, folio);
        preparedStatement.setInt(2, id_embarque);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
