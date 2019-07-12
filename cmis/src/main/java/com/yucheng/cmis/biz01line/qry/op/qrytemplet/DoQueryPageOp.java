package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.QryPubConstant;
//import com.yucheng.cmis.pub.util.PageResultSet;
import com.yucheng.cmis.biz01line.qry.component.QryExeComponent;


public class DoQueryPageOp extends CMISOperation {

    public String doExecute(Context context) throws EMPException {
    	int max = 5000;
        String tempNo = "";
        Connection connection = null;
        try {
           connection = this.getConnection(context);
            tempNo = (String) context.getDataValue("IN_TEMPNO");
        
        } catch (Exception e) {
            e.printStackTrace();
            throw new CMISException("接受前台页面信息失败!");
        }
        try {
            // 获取component对象
            QryExeComponent qryExeComponent = (QryExeComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(QryPubConstant.QRYEXECOMPONENT, context, connection);
            Collection iColl = (Collection)getHttpServletRequest(context).getSession().getAttribute("list");
            LinkedHashMap map = (LinkedHashMap)getHttpServletRequest(context).getSession().getAttribute("map");
            String chineseName = (String)getHttpServletRequest(context).getSession().getAttribute("chineseName");
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,chineseName);
            //得到当前页curPage和每页记录数pageSize 
	       	String curPages = getHttpServletRequest(context).getParameter("cur_page")== null ? "1":getHttpServletRequest(context).getParameter("cur_page");
	       	int curPage = Integer.parseInt(curPages);
	       	int pageSize=10; 
	       	//然后生成PageResultSet对象 
	       	/*
	       	 * 屏蔽错误
	       	 */
	   //    	PageResultSet dataList = new PageResultSet(iColl, curPage, pageSize); 
	    
	       	// LinkedHashMap map = this.getHeader(connection,tempNo);
           
       //    	getHttpServletRequest(context).setAttribute("dataList", dataList);    
            getHttpServletRequest(context).setAttribute("list", iColl);
            getHttpServletRequest(context).setAttribute("map", map);
            getHttpServletRequest(context).setAttribute("chineseName", chineseName);
        } catch (EMPException ee) {
            throw ee;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CMISException("查询失败!" + e.getMessage());
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }

        return null;
    }

    /**
     * 通过tempNo获取表头的名字
     * @param connection
     * @param tempNo
     * @return
     * @throws EMPException
     */
    public LinkedHashMap getHeader(Connection connection,String tempNo) throws EMPException {
    	ResultSet rs = null;
    	PreparedStatement ps = null;
    	LinkedHashMap map = new LinkedHashMap();
    	String sql =  "select enname,cnname from qry_result where temp_no ='"+ tempNo+"'" ;
    	try {
    		ps = connection.prepareStatement(sql);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			String key = rs.getString("enname");
    			String value = rs.getString("cnname");
    			map.put(key, value);
    		
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {    	
    			try {
    				if(rs != null) {
					rs.close();
    				}
    				if(ps !=null) {
    					ps.close();
    				}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		
    	}
    	return map;
    }
  
    
  
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
    }
}
