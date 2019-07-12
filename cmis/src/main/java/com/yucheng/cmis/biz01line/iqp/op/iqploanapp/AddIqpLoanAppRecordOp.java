package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpLoanAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpLoanApp";
	private final String rbuslmtModelId = "RBusLmtInfo";//业务和授信关联表
	private final String rbuslmtCreditModel = "RBusLmtcreditInfo";//业务和第三方授信关联表
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			String prdid = (String)kColl.getDataValue("prd_id");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			kColl.put("serno", serno);
			KeyedCollection subKColl = null;
			/**票据贴现业务插入贴现从表*/
			if(prdid.equals("300021")||prdid.equals("300020")){
				subKColl = (KeyedCollection) kColl.getDataElement("IqpDiscApp");
				subKColl.put("agent_acct_no", subKColl.getDataValue("agent_acct_no").toString().trim());
			}else{
				subKColl = (KeyedCollection) kColl.getDataElement("IqpLoanAppSub");
			}
			
			//调用授信处理方法。
			doLimitManage(context,kColl);
			
			subKColl.addDataField("serno", serno);
			dao.insert(kColl, connection);
			
			context.addDataField("flag", "success");
			context.addDataField("serno", serno);
			
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
	
	/**
	 * 授信处理方法，授信使用情况有如下几种情况
	 * 1、不使用授信 2、使用循环额度 3、使用一次性额度 4、合作方额度 5、使用循环额度+合作方额度 6、使用一次性额度+合作方额度
	 * @param context
	 * @throws EMPException
	 */
	public void doLimitManage(Context context,KeyedCollection kColl) throws EMPException{
		Connection connection = null;
		try {
            connection = this.getConnection(context);
			
			String serno = (String)kColl.getDataValue("serno");
			String limit_ind = (String)kColl.getDataValue("limit_ind");;//授信使用标志
			String prd_id = (String)kColl.getDataValue("prd_id");;//产品编号
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where serno='"+serno+"'";
			
			KeyedCollection lmtKcoll = dao.queryAllDetail(rbuslmtModelId, serno, connection);
			KeyedCollection lmtCreditKcoll = dao.queryFirst(rbuslmtCreditModel,null, condition, connection);
			//1、授信使用情况为：使用循环额度  或者  使用一次性额度
	        if("2".equals(limit_ind) || "3".equals(limit_ind)){
	        	//1.1、检查是否已经存在自有授信关联记录，如果有，那么更新记录；如果没有，则新增。
	        	if(!"".equals(lmtKcoll.getDataValue("serno")) && lmtKcoll.getDataValue("serno")!=null){
	        		lmtKcoll.setDataValue("agr_no", kColl.getDataValue("limit_acc_no"));
	        		dao.update(lmtKcoll, connection);
	        	}else{
	            	lmtKcoll.setDataValue("agr_no", kColl.getDataValue("limit_acc_no"));
	            	lmtKcoll.setDataValue("serno", serno);
	            	lmtKcoll.setDataValue("cont_no", "");
	            	dao.insert(lmtKcoll, connection);
	        	}
	        	//1.2、检查是否存在第三方授信关联记录，如果有则删除。
	        	if(!"".equals(lmtCreditKcoll.getDataValue("serno")) && lmtCreditKcoll.getDataValue("serno")!=null){
	        		String agr_no = (String)lmtCreditKcoll.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", agr_no);
	        		dao.deleteAllByPks(rbuslmtCreditModel, hashMap, connection);
	        	}
	        	setGreenInfo(serno,kColl.getDataValue("limit_acc_no")+"",context,connection);
	        }else if("4".equals(limit_ind)){//2、授信使用情况为：合作方额度
	        	String lmt_type = "";
	        	String limit_credit_no = (String)kColl.getDataValue("limit_credit_no");
	        	if("300021".equals(prd_id)){
	        		//不处理,到修改页面录入票据处理
	        	}else{
	        		lmt_type="03";
	        		//2.1、检查是否已经存在自有授信关联记录，如果有，则删除。
		        	if(!"".equals(lmtKcoll.getDataValue("serno")) && lmtKcoll.getDataValue("serno")!=null){
		        		dao.deleteAllByPk(rbuslmtModelId, serno, connection);
		        	}
		        	//2.2、检查是否存在第三方授信关联记录，如果有则更新；如果没有，则新增。
		        	if(!"".equals(lmtCreditKcoll.getDataValue("serno")) && lmtCreditKcoll.getDataValue("serno")!=null){
		        		String old_agr_no = (String)lmtCreditKcoll.getDataValue("agr_no");
		        		HashMap<String,String> hashMap = new HashMap<String,String>();
		        		hashMap.put("serno", serno);
		        		hashMap.put("agr_no", old_agr_no);
		        		dao.deleteAllByPks(rbuslmtCreditModel, hashMap, connection);
		        		
		        		lmtCreditKcoll.put("agr_no", kColl.getDataValue("limit_credit_no"));
		            	lmtCreditKcoll.put("lmt_type", "03");//03-合作方
		            	lmtCreditKcoll.put("serno", serno);
		            	lmtCreditKcoll.put("cont_no", "");
		            	dao.insert(lmtCreditKcoll, connection);
		        	}else{
		            	lmtCreditKcoll.setDataValue("agr_no", limit_credit_no);
		            	lmtCreditKcoll.setDataValue("lmt_type", lmt_type);//03-合作方
		            	lmtCreditKcoll.setDataValue("serno", serno);
		            	lmtCreditKcoll.setDataValue("cont_no", "");
		            	dao.insert(lmtCreditKcoll, connection);
		        	}
	        	}
	        	setGreenInfo(serno,kColl.getDataValue("limit_credit_no")+"",context,connection);
	        }//3、授信使用情况为：使用循环额度+合作方额度   或者  使用一次性额度+合作方额度
	        else if("5".equals(limit_ind) || "6".equals(limit_ind)){
	        	//3.1、如果存在自有额度关联记录，那么先删除，再新增。
	        	if(!"".equals(lmtKcoll.getDataValue("serno")) && lmtKcoll.getDataValue("serno")!=null){
	        		dao.deleteAllByPk(rbuslmtModelId, serno, connection);
	        	}
	        	lmtKcoll.setDataValue("agr_no", kColl.getDataValue("limit_acc_no"));
	        	lmtKcoll.setDataValue("serno", serno);
	        	lmtKcoll.setDataValue("cont_no", "");
	        	dao.insert(lmtKcoll, connection);
	        	
	        	//3.2、如果存在第三方额度关联记录，那么先删除，再新增。
	        	if(!"".equals(lmtCreditKcoll.getDataValue("serno")) && lmtCreditKcoll.getDataValue("serno")!=null){
	        		String agr_no = (String)lmtCreditKcoll.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", agr_no);
	        		dao.deleteAllByPks(rbuslmtCreditModel, hashMap, connection);
	        	}
	        	
	        	lmtCreditKcoll.setDataValue("agr_no", kColl.getDataValue("limit_credit_no"));
	        	lmtCreditKcoll.setDataValue("lmt_type", "03");//03-合作方
	        	lmtCreditKcoll.setDataValue("serno", serno);
	        	lmtCreditKcoll.setDataValue("cont_no", "");
	        	dao.insert(lmtCreditKcoll, connection);
	        	setGreenInfo(serno,kColl.getDataValue("limit_acc_no")+"",context,connection);
	        }//4、授信使用情况为：不使用授信
	        else if("1".equals(limit_ind)){
	        	//4.1、如果存在自有额度关联记录，那么先删除，再新增。
	        	if(!"".equals(lmtKcoll.getDataValue("serno")) && lmtKcoll.getDataValue("serno")!=null){
	        		dao.deleteAllByPk(rbuslmtModelId, serno, connection);
	        	}
	        	
	        	//4.2、如果存在第三方额度关联记录，那么先删除，再新增。
	        	if(!"".equals(lmtCreditKcoll.getDataValue("serno")) && lmtCreditKcoll.getDataValue("serno")!=null){
	        		String agr_no = (String)lmtCreditKcoll.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", agr_no);
	        		dao.deleteAllByPks(rbuslmtCreditModel, hashMap, connection);
	        	}
	        	setGreenInfo(serno,null,context,connection);
	        }
		} catch (Exception e) {
			throw new EMPException(e);
		}
	}
	
	public void setGreenInfo(String serno,String limit_credit_no,Context context,Connection connection)throws EMPException{
		try {
		TableModelDAO dao = this.getTableModelDAO(context);
		String green_indus = "";
		KeyedCollection kCollLmt = null;
		KeyedCollection kColl = dao.queryDetail("IqpGreenDeclInfo", "", connection);
		if(!"".equals(limit_credit_no) && limit_credit_no != null){
			kCollLmt = dao.queryDetail("LmtAgrDetails", limit_credit_no, connection);
			green_indus = (String)kCollLmt.getDataValue("green_indus");
			kColl = dao.queryDetail("IqpGreenDeclInfo", serno, connection);
			kColl.put("green_indus", green_indus);
		}
		
		if(green_indus!=null && !"".equals(green_indus)){
		}else{
			kColl.put("green_indus", "2");
			kColl.put("green_indus_displayname", "否");
		}
		//如果IqpGreenDeclInfo中不存在数据，则保存
		String serno_value = (String)kColl.getDataValue("serno");
		if(("".equals(serno_value) || serno_value == null) && (green_indus == null || "".equals(green_indus) || "2".equals(green_indus))){
			kColl.put("green_indus", "2");
			kColl.put("serno", serno);
			dao.insert(kColl, connection);
		}
		} catch (Exception e) {
			throw new EMPException(e);
		}
	}
}
