package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;

import javax.sql.DataSource;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtApplyListOp extends CMISOperation {

	private final String modelId = "LmtApply";
	private final String guarModelId = "LmtAppFinGuar";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String returnStr = "";
		try{
			connection = this.getConnection(context);
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			//判断是否担保公司客户 
			String cus_id = "";
			String cus_type = "";
			if(context.containsKey("cus_id")){
				cus_id = (String) context.getDataValue("cus_id");
				KeyedCollection cusKColl = dao.queryDetail("CusBase", cus_id, connection);
				cus_type = (String) cusKColl.getDataValue("cus_type");
			}
			
			if(cus_type != null && "A2".equals(cus_type)){
				KeyedCollection queryData = null;
				try {
					queryData = (KeyedCollection)context.getDataElement(guarModelId);
				} catch (Exception e) {}
				String conditionStr = TableModelUtil.getQueryCondition( guarModelId, queryData, context, false, false, false) ;
				
				if(conditionStr == null || "".equals(conditionStr)){
					conditionStr = "where cus_id ='"+cus_id+"'and approve_status='998' order by serno desc";
				}else{
					conditionStr += "and cus_id ='"+cus_id+"'and approve_status='998' order by serno desc";
				}
				TableModelUtil.parsePageInfo(context, pageInfo);

				IndexedCollection iColl = dao.queryList(guarModelId, null, conditionStr, pageInfo, connection);
				iColl.setName(iColl.getName() + "List");
				this.putDataElement2Context(iColl, context);

				SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id","manager_br_id" });
				SInfoUtils.addUSerName(iColl, new String[] { "input_id","manager_id" });

				String[] args = new String[] { "cus_id" };
				String[] modelIds = new String[] { "CusBase" };
				String[] modelForeign = new String[] { "cus_id" };
				String[] fieldName = new String[] { "cus_name" };
				// 详细信息翻译时调用
				SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);

				if(context.containsKey("menuId")){
					context.setDataValue("menuId", "QueryFinGuar");
				}else{
					context.addDataField("menuId", "QueryFinGuar");
				}
				return "guar";
			}
				
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			if(null != conditionStr && !"".equals(conditionStr)){
				conditionStr += " AND APP_TYPE NOT IN('03','04') ";  //过滤冻结、解冻申请
			}else{
				conditionStr += " WHERE APP_TYPE NOT IN('03','04') ";  //过滤冻结、解冻申请
			}
			
			if(context.containsKey("type") && "app".equals(context.getDataValue("type"))){   //申请
				//conditionStr += " AND APPROVE_STATUS IN('000','991','992') ";
				/**modified by lisj 2014-11-26 需求编号：【XD141107075】 支持输入客户名称可模糊查询 begin**/
				String cusName="";
				String cusIdList="";
				IndexedCollection iCollSelect = new IndexedCollection();
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);	
				if(queryData !=null && !"".equals(queryData)){
					cusName = (String) queryData.getDataValue("cus_name");
					if(cusName !=null && !"".equals(cusName)){
						String conSelect ="select c.cus_id from cus_base c where c.cus_name like '%"+cusName+"%'";
						iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
						if(iCollSelect !=null && iCollSelect.size() > 0){
							for(int i=0;i<iCollSelect.size();i++){
								KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
								cusIdList += kColl.getDataValue("cus_id")+",";
							}
							conditionStr += "and instr('"+ cusIdList+"', cus_id)>0";
						}
					}
				}
				returnStr ="app";
				/**modified by lisj 2014-11-26 需求编号：【XD141107075】 支持输入客户名称可模糊查询 end**/
			}else if(context.containsKey("type") && "cusHis".equals(context.getDataValue("type"))){   //否决历史
				if(context.containsKey("cus_id") && !"".equals(context.getDataValue("cus_id"))){
					conditionStr +=" AND CUS_ID='"+context.getDataValue("cus_id").toString()+"' ";
				}else{
					throw new EMPException("查询客户授信否决历史出错，传入客户码[cus_id]不能为空。");
				}
				conditionStr +=" AND APPROVE_STATUS='998' ";
				returnStr ="cusHis";
			}else{	//历史
				/**modified by lisj 2014-11-26 需求编号：【XD141107075】  支持输入客户名称可模糊查询 begin**/
				String cusName="";
				String cusIdList="";
				IndexedCollection iCollSelect = new IndexedCollection();
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);	
				if(queryData !=null && !"".equals(queryData)){
					cusName = (String) queryData.getDataValue("cus_name");
					if(cusName !=null && !"".equals(cusName)){
						String conSelect ="select c.cus_id from cus_base c where c.cus_name like '%"+cusName+"%'";
						iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
						if(iCollSelect !=null && iCollSelect.size() > 0){
							for(int i=0;i<iCollSelect.size();i++){
								KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
								cusIdList += kColl.getDataValue("cus_id")+",";
							}
							conditionStr += "and instr('"+ cusIdList+"', cus_id)>0";
						}else{
							conditionStr += "and instr('', cus_id)>0";
						}
					}
				}
				returnStr ="his";
				/**modified by lisj 2014-11-26需求编号：【XD141107075】  支持输入客户名称可模糊查询 end**/
			}
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,conditionStr, context, connection);
			
			conditionStr += " AND GRP_SERNO IS NULL order by serno desc";  //剔除集团授信				
			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "input_id","manager_id" });
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
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
		return returnStr;
	}

}
