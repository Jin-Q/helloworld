package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;
import com.yucheng.cmis.util.TableModelUtil;

public class ImportIqpBillDetailOp extends CMISOperation {
	
	 private final String modelId = "IqpBillDetail";
	 private final String modelId1 = "IqpBillIncome";
	 private final String relModel = "IqpBatchBillRel";
	 private final String batModel = "IqpBatchMng";
	 private final String loanModel = "IqpLoanApp"; //业务申请表模型
	 private final String rpddsntModel = "IqpRpddscnt"; //转贴现申请表模型
	 private final String discAppModel = "IqpDiscApp";//贴现申请从表模型
	@Override
	public String doExecute(Context context) throws EMPException {
		
		String tempFileName = "";
		String flagInfo = CMISMessage.DEFEAT;
		Connection connection =null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 //读取xls文件路径
            String tempPath =(String)context.getDataValue("DocBasicinfo__file_path");
            String tempName =(String)context.getDataValue("DocBasicinfo__file_name");
            String batch_no =(String)context.getDataValue("batch_no");
            String bill_type =(String)context.getDataValue("bill_type");
            String biz_type =(String)context.getDataValue("biz_type");
            ResourceBundle res = ResourceBundle.getBundle("cmis");
            String tempFileRootPath = res.getString("tempFileRootPath");
            if(tempFileRootPath.endsWith("/")){   //如果路径以/结束
            	tempFileName = tempFileRootPath + tempPath + tempName;
            }else{
            	tempFileName = tempFileRootPath +'/' + tempPath + tempName;
            }
            
            // 编码
            if (tempFileName != null) {
                try {
                    tempFileName = new String(tempFileName.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：]" + tempFileName + " 错误信息：" + e1.toString());
                    context.addDataField("errorInfo", "errorInfo");
                    return flagInfo;
                }
            } else {
                EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：]" + tempFileName);
            }
            
            // 读取文件
            ExcelVO evo = null;
            try {
			    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：" + tempFileName + "]");
			    evo = ExcelTreat.readExcel(tempFileName);
			}catch (Exception e) {
			    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：" + tempFileName + "]" + "错误信息：" + e.toString());
			    context.addDataField("errorInfo", "errorInfo");
			    throw new EMPException("导入文件失败，请检查文件是否正确及文件路径是否正确！"+e);
			}
			SheetVO[] sheets = evo.sheets;
			CellVO[][] cell = null;
			SheetVO sheet = null;
			String porder_no = "";          //汇票号码
			String drft_amt = "";           //票面金额 
			String bill_isse_date = "";     //票据签发日        
			String porder_end_date = "";    //汇票到期日
			String isse_name = "";          //出票/付款人名称 
			String daorg_acct = "";         //出票人开户行账号  
			String daorg_name = "";         //出票人开户行行名  
			String daorg_no = "";           //出票人开户行行号
			String pyee_name = "";          //收款人名称
			String paorg_acct_no = "";      //收款人开户行账号 
			String paorg_no = "";           //收款人开户行行号
			String paorg_name = "";         //收款人开户行行名
			String porder_addr = "";        //汇票签发地 
			String status = "";             //票据状态

			String fore_disc_date = "";	  //预计转/贴现日期
			String disc_rate = "";		  //转/贴现利率
			
			String daorg_tyep = "";		  //出票人开户行类型
			
			String aorg_type = "";        //承兑行类型
			String aorg_no = "";   		  //承兑行行号
			String aorg_name = "";        //承兑行名称
			
			String aaorg_type = "";        //承兑人开户行类型
			String accptr_cmon_code = "";   //承兑人组织机构代码
			String aaorg_acct_no = "";      //承兑人开户行账号
			String aaorg_no = "";         //承兑人开户行行号
			String aaorg_name = "";   	  //承兑人开户行名称
			
			
			String tcont_no = "";         //贸易合同编号
			String tcont_amt = "";        //贸易合同金额
			String is_ebill = "";         //是否电票
			String utakeover_sign = "";   //不得转让标记
			
			String msg = "";
			IndexedCollection msgIColl = new IndexedCollection("MsgList");
			KeyedCollection batchKColl = dao.queryDetail(batModel, batch_no, connection);
			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				String modelname = ((String)cell[0][15].cellvalue).trim();
				if("100".equals(bill_type)){//银票
					if(!"出票人开户行类型".equals(modelname)){
						msg = "导入文件失败，导入模板错误，请选择银票票据模板！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
				}else{
					if(!"承兑人开户行类型".equals(modelname)){
						msg = "导入文件失败，导入模板错误，请选择商票票据模板！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
				}
				for(int m =1; m< cell.length; m++){
					//需新增记录KC
					try{
						porder_no = ((String)cell[m][0].cellvalue).trim();
						drft_amt = ((String)cell[m][1].cellvalue).trim();
						bill_isse_date = ((String)cell[m][2].cellvalue).trim();
						porder_end_date = ((String)cell[m][3].cellvalue).trim();
						isse_name = ((String)cell[m][4].cellvalue).trim();
						daorg_acct = ((String)cell[m][5].cellvalue).trim();
						daorg_name = ((String)cell[m][6].cellvalue).trim();						
						daorg_no = ((String)cell[m][7].cellvalue).trim();
						pyee_name = ((String)cell[m][8].cellvalue).trim();
						paorg_acct_no = ((String)cell[m][9].cellvalue).trim();
						paorg_name = ((String)cell[m][10].cellvalue).trim();
						paorg_no = ((String)cell[m][11].cellvalue).trim();
						fore_disc_date = ((String)cell[m][12].cellvalue).trim();
						disc_rate = ((String)cell[m][13].cellvalue).trim();
						porder_addr = ((String)cell[m][14].cellvalue).trim();
					}catch (Exception e) {
					}
					if("100".equals(bill_type)){
						try{
							aorg_type = ((String)cell[m][16].cellvalue).trim();
							aorg_no = ((String)cell[m][17].cellvalue).trim();
							aorg_name = ((String)cell[m][18].cellvalue).trim();
							tcont_no = ((String)cell[m][19].cellvalue).trim();
							tcont_amt = ((String)cell[m][20].cellvalue).trim();
							is_ebill = ((String)cell[m][21].cellvalue).trim();
							utakeover_sign = ((String)cell[m][22].cellvalue).trim();
						}catch (Exception e) {
						}

						if("".equals(aorg_type)){
							aorg_type = "";
						}else{
							aorg_type = aorg_type.substring(0,2);
						}
					}else{
						try{
							aaorg_type = ((String)cell[m][15].cellvalue).trim();
							accptr_cmon_code = ((String)cell[m][16].cellvalue).trim();
							aaorg_acct_no = ((String)cell[m][17].cellvalue).trim();
							aaorg_no = ((String)cell[m][18].cellvalue).trim();
							aaorg_name = ((String)cell[m][19].cellvalue).trim();
							tcont_no = ((String)cell[m][20].cellvalue).trim();
							tcont_amt = ((String)cell[m][21].cellvalue).trim();
							is_ebill = ((String)cell[m][22].cellvalue).trim();
							utakeover_sign = ((String)cell[m][23].cellvalue).trim();
						}catch (Exception e) {
						}
						if("".equals(aaorg_type)){
							aaorg_type = "";
						}else{
							aaorg_type = aaorg_type.substring(0,2);
						}
					}
					
					KeyedCollection kColl = new KeyedCollection();
					
					if("".equals(porder_addr)){
						porder_addr = "";
					}else{
						porder_addr = porder_addr.substring(0,1);
					}					
					
					if("".equals(is_ebill)){
						is_ebill = "";
					}else{
						is_ebill = is_ebill.substring(0,1);
					}
					
					if("".equals(utakeover_sign)){
						utakeover_sign = "";
					}else{
						utakeover_sign = utakeover_sign.substring(0,1);
					}
					String matchStr = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
					
					/*** 导入时票号已经存在时处理begin ***/
					KeyedCollection kCollTemp = dao.queryFirst(modelId, null, "where porder_no = '"+porder_no+"'",connection);
					if(kCollTemp.containsKey("porder_no")&&kCollTemp.getDataValue("porder_no")!=null){
						String sql = "select m.batch_no from Iqp_Bill_Detail d , Iqp_Batch_Bill_Rel r ,Iqp_Batch_Mng m"
							+ " where d.porder_no = r.porder_no and r.batch_no = m.batch_no and m.status not in ('03','04') and d.porder_no = '"
							+ porder_no + "'";
						IndexedCollection iColl_Rel = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
						if(iColl_Rel.size() > 0){	//此票号已被引用
							msg = "票号"+porder_no+"已被未办结票据批次"+((KeyedCollection)iColl_Rel.get(0)).getDataValue("batch_no")+"引用！";
							KeyedCollection msgKColl = new KeyedCollection();
							msgKColl.addDataField("msg", msg);
							msgIColl.addDataElement(msgKColl);
						}else{	//没引用则引入当前批次
							KeyedCollection kColl_Rel = new KeyedCollection("IqpBatchBillRel");
							kColl_Rel.addDataField("batch_no", batch_no);
							kColl_Rel.addDataField("porder_no", porder_no);
							dao.insert(kColl_Rel, connection);
						}
						continue;
					}
					/*** 导入时票号已经存在时处理end ***/
					if("".equals(porder_no)){
						msg = "第"+m+"行记录汇票号码未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(drft_amt)){
						msg = "第"+m+"行记录票面金额未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(bill_isse_date)||!bill_isse_date.matches(matchStr)){
						msg = "第"+m+"行记录票据签发日未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(porder_end_date)||!porder_end_date.matches(matchStr)){
						msg = "第"+m+"行记录汇票到期日未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					//取到预计转/贴现日期
					fore_disc_date = (String)batchKColl.getDataValue("fore_disc_date");
					if(porder_end_date != null && !"".equals(porder_end_date) && fore_disc_date != null && !"".equals(fore_disc_date)){
						if(sdf.parse(porder_end_date).before(sdf.parse(fore_disc_date))){
							msg = "第"+m+"行记录汇票到期日不能小于预计转/贴现日期，无法进行导入！";
							KeyedCollection msgKColl = new KeyedCollection();
							msgKColl.addDataField("msg", msg);
							msgIColl.addDataElement(msgKColl);
							continue;
						}
					}
					//取到回购日期
					biz_type = (String)batchKColl.getDataValue("biz_type");//业务类型
					String rebuy_date = null;
					if("04".equals(biz_type)){//卖出回购
						rebuy_date = (String)batchKColl.getDataValue("rebuy_date");//回购日期
						if(porder_end_date != null && !"".equals(porder_end_date) && rebuy_date != null && !"".equals(rebuy_date)){
							if(sdf.parse(porder_end_date).before(sdf.parse(rebuy_date))){
								msg = "第"+m+"行记录汇票到期日不能小于回购日期，无法进行导入！";
								KeyedCollection msgKColl = new KeyedCollection();
								msgKColl.addDataField("msg", msg);
								msgIColl.addDataElement(msgKColl);
								continue;
							}
						}
					}
					
					if("".equals(porder_addr)){
						msg = "第"+m+"行记录汇票签发地未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(is_ebill)){
						msg = "第"+m+"行记录是否电票未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					kColl.put("porder_no", porder_no);
					kColl.put("drft_amt", drft_amt);
					kColl.put("bill_isse_date", bill_isse_date);
					kColl.put("porder_end_date", porder_end_date);
					kColl.put("isse_name", isse_name);
					kColl.put("daorg_acct", daorg_acct);
					kColl.put("daorg_name", daorg_name);
					kColl.put("daorg_no", daorg_no);
					kColl.put("pyee_name", pyee_name);
					kColl.put("paorg_acct_no", paorg_acct_no);
					kColl.put("paorg_name", paorg_name);
					kColl.put("porder_addr", porder_addr);
					kColl.put("status", "01");
					kColl.put("bill_type", bill_type);
					kColl.put("porder_curr", "CNY");	//币种
					
					kColl.put("paorg_no", paorg_no);
					if("100".equals(bill_type)){
						kColl.put("aorg_type", aorg_type);
						kColl.put("aorg_no", aorg_no);
						kColl.put("aorg_name", aorg_name);
					}else{
						kColl.put("aaorg_type", aaorg_type);
						kColl.put("accptr_cmon_code", accptr_cmon_code);
						kColl.put("aaorg_acct_no", aaorg_acct_no);
						kColl.put("aaorg_no", aaorg_no);
						kColl.put("aaorg_name", aaorg_name);
					}
					
					kColl.put("tcont_no", tcont_no);
					kColl.put("tcont_amt", tcont_amt);
					kColl.put("is_ebill", is_ebill);
					kColl.put("utakeover_sign", utakeover_sign);
					
					kColl.setName(modelId);
					dao.insert(kColl, connection);
					KeyedCollection kColl1 = new KeyedCollection();
					KeyedCollection batKColl = dao.queryDetail("IqpBatchMng", batch_no, connection);
					if(batKColl != null && batKColl.size() > 0){
						IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
						.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
						DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
						
						String bizType = (String)batKColl.getDataValue("biz_type");
						String discDate = (String)batKColl.getDataValue("fore_disc_date");
						String discRate = (String)batKColl.getDataValue("rate");
						String rebuyDate = (String)batKColl.getDataValue("rebuy_date");
						String adjDays = "";
						if("2".equals(porder_addr)){//1:本地/2:异地
							adjDays = "3";//调整天数
						}else{
							adjDays = "0";//调整天数
						}
						kColl1.put("adj_days", adjDays);
						if(discRate == null){
							discRate = "0";
						}
						kColl1.put("fore_disc_date", discDate);
						/** 计算贴现天数、回购天数 */
						//计算下一个工作日
						porder_end_date = cmisComponent.getNextWorkDate(porder_end_date, dataSource);
						
						Date discDateHelp = sdf.parse(discDate);
						Date endDateHelp = sdf.parse(porder_end_date);
						/** 计算贴现利息，贴现天数 */
						long discNum = (endDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						if(discNum <= 0){
							discNum = 0;
						}
						double discInt = Double.parseDouble(drft_amt)*(Double.parseDouble(String.valueOf(discNum))+Double.parseDouble(adjDays))*Double.parseDouble(discRate)/360;
						kColl1.put("disc_days", String.valueOf(discNum));
						kColl1.put("int", discInt);
						/** 计算回购利息，回购天数 */
						if("04".equals(bizType)||"02".equals(bizType)){
							if(rebuyDate == null){
								rebuyDate = "";
							}
							//计算下一个工作日
							rebuyDate = cmisComponent.getNextWorkDate(rebuyDate, dataSource);
							Date rebuyDateHelp = sdf.parse(rebuyDate);
							long rebuyNum = (rebuyDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
							if(rebuyNum <= 0){
								rebuyNum = 0;
							}
							//double rebuyInt = Double.parseDouble(drftAmt)*Double.parseDouble(String.valueOf(rebuyNum))*Double.parseDouble(rebuyRate)/360;
							kColl1.put("rebuy_days", String.valueOf(rebuyNum));
							kColl1.put("disc_days", String.valueOf(rebuyNum));
							kColl1.put("rebuy_int", discInt);
						}
						if(!"07".equals(bizType)){
							kColl1.put("disc_rate", discRate);
						}else{
							kColl1.put("disc_rate", disc_rate);
						}
						kColl1.put("fore_rebuy_date", rebuyDate);
						kColl1.put("biz_type", bizType);
						
						kColl1.put("drft_amt", drft_amt);
						kColl1.put("batch_no", batch_no);
						kColl1.put("porder_no", porder_no);
						kColl1.setName(modelId1);
						dao.insert(kColl1, connection);
					}
					
					
//					KeyedCollection kColl1 = new KeyedCollection();
//					/** 计算贴现天数、回购天数 */
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//					Date discDateHelp = sdf.parse(fore_disc_date);
//					Date endDateHelp = sdf.parse(porder_end_date);
//					/** 计算贴现利息，贴现天数 */
//					long discNum = (endDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
//					if(discNum <= 0){
//						discNum = 0;
//					}				
//					kColl1.setName(modelId1);
//					kColl1.put("porder_no", porder_no);
//					kColl1.put("batch_no", batch_no);
//					kColl1.put("fore_disc_date", fore_disc_date);
//					kColl1.put("disc_rate", disc_rate);
//					kColl1.put("drft_amt", drft_amt);
//					kColl1.put("biz_type", biz_type);
//					dao.insert(kColl1, connection);
					KeyedCollection kColl2 = new KeyedCollection();
					kColl2.put("batch_no", batch_no);
					kColl2.put("porder_no", porder_no);
					kColl2.setName(relModel);
					dao.insert(kColl2, connection);
					
					/** -------------更新批次信息-------------- */
					/**4、 通过批次号码遍历批次下所有票据信息，统计得出批次值 */
					KeyedCollection baKColl = new KeyedCollection(batModel);
					baKColl.put("batch_no", batch_no);
					int billNum = 0;
					double billAmt = 0;
					double intAmt = 0;
					double rbuyAmt = 0;
					IndexedCollection reIColl = dao.queryList(relModel, " where batch_no='"+batch_no+"'", connection);
					if(reIColl != null && reIColl.size() > 0){
						/** 封装需要查询的票据信息SQL */
						String porderSQLHelp = " where porder_no in (";
						for(int j=0;j<reIColl.size();j++){
							KeyedCollection kc = (KeyedCollection)reIColl.get(j);
							String porderNo = (String)kc.getDataValue("porder_no");
							porderSQLHelp = porderSQLHelp+"'"+porderNo+"',";
						}
						porderSQLHelp = porderSQLHelp.substring(0, porderSQLHelp.length()-1)+") ";

						/** 计算票据总金额 */
						IndexedCollection biIColl = dao.queryList(modelId, porderSQLHelp, connection);
						if(biIColl != null && biIColl.size() > 0){
							billNum = biIColl.size();
							for(int k=0;k<biIColl.size();k++){
								KeyedCollection kc = (KeyedCollection)biIColl.get(k);
								String amt = (String)kc.getDataValue("drft_amt");
								billAmt += Double.parseDouble(amt);
							}
							baKColl.addDataField("bill_qnt", billNum);//票据数量
							baKColl.addDataField("bill_total_amt", billAmt);//票据总金额
						}
						/** 计算票据利息,回购利息 */
						String conditionStr = porderSQLHelp + " and batch_no = '"+batch_no+"'";
						IndexedCollection inIColl = dao.queryList("IqpBillIncome", conditionStr, connection);
						if(inIColl != null && inIColl.size() > 0){
							for(int n=0;n<inIColl.size();n++){
								KeyedCollection kc = (KeyedCollection)inIColl.get(n);
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
					/** -------------更新批次信息end-------------- */
					
					/**
					 * 检查该批次是否已关联业务，如果已关联，那么要更新申请信息。
					 */
					//首先判断所属那种业务
					KeyedCollection batchKcoll = dao.queryAllDetail(batModel, batch_no, connection);
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
				}	
			}
			if(msgIColl.size()==0){
				context.addDataField("errorInfo", "successInfo");
			}else{
				context.addDataField("errorInfo", "");
				this.putDataElement2Context(msgIColl, context);
			}
			
		}catch(Exception e){
			throw new EMPException("导入文件失败"+e.getMessage());
		} finally {
        	//导入的时候首先上传文件 所以这个地方判断文件是否存在如果存在直接删除文件 所以有异常不处理
        	try{
        		File file = new File(tempFileName);
        		boolean delFlag = file.delete();       		
        		EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "删除对应的文件+"+tempFileName+(delFlag?"成功":"失败"));
        	}catch(Exception e){
        		EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "删除上传的临时文件错误！");
        	}
        	
            if (connection != null)
                this.releaseConnection(context, connection);
		}
		
		return "0";
	}

}
