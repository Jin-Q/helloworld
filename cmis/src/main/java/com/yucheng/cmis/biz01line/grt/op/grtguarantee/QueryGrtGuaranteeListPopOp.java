package com.yucheng.cmis.biz01line.grt.op.grtguarantee;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGrtGuaranteeListPopOp extends CMISOperation {


	private final String modelId = "GrtGuarantee";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String guarId=null;
		try{
			connection= this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
				
			} catch (Exception e) {}
			
			int size = 15;
			//guarId = (String)context.getDataValue("guar_id");
		    //拼接where条件对担保人信息进行过滤（综合授信模块使用）
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
			                        +"order by guar_id desc";
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			List<String> list = new ArrayList<String>();
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			//翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
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
