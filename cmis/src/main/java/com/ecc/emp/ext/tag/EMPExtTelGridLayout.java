/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   EMPExtButton.java

package com.ecc.emp.ext.tag;

import javax.servlet.jsp.JspException;

// Referenced classes of package com.ecc.emp.ext.tag:
//            EMPExtTagSupport

public class EMPExtTelGridLayout extends EMPExtTagSupport
{
private static final long serialVersionUID = 1L;
	protected String title = null;
	

	public int doStartTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"centercontent_box000\">\n");
		sb.append("<div class=\"centercontent_box000_left_title\">\n");
		sb.append("<div class=\"centercontent_box000_right_title\">\n");
		sb.append("<input type=\"hidden\">\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("<div class=\"centercontent_box000_left_info\">\n");
		sb.append("<div class=\"centercontent_box000_right_info\">\n");
		if(title==null||"".equals(title)){
			
		}else{
			sb.append("<div class=\"info_title_bg\">\n");
			sb.append("<table>\n");
			sb.append("<tr>\n");
			sb.append("<td class=\"left_td\"><span>"+title+"</span></td>\n");
			sb.append("<td class=\"right_td\"><span class=\"window_view window_up\"><input type=\"hidden\"/></span></td>\n");
			sb.append("</tr>\n");
			sb.append("</table>\n");
			sb.append("</div>\n");
		}
		outputContent(sb.toString());
		
		return EVAL_BODY_INCLUDE;		
	}
	
	public int doEndTag() throws JspException {
		StringBuffer sbEnd = new StringBuffer();
		sbEnd.append("</div>\n");
		sbEnd.append("</div>\n");
		sbEnd.append("<div class=\"centercontent_box000_left_bottom\">\n");
		sbEnd.append("<div class=\"centercontent_box000_right_bottom\">\n");
		sbEnd.append("<input type=\"hidden\">\n");
		sbEnd.append("</div>\n");
		sbEnd.append("</div>\n");
		sbEnd.append("</div>\n");
		
		outputContent(sbEnd.toString());
		
		return SKIP_BODY;
	}
	
	
	public String getTitle() {
		return getResourceValue(title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

}