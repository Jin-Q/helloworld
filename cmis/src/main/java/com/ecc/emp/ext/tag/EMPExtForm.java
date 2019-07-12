package com.ecc.emp.ext.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.ecc.emp.core.EMPConstance;



public class EMPExtForm extends EMPExtTagSupport {
	
	private static final long serialVersionUID = 1L;

	/**
	 * HTML标准属性
	 */
	protected String target;
	
	/**
	 * HTML标准属性
	 */
	protected String action;
	
	/**
	 * HTML标准属性：
	 * post或者是get
	 */
	protected String method = "post"; 
	
	/**
	 * HTML标准属性：
	 * application/x-www-form-urlencoded或者是multipart/form-data;
	 */
	protected String enctype; 
	
	/**
	 * HTML标准事件
	 */
	protected String onsubmit = null;
	
	
	public int doStartTag() throws JspException {
		String dataGroupId = "dataGroup_in_form"+this.id; 
		this.outputContent("<DIV id='"+dataGroupId+"' class='emp_group_div' style='background-color:transparent;border:none;padding:0;'>");
		//this.outputContent("<DIV id='"+dataGroupId+"' style='background-color=transparent;border=0px'>");
		return EVAL_BODY_INCLUDE;		
	}

	
	public int doEndTag() throws JspException {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("</DIV>\n");
		
		buffer.append("<form ");
		this.writeAttribute(buffer, "id", this.id);
		this.writeAttribute(buffer, "name", this.name);
		
		String method = this.method;
        if( method == null )
        	method = "post";
        this.writeAttribute(buffer, "method", method);
        
        this.writeAttribute(buffer, "enctype", this.enctype);
        this.writeAttribute(buffer, "target", this.target);
        
        String action = this.action;
        if(action == null){
        	HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        	String actionId =(String)request.getAttribute(EMPConstance.ATTR_ACTIONID);
        	action = actionId + ".do";
        }
        this.writeAttribute(buffer, "action", this.getPostURL(action));
        
        
		buffer.append(">\r\n");
		
		//在form中隐藏一些表单提交所需的数据，如URL REWRITE情况下的sessionId
	    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
	    Integer dpId = null;
	    try{
	    	dpId = (Integer)request.getAttribute(EMPConstance.ATTR_DPID);
	    }catch(Exception e){}
	    if( dpId != null )
		{
	    	buffer.append("\n\t");
	    	buffer.append("\n\t<input type='hidden'");
	    	this.writeAttribute(buffer, "name", EMPConstance.ATTR_DPID);
	    	this.writeAttribute(buffer, "value", dpId);
	    	buffer.append("/>\n");
		}
	    buffer.append( super.getAppendPostField("HTML"));
	    
		buffer.append("</form>\r\n");
		
		//生成缺省的各个JS方法，以减少页面的编辑量
	    buffer.append("\n<script>\n");
	    String dataGroupId = "dataGroup_in_form"+this.id;
	    
	    // 缺省的submit方法
		buffer.append("function doSubmit(button){\n");
		buffer.append("\tvar form = document.getElementById('").append(this.id)
				.append("');\n");
		
		// 判断数据合法性
		buffer.append("\tvar result = page.dataGroups.").append(dataGroupId)
				.append(".checkAll();\n");

		// 判断是否都合法
		buffer.append("\tif(form && result){\n");

		// 判断通过后，调用toForm方法，将真实值放到form中
		buffer.append("\t\tpage.dataGroups.").append(dataGroupId).append(
				".toForm(form);\n");
		
        if(this.onsubmit != null && this.onsubmit.length() > 0){
        	
        	String submit = this.onsubmit.replaceAll("this", "form");
        	int idx = submit.indexOf("return");
        	if( idx != -1 )
        		submit = submit.substring( idx + 7);
        	
        	buffer.append("\t\tvar submitRes=").append(submit).append(";\n");
        	buffer.append("\t\tif(!submitRes){return submitRes;}\n");
        }
		buffer.append("\t\tform.submit();\n");

		buffer.append("\t}\n");
		buffer.append("};\n");
	    
		// 缺省的reset方法
		buffer.append("function doReset(button){\n");
		buffer.append("\tpage.dataGroups.").append(dataGroupId).append(
				".reset();\n");
		
		buffer.append("};\n");
	    
	    buffer.append("</script>\n\n");
	    
	    this.outputContent(buffer.toString());
	    
		return SKIP_BODY;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getEnctype() {
		return enctype;
	}


	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public String getOnsubmit() {
		return onsubmit;
	}


	public void setOnsubmit(String onsubmit) {
		this.onsubmit = onsubmit;
	}


	public String getTarget() {
		return target;
	}


	public void setTarget(String target) {
		this.target = target;
	}
	
}
