package com.yucheng.cmis.biz01line.cus.op.cuscomrelapital;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteCusComRelApitalRecordOp extends CMISOperation {
	
	private final String modelId = "CusComRelApital";
	
	private final String cus_id_name = "cus_id";
	private final String cus_id_rel_name = "cus_id_rel";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
				
			String cus_id_rel_value = null;
			try {
				cus_id_rel_value = (String)context.getDataValue(cus_id_rel_name);
			} catch (Exception e) {}
			if(cus_id_rel_value == null || cus_id_rel_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_rel_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("cus_id_rel",cus_id_rel_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("ɾ�����ʧ�ܣ�����Ӱ����"+count+"���¼");
			}
			
//			CusComRelComponent cususComRelComponent = (CusComRelComponent) CMISComponentFactory
//			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMREL,context,connection);
//			String info = cususComRelComponent.deleteCusComRel(cus_id_value,cert_typ_value,cert_code_value,input_br_id_value,modelId);
			flag = "删除成功";
			
			//保存历史信息
			CusBaseComponent cbc = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			CusBase cusBase = cbc.getCusBase(cus_id_rel_value);
			String oldStr = "股东 "+cusBase.getCusName()+"["+cus_id_rel_value+"]由["+context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME)+"]退出";
			saveHistory(oldStr, "", cus_id_value, context, connection);
			
			String modelIdRel="CusComRelInvest";			
			Map<String,String> pkMapRel = new HashMap<String,String>();
		 
			pkMapRel.put("cus_id",cus_id_rel_value);
			pkMapRel.put("cus_id_rel",cus_id_value);
			dao.deleteByPks(modelIdRel, pkMapRel, connection);
			
		}catch (EMPException ee) {
			flag = "删除失败";
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
