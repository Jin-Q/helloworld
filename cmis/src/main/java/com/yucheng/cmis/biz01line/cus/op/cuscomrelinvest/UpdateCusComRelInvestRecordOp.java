package com.yucheng.cmis.biz01line.cus.op.cuscomrelinvest;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateCusComRelInvestRecordOp extends CMISOperation {

	private final String modelId = "CusComRelInvest";

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
				throw new EMPJDBCException("The values to update[" + modelId + "] cannot be empty!");

			kColl.setDataValue("last_upd_id", context.getDataValue("currentUserId"));
			kColl.setDataValue("last_upd_date", context.getDataValue("OPENDAY"));
			
//			ComponentHelper cHelper = new ComponentHelper();
//			CusComRelInvest cusComRelInvest = new CusComRelInvest();
//			cusComRelInvest = (CusComRelInvest) cHelper.kcolTOdomain(cusComRelInvest, kColl);
//			CusComRelApitalComponent cusComRelApitalComponent = (CusComRelApitalComponent) CMISComponentFactory
//			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMRELAPITAL,context,connection);
//			double zczb = cusComRelApitalComponent.getRegAmt(cusComRelInvest.getCusIdRel());//注册资本
//			double xyzh = cusComRelApitalComponent.getSumInvrtAmt(cusComRelInvest.getCusIdRel());//现有总和
//			double xzzb = cusComRelInvest.getComInvAmt();//本次新增
//			double perc = BigDecimalUtil.div(xzzb, zczb, 4, BigDecimal.ROUND_HALF_UP);
//			if( zczb == BigDecimalUtil.add(xyzh, xzzb , 4, BigDecimal.ROUND_HALF_UP)){
//				perc = BigDecimalUtil.sub(1, cusComRelApitalComponent.getSumPerc(cusComRelInvest.getCusIdRel()), 4, BigDecimal.ROUND_HALF_UP);
//			}
//			kColl.setDataValue("com_inv_perc", perc);
			TableModelDAO dao = this.getTableModelDAO(context);
			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("修改失败！");
			}
			flag = "修改成功";
			/*
			 * 资本构成过渡到被投资客户的资本构成信息
			 */
			KeyedCollection kCollApital = new KeyedCollection("CusComRelApital");
			String cur_type = null;
			String cusId = null;
			String cusIdRel = null;
			String invt_amt = null;
			String inv_date = null;
			String com_invt_desc = null;
			String remark = null;
			String input_id = null;
			String input_date = null;
			String invt_type = null;

			cusIdRel = (String) kColl.getDataValue("cus_id");
			cusId = (String) kColl.getDataValue("cus_id_rel");
			cur_type = (String) kColl.getDataValue("com_inv_cur_typ");
			invt_amt = (String) kColl.getDataValue("com_inv_amt");
			inv_date = (String) kColl.getDataValue("com_inv_dt");
			com_invt_desc = (String) kColl.getDataValue("com_inv_desc");
			remark = (String) kColl.getDataValue("remark");
			invt_type = (String) kColl.getDataValue("com_inv_app");
			input_id = (String) (context.getDataValue("currentUserId"));
			input_date = (String) (context.getDataValue("OPENDAY"));

			CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			CusBase cusBase = new CusBase();
			cusBase = cusBaseComponent.getCusBase(cusIdRel);
			if("BL300".equals(cusBase.getBelgLine())){
				invt_type = "07";
			}else{
				invt_type = "01";
			}
			kCollApital.addDataField("cus_id", cusId);
			kCollApital.addDataField("cus_id_rel", cusIdRel); // 投资企业客户码
			kCollApital.addDataField("invt_type", invt_type);// 出资类型
			kCollApital.addDataField("cur_type", cur_type); // 币别
			kCollApital.addDataField("invt_amt", invt_amt);
//			kCollApital.addDataField("invt_perc", perc);
			kCollApital.addDataField("com_invt_desc", com_invt_desc);
			kCollApital.addDataField("inv_date", inv_date);
			kCollApital.addDataField("remark", remark);
			kCollApital.addDataField("last_upd_id", input_id); // 更新人
			kCollApital.addDataField("last_upd_date", input_date); // 更新日期
			kCollApital.addDataField("rela_type", "1");//默认赋值，此字段在【对外投资中没有】。

			dao.update(kCollApital, connection);
		} catch (EMPException ee) {
			flag = "修改失败";
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
