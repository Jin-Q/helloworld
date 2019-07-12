package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class GetCusHalfYearAmtOp extends CMISOperation {
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = null;
			String prd_id = null;
			String flag="success";
			String mes = "";
			try {
				cus_id = (String)context.getDataValue("cus_id");
				prd_id = (String)context.getDataValue("prd_id");
			} catch (Exception e) {}
			if(cus_id == null || cus_id.length()==0)
				throw new EMPJDBCException("The values cus_id cannot be empty!");
				
			/**个人客户查询半年日均 -----start-----*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			ESBServiceInterface serviceEsb = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			PrdBasicinfo prdBasicinfo = service.getPrdBasicinfoList(prd_id, connection);
			String supcatalog = (String)prdBasicinfo.getSupcatalog();
			if("PRD20120802659".equals(supcatalog)){//如果是个人经营性贷款
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
					    context.addDataField("totalAmt", totalAmt+"");
					}else{
						flag = "error";
						mes =(String)retKColl.getDataValue("RET_MSG");
						context.addDataField("totalAmt", 0);
					}
				}catch(Exception e){
					flag = "error";
					mes = "ESB通讯接口【获取半年日均】交易失败："+e.getMessage();
				}
				context.addDataField("flag", flag);
				context.addDataField("mes", mes);
			}
			/**个人客户查询半年日均 -----end-----*/
				
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
