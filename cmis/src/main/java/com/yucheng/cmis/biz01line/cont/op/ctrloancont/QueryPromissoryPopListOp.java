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

public class QueryPromissoryPopListOp extends CMISOperation {
	
	private final String modelId = "CtrLoanCont";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = null;
  			try {   
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {
				throw new Exception("客户码为空，请联系后台管理员"); 
			} 
			//过滤条件，客户码、产品为贷款承诺,信贷证明 合同状态为有效状态，同时以台账状态为正常
			String conditionStr = "where cus_id='"+cus_id+"' and prd_id in('400022','400023') and cont_status ='200' and cont_no in(select cont_no from acc_loan where acc_status='1')";  
            			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			
			iColl.setName(iColl.getName()+"List"); 
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			this.putDataElement2Context(iColl, context);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			
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
