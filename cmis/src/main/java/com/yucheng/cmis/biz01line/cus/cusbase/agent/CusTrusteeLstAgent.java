package com.yucheng.cmis.biz01line.cus.cusbase.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusTrusteeLst;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusTrusteeLstAgent extends CMISAgent {


	public List<CMISDomain> findCusBySernoList(String serno) throws ComponentException{
		List<CMISDomain> cusTrusteeLstList = new ArrayList<CMISDomain>();
		Connection connection =this.getConnection();
		CusTrusteeLst cusTrusteeLst = new CusTrusteeLst();
		String modelId = PUBConstant.CUSTRUSTEELST;
		IndexedCollection icol;
		TableModelDAO dao = this.getTableModelDAO();
		StringBuffer conditionStr = new StringBuffer("");
		conditionStr.append(" where  serno='"+serno+"'");
	
		try{
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("cus_id");
			list.add("cus_name");
			icol = dao.queryList(modelId,list, conditionStr.toString(),connection);
			ComponentHelper cHelper = new ComponentHelper();		
			cusTrusteeLstList = cHelper.icol2domainlist(cusTrusteeLst, icol);			
		}
		catch(EMPJDBCException e){
			throw new ComponentException(e);
		}
		catch(CMISException e2){
			throw new ComponentException(e2);
		}	
		return cusTrusteeLstList;
	}
}
