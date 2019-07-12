package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpmemmana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpMemManaListOp extends CMISOperation {


	private final String modelId = "IqpMemMana";
	public String doExecute(Context context) throws EMPException {		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id="";
			String net_agr_no="";
			if(context.containsKey("cus_id")){
				cus_id = (String)context.getDataValue("cus_id");
			}
			if(context.containsKey("net_agr_no")){
				net_agr_no=(String)context.getDataValue("net_agr_no");
			}
			String[] args=new String[] {"mem_cus_id","cus_id"};
		    String[] modelIds=new String[]{"CusBase","CusBase"};
		    String[] modelForeign=new String[]{"cus_id","cus_id"};
		    String[] fieldName=new String[]{"cus_name","cus_name"};

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String temp=SystemTransUtils.dealQueryName(queryData, args, context, modelIds,modelForeign, fieldName);
			if(temp!=null&&temp.length()>0){
				temp=(("and "+temp).substring(0, temp.length()-4))+")";
			}		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			if("".equals(conditionStr)){
					conditionStr = conditionStr+"where net_agr_no='"+net_agr_no+"'"+temp+" order by mem_cus_id desc";
			}else{
				conditionStr = conditionStr+temp+"and net_agr_no='"+net_agr_no+"' order by mem_cus_id desc";
			}
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));				
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			for(int i=0;i<iColl.size();i++){
			   KeyedCollection  kColl = (KeyedCollection)iColl.get(i);
			   kColl.addDataField("cus_id", cus_id);
			}
			//详细信息翻译时调用			
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
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
