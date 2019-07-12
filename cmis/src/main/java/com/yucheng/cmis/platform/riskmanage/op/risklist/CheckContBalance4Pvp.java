package com.yucheng.cmis.platform.riskmanage.op.risklist;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;

public class CheckContBalance4Pvp implements RiskManageInterface {

	private final String modelId = "PvpLoanApp";
	
	public Map<String, String> getResultMap(String tableName, String serno, Context context,Connection connection) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		String modelIdCont = "CtrLoanCont";
		Double pvp_amt_other =0.00;
		Double pvp_amt_all =0.00;
		Double pvp_amt =0.00;
		Double cont_amt =0.00;
		Double cont_balance=0.00;
		String cont_end_date = null;
		String openDay = (String)context.getDataValue("OPENDAY");
		Date contDate = null;
		Date sysDate = null;
		try {
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//本次出账金额
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			pvp_amt = Double.valueOf(kColl.getDataValue("pvp_amt")+"");
			String cont_no = (String)kColl.getDataValue("cont_no");
			String in_acct_br_id = (String)kColl.getDataValue("in_acct_br_id");
			String prd_id = (String)kColl.getDataValue("prd_id");
			
			//统计审批中的出账占用的合同额度
			String condition ="where cont_no='"+cont_no+"' and approve_status='111'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollPvp = (KeyedCollection)iColl.get(i);
				pvp_amt_other = Double.valueOf(kCollPvp.getDataValue("pvp_amt")+"");
				pvp_amt_all += pvp_amt_other;
			}
			//下面三种产品为转贴现产品，此时合同表模型为转贴现的合同表模型
			if("300024".equals(prd_id) || "300023".equals(prd_id) || "300022".equals(prd_id)){
				modelIdCont = "CtrRpddscntCont";
				KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
				cont_amt = Double.valueOf(kCollCont.getDataValue("bill_total_amt")+"");
			//产品为资产转让业务时，此时合同表模型为资产转让的合同表模型
			}else if("600020".equals(prd_id)){
				modelIdCont = "CtrAssetstrsfCont";
				KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
				cont_amt = Double.valueOf(kCollCont.getDataValue("takeover_total_amt")+"");
			//产品为资产流转业务时，此时合同表模型为资产流转的合同表模型
			}else if("600021".equals(prd_id)){
				modelIdCont = "CtrAssetTransCont";
				KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
				cont_amt = Double.valueOf(kCollCont.getDataValue("trans_amt")+"");
			//产品为资产证券化业务时，此时合同表模型为资产证券化的合同表模型
			}else if("600022".equals(prd_id)){
				modelIdCont = "CtrAssetProCont";
				KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
				cont_amt = Double.valueOf(kCollCont.getDataValue("pro_amt")+"");
			}else{
				modelIdCont = "CtrLoanCont"; 
				KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
				cont_amt = Double.valueOf(kCollCont.getDataValue("cont_amt")+"");
				cont_balance = Double.valueOf(kCollCont.getDataValue("cont_balance")+"");
				
				if(!"300021".equals(prd_id) && !"300020".equals(prd_id)){//商票，银票 贴现不检查合同到期日
					cont_end_date = (String)kCollCont.getDataValue("cont_end_date");
					contDate = sdf.parse(cont_end_date);
					sysDate = sdf.parse(openDay);
					if(contDate.before(sysDate)){
						param.put("OUT_是否通过","不通过");
						param.put("OUT_提示信息","合同到期日为["+cont_end_date+"],已过期!");
						return param;
					}
				}
				
				String firstPayDate =(String)kColl.getDataValue("first_pay_date");
				if(firstPayDate!=null&&!"".equals(firstPayDate)){
					Date firstPay=  sdf.parse(firstPayDate);
					cont_end_date = (String)kCollCont.getDataValue("cont_end_date");
					contDate = sdf.parse(cont_end_date);
					sysDate = sdf.parse(openDay);
					if(firstPay.before(sysDate)){
						param.put("OUT_是否通过","不通过");
						param.put("OUT_提示信息","首次还款日不得早于当前日期!");
						return param;
					}else if(contDate.before(firstPay)){
						param.put("OUT_是否通过","不通过");
						param.put("OUT_提示信息","首次还款日不得晚于合同到期日!");
						return param;
					}
				}else{
					param.put("OUT_是否通过","不通过");
					param.put("OUT_提示信息","首次还款日未输入!");
					return param;
				}
			}				
			//检查合同到期日是否超过系统营业日
			
			//检查首次还款日
			
			
			if(in_acct_br_id != null && !"".equals(in_acct_br_id)){
				
				if("100039".equals(prd_id)&&pvp_amt>(cont_balance-pvp_amt_all)){//循环贷款时的判断
					param.put("OUT_是否通过","不通过");
					param.put("OUT_提示信息","出账金额大于合同可用余额");
			    }else if(pvp_amt>(cont_amt-pvp_amt_all)){
					param.put("OUT_是否通过","不通过");
					param.put("OUT_提示信息","出账金额大于合同可用余额");
				}else{
					param.put("OUT_是否通过","通过");
					param.put("OUT_提示信息","出账金额检查通过!");
				}
			}else{
				param.put("OUT_是否通过","不通过");
				param.put("OUT_提示信息","未录入入账机构");
			}
			return param;
		} catch (Exception e) {
			throw new Exception("合同可用余额和出账金额比较失败!");
		}
	}

}
