package com.yucheng.cmis.biz01line.prd.op.prdlibormaintain;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;

public class ImportPrdLiborMaintainOp extends CMISOperation {
	
	 private final String modelId = "PrdLiborMaintain";

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String tempFileName = "";
		String flagInfo = CMISMessage.DEFEAT;
		Connection connection =null;
		try{
			
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			 
			 //读取xls文件路径
            String tempPath =(String)context.getDataValue("DocBasicinfo__file_path");
            String tempName =(String)context.getDataValue("DocBasicinfo__file_name");
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
			String libor_date = null;//LIBOR日期
			String cur_type = null;//币种
			String pk_id = null;//PK_ID
			String msg = "";
			//日期校验正则表达式
			String matchStr = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
			//利率校验正则表达式
			String rateStr = "^[\\d\\,]*\\.?\\d*[\\%]?$";
			
			IndexedCollection msgIColl = new IndexedCollection("MsgList");
			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				for(int m =1; m< cell.length; m++){
					//需新增记录KC
					KeyedCollection kColl = new KeyedCollection();
					libor_date= ""+cell[m][0].cellvalue;
					cur_type= ""+cell[m][1].cellvalue;
					
					if("".equals(cur_type)){
						cur_type = "";
					}else{
						cur_type = cur_type.substring(0,3);
					}
					if("".equals(libor_date)){
						msg = "第"+m+"行记录LIBOR日期未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!libor_date.matches(matchStr)){
						msg = "第"+m+"行记录LIBOR日期格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!"".equals(libor_date)){
						IndexedCollection liborDateIc = dao.queryList(modelId, "where libor_date='"+libor_date+"' and cur_type = '"+cur_type+"'",connection);
						if(liborDateIc.size()>0){
							msg = "第"+m+"行记录LIBOR日期为"+libor_date+"的记录已经存在，无法进行导入！";
							KeyedCollection msgKColl = new KeyedCollection();
							msgKColl.addDataField("msg", msg);
							msgIColl.addDataElement(msgKColl);
							continue;
						}
					}
					if("".equals(cur_type)){
						msg = "第"+m+"行记录币种未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg2 = new BigDecimal(""+cell[m][2].cellvalue.toString());
					if("".equals(""+cell[m][2].cellvalue)){
						msg = "第"+m+"行第2列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg2.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第2列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg3 = new BigDecimal(""+cell[m][3].cellvalue.toString());
					if("".equals(""+cell[m][3].cellvalue)){
						msg = "第"+m+"行第3列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg3.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第3列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg4 = new BigDecimal(""+cell[m][4].cellvalue.toString());
					if("".equals(""+cell[m][4].cellvalue)){
						msg = "第"+m+"行第4列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg4.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第4列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg5 = new BigDecimal(""+cell[m][5].cellvalue.toString());
					if("".equals(""+cell[m][5].cellvalue)){
						msg = "第"+m+"行第5列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg5.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第5列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg6 = new BigDecimal(""+cell[m][6].cellvalue.toString());
					if("".equals(""+cell[m][6].cellvalue)){
						msg = "第"+m+"行第6列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg6.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第6列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg7 = new BigDecimal(""+cell[m][7].cellvalue.toString());
					if("".equals(""+cell[m][7].cellvalue)){
						msg = "第"+m+"行第7列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg7.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第7列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg8 = new BigDecimal(""+cell[m][8].cellvalue.toString());
					if("".equals(""+cell[m][8].cellvalue)){
						msg = "第"+m+"行第8列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg8.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第8列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg9 = new BigDecimal(""+cell[m][9].cellvalue.toString());
					if("".equals(""+cell[m][9].cellvalue)){
						msg = "第"+m+"行第9列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg9.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第9列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg10 = new BigDecimal(""+cell[m][10].cellvalue.toString());
					if("".equals(""+cell[m][10].cellvalue)){
						msg = "第"+m+"行第10列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg10.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第10列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg11 = new BigDecimal(""+cell[m][11].cellvalue.toString());
					if("".equals(""+cell[m][11].cellvalue)){
						msg = "第"+m+"行第11列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg11.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第11列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg12 = new BigDecimal(""+cell[m][12].cellvalue.toString());
					if("".equals(""+cell[m][12].cellvalue)){
						msg = "第"+m+"行第12列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg12.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第12列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg13 = new BigDecimal(""+cell[m][13].cellvalue.toString());
					if("".equals(""+cell[m][13].cellvalue)){
						msg = "第"+m+"行第13列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg13.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第13列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg14 = new BigDecimal(""+cell[m][14].cellvalue.toString());
					if("".equals(""+cell[m][14].cellvalue)){
						msg = "第"+m+"行第14列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg14.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第14列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg15 = new BigDecimal(""+cell[m][15].cellvalue.toString());
					if("".equals(""+cell[m][15].cellvalue)){
						msg = "第"+m+"行第15列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg15.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第15列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					BigDecimal bg16 = new BigDecimal(""+cell[m][16].cellvalue.toString());
					if("".equals(""+cell[m][16].cellvalue)){
						msg = "第"+m+"行第16列利率未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else if(!(bg16.toPlainString()).matches(rateStr)){
						msg = "第"+m+"行第16列利率格式错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					kColl.addDataField("libor_date",libor_date);
					kColl.addDataField("cur_type",cur_type);
					kColl.addDataField("last_ir",""+cell[m][2].cellvalue);
					kColl.addDataField("one_week_ir",""+cell[m][3].cellvalue);
					kColl.addDataField("two_week_ir",""+cell[m][4].cellvalue);
					kColl.addDataField("one_month_ir",""+cell[m][5].cellvalue);
					kColl.addDataField("two_month_ir",""+cell[m][6].cellvalue);
					kColl.addDataField("three_month_ir",""+cell[m][7].cellvalue);
					kColl.addDataField("four_month_ir",""+cell[m][8].cellvalue);
					kColl.addDataField("five_month_ir",""+cell[m][9].cellvalue);
					kColl.addDataField("six_month_ir",""+cell[m][10].cellvalue);
					kColl.addDataField("seven_month_ir",""+cell[m][11].cellvalue);
					kColl.addDataField("eight_month_ir",""+cell[m][12].cellvalue);
					kColl.addDataField("nine_month_ir",""+cell[m][13].cellvalue);
					kColl.addDataField("ten_month_ir",""+cell[m][14].cellvalue);
					kColl.addDataField("eleven_month_ir",""+cell[m][15].cellvalue);
					kColl.addDataField("twelve_month_ir",""+cell[m][16].cellvalue);
					kColl.addDataField("maintain_id",context.getDataValue("currentUserId"));
					kColl.addDataField("organno",context.getDataValue("organNo"));
					kColl.addDataField("imp_date",context.getDataValue("OPENDAY"));
					kColl.addDataField("status","01");
					
					//业务流水号
					pk_id = CMISSequenceService4JXXD.querySequenceFromDB("INDIVEDTZ", "all", connection, context);
					//设置主键
					kColl.addDataField("pk_id", pk_id);
					kColl.setName(modelId);
					dao.insert(kColl, connection);
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
