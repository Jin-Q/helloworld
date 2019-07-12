package com.yucheng.cmis.platform.organization.sorg.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.initializer.OrganizationInitializer;

public class AddSOrgRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "SOrg";
	
	/**
	 * bussiness logic operation
	 */
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			String organNo=(String)kColl.getDataValue("organno");
			KeyedCollection tmpkColl=dao.queryDetail(modelId, organNo, connection);
			String  tmpOrganno=(String)tmpkColl.getDataValue("organno");
			String  tmpOrganName=(String)tmpkColl.getDataValue("organname");
			/**
			 * 新增机构动态修改内存中的map
			 */
			if(tmpOrganno==null||tmpOrganno.equals("")){
				dao.insert(kColl, connection);
				OrganizationInitializer.addAndUpdateOrgMapInfo(tmpOrganno, tmpOrganName);
				//SInfoUtils.findAllOrgInfo();
			}else{
				context.addDataField("flag", "failure");
				context.addDataField("msg", "机构码["+organNo+"]已经存在！");
				return null;
			}
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "新增失败！失败原因："+ee.getMessage());
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
