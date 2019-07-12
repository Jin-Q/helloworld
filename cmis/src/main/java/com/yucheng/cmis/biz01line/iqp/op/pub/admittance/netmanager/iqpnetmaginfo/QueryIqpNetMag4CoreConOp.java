package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpnetmaginfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpNetMag4CoreConOp extends CMISOperation {


	private final String modelId = "IqpNetMagInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if("".equals(conditionStr)){
				conditionStr = " WHERE 1=1 ";
			}
			 String temp = SystemTransUtils.dealQueryName(queryData, args, context, modelIds,modelForeign, fieldName);
			 if(!"".equals(temp)){
				 conditionStr += " AND "+temp ;
			 }
			conditionStr += " AND STATUS='1' ORDER BY NET_AGR_NO DESC";
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));	
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			//详细信息翻译时调用			
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
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
