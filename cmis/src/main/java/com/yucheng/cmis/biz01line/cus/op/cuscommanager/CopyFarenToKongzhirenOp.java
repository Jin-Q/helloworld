package com.yucheng.cmis.biz01line.cus.op.cuscommanager;

import java.sql.Connection;

import org.jdom.Document;
import org.jdom.Element;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class CopyFarenToKongzhirenOp extends CMISOperation{
	
	private final String modelId = "CusComManager";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = null;
		String cus_id_rel = null;
		context.put("backMsg","-1");
		try {
			connection = this.getConnection(context);
			try {
				cus_id = (String)context.getDataValue("cus_id");
				cus_id_rel = (String)context.getDataValue("cus_id_rel");
			} catch (Exception e) {
				e.printStackTrace();
			}
			TableModelDAO tmdao = this.getTableModelDAO(context);
			IndexedCollection iColl = tmdao.queryList(modelId, " where cus_id ='"+cus_id+"' and com_mrg_typ ='01' " +
					"and cus_id_rel ='"+cus_id_rel+"'", connection);
			KeyedCollection kColl = tmdao.queryFirst(modelId, null, " where cus_id ='"+cus_id+"' and com_mrg_typ ='02' " +
					"and cus_id_rel ='"+cus_id_rel+"'", connection);
			if(iColl.size()>0){
				context.put("backMsg", "0");
				return "0";
			}else{
				kColl.setDataValue("com_mrg_typ", "01");
			}
			//保存历史
			CusBaseComponent cbc = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			CusBase cusBase = cbc.getCusBase(cus_id_rel);
			String newStr = "类型为[实际控制人]的高管 "+cusBase.getCusName()+"["+cus_id_rel+"]由["+context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME)+"]新增";
			saveHistory("", newStr, cus_id, context, connection);
			//插入数据
			tmdao.insert(kColl, connection);
			context.put("backMsg", "1");
		} catch (EMPException ee) {
			throw ee;
		}catch (Exception  e) {
			throw new EMPException(e);
		}finally{
			if (connection != null)
				this.releaseConnection(context, connection);
		}
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

