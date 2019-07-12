package com.yucheng.cmis.biz01line.lmt.op.lmtappgrp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

/**
 * 生成Cus_Cogniz_Apply的流水号
 * @author tangzf
 * @date 20130715
 */
public class GetAddLmtGrpApplyAddPageOp extends CMISOperation {
	private static final Logger logger = Logger.getLogger(GetAddLmtGrpApplyAddPageOp.class);

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnValue="";   //定义返回字符串
		try {
			String grp_no = "";
			String manager_id = "";
			String manager_br_id = "";
			if(context.containsKey("LmtAppGrp.grp_no")){
				grp_no = context.getDataValue("LmtAppGrp.grp_no").toString();
			}
			if(context.containsKey("LmtAppGrp.manager_id")){
				manager_id = context.getDataValue("LmtAppGrp.manager_id").toString();
			}
			if(context.containsKey("LmtAppGrp.manager_br_id")){
				manager_br_id = context.getDataValue("LmtAppGrp.manager_br_id").toString();
			}
			
			connection = this.getConnection(context);
			KeyedCollection kColl = new KeyedCollection("LmtAppGrp");
			//通过规则校验客户该客户是否存在有效授信
			// 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			try {
				shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			} catch (Exception e) {
				EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
				throw new EMPException(e);
			}
			String openDate = context.getDataValue("OPENDAY").toString();
			openDate = openDate.replaceAll("-", "");
			Map<String,String> modelMap=new HashMap<String,String>();
			modelMap.put("IN_CUS_ID", grp_no);
			modelMap.put("IN_CUR_DATE", openDate);
			Map outMap=new HashMap();
			try {
				outMap=shuffleService.fireTargetRule("LMT_CHECK", "ISEXISTGRPAGR", modelMap);
			} catch (Exception e1) {
				logger.error("调用规则自动判断是否存在有效授信出错，客户码："+grp_no+"，规则集：LMT_CHECK，规则名称：ISEXISTGRPAGR\n"+e1);
				throw new ComponentException(CMISMessage.QUERYERROR,"调用规则自动判断是否存在有效授信出错，客户码："+grp_no+"，规则集：LMT_CHECK，规则名称：ISEXISTGRPAGR");
			}
			
			String is_exist =(String) outMap.get("OUT_EXIST_CRD");
			
			if("Y".equalsIgnoreCase(is_exist)){   //客户存在有效授信
				//跳转到修改页面
				TableModelDAO dao = this.getTableModelDAO(context);
				List<String> list = new ArrayList<String>();
				//状态为正常、到期日期大于当前日期
				String condition = " WHERE GRP_NO='"+grp_no+"' AND AGR_STATUS='002' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ";
				kColl = dao.queryFirst("LmtAgrGrp", list, condition, connection);
				if(null != kColl && null != kColl.getDataValue("grp_agr_no") && !"".equals(kColl.getDataValue("grp_agr_no"))){
					kColl.addDataField("org_crd_totl_amt",kColl.getDataValue("crd_totl_amt"));
					
					kColl.removeDataElement("input_date");  //变更时登记日期清除，重新取系统日期
				}
				kColl.addDataField("app_type", "02");  //设置授信申请类型为 变更 
				returnValue = "addPage";
			}else{ 	 //客户没有有效授信
				kColl.addDataField("app_type", "01");  //设置授信申请类型为 新增 
				kColl.addDataField("grp_no", grp_no);  //客户码加入到kColl中，用于翻译客户名称
				kColl.addDataField("grp_agr_no", "");
				kColl.addDataField("input_id",context.getDataValue("currentUserId"));
				kColl.addDataField("input_br_id",context.getDataValue("organNo"));
				
				returnValue = "addPage";
			}
			
			String[] args=new String[] { "grp_no" };
			String[] modelIds=new String[]{"CusGrpInfo"};
			String[] modelForeign=new String[]{"grp_no"};
			String[] fieldName=new String[]{"grp_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);

			kColl.put("manager_id", manager_id);
			kColl.put("manager_br_id", manager_br_id);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
			kColl.addDataField("type", "Y");
			kColl.setName("LmtAppGrp");
			
			context.addDataField("operate", "addLmtGrpApplyRecord.do");
			this.putDataElement2Context(kColl, context);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnValue;
	}
}
