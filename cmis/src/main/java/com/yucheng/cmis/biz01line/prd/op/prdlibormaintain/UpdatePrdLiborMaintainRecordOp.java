package com.yucheng.cmis.biz01line.prd.op.prdlibormaintain;

import java.sql.Connection;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.hp.hpl.sparta.Document.Index;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class UpdatePrdLiborMaintainRecordOp extends CMISOperation {
	

	private final String modelId = "PrdLiborMaintain";
	
	private final String pk_id_name = "pk_id";
	
	
	private boolean updateCheck = true;
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String pk_ids = null;
			String flagV = null;
			try {
				if(context.containsKey("flagV")){
					flagV = (String) context.getDataValue("flagV");
				}
			} catch (Exception e) {}
			TableModelDAO dao = this.getTableModelDAO(context);
			try {
				
			} catch (Exception e) {}
			if("dh".equals(flagV)){//打回是需选择记录
				pk_ids = (String)context.getDataValue("pk_ids");
				for(int i=0;i<pk_ids.split(",").length;i++){
					KeyedCollection kColl = dao.queryDetail(modelId, pk_ids.split(",")[i], connection);
					kColl.setDataValue("check_id",context.getDataValue("currentUserId"));
					kColl.setDataValue("status","01");
					kColl.setDataValue("start_date",context.getDataValue("OPENDAY"));
					dao.update(kColl, connection);
				}
				context.addDataField("flag", "success");
			}else if("fh".equals(flagV)) {
				Map<String,String> map = new HashedMap();
				map.put("check_id","");
				map.put("start_date","");
				map.put("status","02");
				SqlClient.update("updatePrdLiborMaintain","01",map, null, connection);
				context.addDataField("flag", "success");
			}else if("sx".equals(flagV)) {
				String conStr = "where status ='02'";
				IndexedCollection ic = dao.queryList(modelId, conStr, connection);
				if(ic.size()>0){
					Map<String,String> map = new HashedMap();
					map.put("check_id",context.getDataValue("currentUserId").toString());
					map.put("start_date",context.getDataValue("OPENDAY").toString());
					map.put("status","03");
					SqlClient.update("updatePrdLiborMaintain", "02",map, null, connection);
					context.addDataField("flag", "success");
				}else{
					context.addDataField("flag","fail");
				}
			}
			
			
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
