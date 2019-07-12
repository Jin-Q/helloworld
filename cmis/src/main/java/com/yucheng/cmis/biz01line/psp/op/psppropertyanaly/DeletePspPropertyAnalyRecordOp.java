package com.yucheng.cmis.biz01line.psp.op.psppropertyanaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeletePspPropertyAnalyRecordOp extends CMISOperation {

	private final String modelId = "PspPropertyAnaly";
	private final String modelIdPro = "PspPropertyRealpro";
	private final String modelIdEqu = "PspPropertyEquip";
	private final String modelIdTra = "PspPropertyTraffic";
	private final String modelIdLogo = "PspPropertyLogo";
	private final String modelIdPatent = "PspPropertyPatent";
	private final String modelIdStock = "PspPropertyStockright";
	private final String modelIdSea = "PspPropertySearight";
	private final String modelIdForest = "PspPropertyForestright";
	
	private final String pk_id_name = "property_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String pk_id_value = null;
			String property_type = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");
				
			try {
				property_type = (String)context.getDataValue("property_type");
			} catch (Exception e) {}
			if(property_type == null || property_type.length() == 0)
				throw new EMPJDBCException("The value of pk[property_type] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, pk_id_value, connection);
			if("11".equals(property_type)||"12".equals(property_type)||"13".equals(property_type)){//房地产
				count=dao.deleteByPk(modelIdPro, pk_id_value, connection);
			}else if("20".equals(property_type)){//设备及器材
				count=dao.deleteByPk(modelIdEqu, pk_id_value, connection);
			}else if("30".equals(property_type)){//交通工具
				count=dao.deleteByPk(modelIdTra, pk_id_value, connection);
			}else if("40".equals(property_type)){//商标使用权
				count=dao.deleteByPk(modelIdLogo, pk_id_value, connection);
			}else if("50".equals(property_type)){//专利使用权
				count=dao.deleteByPk(modelIdPatent, pk_id_value, connection);
			}else if("60".equals(property_type)){//股权
				count=dao.deleteByPk(modelIdStock, pk_id_value, connection);
			}else if("70".equals(property_type)){//海域使用权
				count=dao.deleteByPk(modelIdSea, pk_id_value, connection);
			}else if("80".equals(property_type)){//林权
				count=dao.deleteByPk(modelIdForest, pk_id_value, connection);
			}
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
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
