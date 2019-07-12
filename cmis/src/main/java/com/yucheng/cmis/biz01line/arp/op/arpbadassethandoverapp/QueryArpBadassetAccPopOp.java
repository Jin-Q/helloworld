package com.yucheng.cmis.biz01line.arp.op.arpbadassethandoverapp;

import java.net.URLDecoder;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.arp.op.pubAction.CheckAssetPreserveOp;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpBadassetAccPopOp extends CMISOperation {

	private final String modelId = "AccLoan";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String outCondition = "";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			if(context.containsKey("outCondition")){
				outCondition = (String) context.getDataValue("outCondition");
			}
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);			
			if(conditionStr.indexOf("WHERE")==-1){
				conditionStr = "where five_class ='50' and acc_status = '1' "+outCondition+" order by bill_no " ;
			}else{
				conditionStr = conditionStr + " and five_class ='50' and acc_status = '1' "+outCondition+" order by bill_no " ;
			}
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));		

			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");			
			IndexedCollection iColl = new IndexedCollection();//贷款台账
			iColl = service.getAccByCondition(conditionStr, context,pageInfo, connection, "6");

			String[] args=new String[] {"cus_id","prd_id","bill_no" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo","ArpBadassetHandoverApp"};
			String[]modelForeign=new String[]{"cus_id","prdid","bill_no"};
			String[] fieldName=new String[]{"cus_name","prdname","approve_status"};
			String[] resultName=new String[]{"cus_id_displayname","prd_id_displayname","handover_status"};
		    //详细信息翻译时调用
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
			SInfoUtils.addSOrgName(iColl, new String[]{"fina_br_id"});
			iColl.setName("AccLoanList");
			
			/*** 外币翻译begin ***/
			CheckAssetPreserveOp cmisOp = new CheckAssetPreserveOp();
			cmisOp.delIcollCurType(iColl, "cur_type", "bill_amt", context);	//借据金额
			cmisOp.delIcollCurType(iColl, "cur_type", "bill_bal", context);	//借据余额
			//cmisOp.delIcollCurType(iColl, "cur_type", "rec_int_accum", context);	//应收利息累计
			//cmisOp.delIcollCurType(iColl, "cur_type", "recv_int_accum", context);	//实收利息累计
			cmisOp.delIcollCurType(iColl, "cur_type", "inner_owe_int", context);	//表内欠息
			cmisOp.delIcollCurType(iColl, "cur_type", "out_owe_int", context);	//表外欠息
			/*** 外币翻译begin ***/
			context.put("outSqlStr",outCondition);
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

}