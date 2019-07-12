package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 异步查询合作方客户是否存在在途、未提交的申请信息
 * @param context context对象
 * @author 唐顺岩
 */
public class SearchLmtCoopInfo extends CMISOperation{
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = "";
		String message = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("cus_id")){
				cus_id = context.getDataValue("cus_id").toString();
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("coop_type");
			list.add("approve_status");
			//查询客户是否存在待发起、打回、追回、审批中的数据
			String condition = " WHERE COOP_TYPE<>'010' AND APPROVE_STATUS NOT IN('990','997','998') AND CUS_ID='"+cus_id+"'";
			KeyedCollection kColl = dao.queryFirst("LmtAppJointCoop", list, condition, connection);
			
			if(null != kColl && null!=kColl.getDataValue("serno") && !"".equals(kColl.getDataValue("serno"))){
				if(!"000".equals(kColl.getDataValue("approve_status"))){
					message = "该合作方客户存在在途的合作方授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status"))){
					message = "该合作方客户存在未提交的合作方授信申请业务，不能重复发起。";
				}
			}
			
			context.addDataField("result", message);
		}catch (EMPException ee) {
			context.addDataField("result", ee.getMessage());
			throw ee;
		} catch(Exception e){
			context.addDataField("result", e.getMessage());
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
