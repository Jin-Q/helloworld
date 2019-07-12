package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBatchComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateIqpLoanAppForDiscRecordOp extends CMISOperation {

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
			
			KeyedCollection kColForDisc = (KeyedCollection) kColl.getDataElement("IqpDiscApp");
			kColForDisc.put("agent_acct_no", kColForDisc.getDataValue("agent_acct_no").toString().trim());
			//调用授信处理方法
			doLimitManage(context);

			TableModelDAO dao = this.getTableModelDAO(context);
			/**获取业务关联批次,更新批次表登记人，登记机构，责任人，责任机构*/
			String serno = (String) kColl.getDataValue("serno");
			IqpBatchComponent batchComponent = (IqpBatchComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPBATCHCOMPONENT, context, connection);
			KeyedCollection kCollBatchMng = batchComponent.getBatchMngBySerno(serno);
			if(kCollBatchMng!=null){
				if(kCollBatchMng.containsKey("input_id")&&"".equals(kCollBatchMng.getDataValue("input_id"))){
					kCollBatchMng.put("input_id", kColl.getDataValue("input_id"));
					kCollBatchMng.put("input_br_id", kColl.getDataValue("input_br_id"));
				}
				kCollBatchMng.setName("IqpBatchMng");
				dao.update(kCollBatchMng, connection);
			}
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			
			//更新贴现从表
			int count1=dao.update(kColForDisc, connection);
			if(count1!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch (EMPException ee) {
			ee.printStackTrace();
			context.addDataField("flag", "error");
			context.addDataField("msg", ee.getCause().getMessage());
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
	 * 额度的授信类型[lmt_type]（01-单一法人   02-同业客户   03-合作方）
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
			String prd_id = (String)kColl.getDataValue("prd_id");;//产品编号
			TableModelDAO dao = this.getTableModelDAO(context);
			String condtition = "where serno='"+serno+"' ";
			
			KeyedCollection lmtKcoll = dao.queryAllDetail(rbuslmtModelId, serno, connection);
			KeyedCollection lmtCreditKcoll = dao.queryFirst(rbuslmtCreditModel,null, condtition, connection);
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
	        }else if("4".equals(limit_ind)){//授信使用情况为：合作方额度
	        	KeyedCollection kCollRel =new KeyedCollection();
	        	String lmt_type = "";
	        	String limit_credit_no = (String)kColl.getDataValue("limit_credit_no");
	        	if("300021".equals(prd_id)){
	        		IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
	    			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
	        		//删除关系表数据
   				    cmisComponent.deleteLmtRelation(serno);
	        		lmt_type="90";//占用承兑行
	        		String condition = "where same_org_no in (select d.head_org_no from cus_same_org d where d.same_org_no in (select c.aorg_no from iqp_bill_detail c where c.porder_no in (select a.porder_no from iqp_batch_bill_rel a where a.batch_no in (select b.batch_no from Iqp_Batch_Mng b where b.serno = '"+serno+"'))))";
	        		IndexedCollection iCollCus = dao.queryList("CusSameOrg", condition, connection);
	        		if(iCollCus.size()==0){
	        			throw new Exception("请检查是否录入票据信息或票据承兑行总行行号不存在!");
	        		}
	        		for(int i=0;i<iCollCus.size();i++){
	        			KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(i);
	        			String cus_id = (String)kCollCus.getDataValue("cus_id");//总行客户码
	        			if(null!=cus_id && !"".equals(cus_id)){
	        				 String openDay = (String)context.getDataValue("OPENDAY");
	        				 KeyedCollection kCollLmtBank = dao.queryFirst("LmtIntbankAcc",null, "where cus_id='"+cus_id+"' and end_date>='"+openDay+"' and lmt_status='10'", connection);
	        				 String agr_no = (String)kCollLmtBank.getDataValue("agr_no");
	        				 if(agr_no == null || "".equals(agr_no)){
	        					 throw new EMPException("请检查票据承兑行总行是否存在有效授信!");
	        				 }
	        				 KeyedCollection kCollCreditRel = new KeyedCollection("RBusLmtcreditInfo");
	        				 kCollCreditRel.put("agr_no", agr_no);
	        				 kCollCreditRel.put("lmt_type", lmt_type);
	        				 kCollCreditRel.put("serno", serno);
	        				 kCollCreditRel.put("cont_no", "");
	        				 HashMap<String,String> map = new HashMap<String,String>();
	        				 map.put("agr_no", agr_no);
	        				 map.put("serno", serno);
	        				 kCollRel = dao.queryDetail("RBusLmtcreditInfo", map, connection);
	        				 String selectSerno = (String)kCollRel.getDataValue("serno");
	        				 if(selectSerno == null || "".equals(selectSerno)){
	        					 dao.insert(kCollCreditRel, connection);
	        				 }else{
	        					 dao.update(kCollCreditRel, connection);
	        				 }
	        			}else{
	        				throw new EMPException("承兑行总行行号不存在!");
	        			}
	        		}
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
	        	setGreenInfo(serno,kColl.getDataValue("limit_acc_no")+"",context,connection);
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
	        }
	        setGreenInfo(serno,kColl.getDataValue("limit_acc_no")+"",context,connection);
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
