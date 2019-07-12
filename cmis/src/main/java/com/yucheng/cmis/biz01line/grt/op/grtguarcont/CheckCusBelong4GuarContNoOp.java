package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

public class CheckCusBelong4GuarContNoOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guar_cont_no = (String)context.getDataValue("guar_cont_no");
			String cusId = "";
			
			IndexedCollection sqliColl = SqlClient.queryList4IColl("queryCusId4ContNo", guar_cont_no, connection);
			if(sqliColl.size()>0){
				KeyedCollection kColl4ContNo = (KeyedCollection) sqliColl.get(0);
				cusId = (String)kColl4ContNo.getDataValue("cus_id");
			}
			if(cusId !=null && !"".equals(cusId)){
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();			
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");//获取客户接口		
				//调用客户接口
				CusBase cusbase = service.getCusBaseByCusId(cusId, context, connection);
				String BelgLine = (String)cusbase.getBelgLine();
				if(BelgLine == "BL300"){
					context.addDataField("flag", PUBConstant.SUCCESS);
				}else{
					context.addDataField("flag", PUBConstant.FAIL);
				}
			}else{
				context.addDataField("flag", PUBConstant.NOTEXISTS);
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
