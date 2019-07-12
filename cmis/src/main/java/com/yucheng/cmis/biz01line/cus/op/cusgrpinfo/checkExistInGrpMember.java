package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMember;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class checkExistInGrpMember extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = (String)context.getDataValue("cus_id");
			CusGrpMemberComponent cgic = (CusGrpMemberComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance("CusGrpMember", context, connection);
			CusGrpMember cgm = cgic.cheakCusGrpMember(cus_id);
			if(cgm!=null && cgm.getCusId()!=null && cgm.getCusId().equals(cus_id)){
				context.addDataField("flag", "true");
			}else{
				context.addDataField("flag", "false");
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
