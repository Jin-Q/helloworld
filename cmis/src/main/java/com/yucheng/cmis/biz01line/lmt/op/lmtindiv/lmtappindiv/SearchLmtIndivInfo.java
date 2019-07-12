package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtappindiv;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
/**
 * 异步查询客户是否存在在途、未提交的申请信息
 * @param context context对象
 * @author 唐顺岩
 */
public class SearchLmtIndivInfo extends CMISOperation{
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = "";
		String message = "";
		String lrisk_type = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("cus_id")){
				cus_id = context.getDataValue("cus_id").toString();
			}
			if(context.containsKey("lrisk_type")){
				lrisk_type = context.getDataValue("lrisk_type").toString();
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("app_type");
			list.add("approve_status");
			list.add("input_id");
			list.add("input_br_id");
			//查询客户是否存在待发起、打回、追回、审批中的数据
			//先查询复议
			String condition = " WHERE APPROVE_STATUS NOT IN('990','997','998') AND CUS_ID='"+cus_id+"'";
			KeyedCollection kColl = dao.queryFirst("LmtAppIndivRedi", list, condition, connection);
			//复议中不存在时查询申请
			if(null==kColl || null==kColl.getDataValue("serno") || "".equals(kColl.getDataValue("serno"))){
				condition = " WHERE APPROVE_STATUS NOT IN('990','997','998') AND CUS_ID='"+cus_id+"' AND LRISK_TYPE='"+lrisk_type+"'";
				kColl = dao.queryFirst("LmtAppIndiv", list, condition, connection);
			}
			
			if(null != kColl && null!=kColl.getDataValue("serno") && !"".equals(kColl.getDataValue("serno"))){
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
				SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
				
				lrisk_type = "10".equals(lrisk_type)?"低风险":"非低风险";
				
				String inputName = (String)kColl.getDataValue("input_id_displayname");
				String inputBrName = (String)kColl.getDataValue("input_br_id_displayname");
				if(!"000".equals(kColl.getDataValue("approve_status")) && "01".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的新增["+lrisk_type+"]授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "02".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的变更["+lrisk_type+"]授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "01".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的新增["+lrisk_type+"]授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "02".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的变更["+lrisk_type+"]授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "03".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的冻结授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "03".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的冻结授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "04".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的解冻授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "04".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的解冻授信申请业务，不能重复发起。";
				
				}else if("000".equals(kColl.getDataValue("approve_status")) && "05".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的["+lrisk_type+"]授信复议申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "05".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的["+lrisk_type+"]授信复议申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "06".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的["+lrisk_type+"]授信变更复议申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "06".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的["+lrisk_type+"]授信变更复议申请业务，不能重复发起。";
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
