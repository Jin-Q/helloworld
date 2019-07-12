package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtbatchcorre;

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

public class ImportBatchRptOp extends CMISOperation {
	
	 private final String modelIdBatch = "LmtBatchCorre";

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String tempFileName = "";
		String errorInfo = CMISMessage.SUCCESS;
		String errorMsg = "";
		KeyedCollection kColl = null;
		Connection connection =null;
		try{
			connection = this.getConnection(context);
			 kColl = (KeyedCollection)context.getDataElement(modelIdBatch);
			 kColl.addDataField("cus_id", "");
			 //获取批量包等级
             String cdt_lvl =((String)kColl.getDataValue("cdt_lvl")).trim();
             String serno = (String)kColl.getDataValue("serno");
             //批量客户号
             String batch_cus_no = (String)kColl.getDataValue("batch_cus_no");
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
                    errorInfo = "errorInfo";
                    errorMsg = "[文件路径：]" + tempFileName + " 错误!";
                    context.addDataField("errorInfo", errorInfo);
        			context.addDataField("errorMsg", errorMsg);
                }
            } else {
                EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：]" + tempFileName);
                errorInfo = "errorInfo";
                errorMsg = "[文件路径：]" + tempFileName + " 错误!";
                context.addDataField("errorInfo", errorInfo);
    			context.addDataField("errorMsg", errorMsg);
            }
            if("success".equals(errorInfo)){
            	// 读取文件
                ExcelVO evo = null;
                try {
    			    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：" + tempFileName + "]");
    			    evo = ExcelTreat.readExcel(tempFileName);
    			}catch (Exception e) {
    			    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：" + tempFileName + "]" + "错误信息：" + e.toString());
    			    errorInfo = "errorInfo";
                    errorMsg = "导入文件失败，请检查文件是否正确及文件路径是否正确！";
                    context.addDataField("errorInfo", errorInfo);
        			context.addDataField("errorMsg", errorMsg);
    			}
    			if("success".equals(errorInfo)){
    				SheetVO[] sheets = evo.sheets;
        			CellVO[][] cell = null;
        			SheetVO sheet = null;
        			String cus_id = null;
        			String crd_lvl = null;
        			int count = 0;
        			int sum = 0;
        			for(int i=0;i<sheets.length;i++){
        				sheet = sheets[i];
        				cell = sheet.cells;
        				for(int m =1; m< cell.length; m++){
        					cus_id = ""+cell[m][0].cellvalue;
        					int type = cell[m][0].celltype;
        					if(type!=1){
        						throw new EMPException("第"+m+"行第一列单元格格式不合法，请改为常规格式！");
        					}
        					crd_lvl =cell[m][6].cellvalue.toString();
        					if(crd_lvl!=null&&!"".equals(crd_lvl)&&cus_id!=null&&!"".equals(cus_id)){
        						IndexedCollection iCollCus = dao.queryList("CusSameOrg", "where cus_id='"+cus_id+"' and crd_grade='"+cdt_lvl+"'", connection);
        						if(iCollCus.size()>0){
        							if(crd_lvl.equals(cdt_lvl)){
        								IndexedCollection iColl = dao.queryList(modelIdBatch, "where cus_id='"+cus_id+"'"+"and batch_cus_no='"+batch_cus_no+"'", connection);
        								if(iColl.size()==0){
        									kColl.setDataValue("cus_id", cus_id);
        									count=dao.insert(kColl, connection);
        								}
        							}else{
        								sum=1;
        							}
        						}else{
        							connection.rollback();
        							errorMsg = "导入文件中客户["+cus_id+"]在系统中不存在或者信用等级与批量包不一致，请检查！";
        							sum=2;
        							break;
        						}
        					}
        				}
        			}
        			context.addDataField("serno", serno);
        			if(sum==1){
        				context.addDataField("errorInfo", "errorInfo2");
        			}else if(sum==2){
        				context.addDataField("errorInfo", "errorInfo3");
        				context.addDataField("errorMsg", errorMsg);
        			}else{
        				if(count==1){
        					context.addDataField("errorInfo", "successInfo");
        				}else{
        					context.addDataField("errorInfo", "errorInfo1");
        				}
        			}
    			}
    		}
        }catch(Exception e){
			throw new EMPException("导入文件失败，错误描述："+e.getMessage());
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
