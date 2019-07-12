package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 批量利率调整/账户调整通知（集中作业用）
 * @author zhaozq
 * 
 */
public class Trade1100200008060 extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			
			KeyedCollection app = esbInterface.getReqAppHead(CD);
			String fileName = (String) app.getDataValue("FILE_NAME");
			String filePath = (String) app.getDataValue("FILE_PATH");
			
			FTPUtil.FTP2local(filePath,fileName);//下载文件
			CompositeData reqCD = new CompositeData();
			FTPUtil.getFile2CD(fileName, reqCD);//文件转CD
			
			KeyedCollection kcoll = esbInterface.getReqBody(reqCD);
			IndexedCollection icoll = (IndexedCollection) kcoll.get("Serv1100200008060");
			
			for(int i=0;i<icoll.size();i++){
				KeyedCollection reqBody = (KeyedCollection) icoll.get(i);
				
				DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
				String REPAY_LOAN_NO = (String)reqBody.getDataValue("REPAY_LOAN_NO");//还款账号
				String GEN_GL_NO = (String)reqBody.getDataValue("GEN_GL_NO");//授权编号
				String ACT_YEAR_INT_RATE = reqBody.getDataValue("ACT_YEAR_INT_RATE").toString();//执行年利率
//				String BASE_INT_RATE = (String)reqBody.getDataValue("BASE_INT_RATE");//基准利率
				String INT_RATE_FLT_RATE = reqBody.getDataValue("INT_RATE_FLT_RATE").toString();//利率浮动比
				String INT_RATE_FLOW_SPREAD = reqBody.getDataValue("INT_RATE_FLOW_SPREAD").toString();//利率浮动点差
				String OVERDUE_INT_RATE = reqBody.getDataValue("OVERDUE_INT_RATE").toString();//逾期利率
				String PENALTY_INT_RATE = reqBody.getDataValue("PENALTY_INT_RATE").toString();//违约利率
				
				String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
				String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
				
				//通过授权编号，查询利率调整申请信息
				KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryRateAppByAuthorizeNo", GEN_GL_NO, null, connection);
				if(kColl==null){//为账号调整
					//还款账号调整
					if(REPAY_LOAN_NO != null && REPAY_LOAN_NO.trim().length() > 0){
						SqlClient.update("updateAcctNo", DUEBILL_NO, REPAY_LOAN_NO, null, connection);
					}
				}else{
					//利率调整
					if(ACT_YEAR_INT_RATE != null && ACT_YEAR_INT_RATE.trim().length() > 0){
						Map value = new HashMap();
						value.put("ruling_ir", kColl.getDataValue("ruling_ir"));
						value.put("ir_float_rate", INT_RATE_FLT_RATE);
						value.put("ir_float_point", INT_RATE_FLOW_SPREAD);
						value.put("reality_ir_y", ACT_YEAR_INT_RATE);
						value.put("overdue_rate_y", OVERDUE_INT_RATE);
						value.put("default_rate_y", PENALTY_INT_RATE);
						SqlClient.update("updateAccRateInfo", DUEBILL_NO, value, null, connection);
					}
				}
			}
			EMPLog.log("Trade1100200008060", EMPLog.INFO, 0, "【批量利率调整/账户调整通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【批量利率调整/账户调整通知】交易处理失败，借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("Trade1100200008060", EMPLog.ERROR, 0, "【批量利率调整/账户调整通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
