package com.yucheng.cmis.biz01line.dth.op.pubAction;

import java.sql.Connection;
import java.util.LinkedList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.component.DthPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.flashcharts.FCFCharHelper;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.TreeDicTools;

public class DthPubAction extends CMISOperation {

	/**
	 * 风险预警
	 * @author GC.20131220
	 */
	public String doExecute(Context context) throws EMPException {
		return "0";
	}
	
	/*** 返回无分页icoll的命名sql调用，kcoll中应该包括submitType***/
	public IndexedCollection delSqlReturnIcoll(KeyedCollection kColl , Context context) throws EMPException {
		DthPubComponent cmisComponent = (DthPubComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("DthPubComponent", context , this.getConnection(context));
		IndexedCollection Results = new IndexedCollection();
		Results =  cmisComponent.delSqlReturnIcoll(kColl);
		return Results;
	}
	
	/*** 返回kcoll的命名sql调用，kcoll中应该包括submitType ***/
	public KeyedCollection delSqlReturnKcoll(KeyedCollection kColl ,Context context) throws EMPException {
		DthPubComponent cmisComponent = (DthPubComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("DthPubComponent", context, this.getConnection(context));
		KeyedCollection Results = new KeyedCollection();
		Results = cmisComponent.delSqlReturnKcoll(kColl);
		return Results;
	}
	
	/*** 无返回值的命名sql调用，kcoll中应该包括submitType ***/
	public void delSqlNoReturn(KeyedCollection kColl ,Context context) throws EMPException {
		DthPubComponent cmisComponent = (DthPubComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("DthPubComponent", context, this.getConnection(context));
		cmisComponent.delSqlNoReturn(kColl);
	}
	
	/*** 带默认值的kColl取值 ***/
	public Object getDataDefvalue(KeyedCollection kColl, String name ,Object Defvalue ) throws EMPException {
		Object result = kColl.getDataValue(name);
		result = (result==null||result.equals(""))?Defvalue:result;
		return result;
	}
	
	/**
	 * 生成单系flash
	 * @param KeyedCollection styles 中必须包括xName,yName,jsValue,jsName,submitType
	 * @return singleXML 返回生成flash的XML
	 * @throws Exception 
	 */	
	public String delSingleCharts(Context context,KeyedCollection styles , KeyedCollection kColl) throws Exception {
		/*** 1.通过命名sql取值 ***/
		IndexedCollection iColl_data = delSqlReturnIcoll(kColl, context);

		/*if(iColl_data.size() < 1){
			return "";
		}*/
		/*** 2.图形数据加工处理 ***/
		String xName = styles.getDataValue("xName").toString();	//配置flash上x轴参数
		String yName = styles.getDataValue("yName").toString();	//配置flash上y轴参数
		String jsValue = styles.getDataValue("jsValue").toString();	//传到js点击方法上的参数
		String jsName = styles.getDataValue("jsName").toString();	//js方法名称
		
		IndexedCollection iColl_Chart = new IndexedCollection("delChart");
		for(int i = 0 ; i < iColl_data.size() ; i++){
			KeyedCollection kColl_data = (KeyedCollection) iColl_data.get(i);
			KeyedCollection kColl_Chart = new KeyedCollection();
			kColl_Chart.addDataField("xName", getDataDefvalue(kColl_data, xName, ""));
			kColl_Chart.addDataField("yName", getDataDefvalue(kColl_data, yName, "0"));
			if(!jsName.equals("")){	//点击弹出框设置link，不用link则设为空
				String data = "";
				if(kColl_data.containsKey(jsValue)){
					if(null != kColl_data.getDataValue(jsValue))data = kColl_data.getDataValue(jsValue).toString();
					if(styles.containsKey("jsValuePlus")){	//参数追加,不管追加几个都以逗号隔开
						data = data + "," + styles.getDataValue("jsValuePlus");
					}
				}else{
					data = jsValue;
				}
				kColl_Chart.addDataField("link","JavaScript:"+jsName+"(&quot;"+data+"&quot;)");
			}
			iColl_Chart.add(kColl_Chart);
		}

		/*** 3.根据配置生成xml ***/
		String singleXML = FCFCharHelper.createSingleCharXML(iColl_Chart, styles);
		return singleXML;
	}
	
	/**
	 * 生成二维多系flash
	 * @param KeyedCollection styles 中必须包括xName,yName,jsName,submitType,jsValue_one,jsValue_two,showCnname
	 * @param showName 图形下方的说明名称
	 * @return multiXML 返回生成flash的XML
	 * @throws Exception 
	 */	
	public String delMultiCharts(Context context,KeyedCollection styles,String[] showName , KeyedCollection kColl) throws Exception {
		/*** 通过命名sql取值 ***/
		IndexedCollection iColl_data = delSqlReturnIcoll(kColl, context);

		if(iColl_data.size() < 1){
			return "";
		}
		/*** 图形数据加工处理 ***/
		String xName = styles.getDataValue("xName").toString();	//配置flash上x轴参数
		String yName = styles.getDataValue("yName").toString();	//配置flash上y轴参数
		String jsName = styles.getDataValue("jsName").toString();	//js方法名称
		String showCnname = styles.getDataValue("showCnname").toString();	//与showName对应的名称
		//二维图形下两个参数确定唯一值，所以两个参数够了
		String jsValue_one = styles.getDataValue("jsValue_one").toString();	//传到js点击方法上的参数一，并要与showName对应
		String jsValue_two = styles.getDataValue("jsValue_two").toString();	//传到js点击方法上的参数二
		
		int i,j,lengths = showName.length;
		IndexedCollection iColl_Chart[] = new IndexedCollection[lengths];
		for(i = 0 ; i < lengths ; i++){
			iColl_Chart[i] = new IndexedCollection();
			for(j = 0 ; j < iColl_data.size() ; j++){
				KeyedCollection kColl_data = (KeyedCollection) iColl_data.get(j);
				KeyedCollection kColl_Chart = new KeyedCollection();
				if(kColl_data.getDataValue(showCnname).equals(showName[i])){
					kColl_Chart.addDataField("xName", getDataDefvalue(kColl_data, xName, ""));
					kColl_Chart.addDataField("yName", getDataDefvalue(kColl_data,yName, "0"));
					if(!jsName.equals("")){	//点击弹出框设置link，不用link则设为空
						String data_one="",data_two="";
						if(null != kColl_data.getDataValue(jsValue_one))data_one = kColl_data.getDataValue(jsValue_one).toString();
						if(null != kColl_data.getDataValue(jsValue_two))data_two = kColl_data.getDataValue(jsValue_two).toString();
						kColl_Chart.addDataField("link","JavaScript:"+jsName+"(&quot;"+data_one+","+data_two+"&quot;)");
					}
					iColl_Chart[i].add(kColl_Chart);
				}
			}
		}
		
		String multiXML = FCFCharHelper.createXMLDataMultis(iColl_Chart, styles, showName);		
		return multiXML;
	}
	
	/**
	 * 生成单系flash，传入已获得的icoll
	 * @param KeyedCollection styles 中必须包括xName,yName,jsValue,jsName,submitType
	 * @return singleXML 返回生成flash的XML
	 * @throws Exception 
	 */	
	public String delSingleChartsIcoll(Context context,KeyedCollection styles , IndexedCollection iColl_data) throws Exception {
		/*** 2.图形数据加工处理 ***/
		String xName = styles.getDataValue("xName").toString();	//配置flash上x轴参数
		String yName = styles.getDataValue("yName").toString();	//配置flash上y轴参数
		String jsValue = styles.getDataValue("jsValue").toString();	//传到js点击方法上的参数
		String jsName = styles.getDataValue("jsName").toString();	//js方法名称
		
		IndexedCollection iColl_Chart = new IndexedCollection("delChart");
		for(int i = 0 ; i < iColl_data.size() ; i++){
			KeyedCollection kColl_data = (KeyedCollection) iColl_data.get(i);
			KeyedCollection kColl_Chart = new KeyedCollection();
			kColl_Chart.addDataField("xName", getDataDefvalue(kColl_data, xName, ""));
			kColl_Chart.addDataField("yName", getDataDefvalue(kColl_data, yName, "0"));
			if(!jsName.equals("")){	//点击弹出框设置link，不用link则设为空
				String data = "";
				if(kColl_data.containsKey(jsValue)){
					if(null != kColl_data.getDataValue(jsValue))data = kColl_data.getDataValue(jsValue).toString();
					if(styles.containsKey("jsValuePlus")){	//参数追加,不管追加几个都以逗号隔开
						data = data + "," + styles.getDataValue("jsValuePlus");
					}
				}else{
					data = jsValue;
				}
				kColl_Chart.addDataField("link","JavaScript:"+jsName+"(&quot;"+data+"&quot;)");
			}
			iColl_Chart.add(kColl_Chart);
		}

		/*** 3.根据配置生成xml ***/
		String singleXML = FCFCharHelper.createSingleCharXML(iColl_Chart, styles);
		return singleXML;
	}
	
	/*** 数组去重复 ***/
	public String[] removeRepeat( String[] str ) {
		LinkedList<String> list = new LinkedList<String>();
		for(int i = 0 ;i < str.length ; i++){
			if(!list.contains(str[i])){
				list.add(str[i]);
			}
		}
		return (String[])list.toArray(new String[list.size()]);		
	}
	
	/*** 多值字典翻译 */
	public KeyedCollection getDicsName(KeyedCollection kCol,String dataId,String opttye,Connection conn) throws Exception {
		if(kCol.getDataValue(dataId)==null||"".equals(kCol.getDataValue(dataId))){
			return kCol;
		}else{
			String prdids = (String) kCol.getDataValue(dataId);
			TreeDicTools treeDicSer = new TreeDicTools();
			String showTxt = treeDicSer.getDicsName(prdids, opttye, conn);			
			if(!"".equals(showTxt)){
				showTxt = showTxt.substring(0, showTxt.length()-1);
			}
			kCol.addDataField(dataId+"_displayname", showTxt);
			return kCol;
		}
	}
	
	/*** 总分支行机构判断处理 ***/
	public String judgeOrg(String organNo ,Connection connection) throws Exception {
		String org_condition = "";
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		OrganizationServiceInterface serviceUser = (OrganizationServiceInterface) serviceJndi
				.getModualServiceById("organizationServices","organization");
		SOrg org_info = serviceUser.getOrgByOrgId(organNo,connection);
		String suporganno = org_info.getSuporganno();
		if(suporganno.equals("9350000000")){	//机构类型：总行
			org_condition = " and 1 = 1 ";	//总行看所有数据，不加条件
		}else{
			org_condition = " and manager_br_id in (select organno from s_org where locate like '%"+organNo+"%') ";
		}

		return org_condition ;
	}
}