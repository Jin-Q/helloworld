package com.yucheng.cmis.biz01line.lmt.op.lmtquotaadjust;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;

public class UpdateLmtQuotaAdjustRecordOp extends CMISOperation {

	private final String modelId = "LmtQuotaAdjustApp";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement("LmtQuotaAdjust");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			if(context.containsKey("update_flag")&&context.getDataValue("update_flag")!=null&&"single".equals(context.getDataValue("update_flag"))){
				dao.update(kColl, connection);
				kColl.setName(modelId);
				dao.update(kColl, connection);
			}else{
				kColl.setName(modelId);
				String status = kColl.getDataValue("status").toString();
				if("0".equals(status)||"1".equals(status)||"3".equals(status)){
					kColl.setDataValue("status", "2");
				}
				String fin_agr_no = (String)kColl.getDataValue("fin_agr_no");
				String serno = (String)kColl.getDataValue("serno");
				String fix_inure_date = (String)kColl.getDataValue("inure_date");
				IndexedCollection iColl = dao.queryList(modelId, " where fin_agr_no ='"+fin_agr_no+"' and status in ('1','3') and serno <> '"+serno+"' and inure_date>='"+fix_inure_date+"'  ", connection);
				KeyedCollection kCollTemp = null;
				if("1".equals(status)||"3".equals(status)){
					if(iColl!=null&&iColl.size()>0){
						for(int i=0; i< iColl.size(); i++){  //遍历
							kCollTemp = (KeyedCollection) iColl.get(i);
							kCollTemp.setDataValue("status", "2");  //置为未生效
							dao.update(kCollTemp, connection);  //更新
						}
					}
				}
				
				
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
			}
			
			context.addDataField("flag", "success");
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
