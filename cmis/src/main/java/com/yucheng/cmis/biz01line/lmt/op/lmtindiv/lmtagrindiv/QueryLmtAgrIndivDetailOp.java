package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtagrindiv;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAgrIndivDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAgrIndiv";
	private final String agr_no_name = "agr_no";
	
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String agr_no_value = null;
			try {
				agr_no_value = (String)context.getDataValue(agr_no_name);
			} catch (Exception e) {}
			if(agr_no_value == null || agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+agr_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, agr_no_value, connection);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
			
			//String crd_totl_amt = kColl.getDataValue("crd_totl_amt").toString();
			//String lrisk_totl_amt = kColl.getDataValue("lrisk_totl_amt").toString();
			
			//授信协议下不展示授信余额    2013-11-30  唐顺岩
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//			KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo(agr_no_value, "01", connection, context);
//			String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
//			double crd_bal_amt = Double.parseDouble(crd_totl_amt) + Double.parseDouble(lrisk_totl_amt) - Double.parseDouble(lmt_amt);
//			kColl.addDataField("crd_bal_amt", crd_bal_amt);
			
			//kColl.put("crd_totl_amt", Double.parseDouble(crd_totl_amt) + Double.parseDouble(lrisk_totl_amt));  //将低风险与非风险金额汇总

			//汇总循环额度、一次性额度
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			KeyedCollection kColl_details = lmtComponent.selectLmtAgrAmtByAgr(kColl.getDataValue("agr_no").toString());
			if(null!=kColl_details){
				kColl.addDataField("totl_amt", kColl_details.getDataValue("total_amt"));
				kColl.addDataField("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
				kColl.addDataField("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
				
				kColl.addDataField("lrisk_total_amt", kColl_details.getDataValue("lrisk_total_amt"));
				kColl.addDataField("lrisk_cir_amt", kColl_details.getDataValue("lrisk_cir_amt"));
				kColl.addDataField("lrisk_one_amt", kColl_details.getDataValue("lrisk_one_amt"));
				
				BigDecimal crd_totl_amt = ((BigDecimal)kColl_details.getDataValue("total_amt")).add((BigDecimal)kColl_details.getDataValue("lrisk_total_amt"));
				crd_totl_amt.add(new BigDecimal(kColl.getDataValue("self_amt")+""));  //个人授信总金额=低风险+非低风险+自助额度    2013-12-12  唐顺岩
				kColl.put("crd_totl_amt",crd_totl_amt.toString());  //将低风险与非风险金额汇总
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
