package com.yucheng.cmis.biz01line.qry.op.qrytemplet.qryparam;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryQryTempletQryParamListOp extends CMISOperation {
	
	private final String modelId = "QryParam";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String temp_no_value = (String)context.getDataValue("QryTemplet.temp_no");
			
			if(temp_no_value==null){
				throw new EMPException("parent primary key not found!");
			}
			
			String conditionStr = "where temp_no = '" + temp_no_value+"' order by to_number(orderid) asc, temp_no desc,param_no desc";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			
			//翻译参数选项字典编号
			String[] args = new String[] { "param_dic_no" };
			String[] modelIds = new String[] { "QryParamDic" };
			String[] modelForeign = new String[] { "param_dic_no" };
			String[] fieldName = new String[] { "name" };
			// 详细信息翻译时调用
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			this.putDataElement2Context(iColl, context);	
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
