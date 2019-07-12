package com.yucheng.cmis.platform.msiview.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 	Title:业务开发平台服务模块服务值对象
 * 	Describe:一个模块下可以有多个接口，与ClassService是一对多关系
 * <pre>
 * 
 * @author yuhq
 * @version 1.0
 */
public class ModualServiceVO {

	private String modualId = null;//模块ID
	private String modualName = null;//模块名称
	private List<ClassServiceVO> classServiceList = new ArrayList<ClassServiceVO>();//接口，是具体服务的一个集合

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

	public List<ClassServiceVO> getClassServiceList() {
		return classServiceList;
	}

	public void setClassServiceList(List<ClassServiceVO> classServiceList) {
		this.classServiceList = classServiceList;
	}
	
}
