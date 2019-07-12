package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
/**
 * 异步查询客户是否存在在途、未提交的申请信息
 * @param context context对象
 * @author 唐顺岩
 */
public class SearchLmtInfo extends CMISOperation{
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = "";
		String lrisk_type = "";
		//String lmt_type = "";
		String message = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("cus_id")){
				cus_id = context.getDataValue("cus_id").toString();
			}
			if(context.containsKey("lrisk_type")){
				lrisk_type = context.getDataValue("lrisk_type").toString();
			}
			//授信类别   2013-12-04  唐顺岩
			//if(context.containsKey("lmt_type")){
			//	lmt_type = context.getDataValue("lmt_type").toString();
			//}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			if("20".equals(lrisk_type)){   //非低风险业务时集团客户不能走单一法人授信   2013-11-26  唐顺岩
				//调用客户管理模块服务接口
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				String str= service.getGrpFinanceType(cus_id, context, connection);  //查询客户所属集团融资模式
				//非空说是是集团客户
				if(!"".equals(str)){
					//message = "该客户归属的集团融资模式为：整体授信、分配额度，需做集团授信。";
					message = "该客户为集团客户，不能发起非低风险单一法人授信，需做集团授信。";
					context.addDataField("result", message);
					return "0";
				}
			}
			
			list.add("serno");
			list.add("app_type");
			list.add("approve_status");
			list.add("input_id");
			list.add("input_br_id");
			list.add("lmt_type");  //授信类别
			list.add("lrisk_type");
			//查询客户是否存在待发起、打回、追回、审批中的数据
			//先查询复议
			String condition = " WHERE APPROVE_STATUS NOT IN('990','997','998') AND CUS_ID='"+cus_id+"' AND LRISK_TYPE='"+lrisk_type+"'";
			KeyedCollection kColl = dao.queryFirst("LmtRediApply", list, condition, connection);
			//复议中不存在时查询申请
			if(null==kColl || null==kColl.getDataValue("serno") || "".equals(kColl.getDataValue("serno"))){
				condition = " WHERE APPROVE_STATUS NOT IN('990','997','998') AND CUS_ID='"+cus_id+"' AND LRISK_TYPE='"+lrisk_type+"'";
				kColl = dao.queryFirst("LmtApply", list, condition, connection);
			}
			
			if(null != kColl && null!=kColl.getDataValue("serno") && !"".equals(kColl.getDataValue("serno"))){
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
				SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
				
				String inputName = (String)kColl.getDataValue("input_id_displayname");
				String inputBrName = (String)kColl.getDataValue("input_br_id_displayname");
				String lriskType = (String)kColl.getDataValue("lrisk_type");
				
				lriskType = "10".equals(lriskType)?"低风险":"非低风险";
				//lmt_type = (String)kColl.getDataValue("lmt_type");
				//lmt_type = "BL100".equals(lrisk_type)?"公司条线":"小微条线";
				
				if(!"000".equals(kColl.getDataValue("approve_status")) && "01".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的新增["+lriskType+"]授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "02".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的变更["+lriskType+"]授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "01".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的新增["+lriskType+"]授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "02".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的变更["+lriskType+"]授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "03".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的冻结授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "03".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的冻结授信申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "04".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的解冻授信申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "04".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的解冻授信申请业务，不能重复发起。";
					
				}else if("000".equals(kColl.getDataValue("approve_status")) && "05".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的["+lriskType+"]授信复议申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "05".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的["+lriskType+"]授信复议申请业务，不能重复发起。";
				}else if("000".equals(kColl.getDataValue("approve_status")) && "06".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在未提交的["+lriskType+"]授信变更复议申请业务，不能重复发起。";
				}else if(!"000".equals(kColl.getDataValue("approve_status")) && "06".equals(kColl.getDataValue("app_type"))){
					message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的["+lriskType+"]授信变更复议申请业务，不能重复发起。";
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
