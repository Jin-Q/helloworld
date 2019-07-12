package com.yucheng.cmis.biz01line.iqp.op.iqpequipinfo;

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

public class QueryIqpEquipInfoListOp extends CMISOperation {


	private final String modelId = "IqpEquipInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			String serno = null;
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
		    	conditionStr = "where serno='"+serno+"' order by equip_pk desc";
		    }else{
		    	conditionStr = " and serno='"+serno+"' order by equip_pk desc";
		    }
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("equip_name");
			list.add("equip_model");
			list.add("equip_unit_price");
			list.add("qnt");
			list.add("pur_total_amt");
			list.add("produce_date");
			list.add("pur_date");
			list.add("equip_pk");
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
