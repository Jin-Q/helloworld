package com.ecc.emp.ext.tag.field;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.log.EMPLog;

/**
 * 查询标签：下拉选项
 * @author JackYu
 *
 */
public class EMPExtSearchSelect extends EMPExtSelect{

	private static final long serialVersionUID = 1L;
	

	@Override
	public Object clone() {
		EMPExtSearchSelect tag = new EMPExtSearchSelect();
		
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

	@Override
	protected String getType() {
		return "SearchSelect";
	}
	
	/**
	 * 标签实现中所应该生成的明确的HTML标签
	 * select(查询模式) select输入框),
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
		
		//下拉选框
		//设制下拉选框的属性
		this.setId(this.id+"searchType");
		this.setDictname("SEARCH_TYPE?enname not in(70)");
		this.onblur = "";
		this.onfocus = "";
		super.renderInnerHtml(sb, kColl);
		
		
		sb.append("&nbsp&nbsp");
		
		//查询框__下拉选项
		sb.append("<select");
		
		this.writeAttribute(sb, "name", t_id);
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
		
		this.writeAttribute(sb, "onblur", onblur);
		this.writeAttribute(sb, "onchange", onchange);
		this.writeAttribute(sb, "onfocus",onfocus);
		
		sb.append("><option value=''>").append(this.getResourceValue(this.defMsg)).append("</option>");
		
		IndexedCollection iColl = this.getDictMapCollection(t_dicName);
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
		this.writeAttribute(sb, "value", this.getDecoratedValue(dataValue, t_dicName, this.languageResouce));
		sb.append(" />");
	  }
}
