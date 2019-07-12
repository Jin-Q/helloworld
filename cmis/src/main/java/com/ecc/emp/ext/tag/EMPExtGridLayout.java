package com.ecc.emp.ext.tag;

import java.util.Vector;

import javax.servlet.jsp.JspException;

import com.ecc.emp.ext.tag.field.EMPExtFieldBase;

public class EMPExtGridLayout extends EMPExtTagSupport {

	private static final long serialVersionUID = 1L;
	
	protected Vector fields = new Vector();
	
	protected int maxColumn = 2; //列数
	
	protected String title = null;
	
	protected String cssTitleClass = "emp_gridlayout_title";
	
	protected String cssClass = "emp_gridLayout_table";

	public int doStartTag() throws JspException {
		
		fields.clear();
		
		return EVAL_BODY_INCLUDE;		
	}
	
	public int doEndTag() throws JspException {
		
		StringBuffer sb = new StringBuffer();
		
		//缺省每个layOut都对应于一个页面的group对象
		if(this.title != null){
			sb.append("<div ");
			this.writeAttribute(sb, "class", this.cssTitleClass);
			sb.append(">"+this.getTitle()+"</div>\n");
		}		
		System.err.println("EMPExtGridLayout: " + this.getTitle());
		
		sb.append("<div ");
		this.writeAttribute(sb, "id", this.id);
		sb.append(" class='emp_group_div'>");
		
		sb.append("<table maxColumn='"+this.maxColumn+"' ");
		this.writeAttribute(sb, "class", this.cssClass + " " + this.maxColumn);
		sb.append(">\n");
		/**
		if(this.title != null){
			sb.append("<thead><tr");
			this.writeAttribute(sb, "class", this.cssTitleClass);
			sb.append("><td colspan='"+(this.maxColumn*2)+"'>"+this.getTitle()+"</td></tr></thead>\n");
		}
		*/
		int count = this.maxColumn;
		for (int i = 0; this.fields != null && i < this.fields.size(); i++) {
			if(count == this.maxColumn){
				sb.append("<tr>\n");
			}
			EMPExtFieldBase column = (EMPExtFieldBase) this.fields.elementAt(i);
			//label部分
			sb.append("<td");
			this.writeAttribute(sb, "class", column.getCssLabelClass());
			sb.append(">").append(column.getCnname()).append("</td>");
			//编辑框部分
			sb.append("<td");;
			this.writeAttribute(sb, "class", column.getCssTDClass());
			
			int colSpan = Integer.parseInt(column.getColSpan());
			if(colSpan > this.maxColumn)
				colSpan = this.maxColumn;
			
			if(i != (this.fields.size()-1) ){
				EMPExtFieldBase nextColumn = (EMPExtFieldBase) this.fields.elementAt(i + 1);
				int nextColSpan = Integer.parseInt(nextColumn.getColSpan());
				if((colSpan + nextColSpan) > count){
					this.writeAttribute(sb, "colspan", String.valueOf(count*2-1));
					column.setColSpan(String.valueOf(count));
					count = 0;
				}else{
					this.writeAttribute(sb, "colspan", String.valueOf(colSpan*2-1));
					count = count - colSpan;
				}
			}else{
				this.writeAttribute(sb, "colspan", String.valueOf(count*2-1));
				column.setColSpan(String.valueOf(count));
				count = 0;
			}
			sb.append(">");	
			
			//获得字段的HTML代码
			column.renderFieldTag(sb, null, column.isFlat());
			
			sb.append("</td>");
			if(count == 0){
				sb.append("\n</tr>\n");
				count = this.maxColumn;
			}
		}
		sb.append("</table>");
		
		sb.append("</div>");
		
		outputContent(sb.toString());
		
		return SKIP_BODY;
	}
	
	public void addCMISDataField(EMPExtFieldBase tag) {
		this.fields.addElement(tag);
	}
	
	public int getMaxColumn() {
		return maxColumn;
	}

	public void setMaxColumn(int maxColumn) {
		this.maxColumn = maxColumn;
	}
	
	public String getTitle() {
		return getResourceValue(title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getCssTitleClass() {
		return cssTitleClass;
	}

	public void setCssTitleClass(String cssTitleClass) {
		this.cssTitleClass = cssTitleClass;
	}

}
