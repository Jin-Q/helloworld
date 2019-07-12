package com.yucheng.cmis.platform.organization.sorg.op;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QuerySOrgListPopForConditionOp extends CMISOperation {


	private final String modelId = "SOrg";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
          
			if(conditionStr==null || conditionStr.trim().equals("")) conditionStr=" where 1=1 ";
			
			conditionStr= StringUtil.transConditionStr(conditionStr, "organname");
			
			String queryFlag = "";
			try{
				queryFlag = (String)context.getDataValue("queryFlag");
				if(queryFlag.equals("mngBrId")){
					String artiOrganno = (String)context.getDataValue("ARTI_ORGANNO");
					conditionStr += " and arti_organno='"+artiOrganno+"'  order by organno desc";
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("organno");
			list.add("suporganno");
			list.add("arti_organno");
			list.add("organname");
			list.add("fincode");
			list.add("distno");
			list.add("area_dev_cate_type");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
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
