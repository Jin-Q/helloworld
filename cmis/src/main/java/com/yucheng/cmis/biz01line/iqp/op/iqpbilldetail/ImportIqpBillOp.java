package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.operation.CMISOperation;

public class ImportIqpBillOp extends CMISOperation {
	private final String batModel = "IqpBatchMng"; //批次表模型
    private final String billModel = "IqpBillDetail"; //票据表模型
	private static final String modelId = "IqpBatchBillRel"; //批次票据关系表模型
	private final String inModel = "IqpBillIncome"; //利息收益表模型
	private final String loanModel = "IqpLoanApp"; //业务申请表模型
	private final String rpddsntModel = "IqpRpddscnt"; //转贴现申请表模型
	private final String discAppModel = "IqpDiscApp";//贴现申请从表模型
	
	@Override
	public String doExecute(Context context) throws EMPException {
		/**
         * 票据引入在系统中有多种场景：
		 * 1、在票据批次管理模块中，可以单笔导入已存在的票据。
		 * 2、在业务申请模块中，在导入批次后，可以单笔导入已存在的票据。
		 * 3、在转贴现申请模块中，在导入批次后，可以单笔导入已存在的票据。
		 * 
		 * 票据导入处理业务逻辑：
		 * 1、首先校验该票据是否已在有效状态的批次中，一张票据不能同时关联在
		 *    两个批次中；
		 *    校验票据的票据种类（银票/商票）是否同批次的票据种类一致；
		 *    校验票据的期限是否能够包含批次的贴现/转贴现日期和回购日期。
		 *    校验票据的状态不能为托收在途（03）、核销（04）和质押（05）状态。
		 * 2、导入后，建立批次票据关联关系，并且生成利息记录，计算该票据的交易利息：   
		 *    如果不为直贴，后台根据批次中的转贴现利率、转贴现日期/回购日期计算每张票的利息。
		 *    如果是直贴，由于每张票的贴现利率都是独立定价，所以利息计算要在
		 *    利息计算页面维护贴现利率和调整天数后才计算。
		 * 3、计算批次下票据的总金额、利息、数量和实付金额信息，更新批次。 
		 * 4、如果导入操作场景为2、3，那么还需要更新业务申请中的相关信息，如金额等。
		 * update by MQ 2013-08-29
		 * 
		 * 注意，以最新注释为准
		 * 目前修改为，票据明细维护批次信息，并不去更新业务信息（优点，操作集中在针对批次，不用再区分不同场景）
		 * 即：1、批次中操作，需要去维护4个表（批次表、关系表、票据表、收益表）
		 *     2、业务引用票据明细后，在主页面实时查询批次中的票据信息（包含票据总金额、利息、实付金额、票据数量等），在最后保存与提交流程时保存到业务表中
		 * add by zhaozq 2013-09-07
		 */
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String batchno = (String)context.getDataValue("batch_no");//批次号
			String porderno = (String)context.getDataValue("porderno");//汇票号码
			String serno = (String)context.getDataValue("serno");
			if((batchno == null || batchno =="") && serno!= ""){//批次号为空，可以通过流水号去批次包中取批次号。
				//此种业务场景为，业务办理时导入票据。
				IndexedCollection batIColl = dao.queryList(batModel, " where serno='"+serno+"'", connection);
				if(batIColl != null && batIColl.size() > 0){
					KeyedCollection kc = (KeyedCollection)batIColl.get(0);
					batchno = (String)kc.getDataValue("batch_no");
					if(batchno == null){
						batchno = "";
					}
				}
			}
			
			//通过批次号获取批次信息
			KeyedCollection batchKcoll = dao.queryAllDetail(batModel, batchno, connection);
			String batchBillType = (String)batchKcoll.getDataValue("bill_type");//批次中的票据种类
            String biz_type = (String)batchKcoll.getDataValue("biz_type");//业务类型
			String fore_disc_date = (String)batchKcoll.getDataValue("fore_disc_date");// 预计贴现/转贴现日期
            String rebuy_date = (String)batchKcoll.getDataValue("rebuy_date");// 回购日期
			String disc_rate = (String)batchKcoll.getDataValue("rate");//  转/再贴现利率
            
			//1、校验票据是否符合导入条件，包括票据种类、期限、状态。
			KeyedCollection porderKcoll = dao.queryAllDetail(billModel, porderno, connection);
			String billStatus = (String)porderKcoll.getDataValue("status");   //票据状态
			String billType = (String)porderKcoll.getDataValue("bill_type");  //票据种类
			String billStartDate = (String)porderKcoll.getDataValue("bill_isse_date"); //票据签发日
			String billEndDate = (String)porderKcoll.getDataValue("porder_end_date");//票据到期日
			if(!billType.equals(batchBillType)){//导入票据的票据种类和批次的票据种类不一致
				context.addDataField("flag", "failure");
				context.addDataField("msg", "导入票据的票据种类和批次的票据种类不一致，不能导入！");
				return null;
			}
			
			/**
			 * 检查票据状态，  1【登记】、【核销】状态才能做直贴、买入返售、买入买断
			 *               2【持有】才能做再贴现、内部转贴现、卖出回购、卖出卖断
			 */
			if(biz_type.equals("07")||biz_type.equals("02")||biz_type.equals("01")){
				if(!"01".equals(billStatus) && !"04".equals(billStatus)){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "【登记】、【核销】状态才能做直贴、买入返售、买入买断！");
					return null;
				}
			}else if(biz_type.equals("03")||biz_type.equals("04")||biz_type.equals("05")||biz_type.equals("06")){
				if(!"02".equals(billStatus)){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "【持有】状态才能做再贴现、内部转贴现、卖出回购、卖出卖断！");
					return null;
				}
			}
			
			
			if(!"04".equals(biz_type)&&!"02".equals(biz_type)){//非卖出回购交易，校验票据期限能否包含 预计贴现/转贴现日期。
				if(!DateUtils.isBetweenStartDateAndEndDate(billStartDate, billEndDate, fore_disc_date)){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "批次中的预计转/贴现日期【"+fore_disc_date+
							             "】与票据期限【"+billStartDate+","+billEndDate+"】冲突，不能导入！");
					return null;
			   	}
			}else{//卖出回购交易，校验票据期限能否包含 转贴现日期和回购日期。
				if(!DateUtils.isBetweenStartDateAndEndDate(billStartDate, billEndDate, fore_disc_date)){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "批次中的预计转/贴现日期【"+fore_disc_date+
				             "】与票据期限【"+billStartDate+","+billEndDate+"】冲突，不能导入！");
					return null;
			   	}
				if(!DateUtils.isBetweenStartDateAndEndDate(billStartDate, billEndDate, rebuy_date)){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "批次中的回购日期【"+rebuy_date+
				             "】与票据期限【"+billStartDate+","+billEndDate+"】冲突，不能导入！");
					return null;
			   	}
			}
			
			
			//2、建立批次和票据关联关系。
			KeyedCollection kcRel = new KeyedCollection();
			kcRel.addDataField("batch_no", batchno);
			kcRel.addDataField("porder_no", porderno);
			kcRel.setName(modelId);
			
			dao.insert(kcRel, connection);
			
			/**3、 更新票据收益明细表中关联关系 */
			KeyedCollection inKColl = new KeyedCollection();
			inKColl.addDataField("batch_no", batchno);
			inKColl.addDataField("porder_no", porderno);
			inKColl.setName(inModel);
			dao.update(inKColl, connection);
			
			/**4、 根据业务类型判断是否应该主动计算利息并保存记录。*/
			if(!"07".equals(biz_type)){//除直贴外
				//查询票据的票面金额，到期日期信息。
				Map<String,String> incomeMap = new HashMap<String,String>();
				incomeMap.put("batch_no",batchno);
				incomeMap.put("porder_no",porderno);
				KeyedCollection kColl = dao.queryDetail(inModel, incomeMap, connection);
				KeyedCollection detKColl = dao.queryDetail("IqpBillDetail", porderno, connection);
				String endDate = "";
				String drftAmt = "";
				if(detKColl != null && detKColl.size() > 0){
					drftAmt = (String)detKColl.getDataValue("drft_amt");//票面金额
					endDate = (String)detKColl.getDataValue("porder_end_date");//到期日期
					
					
					kColl.setDataValue("biz_type", biz_type);//业务类型
					kColl.setDataValue("batch_no", batchno);//批次号
					kColl.setDataValue("porder_no", porderno);//汇票号码
					kColl.setDataValue("fore_disc_date", fore_disc_date);// 转贴现/贴现日期
					kColl.setDataValue("drft_amt", drftAmt);
					kColl.setDataValue("disc_rate", disc_rate);//转/贴现利率
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date discDateHelp = sdf.parse(fore_disc_date);
					if("04".equals(biz_type)||"02".equals(biz_type)){//卖出回购、买入返售
						Date endDateHelp = sdf.parse(endDate);
						long discDays = (endDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						if(discDays <= 0){
							discDays = 0;
						}
						kColl.setDataValue("disc_days", discDays);//贴现天数
						kColl.setDataValue("adj_days", 0);//调整天数
						double intVal = new BigDecimal(Double.parseDouble(drftAmt)*Double.parseDouble(disc_rate)*discDays/360)
		                  .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("int", intVal);//贴现利息
						
						//回购天数=预计回购日期-转贴现日期
						Date rebuyDateHelp = sdf.parse(rebuy_date);
						long rebuyNum = (rebuyDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						if(rebuyNum <= 0){
							rebuyNum = 0;
						}
						kColl.setDataValue("fore_rebuy_date", rebuy_date);//预计回购日期
						kColl.setDataValue("rebuy_days", rebuyNum);//回购天数
						double rebuyInt = new BigDecimal(Double.parseDouble(drftAmt)*Double.parseDouble(disc_rate)*rebuyNum/360)
						                  .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("rebuy_int", rebuyInt);//回购利息
					}else{
						Date endDateHelp = sdf.parse(endDate);
						long discDays = (endDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						if(discDays <= 0){
							discDays = 0;
						}
						kColl.setDataValue("disc_days", discDays);//贴现天数
						kColl.setDataValue("adj_days", 0);//调整天数
						double intVal = new BigDecimal(Double.parseDouble(drftAmt)*Double.parseDouble(disc_rate)*discDays/360)
		                  .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						kColl.setDataValue("int", intVal);//贴现利息
						kColl.setDataValue("rebuy_int", 0);//回购利息
					}
					int flag = dao.insert(kColl, connection);
					if(flag!=1){
						throw new EMPException("插入利息计算失败！！");
					}
				}
			}
			
			/** 5、通过批次号码遍历批次下所有票据信息，统计得出批次值 */
			KeyedCollection baKColl = new KeyedCollection(batModel);
			baKColl.addDataField("batch_no", batchno);
			int billNum = 0;
			double billAmt = 0;
			double intAmt = 0;
			double rbuyAmt = 0;
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
				/** 批次关联表中不存在关联记录，则默认赋值为0 */
				baKColl.addDataField("bill_qnt", 0);//票据数量
				baKColl.addDataField("bill_total_amt", 0);//票据总金额
				baKColl.addDataField("int_amt", 0);//票据利息
				baKColl.addDataField("rebuy_int", 0);//回购利息
				baKColl.addDataField("rpay_amt", 0);
			}
			dao.update(baKColl, connection);
			
//			/**
//			 * 6、检查该批次是否已关联业务，如果已关联，那么要更新申请信息。
//			 */
//			//首先判断所属那种业务
//			if(!"".equals(serno) && serno != null){
//				KeyedCollection ywKcoll = dao.queryAllDetail(loanModel, serno, connection);
//				String sernoOfyw = (String)ywKcoll.getDataValue("serno");
//				if("".equals(sernoOfyw) || sernoOfyw == null){//转贴现
//					ywKcoll = dao.queryAllDetail(rpddsntModel, serno, connection);
//					ywKcoll.setDataValue("bill_qnt", billNum);//票据数量
//					ywKcoll.setDataValue("bill_total_amt", billAmt);//票据总金额
//					ywKcoll.setDataValue("rpddscnt_int", intAmt);//总贴现利息
//					ywKcoll.setDataValue("rpay_amt", billAmt-intAmt-rbuyAmt);//总实付金额
//					ywKcoll.setDataValue("rebuy_int", rbuyAmt);//总回购利息
//					ywKcoll.setDataValue("rebuy_amt", billAmt-intAmt-rbuyAmt);//总回购金额
//					dao.update(ywKcoll, connection);
//				}else{//直贴业务
//					//更新贴现从表信息
//					KeyedCollection newBatchKcoll = dao.queryAllDetail(batModel, batchno, connection);
//					KeyedCollection discCkoll = dao.queryAllDetail(discAppModel, sernoOfyw, connection);
//					discCkoll.setDataValue("disc_date", newBatchKcoll.getDataValue("fore_disc_date"));//贴现日期
//					discCkoll.setDataValue("bill_qty", newBatchKcoll.getDataValue("bill_qnt"));//票据数量
//					discCkoll.setDataValue("disc_rate", newBatchKcoll.getDataValue("int_amt"));//贴现利息
//					discCkoll.setDataValue("net_pay_amt", newBatchKcoll.getDataValue("rpay_amt"));//实付总金额
//					dao.update(discCkoll, connection);
//					
//					//更新申请主表信息
//					KeyedCollection loanKcoll = dao.queryAllDetail(loanModel, sernoOfyw, connection);
//					loanKcoll.setDataValue("apply_amount", billAmt);//申请金额
//					dao.update(loanKcoll, connection);
//				}
//			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "导入失败！");
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
