package com.yucheng.cmis.biz01line.lmt.op.lmtagrfinguar;

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

public class SearchLmtAgrFinGuarDetails  extends CMISOperation {
	private final String modelId = "CtrLoanCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = "";
			KeyedCollection queryData = null;
			try{
				cus_id = (String)context.getDataValue("cus_id");
			}catch (EMPException e){
				throw new Exception("融资性担保公司客户码为空");
			}
			try {   
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {} 
			String conditionStr =TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "5000");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			IndexedCollection iCollIqp = service.getIqp4LmtAgrFinGuar(cus_id, pageInfo, context, connection);
			IndexedCollection  iCollCont= service.getHisCont4LmtAgrFinGuar(cus_id, pageInfo,conditionStr, context, connection);
			
			iCollCont.setName("LmtUseContList");
			iCollIqp.setName("LmtUseIqpList");
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
