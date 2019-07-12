package com.yucheng.cmis.biz01line.acc.op.accassettrans;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryAccAssetTransListOp extends CMISOperation {


	private final String modelId = "AccAssetTrans";//台账
	
	private final String modelIdTrans = "CtrAssetTransCont";//资产流转协议
	private final String modelIdPro = "CtrAssetProCont";//资产项目管理即证券化合同
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String cont_no = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			String cusId = "";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
				if(queryData.containsKey("cus_id")){
					cusId = (String) queryData.getDataValue("cus_id");
				}
			} catch (Exception e) {}
			
			/**modified by lisj 2015-4-9 按照资产台账按照借据号排序,于2015-4-9上线 begin**/
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by bill_no desc";
			/**modified by lisj 2015-4-9 按照资产台账按照借据号排序,于2015-4-9上线 end**/
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			//modified by jiangcuihua 2019-09-26 客户名称查询条件失效，故作此修改
			String sql = "";
			if(cusId!=null&&!"".equals(cusId)){
				sql = "select * from (select a.*,b.cus_id,b.loan_amt from acc_asset_trans a,acc_loan b where a.bill_no = b.bill_no and b.cus_id = '"+cusId+"') ";
			}else{
				sql = "select * from (select a.*,b.cus_id,b.loan_amt from acc_asset_trans a,acc_loan b where a.bill_no = b.bill_no) ";
			}
			sql = sql + conditionStr;
			
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				cont_no = (String) kColl.getDataValue("cont_no");//获取合同编号
				KeyedCollection kColl2 = (KeyedCollection)dao.queryDetail("CtrAssetTransCont", cont_no, connection);
				if(kColl2 != null&&kColl2.getDataValue("cont_no")!=null){
					 String pro_short_name = (String) kColl2.getDataValue("pro_short_name");
					 kColl.put("pro_short_name", pro_short_name);
				}else{
					kColl2 = (KeyedCollection)dao.queryDetail("CtrAssetProCont", cont_no, connection);
					 String pro_short_name = (String) kColl2.getDataValue("pro_short_name");
					 kColl.put("pro_short_name", pro_short_name);
				}
			}
			
			String[] args=new String[] { "cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			String[] resultName = new String[] { "cus_id_displayname"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			iColl.setName(modelId+"List");
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
