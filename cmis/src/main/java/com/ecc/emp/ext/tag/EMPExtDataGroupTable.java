package com.ecc.emp.ext.tag;


import javax.servlet.jsp.JspException;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.ext.tag.field.EMPExtFieldBase;
import com.ecc.emp.ext.tag.page.EMPExtPageObjects;

public class EMPExtDataGroupTable extends EMPExtDataTable {

	private static final long serialVersionUID = 1L;
	
	
	private String icollName = null;
	
	private String cssClass = "emp_table";
	
	private String selectType = "1";	//NONE,SELECT,MULTI-SELECT
	
	private boolean needTableTitle = true;
	
	private boolean pageMode = true;
	
	private boolean editable = false;
	
	private String statisticType = null;
	
	private String url;
	
	private String groupColumn = null;
	
	/*
	 * 下面的属性表示的是分页的按钮显示字符串在多语言配置中的ID
	 */
	private String ext_firstPage="ext_firstPage";//首页
	private String ext_prePage="ext_prePage";//上一页
	private String ext_Page="ext_Page";//页
	private String ext_gong="ext_gong";//共
	private String ext_nextPage="ext_nextPage";//下一页
	private String ext_lostPage="ext_lostPage";//尾页
	private String ext_perPage="ext_perPage";//每页
	private String ext_forwardPage="ext_forwardPage";//跳转
	private String ext_row="ext_row";//行
	private String ext_thePage="ext_thePage";//第
	
	public int doEndTag() throws JspException {

		// 将数据域添加到pageObjs定义中
		EMPExtPageObjects pageObjs = (EMPExtPageObjects) pageContext
				.getRequest().getAttribute(EMPExtPageObjects.PARAM_NAME);
		if (pageObjs != null)
			pageObjs.addDataTable(this.icollName);

		StringBuffer sb = new StringBuffer();
		sb.append("<div id='emp_table_").append(this.icollName).append("_div'>\n");
		
		/* TABLE */
		sb.append("<table id='emp_table_").append(this.icollName).append("_table'");
		IndexedCollection iColl = null;
		try {
			iColl = (IndexedCollection) getDataElement(icollName);
		} catch (Exception e) {}
		if (iColl == null) {
			this.writeAttribute(sb, "nodata", "true");
		}
		
		this.writeAttribute(sb, "tableType", "g");
		this.writeAttribute(sb, "class", this.cssClass);
		this.writeAttribute(sb, "pageMode", new Boolean(this.pageMode));
		this.writeAttribute(sb, "selectType", this.selectType);
		this.writeAttribute(sb, "needTableTitle", new Boolean(this.needTableTitle));
		this.writeAttribute(sb, "editable", new Boolean(this.editable));
		this.writeAttribute(sb, "statisticType", this.statisticType);
		this.writeAttribute(sb, "rendered", "false");
		
		String url = this.url;
		if (this.reqParams != null) {
			if (url.indexOf('?') != -1)
				url = url + "&" + reqParams;
			else
				url = url + "?" + reqParams;
		}
		url = this.parsePamaterStr(url);
		url = this.getGetURL(url);
		
		this.writeAttribute(sb, "url", url);

		sb.append(">\n");

		int colspans = 0;

		/* THEAD 表头标题 */
		if (this.needTableTitle) {
			sb.append("<thead>\n<tr");
			this.writeAttribute(sb, "class", this.cssTitleClass);
			sb.append(">");
		} else {
			sb.append("<thead style='display:none'>\n<tr>");
		}
		for (int i = 0; this.fields != null && i < this.fields.size(); i++) {
			EMPExtFieldBase column = (EMPExtFieldBase) this.fields.elementAt(i);
			sb.append("<th id='emp_table_").append(this.icollName);
			sb.append("_th_").append(column.getId()).append("'");
			
			this.writeAttribute(sb, "columnName", column.getId());
			boolean columnHidden = column.isHidden();
			this.writeAttribute(sb, "hidden", new Boolean(columnHidden));
			if (columnHidden) {
				sb.append(" style='display:none'");
			}
			sb.append(">").append(column.getTableHead()).append("</th>");
			colspans++;
		}
		sb.append("</tr>\n</thead>\n");

		/* TBODY-SAMPLE 数据域模板 */
		KeyedCollection sampleColl = null;
		if (iColl != null) {
			sampleColl = (KeyedCollection) iColl.getDataElement();
		}
		if (sampleColl == null) {
			sampleColl = new KeyedCollection();
		}
		sb.append("<tbody id='emp_table_").append(this.icollName).append("_tbodysample'");
		sb.append(" style='display:none'><tr>\n");

		for (int i = 0; fields != null && i < fields.size(); i++) {
			EMPExtFieldBase column = (EMPExtFieldBase) this.fields.elementAt(i);
			if (column.isHidden()) {
				sb.append("<td style='display:none'>");
			} else {
				sb.append("<td>");
			}
			column.renderFieldTag(sb, sampleColl, column.isFlat());
			sb.append("</td>");
		}
		sb.append("</tr></tbody>\n");

		/* TBODY-MAIN 列表数据 */
		sb.append("<tbody id='emp_table_").append(this.icollName).append("_tbodymain'");
		sb.append(" style='display:none'>\n");
		String _tmp_last_value = "[START_TAG]";
		
		String gcol = this.getGroupColumn();
		int _gcol = 0; 
		try{
		  if(gcol != null && !gcol.trim().equals("")){
		     _gcol = Integer.parseInt(gcol);
		  }else{
			  _gcol = -1;
		  }
		}catch(NumberFormatException ex){
			_gcol = -1;
		}
		if (iColl != null ){
			int goupId = 0;
					
			
			for (int i = 0; i < iColl.size(); i++) {
				KeyedCollection kColl = (KeyedCollection) iColl.getElementAt(i);
				////////////////////
				if( _gcol >= 0){
					EMPExtFieldBase grupcolumn = (EMPExtFieldBase) this.fields.elementAt(_gcol-1);
					try {
						
						String _gValue = (String)kColl.getDataValue(grupcolumn.getId());
						//sb.append(_gValue);
						if(_tmp_last_value != null && (!_tmp_last_value.trim().equals(_gValue)
								||i==0)){
							sb.append("<tr dtype='1' bgcolor='#ffffff'><td style='cursor:hand;color=#325D87' height=25 onclick=\"" + this.icollName+ "._obj.groupExpand('" + this.icollName + i + "')\" colspan=").append(fields.size()).append("><b>");
							sb.append("&nbsp;<img align='ABSMIDDLE' id='img_" + this.icollName + i + "' src='images/minimize.gif'>");
							
							
							sb.append("&nbsp;&nbsp;").append(grupcolumn.getLabel()).append(":&nbsp;&nbsp;");
					 
							
							grupcolumn.renderFieldTag(sb, kColl, true,true);
							sb.append("<b></td></tr>\n");
							sb.append("<tr><td colspan=").append(fields.size()).append("></td></tr>\n");
							goupId = i;
						} 
						
						_tmp_last_value = _gValue;
						
					} catch (ObjectNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				////////////////////
				sb.append("<tr id='tr_" + this.icollName + goupId + "_" + i + "'>\n");
				for (int k = 0; fields != null && k < fields.size(); k++) {
					EMPExtFieldBase column = (EMPExtFieldBase) this.fields.elementAt(k);
					if (column.isHidden()) {
						sb.append("<td style='display:none'");
					} else {
						sb.append("<td");
					}
					this.writeAttribute(sb, "class", column.getCssTDClass());
					sb.append(">");
					column.renderFieldTag(sb, kColl, column.isFlat());
					sb.append("</td>");
				}
				sb.append("</tr>\n");
			}
		}
		sb.append("</tbody>\n");

		/* TBODY-STAT 统计 */
		sb.append("<tbody id='emp_table_").append(this.icollName).append("_tbodystat'");
		sb.append(" style='display:none'>\n<tr>");
		for (int i = 0; fields != null && i < fields.size(); i++) {
			EMPExtFieldBase column = (EMPExtFieldBase) this.fields.elementAt(i);
			if (column.isHidden()) {
				sb.append("<td style='display:none'>");
			} else {
				sb.append("<td>");
			}

			// TODO 未实现统计

			sb.append("</td>");
		}
		sb.append("</tr>\n</tbody>\n");

		/* TBODY-MESSAGE 提示信息 */
		sb.append("<tbody id='emp_table_").append(this.icollName).append("_tbodymsg'");
		sb.append(" style='display:none'>\n");
		sb.append("<tr><td colspan=" + colspans + "><span></span></td></tr>");
		sb.append("</tbody>\n");

		/* TBODY-PAGEQUERY 分页请求功能栏 */
		sb.append("<tbody id='emp_table_").append(this.icollName).append("_tbodypq2'");
		sb.append(" style='display:block'>\n");
		sb.append("<tr><td colspan=" + colspans + ">");
		sb.append(this.getPageQueryHTML()).append("</td></tr>");
		sb.append("</tbody>\n");

		/* TABLE END */
		sb.append("</table>\n</div>\n");

		outputContent(sb.toString());
		
		return SKIP_BODY;
	}
	
	public String getPageQueryHTML() {
		StringBuffer sb = new StringBuffer();
		if(!this.selectType.equals("2")) ;
		else{
			sb.append("<div align='left'><button class='table_btn' id='emp_pq_selall' onclick='"+this.icollName+"._obj.selectAll()'>全选</button> ");
			sb.append("<button class='table_btn'id='emp_pq_selall' onclick='"+this.icollName+"._obj.clearAll()'>取消选中</button></div>");
		}
			
		if (!this.pageMode) return sb.toString();
		
		String pageSize = this.getDataValue("pageInfo.maxLine");
		if(pageSize == null)
			pageSize = "";
		
		String pageIdx = this.getDataValue("pageInfo.currentPage");
		if(pageIdx == null)
			pageIdx = "";
		
		String recordSize = this.getDataValue("pageInfo.recordSize");
		if(recordSize == null)
			recordSize = "";
		
		
		sb.append("<div style='float:right;'><button class='table_btn'id='emp_pq_first'>"+getPageInfo(ext_firstPage,"首页")+"</button>");
		sb.append("<button class='table_btn'id='emp_pq_previous'>"+getPageInfo(ext_prePage,"上一页")+"</button>");
		sb.append(getPageInfo(ext_thePage,"第")+"<span id='emp_pq_currentPage'>").append(pageIdx).append("</span>"+getPageInfo(ext_Page,"页"));
		if(this.needRecordSize){
			sb.append("，"+getPageInfo(ext_gong,"共")+"<span id='emp_pq_totalPage'>?</span>"+getPageInfo(ext_Page,"页"));
			sb.append("<span id='emp_pq_recordSize' style='display:none'>").append(recordSize).append("</span>");
			sb.append("<button class='table_btn'id='emp_pq_next'>"+getPageInfo(ext_nextPage,"下一页")+"</button>");
			sb.append("<button class='table_btn'id='emp_pq_last'>"+getPageInfo(ext_lostPage,"尾页")+"</button>");
		}else{
			sb.append("<button class='table_btn'id='emp_pq_next'>"+getPageInfo(ext_nextPage,"下一页")+"</button>");
		}
		
		sb.append("<input id='emp_pq_jumpInput'/><button class='table_btn'id='emp_pq_jumpButton'>"+getPageInfo(ext_forwardPage,"跳转")+"</button>");
		sb.append(getPageInfo(ext_perPage,"每页")+"<input id='emp_pq_maxLine' value='").append(pageSize).append("'/>"+getPageInfo(ext_row,"行")+"</div>");
		
		return sb.toString();
	}
	
	/**
	 * defaultstr:缺省描述，当找不到对应的国际化描述时使用该缺省描述
	 */
	private String getPageInfo(String resourceId, String defaultstr) {
		if (resourceId == null)
			return defaultstr;
		String str = this.getResourceValue(resourceId);
		if (str == null || str.equals(resourceId))
			return defaultstr;
		return str;
	}
	
	public void setIcollName(String icollName) {
		this.icollName = icollName;
	}

	public void setStatisticType(String statisticType) {
		this.statisticType = statisticType;
	}

	public void addCMISDataField(EMPExtFieldBase tag) {
		this.fields.addElement(tag);
	}

	public void setUrl(String url) {
		this.url = url;
	}



	public boolean getNeedTableTitle() {
		return needTableTitle;
	}

	public void setNeedTableTitle(boolean needTableTitle) {
		this.needTableTitle = needTableTitle;
	}

	public boolean getPageMode() {
		return pageMode;
	}

	public void setPageMode(boolean pageMode) {
		this.pageMode = pageMode;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}


	public String getIcollName() {
		return icollName;
	}

	public String getStatisticType() {
		return statisticType;
	}

	public String getUrl() {
		return url;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}


	public String getGroupColumn() {
		return groupColumn;
	}


	public void setGroupColumn(String groupColumn) {
		this.groupColumn = groupColumn;
	}

	
}