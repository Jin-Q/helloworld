package com.yucheng.cmis.biz01line.cus.op.cuscomrelinvest;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddCusComRelInvestRecordOp extends CMISOperation {

	private final String modelId = "CusComRelInvest";
	private final String modelIdApi = "CusComRelApital";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert[" + modelId+ "] cannot be empty!");

//			ComponentHelper cHelper = new ComponentHelper();
//			CusComRelInvest cusComRelInvest = new CusComRelInvest();
//			cusComRelInvest = (CusComRelInvest) cHelper.kcolTOdomain(cusComRelInvest, kColl);
//			CusComRelApitalComponent cusComRelApitalComponent = (CusComRelApitalComponent) CMISComponentFactory
//			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMRELAPITAL,context,connection);
//			double zczb = cusComRelApitalComponent.getRegAmt(cusComRelInvest.getCusIdRel());//实收注册资金
//			double xyzh = cusComRelApitalComponent.getSumInvrtAmt(cusComRelInvest.getCusIdRel());//现有总和
//			double xzzb = cusComRelInvest.getComInvAmt();//本次新增
//			double perc = BigDecimalUtil.div(xzzb, zczb, 4, BigDecimal.ROUND_HALF_UP);
//			if( zczb == BigDecimalUtil.add(xyzh, xzzb , 4, BigDecimal.ROUND_HALF_UP)){
//				perc = BigDecimalUtil.sub(1, cusComRelApitalComponent.getSumPerc(cusComRelInvest.getCusIdRel()), 4, BigDecimal.ROUND_HALF_UP);
//			}
//			kColl.setDataValue("com_inv_perc", perc);
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			//新增资本构成信息
			KeyedCollection kCollApi = new KeyedCollection();
			kCollApi.setName(modelIdApi);
			kCollApi.addDataField("cus_id", kColl.getDataValue("cus_id_rel"));
			kCollApi.addDataField("cus_id_rel", kColl.getDataValue("cus_id"));
			kCollApi.addDataField("invt_amt", kColl.getDataValue("com_inv_amt"));
//			kCollApi.addDataField("invt_perc", perc);
			kCollApi.addDataField("inv_date", kColl.getDataValue("com_inv_dt"));
			kCollApi.addDataField("input_id", kColl.getDataValue("input_id"));
			kCollApi.addDataField("input_br_id", kColl.getDataValue("input_br_id"));
			kCollApi.addDataField("input_date", kColl.getDataValue("input_date"));
			kCollApi.addDataField("invt_type", kColl.getDataValue("com_inv_app"));
			kCollApi.addDataField("cur_type", kColl.getDataValue("com_inv_cur_typ"));
			kCollApi.addDataField("rela_type", "1");//默认赋值，此字段在【对外投资中没有】。

			dao.insert(kCollApi, connection);
			flag = "新增成功";
		} catch (EMPException ee) {
			flag = "新增失败";
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag", flag);
		return "0";
	}
}
