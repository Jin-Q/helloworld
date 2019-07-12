package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

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
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryMortGuarantyCertiInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "MortGuarantyCertiInfo";

	private final String warrant_no_name = "warrant_no";
	
	private boolean updateCheck = true;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String warrant_no_value = null;
			String warrant_type = null;
			try {
//				warrant_no_value = (String)context.getDataValue(warrant_no_name);
//				warrant_type = (String)context.getDataValue("warrant_type");
				warrant_no_value = URLDecoder.decode((String) context.getDataValue(warrant_no_name),"UTF-8");
				warrant_type = URLDecoder.decode((String) context.getDataValue("warrant_type"),"UTF-8");
				context.put("warrant_no", warrant_no_value);
				context.put("warrant_type", warrant_type);
			} catch (Exception e) {}
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			if(warrant_no_value == null || warrant_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+warrant_no_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> map = new HashMap<String,String>();
			map.put("warrant_no", warrant_no_value);
			map.put("warrant_type", warrant_type);
			KeyedCollection kColl = dao.queryDetail(modelId, map, connection);
			//根据权利类别来区分前台显示的是权利证明类型或其他证明类型
			String flag = (String) kColl.getDataValue("warrant_cls");
			if(flag.equals("1")){
				//权利证明类型
				kColl.addDataField("warrant_type_con",kColl.getDataValue("warrant_type"));
			}else{
				//其他证明类型
				kColl.addDataField("warrant_type_other",kColl.getDataValue("warrant_type"));
			}
			SInfoUtils.addSOrgName(kColl, new String[] {"keep_org_no","hand_org_no"});
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
