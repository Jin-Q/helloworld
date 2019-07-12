package com.yucheng.cmis.biz01line.arp.op.arpbadassethandoverapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class LeadArpBadassetHandoverAppOp extends CMISOperation {

	private final String modelId = "ArpBadassetHandoverApp";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl_arp = new KeyedCollection(modelId);
			String bill_no = context.getDataValue("bill_no").toString();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			kColl_arp = delBillInfo(kColl_arp, context, connection, bill_no,dao);
			SInfoUtils.addSOrgName(kColl_arp, new String[] { "fount_manager_br_id"});
			SInfoUtils.addUSerName(kColl_arp, new String[] { "fount_manager_id"});
			
			this.putDataElement2Context(kColl_arp, context);			
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
	
	public KeyedCollection delBillInfo(KeyedCollection kColl , Context context,
			Connection connection,String bill_no,TableModelDAO dao) throws Exception {
		
		/*** 从业务表里取借据信息 ***/
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
		KeyedCollection kColl_acc = new KeyedCollection();//贷款台账
		kColl_acc = service.getAccByConditionKcoll(bill_no, context, connection, "1");		
		
		String cont_no = kColl_acc.getDataValue("cont_no")+"";
		IndexedCollection iColl_manager = service.getAccByCondition(
				"where is_main_manager='1' and rownum = '1' and cont_no='"+ cont_no + "'", context, null, connection, "0");
		
		if(iColl_manager.size() > 0){
			KeyedCollection kColl_manager = (KeyedCollection) iColl_manager.getElementAt(0);
			
			if(!kColl.containsKey("bill_no")){
				kColl.addDataField("bill_no", bill_no); //借据编号
			}
			if(!kColl.containsKey("cont_no")){
				kColl.addDataField("cont_no", cont_no); //合同编号
			}
			if(!kColl.containsKey("fount_manager_br_id")){
				kColl.addDataField("fount_manager_br_id", kColl_acc.getDataValue("manager_br_id").toString()); //原管理机构
			}
			if(!kColl.containsKey("fount_manager_id")){
				kColl.addDataField("fount_manager_id", kColl_manager.getDataValue("manager_id").toString()); //原主管客户经理
			}
			if(!kColl.containsKey("cus_id")){
				kColl.addDataField("cus_id", kColl_acc.getDataValue("cus_id").toString()); //客户码
			}
			kColl.addDataField("cur_type", kColl_acc.getDataValue("cur_type").toString()); //币种
			kColl.addDataField("prd_id", kColl_acc.getDataValue("prd_id").toString()); //产品类别
			kColl.addDataField("loan_amt", kColl_acc.getDataValue("loan_amt").toString()); //借据金额
			kColl.addDataField("loan_balance", kColl_acc.getDataValue("loan_balance").toString()); //借据余额
			kColl.addDataField("five_class", kColl_acc.getDataValue("five_class").toString()); //五级分类标志
			
			/*** 翻译中文名，中文合同编号,产品编号 ***/
			String[] args=new String[] { "cus_id" ,"cont_no","prd_id"};
			String[] modelIds=new String[]{"CusBase","CtrLoanCont","PrdBasicinfo"};
			String[] modelForeign=new String[]{"cus_id","cont_no","prdid"};
			String[] fieldName=new String[]{"cus_name","cn_cont_no","prdname"};
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);		

			/*** 四级分类现在采用新模式，需要进行判断 ***/
			double normal_balance = Double.parseDouble(kColl_acc.getDataValue("normal_balance").toString()) ; //正常余额
			double overdue_balance = Double.parseDouble(kColl_acc.getDataValue("overdue_balance").toString()) ; //逾期余额
			double slack_balance = Double.parseDouble(kColl_acc.getDataValue("slack_balance").toString()) ; //呆滞余额
			double bad_dbt_balance = Double.parseDouble(kColl_acc.getDataValue("bad_dbt_balance").toString()) ; //呆账余额
			String four_class = "";
			
			if(normal_balance > 0){
				four_class = "1";
			}else if(overdue_balance > 0){
				four_class = "3";
			}else if(slack_balance > 0){
				four_class = "7";
			}else if(bad_dbt_balance > 0){
				four_class = "8";
			}
			kColl.addDataField("four_class", four_class); //四级分类标志
		}
		
		return kColl;		
	}

}