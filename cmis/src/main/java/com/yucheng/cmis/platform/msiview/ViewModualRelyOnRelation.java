package com.yucheng.cmis.platform.msiview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;

import com.yucheng.cmis.platform.msiview.domain.ClassServiceVO;
import com.yucheng.cmis.platform.msiview.domain.MethodServiceVO;
import com.yucheng.cmis.platform.msiview.domain.ModualServiceVO;

/**
 * 依赖关系查看
 * <p>
 * 	模块间的依赖关系理应在设计划分清理，且这种依赖关系不应该仅仅到模块，应该可以具体到方法粒度。
 * 	在设计时较容易能定义两个模块间的依赖关系，但是很难到方法粒度的依赖关系，而方法粒度的依赖关系
 * 	正是模块可替换性的关键所在。模块间依赖关系扫描是事后起作用，即在模块开发完成后，知道该模块与
 * 	其它模块的依赖关系。
 * <p>
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewModualRelyOnRelation {
	private static ViewModualRelyOnRelation instance = null;
	
	//放模块接口
	private Map<String, String> classObjMap = new HashMap<String, String>();
	//放模块接口方法:key-方法 value:接口
	private Map<String, String> methodObjMap = new HashMap<String, String>();
	
	private ViewModualRelyOnRelation(){}
	
	public static ViewModualRelyOnRelation getInstance(){
		instance = new ViewModualRelyOnRelation();
		instance.init();
		return instance;
	}
	
	public void init(){
		classObjMap.clear();
		methodObjMap.clear();
	}
	
	
	/**
	 * 扫描modualPath目录下的所有JAVA文件得到与其它模块的依赖关系
	 * 
	 * @param projectPath 工程目录
	 * @param modualPath 模块目录
	 * @return Map<String, Map<String, String>> key:模块id key(Map key:方法名，value:方法描述)
	 */
	public List<ModualServiceVO> getModualRelyOnRelation(String projectPath,String modualPath) throws Exception{
		List<ModualServiceVO> retList = new ArrayList<ModualServiceVO>();
		try {
			
			//扫描模块目录
			scanAllClass(modualPath);
			if(methodObjMap.size()==0) return retList;
			
			//得到工程目录projectPath下所有服务与模块中的类与方法区配，得到依赖的模块名与方法及描述
			ViewAllModualServices service = new ViewAllModualServices();
			List<ModualServiceVO> mlist = service.parseMoudalServicesDesc(projectPath,false);
			
			
			for (Iterator it = methodObjMap.keySet().iterator(); it.hasNext();) {
				String method = (String) it.next();//依赖其它模块的方法
				String className = methodObjMap.get(method);//依赖其它模块的接口名
				
				for (int j = 0; j < mlist.size(); j++) {
					ModualServiceVO vo = mlist.get(j);
					String modualId = vo.getModualId();//模块ID
					String modualName = vo.getModualName();//模块名称
					
					List<ClassServiceVO> classList = vo.getClassServiceList();
					for (ClassServiceVO classVO : classList) {
						String classId = classVO.getServiceId();//服务集ID
						String clzzName = classVO.getClassName();//服务集名
						String classDesc = classVO.getServiceDesc();//服务集描述
						
						//如果接口名相同，则找到该接口的方法描述
						if(classVO.getClassName().endsWith(className+".java") || classVO.getClassName().endsWith(className)){
							
							//服务集合：方法
							List<MethodServiceVO> methodList = classVO.getMethodServiceList();
							for (MethodServiceVO methodVO : methodList) {
								//服务匹配
								if(methodVO.getMethod().equals(method)){
									//模块未添加，则需要添加模块、服务集合、服务
									if(!containsModual(retList, modualId)){
										ClassServiceVO tmpClassVO = classVO;
										tmpClassVO.setMethodServiceList(new ArrayList<MethodServiceVO>());
										tmpClassVO.getMethodServiceList().add(methodVO);
										ModualServiceVO tmpModualVO = vo;
										tmpModualVO.setClassServiceList(new ArrayList<ClassServiceVO>());
										tmpModualVO.getClassServiceList().add(tmpClassVO);
										retList.add(tmpModualVO);
									}
									//如果服务集合未添加，则需要添加服务集合和服务
									else if(!this.containsClass(retList, className)){
										ClassServiceVO tmpClassVO = classVO;
										tmpClassVO.setMethodServiceList(new ArrayList<MethodServiceVO>());
										tmpClassVO.getMethodServiceList().add(methodVO);
										this.addClass2List(retList, modualId, tmpClassVO);
									}
									//如果服务集合已添加，但是服务未添加
									else if(!this.containsMethod(retList, method) && this.containsClass(retList, className)){
										this.addMethod2List(retList, className, methodVO);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return retList;
	}
	
	
	
	/**
//	 * 扫描modualPath目录下的所有JAVA文件得到与其它模块的依赖关系
//	 * 
//	 * @param projectPath 工程目录
//	 * @param modualPath 模块目录
//	 * @return Map<String, Map<String, String>> key:模块id key(Map key:方法名，value:方法描述)
//	 */
//	public Map<String, Map<String, String>> getModualRelyOnRelation(String projectPath,String modualPath) throws Exception{
//		Map<String, Map<String, String>> retMap = new HashMap<String, Map<String, String>>();
//		try {
//			
//			//扫描模块目录
//			scanAllClass(modualPath);
//			if(methodObjMap.size()==0) return retMap;
//			
//			//得到工程目录projectPath下所有服务与模块中的类与方法区配，得到依赖的模块名与方法及描述
//			ViewAllModualServices service = new ViewAllModualServices();
//			List<ModualServiceVO> mlist = service.parseMoudalServicesDesc();
//			
//			
//			for (Iterator it = methodObjMap.keySet().iterator(); it.hasNext();) {
//				String method = (String) it.next();//依赖其它模块的方法
//				String className = methodObjMap.get(method);//依赖其它模块的接口名
//				
//				for (int j = 0; j < mlist.size(); j++) {
//					ModualServiceVO vo = mlist.get(j);
//					List<ClassServiceVO> classList = vo.getClassServiceList();
//					for (ClassServiceVO classVO : classList) {
//						//如果接口名相同，则找到该接口的方法描述
//						if(classVO.getClassName().endsWith(className+".java") || classVO.getClassName().endsWith(className)){
//							String modualId = classVO.getModualId();
//							Map<String, String> methodMap = null;
//							
//							if(retMap.containsKey(modualId)){
//								methodMap = retMap.get(modualId);
//							}
//							else{
//								methodMap = new HashMap<String, String>();
//								retMap.put(modualId, methodMap);
//							}
//							
//							List<MethodServiceVO> methodList = classVO.getMethodServiceList();
//							for (MethodServiceVO methodVO : methodList) {
//								if(methodVO.getMethod().startsWith(method))
//									methodMap.put(methodVO.getMethod(), methodVO.getDesc());
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		return retMap;
//	}
	
	
	/**
	 * 扫描某一目录，得到该目录下所有JAVA文件依赖哪些模块
	 * 
	 * @param path 目录，一般为模块目录
	 */
	public void scanAllClass(String path) throws Exception{
		try {
			File file = new File(path);
			if(!file.isDirectory()) return ;
			File[] files = file.listFiles();
			
			for (int i = 0; i < files.length; i++) {
				File tmpFile = files[i];
				if(tmpFile.isDirectory())
					scanAllClass(tmpFile.getPath());
				
				//只扫描JAVA文件
				if(!tmpFile.getPath().endsWith(".java"))
					continue;
				
				scanSingleClass(tmpFile.getPath());
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * 扫描某个JAVA文件，得到该JAVA依赖其它哪些模块
	 * 
	 * @param filePath JAVA文件路径
	 * @return 该JAVA文件依赖的模块key：方法，value：接口类
	 * @throws Exception
	 */
	private void scanSingleClass(String filePath) throws Exception{
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(new File(filePath)));
			String line = null;
			
			while((line=in.readLine())!=null){
				isMatch(line);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally{
			in.close();
		}
	}
	
	/**
	 * 配置是否满足模块间服务接口调用
	 * <p>
	 * 	<ul>匹配样例代码1：
	 * 		<ul>CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();</ul>
	 *		<ul>OrganizationServiceInterface orgService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "permission", "organization");</ul>
	 *		<ul>List<SUser> userList = orgService.getOrgAndSubOrgUsers(orgNo, connection);</ul>
	 *  </ul>
	 *  <ul>匹配样例代码2：
	 *  	<ul>OrganizationServiceInterface orgService = (OrganizationServiceInterface)CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "permission", "organization");</ul>
	 *		<ul>List<SUser> userList = orgService.getOrgAndSubOrgUsers(orgNo, connection);</ul>
	 *  </ul>
	 * </p>
	 * @param line 等匹配的字符串
	 */
	private void isMatch(String line){
		//去除前后空格
		line = line.trim();
		
		//匹配OrganizationServiceInterface orgService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("orga..
		String classPattern = "(\\w+)\\s+(\\w+)\\s+=\\s+(\\(\\w+\\))\\w+\\.getModualServiceById\\(.*\\);";
		Pattern p = Pattern.compile(classPattern);
		Matcher m = p.matcher(line);
		
		if(m.find()){
			//匹配OrganizationServiceInterface orgService = (OrganizationServiceInterface)serviceJndi.getModualServiceById(
			if(m.groupCount()==3){
				//key是其它模块的接口类型，value是该接口类型实例的变量
				classObjMap.put(m.group(1), m.group(2));
			}
		}else{
			//匹配OrganizationServiceInterface orgService = (OrganizationServiceInterface)CMISModualServiceFactory.getInstance().getModualServiceById("orga
			classPattern = "(\\w+)\\s+(\\w+)\\s+=\\s+(\\(\\w+\\))CMISModualServiceFactory.getInstance\\(\\)\\.getModualServiceById\\(.*\\);";
			p = Pattern.compile(classPattern);
			m = p.matcher(line);
			if(m.find()){
				//匹配OrganizationServiceInterface orgService = (OrganizationServiceInterface)serviceJndi.getModualServiceById(
				if(m.groupCount()==3){
					//key是其它模块的接口类型，value是该接口类型实例的变量
					classObjMap.put(m.group(1), m.group(2));
				}
			}
		}
		
		//匹配List<SUser> userList = orgService.getOrgAndSubOrgUsers(orgNo, connection) 也有可能是无返回值的方法
		//根据classObjMap中的value来找到调用的方法
		for (Iterator it = classObjMap.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = classObjMap.get(key);
			String methodPattern = ".*"+value+"\\.(\\w+)\\(.*\\);";
			p = Pattern.compile(methodPattern);
			m = p.matcher(line);
			
			if(m.find()){
				//匹配OrganizationServiceInterface orgService = (OrganizationServiceInterface)serviceJndi.getModualServiceById(
				if(m.groupCount()==1){
					//key是接口方法，value是接口
					methodObjMap.put(m.group(1), key);
				}
			}
		}
	}
	
	
	
	/**
	 * 扫描modualPath目录下的所有JAVA文件得到与其它模块的依赖关系,并以XML的形式返回
	 * 
	 * @param projectPath 工程目录
	 * @param modualPath 模块目录
	 * @return JDOM的Document
	 */
	public String getModualRelyOnRelationWithXML(String projectPath, String modualPath) throws Exception{
		String retXml = null;
		//得到依赖关系
		List<ModualServiceVO> list = this.getModualRelyOnRelation(projectPath,modualPath);
		
		//写XML文件
		Element root = new Element("modual_rel");
		Document doc = new Document(root);
		doc.setRootElement(root);
		
		for (ModualServiceVO mvo : list) {
			//模块信息
			Element modualEle = new Element("modual");
			modualEle.setAttribute("id", mvo.getModualId());
			modualEle.setAttribute("name", mvo.getModualName());
			
			root.addContent(modualEle);
			
			//依赖的服务集合
			List<ClassServiceVO> clasList = mvo.getClassServiceList();
			for (ClassServiceVO cvo : clasList) {
				Element classEle = new Element("services");
				classEle.setAttribute("id",cvo.getServiceId());
				classEle.setAttribute("name", cvo.getClassName());
				classEle.setAttribute("desc", cvo.getServiceDesc());
				
				modualEle.addContent(classEle);
				
				//依赖的服务
				List<MethodServiceVO> methodList = cvo.getMethodServiceList();
				for (MethodServiceVO methodVO : methodList) {
					Element methodEle = new Element("service");
					methodEle.setAttribute("name",methodVO.getMethod());
					methodEle.setAttribute("desc", methodVO.getDesc());
//					methodEle.setAttribute("inParam", methodVO.getInParam().toString());
//					methodEle.setAttribute("outParam", methodVO.getOutParam().toString());
					
					classEle.addContent(methodEle);
				}
			}
		}
		
		//XMLOutputter out = new XMLOutputter("", true, "UTF-8");
		//return out.outputString(doc);
		return null;
	}
	
	/**
	 * 向List<ModualServiceVO>中的服务集合className下添加一个服务
	 * @param src 源
	 * @param className 服务集合，接口名
	 * @param methodVO 服务
	 * @return List<ModualServiceVO>
	 */
	private List<ModualServiceVO> addMethod2List(List<ModualServiceVO> src, String className, MethodServiceVO methodVO){
		for (ModualServiceVO mvo : src) {
			List<ClassServiceVO> classList = mvo.getClassServiceList();
			for (ClassServiceVO cvo : classList) {
				if(cvo.getClassName().equals(className)) {
					cvo.getMethodServiceList().add(methodVO);
				}
			}
		}
		
		return src;
	}
	
	/**
	 * 向List<ModualServiceVO>中的模块下添加一个服务集合
	 * @param src 源
	 * @param className 服务集合，接口名
	 * @param methodVO 服务
	 * @return List<ModualServiceVO>
	 */
	private List<ModualServiceVO> addClass2List(List<ModualServiceVO> src, String modualId, ClassServiceVO classVO){
		for (ModualServiceVO mvo : src) {
			List<ClassServiceVO> classList = mvo.getClassServiceList();
			if(mvo.getModualId().equals(modualId)){
				mvo.getClassServiceList().add(classVO);
			}
		}
		
		return src;
	}
	
	

	/**
	 * 判断List<ModualServiceVO>是否含有modual
	 * @param src 源
	 * @param className 是否包含内容
	 * @return true-包含；false-不包含
	 */
	private boolean containsModual(List<ModualServiceVO> src, String modualId){
		for (ModualServiceVO mvo : src) {
			if(mvo.getModualId().equals(modualId))
				return true;
		}
		
		return false;
	}
	
	/**
	 * 判断List<ModualServiceVO>是否含有className
	 * @param src 源
	 * @param className 是否包含内容
	 * @return true-包含；false-不包含
	 */
	private boolean containsClass(List<ModualServiceVO> src, String className){
		for (ModualServiceVO mvo : src) {
			List<ClassServiceVO> classList = mvo.getClassServiceList();
			for (ClassServiceVO cvo : classList) {
				if(cvo.getClassName().equals(className)) return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 判断List<ClassServiceVO>是否含有methodName
	 * @param src 源
	 * @param methodName 是否包含内容
	 * @return true-包含；false-不包含
	 */
	private boolean containsMethod(List<ModualServiceVO> src, String methodName){
		for (ModualServiceVO mvo : src) {
			List<ClassServiceVO> classList = mvo.getClassServiceList();
			for (ClassServiceVO cvo : classList) {
				List<MethodServiceVO> methodList = cvo.getMethodServiceList();
				for (MethodServiceVO methodVO : methodList) {
					if(methodVO.getMethod().equals(methodName))
						return true;
				}
			}
		}
		
		return false;
	}
	
	
	public static void main(String[] args) throws Exception {
		String str = "E:/eclipse_workspace/CMIS5/cmis/src/main/java/com/yucheng/cmis/platform/permission";
		
		ViewAllModualServices msi = new ViewAllModualServices();
		ViewModualRelyOnRelation obj = new ViewModualRelyOnRelation();
		List<ModualServiceVO> mlist = obj.getModualRelyOnRelation("E:/eclipse_workspace/CMIS5/cmis/src/main/java/com/yucheng/",str);
		
		
		for (int j = 0; j < mlist.size(); j++) {
			ModualServiceVO vo = mlist.get(j);
			
			System.out.println("************************************************************");
			
			System.out.println("*         模块:"+vo.getModualName()+"描述信息                ");
			System.out.println("*模块ID："+vo.getModualId());
			System.out.println("*模块名称："+vo.getModualName());
			System.out.println("*");
			
			List<ClassServiceVO> classList = vo.getClassServiceList();
			for (ClassServiceVO classVO : classList) {
				System.out.println("*         服务接口信息                                     ");
				System.out.println("*服务ID："+classVO.getServiceId());
				System.out.println("*服务描述："+classVO.getServiceDesc());
				
				System.out.println("*");
				List<MethodServiceVO> list = classVO.getMethodServiceList();
				System.out.println("*         具体服务描述信息                                     ");
				for (int i = 0; i < list.size(); i++) {
					MethodServiceVO mvo = list.get(i);
					
					System.out.println("*服务名称："+mvo.getMethod());
					System.out.println("*服务描述："+mvo.getDesc());
					Map<String, String> inMap = mvo.getInParam();
					for(Iterator it=inMap.keySet().iterator();it.hasNext();){
						String key = (String)it.next();
						System.out.print("*输入参数名："+key);
						System.out.println("  参数描述："+inMap.get(key));
					}
					
					Map<String, String> outMap = mvo.getInParam();
					for(Iterator it=outMap.keySet().iterator();it.hasNext();){
						String key = (String)it.next();
						System.out.print("*返回参数名："+key);
						System.out.println("  参数描述："+inMap.get(key));
					}
					
					
					System.out.println("*");
				}
				
			}
			System.out.println("************************************************************\n\n");
		}
	}
}
