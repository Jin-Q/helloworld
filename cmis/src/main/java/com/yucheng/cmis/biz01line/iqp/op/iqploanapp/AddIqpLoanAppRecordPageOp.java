package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class AddIqpLoanAppRecordPageOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpLoanApp";
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			String flag="success";
			String mes = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String prdid = (String)kColl.getDataValue("prd_id");
				/**票据贴现业务插入贴现从表*/
				if(prdid.equals("300021")){
					KeyedCollection kc = new KeyedCollection();
					kc.addDataField("bill_type", "100");
					kc.setName("IqpDiscApp");
					this.putDataElement2Context(kc, context);
				}else if(prdid.equals("300020")){
					KeyedCollection kc = new KeyedCollection();
					kc.addDataField("bill_type", "200");
					kc.setName("IqpDiscApp");
					this.putDataElement2Context(kc, context);
				}
				TableModelDAO dao = this.getTableModelDAO(context);
				/**个人客户查询半年日均 -----start-----*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
				PrdBasicinfo prdBasicinfo = service.getPrdBasicinfoList(prdid, connection);
				String supcatalog = (String)prdBasicinfo.getSupcatalog();
				context.addDataField("supcatalog", supcatalog);
				/**个人客户查询半年日均 -----end-----*/
				
				
				String[] args=new String[] {"cus_id","cus_id","prd_id"};
				String[] modelIds=new String[]{"CusBase","CusBase","PrdBasicinfo"};
				String[]modelForeign=new String[]{"cus_id","cus_id","prdid"}; 
				String[] fieldName=new String[]{"cus_name","belg_line","prdname"};
				String[] resultName = new String[] { "cus_id_displayname","belg_line","prd_id_displayname"};
			    //详细信息翻译时调用
				SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
				/** 组织机构、登记机构翻译 */
				SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
				SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
				
				KeyedCollection prdkColl = dao.queryDetail("PrdBasicinfo", prdid, connection);
				String currency =(String)prdkColl.getDataValue("currency");
				String guarway =(String)prdkColl.getDataValue("guarway");  
				String belg_line =(String)kColl.getDataValue("belg_line");  
				
				this.putDataElement2Context(kColl, context);
				context.addDataField("currency", currency);
				context.addDataField("guarway", guarway);
				context.addDataField("belg_line", belg_line);
			
				if(prdid.equals("300021")||prdid.equals("300020")){
					return "disc";
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
}
