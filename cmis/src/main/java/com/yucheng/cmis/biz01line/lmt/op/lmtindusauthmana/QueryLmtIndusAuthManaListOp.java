package com.yucheng.cmis.biz01line.lmt.op.lmtindusauthmana;

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
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtIndusAuthManaListOp extends CMISOperation {


	private final String modelId = "LmtIndusAuthMana";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String agr_no = "";
			String conditionStrType = " order by agr_no desc";
			if(context.containsKey("agr_no")){
				agr_no = (String) context.getDataValue("agr_no");
				conditionStrType = " agr_no = '" + agr_no+"'"+ conditionStrType ;
			}
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);			
			if(conditionStr==null||"".equals(conditionStr)){
				conditionStr = " where " + conditionStrType;
			}else{
				conditionStr =  conditionStr  + conditionStrType ;
			}
			
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("agr_no");
			list.add("input_br_id");
			list.add("guar_type");
			list.add("single_auth_amt");
			list.add("status");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id" });
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
