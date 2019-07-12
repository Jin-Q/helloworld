package com.yucheng.cmis.platform.riskmanage.op.risklist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class CheckCtrLimitAmt4CtrLimitApp implements RiskManageInterface {

	private final String modelId = "CtrLimitApp";
	private final String modelIdRel = "CtrLimitLmtRel";
	private final String modelIdLmt = "LmtAgrDetails";
	public Map<String, String> getResultMap(String tableName, String serno, Context context,Connection connection) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		Double lmt_code_enable_amt =0.00;
		Double app_amt =0.00;
		try {
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			//本次出账金额
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			app_amt = Double.valueOf(kColl.getDataValue("app_amt")+"");
			
			//
			String lmt_code_str = "";
			String condition ="where limit_serno='"+serno+"'";
			IndexedCollection iColl = dao.queryList(modelIdRel, condition, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollRel = (KeyedCollection)iColl.get(i);
				String lmt_code = (String)kCollRel.getDataValue("lmt_code_no");
				lmt_code_str += ("'"+lmt_code+"',");
			}
			if(lmt_code_str.trim().length() > 0){
				lmt_code_str = lmt_code_str.substring(0, lmt_code_str.length()-1);
			}
			if(lmt_code_str == null || lmt_code_str.trim() == ""){
				lmt_code_str = "''";
			}
			String conditionStr = "where limit_code in("+lmt_code_str+")";
			IndexedCollection iCollLmt = dao.queryList(modelIdLmt, conditionStr, connection);
			
			/** 遍历取值 */
			if(iColl != null|| iColl.size() > 0){
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kc = (KeyedCollection)iColl.get(i);
					String lmt_code = (String)kc.getDataValue("lmt_code_no");
					if(iCollLmt != null|| iCollLmt.size() > 0){
						for(int j=0;j<iCollLmt.size();j++){
							KeyedCollection kc1 = (KeyedCollection)iCollLmt.get(j);
							String lmt_code1 = (String)kc1.getDataValue("limit_code");
							if(lmt_code.equals(lmt_code1)){
								String enable_amt = (String)kc1.getDataValue("enable_amt");//启用金额
								String froze_amt = (String)kc1.getDataValue("froze_amt");//冻结金额
								
								IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
								KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo(lmt_code, "01", connection, context);
								String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString(); 
								//去空
								if(enable_amt == null || "".equals(enable_amt)){
									enable_amt = "0";
								}
								if(froze_amt == null || "".equals(froze_amt)){
									froze_amt = "0";
								}
								if(lmt_amt == null || "".equals(lmt_amt)){
									lmt_amt = "0";
								}
								//授信剩余金额 = 启动金额-冻结金额-业务占用授信金额
								lmt_code_enable_amt += (Double.parseDouble(enable_amt) - Double.parseDouble(froze_amt) - Double.parseDouble(lmt_amt));
							}
						}
					}
				}
			}
			
			if(app_amt>(lmt_code_enable_amt)){
				param.put("OUT_是否通过","不通过");
				param.put("OUT_提示信息","额度合同申请金额大于所占用授信明细的授信余额之和");
			}else{
				param.put("OUT_是否通过","通过");
				param.put("OUT_提示信息","所占用授信明细的授信余额之和可以覆盖申请金额，检查通过！");
			}
			return param;
		} catch (Exception e) {
			throw new Exception("合同占用授信余额之和与申请金额比较失败!");
		}
	}

}
