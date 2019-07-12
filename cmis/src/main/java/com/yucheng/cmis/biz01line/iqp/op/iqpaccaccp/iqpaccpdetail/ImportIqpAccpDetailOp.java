package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp.iqpaccpdetail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.PkGeneratorSet;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;

public class ImportIqpAccpDetailOp extends CMISOperation {
	
	 private final String modelId = "IqpAccpDetail";

	public String doExecute(Context context) throws EMPException {
		
		String tempFileName = "";
		String flagInfo = CMISMessage.DEFEAT;
		Connection connection =null;
		try{
			
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String cont="";
			if(context.containsKey("cont")){
				cont = (String)context.getDataValue("cont");
			}
			//修改前备份明细信息
			if(cont!=null && !"".equals(cont) && "modify".equals(cont)){
				IndexedCollection backUpIColl = dao.queryList(modelId, "where serno ='"+(String) context.getDataValue("serno")+"'", connection);//原票据明细列表
				IndexedCollection IADH = dao.queryList("IqpAccpDetailHis", " where serno ='"+(String) context.getDataValue("serno")+"'", connection);
				if(IADH == null || IADH.size()<=0){
					if(backUpIColl !=null && backUpIColl.size()>0){
						for(int i=0;i<backUpIColl.size();i++){
							KeyedCollection temp = (KeyedCollection) backUpIColl.get(i);
							temp.setName("IqpAccpDetailHis");
							dao.insert(temp, connection);
						}
					}
				}
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			 
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
			//String msg = "";
			
			String SERNO= context.getDataValue("serno").toString();//业务编号
			String CLT_PERSON= null;//收款人
			String CLT_ACCT_NO= null;//收款人账号
			String PAORG_NO= null;//收款人开户行行号
			String PAORG_NAME= null;//收款人开户行行名
			double DRFT_AMT = 0; //票面金额
			String TERM_TYPE= null;//期限类型
			double TERM= 0;//期限			
			
			IndexedCollection msgIColl = new IndexedCollection("MsgList");
			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				
				for(int m =1; m< cell.length; m++){
					//需新增记录KC
					KeyedCollection kColl = new KeyedCollection();
					
					CLT_PERSON = ""+cell[m][0].cellvalue;
					CLT_ACCT_NO = ""+cell[m][1].cellvalue;
					PAORG_NO = ""+cell[m][2].cellvalue;
					PAORG_NAME = ""+cell[m][3].cellvalue;
					DRFT_AMT = Double.parseDouble(cell[m][4].cellvalue+"");
					TERM_TYPE = ""+cell[m][5].cellvalue;
					TERM = Double.parseDouble(cell[m][6].cellvalue+"") ;
					
					if("".equals(TERM_TYPE)){	//期限类型截前三位
						TERM_TYPE = "";
					}else{
						TERM_TYPE = TERM_TYPE.substring(0,3);
					}
					
					kColl.addDataField("clt_person",CLT_PERSON);
					kColl.addDataField("clt_acct_no",CLT_ACCT_NO);
					kColl.addDataField("paorg_no",PAORG_NO);
					kColl.addDataField("paorg_name",PAORG_NAME);
					kColl.addDataField("drft_amt",DRFT_AMT);
					kColl.addDataField("term_type",TERM_TYPE);
					kColl.addDataField("term",TERM);
					
					//设置主键
					PkGeneratorSet pkservice = (PkGeneratorSet) context.getService(CMISConstance.ATTR_PRIMARYKEYSERVICE);
					UNIDGenerator pk = (UNIDGenerator) pkservice.getGenerator("UNID");
					String PK1 = pk.getUNID(); //主键
					kColl.addDataField("pk1", PK1);
					kColl.addDataField("serno", SERNO);
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