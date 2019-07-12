package com.yucheng.cmis.biz01line.fnc.op.importreports;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class SaveAssetAndDebtOp extends CMISOperation {

	/**
	 * 保存资产负债表
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			IndexedCollection currentAndDepositList = null;
			IndexedCollection accountReceivableList = null;
			IndexedCollection accountPrePayableList = null;
			IndexedCollection stockList = null;
			IndexedCollection fixedAssetList = null;
			IndexedCollection otherAssetList = null;
			IndexedCollection otherAssetAndNontableAssetList = null;
			IndexedCollection houseList = null;
			IndexedCollection motorCarList = null;
			IndexedCollection bankAccountList = null;
			IndexedCollection payableVendorAccountList = null;
			IndexedCollection prereceiveableAccountList = null;
			IndexedCollection bankDebtList = null;
			IndexedCollection payableOtherAccountList = null;
			KeyedCollection other = null;
			KeyedCollection tableInfo = null;
			
			try {
				other = (KeyedCollection)context.getDataElement("Other");
				tableInfo = (KeyedCollection)context.getDataElement("TableInfo");
				currentAndDepositList = (IndexedCollection)context.getDataElement("CurrentAndDepositList");
				accountReceivableList = (IndexedCollection)context.getDataElement("AccountReceivableList");
				accountPrePayableList = (IndexedCollection)context.getDataElement("AccountPrePayableList");
				stockList = (IndexedCollection)context.getDataElement("StockList");
				fixedAssetList = (IndexedCollection)context.getDataElement("FixedAssetList");
				otherAssetList = (IndexedCollection)context.getDataElement("OtherAssetList");
				otherAssetAndNontableAssetList = (IndexedCollection)context.getDataElement("OtherAssetAndNontableAssetList");
				houseList = (IndexedCollection)context.getDataElement("HouseList");
				motorCarList = (IndexedCollection)context.getDataElement("MotorCarList");
				bankAccountList = (IndexedCollection)context.getDataElement("BankAccountList");
				payableVendorAccountList = (IndexedCollection)context.getDataElement("PayableVendorAccountList");
				prereceiveableAccountList = (IndexedCollection)context.getDataElement("PrereceiveableAccountList");
				bankDebtList = (IndexedCollection)context.getDataElement("BankDebtList");
				payableOtherAccountList = (IndexedCollection)context.getDataElement("PayableOtherAccountList");
				
				
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection tempKcoll = null;
			
			if(currentAndDepositList != null){
				for(int i=0;i<currentAndDepositList.size();++i){
					tempKcoll = (KeyedCollection)currentAndDepositList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(accountReceivableList != null){
				for(int i=0;i<accountReceivableList.size();++i){
					tempKcoll = (KeyedCollection)accountReceivableList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(accountPrePayableList != null){
				for(int i=0;i<accountPrePayableList.size();++i){
					tempKcoll = (KeyedCollection)accountPrePayableList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(stockList != null){
				for(int i=0;i<stockList.size();++i){
					tempKcoll = (KeyedCollection)stockList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(fixedAssetList != null){
				for(int i=0;i<fixedAssetList.size();++i){
					tempKcoll = (KeyedCollection)fixedAssetList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(otherAssetList != null){
				for(int i=0;i<otherAssetList.size();++i){
					tempKcoll = (KeyedCollection)otherAssetList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(otherAssetAndNontableAssetList != null){
				for(int i=0;i<otherAssetAndNontableAssetList.size();++i){
					tempKcoll = (KeyedCollection)otherAssetAndNontableAssetList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(houseList != null){
				for(int i=0;i<houseList.size();++i){
					tempKcoll = (KeyedCollection)houseList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(motorCarList != null){
				for(int i=0;i<motorCarList.size();++i){
					tempKcoll = (KeyedCollection)motorCarList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(bankAccountList != null){
				for(int i=0;i<bankAccountList.size();++i){
					tempKcoll = (KeyedCollection)bankAccountList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(payableVendorAccountList != null){
				for(int i=0;i<payableVendorAccountList.size();++i){
					tempKcoll = (KeyedCollection)payableVendorAccountList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(prereceiveableAccountList != null){
				for(int i=0;i<prereceiveableAccountList.size();++i){
					tempKcoll = (KeyedCollection)prereceiveableAccountList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(bankDebtList != null){
				for(int i=0;i<bankDebtList.size();++i){
					tempKcoll = (KeyedCollection)bankDebtList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(payableOtherAccountList != null){
				for(int i=0;i<payableOtherAccountList.size();++i){
					tempKcoll = (KeyedCollection)payableOtherAccountList.get(i);
					tempKcoll.setName("IqpMeFncBs");
					dao.update(tempKcoll, connection);
				}
			}
			
			if(other != null){
				other.setName("IqpMeFncBs");
				dao.update(other, connection);
			}
			
			if(tableInfo != null){
				tableInfo.setName("IqpMeFncBs");
				dao.update(tableInfo, connection);
			}
			
			
			context.put("flag", "success");
		}catch (EMPException ee) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "更新资产负债表失败！");
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "更新资产负债表失败！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
