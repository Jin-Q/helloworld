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
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.op.conOtherSys.EsbReportTool;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusDeptInItemDetailListOp  extends CMISOperation {
	
	private final String modelId = "CusDeptInItem";
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			IndexedCollection iColl = null;
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			int size = 5;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			String acc_no = null;
			try {
				acc_no = (String)context.getDataValue("acc_no");
			} catch (Exception e) {}
			if(acc_no == null || acc_no.length() == 0)
				throw new EMPJDBCException("The value [acc_no] cannot be null!");

			iColl = getAcctInfoDetList(acc_no, pageInfo, context);
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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
	public IndexedCollection getAcctInfoDetList(String acc_no,PageInfo pageInfo, Context context) throws Exception {
		CompositeData cd = null;
		IndexedCollection iColl = new IndexedCollection("CusDeptInItemDetailList");
		KeyedCollection kColl = null;
		StringBuffer sb = new StringBuffer();//记录日志信息
		sb.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------\n");
		//组装报文头
		cd = EsbReportTool.produceHead("11003000001", "06", context);
		CompositeData body_struct = new CompositeData();
		Array dai_array = new Array();
		
		Field acctNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
		acctNo.setValue(acc_no);
		body_struct.addField("ACCT_NO", acctNo);
		cd.addStruct("BODY", body_struct);
		
		//设置分页信息
		CompositeData appHead_struct = cd.getStruct("APP_HEAD");
		Field totalNum = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		totalNum.setValue(pageInfo.pageSize+"");
		appHead_struct.addField("TOTAL_NUM", totalNum);
		
		Field currentNum = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		//计算当前页起始记录数
		int beginIdx = 1;
		if(pageInfo.pageIdx==1){
			beginIdx = 1;
		}else{
			beginIdx = pageInfo.pageSize*(pageInfo.pageIdx-1)+1;
			if(beginIdx<=0){
				beginIdx = 1;
			}
		}
		currentNum.setValue(beginIdx+"");
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
				CompositeData appHead = resp.getStruct("APP_HEAD");
				String totalRowsR = appHead.getField("TOTAL_ROWS").strValue();//总条数
				pageInfo.setRecordSize(totalRowsR+"");
				
				CompositeData bodyCd = resp.getStruct("BODY");
				dai_array = bodyCd.getArray("ACCT_TRAN_DETAILS_ARRAY");
				for(int i=0;i<dai_array.size();i++){
					CompositeData acctInfo = dai_array.getStruct(i);
					kColl = new KeyedCollection();
					String cus_deposit_typ = acctInfo.getField("DEPOSIT_TYPE").strValue();
					String cus_acc_blc = acctInfo.getField("DEPOSIT_BALANCE").strValue();
					String cus_happen_dt = acctInfo.getField("TRAN_DATE").strValue();
					String cus_biz_typ = acctInfo.getField("TRAN_TYPE").strValue();
					String cus_hpp_amt = acctInfo.getField("TRAN_AMT").strValue();
					String crt_usr_id = acctInfo.getField("REGISTERED_PERSON").strValue();
					String crt_dt = acctInfo.getField("REGISTERED_DATE").strValue();
					if(crt_dt!=null&&!"".equals(crt_dt)){
						crt_dt = crt_dt.substring(0, 4) + "-" + crt_dt.substring(4, 6) + "-" + crt_dt.substring(6);
					}
					
					kColl.addDataField("acc_no", acc_no);//账号
					kColl.addDataField("cus_deposit_typ", cus_deposit_typ);
					kColl.addDataField("cus_acc_blc", cus_acc_blc);
					kColl.addDataField("cus_happen_dt", cus_happen_dt);
					kColl.addDataField("cus_biz_typ", cus_biz_typ);
					kColl.addDataField("cus_hpp_amt", cus_hpp_amt);
					kColl.addDataField("crt_usr_id", crt_usr_id);//登记人
					kColl.addDataField("crt_dt", crt_dt);
					
					iColl.addDataElement(kColl);
				}
			}
			sb.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
		} catch (Exception e) {
			sb.append("-----------------------异常：--end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
			throw e;
		}
		return iColl;
	}
}
