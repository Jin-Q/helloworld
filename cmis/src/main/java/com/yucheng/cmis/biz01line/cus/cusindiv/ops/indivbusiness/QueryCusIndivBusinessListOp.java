package com.yucheng.cmis.biz01line.cus.cusindiv.ops.indivbusiness;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusIndivBusinessListOp extends CMISOperation {

	private final String modelId = "CusIndivBusiness";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cusid = (String)context.getDataValue("cus_id");
			String conditionStr = " where cus_id='"+ cusid +"' order by serno desc";
			
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("com_name");
			list.add("real_controller");
			list.add("ent_model");
			list.add("prop_qlty");
			list.add("reg_fund");
			list.add("serno");
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
