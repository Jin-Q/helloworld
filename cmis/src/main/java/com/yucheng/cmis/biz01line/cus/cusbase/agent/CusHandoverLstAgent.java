package com.yucheng.cmis.biz01line.cus.cusbase.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverLst;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusHandoverLstAgent extends CMISAgent {


	public List<CMISDomain> findCusBySernoList(String serno) throws ComponentException{
		List<CMISDomain> cusHandoverLstList = new ArrayList<CMISDomain>();
		Connection connection=this.getConnection();
		CusHandoverLst cusHandoverLst = new CusHandoverLst();
		String modelId = PUBConstant.CUSHANDOVERLST;
		IndexedCollection icol;
		TableModelDAO dao = this.getTableModelDAO();
		StringBuffer conditionStr = new StringBuffer("");
		conditionStr.append(" where  serno='"+serno+"'");
	
		try{
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("handover_type");
			list.add("business_code");
			list.add("cus_id");
			icol = dao.queryList(modelId,list, conditionStr.toString(),connection);
			ComponentHelper cHelper = new ComponentHelper();
			cusHandoverLstList = cHelper.icol2domainlist(cusHandoverLst, icol);			
		}
		catch(EMPJDBCException e){
			throw new ComponentException(e);
		}
		catch(CMISException e2){
			throw new ComponentException(e2);
		}	
		return cusHandoverLstList;
	}
	 public String deleteCusHandoverLst(String serno) throws AgentException{
			
			String sql = "delete from cus_handover_lst where serno ='"+serno+"'";
			String cusHandoverLst =this.executeSql(sql);
			
			return cusHandoverLst;
		}
}
