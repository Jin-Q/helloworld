package com.ecc.emp.ext.tag.field;

import com.ecc.emp.data.KeyedCollection;

/**
 * 查询框：日期
 * @author JackYu
 *
 */
public class EMPExtSearchDate extends EMPExtSelect{

	private static final long serialVersionUID = 1L;
	protected String onfocus = null;
	protected String onblur = null;
    protected String onchange = null;
    protected String onselect = null;
    protected String autocomplete = null;
    protected String size = null;
    
    public EMPExtSearchDate(){
    	this.dataType = "Date";
    }
	@Override
	public Object clone() {
		EMPExtSearchDate tag = new EMPExtSearchDate();

	    cloneDafaultAttributes(tag);

	    return tag;
	}

	@Override
	protected String getType() {
		return "SearchDate";
	}
	
	/**
	 * 标签实现中所应该生成的明确的HTML标签
	 * select(查询模式) Date(输入框),
	 * 
	 * @param sb
	 * @param kColl
	 */
	protected void renderInnerHtml(StringBuffer sb, KeyedCollection kColl)
	  {
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
		this.setOnchange("EMP.field.SearchDate.prototype.spaceSearch(this)"); //设置默认事件
		this.onblur = "";
		this.onfocus = "";
		this.onselect = "";
		super.renderInnerHtml(sb, kColl);
		
		
		sb.append("&nbsp&nbsp");
		
		//查询框__日期
	    sb.append("<input");
	    writeAttribute(sb, "name", t_id);
	    writeAttribute(sb, "value", getValue(kColl));

	    writeAttribute(sb, "size", this.size); 

	    if ((this.cssElementClass != null) && (this.cssElementClass.length() > 0))
	      writeAttribute(sb, "class", this.cssElementClass);
	    else {
	      writeAttribute(sb, "class", "emp_field_date_input");
	    }

	    addDefaultAttributes(sb, kColl);

	    writeAttribute(sb, "onblur", onblur); 
	    writeAttribute(sb, "onchange", onchange);
	    writeAttribute(sb, "onfocus", onfocus);
	    writeAttribute(sb, "onselect", onselect);

	    writeAttribute(sb, "autocomplete", this.autocomplete);

	    sb.append("/>");
	  }

	public String getAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	
}
