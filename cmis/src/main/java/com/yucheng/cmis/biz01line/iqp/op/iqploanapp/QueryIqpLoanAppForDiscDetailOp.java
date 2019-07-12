package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBatchComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryIqpLoanAppForDiscDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpLoanApp";
	private final String modelIdFollow = "IqpDiscApp";
	private final String cusModel ="CusBase";

	private final String serno_name = "serno";
	
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			BigDecimal total_amt = null;
			BigDecimal t_total_amt = null;
			BigDecimal together_remain_amount = null;
			BigDecimal remain_amount = null;
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeQueryRestrict(this.modelId, null, context, connection);

			String serno_value = null;
			String loan_direction ="";
		
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);

			KeyedCollection kColl = dao.queryAllDetail(modelId, serno_value, connection);
			//从关系表中取授信台账编号 
			kColl = this.getLimitNo(kColl, dao, connection);
			
			KeyedCollection kCollFollow = (KeyedCollection) kColl.getDataElement(modelIdFollow);
			/**获取业务关联批次下的实付总金额和总利息*/
			IqpBatchComponent batchComponent = (IqpBatchComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPBATCHCOMPONENT, context, connection);
			IndexedCollection ic = batchComponent.getBatchTotalInfoBySerno(serno_value);
			KeyedCollection kCollForBatch = null;
			if(!ic.isEmpty()){
				kCollForBatch = (KeyedCollection) ic.get(0);
			}
			if(kCollForBatch!=null){
				kCollFollow.setDataValue("disc_rate", kCollForBatch.getDataValue("int"));//总利息
				kCollFollow.setDataValue("net_pay_amt", kCollForBatch.getDataValue("rpay_amt"));//实付金额
				kCollFollow.setDataValue("bill_qty", kCollForBatch.getDataValue("bill_qnt"));//票据数量
				kCollFollow.setDataValue("disc_date", kCollForBatch.getDataValue("fore_disc_date"));//票据数量
				kColl.setDataValue("apply_amount", kCollForBatch.getDataValue("bill_total_amt"));//票据总金额
			}
			
			String prd_id = (String)kColl.getDataValue("prd_id");
			String cus_id = (String)kColl.getDataValue("cus_id");
			KeyedCollection prdkColl = dao.queryDetail("PrdBasicinfo", prd_id, connection);
			KeyedCollection cuskColl = dao.queryDetail(cusModel, cus_id, connection);
			String currency =(String)prdkColl.getDataValue("currency");
			String guarway =(String)prdkColl.getDataValue("guarway");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			//从cus_base表中获得客户基本信息
			CusBase cus = serviceCus.getCusBaseByCusId(cus_id, context, connection);
			String line = (String)cus.getBelgLine();
			/** 查询剩余额度  开始*/
//			String limit_ind = (String)kColl.getDataValue("limit_ind");
//			if(limit_ind==null){
//				limit_ind = "0";
//			}
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//	    	LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
//	    	IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//		    //循环剩余额度和一次性剩余额度
//	    	if(limit_ind.equals("2") || limit_ind.equals("3")){
//		    	String limit_acc_no = (String)kColl.getDataValue("limit_acc_no");
//		    	//获取总的授信额度
//		    	total_amt = new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(limit_acc_no, "01", connection));
//		    	//获取占用授信额度
//		    	KeyedCollection kCollRel = serviceIqp.getAgrUsedInfoByArgNo(limit_acc_no, "01", connection, context);//01-单一法人 
//		    	remain_amount = caculate(total_amt, kCollRel);
//		    	context.addDataField("remain_amount", remain_amount);
//		    
//		    	//合作方剩余额度
//		    }else if(limit_ind.equals("4")){
//		    	String limit_credit_no = (String)kColl.getDataValue("limit_credit_no");
//		    	//获取总的授信额度
//		    	total_amt =new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(limit_credit_no, "03", connection));
//		    	//获取占用授信额度
//		    	KeyedCollection kCollRel = serviceIqp.getAgrUsedInfoByArgNo(limit_credit_no, "03", connection, context);//03-合作方
//		    	together_remain_amount = caculate(total_amt, kCollRel);
//		    	context.addDataField("together_remain_amount", together_remain_amount);
//		   
//		    	//循环剩余额度+合作方剩余额度 || 一次性剩余额度和合作方剩余额度
//		    }else if(limit_ind.equals("5") || limit_ind.equals("6")){
//		    	String limit_acc_no = (String)kColl.getDataValue("limit_acc_no");
//		    	String limit_credit_no = (String)kColl.getDataValue("limit_credit_no");
//		    	//获取总的授信额度
//		    	total_amt =new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(limit_acc_no, "01", connection));
//		    	t_total_amt =new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(limit_credit_no, "03", connection));
//		    	//获取占用授信额度
//		    	KeyedCollection kCollRel = serviceIqp.getAgrUsedInfoByArgNo(limit_acc_no, "01", connection, context);//01-单一法人
//		    	KeyedCollection kCollInfo = serviceIqp.getAgrUsedInfoByArgNo(limit_credit_no, "03", connection, context);//03-合作方
//		    	remain_amount = caculate(total_amt, kCollRel);
//		    	together_remain_amount = caculate(t_total_amt, kCollInfo);
//		    	context.addDataField("remain_amount", remain_amount); 
//		    	context.addDataField("together_remain_amount", together_remain_amount);
//		    }
	    	/**查询剩余额度结束*/
			
			/** 翻译字典项 */
			Map<String,String> map = new HashMap<String, String>();
			map.put("IqpDiscApp.agriculture_type", "STD_ZB_FARME");
			map.put("IqpDiscApp.ensure_project_loan", "STD_ZB_DKGS5");
			map.put("IqpDiscApp.estate_adjust_type", "STD_ZB_TRD_TYPE");
			map.put("IqpDiscApp.strategy_new_loan", "STD_ZB_ZLXXCYLX");
			map.put("IqpDiscApp.new_prd_loan", "STD_ZB_XXCYDK");
			//map.put("IqpDiscApp.green_prd", "STD_ZB_LSCP");
			map.put("IqpDiscApp.loan_direction", "STD_GB_4754-2011");
			map.put("IqpDiscApp.loan_belong1", "STD_ZB_DKGS1");
			map.put("IqpDiscApp.loan_belong2", "STD_ZB_DKGS2");
			map.put("IqpDiscApp.loan_belong3", "STD_ZB_DKGS3");
			map.put("IqpDiscApp.loan_use_type", "STD_ZB_DKYT");
			if("BL100".equals(line) || "BL200".equals(line)){
				map.put("IqpDiscApp.loan_type", "STD_COM_POSITIONTYPE");
			}else if("BL300".equals(line)){
				map.put("IqpDiscApp.loan_type", "STD_PER_POSITIONTYPE");
			}
			
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			
//			String[] args=new String[] { "estate_adjust_type","strategy_new_loan","new_prd_loan","green_prd","loan_direction"};
//			String[] modelIds=new String[]{"STreedic","STreedic","STreedic","STreedic","STreedic"};
//			String[]modelForeign=new String[]{"enname","enname","enname","enname","enname"};
//			String[] fieldName=new String[]{"cnname","cnname","cnname","cnname","cnname"};
//		    //详细信息翻译时调用			
//		    SystemTransUtils.dealName(kCollFollow, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(prdkColl, context);
			this.putDataElement2Context(cuskColl, context);
			context.addDataField("currency", currency);
			context.addDataField("guarway", guarway);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
			
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
	
	
	/**计算剩余额度公用方法*/
	public BigDecimal caculate(BigDecimal total_amt,KeyedCollection kCollRel){
		BigDecimal lmt_amt = null;
		BigDecimal remain_amount = null;
		try {
			lmt_amt = new BigDecimal(kCollRel.getDataValue("lmt_amt")+"");
			remain_amount = total_amt.subtract(lmt_amt);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return remain_amount;
	}
	
	//'1':'不使用额度', '2':'使用循环额度', '3':'使用一次性额度', '4':'使用第三方额度', '5':'使用循环额度+第三方额度', '6':'使用一次性额度+第三方额度'
	public KeyedCollection getLimitNo(KeyedCollection kColl,TableModelDAO dao,Connection connection) throws Exception{
		try {
			String serno = (String)kColl.getDataValue("serno");
			String limit_ind = (String)kColl.getDataValue("limit_ind");
			if("1".equals(limit_ind)){
				return kColl;
			}else if("2".equals(limit_ind) || "3".equals(limit_ind)){
                String condition = "where serno='"+serno+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                kColl.put("limit_acc_no", agr_no);
                return kColl;
			}else if("4".equals(limit_ind)){
				String condition = "where serno='"+serno+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtcreditInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                kColl.put("limit_credit_no", agr_no);
                return kColl;
			}else if("5".equals(limit_ind) || "6".equals(limit_ind)){
				String condition = "where serno='"+serno+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtInfo", null, condition, connection);
                KeyedCollection kCollCreditRel = dao.queryFirst("RBusLmtcreditInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                String agr_no_credit = (String)kCollCreditRel.getDataValue("agr_no");
                kColl.put("limit_acc_no", agr_no);
                kColl.put("limit_credit_no", agr_no_credit);
                return kColl;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
	}
}
