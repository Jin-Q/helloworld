package com.yucheng.cmis.pub.ide.tagGenerator;

import com.ecc.emp.ide.table.FieldWrapper;
import com.ecc.ide.builder.jsp.JspTagGenerator;

public class HiddenTableItemTagGenerator implements JspTagGenerator {

	public String generateTag(FieldWrapper fieldWra) {
		
		StringBuffer tagbuf = new StringBuffer("<");
		try {
			tagbuf.append("emp:text ");

			tagbuf.append("id=\"" + Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");
			//tagbuf.append(Decide.decideNull("dataType", fieldWra.getDataType()));
			tagbuf.append(Decide.decideNull("dictname", (String)fieldWra.extAttrMap.get("dictname")));
			tagbuf.append("hidden=\"true\"");
			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}
}
