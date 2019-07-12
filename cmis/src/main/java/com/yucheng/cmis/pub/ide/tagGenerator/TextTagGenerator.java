package com.yucheng.cmis.pub.ide.tagGenerator;

import com.ecc.emp.ide.table.FieldWrapper;
import com.ecc.ide.builder.jsp.JspTagGenerator;

public class TextTagGenerator implements JspTagGenerator {
	
	private String tagName = "emp:text";

	public TextTagGenerator() {
		
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
			tagbuf.append("id=\"" + tableName+"."+Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");
			
			tagbuf.append("maxlength=\""+fieldWra.getLength()+"\" ");

			boolean pkGen = fieldWra.isPkGenerator();
			if(pkGen){
				tagbuf.append("readonly=\"true\" ");
				tagbuf.append("required=\"false\" ");
			}else{
				if (fieldWra.isCanBeNull())
					tagbuf.append("required=\"false\" ");
				else
					tagbuf.append("required=\"true\" ");
			}
			
			tagbuf.append(Decide.decideNull("dataType", (String)fieldWra.extAttrMap.get("dataType")));
			tagbuf.append(Decide.decideNull("dictname", (String)fieldWra.extAttrMap.get("dictname")));
			
			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}
}
