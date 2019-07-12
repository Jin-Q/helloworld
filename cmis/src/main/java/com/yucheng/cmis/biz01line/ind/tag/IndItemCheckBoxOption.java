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
import com.yucheng.cmis.pub.CMISComponent;

public class IndItemCheckBoxOption extends EMPExtTagSupport {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CMISComponent.class);	
	protected Vector fields = new Vector();
	


	protected String indValue = "0";
	protected String indDesc = "Option Name";
	protected String indexNo = "i000000";
	

	public int doStartTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		IndItemRadio indItemRadio= (IndItemRadio)this.getParent();
		String selectVal = indItemRadio.getSelectVal();
		sb.append("<tr><td>\n");
		if ("true".equals(indItemRadio.getReadonly())){
			if (selectVal!=null&&selectVal.equals(this.getIndValue())){
				sb.append("<INPUT TYPE=\"checkbox\" NAME=\""+indItemRadio.getGroupNo()+"."+indItemRadio.getIndexNo()+"\" value=\""+this.getIndValue()+"\" checked = \"checked\" disabled=\"disabled\"/>"+this.getIndDesc()+"\n");
			}else{
				sb.append("<INPUT TYPE=\"checkbox\" NAME=\""+indItemRadio.getGroupNo()+"."+indItemRadio.getIndexNo()+"\" value=\""+this.getIndValue()+"\" disabled=\"disabled\"/>"+this.getIndDesc()+"\n");
			}	
			
		}else{
			if (selectVal!=null&&selectVal.equals(this.getIndValue())){
				sb.append("<INPUT TYPE=\"checkbox\" NAME=\""+indItemRadio.getGroupNo()+"."+indItemRadio.getIndexNo()+"\" ID=\""+indItemRadio.getGroupNo()+"."+indItemRadio.getIndexNo()+this.getIndValue()+"\" value=\""+this.getIndValue()+"\" checked = \"checked\"/><label for=\""+indItemRadio.getGroupNo()+"."+indItemRadio.getIndexNo()+this.getIndValue()+"\" value=\""+this.getIndValue()+"\">"+this.getIndDesc()+"</label>\n");
			}else{
				sb.append("<INPUT TYPE=\"checkbox\" NAME=\""+indItemRadio.getGroupNo()+"."+indItemRadio.getIndexNo()+"\" ID=\""+indItemRadio.getGroupNo()+"."+indItemRadio.getIndexNo()+this.getIndValue()+"\" value=\""+this.getIndValue()+"\"/><label for=\""+indItemRadio.getGroupNo()+"."+indItemRadio.getIndexNo()+this.getIndValue()+"\" value=\""+this.getIndValue()+"\">"+this.getIndDesc()+"</label>\n");
			}
		}
		
		sb.append("</td></tr>");
		
		outputContent(sb.toString());
		return EVAL_BODY_INCLUDE;		
	}

	public int doEndTag() throws JspException {
		return SKIP_BODY;
	}
	
	public void addCMISDataField(EMPExtFieldBase tag) {
		this.fields.addElement(tag);
	}

	

	public String getIndexNo() {
		IndItemRadio indItemRadio= (IndItemRadio)this.getParent();	
		return indItemRadio.getIndexNo();
	}

	/**
	 * 保存indexNo(指标编号)
	 * @param indexNo
	 */
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	
	public String getIndValue() {
		return indValue;
	}

	public void setIndValue(String indValue) {
		this.indValue = indValue;
	}

	public String getIndDesc() {
		return indDesc;
	}

	public void setIndDesc(String indDesc) {
		this.indDesc = indDesc;
	}




}
