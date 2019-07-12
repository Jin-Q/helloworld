package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpobilldetail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;
import com.yucheng.cmis.util.TableModelUtil;

public class ImportDpoBillDetailOp extends CMISOperation {
	
	 private final String modelId = "IqpBillDetailInfo";
	 private final String relModel = "IqpCorreInfo";
	 private final String batModel = "IqpDrfpoMana";
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
            String drfpo_no =(String)context.getDataValue("drfpo_no");
            String bill_type =(String)context.getDataValue("bill_type");
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
			//String fore_disc_date = "";	  //预计转/贴现日期
			//String disc_rate = "";		  //转/贴现利率
			String daorg_tyep = "";		  //出票人开户行类型
			String tcont_no = "";         //贸易合同编号
			String tcont_amt = "";        //贸易合同金额
			String is_ebill = "";         //是否电票
			String utakeover_sign = "";   //不得转让标记
			
			String aorg_type = "";        //承兑行类型
			String aorg_no = "";   		  //承兑行行号
			String aorg_name = "";        //承兑行名称
			
			String aaorg_type = "";        //承兑人开户行类型
			String accptr_cmon_code = "";   //承兑人组织机构代码
			String aaorg_acct_no = "";      //承兑人开户行账号
			String aaorg_no = "";         //承兑人开户行行号
			String aaorg_name = "";   	  //承兑人开户行名称

			String msg = "";
			IndexedCollection msgIColl = new IndexedCollection("MsgList");
			KeyedCollection batchKColl = dao.queryDetail(batModel, drfpo_no, connection);
			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				
				for(int m =1; m< cell.length; m++){
					/*** excel取数 ***/
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
/*						fore_disc_date = ((String)cell[m][12].cellvalue).trim();
						disc_rate = ((String)cell[m][13].cellvalue).trim();*/
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
						}catch (Exception e) {
						}
						try{
							tcont_amt = ((String)cell[m][21].cellvalue).trim();
						}catch (Exception e) {
						}
						try{
							is_ebill = ((String)cell[m][22].cellvalue).trim();
						}catch (Exception e) {
						}
						try{
							utakeover_sign = ((String)cell[m][23].cellvalue).trim();
						}catch (Exception e) {
						}
						if("".equals(aaorg_type)){
							aaorg_type = "";
						}else{
							aaorg_type = aaorg_type.substring(0,2);
						}
					}
					
					/*** 截字典项 ***/
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
					
					/*** 校验 ***/
					String matchStr = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
					
					/*** 当票号已经存在时处理begin ***/
					KeyedCollection kCollTemp = dao.queryFirst(modelId, null, "where porder_no = '"+porder_no+"'",connection);
					if(kCollTemp.containsKey("porder_no")&&kCollTemp.getDataValue("porder_no")!=null){
						String sql = "select d.porder_no from iqp_bill_detail_info d , Iqp_Corre_Info i , Iqp_Drfpo_Mana m"
								+ " where d.porder_no = i.porder_no and i.drfpo_no  = m.drfpo_no and m.status != '02' and d.porder_no = '"
								+ porder_no + "'";
						IndexedCollection iColl_Rel = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
						if(iColl_Rel.size() > 0){	//此票号已被引用
							msg = "票号"+porder_no+"已被未办结票据池引用！";
							KeyedCollection msgKColl = new KeyedCollection();
							msgKColl.addDataField("msg", msg);
							msgIColl.addDataElement(msgKColl);
						}else{	//没引用则引入当前批次
							KeyedCollection kColl_Rel = new KeyedCollection(relModel);
							kColl_Rel.addDataField("drfpo_no", drfpo_no);
							kColl_Rel.addDataField("porder_no", porder_no);
							kColl_Rel.addDataField("status", "00");
							dao.insert(kColl_Rel, connection);
						}
						continue;
					}
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

					if("".equals(is_ebill)){
						msg = "第"+m+"行记录是否电票未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					/*** 将excel记录生成iqp_bill_detail_info记录 ***/
					KeyedCollection kColl = new KeyedCollection();
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
					kColl.put("tcont_no", tcont_no);
					kColl.put("tcont_amt", tcont_amt);
					kColl.put("is_ebill", is_ebill);
					kColl.put("utakeover_sign", utakeover_sign);
					kColl.put("paorg_no", paorg_no);
					kColl.put("porder_curr", "CNY");	//币种
					
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
					kColl.setName(modelId);
					dao.insert(kColl, connection);					

					/*** 生成IqpBillIncome记录 ***/
					KeyedCollection kColl2 = new KeyedCollection();
					kColl2.put("drfpo_no", drfpo_no);
					kColl2.put("porder_no", porder_no);
					kColl2.put("status", "00");
					kColl2.setName(relModel);
					dao.insert(kColl2, connection);
					
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
