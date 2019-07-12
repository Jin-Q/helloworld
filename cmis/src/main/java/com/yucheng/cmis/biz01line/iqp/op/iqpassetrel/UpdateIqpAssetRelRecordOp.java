package com.yucheng.cmis.biz01line.iqp.op.iqpassetrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpAssetRelRecordOp extends CMISOperation {
	

	private final String modelId = "IqpAssetRel";
	private final String modelIdMain = "IqpAsset";
	private final String modelIdIqp = "IqpAssetstrsf";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			String asset_no = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				asset_no = (String)kColl.getDataValue("asset_no");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			
			//更新资产后清单后，后台自动计算资产包中的数量、资产总额和转让总额。
			String condition ="where asset_no='"+asset_no+"'";
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
					if(takeoverInt == null){
						takeoverInt = "0";
					}
					astAmtTotal += Double.parseDouble(loanbalTotal);
					tableoverAmtTotal += Double.parseDouble(takeoverAmt);
					takeoverTotalInt += Double.parseDouble(takeoverInt);
				}
			}
			KeyedCollection IqpAssetKColl = dao.queryDetail(modelIdMain, asset_no, connection);
			IqpAssetKColl.setDataValue("asset_qnt",iColl.size()); 
			IqpAssetKColl.setDataValue("asset_total_amt", astAmtTotal);//资产总额
			IqpAssetKColl.setDataValue("takeover_total_amt", tableoverAmtTotal);//转让总额
			IqpAssetKColl.setDataValue("takeover_total_int", takeoverTotalInt);//转让利息
			dao.update(IqpAssetKColl, connection);
			
			//实时更新业务申请中的信息
			IndexedCollection IqpIColl = dao.queryList(modelIdIqp, "where asset_no = '"+asset_no+"'", connection);
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
