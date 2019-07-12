package com.yucheng.cmis.biz01line.cont.op.ctrassetstrsfcont;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteCtrAssetstrsfContRecordOp extends CMISOperation {

	private final String modelId = "CtrAssetstrsfCont";
	private final String modelIdPvp = "PvpLoanApp";
	private final String modelIdAcc = "AccView";
	private final String modelIdAsset= "IqpAsset";
	

	private final String cont_no_name = "cont_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
            				
			String cont_no_value = null;
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//查询出账表
			//'000':'待发起', '111':'审批中', '990':'取消', '991':'重办', '992':'打回', '993':'追回', '997':'通过', '998':'否决'
			String conditionPvp = "where cont_no='"+cont_no_value+"' and approve_status in('111','997')";
			IndexedCollection iCollPvp = dao.queryList(modelIdPvp, conditionPvp, connection);
			String conditionAcc = "where cont_no='"+cont_no_value+"'";
			//查询台账表(贷款意向，信托贷款不需要出账)
	         IndexedCollection iCollAcc = dao.queryList(modelIdAcc, conditionAcc, connection);
	         if((iCollPvp.size()>0 && iCollPvp != null) || (iCollAcc.size()>0 && iCollAcc != null)){
	        	 context.put("flag", "error");
		         context.put("msg", "存在审批中、通过状态出账或台账记录!");
	         }else{
	        	//跟新合同状态
	        	 KeyedCollection kColl = dao.queryDetail(modelId, cont_no_value, connection);
				 kColl.put("cont_status", "700");//撤销操作把合同状态改为撤销
				 dao.update(kColl, connection);
				//跟新资产包状态
					String addet_no = (String)kColl.getDataValue("asset_no");//资产包编号
					if(addet_no != null && !"".equals(addet_no)){
						KeyedCollection kCollAsset = dao.queryDetail(modelIdAsset, addet_no, connection);
						kCollAsset.put("status", "04");//'01':'登记', '02':'已引用', '03':'已办结','04作废'
						dao.update(kCollAsset, connection);
					}


					//如果存在出账记录，前提是 没有 '111':'审批中', '997':'通过'(前面已判断)
					//则把该出账记录改为 取消 (否决的不用改)
					String conditionStr = "where cont_no='"+cont_no_value+"' and approve_status!='998'";
			        IndexedCollection iCollPvpNeedChange = dao.queryList(modelIdPvp, conditionStr, connection);
			        for(int i=0;i<iCollPvpNeedChange.size();i++){
			        	KeyedCollection kCollPvpNeedChange = (KeyedCollection)iCollPvpNeedChange.get(i);
			        	kCollPvpNeedChange.setDataValue("approve_status", "990");
			        	dao.update(kCollPvpNeedChange, connection);
			        }
					context.put("flag", "success");
					context.put("msg", "合同撤销成功!");
	         }
			
		}catch (EMPException e) {
			context.put("flag", "error");
			context.put("msg", "合同撤销失败!原因:"+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
