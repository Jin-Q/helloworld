package com.yucheng.cmis.pub.ide.tagGenerator;

import com.ecc.emp.ide.table.FieldWrapper;
import com.ecc.ide.builder.jsp.JspTagGenerator;

public class TableItemTagGenerator implements JspTagGenerator {

	public String generateTag(FieldWrapper fieldWra) {
		if ("link".equals(fieldWra.getJSPTag()))
			return generateLinkTag(fieldWra);
		else
			return generateTextTag(fieldWra);
	}
 
	public String generateTextTag(FieldWrapper fieldWra) {
		StringBuffer tagbuf = new StringBuffer("<");
		try {
			tagbuf.append("emp:text ");

			tagbuf.append("id=\"" + Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");
			tagbuf.append(Decide.decideNull("dictname", (String)fieldWra.extAttrMap.get("dictname")));

			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}

	public String generateLinkTag(FieldWrapper fieldWra) {
		StringBuffer tagbuf = new StringBuffer("<");
		try {
			tagbuf.append("emp:link ");

			String tableName = fieldWra.getTableModelName();

			tagbuf.append("id=\"" +Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");
			tagbuf.append("operation=\"view"+tableName+"\" ");

			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}

}
