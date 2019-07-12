package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtappindiv;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryLmtAppIndivDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAppIndiv";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			//modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 start
			//RecordRestrict recordRestrict = this.getRecordRestrict(context);
			//recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			//modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 end
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			
			//根据客户码调用客户模块接口获取客户基表信息
			String cus_id = (String)kColl.getDataValue("cus_id");
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase cusBase = service.getCusBaseByCusId(cus_id, context, connection);
			kColl.addDataField("cus_id_displayname", cusBase.getCusName());
			kColl.addDataField("cus_type", cusBase.getCusType());
			
			//冻结申请时汇总分项冻结金额之和,解冻时汇总分项解冻金额之和
			Map<String,String> modelMap=new HashMap<String,String>();
			modelMap.put("IN_申请流水号", serno_value);
			Map<String,String> outMap=new HashMap<String,String>();
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			String appType = kColl.getDataValue("app_type").toString();
			/**added by wangj 2015/08/28  需求编号:XD141222087,法人账户透支需求变更  begin**/
			if("03".equals(appType)){
				outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETFROZENAMTAPPDET", modelMap);
				BigDecimal frozeAmt = BigDecimalUtil.replaceNull(outMap.get("OUT_冻结金额").toString()) ;
				kColl.addDataField("frozen_amt", frozeAmt);
			}else if("04".equals(appType)){	//解冻时获取冻结金额和解冻金额
				outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETFROZENAMTAPPDET", modelMap);
				BigDecimal frozeAmt = BigDecimalUtil.replaceNull(outMap.get("OUT_冻结金额").toString()) ;
				kColl.addDataField("frozen_amt", frozeAmt);
				
				outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETUNFROZENAMTAPPDET", modelMap);
				BigDecimal unfrozenAmt = BigDecimalUtil.replaceNull(outMap.get("OUT_解冻金额").toString()) ;
				kColl.addDataField("unfroze_amt", unfrozenAmt);
			}
			/**added by wangj 2015/08/28  需求编号:XD141222087,法人账户透支需求变更  end**/
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id","input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id","input_id" });
			/**add by lisj 2015-5-7 需求编号：【XD150407025】分支机构授信审批权限配置(社区支行) begin**/
			if((kColl.getDataValue("is_comm_branch"))!=null && !"".equals(kColl.getDataValue("is_comm_branch")) 
					&& "1".equals(kColl.getDataValue("is_comm_branch"))){
				KeyedCollection temp = dao.queryFirst("WfiOrgCbRel", null, "where comm_branch_id='"+(String)kColl.getDataValue("comm_branch_id")+"'", connection);
				kColl.addDataField("comm_branch_name", (String)temp.getDataValue("comm_branch_name"));
			}
			/**add by lisj 2015-5-7 需求编号：【XD150407025】分支机构授信审批权限配置(社区支行) end**/
			String agr_no = (String)kColl.getDataValue("agr_no");
//			String crd_totl_amt = kColl.getDataValue("crd_totl_amt").toString();
			
			//授信申请不展示授信余额    2013-11-30  唐顺岩
//			if(agr_no!=null&&!agr_no.equals("")){
//				IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//				KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo(agr_no, "01", connection, context);
//				String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
//				double crd_bal_amt = Double.parseDouble(crd_totl_amt) - Double.parseDouble(lmt_amt);
//				kColl.addDataField("crd_bal_amt", crd_bal_amt);
//			}
			//汇总循环额度、一次性额度
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			KeyedCollection kColl_details = lmtComponent.selectLmtAppIndivAmt(kColl.getDataValue("serno").toString(),"LMT_APP_DETAILS");
			if(null!=kColl_details){
				kColl.addDataField("totl_amt", kColl_details.getDataValue("total_amt"));
				kColl.addDataField("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
				kColl.addDataField("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
			}
			
			//增加已冻结金额汇总，用于页面显示  start 2013-10-29 add by zhaozq
			if(agr_no!=null&&!agr_no.equals("") && ("03".equals(kColl.getDataValue("app_type")) || "04".equals(kColl.getDataValue("app_type")))){
				BigDecimal agr_froze_amt = new BigDecimal("0");
				IndexedCollection iColl = SqlClient.queryList4IColl("queryFrozeAmtByAgrNo", agr_no, connection);
				KeyedCollection kc = null;
				if(iColl.size()>0){
					kc = (KeyedCollection) iColl.get(0);
					agr_froze_amt = new BigDecimal(kc.getDataValue("agr_froze_amt")+"");
				}
				kColl.addDataField("agr_froze_amt", agr_froze_amt);
				
				//汇总循环额度、一次性额度
				kColl_details = lmtComponent.selectLmtAppIndivAmt(kColl.getDataValue("agr_no").toString(),"LMT_AGR_DETAILS");
				if(null!=kColl_details){
					kColl.put("totl_amt", kColl_details.getDataValue("total_amt"));
					kColl.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
					kColl.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
					
					kColl.put("lrisk_total_amt", kColl_details.getDataValue("lrisk_total_amt"));
					kColl.put("lrisk_cir_amt", kColl_details.getDataValue("lrisk_cir_amt"));
					kColl.put("lrisk_one_amt", kColl_details.getDataValue("lrisk_one_amt"));
					
					BigDecimal crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("total_amt")).add((BigDecimal)kColl_details.getDataValue("lrisk_total_amt"));
					crd_totl_amt.add(new BigDecimal((String)kColl.getDataValue("self_amt")));  //个人授信总金额=低风险+非低风险+自助额度    2013-12-12  唐顺岩
					kColl.put("crd_totl_amt",crd_totl_amt.toString());  //将低风险与非风险金额汇总
				}
			}
			//增加已冻结金额汇总，用于页面显示 end
			
			this.putDataElement2Context(kColl, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
