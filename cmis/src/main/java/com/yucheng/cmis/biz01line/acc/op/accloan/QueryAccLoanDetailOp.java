package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryAccLoanDetailOp  extends CMISOperation {
	
	private final String modelId = "AccLoan";
	private final String modelIdAccAssetstrsf = "AccAssetstrsf";
	private final String modelIdAccAssetTrans = "AccAssetTrans";
	
	private final String bill_name = "bill_no";
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String isSpecialAcc = ""; 
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String bill_no = null;
			try {
				bill_no = (String)context.getDataValue(bill_name);
			} catch (Exception e) {}
			if(bill_no == null || bill_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+bill_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			//判断是否是资产买入或者是资产证券化
			String condition = "where bill_no='"+bill_no+"' and asset_type='02'";
			KeyedCollection kCollAccAssetstrsf = dao.queryDetail(modelIdAccAssetstrsf, bill_no, connection);
			KeyedCollection kCollAccAssetTrans = dao.queryFirst(modelIdAccAssetTrans, null, condition, connection);
			String bill_no_select_4strsf = (String)kCollAccAssetstrsf.getDataValue("bill_no");
			String bill_no_select_4trans = (String)kCollAccAssetTrans.getDataValue("bill_no");
			//资产买入
			if(bill_no_select_4strsf != null && !"".equals(bill_no_select_4strsf)){
				isSpecialAcc = "AccAssetstrsf";
			}
			//资产证券化
			if(bill_no_select_4trans != null && !"".equals(bill_no_select_4trans)){
				isSpecialAcc = "AccAssetTrans";
			}
			KeyedCollection kColl = dao.queryDetail(modelId, bill_no, connection);
			
			String cont_no = (String)kColl.getDataValue("cont_no");
			KeyedCollection kCollCtr = dao.queryDetail("CtrLoanCont", cont_no, connection);
			String biz_type = (String)kCollCtr.getDataValue("biz_type");
			context.put("biz_type", biz_type);
			
			String[] args=new String[] {"cus_id","prd_id" ,"cont_no"};
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo","CtrLoanCont"};
			String[]modelForeign=new String[]{"cus_id","prdid","cont_no"};
			String[] fieldName=new String[]{"cus_name","prdname","serno"};
			String[] resultName=new String[]{"cus_id_displayname","prd_id_displayname","fount_serno"};
		    //详细信息翻译时调用
		    SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
			SInfoUtils.addSOrgName(kColl, new String[]{"fina_br_id"});  
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id"});
			this.putDataElement2Context(kColl, context);
			context.put("isSpecialAcc", isSpecialAcc);
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
