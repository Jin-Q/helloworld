package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
/**
 * 
*@author lisj
*@time 2015-8-6
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class QueryBizModifyApp4IEADetailOp  extends CMISOperation {
	
	private final String modelId = "PvpBizModifyRel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			String modify_rel_serno ="";
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
			}
			if(modify_rel_serno == null || modify_rel_serno.length() == 0)
				throw new EMPJDBCException("The value of pk["+modify_rel_serno+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//查询关联表信息
			KeyedCollection kColl = dao.queryDetail(modelId, modify_rel_serno, connection);
			KeyedCollection kColl4IEA = dao.queryAllDetail("IqpExtensionAgrTmp", modify_rel_serno, connection);
			
			SInfoUtils.addSOrgName(kColl4IEA, new String[] { "manager_br_id" ,"input_br_id"});
			SInfoUtils.addUSerName(kColl4IEA, new String[] { "manager_id" ,"input_id",});
			String[] args=new String[] { "cus_id","fount_cont_no","fount_bill_no"};
			String[] modelIds=new String[]{"CusBase","CtrLoanCont","AccLoan"};
			String[] modelForeign=new String[]{"cus_id","cont_no","bill_no"};
			String[] fieldName=new String[]{"cus_name","serno","prd_id"};
			String[] resultName = new String[] { "cus_id_displayname","fount_serno","prd_id"};
			SystemTransUtils.dealPointName(kColl4IEA, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(kColl4IEA, context);
			
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
