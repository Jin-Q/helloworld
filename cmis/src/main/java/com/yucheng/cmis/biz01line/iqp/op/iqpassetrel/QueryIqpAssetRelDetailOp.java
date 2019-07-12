package com.yucheng.cmis.biz01line.iqp.op.iqpassetrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpAssetRelDetailOp  extends CMISOperation {
	private final String modelId = "IqpAssetRel";
	private final String MainModelId="IqpAsset";

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
			
			String pk_id_value = null;
			String asset_no="";
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
				asset_no = (String)context.getDataValue("asset_no");
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_value+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection mainKColl = dao.queryDetail(MainModelId, asset_no, connection);
	      	String takeover_type = (String)mainKColl.getDataValue("takeover_type");
	      	 context.addDataField("takeover_type", takeover_type); 
			KeyedCollection kColl = dao.queryDetail(modelId, pk_id_value, connection);
			
		    String[] args=new String[] {"repay_type","repay_type","prd_id" ,"cus_id"};
			String[] modelIds=new String[]{"PrdRepayMode","PrdRepayMode","PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"repay_mode_id","repay_mode_id","prdid","cus_id"}; 
			String[] fieldName=new String[]{"repay_mode_dec","repay_mode_type","prdname","belg_line"};
			String[] resultName = new String[] {"repay_type_displayname","repay_mode_type","prd_name","belg_line"};
		    //详细信息翻译时调用	
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
		    
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","fina_br_id"});
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
