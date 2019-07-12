package com.yucheng.cmis.biz01line.iqp.op.iqpbailsubdis;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpBailSubDisDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpBailSubDis";
	private final String modelIdBail = "PubBailInfo";
	private final String serno_name = "serno";
	private boolean updateCheck = true;
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String serno_value = null;
			String cont_no="";
			String bailAcctNo="";//新增的时候，对这些保证金账号做过追加或者提取操作
			String op="";
			KeyedCollection kCollBail = new KeyedCollection("PubBailInfo");
			BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
			try {
				serno_value = (String)context.getDataValue(serno_name);
			   if(context.containsKey("cont_no")){
				   cont_no = (String)context.getDataValue("cont_no");
			   }
				op = (String) context.getDataValue("op");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection kColl1 = dao.queryDetail(modelId, serno_value, connection);
			cont_no = (String)kColl1.getDataValue("cont_no");
			
			//拼接where条件，根据业务编号查出追加或提取明细记录(修改时用到)
		    String conStr = "where serno ='"+serno_value+"'";
		    IndexedCollection ic = dao.queryList("IqpBailSubDisDetail", conStr, connection);
		    ic.setName("IqpBailSubDisDetailList");
		    KeyedCollection kc1 = new KeyedCollection();
		    for(int i=0;i<ic.size();i++){
		    	kc1 = (KeyedCollection) ic.get(i);
		    	bailAcctNo += "'"+kc1.getDataValue("bail_acct_no")+"',";
		    }
		    if(ic.size()==0){
				context.put("isShowAddBail", "yes");//判断页面是否显示新增保证金
		    }else{
		    	context.put("isShowAddBail", "no");//不展示
		    }
		   
		    if("update".equals(op) || "view".equals(op)){
	    	    String conStr1 = null;
			    if(!"".equals(bailAcctNo)){
			    	bailAcctNo = bailAcctNo.substring(0, bailAcctNo.length()-1);
			    	conStr1 ="where cont_no='"+cont_no+"' and bail_acct_no not in ("+bailAcctNo+")";
			    }else{
			    	conStr1 ="where cont_no='"+cont_no+"'";
			    }
			    
				List list = new ArrayList();
				list.add("cont_no");
				list.add("bail_acct_no");
				//根据合同编号获得相关保证金信息。
				IndexedCollection ic1 = dao.queryList("PubBailInfo", list, conStr1,connection);
				KeyedCollection kColl = null;
				
				//根据合同编号获取保证金金额-----start-------------
				kColl = dao.queryDetail("CtrLoanCont", cont_no, connection);
				String cont_cur_type = (String)kColl.getDataValue("cont_cur_type");//合同币种
				String prd_id = (String)kColl.getDataValue("prd_id");//产品编号
				String serno = (String)kColl.getDataValue("serno");//业务编号
				BigDecimal cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));//合同金额
				String security_cur_type = (String)kColl.getDataValue("security_cur_type");//保证金币种
				BigDecimal security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));//保证金比例

				//获取实时汇率  start
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				KeyedCollection kCollRate = service.getHLByCurrType(cont_cur_type, context, connection);
				KeyedCollection kCollRate4Security = service.getHLByCurrType(security_cur_type, context, connection);
				if("failed".equals(kCollRate.getDataValue("flag"))){
					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				}
				if("failed".equals(kCollRate4Security.getDataValue("flag"))){
					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				}
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//合同汇率
				BigDecimal security_exchange_rate = BigDecimalUtil.replaceNull(kCollRate4Security.getDataValue("sld"));//保证金汇率
				//获取实时汇率  end
				
				//计算保证金金额
				
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
				if(ic1.size()>0){
					KeyedCollection kc = new KeyedCollection();
					for(int i=0;i<ic1.size();i++){
						kc = (KeyedCollection) ic1.get(i);
						kc.addDataField("origi_bail_bal", ori_bail_amt);
						ic.add(kc);
					}
					context.put("isShowAddBail", "no");//不展示
				}else{
					IndexedCollection iCollBail = dao.queryList(modelIdBail, conStr, connection);
					if(iCollBail.size()>0){
						kCollBail = (KeyedCollection)iCollBail.get(0);
						context.put("isShowAddBail", "yes");//判断页面是否显示新增保证金
					}
				}
				context.put("floodact_perc", floodact_perc);
				context.put("prd_id", prd_id);
		    }
			
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl1, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl1, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl1, new String[] { "input_id" });
			SInfoUtils.addSOrgName(kColl1, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(kColl1, new String[] { "manager_id" });
			kColl1.addDataElement(kCollBail);
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
			context.put("manager_id", kColl1.getDataValue("manager_id"));
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
			this.putDataElement2Context(kColl1, context);
			this.putDataElement2Context(ic, context);
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
