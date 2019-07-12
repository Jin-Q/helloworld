package com.yucheng.cmis.biz01line.iqp.op.cusmanager;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.component.CusManagerComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusManagerListOp extends CMISOperation {


	private final String modelId = "CusManager";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno ="";
			String cont_no="";
			String conditionStr="";
		    if(context.containsKey("serno")){
		    	serno = (String)context.getDataValue("serno");
		    }
		    if(context.containsKey("cont_no")){
		    	cont_no = (String)context.getDataValue("cont_no");
		    }

            if(!"".equals(cont_no) && cont_no != null){
            	conditionStr = "where cont_no= '"+cont_no+"' order by manager_id desc"; 
		    }else{
		    	conditionStr = "where serno= '"+serno+"' order by manager_id desc"; 
		    }     
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>(); 
			list.add("serno");
			list.add("biz_type");
			list.add("manager_id");
			list.add("is_main_manager");
			list.add("ser_rate");
			IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
			String[] args=new String[] { "manager_id" };
			String[] modelIds=new String[]{"SUser"};
			String[]modelForeign=new String[]{"actorno "};
			String[] fieldName=new String[]{"actorname"};
		//详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);

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
