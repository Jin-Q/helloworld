package com.yucheng.cmis.biz01line.iqp.op.iqprpddscnt;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBatchComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpRpddscntDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpRpddscnt";
	private final String serno_name = "serno";
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String limit_acc_no = null;
		String limit_credit_no = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeQueryRestrict(this.modelId, null, context, connection);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			kColl = this.getLimitNo(kColl, dao, connection);
			
			/** 查询剩余额度 */
//			BigDecimal total_amt = null;//总的授信额度
//			BigDecimal remain_amount = null;//剩余授信额度
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
//		    	total_amt = new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(limit_acc_no, "02", connection));
//		    	//获取占用授信额度
//		    	KeyedCollection kCollRel = serviceIqp.getAgrUsedInfoByArgNo(limit_acc_no, "02", connection, context);//02-同业授信
//		    	remain_amount = caculate(total_amt, kCollRel);
//		    	context.addDataField("remain_amount", remain_amount);
//		    }
	    	/**查询剩余额度结束*/
			
			/**获取业务关联批次下的实付总金额和总利息*/
			IqpBatchComponent batchComponent = (IqpBatchComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPBATCHCOMPONENT, context, connection);
			IndexedCollection ic = batchComponent.getBatchTotalInfoBySerno(serno_value);
			KeyedCollection kCollForBatch = null;
			if(!ic.isEmpty()){
				kCollForBatch = (KeyedCollection) ic.get(0);
			}
			if(kCollForBatch!=null){
				kColl.setDataValue("rpddscnt_int", kCollForBatch.getDataValue("int"));//总利息
				kColl.setDataValue("rpay_amt", kCollForBatch.getDataValue("rpay_amt"));//实付金额
				kColl.setDataValue("bill_qnt", kCollForBatch.getDataValue("bill_qnt"));//票据数量
				kColl.setDataValue("rpddscnt_date", kCollForBatch.getDataValue("fore_disc_date"));//贴现日期
				kColl.setDataValue("bill_total_amt", kCollForBatch.getDataValue("bill_total_amt"));//票据总金额
				kColl.setDataValue("rpddscnt_rate", kCollForBatch.getDataValue("rate"));//转贴现利率
				kColl.setDataValue("rebuy_date", kCollForBatch.getDataValue("rebuy_date"));//回购日期
				kColl.setDataValue("rebuy_rate", kCollForBatch.getDataValue("rebuy_rate"));//回购利率
				kColl.setDataValue("rebuy_int", kCollForBatch.getDataValue("rebuy_int"));//总回购利息
			}
			String condition = " where porder_no in (select a.porder_no from iqp_batch_bill_rel a where exists (select 1 from iqp_batch_mng b where a.batch_no = b.batch_no and b.serno = '"+serno_value+"')) ";
			IndexedCollection billIColl = dao.queryList("IqpBillDetail", condition, connection);
			String isEbill = "";
			if(billIColl!=null&&billIColl.size()>0){
				for(int i=0;i<billIColl.size();i++){
					KeyedCollection billKColl = (KeyedCollection) billIColl.get(i);
					if(billKColl!=null&&billKColl.getDataValue("is_ebill")!=null){
						isEbill = billKColl.getDataValue("is_ebill").toString();
					}
				}
			}
			context.put("isEbill", isEbill);
						
			this.putDataElement2Context(kColl, context);
			
			String[] args=new String[] {"prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[]modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});

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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kColl;
	}
}
