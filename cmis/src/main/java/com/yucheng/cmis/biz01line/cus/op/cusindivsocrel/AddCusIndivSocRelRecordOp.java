package com.yucheng.cmis.biz01line.cus.op.cusindivsocrel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCusIndivSocRelRecordOp extends CMISOperation {

    // operation TableModel
    private final String modelId = "CusIndivSocRel";

    /**
     * bussiness logic operation
     */
    @Override
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
            if (kColl == null || kColl.size() == 0) {
                throw new EMPJDBCException("The values to insert[" + modelId+ "] cannot be empty!");
            }
            String cusid = (String) kColl.getDataValue("cus_id");
			//是否有配偶
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection icSoc = dao.queryList(modelId, " where cus_id = '" + cusid + "'", connection);
			IndexedCollection icSpouse = dao.queryList(modelId, " where cus_id = '" + cusid + "' and indiv_cus_rel='1' ", connection);
			
			String conditionStr = "where cus_id = '"+cusid+"'";
			KeyedCollection kCollTemp = dao.queryFirst("CusBase", null, conditionStr, connection);
			String cus_status = (String)kCollTemp.getDataValue("cus_status");
			if(icSoc.size() >= 1){
				if(icSpouse.size() == 1){
					//修改更新 配偶信息
					String weatherSp = null;
					try{weatherSp = (String) kColl.getDataValue("weatherSpouse");}catch(Exception e){}
					if("1".equals(weatherSp)){
						kColl.remove("weatherSpouse");
						String cus_id = (String)kColl.getDataValue("cus_id");
						//删除原有配偶信息
						String condition = " WHERE cus_id = '" + cus_id + "' and indiv_cus_rel='1'";
						IndexedCollection ic = dao.queryList(modelId, condition, connection);
						KeyedCollection kc = null;
						if(ic.size() == 1){
							kc = (KeyedCollection) ic.get(0);
						}
						String cus_id_rel = (String)kc.getDataValue("cus_id_rel");  //原来配偶ID
						delete(cus_id_rel,context,connection);
						//删除自己
						delete((String) kc.getDataValue("cus_id"),context,connection);
						//新增
						insertKK(kColl, context, connection);
						
						flag = "修改成功";
					//删除配偶信息
					}else if("2".equals(kColl.getDataValue("weatherSpouse"))){
						//删除自己
						String cus_id = (String)kColl.getDataValue("cus_id");
						delete(cus_id,context,connection);
						
						//删除配偶
						String cus_id_rel = (String) kColl.getDataValue("cus_id_rel");
						delete(cus_id_rel,context,connection);
						//保存历史修改信息
						if("20".equals(cus_status)){
							saveHistory("是", "否", cus_id, context, connection);
						}
						flag = "删除成功";
					}else
						flag = "该客户已经存在配偶信息，不能新增！";
				}else{
					if(icSpouse.size() == 0){
						if("1".equals(kColl.getDataValue("weatherSpouse"))){
							//不存在，可以新增
							insertKK(kColl,context,connection);
							//保存历史修改信息
							if("20".equals(cus_status)){
								String cus_id = (String)kColl.getDataValue("cus_id");
								saveHistory("否", "是", cus_id, context, connection);
							}
							flag = "新增成功";
						}else{
							flag = "保存成功";
						}
					}else{
						//证件类型+证件号码+客户码已存在
						flag = "该客户下的同一关联客户已存在，不能新增！";
					}
				}
			}else{
				if(icSpouse.size() == 0){
					if("1".equals(kColl.getDataValue("weatherSpouse"))){
						//不存在，可以新增
						insertKK(kColl,context,connection);
						if("20".equals(cus_status)){
							String cus_id = (String)kColl.getDataValue("cus_id");
							saveHistory("否", "是", cus_id, context, connection);
						}
						flag = "新增成功";
					}else{
						flag = "保存成功";
					}
				}else{
					//证件类型+证件号码+客户码已存在
					flag = "该客户下的同一关联客户已存在，不能新增！";
				}
			}
        } catch (EMPException ee) {
            flag = "失败";
            EMPLog.log("CusIndivSocRel", EMPLog.ERROR, 0, "新增失败!", ee);
            throw ee;
        } catch (Exception e) {
            flag = "失败";
            EMPLog.log("CusIndivSocRel", EMPLog.ERROR, 0, "新增失败!", e);
            throw new EMPException(e);
        } finally {

            if (connection != null) {
                this.releaseConnection(context, connection);
            }
        }
        context.addDataField("flag", flag);
        return "0";
    }

	private void delete(String cusIdRel, Context context, Connection connection) throws EMPException {
		String condition = " WHERE cus_id = '" + cusIdRel + "' and indiv_cus_rel='1'";
		TableModelDAO dao = this.getTableModelDAO(context);
		IndexedCollection ic = dao.queryList(modelId, condition, connection);
		Map<String,String> map2 = new HashMap<String,String>();	//删除配偶二
		KeyedCollection kc = null;
		if(ic.size() == 1){
			kc = (KeyedCollection) ic.get(0);
		}
		
		map2.put("cus_id", kc.getDataValue("cus_id").toString());
		map2.put("cus_id_rel", kc.getDataValue("cus_id_rel").toString());
//		map2.put("indiv_rl_cert_code", kc.getDataValue("indiv_rl_cert_code"));
		dao.deleteByPks(modelId, map2, connection);
	}

	private void insertKK(KeyedCollection kColl, Context context, Connection connection) throws EMPException {
		TableModelDAO dao = this.getTableModelDAO(context);
		String cus_id = (String) kColl.getDataValue("cus_id");//本人ID
		String cus_id_rel = (String) kColl.getDataValue("cus_id_rel");//关联ID
		String openDay = context.getDataValue("OPENDAY").toString();
		String inputId = context.getDataValue("currentUserId").toString();
		String inputBrId = context.getDataValue("organNo").toString();
		
		KeyedCollection kColl2 = new KeyedCollection(modelId);
		kColl2.addDataField("cus_id_rel",cus_id);
		kColl2.addDataField("cus_id",cus_id_rel); 
		kColl2.addDataField("indiv_cus_rel","1");
		kColl2.addDataField("indiv_family_flg", "1");
		kColl2.addDataField("input_id", inputId);
		kColl2.addDataField("input_br_id", inputBrId);
		kColl2.addDataField("input_date", openDay);
		
		dao.insert(kColl2, connection);
		
		KeyedCollection kc = new KeyedCollection(modelId);
		kc.addDataField("cus_id_rel",cus_id_rel);
		kc.addDataField("cus_id",cus_id); 
		kc.addDataField("indiv_cus_rel","1");
		kc.addDataField("indiv_family_flg", "1");
		kc.addDataField("input_id", inputId);
		kc.addDataField("input_br_id", inputBrId);
		kc.addDataField("input_date", openDay);
		
		dao.insert(kc, connection);
	}
	
	//保存历史修改信息
	private void saveHistory(String oldName,String newName, String cusId ,Context context, Connection connection) throws EMPException{
		Element root = new Element("columns");
		Element element = new Element("columnname");
		element.setAttribute("id", "是否有配偶");
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
