package com.yucheng.cmis.biz01line.iqp.op.iqpbailsubdis;

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
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpBailSubDisList4BailAccOp extends CMISOperation {

	private final String modelId = "IqpBailSubDis";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String flag = null;//申请类型（1--追加，2--提取）
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String cont_no = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			try {
				cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {
                throw new Exception("合同编号获取异常");				
			}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"";
		
			conditionStr ="where flag ='1' and cont_no='"+cont_no+"'";
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("serno");
			list.add("cont_no");
			list.add("cus_id");
			list.add("ori_bail_amt");
			list.add("adjust_bail_amt");
			list.add("adjusted_bail_amt");
			list.add("input_id");
			list.add("input_br_id");
			list.add("approve_status");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			
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
