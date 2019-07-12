package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.ccr.msi.CcrServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.platform.workflow.msi.Workflow4BIZIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class LmtAppIndivFlow extends CMISComponent implements BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		
		Connection connection = null;
		try{
			connection = this.getConnection();
			String openDate = context.getDataValue("OPENDAY").toString();
		
			String serno_value = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			/**获取审批中变更信息*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			Workflow4BIZIface wfi4biz = (Workflow4BIZIface)serviceJndi.getModualServiceById("workflow4BizService", "workflow");
			HashMap hm = new HashMap();
			hm = (HashMap)wfi4biz.getAllModifiedBizVar(wfiMsg.getInstanceid(), connection);
			
			KeyedCollection kColl_agr = dao.queryDetail(wfiMsg.getTableName(), serno_value, connection);
			//修改申请表的办结日期
			kColl_agr.setDataValue("over_date", openDate);
			dao.update(kColl_agr, connection);
			
			//自助金额
			String self_amt = (String)hm.get("self_amt");
			if(self_amt!=null && !"".equals(self_amt)){
				kColl_agr.put("self_amt", self_amt);
			}
			
			String cus_id = kColl_agr.getDataValue("cus_id").toString();
			
			/**  查询授信客户下是否存在有效的授信协议    低风险与非低风险业务共用协议表，通过查询是否存在有效授信协议来判断走新增还是修改		2013-11-25  唐顺岩  */
			List<String> list = new ArrayList<String>();
			list.add("agr_no");
			list.add("totl_end_date");
			list.add("totl_start_date");
			String condition = " WHERE CUS_ID='"+ cus_id +"' AND AGR_STATUS='002' AND to_char(add_months(to_date(TOTL_END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ";
			KeyedCollection kColl = new KeyedCollection(); 
			kColl = dao.queryFirst("LmtAgrIndiv", list, condition, connection);
			
			String lmt_agr_no = "";
			String totlEndDate = openDate;  //授信到期日
			//存在有效授信协议
			if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
			//if(!"".equals(kColl_agr.getDataValue("app_type")) && ("01".equals(kColl_agr.getDataValue("app_type"))|| "05".equals(kColl_agr.getDataValue("app_type")))){  //新增
				//lmt_agr_no = kColl_agr.getDataValue("agr_no").toString();
				lmt_agr_no = kColl.getDataValue("agr_no").toString();
				//授信总额期限
				//String totlStartDate = kColl_agr.getDataValue("totl_start_date").toString();
				//String term = kColl_agr.getDataValue("term").toString();
				//String termType = kColl_agr.getDataValue("term_type").toString();
				//String totlEndDate = LmtUtils.computeEndDate(totlStartDate, term, termType);
				kColl_agr.setDataValue("totl_start_date", kColl.getDataValue("totl_start_date"));  //如果存在协议 则从协议表中取 起始日期
				//自助额度期限
				String is_adj_term_self = (String)kColl_agr.getDataValue("is_adj_term_self");
				if("1".equals(is_adj_term_self)){  //是否调整自助额度期限 为是时重新计算到期日
					String selfTerm = kColl_agr.getDataValue("self_term").toString();
					String selfStartDate = kColl_agr.getDataValue("self_start_date").toString();
					String selfEndDate = LmtUtils.computeEndDate(selfStartDate, "002", selfTerm);
					kColl_agr.setDataValue("self_end_date", selfEndDate);
				}
			//}else if(!"".equals(kColl_agr.getDataValue("app_type")) && ("02".equals(kColl_agr.getDataValue("app_type")) || "06".equals(kColl_agr.getDataValue("app_type")))){   //变更
			}else{
				//生成授信协议编号
				lmt_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
				kColl_agr.setDataValue("totl_start_date", openDate);
				kColl_agr.setDataValue("self_start_date", openDate);
			}
			
			//设置自助额度到期日
			String selfEndDate = "";
			if(null != kColl_agr.getDataValue("self_term") && !"".equals(kColl_agr.getDataValue("self_term"))){  //有设置自助额度的情况下
				selfEndDate = LmtUtils.computeEndDate(openDate, "002", kColl_agr.getDataValue("self_term").toString());
				kColl_agr.setDataValue("self_end_date", selfEndDate);
			}
			
			kColl_agr.put("agr_no", lmt_agr_no);  //设置协议号
			
			kColl_agr.removeDataElement("org_crd_totl_amt");   //清除变更前授信总额
			kColl_agr.removeDataElement("org_self_amt");    //清除变更前循环授信总额
			kColl_agr.removeDataElement("org_crd_one_amt");    //清除变更前一次性授信总额
			kColl_agr.removeDataElement("approve_status");  //清除审批状态
			kColl_agr.removeDataElement("app_date");	//申请日期
			kColl_agr.removeDataElement("over_date");  //清除到期日期
//			kColl_agr.removeDataElement("app_type");  //申请类型
			
			kColl_agr.removeDataElement("term");  //期限
			kColl_agr.removeDataElement("term_type");  //期限类型
			
			kColl_agr.setName("LmtAgrIndiv");
			
			condition = " WHERE SERNO='"+serno_value+"'";
			IndexedCollection iColl = dao.queryList("LmtAppDetails", condition, connection);
			String endDate = "";
			
			double crd_totl_amt = 0;//循环总额
			for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
				KeyedCollection kColl_details = (KeyedCollection) iterator.next();
				kColl_details.addDataField("lmt_status", "10");  //额度状态默认为[正常]
				
				/**调用流程接口取流程中分项变更信息 add by tangzf 2014.01.13 start**/
				String finalTerm = (String)hm.get("term@"+serno_value+"@"+kColl_details.getDataValue("org_limit_code"));//期限
				if(finalTerm!=null && !"".equals(finalTerm)){
					kColl_details.put("term", finalTerm);
				}
				String finalTermType = (String)hm.get("termType@"+serno_value+"@"+kColl_details.getDataValue("org_limit_code"));//期限类型
				if(finalTermType!=null && !"".equals(finalTermType)){
					kColl_details.put("term_type", finalTermType);
				}
				String finalCrdAmt = (String)hm.get("crdAmt@"+serno_value+"@"+kColl_details.getDataValue("org_limit_code"));//授信金额
				if(finalCrdAmt!=null && !"".equals(finalCrdAmt)){
					kColl_details.put("crd_amt", finalCrdAmt);
				}
				/**调用流程接口取流程中变更信息 end**/
				
				/** 授信启用金额不再根据担保  有效担保合同金额来设置  2014-01-08 注释
				 * 启用金额，授信担保方式不为信用时  
				 * 当授信项下所关联的担保合同总额小于或等于授信金额时，启用金额等于担保合同总额；
				 * 当授信项下所关联的担保合同总额大于授信金额时，启用金额等于授信金额
				 * */
//				if(!"400".equals(kColl_details.getDataValue("guar_type"))){
//					BigDecimal eable_amt = LmtUtils.computLmtEnableAmt(serno_value, kColl_details, context, connection, dao);
//					kColl_details.addDataField("enable_amt", eable_amt); 
//				}else{   //担保方式为信用时直接取授信金额为启用金额
//					kColl_details.addDataField("enable_amt", kColl_details.getDataValue("crd_amt"));  //启用金额默认等于授信金额
//				}
				
				/** 供应链授信时将授信类别置为 供应链授信    需求编号：20131202001  2013-12-09  唐顺岩 */
				kColl_details.addDataField("enable_amt", kColl_details.getDataValue("crd_amt"));  
				/** 启用金额设值完成  */
				
				crd_totl_amt += Double.parseDouble((String)kColl_details.getDataValue("crd_amt"));
				
				if("".equals(kColl_details.getDataValue("cus_id"))){   //申请表中客户码为空的情况
					kColl_details.setDataValue("cus_id", cus_id);
				}
				kColl_details.addDataField("agr_no", lmt_agr_no);  //授信协议编号
				
				kColl_details.setName("LmtAgrDetails");
				String update_flag = kColl_details.getDataValue("update_flag").toString();
				
				kColl_details.removeDataElement("update_flag");   //清除修改类型
				
				if("01".equals(update_flag)){  //新增时
					endDate = LmtUtils.computeEndDate(openDate, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
					kColl_details.setDataValue("start_date", openDate);
					kColl_details.setDataValue("end_date", endDate);
					
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------个人授信   新增授信时将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 开始---------------", null);
					//将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系
					KeyedCollection kColl4Value = new KeyedCollection();
					kColl4Value.addDataField("LIMIT_CODE", (String)kColl_details.getDataValue("org_limit_code"));
					kColl4Value.addDataField("AGR_NO", lmt_agr_no);
					
					KeyedCollection kColl4Parameter = new KeyedCollection();
					kColl4Parameter.addDataField("LIMIT_CODE", (String)kColl_details.getDataValue("org_limit_code"));
					kColl4Parameter.addDataField("SERNO", serno_value);
					
					SqlClient.executeUpd("createRLmtGuarContRecord", kColl4Parameter, kColl4Value, null,this.getConnection());
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------个人授信   新增授信时将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 结束---------------", null);
					
					//插入台账表
					kColl_details.removeDataElement("org_limit_code");  //清除台账授信额度编号
					dao.insert(kColl_details, connection);
				}else{   //变更
					//分项变更时不管是否调整期限都从新计算期限，原因：审批中会调整期限    2014-01-18 
					if("1".equals(kColl_details.getDataValue("is_adj_term"))){  //并且调整期限
						//String star_date = kColl_details.getDataValue("start_date").toString();
						//endDate = LmtUtils.computeEndDate(star_date, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
						endDate = LmtUtils.computeEndDate(openDate, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
						kColl_details.setDataValue("end_date", endDate);
					}
					
					String org_limit_code =  (String)kColl_details.getDataValue("org_limit_code");

					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------个人授信   变更授信时删除原分项原额度台账与担保合同关系数据，将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系     开始---------------", null);
					//删除原分项原额度台账与担保合同关系数据
					LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
					Map<String, String> conditionFields = new HashMap<String, String>();
					conditionFields.put("limit_code", org_limit_code);
					lmtComponent.deleteByField("RLmtGuarCont", conditionFields);
					//将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系
					KeyedCollection kColl4Value = new KeyedCollection();
					kColl4Value.addDataField("LIMIT_CODE", org_limit_code);
					kColl4Value.addDataField("AGR_NO", lmt_agr_no);
					
					KeyedCollection kColl4Parameter = new KeyedCollection();
					kColl4Parameter.addDataField("LIMIT_CODE", org_limit_code);
					kColl4Parameter.addDataField("SERNO", serno_value);
					
					SqlClient.executeUpd("createRLmtGuarContRecord", kColl4Parameter, kColl4Value, null,this.getConnection());
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------个人授信   变更授信时删除原分项原额度台账与担保合同关系数据，将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 结束---------------", null);
					
					kColl_details.setDataValue("limit_code", kColl_details.getDataValue("org_limit_code"));  //将原授信额度编号赋给授信额度编号，用于做主键更新
					kColl_details.removeDataElement("org_limit_code");  //清除台账授信额度编号
					dao.update(kColl_details, connection);
				}
			}
			
			//将授信台账数据根据到期日期排序
			kColl_agr.addDataField("end_date", kColl_agr.getDataValue("self_end_date"));  //在协议中新增end_date设值为自助额度到期日
			
			//有设置自助额度的情况下
			if(null != kColl_agr.getDataValue("is_self_revolv") && "1".equals(kColl_agr.getDataValue("is_self_revolv"))){
				kColl_agr.put("end_date", kColl_agr.getDataValue("self_end_date"));
				iColl.add(kColl_agr); //将协议表也加入到排序集合中，排序完成后就能判断出各分项及自助额度的最大到期日
			}
			kColl_agr.removeDataElement("self_term");  //移除自助期限
			
			/** 判断该协议下是否存在与当前授信的低风险业务类型不一致的有效授信台账，取其最大到期日   2014-01-21  唐顺岩  */
			if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
				String agr_details_sql = " WHERE AGR_NO='"+kColl.getDataValue("agr_no")+"' AND LRISK_TYPE <> '"+kColl_agr.getDataValue("lrisk_type")+"' ";
				agr_details_sql += " AND SUB_TYPE = '01' AND LMT_STATUS<>'30' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ORDER BY END_DATE DESC ";
				KeyedCollection kcoll_agr_details = dao.queryFirst("LmtAgrDetails", null, agr_details_sql, connection);
				if(null!=kcoll_agr_details && null!=kcoll_agr_details.getDataValue("limit_code")){  
					iColl.add(kcoll_agr_details);
				}
				//iColl.add(kColl);
			}
			/** END */
			
			iColl = LmtUtils.sort(iColl);
			KeyedCollection kColl_last = (KeyedCollection)iColl.get(0);  //取排序后的最后一条记录
			totlEndDate = kColl_last.getDataValue("end_date").toString();  //获得分项中最大的到期日期
			
			kColl_agr.setDataValue("totl_end_date", totlEndDate);  //设置个人额度到期日
			kColl_agr.addDataField("agr_status", "002");  //设置协议状态为 生效
			
			/**  查询授信客户下是否存在有效的授信协议    低风险与非低风险业务共用协议表，通过查询是否存在有效授信协议来判断走新增还是修改		2013-11-25  唐顺岩  */
			kColl_agr.removeDataElement("app_type");  //清除申请类型
//			if(!"".equals(kColl_agr.getDataValue("app_type")) && ("01".equals(kColl_agr.getDataValue("app_type")) || "05".equals(kColl_agr.getDataValue("app_type")))){  //新增
//				kColl_agr.removeDataElement("app_type");  //清除申请类型
//				dao.insert(kColl_agr, connection); 
//			}else if(!"".equals(kColl_agr.getDataValue("app_type")) && ("02".equals(kColl_agr.getDataValue("app_type")) || "06".equals(kColl_agr.getDataValue("app_type")))){   //变更
//				kColl_agr.removeDataElement("app_type");  //清除申请类型
//				dao.update(kColl_agr, connection);
//			}
			//设置授信总额
			kColl_agr.put("crd_totl_amt", crd_totl_amt);
			//低风险
			if("10".equals(kColl_agr.getDataValue("lrisk_type"))){
				kColl_agr.addDataField("lrisk_totl_amt",kColl_agr.getDataValue("crd_totl_amt"));
				kColl_agr.remove("crd_totl_amt");
			}
			
			if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
				dao.update(kColl_agr, connection);
			}else{
				dao.insert(kColl_agr, connection); 
			}
			/** END */
			//调用评级模块接口
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CcrServiceInterface service = (CcrServiceInterface)serviceJndi.getModualServiceById("ccrServices", "ccr");
			service.updateCcrAppInfo(serno_value, "997", context,connection);
			
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", "");
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, context, connection);	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, context)){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new EMPException("个人授信流程审批报错，错误描述："+e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		try {
			connection = this.getConnection();
			String serno_value = wfiMsg.getPkValue();
			//调用评级模块接口
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CcrServiceInterface service;
			service = (CcrServiceInterface)serviceJndi.getModualServiceById("ccrServices", "ccr");
			service.updateCcrAppInfo(serno_value, "998", context,connection);
		} catch (Exception e) {
			throw new EMPException("个人授信流程审批报错，错误描述："+e.getMessage());
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String app_type = (String)kc.getDataValue("app_type");
		String instanceId = "";//流程实例号
		String lriskType = (String)kc.getDataValue("lrisk_type");//低风险业务类型
		String appTypeAuth = "";//个人授权业务类型
		//获取流程实例号作为规则参数
		String condition = "where pk_value='"+pkVal+"' and table_name='"+tabModelId+"'";
		IndexedCollection iCollJoin = dao.queryList("WfiJoin", condition, this.getConnection());
		if(iCollJoin.size()>0){
			KeyedCollection kCollJoin = (KeyedCollection)iCollJoin.get(0);
			instanceId = (String)kCollJoin.getDataValue("instanceid");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("amt", (String)kc.getDataValue("crd_totl_amt"));
		param.put("lrisk_type", (String)kc.getDataValue("lrisk_type"));
		param.put("bizline", "BL300");
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("isIndiv", "Y");//需求编号：XD140715024 Edited by FCL 2014-07-22   
		/* added by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发 start */
		param.put("isIndivSH", "N");
		/* added by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发 end */
		
		/**责任人存在多个机构时取责任机构*/
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ShuffleServiceInterface shuffleService;
		/**  审批机构直接取登录机构   2014-06-03    唐顺岩  
		//OrganizationServiceInterface userService;
		//List<SOrg> orgslist = null;
		END */
		try {
			/**  审批机构直接取登录机构   2014-06-03    唐顺岩   */
			//userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			//orgslist = userService.getDeptOrgByActno(manager_id, this.getConnection());
			
			manager_br_id = (String)this.getContext().getDataValue("organNo");
			/** END */
			shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		/**  审批机构直接取登录机构   2014-06-03    唐顺岩   
		if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
			manager_br_id = orgslist.get(0).getOrganno();
		}
	    END */
		Map<String, String> modelMap = new HashMap<String, String>();
		
		modelMap.put("IN_MANAGERBRID", manager_br_id);
		
		modelMap.put("IN_BIZ_BRID", (String)kc.getDataValue("manager_br_id"));    //增加业务管理机构参数   2016-06-26
		/**modified by lisj 2015-5-10 需求编号：【XD150407025】分支机构授信审批权限配置（注释旧版本） begin**/
		/*String existFlag = "";
		if("10".equals(lriskType)){
			if("05".equals(app_type)){//复议首次
				appTypeAuth = "01_04_10";
			}else if("06".equals(app_type)){//复议非首次
				appTypeAuth = "02_04_10";
			}else{
				appTypeAuth = app_type + "_04_10";//个人低风险
			}
		}else{
			*//** 获取授信申请分项中授权金额较低的分项 add by tangzf *//*
			modelMap.put("IN_SERNO", pkVal);
			modelMap.put("IN_CUSID", (String)kc.getDataValue("cus_id"));
			modelMap.put("IN_APPTYPE", app_type);
			
			String condAuth = "";
			String condDet = "where serno='"+pkVal+"' and crd_amt>0 ";//2014-07-25 Edited by FCL 过滤掉授信分项额度为0的记录
			IndexedCollection iCollDet = dao.queryList("LmtAppDetails", condDet, this.getConnection());
			for(int i=0;i<iCollDet.size();i++){
				KeyedCollection kCollDet = (KeyedCollection)iCollDet.get(i);
				modelMap.put("IN_LIMITCODE", (String)kCollDet.getDataValue("limit_code"));
				try {
					Map outMap=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTINDIVAPP", modelMap);
					String appType = (String)outMap.get("OUT_个人授权类型");
					String tmpFlag = (String)outMap.get("OUT_个人授信是否有配置权限");
					 modified by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发 start 
					if(Double.parseDouble(tmpFlag)==0){//该分项没有进行授权
						existFlag = "none";
					}
					 modified by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发 end 
					if(appType!=null&&!"".equals(appType)){
						condAuth = condAuth + "'"+appType+"'," ;
					}
				} catch (Exception e) {
					throw new EMPException(e.getMessage());
				}
			}
			 added by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发，单独20w支行权限 start 
			if(condAuth.endsWith(",")&&condAuth.indexOf("04_20_400_100080")>=0){
				existFlag = "have";
				param.put("isIndivSH", "Y");
			}
			 added by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发，单独20w支行权限 end 
			if(!"none".equals(existFlag) && condAuth.endsWith(",")){
				condAuth = condAuth.substring(0, condAuth.length()-1);
				 modified by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发，单独20w支行权限 start 
				String condition1 = "";
				if(condAuth.indexOf("04_20_400_100080")>=0){
					condition1 = " and app_type like '%04_20_400_100080' ";
				}
				IndexedCollection iCollRight = dao.queryList("WfiBpRight", "where approve_org='"+manager_br_id+"' and app_type in("+condAuth+") "+condition1+" order by single_amt asc", this.getConnection());
				if(iCollRight.size()>0){
					KeyedCollection kCollRight = (KeyedCollection)iCollRight.get(0);
					appTypeAuth = (String)kCollRight.getDataValue("app_type");
				}else{
					appTypeAuth = "000";
				}
				 modified by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发，单独20w支行权限 end 
			}
		}*/
		/**modified by lisj 2015-5-10 需求编号：【XD150407025】分支机构授信审批权限配置（注释旧版本） end**/
		/**modified by lisj 2015-4-1 需求编号：【XD150407025】分支机构授信审批权限配置 begin**/
		String approve_org = "";
		try {
			/**若存在没有进行授权的分项则直接往上提交**/
			/**if("none".equals(existFlag)){
				
			}else{
				modelMap.put("IN_SERNO", pkVal);
				modelMap.put("IN_CUSID", (String)kc.getDataValue("cus_id"));
				modelMap.put("IN_INSTANCEID", instanceId);
				modelMap.put("IN_APPTYPE", app_type);
				modelMap.put("IN_INDIVAPPTYPE", appTypeAuth);
				Map<String, String> outMap4LmtSCORule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTSCOPRATIONRULE", modelMap);//授信特殊权限授权入口（数据需后台维护）
				Map<String, String> outMap4LmtLCRGRule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTLCRGENERALRULE", modelMap);//授信权限配置总规则
				String flowType4SCORule  = (String) outMap4LmtSCORule.get("OUT_FLOWTYPE");
				String flowType4LmtLCRGRule= (String)outMap4LmtLCRGRule.get("OUT_FLOWTYPE");
				if("ALLOW".equals(flowType4SCORule)){
					approve_org  = "S0200";
				}else if("ALLOW".equals(flowType4LmtLCRGRule)){
					approve_org  = "S0200";
				}else if("ALLOWBRANCH".equals(flowType4LmtLCRGRule)){
					approve_org = "S0226";
				}
			}
			/**注释流程审批授权规则代码，现在授信依据LMTLCRGENERALRULE授权配置总规则，故不启用**/
			/**if(approve_org==null||"".equals(approve_org)){
				Map<String, String> outMap=shuffleService.fireTargetRule("LMTBPRIGHT", "HEADRULE", modelMap);
				//param.put("approve_org", (String)outMap.get("OUT_终审岗位"));//岗位
				System.out.println(outMap);
				approve_org = (String)outMap.get("OUT_终审岗位");
				
			}**/
			/**
			 
			
			if(approve_org==null||"".equals(approve_org)){
				modelMap.put("IN_SERNO", pkVal);
				modelMap.put("IN_CUSID", (String)kc.getDataValue("cus_id"));
				modelMap.put("IN_INSTANCEID", instanceId);
				modelMap.put("IN_APPTYPE", app_type);
				//modelMap.put("IN_INDIVAPPTYPE", appTypeAuth);
				String openDay = (String)this.getContext().getDataValue("OPENDAY");
				modelMap.put("IN_OPENDAY",openDay);
				Map<String, String> outMap4LmtSCORule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTSCOPRATIONRULE", modelMap);//特别授权入口规则（无条件执行支行权限）
				String flowType4SCORule = (String) outMap4LmtSCORule.get("OUT_FLOWTYPE");
				Map<String, String> outMap4LmtSCRRule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTSPECIALCREDITRIGHT", modelMap);//授信特殊权限授权（数据需后台维护）
				String flowType4SCRRule  = (String) outMap4LmtSCRRule.get("OUT_FLOWTYPE");
				Map<String, String> outMap4LmtLCRGRule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTLCRGENERALRULE", modelMap);//授信权限配置总规则
				String flowType4LmtLCRGRule= (String)outMap4LmtLCRGRule.get("OUT_FLOWTYPE");
				String isLifeLoan4LmtLCRGRule = (String)outMap4LmtLCRGRule.get("OUT_ISLIFELOAN");//生活贷标志
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"特别授权入口规则（无条件执行支行权限）:"+flowType4SCORule,null);
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"授信特殊权限授权（数据需后台维护）:"+flowType4SCRRule,null);
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"授信权限配置总规则:"+flowType4LmtLCRGRule,null);
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"授信权限配置总规则:"+isLifeLoan4LmtLCRGRule,null);
				if("ALLOW".equals(flowType4SCRRule)){
					approve_org  = "S0200";
				}else if ("ALLOW".equals(flowType4SCORule)){
					approve_org  = "S0200";
				}else if("ALLOW".equals(flowType4LmtLCRGRule)){
					approve_org  = "S0200";
				}else if("ALLOWBRANCH".equals(flowType4LmtLCRGRule)){
					approve_org = "S0226";
				}
				if("Y".equals(isLifeLoan4LmtLCRGRule)){
					param.put("isIndivSH", "Y");
				}
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"最终岗位:"+approve_org,null);
			}
			param.put("approve_supor_org", "");//支行行长-->总行权限
			KeyedCollection  SOrg = dao.queryAllDetail("SOrg", manager_br_id, this.getConnection());
			String suporganno = (String) SOrg.getDataValue("suporganno");//上级机构
			if(suporganno!=null && !"".equals(suporganno) && "9350500000".equals(suporganno)){
				param.put("approve_supor_org", "D0005");//支行行长-->总行权限
			}				

			param.put("approve_org", approve_org);//岗位
			modified by xujh 这里规则引擎不需要调，因为不用这个来判断岗位了
			 **/
			/**modified by lisj 2015-4-1 需求编号：【XD150407025】分支机构授信审批权限配置 end**/
			System.out.println(param);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return param;
	}

}
