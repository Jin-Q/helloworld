package com.yucheng.cmis.platform.msiview.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 	业务开发平台服务接口中的具体服务值对象，与接口中的方法对应
 * <p>
 * 
 * @author yuhq
 * @version 1.0
 */
public class MethodServiceVO {
	private String serviceId = null;//服务ID,如果是JAVA服务接口，则该属性有意义
	private String modualId = null;//模块ID
	private String method = null;//接口方法
	private String desc = null;//方法描述
	private String methodType = null;//方法类型：JAVA  JSP
	private Map<String, String> inParam = new LinkedHashMap<String, String>();//输入参数
	private Map<String, String> outParam = new LinkedHashMap<String, String>();//输出参数
	private String example = null;//调用示例
	
	public String getModualId() {
		return modualId;
	}
	public void setModualId(String modualId) {
		this.modualId = modualId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	public Map<String, String> getInParam() {
		return inParam;
	}
	
	public void setInParam(Map<String, String> inParam) {
		this.inParam = inParam;
	}
	public Map<String, String> getOutParam() {
		return outParam;
	}
	public void setOutParam(Map<String, String> outParam) {
		this.outParam = outParam;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	
}
