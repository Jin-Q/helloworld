package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateIqpCoreConNetRecordOp extends CMISOperation {

	private final String modelId = "IqpCoreConNet";
	private final String modelIdMemMana = "IqpAppMemMana";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			String serno = (String)kColl.getDataValue("serno");
			String dealer_lmt_type = (String)kColl.getDataValue("dealer_lmt_type");//经销商授信业务种类
			String provider_lmt_type = (String)kColl.getDataValue("provider_lmt_type");//供应商授信业务种类
			TableModelDAO dao = this.getTableModelDAO(context);
			/**查询新入网成员，原有成员*/
			String condition="where serno='"+serno+"' and status<>'2'";
			IndexedCollection iColl = dao.queryList(modelIdMemMana, condition, connection);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollMemMana = (KeyedCollection)iColl.get(i);
				//{'01':'供应商', '02':'经销商'}
				String mem_manuf_type = (String)kCollMemMana.getDataValue("mem_manuf_type");
				if("01".equals(mem_manuf_type)){
					kCollMemMana.put("lmt_type", provider_lmt_type);
				}else if("02".equals(mem_manuf_type)){
					kCollMemMana.put("lmt_type", dealer_lmt_type);
				}
				dao.update(kCollMemMana, connection);
			}
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}

			context.addDataField("flag", PUBConstant.SUCCESS);
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
