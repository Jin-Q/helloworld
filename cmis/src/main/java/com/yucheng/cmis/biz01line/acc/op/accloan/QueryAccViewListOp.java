package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryAccViewListOp extends CMISOperation {
	private final String modelId = "AccView";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String cont_no = "";
	        try {
	             queryData = (KeyedCollection)context.getDataElement(this.modelId);
	        } catch (Exception e) {}
	            
	        try {
                 cont_no = (String) context.getDataValue("cont_no");
            } catch (Exception e) {
            	throw new Exception("合同编号为空");
            }
            String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);    	
	            
            if("".equals(conditionStr)||conditionStr==null){
            	conditionStr = " where 1=1 " ;
            }
            conditionStr = conditionStr +" and cont_no='"+cont_no+"'";
            int size = 15;
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
            IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
            
			iColl.setName("AccViewPopList");
			
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"};
		    //详细信息翻译时调用		
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","fina_br_id"});  
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
