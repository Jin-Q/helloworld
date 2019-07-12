package com.yucheng.cmis.biz01line.cus.cusbase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.CMISDao;

public class CusBlkListDao extends CMISDao {

    /**
	    * 查询指定条件下的客户是否是不易贷款户
	    * @param certType   证件类型
	    * @param certCode   证件号码
	     * @param conn      连接
	    * @return
     * @throws Exception 
	    */
	    public  int getCusBlkList(String certType,String certCode,Connection conn) throws Exception{
	    	int intflag = 0;
	    	PreparedStatement ps = null;
	    	String sql = "";
	    	
	        sql = "select count(*) as no from  CUS_BLK_LIST WHERE CERT_TYPE='" +certType
	        +"' and CERT_CODE='"+certCode
	        +"' and STATUS in ('002','004')";
	    	try {
	    		ps = conn.prepareStatement(sql);
	    		ResultSet rs = ps.executeQuery();
	    		while(rs.next()){
	    			intflag =rs.getInt("no");
				}
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(ps !=null){
					ps.close();
					ps = null;
				}
			} catch (SQLException e) {
				EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
				throw e;
			}	
	    	return intflag;
	    }
}
