package com.yucheng.cmis.biz01line.cus.op.cusdeptinitem;

import java.sql.Connection;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.client.exception.TimeoutException;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.op.conOtherSys.EsbReportTool;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;

public class QueryCusDeptInItemDetailOp  extends CMISOperation {
	
	private final String modelId = "CusDeptInItem";
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String acc_no = null;
			try {
				acc_no = (String)context.getDataValue("acc_no");
			} catch (Exception e) {}
			if(acc_no == null || acc_no.length() == 0)
				throw new EMPJDBCException("The value [acc_no] cannot be null!");

			kColl = getAcctInfoDet(acc_no, context);
			
			this.putDataElement2Context(kColl, context);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	/**
	 * 根据账号查询账号明细信息
	 * @param acc_no
	 * @return
	 * @throws Exception 
	 * @throws TimeoutException
	 * @throws InvalidArgumentException
	 * @throws DuplicatedDataNameException
	 */
	public KeyedCollection getAcctInfoDet(String acc_no, Context context) throws Exception {
		CompositeData cd = null;
		KeyedCollection kColl = new KeyedCollection("CusDeptInItem");
		StringBuffer sb = new StringBuffer();//记录日志信息
		sb.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------\n");
		
		//组装报文头
		cd = EsbReportTool.produceHead("11003000001", "05", context);
		CompositeData body_struct = new CompositeData();
		Array dai_array = new Array();
		
		Field acctNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
		acctNo.setValue(acc_no);
		body_struct.addField("ACCT_NO", acctNo);
		
		Field clientNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
		clientNo.setValue("");
		body_struct.addField("CLIENT_NO", clientNo);
		cd.addStruct("BODY", body_struct);
		
		//设置分页信息
		CompositeData appHead_struct = cd.getStruct("APP_HEAD");
		Field totalNum = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		totalNum.setValue("5");
		appHead_struct.addField("TOTAL_NUM", totalNum);
		
		Field currentNum = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		currentNum.setValue("1");
		appHead_struct.addField("CURRENT_NUM", currentNum);
		
		sb.append("**********************请求报文开始************************\n");
		sb.append(cd);//记录请求报文
		sb.append("**********************请求报文结束************************\n");
		CompositeData resp;
		try {
			resp = ESBClient.request(cd);
			//发送报文
			sb.append("**********************接收报文开始************************\n");
			sb.append(resp);//记录返回报文
			sb.append("**********************接收报文结束************************\n");
			
			//判断交易是否成功
			String retStatus = resp.getStruct("SYS_HEAD").getField("RET_STATUS").strValue();
			String retCode = resp.getStruct("SYS_HEAD").getArray("RET").getStruct(0).getField("RET_CODE").strValue();
			if ("S".equals(retStatus) && "000000".equals(retCode)){
				CompositeData bodyCd = resp.getStruct("BODY");
				dai_array = bodyCd.getArray("DEPOSIT_ACCT_INFO_ARRAY");
				if(dai_array.size()>0){
					CompositeData acctInfo = dai_array.getStruct(0);
					String seq = acctInfo.getField("SEQUENCE_NO").strValue();
	//				String acc_no = acctInfo.getField("ACCT_NO").strValue();
					String cus_typ = acctInfo.getField("CLIENT_TYPE").strValue();
					String cus_bch_id = acctInfo.getField("OPEN_ACCT_BRANCH_ID").strValue();
					String cus_open_dt = acctInfo.getField("OPEN_ACCT_DATE").strValue();
					if(cus_open_dt!=null&&!"".equals(cus_open_dt)){
						cus_open_dt = cus_open_dt.substring(0, 4) + "-" + cus_open_dt.substring(4, 6) + "-" + cus_open_dt.substring(6);
					}
					String cus_close_dt = acctInfo.getField("CANCEL_ACCT_DATE").strValue();
					if(cus_close_dt!=null&&!"".equals(cus_close_dt)){
						cus_close_dt = cus_close_dt.substring(0, 4) + "-" + cus_close_dt.substring(4, 6) + "-" + cus_close_dt.substring(6);
					}
					String cus_acc_cur_typ = acctInfo.getField("CCY").strValue();
					String cus_acc_typ = acctInfo.getField("ACCT_TYPE").strValue();
					String cus_acc_cls = acctInfo.getField("GL_CODE").strValue();
					String cus_acc_st = acctInfo.getField("ACCT_STAT").strValue();
					String cus_deposit_typ = acctInfo.getField("DEPOSIT_TYPE").strValue();
					String cus_acc_blc = acctInfo.getField("DEPOSIT_BALANCE").strValue();
					String cus_end_dt = acctInfo.getField("EXPIRY_DATE").strValue();
					if(cus_end_dt!=null&&!"".equals(cus_end_dt)){
						cus_end_dt = cus_end_dt.substring(0, 4) + "-" + cus_end_dt.substring(4, 6) + "-" + cus_end_dt.substring(6);
					}
					String cus_card_no = acctInfo.getField("CARD_NO").strValue();
					String cert_typ = acctInfo.getField("GLOBAL_TYPE").strValue();
					String cert_code = acctInfo.getField("GLOBAL_ID").strValue();
					String cus_last_m_avg_day = acctInfo.getField("LAST_MON_AVG_BAL").strValue();
					String cus_high_amt = acctInfo.getField("HIS_MAX_AMT").strValue();
					String cus_low_amt = acctInfo.getField("HIS_MIN_AMT").strValue();
					String cus_last_y_avg_day = acctInfo.getField("LAST_YEAR_AVG_BAL").strValue();
					String cus_plot = acctInfo.getField("THIS_YEAR_ACCRUNCE").strValue();
					String cus_avg_day = acctInfo.getField("THIS_YEAR_AVG_BAL").strValue();
					String cus_last_plot = acctInfo.getField("LAST_YEAR_ACCRUNCE").strValue();
					String cus_avg_day_cr = acctInfo.getField("YEAR_AVG_BAL_RATE").strValue();
					String cus_happen_dt = acctInfo.getField("DATA_DATE").strValue();
					if(cus_happen_dt!=null&&!"".equals(cus_happen_dt)){
						cus_happen_dt = cus_happen_dt.substring(0, 4) + "-" + cus_happen_dt.substring(4, 6) + "-" + cus_happen_dt.substring(6);
					}
					
					kColl.addDataField("seq", seq);//序号
					kColl.addDataField("acc_no", acc_no);//账号
					kColl.addDataField("cus_typ", cus_typ);
					kColl.addDataField("cus_bch_id", cus_bch_id);
					kColl.addDataField("cus_open_dt", cus_open_dt);
					kColl.addDataField("cus_close_dt", cus_close_dt);
					kColl.addDataField("cus_acc_cur_typ", EsbReportTool.changeEcifCurType(cus_acc_cur_typ));
					kColl.addDataField("cus_acc_typ", cus_acc_typ);
					kColl.addDataField("cus_acc_cls", cus_acc_cls);
					kColl.addDataField("cus_acc_st", cus_acc_st);
					kColl.addDataField("cus_deposit_typ", cus_deposit_typ);
					kColl.addDataField("cus_acc_blc", cus_acc_blc);
					kColl.addDataField("cus_end_dt", cus_end_dt);
					kColl.addDataField("cus_card_no", cus_card_no);
					kColl.addDataField("cert_typ", EsbReportTool.changeEcifCertType(cert_typ));
					kColl.addDataField("cert_code", cert_code);
					kColl.addDataField("cus_last_m_avg_day", cus_last_m_avg_day);
					kColl.addDataField("cus_high_amt", cus_high_amt);
					kColl.addDataField("cus_low_amt", cus_low_amt);
					kColl.addDataField("cus_last_y_avg_day", cus_last_y_avg_day);
					kColl.addDataField("cus_plot", cus_plot);
					kColl.addDataField("cus_avg_day", cus_avg_day);
					kColl.addDataField("cus_last_plot", cus_last_plot);
					kColl.addDataField("cus_avg_day_cr", cus_avg_day_cr);
					kColl.addDataField("cus_happen_dt", cus_happen_dt);
				}
			}
			sb.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
		} catch (Exception e) {
			sb.append("-----------------------异常：--end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
			throw e;
		}
		return kColl;
	}
}
