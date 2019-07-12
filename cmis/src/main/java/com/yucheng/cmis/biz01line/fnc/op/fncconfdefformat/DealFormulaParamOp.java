package com.yucheng.cmis.biz01line.fnc.op.fncconfdefformat;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class DealFormulaParamOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		FncConfDefFormat domain = new FncConfDefFormat();
		String rptType = (String)context.getDataValue("rptType");
		String typeFg = (String)context.getDataValue("typeFg");
		String gs = "";
		String chkGs = "";
		
		
		if("0".equals(typeFg)){
			KeyedCollection kColl = (KeyedCollection)context.getDataElement(PUBConstant.FNCCONFDEFFORMAT);
			ComponentHelper componetHelper = new ComponentHelper();
			domain = (FncConfDefFormat)componetHelper.kcolTOdomain(domain, kColl);
			gs = domain.getFncConfCalFrm();
			chkGs = domain.getFncConfChkFrm();
			
			HttpSession   session = null; 
			HttpServletRequest request;
			try {
				request = (HttpServletRequest)context.getDataValue(EMPConstance.SERVLET_REQUEST);
				session=request.getSession();
			} catch (ObjectNotFoundException e) {
				//异常处理
			}
			session.setAttribute("gs", gs);
			session.setAttribute("chkGs", chkGs);
			
			
		}else if("1".equals(typeFg)){
			gs = (String)context.getDataValue("gs");
		}
		
		if("0".equals(typeFg)){
			context.addDataField("gs", gs);
		}else{
			context.setDataValue("gs", gs);
		}
		context.setDataValue("rptType", rptType);
		return PUBConstant.SUCCESS;
	}
}
