package com.yucheng.cmis.biz01line.cont.op.iqpcusacct;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-9-15
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class CheckIsCanBeDeteleOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String modiflg ="";
			String modify_rel_serno ="";
			String acct_no="";
			String acct_attr="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(context.containsKey("acct_no")){
				acct_no = (String) context.getDataValue("acct_no");
			}
			if(context.containsKey("acct_attr")){
				acct_attr = (String) context.getDataValue("acct_attr");
			}
			
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				if("04".equals(acct_attr)){
					context.addDataField("flag", "success");
				}else{
					context.addDataField("flag", "forbidden");
				}
			}else{
				context.addDataField("flag", "success");
			}
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
