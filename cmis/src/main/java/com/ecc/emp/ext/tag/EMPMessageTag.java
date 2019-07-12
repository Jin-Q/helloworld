package com.ecc.emp.ext.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.ecc.emp.web.jsptags.EMPTagSupport;
import com.yucheng.cmis.message.CMISMessage;
import com.yucheng.cmis.message.CMISMessageManager;

/**
 * 异常、错误信息信息获取标签
 * @author ljt
 *
 */
public class EMPMessageTag extends EMPTagSupport {

	//信息组（通过组件定义对应的组）
	private String group;
	//信息代码，标识唯一的一个信息
	private String code;
	
	public EMPMessageTag() {
		super();
	}
	
	public int doStartTag() throws JspException {
		
		try { 
			
			JspWriter out = pageContext.getOut();
			StringBuffer buffer = new StringBuffer();
			
			CMISMessage message = CMISMessageManager.getCMISMessage(group, code, null);
			
			buffer.append(message.getDisplay());
			out.write(buffer.toString());
			
		} catch (Exception e) {
		}
		System.out.println("");
		return EVAL_BODY_INCLUDE;
	}
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
