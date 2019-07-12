package com.yucheng.cmis.biz01line.fnc.taglib;


import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.datatype.EMPDataType;
import com.ecc.emp.datatype.EMPDataTypeDef;
import com.ecc.emp.ext.tag.field.EMPExtFieldBase;
import com.ecc.emp.ext.tag.page.EMPExtPageObjects;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.FNCFactory;


public class EMPExtRptTag extends EMPExtFieldBase {

	private static final long serialVersionUID = 1L;
	
	protected String id;
	
	protected String cusComRptId;

	//protected String dataType = "Decimal";
	//protected String dataType = "Double";\
	//设置为 可负的金额类型
//	protected String dataType = "NCurrency";
	protected String dataType = "Currency";

	protected String dataTypeConfig;

	protected String identStr = "&nbsp;&nbsp";
	
	protected String editFlag;
	
	public int doStartTag() throws JspException {

		FncStatBase cusComRpt = null;//当前报表所展示的具体用户信息
		FncConfStyles fcs = null;//缓存中的标签配置信息
		FncConfStyles fcs_data = null;//当前页面需要显示的数据对象
		List dataList = null;
		String fnc_conf_typ = null;   //报表类型
		String fuc_style_id = null;
		String state_flg = null;
		String edit_flag = null;
		try {
			//FNCFactory fncFactory = null;
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			
			Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
			if (context != null){
				cusComRpt = (FncStatBase)context.getDataValue(cusComRptId);
				//fncFactory = (FNCFactory) context.getService(CMISConstance.ATTR_RPTSERVICE);
				fcs_data = (FncConfStyles)context.getDataValue(this.id);
				edit_flag = (String)context.getDataValue(editFlag);
				if(fcs_data != null){
					dataList = fcs_data.getItems();
					fnc_conf_typ = fcs_data.getFncConfTyp();
					if(fnc_conf_typ==null){
						EMPLog.log("RPTTag",EMPLog.ERROR,0,"当前页面取报表类型数据！");
						return EVAL_BODY_INCLUDE;
					}
					
					//标签类 是一个实例，所以这个地方必须每次赋值一次。对于财务指标表 用 百分率显示
//					this.dataType = "NCurrency";
					this.dataType = "Currency";
					if("01".equals(fnc_conf_typ)){
						fuc_style_id = cusComRpt.getStatBsStyleId();    //01:资产负债表
					//	this.dataType="CurrencySm";
					}else if("02".equals(fnc_conf_typ)){
						fuc_style_id = cusComRpt.getStatPlStyleId();    //02:损益表
					//	this.dataType="CurrencySm";
//						this.dataType = "Currency";
					}else if("03".equals(fnc_conf_typ)){
						fuc_style_id = cusComRpt.getStatCfStyleId();    //03:现金流量
						//this.dataType="CurrencySm";
//						this.dataType = "Currency";
					}else if("04".equals(fnc_conf_typ)){
						fuc_style_id = cusComRpt.getStatFiStyleId();    //04:财务指标
//						this.dataType = "Percent2";
//						this.dataType = "Currency";
					}else if("05".equals(fnc_conf_typ)){
						fuc_style_id = cusComRpt.getStatSoeStyleId();    //05.所有者权益变动表
					}else if("06".equals(fnc_conf_typ)){
						fuc_style_id = cusComRpt.getStatSlStyleId();    //06财务简表
						//this.dataType="CurrencySm";
					}
				}else{
					EMPLog.log("RPTTag",EMPLog.ERROR,0,"当前页面取不到相应的带数据的报表信息！");
					return EVAL_BODY_INCLUDE;
				}
				fcs = (FncConfStyles) FNCFactory.getFNCInstance(fuc_style_id);
			}else{
				EMPLog.log("RPTTag",EMPLog.ERROR,0,"当前页面取不到Context数据！");
				return EVAL_BODY_INCLUDE;
			}
		} catch (Exception e) {
			EMPLog.log("RPTTag",EMPLog.ERROR,0,"获取报表配置信息出错！",e);
		}
		if(fcs == null){
			EMPLog.log("RPTTag",EMPLog.ERROR,0,"当前页面取不到相应的报表配置信息！");
			return EVAL_BODY_INCLUDE;
		}
		
		int orderNum = 1; //当前行次
		int dataNum = 0; //当前需要显示的数据(数据列表中的行次)
		
		try {
			StringBuffer sb = new StringBuffer();

			//生成报表的标题
			sb.append(this.getRptTitleHtml(fcs.getFncConfDisName(), cusComRpt,cusComRpt.getStateFlg(),fnc_conf_typ));

			sb.append("<table class='emp_rpt_whole_table'><tr>");

			int cote = fcs.getFncConfCotes();//标签栏数(label+数据表示一列)
			int width = 100 / cote;

			//输入框列数(即数据列数，通常只有一列或两列)
			int dataCol = fcs.getFncConfDataCol();
			
			boolean flat = this.isRenderFlat(edit_flag);//当前是只显示数据还是可以输入
			boolean readonly = this.isRenderReadOnly(edit_flag);//若flat为false，判断当前的输入框是否只读
			
			int currentCote = 0; //当前标签栏数
			List itemList = fcs.getItems();
			
			for (int i = 0; i < itemList.size(); i++) {
				FncConfDefFormat itemFmt = (FncConfDefFormat) itemList.get(i);
				FncConfDefFormat itemFmtNx = null;
				if(i+1<itemList.size()){//当下一个存在时
//					if(!"3".equals(itemFmtNx.getFncItemEditTyp())){
//						itemFmtNx = (FncConfDefFormat) itemList.get(i+1);
//					}else{
//						if(i+1<itemList.size()){
//							
//						}
//					}
					for(int j=i+1;j<itemList.size();j++){
						FncConfDefFormat tmpItem = (FncConfDefFormat)itemList.get(j);
						if(tmpItem!=null&&!"3".equals(tmpItem.getFncItemEditTyp())){
							itemFmtNx = tmpItem;
							break;
						}
					}
				}
				
				int confCote = itemFmt.getFncConfCotes();
				//需要重新开始一列
				if (confCote == (currentCote + 1)) {
					sb.append("<td valign=top align=center width='").append(width).append("%'><table width=100%>");
					sb.append(this.getHeadHtml(dataCol,fnc_conf_typ));//每列的标题
					currentCote++;
				} else if (confCote > cote || confCote != currentCote) {//非法的配置信息，略过
					continue;
				}
				FncConfDefFormat dataObj = null;
				if (!"3".equals(itemFmt.getFncItemEditTyp())) {
					if(dataList != null && dataList.size() > dataNum)
						dataObj = (FncConfDefFormat) dataList.get(dataNum);
					dataNum ++;
				}
				int num = 0;
				if ("1".equals(itemFmt.getFncConfRowFlg())) {// 是否显示行次
					num = orderNum;
					orderNum++;
				}
				
				//具体标签栏生成的页面源码(即列表中的一行)
				sb.append(this.getItemHtml(itemFmt, dataObj, dataCol, num, flat, readonly, fnc_conf_typ,itemFmtNx));
				
				//已经到最后一个报表配置或者是下一个报表配置就不属于当前列时，需要将当前列的td标签封闭
				if (i == itemList.size() - 1) {
					sb.append("</table></td>");
				} else {
					FncConfDefFormat nextItemFmt = (FncConfDefFormat) itemList.get(i + 1);
					int nextItemCote = nextItemFmt.getFncConfCotes();
					if (nextItemCote > cote || nextItemCote != currentCote) {
						sb.append("</table></td>");
					}
				}
			}

			sb.append("</tr></table>");

			//生成报表的页脚
			sb.append(this.getRptFootHtml(cusComRpt));
			
			outputContent(sb.toString());
		} catch (Exception e) {
			EMPLog.log("RPTTag",EMPLog.ERROR,0,"生成报表显示页面出错！",e);
		}

		return EVAL_BODY_INCLUDE;
	}

	/**
	 * 生成财务报表页面的表头。
	 * @param title
	 * @param cusComRpt
	 * @return
	 */
	protected String getRptTitleHtml(String title, FncStatBase cusComRpt,String state_flg,String fnc_conf_typ) {

		StringBuffer results = new StringBuffer();
		String statFlg = this.getStatFlg(fnc_conf_typ,state_flg);
		
		/*
		 * 标题中   增加 一个  | 分割 如果存在  |  
		 * 例子  财务简表|单位：元
		 * 目前阶段不想在 配置表增加字段 按照这种方式实现
		 */
		String[] tmpTitle = title.split("\\|");
		
		String tmpTile = null;
		String tmpUnit = null;
		if(tmpTitle.length == 2){
			tmpTile = tmpTitle[0];
			tmpUnit = tmpTitle[1];
		}else{
			tmpTile = tmpTitle[0];
			tmpUnit = "单位：元";
		}
		
		
		
		
		results.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
		results.append("<tr><td colspan=\"4\" align=\"left\" valign=\"top\" class=\"emp_rpt_label_formula\">");
		results.append(statFlg);
		results.append("<tr><td colspan=\"4\" align=\"center\" class=\"rptTitle\">");
		results.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		results.append(tmpTile);
		switch (Integer.parseInt(cusComRpt.getStatPrdStyle())) {
		case 1:
			results.append("（月报）");
			break;
		case 2:
			results.append("（季报）");
			break;
		case 3:
			results.append("（半年报）");
			break;
		case 4:
			results.append("（年报）");
			break;
		}

		results.append("</td></tr>\n");
		results.append("<tr> <td height=\"28\" colspan=\"4\" align=\"center\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font size=\"3\">");
		results.append(cusComRpt.getStatPrd().substring(0, 4) + " 年　"
				+ cusComRpt.getStatPrd().substring(4) + "月");
		results.append("</font></td></tr>\n");
		results.append("<tr>\n");
		results.append("<td width=\"525\" height=\"26\" >编制单位："
				+ cusComRpt.getInputBrId()
				+ "</td>\n");
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy 年 MM 月 dd 日");
		results.append("<td width=\"275\">填报日期： "
				+ sdf.format(this.strToDate(cusComRpt.getInputDate())) + "</td>\n");
		results.append("<td width=\"157\" align=\"right\" >").append(tmpUnit).append("</td>\n");
		results.append("<td width=\"85\">&nbsp;</td>\n");
		results.append("</tr>\n");
		results.append("</table>\n");

		return results.toString();
	}

	/**
	 * 生成财务报表页面的页脚。
	 * @param cusComRpt
	 * @return
	 */
	private String getRptFootHtml(FncStatBase cusComRpt) {
		
		StringBuffer results = new StringBuffer("");
		results.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
		results.append("<tr>\n");
		results.append("<td height=\"26\" align=\"right\" >制表人：　"
				+ cusComRpt.getInputId() + "</td>\n");
		results.append("<td width=\"183\"></td>\n");
		results.append("</tr>\n");
		results.append("</table>\n");
		return results.toString();
	}

	/**
	 * 生成财务报表数据表格中的表头信息。
	 * @param dataCol
	 * @return
	 */
	protected String getHeadHtml(int dataCol,String fnc_conf_typ) {
		StringBuffer results = new StringBuffer("<tr class='emp_rpt_head'>");
		
		switch (dataCol) {
		case 1:
			results.append("<td width=\"45%\" align=\"center\">项目</td>\n");
			results.append("<td align=\"center\" nowrap>行次</td>\n");
			if("04".equals(fnc_conf_typ)){
				results.append("<td width=\"20%\" align=\"center\">指标值（比率:%）</td>\n");
				//results.append("<td width=\"20%\" align=\"center\">行业标准值</td>\n");
			}else{
				results.append("<td width=\"40%\" align=\"center\">金额</td>\n");
			}
			break;
		case 2:
			results.append("<td width=\"45%\" align=\"center\">项目</td>\n");
			results.append("<td align=\"center\" nowrap>行次</td>\n");
			if("02".equals(fnc_conf_typ)||"03".equals(fnc_conf_typ)){
				results.append("<td width=\"20%\" align=\"center\">上年同期数</td>\n");
				results.append("<td width=\"20%\" align=\"center\">本年累计数</td>");
			}else{
				results.append("<td width=\"20%\" align=\"center\">期初数</td>\n");
				results.append("<td width=\"20%\" align=\"center\">期末数</td>");
			}
			break;
		case 8:
			results.append("<td width=\"20%\" align=\"center\">项目</td>\n");
			results.append("<td align=\"center\" nowrap>行次</td>\n");
			
			/*results.append("<td width=\"45%\" align=\"center\">");
			results.append("<table  width=\"100%\" bordercolor=\"#ffffff\" border=1>");
			results.append("<tr>");
			results.append("<td width=\"100%\" align=\"center\" colspan=8>上年金额</td>\n");
			results.append("</tr>");
			results.append("<tr>");
			results.append("<td width=\"13%\" align=\"center\">实收资本（股本）</td>\n");
			results.append("<td width=\"12%\" align=\"center\">资本公积</td>\n");
			results.append("<td width=\"12%\" align=\"center\">减：库存股</td>\n");
			results.append("<td width=\"12%\" align=\"center\">盈余公积</td>\n");
			results.append("<td width=\"12%\" align=\"center\">未分配利润</td>\n");
			
			results.append("<td width=\"13%\" align=\"center\">其他</td>\n");
			//results.append("<td width=\"13%\" align=\"center\">外币报表折算差额</td>\n");
			//results.append("<td width=\"12%\" align=\"center\">少数股东权益</td>\n");
			
			results.append("<td width=\"12%\" align=\"center\">所有者权益合计</td>\n");
			results.append("</tr>");
			results.append("</table>");
			results.append("</td>\n");*/
			
			results.append("<td width=\"80%\" align=\"center\">");
			results.append("<table  width=\"100%\" bordercolor=\"#ffffff\" border=1>");
			results.append("<tr>");
			results.append("<td width=\"100%\" align=\"center\" colspan=8>本年金额</td>\n");
			results.append("</tr>");
			results.append("<tr>");
			results.append("<td width=\"15%\" align=\"center\">实收资本（股本）</td>\n");
			results.append("<td width=\"14%\" align=\"center\">资本公积</td>\n");
			results.append("<td width=\"14%\" align=\"center\">减：库存股</td>\n");
			results.append("<td width=\"14%\" align=\"center\">盈余公积</td>\n");
			results.append("<td width=\"14%\" align=\"center\">未分配利润</td>\n");
			
			results.append("<td width=\"14%\" align=\"center\">其他</td>\n");
			//results.append("<td width=\"13%\" align=\"center\">外币报表折算差额</td>\n");
			//results.append("<td width=\"12%\" align=\"center\">少数股东权益</td>\n");
			
			results.append("<td width=\"15%\" align=\"center\">所有者权益合计</td>\n");
			results.append("</tr>");
			results.append("</table>");
			results.append("</td>\n");
			
			break;
		}
		results.append("</tr>");
		return results.toString();
	}

	/**
	 * 生成财务报表中每个报表项的页面源码。
	 * @param itemFmt
	 * @param dataCol
	 * @return
	 */
	protected String getItemHtml(FncConfDefFormat itemFmt, FncConfDefFormat dataObj, int dataCol, int orderNum, boolean flat,
			boolean readonly, String fnc_conf_typ,FncConfDefFormat itemFmtNx) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr class='emp_rpt_tr'><td class='");
		if (itemFmt.getFncConfCalFrm() != null && !"".equals(itemFmt.getFncConfCalFrm()))
			sb.append("emp_rpt_label_formula");// 自动项的label样式
		else
			sb.append("emp_rpt_label");// 普通数据项的label样式
		sb.append("'>");
		
		//label，包括label中的前缀和缩进(前缀的个数也要算到缩进的个数里)
		int indent = itemFmt.getFncConfIndent();
		String prefix = itemFmt.getFncConfPrefix();
		if(prefix == null)
			prefix = "";
		for (int idx = 0; idx < indent - prefix.length(); idx++) {// 缩进
			sb.append(this.identStr);
		}
		sb.append(prefix);// 前缀
		sb.append(itemFmt.getItemName());//报表项的中文名称
		sb.append("</td><td class='emp_rpt_order'>");
		if(orderNum != 0) //显示行次
			sb.append(orderNum);
		sb.append("</td>");

		//需要显示数据列(即该标签栏有数据、非标题栏)，同时将需要从列表中取数的下标递增
		if (!"3".equals(itemFmt.getFncItemEditTyp())) {
			
			//将数据注册为页面的js对象
			EMPExtPageObjects pageObjs = (EMPExtPageObjects) pageContext
					.getRequest().getAttribute(EMPExtPageObjects.PARAM_NAME);
			
			switch (dataCol) {
			case 1:
				String dataItem = this.id+"."+itemFmt.getItemId() + ".data1";
				String dataItemNx = "";
				if(itemFmtNx!=null){
					dataItemNx = this.id+"."+itemFmtNx.getItemId()+".data1";//下一行id
				}
				String avg = "";
				if("04".equals(fnc_conf_typ)){
					//avg = this.id+"."+itemFmt.getItemId() + ".average";
				}
				if (pageObjs != null) {
					pageObjs.addDataField(dataItem);
					if("04".equals(fnc_conf_typ)){
						//pageObjs.addDataField(avg);
					}
				}
				
				sb.append("<td>");
				sb.append(this.getDataHtml(dataItem, itemFmt.getItemName(),
						this.getDataValueHtml(dataObj, 1), flat, readonly,fnc_conf_typ,dataItemNx));
				sb.append("</td>");
				if("04".equals(fnc_conf_typ)){
					//sb.append("<td>");
					//sb.append(this.getDataHtml(avg, itemFmt.getItemName(),
					//		String.valueOf(dataObj.getAverage()), flat, readonly));
					//sb.append("</td>");
				}
				break;
			case 2:
				String dataItem1 = this.id+"."+itemFmt.getItemId() + ".data1";
				String dataItem2 = this.id+"."+itemFmt.getItemId() + ".data2";
				if("1".equals(itemFmt.getFncItemEditTyp())){
					readonly = false;
				}else{
					readonly = true;
				}
				//下一行表格信息
				String dataItemNx1 = "";
				String dataItemNx2 = "";
				if(itemFmtNx!=null){
					dataItemNx1 = this.id+"."+itemFmtNx.getItemId() + ".data1";
					dataItemNx2 = this.id+"."+itemFmtNx.getItemId() + ".data2";
				}
				if (pageObjs != null) {
					pageObjs.addDataField(dataItem1);
					pageObjs.addDataField(dataItem2);
				}
				
				sb.append("<td>");
				sb.append(this.getDataHtml(dataItem1, itemFmt.getItemName()
						+ "(期初数)", this.getDataValueHtml(dataObj, 1), flat, true,fnc_conf_typ,dataItemNx1));
				sb.append("</td><td>");
				sb.append(this.getDataHtml(dataItem2, itemFmt.getItemName()
						+ "(期末数)", this.getDataValueHtml(dataObj, 2), flat, readonly,fnc_conf_typ,dataItemNx2));
				sb.append("</td>");
				break;
			
			case 8:
				//String dataItem81 = this.id+"."+itemFmt.getItemId() + ".dataA[0]";
				//String dataItem82 = this.id+"."+itemFmt.getItemId() + ".dataB[0]";
				double[] dataValues1 = this.getData8(dataObj, 1);
				double[] dataValues2 = this.getData8(dataObj, 2);
				//if (pageObjs != null) {
				//	pageObjs.addDataField(dataItem81);
				//	pageObjs.addDataField(dataItem82);
				//}
				
				/*sb.append("<td width=\"45%\" align=\"center\">");
				sb.append("<table  width=\"100%\">");
				sb.append("<tr>");
				for(int i=0;i<dataValues1.length;i++){
					if (pageObjs != null) {
						pageObjs.addDataField(this.id+"."+itemFmt.getItemId() + ".dataA["+i+"]");
					}
					double dValue = dataValues1[i];
					String dValue1 = this.dealData(dValue);
					if(i!=6){
						sb.append("<td width=\"12%\">");
						sb.append(this.getDataHtml(this.id+"."+itemFmt.getItemId() + ".dataA["+i+"]", itemFmt.getItemName()
								+ "(上年金额)", dValue1, flat, readonly,fnc_conf_typ));
						sb.append("</td>");
					}
				}
				sb.append("</tr>");
				sb.append("</table>");
				sb.append("</td>");*/
				sb.append("<td width=\"80%\" align=\"center\">");
				sb.append("<table  width=\"100%\">");
				sb.append("<tr>");
				for(int i=0;i<dataValues2.length;i++){
					if (pageObjs != null) {
						pageObjs.addDataField(this.id+"."+itemFmt.getItemId() + ".dataB["+i+"]");
					}
					double dValue = dataValues2[i];
					String dValue2 = this.dealData(dValue);
					if(i!=6){
						if(i==0||i==7){
							sb.append("<td width=\"15%\">");
						}else{
							sb.append("<td width=\"14%\">");
						}
						sb.append(this.getDataHtml(this.id+"."+itemFmt.getItemId() + ".dataB["+i+"]", itemFmt.getItemName()
								+ "(本年金额)", dValue2, flat, readonly,fnc_conf_typ,this.id+"."+itemFmtNx.getItemId()+".dataB["+i+"]"));
						sb.append("</td>");
					}
				}
				sb.append("</tr>");
				sb.append("</table>");
				sb.append("</td>");
				break;
			}
		} else {//对于标题栏，数据列部分为空
			switch (dataCol) {
			case 1:
				sb.append("<td></td>");
				break;
			case 2:
				sb.append("<td></td><td></td>");
				break;
			}
		}
		sb.append("</tr>");

		// 需要扩展的标签栏数
		int appendRow = itemFmt.getFncCnfAppRow();
		for (int j = 0; j < appendRow; j++) {
			sb.append("<tr class='emp_rpt_tr'><td class='emp_rpt_label'></td><td></td>");
			switch (dataCol) {
			case 1:
				sb.append("<td></td>");
				break;
			case 2:
				sb.append("<td></td>");
				sb.append("<td></td>");
				break;
			}
			sb.append("</tr>");
		}

		return sb.toString();
	}
	
	/**
	 * 针对每个数据项，生成EMPExt标签所支持的页面源码(保证整个系统的标签处理机制一致)。
	 * @param itemId
	 * @param itemName
	 * @param dataValue
	 * @return
	 */
	protected String getDataHtml(String itemId, String itemName, String dataValue, boolean flat, boolean readonly
			,String fnc_conf_typ,String itemIdNx) {

		StringBuffer sb = new StringBuffer();

		sb.append("<span ").append(str("id", "emp_field_" + itemId));
		sb.append(str("title", itemName));
		if (flat) {
			sb.append(str("type", "Base"));
		} else {
			sb.append(str("type", "Text"));
		}
		sb.append(str("readonly", new Boolean(readonly)));

		/*if (this.dataType != null){
			sb.append(str("dataType", this.dataType));
		}*/
		//对dataType进行处理，生成五个属性：dataType、validateJS、convertorJS、rangeErrorMsg、formatErrorMsg
		if(this.dataType != null){
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
	    	Map dataTypeDefMap = (Map)request.getAttribute( EMPConstance.ATTR_DATA_TYPE);
	    	EMPDataType empDataType = (EMPDataType)dataTypeDefMap.get( this.dataType );
	    	if(empDataType != null){
	    		EMPDataTypeDef typeDef = empDataType.getDataTypeDef();
	    		this.writeAttribute(sb, "dataType", this.dataType);
				this.writeAttribute(sb, "validateJS",this.parseScriptStr(itemName, typeDef.getValidateJS().getFunction(), empDataType));
				this.writeAttribute(sb, "convertorJS",this.parseScriptStr(itemName, typeDef.getConvertorJS().getFunction(), empDataType));
				
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
				
				rangeErrorMsg = parseScriptStr(itemName ,rangeErrorMsg, empDataType);
				formatErrorMsg = parseScriptStr(itemName ,formatErrorMsg, empDataType);
				
				this.writeAttribute(sb, "rangeErrorMsg", rangeErrorMsg);
				this.writeAttribute(sb, "formatErrorMsg", formatErrorMsg);
	    	}
		}
			
			
		if (this.dataTypeConfig != null)
			sb.append(str("dataTypeConfig", this.dataTypeConfig));
		
		sb.append(str("value", dataValue));
		sb.append(" rendered='false'");
		sb.append(">");

		if (flat) {
			sb.append("<span class='emp_rpt_flatspan'>");
			sb.append(dataValue).append("</span>");
		} else {
			sb.append("<input name='").append(itemId);
			if(readonly)
//				{sb.append("' class='emp_rpt_text_input_readonly' value='");}
				{sb.append("' class='emp_rpt_text_input' value='");}
			else{
				if("05".equals(fnc_conf_typ)){
					sb.append("' class='emp_rpt_text_input' value='");
				}else{
					sb.append("' class='emp_rpt_text_input' value='");
				}
			
			}
			sb.append(dataValue);
			sb.append("' id='").append(itemId);
			if(itemIdNx!=null&&!"".equals(itemIdNx)){
				sb.append("' nextElement='").append(itemIdNx);
			}
			//onkeypress='javascript:_doKeypressDown(this,"500")'
			sb.append("' onkeypress='javascript:_doKeypressDown(this)' onchange='calculate()");
			sb.append("'/>");
			//sb.append(dataValue).append("' onblur='javascript:doWhenBlurHappen(&#39;"+this+"&#39;,&#39;"+itemName+"&#39;)'/>");
			//sb.append(dataValue).append("' onblur='javascript:doWhenBlurHappen2(&#39;"+itemId+"&#39;,&#39;"+itemName+"&#39;)'/>");
		}

		sb.append("</span>\n");

		return sb.toString();
	}

	/**
	 * 判断当前是只显示还是可以编辑(缺省是都可以编辑)。
	 * @return
	 */
	protected boolean isRenderFlat(String editFlag){
		boolean flag = false;
		if(editFlag.equals("noedit")){
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}
	
	protected boolean isRenderFlat(FncStatBase fncStatBase){
		return false;
	}
	
	/**
	 * 判断当前的编辑框是只读还是可修改(缺省是都可修改)。
	 * @return
	 */
	protected boolean isRenderReadOnly(String editFlag) {
		return this.isRenderFlat(editFlag);
	}
	
	protected boolean isRenderReadOnly(FncStatBase fncStatBase) {
		return false;
	}
	
	/**
	 * 取得报表配置项中的数据
	 * @param dataObj：报表配置数据对象
	 * @param dataCol：第几个数据(只有1、2两种)
	 * @return
	 */
	protected String getDataValueHtml(FncConfDefFormat dataObj, int dataCol){
		String dataValue = "";
		//DecimalFormat df = new DecimalFormat("########0.0000");
		DecimalFormat df = new DecimalFormat("########0.00");//2013.10.07改为两位小数
		switch (dataCol) {
		case 1:
			if(dataObj != null){
				dataValue = String.valueOf(df.format(new BigDecimal(dataObj.getData1()+"")));
			}
			break;
		case 2:
			if(dataObj != null){
				dataValue = String.valueOf(df.format(new BigDecimal(dataObj.getData2()+"")));
				
			}
			break;
		}
		return dataValue;
	}
	/**
	 * 当数据列数是8列时，先获取数组
	 * @param dataObj
	 * @param dataCol
	 * @return
	 */
	protected double[] getData8(FncConfDefFormat dataObj, int dataCol){
		double[] dataValue = null;
		switch (dataCol) {
		case 1:
			if(dataObj != null){
				dataValue = dataObj.getDataA();
			}
			break;
		case 2:
			if(dataObj != null){
				dataValue = dataObj.getDataB();
				
			}
			break;
		
		}
		return dataValue;
	}
	/**
	 *将double类型的数据转换成String类型的数据展示
	 * @param dValue
	 * @return
	 */
	protected String dealData(double dValue){
		String dataValue = "";
		DecimalFormat df = new DecimalFormat("########0.00");
		dataValue = String.valueOf(df.format(new BigDecimal(dValue+"")));
		return dataValue;
	}

	/**
	 * 报表状态
	 * @param fnc_conf_typ
	 * @param state_flg
	 * @return
	 */
	public String getStatFlg(String fnc_conf_typ,String state_flg){
		String statFlg = null;
		if(state_flg != null && state_flg.length()==9){
			if("01".equals(fnc_conf_typ)){
			    //01:资产负债表
				if("0".equals(state_flg.substring(0, 1))){
					statFlg = CMISConstance.CMIS_STATFLG0;
				}else if("1".equals(state_flg.substring(0, 1))){
					statFlg = CMISConstance.CMIS_STATFLG1;
				}else if("2".equals(state_flg.substring(0, 1))){
					statFlg = CMISConstance.CMIS_STATFLG2;
				}
			}else if("02".equals(fnc_conf_typ)){
			    //02:损益表
				if("0".equals(state_flg.substring(1, 2))){
					statFlg = CMISConstance.CMIS_STATFLG0;
				}else if("1".equals(state_flg.substring(1, 2))){
					statFlg = CMISConstance.CMIS_STATFLG1;
				}else if("2".equals(state_flg.substring(1, 2))){
					statFlg = CMISConstance.CMIS_STATFLG2;
				}
			}else if("03".equals(fnc_conf_typ)){
			    //03:现金流量
				if("0".equals(state_flg.substring(2, 3))){
					statFlg = CMISConstance.CMIS_STATFLG0;
				}else if("1".equals(state_flg.substring(2, 3))){
					statFlg = CMISConstance.CMIS_STATFLG1;
				}else if("2".equals(state_flg.substring(2, 3))){
					statFlg = CMISConstance.CMIS_STATFLG2;
				}
			}else if("04".equals(fnc_conf_typ)){
			    //04:财务指标
				if("0".equals(state_flg.substring(3, 4))){
					statFlg = CMISConstance.CMIS_STATFLG0;
				}else if("1".equals(state_flg.substring(3, 4))){
					statFlg = CMISConstance.CMIS_STATFLG1;
				}else if("2".equals(state_flg.substring(3, 4))){
					statFlg = CMISConstance.CMIS_STATFLG2;
				}
			}else if("05".equals(fnc_conf_typ)){
			    //05.所有者权益变动表
				if("0".equals(state_flg.substring(4, 5))){
					statFlg = CMISConstance.CMIS_STATFLG0;
				}else if("1".equals(state_flg.substring(4, 5))){
					statFlg = CMISConstance.CMIS_STATFLG1;
				}else if("2".equals(state_flg.substring(4, 5))){
					statFlg = CMISConstance.CMIS_STATFLG2;
				}
			}else if("06".equals(fnc_conf_typ)){
			    //06财务简表
				if("0".equals(state_flg.substring(5,6))){
					statFlg = CMISConstance.CMIS_STATFLG0;
				}else if("1".equals(state_flg.substring(5,6))){
					statFlg = CMISConstance.CMIS_STATFLG1;
				}else if("2".equals(state_flg.substring(5,6))){
					statFlg = CMISConstance.CMIS_STATFLG2;
				}
			}
			
		}else {
			EMPLog.log("RPTTag",EMPLog.ERROR,0,"当前页面取不到相应的报表状态信息！");
			return EVAL_BODY_INCLUDE + "";
		}
		return statFlg;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataTypeConfig() {
		return dataTypeConfig;
	}

	public void setDataTypeConfig(String dataTypeConfig) {
		this.dataTypeConfig = dataTypeConfig;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdentStr() {
		return identStr;
	}

	public void setIdentStr(String identStr) {
		this.identStr = identStr;
	}

	public String getCusComRptId() {
		return cusComRptId;
	}

	public void setCusComRptId(String cusComRptId) {
		this.cusComRptId = cusComRptId;
	}

	public String getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(String editFlag) {
		this.editFlag = editFlag;
	}

	/**
	 protected StringBuffer getHeadHtml(String rptType,int dataCol){
	 StringBuffer results=new StringBuffer("<tr>");
	 switch (Integer.parseInt(rptType)){
	 case 1:
	 results.append("<td width=\"45%\" align=\"center\">项 目</td>\n");
	 results.append("<td width=\"5%\" align=\"center\" nowrap>行次</td>\n");
	 results.append("<td width=\"20%\" align=\"center\">期初数</td>\n");
	 results.append("<td width=\"20%\" align=\"center\">期末数</td>");
	 break;
	 case 2:
	 results.append("<td width=\"45%\" align=\"center\">项 目</td>\n");
	 results.append("<td width=\"5%\" align=\"center\" nowrap>行次</td>\n");
	 results.append("<td width=\"20%\" align=\"center\">本期数</td>\n");
	 results.append("<td width=\"20%\" align=\"center\">本年累计数</td>");
	 break;
	 case 3:
	 results.append("<td width=\"45%\" align=\"center\">项 目</td>\n");
	 results.append("<td width=\"5%\" align=\"center\" nowrap>行次</td>\n");
	 results.append("<td width=\"40%\" align=\"center\">金 额</td>\n");
	 break;
	 case 4:
	 if(dataCol == 1){
	 results.append("<td width=\"45%\" align=\"center\">项 目</td>\n");
	 results.append("<td width=\"5%\" align=\"center\" nowrap>行次</td>\n");
	 results.append("<td width=\"40%\" align=\"center\">指标值 (比率：％)</td>\n");
	 }else{
	 results.append("<td width=\"45%\" align=\"center\">项 目</td>\n");
	 results.append("<td width=\"5%\" align=\"center\" nowrap>行次</td>\n");
	 results.append("<td width=\"20%\" align=\"center\">指标值 (比率：％)</td>\n");
	 results.append("<td width=\"20%\" align=\"center\">行业标准值 </td>\n");
	 }
	 break;
	 }
	 results.append("</tr>");
	 return results;
	 }
	 */
	
	public Date strToDate(String dateStr) {
		String[] dateArray = dateStr.split("-");
		java.util.Date date = new java.util.Date(Integer.parseInt(dateArray[0],
				10) - 1900, Integer.parseInt(dateArray[1], 10) - 1, Integer
				.parseInt(dateArray[2], 10));
		return date;
	}
	
	 protected String str(String name, Object value)
	    {
	        if(value != null)
	            return " " + name + "='" + value + "'";
	        else
	            return "";
	    }
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
						System.out.println( e );
//						dstStr.append( "''");
					}
				}
				tmp = tmp.substring(idx2 + 1);
			}
			
			String retValue = dstStr.toString();
			return retValue;
		}

	@Override
	protected void addDefaultAttributes(StringBuffer sb, KeyedCollection coll) {
		// TODO Auto-generated method stub
		super.addDefaultAttributes(sb, coll);
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void cloneDafaultAttributes(EMPExtFieldBase target) {
		// TODO Auto-generated method stub
		super.cloneDafaultAttributes(target);
	}

	@Override
	public String getAccesskey() {
		// TODO Auto-generated method stub
		return super.getAccesskey();
	}

	@Override
	public String getCnname() {
		// TODO Auto-generated method stub
		return super.getCnname();
	}

	@Override
	public String getColSpan() {
		// TODO Auto-generated method stub
		return super.getColSpan();
	}

	@Override
	public String getCssClass() {
		// TODO Auto-generated method stub
		return super.getCssClass();
	}

	@Override
	public String getCssElementClass() {
		// TODO Auto-generated method stub
		return super.getCssElementClass();
	}

	@Override
	public String getCssErrorClass() {
		// TODO Auto-generated method stub
		return super.getCssErrorClass();
	}

	@Override
	public String getCssFlatClass() {
		// TODO Auto-generated method stub
		return super.getCssFlatClass();
	}

	@Override
	public String getCssLabelClass() {
		// TODO Auto-generated method stub
		return super.getCssLabelClass();
	}

	@Override
	public String getCssRequiredClass() {
		// TODO Auto-generated method stub
		return super.getCssRequiredClass();
	}

	@Override
	public String getCssTDClass() {
		// TODO Auto-generated method stub
		return super.getCssTDClass();
	}

	@Override
	public String getDefvalue() {
		// TODO Auto-generated method stub
		return super.getDefvalue();
	}

	@Override
	public String getDictname() {
		// TODO Auto-generated method stub
		return super.getDictname();
	}

	@Override
	public String getFieldErrorDataName() {
		// TODO Auto-generated method stub
		return super.getFieldErrorDataName();
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return super.getHelp();
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return super.getLabel();
	}

	@Override
	public String getOnclick() {
		// TODO Auto-generated method stub
		return super.getOnclick();
	}

	@Override
	public String getOndblclick() {
		// TODO Auto-generated method stub
		return super.getOndblclick();
	}

	@Override
	public String getOnkeydown() {
		// TODO Auto-generated method stub
		return super.getOnkeydown();
	}

	@Override
	public String getOnkeypress() {
		// TODO Auto-generated method stub
		return super.getOnkeypress();
	}

	@Override
	public String getOnkeyup() {
		// TODO Auto-generated method stub
		return super.getOnkeyup();
	}

	@Override
	public String getOnmousedown() {
		// TODO Auto-generated method stub
		return super.getOnmousedown();
	}

	@Override
	public String getOnmousemove() {
		// TODO Auto-generated method stub
		return super.getOnmousemove();
	}

	@Override
	public String getOnmouseout() {
		// TODO Auto-generated method stub
		return super.getOnmouseout();
	}

	@Override
	public String getOnmouseover() {
		// TODO Auto-generated method stub
		return super.getOnmouseover();
	}

	@Override
	public String getOnmouseup() {
		// TODO Auto-generated method stub
		return super.getOnmouseup();
	}

	@Override
	public Tag getParent() {
		// TODO Auto-generated method stub
		return super.getParent();
	}

	@Override
	public String getTabindex() {
		// TODO Auto-generated method stub
		return super.getTabindex();
	}

	@Override
	public String getTableHead() {
		// TODO Auto-generated method stub
		return super.getTableHead();
	}

	@Override
	protected String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getValue(KeyedCollection arg0) {
		// TODO Auto-generated method stub
		return super.getValue(arg0);
	}

	@Override
	public boolean isDisabled() {
		// TODO Auto-generated method stub
		return super.isDisabled();
	}

	@Override
	public boolean isFlat() {
		// TODO Auto-generated method stub
		return super.isFlat();
	}

	@Override
	public boolean isHidden() {
		// TODO Auto-generated method stub
		return super.isHidden();
	}

	@Override
	public boolean isLanguageResouce() {
		// TODO Auto-generated method stub
		return super.isLanguageResouce();
	}

	@Override
	public boolean isOnlyControlElement() {
		// TODO Auto-generated method stub
		return super.isOnlyControlElement();
	}

	@Override
	public boolean isReadonly() {
		// TODO Auto-generated method stub
		return super.isReadonly();
	}

	@Override
	public boolean isRequired() {
		// TODO Auto-generated method stub
		return super.isRequired();
	}

	@Override
	public boolean isStatistic() {
		// TODO Auto-generated method stub
		return super.isStatistic();
	}

	@Override
	public void renderFieldTag(StringBuffer arg0, KeyedCollection arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		super.renderFieldTag(arg0, arg1, arg2);
	}

	@Override
	protected void renderFlatInnerHtml(StringBuffer sb, KeyedCollection coll) {
		// TODO Auto-generated method stub
		super.renderFlatInnerHtml(sb, coll);
	}

	@Override
	protected void renderInnerHtml(StringBuffer arg0, KeyedCollection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void renderOtherAttribute(StringBuffer arg0,
			KeyedCollection arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccesskey(String accesskey) {
		// TODO Auto-generated method stub
		super.setAccesskey(accesskey);
	}

	@Override
	public void setCnname(String cnname) {
		// TODO Auto-generated method stub
		super.setCnname(cnname);
	}

	@Override
	public void setColSpan(String colSpan) {
		// TODO Auto-generated method stub
		super.setColSpan(colSpan);
	}

	@Override
	public void setCssClass(String cssClass) {
		// TODO Auto-generated method stub
		super.setCssClass(cssClass);
	}

	@Override
	public void setCssElementClass(String cssElementClass) {
		// TODO Auto-generated method stub
		super.setCssElementClass(cssElementClass);
	}

	@Override
	public void setCssErrorClass(String cssErrorClass) {
		// TODO Auto-generated method stub
		super.setCssErrorClass(cssErrorClass);
	}

	@Override
	public void setCssFlatClass(String cssFlatClass) {
		// TODO Auto-generated method stub
		super.setCssFlatClass(cssFlatClass);
	}

	@Override
	public void setCssLabelClass(String cssLabelClass) {
		// TODO Auto-generated method stub
		super.setCssLabelClass(cssLabelClass);
	}

	@Override
	public void setCssRequiredClass(String cssRequiredClass) {
		// TODO Auto-generated method stub
		super.setCssRequiredClass(cssRequiredClass);
	}

	@Override
	public void setCssTDClass(String cssTDClass) {
		// TODO Auto-generated method stub
		super.setCssTDClass(cssTDClass);
	}

	@Override
	public void setDefvalue(String defvalue) {
		// TODO Auto-generated method stub
		super.setDefvalue(defvalue);
	}

	@Override
	public void setDictname(String dictname) {
		// TODO Auto-generated method stub
		super.setDictname(dictname);
	}

	@Override
	public void setDisabled(boolean disabled) {
		// TODO Auto-generated method stub
		super.setDisabled(disabled);
	}

	@Override
	public void setFieldErrorDataName(String fieldErrorDataName) {
		// TODO Auto-generated method stub
		super.setFieldErrorDataName(fieldErrorDataName);
	}

	@Override
	public void setFlat(boolean flat) {
		// TODO Auto-generated method stub
		super.setFlat(flat);
	}

	@Override
	public void setHelp(String help) {
		// TODO Auto-generated method stub
		super.setHelp(help);
	}

	@Override
	public void setHidden(boolean hidden) {
		// TODO Auto-generated method stub
		super.setHidden(hidden);
	}

	@Override
	public void setLabel(String label) {
		// TODO Auto-generated method stub
		super.setLabel(label);
	}

	@Override
	public void setLanguageResouce(boolean languageResouce) {
		// TODO Auto-generated method stub
		super.setLanguageResouce(languageResouce);
	}

	@Override
	public void setOnclick(String onclick) {
		// TODO Auto-generated method stub
		super.setOnclick(onclick);
	}

	@Override
	public void setOndblclick(String ondblclick) {
		// TODO Auto-generated method stub
		super.setOndblclick(ondblclick);
	}

	@Override
	public void setOnkeydown(String onkeydown) {
		// TODO Auto-generated method stub
		super.setOnkeydown(onkeydown);
	}

	@Override
	public void setOnkeypress(String onkeypress) {
		// TODO Auto-generated method stub
		super.setOnkeypress(onkeypress);
	}

	@Override
	public void setOnkeyup(String onkeyup) {
		// TODO Auto-generated method stub
		super.setOnkeyup(onkeyup);
	}

	@Override
	public void setOnlyControlElement(boolean onlyControlElement) {
		// TODO Auto-generated method stub
		super.setOnlyControlElement(onlyControlElement);
	}

	@Override
	public void setOnmousedown(String onmousedown) {
		// TODO Auto-generated method stub
		super.setOnmousedown(onmousedown);
	}

	@Override
	public void setOnmousemove(String onmousemove) {
		// TODO Auto-generated method stub
		super.setOnmousemove(onmousemove);
	}

	@Override
	public void setOnmouseout(String onmouseout) {
		// TODO Auto-generated method stub
		super.setOnmouseout(onmouseout);
	}

	@Override
	public void setOnmouseover(String onmouseover) {
		// TODO Auto-generated method stub
		super.setOnmouseover(onmouseover);
	}

	@Override
	public void setOnmouseup(String onmouseup) {
		// TODO Auto-generated method stub
		super.setOnmouseup(onmouseup);
	}

	@Override
	public void setParent(Tag tag) {
		// TODO Auto-generated method stub
		super.setParent(tag);
	}

	@Override
	public void setReadonly(boolean readonly) {
		// TODO Auto-generated method stub
		super.setReadonly(readonly);
	}

	@Override
	public void setRequired(boolean required) {
		// TODO Auto-generated method stub
		super.setRequired(required);
	}

	@Override
	public void setStatistic(boolean statistic) {
		// TODO Auto-generated method stub
		super.setStatistic(statistic);
	}

	@Override
	public void setTabindex(String tabindex) {
		// TODO Auto-generated method stub
		super.setTabindex(tabindex);
	}

	@Override
	protected String getDecoratedValue(String value, String dictname,
			boolean languageResouce) {
		// TODO Auto-generated method stub
		return super.getDecoratedValue(value, dictname, languageResouce);
	}

	@Override
	protected IndexedCollection getDictMapCollection(String dictname) {
		// TODO Auto-generated method stub
		return super.getDictMapCollection(dictname);
	}

	@Override
	protected boolean judgeReadPermission(String fieldId) {
		// TODO Auto-generated method stub
		return super.judgeReadPermission(fieldId);
	}

	@Override
	public boolean judgeWritePermission(String fieldId) {
		// TODO Auto-generated method stub
		return super.judgeWritePermission(fieldId);
	}

	@Override
	protected void writeAttribute(StringBuffer buffer, String name, Object value) {
		// TODO Auto-generated method stub
		super.writeAttribute(buffer, name, value);
	}

	@Override
	protected String XMLDecode(String src) {
		// TODO Auto-generated method stub
		return super.XMLDecode(src);
	}

	@Override
	protected String XMLencode(String arg0) {
		// TODO Auto-generated method stub
		return super.XMLencode(arg0);
	}

	@Override
	public String formatValue(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return super.formatValue(arg0, arg1);
	}

	@Override
	public String getAlign() {
		// TODO Auto-generated method stub
		return super.getAlign();
	}

	@Override
	public String getAppendPostField(String targetClient) {
		// TODO Auto-generated method stub
		return super.getAppendPostField(targetClient);
	}

	@Override
	public String getCSSClass() {
		// TODO Auto-generated method stub
		return super.getCSSClass();
	}

	@Override
	public String getCltType() {
		// TODO Auto-generated method stub
		return super.getCltType();
	}

	@Override
	public String getContentDivId() {
		// TODO Auto-generated method stub
		return super.getContentDivId();
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return super.getContextPath();
	}

	@Override
	public String getCustomAttr() {
		// TODO Auto-generated method stub
		return super.getCustomAttr();
	}

	@Override
	public DataElement getDataElement(String dataName) throws Exception {
		// TODO Auto-generated method stub
		return super.getDataElement(dataName);
	}

	@Override
	public EMPDataType getDataType(String arg0) {
		// TODO Auto-generated method stub
		return super.getDataType(arg0);
	}

	@Override
	public String getDataTypeName(String arg0) {
		// TODO Auto-generated method stub
		return super.getDataTypeName(arg0);
	}

	@Override
	public String getDataValue(String arg0) {
		// TODO Auto-generated method stub
		return super.getDataValue(arg0);
	}

	@Override
	public String getFileURL(String srcURL) {
		// TODO Auto-generated method stub
		return super.getFileURL(srcURL);
	}

	@Override
	protected String getGetURL(String arg0) {
		// TODO Auto-generated method stub
		return super.getGetURL(arg0);
	}

	@Override
	public String getLanguage() {
		// TODO Auto-generated method stub
		return super.getLanguage();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}

	@Override
	public String getPostURL(String arg0) {
		// TODO Auto-generated method stub
		return super.getPostURL(arg0);
	}

	@Override
	public String getRequestValue(String arg0) {
		// TODO Auto-generated method stub
		return super.getRequestValue(arg0);
	}

	@Override
	public String getResourceValue(String resId) {
		// TODO Auto-generated method stub
		return super.getResourceValue(resId);
	}

	@Override
	public String getURL(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return super.getURL(arg0, arg1);
	}

	@Override
	public String getValign() {
		// TODO Auto-generated method stub
		return super.getValign();
	}

	@Override
	protected void outputContent(String arg0) {
		// TODO Auto-generated method stub
		super.outputContent(arg0);
	}

	@Override
	protected String parsePamaterStr(String arg0) {
		// TODO Auto-generated method stub
		return super.parsePamaterStr(arg0);
	}

	@Override
	protected String replaceAll(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return super.replaceAll(arg0, arg1, arg2);
	}

	@Override
	public void setAlign(String align) {
		// TODO Auto-generated method stub
		super.setAlign(align);
	}

	@Override
	public void setCSSClass(String value) {
		// TODO Auto-generated method stub
		super.setCSSClass(value);
	}

	@Override
	public void setCltType(String cltType) {
		// TODO Auto-generated method stub
		super.setCltType(cltType);
	}

	@Override
	public void setCustomAttr(String customAttr) {
		// TODO Auto-generated method stub
		super.setCustomAttr(customAttr);
	}

	@Override
	public void setName(String value) {
		// TODO Auto-generated method stub
		super.setName(value);
	}

	@Override
	public void setValign(String valign) {
		// TODO Auto-generated method stub
		super.setValign(valign);
	}

	@Override
	public int doAfterBody() throws JspException {
		// TODO Auto-generated method stub
		return super.doAfterBody();
	}

	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		return super.doEndTag();
	}

	@Override
	public Object getValue(String k) {
		// TODO Auto-generated method stub
		return super.getValue(k);
	}

	@Override
	public Enumeration<String> getValues() {
		// TODO Auto-generated method stub
		return super.getValues();
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		super.release();
	}

	@Override
	public void removeValue(String k) {
		// TODO Auto-generated method stub
		super.removeValue(k);
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		// TODO Auto-generated method stub
		super.setPageContext(pageContext);
	}

	@Override
	public void setValue(String k, Object o) {
		// TODO Auto-generated method stub
		super.setValue(k, o);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	 
	 
	 
}
