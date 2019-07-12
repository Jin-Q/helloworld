package com.yucheng.cmis.biz01line.cus.op.cuscommanager;

import java.sql.Connection;

import org.jdom.Document;
import org.jdom.Element;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComManagerComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComManager;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TranslateDic;

public class AddCusComManagerRecordOp extends CMISOperation {

	private final String modelId = "CusComManager";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "";
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert[" + modelId + "] cannot be empty!");

			String cus_id = (String)kColl.getDataValue("cus_id");
			String cus_id_rel = (String)kColl.getDataValue("cus_id_rel");//高管客户码
			String mrg_name = (String)kColl.getDataValue("com_mrg_name");//高管姓名
			String com_mrg_typ = (String)kColl.getDataValue("com_mrg_typ");//高管类型
			String phone = (String)kColl.getDataValue("com_relate_phone");//高管联系手机  Added by FCL 2015-01-27
			ComponentHelper cHelper = new ComponentHelper();
			CusComManager cusComManager = new CusComManager();
			CusComManager cusComManagerNew = new CusComManager();

			cusComManager = (CusComManager) cHelper.kcolTOdomain(cusComManager,kColl);
			cusComManagerNew = (CusComManager) cusComManager.clone();
			
			CusComManagerComponent cusComManagerComponent = (CusComManagerComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMMANAGER, context, connection);
			String strReturnMessage = cusComManagerComponent.checkExist(cusComManager);
			TableModelDAO dao = this.getTableModelDAO(context);
			if (strReturnMessage.equals("yes")) {
				flag = "该客户下的证件和高管类别对应的高管人已存在，不能新增！";
			} else {
				//如果高管类别是实际控制人、法人代表、总经理、财务负责人、董事长，则只能唯一
				if(com_mrg_typ.equals("01") || com_mrg_typ.equals("02") || com_mrg_typ.equals("03") || com_mrg_typ.equals("04") || com_mrg_typ.equals("07")){
					strReturnMessage = cusComManagerComponent.checkExistByMrgType(cusComManagerNew);
					if (strReturnMessage.equals("no")) {
						//存储历史
						KeyedCollection kCollDic = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
						TranslateDic trans = new TranslateDic();
						String cnName = trans.getCnnameByOpttypeAndEnname(kCollDic, "STD_ZB_MANAGER_TYPE", com_mrg_typ);
						String newStr = "类型为["+cnName+"]的高管 "+mrg_name+"["+cus_id_rel+"]由["+context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME)+"]新增";
						saveHistory("", newStr, cus_id, context, connection);
						// 不存在，可以新增
						dao.insert(kColl, connection);
						flag = "新增成功";
					} else if (strReturnMessage.equals("yes")) {
						// 该资质证书编号+客户码已存在
						//flag = "该高管客户已经存在高管类别是法人代表、总经理、财务负责人、董事长，不能新增！";
						flag = "已经存在此高管类别的客户，不能新增！";
					}
				}else{
					//存储历史
					KeyedCollection kCollDic = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
					TranslateDic trans = new TranslateDic();
					String cnName = trans.getCnnameByOpttypeAndEnname(kCollDic, "STD_ZB_MANAGER_TYPE", com_mrg_typ);
					String newStr = "类型为["+cnName+"]的高管 "+mrg_name+"["+cus_id_rel+"]由["+context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME)+"]新增";
					saveHistory("", newStr, cus_id, context, connection);
					dao.insert(kColl, connection);
					flag = "新增成功";
				}
			}
			//--------------20150127 Edited by FCL 高管手机号码有更新，同步更新到个人基础信息中
			CusIndivComponent cusIndivComponent = (CusIndivComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIV, context, connection);
			CusIndiv cindiv = cusIndivComponent.getCusIndiv(cus_id_rel);
			String ori_mobile = cindiv.getMobile();
			if(phone!=null&&!phone.equals(ori_mobile)){//高管手机号码有更新，同步更新到个人基础信息中
				System.out.println("--------高管手机号有更新------新手机号："+phone+"----旧号码:"+ori_mobile);
				int i = cusIndivComponent.updateMobileByCusId(cus_id_rel, phone);
				if(i>0){
					System.out.println("更新成功");
				}
			}
			//-----------------------END------------------------------------------------
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
	
	//保存历史修改信息
	private void saveHistory(String oldName,String newName, String cusId ,Context context, Connection connection) throws EMPException{
		Element root = new Element("columns");
		Element element = new Element("columnname");
		element.setAttribute("id", "高管信息");
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
