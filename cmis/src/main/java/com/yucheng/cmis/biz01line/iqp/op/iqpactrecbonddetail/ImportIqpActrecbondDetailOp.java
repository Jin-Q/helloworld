package com.yucheng.cmis.biz01line.iqp.op.iqpactrecbonddetail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;
/**
 * 
*@author wangj
*@time 2015-5-21
*@description TODO 需求编号【HS141110017】保理业务改造
*@version v1.0
*
 */
public class ImportIqpActrecbondDetailOp extends CMISOperation {
	
	 private final String IADModelId = "IqpActrecbondDetail";//应收账款明细表
	 private final String IAMmodelId= "IqpActrecpoMana";//应收账款池管理表
	 private final String IBImodelId = "IqpBuscontInfo";//贸易合同信息
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
            String po_no =(String)context.getDataValue("po_no");  //池编号
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
			String buy_cus_id = "";    //买方客户编号
			String sel_cus_id="";      //卖方客户编号
			String bond_mode = "";     //债权类型     
			String invc_no = "";       //权证号
			String invc_amt = "";      //权证金额  
			String cont_no = "";       //贸易合同编号
			String bond_amt = "";      //债权金额
			String invc_date = "";     //权证日期
			String bond_pay_date = ""; //付款日期    
			String msg = "";
			IndexedCollection msgIColl = new IndexedCollection("MsgList");
			for(int i=0;i<sheets.length;i++){
				sheet = sheets[i];
				cell = sheet.cells;
				for(int m =1; m< cell.length; m++){
					//需新增记录KC
					try{
						buy_cus_id = ((String)cell[m][0].cellvalue).trim();
						sel_cus_id= ((String)cell[m][1].cellvalue).trim();
						bond_mode = ((String)cell[m][2].cellvalue).trim();
						invc_no = ((String)cell[m][3].cellvalue).trim();
						invc_amt = ((String)cell[m][4].cellvalue).trim();
						cont_no = ((String)cell[m][5].cellvalue).trim();
						bond_amt = ((String)cell[m][6].cellvalue).trim();						
						invc_date = ((String)cell[m][7].cellvalue).trim();
						bond_pay_date = ((String)cell[m][8].cellvalue).trim();
					}catch (Exception e) {
					}
					String matchAmt="^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,})?$";
					String matchStr = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
					if("".equals(buy_cus_id)){
						msg = "第"+m+"行记录买方客户码未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(sel_cus_id)){
						msg = "第"+m+"行记录卖方客户码未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(bond_mode)){//债权类型     
						bond_mode = "";
					}else{
						bond_mode = bond_mode.substring(0,1);
					}		
					
					if("".equals(bond_mode)){
						msg = "第"+m+"行记录债权类型未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(invc_no)){
						msg = "第"+m+"行记录权证号未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(invc_amt)||!invc_amt.matches(matchAmt)){
						msg = "第"+m+"行记录权证金额未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(cont_no)){
						msg = "第"+m+"行记录贸易合同编号未录入，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(bond_amt)||!bond_amt.matches(matchAmt)){
						msg = "第"+m+"行记录债权金额未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					if("".equals(invc_date)||!invc_date.matches(matchStr)){
						msg = "第"+m+"行记录权证日期未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					if("".equals(bond_pay_date)||!bond_pay_date.matches(matchStr)){
						msg = "第"+m+"行记录付款日期未录入/格式出错，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					KeyedCollection buyCusInfo=dao.queryDetail("CusBase", buy_cus_id, connection);
					if(buyCusInfo==null||buyCusInfo.getDataValue("cus_id")==null){
						msg = "第"+m+"行记录买方客户号输入错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					KeyedCollection selCusInfo=dao.queryDetail("CusBase", sel_cus_id, connection);
					if(selCusInfo==null||selCusInfo.getDataValue("cus_id")==null){
						msg = "第"+m+"行记录卖方客户号输入错误，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					Map pkMap1 = new HashMap();
					pkMap1.put("po_no", po_no);
					pkMap1.put("tcont_no", cont_no);
					KeyedCollection IBIkColl = dao.queryDetail(IBImodelId, pkMap1,connection);
					if (IBIkColl == null||IBIkColl.getDataValue("po_no") == null||"".equals(IBIkColl.getDataValue("po_no"))) {
						msg = "第"+m+"行记录贸易合同编号无法导入，请在贸易合同详细信息页签新增相关信息！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}
					
					Map pkMap2 = new HashMap();
					pkMap2.put("cont_no", cont_no);
					pkMap2.put("invc_no", invc_no);
					KeyedCollection kCollQuery = dao.queryDetail(IADModelId, pkMap2,connection);
					if (kCollQuery != null&& kCollQuery.getDataValue("invc_no") != null&&!"".equals(kCollQuery.getDataValue("invc_no"))) {
						msg = "第"+m+"行记录应收账款明细信息已存在，无法进行导入！";
						KeyedCollection msgKColl = new KeyedCollection();
						msgKColl.addDataField("msg", msg);
						msgIColl.addDataElement(msgKColl);
						continue;
					}

					KeyedCollection kColl = new KeyedCollection();
					kColl.setName(IADModelId);
					kColl.put("po_no", po_no);//池编号
					kColl.put("sel_cus_id", sel_cus_id);//卖方客户编号
					kColl.put("sel_cus_name", selCusInfo.getDataValue("cus_name"));//卖方客户名称
					kColl.put("buy_cus_id", buy_cus_id);//买方客户编号
					kColl.put("buy_cus_name", buyCusInfo.getDataValue("cus_name"));//卖方客户名称
					kColl.put("bond_mode", bond_mode);//债权类型     
					kColl.put("invc_no", invc_no); //权证号
					kColl.put("invc_amt", invc_amt);//权证金额  
					kColl.put("cont_no", cont_no); //贸易合同编号
					kColl.put("bond_amt", bond_amt);//债权金额
					kColl.put("invc_date", invc_date); //权证日期
					kColl.put("bond_pay_date", bond_pay_date); //付款日期    
					kColl.put("status", "1"); //状态
					dao.insert(kColl, connection);
					
	    			KeyedCollection kCollMana = null;
	    			kCollMana = dao.queryFirst(IAMmodelId, null, " where po_no = '"
	    					+ po_no + "'", connection);
	    			IqpActrecBondComponent component = new IqpActrecBondComponent();
	    			String sAmt = component.getAllInvcAndBondAmt(po_no, connection);
	    			kCollMana.setDataValue("invc_quant", Integer.parseInt(sAmt
	    					.split("@")[0]));
	    			kCollMana.setDataValue("invc_amt", Double.parseDouble(sAmt
	    					.split("@")[1]));
	    			kCollMana.setDataValue("crd_rgtchg_amt", Double.parseDouble(sAmt
	    					.split("@")[2]));
	    			dao.update(kCollMana, connection);
	                
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
