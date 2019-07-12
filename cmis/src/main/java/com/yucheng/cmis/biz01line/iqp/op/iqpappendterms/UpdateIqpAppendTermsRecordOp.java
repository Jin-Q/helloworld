package com.yucheng.cmis.biz01line.iqp.op.iqpappendterms;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class UpdateIqpAppendTermsRecordOp extends CMISOperation {
	

	private final String modelId = "IqpAppendTerms";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				IndexedCollection iColl = dao.queryList("IqpAppendTermsTmp", "where modify_rel_serno='"+modify_rel_serno+"'", connection);//附加条款费用列表数据
				IndexedCollection IATH = dao.queryList("IqpAppendTermsHis", "where modify_rel_serno='"+modify_rel_serno+"'", connection);
				if(IATH ==null || IATH.size()<=0){
					if(iColl!=null && iColl.size()>0){					
						for(int i=0;i<iColl.size();i++){
							KeyedCollection temp = (KeyedCollection) iColl.get(i);
							temp.put("modify_rel_serno", modify_rel_serno);
							temp.setName("IqpAppendTermsHis");
							dao.insert(temp, connection);
						}
					}
				}
				kColl.put("modify_rel_serno", modify_rel_serno);
				kColl.setName("IqpAppendTermsTmp");
			}

			//如果使用固定金额，保存时系统自动计算手续费率并保存
			String collect_type = (String) kColl.getDataValue("collect_type");
			String serno = (String) kColl.getDataValue("serno");
			KeyedCollection iqpkc = dao.queryDetail("IqpLoanApp", serno, connection);
			/**modified by lisj 2015-3-6 修复附加条款费用费率反显BUG，于2015-3-12上线 begin**/
			String fee_code = (String) kColl.getDataValue("fee_code");//费用描述
			if(collect_type!=null&&collect_type.equals("01")){
				if(fee_code!=null && ("01".equals(fee_code) || "09".equals(fee_code))){
					//获取委托人信息的手续费率
					KeyedCollection iCsgnLoanInfo = dao.queryDetail("IqpCsgnLoanInfo", serno, connection);
					BigDecimal chrg_rate = new BigDecimal((String) iCsgnLoanInfo.getDataValue("chrg_rate"));
					BigDecimal fee_amt = new BigDecimal((String)kColl.getDataValue("fee_amt"));
					BigDecimal apply_amount = new BigDecimal((String)iqpkc.getDataValue("apply_amount"));
					BigDecimal fee_rate = (fee_amt.divide(apply_amount,6,BigDecimal.ROUND_HALF_UP)).divide(chrg_rate, BigDecimal.ROUND_HALF_UP);
					kColl.setDataValue("fee_rate", fee_rate);
					
				}else{
					BigDecimal fee_amt = new BigDecimal((String)kColl.getDataValue("fee_amt"));
					BigDecimal apply_amount = new BigDecimal((String)iqpkc.getDataValue("apply_amount"));
					BigDecimal fee_rate = fee_amt.divide(apply_amount,6,BigDecimal.ROUND_HALF_UP);
					kColl.setDataValue("fee_rate", fee_rate);
				}
			}
			/**modified by lisj 2015-3-6 修复附加条款费用费率反显BUG，于2015-3-12上线 end**/
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){				
				Map<String,String> ValueMap = new HashedMap();
				ValueMap.put("chrg_date", (String) kColl.getDataValue("chrg_date"));
				ValueMap.put("chrg_freq", (String) kColl.getDataValue("chrg_freq"));
				ValueMap.put("collect_type", collect_type);
				ValueMap.put("fee_acct_no", (String) kColl.getDataValue("fee_acct_no"));
				ValueMap.put("fee_amt", (String) kColl.getDataValue("fee_amt"));
				ValueMap.put("fee_code", (String) kColl.getDataValue("fee_code"));
				ValueMap.put("fee_cur_type", (String) kColl.getDataValue("fee_cur_type"));
				if(!"01".equals(collect_type)){
					ValueMap.put("fee_rate", (String) kColl.getDataValue("fee_rate"));		
				}else{
					ValueMap.put("fee_rate", "");
				}
				ValueMap.put("fee_type", (String) kColl.getDataValue("fee_type"));
				ValueMap.put("is_cycle_chrg", (String) kColl.getDataValue("is_cycle_chrg"));
				KeyedCollection paramKcoll = new KeyedCollection();
				paramKcoll.put("modify_rel_serno", (String) kColl.getDataValue("modify_rel_serno"));
				paramKcoll.put("append_terms_pk", (String) kColl.getDataValue("append_terms_pk"));
				try {
					int count = SqlClient.update("updateIqpAppendTermsTmpByM", paramKcoll, ValueMap, null, connection);
					if(count!=1){
						throw new EMPException("Update Failed! Record Count: " + count);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}else{
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
			}
            context.addDataField("flag", "success");
            context.addDataField("msg","");
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg","保存失败，原因:"+ee.getMessage());
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
