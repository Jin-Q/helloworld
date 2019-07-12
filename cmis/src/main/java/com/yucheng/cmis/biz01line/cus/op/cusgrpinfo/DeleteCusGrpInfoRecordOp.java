package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteCusGrpInfoRecordOp extends CMISOperation {
	
	private final String modelId = "CusGrpInfo";
	private final String grp_no_name = "grp_no";
	
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

			String grp_no_value = null;
			try {
				grp_no_value = (String)context.getDataValue(grp_no_name);
			} catch (Exception e) {}
			if(grp_no_value == null || grp_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+grp_no_name+"] cannot be null!");
			String parentCusId = (String)context.getDataValue("parent_cus_id");
			
			CusGrpInfoComponent cusGrpInfoComponent = (CusGrpInfoComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPINFO,context,connection);
			//更改客户信息表里的字段
			cusGrpInfoComponent.updateGrpModeAndNo(grp_no_value);
			//删除集团客户和集团成员表的信息 CusGrpInfo
			cusGrpInfoComponent.removeCusGrpInfo(grp_no_value, parentCusId);
			//删除集团客户和集团成员表的信息CusGrpMember
			cusGrpInfoComponent.deleteGrpMemberByGrpNo(grp_no_value);
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
