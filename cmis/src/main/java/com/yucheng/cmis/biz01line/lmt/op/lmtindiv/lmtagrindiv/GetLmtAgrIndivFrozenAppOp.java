package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtagrindiv;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetLmtAgrIndivFrozenAppOp extends CMISOperation {
	
	//operation TableModel
	private final String modelIdApp = "LmtAppIndiv";
	private final String modelIdAgr = "LmtAgrIndiv";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String agrNo = "";
		String serno="";
		String appType="";
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kCollAgr = null;
			try {
				agrNo = (String)context.getDataValue("agr_no");
				appType = (String)context.getDataValue("app_type");
			} catch (Exception e) {}
			if(agrNo == null || agrNo.length()==0||appType==null||appType.length()==0)
				throw new EMPJDBCException("The value [agr_no][app_type] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//新增前先进行查询操作，以防刷新重复提交
			String condition = " where agr_no='"+agrNo+"' and app_type='"+appType+"' and approve_status not in('997','998')";
			IndexedCollection iColl = dao.queryList(modelIdApp, condition, connection);
			if(iColl.size()>0){
				kCollAgr = (KeyedCollection)iColl.get(0);
			}else{
				kCollAgr = dao.queryDetail(modelIdAgr, agrNo, connection);
				String managerBrId = (String)kCollAgr.getDataValue("manager_br_id");
				//申请流水号
				serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", managerBrId, connection, context);
				
				kCollAgr.setDataValue("serno", serno);
				kCollAgr.addDataField("app_type", appType);
				kCollAgr.addDataField("flow_type", "01");
				kCollAgr.addDataField("app_date",context.getDataValue("OPENDAY"));
				kCollAgr.setDataValue("input_date",context.getDataValue("OPENDAY"));
				kCollAgr.addDataField("approve_status","000");
				kCollAgr.setDataValue("memo", "");
				//added by yangzy 2015/04/29 需求：XD150325024，集中作业扫描岗权限改造 start
				kCollAgr.put("input_br_id",context.getDataValue("organNo"));
				kCollAgr.put("input_id",context.getDataValue("currentUserId"));
				//added by yangzy 2015/04/29 需求：XD150325024，集中作业扫描岗权限改造 end
				kCollAgr.setName(modelIdApp);
//				dao.insert(kCollAgr, connection);
//				double frozenAmt = 0.0;
//				LmtPubComponent lmtAgr = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
//				List<CMISDomain> lmtAgrDets = lmtAgr.queryLmtAgrDetailsListByLmtSerno(agrNo);
//				for(int i=0;i<lmtAgrDets.size();i++){
//					String lmtCode = CMISSequenceService4JXXD.querySequenceFromED("ED", "all", connection, context);
//					LmtAgrDetails lmtAgrDet = (LmtAgrDetails)lmtAgrDets.get(i);
//					KeyedCollection kColl4AppDet = new KeyedCollection("LmtAppDetails");
//					kColl4AppDet.addDataField("serno", serno);
//					kColl4AppDet.addDataField("crd_amt", lmtAgrDet.getCrdAmt());
//					kColl4AppDet.addDataField("prd_id", lmtAgrDet.getPrdId());
//					kColl4AppDet.addDataField("limit_code", lmtCode);
//					kColl4AppDet.addDataField("limit_name", lmtAgrDet.getLimitName());
//					kColl4AppDet.addDataField("limit_type", lmtAgrDet.getLimitType());
//					kColl4AppDet.addDataField("sub_type", lmtAgrDet.getSubType());
//					kColl4AppDet.addDataField("cur_type", "CNY");
//					kColl4AppDet.addDataField("org_limit_code", lmtAgrDet.getLimitCode());
//					kColl4AppDet.addDataField("froze_amt", lmtAgrDet.getFrozeAmt());
//					kColl4AppDet.addDataField("guar_type", lmtAgrDet.getGuarType());
//					kColl4AppDet.addDataField("term", lmtAgrDet.getTerm());
//					kColl4AppDet.addDataField("term_type", lmtAgrDet.getTermType());
//					kColl4AppDet.addDataField("start_date", lmtAgrDet.getStartDate());
//					kColl4AppDet.addDataField("end_date", lmtAgrDet.getEndDate());
//					dao.insert(kColl4AppDet, connection);
//					//汇总冻结金额
//					String fAmt = lmtAgrDet.getFrozeAmt();
//					if(fAmt!=null&&!"".equals(fAmt)){
//						frozenAmt = frozenAmt + Double.parseDouble(fAmt);
//					}
//				}
//				//设置冻结金额
//				kCollAgr.addDataField("frozen_amt", frozenAmt);
			}
			//授信余额
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			String crd_totl_amt = kCollAgr.getDataValue("crd_totl_amt").toString();
//			if(agrNo!=null&&!agrNo.equals("")){
//				IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//				KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo(agrNo, "01", connection, context);
//				String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
//				double crd_bal_amt = Double.parseDouble(crd_totl_amt) - Double.parseDouble(lmt_amt);
//				kCollAgr.addDataField("crd_bal_amt", crd_bal_amt);
//			}
			//汇总循环额度、一次性额度
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			KeyedCollection kColl_details = lmtComponent.selectLmtAppIndivAmt(kCollAgr.getDataValue("agr_no").toString(),"LMT_AGR_DETAILS");
			if(null!=kColl_details){
				kCollAgr.addDataField("totl_amt", kColl_details.getDataValue("total_amt"));
				kCollAgr.addDataField("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
				kCollAgr.addDataField("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
				
				kCollAgr.put("totl_amt", kColl_details.getDataValue("total_amt"));
				kCollAgr.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
				kCollAgr.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
				
				kCollAgr.put("lrisk_total_amt", kColl_details.getDataValue("lrisk_total_amt"));
				kCollAgr.put("lrisk_cir_amt", kColl_details.getDataValue("lrisk_cir_amt"));
				kCollAgr.put("lrisk_one_amt", kColl_details.getDataValue("lrisk_one_amt"));
				
				BigDecimal crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("total_amt")).add((BigDecimal)kColl_details.getDataValue("lrisk_total_amt"));
				crd_totl_amt.add(new BigDecimal((String)kCollAgr.getDataValue("self_amt")));  //个人授信总金额=低风险+非低风险+自助额度    2013-12-12  唐顺岩
				kCollAgr.put("crd_totl_amt",crd_totl_amt.toString());  //将低风险与非风险金额汇总
			}
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kCollAgr, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(kCollAgr, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(kCollAgr, new String[] { "input_id","manager_id" });
			this.putDataElement2Context(kCollAgr, context);
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
