package com.yucheng.cmis.biz01line.lmt.op.lmtappgrp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 异步查询客户是否存在在途、未提交的申请信息
 * @param context context对象
 * @author tangzf
 */
public class SearchLmtGrpInfo extends CMISOperation{
	
	private final String modelId = "LmtAppGrp";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String grp_no = "";
		String message = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("grp_no")){
				grp_no = context.getDataValue("grp_no").toString();
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("app_type");
			list.add("approve_status");
			//查询客户是否存在待发起、打回、追回、审批中的数据
			String condition = " WHERE APPROVE_STATUS NOT IN('990','997','998') AND GRP_NO='"+grp_no+"'";
			KeyedCollection kColl = dao.queryFirst(modelId, list, condition, connection);
			
			if(null != kColl && null!=kColl.getDataValue("serno") && !"".equals(kColl.getDataValue("serno"))){
				if(!"000".equals(kColl.getDataValue("approve_status")) && "01".equals(kColl.getDataValue("app_type"))){
					message = "该客户存在在途的新增授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "02".equals(kColl.getDataValue("app_type"))){
					message = "该客户存在在途的变更授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "01".equals(kColl.getDataValue("app_type"))){
					message = "该客户存在未提交的新增授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "02".equals(kColl.getDataValue("app_type"))){
					message = "该客户存在未提交的变更授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "03".equals(kColl.getDataValue("app_type"))){
					message = "该客户存在未提交的冻结授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "03".equals(kColl.getDataValue("app_type"))){
					message = "该客户存在未提交的冻结授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "04".equals(kColl.getDataValue("app_type"))){
					message = "该客户存在未提交的解冻授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "04".equals(kColl.getDataValue("app_type"))){
					message = "该客户存在未提交的解冻授信申请业务，不能重复发起。";
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
