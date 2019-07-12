package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class QueryCusGrpInfoDomainOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
	Connection connection = null;
	try{	
		 connection = this.getConnection(context);
		String grpNo = (String)context.getDataValue("grpNo");
		
		CusGrpInfoComponent cgic = (CusGrpInfoComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("CusGrpInfo", context,connection);
		CusGrpInfo cgi = cgic.getCusGrpInfoDomainByGrpNo(grpNo);
		
		if(cgi!=null){
			context.addDataField("grpName", cgi.getGrpName());
		}else{
			throw new EMPException("根据集团编号【"+grpNo+"】无法取到集团客户信息！");
		}
	}catch (EMPException ee) {
		throw ee;
	} catch(Exception e){
		throw new EMPException(e);
	} finally {
		if (connection != null)
			this.releaseConnection(context, connection);
	}		
		return null;
	}

}
