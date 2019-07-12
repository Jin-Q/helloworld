package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo.cusgrpmember;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteCusGrpInfoCusGrpMemberRecordOp extends CMISOperation {
	
	
	private final String grp_no_name = "grp_no";
	private final String cus_id_name = "cus_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			String grp_no_value = null;
			try {
				grp_no_value = (String)context.getDataValue(grp_no_name);
			} catch (Exception e) {}
			if(grp_no_value == null || grp_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+grp_no_name+"] cannot be null!");
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
			
			//该客户的授信从集团授信协议中分离出去
		//	ILmt iLmt = (ILmt)CMISComponentFactory
		//	.getComponentFactoryInstance().getComponentInterface("LmtInterface", context, connection);
		//	iLmt.seperateCusFromGrp(cus_id_value);
			//构件业务处理类
			CusGrpMemberComponent cusGrpMemberComponent = (CusGrpMemberComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPMEMBER,context,connection);
			
			cusGrpMemberComponent.removeCusGrpMember(grp_no_value,cus_id_value);
			
			/*TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("grp_no",grp_no_value);
			pkMap.put("cus_id",cus_id_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}*/
			
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
