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
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpLoanAppDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpLoanApp";
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
		
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);

			KeyedCollection kColl = dao.queryAllDetail(modelId, serno_value, connection);
			KeyedCollection kCollSub = (KeyedCollection)kColl.getDataElement("IqpLoanAppSub");
			//从关系表中取授信台账编号 
			kColl = this.getLimitNo(kColl, dao, connection);
			
			String prd_id = (String)kColl.getDataValue("prd_id");
			String cus_id = (String)kColl.getDataValue("cus_id");
			String is_close_loan = (String)kCollSub.getDataValue("is_close_loan");
			KeyedCollection prdkColl = dao.queryDetail("PrdBasicinfo", prd_id, connection);
			String currency =(String)prdkColl.getDataValue("currency");
			String guarway =(String)prdkColl.getDataValue("guarway");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			//从cus_base表中获得客户基本信息
			CusBase cus = serviceCus.getCusBaseByCusId(cus_id, context, connection);
			String line = (String)cus.getBelgLine();
			/** 查询剩余额度 */
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
			map.put("IqpLoanAppSub.agriculture_type", "STD_ZB_FARME");
			map.put("IqpLoanAppSub.ensure_project_loan", "STD_ZB_DKGS5");
			map.put("IqpLoanAppSub.estate_adjust_type", "STD_ZB_TRD_TYPE");
			map.put("IqpLoanAppSub.strategy_new_loan", "STD_ZB_ZLXXCYLX");
			map.put("IqpLoanAppSub.new_prd_loan", "STD_ZB_XXCYDK");
			//map.put("IqpLoanAppSub.green_prd", "STD_ZB_LSCP");
			map.put("IqpLoanAppSub.loan_direction", "STD_GB_4754-2011");
			map.put("IqpLoanAppSub.loan_belong1", "STD_ZB_DKGS1");
			map.put("IqpLoanAppSub.loan_belong2", "STD_ZB_DKGS2");
			map.put("IqpLoanAppSub.loan_belong3", "STD_ZB_DKGS3");
			if("BL100".equals(line) || "BL200".equals(line)){
				map.put("IqpLoanAppSub.loan_type", "STD_COM_POSITIONTYPE");
			}else if("BL300".equals(line)){
				map.put("IqpLoanAppSub.loan_type", "STD_PER_POSITIONTYPE");
			}
			
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			
			String[] args=new String[] {"cus_id","cus_id","repay_type","repay_type" };
			String[] modelIds=new String[]{"CusBase","CusBase","PrdRepayMode","PrdRepayMode"};
			String[]modelForeign=new String[]{"cus_id","cus_id","repay_mode_id","repay_mode_id"}; 
			String[] fieldName=new String[]{"cus_name","belg_line","repay_mode_dec","repay_mode_type"};
			String[] resultName = new String[] { "cus_id_displayname","belg_line","repay_type_displayname","repay_mode_type"};
		    //详细信息翻译时调用	
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			SystemTransUtils.dealPointName(kCollSub, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			String belg_line =(String)kColl.getDataValue("belg_line");  
			
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(prdkColl, context);
			context.addDataField("currency", currency);
			context.addDataField("guarway", guarway);
			context.addDataField("belg_line", belg_line);
			context.addDataField("repay_type", kColl.getDataValue("IqpLoanAppSub.repay_type"));
			/**add by lisj 2015-10-20 需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
			context.put("prd_id", prd_id);
			/**add by lisj 2015-10-20 需求编号：XD150409029 信贷保函及资产模块改造需求 end**/
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
			
			if(prd_id.equals("200024")){
				//获取明细金额数量汇总
				String condition = " where serno = '"+serno_value+"'";
				IndexedCollection accpIColl = dao.queryList("IqpAccpDetail", condition, connection);
				BigDecimal drft_amt_total = new BigDecimal("0");
				for(int i=0;i<accpIColl.size();i++){
					KeyedCollection accpKColl = (KeyedCollection) accpIColl.get(i);
					BigDecimal drft_amt = new BigDecimal(accpKColl.getDataValue("drft_amt")+"");
					drft_amt_total = drft_amt_total.add(drft_amt);
				}
				kColl.setDataValue("apply_amount", drft_amt_total);
				KeyedCollection kColl4Accp = dao.queryAllDetail("IqpAccAccp", serno_value, connection);
				if(kColl4Accp!=null&&kColl4Accp.containsKey("is_elec_bill")&&kColl4Accp.getDataValue("is_elec_bill")!=null){
					if("1".equals(kColl4Accp.getDataValue("is_elec_bill"))){
						context.put("is_elec_bill", "1");
					}else{
						context.put("is_elec_bill", "2");
					}
				}
			}
			/**个人客户查询半年日均 -----start-----*/
			String flag="success";
			String mes = "";
			PrdServiceInterface servicePrd = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			ESBServiceInterface serviceEsb = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			PrdBasicinfo prdBasicinfo = servicePrd.getPrdBasicinfoList(prd_id, connection);
			String supcatalog = (String)prdBasicinfo.getSupcatalog();
			context.put("supcatalog", supcatalog);
			if("PRD20120802659".equals(supcatalog) && "1".equals(is_close_loan) ){//如果是个人经营性贷款
				String spouse_cus_id ="";
				IndexedCollection iCollCus = serviceCus.getIndivSocRel(cus_id, "1", connection);
				if(iCollCus.size()>0){
                   KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(0);
                   spouse_cus_id = (String)kCollCus.getDataValue("cus_id_rel");
				}
				/*** 调用核心实时接口半年日均 ***/
				KeyedCollection retKColl = null;
				KeyedCollection BODY = new KeyedCollection("BODY");
				try{
					retKColl = serviceEsb.tradeBNRJ(cus_id, spouse_cus_id, context, connection);
					if(TagUtil.haveSuccess(retKColl, context)){//成功
						BODY = (KeyedCollection)retKColl.getDataElement("BODY");
						BigDecimal day_eql_bal = BigDecimalUtil.replaceNull(BODY.getDataValue("DAY_EQL_BAL"));
						BigDecimal mate_day_eql_bal = BigDecimalUtil.replaceNull(BODY.getDataValue("MATE_DAY_EQL_BAL"));
					    BigDecimal totalAmt = day_eql_bal.add(mate_day_eql_bal);
					    context.put("totalAmt", totalAmt+"");
					    context.put("supcatalog", supcatalog);
					}else{
						flag = "error";
						mes =(String)retKColl.getDataValue("RET_MSG");
					}
				}catch(Exception e){
					flag = "error";
					mes = "ESB通讯接口【获取半年日均】交易失败："+e.getMessage();
				}
				context.put("flag", flag);
				context.put("mes", mes);
			}
			/**个人客户查询半年日均 -----end-----*/
			/** 如果是信用证业务，则查询溢装比例 */
			if("700020".equals(prd_id) || "700021".equals(prd_id)){
				KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno_value, connection);
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
