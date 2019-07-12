package com.yucheng.cmis.biz01line.ind.tag;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.ext.tag.field.EMPExtFieldBase;
import com.yucheng.cmis.pub.CMISComponent;

/**
 *@Classname	IndItemText.java
 *@Version 1.0	
 *@Since   1.0 	Dec 19, 2009 
 *@Copyright 	yuchengtech
 *@Author 		apple
 *@Description：指标 Text文本标签解析类
 *@Lastmodified 
 *@Author	    
 */
public class IndItemText extends EMPExtTagSupport{
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CMISComponent.class);	
	protected Vector fields = new Vector();
	


	protected String indexNo = "0";
	protected String indexName = "index Name";
	protected String indexVal = "";
	protected String groupNo = "G00000";
	protected String readonly = "false";

	public int doStartTag() throws JspException {
		IndGroup indGroup= (IndGroup)this.getParent();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String indexVal = this.indexVal();
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>"+
			"<td class = \"bodyIndex\">");
		//增加指标ID
		sb.append(this.getIndexName());
		//如果有原始值，则增加原始值
		if (indexVal!=null&&!indexVal.equals("")){
			sb.append("<div id = \""+indGroup.getGroupNo()+"."+this.getIndexNo()+"_orgVal\">"+
			"</div>");
			sb.append("<input name = \""+indGroup.getGroupNo()+"."+this.getIndexNo()+"_orgVal\" type=\"hidden\" value = \""+indexVal+"\"/>\n");
		}
		sb.append("</td>");
		//下方添加指标选项表头，和隐藏的第一项，隐藏第一项是由于ajax发送数据有错误。选择第一项提交会出错。
		sb.append("<td class = \"bodySelection\">"
		);
		if(readonly.equals("true")){
			sb.append(this.indexVal());
		}
		sb.append("\n<table>\n");
		sb.append("<INPUT TYPE=\"text\" NAME=\""+indGroup.getGroupNo()+"."+this.getIndexNo()+"\" value=\""+
					indexVal+
				"\" ");
		if(readonly.equals("true")){
			sb.append("class = \"hidden\"/>");
		}else{
			sb.append("/>");
		}
	
		outputContent(sb.toString());
		return EVAL_BODY_INCLUDE;		
	}

	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer();

		IndGroup indGroup= (IndGroup)this.getParent();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String score = "";
		
		try {
			score= (String) indGroup.getGroupKcoll().getDataValue(this.getIndexNo()+"_score");
		} catch (ObjectNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			score = "";
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			score = "";
		}
		if (score==null||score.equals("")){
			score="";
		}
		sb.append("</table>"+
		"</td>"+   
		"<td class=\"bodyScore\">"+ 
		"<INPUT TYPE=\"text\" NAME=\""+indGroup.getGroupNo()+"."+this.getIndexNo()+"_score\" value=\""+score+"\" size=\"3\" disabled=\"disabled\"/>"+
		"</td>"+
		"</tr>"); 		
		outputContent(sb.toString());
		
		return SKIP_BODY;
	}
	
	public void addCMISDataField(EMPExtFieldBase tag) {
		this.fields.addElement(tag);
	}
	
	/**
	 * 取得indexNo(指标编号)。
	 * <p>
	 * 取缺省值的方式是：
	 * 如果defvalue是以$开头的字符串，则表示$后面的部分代表一个数据名称，取得该数据的值返回(如果Context中没有该数据，则从request中取得)
	 * 否则直接返回defvalue的值
	 * @return
	 */
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	public String getIndexVal() {
		return indexVal;
	}
	public void setIndexVal(String indexVal) {
		this.indexVal = indexVal;
	}
	public String getReadonly() {
		return readonly;
	}
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	} 
	
	/**
	 * 取定量指标的指标值
	 */
	public String indexVal(){
		IndGroup indGroup= (IndGroup)this.getParent();
		String indexVal = this.indexVal;
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
			if (context != null) {
				try {
					indexVal = (String) indGroup.getGroupKcoll().getDataValue(this.getIndexNo());

				} catch (Exception e) {
					indexVal = "0";
				}
			} 
			if (indexVal == null) {
				indexVal = request.getParameter(indGroup.getGroupNo()+"."+this.getIndexNo());
			}
		return indexVal;
		
	}
	

}	
