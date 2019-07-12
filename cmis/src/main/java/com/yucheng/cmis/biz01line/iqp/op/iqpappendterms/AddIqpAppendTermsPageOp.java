package com.yucheng.cmis.biz01line.iqp.op.iqpappendterms;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class AddIqpAppendTermsPageOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpAppendTerms";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String prd_id = null;
			try {
				prd_id = (String)context.getDataValue("prd_id");
			} catch (Exception e) {
				throw new Exception("产品编号获取失败，请联系后台管理员！");
			}
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
	    	PrdServiceInterface servicePrd = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
	    	PrdBasicinfo prdBasicinfo = servicePrd.getPrdBasicinfoList(prd_id, connection);
	    	//获取可用费用
	    	TableModelDAO dao = this.getTableModelDAO(context);
	    	KeyedCollection kCollPrd = dao.queryDetail("PrdBasicinfo", prd_id, connection);
            String canFeeCode = (String)kCollPrd.getDataValue("canFeeCode");
	    	context.addDataField("canFeeCode", canFeeCode);
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
