package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtsiglmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtSigLmtLsOp extends CMISOperation {
	//个人授信历史记录展示

	private final String modelId = "LmtSigLmt";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
		if(null==conditionStr || "".equals(conditionStr)){
			conditionStr ="where app_cls= 01 order by serno desc";
		}				     
		else{
			conditionStr = conditionStr + "and app_cls= 01 order by serno desc";
		}			
			int size = 15;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			for(int i=0;i<iColl.size();i++)
			{
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String cus_id =(String)kColl.getDataValue("cus_id");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				KeyedCollection kColl_cus =service.getCusSameOrgKcoll(cus_id, context, connection);
				String same_org_cnname = (String)kColl_cus.getDataValue("same_org_cnname");
				kColl.addDataField("same_org_cnname", same_org_cnname);
				iColl.setDataElement(kColl);
			}
			iColl.setName(iColl.getName()+"List");
			//翻译责任人、管理机构信息
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"manager_id"});
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
