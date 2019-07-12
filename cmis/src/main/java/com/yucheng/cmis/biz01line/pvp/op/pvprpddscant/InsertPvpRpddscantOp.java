package com.yucheng.cmis.biz01line.pvp.op.pvprpddscant;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.pvp.component.PvpComponent;
import com.yucheng.cmis.biz01line.pvp.pvptools.PvpConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class InsertPvpRpddscantOp extends CMISOperation {

	private final String modelId = "AccLoan";                        //贷款台账表模型
	private final String modelIdPvp = "PvpLoanApp";                  //贷款出账表模型
	private final String modelIdCont = "CtrRpddscntCont";            //转贴现合同表模型
	private final String modelIdIqpBatchBillRel = "IqpBatchBillRel"; //批次和票据关系表表模型
	private final String modelIdIqpBillIncome = "IqpBillIncome";     //票据收益计算表表模型
	private static final String AUTHMODEL = "PvpAuthorize";          //出账授权表模型

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		//取系统当前日期
		String date = context.getDataValue(PUBConstant.OPENDAY).toString();
		String year = date.substring(0,4);
		String month = date.substring(5,7);
		try{
			connection = this.getConnection(context);
			KeyedCollection PvpkColl = null;
			String cont_no = "";
			String serno = "";
			String prd_id = "";
			String cus_id = "";
			String manager_br_id = "";
			String fina_br_id = "";
			try {
				/*从贷款出账表中取数据*/
				PvpkColl = (KeyedCollection)context.getDataElement(modelIdPvp);
				cont_no = (String)PvpkColl.getDataValue("cont_no");
				serno = (String)PvpkColl.getDataValue("serno");
				prd_id = (String)PvpkColl.getDataValue("prd_id");
				cus_id = (String)PvpkColl.getDataValue("cus_id");
				manager_br_id = (String)PvpkColl.getDataValue("manager_br_id");
				fina_br_id = (String)PvpkColl.getDataValue("fina_br_id");
			} catch (Exception e) {}
			if(PvpkColl == null || PvpkColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
            //取转贴现合同表数据
			KeyedCollection ContkColl = (KeyedCollection)dao.queryDetail(modelIdCont, cont_no, connection);
			String batch_no = (String)ContkColl.getDataValue("batch_no");//从合同中获取批次号
			String bill_curr = (String)ContkColl.getDataValue("bill_curr");//从合同中获取票据币种
			String rpddscnt_rate = (String)ContkColl.getDataValue("rpddscnt_rate");//从合同中获取转贴现利息
			String rpay_amt = (String)ContkColl.getDataValue("rpay_amt");//从合同中获取实付金额
			String rebuy_date = (String)ContkColl.getDataValue("rebuy_date");//从合同中获取回购日期
			String rebuy_rate = (String)ContkColl.getDataValue("rebuy_rate");//从合同中获取回购利率
			String rebuy_amt = (String)ContkColl.getDataValue("rebuy_amt");//从合同中获取回购金额
			String condition = "where batch_no='"+batch_no+"'";
            IndexedCollection iCollBillRel = dao.queryList(modelIdIqpBatchBillRel, condition, connection);
            
            PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(PvpConstant.PVPCOMPONENT, context, connection);
			//迭代票据明细
            for(int i=0;i<iCollBillRel.size();i++){
            	String billNo = cmisComponent.getBillNoByContNo(cont_no);//生成借据编号
            	KeyedCollection BillRelkColl = (KeyedCollection)iCollBillRel.get(i);
            	String porder_no =(String)BillRelkColl.getDataValue("porder_no");
            	Map map = new HashMap();
      		    map.put("batch_no",batch_no);
      		    map.put("porder_no",porder_no);
      		    //取票据收益计算表
      		    KeyedCollection kCollBillIncome = dao.queryDetail(modelIdIqpBillIncome, map, connection);
      		    String disc_days = (String)kCollBillIncome.getDataValue("disc_days");
      		    String adj_days = (String)kCollBillIncome.getDataValue("adj_days");
      		    String disc_rate = (String)kCollBillIncome.getDataValue("disc_rate");
      		    String rebuy_days = (String)kCollBillIncome.getDataValue("rebuy_days");
      		    String due_rebuy_rate = (String)kCollBillIncome.getDataValue("due_rebuy_rate");
      		    
            	KeyedCollection pvpkColl = new KeyedCollection();
            	pvpkColl.setId("AccDrft");
            	pvpkColl.addDataField("serno",serno);
            	pvpkColl.addDataField("acc_day",date);
            	pvpkColl.addDataField("acc_year",year);
            	pvpkColl.addDataField("acc_mon",month);
            	pvpkColl.addDataField("prd_id",prd_id);
            	pvpkColl.addDataField("cont_no",cont_no);
            	pvpkColl.addDataField("bill_no",billNo);
            	pvpkColl.addDataField("dscnt_type","");//贴现方式
            	pvpkColl.addDataField("porder_no",porder_no);
            	pvpkColl.addDataField("discount_per",cus_id);//对手行行号
            	pvpkColl.addDataField("dscnt_date",disc_days);//贴现天数
            	pvpkColl.addDataField("adjust_day",adj_days);//调整天数
            	pvpkColl.addDataField("dscnt_rate",disc_rate);//贴现利息
            	pvpkColl.addDataField("cur_type",bill_curr);//交易币种
            	pvpkColl.addDataField("dscnt_int",rpddscnt_rate);//贴现利息
            	pvpkColl.addDataField("rpay_amt",rpay_amt);//实付金额
            	pvpkColl.addDataField("rebuy_date",rebuy_date);//回购日期
            	pvpkColl.addDataField("rebuy_day",rebuy_days);//回购天数
            	pvpkColl.addDataField("rebuy_rate",rebuy_rate);//回购利率
            	pvpkColl.addDataField("overdue_rebuy_rate",due_rebuy_rate);//逾期回购利率
            	pvpkColl.addDataField("rebuy_int",rebuy_amt);//回购金额
            	pvpkColl.addDataField("separate_date","");//清分日期
            	pvpkColl.addDataField("writeoff_date","");//核销日期
            	pvpkColl.addDataField("five_class","");//五级分类 
            	pvpkColl.addDataField("twelve_cls_flg","");//十二级分类
            	pvpkColl.addDataField("manager_br_id",manager_br_id);//管理机构
            	pvpkColl.addDataField("fina_br_id",fina_br_id);//账务机构
            	pvpkColl.addDataField("accp_status","0");//台账状态
            	dao.insert(pvpkColl, connection);
            }
            /** 修改出账记录的出账状态为 已发出*/
			KeyedCollection pvpColl = dao.queryDetail(modelIdPvp, serno, connection);
			pvpColl.setDataValue("approve_status", "2");
			dao.update(pvpColl, connection);
			context.addDataField("flag", "success");
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
