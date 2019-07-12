package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
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
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryCtrLoanContDetailOp  extends CMISOperation {

	private final String modelId = "CtrLoanCont";

	private final String cont_no_name = "cont_no";
	
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			/**不用变量，注释不用  2014-03-11  唐顺岩*/
			//BigDecimal total_amt = null;
			//BigDecimal t_total_amt = null;
			//BigDecimal together_remain_amount = null;
			//BigDecimal remain_amount = null;
			//String totAmt =null;
			//String t_totAmt = null;
			/**END */
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			String condition = recordRestrict.judgeQueryRestrict(this.modelId, null, context, connection);
			
			String cont_no_value = null;
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
				cont_no_value = new String(cont_no_value.getBytes("ISO8859-1"),"UTF-8");
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, cont_no_value, connection);
			//从关系表中取授信台账编号 
			kColl = this.getLimitNo(kColl, dao, connection);			
			
			String cus_id = (String) kColl.getDataValue("cus_id");
			String prd_id = (String) kColl.getDataValue("prd_id");
			String serno = (String) kColl.getDataValue("serno");
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			//从cus_base表中获得客户基本信息
			CusBase cus = serviceCus.getCusBaseByCusId(cus_id, context, connection);
			String line = (String)cus.getBelgLine();
//			/** 查询剩余额度 */
//			String limit_ind = (String)kColl.getDataValue("limit_ind");
//			if(limit_ind==null){
//				limit_ind = "0";
//			}
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//	    	LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
//	    	IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//		    //循环剩余额度和一次性剩余额度
//	    	if("2".equals(limit_ind) || "3".equals(limit_ind)){ 
//		    	String limit_acc_no = (String)kColl.getDataValue("limit_acc_no");
//		    	totAmt = (String)serviceLmt.searchLmtAgrAmtByAgrNO(limit_acc_no, "01", connection);
//		    	//获取总的授信额度
//		    	total_amt = BigDecimalUtil.replaceNull(totAmt);
//		    	//获取占用授信额度
//		    	KeyedCollection kCollRel = serviceIqp.getAgrUsedInfoByArgNo(limit_acc_no, "01", connection, context);//01-单一法人 
//		    	remain_amount = caculate(total_amt, kCollRel);
//		    	context.addDataField("remain_amount", remain_amount);
//		    
//		    	//合作方剩余额度
//		    }else if("4".equals(limit_ind)){
//		    	String limit_credit_no = (String)kColl.getDataValue("limit_credit_no");
//		    	//获取总的授信额度
//		    	totAmt = (String)serviceLmt.searchLmtAgrAmtByAgrNO(limit_credit_no, "03", connection);
//		    	total_amt =BigDecimalUtil.replaceNull(totAmt);
//		    	//获取占用授信额度
//		    	KeyedCollection kCollRel = serviceIqp.getAgrUsedInfoByArgNo(limit_credit_no, "03", connection, context);//03-合作方
//		    	together_remain_amount = caculate(total_amt, kCollRel);
//		    	context.addDataField("together_remain_amount", together_remain_amount);
//		   
//		    	//循环剩余额度+合作方剩余额度 || 一次性剩余额度和合作方剩余额度
//		    }else if("5".equals(limit_ind) || "6".equals(limit_ind)){
//		    	String limit_acc_no = (String)kColl.getDataValue("limit_acc_no");
//		    	String limit_credit_no = (String)kColl.getDataValue("limit_credit_no");
//		    	//获取总的授信额度
//		    	totAmt = (String)serviceLmt.searchLmtAgrAmtByAgrNO(limit_acc_no, "01", connection);
//		    	t_totAmt = (String)serviceLmt.searchLmtAgrAmtByAgrNO(limit_credit_no, "03", connection);
//		    	total_amt =BigDecimalUtil.replaceNull(totAmt);
//		    	t_total_amt =BigDecimalUtil.replaceNull(t_totAmt); 
//		    	//获取占用授信额度
//		    	KeyedCollection kCollRel = serviceIqp.getAgrUsedInfoByArgNo(limit_acc_no, "01", connection, context);//01-单一法人
//		    	KeyedCollection kCollInfo = serviceIqp.getAgrUsedInfoByArgNo(limit_credit_no, "03", connection, context);//03-合作方
//		    	remain_amount = caculate(total_amt, kCollRel);
//		    	together_remain_amount = caculate(t_total_amt, kCollInfo);
//		    	context.addDataField("remain_amount", remain_amount); 
//		    	context.addDataField("together_remain_amount", together_remain_amount);
//		    }
//	    	/**查询剩余额度结束*/
	    	
			/** 翻译字典项 */
			Map<String,String> map = new HashMap<String, String>();
			map.put("CtrLoanContSub.agriculture_type", "STD_ZB_FARME");
			map.put("CtrLoanContSub.ensure_project_loan", "STD_ZB_DKGS5"); 
			map.put("CtrLoanContSub.estate_adjust_type", "STD_ZB_TRD_TYPE");
			map.put("CtrLoanContSub.strategy_new_type", "STD_ZB_ZLXXCYLX");   
			map.put("CtrLoanContSub.new_prd_loan", "STD_ZB_XXCYDK");
			//map.put("CtrLoanContSub.green_prd", "STD_ZB_LSCP");
			map.put("CtrLoanContSub.loan_direction", "STD_GB_4754-2011");
			map.put("CtrLoanContSub.loan_belong1", "STD_ZB_DKGS1");
			map.put("CtrLoanContSub.loan_belong2", "STD_ZB_DKGS2");
			map.put("CtrLoanContSub.loan_belong3", "STD_ZB_DKGS3");
			if("BL100".equals(line) || "BL200".equals(line)){
				map.put("CtrLoanContSub.loan_type", "STD_COM_POSITIONTYPE");
			}else if("BL300".equals(line)){
				map.put("CtrLoanContSub.loan_type", "STD_PER_POSITIONTYPE");
			}
			
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			
			
			String[] args=new String[] {"cus_id","cus_id","repay_type","repay_type","prd_id" };
			String[] modelIds=new String[]{"CusBase","CusBase","PrdRepayMode","PrdRepayMode","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","cus_id","repay_mode_id","repay_mode_id","prdid"}; 
			String[] fieldName=new String[]{"cus_name","belg_line","repay_mode_dec","repay_mode_type","prdname"};
			String[] resultName = new String[] { "cus_id_displayname","belg_line","repay_type_displayname","repay_mode_type","prd_id_displayname"};
		    //详细信息翻译时调用	
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			KeyedCollection kCollSub = (KeyedCollection)kColl.getDataElement("CtrLoanContSub");
			SystemTransUtils.dealPointName(kCollSub, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			String belg_line =(String)kColl.getDataValue("belg_line");
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
			this.putDataElement2Context(kColl, context);
			context.addDataField("belg_line", belg_line);
			context.addDataField("repay_type", kColl.getDataValue("CtrLoanContSub.repay_type"));
			
			/** 如果是信用证业务，则查询溢装比例 */
			if("700020".equals(prd_id) || "700021".equals(prd_id)){
				KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
				if(kCollCredit != null){
					BigDecimal floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
					context.put("floodact_perc", floodact_perc);
				}else{
					context.put("floodact_perc", "0");
				}
			}else{
				context.put("floodact_perc", "0");
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
			String cont_no = (String)kColl.getDataValue("cont_no");
			String limit_ind = (String)kColl.getDataValue("limit_ind");
			if("1".equals(limit_ind)){
				return kColl;
			}else if("2".equals(limit_ind) || "3".equals(limit_ind)){
                String condition = "where cont_no='"+cont_no+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                kColl.put("limit_acc_no", agr_no);
                return kColl;
			}else if("4".equals(limit_ind)){
				String condition = "where cont_no='"+cont_no+"'";
                KeyedCollection kCollRel = dao.queryFirst("RBusLmtcreditInfo", null, condition, connection);
                String agr_no = (String)kCollRel.getDataValue("agr_no");
                kColl.put("limit_credit_no", agr_no);
                return kColl;
			}else if("5".equals(limit_ind) || "6".equals(limit_ind)){
				String condition = "where cont_no='"+cont_no+"'";
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
