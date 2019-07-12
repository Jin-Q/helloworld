package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
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
public class QueryBizModifyAppDetailOp  extends CMISOperation {
	
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
			KeyedCollection kColl4CLCTMP = dao.queryAllDetail("CtrLoanContTmp", modify_rel_serno, connection);//包括主从缓存表信息
			String prd_id = kColl4CLCTMP.getDataValue("prd_id").toString();
			
			String[] args=new String[] {"cus_id","cus_id","repay_type","repay_type","prd_id" };
			String[] modelIds=new String[]{"CusBase","CusBase","PrdRepayMode","PrdRepayMode","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","cus_id","repay_mode_id","repay_mode_id","prdid"}; 
			String[] fieldName=new String[]{"cus_name","belg_line","repay_mode_dec","repay_mode_type","prdname"};
			String[] resultName = new String[] { "cus_id_displayname","belg_line","repay_type_displayname","repay_mode_type","prd_id_displayname"};
		    //详细信息翻译时调用	
			SystemTransUtils.dealPointName(kColl4CLCTMP, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			KeyedCollection kCollSubTmp = (KeyedCollection)kColl4CLCTMP.getDataElement("CtrLoanContSubTmp");
			SystemTransUtils.dealPointName(kCollSubTmp, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			context.put("prd_id", prd_id);//获取产品编号
			context.put("repay_type", (String) kCollSubTmp.getDataValue("repay_type"));//还款方式A001 --自由还款法（不确定还款日）
			context.put("pay_type", (String) kCollSubTmp.getDataValue("pay_type"));//支付方式 --1 受托支付
			context.put("is_close_loan", (String)kCollSubTmp.getDataValue("is_close_loan"));//是否无间贷
			
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(kColl4CLCTMP, context);
			
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
