package com.yucheng.cmis.biz01line.lmt.op.lmtagrinfo;


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
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetLmtFrozenApplyAddPageOp extends CMISOperation {
	
	private final String modelId = "LmtAgrInfo";
	private final String modelId4App = "LmtApply";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = "";
			String agrNo = "";
			
			try {
				agrNo = (String)context.getDataValue("agr_no");//协议编号
			} catch (Exception e) {}
			if(agrNo == null || agrNo.length() == 0)
				throw new EMPJDBCException("The value of pk[agr_no] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, agrNo, connection);
			
			serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", kColl.getDataValue("manager_br_id").toString(), connection, context);
			
			KeyedCollection kColl4App = new KeyedCollection(modelId4App);
			kColl4App.addDataField("serno", serno);
			kColl4App.addDataField("agr_no", agrNo);
			kColl4App.addDataField("cus_id", kColl.getDataValue("cus_id"));
			kColl4App.addDataField("crd_totl_amt", kColl.getDataValue("crd_totl_amt"));
			kColl4App.addDataField("crd_cir_amt", kColl.getDataValue("crd_cir_amt"));
			kColl4App.addDataField("crd_one_amt", kColl.getDataValue("crd_one_amt"));
			kColl4App.addDataField("biz_type", kColl.getDataValue("biz_type"));
			
			kColl4App.addDataField("input_br_id",context.getDataValue("organNo"));
			kColl4App.addDataField("input_id",context.getDataValue("currentUserId"));
			//冻结金额用规则计算
			Map modelMap=new HashMap();
			modelMap.put("IN_协议号", agrNo);
			Map outMap=new HashMap();
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETFROZENAMT", modelMap);
			/**added by wangj 2015/09/02  需求编号:XD141222087,法人账户透支需求变更  begin**/
			BigDecimal frozenAmt = BigDecimalUtil.replaceNull(outMap.get("OUT_冻结金额").toString()) ;//冻结金额
			/**added by wangj 2015/09/02  需求编号:XD141222087,法人账户透支需求变更  end**/
			kColl4App.addDataField("frozen_amt", frozenAmt);
			
			//汇总循环额度、一次性额度
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			KeyedCollection kColl_details = lmtComponent.selectLmtAgrAmtByAgr(agrNo);
			if(null!=kColl_details){
				kColl4App.put("totl_amt", kColl_details.getDataValue("total_amt"));
				kColl4App.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
				kColl4App.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
				
				kColl4App.put("lrisk_total_amt", kColl_details.getDataValue("lrisk_total_amt"));
				kColl4App.put("lrisk_cir_amt", kColl_details.getDataValue("lrisk_cir_amt"));
				kColl4App.put("lrisk_one_amt", kColl_details.getDataValue("lrisk_one_amt"));
				BigDecimal crd_totl_amt = new BigDecimal("0.0");
				if(((String)context.getDataValue("menuId")).indexOf("grp")<0){   //单一法人授信需统计低风险+非低风险
					crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("total_amt")).add((BigDecimal)kColl_details.getDataValue("lrisk_total_amt"));
					context.addDataField("origin", "SINGLE");   //来源为单一法人  
				}else{    //集团授信时只需统计非低风险额度
					crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("total_amt"));
					context.addDataField("origin", "GRP");   //来源为集团
				}
				kColl4App.put("crd_totl_amt",crd_totl_amt.toString());  //将低风险与非风险金额汇总
			}
			
			//增加已冻结金额汇总，用于页面显示  start 2013-10-29 add by zhaozq
			BigDecimal agr_froze_amt = new BigDecimal("0");
			IndexedCollection iColl = SqlClient.queryList4IColl("queryFrozeAmtByAgrNo", agrNo, connection);
			KeyedCollection kc = null;
			if(iColl.size()>0){
				kc = (KeyedCollection) iColl.get(0);
				agr_froze_amt = new BigDecimal(kc.getDataValue("agr_froze_amt")+"");
			}
			kColl4App.addDataField("agr_froze_amt", agr_froze_amt);
			
			SInfoUtils.addSOrgName(kColl4App, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl4App, new String[] { "input_id" });
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl4App, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(kColl4App, context);
			
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
