package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryContBalByArgno extends CMISOperation {
	@Override
	public String doExecute(Context context) throws EMPException {
		/**将业务申请数据插入合同信息，需要包含从表以及相关tab页签数据*/
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String agr_no = "";
			String lmt_type = "";
			try {
				agr_no = (String)context.getDataValue("agr_no");
				lmt_type = (String)context.getDataValue("lmt_type");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//			IndexedCollection inColl = service.getAgrUsedInfoList(agr_no,lmt_type,connection);
			IndexedCollection inColl = new IndexedCollection();
			double balanceVal = 0;
			if(inColl.size()>0){
				KeyedCollection iKcoll = null;
				for(int i=0;i<inColl.size();i++){
					iKcoll = (KeyedCollection)inColl.get(i);
					balanceVal += new BigDecimal(iKcoll.getDataValue("cont_balance").toString()).doubleValue();
				}
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
			context.addDataField("balanceVal", balanceVal);
		} catch (Exception e) {
			context.addDataField("flag", "fail");
			context.addDataField("msg", "查询失败！");
			context.addDataField("balanceVal", 0);
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
