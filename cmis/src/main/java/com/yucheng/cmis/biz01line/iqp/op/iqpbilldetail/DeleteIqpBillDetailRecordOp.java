package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class DeleteIqpBillDetailRecordOp extends CMISOperation {
	private final String batModel = "IqpBatchMng";
	private final String modelId = "IqpBatchBillRel";
	private final String incomeModel ="IqpBillIncome"; //利息表模型
	private final String pIntModel = "IqpBillPintDetail";
	private final String loanModel = "IqpLoanApp"; //业务申请表模型
	private final String rpddsntModel = "IqpRpddscnt"; //转贴现申请表模型
	private final String discAppModel = "IqpDiscApp";//贴现申请从表模型
	
	public String doExecute(Context context) throws EMPException {
		/** 票据列表删除操作，只删除关联表中关联关系，不删票据、不删批次 
		 * 
		 *  票据列表剔票后，不仅要删除批次和票据的关系表，并且要删除该批次下
		 *  该票据的利息计算数据。
		 *  add by MQ 2013-08-16
		 *  
		 *  票据剔除有三种业务场景：
		 *  1、在票据批次管理模块中，可以对批次下的票据进行剔除。
		 *  2、在业务申请模块中，主要为直贴业务，导入批次后可以对票据进行剔除。
		 *  3、在转贴现业务模块中，主要为同业转贴现、再贴现和行内转贴现业务，导入批次后可以对票据进行剔除。
		 *  上述三种场景都需要对批次信息中的数量、利息和金额等信息进行实时计算更新；
		 *  如果在实际业务场景中，那么要对业务要素进行更新。
		 *  update by MQ 2013-08-19
		 *  
		 *  注意，以最新注释为准
		 *  目前修改为，票据明细维护批次信息，并不去更新业务信息（优点，操作集中在针对批次，不用再区分不同场景）
		 *  即：1、批次中操作，需要去维护4个表（批次表、关系表、票据表、收益表）
		 *     2、业务引用票据明细后，在主页面实时查询批次中的票据信息（包含票据总金额、利息、实付金额、票据数量等），在最后保存与提交流程时保存到业务表中
		 *  add by zhaozq 2013-09-07
		 *  
		 *  实现票据明细可批量剔除  2014-09-26 唐顺岩
		 * */
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String porderno = null;
			//票据明细可批量剔除   2014-09-26  唐顺岩
			String batchno = (String)context.getDataValue("batch_no");
			if(batchno == null || batchno.length() == 0) {
				throw new EMPJDBCException("The value of pk["+batchno+"] cannot be null!");
			}
			
			IndexedCollection icoll = (IndexedCollection)context.getDataElement("IqpBillDetailList");
			for (Iterator iterator = icoll.iterator(); iterator.hasNext();) {
				KeyedCollection kcoll = (KeyedCollection) iterator.next();
				porderno = (String)kcoll.getDataValue("porder_no");
			 
	//			porderno = (String)context.getDataValue("porder_no");
	//			if(porderno == null || porderno.length() == 0) {
	//				throw new EMPJDBCException("The value of pk["+porderno+"] cannot be null!");
	//			}
				
				//1、删除批次与票据关系表记录。
				Map<String,String> param = new HashMap<String,String>();
				param.put("batch_no", batchno);
				param.put("porder_no", porderno);
				int count=dao.deleteByPks(modelId, param, connection);
				if(count!=1){
					throw new EMPException("Remove Failed! Records :"+count);
				}
				
				//2、删除该张票据在该批次下的利息记录。
				int flag=dao.deleteByPks(incomeModel, param, connection);
				if(flag!=1 && flag!=0){
					throw new EMPException("Remove Failed! Records :"+flag);
				}
				
				/**3、 通过批次号码遍历批次下所有票据信息，统计得出批次值 */
				KeyedCollection baKColl = new KeyedCollection(batModel);
				baKColl.addDataField("batch_no", batchno);
				int billNum = 0;//票据数量
				double billAmt = 0;//票据总金额
				double intAmt = 0;//票据利息
				double rbuyAmt = 0;//回购利息
				IndexedCollection reIColl = dao.queryList(modelId, " where batch_no='"+batchno+"'", connection);
				if(reIColl != null && reIColl.size() > 0){
					/** 封装需要查询的票据信息SQL */
					String porderSQLHelp = " where porder_no in (";
					for(int i=0;i<reIColl.size();i++){
						KeyedCollection kc = (KeyedCollection)reIColl.get(i);
						String porderNo = (String)kc.getDataValue("porder_no");
						porderSQLHelp = porderSQLHelp+"'"+porderNo+"',";
					}
					porderSQLHelp = porderSQLHelp.substring(0, porderSQLHelp.length()-1)+") ";
	
					/** 计算票据总金额 */
					IndexedCollection biIColl = dao.queryList("IqpBillDetail", porderSQLHelp, connection);
					if(biIColl != null && biIColl.size() > 0){
						billNum = biIColl.size();
						for(int i=0;i<biIColl.size();i++){
							KeyedCollection kc = (KeyedCollection)biIColl.get(i);
							String amt = (String)kc.getDataValue("drft_amt");
							billAmt += Double.parseDouble(amt);
						}
						baKColl.addDataField("bill_qnt", billNum);//票据数量
						baKColl.addDataField("bill_total_amt", billAmt);//票据总金额
					}
					
					/** 计算票据利息,回购利息 */
					String incomestr = porderSQLHelp + " and batch_no = '"+batchno+"'";
					IndexedCollection inIColl = dao.queryList("IqpBillIncome", incomestr, connection);
					if(inIColl != null && inIColl.size() > 0){
						for(int i=0;i<inIColl.size();i++){
							KeyedCollection kc = (KeyedCollection)inIColl.get(i);
							String amt = (String)kc.getDataValue("int");
							String ramt = (String)kc.getDataValue("rebuy_int");
							if(ramt == null){
								ramt = "0";
							}
							intAmt += Double.parseDouble(amt);
							rbuyAmt += Double.parseDouble(ramt);
						}
						baKColl.addDataField("int_amt", intAmt);//票据利息
						baKColl.addDataField("rebuy_int", rbuyAmt);//回购利息
					}
					baKColl.addDataField("rpay_amt", billAmt-intAmt);//实付金额=票面金额-票据利息（不计算回购利息）
				}else {
					/** 批次关联表中不存在关联记录，则默认赋值为0 
					 * 如果剔除后该批次下不存在关联票据，那么批次中的数量、金额和利息等信息要归零。
					 * add by MQ  2013-08-16
					 * */
					baKColl.addDataField("bill_qnt", 0);//票据数量
					baKColl.addDataField("bill_total_amt", 0);//票据总金额
					baKColl.addDataField("int_amt", 0);//票据利息
					baKColl.addDataField("rebuy_int", 0);//回购利息
					baKColl.addDataField("rpay_amt", 0);
				}
				int count1 = dao.update(baKColl, connection);
				
				
				/**
				 * 4、如果该票据所属批次已经关联业务申请信息，那么剔除票据后，还要更新业务申请信息。
				 *    关联的业务有：直贴、转贴现（包含再贴现和行内转贴现）
				 */
				//首先判断所属那种业务
				KeyedCollection batchKcoll = dao.queryAllDetail(batModel, batchno, connection);
				String batch_serno = (String)batchKcoll.getDataValue("serno");//取批次中的业务编号
				IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
	            .getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
				if(!"".equals(batch_serno) && batch_serno != null){
					KeyedCollection ywKcoll = dao.queryAllDetail(loanModel, batch_serno, connection);
					String sernoOfyw = (String)ywKcoll.getDataValue("serno");
					if("".equals(sernoOfyw) || sernoOfyw == null){//转贴现
						ywKcoll = dao.queryAllDetail(rpddsntModel, batch_serno, connection);
						ywKcoll.put("bill_qnt", billNum);//票据数量
						ywKcoll.put("bill_total_amt", billAmt);//票据总金额
						ywKcoll.put("rpddscnt_int", intAmt);//总贴现利息
						ywKcoll.put("rpay_amt", billAmt-intAmt-rbuyAmt);//总实付金额
						ywKcoll.put("rebuy_int", rbuyAmt);//总回购利息
						ywKcoll.put("rebuy_amt", billAmt-intAmt-rbuyAmt);//总回购金额
						dao.update(ywKcoll, connection);
						iqpLoanAppComponent.deleteIqpLmtRel("IqpRpddscnt",batch_serno, porderno, dao);
					}else{//直贴业务
						//更新申请主表信息
						KeyedCollection loanKcoll = dao.queryAllDetail(loanModel, sernoOfyw, connection);
						loanKcoll.put("apply_amount", billAmt);//申请金额
						/**计算敞口金额*/
						BigDecimal apply_amount = BigDecimalUtil.replaceNull(billAmt);
						//获取实时汇率  start
						String cur_type = (String) loanKcoll.getDataValue("apply_cur_type");
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
						KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
						if("failed".equals(kCollRate.getDataValue("flag"))){
							throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
						}
						BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
						//获取实时汇率  end
						BigDecimal security_rate = BigDecimalUtil.replaceNull(loanKcoll.getDataValue("security_rate")); //保证金比例
						BigDecimal same_security_amt = BigDecimalUtil.replaceNull(loanKcoll.getDataValue("same_security_amt"));//视同保证金
						BigDecimal risk_open_amt = new BigDecimal(0);
						risk_open_amt = ((apply_amount.multiply(new BigDecimal(1).subtract(security_rate))).multiply(exchange_rate)).subtract(same_security_amt);
						loanKcoll.put("risk_open_amt", risk_open_amt);
						dao.update(loanKcoll, connection);
						//更新贴现从表信息
						KeyedCollection discCkoll = dao.queryAllDetail(discAppModel, sernoOfyw, connection);
						discCkoll.put("disc_date", batchKcoll.getDataValue("fore_disc_date"));//贴现日期
						discCkoll.put("bill_qty", batchKcoll.getDataValue("bill_qnt"));//票据数量
						discCkoll.put("disc_rate", batchKcoll.getDataValue("int_amt"));//贴现利息
						discCkoll.put("net_pay_amt", batchKcoll.getDataValue("rpay_amt"));//实付总金额
						dao.update(discCkoll, connection);
						iqpLoanAppComponent.deleteIqpLmtRel("IqpLoanApp",sernoOfyw, porderno, dao);
					}
				}
				
				/**删除收益信息下的付息信息*/
				String condition = " where batch_no = '"+batchno+"' and porder_no='"+porderno+"'";
				IndexedCollection pIntIColl = dao.queryList(pIntModel, null, condition, connection);
				for(int i=0;i<pIntIColl.size();i++){
					KeyedCollection pIntKColl = (KeyedCollection) pIntIColl.get(i);
					String pk = (String) pIntKColl.getDataValue("pk");
					dao.deleteByPk(pIntModel, pk, connection);
				}
				
				/**判断被剔除的票是否为存量的票据（被用过），如果不是则删除票据明细表*/
				String condition4detail = " where batch_no in (select batch_no from iqp_batch_bill_rel where porder_no='"+porderno+"') and status = '03' ";
				IndexedCollection detailIColl = dao.queryList(batModel, null, condition4detail, connection);
				if(detailIColl==null||detailIColl.size()==0){
					dao.deleteByPk("IqpBillDetail", porderno, connection);
				}
			}
			context.put("flag", "success");
			context.put("msg", "success");
		}catch (EMPException ee) {
			context.put("flag", "failed");
			context.put("msg", "失败，原因:"+ee.getCause().getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
