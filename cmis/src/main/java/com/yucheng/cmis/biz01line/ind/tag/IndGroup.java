package com.yucheng.cmis.biz01line.ind.tag;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.ext.tag.field.EMPExtFieldBase;
import com.yucheng.cmis.pub.CMISComponent;

/**
 * <h1>指标组展示标签</h1>
 * <p>用来输出每个组的头部和尾部html代码</p>
 * @author toto233
 *
 */
public class IndGroup extends EMPExtTagSupport {

	private static final Logger logger = Logger.getLogger(CMISComponent.class);
	private static final long serialVersionUID = 1L;
	
	protected Vector fields = new Vector();
	


	protected String seqno = "0";
	protected String groupNo = "G0000000";
	protected String groupName = "Group Name";
	protected String weight = "weight";

	
	
	public int doStartTag() throws JspException {
		
		fields.clear();
		//缺省每个layOut都对应于一个页面的group对象
		
		StringBuffer sb = new StringBuffer();
		sb.append("<tr class='tableBody'>\n"+
		"<td class = \"bodySerno\" align=\"center\">"+this.seqno+"</td>\n"+
		"<td class = \"bodyGroup\" align=\"center\">"+this.groupName+"</td>\n"+
		
		"<td class = \"indexGroup\" colSpan = \"3\">\n"+
		"<table class = \"indexTable\">\n");

		outputContent(sb.toString());
		return EVAL_BODY_INCLUDE;		
	}
	
	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		
		sb.append("</table>\n"+
			"</td>\n"+
			"</tr>\n"); 		
		outputContent(sb.toString());
		
		return SKIP_BODY;
	}
	
	public void addCMISDataField(EMPExtFieldBase tag) {
		this.fields.addElement(tag);
	}
	public String getSeqno() {
		String st_return = "";
		String value = this.seqno;
		if (value != null && value.startsWith("$")) {
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
			if (context != null) {
				try {
					st_return = (String) context.getDataValue(value.substring(1));
				} catch (Exception e) {
				}
			} 
			if (st_return == null) {
				st_return = request.getParameter(value.substring(1));
			}
		} else {
			st_return = this.seqno;
		}
		return st_return;
		
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getGroupName() {
		String st_return = "";
		String value = this.groupName;
		if (value != null && value.startsWith("$")) {
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
			if (context != null) {
				try {
					st_return = (String) context.getDataValue(value.substring(1));
				} catch (Exception e) {
				}
			} 
			if (st_return == null) {
				st_return = request.getParameter(value.substring(1));
			}
		} else {
			st_return = this.groupName;
		}
		return st_return;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getWeight() {
		String st_return = "";
		String value = this.weight;
		if (value != null && value.startsWith("$")) {
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
			if (context != null) {
				try {
					st_return = (String) context.getDataValue(value.substring(1));
				} catch (Exception e) {
				}
			} 
			if (st_return == null) {
				st_return = request.getParameter(value.substring(1));
			}
		} else {
			st_return = this.weight;
		}
		return st_return;		
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getGroupNo() {
		String st_return = "";
		String value = this.groupNo;
		if (value != null && value.startsWith("$")) {
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
			if (context != null) {
				try {
					st_return = (String) context.getDataValue(value.substring(1));
				} catch (Exception e) {
				}
			} 
			if (st_return == null) {
				st_return = request.getParameter(value.substring(1));
			}
		} else {
			st_return = this.groupNo;
		}
		return st_return;

	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public KeyedCollection getGroupKcoll(){
		KeyedCollection kcoll =null;
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		try {
			kcoll = (KeyedCollection)context.getDataElement(this.getGroupNo());
		} catch (ObjectNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		return kcoll;
	}
}
