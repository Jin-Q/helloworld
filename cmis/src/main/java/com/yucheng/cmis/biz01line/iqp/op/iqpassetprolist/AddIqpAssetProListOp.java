package com.yucheng.cmis.biz01line.iqp.op.iqpassetprolist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpAssetProListOp  extends CMISOperation {
	
	/**添加项目金额的值，将其传入到后台并用于返回页面     2014-08-05 邓亚辉*/
	private final String iqpassetproappModel = "IqpAssetProApp";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		String pro_amt = "";
		try{
			connection = this.getConnection(context);
		    serno = (String)context.getDataValue("serno");
		    TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		    //将项目金额查出
		    if(serno!=null && serno != ""){
		    	IndexedCollection iapaIcoll = dao.queryList(iqpassetproappModel,  " where serno='"+serno+"'", connection);
		    	if(iapaIcoll != null && iapaIcoll.size() > 0){
		    		KeyedCollection kc = (KeyedCollection) iapaIcoll.get(0);
		    		pro_amt=(String) kc.getDataValue("pro_amt");
		    		context.addDataField("pro_amt", pro_amt);
		    	}
		    }
		   
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
