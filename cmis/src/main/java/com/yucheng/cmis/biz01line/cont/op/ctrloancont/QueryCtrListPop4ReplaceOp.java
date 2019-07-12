package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCtrListPop4ReplaceOp extends CMISOperation {
	
	private final String modelId = "AccView";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String conditionStr ="";
			String prd_id = null;
			String cus_id = null;
			String condition = null;
  			try {   
				prd_id = (String)context.getDataValue("prd_id");
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {
				throw new Exception("请检查产品编号或客户码!");
			}
			
			if("500025".equals(prd_id) || "500024".equals(prd_id) ){
				condition = "where cus_id='"+cus_id+"' and prd_id='500023' and status='1'";
			}else if("500026".equals(prd_id) || "500029".equals(prd_id) || "500027".equals(prd_id) || "500028".equals(prd_id)){
				condition = "where cus_id='"+cus_id+"' and prd_id='500022' and status='1'";
			}else if("500021".equals(prd_id) || "500020".equals(prd_id) || "500032".equals(prd_id)){
				condition = "where cus_id='"+cus_id+"' and prd_id in('700021','700020') and status='1'";
			}
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			IndexedCollection iColl = dao.queryList(modelId,null ,condition,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			/** 组织机构、登记机构翻译 */
			//SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			
		}catch (EMPException ee){
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
