package com.yucheng.cmis.biz01line.iqp.op.iqprentinfo;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpRentInfoListOp extends CMISOperation {


	private final String modelId = "IqpRentInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String serno = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			try{
		        serno = (String)context.getDataValue("serno");
		    }catch(Exception e){
		    	throw new Exception("业务流水号为空！");
		    }
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if("".equals(conditionStr) || conditionStr!=null){
		    	conditionStr = "where serno='"+serno+"' order by rent_serno desc";
		    }else{
		    	conditionStr = " and serno='"+serno+"' order by rent_serno desc";
		    }
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("lessee_name");
			list.add("lessee_cert_no");
			list.add("every_rent_amt");
			list.add("pld_amt");
			list.add("total_rent_amt");
			list.add("start_date");
			list.add("end_date");
			list.add("rent_serno");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
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
