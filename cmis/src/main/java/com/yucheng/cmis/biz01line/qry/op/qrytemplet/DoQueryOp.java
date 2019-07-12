package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.QryPubConstant;
import com.yucheng.cmis.biz01line.qry.component.QryExeComponent;

/**
 * <p>
 * 解析SQL语句，生成数据文件,目前仅支持Oracle数据库
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * yucheng:
 * </p>
 * 
 * @author 胡宏伟 edit By xubin
 * @version 1.0
 * for update hwc 2010-5-5
 */
public class DoQueryOp extends CMISOperation {

	 public String doExecute(Context context) throws EMPException {
	        String tempNo = "";
	        String showColumns = "";
	        String orderByColumns = "";
	        String fileName = "";

	        int max = 1000;
	        Connection connection = null;

	        try {
	            connection = this.getConnection(context);
	            tempNo = (String) context.getDataValue("IN_TEMPNO");
	            showColumns = (String) context.getDataValue("ShowColumns");
	            orderByColumns = (String) context.getDataValue("OrderByColumns");
	            showColumns = showColumns.trim();
	            orderByColumns = orderByColumns.trim();
	            max = Integer.parseInt((String) context.getDataValue("INT_COUNT"));
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new CMISException("接受前台页面信息失败!");
	        }
	        try {
	            // 获取component对象
	            QryExeComponent qryExeComponent = (QryExeComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(QryPubConstant.QRYEXECOMPONENT, context, connection);
	           
	            fileName = qryExeComponent.doQuery(tempNo, showColumns, orderByColumns, max);
	            getHttpServletRequest(context).setAttribute("fileName", fileName);
	            try {
	                context.addDataField("fileName", fileName);
	            } catch (Exception e) {
	                context.setDataValue("fileName", fileName);
	            }
	            
	        } catch (Exception e) {
	        	 e.printStackTrace();
		         throw new CMISException("生成查询结果失败，失败原因：" + e.getCause().getMessage());
	        } finally {
	            if (connection != null)
	                this.releaseConnection(context, connection);
	        }

	        return "0";
	    }
    
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
    }
}
