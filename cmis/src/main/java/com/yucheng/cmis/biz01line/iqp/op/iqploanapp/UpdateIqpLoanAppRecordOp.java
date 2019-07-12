package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class UpdateIqpLoanAppRecordOp extends CMISOperation {

	private final String modelId = "IqpLoanApp";
	private final String rbuslmtModelId = "RBusLmtInfo";//业务和授信关联表
	private final String rbuslmtCreditModel = "RBusLmtcreditInfo";//业务和第三方授信关联表
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			/**add by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造  begin**/
			TableModelDAO dao = this.getTableModelDAO(context);
			//如果是无间贷，删除 放款账户
			String serno = (String)kColl.getDataValue("serno");
			/** add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin **/
			KeyedCollection kColl4ILA = dao.queryAllDetail(modelId, serno, connection);
			KeyedCollection kColl4ILAS = (KeyedCollection) kColl4ILA.getDataElement("IqpLoanAppSub");
			BigDecimal old_apply_amount  = BigDecimalUtil.replaceNull(kColl4ILA.getDataValue("apply_amount")) ;//原申请金额
			BigDecimal old_apply_term  = BigDecimalUtil.replaceNull(kColl4ILAS.getDataValue("apply_term")) ;//原申请期限
			String old_term_type = kColl4ILAS.getDataValue("term_type").toString();//原期限类型
			KeyedCollection kCollSub = (KeyedCollection)kColl.getDataElement("IqpLoanAppSub");
			BigDecimal apply_amount  = BigDecimalUtil.replaceNull(kColl.getDataValue("apply_amount")) ;//申请金额
			BigDecimal apply_term  = BigDecimalUtil.replaceNull(kCollSub.getDataValue("apply_term")) ;//无间贷申请期限
			String term_type = (String) kCollSub.getDataValue("term_type");//无间贷期限类型

			String is_close_loan = (String)kCollSub.getDataValue("is_close_loan");
			/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造  begin*/
			/*String cus_id  = (String)kColl.getDataValue("cus_id");
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();			
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");//获取客户接口		
			//调用客户接口
			CusBase cusbase = service.getCusBaseByCusId(cus_id, context, connection);
			String BelgLine = (String)cusbase.getBelgLine();*/		
			if("1".equals(is_close_loan) ){//&& !"BL300".equals(BelgLine)
			/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造  end*/
				String condition = "where serno='"+serno+"' and acct_attr='01'";
				IndexedCollection iColl = dao.queryList("IqpCusAcct", condition, connection);
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kCollAcct = (KeyedCollection)iColl.get(i);
					String pk_id = (String)kCollAcct.getDataValue("pk_id");
					dao.deleteAllByPk("IqpCusAcct", pk_id, connection);
				}
				
				String repay_bill = (String) kCollSub.getDataValue("repay_bill");//偿还借据号
				//BigDecimal apply_term  = BigDecimalUtil.replaceNull(kCollSub.getDataValue("apply_term")) ;//无间贷申请期限
				//String term_type = (String) kCollSub.getDataValue("term_type");//无间贷期限类型
				IndexedCollection contTermIColl = SqlClient.queryList4IColl("queryContTerm", repay_bill, connection);
                if(contTermIColl!=null && contTermIColl.size()>0){
                	KeyedCollection temp = (KeyedCollection) contTermIColl.get(0);
                	BigDecimal	cont_term = BigDecimalUtil.replaceNull(temp.getDataValue("cont_term"));
                	//String	cont_term_type =(String) temp.getDataValue("term_type");
                	if("001".equals(term_type)){
                		apply_term = apply_term.multiply(new BigDecimal(12));
                	}else if("003".equals(term_type)){
                		apply_term = apply_term.divide(new BigDecimal(30));
                	}
                	if(cont_term.compareTo(apply_term)<0){
                		context.addDataField("flag", "termError");
                	}else{
                		//调用授信处理方法
        				doLimitManage(context);
        				int count=dao.update(kColl, connection);
        				if(count!=1){
        					throw new EMPException("Update Failed! Record Count: " + count);
        				}
        				context.addDataField("flag", "success");
                	}
                }
			}else{
				//调用授信处理方法
				doLimitManage(context);
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
				context.addDataField("flag", "success");
			}
			/**add by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造  end**/
			
			if("success".equals((String) context.getDataValue("flag")) 
					&& "trust".equals((String)context.getDataValue("flg"))){//当为信托贷款时，对费用信息进行更新提示
				//检查申请金额/期限/期限类型数据是否进行变更
				if((apply_amount.compareTo(old_apply_amount)!=0) 
						|| (apply_term.compareTo(old_apply_term)!=0) 
						|| (!term_type.equals(old_term_type))){
					context.put("flag", "sucAlertFee");
				}
			}
			/** add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end **/
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
	public void doLimitManage(Context context) throws EMPException{
		Connection connection = null;
		try {
            connection = this.getConnection(context);
			
			KeyedCollection kColl = (KeyedCollection)context.getDataElement(modelId);
			String serno = (String)kColl.getDataValue("serno");
			String limit_ind = (String)kColl.getDataValue("limit_ind");;//授信使用标志
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
	            	lmtCreditKcoll.setDataValue("agr_no", kColl.getDataValue("limit_credit_no"));
	            	lmtCreditKcoll.setDataValue("lmt_type", "03");//03-合作方
	            	lmtCreditKcoll.setDataValue("serno", serno);
	            	lmtCreditKcoll.setDataValue("cont_no", "");
	            	dao.insert(lmtCreditKcoll, connection);
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
	        	setGreenInfo(serno,kColl.getDataValue("limit_acc_no")+"",context,connection);
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
			KeyedCollection greedkc = dao.queryDetail("IqpGreenDeclInfo", serno, connection);
			if(greedkc.getDataValue("serno")==null){
				dao.insert(kColl, connection);
			}else{
				dao.update(kColl, connection);
			}
		}
		} catch (Exception e) {
			throw new EMPException(e);
		}
	}
}
