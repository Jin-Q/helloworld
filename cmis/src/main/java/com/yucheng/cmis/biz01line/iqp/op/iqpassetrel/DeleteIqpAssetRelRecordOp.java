package com.yucheng.cmis.biz01line.iqp.op.iqpassetrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAssetRelRecordOp extends CMISOperation {

	private final String modelId = "IqpAssetRel";
	private final String modelIdMain = "IqpAsset";
	private final String modelIdIqp = "IqpAssetstrsf";

	private final String pk_id_name = "pk_id";
	private final String asset_no_name = "asset_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String pk_id_value = null;
			String asset_no_value = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
				asset_no_value = (String)context.getDataValue(asset_no_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");
			String condition ="where asset_no='"+asset_no_value+"'";	

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, pk_id_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
 		    //删除资产清单后，后台重新计算资产包的资产数量、资产总额和转让总额。
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			double astAmtTotal = 0;//资产总额
		    double tableoverAmtTotal = 0;//转让总额
		    double takeoverTotalInt = 0;//转让利息
			if(iColl != null && iColl.size() > 0){
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kc = (KeyedCollection)iColl.get(i);
					String loanbalTotal = (String)kc.getDataValue("loan_bal");
					String takeoverAmt = (String)kc.getDataValue("takeover_amt");
					String takeoverInt = (String)kc.getDataValue("takeover_int");
					takeoverInt = takeoverInt == null?"0":takeoverInt;
					astAmtTotal += Double.parseDouble(loanbalTotal);
					tableoverAmtTotal += Double.parseDouble(takeoverAmt);
					takeoverTotalInt += Double.parseDouble(takeoverInt);
				}
			}
			KeyedCollection IqpAssetKColl = dao.queryDetail(modelIdMain, asset_no_value, connection);
			IqpAssetKColl.setDataValue("asset_qnt",iColl.size());
			IqpAssetKColl.setDataValue("asset_total_amt", astAmtTotal);//资产总额
			IqpAssetKColl.setDataValue("takeover_total_amt", tableoverAmtTotal);//转让总额
			IqpAssetKColl.setDataValue("takeover_total_int", takeoverTotalInt);//转让利息
			dao.update(IqpAssetKColl, connection);

			//实时更新业务申请中的信息
			IndexedCollection IqpIColl = dao.queryList(modelIdIqp, "where asset_no = '"+asset_no_value+"'", connection);
			if(IqpIColl.size()>0){
				KeyedCollection IqpKColl = (KeyedCollection)IqpIColl.get(0);
				IqpKColl.put("takeover_qnt",iColl.size()); //转让笔数
				IqpKColl.put("asset_total_amt", astAmtTotal);//资产总额
				IqpKColl.put("takeover_total_amt", tableoverAmtTotal);//转让总额
				IqpKColl.put("takeover_int", takeoverTotalInt);//转让利息
				dao.update(IqpKColl, connection);
			}
			
			context.addDataField("flag", "success");
			
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
