package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankacc;

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
/**
 * 
*@author lisj
*@time 2015-2-28
*@description TODO 需求编号：【XD150213011】增加同业授信批量导入功能
*@version v1.0
*
 */
public class ImportLmtIntbankAccOp extends CMISOperation {
	
	 private final String modelId = "LmtIntbankAcc";

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
			String same_org_no = "";         //同业行号
			String same_org_cnname = "";     //同业机构(行)名称
			String lmt_amt ="";				 //授信额度（万元）
			String crd_grade = "";           //信用等级
			String start_date = "";          //授信起始日期
			String end_date ="";			 //授信到期日期

			String msg = "";
			IndexedCollection msgIColl = new IndexedCollection("MsgList");

			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				String modelname = ((String)cell[0][4].cellvalue).trim();
				if(!"授信起始日".equals(modelname)){
					msg = "导入文件失败，导入模板错误，请重新下载导入模板！";
					KeyedCollection msgKColl = new KeyedCollection();
					msgKColl.addDataField("msg", msg);
					msgIColl.addDataElement(msgKColl);
					continue;
				}
				for(int m =1; m< cell.length; m++){
					try{
						same_org_no = ((String)cell[m][0].cellvalue).trim();
						same_org_cnname = ((String)cell[m][1].cellvalue).trim();
						crd_grade = ((String)cell[m][2].cellvalue).trim();
						lmt_amt = ((String)cell[m][3].cellvalue).trim();
						start_date = ((String)cell[m][4].cellvalue).trim();
						end_date = ((String)cell[m][5].cellvalue).trim();

					}catch (Exception e) {
					}		
					KeyedCollection kColl = new KeyedCollection();
				    String matchStr = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
					
					if("".equals(same_org_no)){
						msg = "第"+m+"行记录同业行号未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(same_org_cnname)){
						msg = "第"+m+"行记录公司名称未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
						
					if("".equals(lmt_amt)){
						msg = "第"+m+"行记录授信额度（万元）未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}

					if("".equals(crd_grade)){
						msg = "第"+m+"行记录信用等级未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(start_date)||!start_date.matches(matchStr)){
						msg = "第"+m+"行记录授信起始日未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(end_date)||!end_date.matches(matchStr)){
						msg = "第"+m+"行记录授信到期日未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if(start_date != null && !"".equals(start_date) && end_date != null && !"".equals(end_date)){
						if(sdf.parse(end_date).before(sdf.parse(start_date))){
							msg = "第"+m+"行记录授信到期日不能小于授信起始日，无法进行导入！";
							KeyedCollection msgKColl = new KeyedCollection();
							msgKColl.addDataField("msg", msg);
							msgIColl.addDataElement(msgKColl);
							continue;
						}
					}
					
					//查询同业行号对应的客户码
					IndexedCollection temp = dao.queryList("CusSameOrg", "where same_org_no='"+same_org_no+"'", connection);
					if(temp!=null && temp.size()>0){
						KeyedCollection CSO  = (KeyedCollection) temp.get(0);
						String cus_id = CSO.getDataValue("cus_id").toString();
						//查询同业授信额度台账流水号
						IndexedCollection tempSerno = dao.queryList(modelId, "where cus_id='"+cus_id+"'", connection);
						if(tempSerno!=null && tempSerno.size()>0){
							KeyedCollection LIBA  = (KeyedCollection) tempSerno.get(0);
							kColl.put("serno",LIBA.getDataValue("serno").toString());
							kColl.put("agr_no", LIBA.getDataValue("agr_no").toString());
							kColl.put("cus_id", cus_id);
							kColl.put("same_org_no", same_org_no);
							kColl.put("same_org_cnname", same_org_cnname);
							kColl.put("lmt_amt", lmt_amt);
							kColl.put("start_date", start_date);
							kColl.put("end_date", end_date);
							kColl.put("lmt_status", "10");//更新额度状态
							kColl.setName(modelId);
							dao.update(kColl, connection);
							//更新同业客户信用等级
							KeyedCollection sameCusBaseInfoKC = new KeyedCollection();
							sameCusBaseInfoKC.put("cus_id", cus_id);
							sameCusBaseInfoKC.put("same_org_no", same_org_no);
							sameCusBaseInfoKC.put("crd_grade", crd_grade);
							sameCusBaseInfoKC.setName("CusSameOrg");
							dao.update(sameCusBaseInfoKC, connection);
						}
					}
			}
			if(msgIColl.size()==0){
				context.addDataField("errorInfo", "successInfo");
			}else{
				context.addDataField("errorInfo", "");
				this.putDataElement2Context(msgIColl, context);
			}
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
