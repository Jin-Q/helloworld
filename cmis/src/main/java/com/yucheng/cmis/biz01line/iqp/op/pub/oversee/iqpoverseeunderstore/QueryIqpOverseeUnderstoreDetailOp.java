package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpoverseeunderstore;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryIqpOverseeUnderstoreDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpOverseeUnderstore";
	

	private final String store_id_name = "store_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String store_id_value = null;
			try {
				store_id_value = (String)context.getDataValue(store_id_name);
			} catch (Exception e) {}
			if(store_id_value == null || store_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+store_id_name+"] cannot be null!");

			
			//中文转码
			store_id_value = URLDecoder.decode(store_id_value,"UTF-8");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, store_id_value, connection);
			Map<String,String> map = new HashMap<String,String>();
            map.put("store_addr", "STD_GB_AREA_ALL");//行政区划名称
            CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
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
