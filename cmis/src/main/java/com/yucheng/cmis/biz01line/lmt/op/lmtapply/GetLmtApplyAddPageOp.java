package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

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
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

/**
 * @author 唐顺岩
 * @date 20130715
 */
public class GetLmtApplyAddPageOp extends CMISOperation {
	private static final Logger logger = Logger.getLogger(GetLmtApplyAddPageOp.class);

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnValue="";   //定义返回字符串
		try {
			String cus_id = "";
			String lrisk_type = "";
			String manager_id = "";
			String manager_br_id = "";
			//String lmt_type = "";
			if(context.containsKey("LmtApply.cus_id")){
				cus_id = context.getDataValue("LmtApply.cus_id").toString();
			}
			if(context.containsKey("LmtApply.lrisk_type")){
				lrisk_type = context.getDataValue("LmtApply.lrisk_type").toString();
			}
			if(context.containsKey("LmtApply.manager_id")){
				manager_id = context.getDataValue("LmtApply.manager_id").toString();
			}
			if(context.containsKey("LmtApply.manager_br_id")){
				manager_br_id = context.getDataValue("LmtApply.manager_br_id").toString();
			}
			//授信类别（区分条线）
			//if(context.containsKey("LmtApply.lmt_type")){
			//	lmt_type = context.getDataValue("LmtApply.lmt_type").toString();
			//}
			
			connection = this.getConnection(context);
			
			KeyedCollection kColl = new KeyedCollection("LmtApply");
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
			String openDate = "";
			if(context.containsKey("OPENDAY")){
				openDate = context.getDataValue("OPENDAY").toString();
			}
			openDate = openDate.replaceAll("-", "");
			Map<String,String> modelMap=new HashMap<String,String>();
			modelMap.put("IN_CUS_ID", cus_id);
			modelMap.put("IN_CUS_TYPE", "1");  //客户类型为单一法人
			modelMap.put("IN_LRISK_TYPE", lrisk_type);  //新增低风险业务类型
			modelMap.put("IN_LMT_TYPE", "");  //授信类别（区分条线）
			modelMap.put("IN_CUR_DATE", openDate);
			Map<String,String> outMap=new HashMap<String,String>();
			try {
				outMap=shuffleService.fireTargetRule("LMT_CHECK", "ISEXISTCRD", modelMap);
			} catch (Exception e1) {
				logger.error("调用规则自动判断是否存在有效授信出错，客户码："+cus_id+"，规则集：LMT_CHECK，规则名称：ISEXISTCRD\n错误原因："+e1);
				throw new ComponentException(CMISMessage.QUERYERROR,"调用规则自动判断是否存在有效授信出错，客户码："+cus_id+"，规则集：LMT_CHECK，规则名称：ISEXISTCRD\n错误原因："+e1);
			}
			
			String is_exist =(String) outMap.get("OUT_EXIST_CRD");
			
			//查询客户是否存在有效授信协议
			TableModelDAO dao = this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			String condition = " WHERE CUS_ID='"+cus_id+"' AND AGR_STATUS='002' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ";
			kColl = dao.queryFirst("LmtAgrInfo", list, condition, connection);
			//如果存在有效授信协议将协议编号赋值给申请中的协议编号
			if(null != kColl && null != kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
				kColl.put("agr_no", kColl.getDataValue("agr_no"));
				
				kColl.removeDataElement("serno");  //清除原协议中流水号，变更保存是自动生成
				kColl.removeDataElement("input_date");  //变更时登记日期清除，从新取系统日期
				
				kColl.removeDataElement("input_id");  //变更时登记人 清除，从新取系统当前登录人
				kColl.removeDataElement("input_br_id");  //变更时登记机构清除，从新取系统当前登录机构
				
				kColl.removeDataElement("manager_id");  //变更时登记人 清除，从新取系统当前登录人
				kColl.removeDataElement("manager_br_id");  //变更时登记机构清除，从新取系统当前登录机构
				
				//清楚原有额度信息
				kColl.removeDataElement("org_crd_totl_amt");
				kColl.removeDataElement("org_crd_cir_amt");
				kColl.removeDataElement("org_crd_one_amt");
				
				/**从台账中实时汇总循环额度、一次性额度    2013-12-09  唐顺岩   */
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				//KeyedCollection kColl_details = lmtComponent.selectLmtAgrDetailsAmt(kColl.getDataValue("agr_no").toString(),lmt_type);
				KeyedCollection kColl_details = lmtComponent.selectLmtAgrDetailsAmt(kColl.getDataValue("agr_no").toString());
				if(null != kColl_details){
					if("20".equals(lrisk_type)){
						//原有
						kColl.put("org_crd_totl_amt", kColl.getDataValue("crd_totl_amt")); //授信总额    --从协议中取，原有协议多少就多少  2014-01-04
						kColl.put("org_crd_cir_amt", kColl.getDataValue("crd_cir_amt"));//循环总额    --从协议中取，原有协议多少就多少  2014-01-04
						kColl.put("org_crd_one_amt", kColl.getDataValue("crd_one_amt"));//一次性总额    --从协议中取，原有协议多少就多少  2014-01-04
						//现有
						kColl.put("crd_totl_amt", kColl_details.getDataValue("total_amt"));
						kColl.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
						kColl.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
					}else if("10".equals(lrisk_type)){
						//原有											  
						kColl.put("org_crd_totl_amt", kColl.getDataValue("lrisk_totl_amt"));   //低风险授信总额    --从协议中取，原有协议多少就多少  2014-01-04
						kColl.put("org_crd_cir_amt", kColl.getDataValue("lrisk_cir_amt"));   //低风险循环总额    --从协议中取，原有协议多少就多少  2014-01-04
						kColl.put("org_crd_one_amt", kColl.getDataValue("lrisk_one_amt"));   //低风险一次性总额    --从协议中取，原有协议多少就多少  2014-01-04
						//现有
						kColl.put("crd_totl_amt", kColl_details.getDataValue("lrisk_total_amt"));
						kColl.put("crd_cir_amt", kColl_details.getDataValue("lrisk_cir_amt"));
						kColl.put("crd_one_amt", kColl_details.getDataValue("lrisk_one_amt"));
					}
				}
				/** END */
			}
			
			if("Y".equalsIgnoreCase(is_exist)){   //客户存在有效授信
				kColl.addDataField("app_type", "02");  //设置授信申请类型为 变更 
				returnValue = "addPage";
			}else{ 	 //客户没有有效授信
				kColl.addDataField("app_type", "01");  //设置授信申请类型为 新增 
				kColl.put("cus_id", cus_id);  //客户码加入到kColl中，用于翻译客户名称
				
				returnValue = "addPage";
			}
			
			kColl.put("input_id",context.getDataValue("currentUserId"));
			kColl.put("input_br_id",context.getDataValue("organNo"));
			kColl.put("manager_id", manager_id);
			kColl.put("manager_br_id", manager_br_id);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
			
			kColl.addDataField("type", "Y");
			kColl.setName("LmtApply");
			
			context.addDataField("operate", "addLmtApplyRecord.do");
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
