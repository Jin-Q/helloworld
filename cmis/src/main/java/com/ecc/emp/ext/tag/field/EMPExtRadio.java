package com.ecc.emp.ext.tag.field;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.log.EMPLog;

/**
 * 单选框标签。
 * <p>
 * 缺省的单选框的样式是：emp_field_radio_input
 * @author liubq
 *
 */
public class EMPExtRadio extends EMPExtFieldBase {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * HTML标准事件
	 */
	protected String onfocus = null;
	
	/**
	 * HTML标准事件
	 */
	protected String onblur = null;
	
	/**
	 * HTML标准事件
	 */
	protected String onchange = null;
	
	/**
	 * HTML标准事件
	 */
	protected String onclick = null;
	
	/**
	 * 选中时的取值，如果当前值与选中值相等，则单选框显示为选中状态
	 */
	protected String checkValue = null;
	
	/**
	 * 是否使用table对多个的单选框进行分层显示
	 */
	protected boolean layout = true;
	
	/**
	 * 如果不分层显示，则定义每个单选框之间的分割符
	 */
	protected String delimiter = null;
	
	/**
	 * 注册到页面中的JS对象类型(Radio)
	 */
	protected String getType() {
		return "Radio";
	}

	/**
	 * 标签实现中所应该生成的明确的HTML标签(SELECT标签)
	 */
	protected void renderInnerHtml(StringBuffer sb, KeyedCollection kColl) {
		
		if(this.dictname == null || this.dictname.length() == 0){
			sb.append("<input");
			
			this.writeAttribute(sb, "name", this.id);
			String value = this.getValue(kColl);
			this.writeAttribute(sb, "value", this.checkValue);
			this.writeAttribute(sb, "type", "radio");
			if(value != null && value.equals(this.checkValue)){
				this.writeAttribute(sb, "checked", "true");
			}

			//设置input框的样式，缺省使用emp_field_radio_input
			if (this.cssElementClass != null && this.cssElementClass.length() > 0) {
				this.writeAttribute(sb, "class", cssElementClass);
			}else{
				this.writeAttribute(sb, "class", "emp_field_radio_input");
			}
			
			//在CheckBox选择框上添加基本的属性
			this.addDefaultAttributes(sb, kColl);
			
			this.writeAttribute(sb, "onblur", this.onblur);
			this.writeAttribute(sb, "onchange", this.onchange);
			this.writeAttribute(sb, "onfocus", this.onfocus);
			this.writeAttribute(sb, "onclick", this.onclick);
			
			sb.append("/>");
			
		}else{
			String value = this.getValue(kColl);
			IndexedCollection iColl = this.getDictMapCollection(this.dictname);
			if(this.layout){
				sb.append("<table");
				//sb.append(str("class","emp_field_checkbox_table"));
				sb.append(">");
			}
			
			String language = this.getLanguage();// 具体当前页面所需要显示的语言(用于多语言显示的情况)
			try{
				for( int i=0; i<iColl.size(); i++){
	        		KeyedCollection aKColl = (KeyedCollection)iColl.getElementAt(i);
		        	if(this.layout){
		        		sb.append("<tr><td>");
		        	}
		        	sb.append("<input");
		        	this.writeAttribute(sb, "name", this.id);
					this.writeAttribute(sb, "type", "radio");
		        	
					String enname = (String) aKColl.getDataValue(EMPExtTagSupport.ATTR_ENNAME);
					String cnname = null;
					if (this.languageResouce) {
						try {
							cnname = (String) aKColl.getDataValue(EMPExtTagSupport.ATTR_CNNAME
											+ "_" + language);
						} catch (Exception e) {
						}
					}
					if (cnname == null) {
						cnname = (String) aKColl.getDataValue(EMPExtTagSupport.ATTR_CNNAME);
					}
					this.writeAttribute(sb, "value", enname);
					
					if(value != null && value.equals(enname)){
						this.writeAttribute(sb, "checked", "true");
					}
					
					//设置input框的样式，缺省使用emp_field_radio_input
					if (this.cssElementClass != null && this.cssElementClass.length() > 0) {
						this.writeAttribute(sb, "class", cssElementClass);
					}else{
						this.writeAttribute(sb, "class", "emp_field_radio_input");
					}

					//在CheckBox选择框上添加基本的属性
					this.addDefaultAttributes(sb, kColl);
					
					this.writeAttribute(sb, "onblur", this.onblur);
					this.writeAttribute(sb, "onchange", this.onchange);
					this.writeAttribute(sb, "onfocus", this.onfocus);
					this.writeAttribute(sb, "onclick", this.onclick);
					
					sb.append("/>").append(cnname);
					
		        	if(this.layout){
						sb.append("</td></tr>");
		        	} else if(this.delimiter != null){
		        		sb.append(this.delimiter);
		        	}
				}
	        }catch(Exception e){
				EMPLog.log("EMPExt", EMPLog.WARNING, 0, "生成复选框内容出错!", e);
        	}

	        if(this.layout){
	        	sb.append("</table>");
        	}
		}
	}

	/**
	 * 标签实现中所拥有的特殊的属性
	 */
	protected void renderOtherAttribute(StringBuffer buffer, KeyedCollection kColl, boolean flat) {
		
	}
	
	public Object clone() {
		EMPExtRadio tag = new EMPExtRadio();

		//复制缺省的属性
		this.cloneDafaultAttributes(tag);
		
		tag.onfocus = this.onfocus;
		tag.onblur = this.onblur;
		tag.onchange = this.onchange;
		tag.layout = this.layout;
		tag.checkValue = this.checkValue;
		tag.delimiter = this.delimiter;
		return tag;	
	}

	public String getOnblur() {
		return onblur;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getOnfocus() {
		return onfocus;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	public boolean isLayout() {
		return layout;
	}

	public void setLayout(boolean layout) {
		this.layout = layout;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}
	
}
