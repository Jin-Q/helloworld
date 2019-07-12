package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappbizarea;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAppBizAreaDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAppBizArea";
	private final String modelIdComn = "LmtAppBizAreaComn";
	private final String modelIdCore = "LmtAppBizAreaCore";
	private final String modelIdSupmk = "LmtAppBizAreaSupmk";

	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String biz_area_type = null;
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}

			if(serno_value==null||"".equals(serno_value)){
				throw new Exception("商圈准入申请编号为空！");
			}
			IndexedCollection iCollSupmk = null;
			KeyedCollection kCollComn = new KeyedCollection(modelIdComn);
			KeyedCollection kCollCore = new KeyedCollection(modelIdCore);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			biz_area_type = (String)kColl.getDataValue("biz_area_type");
			if("0".equals(biz_area_type)){
				kCollComn= dao.queryDetail(modelIdComn, serno_value, connection);//(modelIdComn, " where serno = '" + serno_value + "'", connection);
				this.putDataElement2Context(kCollComn, context);
			}else if("1".equals(biz_area_type)){
				kCollCore = dao.queryDetail(modelIdCore, serno_value, connection);
				this.putDataElement2Context(kCollCore, context);
			}else if("2".equals(biz_area_type)){
				String condition = " where serno='"+serno_value+"'";
				iCollSupmk = dao.queryList(modelIdSupmk, condition, connection);
				iCollSupmk.setName(modelIdSupmk + "List");
				this.putDataElement2Context(iCollSupmk, context);
			}
			
			if(kColl.getDataValue("belg_org") == null){
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			}else{
				SystemTransUtils.containCommaORG2CN("belg_org",kColl,context);
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			}
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
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
