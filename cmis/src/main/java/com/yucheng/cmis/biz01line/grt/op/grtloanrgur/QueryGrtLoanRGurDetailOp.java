package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryGrtLoanRGurDetailOp  extends CMISOperation {
	
	private final String modelId = "GrtLoanRGur";
	
	private final String guar_cont_no_name = "guar_cont_no";
	private final String pk_id_name = "pk_id";
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String guar_cont_no_value = null;
			String pk_id_value = null;
			String oper ="";
			String op="";
			try {
				guar_cont_no_value = (String)context.getDataValue(guar_cont_no_name);
				pk_id_value = (String)context.getDataValue(pk_id_name);
				oper = (String)context.getDataValue("oper");
				op= (String)context.getDataValue("op");
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guar_cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
            /**调用担保模块接口*/			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
			KeyedCollection kCollCont = service.viewGuarContInfoDetail(guar_cont_no_value, connection,context);
			kCollCont.setId("GrtGuarCont");
			KeyedCollection kColl = dao.queryDetail(modelId, pk_id_value, connection);
			
			
			this.putDataElement2Context(kCollCont, context);
			this.putDataElement2Context(kColl, context);
			context.setDataValue("oper", oper); 
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
