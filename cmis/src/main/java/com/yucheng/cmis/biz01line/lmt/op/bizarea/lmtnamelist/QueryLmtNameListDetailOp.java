package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtnamelist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtNameListDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtNameList";
	private final String modelIdAgrDe = "LmtAgrDetails";
	
	private boolean updateCheck = true;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
//			if(this.updateCheck){
//				RecordRestrict recordRestrict = this.getRecordRestrict(context);
//				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//			}
			String agr_no = null;
			String cus_id = null;
			try {
				agr_no = (String)context.getDataValue("agr_no");
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(agr_no == null || "".equals(agr_no))
				throw new EMPJDBCException("The value of pk[agr_no] cannot be null!");

			if(cus_id == null || "".equals(cus_id))
				throw new EMPJDBCException("The value of pk[cus_id] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("agr_no", agr_no);
			pkMap.put("cus_id", cus_id);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			//设置客户名称
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			//查询额度分项信息
			String condition = " where agr_no='"+agr_no+"' and cus_id='"+cus_id+"'"; 
			IndexedCollection iCollAppDet = dao.queryList(modelIdAgrDe, condition, connection);
			iCollAppDet.setName(modelIdAgrDe+"List");
			/**翻译额度名称**/
			args=new String[] { "limit_name" };
			modelIds=new String[]{"PrdBasicinfo"};
			modelForeign=new String[]{"prdid"};
			fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iCollAppDet, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(iCollAppDet, context);
			
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
