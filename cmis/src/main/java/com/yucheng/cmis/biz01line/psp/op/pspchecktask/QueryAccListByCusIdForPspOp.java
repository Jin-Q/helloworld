package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryAccListByCusIdForPspOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String cus_id = null;
		String task_type = null;
		String task_id = null;
		try{
			connection = this.getConnection(context);
			try {
				cus_id = (String) context.getDataValue("cus_id");
			} catch (Exception e) {
            	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "获取不到客户编号", null);
            	throw new EMPException(e);
            }
			if(context.containsKey("task_type")){
				task_type = (String) context.getDataValue("task_type");
			}
			if(context.containsKey("task_id")){
				task_id = (String) context.getDataValue("task_id");
			}
			KeyedCollection acckColl = null;
			IndexedCollection accLoanIColl = new IndexedCollection();//贷款台账
			IndexedCollection accAccpIColl = new IndexedCollection();//银承台账
			IndexedCollection accDrftIColl = new IndexedCollection();//票据台账
			IndexedCollection accPadIColl = new IndexedCollection();//垫款台账
			IndexedCollection accAssetstrsfIColl = new IndexedCollection();//转资产台账
			if("05".equals(task_type)){//合作方
				TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
				String conditionStr = " where bill_no in (select c.bill_no                                                    "
									 +"                     from lmt_agr_joint_coop a, r_bus_lmtcredit_info b, psp_acc_view c "
									 +"                    where a.agr_no = b.agr_no                                          "
									 +"  					 and b.cont_no = c.cont_no                                        "
									 +"  					 and a.coop_type <> '010' 										  "
									 +"  					 and a.agr_status in ('002', '004') 							  "
									 +"  					 and a.cus_id = '"+cus_id+"')								      ";
				accLoanIColl = dao.queryList("AccLoan", null,conditionStr ,connection);
				String[] args=new String[] {"cus_id","prd_id"};
				String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
				String[]modelForeign=new String[]{"cus_id","prdid"};
				String[] fieldName=new String[]{"cus_name","prdname"};
				SystemTransUtils.dealName(accLoanIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				accLoanIColl.setName(accLoanIColl.getName() + "List");
				this.putDataElement2Context(accLoanIColl, context);
				
				accAccpIColl = dao.queryList("AccAccp", null,conditionStr ,connection);
				String[] args1=new String[] {"daorg_cusid"};
				String[] modelIds1=new String[]{"CusBase"};
				String[]modelForeign1=new String[]{"cus_id"};
				String[] fieldName1=new String[]{"cus_name"};
				SystemTransUtils.dealName(accAccpIColl, args1, SystemTransUtils.ADD, context, modelIds1,modelForeign1, fieldName1);
				accAccpIColl.setName(accAccpIColl.getName() + "List");
				this.putDataElement2Context(accAccpIColl, context);
				
				accDrftIColl = dao.queryList("AccDrft", null,conditionStr ,connection);
				String[] args2=new String[] {"discount_per"};
				String[] modelIds2=new String[]{"CusBase"};
				String[]modelForeign2=new String[]{"cus_id"};
				String[] fieldName2=new String[]{"cus_name"};
				SystemTransUtils.dealName(accDrftIColl, args2, SystemTransUtils.ADD, context, modelIds2,modelForeign2, fieldName2);
				accDrftIColl.setName(accDrftIColl.getName() + "List");
				this.putDataElement2Context(accDrftIColl, context);
				
				accPadIColl = dao.queryList("AccPad", null,conditionStr ,connection);
				SystemTransUtils.dealName(accPadIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				accPadIColl.setName(accPadIColl.getName() + "List");
				this.putDataElement2Context(accPadIColl, context);
				
			}else if("06".equals(task_type)){//担保
				TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
				String conditionStr = " where bill_no in (select e.bill_no                                                    "
									 +"                     from lmt_agr_fin_guar a, grt_guarantee    b,                      "
									 +"                          grt_guaranty_re  c, grt_loan_r_gur   d,                      "
									 +"                          psp_acc_view     e                                           "
									 +"                    where a.cus_id = b.cus_id                                          "
									 +"  					 and b.guar_id = c.guaranty_id                                    "
									 +"  					 and c.guar_cont_no = d.guar_cont_no 							  "
									 +"  					 and d.cont_no = e.cont_no 							              "
									 +"  					 and a.agr_status in ('002', '004') 				              "
									 +"  					 and a.cus_id = '"+cus_id+"')								      ";
				accLoanIColl = dao.queryList("AccLoan", null,conditionStr ,connection);
				String[] args=new String[] {"cus_id","prd_id"};
				String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
				String[]modelForeign=new String[]{"cus_id","prdid"};
				String[] fieldName=new String[]{"cus_name","prdname"};
				SystemTransUtils.dealName(accLoanIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				accLoanIColl.setName(accLoanIColl.getName() + "List");
				this.putDataElement2Context(accLoanIColl, context);
				
				accAccpIColl = dao.queryList("AccAccp", null,conditionStr ,connection);
				String[] args1=new String[] {"daorg_cusid"};
				String[] modelIds1=new String[]{"CusBase"};
				String[]modelForeign1=new String[]{"cus_id"};
				String[] fieldName1=new String[]{"cus_name"};
				SystemTransUtils.dealName(accAccpIColl, args1, SystemTransUtils.ADD, context, modelIds1,modelForeign1, fieldName1);
				accAccpIColl.setName(accAccpIColl.getName() + "List");
				this.putDataElement2Context(accAccpIColl, context);
				
				accDrftIColl = dao.queryList("AccDrft", null,conditionStr ,connection);
				String[] args2=new String[] {"discount_per"};
				String[] modelIds2=new String[]{"CusBase"};
				String[]modelForeign2=new String[]{"cus_id"};
				String[] fieldName2=new String[]{"cus_name"};
				SystemTransUtils.dealName(accDrftIColl, args2, SystemTransUtils.ADD, context, modelIds2,modelForeign2, fieldName2);
				accDrftIColl.setName(accDrftIColl.getName() + "List");
				this.putDataElement2Context(accDrftIColl, context);
				
				accPadIColl = dao.queryList("AccPad", null,conditionStr ,connection);
				SystemTransUtils.dealName(accPadIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				accPadIColl.setName(accPadIColl.getName() + "List");
				this.putDataElement2Context(accPadIColl, context);
				
			}else{
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				
				acckColl = service.getCusBizBalanceByCusId(cus_id, connection);
				acckColl.setName("AccDetails");
				this.putDataElement2Context(acckColl, context);
				
				KeyedCollection queryData = new KeyedCollection();
				queryData.addDataField("cus_id", cus_id);
				queryData.addDataField("acc_status", "1");
				/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 start */
				TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
				accLoanIColl = dao.queryList("AccLoan", null, " where bill_no in (select bill_no from psp_check_task_rel where task_id = '"+task_id+"' and prd_type = 'ACC_LOAN')", connection);
				if(accLoanIColl==null||accLoanIColl.size()<1){
					accLoanIColl = service.getAccByQueryData(queryData, null, context, connection, "1");
				}
				/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 end */
				String[] args=new String[] {"cus_id","prd_id"};
				String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
				String[]modelForeign=new String[]{"cus_id","prdid"};
				String[] fieldName=new String[]{"cus_name","prdname"};
				SystemTransUtils.dealName(accLoanIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				accLoanIColl.setName("AccLoan"+"List");
				this.putDataElement2Context(accLoanIColl, context);
				
				KeyedCollection queryDataForAccp = new KeyedCollection();
				queryDataForAccp.addDataField("daorg_cusid", cus_id);
				queryDataForAccp.addDataField("accp_status", "1");
				/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 start */
				accAccpIColl = dao.queryList("AccAccp", null, " where bill_no in (select bill_no from psp_check_task_rel where task_id = '"+task_id+"' and prd_type = 'ACC_ACCP')", connection);
				if(accAccpIColl==null||accAccpIColl.size()<1){
					accAccpIColl = service.getAccByQueryData(queryDataForAccp, null, context, connection, "2");
				}
				/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 end */
				String[] args1=new String[] {"daorg_cusid"};
				String[] modelIds1=new String[]{"CusBase"};
				String[]modelForeign1=new String[]{"cus_id"};
				String[] fieldName1=new String[]{"cus_name"};
				SystemTransUtils.dealName(accAccpIColl, args1, SystemTransUtils.ADD, context, modelIds1,modelForeign1, fieldName1);
				accAccpIColl.setName("AccAccp"+"List");
				this.putDataElement2Context(accAccpIColl, context);
				
				KeyedCollection queryDataForDrft = new KeyedCollection();
				queryDataForDrft.addDataField("discount_per", cus_id);
				queryDataForDrft.addDataField("accp_status", "1");
				/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 start */
				accDrftIColl = dao.queryList("AccDrft", null, " where bill_no in (select bill_no from psp_check_task_rel where task_id = '"+task_id+"' and prd_type = 'ACC_DRFT')", connection);
				if(accDrftIColl==null||accDrftIColl.size()<1){
					accDrftIColl = service.getAccByQueryData(queryDataForDrft, null, context, connection, "3");
				}
				/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 end */
				String[] args2=new String[] {"discount_per"};
				String[] modelIds2=new String[]{"CusBase"};
				String[]modelForeign2=new String[]{"cus_id"};
				String[] fieldName2=new String[]{"cus_name"};
				SystemTransUtils.dealName(accDrftIColl, args2, SystemTransUtils.ADD, context, modelIds2,modelForeign2, fieldName2);
				accDrftIColl.setName("AccDrft"+"List");
				this.putDataElement2Context(accDrftIColl, context);
				
				KeyedCollection queryDataForPad = new KeyedCollection();
				queryDataForPad.addDataField("cus_id", cus_id);
				queryDataForPad.addDataField("accp_status", "1");
				/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 start */
				accPadIColl = dao.queryList("AccPad", null, " where bill_no in (select bill_no from psp_check_task_rel where task_id = '"+task_id+"' and prd_type = 'ACC_PAD')", connection);
				if(accPadIColl==null||accPadIColl.size()<1){
					accPadIColl = service.getAccByQueryData(queryDataForPad, null, context, connection, "4");
				}
				/* modified by yangzy 2014/10/23 贷后改造需求_XD140922063 end */
				SystemTransUtils.dealName(accPadIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				accPadIColl.setName("AccPad"+"List");
				this.putDataElement2Context(accPadIColl, context);
				
				SystemTransUtils.dealName(accAssetstrsfIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
				accAssetstrsfIColl = service.getAccByQueryData(queryData, null, context, connection, "5");
				accAssetstrsfIColl.setName("AccAssetstrsf"+"List");
				this.putDataElement2Context(accAssetstrsfIColl, context);
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
