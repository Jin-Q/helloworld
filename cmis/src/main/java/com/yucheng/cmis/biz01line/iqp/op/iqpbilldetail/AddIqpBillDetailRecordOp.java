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
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class AddIqpBillDetailRecordOp extends CMISOperation {
	private final String batModel = "IqpBatchMng";
	private final String relModel = "IqpBatchBillRel";
	private final String modelId = "IqpBillDetail";
	private final String inModel = "IqpBillIncome";
	private final String loanModel = "IqpLoanApp"; //业务申请表模型
	private final String rpddsntModel = "IqpRpddscnt"; //转贴现申请表模型
	private final String discAppModel = "IqpDiscApp";//贴现申请从表模型
	
	/** 新增操作执行两部，第一步新增关联表信息，第二步插入票据信息 
	 * 补充：如果业务类型非直贴，那么新增票据后，后台要自动计算利息并且生成记录。
	 *   add by MQ 2013-08-16
	 *   
	 * 票据新增发生的场景如下：
	 * 1、在票据批次管理模块中，在批次下可以单笔新增票据。
	 * 2、在业务申请模块中，导入批次后，可以单笔新增票据。
	 * 3、在转贴现申请模块中，导入批次后，可以单笔新增票据。
	 * add by MQ 2013-08-19
	 * 
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
			String porderno = "";
			String batchno = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				batchno = (String)context.getDataValue("batch_no");//批次号
				porderno = (String)kColl.getDataValue("porder_no");//汇票号码
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//0、首先校验新增票据的票据种类是否与批次的票据种类一致。
			KeyedCollection batchKColl = dao.queryAllDetail(batModel, batchno, connection);
			String bill_type = (String)batchKColl.getDataValue("bill_type");//批次票据种类
			String billTypeOfporder = (String)kColl.getDataValue("bill_type");//票据种类
			String porder_addr = (String)kColl.getDataValue("porder_addr");//汇票签发地
			if(!billTypeOfporder.equals(bill_type)){
				context.addDataField("flag", "failed");
				context.addDataField("msg", "票据种类必须同批次的票据种类一致！");
				context.addDataField("porderno", "");
				return "0";
			}
			
			//1、插入批次与票据关系表
			Map<String,String> param = new HashMap();
			param.put("batchno", batchno);
			param.put("porderno", porderno);
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			int count = cmisComponent.insertIqpBatchBillRel(param);
		    
			//2、判断票据是否已经在票据明细表中存在，如果不存在，生成票据明细记录。如果存在，
			KeyedCollection detKColl = dao.queryAllDetail(modelId, porderno, connection);
			if(detKColl.getDataValue("porder_no") != null && ((String)detKColl.getDataValue("porder_no")).length() > 0){
				//如果为已存在票据，对于票据状态为【核销】的，需重新更新为登记
				String status = (String) detKColl.getDataValue("status");
				if(status.equals("04")){
					kColl.setDataValue("status", "01");
				}
				dao.update(kColl, connection);
			}else {
				dao.insert(kColl, connection);
			}
			
			//3、根据批次业务类型判断后台是否应自动进行利息计算并保存记录。
			
			String bizType = (String)batchKColl.getDataValue("biz_type");//业务类型
			if(!"07".equals(bizType)){//非直贴
				//查询票据的票面金额，到期日期信息。
				Map<String,String> incomeMap = new HashMap<String,String>();
				incomeMap.put("batch_no",batchno);
				incomeMap.put("porder_no",porderno);
				KeyedCollection inColl = dao.queryDetail(inModel, incomeMap, connection);
				String endDate = "";
				String drftAmt = "";
				if(detKColl != null && detKColl.size() > 0){
					drftAmt = (String)kColl.getDataValue("drft_amt");//票面金额
					endDate = (String)kColl.getDataValue("porder_end_date");//到期日期
					String fore_disc_date = (String)batchKColl.getDataValue("fore_disc_date");//预计转/贴现日期
					String disc_rate = (String)batchKColl.getDataValue("rate");//  转/再贴现利率
					String rebuy_date = (String)batchKColl.getDataValue("rebuy_date");//回购日期
					
					inColl.setDataValue("biz_type", bizType);//业务类型
					inColl.setDataValue("batch_no", batchno);//批次号
					inColl.setDataValue("porder_no", porderno);//汇票号码
					inColl.setDataValue("fore_disc_date", fore_disc_date);// 转贴现/贴现日期
					inColl.setDataValue("drft_amt", drftAmt);
					inColl.setDataValue("disc_rate", disc_rate);//转/贴现利率
					
					//计算下一个工作日
					endDate = cmisComponent.getNextWorkDate(endDate, dataSource);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date discDateHelp = sdf.parse(fore_disc_date);
					String adjDays = "";
					if("2".equals(porder_addr)){//1:本地/2:异地
						adjDays = "3";//调整天数
					}else{
						adjDays = "0";//调整天数
					}
					if("04".equals(bizType)||"02".equals(bizType)){//卖出回购、买入返售
						inColl.setDataValue("adj_days", adjDays);//调整天数
						String rebuy_rate = (String)batchKColl.getDataValue("rebuy_rate");//  回购利率
						//计算下一个工作日
						rebuy_date = cmisComponent.getNextWorkDate(rebuy_date, dataSource);
						Date rebuyDateHelp = sdf.parse(rebuy_date);
						long discDays = (rebuyDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						if(discDays <= 0){
							discDays = 0;
						}
						inColl.setDataValue("disc_days", discDays);//贴现天数
						BigDecimal intVal = new BigDecimal(drftAmt).multiply(new BigDecimal(rebuy_rate)).multiply(new BigDecimal(discDays).add(new BigDecimal(adjDays))).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_EVEN);
						inColl.setDataValue("int", intVal);//贴现利息
						
						//回购天数=到期日-回购日期
						
						long rebuyNum = (rebuyDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						if(rebuyNum <= 0){
							rebuyNum = 0;
						}
						inColl.setDataValue("fore_rebuy_date", rebuy_date);//预计回购日期 
						inColl.setDataValue("rebuy_days", rebuyNum);//回购天数
						inColl.setDataValue("disc_days", rebuyNum);
						inColl.setDataValue("rebuy_int", intVal);//回购利息
						
						if("04".equals(bizType)){
							String condition = "where porder_no='"+porderno+"' and biz_type='02'";
							IndexedCollection iColl4Date = dao.queryList(inModel, condition, connection);
							String fore_rebuy_date = "";
							for(int i=0;i<iColl4Date.size();i++){
								KeyedCollection kColl4Date = (KeyedCollection)iColl4Date.get(i);
								fore_rebuy_date = (String)kColl4Date.getDataValue("fore_rebuy_date");
								if(DateUtils.isBigBetweenDate(rebuy_date,fore_rebuy_date)){
									context.addDataField("flag", "failed");
									context.addDataField("msg", "该票据买入返售的到期日为："+fore_rebuy_date+",回购到期日不能超过该日期!");
									context.addDataField("porderno", "");
									connection.rollback();
									return "0";
								}
							}
						}
					}else{
						Date endDateHelp = sdf.parse(endDate);
						long discDays = (endDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						if(discDays <= 0){
							discDays = 0;
						}
						inColl.setDataValue("disc_days", discDays);//贴现天数
						inColl.setDataValue("adj_days", adjDays);//调整天数
		                BigDecimal intVal = new BigDecimal(drftAmt).multiply(new BigDecimal(disc_rate)).multiply(new BigDecimal(discDays).add(new BigDecimal(adjDays))).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_EVEN);
						inColl.setDataValue("int", intVal);//贴现利息
						inColl.setDataValue("rebuy_int", 0);//回购利息
					}
					int flag = dao.insert(inColl, connection);
					if(flag!=1){
						throw new EMPException("插入利息计算失败！！");
					}
				}
			}
			
			/** -------------更新批次信息-------------- */
			/**4、 通过批次号码遍历批次下所有票据信息，统计得出批次值 */
			KeyedCollection baKColl = new KeyedCollection(batModel);
			baKColl.addDataField("batch_no", batchno);
			int billNum = 0;
			double billAmt = 0;
			double intAmt = 0;
			double rbuyAmt = 0;
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
				/** 计算实付金额*/
				baKColl.addDataField("rpay_amt", billAmt-intAmt);//实付金额=票面金额-票据利息（不计算回购利息）
			}else {
				/** 批次关联表中不存在关联记录，则默认赋值为0 */
			}
			int count1 = dao.update(baKColl, connection);
			
			/**
			 * 4、检查该批次是否已关联业务，如果已关联，那么要更新申请信息。
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
					iqpLoanAppComponent.updateIqpLmtRel("IqpRpddscnt",batch_serno, null, dao);
				}else{//业务申请,包含银行承兑汇票贴现和商业承兑汇票贴现两个产品。
					//更新申请主表信息
					KeyedCollection loanKcoll = dao.queryAllDetail(loanModel, batch_serno, connection);
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
					KeyedCollection discCkoll = dao.queryAllDetail(discAppModel, batch_serno, connection);
					discCkoll.put("disc_date", batchKcoll.getDataValue("fore_disc_date"));//贴现日期
					discCkoll.put("bill_qty", billNum);//票据数量
					discCkoll.put("disc_rate", intAmt);//贴现利息
					discCkoll.put("net_pay_amt", billAmt-intAmt-rbuyAmt);//实付总金额
					dao.update(discCkoll, connection);
					
					iqpLoanAppComponent.updateIqpLmtRel("IqpLoanApp",batch_serno, null, dao);
				}
			}
			context.addDataField("flag", "success");
			context.addDataField("msg", "新增成功！");
			context.addDataField("porderno", porderno);
		}catch (Exception ee) {
			context.addDataField("flag", "failed");
			context.addDataField("msg", "新增失败"+ee.getMessage());
			context.addDataField("porderno", "");
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
