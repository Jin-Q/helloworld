package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class AccLoanFlagExpBatchToExcelOp extends CMISOperation {

	private final String modelId = "AccLoan";
	
	//台账标志
	private  String returnFlag;

	public String doExecute(Context context) throws EMPException {
/*		贸易融资台账     flag=ctrLimitAcc
		保函台账         flag=guarantAcc
		委托贷款台账     flag=csgnLoanAcc
		国内保理台账     flag=interFactAcc
		银行信贷证明台账 flag=proveAcc
		贷款承诺台账     flag=promissoryAcc
		贷款意向台账     flag=adviceAcc
		信托公司贷款台账 flag=isTrustAcc
		逾期台账 flag=isTrustAcc*/
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String flag=null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			try {
				flag = (String)context.getDataValue("flag");
			} catch (Exception e) {
				throw new Exception("参数异常，联系后台管理员!");
			}
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			conditionStr +=" order by bill_no desc";
				
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();    
			list.add("serno");
			list.add("cont_no");
			list.add("prd_id");
			list.add("cus_id");
			list.add("loan_balance");
			list.add("loan_amt");
			list.add("distr_date");
			list.add("end_date");
			list.add("acc_status");
			list.add("fina_br_id");
			list.add("bill_no");
			list.add("cur_type");
			list.add("manager_br_id");
			list.add("five_class");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			/**add by lisj 2015-10-19  需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			//添加委托管理费率、执行利率（年）、信托项目名称、贷款投向字段
			if("isTrustAcc".equals(flag)){//信托公司贷款台账
				for(Iterator<KeyedCollection> iterator =iColl.iterator();iterator.hasNext();){
					KeyedCollection  temp = (KeyedCollection)iterator.next();
					KeyedCollection kColl4CLC = dao.queryAllDetail("CtrLoanCont", (String) temp.getDataValue("cont_no"), connection);
					KeyedCollection kColl4CLCS = (KeyedCollection) kColl4CLC.getDataElement("CtrLoanContSub");
					String serno  = (String) kColl4CLC.getDataValue("serno");
					String trust_pro_name = (String) kColl4CLC.getDataValue("trust_pro_name");//信托项目名称
					String ir_accord_type = (String) kColl4CLCS.getDataValue("ir_accord_type");//利率依据方式
					BigDecimal reality_ir_y = new BigDecimal(0);//执行利率（年）
					/** 翻译字典项 */	
					Map<String,String> map = new HashMap<String, String>();
					map.put("loan_direction", "STD_GB_4754-2011");
					SInfoUtils.addPopName(kColl4CLCS, map, service);
					String loan_direction_displayname = (String) kColl4CLCS.getDataValue("loan_direction_displayname");//贷款投向
					KeyedCollection kColl4ITFI = dao.queryDetail("IqpTrustFeeInfo", serno, connection);
					BigDecimal mm_fee_rate = BigDecimalUtil.replaceNull(kColl4ITFI.getDataValue("mm_fee_rate"));//委托管理费率
					//将小数转化成百分比
					NumberFormat nt = NumberFormat.getPercentInstance();
					nt.setMaximumFractionDigits(3);				
					temp.put("trust_pro_name", trust_pro_name);
					temp.put("loan_direction_displayname", loan_direction_displayname);
					temp.put("mm_fee_rate", nt.format(mm_fee_rate));
					//03表示不计息，执行利率为0%
					if(ir_accord_type!=null && !"".equals(ir_accord_type) && !"03".equals(ir_accord_type)){
						reality_ir_y = BigDecimalUtil.replaceNull(kColl4CLCS.getDataValue("reality_ir_y"));
					}
					temp.put("reality_ir_y", nt.format(reality_ir_y));		
				}	
			}else if("csgnLoanAcc".equals(flag)){//委托贷款台账，添加委托人账户名称、手续费率、执行利率（年）、贷款投向字段
				for(Iterator<KeyedCollection> iterator =iColl.iterator();iterator.hasNext();){
					KeyedCollection  temp = (KeyedCollection)iterator.next();
					KeyedCollection kColl4CLC = dao.queryAllDetail("CtrLoanCont", (String) temp.getDataValue("cont_no"), connection);
					KeyedCollection kColl4CLCS = (KeyedCollection) kColl4CLC.getDataElement("CtrLoanContSub");
					String serno  = (String) kColl4CLC.getDataValue("serno");
					String ir_accord_type = (String) kColl4CLCS.getDataValue("ir_accord_type");//利率依据方式
					BigDecimal reality_ir_y = new BigDecimal(0);//执行利率（年）
					/** 翻译字典项 */	
					Map<String,String> map = new HashMap<String, String>();
					map.put("loan_direction", "STD_GB_4754-2011");
					SInfoUtils.addPopName(kColl4CLCS, map, service);
					String loan_direction_displayname = (String) kColl4CLCS.getDataValue("loan_direction_displayname");//贷款投向
					KeyedCollection kColl4ICLI = dao.queryDetail("IqpCsgnLoanInfo", serno, connection);
					BigDecimal chrg_rate = BigDecimalUtil.replaceNull(kColl4ICLI.getDataValue("chrg_rate"));//手续费率
					String csgn_acct_name = (String) kColl4ICLI.getDataValue("csgn_acct_name");//委托人一般账户名
					//将小数转化成百分比
					NumberFormat nt = NumberFormat.getPercentInstance();
					nt.setMaximumFractionDigits(3);				
					temp.put("csgn_acct_name", csgn_acct_name);
					temp.put("loan_direction_displayname", loan_direction_displayname);
					temp.put("chrg_rate", nt.format(chrg_rate));
					//03表示不计息，执行利率为0%
					if(ir_accord_type!=null && !"".equals(ir_accord_type) && !"03".equals(ir_accord_type)){
						reality_ir_y = BigDecimalUtil.replaceNull(kColl4CLCS.getDataValue("reality_ir_y"));
					}
					temp.put("reality_ir_y", nt.format(reality_ir_y));		
				}
			}
			/**add by lisj 2015-10-19  需求编号：XD150409029 信贷保函及资产模块改造需求 end**/
			/**添加客户条线、委托贷款类型字段 2014-08-07  邓亚辉*/
			String[] args=new String[] {"cus_id","cus_id","prd_id" ,"cont_no","cont_no"};
			String[] modelIds=new String[]{"CusBase","CusBase","PrdBasicinfo","CtrLoanCont","CtrLoanContSub"};
			String[]modelForeign=new String[]{"cus_id","cus_id","prdid","cont_no","cont_no"};
			String[] fieldName=new String[]{"cus_name","belg_line","prdname","serno","principal_loan_typ"};
			String[] resultName=new String[]{"cus_id_displayname","belg_line","prd_id_displayname","fount_serno","principal_loan_typ"};
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
		    
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});  
			this.putDataElement2Context(iColl, context);
			returnFlag = flag;
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnFlag;
	}

}
