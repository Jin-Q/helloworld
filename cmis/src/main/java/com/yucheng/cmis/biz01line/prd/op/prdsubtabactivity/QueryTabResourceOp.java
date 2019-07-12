package com.yucheng.cmis.biz01line.prd.op.prdsubtabactivity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryTabResourceOp extends CMISOperation {

	private static final String PARENTID = "tabResourceId";//TAB标签页管理资源ID
	private final String modelId = "SResource";
	@Override
	public String doExecute(Context context) throws EMPException {
		/**
		 * 查询S_RESOURCE下TAB标签页管理资源目录下的所有tab页签资源ID
		 */
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement("SResource");
				
			} catch (Exception e) {}
//			try {
//				queryData = (KeyedCollection)context.getDataElement(this.modelId);
//			} catch (Exception e) {}
//			IndexedCollection iColl = new IndexedCollection();
//			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
//			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
//			iColl = ppsc.getTabResourceListByParentId(PARENTID);
//			iColl.setName("TabResourceList");			
//			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
//					
//			this.putDataElement2Context(iColl, context);
//			TableModelUtil.parsePageInfo(context, pageInfo);
			int size = 15;
			String conditionStr ="";
			 conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
				+"order by resourceid desc";
			if(queryData!=null){
			 if(queryData.getDataValue("cnname")!="" && queryData.getDataValue("resourceid")==""){
				String cnname =(String)queryData.getDataValue("cnname");
				conditionStr = "where cnname like '%"+cnname+"%'";
			 }else if(queryData.getDataValue("cnname")=="" && queryData.getDataValue("resourceid")!=""){
				 String resourceid =(String)queryData.getDataValue("resourceid");
				 conditionStr = "where resourceid like '%"+resourceid+"%'"; 
			 }else if(queryData.getDataValue("cnname")!="" && queryData.getDataValue("resourceid")!=""){
				 String resourceid =(String)queryData.getDataValue("resourceid");
				 String cnname =(String)queryData.getDataValue("cnname");
				 conditionStr = "where cnname like '%"+cnname+"%' and resourceid like '%"+resourceid+"%'"; 
			 }
			}
			   
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("resourceid");
			list.add("cnname");
			
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
