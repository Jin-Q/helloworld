package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * 根据机构号获取下级机构的Xtree加载的xml文件
 * @author liuhw
 */
public class GetSubOrg4XtreeOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			String getOrgId = (String) context.getDataValue("getOrgId");
			connection = this.getConnection(context);
			OrganizationServiceInterface orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			List<SOrg> orgs = orgMsi.getDirectSubOrgs(getOrgId, connection);
			StringBuffer rexml=new StringBuffer();
			HttpServletRequest request = this.getHttpServletRequest(context);
			rexml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        rexml.append("<tree>");
	        if(orgs.size()!=0){
               for (SOrg org : orgs) {
            	   if(org.getOrganno().equals(getOrgId)) {
            		   continue; //排除可能自身的情况
            	   }
            	   //url参数&一定要写成&amp;
            	   rexml.append("<tree text=\""+org.getOrganname()+"\""+" src=\""+request.getContextPath()+"/getSubOrg4Xtree.do?getOrgId="+org.getOrganno()+"&amp;EMP_SID="+request.getParameter("EMP_SID")+"\" action=\"javascript:getUser('"+org.getOrganno()+ "');\"></tree>");
               }
           }
           rexml.append("</tree>");
           String rexmlStr = rexml.toString();
           context.put("xmlStr", rexmlStr);
		} catch (Exception e) {
			EMPLog.log("GetSubOrg4XtreeOp", EMPLog.ERROR, EMPLog.ERROR, "根据机构号获取下级机构的Xtree加载的xml文件出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
