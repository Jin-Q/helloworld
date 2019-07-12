package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtIntbankDetailListOp extends CMISOperation {
	private final String modelId = "CtrLoanCont";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {   
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {} 
			String conditionStr =TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			
			String agr_no = "";
			if(context.containsKey("agr_no") && null!=context.getDataValue("agr_no") && !"".equals(context.getDataValue("agr_no"))){
				agr_no = (String)context.getDataValue("agr_no");
			}
			
			int size = 1000;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			IndexedCollection iCollCont = service.getHistoryContByLimitArgNo(agr_no, conditionStr, context, connection);
			IndexedCollection iCollIqp = service.getIqpByLimitAgrNoForSame(agr_no, pageInfo, context, connection);
			iCollCont.setName("LmtIntbankDetailContList");
			iCollIqp.setName("LmtIntbankDetailIqpList");
			this.putDataElement2Context(iCollCont, context);
			this.putDataElement2Context(iCollIqp, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
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
