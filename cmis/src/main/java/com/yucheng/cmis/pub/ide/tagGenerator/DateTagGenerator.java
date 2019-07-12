//创建日期：2008-9-18
package com.yucheng.cmis.pub.ide.tagGenerator;

import com.ecc.emp.ide.table.FieldWrapper;
import com.ecc.ide.builder.jsp.JspTagGenerator;

public class DateTagGenerator implements JspTagGenerator{

	private String tagName = "emp:date";
	
	public DateTagGenerator() {

	}
 
	public String generateTag(FieldWrapper fieldWra) {
		StringBuffer tagbuf = new StringBuffer("<");
		try {
			tagbuf.append(tagName + " ");

			String tableName = "";
			if(fieldWra.getPrimaryTableName()!=null){
				tableName = fieldWra.getUniteName();
			}else{
				tableName = fieldWra.getTableModelName();
			}

			tagbuf.append("id=\"" + tableName + "." + Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");

			if (fieldWra.isCanBeNull())
				tagbuf.append("required=\"false\" ");
			else
				tagbuf.append("required=\"true\" ");
			
			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}

}
