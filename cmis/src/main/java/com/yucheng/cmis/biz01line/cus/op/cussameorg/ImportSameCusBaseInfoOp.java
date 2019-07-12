package com.yucheng.cmis.biz01line.cus.op.cussameorg;

import java.io.File;
import java.io.UnsupportedEncodingException;
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
public class ImportSameCusBaseInfoOp extends CMISOperation {
	
	 private final String modelId = "CusSameOrg";

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
			String com_ins_code = "";        //组织机构代码     
			String same_org_type = "";       //机构类型
			String country = "";             //国别
			String reg_cap_amt = "";         //注册资本(万元) 
			String assets = "";              //资产总额(万元)
			String crd_grade = "";           //信用等级
			String street = "";             //注册街道

			String msg = "";
			IndexedCollection msgIColl = new IndexedCollection("MsgList");

			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				String modelname = ((String)cell[0][3].cellvalue).trim();
				if(!"机构类型".equals(modelname)){
					msg = "导入文件失败，导入模板错误，请重新下载导入模板！";
					KeyedCollection msgKColl = new KeyedCollection();
					msgKColl.addDataField("msg", msg);
					msgIColl.addDataElement(msgKColl);
					continue;
				}
				for(int m =1; m< cell.length; m++){
					//需新增记录KC
					try{
						same_org_no = ((String)cell[m][0].cellvalue).trim();
						same_org_cnname = ((String)cell[m][1].cellvalue).trim();
						com_ins_code = ((String)cell[m][2].cellvalue).trim();
						same_org_type = ((String)cell[m][3].cellvalue).trim();
						country = ((String)cell[m][4].cellvalue).trim();
						reg_cap_amt = ((String)cell[m][5].cellvalue).trim();
						assets = ((String)cell[m][6].cellvalue).trim();						
						crd_grade = ((String)cell[m][7].cellvalue).trim();
						street = ((String)cell[m][8].cellvalue).trim();

					}catch (Exception e) {
					}		
					KeyedCollection kColl = new KeyedCollection();
					//String matchStr = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
					
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
					
//					if("".equals(bill_isse_date)||!bill_isse_date.matches(matchStr)){
//						msg = "第"+m+"行记录票据签发日未录入/格式出错，无法进行导入！";
//						KeyedCollection msgKColl = new KeyedCollection();
//						msgKColl.addDataField("msg", msg);
//						msgIColl.addDataElement(msgKColl);
//						continue;
//					}
					
					if("".equals(com_ins_code)){
						msg = "第"+m+"行记录组织机构代码证未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(same_org_type)){
						msg = "第"+m+"行记录机构类型未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(country)){
						msg = "第"+m+"行记录国别未录入，无法进行导入！";
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
					//查询同业行号对应的客户码
					IndexedCollection temp = dao.queryList(modelId, "where same_org_no='"+same_org_no+"'", connection);
					if(temp!=null && temp.size()>0){
						KeyedCollection CSO  = (KeyedCollection) temp.get(0);
						String cus_id = CSO.getDataValue("cus_id").toString();
						kColl.put("cus_id", cus_id);
						kColl.put("same_org_no", same_org_no);
						kColl.put("same_org_cnname", same_org_cnname);
						kColl.put("com_ins_code", com_ins_code);
						kColl.put("same_org_type", same_org_type);
						kColl.put("country", country);
						kColl.put("reg_cap_amt", reg_cap_amt);
						kColl.put("assets", assets);
						kColl.put("crd_grade", crd_grade);
						kColl.put("street", street);					
						kColl.setName(modelId);

						dao.update(kColl, connection);
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
