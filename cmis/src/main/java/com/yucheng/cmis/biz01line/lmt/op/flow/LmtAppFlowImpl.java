package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
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

public class LmtAppFlowImpl extends CMISComponent implements BIZProcessInterface {

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
			KeyedCollection kColl_agr = dao.queryDetail(wfiMsg.getTableName(), serno_value, connection);
			
			//修改申请表的办结日期
			kColl_agr.setDataValue("over_date", context.getDataValue("OPENDAY"));
			dao.update(kColl_agr, connection);
			
			String cus_id = kColl_agr.getDataValue("cus_id").toString();
			
			/**  查询授信客户下是否存在有效的授信协议    低风险与非低风险业务共用协议表，通过查询是否存在有效授信协议来判断走新增还是修改		2013-11-26  唐顺岩  */
			List<String> list = new ArrayList<String>();
			list.add("agr_no");
			list.add("end_date");
			list.add("crd_totl_amt");
			list.add("lrisk_totl_amt");
			String condition = " WHERE CUS_ID='"+ cus_id +"' AND AGR_STATUS='002' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),12),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ";
			KeyedCollection kColl = new KeyedCollection(); 
			kColl = dao.queryFirst("LmtAgrInfo", list, condition, connection);
			
			String lmt_agr_no = "";
			//存在有效授信协议
			if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
				lmt_agr_no = kColl.getDataValue("agr_no").toString();
			}else{  //不存在授信协议
				//生成授信协议编号
				lmt_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
				kColl_agr.addDataField("start_date", openDate);
			}
			kColl_agr.put("agr_no", lmt_agr_no);   //设置协议号
			
//			if(!"".equals(kColl_agr.getDataValue("app_type")) && ("01".equals(kColl_agr.getDataValue("app_type")) || "05".equals(kColl_agr.getDataValue("app_type")))){  //新增、复议
//				//生成授信协议编号
//				lmt_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
//				kColl_agr.setDataValue("agr_no", lmt_agr_no);
//				kColl_agr.addDataField("start_date", openDate);
//			}else if(!"".equals(kColl_agr.getDataValue("app_type")) && ("02".equals(kColl_agr.getDataValue("app_type")) || "06".equals(kColl_agr.getDataValue("app_type")))){  //变更、变更复议
//				lmt_agr_no = kColl_agr.getDataValue("agr_no").toString();
//			}
			/** END */
			
			kColl_agr.removeDataElement("org_crd_totl_amt");   //清除变更前授信总额
			kColl_agr.removeDataElement("org_crd_cir_amt");    //清除变更前循环授信总额
			kColl_agr.removeDataElement("org_crd_one_amt");    //清除变更前一次性授信总额
			kColl_agr.removeDataElement("approve_status");  //清除审批状态
			kColl_agr.removeDataElement("over_date");  //清除到期日期
			
			kColl_agr.setName("LmtAgrInfo");
			
			condition = " WHERE SERNO='"+serno_value+"'";
			IndexedCollection iColl = dao.queryList("LmtAppDetails", condition, connection);
			String endDate = "";
			
			double crd_cir_amt_f = 0;//循环总额
			double crd_one_amt_f = 0;//一次性总额
			for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
				KeyedCollection kColl_details = (KeyedCollection) iterator.next();
				kColl_details.addDataField("lmt_status", "10");  //额度状态默认为[正常]
				
				/**调用流程接口取流程中变更信息 add by tangzf 2014.01.03 start**/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				Workflow4BIZIface wfi4biz = (Workflow4BIZIface)serviceJndi.getModualServiceById("workflow4BizService", "workflow");
				HashMap hm = new HashMap();
				hm = (HashMap)wfi4biz.getAllModifiedBizVar(wfiMsg.getInstanceid(), connection);
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
				String limit_type = (String)kColl_details.getDataValue("limit_type");
				if("01".equals(limit_type)){//循环额度
					crd_cir_amt_f += Double.parseDouble((String)kColl_details.getDataValue("crd_amt"));
				}else{//一次性额度
					crd_one_amt_f += Double.parseDouble((String)kColl_details.getDataValue("crd_amt"));
				}
				
				/** 授信启用金额不再根据担保  有效担保合同金额来设置  2014-01-08 注释
				 * 启用金额，授信担保方式不为信用时  
				 * 当授信项下所关联的担保合同总额小于或等于授信金额时，启用金额等于担保合同总额；
				 * 当授信项下所关联的担保合同总额大于授信金额时，启用金额等于授信金额
				 * */
//				if(!"400".equals(kColl_details.getDataValue("guar_type"))){
//					BigDecimal eable_amt = LmtUtils.computLmtEnableAmt(serno_value, kColl_details, context, connection, dao);
//					kColl_details.addDataField("enable_amt", eable_amt);
//				}else{  //担保方式为信用时直接取授信金额为启用金额
//					kColl_details.addDataField("enable_amt", kColl_details.getDataValue("crd_amt"));
//				}
				
				/** 启用金额直接改为取授信金额   2014-01-08  唐顺岩    */
				kColl_details.addDataField("enable_amt", kColl_details.getDataValue("crd_amt"));  //启用金额默认等于授信金额
				/** 启用金额设值完成  */
				
				if("".equals(kColl_details.getDataValue("cus_id"))){   //申请表中客户码为空的情况
					kColl_details.setDataValue("cus_id", cus_id);
				}
				kColl_details.addDataField("agr_no", lmt_agr_no);  //授信协议编号
				
				kColl_details.setName("LmtAgrDetails");
				String update_flag = kColl_details.getDataValue("update_flag").toString();
				kColl_details.removeDataElement("update_flag");   //清除修改类型
				
				/** 供应链授信时将授信类别置为 供应链授信    需求编号：20131202001  2013-12-09  唐顺岩 */
				if("05".equals(kColl_details.getDataValue("sub_type"))){
				//	kColl_details.put("lmt_type", "03");
				}
				/** 需求编号：20131202001  END */
				
				if("01".equals(update_flag)){  //新增时
					endDate = LmtUtils.computeEndDate(openDate, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
					kColl_details.setDataValue("start_date", openDate);
					kColl_details.setDataValue("end_date", endDate);
					
					//如果是授信新增将分项与担保合同关系表的协议编号更新为生成的协议编号
					//SqlClient.executeUpd("updateRLmtAppGuarContRecord", kColl_details.getDataValue("limit_code"), lmt_agr_no, null, connection);
					//将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------单一法人授信   新增授信时将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 开始---------------", null);
					KeyedCollection kColl4Value = new KeyedCollection();
					kColl4Value.addDataField("LIMIT_CODE", (String)kColl_details.getDataValue("org_limit_code"));
					kColl4Value.addDataField("AGR_NO", lmt_agr_no);
					
					KeyedCollection kColl4Parameter = new KeyedCollection();
					kColl4Parameter.addDataField("LIMIT_CODE", (String)kColl_details.getDataValue("org_limit_code"));
					kColl4Parameter.addDataField("SERNO", serno_value);
					
					SqlClient.executeUpd("createRLmtGuarContRecord", kColl4Parameter, kColl4Value, null,this.getConnection());
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------单一法人授信   新增授信时将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 结束---------------", null);
					
					//插入台账表
					kColl_details.removeDataElement("org_limit_code");  //清除台账授信额度编号
					dao.insert(kColl_details, connection);
				}else{   //变更
					//分项变更时不管是否调整期限都从新计算期限，原因：审批中会调整期限    2014-01-18 
					if("1".equals(kColl_details.getDataValue("is_adj_term"))){  //并且调整期限
						endDate = LmtUtils.computeEndDate(openDate, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
						kColl_details.setDataValue("end_date", endDate);
					}
					
					String org_limit_code =  (String)kColl_details.getDataValue("org_limit_code");
					
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------单一法人授信   变更授信时删除原分项原额度台账与担保合同关系数据，将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系     开始---------------", null);
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
					
					SqlClient.executeUpd("createRLmtGuarContRecord", kColl4Parameter, kColl4Value, null, connection);
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------单一法人授信   变更授信时删除原分项原额度台账与担保合同关系数据，将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 结束---------------", null);
					
					kColl_details.setDataValue("limit_code", org_limit_code);  //将原授信额度编号赋给授信额度编号，用于做主键更新
					kColl_details.removeDataElement("org_limit_code");  //清除台账授信额度编号
					dao.update(kColl_details, connection);
				}
			}
			
			/** 判断该协议下是否存在与当前授信的低风险业务类型不一致的有效授信台账 取其最大到期日   2014-01-21  唐顺岩  */
			if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
				String agr_details_sql = " WHERE AGR_NO='"+kColl.getDataValue("agr_no")+"' AND LRISK_TYPE <> '"+kColl_agr.getDataValue("lrisk_type")+"' ";
				agr_details_sql += " AND SUB_TYPE IN('01','05') AND LMT_STATUS<>'30' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ORDER BY END_DATE DESC ";
				KeyedCollection kcoll_agr_details = dao.queryFirst("LmtAgrDetails", null, agr_details_sql, connection);
				if(null!=kcoll_agr_details && null!=kcoll_agr_details.getDataValue("limit_code")){  
					iColl.add(kcoll_agr_details);
				}
				//iColl.add(kColl);
			}
			/** END */
			
			/**统计本次申请的一次性额度、循环额度、授信总额度**/
			kColl_agr.put("crd_cir_amt", crd_cir_amt_f);
			kColl_agr.put("crd_one_amt", crd_one_amt_f);
			kColl_agr.put("crd_totl_amt", crd_cir_amt_f+crd_one_amt_f);
			
			//将授信台账数据根据到期日期排序
			iColl = LmtUtils.sort(iColl);
			KeyedCollection kColl_last = (KeyedCollection)iColl.get(0);  //取排序后的最后一条记录
			String arg_end_date = kColl_last.getDataValue("end_date").toString();  //获得分项中最大的到期日期
	        
			kColl_agr.addDataField("end_date", arg_end_date);  //设置到期日期
			kColl_agr.addDataField("agr_status", "002");  //设置协议状态为 生效
			
			/**  查询授信客户下是否存在有效的授信协议    低风险与非低风险业务共用协议表，通过查询是否存在有效授信协议来判断走新增还是修改		2013-11-25  唐顺岩  */
//			if(!"".equals(kColl_agr.getDataValue("app_type")) && ("01".equals(kColl_agr.getDataValue("app_type")) || "05".equals(kColl_agr.getDataValue("app_type")) )){  //新增、复议
//				kColl_agr.removeDataElement("app_type");  //清除申请类型
//				dao.insert(kColl_agr, connection);
//			}else if(!"".equals(kColl_agr.getDataValue("app_type")) && ("02".equals(kColl_agr.getDataValue("app_type")) || "06".equals(kColl_agr.getDataValue("app_type")))){ //变更、变更复议
//				kColl_agr.removeDataElement("app_type");  //清除申请类型
//				dao.update(kColl_agr, connection);
//			}
			
			//低风险
			if("10".equals(kColl_agr.getDataValue("lrisk_type"))){
				kColl_agr.addDataField("lrisk_totl_amt",kColl_agr.getDataValue("crd_totl_amt"));
				kColl_agr.addDataField("lrisk_cir_amt",kColl_agr.getDataValue("crd_cir_amt"));
				kColl_agr.addDataField("lrisk_one_amt",kColl_agr.getDataValue("crd_one_amt"));
				//若为低风险则移除掉非低风险金额，不做变更
				kColl_agr.remove("crd_totl_amt");
				kColl_agr.remove("crd_cir_amt");
				kColl_agr.remove("crd_one_amt");
			}
			
			//存在有效授信协议
			kColl_agr.removeDataElement("app_type");  //清除申请类型
			if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
				/** 条线变更时 将原有条线的担保合同与授信的关系解除、将原有台账的可用金额及授信金额置为0  需求编号：20131202001  2013-12-09  唐顺岩 */
				//String lmt_type = (String)kColl_agr.getDataValue("lmt_type");  //申请中授信类别
				
				//先解除原有条线下台账与担保的关系
				//KeyedCollection parameter = new KeyedCollection();
				//parameter.put("AGR_NO", kColl.getDataValue("agr_no"));
				
				//if("01".equals(lmt_type)){  //如果当前为公司条线，则解除原有小微条线担保 
				//	parameter.put("LMT_TYPE", "02");
				//}else{
				//	parameter.put("LMT_TYPE", "01");
				//}
				
				//SqlClient.executeUpd("unchainRLmtGuarContByAgrNo", parameter, null, null, connection);
				//将原有条线台账授信金额、启用金额置为0
				//SqlClient.executeUpd("updateLmtAgrDetailsByAgrNo", parameter, null, null, connection);
				/**  需求编号：20131202001  END */
				
				dao.update(kColl_agr, connection);
			}else{   //不存在有效授信协议
				dao.insert(kColl_agr, connection); 
			}
			/** END */
			
			/**  审批结束更新客户所属条线  START */
//			String condition4count = " where cus_id = '"+cus_id+"' and lrisk_type <> '"+kColl_agr.getDataValue("lrisk_type")+"' and lmt_status <> '30' ";
//			IndexedCollection iColl4count = dao.queryList("LmtAgrDetails", condition4count, connection);
//			double crd_totl_amt4c = 0;
//			if(iColl4count!=null&&iColl4count.size()>0){
//				for(int i=0;i<iColl4count.size();i++){
//					KeyedCollection kColl4c = (KeyedCollection) iColl4count.get(i);
//					crd_totl_amt4c += Double.valueOf(kColl4c.getDataValue("crd_amt").toString());
//				}
//			}
//			double crd_totl_amt = Double.valueOf(kColl_agr.getDataValue("crd_totl_amt").toString());
			//本次授信总额取上面汇总金额，不从申请表中取
//			double crd_totl_amt = crd_cir_amt_f + crd_one_amt_f;
			/**根据授信类别与授信金额更新 客户归属条线    */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
//			CusCom cuscom = service.getCusComByCusId(cus_id, context, connection);//对公客户信息，获取企业规模
//			ComponentHelper helper = new ComponentHelper();//获取工具类
//			KeyedCollection ComkColl = new KeyedCollection("CusCom");
//			ComkColl = helper.domain2kcol(cuscom, "CusCom");
//			String com_scale = "";
//			if(ComkColl.containsKey("com_scale")&&ComkColl.getDataValue("com_scale")!=null&&!"".equals(ComkColl.getDataValue("com_scale"))){
//				com_scale = ComkColl.getDataValue("com_scale").toString();
//			}
//			if(("30".equals(com_scale)||"31".equals(com_scale))&&(BigDecimal.valueOf(crd_totl_amt).add(BigDecimal.valueOf(crd_totl_amt4c)).compareTo(new BigDecimal(5000000))<=0)){
//				service.updateCusBelgLine(cus_id, "BL200", connection);  //更改客户所属条线为小微
//			}else{   //授信金额 大于 500W 变更客户条线为公司条线 
//				service.updateCusBelgLine(cus_id, "BL100", connection);  //更改客户所属条线为公司
//			}
			/**modified by lisj 2015-5-7 需求编号：【XD150407025】分支机构授信审批权限配置 begin**/
			/**根据授信类别更新 客户归属条线    */
			String lmt_type = (String)kColl_agr.getDataValue("lmt_type");
			if(lmt_type!=null&& "01".equals(lmt_type)){//
				service.updateCusBelgLine(cus_id, "BL200", connection);  //更改客户所属条线为小微
			}else{
				service.updateCusBelgLine(cus_id, "BL100", connection);  //更改客户所属条线为公司
			}
			/**modified by lisj 2015-5-7 需求编号：【XD150407025】分支机构授信审批权限配置 end**/
			/**  审批结束更新客户所属条线  END */
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
			throw new EMPException("单一法人授信流程审批报错，错误描述："+e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String app_type = (String)kc.getDataValue("app_type");
		String openDay = (String)this.getContext().getDataValue("OPENDAY");
		String cus_id = (String)kc.getDataValue("cus_id");
		String instanceId = "";//流程实例号
		//获取流程实例号作为规则参数
		String condition = "where pk_value='"+pkVal+"' and table_name='"+tabModelId+"'";
		IndexedCollection iCollJoin = dao.queryList("WfiJoin", condition, this.getConnection());
		if(iCollJoin.size()>0){
			KeyedCollection kCollJoin = (KeyedCollection)iCollJoin.get(0);
			instanceId = (String)kCollJoin.getDataValue("instanceid");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("cus_id", cus_id);
		param.put("isIndiv", "N");//需求编号：XD140715024 Edited by FCL 2014-07-22   
		/* added by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发 start */
		param.put("isIndivSH", "N");
		/* added by yangzy 2014/12/01 需求:XD140925064,生活贷需求开发 end */
		
		/**责任人存在多个机构时取责任机构*/
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		/**  审批机构直接取登录机构   2014-05-30    唐顺岩   */
		/*OrganizationServiceInterface userService;
		List<SOrg> orgslist = null;
		*/
		/** END */
//		ShuffleServiceInterface shuffleService;
		try {
			/**  审批机构直接取登录机构   2014-05-30    唐顺岩   */
			//userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			//orgslist = userService.getDeptOrgByActno(manager_id, this.getConnection());
			/** END */
//			shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		/**  审批机构直接取登录机构   2014-05-30    唐顺岩   */
		//if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
		//	manager_br_id = orgslist.get(0).getOrganno();
		//}
		/** END */
		
		//计算到期日(当前日期减去6个月)
		openDay = LmtUtils.computeEndDate(openDay, "002", "-6");
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("IN_SERNO", pkVal);
		modelMap.put("IN_CUSID", (String)kc.getDataValue("cus_id"));
		modelMap.put("IN_OPDATE", openDay);
		modelMap.put("IN_OPENDAY", openDay);
		modelMap.put("IN_INSTANCEID", instanceId);
		/**  审批机构直接取登录机构   2014-05-30    唐顺岩   */
		//modelMap.put("IN_MANAGERBRID", manager_br_id);
		modelMap.put("IN_MANAGERBRID", (String)this.getContext().getDataValue("organNo"));
		modelMap.put("IN_BIZ_BRID", (String)kc.getDataValue("manager_br_id"));    //增加业务管理机构参数   2016-06-26
		modelMap.put("IN_APPTYPE", app_type);
		/** END */
		/**modified by lisj 2015-4-1 需求编号：【XD150407025】分支机构授信审批权限配置 begin**/
		try {
			/**modified by lisj 2014-12-20 需求编号：【XD140826053】南安区域中心支行权限变更  begin**/			
			/**modified by lisj 2014-12-24需求编号:【XD141024071】晋江支行富信天伦天、美斯克特别授权开发需求  begin**/
//			Map<String, String>  outMap=shuffleService.fireTargetRule("SINGLECOMAUTH", "CHKISSPECIALLMT", modelMap);
//			String flowType = (String)outMap.get("OUT_FLOWTYPE");
			//Map<String, String>  outMap4Nanan=shuffleService.fireTargetRule("SINGLECOMAUTH", "CHECKNNREGBRANAUTH", modelMap);
			//Map<String, String>  outMap4JinJiang=shuffleService.fireTargetRule("SINGLECOMAUTH", "CHECKBONWARHOUAUTH", modelMap);
//			Map<String, String> outMap4LmtSCORule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTSCOPRATIONRULE", modelMap);//特别授权入口规则（无条件执行支行权限）
//			String flowType4SCORule = (String) outMap4LmtSCORule.get("OUT_FLOWTYPE");
//			Map<String, String> outMap4LmtSCRRule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTSPECIALCREDITRIGHT", modelMap);//授信特殊权限授权（数据需后台维护）
//			String flowType4SCRRule  = (String) outMap4LmtSCRRule.get("OUT_FLOWTYPE");
//			Map<String, String> outMap4LmtLCRGRule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTLCRGENERALRULE", modelMap);//授信权限配置总规则
			//String flowType4Nanan = (String)outMap4Nanan.get("OUT_FLOWTYPE");
			//String flowType4JinJiang = (String)outMap4JinJiang.get("OUT_FLOWTYPE");
//			String flowType4LmtLCRGRule= (String)outMap4LmtLCRGRule.get("OUT_FLOWTYPE");
//			if("ALLOW".equals(flowType4SCRRule)){
//				param.put("approve_org", "S0200");
//			}else if("SPE".equals(flowType)){
//				param.put("approve_org", "S0200");//判断是否符合行业授信特殊事项，若符合则直接赋值支行行长审批通过
//			}/**else if("ALLOW".equals(flowType4Nanan)){
//				param.put("approve_org", "S0200");//判断是否符合行业授信特殊事项，若符合则直接赋值支行行长审批通过
//			}else if("ALLOW".equals(flowType4JinJiang)){
//				param.put("approve_org", "S0200");//判断是否符合行业授信特殊事项，若符合则直接赋值支行行长审批通过
//			}**/else if("ALLOW".equals(flowType4SCORule)){
//				param.put("approve_org", "S0200");
//			}else if("ALLOW".equals(flowType4LmtLCRGRule)){
//				param.put("approve_org", "S0200");
//			}else if("ALLOWBRANCH".equals(flowType4LmtLCRGRule)){
//				param.put("approve_org", "S0226");//分行权限
//			}else{
//				/**注释流程审批授权规则代码，现在授信依据LMTLCRGENERALRULE授权配置总规则，故不启用**/
//				/**outMap.clear();
//				modelMap.put("IN_SERNO", pkVal);
//				modelMap.put("IN_CUSID", (String)kc.getDataValue("cus_id"));
//				modelMap.put("IN_INSTANCEID", instanceId);
//				modelMap.put("IN_APPTYPE", app_type);
//				modelMap.put("IN_MANAGERBRID",  (String)this.getContext().getDataValue("organNo"));
//				modelMap.put("IN_BIZ_BRID", (String)kc.getDataValue("manager_br_id"));    //增加业务管理机构参数   2016-06-26
//				outMap=shuffleService.fireTargetRule("LMTBPRIGHT", "HEADRULE", modelMap);
//				param.put("approve_org", (String)outMap.get("OUT_终审岗位"));//岗位
//				**/
//				param.put("approve_org", "");//总行权限				
//			}
			/**modified by lisj 2014-12-24需求编号:【XD141024071】晋江支行富信天伦天、美斯克特别授权开发需求  end**/
			/**modified by lisj 2014-12-20 需求编号：【XD140826053】南安区域中心支行权限变更  end**/
			param.put("approve_supor_org", "");//支行行长-->总行权限
			param.put("lrisk_type", (String)kc.getDataValue("lrisk_type"));
			KeyedCollection  SOrg = dao.queryAllDetail("SOrg", manager_br_id, this.getConnection());
			String suporganno = (String) SOrg.getDataValue("suporganno");//上级机构
			if(suporganno!=null && !"".equals(suporganno) && "9350500000".equals(suporganno)){
				param.put("approve_supor_org", "D0005");//支行行长-->总行权限
			}
			/**modified by lisj 2015-4-1 需求编号：【XD150407025】分支机构授信审批权限配置 end**/
//			System.out.println(outMap);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		
		return param;
	}

}
