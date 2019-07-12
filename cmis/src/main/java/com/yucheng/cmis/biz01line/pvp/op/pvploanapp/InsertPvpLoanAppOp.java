package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

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

public class InsertPvpLoanAppOp extends CMISOperation {
	

	private final String modelId = "AccLoan";                        //贷款台账表模型
	private final String modelIdPvp = "PvpLoanApp";                  //贷款出账表模型
	private final String modelIdCont = "CtrLoanCont";                //合同主表表模型
	private final String modelIdContSub = "CtrLoanContSub";          //合同从表表模型
	private final String modelIdIqpAccp = "IqpAccAccp";              //银行承兑汇票表模型
	private final String modelIdIqpDetail = "IqpAccpDetail";         //承兑汇票申请明细表模型
	private final String modelIdIqpBatchMng = "IqpBatchMng";         //批次管理表表模型
	private final String modelIdIqpBatchBillRel = "IqpBatchBillRel"; //批次和票据关系表表模型
	private final String modelIdIqpBillDetail = "IqpBillDetail";     //票据明细表表模型
	private final String modelIdIqpBillIncome = "IqpBillIncome";     //票据收益计算表表模型
	private final String modelIdIqpDiscApp = "IqpDiscApp";     //贴现申请从表表模型
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
			String bill_no = "";
			String loan_amt = "";
            String end_date = "";
            String exchange_rate ="";
            String cur_type ="";
            String ruling_ir = "";
            String ir_float_rate = "";
            String ir_float_point = "";
            String reality_ir_y = "";
            String five_class ="";
			String manager_br_id = "";
			String fina_br_id = "";
			String sernoLoan = "";
			try {
				/*从贷款出账表中取数据*/
				PvpkColl = (KeyedCollection)context.getDataElement(modelIdPvp);
				cont_no = (String)PvpkColl.getDataValue("cont_no");
				serno = (String)PvpkColl.getDataValue("serno");//出账自有编号
				prd_id = (String)PvpkColl.getDataValue("prd_id");
				cus_id = (String)PvpkColl.getDataValue("cus_id");
				bill_no = (String)PvpkColl.getDataValue("bill_no"); 
				loan_amt = (String)PvpkColl.getDataValue("pvp_amt");//贷款金额，取出账金额
				manager_br_id = (String)PvpkColl.getDataValue("manager_br_id");
				fina_br_id = (String)PvpkColl.getDataValue("in_acct_br_id");//入账机构
			} catch (Exception e) {}
			if(PvpkColl == null || PvpkColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
			KeyedCollection kCollContSub = dao.queryDetail(modelIdContSub, cont_no, connection);
			 PvpComponent cmisComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(PvpConstant.PVPCOMPONENT, context, connection);
			
			sernoLoan = (String)kCollCont.getDataValue("serno");//取合同中的业务编号
			end_date = (String)kCollCont.getDataValue("cont_end_date");//到期日取合同到期日
			exchange_rate =(String)kCollCont.getDataValue("exchange_rate");//从合同取汇率
			cur_type =(String)kCollCont.getDataValue("cont_cur_type");//从合同取币种
			ruling_ir = (String)kCollContSub.getDataValue("ruling_ir");//从合同从表取基准利率
			ir_float_rate = (String)kCollContSub.getDataValue("ir_float_rate");//从合同从表取利率浮动比
			ir_float_point = (String)kCollContSub.getDataValue("ir_float_point");//从合同从表取利率浮动点数
			reality_ir_y = (String)kCollContSub.getDataValue("reality_ir_y");//从合同从表取执行年利率（年）
			five_class = (String)kCollContSub.getDataValue("five_classfiy");//从合同从表取五级分类
			
			/** 生成出账授权信息 */
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, context);
			KeyedCollection authKColl = new KeyedCollection(AUTHMODEL);
			authKColl.addDataField("serno", authSerno);
			authKColl.addDataField("tran_serno", sernoLoan);
			authKColl.addDataField("prd_id", prd_id);
			authKColl.addDataField("cus_id", cus_id);
			authKColl.addDataField("cus_name", "");
			authKColl.addDataField("send_times", "0");
			authKColl.addDataField("cont_no", cont_no);
			authKColl.addDataField("bill_no", bill_no);
			authKColl.addDataField("tran_amt", loan_amt);
			authKColl.addDataField("status", "00");//状态默认为登记
			dao.insert(authKColl, connection);
			
			/** 修改出账记录的出账状态为 已发出*/
			KeyedCollection pvpColl = dao.queryDetail(modelIdPvp, serno, connection);
			pvpColl.setDataValue("approve_status", "2");
			dao.update(pvpColl, connection);
			
			//产品为银票
            if(prd_id.equals("200024")){
        	     KeyedCollection kCollIqpAccp = dao.queryDetail(modelIdIqpAccp, sernoLoan, connection);
        	     String conditonStr = "where serno='"+sernoLoan+"'";
        	     IndexedCollection iColl = dao.queryList(modelIdIqpDetail, conditonStr, connection);
        	     String is_elec_bill = (String)kCollIqpAccp.getDataValue("is_elec_bill");
        	     String aorg_type = (String)kCollIqpAccp.getDataValue("acpt_org_type");
        	     String aorg_no = (String)kCollIqpAccp.getDataValue("actp_org_no");
        	     String aorg_name =(String)kCollIqpAccp.getDataValue("actp_org_name");
        	     for(int i=0;i<iColl.size();i++){
        	    	 String billNo = cmisComponent.getBillNoByContNo(cont_no);//生成借据编号
        	    	 KeyedCollection AcckColl = new KeyedCollection();
        	    	 AcckColl.setId("AccAccp");
        	    	 KeyedCollection kCollIqpDetail = (KeyedCollection)iColl.get(i);
        	    	 String paorg_no = (String)kCollIqpDetail.getDataValue("paorg_no");
        	    	 String paorg_name =(String)kCollIqpDetail.getDataValue("paorg_name");
        	    	 String paorg_acct_no =(String)kCollIqpDetail.getDataValue("clt_acct_no");
        	    	 String drft_amt = (String)kCollIqpDetail.getDataValue("drft_amt");
        	    	 
        	    	 AcckColl.addDataField("serno",serno);
            	     AcckColl.addDataField("prd_id",prd_id);
            	     AcckColl.addDataField("cont_no",cont_no);
            	     AcckColl.addDataField("bill_no",billNo);
            	     AcckColl.addDataField("bill_type","100");//票据类型
            	     AcckColl.addDataField("porder_no","");//汇票号码
            	     AcckColl.addDataField("utakeover_sign","");//不得转让标记
            	     AcckColl.addDataField("is_ebill",is_elec_bill);//是否电子票据
            	     AcckColl.addDataField("daorg_cusid",cus_id);//出票人客户码
            	     AcckColl.addDataField("daorg_cus_name","");//出票人名称 
            	     AcckColl.addDataField("drwr_org_code","");//出票人组织机构代码
            	     AcckColl.addDataField("daorg_no","");//出票人开户行行号
            	     AcckColl.addDataField("daorg_name","");//出票人开户行行名
            	     AcckColl.addDataField("daorg_acct","");//出票人开户行账号
            	     AcckColl.addDataField("aorg_type",aorg_type);//承兑行类型
            	     AcckColl.addDataField("aorg_no",aorg_no);//承兑行行号
            	     AcckColl.addDataField("aorg_name",aorg_name);//承兑行名称
            	     AcckColl.addDataField("pyee_name","");//收款人名称暂无
            	     AcckColl.addDataField("paorg_no",paorg_no);//收款人开户行行号
            	     AcckColl.addDataField("paorg_name",paorg_name);//收款人开户行行名
            	     AcckColl.addDataField("paorg_acct_no",paorg_acct_no);//收款人账号
            	     AcckColl.addDataField("exchange_rate",exchange_rate);//汇率
            	     AcckColl.addDataField("cur_type",cur_type);//币种
            	     AcckColl.addDataField("drft_amt",drft_amt);//票面金额
            	     AcckColl.addDataField("bill_isse_date","");//签发日期暂无
            	     AcckColl.addDataField("isse_date","");//出票日期暂无
            	     AcckColl.addDataField("porder_end_date","");//到期日期暂无
            	     AcckColl.addDataField("pad_rate","");//垫款利率暂无
            	     AcckColl.addDataField("pad_amt",0);//垫款金额初始为0
            	     AcckColl.addDataField("paydate","");//转垫款日期默认为空
            	     AcckColl.addDataField("separate_date","");//清分日期默认为空
            	     AcckColl.addDataField("writeoff_date","");//核销日期默认为空
            	     AcckColl.addDataField("five_class",five_class);
            	     AcckColl.addDataField("twelve_cls_flg","");//十二级分类标志
     				 AcckColl.addDataField("acc_day",date);
     				 AcckColl.addDataField("acc_year",year);
     				 AcckColl.addDataField("acc_mon",month);
            	     AcckColl.addDataField("manager_br_id",manager_br_id);
            	     AcckColl.addDataField("fina_br_id",fina_br_id);
            	     AcckColl.addDataField("accp_status","0");
            	     dao.insert(AcckColl, connection);
        	     }
        	     context.addDataField("flag", "success");
           }else if(prd_id.equals("300021") || prd_id.equals("300020")){
        	   //从贴现申请中取数据
        	   KeyedCollection IqpDiscAppkColl = dao.queryDetail(modelIdIqpDiscApp, sernoLoan, connection);
        	   String disc_type = (String)IqpDiscAppkColl.getDataValue("disc_type");
        	   String conditionStr = "where cont_no='"+cont_no+"'";
        	   IndexedCollection iColl = dao.queryList(modelIdIqpBatchMng, conditionStr, connection);  
               KeyedCollection kColl =(KeyedCollection)iColl.get(0);        	   
        	   String batch_no = (String)kColl.getDataValue("batch_no");
        	   String conditionRel = "where batch_no='"+batch_no+"'";
               IndexedCollection iCollBill = dao.queryList(modelIdIqpBatchBillRel,conditionRel, connection);
        	   for(int i=0;i<iCollBill.size();i++){
        		   String billNo = cmisComponent.getBillNoByContNo(cont_no);//生成借据编号
        		   KeyedCollection AcckColl = new KeyedCollection();
            	   AcckColl.setId("AccDrft");
        		   KeyedCollection kCollBillRel = (KeyedCollection)iCollBill.get(i);
        		   String porder_no = (String)kCollBillRel.getDataValue("porder_no");
        		   //取票据明细表
        		   KeyedCollection kCollBill = dao.queryDetail(modelIdIqpBillDetail, porder_no, connection);
        		   String porder_curr =(String)kCollBill.getDataValue("porder_curr");
        		   Map map = new HashMap();
        		   map.put("batch_no",batch_no);
        		   map.put("porder_no",porder_no);
        		   //取票据收益计算表
        		   KeyedCollection kCollBillIncome = dao.queryDetail(modelIdIqpBillIncome, map, connection);
        		   String fore_disc_date =(String)kCollBillIncome.getDataValue("fore_disc_date");
        		   String disc_days =(String)kCollBillIncome.getDataValue("disc_days");
        		   String adj_days =(String)kCollBillIncome.getDataValue("adj_days");
        		   String disc_rate =(String)kCollBillIncome.getDataValue("disc_rate");
        		   String dscnt_int =(String)kCollBillIncome.getDataValue("int");
        		   String drft_amt =(String)kCollBillIncome.getDataValue("drft_amt");
        		   String fore_rebuy_date =(String)kCollBillIncome.getDataValue("fore_rebuy_date");
        		   String rebuy_days =(String)kCollBillIncome.getDataValue("rebuy_days");
        		   String rebuy_rate =(String)kCollBillIncome.getDataValue("rebuy_rate");
        		   String due_rebuy_rate =(String)kCollBillIncome.getDataValue("due_rebuy_rate");
        		   String rebuy_int =(String)kCollBillIncome.getDataValue("rebuy_int");
        		   AcckColl.addDataField("serno",serno);
          	       AcckColl.addDataField("prd_id",prd_id);
          	       AcckColl.addDataField("cus_id",cus_id);
          	       AcckColl.addDataField("cont_no",cont_no);
          	       AcckColl.addDataField("bill_no",billNo);
          	       AcckColl.addDataField("dscnt_type",disc_type);//贴现方式 直贴
          	       AcckColl.addDataField("porder_no",porder_no);
          	       AcckColl.addDataField("discount_per","");//贴现人/交易对手
          	       AcckColl.addDataField("dscnt_date",fore_disc_date);//预计贴现日
          	       AcckColl.addDataField("dscnt_day",disc_days);//贴现天数
          	       AcckColl.addDataField("adjust_day",adj_days);//调整天数
          	       AcckColl.addDataField("dscnt_rate",disc_rate);//贴现利率
          	       AcckColl.addDataField("cur_type",porder_curr);//交易币种
          	       AcckColl.addDataField("dscnt_int",dscnt_int);//贴现利息
          	       AcckColl.addDataField("rpay_amt",drft_amt);//实付金额去票据收益计算表中的票面金额
          	       AcckColl.addDataField("rebuy_date",fore_rebuy_date);//回顾日期取预期回购日期
          	       AcckColl.addDataField("rebuy_day",rebuy_days);//回购天数
          	       AcckColl.addDataField("rebuy_rate",rebuy_rate);//回购利率
          	       AcckColl.addDataField("overdue_rebuy_rate",due_rebuy_rate);//逾期回购利率
          	       AcckColl.addDataField("rebuy_int",rebuy_int);//回购利息 
          	       AcckColl.addDataField("separate_date","");//清分日期默认为空
          	       AcckColl.addDataField("writeoff_date","");//核销日期默认为空
          	       AcckColl.addDataField("five_class",five_class);//五级分类
          	       AcckColl.addDataField("twelve_cls_flg","");//十二级分类
          	       AcckColl.addDataField("acc_day",date);
 				   AcckColl.addDataField("acc_year",year);
 				   AcckColl.addDataField("acc_mon",month);
          	       AcckColl.addDataField("manager_br_id",manager_br_id);
          	       AcckColl.addDataField("fina_br_id",fina_br_id);
          	       AcckColl.addDataField("accp_status","0");
          	       dao.insert(AcckColl, connection);
        	   }
        	   context.addDataField("flag", "success");
           }else{
        	     KeyedCollection AcckColl = new KeyedCollection();
        	     AcckColl.setId("AccLoan");
        	     AcckColl.addDataField("serno",serno);
        	     AcckColl.addDataField("prd_id",prd_id);
        	     AcckColl.addDataField("cus_id",cus_id);
        	     AcckColl.addDataField("cont_no",cont_no);
        	     AcckColl.addDataField("bill_no",bill_no);
        	     AcckColl.addDataField("loan_amt",loan_amt);
        	     AcckColl.addDataField("loan_balance",loan_amt);//贷款余额默认为贷款金额
        	     AcckColl.addDataField("distr_date",date);//发放日期默认
 				 AcckColl.addDataField("end_date",end_date);
 				 AcckColl.addDataField("ori_end_date",end_date);//原到期日默认为到期日
 				 AcckColl.addDataField("post_count",0);//展期次数默认为0
 				 AcckColl.addDataField("overdue",0);   //逾期期数默认为0
 				 AcckColl.addDataField("separate_date","");//清分日期默认为空
 				 AcckColl.addDataField("ruling_ir",ruling_ir);//从合同从表取基准利率
 				 AcckColl.addDataField("ir_float_rate",ir_float_rate);//从合同从表取利率浮动比
 				 AcckColl.addDataField("ir_float_point",ir_float_point);//从合同从表取利率浮动点数
 				 AcckColl.addDataField("reality_ir_y",reality_ir_y);//从合同从表取执行年利率（年）
 				 AcckColl.addDataField("comp_int_balance",0);//复利余额默认为0
 				 AcckColl.addDataField("inner_owe_int",0);//表内欠息默认为0
 				 AcckColl.addDataField("out_owe_int",0);//表外欠息默认为0
 				 AcckColl.addDataField("rec_int_accum",0);//应收利息累计默认为0
 				 AcckColl.addDataField("recv_int_accum",0);//实收利息累计默认为0
 				 AcckColl.addDataField("normal_balance",loan_amt);//正常余额默认为贷款金额
 				 AcckColl.addDataField("overdue_balance",0);//逾期余额默认为0
 				 AcckColl.addDataField("slack_balance",0);//呆滞余额默认为0
 				 AcckColl.addDataField("bad_dbt_balance",0);//呆账余额默认为0
 				 AcckColl.addDataField("twelve_cls_flg","");//十二级分类标志
 				 AcckColl.addDataField("manager_br_id",manager_br_id);
 				 AcckColl.addDataField("fina_br_id",fina_br_id);
 				 AcckColl.addDataField("acc_day",date);
 				 AcckColl.addDataField("acc_year",year);
 				 AcckColl.addDataField("acc_mon",month);
 				 AcckColl.addDataField("writeoff_date","");//核销日期默认为空
 				 AcckColl.addDataField("paydate","");//软垫款日默认为空
 				 AcckColl.addDataField("five_class",five_class);//五级分类
 				 AcckColl.addDataField("acc_status","0");//默认为未出帐状态
 				 dao.insert(AcckColl, connection);
 	             context.addDataField("flag", "success");
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
