package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class CkeckCusIsRfuHisOp extends CMISOperation {
	private final String modelId = "IqpLoanApp";
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = null;
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {
				throw new Exception("客户码为空!");
			}
			
			int size = 15; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			//过滤条件 客户码，申请状态为否决
			String conditionStr = "where cus_id='"+cus_id+"' and approve_status='998'";
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			if(iColl.size()>0){
				context.addDataField("flag","error");
			}else{
				context.addDataField("flag","success");
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
