package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtApplyDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtApply";
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
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" });
			/** END */
			
			/** 校验为冻结解冻时才处理冻结解冻业务    2013-10-30 唐顺岩   */
			String appType = kColl.getDataValue("app_type").toString();
			if("03".equals(appType) || "04".equals(appType)){
				//冻结申请时汇总分项冻结金额之和,解冻时汇总分项解冻金额之和
				Map<String,String> modelMap=new HashMap<String,String>();
				modelMap.put("IN_申请流水号", serno_value);
				Map<String,String> outMap=new HashMap<String,String>();
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ShuffleServiceInterface shuffleService = null;
				shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
				/**added by wangj 2015/08/28  需求编号:XD141222087,法人账户透支需求变更  begin**/
				if("03".equals(appType)){
					outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETFROZENAMTAPPDET", modelMap);
					BigDecimal frozeAmt = BigDecimalUtil.replaceNull(outMap.get("OUT_冻结金额").toString()) ;
					kColl.addDataField("frozen_amt", frozeAmt);
				}else if("04".equals(appType)){
					outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETUNFROZENAMTAPPDET", modelMap);
					BigDecimal unfrozenAmt = BigDecimalUtil.replaceNull(outMap.get("OUT_解冻金额").toString()) ;
					kColl.addDataField("unfroze_amt", unfrozenAmt);
				}
				/**added by wangj 2015/08/28  需求编号:XD141222087,法人账户透支需求变更  end**/
				//增加已冻结金额汇总，用于页面显示  start 2013-10-29 add by zhaozq
				String agr_no = (String) kColl.getDataValue("agr_no");
				if(agr_no!=null&&!agr_no.equals("")){
					BigDecimal agr_froze_amt = new BigDecimal("0");
					IndexedCollection iColl = SqlClient.queryList4IColl("queryFrozeAmtByAgrNo", agr_no, connection);
					KeyedCollection kc = null;
					if(iColl.size()>0){
						kc = (KeyedCollection) iColl.get(0);
						agr_froze_amt = new BigDecimal(kc.getDataValue("agr_froze_amt")+"");
					}
					kColl.addDataField("agr_froze_amt", agr_froze_amt);
				}
				//增加已冻结金额汇总，用于页面显示 end
				
				//汇总循环额度、一次性额度
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				KeyedCollection kColl_details = lmtComponent.selectLmtAgrAmtByAgr(kColl.getDataValue("agr_no").toString());
				if(null!=kColl_details){
					kColl.put("totl_amt", kColl_details.getDataValue("total_amt"));
					kColl.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
					kColl.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
					
					kColl.put("lrisk_total_amt", kColl_details.getDataValue("lrisk_total_amt"));
					kColl.put("lrisk_cir_amt", kColl_details.getDataValue("lrisk_cir_amt"));
					kColl.put("lrisk_one_amt", kColl_details.getDataValue("lrisk_one_amt"));
					BigDecimal crd_totl_amt = new BigDecimal("0.0");
					if(((String)context.getDataValue("menuId")).indexOf("grp")<0){   //单一法人授信需统计低风险+非低风险
						crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("total_amt")).add((BigDecimal)kColl_details.getDataValue("lrisk_total_amt"));
						context.addDataField("origin", "SINGLE");   //来源为单一法人  
					}else{    //集团授信时只需统计非低风险额度
						crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("total_amt"));
						context.addDataField("origin", "GRP");   //来源为集团
					}
					
					kColl.put("crd_totl_amt",crd_totl_amt.toString());  //将低风险与非风险金额汇总
				}
				
			}
			
			//如果是集团授信新增、变更、复议、变更复议 时首次进入时从台账中获取授信总额    2012-12-18  唐顺岩
//			if("01".equals(appType) || "02".equals(appType) || "05".equals(appType) || "06".equals(appType)){
//				if(context.containsKey("type") && "surp".equalsIgnoreCase((String)(context.getDataValue("type")))){  //集团授信设置额度
//					
//					/**从台账中实时汇总循环额度、一次性额度    2012-12-18   唐顺岩   */
//					LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
//					if(kColl.getDataValue("agr_no")!=null&&!"".equals(kColl.getDataValue("agr_no"))){
//						KeyedCollection kColl_details = lmtComponent.selectLmtAgrDetailsAmt(kColl.getDataValue("agr_no").toString(),(String)context.getDataValue("lmt_type"));
//						if(null != kColl_details){
//								//原有
//								kColl.put("org_crd_totl_amt", kColl_details.getDataValue("total_amt"));
//								kColl.put("org_crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
//								kColl.put("org_crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
//								//现有
//								kColl.put("crd_totl_amt", kColl_details.getDataValue("total_amt"));
//								kColl.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
//								kColl.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
//								
//								kColl.put("lmt_type", context.getDataValue("lmt_type"));
//								dao.update(kColl, connection);
//						}
//					}else{
//						kColl.put("lmt_type", context.getDataValue("lmt_type"));
//						dao.update(kColl, connection);
//					}
//					/** END */
//				}
//			}
			
			context.addDataField("operate", "updateLmtApplyRecord.do");
			
			//判断是否是事业法人，为后续tab页传参
			String cus_id = (String) kColl.getDataValue("cus_id");
			KeyedCollection cusKColl = dao.queryDetail("CusBase", cus_id, connection);
			String cus_type = (String) cusKColl.getDataValue("cus_type");
			if(cus_type!=null&&cus_type.startsWith("D")){
				context.addDataField("cus_flag", "1");
			}else{
				context.addDataField("cus_flag", "2");
			}
			
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
