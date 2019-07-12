package com.yucheng.cmis.pub.util;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.sql.DataSource;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.ecc.emp.comm.http.HttpResource;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
/**
 * 
 * <p>
 * <h2>简述</h2>
 * 		<ol>ESB交易处理类</ol>
 * <h2>功能描述</h2>
 * 		<ol>请添加功能详细的描述</ol>
 * <h2>修改历史</h2>
 *    <ol>如有修改，添加修改历史</ol>
 * </p>
 * @author 杨锦华
 * @2016-4-19
 * @version 1.0
 */
@SuppressWarnings("unused")
public class ESBUtil {
	
	/**ESB配置**/
	private static Map<String, String> config = null;
	/**请求报文头**/
	private static StringBuffer reqHead = null;
	/**响应报文头**/
	private static StringBuffer rspHead = null;
	/**响应报文**/
	private static String response = "";
	/**响应码**/
	private static String returnCode = "";
	/**响应信息**/
	private static String returnMsg = "";
	/**服務id**/
	private static String flowId = "";
	/**交易流水号**/
	private static String tradeSerno = "";
	/**交易ID**/
	private static String serviceCode = "";
	/**交易 类型**/
	private static String serviceType = "";
	/**交易成功**/
	private static String YES = "1";
	/**交易失败**/
	private static String NO = "2";
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>初始化方法</ol>
	 * </p>
	 */
	public static void init(){
		reqHead = new StringBuffer();
		rspHead = new StringBuffer();
		response = "";
		returnCode = "";
		returnMsg = "";
	}
 

	
	/***
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>将DataElement转成JSON字符串格式</ol>
	 *   	<ol>将DataElement转成JSON字符串格式</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>不支持iColl里还有iColl的情况，DataElement尽量使用DataObjectSwitch转换</ol>
	 * </p>
	 * @author xumingtai
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public static String toJSONString(KeyedCollection data) throws Exception{
		String s=data.toString();
	
		s=replaceIColl2(s); 
		s=replaceFild(s);
		s=replaceKColl(s);
		return s.replaceAll("\\n*", "").replaceAll("\\t*", "");
	}
	
	/***
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>获取标签属性值</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>获取属性=""引号内的内容,只查找最前面的，只传需要获取属性的标签字符串</ol>
	 * </p>
	 * @author xumingtai
	 * @param str 需要获取属性的标签字符串
	 * @param id  要获取的属性字符串 
	 * @return
	 */
	public static String getValue(String str,String id){
		 int n=str.indexOf(id);
		 if(n>=0){
			 return str.substring(n+id.length()+2,str.indexOf("\"",n+id.length()+2 ));
		 }
		 return ""; 
	}
	
	
	
	public static String replaceIColl2(String str) {
		int start=str.indexOf("<iColl");
		int end;
		StringBuffer buf=new StringBuffer(str);
		String id="";
		String a="";
		String sub="";
		while(start>=0){
			/***获取<iColl id="" >开始标签**/
			a=buf.substring(start, buf.indexOf(">",start)+1);
			/*****获取iColl的id值*******/
			id=getValue(a,"id");
			/******获取</iColl>结束位置**********/
			end=buf.indexOf("</iColl>");
			/*******将iColl标签删除*********/
			str=buf.substring(start, end+8).replace(a, "");
			str = str.replace("</iColl>", "");
			/*****替换所有数组结束标签******/
			buf.replace(start, end+8, str);
			
			start=buf.indexOf("<iColl");
		}
		return buf.toString();
	}
 
	
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>将字符串中的所有<field>标签转换成XML标签，field格式必须为：<field id="" value=""/></ol>
	 * <h2>功能描述</h2>
	 * 		<ol>请添加功能详细的描述</ol>
	 * </p>
	 * @author xumingtai
	 * @param str
	 * @return
	 */
	public static String replaceFild(String str){
		int start=str.indexOf("<field");
		int end;
		StringBuffer buf=new StringBuffer(str);
		String sub;
		String id;
		String value;
		while(start>=0){
			/***查找标签结束符位置**/
			end=buf.indexOf("/>",start);
			/***获取整个field标签***/
			sub=buf.substring(start,end+2);
			/*****获取标签id值********/
			id=getValue(sub,"id");
			/*******获取标签value值*************/
			value=getValue(sub,"value");
			/*******替换成xml格式************/
			buf.replace(start,end+2, "<"+id+">"+value+"</"+id+">");
			start=buf.indexOf("<field");
		}
		return buf.toString();
		
	}
	/** 
     * 转换一个xml格式的字符串到json格式 
     *  
     * @param xml 
     *            xml格式的字符串 
     * @return 成功返回json 格式的字符串;失败反回null 
     */  
    public static  String xml2JSON(String xml) {   
        try {  
        	XMLSerializer xmlSerializer = new XMLSerializer(); 
            JSON json = xmlSerializer.read(xml);

            return json.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
	
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>将字符串中的所有<kColl>标签转换成XML标签</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>请添加功能详细的描述</ol>
	 * </p>
	 * @author xumingtai
	 * @param str
	 * @return
	 */
	public static String replaceKColl(String str){
		int start=str.indexOf("<kColl");
		int end;
		int next;
		String id="";
		StringBuffer buf=new StringBuffer(str);
		List<Integer> list=new ArrayList<Integer>();
		while(start>-1){
			/*****获取kColl结束标签位置*****/
			end=buf.indexOf("</kColl>",start);
			/*****获取下一个kColl标签位置*****/
			next=buf.indexOf("<kColl",start+1);
			/*****如果kColl开始标签和结束标签之间没有kColl标签则配对成功可替换标签*****/
			if(next>end || next==-1){
				/*****获取kColl的id值*****/
				id=getValue(buf.substring(start), "id");
				/*****先将kColl标签替换成XML格式*****/
				buf.replace(end, end+8, "</"+id+">");
				/*****替换开始标签*****/
				buf.replace(start, buf.indexOf(">", start)+1,"<"+id+">");
				/*****如果还存在未配对的kColl则返回上个kColl位置重新查找*****/
				if(list.size()>0){
					start=list.get(list.size()-1).intValue();
					list.remove(list.size()-1);
				/*******不存在未配对的kColl则继续往下查找************/
				}else{
					start=buf.indexOf("<kColl");	 
				}
			/*****配对失败先缓存kColl位置*****/
			}else{
				list.add(start);
				start=next;
			}
			
		}
		return buf.toString();
		
	}
	
	
	/**
	 * @Title: kCollToJson
	 * @Description: 将KColl转换为json格式报文<br>
	 *               V0.2 增加处理int类型数据方法
	 * @Parmaters: @param reqKColl
	 * @Parmaters: @return
	 * @Return: String
	 * @Throws: Exception
	 * @Author:lutong
	 * @CreateDate:2017年12月15日 上午11:44:35
	 * @Version:0.2
	 * @ModifyLog:2017年12月15日 上午11:44:35
	 */
	public static JSONObject kCollToJson(KeyedCollection reqKColl) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		for (int i = 0; i < reqKColl.size(); i++) {
			DataElement da = reqKColl.getDataElement(i);
			String name = da.getName();
			Object value = reqKColl.get(name);
			if (value instanceof String) {
				// 将KColl中String类型转换为json
				buf.append("\"" + name + "\"" + ":" + "\"" + value.toString() + "\"");
			} else if (value instanceof Integer) {
				// 将KColl中int类型转换为json
				buf.append("\"" + name + "\"" + ":" + (Integer)value);
			}else if (value instanceof Double) {
				// 将KColl中int类型转换为json
				buf.append("\"" + name + "\"" + ":" + (Double)value);
			}else if (value instanceof BigDecimal) {
				// 将KColl中int类型转换为json
				buf.append("\"" + name + "\"" + ":" + (BigDecimal)value);
			}else if(value instanceof IndexedCollection){
				 
				IndexedCollection iColl = (IndexedCollection)value;
				String iCollValue = "\""+ name + "\"" + ":[";
				for (int j = 0; j < iColl.size(); j++) {
					KeyedCollection kColl = (KeyedCollection)iColl.get(j);
					//iCollValue +="\"" + name + "\"" + ":" + kCollToJson(kColl).toString();
					iCollValue += kCollToJson(kColl).toString();
					if (j != iColl.size() - 1) {
						iCollValue +=",";
					} 
				}
				iCollValue +="]";
				buf.append(iCollValue);
			}
			else if(value instanceof KeyedCollection){ 
				buf.append("\"" + name + "\"" + ":" + kCollToJson((KeyedCollection)value).toString()); 
			}

			if (i != reqKColl.size() - 1) {
				buf.append(",");
			}
		}
		buf.append("}");
		JSONObject jsonObject = JSONObject.fromObject(buf.toString());
		return jsonObject;
	}
	
	/**
	 * added by yangzy 2017-07-04
	 * 从配置文件cmis.properties获取交易超时时间
	 * @return
	 */
	public static int getTradeTimeout(){
		int tradeTimeout = 0;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		try{
			tradeTimeout = Integer.valueOf(res.getString("tradeTimeout"));
		}catch (Exception e) {}
		if(tradeTimeout == 0){
			tradeTimeout = 60000;
		}
		return tradeTimeout;
	}
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>XML字符串截取指定标签内容</ol>
	 * <h2>功能描述</h2>
	 * 		<ol></ol>
	 * </p>
	 * @author xumingtai
	 * @param data
	 * @param sub
	 * @return
	 */
	public static String subXMLString(String data,String sub){
		String result="";
		int s1=data.indexOf(ESBConstance.LEFT_C+sub+ESBConstance.RIGHT_C);
		int s2=data.indexOf(ESBConstance.LEFT_END_STR+sub+ESBConstance.RIGHT_C);
		if(s1>=0 && s2>=0 && s1<s2){
			result=data.substring(s1,s2).replace(ESBConstance.LEFT_C+sub+ESBConstance.RIGHT_C, "").trim();
		}
		return result;
	}
	/**
	 * 
	 * <p>Title: sendEsbMsg</p>  
	 * <p>Description:发送ESB请求报文 </p>  
	 * @author xujiahui
	 * @param headColl 报文头，除核心外只传交易码，交易场景
	 * @param bodyColl
	 * @return retKColl
	 * @throws Exception
	 */
	public static KeyedCollection  sendEsbMsg(KeyedCollection headColl,KeyedCollection bodyColl) throws Exception { 
		EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
		Context context = factory.getContextNamed(factory.getRootContextName());
		DataSource dataSource = (DataSource) context
				.getService(CMISConstance.ATTR_DATASOURCE);
		Connection connection = dataSource.getConnection();
		KeyedCollection esbTradeInfo = new KeyedCollection();
		StringBuffer sb = new StringBuffer();
		JSONObject jsonObject = new JSONObject();
		StringBuffer reqStr = new StringBuffer();
		String reMsg = "";
		long time = System.currentTimeMillis();//计算交易耗时，单位ms
		String MESS = "<=================信贷消费方接口交易START=================>"+"\n";
		KeyedCollection retKColl = new KeyedCollection();
		try {	
			String seqNum = String.valueOf((BigDecimal)SqlClient.queryFirst("queryEsbSeq", "", null, connection));
			//String tranDate = TimeUtil.getDateyyyyMMdd();
			String tranDate = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			tranDate = tranDate.replaceAll("-", "");
			String tranTime = TimeUtil.getDateHHmmss();
			String seqNo = String.format("%012d", Integer.valueOf(seqNum));;
			String glSeqNo = "300400"+tranDate+tranTime+seqNo;
			KeyedCollection sysHead = new KeyedCollection("SYS_HEAD");
			KeyedCollection appHead = new KeyedCollection("APP_HEAD");
			String pubReqBody = "";
			String pubSysHead="";	
			String pubAppHead="";	
			if(headColl.containsKey("jkType")&&"cbs".equals((String)headColl.getDataValue("jkType"))){
				//核心特殊报文头
			    sysHead.put("SvcCd",  headColl.getDataValue("SvcCd"));//服务代码
			    sysHead.put("ScnCd", headColl.getDataValue("ScnCd"));//场景代码
			    sysHead.put("CnsmrCnlTp",  "020114");// 消费方渠道类型
			    sysHead.put("CnsmrSysId","300400");//消费方系统编号
			    sysHead.put("CnsmrSysSrlNo",  seqNo);// 消费方系统流水号
			    sysHead.put("SrcCnsmrSysId", "300400");//源消费方系统编号
			    sysHead.put("CnsmrSrvInd", "");//消费方服务器标识
		    	sysHead.put("GlblSeqNo", glSeqNo);//全局流水号
			    sysHead.put("TxnDt", tranDate);//交易日期
			    sysHead.put("TxnTm", tranTime);//交易日期
			    sysHead.put("FileFlg","0");//文件标志
			    sysHead.put("FileNm", "");//文件名称
			    sysHead.put("SvcVerNo","");//服务版本号
			    sysHead.put("TmlNo", "");//终端号
			    sysHead.put("MACVal", "");//MAC值
			    sysHead.put("BrCd","9901");//机构代码必输  
			    sysHead.put("TlrNo","CMS01"); //柜员号必输  
			     
			    sysHead.put("TxnMd", "ONLINE");
			    sysHead.put("UsrLngKnd", "CHINESE");
			    sysHead.put("TxnScrnInd","");
			    sysHead.put("AuthTlrNo","");
			    sysHead.put("AuthFlg","M");
			    sysHead.put("TxnInptTlrNo","");
			    sysHead.put("ReChkFlg","");
			    sysHead.put("SrcNodeNo","");
			    sysHead.put("TrgtNodeNo","");
			    sysHead.put("LglPrsnCd","");
			    sysHead.put("AuthTlrLvl","");
			    sysHead.put("ClntChkRsltFlg","");
			    
			    if(headColl.containsKey("TurnPgFlg")){//加了翻页
			    	 appHead.put("TurnPgFlg","1");//翻页标志
					 appHead.put("PgDsplLineNum",headColl.getDataValue("PgDsplLineNum"));//每页显示条数
					 appHead.put("CrnPgRcrdNo",headColl.getDataValue("CrnPgRcrdNo"));//当前页记录号
			    }else{
			    	appHead.put("TurnPgFlg","");//翻页标志
					 appHead.put("PgDsplLineNum","");//每页显示条数
					 appHead.put("CrnPgRcrdNo","");//当前页记录号
			    }
			    
			   
			    appHead.put("SmzgFlg","");//汇总标志 
			    
			    appHead.put("BrCd","9901");//机构代码
			    appHead.put("TlrNo","CMS01");//柜员号
			    
			    
			
			    KeyedCollection fdcrPrsnInfKcol = new KeyedCollection("FdcrPrsnInfArry");
			    fdcrPrsnInfKcol.put("FdcrPrsnIdentTp","");
			    fdcrPrsnInfKcol.put("FdcrPrsnIdentNo","");
			    fdcrPrsnInfKcol.put("FdcrPrsnIdentMatDt","");
			    fdcrPrsnInfKcol.put("FdcrPrsnNm","");
			    fdcrPrsnInfKcol.put("FdcrRsn","");
			    fdcrPrsnInfKcol.put("FdcrPrsnChkRsltFlg","");
			    fdcrPrsnInfKcol.put("FdcrPrsnNtntyTp","");
			    fdcrPrsnInfKcol.put("FdcrPrsnTelNo","");
			    fdcrPrsnInfKcol.put("FdcrPrsnGndTp","");
			    fdcrPrsnInfKcol.put("FdcrPrsnOcpKnd","");
				fdcrPrsnInfKcol.put("FdcrPrsnAdr","");
				fdcrPrsnInfKcol.put("FdcrPrsnRltnpTp","");
				
				String fdcrPrsnInfArry = kCollToJson(fdcrPrsnInfKcol).toString();	 
				fdcrPrsnInfArry = "["+fdcrPrsnInfArry+"]";
				
				JSONArray fdcrPrsnInfJsonArry= JSONArray.fromObject(fdcrPrsnInfArry);
				
				IndexedCollection lclAuthRsltInfIColl = new IndexedCollection();
				String localHead  = "";
				if(headColl.containsKey("LclAuthRsltInfArry")){
					lclAuthRsltInfIColl = (IndexedCollection) headColl.getDataElement("LclAuthRsltInfArry");
					if(lclAuthRsltInfIColl.size()>0){
					     localHead = "\"LOCAL_HEAD\": {"; 
					    for(int k = 0 ; lclAuthRsltInfIColl.size()>k ; k++){
						    KeyedCollection lclAuthRsltInfArryKcoll = new KeyedCollection();
						    lclAuthRsltInfArryKcoll = (KeyedCollection) lclAuthRsltInfIColl.get(k);
						    localHead +=
					    		    "\"LclAuthRsltInfArry\": [{"+
					    			    "\"AuthRetCd\": \""+lclAuthRsltInfArryKcoll.getDataValue("AuthRetCd")+"\","+
					    			    "\"AuthRetInf\":\""+lclAuthRsltInfArryKcoll.getDataValue("AuthRetInf")+"\""+
					    			    "}]";
						    if (k != lclAuthRsltInfIColl.size() - 1){
						    	localHead += ",";
						    }
						    
					    }
					    localHead+= "},";
					}
				}
				 
				 
			    
			    
				pubSysHead=kCollToJson(sysHead).toString();	
			  
				JSONObject jsonobj = JSONObject.fromObject(pubSysHead);
				jsonobj.put("FdcrPrsnInfArry", fdcrPrsnInfJsonArry);
				pubSysHead = jsonobj.toString();
				
			    pubAppHead=kCollToJson(appHead).toString();	 
			    
			    
			    
			    
			    pubReqBody=kCollToJson(bodyColl).toString();
			    reqStr.append("{\"SYS_HEAD\":").append(pubSysHead).append(",\"APP_HEAD\":");
			    reqStr.append(pubAppHead).append(",").append(localHead).append("\"BODY\":").append(pubReqBody).append("}");
			}else{
				sysHead.put("SvcCd",  headColl.getDataValue("SvcCd"));//服务代码
				sysHead.put("ScnCd", headColl.getDataValue("ScnCd"));//场景代码
			 
				sysHead.put("CnsmrCnlTp",  "020114");// 消费方渠道类型
				sysHead.put("CnsmrSysId","300400");//消费方系统编号
				sysHead.put("CnsmrSysSrlNo",  seqNo);// 消费方系统流水号
				sysHead.put("SrcCnsmrSysId", "300400");//源消费方系统编号
				sysHead.put("CnsmrSrvInd", "");//消费方服务器标识
				sysHead.put("GlblSeqNo", glSeqNo);//全局流水号
				sysHead.put("TxnDt", tranDate);//交易日期
				sysHead.put("TxnTm", tranTime);//交易日期
				sysHead.put("FileFlg","0");//文件标志
				sysHead.put("FileNm", "");//文件名称
				sysHead.put("SvcVerNo","");//服务版本号
				sysHead.put("TmlNo", "");//终端号
				sysHead.put("MACVal", "");//MAC值
				 
				
				appHead.put("BrCd","9999");//机构代码必输
				appHead.put("TlrNo","0000"); //柜员号必输
				
				 if(headColl.containsKey("TurnPgFlg")){//加了翻页
			    	 appHead.put("TurnPgFlg","1");//翻页标志
					 appHead.put("PgDsplLineNum",headColl.getDataValue("PgDsplLineNum"));//每页显示条数
					 appHead.put("CrnPgRcrdNo",headColl.getDataValue("CrnPgRcrdNo"));//当前页记录号
			    }else{
			    	appHead.put("TurnPgFlg","");//翻页标志
					 appHead.put("PgDsplLineNum","");//每页显示条数
					 appHead.put("CrnPgRcrdNo","");//当前页记录号
			    }
				appHead.put("SmzgFlg","");//汇总标志
			
				pubSysHead=kCollToJson(sysHead).toString();	
				pubAppHead=kCollToJson(appHead).toString();	 
				pubReqBody=kCollToJson(bodyColl).toString();
				reqStr.append("{\"SYS_HEAD\":").append(pubSysHead).append(",\"APP_HEAD\":");
				reqStr.append(pubAppHead).append(",\"BODY\":").append(pubReqBody).append("}");
			}
			
			esbTradeInfo = addEsbTradeInfo(sysHead,reqStr.toString(),connection);
			
			sb.append("\n**********************ESB请求报文开始************************\n");
			sb.append(reqStr);//记录请求报文
			sb.append("\n**********************ESB请求报文结束************************\n");
			Object service = context.getService("YM_HTTP_ESB");
			if(service instanceof HttpResource){//TCPIP渠道服务
				HttpResource httpResource = (HttpResource)service;
				reMsg = httpResource.sendAndWait(reqStr.toString(), getTradeTimeout());
			}
			
			sb.append("\n**********************ESB响应报文开始************************\n");
			sb.append(reMsg);//响应报文
			sb.append("\n**********************ESB响应报文结束************************\n");
			 
			//开始解析json报文
			
			
			retKColl = jsonToKcoll(reMsg) ;
			
			
			
 			if(retKColl != null){/**更新交易流水表信息**/
 				long time2 = System.currentTimeMillis();
 				String pvdrSysId = "";  
 				if(retKColl.containsKey("PvdrSysId")) {
 					pvdrSysId= (String)retKColl.getDataValue("PvdrSysId"); 
 				}
				
				esbTradeInfo.put("times",new BigDecimal(time2 - time));//计算交易耗时，单位ms 
				esbTradeInfo.put("response_report", reMsg);
				esbTradeInfo.put("source_sys_id", pvdrSysId);
				if(haveSuccess(retKColl)){//成功
					esbTradeInfo.put("suss_flag",YES);
				}else{//交易失败 
					esbTradeInfo.put("suss_flag",NO);
				}
				try{
					updateEsbTradeInfo(esbTradeInfo, connection);//保存响应报文会报字段长度问题***********
				}catch(Exception e){
					EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++更新交易日志失败："+e.getMessage()+"=======+++++++++++\n");
				}
				connection.commit();
			}else{
				EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++交易流水对象esbTradeInfo不存在！+++++++++++\n");
			}
		} catch (Exception e) { 
			
			if(esbTradeInfo != null){/**更新交易流水表信息**/
 				long time2 = System.currentTimeMillis();
 				String pvdrSysId = "";  
 				if(retKColl!=null&&retKColl.containsKey("PvdrSysId")) {
 					pvdrSysId= (String)retKColl.getDataValue("PvdrSysId"); 
 				}
				
				esbTradeInfo.put("times",new BigDecimal(time2 - time));//计算交易耗时，单位ms 
				esbTradeInfo.put("response_report", reMsg);
				esbTradeInfo.put("source_sys_id", pvdrSysId);
				if(haveSuccess(retKColl)){//成功
					esbTradeInfo.put("suss_flag",YES);
				}else{//交易失败 
					esbTradeInfo.put("suss_flag",NO);
				}
				try{
					updateEsbTradeInfo(esbTradeInfo, connection);//保存响应报文会报字段长度问题***********
				}catch(Exception ee){
					EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++更新交易日志失败："+e.getMessage()+"=======+++++++++++\n");
				}
				connection.commit();
			}else{
				EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++交易流水对象esbTradeInfo不存在！+++++++++++\n");
			}
			throw e;
		}finally {
			ConnectionManager.releaseConnection(dataSource, connection);
			try {
				connection.close();
			} catch (Exception e2) {
			}
		}
		sb.append("<=================信贷消费方接口交易END=================>"+"\n");
		EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
		return retKColl;
	}


	public static String jsonToXml(String json){
	    try {
	        StringBuffer buffer = new StringBuffer();
	        buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        JSONObject jso = JSONObject.fromObject(json);
	        jsonToXmlstr(jso,buffer);
	        return buffer.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "";
	    }
	}
	/**
	 * json直接转Kcoll
	 * <p>Title: jsonToKcoll</p>  
	 * <p>Description: </p>  
	 * @param json
	 * @return
	 */
	public static KeyedCollection jsonToKcoll(String json){
		KeyedCollection kColl = new KeyedCollection();
	    try {
	        JSONObject jso = JSONObject.fromObject(json);
	        jsonToKcollSub(jso,kColl);
	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }
	    return kColl;
	}
	@SuppressWarnings("unchecked")
	public static String jsonToXmlstr(JSONObject jObj,StringBuffer buffer ){
	    Set<Entry<String, Object>>  se = jObj.entrySet(); 
	    for( Iterator<Entry<String, Object>>   it = se.iterator();  it.hasNext(); )
	  {             
	      Entry<String, Object> en = it.next();
	      
	     Object a =  en.getValue();
	     String b =  en.getValue().getClass().getName();
	      if(en.getValue().getClass().getName().equals("net.sf.json.JSONObject")){
	          buffer.append("<"+en.getKey()+">");
	              JSONObject jo = jObj.getJSONObject(en.getKey());
	              jsonToXmlstr(jo,buffer);
	          buffer.append("</"+en.getKey()+">");
	      }else if(en.getValue().getClass().getName().equals("net.sf.json.JSONArray")){
	          JSONArray jarray = jObj.getJSONArray(en.getKey());
	          for (int i = 0; i < jarray.size(); i++) {
	              buffer.append("<"+en.getKey()+">");
	                  JSONObject jsonobject =  jarray.getJSONObject(i);
	                  jsonToXmlstr(jsonobject,buffer);
	              buffer.append("</"+en.getKey()+">");    
	          }
	      }else if(en.getValue().getClass().getName().equals("java.lang.String")){
	          buffer.append("<"+en.getKey()+">"+en.getValue());
	          buffer.append("</"+en.getKey()+">");
	      }
	      
	  }
	    return buffer.toString();
	}
	
	/**
	 * json直接转kcoll
	 * <p>Title: jsonToKcoll</p>  
	 * <p>Description: </p>  
	 * @param jObj
	 * @param kColl
	 * @return
	 * @throws InvalidArgumentException 
	 * @throws DuplicatedDataNameException 
	 */
	@SuppressWarnings("unchecked")
	public static KeyedCollection jsonToKcollSub(JSONObject jObj,KeyedCollection kColl) throws InvalidArgumentException, DuplicatedDataNameException {
	    Set<Entry<String, Object>>  se = jObj.entrySet(); 
	    for( Iterator<Entry<String, Object>>   it = se.iterator();  it.hasNext(); )
	  {             
	      Entry<String, Object> en = it.next();
	      
	     Object a =  en.getValue();
	     String b =  en.getValue().getClass().getName();
	      if(en.getValue().getClass().getName().equals("net.sf.json.JSONObject")){ 
	          JSONObject jo = jObj.getJSONObject(en.getKey());
	          KeyedCollection subKcoll =  new KeyedCollection(); 	
	          subKcoll.setId(en.getKey());
	           kColl.addKeyedCollection(jsonToKcollSub(jo,subKcoll));
	      }else if(en.getValue().getClass().getName().equals("net.sf.json.JSONArray")){
	          JSONArray jarray = jObj.getJSONArray(en.getKey());
            IndexedCollection iColl = new IndexedCollection();
	          iColl.setId(en.getKey());
	          for (int i = 0; i < jarray.size(); i++) { 
	              JSONObject jsonobject =  jarray.getJSONObject(i);
                  KeyedCollection subKcoll =  new KeyedCollection();
	              iColl.add(jsonToKcollSub(jsonobject,subKcoll));  
	          }
	        kColl.addIndexedCollection(iColl);
	      }else if(en.getValue().getClass().getName().equals("java.lang.String")){
	        
	          kColl.put(en.getKey(), en.getValue());
	      }
	      
	  }
	    return kColl;
	}
	
	public static KeyedCollection getReq4KColl_Part1(String XMLStr) throws Exception {
		EMPLog.log("XMLstr:", EMPLog.ERROR, 0, XMLStr);
		String name = "msbody";
		KeyedCollection k = new KeyedCollection(name);
		try {
			String dataStr_syshead = ESBConstance.SYS_HEAD_START+ESBUtil.subXMLString(XMLStr,ESBConstance.SYS_SERVICE_HEAD)+ESBConstance.SYS_HEAD_END;

			String dataStr_apphead = ESBConstance.APP_HEAD_START+ESBUtil.subXMLString(XMLStr,ESBConstance.APP_SERVICE_HEAD)+ESBConstance.APP_HEAD_END;
			StringBuffer dataStrBuf = new StringBuffer();
			dataStrBuf = dataStrBuf.append(ESBConstance.LEFT_C).append("BODY").append(ESBConstance.RIGHT_C);
			dataStrBuf.append(dataStr_syshead);
			dataStrBuf.append(dataStr_apphead);
			dataStrBuf.append(ESBConstance.LEFT_END_STR).append("BODY").append(ESBConstance.RIGHT_C);
			
			
			KeyedCollection sysHead = getReq4KColl_Part2("SYS_HEAD",dataStrBuf.toString());
			KeyedCollection appHead = getReq4KColl_Part2("APP_HEAD",dataStrBuf.toString());
			KeyedCollection body = new KeyedCollection("");
			
			if(ESBUtil.subXMLString(XMLStr,ESBConstance.SERVICE_BODY)!=null && !"".equals(ESBUtil.subXMLString(XMLStr,ESBConstance.SERVICE_BODY))){
				String dataStr_body = ESBConstance.BODY_START+ESBUtil.subXMLString(XMLStr,ESBConstance.SERVICE_BODY)+ESBConstance.BODY_END;
				StringBuffer dataStrBuf2 = new StringBuffer();
				dataStrBuf2 = dataStrBuf2.append(ESBConstance.LEFT_C).append("BODY").append(ESBConstance.RIGHT_C);
				dataStrBuf2.append(dataStr_body);
				dataStrBuf2.append(ESBConstance.LEFT_END_STR).append("BODY").append(ESBConstance.RIGHT_C);
				body = getReq4KColl_Part2("BODY",dataStrBuf2.toString());
			}
			
			 k.addDataElement(sysHead);
			 k.addDataElement(appHead);
			 k.addDataElement(body);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		EMPLog.log("XMLstr:", EMPLog.INFO, 0, k.toString());
		return k;
	}
	
	public static KeyedCollection getReq4KColl_Part2(String name, String XMLStr) throws Exception {
		EMPLog.log("XMLstr:", EMPLog.ERROR, 0, XMLStr);

		KeyedCollection k = new KeyedCollection(name);
		if (XMLStr == null || "".equals(XMLStr)) {
			return null;
		}
		/** 创建一个新的字符串 **/
		StringReader xmlString = new StringReader(XMLStr);
		/** 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入 **/
		InputSource source = new InputSource(xmlString);
		/** 创建一个新的SAXBuilder **/
		SAXBuilder saxb = new SAXBuilder();

		IndexedCollection result = null;
		try {
			result = new IndexedCollection(name);
			/** 通过输入源构造一个Document **/
			org.jdom.Document doc = saxb.build(source);
			/** 取的根元素 **/
			Element root = doc.getRootElement();
			/** 得到根元素下指定名字的数组集合 **/
			List<Element> node = queryIcol(root, name);
			/** 将Element集合转换成IndexedCollection */
			result = getIcol(node, name);
			if (result != null && result.size() > 0) {
				k = (KeyedCollection) result.get(0);
				k.setName(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		EMPLog.log("XMLstr:", EMPLog.INFO, 0, k.toString());
		return k;
	}
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>AFE通讯交易中,kColl合并</ol>
	 * </p>
	 * @param kCollFor 
	 * @param kCollTo
	 * @param serviceCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static KeyedCollection KCollMerge(KeyedCollection kCollFor, KeyedCollection kCollTo)throws Exception{
		if (kCollFor != null) {
			for (Iterator iterator = kCollFor.keySet().iterator(); iterator.hasNext(); ) {
				Object obj = iterator.next();
			    if ((obj instanceof String)) {
			        String key = (String)obj;
			        Object value = kCollFor.getDataValue(key);
			        kCollTo.put(key, value);
			    }
			}
		}
		return kCollTo;
	}
	
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>将Element的集合转换成IndexedCollection</ol>
	 * <h2>功能描述</h2>
	 * 		<ol></ol>
	 * </p>
	 * @author xumingtai
	 * @param node  Element集合
	 * @param name	返回数组标签的名字
	 * @return
	 */
	private static IndexedCollection getIcol(List<Element> node, String name) {
		IndexedCollection result = new IndexedCollection(name);
		Element element = null;
		/** 循环获取集合标签下的所有子标签并转换为DataElement */
		for (int i = 0; i < node.size(); i++) {
			element = (Element) node.get(i);
			DataElement io = getChiled(element, "");
			result.addDataElement(io);
		}
		return result;
	}
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>递归查询指定标签名的数组集合>
	 * <h2>功能描述</h2>
	 * 		<ol>请添加功能详细的描述</ol>
	 * </p>
	 * @author xumingtai
	 * @param node		查询的标签元素 
	 * @param iName		指定获取的标签数组名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<Element> queryIcol(Element node, String iName) {
		List<Element> result = new ArrayList<Element>();
		try {
			/** 尝试在该标签下获取指定标签数组，如果获取到直接返回 */
			List<Element> list = node.getChildren(iName);
			if (list.size() > 0) {
				return list;
			}
			/** 递归获取所有子标签查询 */
			list = node.getChildren();
			Element element = null;
			for (int i = 0; i < list.size(); i++) {
				element = (Element) list.get(i);
				List<Element> io = queryIcol(element, iName);
				if (io != null && io.size() > 0) {
					result.addAll(io);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(ESBUtil.class.getName(), 0, EMPLog.ERROR, "ESB执行交易出错" + e.getMessage());
		}
		return result;
	}
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>获取Element的所有子标签并转换成DataElement</ol>
	 * <h2>功能描述</h2>
	 * 		<ol></ol>
	 * </p>
	 * @author xumingtai
	 * @param node	需要转换的Element
	 * @param name 指定返回DataElement的名字，可传空字符串
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static DataElement getChiled(Element node, String name) {
		KeyedCollection result = new KeyedCollection(name);
		try {
			Element element = null;
			/** 如果该标签没有子标签了就返回DataField */
			List<Element> list = node.getChildren();
			if (list.size() == 0) {
				DataField data = new DataField();
				data.setValue(node.getText());
				return data;
			}
			/** 记录数组标签名集合 */
			List<String> ioName = new ArrayList<String>();
			/** 存在子标签则递归获取子标签 */
			for (int i = 0; i < list.size(); i++) {
				/** 判断该标签是否已经以数组形式被获取过 */
				element = (Element) list.get(i);
				String eName = element.getName();
				if (ioName.contains(eName)) {
					continue;
				}
				/** 尝试获取数组，如果存在1个以上的同名标签则以iColl形式返回 **/
				List<Element> childers = node.getChildren(eName);
				if (childers != null && childers.size() > 1) {
					ioName.add(eName);
					result.addDataElement(getIcol(childers, eName));
					continue;
				}
				childers = element.getChildren();
				String text = element.getText();
				result.addDataField(eName, text);
					
			}
		} catch (Exception e) {
			e.printStackTrace();
			EMPLog.log(ESBUtil.class.getName(), 0, EMPLog.ERROR, "ESB执行交易出错" + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	 * <p>
	 * <h2>获取当前自然日时间</h2>
	 * 		<ol>获取年月日时间，格式为：yyyy-MM-dd HH：mm：ss,示例：2015-08-17 16:15:10</ol>
	 * </p>
	 * @return
	 */
	public static String getCurrentTime(){
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str = sdf.format(new Date());
		return str;
	}
	
	/**
	 * 
	 * <p>
	 * <h2>更新交易流水表信息</h2>
	 * 		<ol>更新交易流水表的交易成功标志和耗时情况</ol>
	 * </p>
	 * @param esbTradeInfo
	 * @param connection
	 * @throws Exception
	 */
	public static void updateEsbTradeInfo(KeyedCollection esbTradeInfo,
			Connection connection) throws Exception {
		if(connection == null){
			return;
		}
		try{
//			EsbTradeInfoComponent comp = (EsbTradeInfoComponent) CMISFactory
//					.getComponent(EsbTradeInfoConstance.ESB_TRADE_INFO_ID);
//			boolean rs = comp.updateEsbTradeInfo(esbTradeInfo, connection);
			
			int count = SqlClient.updateAuto("updateESBTradeInfo", esbTradeInfo, new String[]{"tx_date","msg_id"}, connection);
			
			if(count!=1){
				EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++更新交易日志失败+++++++++++\n");
			}
		}catch(Exception e){
			EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++更新交易流水表信息失败！+++++++++++\n");
//			throw new Exception("更新交易流水表信息失败！");
		}
	}
	
	/**
	 * 
	 * <p>
	 * <h2>新增交易流水表信息</h2>
	 * 		<ol>新增交易流水表信息，把交易报文存储到数据库中</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>请添加功能详细的描述</ol>
	 * </p>
	 * @param dataStr
	 * @param connection
	 * @throws Exception
	 */
	public static KeyedCollection addEsbTradeInfo(KeyedCollection headColl,String reqstr, Connection connection) throws Exception {
		 
		KeyedCollection esbTradeInfo = new KeyedCollection();
		String txDate = getCurrentTime();
		String msgId = (String)headColl.getDataValue("GlblSeqNo");
		
		
		String sourceSysId = ""; 
		if(headColl.containsKey("PvdrSysId")) {
			sourceSysId= (String)headColl.getDataValue("PvdrSysId"); 
		}
		  
		String consumerId = "";
		if(headColl.containsKey("CnsmrSysId")) {
			consumerId= (String)headColl.getDataValue("CnsmrSysId"); 
		}
		   
		String serviceCode = (String)headColl.getDataValue("SvcCd"); 
		String serviceScene = (String)headColl.getDataValue("ScnCd"); 
		
		
		
		esbTradeInfo.put("msg_id",msgId);//交易流水号
		esbTradeInfo.put("tx_date",txDate);//交易日期
		esbTradeInfo.put("source_sys_id",sourceSysId);// 服务方系统id
		esbTradeInfo.put("time_stamp", getRequestTime());//时间戳
		esbTradeInfo.put("consumer_id",consumerId);//消费方系统id
		esbTradeInfo.put("service_code",serviceCode);//原交易日期
		esbTradeInfo.put("service_scene",serviceScene);//原交易流水号
		esbTradeInfo.put("request_report",reqstr);//交易报文
		
		 
		SqlClient.insertAuto("insertESBTradeInfo", esbTradeInfo, connection);
		connection.commit();
		return esbTradeInfo;
	}

	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>获取当前时间戳，格式：201508171615101</ol>
	 * </p>
	 * @return
	 */
	public static String getRequestTime(){
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		str = sdf.format(new Date());
		return str;
	}
	
	
	/**
	 * 解析报文反馈信息是否正确，true表示交易成功，false表示失败
	 * @param returnKColl  反馈信息
	 * @param context 上下文
	 * @return boolean
	 * @throws EMPException
	 */
	public static boolean haveSuccess(KeyedCollection returnKColl) throws EMPException{
		boolean flag  = true;
		if(returnKColl != null && returnKColl.size() > 0){
			KeyedCollection sysHeadKcoll = (KeyedCollection)returnKColl.getDataElement("SYS_HEAD");
			IndexedCollection retIColl = (IndexedCollection)sysHeadKcoll.getDataElement("RetInfArry");
			if(retIColl!=null&&retIColl.size()>0){
				for(int i=0;i<retIColl.size();i++){
					String retCode = (String)((KeyedCollection)retIColl.get(i)).getDataValue("RetCd");
					if(!retCode.equals(TradeConstance.RETCODE1)){
						flag = false;
					}	
				}
				
			}
			
		}else {
			flag = false;
		}
		return flag;
	}
}
