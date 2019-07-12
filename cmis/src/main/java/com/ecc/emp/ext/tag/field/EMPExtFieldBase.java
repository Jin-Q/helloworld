package com.ecc.emp.ext.tag.field;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.datatype.EMPDataType;
import com.ecc.emp.datatype.EMPDataTypeDef;
import com.ecc.emp.ext.tag.EMPExtDataTable;
import com.ecc.emp.ext.tag.EMPExtGridLayout;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.ext.tag.page.EMPExtPageObjects;

public abstract class EMPExtFieldBase extends EMPExtTagSupport {

	private static final long serialVersionUID = 1L;

	protected Tag parent = null;

	/**
	 * 该字段的惟一标识，必设
	 */
	protected String id = null;
	
	/**
	 * 该字段的显示名称，可使用多语言进行处理，必设
	 */
	protected String label = null;
	
	/**
	 * 用于显示的默认值
	 */
	protected String defvalue = ""; 
	
	/**
	 * 用于修饰的字典名称
	 */
	protected String dictname = null;
	
	/**
	 * 输入校验失败的错误信息数据域名称
	 */
	protected String fieldErrorDataName = null;
	
	/**
	 * 整个标签(span)的样式名称(通常用于定义其中特定元素样式)，缺省值是emp_field
	 */
	protected String cssClass = "emp_field"; 
	
	/**
	 * 必输标志符号的样式(即*号的样式)，缺省值是emp_field_required
	 */
	protected String cssRequiredClass = "emp_field_required";
	
	/**
	 * 校验出错信息的样式，缺省值是emp_field_error
	 */
	protected String cssErrorClass = "emp_field_error";
	
	/**
	 * 标签的显示名称的样式(只适用于布局组件中)，缺省值是emp_field_label
	 */
	protected String cssLabelClass = "emp_field_label";
	
	/**
	 * 标签只以文本方式显示数值时的样式，缺省值是emp_field_flat
	 */
	protected String cssFlatClass = "emp_field_flat";
	
	/**
	 * 标签内具体元素的样式，没有缺省值，由各个具体的标签决定
	 */
	protected String cssElementClass = null;
	
	/**
	 * 标签的数据部分在布局组件和表格组件中的td属性，缺省值是emp_field_td
	 */
	protected String cssTDClass = "emp_field_td";
	
	/**
	 * 标签的数据部分在布局组件和表格组件中的td属性，dataType是数值类型则居右
	 */
	protected String cssTDClassNumber = "emp_field_td_number";
	
	/**
	 * HTML标准属性，tab键遍历的顺序
	 */
	protected String tabindex = null;
	
	/**
	 * HTML标准属性，快捷键访问方式
	 */
	protected String accesskey = null;
	
	/**
	 * 是否需要按照多语言的规则显示，该属性由具体的标签实现决定，缺省不需要
	 */
	protected boolean languageResouce = false;
	
	/**
	 * tooltip帮助
	 */
	protected String help = null; 
	
	/**
	 * 是否是必输项
	 */
	protected Boolean required = null; 
	
	/**
	 * 是否隐藏
	 */
	protected Boolean hidden = null;
	
	/**
	 * 是否无效状态
	 */
	protected Boolean disabled = null;
	
	/**
	 * 是否只以文本方式显示数值
	 */
	protected Boolean flat = null;
	
	/**
	 * 是否只读
	 */
	protected Boolean readonly = null;
	
	/**
	 * 所使用的数据类型
	 */
	protected String dataType = null;
	
	/**
	 * 在表格组件中表示是否是统计列(该属性尚未使用)
	 */
	protected Boolean statistic = null;
	
	/**
	 * 所占的列数，只在布局组件中使用，在表格组件中不起作用
	 */
	protected String colSpan = "1"; 
	
	/**
	 * 当前标签是否只针对自身的span进行控制(不控制周边的td、label等)
	 */
	protected boolean onlyControlElement = false;
	
	
	
	//以下是需要添加的基本事件
	/**
	 * HTML标准事件，单击
	 */
	protected String onclick = null;
	
	/**
	 * HTML标准事件，双击
	 */
	protected String ondblclick = null;
	
	/**
	 * HTML标准事件，鼠标按下
	 */
	protected String onmousedown = null;
	
	/**
	 * HTML标准事件，鼠标释放
	 */
	protected String onmouseup = null;
	
	/**
	 * HTML标准事件，鼠标移过
	 */
	protected String onmouseover = null;
	
	/**
	 * HTML标准事件，鼠标移动
	 */
	protected String onmousemove = null;
	
	/**
	 * HTML标准事件，鼠标移走
	 */
	protected String onmouseout = null;
	
	/**
	 * HTML标准事件，输入字符
	 */
	protected String onkeypress = null;
	
	/**
	 * HTML标准事件，按键按下
	 */
	protected String onkeyup = null;
	
	/**
	 * HTML标准事件，按键释放
	 */
	protected String onkeydown = null;
	
	/**
	 * 注册到页面中的JS对象类型(每个Tag都必须指定一个JS对象的类型)
	 * @return
	 */
	abstract protected String getType();
	
	/**
	 * clone标签对象所调用的方式
	 */
	abstract public Object clone();
	/**
	 * 具体的标签实现中所应该生成的明确的HTML标签(如input、textarea等等)。
	 * @param sb
	 * @param kColl：列表数据中的一条记录。当kColl为空时，表示为普通的数据
	 */
	abstract protected void renderInnerHtml(StringBuffer sb, KeyedCollection kColl);
	
	/**
	 * 具体的标签实现中所拥有的特殊的属性。
	 * @param buffer
	 * @param kColl
	 * @param flat
	 */
	abstract protected void renderOtherAttribute(StringBuffer buffer, KeyedCollection kColl, boolean flat);
		
	/**
	 * 进入标签时所执行的方法(由JSPTag的API决定)。
	 */
	public int doStartTag() throws JspException {

		if (!this.judgeReadPermission(this.id)) {
			return SKIP_BODY;
		}

		boolean flat = false;
		
		if (this.parent != null && this.parent instanceof EMPExtDataTable) {
			if (!((EMPExtDataTable) this.parent).isEditable() && this.flat == null) {
				flat = true; // 在Table中，默认是只以文本方式显示
			}
			EMPExtFieldBase newfield = (EMPExtFieldBase) this.clone();
			newfield.setFlat(flat);
			newfield.setOnlyControlElement(false);//使用表格组件，则生成的标签对象是完备的对象
			((EMPExtDataTable) this.parent).addCMISDataField(newfield);
		} else if (this.parent != null
				&& this.parent instanceof EMPExtGridLayout) {
			EMPExtFieldBase newfield = (EMPExtFieldBase) this.clone();
			newfield.setOnlyControlElement(false);//使用布局组件，则生成的标签对象是完备的对象
			((EMPExtGridLayout) this.parent).addCMISDataField(newfield);
			
			// 将数据域添加到pageObjs定义中
			EMPExtPageObjects pageObjs = (EMPExtPageObjects) pageContext
					.getRequest().getAttribute(EMPExtPageObjects.PARAM_NAME);
			if (pageObjs != null) {
				pageObjs.addDataField(this.id);
			}
		} else {
			if (this.flat != null) {
				flat = this.flat.booleanValue();
			}
			// 将数据域添加到pageObjs定义中
			EMPExtPageObjects pageObjs = (EMPExtPageObjects) pageContext
					.getRequest().getAttribute(EMPExtPageObjects.PARAM_NAME);
			if (pageObjs != null) {
				pageObjs.addDataField(this.id);
			}
			StringBuffer sb = new StringBuffer();
			this.renderFieldTag(sb, null, flat);
			this.outputContent(sb.toString());
		}
		
		return SKIP_BODY;
	}
	public void renderFieldTag(StringBuffer sb, KeyedCollection kColl, boolean flat){
		this.renderFieldTag(sb, kColl, flat,false);
	}
	/**
	 * 生成完整的EMPExt标签所有的HTML内容。
	 * @param sb
	 * @param kColl：列表数据中的一条记录。当kColl为空时，表示为普通的数据
	 * @param flat：当前是否只以文本方式显示
	 * @param groupTitle: 是否用作列表分组TITLE
	 */
	public void renderFieldTag(StringBuffer sb, KeyedCollection kColl, boolean flat, boolean groupTitle){
		
		//如果定义了dictname，则将dictname注释到page.objectsDefine页面对象中
		EMPExtPageObjects pageObjs = (EMPExtPageObjects) pageContext.getRequest()
				.getAttribute(EMPExtPageObjects.PARAM_NAME);
		if (pageObjs!=null && this.dictname!=null && !pageObjs.hasDataDic(this.dictname)) {
			IndexedCollection dicIColl = this.getDictMapCollection(this.dictname);
			if (dicIColl != null)
				pageObjs.addDataDic(this.dictname,dicIColl,"cn");
		}
	
		sb.append("<span");
		this.writeAttribute(sb, "id", "emp_field_"+this.id);
		//在span标签中添加：title属性 以供data.js中的title属性使用 add at 2010年11月18日11:38:00
		this.writeAttribute(sb, "title", this.label); 
		
		//this.writeAttribute(sb, "title", this.getLabel());
		if (flat) {
			this.writeAttribute(sb, "type", "Base");
		} else {
			this.writeAttribute(sb, "type", this.getType());
		}
		this.writeAttribute(sb, "tooltip", this.getHelp());
		this.writeAttribute(sb, "class", this.cssClass);
		this.writeAttribute(sb, "cssErrorClass", this.cssErrorClass);
		this.writeAttribute(sb, "cssRequiredClass", this.cssRequiredClass);
		
		//sb.append(this.getClassStr("emp_field"));
		this.writeAttribute(sb, "required", this.required);
		if(groupTitle){
			this.writeAttribute(sb, "hidden", "false");///用作列表分组的TITLE
		}else{
		    this.writeAttribute(sb, "hidden", hidden);
		}
		this.writeAttribute(sb, "colSpan", this.colSpan);
		this.writeAttribute(sb, "disabled", this.disabled);
		this.writeAttribute(sb, "readonly", new Boolean(this.isReadonly()));
		this.writeAttribute(sb, "onlyControlElement", new Boolean(this.onlyControlElement));
		this.writeAttribute(sb, "dictname", this.dictname);
		if(kColl != null)//statistic属性只对表格起作用
			this.writeAttribute(sb, "statistic", this.statistic);

		if (this.fieldErrorDataName != null) {
			String fieldErrorValue = this.getDataValue(EMPConstance.ERROR_MSG_KCOLL + "."
							+ this.fieldErrorDataName);
			if (fieldErrorValue != null)
				fieldErrorValue = this.getResourceValue(fieldErrorValue);
			this.writeAttribute(sb, "fieldErrorValue", fieldErrorValue);
		}
		
		//对dataType进行处理，生成五个属性：dataType、validateJS、convertorJS、rangeErrorMsg、formatErrorMsg
		if(this.dataType != null){
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
	    	Map dataTypeDefMap = (Map)request.getAttribute( EMPConstance.ATTR_DATA_TYPE);
	    	EMPDataType empDataType = (EMPDataType)dataTypeDefMap.get( this.dataType );
	    	if(empDataType != null){
	    		EMPDataTypeDef typeDef = empDataType.getDataTypeDef();
	    		this.writeAttribute(sb, "dataType", this.dataType);
				this.writeAttribute(sb, "validateJS",this.parseScriptStr(this.getLabel(), typeDef.getValidateJS().getFunction(), empDataType));
				this.writeAttribute(sb, "convertorJS",this.parseScriptStr(this.getLabel(), typeDef.getConvertorJS().getFunction(), empDataType));
				
				//得到rangeErrorMsg和formatErrorMsg作为标签的两个属性
		    	String rangeErrorMsg = "rangeErrorInfo";
				String formatErrorMsg = "formatErrorInfo";
				
				//根据具体的错误信息配置得到多语言定义中的相关显示信息
				if(empDataType.getRangeErrorMsg() != null)
					rangeErrorMsg = empDataType.getRangeErrorMsg();
				if(empDataType.getFormatErrorMsg() != null)
					formatErrorMsg = empDataType.getFormatErrorMsg();
				rangeErrorMsg = this.getResourceValue(rangeErrorMsg);
				formatErrorMsg = this.getResourceValue(formatErrorMsg);
				
				//定义缺省的错误提示信息
				if("rangeErrorInfo".equals(rangeErrorMsg))
					rangeErrorMsg = "please input [$fieldName;] between [$max;] and [$min;]!";
				if( "formatErrorInfo".equals(formatErrorMsg))
					formatErrorMsg = "input field [$fieldName;] format error,the right format as: [$format;]!";
				
				rangeErrorMsg = parseScriptStr(this.getLabel() ,rangeErrorMsg, empDataType);
				formatErrorMsg = parseScriptStr(this.getLabel() ,formatErrorMsg, empDataType);
				
				this.writeAttribute(sb, "rangeErrorMsg", rangeErrorMsg);
				this.writeAttribute(sb, "formatErrorMsg", formatErrorMsg);
	    	}
		}
	//	sb.append(str("tooltip",this.help));
		
		this.writeAttribute(sb, "value", this.getValue(kColl));
		
		sb.append(" ");
		this.renderOtherAttribute(sb, kColl, flat);
		
		this.writeAttribute(sb, "rendered", new Boolean(false));
		sb.append(">");
		
		if (flat) {
			this.renderFlatInnerHtml(sb, kColl);
		} else {
			this.renderInnerHtml(sb, kColl);
		}
		
		sb.append("</span>\n");
	}
 
	/**
	 * 在只以文本方式显示的情况下所要显示的内容。
	 * @param sb
	 * @param kColl：列表数据中的一条记录。当kColl为空时，表示为普通的数据
	 */
	protected void renderFlatInnerHtml(StringBuffer sb, KeyedCollection kColl){
		sb.append("<span");
		
		if(this.dataType!=null){
			//dataType是数值类型则居右显示  add by JackYu at2011年4月12日14:02:38
			if(dataType.equals("Currency")||dataType.equals("Rate")||dataType.equals("Rate4Month")
					||dataType.equals("Percent")||dataType.equals("Percent2")||dataType.equals("Permille")
					||dataType.equals("Short")||dataType.equals("Int")||dataType.equals("Long")
					||dataType.equals("Float")||dataType.equals("Double")||dataType.equals("Decimal5")
					||dataType.equals("DecimalA")){
				
				this.writeAttribute(sb, "class", this.cssTDClassNumber);
			
			}
		}else
			this.writeAttribute(sb, "class", this.cssFlatClass);
		
		sb.append(">");
		String decoratedValue = this.getDecoratedValue(this.getValue(kColl),this.dictname, this.languageResouce);
		sb.append(decoratedValue);
		sb.append("</span>");
	}
	
	/**
	 * 添加缺省的事件定义。
	 * @param sb
	 * @param kColl
	 */
	protected void addDefaultAttributes(StringBuffer sb, KeyedCollection kColl){
		this.writeAttribute(sb, "tabindex", this.tabindex);
		this.writeAttribute(sb, "accesskey", this.accesskey);
		
		this.writeAttribute(sb, "onclick", this.onclick);
		this.writeAttribute(sb, "ondblclick", this.ondblclick);
		this.writeAttribute(sb, "onkeydown", this.onkeydown);
		this.writeAttribute(sb, "onkeypress", this.onkeypress);
		this.writeAttribute(sb, "onkeyup", this.onkeyup);
		this.writeAttribute(sb, "onmousedown", this.onmousedown);
		this.writeAttribute(sb, "onmousemove", this.onmousemove);
		this.writeAttribute(sb, "onmouseout", this.onmouseout);
		this.writeAttribute(sb, "onmouseover", this.onmouseover);
		this.writeAttribute(sb, "onmouseup", this.onmouseup);
	}
	
	/**
	 * 取得当前标签所对应的数据的值。
	 * @param kColl：列表数据中的一条记录。当kColl为空时，表示为普通的数据
	 * @return
	 * 
	 * <p>
	 * 取值的方式是：<br>
	 * 当kColl不为空时，则从kColl中取值<br>
	 * 当kColl为空时，则从Context中取值，若Context中无此数据，则从request中取得<br>
	 * 如果取值都为空，则使用defvalue<br>
	 *
	 */
	protected String getValue(KeyedCollection kColl) {
		String value = null;
		if (kColl != null) {
			try {
				Object obj = kColl.getDataValue(this.id);
				if (obj != null)
					value = obj.toString();
			} catch (Exception e) {
			}
		} else {
			value = this.getDataValue(this.id);
		}

		if (value == null) {
			value = this.getDefvalue();
		}
		return value;
	}
	
	public String getTableHead(){
		return this.getLabel();
	}
	
	protected void cloneDafaultAttributes(EMPExtFieldBase target) {
		target.label = this.label;
		target.id = this.id;
		target.defvalue = this.defvalue;
		target.dictname = this.dictname;
		target.cssClass = this.cssClass;
		target.cssElementClass = this.cssElementClass;
		target.cssErrorClass = this.cssErrorClass;
		target.cssLabelClass = this.cssLabelClass;
		target.cssRequiredClass = this.cssRequiredClass;
		target.cssTDClass = this.cssTDClass;
		target.cssFlatClass	= this.cssFlatClass;
		target.help = this.help;
		target.required = this.required;
		target.hidden = this.hidden;
		target.readonly = this.readonly;
		target.flat = this.flat;
		target.disabled = this.disabled;
		target.colSpan = this.colSpan;
		target.fieldErrorDataName = this.fieldErrorDataName;
		target.onlyControlElement = this.onlyControlElement;
		target.parent = this.parent;
		target.dataType = this.dataType;
		target.statistic = this.statistic;
		target.languageResouce = this.languageResouce;
		target.tabindex = this.tabindex;
		target.accesskey = this.accesskey;
		
		target.onclick = this.onclick;
		target.ondblclick = this.ondblclick;
		target.onkeydown = this.onkeydown;
		target.onkeypress = this.onkeypress;
		target.onkeyup = this.onkeyup;
		target.onmousedown = this.onmousedown;
		target.onmousemove = this.onmousemove;
		target.onmouseout = this.onmouseout;
		target.onmouseover = this.onmouseover;
		target.onmouseup = this.onmouseup;
		
		target.setPageContext(this.pageContext);
		
	}
	

	/**
	 * 解析Script调用，以及错误提示的字符串，允许以$param;的方式引入DataType定义中的参数
	 * @param srcStr
	 * @param dataType
	 * @param fieldLabel
	 * @return
	 */
	protected String parseScriptStr(String fieldLabel, String funcStr, EMPDataType dataType)
	{
		int idx = 0;
		int idx2;
		
		StringBuffer dstStr = new StringBuffer();
		String tmp = funcStr;
		while(true)
		{
			idx = tmp.indexOf('$');
			idx2 = tmp.indexOf(';', idx);
			
			if( idx == -1 || idx2 == -1 )
			{
				dstStr.append( tmp );
				break;
			}
			String paramName = tmp.substring(idx+1, idx2);
			if( paramName.equals("fieldName"))
			{
				dstStr.append( tmp.substring(0, idx));
				dstStr.append( fieldLabel );
			}
			else
			{
				try{
					dstStr.append( tmp.substring(0, idx));
					String methodName = "get" + paramName.substring(0, 1).toUpperCase() + paramName.substring( 1 );
					Class aClass = dataType.getClass();
					Method method = aClass.getMethod(methodName, null );
					Object valueObj = method.invoke(dataType, null );
					String value = valueObj.toString();
					dstStr.append( value );
					
				}catch(Exception e)
				{
//					System.out.println( e );
//					dstStr.append( "''");
				}
			}
			tmp = tmp.substring(idx2 + 1);
		}
		
		String retValue = dstStr.toString();
		return retValue;
	}
	
	//以下为getter和setter方法
	
	public void setParent(Tag tag) {
		this.parent = tag;
	}
	
	public String getCnname() {
		return this.getLabel();
	}

	public void setCnname(String cnname) {
		this.label = cnname;
	}

	/**
	 * 取得defvalue(缺省值)。
	 * <p>
	 * 取缺省值的方式是：
	 * 如果defvalue是以$开头的字符串，则表示$后面的部分代表一个数据名称，取得该数据的值返回(如果Context中没有该数据，则从request中取得)
	 * 否则直接返回defvalue的值
	 * @return
	 */
	public String getDefvalue() {
		String st_return = "";
		String value = this.defvalue;
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
			st_return = this.defvalue;
		}
		return st_return;
	}
	
	/**
	 * 判断当前是否只读。
	 * <p>
	 * 判断方式是：
	 * 如果标签中定义了readonly属性，则返回readonly属性<br>
	 * 如果没有定义readonly属性，则通过写权限进行判断
	 * @return
	 */
	public boolean isReadonly() {
		if (this.readonly != null) {
			return readonly.booleanValue();
		} else {
			return (!this.judgeWritePermission(this.id));
		}
	}
	
	public void setDefvalue(String defvalue) {
		this.defvalue = defvalue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return this.getResourceValue(label);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getHelp() {
		return help;
	}

	public void setColSpan(String colSpan) {
		this.colSpan = colSpan;
	}

	public String getColSpan() {
		return colSpan;
	}

	public String getDictname() {
		return dictname;
	}

	public void setDictname(String dictname) {
		this.dictname = dictname;
	}
	
	public boolean isStatistic() {
		if(this.statistic == null)
			return false;
		return this.statistic.booleanValue();
	}

	public void setStatistic(boolean statistic) {
		this.statistic = new Boolean(statistic);
	}	
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public boolean isDisabled() {
		if(this.disabled == null)
			return false;
		return this.disabled.booleanValue();
	}

	public void setDisabled(boolean disabled) {
		this.disabled = new Boolean(disabled);
	}

	public boolean isFlat() {
		if(this.flat == null)
			return false;
		return this.flat.booleanValue();
	}

	public void setFlat(boolean flat) {
		this.flat = new Boolean(flat);
	}

	public boolean isHidden() {
		if(this.hidden == null)
			return false;
		return hidden.booleanValue();
	}

	public void setHidden(boolean hidden) {
		this.hidden = new Boolean(hidden);
	}

	public boolean isLanguageResouce() {
		return languageResouce;
	}

	public void setLanguageResouce(boolean languageResouce) {
		this.languageResouce = languageResouce;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOndblclick() {
		return ondblclick;
	}

	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public String getOnkeydown() {
		return onkeydown;
	}

	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	public String getOnkeypress() {
		return onkeypress;
	}

	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}

	public String getOnkeyup() {
		return onkeyup;
	}

	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	public String getOnmousedown() {
		return onmousedown;
	}

	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	public String getOnmousemove() {
		return onmousemove;
	}

	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}

	public String getOnmouseout() {
		return onmouseout;
	}

	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	public String getOnmouseover() {
		return onmouseover;
	}

	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	public String getOnmouseup() {
		return onmouseup;
	}

	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}
	
	public void setReadonly(boolean readonly) {
		this.readonly = new Boolean(readonly);
	}

	public boolean isRequired() {
		if(this.required == null)
			return false;
		return this.required.booleanValue();
	}

	public void setRequired(boolean required) {
		this.required = new Boolean(required);
	}

	public String getTabindex() {
		return tabindex;
	}

	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}

	public Tag getParent() {
		return parent;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getCssErrorClass() {
		return cssErrorClass;
	}

	public void setCssErrorClass(String cssErrorClass) {
		this.cssErrorClass = cssErrorClass;
	}

	public String getCssLabelClass() {
		return cssLabelClass;
	}

	public void setCssLabelClass(String cssLabelClass) {
		this.cssLabelClass = cssLabelClass;
	}

	public String getCssRequiredClass() {
		return cssRequiredClass;
	}

	public void setCssRequiredClass(String cssRequiredClass) {
		this.cssRequiredClass = cssRequiredClass;
	}

	public String getCssTDClass() {
		return cssTDClass;
	}

	public void setCssTDClass(String cssTDClass) {
		this.cssTDClass = cssTDClass;
	}

	public String getCssElementClass() {
		return cssElementClass;
	}

	public void setCssElementClass(String cssElementClass) {
		this.cssElementClass = cssElementClass;
	}

	public String getCssFlatClass() {
		return cssFlatClass;
	}

	public void setCssFlatClass(String cssFlatClass) {
		this.cssFlatClass = cssFlatClass;
	}

	public String getFieldErrorDataName() {
		return fieldErrorDataName;
	}

	public void setFieldErrorDataName(String fieldErrorDataName) {
		this.fieldErrorDataName = fieldErrorDataName;
	}

	public boolean isOnlyControlElement() {
		return onlyControlElement;
	}

	public void setOnlyControlElement(boolean onlyControlElement) {
		this.onlyControlElement = onlyControlElement;
	}
}
