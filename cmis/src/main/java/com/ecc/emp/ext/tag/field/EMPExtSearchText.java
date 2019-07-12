package com.ecc.emp.ext.tag.field;

import com.ecc.emp.data.KeyedCollection;

/**
 * 查询标签_文本
 * <p>
 * 缺省的下拉框的显示样式是：emp_field_select_select<br>
 * 只读状态下，缺省的输入框样式是：emp_field_select_input
 * 
 * 缺省的文本框显示样式是：.emp_field_text_input
 * 
 * @author JackYu
 *
 */
public class EMPExtSearchText extends EMPExtSelect{

	private static final long serialVersionUID = 1L;
	protected String maxlength = null;
	protected String minlength = null;
	protected String onselect = null;
	protected String autocomplete = null;
	protected String cssElementClass = null;
	
	@Override
	public Object clone() {
		EMPExtSearchText tag = new EMPExtSearchText();

	    cloneDafaultAttributes(tag);

	    tag.maxlength = this.maxlength;
	    tag.minlength = this.minlength;
	    tag.size = this.size;
	    tag.onfocus = this.onfocus;
	    tag.onblur = this.onblur;
	    tag.onchange = this.onchange;
	    tag.onselect = this.onselect;
	    tag.autocomplete = this.autocomplete;
	    return tag;
	}

	@Override
	protected String getType() {
		return "SearchText";
	}

	/**
	 * 标签实现中所应该生成的明确的HTML标签
	 * select(查询模式) text(输入框),
	 * 
	 * @param sb
	 * @param kColl
	 */
	protected void renderInnerHtml(StringBuffer sb, KeyedCollection kColl) {
		//区分查询下拉选项框与输入框属性
		String t_id = this.id;
		String t_dicName = this.dictname;
		String onchange = this.onchange;
		String onblur = this.onblur;
		String onfocus = this.onfocus;
		String onselect = this.onselect;
		//下拉选框
		//设制下拉选框的属性
		this.setId(this.id+"searchType");
		this.setDictname("SEARCH_TYPE");
		this.onblur = "";
		this.onfocus = "";
		this.onselect = "";
		//设置onChange事件
		this.setOnchange("EMP.field.SearchText.prototype.spaceSearch(this)"); 
		super.renderInnerHtml(sb, kColl);
		
		
		sb.append("&nbsp&nbsp");
		
		//查询框
		sb.append("<input");
	    writeAttribute(sb, "name", t_id);
	    writeAttribute(sb, "value", getValue(kColl));

	    writeAttribute(sb, "maxlength", this.maxlength);
	    writeAttribute(sb, "size", this.size);

	    if ((this.cssElementClass != null) && (this.cssElementClass.length() > 0))
	      writeAttribute(sb, "class", this.cssElementClass);
	    else {
	      writeAttribute(sb, "class", "emp_field_text_input");
	    }

	    addDefaultAttributes(sb, kColl);

	    writeAttribute(sb, "onblur", onchange);
	    writeAttribute(sb, "onchange", onchange);
	    writeAttribute(sb, "onfocus", onfocus);
	    writeAttribute(sb, "onselect", onselect);

	    writeAttribute(sb, "autocomplete", this.autocomplete);

	    sb.append("/>");
		
	}

	
	@Override
	protected void renderOtherAttribute(StringBuffer buffer,
			KeyedCollection kColl, boolean flat) {
		this.writeAttribute(buffer, "defMsg", this.defMsg);
		
	}
	
	public String getAutocomplete() {
		return this.autocomplete;
	}

	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}

	public String getMaxlength() {
		return this.maxlength;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	public String getMinlength() {
		return this.minlength;
	}

	public void setMinlength(String minlength) {
		this.minlength = minlength;
	}

	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCssElementClass() {
		return cssElementClass;
	}

	public void setCssElementClass(String cssElementClass) {
		this.cssElementClass = cssElementClass;
	}
}
