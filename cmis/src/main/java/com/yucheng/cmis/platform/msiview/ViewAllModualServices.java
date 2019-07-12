package com.yucheng.cmis.platform.msiview;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yucheng.cmis.platform.msiview.dao.MsiviewDao;
import com.yucheng.cmis.platform.msiview.domain.ClassServiceVO;
import com.yucheng.cmis.platform.msiview.domain.MethodServiceVO;
import com.yucheng.cmis.platform.msiview.domain.ModualServiceVO;
import com.yucheng.cmis.platform.msiview.exception.MSIViewException;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;
import com.yucheng.cmis.pub.util.CMISPropertyManager;

/**
 * <p>显示业务开发平台中所有模块服务</p>
 * <p>
 * 	描述：
 * 		<ul>以模块为维度显示该模块所有对外提供的服务，该类服务有JAVA服务和JSP服务。</ul>
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 */
public class ViewAllModualServices {
	//JAVA接口package开头
	private static String CLASS_FLAG = "com.yucheng";
	//用于存放所有标准服务接口的集合
	private List<String> classList = new ArrayList<String>();
	private List<ModualServiceVO> MSIList = new ArrayList<ModualServiceVO>();
	
	/**
	 * <p>加载指定目录下所有模块注册服务</p>
	 * <p>默认的目录为 CMISPropertyManager.getInstance().getCmisProjectPath()+"/src/main/java/com/yucheng/"
	 * <p>
	 * 	描述：
	 * 		<ul>所有添加注解的接口、类都会被解析。</ul>
	 * 		<ul>
	 * 			每个模块可以对外提供多种类型的服务，如JAVA服务、页面服务，每种类型的服务对应一个接口定义，每个接口下有多个方法。
	 * 			所以最终解析后的结构如下：
	 * 				模块ID、模块描述、模块服务类型集合(接口的集合)
	 * 									服务集合1（接口2）
	 * 													服务1(方法1)
	 * 													服务2(方法2)
	 * 
	 * 									服务集合2（接口2）
	 * 		</ul>
	 * </p>
	 * @return List<ModualServiceVO>
	 */
	public List<ModualServiceVO> parseMoudalServicesDesc()throws MSIViewException{
		String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
		projectPath += "/src/main/java/com/yucheng/";
		
		return parseMoudalServicesDesc(projectPath,false);
	}
	
	/**
	 * <p>加载指定目录下所有模块注册服务</p>
	 * <p>
	 * 	描述：
	 * 		<ul>所有添加注解的接口、类都会被解析。</ul>
	 * 		<ul>
	 * 			每个模块可以对外提供多种类型的服务，如JAVA服务、页面服务，每种类型的服务对应一个接口定义，每个接口下有多个方法。
	 * 			所以最终解析后的结构如下：
	 * 				模块ID、模块描述、模块服务类型集合(接口的集合)
	 * 									服务集合1（接口2）
	 * 													服务1(方法1)
	 * 													服务2(方法2)
	 * 
	 * 									服务集合2（接口2）
	 * 		</ul>
	 * 
	 * </p>
	 * 
	 * @param rootParh java接口的根目录，一般是%PROJECT_HOME%/src/yucheng/cmis
	 * @param isForce true重新加载，fase只加载一次
	 * @return List<ModualServiceVO>
	 */
	public List<ModualServiceVO> parseMoudalServicesDesc(String rootPath,boolean isForce)throws MSIViewException{
		if(!isForce && MSIList.size()>0)
			return MSIList;
		
		MSIList.clear();
		
		return parseJavaMoudalServicesDesc(rootPath);
	}
	
	
	/**
	 * <p>加载指定目录下所有模块注册服务</p>
	 * <p>
	 * 	描述：
	 * 		<ul>所有添加注解的接口、类都会被解析。</ul>
	 * 		<ul>
	 * 			每个模块可以对外提供多种类型的服务，如JAVA服务、页面服务，每种类型的服务对应一个接口定义，每个接口下有多个方法。
	 * 			所以最终解析后的结构如下：
	 * 				模块ID、模块描述、模块服务类型集合(接口的集合)
	 * 									服务集合1（接口2）
	 * 													服务1(方法1)
	 * 													服务2(方法2)
	 * 
	 * 									服务集合2（接口2）
	 * 		</ul>
	 * 
	 * </p>
	 * 
	 * @param rootParh java接口的根目录，一般是%PROJECT_HOME%/src/yucheng/cmis
	 * @return List<ModualServiceVO>
	 */
	private List<ModualServiceVO> parseJavaMoudalServicesDesc(String rootPath)throws MSIViewException{
		try {
			classList.clear();
			this.findClass(rootPath);
			for (int i = 0; i < classList.size(); i++) {
				boolean flag = true;
				String className = classList.get(i);
				try {
					ClassServiceVO classVO = this.parseJavaMoudalServiceDesc(className);
					if(classVO==null) continue;
					
					for (ModualServiceVO mvo : MSIList) {
						//如果该模块下的服务接口已经添加到集合中，已不需要新增模块了
						if(mvo.getModualId().equals(classVO.getModualId())){
							mvo.getClassServiceList().add(classVO);
							flag = false;
						}
					}
					
					//如果该服务接口第一次添加，则需要新增模块
					if(flag){
						ModualServiceVO mvo = new ModualServiceVO();
						mvo.setModualId(classVO.getModualId());
						mvo.setModualName(classVO.getModualName());
						mvo.getClassServiceList().add(classVO);
						MSIList.add(mvo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new MSIViewException(e);
		}
		
		return MSIList;
	}
	
	/**
	 * 根据模块ID获得模块名称
	 * @param modualId 模块ID
	 * @return 模块名称
	 */
	public String getModualNameById(String modualId) throws MSIViewException{
		String retStr = null;
		if(MSIList.isEmpty())
			this.parseMoudalServicesDesc();
		
		for (ModualServiceVO mvo : MSIList) {
			if(mvo.getModualId().equals(modualId))
				retStr = mvo.getModualName();
		}
		
		return retStr;
		
	}
	
	/**
	 * 根据模块ID得到该模块下所有的服务集合
	 * @param modualId 模块ID
	 * @return 服务集合
	 * @throws MSIViewException
	 */
	public List<ClassServiceVO> getClassServicesByModualId(String modualId) throws MSIViewException{
		List<ClassServiceVO> retList = new ArrayList<ClassServiceVO>();
		String retStr = null;
		if(MSIList.isEmpty())
			this.parseMoudalServicesDesc();
		
		
		for (ModualServiceVO mvo : MSIList) {
			String mid = mvo.getModualId();
			if(mid.equals(modualId))
				return mvo.getClassServiceList();
		}
	
		return retList;
	}
	
	/**
	 * 根据服务集合ID得到该集下所有的服务
	 * @param classId 服务集合ID
	 * @return List<MethodServiceVO>
	 */
	public List<MethodServiceVO> getMethodServicesByClassId(String classId) throws MSIViewException{
		List<MethodServiceVO> retList = new ArrayList<MethodServiceVO>();
		String retStr = null;
		if(MSIList.isEmpty())
			this.parseMoudalServicesDesc();
		
		
		for (ModualServiceVO mvo : MSIList) {
			List<ClassServiceVO> classList = mvo.getClassServiceList();
			
			for (ClassServiceVO cvo : classList) {
				if(cvo.getServiceId().equals(classId))
					return cvo.getMethodServiceList();
			}
			
		}
	
		return retList;
	}
	
	/**
	 * 找到该目录下所有java文件，并将这些java文件以com.yucheng.xxx的形式放在classList集合中
	 * @param path 文件目录
	 */
	private void findClass(String path){
		if(path==null || path.equals("") || path.contains(".svn")) return;
		
		File file = new File(path);
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File tmpFile = fileList[i];
			if(tmpFile.isDirectory())
				findClass(tmpFile.getPath());
			else{
				//两种目录分隔符“/”、“\\”和“\”都替换掉
				String className = tmpFile.getPath().replace("/", ".").replace("\\\\", ".").replace("\\", ".");
				//移除不含CLASS_FLAG的
				if(!className.contains(CLASS_FLAG)) continue;
				//截取类的package
				className = className.substring(className.indexOf(CLASS_FLAG));
				//去掉.java
				if(className.endsWith(".java")){
					className = className.substring(0, className.indexOf(".java"));
					classList.add(className);
				}
			}
		}
	}
	
	/**
	 * <p>解释指定接口类服务</p>
	 * <p>
	 * 	描述：以Annotation（注解）的方式解释该接口以及该接口下所有的方法
	 * </p>
	 * @param className 类名称 如：com.yucheng...java
	 * @return
	 * @throws Exception
	 */
	/**
	 * @param className
	 * @return
	 * @throws Exception
	 */
	private ClassServiceVO parseJavaMoudalServiceDesc(String className) throws MSIViewException {
		if (className == null || className.length() <= 0)
			return null;
		ClassServiceVO classVO = new ClassServiceVO();
		MethodServiceVO methodVO = null;
		try {
			Class modualService = null;
			Method[] method = null;
			
			try {
				modualService = Class.forName(className);
				method = modualService.getMethods();
			} catch (Exception e) {
				System.err.println("系统注册服务查看出错，出错原因：反射出错，请检查该类："+className);
				return null;
			} catch (Error e){
				System.err.println("系统注册服务查看出错，出错原因：编译出错，请检查该类："+className);
				return null;
			}
			
			//只有添加了注解的接口类，才会被解析
			boolean flag = modualService.isAnnotationPresent(ModualService.class);
			
			if(!flag) return null;
			
			//解析接口类的描述信息
			ModualService desc = (ModualService) modualService.getAnnotation(ModualService.class);
			
			classVO.setServiceId(desc.serviceId());//接口服务ID
			classVO.setModualId(desc.modualId());//模块ID
			classVO.setModualName(desc.modualName());//模块名称
			classVO.setServiceDesc(desc.serviceDesc());//接口服务描述
			classVO.setClassName(desc.className());//接口类名
			

			//解析该接口中所有方法
			Set<Method> set = new HashSet<Method>();
			for (int i = 0; i < method.length; i++) {
				//只有添加了注解的方法，才会被解析
				boolean otherFlag = method[i].isAnnotationPresent(MethodService.class);
				if (otherFlag){
					methodVO = new MethodServiceVO();
					MethodService methodService = method[i].getAnnotation(MethodService.class);
					
					methodVO.setModualId(desc.modualId());//模块ID
					methodVO.setServiceId(desc.serviceDesc());//接口服务ID
					methodVO.setMethod(methodService.method());//方法名
					methodVO.setDesc(methodService.desc());//方法描述
					methodVO.setMethodType(methodService.methodType()==MethodType.JAVA?"JAVA":"JSP");
					//方法输入参数
					MethodParam[] inParams = methodService.inParam();
					for (int j = 0; j < inParams.length; j++) {
						MethodParam param = inParams[j];
						if(param.paramName()==null || param.paramName().equals("")) continue;
						methodVO.getInParam().put(param.paramName(),param.paramDesc());
					}
					
					//方法输出参数
					MethodParam[] outParams = methodService.outParam();
					for (int j = 0; j < outParams.length; j++) {
						MethodParam param = outParams[j];
						if(param.paramName()==null || param.paramName().equals("")) continue;
						methodVO.getOutParam().put(param.paramName(),param.paramDesc());
					}
					
					//调用示例
					methodVO.setExample(methodService.example());
					
					classVO.getMethodServiceList().add(methodVO);
				}
			}
		} catch (Exception e) {
			throw new MSIViewException(e);
		}

		return classVO;

	}
	
	/**
	 * 加载指定目录下所有模块注册服务，并保存到数据库中
	 * <p>每次在保存前都先将原数据清空
	 * 
	 * @param path java接口的根目录，一般是%PROJECT_HOME%/src/yucheng/cmis
	 * @param con 数据库连接
	 * @throws Exception
	 */
	public void loadMSI2DataBase(String path, Connection con) throws MSIViewException{
		try {
			//解析所有注册的服务
			List<ModualServiceVO> modSerList =  this.parseJavaMoudalServicesDesc(path);
			
			//待插入s_msi_class_view表的数据集合
			Map<String, Map<String,String>> classMap = new HashMap<String, Map<String,String>>();
			//待插入s_msi_method_view表的数据集合
			Map<String, Map<String,String>> methodMap = new HashMap<String, Map<String,String>>();
			
			int count = 0;
			for (int i = 0; i < modSerList.size(); i++) {
				ModualServiceVO modVO = modSerList.get(i);
				String modualName = modVO.getModualName();
				
				List<ClassServiceVO> classList = modVO.getClassServiceList();
				for(ClassServiceVO classVO: classList){
					Map<String,String> tmpMap1 = new HashMap<String, String>();
					tmpMap1.put("modual_id", modVO.getModualId());
					tmpMap1.put("modual_name", modVO.getModualName());
					tmpMap1.put("msi_class_id", classVO.getServiceId());
					tmpMap1.put("msi_class_desc", classVO.getServiceDesc());
					tmpMap1.put("msi_class_name", classVO.getClassName());
					classMap.put(classVO.getServiceId(), tmpMap1);
					
					List<MethodServiceVO> medSerList = classVO.getMethodServiceList();
					for (int j = 0; j < medSerList.size(); j++) {
						MethodServiceVO medVO = medSerList.get(j);
						String inParam = "";
						String outParam = "";
						Map<String, String> inMap = medVO.getInParam();
						for (Iterator iterator = inMap.keySet().iterator(); iterator.hasNext();) {
							String key = (String) iterator.next();
							inParam = inParam+key+":"+inMap.get(key)+";";
						}
						
						Map<String, String> outMap = medVO.getOutParam();
						for (Iterator iterator = outMap.keySet().iterator(); iterator.hasNext();) {
							String key = (String) iterator.next();
							outParam = outParam+key+":"+outMap.get(key)+";";
						}
						
						count ++;
						String methodId = count+"_"+medVO.getMethod();
						
						Map<String,String> tmpMap2 = new HashMap<String, String>();
						tmpMap2.put("msi_class_id", classVO.getServiceId());
						tmpMap2.put("msi_method_id", methodId);
						tmpMap2.put("msi_method_name", medVO.getMethod());
						tmpMap2.put("msi_method_desc", medVO.getDesc());
						tmpMap2.put("msi_method_param_in", inParam);
						tmpMap2.put("msi_method_param_out", outParam);
						tmpMap2.put("msi_method_type", medVO.getMethodType());
						tmpMap2.put("msi_method_example", medVO.getExample());
						methodMap.put(methodId, tmpMap2);
					}
				}
			}
			
			//数据库操作
			MsiviewDao dao = new MsiviewDao();
			//清空原数据
			dao.deleteAllMsiClassView(con);
			dao.deleteAllMsiMethodView(con);
			
			//保存最新数据
			dao.insertMsiClassView(con, classMap);
			dao.insertMsiMethodView(con, methodMap);
			
		} catch (Exception e) {
			throw new MSIViewException("解析并保存MSI服务出错！"+e);
		} 
	}
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		List<ModualServiceVO> mlist = MSIList;
		for (int j = 0; j < mlist.size(); j++) {
			ModualServiceVO vo = mlist.get(j);
			
			sb.append("************************************************************");
			
			sb.append("*         模块:"+vo.getModualName()+"描述信息                ");
			sb.append("*模块ID："+vo.getModualId());
			sb.append("*模块名称："+vo.getModualName());
			sb.append("*");
			
			List<ClassServiceVO> classList = vo.getClassServiceList();
			for (ClassServiceVO classVO : classList) {
				sb.append("*         服务接口信息                                     ");
				sb.append("*服务ID："+classVO.getServiceId());
				sb.append("*服务描述："+classVO.getServiceDesc());
				
				sb.append("*");
				List<MethodServiceVO> list = classVO.getMethodServiceList();
				sb.append("*         具体服务描述信息                                     ");
				for (int i = 0; i < list.size(); i++) {
					MethodServiceVO mvo = list.get(i);
					
					sb.append("*服务名称："+mvo.getMethod());
					sb.append("*服务描述："+mvo.getDesc());
					Map<String, String> inMap = mvo.getInParam();
					for(Iterator it=inMap.keySet().iterator();it.hasNext();){
						String key = (String)it.next();
						System.out.print("*输入参数名："+key);
						sb.append("  参数描述："+inMap.get(key));
					}
					
					Map<String, String> outMap = mvo.getInParam();
					for(Iterator it=outMap.keySet().iterator();it.hasNext();){
						String key = (String)it.next();
						System.out.print("*返回参数名："+key);
						sb.append("  参数描述："+inMap.get(key));
					}
					
					
					sb.append("*");
				}
				
			}
			sb.append("************************************************************\n\n");
		}
		return null;
	}
	
	
	public static void main(String[] args) throws MSIViewException {
		ViewAllModualServices obj = new ViewAllModualServices();
		String rootPath = "E:/WorkSpace/svn-dir/20 代码管理/cmis/src/main/java/com/yucheng/";
		
		List<ModualServiceVO> mlist = obj.parseMoudalServicesDesc(rootPath,true);
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
