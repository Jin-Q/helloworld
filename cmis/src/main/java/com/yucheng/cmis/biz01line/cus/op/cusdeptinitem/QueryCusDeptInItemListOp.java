package com.yucheng.cmis.biz01line.cus.op.cusdeptinitem;

import java.sql.Connection;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.op.conOtherSys.EsbReportTool;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusDeptInItemListOp extends CMISOperation {
	
	
//	private final String modelId = "CusDeptInItem";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String cusId = "";
		try{
			connection = this.getConnection(context);
		
			cusId = (String)context.getDataValue("CusDeptInItem.cus_id");
			if(cusId==null||"".equals(cusId)){
				throw new Exception("客户码不能为空！");
			}
			
			int size = 5;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			IndexedCollection iColl = getAcctInfo(cusId, pageInfo, context); 
		
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

	public IndexedCollection getAcctInfo(String cusId, PageInfo pageInfo, Context context) throws Exception {
		CompositeData cd = null;
		IndexedCollection iColl = new IndexedCollection();
		iColl.setName("CusDeptInItemList");
		KeyedCollection kColl = null;
		StringBuffer sb = new StringBuffer();//记录日志信息
		sb.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------\n");
		//组装报文头
		cd = EsbReportTool.produceHead("11003000001", "05", context);
		CompositeData body_struct = new CompositeData();
		Array dai_array = new Array();
		
		Field clientNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
		clientNo.setValue(cusId);
		body_struct.addField("CLIENT_NO", clientNo);
		
		Field acctNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
		acctNo.setValue("");
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
	//			int totalNumR = appHead.getField("TOTAL_NUM").intValue();//每页条数
	//			int currentNumR = appHead.getField("CURRENT_NUM").intValue();//当前条数
	//			int totalPageR = appHead.getField("TOTAL_PAGES").intValue();//总页数
				
				pageInfo.setRecordSize(totalRowsR);
				
				CompositeData bodyCd = resp.getStruct("BODY");
				dai_array = bodyCd.getArray("DEPOSIT_ACCT_INFO_ARRAY");
				for(int i=0;i<dai_array.size();i++){
					kColl = new KeyedCollection();
					CompositeData acctInfo = dai_array.getStruct(i);
					String seq = acctInfo.getField("SEQUENCE_NO").strValue();
					String acc_no = acctInfo.getField("ACCT_NO").strValue();
					String cus_bch_id = acctInfo.getField("OPEN_ACCT_BRANCH_ID").strValue();
					String cus_open_dt = acctInfo.getField("OPEN_ACCT_DATE").strValue();
					if(cus_open_dt!=null&&!"".equals(cus_open_dt)){
						cus_open_dt = cus_open_dt.substring(0, 4) + "-" + cus_open_dt.substring(4, 6) + "-" + cus_open_dt.substring(6);
					}
					//币种转为本系统币种
					String cus_acc_cur_typ = acctInfo.getField("CCY").strValue();
					cus_acc_cur_typ = EsbReportTool.changeEcifCurType(cus_acc_cur_typ);
					String cus_acc_blc = acctInfo.getField("DEPOSIT_BALANCE").strValue();
	
					kColl.addDataField("seq", seq);//序号
					kColl.addDataField("acc_no", acc_no);//账号
					kColl.addDataField("cus_bch_id", cus_bch_id);
					kColl.addDataField("cus_open_dt", cus_open_dt);
					kColl.addDataField("cus_acc_cur_typ", cus_acc_cur_typ);
					kColl.addDataField("cus_acc_blc", cus_acc_blc);
					kColl.addDataField("cus_id", cusId);
					iColl.addDataElement(kColl);
				}
			}
			sb.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
		} catch (Exception e) {
			sb.append("-----------------------异常：--end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
			//throw e;
		}
		return iColl;
	}
}
