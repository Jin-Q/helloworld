package com.yucheng.cmis.biz01line.cus.cuscom.component;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.ModifyHistory;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.ModifyHistoryAgent;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.KeyUtil;
import com.yucheng.cmis.pub.util.TimeUtil;

public class ModifyHistoryComponent extends CMISComponent {
	/**
	 * 把修改记录放进DB中。下列各参数为数据库中需要插入的参数
	 * 
	 * @param modifyRecord
	 * @param tableName
	 * @param cusId
	 * @param userIP
	 * @param status
	 * @throws ComponentException
	 */
	public void savedRecordToDB(String modifyRecord, String tableName,
			String cusId, String userIP, String status)
			throws EMPException {
		try {
			String keyId = KeyUtil.createUniqueKey();
			ModifyHistory modifyHistory = new ModifyHistory();
			modifyHistory.setCusId(cusId);
			modifyHistory.setKeyId(keyId);
			modifyHistory.setTableName(tableName);
			modifyHistory.setModifyRecord(modifyRecord);
			modifyHistory.setModifyTime(TimeUtil.getDateTime(""));
			modifyHistory.setModifyUserId(this.getUsrId());
			modifyHistory.setModifyUserBrId(this.getUsrBchId());
			modifyHistory.setModifyUserIp(userIP);
			modifyHistory.setModifyStatus(status);
			
			ModifyHistoryAgent agent = (ModifyHistoryAgent) this
					.getAgentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT);
			agent.insertBlobData(modifyHistory);
		} catch (Exception e) {
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT, EMPLog.ERROR, 0, "database error", e);
			throw new EMPException(e);
		}
	}

	/**
	 * 把KColl转换成XML格式 从理论上说，修改记录时修改前的kColl的id和修改后的kColl的id是一样的
	 * 新增记录时，只有修改后kColl，删除记录时，只有修改前kColl
	 * 
	 * @param kCollOld
	 *            修改前的kColl值
	 * @param kCollNew
	 *            修改后的kColl值
	 * @return
	 * @throws Exception
	 */
	public String transferKCollToXMLStr(KeyedCollection kCollOld, KeyedCollection kCollNew, String phyTable) throws Exception {
		//根节点
		Element root = new Element("columns");
		Element element;
		Iterator it;
		//如果修改前值为空，就使用修改后的值作比较
		if (kCollOld == null || kCollOld.size() == 0) {
			it = kCollNew.entrySet().iterator();
		} else {
			it = kCollOld.entrySet().iterator();
		}
		KeyedCollection.Entry entry = null;
		if (it != null) {
			while (it.hasNext()) {
				entry = (KeyedCollection.Entry) it.next();
				Object obj = entry.getValue();
				if (!(obj instanceof DataField)) {
					continue;
				}
				DataField df = (DataField) (entry.getValue());
				String id = df.getName();
				//如果数据库里有字段，但是页面上没有字段，就不比较了
				if(!kCollOld.containsKey(id) || !kCollNew.containsKey(id)) {
					continue;
				}
				element = new Element("columnname");
				//element.setAttribute("id", id);
				//=============add by 徐凯希================
				ModifyHistoryAgent agent = (ModifyHistoryAgent) this
				.getAgentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT);
				String cnname = agent.getColCnName(id, phyTable);
				element.setAttribute("id", cnname);
				//如果修改前的值不为空，就放入old节点下
				if (kCollOld != null && kCollOld.size() > 0) {
					Element subOldElement = new Element("old");
					String cnValueName = getValueCnName(id,(String)(kCollOld.getDataValue(id)),kCollOld.getName());
					subOldElement.addContent(cnValueName);
					
					element.addContent(subOldElement);
				}
				//如果修改后的值不为空，就放入new节点下
				if (kCollNew != null && kCollNew.size() > 0) {
					Element subNewElement = new Element("new");
					String cnValueName = getValueCnName(id,(String)(kCollNew.getDataValue(id)),kCollNew.getName());
					subNewElement.addContent(cnValueName);

					element.addContent(subNewElement);
				}
				//===========end add=======================
				root.addContent(element);
			}
		}
		
		Document doc = new Document(root);
		//转换document的值为字符串
		return OutputToString(doc, "UTF-8");
	}
	
	/**
	 * 若配置了字典则翻译成中文再存储
	 * @param colName
	 * @param colValue
	 * @param modelId
	 * @return
	 * @throws Exception
	 */
	public String getValueCnName(String colName,String colValue, String modelId) throws Exception{
		TableModelDAO dao = (TableModelDAO) this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		String popordic = "";
		String opttype = "";
		String cnValueName = "";
		Map<String,String> map = new HashMap<String,String>();
		map.put("model_id", modelId);
		map.put("column_name", colName);
		KeyedCollection kColl = dao.queryDetail("ModifyHistoryCfg", map, this.getConnection());
		if(kColl!=null){
			popordic = (String)kColl.getDataValue("popordic");
			opttype = (String)kColl.getDataValue("opttype");
		}
		if(popordic!=null&&!"".equals(popordic)&&opttype!=null&&!"".equals(opttype)){
			if("01".equals(popordic)){//字典项
				ModifyHistoryAgent modifyHistoryAgent = (ModifyHistoryAgent) this.getAgentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT);
				cnValueName = modifyHistoryAgent.getValueCnName(opttype,colValue);
			}else{//pop框
				CMISTreeDicService service = (CMISTreeDicService) this.getContext().getService(CMISConstance.ATTR_TREEDICSERVICE);
				if(colValue!=null&&!"".equals(colValue)){
					if(("STD_GB_4754-2011".equals(opttype)||"STD_GB_CORP_QLTY".equals(opttype)||"STD_GB_ECON_DEPT".equals(opttype))){	//单独处理，过滤掉enname开头的字母
						colValue = colValue.substring(1);
					}
					String locate = service.getNode(opttype, colValue).locate;
					String locates[] = locate.split(",");
					if(locates.length>1){
						for(int i=1;i<locates.length;i++){
							if(locates[i]!=null&&!"".equals(locates[i])){
								if(i!=locates.length-1){
									cnValueName += service.getNode(opttype, locates[i]).cnName + "->";
								}else{
									cnValueName += service.getNode(opttype, locates[i]).cnName;
								}
							}
						}
					}else{
						cnValueName = service.getNode(opttype, colValue).cnName;
					}
				}
			}
		}
		if(cnValueName==null||"".equals(cnValueName)){
			cnValueName = colValue;
		}

		return cnValueName;
	}

	/**
	 * 将JDom对象转换字符串.
	 * 
	 * @param document
	 *            将要被转换的JDom对象
	 * @param encoding
	 *            输出字符串使用的编码
	 * @return String xml对象保存到的字符串
	 * @throws EMPException
	 */
	private String OutputToString(Document document, String encoding)
			throws EMPException {
		ByteArrayOutputStream byteRep = new ByteArrayOutputStream();
		XMLOutputter docWriter = new XMLOutputter();
		try {
			docWriter.output(document, byteRep);
		} catch (Exception e) {
			throw new ComponentException();
		}
		try {
			return byteRep.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT, EMPLog.ERROR, 0, "transfer error", e);
			throw new EMPException();
		}
	}

	/**
	 * 比较新旧KeyColl，删掉其中的相同的部分
	 * 
	 * @param oldKColl
	 *            修改前的kColl
	 * @param newKColl
	 *            修改后的kColl
	 * @throws EMPException
	 */
	public void getModifiedKColl(KeyedCollection oldKColl,
			KeyedCollection newKColl) throws EMPException {
		try {
			KeyedCollection tmpOldKcoll = (KeyedCollection) (oldKColl.clone());
			Iterator it = tmpOldKcoll.entrySet().iterator();
			KeyedCollection.Entry entry = null;
			if (it != null) {
				while (it.hasNext()) {
					entry = (KeyedCollection.Entry) it.next();
					DataField df = (DataField) (entry.getValue());
					String id = df.getName();
					String value = (String) df.getValue();
					String newValue = newKColl.containsKey(id) ? newKColl.getDataValue(id)+"": null;
					//如果一方没有值，另一方值为空，或者两方的值相等，就需要把它们都去掉
					if ((value == null&& (newValue == null || "".equals(newValue)))
							|| ("".equals(value) && (newValue == null || "".equals(newValue))) || (value != null && value.equals(newValue))) {
						oldKColl.remove(id);
						newKColl.remove(id);
					}
					// 此处处理页面上的翻译问题，数据库里没有这几个字段
					if (newKColl.containsKey(id + "_displayname")) {
						newKColl.remove(id + "_displayname");
					}
				}
			}
		} catch (Exception e) {
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT, EMPLog.ERROR, 0, "compare error", e);
			throw new EMPException(e);
		}
	}

	/**
	 * 对外方法，把修改过的记录放到DB中
	 * @throws Exception 
	 */
	public void recordHistoryModify(KeyedCollection oldKColl,
			KeyedCollection newKColl, String tableName, String userIP)
			throws Exception {
		String cusId = (String) ((oldKColl == null || oldKColl.size() == 0) ? newKColl
				: oldKColl).getDataValue("cus_id");
		String status = "";
		//记录此次修改的状态
		if (oldKColl == null || oldKColl.size() == 0) {
			status = "新增";
		} else if (newKColl == null || newKColl.size() == 0) {
			status = "删除";
		} else {
			status = "修改";
		}
		getModifiedKColl(oldKColl, newKColl);
		// 如果比较后发现没有修改，就直接返回不作处理
		if ((oldKColl == null || oldKColl.size() == 0)
				&& (newKColl == null || newKColl.size() == 0)) {
			return;
		}
		// 如果是修改操作，但是比较后发现只有新kColl里有数据，那也不作处理
		if ("修改".equals(status) && (newKColl == null || newKColl.size() == 0)) {
			return;
		}
		String modifyRecord ="";
		try {
			TableModelLoader modelLoader = (TableModelLoader)this.getContext().getService(CMISConstance.ATTR_TABLEMODELLOADER);
			TableModel tableModel = modelLoader.getTableModel(tableName);
			String phyTable = tableModel.getDbTableName();//获得数据库表名
			modifyRecord = transferKCollToXMLStr(oldKColl, newKColl, phyTable);
		} catch (Exception e) {
			throw e;
		}
		if (modifyRecord ==null) modifyRecord="";
		int modifyRecord11 = modifyRecord.length();
		if(modifyRecord.equals("")||modifyRecord11==53)
		{
			return;
		}
		savedRecordToDB(modifyRecord, tableName, cusId, userIP, status);
	}

	/**
	 * 把XML字符串转换成KColl
	 * 
	 * @param XMLStr
	 *            XML字符串
	 * @return
	 * @throws EMPException 
	 */
	public IndexedCollection transferXMLTOKColl(String XMLStr) throws EMPException {
		if (XMLStr == null || "".equals(XMLStr)) {
			return null;
		}
		// 创建一个新的字符串
		StringReader xmlString = new StringReader(XMLStr);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(xmlString);
		// 创建一个新的SAXBuilder
		SAXBuilder saxb = new SAXBuilder();

		IndexedCollection result = null;
		try {
			result = new IndexedCollection("ModifyHistoryDetailList");
			// 通过输入源构造一个Document
			Document doc = saxb.build(source);
			// 取的根元素
			Element root = doc.getRootElement();

			// 得到根元素所有子元素的集合
			List node = root.getChildren();
			Element element = null;
			for (int i = 0; i < node.size(); i++) {
				element = (Element) node.get(i);// 循环依次得到子元素
				String id = element.getAttributeValue("id");
				String oldValue = element.getChildText("old");
				String newValue = element.getChildText("new");
				oldValue = (oldValue == null) ? "" : oldValue;
				newValue = (newValue == null) ? "" : newValue;
				KeyedCollection subKColl = new KeyedCollection(
						"ModifyHistoryDetail");
				subKColl.addDataField("modify_name", id);
				subKColl.addDataField("modify_old_value", oldValue);
				subKColl.addDataField("modify_new_value", newValue);
				result.addDataElement(subKColl);
			}
		} catch (Exception e) {
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT, EMPLog.ERROR, 0, "database error", e);
			throw new EMPException(e);
		}
		return result;
	}

	/**
	 * 从数据库中取到修改的详细信息，把它转换成KColl
	 * @param keyId 数据库表记录的关键值
	 * @return
	 * @throws EMPException
	 */
	public IndexedCollection getDetailKColl(String keyId) throws EMPException {
		ModifyHistoryAgent agent = (ModifyHistoryAgent) this
				.getAgentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT);
		String xmlStr = agent.getDetailKColl(keyId);
		return transferXMLTOKColl(xmlStr);
	}
	
	/**
	 * 根据配置表模型配置对kColl进行过滤
	 * @throws EMPJDBCException 
	 * @throws ComponentException 
	 */
	public KeyedCollection getCfgedKColl(String model_Id, KeyedCollection kColl) throws Exception{
		KeyedCollection kCollN = new KeyedCollection(model_Id);
		TableModelDAO dao = (TableModelDAO) this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kCollCfg = dao.queryDetail("CusModifyHistoryCfg", model_Id, this.getConnection());
		if(kCollCfg!=null&&kCollCfg.getDataValue("cfg_column")!=null&&!"".equals(kCollCfg.getDataValue("cfg_column"))){
			String cfgColumn = (String)kCollCfg.getDataValue("cfg_column");
			String[] cfgColumns = cfgColumn.split(",");
			for(int i=0;i<cfgColumns.length;i++){
				if(kColl.containsKey(cfgColumns[i])){
					kCollN.addDataField(cfgColumns[i], kColl.getDataValue(cfgColumns[i]));
				}
			}
			if(!kCollN.containsKey("cus_id")){
				kCollN.addDataField("cus_id", kColl.getDataValue("cus_id"));
			}
			return kCollN;
		}else{
			return null;
		}
	}
	
	/**
	 * 根据表模型删除原来的配置字段配置信息
	 * @param model_id
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public void deleteModifyCfgByModelId(String model_id) throws Exception{
		ModifyHistoryAgent agent = (ModifyHistoryAgent) this
		.getAgentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT);
		agent.deleteModifyCfgByModelId(model_id);
	}
	
	/**
	 * 根据表模型插入字段配置信息
	 * @param model_id
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public void insertModifyCfgByModelId(IndexedCollection iColl) throws Exception{
		ModifyHistoryAgent agent = (ModifyHistoryAgent) this
		.getAgentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT);
		agent.insertModifyCfgByModelId(iColl);
	}
	
	/**
	 * 自定义保存修改历史
	 * @throws EMPException 
	 */
	public void saveHistoryTemp(Document doc,String tableName,String cusId,String status) throws EMPException{
		String toSave = OutputToString(doc, "UTF-8");
		if (toSave ==null) toSave="";
		int modifyRecord11 = toSave.length();
		if(toSave.equals("")||modifyRecord11==53){
			return;
		}
		HttpServletRequest request = (HttpServletRequest)this.getContext().getDataValue(EMPConstance.SERVLET_REQUEST);
		String userIP = request.getRemoteAddr();
		savedRecordToDB(toSave, tableName, cusId, userIP, status);
	}
}
