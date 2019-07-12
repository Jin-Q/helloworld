package com.yucheng.cmis.biz01line.cus.op.cussubmitinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cussubmitinfo.component.CusSubmitInfoComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.TimeUtil;

public class AddCusSubmitInfoRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusSubmitInfo";
	public static String curTaskUser = "";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			context.addDataField("flag", "");
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			//再次提交标志
			String twoFlag = (String)kColl.getDataValue("twoSubFlag");
			
			String cusId = (String) kColl.getDataValue("cus_id");
			String opType = (String) kColl.getDataValue("opr_type");
			CusBaseComponent cusComponent = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("CusBase", context,connection);
			CusBase cusBase = cusComponent.getCusBase(cusId);
			String cusName = cusBase.getCusName();//客户名称
			
			if("2".equals(opType)){   //打回
				String condition = " where cus_id='" + cusId + "' and end_flag='1' ";
				IndexedCollection iColl = dao.queryList(modelId, condition , connection);
				if(iColl.size() == 1){ //有一个提交
					//将提交的设置为完成
					KeyedCollection ikColl = (KeyedCollection) iColl.get(0);
					ikColl.setDataValue("over_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					ikColl.setDataValue("end_flag", "0");
					dao.update(ikColl, connection);  // 设置为完成
					//插入一条打回数据
					kColl.setDataValue("rcv_id", ikColl.getDataValue("submit_id"));
					kColl.setDataValue("opr_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					kColl.setDataValue("cus_name", cusName);
					//add a record
					dao.insert(kColl, connection);
					context.setDataValue("flag", "back");
				}else{
					context.setDataValue("flag", "");
				}
			}else if( "1".equals( opType )){  //提交
				if(twoFlag!=null&&!"".equals(twoFlag)){//再次提交 
					//将打回的查出来  
					String condition = " where cus_id='" + cusId + "' and end_flag='1'";
					IndexedCollection iColl = dao.queryList(modelId, condition , connection);
					KeyedCollection ikColl = (KeyedCollection) iColl.get(0);
					//更新为完成
					ikColl.setDataValue("over_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					ikColl.setDataValue("end_flag", "0");
					dao.update(ikColl, connection);
					//插入一条新数据    接收人为  原来的移出人 
					kColl.remove("twoSubFlag");
					kColl.setDataValue("rcv_id", ikColl.getDataValue("submit_id"));
					kColl.setDataValue("cus_name", cusName);
					kColl.setDataValue("opr_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					dao.insert(kColl, connection);
					context.setDataValue("flag", "submit");
				}else{
					//首次提交,得到当前任务最少的人
					CusSubmitInfoComponent ccsComp = (CusSubmitInfoComponent) CMISComponentFactory.getComponentFactoryInstance()
					.getComponentInstance("CusSubmitInfo", context, connection);
					String userId = ccsComp.getLeastTaskUserId(context,connection);
					kColl.setDataValue("rcv_id", userId);
					kColl.setDataValue("cus_name", cusName);
					kColl.setDataValue("opr_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					//add a record
					dao.insert(kColl, connection);
					context.setDataValue("flag", "submit");
				}
			}else if( "3".equals( opType )){  //移交
				String condition = " where cus_id='" + cusId + "' and end_flag='1' ";
				IndexedCollection iColl = dao.queryList(modelId, condition , connection);
				if(iColl.size() == 1){ //有一个提交
					//设置移交信息并置为完成
					KeyedCollection ikColl = (KeyedCollection) iColl.get(0);
					KeyedCollection kCollNew = (KeyedCollection) ikColl.clone();//复制一条
					ikColl.setDataValue("handover_id", kColl.getDataValue("handover_id"));
					ikColl.setDataValue("handover_date", kColl.getDataValue("handover_date"));
					ikColl.setDataValue("handover_memo", kColl.getDataValue("handover_memo"));
					ikColl.setDataValue("over_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					ikColl.setDataValue("end_flag", "0");
					dao.update(ikColl, connection);  // 设置为完成
					//插入一条移交数据
					kCollNew.remove("serno");
					kCollNew.setDataValue("opr_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					kCollNew.setDataValue("rcv_id", kColl.getDataValue("handover_id"));
					kCollNew.setDataValue("cus_name", cusName);
//					kColl.setDataValue("handover_id", (String)ikColl.getDataValue("handover_id"));
					dao.insert(kCollNew, connection);
					context.setDataValue("flag", "handover");
				}else{
					context.setDataValue("flag", "");
				}
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
}
