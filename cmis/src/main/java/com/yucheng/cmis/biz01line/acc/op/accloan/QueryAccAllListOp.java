package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryAccAllListOp extends CMISOperation {

	private final String modelId = "AccView";
	private final String modelIdCtr = "CtrLoanCont";
	private final String modelIdAccLoan = "AccLoan";
	private final String modelIdAccAccp = "AccAccp";
	private final String modelIdAccDrft = "AccDrft";
	private final String modelIdAccAssetstrsf = "AccAssetstrsf";
	private final String modelIdAccPad = "AccPad";
	private final String bill_name = "bill_no";
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String flag = "AccLoan";
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = new KeyedCollection();
			KeyedCollection kCollAcc = new KeyedCollection();
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String bill_no = null;
			String menuId = null;
			String biz_type = "";
			String isAccPad = null;
			String condition ="";
			try {
				bill_no = (String)context.getDataValue(bill_name);
			} catch (Exception e) {}
			if(bill_no == null || bill_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+bill_name+"] cannot be null!");
		    
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//如果是需要查询垫款台账
			if(context.containsKey("isAccPad")){
				isAccPad = (String)context.getDataValue("isAccPad");
			}
			condition = "where bill_no='"+bill_no+"'";
			
			IndexedCollection iColl = (IndexedCollection)dao.queryList(modelId, condition, connection);
			if(iColl.size()>0){
				if(iColl.size()>1){
					if("is".equals(isAccPad)){
						kColl = (KeyedCollection)iColl.get(1);
					}else{
						String conditionStr = "where bill_no='"+bill_no+"' and table_model!='AccLoan'";
						IndexedCollection iCollSelect = (IndexedCollection)dao.queryList(modelId, conditionStr, connection);
						if(iCollSelect.size()>0){
							kColl = (KeyedCollection)iCollSelect.get(0);	
						}else{
							throw new Exception("该笔借据台账异常！");
						}
					}
				}else{
					kColl = (KeyedCollection)iColl.get(0);
				}
				String table_model ="";
				if(context.containsKey("table_model")){
					table_model = (String)context.getDataValue("table_model");	
				}else{
					table_model = (String)kColl.getDataValue("table_model");
				}
				if("AccLoan".equals(table_model)){//贷款台账
					//and cont_no in(select cont_no from ctr_loan_cont where is_trust_loan='1'
					kCollAcc = (KeyedCollection)dao.queryDetail(modelIdAccLoan, bill_no, connection);
					String cont_no = (String)kCollAcc.getDataValue("cont_no");
					KeyedCollection kCollCont = (KeyedCollection)dao.queryDetail(modelIdCtr, cont_no, connection);
					biz_type = (String)kCollCont.getDataValue("biz_type");
					String is_trust_loan = (String)kCollCont.getDataValue("is_trust_loan");
					if("1".equals(is_trust_loan)){//是否信托贷款
						menuId = "isTrustAccLoanList";//信托贷款台账
					}else{
						menuId = "AccLoanList";//普通贷款
					}
					flag = "AccLoan";
				}else if("AccAccp".equals(table_model)){//银承台帐
					kCollAcc = (KeyedCollection)dao.queryDetail(modelIdAccAccp, bill_no, connection);
					String cont_no = (String)kCollAcc.getDataValue("cont_no");
					KeyedCollection kCollCont = (KeyedCollection)dao.queryDetail(modelIdCtr, cont_no, connection);
					if((String)kCollCont.getDataValue("cont_no") != null && !"".equals((String)kCollCont.getDataValue("cont_no"))){
						biz_type = (String)kCollCont.getDataValue("biz_type");
					}
					menuId = "accAccpList";
					flag = "AccAccp";
				}else if("AccDrft".equals(table_model)){//票据流水台帐
					kCollAcc = (KeyedCollection)dao.queryDetail(modelIdAccDrft, bill_no, connection);
					String cont_no = (String)kCollAcc.getDataValue("cont_no");
					KeyedCollection kCollCont = (KeyedCollection)dao.queryDetail(modelIdCtr, cont_no, connection);
					if((String)kCollCont.getDataValue("cont_no") != null && !"".equals((String)kCollCont.getDataValue("cont_no"))){
						biz_type = (String)kCollCont.getDataValue("biz_type");
					}
					menuId = "billDetailPvpList";
					flag = "AccDrft";
				}else if("AccAssetstrsf".equals(table_model)){//资产转让台账
					kCollAcc = (KeyedCollection)dao.queryDetail(modelIdAccAssetstrsf, bill_no, connection);
					menuId = "accAssetstrsfList";
					flag = "AccAssetstrsf";
				}else{//垫款台账
					kCollAcc = (KeyedCollection)dao.queryDetail(modelIdAccPad, bill_no, connection);
					String cont_no = (String)kCollAcc.getDataValue("cont_no");
					KeyedCollection kCollCont = (KeyedCollection)dao.queryDetail(modelIdCtr, cont_no, connection);
					if((String)kCollCont.getDataValue("cont_no") != null && !"".equals((String)kCollCont.getDataValue("cont_no"))){
						biz_type = (String)kCollCont.getDataValue("biz_type");
					}
					menuId = "accPadList";
					flag = "AccPad";
				}
			}else{
				throw new Exception("此笔借据不存在");
			}
			context.put("menuId", menuId);
			String[] args=new String[] {"cus_id","prd_id" ,"cont_no","repay_type"};
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo","CtrLoanCont","PrdRepayMode"};
			String[]modelForeign=new String[]{"cus_id","prdid","cont_no","repay_mode_id"};
			String[] fieldName=new String[]{"cus_name","prdname","serno","repay_mode_dec"};
			String[] resultName=new String[]{"cus_id_displayname","prd_id_displayname","fount_serno","repay_type_displayname"};
		    //详细信息翻译时调用
		    SystemTransUtils.dealPointName(kCollAcc, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
			SInfoUtils.addSOrgName(kCollAcc, new String[]{"fina_br_id"});  
			SInfoUtils.addSOrgName(kCollAcc, new String[]{"manager_br_id"});
			
			//查询催收记录数如果存在，则设置参数
			String condDun = " where task_serno='"+bill_no+"'";
			IndexedCollection iCollDun = dao.queryList("PspDunningRecord", condDun, connection);
			if(iCollDun.size()>0){
				context.put("dunCount", "Y");
			}
			
			this.putDataElement2Context(kCollAcc, context);
            context.put("biz_type", biz_type);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return flag;
	}

}
