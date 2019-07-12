package com.yucheng.cmis.biz01line.iqp.op.iqpasset;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryIqpAssetDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAsset";
	

	private final String asset_no_name = "asset_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			KeyedCollection kColl = new KeyedCollection();
			String asset_no_value = null;
			try {
				asset_no_value = (String)context.getDataValue(asset_no_name);
			} catch (Exception e) {}
			if(asset_no_value == null || asset_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+asset_no_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl = dao.queryDetail(modelId, asset_no_value, connection);
			
			this.putDataElement2Context(kColl, context);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
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
