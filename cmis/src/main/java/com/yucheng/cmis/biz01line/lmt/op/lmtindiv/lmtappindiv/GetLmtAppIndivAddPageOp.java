package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtappindiv;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
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
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

/**
 * 生成Cus_Cogniz_Apply的流水号
 * @author tangzf
 * @date 20130814
 */
public class GetLmtAppIndivAddPageOp extends CMISOperation {
	private static final Logger logger = Logger.getLogger(GetLmtAppIndivAddPageOp.class);

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		try {
			String cus_id = "";
			String cus_type = "";
			String belg_line = "";
			String lrisk_type = "";
			if(context.containsKey("LmtAppIndiv.cus_id")){
				cus_id = (String)context.getDataValue("LmtAppIndiv.cus_id");
				cus_type = (String)context.getDataValue("LmtAppIndiv.cus_type");
				belg_line = (String)context.getDataValue("LmtAppIndiv.belg_line");
			}
			
			if(context.containsKey("LmtAppIndiv.lrisk_type")){
				lrisk_type = context.getDataValue("LmtAppIndiv.lrisk_type").toString();
			}
			
			connection = this.getConnection(context);
			
			KeyedCollection kColl = new KeyedCollection("LmtAppIndiv");
			//通过规则校验客户该客户是否存在有效授信
			// 调用规则管理模块对外提供的服务
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
			modelMap.put("IN_LMT_TYPE", "BL300");  //授信类别（区分条线）
			modelMap.put("IN_CUR_DATE", openDate);
			Map<String,String> outMap=new HashMap<String,String>();
			try {
				outMap=shuffleService.fireTargetRule("LMT_CHECK", "ISEXISTCRD", modelMap);
			} catch (Exception e1) {
				logger.error("调用规则自动判断是否存在有效授信出错，客户码："+cus_id+"，规则集：LMT_CHECK，规则名称：ISEXISTCRD，错误描述："+e1.getMessage());
				context.addDataField("flag", "error");
				context.addDataField("serno", "");
				context.addDataField("msg", "调用规则自动判断是否存在有效授信出错，客户码："+cus_id+"，规则集：LMT_CHECK，规则名称：ISEXISTCRD，错误描述："+e1.getMessage());
				return null;
				//throw new EMPException(CMISMessage.QUERYERROR,"调用规则自动判断是否存在有效授信出错，客户码："+cus_id+"，规则集：LMT_CHECK，规则名称：ISEXISTCRD，错误描述："+e1.getMessage());
			}
			String is_exist =(String) outMap.get("OUT_EXIST_CRD");

			String manager_br_id = context.getDataValue("LmtAppIndiv.manager_br_id").toString();
			serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
			if("Y".equalsIgnoreCase(is_exist)){   //客户存在有效授信
				//跳转到修改页面
				TableModelDAO dao = this.getTableModelDAO(context);
				List<String> list = new ArrayList<String>();
				String condition = " WHERE CUS_ID='"+cus_id+"' AND AGR_STATUS='002' AND to_char(add_months(to_date(TOTL_END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ";
				kColl = dao.queryFirst("LmtAgrIndiv", list, condition, connection);
				if(null != kColl && null != kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
					/**从台账中实时汇总循环额度、一次性额度    2012-12-09  唐顺岩   */
					LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
					KeyedCollection kColl_details = lmtComponent.selectLmtAgrDetailsAmt(kColl.getDataValue("agr_no").toString());
					if(null != kColl_details){
						String self_amt_tmp = kColl.getDataValue("self_amt")+"";
						if(self_amt_tmp==null ||"".equals(self_amt_tmp)){
							self_amt_tmp = "0";
						}
						BigDecimal self_amt = new BigDecimal(self_amt_tmp);
						BigDecimal crd_totl_amt = new BigDecimal("0.0");
						if("20".equals(lrisk_type)){   //非低风险
							crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("total_amt")).add(self_amt);
							
							kColl.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));  //循环额度
							kColl.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));  //一次性额度
							
							/** 非低风险原额度取协议中非低风险额度+自助额度   2014-01-18 */
							BigDecimal org_crd_totl_amt = new BigDecimal(kColl.getDataValue("crd_totl_amt")+"");
							kColl.addDataField("org_crd_totl_amt", org_crd_totl_amt.add(self_amt));
						}else if("10".equals(lrisk_type)){   //低风险
							crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("lrisk_total_amt")).add(self_amt);
							
							kColl.put("crd_cir_amt", kColl_details.getDataValue("lrisk_cir_amt"));  //循环额度 
							kColl.put("crd_one_amt", kColl_details.getDataValue("lrisk_one_amt"));  //一次性额度
							
							/** 低风险原额度取协议中低风险额度+自助额度   2014-01-18 */
							String lrisk_totl_amt_str = kColl.getDataValue("lrisk_totl_amt")+"";
							BigDecimal org_lrisk_totl_amt = new BigDecimal(lrisk_totl_amt_str);
							kColl.addDataField("org_crd_totl_amt", org_lrisk_totl_amt.add(self_amt));
						}
						
						kColl.put("crd_totl_amt",crd_totl_amt);  //将低风险与非风险金额汇总
						kColl.put("totl_amt",crd_totl_amt);  //将低风险与非风险金额汇总
						
					}					
					kColl.addDataField("org_self_amt", kColl.getDataValue("self_amt"));
					
					kColl.removeDataElement("input_date");  //变更时登记日期清除，从新取系统日期
					kColl.removeDataElement("serno");  //变更时清除原流水号
					kColl.removeDataElement("flow_type");  //变更时清除流程类型
					kColl.removeDataElement("approve_status");  //变更时清除申请状态
					kColl.removeDataElement("input_id");  //变更时清除登记人
					kColl.removeDataElement("input_br_id");  //变更时清除登记机构
					kColl.removeDataElement("manager_id");  //变更时清除主管客户经理
					kColl.removeDataElement("manager_br_id");  //变更时清除主管机构
				}
				kColl.addDataField("app_type", "02");  //设置授信申请类型为 变更 
				kColl.addDataField("cus_type", cus_type);
				kColl.addDataField("belg_line", belg_line);
				
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				//复制数据到申请分项表
				lmtComponent.createLmtAppDetailsRecord((String)kColl.getDataValue("agr_no"), serno, lrisk_type);
				//复制数据到申请分项历史表
				lmtComponent.createLmtAppDetailsHisRecord((String)kColl.getDataValue("agr_no"), serno, lrisk_type);
			}else{ 	 //客户没有有效授信
				kColl.addDataField("app_type", "01");  //设置授信申请类型为 新增 
				kColl.addDataField("cus_id", cus_id);  //客户码加入到kColl中，用于翻译客户名称
				kColl.addDataField("cus_type", cus_type);
				kColl.addDataField("belg_line", belg_line);
				
				kColl.addDataField("biz_type", "01");  //授信业务类型
				kColl.addDataField("cur_type", "CNY");  //币种
				kColl.addDataField("crd_totl_amt", 0.00);  //授信总金额
				kColl.addDataField("is_self_revolv", "2");  //是否开通自助循环
				kColl.addDataField("is_same_debtor", "2");  //是否有共同债务人
				kColl.addDataField("is_open_pos", "2");  //是否开通pos支付
				kColl.addDataField("util_mode", "02");  //提用方式
			}
			kColl.put("cus_id", cus_id);
			kColl.put("lrisk_type", lrisk_type);  //是否低风险
			kColl.put("serno", serno);  //流水号
			kColl.put("app_date", context.getDataValue("OPENDAY"));  //申请日期
			kColl.put("flow_type", "01");  //流程类型
			
			kColl.put("input_date", context.getDataValue("OPENDAY"));  //登记日期
			kColl.put("approve_status", "000");  //申请状态
			
			kColl.put("input_id",context.getDataValue("currentUserId"));
			kColl.put("input_br_id",context.getDataValue("organNo"));
			kColl.put("manager_id", context.getDataValue("LmtAppIndiv.manager_id"));  //主管客户经理
			kColl.put("manager_br_id", manager_br_id);  //主管机构
			
			kColl.setName("LmtAppIndiv");
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);

			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
			kColl.addDataField("type", "Y");
			kColl.setName("LmtAppIndiv");
			
			this.putDataElement2Context(kColl, context);
			
			context.addDataField("flag", "success");
			context.addDataField("serno", serno);
			context.addDataField("msg", "保存成功");
		}catch(Exception e){
			context.addDataField("flag", "error");
			context.addDataField("serno", "");
			context.addDataField("msg", "异步生成个人授信申请错误，错误描述："+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}
}
