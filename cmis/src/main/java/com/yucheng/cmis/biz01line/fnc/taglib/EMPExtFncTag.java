package com.yucheng.cmis.biz01line.fnc.taglib;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.pub.FNCFactory;


public class EMPExtFncTag extends EMPExtTagSupport {

	
	private static final long serialVersionUID = 1L;
	
	protected String id;//formats
	protected String identStr = "&nbsp;&nbsp";
	
	public int doStartTag() throws JspException {
		FncConfDefFormat f_format;
		FncConfStyles fcs = null;//缓存中的标签配置信息
		List f_list;
		int orderNum = 1; //当前行次
		StringBuffer sb = new StringBuffer();
		//画table
		sb.append("<table width=100%>");
		sb.append("<tr>");
		
		try{
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
			f_list = (List)context.getDataValue(id);
			int currentCote = 0; //当前标签栏数
			for(int i=0;i<f_list.size();i++){
				f_format = (FncConfDefFormat)f_list.get(i);
				String style_id = f_format.getStyleId();
				fcs = (FncConfStyles)FNCFactory.getFNCInstance(style_id);
				int cote = fcs.getFncConfCotes();//标签栏数(label+数据表示一列)
				int width = 100 / cote;
				//输入框列数(即数据列数，通常只有一列或两列)
				int dataCol = fcs.getFncConfDataCol();
				int confCote = f_format.getFncConfCotes();
				//是否重新开始一行
				if(confCote == (currentCote + 1)){
					sb.append("<td valign=top align=center width='").append(width).append("%'><table width=100%>");
					sb.append(this.getHeadHtml(dataCol,fcs.getFncConfTyp()));//每列的标题
					currentCote++;
				}else if (confCote > cote || confCote != currentCote) {//非法的配置信息，略过
					continue;
				}
				int dataNum = 0; //当前需要显示的数据(数据列表中的行次)
				List fsc_list = fcs.getItems();
				FncConfDefFormat dataObj = null;
				if (!"3".equals(f_format.getFncItemEditTyp())) {
					if(f_list != null && f_list.size() > dataNum)
					dataNum ++;
				}
				int num = 0;
				if ("1".equals(f_format.getFncConfRowFlg())) {// 是否显示行次
					num = orderNum;
					orderNum++;
				}
				//具体标签栏生成的页面源码(即列表中的一行)
				sb.append(this.getItemHtml(f_format, dataCol, num));
				//已经到最后一个报表配置或者是下一个报表配置就不属于当前列时，需要将当前列的td标签封闭
				if (i == f_list.size() - 1) {
					sb.append("</table></td>");
				} else {
					FncConfDefFormat nextItemFmt = (FncConfDefFormat) f_list.get(i + 1);
					int nextItemCote = nextItemFmt.getFncConfCotes();
					if (nextItemCote > cote || nextItemCote != currentCote) {
						sb.append("</table></td>");
					}
				}
			   
			}
			sb.append("</tr></table>");
			outputContent(sb.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	
	/**
	 * 生成财务报表数据表格中的表头信息。
	 * @param dataCol
	 * @return
	 */
	protected String getHeadHtml(int dataCol,String fncConfTyp) {
		StringBuffer results = new StringBuffer("<tr class='emp_rpt_head'>");
		
		switch (dataCol) {
		case 1:
			results.append("<td width=\"45%\" align=\"center\">项目</td>\n");
			results.append("<td align=\"center\" nowrap>行次</td>\n");
			if("04".equals(fncConfTyp)){
				results.append("<td width=\"40%\" align=\"center\">指标值（比率:%）</td>\n");
			}else{
				results.append("<td width=\"40%\" align=\"center\">金额</td>\n");
			}
			break;
		case 2:
			results.append("<td width=\"45%\" align=\"center\">项目</td>\n");
			results.append("<td align=\"center\" nowrap>行次</td>\n");
			if("02".equals(fncConfTyp)||"03".equals(fncConfTyp)){
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
			
			//results.append("<td width=\"40%\" align=\"center\">");
			//results.append("<table width=\"100%\" bordercolor=\"#ffffff\" border=1>");
			//results.append("<tr>");
			//results.append("<td width=\"100%\" align=\"center\" colspan=8>上年金额</td>\n");
			//results.append("</tr>");
			//results.append("<tr>");
			//results.append("<td width=\"13%\" align=\"center\">实收资本（股本）</td>\n");
			//results.append("<td width=\"12%\" align=\"center\">资本公积</td>\n");
			//results.append("<td width=\"12%\" align=\"center\">减：库存股</td>\n");
			//results.append("<td width=\"12%\" align=\"center\">盈余公积</td>\n");
			//results.append("<td width=\"12%\" align=\"center\">未分配利润</td>\n");
			
			//results.append("<td width=\"13%\" align=\"center\">其他</td>\n");
			//results.append("<td width=\"13%\" align=\"center\">外币报表折算差额</td>\n");
			//results.append("<td width=\"12%\" align=\"center\">少数股东权益</td>\n");
			
			//results.append("<td width=\"12%\" align=\"center\">所有者权益合计</td>\n");
			////results.append("</tr>");
			//results.append("</table>");
			//results.append("</td>\n");
			
			results.append("<td width=\"80%\" align=\"center\">");
			results.append("<table width=\"100%\" bordercolor=\"#ffffff\" border=1>");
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
	
	protected String getItemHtml(FncConfDefFormat f_format,int dataCol,int orderNum){
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr class='emp_rpt_tr'><td class='");
		if (f_format.getFncConfCalFrm() != null && !"".equals(f_format.getFncConfCalFrm()))
			sb.append("emp_rpt_label_formula");// 自动项的label样式
		else
			sb.append("emp_rpt_label");// 普通数据项的label样式
		sb.append("' style='cursor:hand'  onClick='javascript:doGetUpdateFncConfDefFormat(&#39;"+f_format.getStyleId()+"&#39;,&#39;"+f_format.getItemId()+"&#39;)'>");
		
		//label，包括label中的前缀和缩进(前缀的个数也要算到缩进的个数里)
		int indent = f_format.getFncConfIndent();
		String prefix = f_format.getFncConfPrefix();
		if(prefix == null)
			prefix = "";
		for (int idx = 0; idx < indent - prefix.length(); idx++) {// 缩进
			sb.append(this.identStr);
		}
		sb.append(prefix);// 前缀
		sb.append(f_format.getItemName());//报表项的中文名称
		
		sb.append("</td><td class='emp_rpt_order'>");
		if(orderNum != 0) //显示行次
			sb.append(orderNum);
		sb.append("</td>");
		
        //需要显示数据列(即该标签栏有数据、非标题栏)，同时将需要从列表中取数的下标递增
		if (!"3".equals(f_format.getFncItemEditTyp())) {
			switch (dataCol) {
			case 1:
				sb.append("<td>");
				sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
				sb.append("</td>");
				break;
			case 2:
				sb.append("<td>");
				sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
				sb.append("</td>");
				sb.append("<td>");
				sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
				sb.append("</td>");
				break;
			
			case 8:
				//sb.append("<td width=\"40%\">");
					/*sb.append("<table width=\"100%\">");
						sb.append("<tr>");
						sb.append("<td width=\"13%\">");
						sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
						sb.append("</td>");
						sb.append("<td width=\"12%\">");
						sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
						sb.append("</td>");
						sb.append("<td width=\"12%\">");
						sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
						sb.append("</td>");
						sb.append("<td width=\"12%\">");
						sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
						sb.append("</td>");
						sb.append("<td width=\"12%\">");
						sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
						sb.append("</td>");
						sb.append("<td width=\"13%\">");
						sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
						sb.append("</td>");
						//sb.append("<td width=\"12%\">");
						//sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
						//sb.append("</td>");
						sb.append("<td width=\"12%\">");
						sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
						sb.append("</td>");
						sb.append("</tr>");
					sb.append("</table>");*/
					sb.append("<td width=\"80%\">");
					sb.append("<table width=\"100%\">");
					sb.append("<tr>");
					sb.append("<td width=\"15%\">");
					sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
					sb.append("</td>");
					sb.append("<td width=\"14%\">");
					sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
					sb.append("</td>");
					sb.append("<td width=\"14%\">");
					sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
					sb.append("</td>");
					sb.append("<td width=\"14%\">");
					sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
					sb.append("</td>");
					sb.append("<td width=\"14%\">");
					sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
					sb.append("</td>");
					sb.append("<td width=\"14%\">");
					sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
					sb.append("</td>");
					//sb.append("<td width=\"12%\">");
					//sb.append("<input class='emp_rpt_text_input_8' type='text' name="+f_format.getItemId()+">");
					//sb.append("</td>");
					sb.append("<td width=\"15%\">");
					sb.append("<input class='emp_rpt_text_input' type='text' name="+f_format.getItemId()+">");
					sb.append("</td>");
					sb.append("</tr>");
					sb.append("</table>");
				sb.append("</td>");
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
		//需要扩展的标签栏数
		int appendRow = f_format.getFncCnfAppRow();
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

	
}
