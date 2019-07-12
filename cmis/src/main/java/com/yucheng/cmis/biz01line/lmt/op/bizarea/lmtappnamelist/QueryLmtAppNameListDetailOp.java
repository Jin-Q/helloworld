package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappnamelist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAppNameListDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAppNameList";
	private final String modelIdAppDet = "LmtAppDetails";//授信分项表
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
//			if(this.updateCheck){
//				RecordRestrict recordRestrict = this.getRecordRestrict(context);
//				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//			}
			
			String serno_value = null;
			String cus_id = null;
			try {
				serno_value = (String)context.getDataValue("serno");
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk[serno] cannot be null!");
			
			if(cus_id==null||cus_id.length()==0)
				throw new EMPJDBCException("The value of pk[cus_id] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("serno", serno_value);
			pkMap.put("cus_id", cus_id);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);

			//接口调用
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase cusBase = service.getCusBaseByCusId(cus_id, context, connection);
			kColl.addDataField("cus_id_displayname", cusBase.getCusName());
			context.addDataField("belg_line", cusBase.getBelgLine());
			//查询额度分项信息
			String condition = " where serno='"+serno_value+"' and cus_id='"+cus_id+"'"; 
			IndexedCollection iCollAppDet = dao.queryList(modelIdAppDet, condition, connection);
			iCollAppDet.setName(modelIdAppDet+"List");
			
			/**翻译额度名称**/
			String[] args=new String[] { "limit_name" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
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
