package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;

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
*@author yezm
*@time 2015-8-14
*@description TODO 【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程,权证信息导入
*@version v1.0
*
 */
public class ImportMortGuarantyCertiInfoOp extends CMISOperation {

	private final String modelId = "MortGuarantyCertiInfo";
	@Override
public String doExecute(Context context) throws EMPException {
		
		String tempFileName = "";
		String flagInfo = CMISMessage.DEFEAT;
		Connection connection =null;
		try{
			connection = this.getConnection(context);
			String guaranty_no = (String) context.getDataValue("guaranty_no");
			TableModelDAO dao = this.getTableModelDAO(context);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
			String warrant_cls = "";               //权证类别
			String warrant_type = "";              //权证类型
			String is_main_warrant = "";           //是否主权证
			String warrant_no = "";                //权证编号
			String warrant_name = "";         	   //权证名称
			String warrant_appro_unit = "";        //权利凭证核发单位
			String warrant_appro_date = "";        //权利凭证核发日期
			String warrant_trem = "";              //权利凭证期限
			String keep_org_no_displayname = "";   //保管机构
			String hand_org_no_displayname = "";   //经办机构
			String keep_org_no = "";			   //保管机构
			String hand_org_no = "";			   //经办机构

			

			String msg = "";
			IndexedCollection msgIColl = new IndexedCollection("MsgList");

			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				String sheetname = sheet.sheetname;
				String modelname = ((String)cell[0][0].cellvalue).trim();
				if(!"泉州银行".equals(sheetname)){
					continue;
				}
				if(!"权证类别".equals(modelname)){
					msg = "导入文件失败，导入模板错误，请重新下载导入模板！";
					KeyedCollection msgKColl = new KeyedCollection();
					msgKColl.addDataField("msg", msg);
					msgIColl.addDataElement(msgKColl);
					continue;
				}
				for(int m =1; m< cell.length; m++){
					try{
						warrant_cls = ((String)cell[m][0].cellvalue).trim();
						warrant_type = ((String)cell[m][1].cellvalue).trim();
						is_main_warrant = ((String)cell[m][2].cellvalue).trim();
						warrant_no = ((String)cell[m][3].cellvalue).trim();
						warrant_name = ((String)cell[m][4].cellvalue).trim();
						warrant_appro_unit = ((String)cell[m][5].cellvalue).trim();
						warrant_appro_date = ((String)cell[m][6].cellvalue).trim();
						warrant_trem = ((String)cell[m][7].cellvalue).trim();
						keep_org_no_displayname = ((String)cell[m][8].cellvalue).trim();
						hand_org_no_displayname = ((String)cell[m][9].cellvalue).trim();

					}catch (Exception e) {
					}		
					KeyedCollection kColl = new KeyedCollection();
				    String matchStr = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
					
					if("".equals(warrant_cls)){
						warrant_cls = "1";
					}else{
						warrant_cls = warrant_cls.substring(0, 1);
						}
					
					if("".equals(warrant_type)){
						msg = "第"+m+"行权证类型未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else{
						warrant_type = warrant_type.substring(0, 2);
					}
					
					if(("1".equals(warrant_cls)&&warrant_type.compareTo("14")==-1)||("2".equals(warrant_cls)&&warrant_type.compareTo("13")==1)){
						msg = "第"+m+"行权证类别与权证类型不匹配，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(warrant_no)){
						msg = "第"+m+"行记录权证编号未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}

					if("".equals(warrant_name)){
						msg = "第"+m+"行记录权证名称未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
														
					if(!"".equals(warrant_appro_date)&&!warrant_appro_date.matches(matchStr)){
						msg = "第"+m+"行记录权利凭证核发日期未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					String openDay = (String)context.getDataValue("OPENDAY");
					if(warrant_appro_date != null && !"".equals(warrant_appro_date) && openDay != null && !"".equals(openDay)){
						if(sdf.parse(openDay).before(sdf.parse(warrant_appro_date))){
							msg = "第"+m+"行记录权利凭证核发日期不能大于当前日期，无法进行导入！";
							KeyedCollection msgKColl = new KeyedCollection();
							msgKColl.addDataField("msg", msg);
							msgIColl.addDataElement(msgKColl);
							continue;
						}
					}					
					
					if("".equals(keep_org_no_displayname)){
						msg = "第"+m+"行记录保管机构未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					String conditionStr = " where organname = '"+keep_org_no_displayname+"'";
					IndexedCollection inIColl = dao.queryList("SOrg", conditionStr, connection);
					if(inIColl.size()<1){
						msg = "第"+m+"行记录保管机构录入有误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else{
						KeyedCollection SOrgKColl = (KeyedCollection) inIColl.get(0);
						keep_org_no = (String) SOrgKColl.getDataValue("organno");	
					}
					
					if("".equals(hand_org_no_displayname)){
						msg = "第"+m+"行记录经办机构未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					String conditionStr1 = " where organname = '"+hand_org_no_displayname+"'";
					IndexedCollection inIColl1 = dao.queryList("SOrg", conditionStr1, connection);
					if(inIColl1.size()<1){
						msg = "第"+m+"行记录经办机构录入有误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}else{
						KeyedCollection SOrgKColl1 = (KeyedCollection) inIColl1.get(0);
						hand_org_no = (String) SOrgKColl1.getDataValue("organno");	
					}
					
					//校验权证编号是否唯一
					Map<String,String> map = new HashedMap();
					map.put("warrant_no", warrant_no);
					map.put("warrant_type",warrant_type);
					KeyedCollection kc = dao.queryAllDetail("MortGuarantyCertiInfo", map, this.getConnection(context));
					if(!(null==(String)kc.getDataValue("warrant_no"))){
						msg = "第"+m+"行记录权证类型下的权证编号已经存在，请重新录入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}					
					
					
					kColl.put("guaranty_no", guaranty_no);
					kColl.put("warrant_cls", warrant_cls);
					kColl.put("warrant_type", warrant_type);
					kColl.put("is_main_warrant", "2");
					kColl.put("warrant_no", warrant_no);
					kColl.put("warrant_name", warrant_name);
					kColl.put("warrant_appro_unit", warrant_appro_unit);
					kColl.put("warrant_appro_date", warrant_appro_date);
					kColl.put("warrant_trem", warrant_trem);
					kColl.put("keep_org_no_displayname", keep_org_no_displayname);
					kColl.put("hand_org_no_displayname", hand_org_no_displayname);
					kColl.put("keep_org_no", keep_org_no);
					kColl.put("hand_org_no", hand_org_no);
					kColl.put("warrant_state", "1");
					
					kColl.setName(modelId);
					dao.insert(kColl, connection);
					
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
