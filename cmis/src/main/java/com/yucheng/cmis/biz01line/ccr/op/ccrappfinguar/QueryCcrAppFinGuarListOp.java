package com.yucheng.cmis.biz01line.ccr.op.ccrappfinguar;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCcrAppFinGuarListOp extends CMISOperation {


	private final String modelId = "CcrAppInfo";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String flag = "";
		String conditionStr = "";
		IndexedCollection iColl = new IndexedCollection();
		try{
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[] { "cus_id" };
			String[] fieldName=new String[]{"cus_name"};

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String temp=SystemTransUtils.dealQueryName(queryData, args, context, modelIds,modelForeign, fieldName);
			if(temp!=null&&temp.length()>0){
				temp=" and "+temp;
			}
			//modify by jiangcuihua 2019-03-16 注释掉会使页面查询条件失效
			if("6".equals(flag)){
			conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
			}
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			//conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");

			String input_id = (String) context.getDataValue("currentUserId");
			String organNo = (String) context.getDataValue("organNo");
			String roles = (String) context.getDataValue("roles");
			if(context.containsKey("flag")){
				flag = (String) context.getDataValue("flag");
			    CcrComponent ccrComponent = (CcrComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT, context, connection);
				if("6".equals(flag)){//监管机构评级
					if(conditionStr==null||"".equals(conditionStr.trim())){
						conditionStr += " where flag = '6' "+temp+" order by serno desc";
						
					}else{
						conditionStr += " and flag = '6' "+temp+" order by serno desc";
					}
					TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
					iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
				}else if("4".equals(flag)){
					    if(!"".equals(conditionStr)){
						    conditionStr = " and "+conditionStr.substring(7, conditionStr.length()-1);
						}
						String serno = "";
						if(context.containsKey("CcrAppInfo.serno") && !"".equals(context.getDataValue("CcrAppInfo.serno"))){
							serno = context.getDataValue("CcrAppInfo.serno").toString();
							conditionStr += " AND a.serno='"+serno+"'";
						}
						String cus_id = "";
						if(context.containsKey("CcrAppInfo.cus_id") && !"".equals(context.getDataValue("CcrAppInfo.cus_id"))){
							cus_id = context.getDataValue("CcrAppInfo.cus_id").toString();
							conditionStr += " AND a.cus_id='"+cus_id+"'";
						}
						String app_begin_date = "";
						if(context.containsKey("CcrAppInfo.app_begin_date") && !"".equals(context.getDataValue("CcrAppInfo.app_begin_date"))){
							app_begin_date = context.getDataValue("CcrAppInfo.app_begin_date").toString();
							conditionStr += " AND a.app_begin_date='"+app_begin_date+"'";
						}
						String manager_id = "";
						if(context.containsKey("CcrAppInfo.manager_id") && !"".equals(context.getDataValue("CcrAppInfo.manager_id"))){
							manager_id = context.getDataValue("CcrAppInfo.manager_id").toString();
							conditionStr += " AND a.manager_id='"+manager_id+"'";
						}
						String approve_status = "";
						if(context.containsKey("CcrAppInfo.approve_status") && !"".equals(context.getDataValue("CcrAppInfo.approve_status"))){
							approve_status = context.getDataValue("CcrAppInfo.approve_status").toString();
							conditionStr += " AND a.approve_status='"+approve_status+"'";
						}
						//获取查询SQL
						String cond = ccrComponent.getSql(conditionStr);
					    //分页处理
						iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context),cond);
				}else if("priv".equals(flag)){
					if(!"".equals(conditionStr)){
						conditionStr = conditionStr.replace("WHERE","AND");
					}
					String serno = "";
					if(context.containsKey("CcrAppInfo.serno") && !"".equals(context.getDataValue("CcrAppInfo.serno"))){
						serno = context.getDataValue("CcrAppInfo.serno").toString();
						conditionStr += " AND a.serno='"+serno+"'";
					}
					String cus_id = "";
					if(context.containsKey("CcrAppInfo.cus_id") && !"".equals(context.getDataValue("CcrAppInfo.cus_id"))){
						cus_id = context.getDataValue("CcrAppInfo.cus_id").toString();
						conditionStr += " AND a.cus_id='"+cus_id+"'";
					}
					String app_begin_date = "";
					if(context.containsKey("CcrAppInfo.app_begin_date") && !"".equals(context.getDataValue("CcrAppInfo.app_begin_date"))){
						app_begin_date = context.getDataValue("CcrAppInfo.app_begin_date").toString();
						conditionStr += " AND a.app_begin_date='"+app_begin_date+"'";
					}
					String manager_id = "";
					if(context.containsKey("CcrAppInfo.manager_id") && !"".equals(context.getDataValue("CcrAppInfo.manager_id"))){
						manager_id = context.getDataValue("CcrAppInfo.manager_id").toString();
						conditionStr += " AND a.manager_id='"+manager_id+"'";
					}
					String approve_status = "";
					if(context.containsKey("CcrAppInfo.approve_status") && !"".equals(context.getDataValue("CcrAppInfo.approve_status"))){
						approve_status = context.getDataValue("CcrAppInfo.approve_status").toString();
						conditionStr += " AND a.approve_status='"+approve_status+"'";
					}
					//获取查询SQL
					String cond = ccrComponent.getSql(conditionStr);
				    //分页处理
					iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context),cond);
				}
			}
			SInfoUtils.addUSerName(iColl, new String[] { "input_id","manager_id"});
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id"});
			
			iColl.setName("CcrAppInfoList");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			
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
