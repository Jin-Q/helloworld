package com.yucheng.cmis.biz01line.iqp.op.iqpbailsubdis;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBailComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class CheckIqpBailSubDisRecordOp extends CMISOperation{

	private final String modelId = "IqpBailSubDis";
	private final String serno_name = "serno";
	private boolean updateCheck = true;
	private String checked = "2";//默认追加的类型为新增
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			//拼接where条件，根据业务编号查出追加的保证金追加的类型
		    String conStr = "where serno ='"+serno_value+"'";
		    KeyedCollection kc = dao.queryAllDetail(modelId, serno_value, connection);
		    String flag = (String)kc.getDataValue("flag");
		    String addFlag = (String)kc.getDataValue("addflag");
		    if("1".equals(flag) && "2".equals(addFlag)){
		    	checked = "1";//确认为追加保证金  
		    }else{
		    	checked = "2";//新增的保证金
		    }
		    context.put("checked", checked);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return checked;
	}
}
