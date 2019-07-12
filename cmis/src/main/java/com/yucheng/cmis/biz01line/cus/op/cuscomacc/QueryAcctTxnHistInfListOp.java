package com.yucheng.cmis.biz01line.cus.op.cuscomacc;

import java.sql.Connection;
import java.text.SimpleDateFormat;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryAcctTxnHistInfListOp  extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String acc_no = null;
			try {
				acc_no = (String)context.getDataValue("acc_no");
			} catch (Exception e) {}
			if(acc_no == null || acc_no.length() == 0)
				throw new EMPJDBCException("The value [acc_no] cannot be null!");
			
			int size = 10;  
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
	        IndexedCollection iColl = new IndexedCollection();
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			KeyedCollection ESB_kColl = service.tradeJYLS(acc_no, context, connection,pageInfo);
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
