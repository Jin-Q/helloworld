package com.yucheng.cmis.biz01line.ind.tag;

import java.util.Vector;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.ext.tag.field.EMPExtFieldBase;
import com.yucheng.cmis.pub.CMISComponent;

public class IndTableLayout extends EMPExtTagSupport {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CMISComponent.class);	
	protected Vector fields = new Vector();
	
	protected String cssClass = "ccrTable";

	public int doStartTag() throws JspException {
		
		fields.clear();
		//缺省每个layOut都对应于一个页面的group对象
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table ");
		this.writeAttribute(sb, "class", this.cssClass);
		sb.append(">\n");
		sb.append("<tr class='tableTitle'>" +
				"<th class = \"titleSerno\" align=\"center\">序号</td>"+
				"<th class = \"titleGroup\" align=\"center\">组别</td>"+
				"<th class = \"titleWeight\" align=\"center\">权重</td>"+
				"<th class = \"titleIndex\" align=\"center\">指标</td>"+
				"<th class = \"titleSelection\" align=\"center\">选项/指标值</td>"+
				"<th class = \"titleScore\" align=\"center\">得分</td>" +
				"</tr>");		
		outputContent(sb.toString());
		return EVAL_BODY_INCLUDE;		
	}
	
	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		
		sb.append("</TABLE>"); 		
		outputContent(sb.toString());
		
		return SKIP_BODY;
	}
	
	public void addCMISDataField(EMPExtFieldBase tag) {
		this.fields.addElement(tag);
	}
	

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

}
