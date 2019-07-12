package com.yucheng.cmis.pub.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jfree.util.Log;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.dic.CMISTreeDicNode;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.pub.TreeDicTools;

public class SInfoUtils {
	/**
	 * 用户信息
	 */
	  public static final String USER="user";
	  /**
	   *机构信息 
	   */
	  public static final String ORG="org";
	  /**
	   *岗位信息
	   */
	  public static final String DUTY="duty";
	  
	  /**
	   * 替换方式
	   */
	  public static final String REPL="replace";
	  /**
	   * 增加方式
	   */
	  public static final String ADD="add";
	
	
	/**
	 * 将机构码转换为机构名称（机构编码）的形式
	 * @param icol
	 * @param argOrg  要替换机构的字段名称
	 * @return
	 */
	
  
	
	private static IndexedCollection replaceOrgCode(IndexedCollection icol,
			String[] arg) throws EMPException {
		SInfoUtils.dealReplaceName(icol, arg, SInfoUtils.REPL);
		return icol;
	}
	
	/**
	 * 将机构码转换为名称（客户编码）的形式
	 * @param icol
	 * @param argUser
	 * @return
	 * @throws EMPException 
	 */
	private static IndexedCollection replaceUSerCode(IndexedCollection icol,
			String[] arg) throws EMPException {
		SInfoUtils.dealReplaceName(icol, arg, SInfoUtils.REPL);
		return icol;
	}

	
	/**
	 * 新增userid__displayname dataFild
	 * @param icol
	 * @param argUSer
	 * @return
	 * @throws EMPException
	 */
	public static IndexedCollection addUSerName(IndexedCollection icol,
			String[] arg) throws EMPException {
		dealAddName(icol,arg,SInfoUtils.USER);
		return icol;
	}
	
	/**
	 * 新增dutyno__displayname dataFild
	 * @param icol
	 * @param argUSer
	 * @return
	 * @throws EMPException
	 */
	public static IndexedCollection addDutyName(IndexedCollection icol,
			String[] arg) throws EMPException {
		dealAddName(icol,arg,SInfoUtils.DUTY);
		return icol;
	}
	
	/**
	 * 新增userid__displayname dataFild
	 * @param icol
	 * @param argUSer
	 * @return
	 * @throws EMPException
	 */
	public static IndexedCollection addSOrgName(IndexedCollection icol,
			String[] arg) throws EMPException {
		dealAddName(icol,arg,SInfoUtils.ORG);
		return icol;
	}
	
	
	/**
	 * 新增userid__displayname dataFild
	 * @param kCol
	 * @param argUSer
	 * @return
	 * @throws EMPException
	 */
	public static KeyedCollection addUSerName(KeyedCollection kCol,
			String[] arg) throws EMPException {
		dealAddName(kCol,arg,SInfoUtils.USER);
		return kCol;
	}
	
	/**
	 * 新增dutyno__displayname dataFild
	 * @param kCol
	 * @param argUSer
	 * @return
	 * @throws EMPException
	 */
	public static KeyedCollection addDutyName(KeyedCollection kCol,
			String[] arg) throws EMPException {
		dealAddName(kCol,arg,SInfoUtils.DUTY);
		return kCol;
	}
	/**
	 * 新增userid__displayname dataFild
	 * @param kcol
	 * @param argUSer
	 * @return
	 * @throws EMPException
	 */
	public static KeyedCollection addSOrgName(KeyedCollection kCol,
			String[] arg) throws EMPException {
		dealAddName(kCol,arg,SInfoUtils.ORG);
		return kCol;
	}
	
	/**
	 * 将pop框的值翻译出来，并添加到原kCol用于页面展示 addby tangzf
	 * @param kCol
	 * @param arg map中存放字段名及对应的字典类别，如map.put("com_cll_type", "STD_GB_4754-2011");
	 * @return
	 * @throws EMPException
	 */
	public static KeyedCollection addPopName(KeyedCollection kCol, Map arg, CMISTreeDicService service) throws EMPException {
		Iterator it = arg.keySet().iterator();
		while (it.hasNext()) {
			String showTxt = "";
			String dataId = (String)it.next();
			String optType = (String) arg.get(dataId);
			String enname = (String) kCol.getDataValue(dataId);
			if(enname!=null&&!"".equals(enname)){
				if(("STD_GB_4754-2011".equals(optType))&& enname!=null&&!"".equals(enname)){	//单独处理，过滤掉enname开头的字母
					char t_one = enname.charAt(0);  //将行业分类的第一位转换为字符
					if(t_one-65>=0 && t_one-90<=0){  //字符的ASCII 码值大于65 小于 90 说明为A到Z
						enname = enname.substring(1);  //将第一位截取
					}
				}
				CMISTreeDicNode nodes = service.getNode(optType, enname); 
				if(null != nodes){
					String locate = nodes.locate;
					locate = locate.substring(locate.indexOf(",",0)+1);  //去掉字树形典项的最前项
					String[] locates = locate.split(",");
					for(int i=0;i<locates.length;i++){
						String ennameTmp = locates[i];
						if(ennameTmp!=null&&!"".equals(ennameTmp)){
							showTxt = showTxt + service.getNode(optType, ennameTmp).cnName;
						}
						//i小于数组长度说明不是最后一个
						if(i < locates.length -1){
							showTxt+="->";
						}
					}
				}else{
					showTxt += enname;
				}
			}
			if(dataId.indexOf(".") != -1){
				/** 存在主从关系表进行判断 */
				String subKName = dataId.substring(0, dataId.indexOf("."));
				String subFName = dataId.substring(dataId.indexOf(".")+1, dataId.length());

				KeyedCollection subKC = (KeyedCollection)kCol.getDataElement(subKName);
				subKC.addDataField(subFName+"_displayname", showTxt);
				subKC.setName(subKName);
				kCol.removeDataElement(subKName);
				kCol.addKeyedCollection(subKC);
			}else {
				kCol.addDataField(dataId+"_displayname", showTxt);	
			}
		}
		
		return kCol;
	}
	/**
	 * 将pop框的值翻译出来，并添加到原kCol用于页面展示 addby Xiaod
	 * @param kCol
	 * @param arg map中存放字段名及对应的字典类别，如map.put("com_cll_type", "STD_GB_4754-2011");
	 * @return
	 * @throws EMPException
	 */
	public static KeyedCollection addIcollPopName(KeyedCollection kCol, Map arg, CMISTreeDicService service) throws EMPException {
		Iterator it = arg.keySet().iterator();
		while (it.hasNext()) {
			String showTxt = "";
			String dataId = (String)it.next();
			String optType = (String) arg.get(dataId);
			String enname = (String) kCol.getDataValue(dataId);
			if(enname!=null&&!"".equals(enname)){
				if(("STD_GB_4754-2011".equals(optType)||"STD_GB_CORP_QLTY".equals(optType)||"STD_GB_ECON_DEPT".equals(optType))&& enname!=null&&!"".equals(enname)){	//单独处理，过滤掉enname开头的字母
					enname = enname.substring(1);
				}
				CMISTreeDicNode nodes = service.getNode(optType, enname); 
				if(null != nodes){  //如果返回的节点不为空，将节点对应的中文返回
					showTxt = service.getNode(optType, enname).cnName;
				}else{   //否则直接返回原字段值
					showTxt = enname;
				}
			}
			kCol.addDataField(dataId+"_displayname", showTxt);
		}
		
		return kCol;
	}
	/**
	 *  将pop框的值翻译出来，并添加到原iColl用于页面展示 addby tangzf
	 *  
	 */
	public static IndexedCollection addPopName(IndexedCollection iCol, Map arg, CMISTreeDicService service) throws EMPException {
		for(int i=0;i<iCol.size();i++){
			KeyedCollection kColl = (KeyedCollection) iCol.get(i);
			kColl = addPopName(kColl, arg, service);
		}
		return iCol;
	}
	/**
	 * 针对产品多选树图进行翻译，并添加到原kCol用于页面展示 addby tangzf
	 * @param kCol
	 * @param arg 
	 * @return
	 * @throws EMPException
	 */
	public static KeyedCollection addPrdPopName(KeyedCollection kCol,String dataId,Connection conn) throws Exception {
		String nodId = (String) kCol.getDataValue(dataId);
		String nodeIds[] = nodId.split(",");
		TreeDicTools treeDicSer = new TreeDicTools();
		String showTxt = "";
		for(int i=0;i<nodeIds.length;i++){
			String cataLogName = treeDicSer.getPrdPopCataLogName(nodeIds[i], conn);
			showTxt = showTxt + cataLogName + "," ;
		}
		if(!"".equals(showTxt)){
			showTxt = showTxt.substring(0, showTxt.length()-1);
		}
		kCol.addDataField(dataId+"_displayname", showTxt);
		
		return kCol;
	}
	
	/**
	 * 针对有层级的数据进行翻译，并添加到原kCol用于页面展示 
	 * @param moduleId	查询数据来源表模型
	 * @param kCol		需翻译的KeyedCollection对象
	 * @param dataId	需要翻译的字段
	 * @param conditionField 翻译表模型时的条件字段（moduleId的查询条件）
	 * @param showField 翻译后的显示字段		
	 * @param splitStr	显示分割分隔串（翻译完成后各中文字段中间的分隔符）
	 * @param dao		查询的TableModelDAO
	 * @return  返回编译后的KeyedCollection
	 * @throws EMPException
	 * @author 唐顺岩
	 */
	public static KeyedCollection addPrdPopName(String moduleId,KeyedCollection kCol,String dataId,String conditionField,String showField,String splitStr,Connection conn,TableModelDAO dao)throws Exception {
		String nodId = (String) kCol.getDataValue(dataId);
		if(null==nodId || "".equals(nodId)){   //如果字段值为空，不处理，直接返回
			return kCol;   
		}
		String nodeIds[] = nodId.split(",");
		String showTxt = "";
		for(int i=0;i<nodeIds.length;i++){
			List<String> list = new ArrayList<String>();
			list.add(showField);
			String condition="WHERE "+conditionField+"='"+nodeIds[i]+"'";
			KeyedCollection kColl=dao.queryFirst(moduleId, list, condition, conn);
			
			if(null!=kColl.getDataValue(showField) && !"".equals(kColl.getDataValue(showField))){
				showTxt = showTxt + kColl.getDataValue(showField).toString() + splitStr;
			}else{   //如果未查询到数据直接将原有编码返回
				showTxt = showTxt + nodeIds[i] + splitStr;
			}
		}
		if(!"".equals(showTxt)){
			showTxt = showTxt.substring(0, showTxt.length()-splitStr.length());
		}
		kCol.addDataField(dataId+"_displayname", showTxt);
		
		return kCol;
	}
	public static KeyedCollection addPrdPopName4Mort(String moduleId,KeyedCollection kCol,String dataId,String conditionField,String showField,String splitStr,Connection conn,TableModelDAO dao)throws Exception {
		String nodId = (String) kCol.getDataValue(dataId);
		String nodeIds[] = nodId.split(",");
		String showTxt = "";
		for(int i=0;i<nodeIds.length;i++){
			List<String> list = new ArrayList<String>();
			list.add(showField);
			String condition="WHERE "+conditionField+"='"+nodeIds[i]+"'";
			KeyedCollection kColl=dao.queryFirst(moduleId, list, condition, conn);
			
			if(null!=kColl.getDataValue(showField) && !"".equals(kColl.getDataValue(showField))){
				showTxt = showTxt + kColl.getDataValue(showField).toString() + splitStr;
			}else{   //如果未查询到数据直接将原有编码返回
				showTxt = showTxt + nodeIds[i] + splitStr;
			}
		}
		if(!"".equals(showTxt)){
			showTxt = showTxt.substring(0, showTxt.length()-splitStr.length());
		}
		kCol.put(dataId+"_displayname", showTxt);
		
		return kCol;
	}
	/**
	 * 针对有层级的数据进行翻译，并添加到原iCol用于页面展示 
	 * @param moduleId	查询数据来源表模型
	 * @param iCol		需翻译的IndexedCollection对象
	 * @param dataId	需要翻译的字段
	 * @param conditionField 翻译表模型时的条件字段（moduleId的查询条件）
	 * @param showField 翻译后的显示字段		
	 * @param splitStr	显示分割分隔串（翻译完成后各中文字段中间的分隔符）
	 * @param dao		查询的TableModelDAO
	 * @return  返回编译后的IndexedCollection
	 * @throws EMPException
	 * @author 唐顺岩
	 */
	public static IndexedCollection addPrdPopName(String moduleId,IndexedCollection iCol,String dataId,String conditionField,String showField,String splitStr,Connection conn,TableModelDAO dao) throws EMPException {
		for(int i=0;i<iCol.size();i++){
			KeyedCollection kColl = (KeyedCollection) iCol.get(i);
			try {
				kColl = addPrdPopName(moduleId, kColl, dataId, conditionField,showField,splitStr,conn,dao);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return iCol;
	}
	public static IndexedCollection addPrdPopName4Mort(String moduleId,IndexedCollection iCol,String dataId,String conditionField,String showField,String splitStr,Connection conn,TableModelDAO dao) throws EMPException {
		for(int i=0;i<iCol.size();i++){
			KeyedCollection kColl = (KeyedCollection) iCol.get(i);
			try {
				if(kColl.containsKey(dataId+"_displayname")&&kColl.getDataValue(dataId+"_displayname")!=null&&!"".equals(kColl.getDataValue(dataId+"_displayname"))&&!kColl.getDataValue(dataId).equals(kColl.getDataValue(dataId+"_displayname"))){
					
				}else{
					kColl = addPrdPopName4Mort(moduleId, kColl, dataId, conditionField,showField,splitStr,conn,dao);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return iCol;
	}
	/**
	 * 将多选的产品翻译成产品名称，并添加到原kCol用于页面展示（在翻译字段后加_displayname）
	 * @param kCol
	 * @param dataId 翻译的字段（放产品ID的字段） 
	 * @return
	 * @throws EMPException
	 */
	public static KeyedCollection getPrdPopName(KeyedCollection kCol,String dataId,Connection conn) throws Exception {
		if(kCol.getDataValue(dataId)==null||"".equals(kCol.getDataValue(dataId))){
			return kCol;
		}else{
			String prdids = (String) kCol.getDataValue(dataId);
			TreeDicTools treeDicSer = new TreeDicTools();
			String showTxt = treeDicSer.getPrdPopName(prdids, conn);
			
			if(!"".equals(showTxt)){
				showTxt = showTxt.substring(0, showTxt.length()-1);
			}
			kCol.addDataField(dataId+"_displayname", showTxt);
			
			return kCol;
		}
	}
	
	/**
	 * 处理替换公用方法
	 * @param icol
	 * @param arg
	 * @param type
	 * @throws EMPException
	 */
	private  static void dealReplaceName(IndexedCollection icol,
			String[] arg,String type) throws EMPException{
		for (int i = 0; i < icol.size(); i++) {

			KeyedCollection kCol = (KeyedCollection) icol.get(i);
		
			/*Iterator it = (Iterator) kCol.values().iterator();
			while (it.hasNext()) {
				DataElement element = (DataElement) it.next();
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int j = 0; j < arg.length; j++) {
						if (arg[j].equals(aField.getName())) {
							//加转换函数
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"1");
							aField.setValue(str);
						}
					}
				}
			}*/
			for(int j=0;j<kCol.size();j++){
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							//加转换函数
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"1");
							aField.setValue(str);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 处理替换公用方法
	 * @param icol
	 * @param arg
	 * @param type
	 * @throws EMPException
	 */
	private  static void dealReplaceName(KeyedCollection kCol,
			String[] arg,String type) throws EMPException{
		
		
			/*Iterator it = (Iterator) kCol.values().iterator();
			while (it.hasNext()) {
				DataElement element = (DataElement) it.next();
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int j = 0; j < arg.length; j++) {
						if (arg[j].equals(aField.getName())) {
							//加转换函数
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"1");
							aField.setValue(str);
						}
					}
				}
			}*/
			for(int j=0;j<kCol.size();j++){
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							//加转换函数
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"1");
							aField.setValue(str);
						}
					}
				}
			}
		}
	
	
	
	/**
	 * 通用处理名称方法
	 * @param icol
	 * @param arg
	 * @param type
	 * @param mode
	 * @throws EMPException
	 */
	private  static void dealName(IndexedCollection icol,
			String[] arg,String type,String mode) throws EMPException{
		for (int i = 0; i < icol.size(); i++) {

			KeyedCollection kCol = (KeyedCollection) icol.get(i);
		//用迭代器有时会抛出ConcurrentModificationException
		/*	Iterator it = (Iterator) kCol.values().iterator();
			while (it.hasNext()) {
				DataElement element = (DataElement) it.next();
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int j = 0; j < arg.length; j++) {
						if (arg[j].equals(aField.getName())) {
							//加转换函数
							
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"1");
							if(SInfoUtils.ADD.equals(mode)){
								kCol.addDataField((String)aField.getName()+"_displayname", str);
							}else if(SInfoUtils.REPL.equals(mode)){
								aField.setValue(str);
							}
							
						}
					}
				}
			}*/

			for(int j=0;j<kCol.size();j++){
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							//加转换函数
							
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"1");
							if(SInfoUtils.ADD.equals(mode)){
								kCol.addDataField((String)aField.getName()+"_displayname", str);
							}else if(SInfoUtils.REPL.equals(mode)){
								aField.setValue(str);
							}
							
						}
					}
				}
			}
		}
	}
	
	
	private  static void dealName(KeyedCollection kCol,
			String[] arg,String type,String mode) throws EMPException{
		
		//用迭代器有时会抛出ConcurrentModificationException
		/*	Iterator it = (Iterator) kCol.values().iterator();
			while (it.hasNext()) {
				DataElement element = (DataElement) it.next();
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int j = 0; j < arg.length; j++) {
						if (arg[j].equals(aField.getName())) {
							//加转换函数
							
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"1");
							if(SInfoUtils.ADD.equals(mode)){
								kCol.addDataField((String)aField.getName()+"_displayname", str);
							}else if(SInfoUtils.REPL.equals(mode)){
								aField.setValue(str);
							}
							
						}
					}
				}
			}*/

			for(int j=0;j<kCol.size();j++){
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							//加转换函数
							
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"1");
							if(SInfoUtils.ADD.equals(mode)){
								kCol.addDataField((String)aField.getName()+"_displayname", str);
							}else if(SInfoUtils.REPL.equals(mode)){
								aField.setValue(str);
							}
							
						}
					}
				}
			}
		}
	

	/**
	 * 处理增加模式的名称
	 * @param icol
	 * @param arg
	 * @param type
	 * @throws EMPException
	 */
	
	private  static void dealAddName(IndexedCollection icol,
			String[] arg,String type) throws EMPException{
		for (int i = 0; i < icol.size(); i++) {

			KeyedCollection kCol = (KeyedCollection) icol.get(i);
		
/*			Iterator it = (Iterator) kCol.values().iterator();
			while (it.hasNext()) {
				DataElement element = (DataElement) it.next();
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int j = 0; j < arg.length; j++) {
						if (arg[j].equals(aField.getName())) {
							//加转换函数
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"0");
							kCol.addDataField((String)aField.getName()+"_displayname", str);				
						}
					}
				}
			}*/
			
			for(int j=0;j<kCol.size();j++){
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						Log.debug("翻译字段："+arg[k] +" 与表模型比较属性比较："+arg[k].equals(aField.getName()));
						if (arg[k].equals(aField.getName())) {
							if(aField.getValue()!=null&&!"".equals(aField.getValue())){//判断若要翻译的对象为空则不进行翻译
								//加转换函数
								/* modified by yangzy 2014/12/03 需求：XD140718026_新信贷系统授信进度查询改造 start  */
								String aField_value = (String) aField.getValue();
								String str=SInfoUtils.getName((String)(aField_value.replace(";", "")),type,"0");
								/* modified by yangzy 2014/12/03 需求：XD140718026_新信贷系统授信进度查询改造 end  */
								kCol.addDataField((String)aField.getName()+"_displayname", str);				
							}else{   //当需翻译字段为空时，显示值赋空值，否则异步查询JSION会报错
								kCol.addDataField((String)aField.getName()+"_displayname", "");
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param kCol
	 * @param arg
	 * @param type
	 * @throws EMPException
	 */
	private  static void dealAddName(KeyedCollection kCol, String[] arg,String type) {
/*			Iterator it = (Iterator) kCol.values().iterator();
			while (it.hasNext()) {
				DataElement element = (DataElement) it.next();
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int j = 0; j < arg.length; j++) {
						if (arg[j].equals(aField.getName())) {
							//加转换函数
							String str=SInfoUtils.getName((String)(aField.getValue()),type,"0");
							kCol.addDataField((String)aField.getName()+"_displayname", str);				
						}
					}
				}
			}*/
			
			for(int j=0;j<kCol.size();j++){
				try{
					DataElement element = (DataElement) kCol.getDataElement(j);
					if (element instanceof DataField) {
						DataField aField = (DataField) element;
						for (int k = 0; k < arg.length; k++) {
							if (arg[k].equals(aField.getName())) {
								//加转换函数
								if(null!=aField.getValue() && !"".equals(aField.getValue())){
									String str=SInfoUtils.getName((String)(aField.getValue()),type,"0");
									kCol.addDataField((String)aField.getName()+"_displayname", str);
								}else{
									System.err.println("翻译表模型字段错误，翻译字段"+aField.getName()+"值为空!s");
								}
							}
						}
					}
				}catch(EMPException e){
					e.printStackTrace();
				}
			}
		}
	
	
	/**
	 * 根据用户ID获取用户名
	 * @param uSerId
	 * @param type  "user" 用户 ,"org" 机构
	 * @param flag  1 返回name+id的格式 0 只返回name
	 * @return
	 * @throws EMPException
	 */
	private static String getName(String Id,String type,String flag) {
		String rt=Id;
		if(SInfoUtils.USER.equals(type)){
			SUser sUser=null;
			try {
				sUser = SInfoFactory.getSysInfoFactoryInstance().getSUserInstance(Id);
			} catch (EMPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null!=sUser){
				String name=sUser.getActorname();
				if(null!=name){
					if("0".equals(flag)){
						rt=name;
					}else{
						rt=SInfoUtils.formatName(name, Id);
					}		
				}
			}
		}else if(SInfoUtils.ORG.equals(type)){
			SOrg sOrg=null;
			try {
				sOrg = SInfoFactory.getSysInfoFactoryInstance().getSorgInstance(Id);
			} catch (EMPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null!=sOrg){
				String name=sOrg.getOrganname();
				if(null!=name){
					if("0".equals(flag)){
						rt=name;
					}else{
						rt=SInfoUtils.formatName(name, Id);
					}		
				}
			}
		}else if(SInfoUtils.DUTY.equals(type)){
			SDuty sDuty=null;
			try {
				sDuty = SInfoFactory.getSysInfoFactoryInstance().getSdutyInstance(Id);
			} catch (EMPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null!=sDuty){
				String name=sDuty.getDutyname();
				if(null!=name){
					if("0".equals(flag)){
						rt=name;
					}else{
						rt=SInfoUtils.formatName(name, Id);
					}		
				}
			}
		}
		
		
		return rt;
	}
	
	
	//根据机构编号获得机构名称
	public static String getOrgNameByOrgId(String orgId){
		return getName(orgId,"org","0");
	} 
	
	/**
	 * 新增机构动态修改map值
	 * @param kCol
	 * @throws ObjectNotFoundException
	 * @throws InvalidArgumentException
	 */
	public static void addOrgMapInfo(KeyedCollection kCol) throws ObjectNotFoundException, InvalidArgumentException{
		SOrg sOrg = new SOrg();
		sOrg.setOrganno((String)kCol.getDataValue("ORGANNO".toLowerCase()));
		sOrg.setSuporganno((String)kCol.getDataValue("SUPORGANNO".toLowerCase()));
		sOrg.setLocate((String)kCol.getDataValue("LOCATE".toLowerCase()));
		sOrg.setOrganname((String)kCol.getDataValue("ORGANNAME".toLowerCase()));
		sOrg.setOrganshortform((String)kCol.getDataValue("ORGANSHORTFORM".toLowerCase()));
		sOrg.setEnname((String)kCol.getDataValue("ENNAME".toLowerCase()));
		try{
			sOrg.setOrderno(Integer.parseInt((String)kCol.getDataValue("ORDERNO".toLowerCase())));	
		}catch(NumberFormatException e){
			sOrg.setOrderno(-1);
		}
		
		
		sOrg.setDistname((String)kCol.getDataValue("DISTNAME".toLowerCase()));
		sOrg.setLaunchdate((String)kCol.getDataValue("LAUNCHDATE".toLowerCase()));
		try{
			sOrg.setOrganlevel(Integer.parseInt((String)kCol.getDataValue("ORGANLEVEL".toLowerCase())));
		}catch(NumberFormatException e){
			sOrg.setOrganlevel(-1);
		}
		
		
		try{
			sOrg.setState(Integer.parseInt((String)kCol.getDataValue("STATE".toLowerCase())));	
		}catch(NumberFormatException e){
			sOrg.setState(-1);
		}
		sOrg.setFincode((String)kCol.getDataValue("FINCODE".toLowerCase()));
		sOrg.setOrganchief("");
		sOrg.setTelnum((String)kCol.getDataValue("TELNUM".toLowerCase()));
		sOrg.setAddress((String)kCol.getDataValue("ADDRESS".toLowerCase()));
		sOrg.setPostcode((String)kCol.getDataValue("POSTCODE".toLowerCase()));
		sOrg.setControl((String)kCol.getDataValue("CONTROL".toLowerCase()));
		sOrg.setArtiOrganno((String)kCol.getDataValue("ARTI_ORGANNO".toLowerCase()));
		sOrg.setDistno((String)kCol.getDataValue("DISTNO".toLowerCase()));
		
	//	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FNC_CONF_TYP:"+rs.getString("FNC_CONF_TYP"));
		
		Map<String,SOrg> sOrgMap=SInfoFactory.getSysInfoFactoryInstance().getSOrgMap();
		sOrgMap.put(sOrg.getOrganno(), sOrg);
	}
	
	/**
	 * 修改机构后动态修改map信息
	 * @param kCol
	 * @throws ObjectNotFoundException
	 * @throws InvalidArgumentException
	 */
	public static void modifyOrgMapInfo(KeyedCollection kCol) throws ObjectNotFoundException, InvalidArgumentException{
		Map<String,SOrg> sOrgMap=SInfoFactory.getSysInfoFactoryInstance().getSOrgMap();
		
		SOrg sOrg = sOrgMap.get((String)kCol.getDataValue("ORGANNO".toLowerCase()));
		if(null!=sOrg){
			sOrg.setOrganno((String)kCol.getDataValue("ORGANNO".toLowerCase()));
			sOrg.setSuporganno((String)kCol.getDataValue("SUPORGANNO".toLowerCase()));
			sOrg.setLocate((String)kCol.getDataValue("LOCATE".toLowerCase()));
			sOrg.setOrganname((String)kCol.getDataValue("ORGANNAME".toLowerCase()));
			sOrg.setOrganshortform((String)kCol.getDataValue("ORGANSHORTFORM".toLowerCase()));
			sOrg.setEnname((String)kCol.getDataValue("ENNAME".toLowerCase()));
			try{
				sOrg.setOrderno(Integer.parseInt((String)kCol.getDataValue("ORDERNO".toLowerCase())));	
			}catch(NumberFormatException e){
				sOrg.setOrderno(-1);
			}
			sOrg.setDistname((String)kCol.getDataValue("DISTNAME".toLowerCase()));
			sOrg.setLaunchdate((String)kCol.getDataValue("LAUNCHDATE".toLowerCase()));
			try{
				sOrg.setOrganlevel(Integer.parseInt((String)kCol.getDataValue("ORGANLEVEL".toLowerCase())));	
			}catch(NumberFormatException e){
				sOrg.setOrganlevel(-1);
			}
			
			sOrg.setFincode((String)kCol.getDataValue("FINCODE".toLowerCase()));
			try{
				sOrg.setState(Integer.parseInt((String)kCol.getDataValue("STATE".toLowerCase())));	
			}catch(NumberFormatException e){
				sOrg.setState(-1);
			}
			
			sOrg.setOrganchief("");
			sOrg.setTelnum((String)kCol.getDataValue("TELNUM".toLowerCase()));
			sOrg.setAddress((String)kCol.getDataValue("ADDRESS".toLowerCase()));
			sOrg.setPostcode((String)kCol.getDataValue("POSTCODE".toLowerCase()));
			sOrg.setControl((String)kCol.getDataValue("CONTROL".toLowerCase()));
			sOrg.setArtiOrganno((String)kCol.getDataValue("ARTI_ORGANNO".toLowerCase()));
			sOrg.setDistno((String)kCol.getDataValue("DISTNO".toLowerCase()));
		}
	}
	
	
	/**
	 * 删除机构后动态修改map信息
	 * @param kCol
	 * @throws ObjectNotFoundException
	 * @throws InvalidArgumentException
	 */
	public static void delOrgMapInfo(String id) throws ObjectNotFoundException, InvalidArgumentException{
		Map<String,SOrg> sOrgMap=SInfoFactory.getSysInfoFactoryInstance().getSOrgMap();
		SOrg sOrg = sOrgMap.get(id);
		if(null!=sOrg){
			sOrgMap.remove(id);
		}
	}
	
	/**
	 * 查看org map内容
	 */
	public static void findAllOrgInfo(){
		Map<String,SOrg> sOrgMap=SInfoFactory.getSysInfoFactoryInstance().getSOrgMap();
		Set<Entry<String, SOrg>> set = sOrgMap.entrySet(); 
		Iterator<Entry<String, SOrg>> itor = set.iterator(); 
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"*************org mapinfo *****************");
		while(itor.hasNext()) 
		{ 
		
		Entry<String, SOrg> entry = itor.next(); 
//if(entry.getKey().indexOf("test")!=-1){
	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,entry.getKey()+"**"+(entry.getValue()).getOrganno()+"---"+(entry.getValue()).getOrganname());	
		//}
		 
		} 
	}
	
	/**
	 * 新增用户动态修改map值
	 * @param kCol
	 * @throws ObjectNotFoundException
	 * @throws InvalidArgumentException
	 */
	public static void addUserMapInfo(KeyedCollection kCol) throws ObjectNotFoundException, InvalidArgumentException{
		SUser sUser = new SUser();
		
		sUser.setActorname((String)kCol.getDataValue("ACTORNAME".toLowerCase()));
		sUser.setActorno((String)kCol.getDataValue("actorno".toLowerCase()));
		sUser.setAllowopersys((String)kCol.getDataValue("allowopersys".toLowerCase()));
		sUser.setAnswer((String)kCol.getDataValue("answer".toLowerCase()));
		sUser.setBirthday((String)kCol.getDataValue("birthday".toLowerCase()));
		sUser.setBirthday((String)kCol.getDataValue("birthday".toLowerCase()));
		sUser.setCreater((String)kCol.getDataValue("creater".toLowerCase()));
		sUser.setCreattime((String)kCol.getDataValue("creattime".toLowerCase()));
		sUser.setFiredate((String)kCol.getDataValue("firedate".toLowerCase()));
		sUser.setIdcardno((String)kCol.getDataValue("idcardno".toLowerCase()));
		sUser.setIpmask((String)kCol.getDataValue("ipmask".toLowerCase()));
		sUser.setIsadmin((String)kCol.getDataValue("isadmin".toLowerCase()));
		sUser.setLastlogdat((String)kCol.getDataValue("lastlogdat".toLowerCase()));
		sUser.setMemo((String)kCol.getDataValue("memo".toLowerCase()));
		sUser.setNickname((String)kCol.getDataValue("nickname".toLowerCase()));
		//sUser.setOrderno((String)kCol.getDataValue("orderno"));
		sUser.setState((String)kCol.getDataValue("state".toLowerCase()));
		sUser.setTelnum((String)kCol.getDataValue("telnum".toLowerCase()));
		sUser.setUsermail((String)kCol.getDataValue("usermail".toLowerCase()));
		//sUser.setActorright((String)kCol.getDataValue("actorright".toLowerCase()));
			
	/*	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FNC_CONF_TYP:"+rs.getString("FNC_CONF_TYP"));
		*/		
		Map<String,SUser> sUerMap=SInfoFactory.getSysInfoFactoryInstance().getSUserMap();		
		sUerMap.put(sUser.getActorno(), sUser);
	}
	
	
	/**
	 * 修改用户动态修改map值
	 * @param kCol
	 * @throws ObjectNotFoundException
	 * @throws InvalidArgumentException
	 */
	public static void modifyUserMapInfo(KeyedCollection kCol) throws ObjectNotFoundException, InvalidArgumentException{
		Map<String,SUser> sUerMap=SInfoFactory.getSysInfoFactoryInstance().getSUserMap();		
		SUser sUser =sUerMap.get((String)kCol.getDataValue("actorno"));
		if(null!=sUser){
			sUser.setActorname((String)kCol.getDataValue("ACTORNAME".toLowerCase()));
			sUser.setActorno((String)kCol.getDataValue("actorno".toLowerCase()));
			sUser.setAllowopersys((String)kCol.getDataValue("allowopersys".toLowerCase()));
			sUser.setAnswer((String)kCol.getDataValue("answer".toLowerCase()));
			sUser.setBirthday((String)kCol.getDataValue("birthday".toLowerCase()));
			sUser.setBirthday((String)kCol.getDataValue("birthday".toLowerCase()));
			sUser.setCreater((String)kCol.getDataValue("creater".toLowerCase()));
			sUser.setCreattime((String)kCol.getDataValue("creattime".toLowerCase()));
			sUser.setFiredate((String)kCol.getDataValue("firedate".toLowerCase()));
			sUser.setIdcardno((String)kCol.getDataValue("idcardno".toLowerCase()));
			sUser.setIpmask((String)kCol.getDataValue("ipmask".toLowerCase()));
			sUser.setIsadmin((String)kCol.getDataValue("isadmin".toLowerCase()));
			sUser.setLastlogdat((String)kCol.getDataValue("lastlogdat".toLowerCase()));
			sUser.setMemo((String)kCol.getDataValue("memo".toLowerCase()));
			sUser.setNickname((String)kCol.getDataValue("nickname".toLowerCase()));
			//sUser.setOrderno((String)kCol.getDataValue("orderno"));
			sUser.setState((String)kCol.getDataValue("state".toLowerCase()));
			sUser.setTelnum((String)kCol.getDataValue("telnum".toLowerCase()));
			sUser.setUsermail((String)kCol.getDataValue("usermail".toLowerCase()));
			//sUser.setActorright((String)kCol.getDataValue("actorright".toLowerCase()));
		}
	}
	
	/**
	 * 删除用户动态修改map值
	 * @param kCol
	 * @throws ObjectNotFoundException
	 * @throws InvalidArgumentException
	 */
	public static void delUserMapInfo(String id) throws ObjectNotFoundException, InvalidArgumentException{
		Map<String,SUser> sUerMap=SInfoFactory.getSysInfoFactoryInstance().getSUserMap();		
		SUser sUser =sUerMap.get(id);
		if(null!=sUser){
			sUerMap.remove(id);
		}
	}
	
	
	/**
	 * 查看user map内容
	 */
	public static void findAllUserInfo(){
		Map<String,SUser> sUerMap=SInfoFactory.getSysInfoFactoryInstance().getSUserMap();
		Set<Entry<String, SUser>> set = sUerMap.entrySet(); 
		Iterator<Entry<String, SUser>> itor = set.iterator(); 
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"*************item map *****************");
		while(itor.hasNext()) 
		{ 
		Entry<String, SUser> entry = itor.next(); 
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,entry.getKey()+"**"+(entry.getValue()).getActorname()+"---"+(entry.getValue()).getActorno()); 
		}
	}
	
	/**
	 * 返回name[id]的格式
	 * @param name
	 * @param Id
	 * @return
	 */
	private static String formatName(String name,String Id){
		String rt=name;
		rt=name+"["+Id+"]";
		return rt;
	}
	
	/**
	 * 拆分name[id]的格式
	 * @param param
	 * @return
	 */
	private static String[] unFormantName(String param){
		String name=param.substring(0, param.indexOf("["));
		String id=TransUtil.getParam(param,'[',']');
		String rt[]={name,id} ;
		
		return rt;
	}
	

	
	//两个数相减
	public  static double getAmtSubtract(Double AMT1,Double AMT2) throws EMPException{
		return AMT1-AMT2;
	}
	
	//两个数相减
	public  static double getAmtSubtract(int AMT1,Double AMT2) throws EMPException{
		return AMT1-AMT2;
	}
	
	//两个数相加
	public  static double getAmtAdd(Double AMT1,Double AMT2) throws EMPException{
		return AMT1+AMT2;
	}
	//两个数相加
	public  static double getAmtAdd(int AMT1,Double AMT2) throws EMPException{
		return AMT1+AMT2;
	}
	//两个数相乘
	public  static double getAmtMultiply(Double AMT1,Double AMT2) throws EMPException{
		return AMT1*AMT2;
	}
	
	//两个数相除
	public  static double getAmtDivide(Double AMT1,int AMT2) throws EMPException{
		return AMT1/AMT2;
	}
	//两个数相除
	public  static double getAmtDivide(Double AMT1,Double AMT2) throws EMPException{
		return AMT1/AMT2;
	}
	//两个数相除
	public  static double getAmtDivide(int AMT1,Double AMT2) throws EMPException{
		return AMT1/AMT2;
	}
	//两个数取最大值
	public  static double getMaxValue(Double AMT1,Double AMT2) throws EMPException{
		if(AMT2>AMT1) AMT1=AMT2;
		return AMT1;
	}
	//两个数取最大值
	public  static int getMaxValue(int AMT1,int AMT2) throws EMPException{
		if(AMT2>AMT1) AMT1=AMT2;
		return AMT1;
	}
	//两个数取最大值
	public  static float getMaxValue(float AMT1,float AMT2) throws EMPException{
		if(AMT2>AMT1) AMT1=AMT2;
		return AMT1;
	}

	//两个数取最小值
	public  static double getMinValue(Double AMT1,Double AMT2) throws EMPException{
		if(AMT2<AMT1) AMT1=AMT2;
		return AMT1;
	}
	//两个数取最小值
	public  static int getMinValue(int AMT1,int AMT2) throws EMPException{
		if(AMT2<AMT1) AMT1=AMT2;
		return AMT1;
	}
	//两个数取最小值
	public  static float getMinValue(float AMT1,float AMT2) throws EMPException{
		if(AMT2<AMT1) AMT1=AMT2;
		return AMT1;
	}
	//两个数取之和
	public  static Double getTotlValue(Double AMT1,Double AMT2) throws EMPException{
		
		return AMT1+AMT2;
	}
	//取整数
	public  static int getIntValue(Double AMT1) throws EMPException{
		
		return Integer.parseInt(AMT1+"");
	}
}
