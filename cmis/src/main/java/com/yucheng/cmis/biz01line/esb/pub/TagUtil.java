package com.yucheng.cmis.biz01line.esb.pub;

import java.sql.Connection;
import java.util.Iterator;

import com.dc.eai.data.Array;
import com.dc.eai.data.AtomData;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ClientTradeInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class TagUtil {

	/**
	 * 返回神码API需要的单记录数据类型,数据类型可自动添加，目前只放置五种常用类型
	 * @param fieldType 数据类型
	 * @param length 数据长度
	 * @param scale 数据精度
	 * @return com.dc.eai.data.Field
	 */
	public static Field getEMPField(Object value, FieldType fieldType, int length, int scale) throws Exception {
		/** 出去value值中@名称，取出其值 */
		if(value == null){
			value = "";
		}
		String valueHelp = value.toString();
		valueHelp = valueHelp.substring(valueHelp.indexOf("@")+1, valueHelp.length());
		Field field =null;
		//chenBQ 20190409 如果为空则统一类型为String
		if(valueHelp.trim().length() == 0) {
			field = new Field(new FieldAttr(FieldType.FIELD_STRING, length, scale));
			String tt = field.getFieldType().getName();
			field.setValue(valueHelp.toString());		
		} else {
			field = new Field(new FieldAttr(fieldType, length, scale));
			String tt = field.getFieldType().getName();
			if(field.getFieldType().getName().equals("string")){
				field.setValue(valueHelp.toString());		
			}
			if(field.getFieldType().getName().equals("int")){
				field.setValue(Integer.parseInt(valueHelp.toString()));		
			}
			if(field.getFieldType().getName().equals("long")){
				field.setValue(Long.parseLong(valueHelp.toString()));
			}
			if(field.getFieldType().getName().equals("double")){
				field.setValue(Double.valueOf(valueHelp.toString()));
				
			}
			if(field.getFieldType().getName().equals("float")){
				field.setValue(Float.valueOf(valueHelp.toString()));
			}
		}
		return field;
	}
	
	/**
	 * 解析CompositeData结构体，返回EMP KeyedCollection
	 * @param data 解析结构体
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public static KeyedCollection replaceCD2KColl(CompositeData data) throws Exception {
		if(data == null){
			return null;
		}
		KeyedCollection kColl = new KeyedCollection();
		for(Iterator it = data.iterator();it.hasNext();){
			String elementName = (String)it.next();
			AtomData element = data.getObject(elementName);
			if(element instanceof CompositeData){
				KeyedCollection subKColl = replaceCD2KColl((CompositeData)element);
				subKColl.setName(elementName);
				kColl.addKeyedCollection(subKColl);
			}else if(element instanceof Field){
				Field field = (Field)element;
				Object fieldValue = field.getValue();
				kColl.addDataField(elementName, fieldValue);
			}else if(element instanceof Array){
				Array array = (Array)element;
				IndexedCollection iColl = new IndexedCollection(elementName);
				for(int i=0;i<array.size();i++){
					CompositeData arrCD = array.getStruct(i);
					iColl.add(replaceCD2KColl(arrCD));
				}
				kColl.addIndexedCollection(iColl);
			}
		}
		return kColl;
	} 
	/**
	 * 控制转换，将null装换为""
	 * @param o 转换对象
	 * @return
	 * @throws Exception
	 */
	public static String replaceNull4String(Object o) throws Exception {
		String retValue = "";
		if(o == null || o == "null"){
			retValue = "";
		}else {
			retValue = o.toString();
		}
		return retValue;
	}
	/**
	 * 控制转换，将null转换为0
	 * @param o 转换对象
	 * @return
	 * @throws Exception
	 */
	public static double replaceNull4Double(Object o) throws Exception {
		double retValue = 0;
		if(o == null || o == "null" || o.toString().trim() == ""){
			retValue = 0;
		}else {
			retValue = Double.parseDouble(o.toString());
		}
		return retValue;
	}
	/**
	 * 控制转换，将null转换为0
	 * @param o 转换对象
	 * @return
	 * @throws Exception
	 */
	public static int replaceNull4Int(Object o) throws Exception {
		int retValue = 0;
		if(o == null || o == "null" || o.toString().trim() == ""){
			retValue = 0;
		}else {
			retValue = Integer.parseInt(o.toString());
		}
		return retValue;
	}
	/**
	 * 日期的格式化转换，去除"-"
	 * @return 格式化后日期，为8位长度
	 */
	public static String formatDate(Object o) throws Exception {
		String retValue = "";
		if(o == null || o == "null"){
			retValue = "";
		}else {
			retValue = o.toString().replaceAll("-", "");
		}
		return retValue;
	}
	/**
	 * 日期的格式化转换，加上"-"
	 * @return 格式化后日期，为10位长度
	 */
	public static String formatDate2Ten(Object o) throws Exception {
		String retValue = "";
		if(o == null || o == "null"){
			retValue = "";
		}else {
			if(o.toString().length()!=8){
				retValue = o.toString();
			}else{
				retValue = o.toString().substring(0, 4)+"-"+o.toString().substring(4,6)+"-"+o.toString().substring(6, 8);
			}
		}
		return retValue;
	}
	/**
	 * 授权发送转换，将需要发送的授权信息转换为可读KeyedCollection
	 * @param KColl 授权查询出的KeyedCollection
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public static KeyedCollection getReflectKColl(KeyedCollection KColl) throws Exception {
		KeyedCollection retKColl = new KeyedCollection(KColl.getName());
		for(int i=0;i<KColl.size();i++){
			DataElement element = (DataElement) KColl.getDataElement(i);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				String columnValue = (String)aField.getValue();
				if(columnValue!= null && columnValue.indexOf("@") != -1){
					String arr[] = columnValue.split("@");
					if(arr.length < 2){
						retKColl.addDataField(arr[0], "");
					}else {
						String key = arr[0];
						String value = arr[1];
						if(value == null || value == "null"){
							value = "";
						}
						retKColl.addDataField(key, value);
					}
				}
			}
		}
		return retKColl;
	}
	/**
	 * 封装发送到ESB的特殊需求流水号，规则如下：
	 * 源系统标识号（6位）+源系统交易日期（8位：YYYYMMDD)+批次号（2位）+业务流水序号
	 * 默认300008                                                                                                          默认00            
	 * @param serno 业务流水号
	 * @param context 上下文
	 * @return 规则业务流水号
	 * @throws Exception
	 */
	public static String getSysHeadSerno(String serno, Context context) throws Exception {
		String nowDate = (String)context.getDataValue(CMISConstance.OPENDAY);
		return TradeConstance.CONSUMERID+nowDate.replaceAll("-", "")+"00"+serno;
	}
	/**
	 * 返回交易封装的默认结果
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public static KeyedCollection getDefaultResultKColl() throws Exception {
		KeyedCollection retKColl = new KeyedCollection();
		retKColl.addDataField("ret_code", "000000");
		retKColl.addDataField("ret_msg", "");
		return retKColl;
	}
	/**
	 * 解析报文反馈信息是否正确，true表示交易成功，false表示失败
	 * @param returnKColl  反馈信息
	 * @param context 上下文
	 * @return boolean
	 * @throws EMPException
	 */
	public static boolean haveSuccess(KeyedCollection returnKColl, Context context) throws EMPException{
		boolean flag  = true;
		if(returnKColl != null && returnKColl.size() > 0){
			String retStatus = (String)returnKColl.getDataValue("RET_STATUS");
			String retCode = (String)returnKColl.getDataValue("RET_CODE");
			if(!retCode.equals(TradeConstance.RETCODE1)){
				flag = false;
			}
		}else {
			flag = false;
		}
		return flag;
	}
	/**
	 * 通过业务实现类，获取返回报文
	 * @param impleClass 实现类
	 * @param context 
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public static KeyedCollection getRespCD(String impleClass, Context context, Connection connection) throws Exception {
		KeyedCollection resultKColl = new KeyedCollection();
		ClientTradeInterface clientTradeInterface = (ClientTradeInterface)Class.forName(impleClass.trim()).newInstance();
		CompositeData reqCD = clientTradeInterface.doExecute(context, connection);
		CompositeData retCD = ESBClient.request(reqCD);
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		/** 解析反馈报文体 */
		KeyedCollection retKColl = esbInterfacesImple.getRespSysHeadCD(retCD);
		KeyedCollection respBodyKColl = esbInterfacesImple.getRespBodyCD4KColl(retCD);
		
		if(TagUtil.haveSuccess(retKColl, context)){
			resultKColl.addDataField("flag", "success");
			resultKColl.addDataField("retMsg", (String)retKColl.getDataValue("RET_MSG"));
			resultKColl.addKeyedCollection(respBodyKColl);
		}else {
			resultKColl.addDataField("flag", "failed");
			resultKColl.addDataField("retMsg", (String)retKColl.getDataValue("RET_MSG"));
		}
		return resultKColl;
	}

}
