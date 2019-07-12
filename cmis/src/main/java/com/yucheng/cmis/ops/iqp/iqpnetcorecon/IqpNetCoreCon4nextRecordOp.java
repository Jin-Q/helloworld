package com.yucheng.cmis.ops.iqp.iqpnetcorecon;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class IqpNetCoreCon4nextRecordOp extends CMISOperation {
	
	private final String modelId = "IqpNetCoreCon";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		try{
			connection = this.getConnection(context);
			String net_agr_no = (String)context.getDataValue("net_agr_no");
			String app_flag = (String)context.getDataValue("app_flag");
			
			if(net_agr_no==null||"".equals(net_agr_no))
				throw new Exception("网络编号[net_agr_no]为空！");
			
			if(app_flag==null||"".equals(app_flag))
				throw new Exception("申请标识[app_flag]为空！");
			
			TableModelDAO dao = getTableModelDAO(context);
			String condition = "where net_agr_no='"+net_agr_no+"' and approve_status not in ('997','998')";//存在在途的入/退网申请
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			
			if(iColl.size() > 0){
				KeyedCollection kColl = (KeyedCollection) iColl.get(0);
				String approve_status = (String)kColl.getDataValue("approve_status");//申请状态
				if("000".equals(approve_status)){
					if(app_flag.equals(kColl.getDataValue("app_flag"))){
						context.addDataField("flag", "existTaskUpd");//可以修改
					}else{
						context.addDataField("flag", "existTask");
					}
				}else{
					if(app_flag.equals(kColl.getDataValue("app_flag"))){
						context.addDataField("flag", "existTaskView");//可以查看
					}else{
						context.addDataField("flag", "existTask");
					}
				}
				context.addDataField("serno", kColl.getDataValue("serno"));
			}else{
				serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				//如果是退网  首先要查询圈内是否有 有效的客户
				if("1".equals(app_flag)){
					//查询是否有有效客户
					String conditionStr = " where net_agr_no='"+net_agr_no+"' and cus_status='1'";
					IndexedCollection iCollNLWork = dao.queryList("LmtNameList", conditionStr, connection);
					if(iCollNLWork.size()>0){
						//add a record入网退网申请表   增加一条纪录
						save2JoinBack(net_agr_no, app_flag , context, connection, serno);
						context.addDataField("flag", "suc");
					}else{
						context.addDataField("flag", "dont");
					}
				}else{ //入网申请
					//add a record入网退网申请表   增加一条纪录
					save2JoinBack(net_agr_no, app_flag , context, connection, serno);
					context.addDataField("flag", "suc");
				}
				//最后将生成的serno放入context
				context.addDataField("serno", serno);
			}
			
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
	
	private String save2JoinBack(String net_agr_no, String app_flag,Context context , Connection connection,String serno) throws EMPException {
		String modelIdJB = "IqpNetCoreCon";
		try {
			//将LmtAgrBizArea 数据转换为  LmtAppJoinBack 存储
			TableModelDAO dao = getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList("IqpNetMagInfo", " WHERE net_agr_no='" + net_agr_no + "'", connection);
			if(iColl.size() == 0) return "fail";
			KeyedCollection kCollAgrBiz = (KeyedCollection)iColl.get(0);
			KeyedCollection kCollAppJB =  new KeyedCollection(modelIdJB);
			kCollAppJB.addDataField("serno", serno);
			kCollAppJB.addDataField("net_agr_no", net_agr_no);
			kCollAppJB.addDataField("app_flag", app_flag);
			kCollAppJB.addDataField("approve_status", "000");
			kCollAppJB.addDataField("input_id", context.getDataValue("currentUserId"));
			kCollAppJB.addDataField("input_br_id", context.getDataValue("organNo"));
			kCollAppJB.addDataField("manager_id", kCollAgrBiz.getDataValue("manager_id"));
			kCollAppJB.addDataField("manager_br_id", kCollAgrBiz.getDataValue("manager_br_id"));
			kCollAppJB.addDataField("input_date", context.getDataValue("OPENDAY"));
			dao.insert(kCollAppJB, connection);
		} catch (EMPException e) {
			throw e;
		}
		return "suc";
	}
}
