
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
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class LmtGrpApplyFlowImpl extends CMISComponent implements BIZProcessInterface {

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
			String table_name = wfiMsg.getTableName();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollAppAgr = dao.queryDetail(table_name, serno_value, connection);
			
			kCollAppAgr.put("over_date", context.getDataValue("OPENDAY"));
			//修改集团申请表的办结日期、申请状态
			dao.update(kCollAppAgr, connection);
			
			String grp_agr_no = "";
			if(!"".equals(kCollAppAgr.getDataValue("app_type")) && ("01".equals(kCollAppAgr.getDataValue("app_type"))||"05".equals(kCollAppAgr.getDataValue("app_type")))){  //新增、复议
				//生成授信协议编号
				grp_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
				kCollAppAgr.setDataValue("grp_agr_no", grp_agr_no);
				kCollAppAgr.addDataField("start_date", openDate);
				kCollAppAgr.addDataField("agr_status", "002");
			}else if(!"".equals(kCollAppAgr.getDataValue("app_type")) && ("02".equals(kCollAppAgr.getDataValue("app_type"))||"06".equals(kCollAppAgr.getDataValue("app_type")))){   //变更、变更复议
				grp_agr_no = kCollAppAgr.getDataValue("grp_agr_no").toString();
			}
			
			kCollAppAgr.removeDataElement("org_crd_totl_amt");   //清除变更前授信总额
			kCollAppAgr.removeDataElement("org_crd_cir_amt");    //清除变更前循环授信总额
			kCollAppAgr.removeDataElement("org_crd_one_amt");    //清除变更前一次性授信总额
			kCollAppAgr.removeDataElement("approve_status");  //清除审批状态
			kCollAppAgr.removeDataElement("over_date");  //清除到期日期
			
			kCollAppAgr.setName("LmtAgrGrp");
			
			String condition = " WHERE GRP_SERNO='"+serno_value+"'";
			IndexedCollection iCollApp = dao.queryList("LmtApply", condition, connection);
			for(int i=0;i<iCollApp.size();i++){
				KeyedCollection kCollApp = (KeyedCollection) iCollApp.get(i);
				String sernoApp = kCollApp.getDataValue("serno").toString();
				String cus_id = kCollApp.getDataValue("cus_id").toString();
				kCollApp.setDataValue("approve_status", "997");
				kCollApp.setDataValue("over_date", openDate);
				//修改单一授信申请表的办结日期、申请状态
				dao.update(kCollApp, connection);
				
				/**  查询授信客户下是否存在有效的授信协议    低风险与非低风险业务共用协议表，通过查询是否存在有效授信协议来判断走新增还是修改		2014-01-04   唐顺岩  */
				List<String> list = new ArrayList<String>();
				list.add("agr_no");
				list.add("end_date");
				String condition_apply = " WHERE CUS_ID='"+ cus_id +"' AND AGR_STATUS='002' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ";
				KeyedCollection kColl = new KeyedCollection(); 
				kColl = dao.queryFirst("LmtAgrInfo", list, condition_apply, connection);
				
				String lmt_agr_no = "";
				//存在有效授信协议
				if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
					lmt_agr_no = kColl.getDataValue("agr_no").toString();
				}else{  //不存在授信协议
					//生成授信协议编号
					lmt_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
					kCollApp.addDataField("start_date", openDate);
				}
				kCollApp.put("agr_no", lmt_agr_no);   //设置协议号
				
//				if(!"".equals(kCollApp.getDataValue("app_type")) && ("01".equals(kCollApp.getDataValue("app_type"))||"05".equals(kCollApp.getDataValue("app_type")))){  //新增
//					//生成授信协议编号
//					app_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
//					kCollApp.setDataValue("agr_no", app_agr_no);
//					kCollApp.addDataField("start_date", openDate);
//				}else if(!"".equals(kCollApp.getDataValue("app_type")) && ("02".equals(kCollApp.getDataValue("app_type"))||"06".equals(kCollApp.getDataValue("app_type")))){   //变更
//					app_agr_no = kCollApp.getDataValue("agr_no").toString();
//				}
				/**END */
				
				kCollApp.addDataField("grp_agr_no", grp_agr_no);  //设置集团协议编号
				
				kCollApp.removeDataElement("org_crd_totl_amt");   //清除变更前授信总额
				kCollApp.removeDataElement("org_crd_cir_amt");    //清除变更前循环授信总额
				kCollApp.removeDataElement("org_crd_one_amt");    //清除变更前一次性授信总额
				kCollApp.removeDataElement("approve_status");  //清除审批状态
				kCollApp.removeDataElement("over_date");  //清除到期日期
				
				kCollApp.setName("LmtAgrInfo");
				String conditionDet = " where serno = '"+sernoApp+"'";
				IndexedCollection iColl = dao.queryList("LmtAppDetails", conditionDet, connection);
				String endDate = "";
				String startDate = "";
				IndexedCollection iCollbak = dao.queryList("LmtAppDetails", " where serno = '"+sernoApp+"' order by start_date asc ", connection);
				if(iCollbak!=null&&iCollbak.size()>0){
					KeyedCollection kColl_detailsbak = (KeyedCollection) iCollbak.get(0);
					startDate = (String)kColl_detailsbak.getDataValue("start_date");
				}
				for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
					KeyedCollection kColl_details = (KeyedCollection) iterator.next();
					kColl_details.addDataField("lmt_status", "10");  //额度状态默认为[正常]
					
					/** 授信启用金额不再根据担保  有效担保合同金额来设置  2014-01-08 注释
					 * 启用金额，授信担保方式不为信用时
					 * 当授信项下所关联的担保合同总额小于或等于授信金额时，启用金额等于担保合同总额；
					 * 当授信项下所关联的担保合同总额大于授信金额时，启用金额等于授信金额
					 * */
//					if(!"400".equals(kColl_details.getDataValue("guar_type"))){
//						BigDecimal eable_amt = LmtUtils.computLmtEnableAmt(serno_value, kColl_details, context, connection, dao);
//						kColl_details.addDataField("enable_amt", eable_amt); 
//					}else{   //担保方式为信用时直接取授信金额为启用金额
//						kColl_details.addDataField("enable_amt", kColl_details.getDataValue("crd_amt"));  //启用金额默认等于授信金额
//					}
					
					/** 启用金额直接改为取授信金额   2014-01-08  唐顺岩    */ 
					kColl_details.addDataField("enable_amt", kColl_details.getDataValue("crd_amt"));   //启用金额默认等于授信金额
					/** 启用金额设值完成  */
					
					//申请分项表中已经存入客户码，此处只判断是否为空
					if("".equals(kColl_details.getDataValue("cus_id"))){   //申请表中客户码为空的情况
						kColl_details.setDataValue("cus_id", cus_id);
					}
					
					kColl_details.setName("LmtAgrDetails");
					kColl_details.addDataField("agr_no", lmt_agr_no);  //授信协议编号
					
					String update_flag = kColl_details.getDataValue("update_flag").toString();
					kColl_details.removeDataElement("update_flag");   //清除修改类型

					/** 供应链授信时将授信类别置为 供应链授信    需求编号：20131202001  2013-12-09  唐顺岩 */
					if("05".equals(kColl_details.getDataValue("sub_type"))){
						//kColl_details.put("lmt_type", "03");
					}
					/** 需求编号：20131202001  END */
					
					if("01".equals(update_flag)){  //新增时
						endDate = LmtUtils.computeEndDate(openDate, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
						kColl_details.setDataValue("start_date", openDate);
						kColl_details.setDataValue("end_date", endDate);
						
						EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------集团授信   新增授信时将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 开始---------------", null);
						//将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系
						KeyedCollection kColl4Value = new KeyedCollection();
						kColl4Value.addDataField("LIMIT_CODE", (String)kColl_details.getDataValue("org_limit_code"));
						kColl4Value.addDataField("AGR_NO", lmt_agr_no);
						
						KeyedCollection kColl4Parameter = new KeyedCollection();
						kColl4Parameter.addDataField("LIMIT_CODE", (String)kColl_details.getDataValue("org_limit_code"));
						kColl4Parameter.addDataField("SERNO", sernoApp);
						
						SqlClient.executeUpd("createRLmtGuarContRecord", kColl4Parameter, kColl4Value, null,this.getConnection());
						EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------集团授信   新增授信时将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 结束---------------", null);
						
						//插入台账表
						kColl_details.removeDataElement("org_limit_code");  //清除台账授信额度编号
						dao.insert(kColl_details, connection);
					}else{   //变更
						if("1".equals(kColl_details.getDataValue("is_adj_term"))){  //并且调整期限
							//String star_date = kColl_details.getDataValue("start_date").toString();
							//endDate = LmtUtils.computeEndDate(star_date, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
							endDate = LmtUtils.computeEndDate(openDate, kColl_details.getDataValue("term_type").toString(), kColl_details.getDataValue("term").toString());
							kColl_details.put("end_date", endDate);
							kColl_details.put("start_date", startDate);
						}
						
						String org_limit_code =  (String)kColl_details.getDataValue("org_limit_code");

						EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------集团授信   变更授信时删除原分项原额度台账与担保合同关系数据，将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系     开始---------------", null);
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
						kColl4Parameter.addDataField("SERNO", sernoApp);
						
						SqlClient.executeUpd("createRLmtGuarContRecord", kColl4Parameter, kColl4Value, null,this.getConnection());
						EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------集团授信   变更授信时删除原分项原额度台账与担保合同关系数据，将申请分项与担保合同关系表数据过渡成授信台账与担保合同关系 结束---------------", null);
						
						kColl_details.setDataValue("limit_code", kColl_details.getDataValue("org_limit_code"));  //将原授信额度编号赋给授信额度编号，用于做主键更新
						kColl_details.removeDataElement("org_limit_code");  //清除台账授信额度编号
						dao.update(kColl_details, connection);
					}
				}
				//将授信台账数据根据到期日期排序
				if(iColl.size()>0){
					/** 判断该协议下是否存在低风险授信台账，取其最大到期日   2014-01-21  唐顺岩  */
					if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
						String agr_details_sql = " WHERE AGR_NO='"+kColl.getDataValue("agr_no")+"' AND LRISK_TYPE = '10' ";
						agr_details_sql += " AND SUB_TYPE IN('01','05') AND LMT_STATUS<>'30' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ORDER BY END_DATE DESC ";
						KeyedCollection kcoll_agr_details = dao.queryFirst("LmtAgrDetails", null, agr_details_sql, connection);
						if(null!=kcoll_agr_details && null!=kcoll_agr_details.getDataValue("limit_code")){  
							iColl.add(kcoll_agr_details);
						}
						//iColl.add(kColl);
					}
					/** END */
					iColl = LmtUtils.sort(iColl);
					KeyedCollection kColl_last = (KeyedCollection)iColl.get(0);  //取排序后的最后一条记录
					String arg_end_date = kColl_last.getDataValue("end_date").toString();  //获得分项中最大的到期日期
					
					kCollApp.addDataField("end_date", arg_end_date);  //设置到期日期
				}else{   //如果单个成员未设置授信，则将当期日期设置为到期日期
					kCollApp.addDataField("end_date", openDate);  //设置到期日期
				}
				
				kCollApp.addDataField("agr_status", "002");  //设置协议状态为 生效
//				
//				if(!"".equals(kCollApp.getDataValue("app_type")) && "01".equals(kCollApp.getDataValue("app_type"))){  //新增
//					kCollApp.removeDataElement("app_type");  //清除申请类型
//					dao.insert(kCollApp, connection); 
//				}else if(!"".equals(kCollApp.getDataValue("app_type")) && "02".equals(kCollApp.getDataValue("app_type"))){   //变更
//					kCollApp.removeDataElement("app_type");  //清除申请类型
//					dao.update(kCollApp, connection);
//				}
				//存在有效授信协议
				kCollApp.removeDataElement("app_type");  //清除申请类型
				if(kColl.containsKey("agr_no") && null!=kColl.getDataValue("agr_no") && !"".equals(kColl.getDataValue("agr_no"))){
					/** 条线变更时 将原有条线的担保合同与授信的关系解除、将原有台账的可用金额及授信金额置为0  需求编号：20131202001  2013-12-09  唐顺岩 */
					//String lmt_type = (String)kCollApp.getDataValue("lmt_type");
					//if("01".equals(lmt_type)){  //如果当前为公司条线，则解除原有小微条线担保
					//	lmt_type = "02";
					//}else{
					//	lmt_type = "01";
					//}
					//先解除原有条线下台账与担保的关系
					//KeyedCollection parameter = new KeyedCollection();
					//parameter.put("AGR_NO", kColl.getDataValue("agr_no"));
					//parameter.put("LMT_TYPE", lmt_type);
					//SqlClient.executeUpd("unchainRLmtGuarContByAgrNo", parameter, null, null, connection);
					//将原有条线台账授信金额、启用金额置为0
					//SqlClient.executeUpd("updateLmtAgrDetailsByAgrNo", parameter, null, null, connection);
					/**  需求编号：20131202001  END */
					
					dao.update(kCollApp, connection);
				}else{   //不存在有效授信协议
					dao.insert(kCollApp, connection);
				}
				/** END */
				

				/** 条线变更时 将原有条线的担保合同与授信的关系解除、将原有台账的可用金额及授信金额置为0  需求编号：20131202001  2013-12-09  唐顺岩 */
				String lmt_type = (String)kCollApp.getDataValue("lmt_type");   //申请中授信类别
				
				//先解除原有条线下台账与担保的关系
				//KeyedCollection parameter = new KeyedCollection();
				//parameter.put("AGR_NO", kCollApp.getDataValue("agr_no"));
				//if("01".equals(lmt_type)){  //如果当前为公司条线，则解除原有小微条线担保
				//	parameter.put("LMT_TYPE", "02");
				//}else{
				//	parameter.put("LMT_TYPE", "01");
				//}
				
				//SqlClient.executeUpd("unchainRLmtGuarContByAgrNo", parameter, null, null, connection);
				//将原有条线台账授信金额、启用金额置为0
				//SqlClient.executeUpd("updateLmtAgrDetailsByAgrNo", parameter, null, null, connection);
				/**  需求编号：20131202001  END */
				
				/**  审批结束更新客户所属条线  START */
//				String condition4count = " where cus_id = '"+cus_id+"' and lrisk_type <> '"+kCollApp.getDataValue("lrisk_type")+"' and lmt_status <> '30' ";
//				IndexedCollection iColl4count = dao.queryList("LmtAgrDetails", condition4count, connection);
//				double crd_totl_amt4c = 0;
//				if(iColl4count!=null&&iColl4count.size()>0){
//					for(int j=0;j<iColl4count.size();j++){
//						KeyedCollection kColl4c = (KeyedCollection) iColl4count.get(j);
//						crd_totl_amt4c += Double.valueOf(kColl4c.getDataValue("crd_amt").toString());
//					}
//				}
//				
//				double crd_totl_amt = Double.valueOf(kCollApp.getDataValue("crd_totl_amt").toString());
				
				/**根据授信类别与授信金额更新 客户归属条线    */
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				
				/**根据授信类别更新 客户归属条线    */
				if(lmt_type!=null&&("05".equals(lmt_type)||"06".equals(lmt_type))){//
					service.updateCusBelgLine(cus_id, "BL200", connection);  //更改客户所属条线为小微
				}else{
					service.updateCusBelgLine(cus_id, "BL100", connection);  //更改客户所属条线为公司
				}
//				CusCom cuscom = service.getCusComByCusId(cus_id, context, connection);//对公客户信息，获取企业规模
//				ComponentHelper helper = new ComponentHelper();//获取工具类
//				KeyedCollection ComkColl = new KeyedCollection("CusCom");
//				ComkColl = helper.domain2kcol(cuscom, "CusCom");
//				String com_scale = "";
//				if(ComkColl.containsKey("com_scale")&&ComkColl.getDataValue("com_scale")!=null&&!"".equals(ComkColl.getDataValue("com_scale"))){
//					com_scale = ComkColl.getDataValue("com_scale").toString();
//				}
//				
//				if(("30".equals(com_scale)||"31".equals(com_scale))&&(BigDecimal.valueOf(crd_totl_amt).add(BigDecimal.valueOf(crd_totl_amt4c)).compareTo(new BigDecimal(5000000))<=0)){
//					service.updateCusBelgLine(cus_id, "BL200", connection);  //更改客户所属条线为小微
//				}else{   //授信金额 大于 500W 变更客户条线为公司条线 
//					service.updateCusBelgLine(cus_id, "BL100", connection);  //更改客户所属条线为公司
//				}
				
//				if(null!=lmt_type && "02".equals(lmt_type) ){    //授信类别为 对公条线 时直接将客户条线直接变更为公司
//					service.updateCusBelgLine(cus_id, "BL100", connection);  //更改客户所属条线为公司
//				}else if(null!=lmt_type && "01".equals(lmt_type)){    //授信类别为 小微条线
//					//授信金额 小于等于500W  变更客户条线为小微条线
//					if(BigDecimal.valueOf(crd_totl_amt).add(BigDecimal.valueOf(crd_totl_amt4c)).compareTo(new BigDecimal(5000000))<=0){
//						service.updateCusBelgLine(cus_id, "BL200", connection);  //更改客户所属条线为小微
//					}else{   //授信金额 大于 500W 变更客户条线为公司条线 
//						service.updateCusBelgLine(cus_id, "BL100", connection);  //更改客户所属条线为公司
//					}
//				}
				/**  审批结束更新客户所属条线  END */
				/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//				ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//				KeyedCollection retKColl = new KeyedCollection();
//				KeyedCollection kColl4trade = new KeyedCollection();
//				kColl4trade.put("CLIENT_NO", cus_id);
//				kColl4trade.put("BUSS_SEQ_NO", "");
//				kColl4trade.put("TASK_ID", "");
//				try{
//					retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, context, connection);	//调用影像锁定接口
//				}catch(Exception e){
//					throw new Exception("影像锁定接口失败!");
//				}
//				if(!TagUtil.haveSuccess(retKColl, context)){
//					//交易失败信息
//					throw new Exception("影像锁定接口失败!");
//				}
				/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
			}			
			//将授信协议数据根据到期日期排序
			if(iCollApp.size()>0){
				iCollApp = LmtUtils.sort(iCollApp);
				KeyedCollection kColl_last = (KeyedCollection)iCollApp.get(0);  //取排序后的最后一条记录
				String arg_end_date = kColl_last.getDataValue("end_date").toString();  //获得分项中最大的到期日期
				
				kCollAppAgr.addDataField("end_date", arg_end_date);  //设置到期日期
			}
			
			if(!"".equals(kCollAppAgr.getDataValue("app_type")) && ("01".equals(kCollAppAgr.getDataValue("app_type"))||"05".equals(kCollAppAgr.getDataValue("app_type")))){  //新增、复议
				kCollAppAgr.removeDataElement("app_type");  //清除申请类型
				dao.insert(kCollAppAgr, connection); 
			}else if(!"".equals(kCollAppAgr.getDataValue("app_type")) && ("02".equals(kCollAppAgr.getDataValue("app_type"))||"06".equals(kCollAppAgr.getDataValue("app_type")))){   //变更、变更复议
				kCollAppAgr.removeDataElement("app_type");  //清除申请类型
				dao.update(kCollAppAgr, connection);
			}
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new EMPException("集团客户授信流程审批报错，错误描述："+e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		Connection connection = this.getConnection();
		String serno = wfiMsg.getPkValue();
		try {
			/**集团授信否决时将集团下的成员申请也置为否决状态**/
			SqlClient.executeUpd("updateLmtGrpApproveStatus", serno, "998", null, connection);
		} catch (SQLException e) {
			throw new EMPException("集团客户授信流程否决报错，错误描述："+e.getMessage());
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
	//	String cus_id = (String)kc.getDataValue("cus_id");
		//added by yangzy 2015/04/23 集团流程改造，当成员发起流程时，流程需要经过集团主办行行长审批 start
		String is_main_org = "Y";
		//IndexedCollection iColl = new IndexedCollection();
		//try {
		//	iColl = SqlClient.queryList4IColl("queryLmtAppGrpManagerInfoBySerno", pkVal, this.getConnection());
		//} catch (SQLException e) {
		//	throw new EMPException("集团客户授信流程异常无法获取集团及其成员【主管机构、主管客户经理】！");
		//}
		//if(iColl!=null&&iColl.size()>0){
		//	KeyedCollection kColl = (KeyedCollection) iColl.get(0);
		//	if(kColl!=null&&kColl.size()>0){
		//		manager_br_id = (String) kColl.getDataValue("manager_br_id");
		//		manager_id = (String) kColl.getDataValue("manager_id");
		//		is_main_org = "N";
		//	}
		//}
		//added by yangzy 2015/04/23 集团流程改造，当成员发起流程时，流程需要经过集团主办行行长审批 end
	    //2014-12-16   无需求 Edited by FCL 新增变量suporganno来验证当前是否需经过分行行长节点
		kc=null;
		kc = dao.queryDetail("SOrg", manager_br_id, this.getConnection());
		String suporganno = (String) kc.getDataValue("suporganno");
		//-------------------2014-12-16 END------------------------------------------
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("suporganno", suporganno);
		//added by yangzy 2015/04/23 集团流程改造，当成员发起流程时，流程需要经过集团主办行行长审批 start
		param.put("is_main_org", is_main_org);
		//added by yangzy 2015/04/23 集团流程改造，当成员发起流程时，流程需要经过集团主办行行长审批 end
		//param.put("cus_id", cus_id);
		return param;
	}

}
