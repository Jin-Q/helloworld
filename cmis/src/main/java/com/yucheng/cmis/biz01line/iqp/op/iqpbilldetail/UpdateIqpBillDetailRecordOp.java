package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.hp.hpl.sparta.ParseLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class UpdateIqpBillDetailRecordOp extends CMISOperation {
	private final String modelId = "IqpBillDetail";
	private final String batModel = "IqpBatchMng";
	private final String relModel = "IqpBatchBillRel";
	private final String inModel = "IqpBillIncome";
	private final String pIntModel = "IqpBillPintDetail";
	private final String loanModel = "IqpLoanApp"; //业务申请表模型
	private final String rpddsntModel = "IqpRpddscnt"; //转贴现申请表模型
	private final String discAppModel = "IqpDiscApp";//贴现申请从表模型
	
	/** 
	 * 注意，以最新注释为准
	 * 目前修改为，票据明细维护批次信息，并不去更新业务信息（优点，操作集中在针对批次，不用再区分不同场景）
	 * 即：1、批次中操作，需要去维护4个表（批次表、关系表、票据表、收益表）
	 *     2、业务引用票据明细后，在主页面实时查询批次中的票据信息（包含票据总金额、利息、实付金额、票据数量等），在最后保存与提交流程时保存到业务表中
	 * add by zhaozq 2013-09-07
	 * */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			
			/** 更新票据信息时，同步计算出当前批次下所存在的票据信息，以及更新票据批次信息 */
			String porderno = (String)kColl.getDataValue("porder_no");
			
			/** 获取批次号码 */
			String batchno = (String) context.getDataValue("batch_no");
			
			/** 通过批次号获取批次详细信息  */
			KeyedCollection batchHelp = dao.queryDetail(batModel, batchno, connection);
			String billType = (String)batchHelp.getDataValue("bill_type");//票据种类
			String bizType = (String)batchHelp.getDataValue("biz_type");//业务种类
			String batchSerno = (String)batchHelp.getDataValue("serno");//批次关联的业务编号
			
			/** 更新票据收益表相关信息，由于修改页面只有票据金额以及票据到期日会对利息计算产生影响 */
			Map<String,String> incomeMap = new HashMap<String,String>();
			incomeMap.put("batch_no",batchno);
			incomeMap.put("porder_no",porderno);
			KeyedCollection inKColl = dao.queryDetail(inModel, incomeMap, connection);
			if(inKColl != null && inKColl.size() > 0){
				String pordernoHelp = (String)inKColl.getDataValue("porder_no");
				if(pordernoHelp != null && pordernoHelp.length() > 0){
					/** 重新计算票据收益表,需要通过业务类型判断 */
					String drftAmt = (String)kColl.getDataValue("drft_amt");//票面金额
					String endDate = (String)kColl.getDataValue("porder_end_date");//汇票到期日
					String porder_addr = (String)kColl.getDataValue("porder_addr");//汇票签发地
					String discDate = (String)inKColl.getDataValue("fore_disc_date");//贴现日期
					String adjDays = (String)inKColl.getDataValue("adj_days");//调整天数
					String discRate = (String)inKColl.getDataValue("disc_rate");//贴现利率
					String rebuyDate = (String)inKColl.getDataValue("fore_rebuy_date");//回购日期
					if("2".equals(porder_addr)){//1:本地/2:异地
						adjDays = "3";//调整天数
					}else{
						adjDays = "0";//调整天数
					}
					inKColl.put("adj_days", adjDays);
					if(discRate == null){
						discRate = "0";
					}
					//计算下一个工作日
					endDate = cmisComponent.getNextWorkDate(endDate, dataSource);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date discDateHelp = sdf.parse(discDate);
					Date endDateHelp = sdf.parse(endDate);
				
					/** 计算贴现天数、贴现利息 */
					if("04".equals(bizType)||"02".equals(bizType)){//卖出回购、买入返售
						//计算下一个工作日
						rebuyDate = cmisComponent.getNextWorkDate(rebuyDate, dataSource);
						Date rebuyDateHelp = sdf.parse(rebuyDate);
						String rebuy_rate = (String)batchHelp.getDataValue("rebuy_rate");//回购利率
						/**计算票据利息*/
						long discNum = (rebuyDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						/* modified by yangzy 2015/03/19 票据信息利息计算改造四舍五入 start */
						BigDecimal discInt = new BigDecimal(drftAmt).multiply(new BigDecimal(rebuy_rate)).multiply(new BigDecimal(discNum).add(new BigDecimal(adjDays))).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_UP);
						/* modified by yangzy 2015/03/19 票据信息利息计算改造四舍五入 end */
						inKColl.setDataValue("disc_days", String.valueOf(discNum));
						inKColl.setDataValue("int", discInt);
						
						/** 计算回购天数、回购利息 */
						if(rebuyDate == null || rebuyDate == ""){
							inKColl.setDataValue("rebuy_days", "0");
							inKColl.setDataValue("rebuy_int", "0");
						}else {
							//回购天数=到期日-预计回购日期
							long rebuyNum = (rebuyDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
							inKColl.setDataValue("rebuy_days", String.valueOf(rebuyNum));
							inKColl.setDataValue("rebuy_int", discInt);
						}
					}else{
						long discNum = (endDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						/* modified by yangzy 2015/03/19 票据信息利息计算改造四舍五入 start */
						BigDecimal discInt = new BigDecimal(drftAmt).multiply(new BigDecimal(discRate)).multiply(new BigDecimal(discNum).add(new BigDecimal(adjDays))).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_UP);
						/* modified by yangzy 2015/03/19 票据信息利息计算改造四舍五入 end */
						inKColl.setDataValue("disc_days", String.valueOf(discNum));
						inKColl.setDataValue("int", discInt);
						inKColl.setDataValue("rebuy_int", 0);
					}
					inKColl.setDataValue("drft_amt", drftAmt);
					dao.update(inKColl, connection);
				}
			}
			
			
			/** 通过批次号码遍历批次下所有票据信息，统计得出批次值 */
			KeyedCollection baKColl = new KeyedCollection(batModel);
			baKColl.addDataField("batch_no", batchno);
			int billNum = 0;
			double intAmt = 0;
			double rbuyAmt = 0;
			double billAmt = 0;
			IndexedCollection reIColl = dao.queryList(relModel, " where batch_no='"+batchno+"'", connection);
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
				IndexedCollection biIColl = dao.queryList(modelId, porderSQLHelp, connection);
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
				String conditionStr = porderSQLHelp + " and batch_no = '"+batchno+"'";
				IndexedCollection inIColl = dao.queryList("IqpBillIncome", conditionStr, connection);
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
				/** 批次关联表中不存在关联记录，则默认赋值为0 */
			}
			int count1 = dao.update(baKColl, connection);
			
			/**总利息重新计算应重新分配付息信息*/
			BigDecimal int_amt = BigDecimalUtil.replaceNull(inKColl.getDataValue("int")) ;//当前票据的利息信息
			String condition = " where batch_no = '"+batchno+"' and porder_no='"+porderno+"'";
			IndexedCollection pIntIColl = dao.queryList(pIntModel, null, condition, connection);
			for(int i=0;i<pIntIColl.size();i++){
				KeyedCollection pIntKColl = (KeyedCollection) pIntIColl.get(i);
				BigDecimal pint_perc = new BigDecimal(pIntKColl.getDataValue("pint_perc")+"");
				BigDecimal pint_amt = int_amt.multiply(pint_perc);
				pIntKColl.setDataValue("pint_amt", pint_amt);
				dao.update(pIntKColl, connection);
			}
			
			//首先判断所属那种业务
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
            .getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			if(!"".equals(batchSerno) && batchSerno != null){
				KeyedCollection ywKcoll = dao.queryAllDetail(loanModel, batchSerno, connection);
				String sernoOfyw = (String)ywKcoll.getDataValue("serno");
				if("".equals(sernoOfyw) || sernoOfyw == null){//转贴现
					ywKcoll = dao.queryAllDetail(rpddsntModel, batchSerno, connection);
					ywKcoll.put("bill_qnt", billNum);//票据数量
					ywKcoll.put("bill_total_amt", billAmt);//票据总金额
					ywKcoll.put("rpddscnt_int", intAmt);//总贴现利息
					ywKcoll.put("rpay_amt", billAmt-intAmt-rbuyAmt);//总实付金额
					ywKcoll.put("rebuy_int", rbuyAmt);//总回购利息
					ywKcoll.put("rebuy_amt", billAmt-intAmt-rbuyAmt);//总回购金额
					dao.update(ywKcoll, connection);
					iqpLoanAppComponent.updateIqpLmtRel("IqpRpddscnt",batchSerno, null, dao);
				}else{//业务申请,包含银行承兑汇票贴现和商业承兑汇票贴现两个产品。
					//更新申请主表信息
					KeyedCollection loanKcoll = dao.queryAllDetail(loanModel, batchSerno, connection);
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
					KeyedCollection batchKcoll = dao.queryAllDetail(batModel, batchno, connection);
					KeyedCollection discCkoll = dao.queryAllDetail(discAppModel, batchSerno, connection);
					discCkoll.put("disc_date", batchKcoll.getDataValue("fore_disc_date"));//贴现日期
					discCkoll.put("bill_qty", batchKcoll.getDataValue("bill_qnt"));//票据数量
					discCkoll.put("disc_rate", batchKcoll.getDataValue("int_amt"));//贴现利息
					discCkoll.put("net_pay_amt", batchKcoll.getDataValue("rpay_amt"));//实付总金额
					dao.update(discCkoll, connection);
					iqpLoanAppComponent.updateIqpLmtRel("IqpLoanApp",batchSerno, null, dao);
				}
			}
			context.put("flag", "success");
			context.put("msg", "success");
		}catch (EMPException ee) {
			context.put("flag", "failed");
			context.put("msg", "修改失败:"+ee.getCause().getMessage());
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
