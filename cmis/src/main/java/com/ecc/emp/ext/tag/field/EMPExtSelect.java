package com.ecc.emp.ext.tag.field;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.log.EMPLog;

/**
 * 下拉框标签。
 * <p>
 * 缺省的下拉框的显示样式是：emp_field_select_select<br>
 * 只读状态下，缺省的输入框样式是：emp_field_select_input
 * @author liubq
 *
 */
public class EMPExtSelect extends EMPExtFieldBase {

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
	 * HTML标准属性
	 */
	protected String size = null;
	
	protected String multiple = null;
	


	/**
	 * 只读时的显示的输入框样式，缺省是emp_field_select_input
	 */
	protected String cssFakeInputClass = "emp_field_select_input";
	
	/**
	 * 未选择状态下，下拉框的显示信息(未选择状态下，下拉框的值为"")，缺省是"-----请选择-----"
	 */
	protected String defMsg = "-----请选择-----";
	
	/**
	 * 注册到页面中的JS对象类型(Select)
	 */
	protected String getType() {
		return "Select";
	}

	/**
	 * 标签实现中所应该生成的明确的HTML标签(SELECT标签)
	 */
	protected void renderInnerHtml(StringBuffer sb, KeyedCollection kColl) {
		
		sb.append("<select");
		
		this.writeAttribute(sb, "name", this.id);
		this.writeAttribute(sb, "size", this.size);
		String dataValue = this.getValue(kColl);
		this.writeAttribute(sb, "value", dataValue);

		if(this.multiple != null && this.multiple.trim().toUpperCase().equals("TRUE")){
			sb.append(" multiple ");
		}
		
		//设置select框的样式，缺省使用emp_field_select_select
		if (this.cssElementClass != null && this.cssElementClass.length() > 0) {
			this.writeAttribute(sb, "class", cssElementClass);
		}else{
			this.writeAttribute(sb, "class", "emp_field_select_select");
		}
		
		//在select选择框上添加基本的属性
		this.addDefaultAttributes(sb, kColl);
		
		this.writeAttribute(sb, "onblur", this.onblur);
		this.writeAttribute(sb, "onchange", this.onchange);
		this.writeAttribute(sb, "onfocus", this.onfocus);
		
		sb.append("><option value=''>").append(this.getResourceValue(this.defMsg)).append("</option>");
		
		IndexedCollection iColl = this.getDictMapCollection(this.dictname);
		if (iColl != null) {
			String language = getLanguage();// 具体当前页面所需要显示的语言(用于多语言显示的情况)
			try {
				for (int i = 0; i < iColl.size(); i++) {
					KeyedCollection record = (KeyedCollection) iColl.get(i);
					String enname = (String) record.getDataValue(EMPExtTagSupport.ATTR_ENNAME);
					String cnname = null;
					if (this.languageResouce) {
						try {
							cnname = (String) kColl.getDataValue(EMPExtTagSupport.ATTR_CNNAME
											+ "_" + language);
						} catch (Exception e) {
						}
					}
					if (cnname == null) {
						cnname = (String) record.getDataValue(EMPExtTagSupport.ATTR_CNNAME);
					}
					sb.append("<option");
					this.writeAttribute(sb, "value", enname);

					if (enname != null && enname.equals(dataValue)) {
						sb.append(" selected");
					}
					
					sb.append(">").append(cnname).append("</option>");
				}
			} catch (Exception e) {
				EMPLog.log("EMPExt", EMPLog.WARNING, 0, "生成下拉框内容出错!", e);
			}

		}
		sb.append("</select>");
		
		//生成一个隐藏的input框用于select需要只读时显示
		sb.append("<input readonly");
		this.writeAttribute(sb, "class", this.cssFakeInputClass);
		this.writeAttribute(sb, "value", this.getDecoratedValue(dataValue, this.dictname, this.languageResouce));
		sb.append(" />");
	}
	
	/**
	 * 标签实现中所拥有的特殊的属性
	 */
	protected void renderOtherAttribute(StringBuffer buffer, KeyedCollection kColl, boolean flat) {
		this.writeAttribute(buffer, "defMsg", this.defMsg);
	}
	
	
	public Object clone() {
		EMPExtSelect tag = new EMPExtSelect();
		
		//复制缺省的属性
		this.cloneDafaultAttributes(tag);
		
		tag.onfocus = this.onfocus;
		tag.onblur = this.onblur;
		tag.onchange = this.onchange;
		tag.size = this.size;
		tag.defMsg = this.defMsg;
		tag.cssFakeInputClass = this.cssFakeInputClass;
		return tag;		
	}

	public String getDefMsg() {
		return defMsg;
	}

	public void setDefMsg(String defMsg) {
		this.defMsg = defMsg;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCssFakeInputClass() {
		return cssFakeInputClass;
	}

	public void setCssFakeInputClass(String cssFakeInputClass) {
		this.cssFakeInputClass = cssFakeInputClass;
	}
	
	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
}
