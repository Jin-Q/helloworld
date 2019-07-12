package com.yucheng.cmis.biz01line.cus.op.cuscomacc;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusComAccDetailOp  extends CMISOperation {
	
	private final String modelId = "CusComAcc";

	private final String cus_id_name = "cus_id";
	private final String acc_no_name = "acc_no";
	
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");

			String acc_no_value = null;
			try {
				acc_no_value = (String)context.getDataValue(acc_no_name);
			} catch (Exception e) {}
			if(acc_no_value == null || acc_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+acc_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("acc_no",acc_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			
			int size = 10;  
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
	        IndexedCollection iColl = new IndexedCollection();
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			KeyedCollection ESB_kColl = service.tradeJYLS(acc_no_value, context, connection,pageInfo);
			String RET_CODE = (String)ESB_kColl.getDataValue("RET_CODE");
			if("000000".equals(RET_CODE)){
				KeyedCollection bodyKColl = (KeyedCollection)ESB_kColl.getDataElement("BODY");
				iColl = (IndexedCollection)bodyKColl.getDataElement("AcctTxnHistInfArry");
				iColl.setName("AcctTxnHistInfList"); 
				KeyedCollection appKColl = (KeyedCollection)ESB_kColl.getDataElement("BODY");
//				String totalRowsR = (String)ESB_kColl.getDataValue("TotLineNum");//总条数  
				pageInfo.setRecordSize(String.valueOf(iColl.size()) ); 
			}else{
				throw new EMPException(""+(String)ESB_kColl.getDataValue("RET_STATUS"));
			}
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
