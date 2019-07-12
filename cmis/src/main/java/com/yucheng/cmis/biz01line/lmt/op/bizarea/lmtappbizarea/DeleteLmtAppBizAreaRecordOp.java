package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappbizarea;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteLmtAppBizAreaRecordOp extends CMISOperation {

	private final String modelId = "LmtAppBizArea";
	private final String modelIdComn = "LmtAppBizAreaComn";
	private final String modelIdCore = "LmtAppBizAreaCore";
	private final String modelIdSupmk = "LmtAppBizAreaSupmk";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			String serno_value = null;
			String biz_area_type = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				biz_area_type = (String)context.getDataValue("biz_area_type");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			//删除主表  从表 
			dao.deleteByPk(modelId, serno_value, connection);
			if("0".equals(biz_area_type)){
				dao.deleteByPk(modelIdComn, serno_value, connection);
			}else if("1".equals(biz_area_type)){
				dao.deleteByPk(modelIdCore, serno_value, connection);
			}else{
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance("LmtPubComponent", context,connection);
				Map<String,String> map = new HashMap<String, String>();
				map.put("serno", serno_value);
				lmtComponent.deleteByField(modelIdSupmk, map);
			}
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
