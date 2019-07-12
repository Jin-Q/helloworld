package com.yucheng.cmis.biz01line.iqp.op.iqpbailsubdis;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetAddIqpBailSubDisRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpBailSubDis";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cont_no = "";
		String cus_id = "";
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			if(context.containsKey("cont_no")){
				cont_no = (String) context.getDataValue("cont_no");
				cus_id = (String) context.getDataValue("cus_id");
				String conStr = "where cont_no='"+cont_no+"'";
				List list = new ArrayList();
				list.add("cont_no");
				list.add("bail_acct_no");
				//根据合同编号获得相关保证金信息。
				IndexedCollection ic = dao.queryList("PubBailInfo", list, conStr,connection);
				KeyedCollection kColl = null;
				
				//根据合同编号获取保证金金额-----start-------------
				kColl = dao.queryDetail("CtrLoanCont", cont_no, connection);
				String cont_cur_type = (String)kColl.getDataValue("cont_cur_type");//合同币种
				String prd_id = (String)kColl.getDataValue("prd_id");//产品编号
				String serno = (String)kColl.getDataValue("serno");//业务编号
				BigDecimal cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));//合同金额
				String security_cur_type = (String)kColl.getDataValue("security_cur_type");//保证金币种
				BigDecimal security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));//保证金比例
				/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
				String manager_br_id = (String)kColl.getDataValue("manager_br_id");
				KeyedCollection kColl4SO =  dao.queryFirst("SOrg", null, "where organno='"+manager_br_id+"'", connection);
				String manager_br_id_displayname = (String)kColl4SO.getDataValue("organname");
				KeyedCollection kColl4CM = dao.queryFirst("CusManager", null, "where is_main_manager='1' and cont_no='"+cont_no+"'", connection);
				String manager_id ="";
				String manager_id_displayname ="";
				if(kColl4CM!=null && !"".equals(kColl4CM.getDataValue("manager_id"))){
					manager_id = (String) kColl4CM.getDataValue("manager_id");
					KeyedCollection kColl4SU =  dao.queryFirst("SUser", null, "where actorno='"+manager_id+"'", connection);
					manager_id_displayname = (String)kColl4SU.getDataValue("actorname");
				}
				/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
				//获取实时汇率  start
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
//				KeyedCollection kCollRate = service.getHLByCurrType(cont_cur_type, context, connection);
//				KeyedCollection kCollRate4Security = service.getHLByCurrType(security_cur_type, context, connection);
//				if("failed".equals(kCollRate.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				if("failed".equals(kCollRate4Security.getDataValue("flag"))){
//					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
//				}
//				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//合同汇率
//				BigDecimal security_exchange_rate = BigDecimalUtil.replaceNull(kCollRate4Security.getDataValue("sld"));//保证金汇率
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("exchange_rate"));//汇率
				BigDecimal security_exchange_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_exchange_rate"));//保证金币种汇率
				/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
				//获取实时汇率  end
				
				//计算保证金金额
				BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
				BigDecimal ori_bail_amt = new BigDecimal(0);//保证金金额
				BigDecimal securityAmt = new BigDecimal(0);
				if(prd_id.equals("500027") || prd_id.equals("500028") || prd_id.equals("500026") || prd_id.equals("500021") || prd_id.equals("500020") ||
						prd_id.equals("500032") || prd_id.equals("500029") || prd_id.equals("500031") || prd_id.equals("500022") || prd_id.equals("500025") || 
						prd_id.equals("500024") || prd_id.equals("500023") || prd_id.equals("400020") || prd_id.equals("700020") || prd_id.equals("700021")){
					
					//判断是否为信用证业务
					if("700020".equals(prd_id) || "700021".equals(prd_id)){
						KeyedCollection kCollCredit = dao.queryDetail("IqpCredit", serno, connection);
						if(kCollCredit.containsKey("serno")){
							floodact_perc = BigDecimalUtil.replaceNull(kCollCredit.getDataValue("floodact_perc"));
						}
					}
					securityAmt = cont_amt.multiply(security_rate).multiply(new BigDecimal(1).add(floodact_perc));
					securityAmt = (securityAmt.multiply(exchange_rate)).divide(security_exchange_rate,2,BigDecimal.ROUND_HALF_EVEN);
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					String caculateAmt = String.valueOf(securityAmt);
					securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
					String changeAmt = nf.format(securityAmt);
					ori_bail_amt = BigDecimalUtil.replaceNull(changeAmt);
				}else{
					ori_bail_amt = cont_amt.multiply(security_rate).multiply(exchange_rate).divide(security_exchange_rate,2,BigDecimal.ROUND_HALF_EVEN);
				}
				//根据合同编号获取保证金金额------end------------
				if(ic.size()>0){
					KeyedCollection kc = new KeyedCollection();
					for(int i=0;i<ic.size();i++){
						kc = (KeyedCollection) ic.get(i);
						kc.addDataField("origi_bail_bal", ori_bail_amt);
					}
					context.put("isShowAddBail", "no");//不展示
				}else{
					context.put("isShowAddBail", "yes");//判断页面是否显示新增保证金
				}
				ic.setName("IqpBailSubDisDetailList");
				
				CusServiceInterface cusServiceInterface = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				CusBase cusBase = cusServiceInterface.getCusBaseByCusId(cus_id, context, connection);
				String cus_id_displayname = cusBase.getCusName();
				context.put("cus_id_displayname", cus_id_displayname);
				/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
				context.put("manager_id", manager_id);
				context.put("manager_br_id", manager_br_id);
				context.put("manager_id_displayname", manager_id_displayname);
				context.put("manager_br_id_displayname", manager_br_id_displayname);
				/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
				this.putDataElement2Context(ic, context);
				context.put("floodact_perc", floodact_perc);
				context.put("prd_id", prd_id);
			}
			
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
