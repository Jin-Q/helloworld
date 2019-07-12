package com.yucheng.cmis.biz01line.iqp.op.iqpchkstoreset;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortGuarantyBaseInfoTabForChkStoreListOp extends CMISOperation {


	private final String modelId = "MortGuarantyBaseInfo";
	private final String reModelId = "IqpCargoOverseeRe";
	private final String agrModelId = "IqpOverseeAgr";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String guarantyStr = "";//要展示的押品串
			
			if(context.containsKey("oversee_agr_no")&&context.getDataValue("oversee_agr_no")!=null&&!(context.getDataValue("oversee_agr_no")).equals("")){//不存在，入口为任务设置
				String oversee_agr_no = (String) context.getDataValue("oversee_agr_no");
				String reCondition = " where agr_type = '02' and agr_no='"+oversee_agr_no+"'";//协议类型为监管协议
				IndexedCollection reIColl = dao.queryList(reModelId, reCondition, connection);
				for(int j=0;j<reIColl.size();j++){
					KeyedCollection reKColl = (KeyedCollection) reIColl.get(j);
					String guaranty_no = (String) reKColl.getDataValue("guaranty_no");
					if(guarantyStr.equals("")){
						guarantyStr = "('"+guaranty_no;
					}else{
						guarantyStr = guarantyStr + "','"+guaranty_no;
					}
				}
			}else{//入口为核巡任务
				String task_set_type = (String) context.getDataValue("task_set_type");
				String cus_id = (String) context.getDataValue("cus_id");
				
				//先获取监管协议
				String agrCondition = "";
				if(task_set_type.equals("01")){//出质人
					agrCondition = " where mortgagor_id = '"+cus_id+"'";
				}else if(task_set_type.equals("02")){//监管企业
					agrCondition = " where oversee_con_id = '"+cus_id+"'";
				}
				
				IndexedCollection agrIColl = dao.queryList(agrModelId,agrCondition,connection);
				//获取监管协议下所有押品，并组装where语句
				for(int i=0;i<agrIColl.size();i++){
					KeyedCollection agrKColl = (KeyedCollection) agrIColl.get(i);
					String oversee_agr_no = (String) agrKColl.getDataValue("oversee_agr_no");
					String reCondition = " where agr_type = '02' and agr_no='"+oversee_agr_no+"'";//协议类型为监管协议
					IndexedCollection reIColl = dao.queryList(reModelId, reCondition, connection);
					for(int j=0;j<reIColl.size();j++){
						KeyedCollection reKColl = (KeyedCollection) reIColl.get(j);
						String guaranty_no = (String) reKColl.getDataValue("guaranty_no");
						if(guarantyStr.equals("")){
							guarantyStr = "('"+guaranty_no;
						}else{
							guarantyStr = guarantyStr + "','"+guaranty_no;
						}
					}
				}
			}
			
			if(!guarantyStr.equals("")){
				guarantyStr = guarantyStr + "')";
				
				if(conditionStr==null||conditionStr.equals("")){
					conditionStr = " where guaranty_no in "+guarantyStr;
				}else{
					conditionStr = conditionStr + " and guaranty_no in "+guarantyStr;
				}
			}else{
				conditionStr = " where 1=2";
			}
			
			
			
			int size = 10; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			//翻译机构和登记人
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" ,"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" ,"input_br_id","keep_org_no","hand_org_no"});
			Map<String,String> map = new HashMap<String,String>();
			map.put("guaranty_type","MORT_TYPE");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
	    	SInfoUtils.addPopName(iColl, map, service);
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
