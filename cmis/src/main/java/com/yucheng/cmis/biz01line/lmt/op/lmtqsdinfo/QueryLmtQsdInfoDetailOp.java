package com.yucheng.cmis.biz01line.lmt.op.lmtqsdinfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class QueryLmtQsdInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtQsdInfo";
	

	private final String serno_name = "serno";
	private final String org_limit_code_name = "org_limit_code";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			String org_limit_code_value = null;
			try {
				org_limit_code_value = (String)context.getDataValue(org_limit_code_name);
			} catch (Exception e) {}
			if(org_limit_code_value == null || org_limit_code_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+org_limit_code_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno_value);
			pkMap.put("org_limit_code",org_limit_code_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			
			String cus_id = (String) context.getDataValue("cus_id");
			kColl.setDataValue("serno", serno_value);
			kColl.setDataValue("org_limit_code", org_limit_code_value);
			kColl.setDataValue("cus_id", cus_id);
			
			//获取客户半年日均
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			String spouse_cus_id ="";
			IndexedCollection iCollCus = serviceCus.getIndivSocRel(cus_id, "1", connection);
			if(iCollCus.size()>0){
               KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(0);
               spouse_cus_id = (String)kCollCus.getDataValue("cus_id_rel");
			}
			KeyedCollection retKColl = null;
			KeyedCollection BODY = new KeyedCollection("BODY");
			ESBServiceInterface serviceEsb = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			BigDecimal totalAmt = null;
			try{
				retKColl = serviceEsb.tradeBNRJ(cus_id, spouse_cus_id, context, connection);
				if(TagUtil.haveSuccess(retKColl, context)){//成功
					BODY = (KeyedCollection)retKColl.getDataElement("BODY");
					BigDecimal day_eql_bal = BigDecimalUtil.replaceNull(BODY.getDataValue("DAY_EQL_BAL"));
					BigDecimal mate_day_eql_bal = BigDecimalUtil.replaceNull(BODY.getDataValue("MATE_DAY_EQL_BAL"));
				    totalAmt = day_eql_bal.add(mate_day_eql_bal);
				    kColl.setDataValue("cus_half_year_aday", totalAmt);
				}else{
					throw new EMPException("ESB通讯接口【获取半年日均】交易失败："+(String)retKColl.getDataValue("RET_MSG"));
				}
			}catch(Exception e){
				throw new EMPException("ESB通讯接口【获取半年日均】交易失败："+e.getMessage());
			}
			
			//计算综合贡献度
			BigDecimal contribute = new BigDecimal(0.0);
			BigDecimal crd_amt = (BigDecimal)SqlClient.queryFirst("queryCrdAmtByOrgLimitCodeForQsd", org_limit_code_value, null, connection);
			if(crd_amt!=null){
				contribute = totalAmt.divide(crd_amt,RoundingMode.DOWN);
			}
			kColl.setDataValue("cus_inte_contribute", contribute);
			
			this.putDataElement2Context(kColl, context);
			
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
}
