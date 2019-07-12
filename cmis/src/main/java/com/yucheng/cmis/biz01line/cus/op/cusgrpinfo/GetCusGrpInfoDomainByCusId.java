package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.cus.interfaces.CustomIface;
import com.yucheng.cmis.biz01line.cus.interfaces.impl.CustomIfaceImpl;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.exception.ComponentException;

public class GetCusGrpInfoDomainByCusId extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		//根据传入的CUS_ID获得其所在集团DOMAIN
		Connection connection = null;
		
		try {
			connection = this.getConnection(context);
			CustomIface ci = (CustomIfaceImpl)CMISComponentFactory.
				getComponentFactoryInstance().getComponentInterface("CustomIface", context, connection);
			String cus_id = (String)context.getDataValue("cus_id");
			if(cus_id!=null && !cus_id.equals("")){
				CusGrpInfo cgi = ci.getCusGrpInfoDomainByCusId(cus_id);
				if(cgi==null || cgi.getGrpNo()==null){
					try {
						context.addDataField("flag", "false");
					} catch (Exception e) {
						context.setDataValue("flag", "false");
					}
					try {
						context.addDataField("grp_no", "");
					} catch (Exception ee) {
						context.setDataValue("grp_no", "");
					}
					try {
						context.addDataField("grp_name", "");
					} catch (Exception ee) {
						context.setDataValue("grp_name", "");
					}
				}else{
					try {
						context.addDataField("grp_no", cgi.getGrpNo());
					} catch (Exception e) {
						context.setDataValue("grp_no", cgi.getGrpNo());
					}
					try {
						context.addDataField("grp_name", cgi.getGrpName());
					} catch (Exception e) {
						context.setDataValue("grp_name", cgi.getGrpName());
					}
					try {
						context.addDataField("flag", "true");
					} catch (Exception e) {
						context.setDataValue("flag", "true");
					}
				}
			}else{
				try {
					context.addDataField("flag", "false");
				} catch (Exception e) {
					context.setDataValue("flag", "false");
				}
			}
		} catch (Exception e) {
			try {
				context.addDataField("flag", "error");
			} catch (Exception ee) {
				context.setDataValue("flag", "error");
			}
			try {
				context.addDataField("grp_no", "");
			} catch (Exception ee) {
				context.setDataValue("grp_no", "");
			}
			try {
				context.addDataField("grp_name", "");
			} catch (Exception ee) {
				context.setDataValue("grp_name", "");
			}
			throw new ComponentException(e);
		}finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
