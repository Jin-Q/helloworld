package com.yucheng.cmis.biz01line.ind.tag;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.ecc.emp.ext.tag.field.EMPExtFieldBase;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.CMISComponent;
/**
 * <h1>单选指标展示标签</h1>
 * <p>用来输出每个单选指标的头部和尾部html代码</p>
 * @author toto233
 *
 */
public class IndItemRadio extends EMPExtTagSupport {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CMISComponent.class);	
	protected Vector fields = new Vector();
	


	protected String indexNo = "0";
	protected String indexName = "index Name";
	protected String orgVal = "";
	protected String selectVal = "0";
	protected String groupNo = "G00000";
	protected String readonly = "false"; 




	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public int doStartTag() throws JspException {
		IndGroup indGroup= (IndGroup)this.getParent();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String orgVal = this.getOrgVal();
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>"+
			"<td class = \"bodyIndex\">");
		//增加指标ID
		sb.append(this.getIndexName());
		//如果有原始值，则增加原始值
		if (orgVal!=null&&!orgVal.equals("")){
			//sb.append("<div id = \""+indGroup.getGroupNo()+"."+this.getIndexNo()+"_orgVal\">"+
			//"<br>("+orgVal+")</div>");
			sb.append("<div id = \""+indGroup.getGroupNo()+"."+this.getIndexNo()+"_orgVal\">"+
					"<br></div>");
			sb.append("<input name = \""+indGroup.getGroupNo()+"."+this.getIndexNo()+"_orgVal\" type=\"hidden\" value = \""+orgVal+"\"/>\n");
		}
		sb.append("</td>");
		//下方添加指标选项表头，和隐藏的第一项，隐藏第一项是由于ajax发送数据有错误。选择第一项提交会出错。
		sb.append("<td class = \"bodySelection\">\n"+
		"<table>\n");
		if("true".equals(this.getReadonly())){
			sb.append("<INPUT TYPE=\"text\" NAME=\""+indGroup.getGroupNo()+"."+this.getIndexNo()+"\" value=\""+
				this.getSelectVal()+
				"\" class = \"hidden\"/>");
		}else{
			sb.append("<INPUT TYPE=\"radio\" NAME=\""+indGroup.getGroupNo()+"."+this.getIndexNo()+"\" value=\"0\" class = \"hidden\"/>");				

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
	public String getOrgVal() {

		IndGroup indGroup= (IndGroup)this.getParent();
		String orgVal = this.orgVal;
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
			if (context != null) {
				try {
					
					orgVal = (String) indGroup.getGroupKcoll().getDataValue(this.getIndexNo()+"_orgVal");
				} catch (Exception e) {
					orgVal ="";
				}
			} 
			if (orgVal == null) {
				orgVal = request.getParameter(indGroup.getGroupNo()+"."+this.getIndexNo()+"_orgVal");
			}
		return orgVal;

	}

	public void setOrgVal(String orgVal) {
		this.orgVal = orgVal;
	}

	
	/**
	 * 取得indexNo(指标编号)。
	 * <p>
	 * 取缺省值的方式是：
	 * 如果defvalue是以$开头的字符串，则表示$后面的部分代表一个数据名称，取得该数据的值返回(如果Context中没有该数据，则从request中取得)
	 * 否则直接返回defvalue的值
	 * @return
	 */
	public String getIndexNo() {
		return this.indexNo;
	}
	/**
	 * 保存indexNo(指标编号)
	 * @param indexNo
	 */
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	
	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getSelectVal() {

		IndGroup indGroup= (IndGroup)this.getParent();
		String selectVal = this.selectVal;
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
			if (context != null) {
				try {
					
					selectVal = (String) indGroup.getGroupKcoll().getDataValue(this.getIndexNo());
				} catch (Exception e) {
					selectVal = "0";
				}
			} 
			if (selectVal == null) {
				selectVal = request.getParameter(indGroup.getGroupNo()+"."+this.getIndexNo());
			}
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
            		this.getIndexNo()+"|"+selectVal);
		return selectVal;
	}

	public void setSelectVal(String selectVal) {
		this.selectVal = selectVal;
	}
	public String getGroupNo() {
		IndGroup indGroup= (IndGroup)this.getParent();
		return indGroup.groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
}
