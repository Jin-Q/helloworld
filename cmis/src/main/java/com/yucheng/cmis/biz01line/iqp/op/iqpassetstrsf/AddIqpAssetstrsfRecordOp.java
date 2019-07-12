package com.yucheng.cmis.biz01line.iqp.op.iqpassetstrsf;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpAssetstrsfRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpAssetstrsf";
	private final String asModelId = "IqpAsset";
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			KeyedCollection kColl = null;
			String asset_no ="";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				asset_no = (String) kColl.getDataValue("asset_no");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			kColl.setDataValue("serno", serno);
			kColl.put("topp_acct_no", kColl.getDataValue("topp_acct_no").toString().trim());
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			//更新批次的状态
			KeyedCollection asKcoll = dao.queryAllDetail(asModelId, asset_no, connection);
			asKcoll.setDataValue("status", "02");//已引用
			dao.update(asKcoll, connection);
			
			//添加授信关系
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			String limit_ind = (String) kColl.getDataValue("limit_ind");
			if(limit_ind!=null&&!limit_ind.equals("")){//循环，一次性额度
				if(limit_ind.equals("2")||limit_ind.equals("3")){
					String agr_no = (String) kColl.getDataValue("limit_acc_no");
					iqpLoanAppComponent.doLmtRelation("01", serno, agr_no);
				}else if(limit_ind.equals("4")){//第三方额度
					String agr_no = (String) kColl.getDataValue("limit_acc_no");
					iqpLoanAppComponent.doLmtRelation("02", serno, agr_no);
				}else{
					iqpLoanAppComponent.deleteLmtRelation(serno);
				}
			}
			
			context.addDataField("serno", serno);
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
