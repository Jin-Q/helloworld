package com.yucheng.cmis.platform.workflow.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiBizConfigVO;
import com.yucheng.cmis.platform.workflow.exception.WFIException;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.CommonUtil;

/**
 * <p>流程引擎接入工具类</p>
 * @author liuhw
 *
 */
public class WorkFlowUtil {
	
	/**
	 * 读取cmis-wfi-biz.xml配置文件，获取流程审批业务处理接口
	 * @param applType 申请类型
	 * @throws EMPException 
	 */
	private static Document doc;
	public static WfiBizConfigVO getBizInterfaceId(String applType) throws EMPException {
//		Document doc = null;
		String path = CommonUtil.getClassesPath()+WorkFlowConstance.WFI_BIZ_XML;
		System.out.println(path);
		InputStream is =null;
		try {
			if(doc==null){
				is = new FileInputStream(path);
				SAXReader saxReader = new SAXReader();
				doc = saxReader.read(is);
			}
			Node note = null;
			note = doc.selectSingleNode("/CMIS/wfiBiz/biz[@applType=\""+applType+"\"]");
			WfiBizConfigVO configVO = new WfiBizConfigVO();
			//在xml文件配置没有获取到，返回默认的空白接口信息
			if(note==null){
				configVO.setBizInterfaceId(WorkFlowConstance.WFI_BIZIF_BLANK);
				configVO.setApplType(applType);
				configVO.setDesc("没有配置接口");
			} else {
				DefaultElement de = (DefaultElement) note;
				configVO.setBizInterfaceId(de.attributeValue("bizInterfaceId"));
				configVO.setApplType(de.attributeValue("applType"));
				configVO.setDesc(de.attributeValue("desc"));
			}
			return configVO;	
		} catch (Exception e) {
			EMPLog.log("WorkFlowUtil", EMPLog.ERROR, EMPLog.ERROR, "读取"+WorkFlowConstance.WFI_BIZ_XML+"文件，获取流程审批业务处理接口[parserCMISWfiBiz]出错。异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally{
			try {
				if(is!=null)
					is.close();
			} catch(IOException e) {
				EMPLog.log("WorkFlowUtil", EMPLog.ERROR, EMPLog.ERROR, "读取"+WorkFlowConstance.WFI_BIZ_XML+"文件，获取流程审批业务处理接口[parserCMISWfiBiz]出错。异常信息："+e.getMessage(), e);
				throw new EMPException(e);
			}
		}
	}
	
	/**
	 * <p>返回中文名称的全拼音及拼音首字母</p>
	 * @param orgiChinese 中文名称
	 * @return 如传入王五，返回['wangwu','ww']
	 */
	public static String[] toChinese(String orgiChinese){
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		if(orgiChinese==null)
			orgiChinese="";
		char[] orgiChar = orgiChinese.toCharArray();
		String[] ret = new String[2];
		for(int i=0;i<orgiChar.length;i++){
			String[] newStr = PinyinHelper.toHanyuPinyinStringArray(orgiChar[i]);
			if(newStr!=null){
				sb.append(newStr[0].substring(0,newStr[0].length()-1));
				sb2.append(newStr[0].substring(0,1));
			}
		}
		ret[0] = sb.toString();
		ret[1] = sb2.toString();
		return ret;
	}
	
	/**
	 * 解析URL中的参数。url格式为one.do?serno=${pk_value}&flag=y，即将需要解析的参数以${paramname}标识
	 * @param url 需要处理的URL
	 * @param paramMap 存放url参数可能用到的键值对，键对应参数名，值为参数值
	 * @return 处理后的URL
	 */
	public static String processURLParam(String url, Map<String, String> paramMap) {
		if(url==null || url.trim().equals("") || url.indexOf("${")==-1 || paramMap==null || paramMap.isEmpty()) {
			return url;
		}
		String regexStr = "\\$\\{[\\w]+\\}";
		Pattern pattern = Pattern.compile(regexStr);
		Matcher matcher = pattern.matcher(url);
		while(matcher.find()) {
			String tmp = matcher.group();
			String paramName = tmp.substring(2, tmp.length()-1);
			String paramValue = paramMap.get(paramName);
			if(paramValue != null) {
				url = url.replace(tmp, paramValue);
			}
		}
		return url;
	}
	
	
	/**
	 * 信贷流程引擎服务接入接口
	 */
	private static WorkflowServiceInterface wfi = null;
	static {
		try {
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据传入的流程ID及流程属性key，获取属性名称对应的值
	 * @param wfId 流程ID
	 * @param propertyName 属性名称（忽略大小写）
	 * @return
	 * @throws WFIException
	 */
	public static Object getWFPropertyByWfId(String wfId, String propertyName) throws WFIException {
		try {
			return wfi.getWFPropertyByWfId(wfId, propertyName);
		} catch (WFIException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据传入的流程标识及流程属性key，获取属性名称对应的obj
	 * @param wfSign 流程标识
	 * @param propertyName 属性名称（忽略大小写）
	 * @return
	 * @throws WFIException
	 */
	public static Object getWFPropertyByWfSign(String wfSign, String propertyName) throws WFIException {
		try {
			return wfi.getWFPropertyByWfSign(wfSign, propertyName);
		} catch (WFIException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据传入的流程ID及扩展流程属性名称，获取扩展属性名称对应的值
	 * @param wfId 流程ID
	 * @param propertyName 扩展流程属性名称
	 * @return
	 * @throws WFIException
	 */
	public static String getWFExtPropertyByWfId(String wfId, String propertyName) throws WFIException {
		try {
			return wfi.getWFExtPropertyByWfId(wfId, propertyName);
		} catch (WFIException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据传入的流程标识及流程扩展属性名称，获取属性对应的obj
	 * @param wfsign 流程扩展属性名称
	 * @param propertyName 流程扩展属性名称
	 * @return
	 * @throws WFIException
	 */
	public static String getWFExtPropertyByWfSign(String wfsign, String propertyName) throws WFIException {
		try {
			return wfi.getWFExtPropertyByWfSign(wfsign, propertyName);
		} catch (WFIException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据传入的节点ID及节点属性名称，获取节点属性对应的obj
	 * @param nodeid 节点ID
	 * @param propertyName 节点属性名称
	 * @return
	 * @throws WFIException
	 */
	public static Object getWFNodeProperty(String nodeid, String propertyName) throws WFIException {
		try {
			return wfi.getWFNodeProperty(nodeid, propertyName);
		} catch (WFIException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据传入的节点ID及节点扩展属性名称，获取扩展属性名称对应的obj
	 * @param nodeid 节点ID
	 * @param propertyName 节点扩展属性名称
	 * @return
	 * @throws WFIException
	 */
	public static String getWFNodeExtProperty(String nodeid, String propertyName) throws WFIException {
		try {
			return wfi.getWFNodeExtProperty(nodeid, propertyName);
		} catch (WFIException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 为icoll中指定的用户ID字段添加中文名称（由于流程中存在多个用户ID用;分隔，直接调用组织机构服务不能转换，故提供一个加工过的方法，目前仅供流程使用）
	 * @param icoll 要添加中文名称的icoll
	 * @param fields 指定的字段名
	 * @throws WFIException
	 */
	public static void addUserName4WF(IndexedCollection icoll, String[] fields) throws WFIException {
		
		try {
			OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
			if(icoll!=null && icoll.size()>0 && fields!=null && fields.length>0) {
				for(Object obj : icoll) {
					KeyedCollection kcoll = (KeyedCollection) obj;
					for(String field : fields) {
						try {
							String userIdTmp = (String) kcoll.getDataValue(field);
							String[] userIdArr = userIdTmp.split(";");
							String userName = "";
							for(String uid : userIdArr) {
								userName += orgCacheMsi.getUserName(uid) + ";";
							}
							userName = userName.equals("")?userIdTmp:userName.substring(0, userName.length()-1);
							if("null".equals(userName)&&"signUser;".equals(userIdTmp)){
								kcoll.addDataField(field+"_displayname", "贷审会成员");//xujh  贷审会时已办中办理显示为null
							}else{
								kcoll.addDataField(field+"_displayname", userName);
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							//异常忽略
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
	}
	
	
}
