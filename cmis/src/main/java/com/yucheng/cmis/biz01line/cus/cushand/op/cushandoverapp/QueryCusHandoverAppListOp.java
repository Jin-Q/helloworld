package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 该类是 客户移交申请 
 * 客户移交审批
 * 客户移交接收 三个模块调用的同一个类 每个模块url配置中增加不同的状态
 * 如果只是单纯客户资料移交，不需要监交人。接收人直接来接受模块 可以看到
 * 如果不是这种需要 监交人处理
 * @Version bsbcmis
 * @author wuming 2012-3-19 
 * Description:
 */
public class QueryCusHandoverAppListOp extends CMISOperation {


	private final String modelId = "CusHandoverApp";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null; 
			
			String conditionStr =null;  //查询条件
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			 conditionStr =TableModelUtil.getQueryCondition(this.modelId,queryData, context, false, false, false);
			 
			 RecordRestrict recordRestrict = this.getRecordRestrict(context);
			 conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
		/*	
		    String status = (String)context.getDataValue("CusHandoverApp.approve_status");
		    
		    String currentUserId=(String)context.getDataValue("currentUserId");
		    String organNo=(String)context.getDataValue("organNo");
		    //监交人才具有 查看提交给自己审批的申请
		    if(CusPubConstant.CUS_CREDIT_SUBMIT.equals(status)){
		        //如果仅客户资料移交，不需要审批，在where中过滤掉
		    	queryData.put("supervise_br_id", organNo);
		    	queryData.put("supervise_id", currentUserId);
		    	TableModelUtil.setCustomizeQueryConditionB(" handover_mode <> '1'", context);
		    	 conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
					            +"  order by serno desc";
		    }else if(CusPubConstant.CUS_CREDIT_AGREE.equals(status)){
		    	//点查询按钮时不需要下面两个值
		    	if(!queryData.containsKey("receiver_br_id")) {
			        //如果仅客户资料移交，需要审批，在where中增加
			    	queryData.put("receiver_br_id", organNo);
			    	queryData.put("receiver_id", currentUserId);
		    	}
		    	
		    	*//**
		    	 *客户移交 接收的相关条件控制
		    	 *STD_ZB_HAND_SCOPE' : {'1':'单个客户移交', '2':'按客户经理所有客户', '3':'按客户所属区域'
		    	 *如果 移交模式 是 客户移交 那么 控制条件  是 10 （提交状态） 并且 接收人 和接收机构是当前人 
		    	 *//*
		    	StringBuffer sqlCondition =new StringBuffer(" or (handover_mode='1' and approve_status = '10' and receiver_br_id='");
		    	
                sqlCondition.append(organNo).append("' and receiver_id='").append(currentUserId).append("') order by serno desc");

                conditionStr =TableModelUtil.getQueryCondition(this.modelId,queryData, context, false, false, false)
                                + sqlCondition.toString();
		    }else if(CusPubConstant.CUS_CREDIT_REGISTER.equals(status)){
		    	queryData.remove("status");
                conditionStr =
                        TableModelUtil.getQueryCondition(this.modelId,
                                queryData, context, false, false, false);
                if (conditionStr == null || conditionStr.trim().equals(""))
                    conditionStr = " where 1=1 ";
                StringBuffer conditionBuffer = new StringBuffer("");
                //申请时能看到自己的所有正在审批的申请单
                conditionBuffer.append(" and approve_status in('00', '10', '22', '21', '22')");
                //申请人只能看到自己的申请
                conditionBuffer.append(" and handover_id='").append(currentUserId).append("' ");
                conditionStr =
                        conditionStr + conditionBuffer.toString()
                                + "order by serno desc";
		    }
		    */
			int size = PUBConstant.MAXLINE;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			/*List list = new ArrayList();
			list.add("serno");
			list.add("handover_id");
			list.add("handover_br_id");
			list.add("receiver_id");
			list.add("receiver_br_id");
			list.add("supervise_id");
			list.add("supervise_br_id");
			list.add("status");
			list.add("handover_mode");*/
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			SInfoUtils.addSOrgName(iColl, new String[]{"handover_br_id", "receiver_br_id", "supervise_br_id"});
            SInfoUtils.addUSerName(iColl, new String[]{"handover_id", "receiver_id","supervise_id"});
            
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
