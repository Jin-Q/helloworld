package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtsiglmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryNextLmtSigPageOp extends CMISOperation {

	private final String modelId = "LmtSigLmt";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}

			String cus_id = (String)context.getDataValue("cus_id");
			
//			String conditionStr = "where cus_id="+cus_id;
//			KeyedCollection kColl = dao.queryFirst(modelId, null, conditionStr, connection);
			KeyedCollection kColl = new KeyedCollection(modelId);
			kColl.addDataField("cus_id", cus_id);
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			KeyedCollection kColl_cus =service.getCusSameOrgKcoll(cus_id, context, connection);			
			String same_org_cnname =(String)kColl_cus.getDataValue("same_org_cnname");
			String same_org_type =(String)kColl_cus.getDataValue("same_org_type");
			String crd_grade =(String)kColl_cus.getDataValue("crd_grade");
			String assets =(String)kColl_cus.getDataValue("assets");
			kColl.addDataField("same_org_cnname", same_org_cnname);
			kColl.addDataField("same_org_type", same_org_type);
			kColl.addDataField("crd_grade", crd_grade);
			kColl.addDataField("asserts", assets);
			
//			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id"});
//			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" ,"input_br_id"});
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
