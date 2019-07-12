package com.yucheng.cmis.platform.msiview.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 	Title:模块服务值对象
 * 	Describe:与JAVA接口一一对应
 * <pre>
 * 
 * @author yuhq
 * @version 1.0
 */
public class ClassServiceVO {

	private String modualId = null;//模块ID
	private String modualName = null;//模块名乐
	private String serviceId = null;//服务ID
	private String serviceDesc = null;//服务描述
	private String className = null;//类名
	
	private List<MethodServiceVO> methodServiceList = new ArrayList<MethodServiceVO>();//服务接口中的具体服务
	
	public String getModualId() {
		return modualId;
	}
	public void setModualId(String modualId) {
		this.modualId = modualId;
	}
	public String getModualName() {
		return modualName;
	}
	public void setModualName(String modualName) {
		this.modualName = modualName;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<MethodServiceVO> getMethodServiceList() {
		return methodServiceList;
	}

	public void setMethodServiceList(List<MethodServiceVO> methodServiceList) {
		this.methodServiceList = methodServiceList;
	}
	
	public void setMethodServiceVO(MethodServiceVO methodVO){
		this.methodServiceList.add(methodVO);
	}
	
	public void removeMethodServiceVO(MethodServiceVO methodVO){
		this.methodServiceList.remove(methodVO);
	}
}
