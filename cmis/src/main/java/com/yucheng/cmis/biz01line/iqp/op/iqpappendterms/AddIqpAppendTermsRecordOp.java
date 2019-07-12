package com.yucheng.cmis.biz01line.iqp.op.iqpappendterms;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpAppendTermsRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpAppendTerms";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String append_terms_pk = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			kColl.put("append_terms_pk", append_terms_pk);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				IndexedCollection iColl = dao.queryList("IqpAppendTermsTmp", "where modify_rel_serno='"+modify_rel_serno+"'", connection);//附加条款费用列表数据
				IndexedCollection IATH = dao.queryList("IqpAppendTermsHis", "where modify_rel_serno ='"+modify_rel_serno+"'", connection);
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
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			
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
			dao.insert(kColl, connection);
			context.addDataField("flag", "success");
			context.addDataField("msg","");
			
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
