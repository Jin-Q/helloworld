package com.yucheng.cmis.biz01line.cus.op.cuscomrelapital;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TranslateDic;

public class UpdateCusComRelApitalRecordOp extends CMISOperation {

	private final String modelId = "CusComRelApital";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "修改成功";
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelId+ "] cannot be empty!");

			kColl.setDataValue("last_upd_id", context.getDataValue("currentUserId"));
			kColl.setDataValue("last_upd_date", context.getDataValue("OPENDAY"));
//			kColl.setDataValue("status", "1");
//			kColl.setDataValue("cus_status", "1");

//			CusComRelApital cusComRelApital = new CusComRelApital();
//			ComponentHelper cHelper = new ComponentHelper();
//			cusComRelApital = (CusComRelApital) cHelper.kcolTOdomain(cusComRelApital, kColl);
//
//			CusComRelApitalComponent cusComRelApitalComponent = (CusComRelApitalComponent) CMISComponentFactory
//					.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMRELAPITAL, context, connection);
//			
//			double zczb = cusComRelApitalComponent.getRegAmt(cusComRelApital.getCusId());//注册资本
//			double xzzb = cusComRelApital.getInvtAmt();//本次新增
//			double perc = BigDecimalUtil.div(xzzb, zczb, 4, BigDecimal.ROUND_HALF_UP);
//			kColl.setDataValue("invt_perc", perc);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection kCollBase = dao.queryDetail("CusBase", (String)kColl.getDataValue("cus_id"), connection);
			String cus_status = (String)kCollBase.getDataValue("cus_status");
			if(cus_status!=null&&!"".equals(cus_status)&&"20".equals(cus_status)){//正式客户保存修改历史
				//保存历史
				Map<String, String> pkValues = new HashMap<String, String>();
				pkValues.put("cus_id", (String)kColl.getDataValue("cus_id"));
				pkValues.put("cus_id_rel", (String)kColl.getDataValue("cus_id_rel"));
				
				KeyedCollection kCollDic = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
				TranslateDic trans = new TranslateDic();
				KeyedCollection kCollApi = dao.queryDetail(modelId, pkValues, connection);
				if(!kCollApi.getDataValue("cur_type").equals(kColl.getDataValue("cur_type"))||!kCollApi.getDataValue("invt_amt").equals(kColl.getDataValue("invt_amt"))){
					CusBaseComponent cbc = (CusBaseComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
					CusBase cusBase = cbc.getCusBase((String)kColl.getDataValue("cus_id_rel"));
					String oldStr = "股东"+cusBase.getCusName()+"出资币种["+trans.getCnnameByOpttypeAndEnname(kCollDic, "STD_ZX_CUR_TYPE", (String)kCollApi.getDataValue("cur_type"))+"]出资金额["+kCollApi.getDataValue("invt_amt")+"]";
					String newStr = "股东"+cusBase.getCusName()+"出资币种["+trans.getCnnameByOpttypeAndEnname(kCollDic, "STD_ZX_CUR_TYPE", (String)kColl.getDataValue("cur_type"))+"]出资金额["+kColl.getDataValue("invt_amt")+"]";
					saveHistory(oldStr, newStr, (String)kColl.getDataValue("cus_id"), context, connection);
				}
			}
			
			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("修改资本构成失败！");
			}
			flag = "修改成功";

			/*
			 * 资本构成过渡到投资客户的对外投资信息
			 */
			KeyedCollection kCollInvest = new KeyedCollection("CusComRelInvest");
			String cur_type = null;
			String cusId = null;
			String cusIdRel = null;
			String invt_amt = null;
			String inv_date = null;
			String invt_type = null;
			String com_invt_desc = null;
			String remark = null;
			String input_id = null;
			String input_date = null;

			cusIdRel = (String) kColl.getDataValue("cus_id");
			cusId = (String) kColl.getDataValue("cus_id_rel");
			cur_type = (String) kColl.getDataValue("cur_type");
			invt_amt = (String) kColl.getDataValue("invt_amt");
			inv_date = (String) kColl.getDataValue("inv_date");
			com_invt_desc = (String) kColl.getDataValue("com_invt_desc");
			remark = (String) kColl.getDataValue("remark");
			com_invt_desc = (String) kColl.getDataValue("com_invt_desc");
			input_id = (String) (context.getDataValue("currentUserId"));
			input_date = (String) (context.getDataValue("OPENDAY"));
			invt_type = (String) kColl.getDataValue("invt_type");

			kCollInvest.addDataField("cus_id", cusId);
			kCollInvest.addDataField("cus_id_rel", cusIdRel); // 投资企业客户码
			kCollInvest.addDataField("com_inv_typ", "1"); // 投资性质
			kCollInvest.addDataField("com_inv_cur_typ", cur_type); // 币别
			kCollInvest.addDataField("com_inv_amt", invt_amt);
//			kCollInvest.addDataField("com_inv_perc", perc);
			kCollInvest.addDataField("com_inv_dt", inv_date);
			kCollInvest.addDataField("com_inv_desc", com_invt_desc);
			kCollInvest.addDataField("remark", remark);
			kCollInvest.addDataField("last_upd_id", input_id); // 更新人
			kCollInvest.addDataField("last_upd_date", input_date); // 更新日期
			kCollInvest.addDataField("com_inv_app", invt_type); // 更新出资类型
			dao.update(kCollInvest, connection);
			
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
	
	//保存历史修改信息
	private void saveHistory(String oldName,String newName, String cusId ,Context context, Connection connection) throws EMPException{
		Element root = new Element("columns");
		Element element = new Element("columnname");
		element.setAttribute("id", "股东信息");
		Element subOldElement = new Element("old");//旧值
		subOldElement.addContent(oldName);
		element.addContent(subOldElement);
		Element subNewElement = new Element("new");//新值
		subNewElement.addContent(newName);
		element.addContent(subNewElement);
		root.addContent(element);
		Document doc = new Document(root);
		ModifyHistoryComponent historyComponent = (ModifyHistoryComponent) CMISComponentFactory
		.getComponentFactoryInstance().getComponentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT,context, connection);
		historyComponent.saveHistoryTemp(doc, "CusBase", cusId, "修改");
	}
}
