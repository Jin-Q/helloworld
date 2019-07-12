package com.ecc.emp.ext.tag.field;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.log.EMPLog;

/**
 * 复选框标签。
 * <p>
 * 缺省的复选框的样式是：emp_field_checkbox_input
 * @author liubq
 *
 */
public class EMPExtCheckBox extends EMPExtRadio {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 当checkBox对应的值为IndexedCollection时，该IndexedCollection的名称<br>
	 * 若未定义该属性，则表示checkBox的值是以";"分割的字符串
	 */
	protected String valueCollection = null;
	
	
	/**
	 * 注册到页面中的JS对象类型(CheckBox)
	 */
	protected String getType() {
		return "CheckBox";
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
			this.writeAttribute(sb, "type", "checkbox");
			if(value != null && value.equals(this.checkValue)){
				this.writeAttribute(sb, "checked", "true");
			}

			//设置input框的样式，缺省使用emp_field_checkbox_input
			if (this.cssElementClass != null && this.cssElementClass.length() > 0) {
				this.writeAttribute(sb, "class", cssElementClass);
			}else{
				this.writeAttribute(sb, "class", "emp_field_checkbox_input");
			}
			
			//在CheckBox选择框上添加基本的属性
			this.addDefaultAttributes(sb, kColl);
			
			this.writeAttribute(sb, "onblur", this.onblur);
			this.writeAttribute(sb, "onchange", this.onchange);
			this.writeAttribute(sb, "onfocus", this.onfocus);
			this.writeAttribute(sb, "onclick", this.onclick);
						
			sb.append("/>");
			
		}else{
			String[] valueList = null;
			if(this.valueCollection != null){
				IndexedCollection valueColl = null;
				try {
					valueColl = (IndexedCollection)this.getDataElement(this.valueCollection);
				} catch (Exception e) {}
				
				if (valueColl != null) {
					valueList = new String[valueColl.size()];
					for (int i = 0; i < valueColl.size(); i++) {
						try {
							KeyedCollection aColl = (KeyedCollection) valueColl.getElementAt(i);
							Object valueObject = aColl.getDataValue(ATTR_ENNAME);
							
							if (valueObject != null)
								valueList[i] = valueObject.toString();
							else
								valueList[i] = null;
						} catch (Exception e) {

						}
					}

				}else{
					valueList = new String[0];
				}
			}else{
				String value = this.getValue(kColl);
				valueList = value.split(",");
			}
			
			IndexedCollection iColl = this.getDictMapCollection(this.dictname);
			if(this.layout){
				sb.append("<table");
				//sb.append(str("class","emp_field_checkbox_table"));
				sb.append(">");
			}
			
			String language = this.getLanguage();// 具体当前页面所需要显示的语言(用于多语言显示的情况)
			try{
				for( int i=0; iColl != null && i<iColl.size(); i++){
	        		KeyedCollection aKColl = (KeyedCollection)iColl.getElementAt(i);
		        	if(this.layout){
		        		sb.append("<tr><td>");
		        	}
		        	sb.append("<input");
		        	this.writeAttribute(sb, "name", this.id);
					this.writeAttribute(sb, "type", "checkbox");
		        	
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
					
					for (int j = 0; j < valueList.length; j++) {
						String value = valueList[j];
						if(value != null && value.equals(enname)){
							this.writeAttribute(sb, "checked", "true");
							break;
						}
					}

					//设置input框的样式，缺省使用emp_field_checkbox_input
					if (this.cssElementClass != null && this.cssElementClass.length() > 0) {
						this.writeAttribute(sb, "class", cssElementClass);
					}else{
						this.writeAttribute(sb, "class", "emp_field_checkbox_input");
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
		super.renderOtherAttribute(buffer, kColl, flat);
		this.writeAttribute(buffer, "valueCollection", this.valueCollection);
	}
	
	public Object clone() {
		EMPExtCheckBox tag = new EMPExtCheckBox();

		//复制缺省的属性
		this.cloneDafaultAttributes(tag);
		
		tag.onfocus = this.onfocus;
		tag.onblur = this.onblur;
		tag.onchange = this.onchange;
		tag.layout = this.layout;
		tag.valueCollection = this.valueCollection;
		return tag;	
	}

	public String getValueCollection() {
		return valueCollection;
	}

	public void setValueCollection(String valueCollection) {
		this.valueCollection = valueCollection;
	}
}
