package com.kjt.service.common.exception.manager.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.google.common.collect.Lists;
import com.kjt.service.common.exception.manager.model.AjkSoaSp;

/**
 * Created by kevin on 15/1/13.
 */
public class AjkSoaSpDao {

    public List<AjkSoaSp> list(){
        String sql="select id,sp_name,sp_description from ajk_soa_sp ";

        List<AjkSoaSp> ajkSoaSps= Lists.newArrayList();
        Statement statement= null;
        Connection connection=null;
        try {
            connection=DbUtil.getConnection();
            statement = connection.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            while(rs.next()){
                AjkSoaSp ajkSoaSp=new AjkSoaSp();
                ajkSoaSp.setId(rs.getInt(1));
                ajkSoaSp.setSp_name(rs.getString(2));
                ajkSoaSp.setSp_description(rs.getString(3));
                ajkSoaSps.add(ajkSoaSp);
            }
            rs.close();
            statement.close();
            DbUtil.closeConnection(connection);
        } catch (SQLException e) {
        }

        return ajkSoaSps;
    }
    public AjkSoaSp get(int id){
        AjkSoaSp ajkSoaSp=new AjkSoaSp();
        if(id<=0){
            ajkSoaSp.setId(0);
            ajkSoaSp.setSp_name("框架");
            return ajkSoaSp;
        }
        String sql="select id,sp_name,sp_description from ajk_soa_sp ";

        List<AjkSoaSp> ajkSoaSps= Lists.newArrayList();
        Statement statement= null;
        Connection connection=null;
        try {
            connection=DbUtil.getConnection();
            statement = connection.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            if(rs.next()){
                ajkSoaSp=new AjkSoaSp();
                ajkSoaSp.setId(rs.getInt(1));
                ajkSoaSp.setSp_name(rs.getString(2));
                ajkSoaSp.setSp_description(rs.getString(3));
                ajkSoaSps.add(ajkSoaSp);
            }
            rs.close();
            statement.close();
            DbUtil.closeConnection(connection);
        } catch (SQLException e) {
        }

        return ajkSoaSp;
    }
}
