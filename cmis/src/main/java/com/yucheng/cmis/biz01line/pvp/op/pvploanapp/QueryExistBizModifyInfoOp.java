package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import org.quartz.utils.Key;

import sun.security.krb5.internal.ccache.ap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-8-6
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*							 校验是否存在在途的修改申请信息
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class QueryExistBizModifyInfoOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
	
		try {
			connection = this.getConnection(context);
			String serno="";
			String cont_no="";
			String biz_cate="";
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}
			if(context.containsKey("biz_cate")){
				biz_cate = (String)context.getDataValue("biz_cate");
			}
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList("PvpBizModifyRel", "where biz_serno ='"+serno+"' and cont_no='"+cont_no+"' and approve_status not in ('997','998')", connection);
			String approve_status ="";
			if("0011".equals(biz_cate) || "0012".equals(biz_cate)){
				 KeyedCollection kColl = dao.queryDetail("PvpLoanApp", serno, connection);
				 approve_status = kColl.getDataValue("approve_status").toString();
			}else if("016".equals(biz_cate)){
				KeyedCollection kColl = dao.queryDetail("IqpExtensionPvp", serno, connection);
				approve_status = kColl.getDataValue("approve_status").toString();
			}
			if("992".equals(approve_status)){
				if(iColl.size() > 0){
					context.addDataField("flag", "failed");
				}else {
					context.addDataField("flag", "success");
				}
			}else{
				context.addDataField("flag", "forbidden");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.releaseConnection(context, connection);
		}
		return null;
	}
}
