package com.yucheng.cmis.biz01line.cus.op.cuscomrelapital;

import java.sql.Connection;

import org.jdom.Document;
import org.jdom.Element;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComRelApitalComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComRelApital;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCusComRelApitalRecordOp extends CMISOperation {
	
	private final String modelId = "CusComRelApital";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		ComponentHelper cHelper = new ComponentHelper();
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			CusComRelApital cusComRelApital = new CusComRelApital();
			cusComRelApital = (CusComRelApital) cHelper.kcolTOdomain(cusComRelApital, kColl);
			CusComRelApitalComponent cusComRelApitalComponent = (CusComRelApitalComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMRELAPITAL,context,connection);
			
//			double zczb = cusComRelApitalComponent.getRegAmt(cusComRelApital.getCusId());//注册资本
//			double xyzh = cusComRelApitalComponent.getSumInvrtAmt(cusComRelApital.getCusId());//现有总和
//			double xzzb = cusComRelApital.getInvtAmt();//本次新增
//			double perc = BigDecimalUtil.div(xzzb, zczb, 4, BigDecimal.ROUND_HALF_UP);
//			if( zczb == BigDecimalUtil.add(xyzh, xzzb , 4, BigDecimal.ROUND_HALF_UP)){
//				perc = BigDecimalUtil.sub(1, cusComRelApitalComponent.getSumPerc(cusComRelApital.getCusId()), 4, BigDecimal.ROUND_HALF_UP);
//			}
//			cusComRelApital.setInvtPerc(perc);
			String strReturn = cusComRelApitalComponent.addCusComRelApital(cusComRelApital);
			if(strReturn.equals(CMISMessage.ADDSUCCEESS)){
				flag = "新增成功";	
			}
			
			//保存历史信息
			String cus_id_api = (String)kColl.getDataValue("cus_id_rel");//股东客户码
			String invt_name = (String)kColl.getDataValue("invt_name");//股东客名称
			String newStr = "股东 "+invt_name+"["+cus_id_api+"]由["+context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME)+"]新增";
			saveHistory("", newStr, (String)kColl.getDataValue("cus_id"), context, connection);
			
			if(flag.equals("新增成功")){
				/*
				 * 资本构成过渡到投资客户的对外投资信息
				 * */
				KeyedCollection kCollInvest=new KeyedCollection("CusComRelInvest");
				String cur_type=null;
				String cusId = null;
				String cusIdRel = null;
				String invt_amt=null;
				String inv_date=null;
              
				String com_invt_desc=null;
				String remark=null;
				String input_id=null;
				String input_date=null;
				String input_br_id=null;
				String invt_type = null;
				
				cusIdRel=(String)kColl.getDataValue("cus_id");
				cusId=(String)kColl.getDataValue("cus_id_rel");
				cur_type=(String)kColl.getDataValue("cur_type");
				invt_amt=(String)kColl.getDataValue("invt_amt");
				inv_date=(String)kColl.getDataValue("inv_date");
				com_invt_desc=(String)kColl.getDataValue("com_invt_desc");
				remark=(String)kColl.getDataValue("remark");
				com_invt_desc=(String)kColl.getDataValue("com_invt_desc");
				input_id=(String)(context.getDataValue("currentUserId"));
				input_date=(String)(context.getDataValue("OPENDAY"));
				input_br_id= (String)(context.getDataValue("organNo"));
				invt_type = (String)kColl.getDataValue("invt_type");
				 
				kCollInvest.addDataField("cus_id", cusId);
				kCollInvest.addDataField("cus_id_rel", cusIdRel);	//投资企业客户码
				kCollInvest.addDataField("com_inv_typ", "1");         //投资性质
				kCollInvest.addDataField("com_inv_cur_typ", cur_type); //币别
 
				kCollInvest.addDataField("com_inv_amt", invt_amt);
//				kCollInvest.addDataField("com_inv_perc", perc);
				kCollInvest.addDataField("com_inv_dt", inv_date);
				kCollInvest.addDataField("com_inv_desc", com_invt_desc);	
				kCollInvest.addDataField("remark", remark);
				kCollInvest.addDataField("input_id", input_id); // 登记人
				kCollInvest.addDataField("input_date", input_date); //登记日期
				kCollInvest.addDataField("input_br_id", input_br_id); // 登记机构
				kCollInvest.addDataField("com_inv_app", invt_type);//出资方式
 
				TableModelDAO dao = this.getTableModelDAO(context);
				dao.insert(kCollInvest, connection);
			}
		}catch (EMPException ee) {
			flag = "新增失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
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
