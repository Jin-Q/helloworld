package com.yucheng.cmis.biz01line.cus.op.cusblklist;

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
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;

public class ImportCusBlkListOp extends CMISOperation {
	
	 private final String modelId = "CusBlkListTemp";

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
			String data_source = "20";//数据来源
			String serno = null;//业务编号
			String cus_name = null;//客户名称
			String type = null;//类别
			String cert_type = null;//证件类型
			String cert_code = null;//证件号码
			String legal_name = null;//法定代表人
			String legal_addr = null;//地址
			String black_type = null;//客户类型
			String black_reason = null;//客户描述
			String msg = "";
			IndexedCollection msgIColl = new IndexedCollection("MsgList");
			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				
				for(int m =1; m< cell.length; m++){
					//需新增记录KC
					KeyedCollection kColl = new KeyedCollection();
					cus_name= ""+cell[m][0].cellvalue;
					type= ""+cell[m][1].cellvalue;
					cert_type = ""+cell[m][2].cellvalue;
					cert_code = ""+cell[m][3].cellvalue;
					legal_name = ""+cell[m][4].cellvalue;
					legal_addr = ""+cell[m][5].cellvalue;
					black_type = ""+cell[m][6].cellvalue;
					black_reason = ""+cell[m][7].cellvalue;
					if("".equals(cert_type)){
						cert_type = "";
					}else{
						cert_type = cert_type.substring(0,1);
					}
					if("".equals(black_type)){
						black_type = "";
					}else{
						black_type = black_type.substring(0,2);
					}
					KeyedCollection kCollTemp = dao.queryFirst(modelId, null, "where cert_code = '"+cert_code+"'",connection);
					if(kCollTemp.getDataValue("cert_code")!=null){
						msg = "第"+m+"行记录已存在，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(type)){
						msg = "第"+m+"行记录类别未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(cert_type)){
						msg = "第"+m+"行记录证件类型未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(cus_name)){
						msg = "第"+m+"行记录名称未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(black_type)){
						msg = "第"+m+"行记录客户类型未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(black_reason)){
						msg = "第"+m+"行记录客户描述未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("01".equals(type.substring(0,2))){//个人
							if("20".equals(cert_type)||"a".equals(cert_type)){
								msg = "第"+m+"行记录证件类型有误，无法进行导入！";
								KeyedCollection msgKColl = new KeyedCollection();
								msgKColl.addDataField("msg",msg);
								msgIColl.addDataElement(msgKColl);
								continue;					
							}
							if("".equals(cert_code)){
								msg = "第"+m+"行记录证件号码为空，无法进行导入！";
								KeyedCollection msgKColl = new KeyedCollection();
								msgKColl.addDataField("msg",msg);
								msgIColl.addDataElement(msgKColl);
								continue;					
							}
						//	kColl.addDataField("cert_type","0");
					}else if("02".equals(type.substring(0,2))){//企业
						cert_type="a";
					}else{
						msg = "第"+m+"行记录类别类别信息录入错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					IndexedCollection iCollTemp= dao.queryList(modelId, null, "where cert_code = '"+cert_code+"'",connection);
					if(iCollTemp.size()!=0){
						kCollTemp = (KeyedCollection) iCollTemp.get(0);
						kColl.addDataField("cus_id", kCollTemp.getDataValue("cus_id"));
						data_source= "11";
					}
					kColl.addDataField("data_source",data_source);
					kColl.addDataField("cert_type",cert_type);
					kColl.addDataField("cert_code",cert_code);
					kColl.addDataField("black_type",black_type);
					kColl.addDataField("legal_name",legal_name);
					kColl.addDataField("street",legal_addr);
					kColl.addDataField("black_reason",black_reason);
					kColl.addDataField("cus_name",cus_name);
					kColl.addDataField("input_id",context.getDataValue("currentUserId"));
					kColl.addDataField("input_br_id",context.getDataValue("organNo"));
					kColl.addDataField("input_date",context.getDataValue("OPENDAY"));
					kColl.addDataField("status","001");
					kColl.addDataField("source","1");
					
					//业务流水号
					serno = CMISSequenceService4JXXD.querySequenceFromDB("CUS", "all", connection, context);
					//设置主键
					kColl.addDataField("serno", serno);
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
