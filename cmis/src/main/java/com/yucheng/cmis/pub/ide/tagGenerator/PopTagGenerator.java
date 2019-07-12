//创建日期：2008-9-18
package com.yucheng.cmis.pub.ide.tagGenerator;

import com.ecc.emp.ide.table.FieldWrapper;
import com.ecc.ide.builder.jsp.JspTagGenerator;

public class PopTagGenerator implements JspTagGenerator{
	
	private String tagName = "emp:pop";

	public PopTagGenerator() {
		
	}
 
	public String generateTag(FieldWrapper fieldWra) {
		StringBuffer tagbuf = new StringBuffer("<");
		try {
			tagbuf.append(tagName + " ");

			String tableName = fieldWra.getTableModelName();

			tagbuf.append("id=\"" + tableName+"."+Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");
			
			//tagbuf.append("maxlength=\""+fieldWra.getLength()+"\" ");
			
			String url = (String)fieldWra.extAttrMap.get("url");
			tagbuf.append("url=\"" + url + "\" ");

			String returnmethod = (String)fieldWra.extAttrMap.get("returnmethod");
			if(returnmethod != null)
			   tagbuf.append("returnMethod=\"" + returnmethod + "\" ");

			String datamapping = (String)fieldWra.extAttrMap.get("datamapping");
			if(datamapping != null)
			   tagbuf.append("dataMapping=\"" + datamapping + "\" ");
			
			if (fieldWra.isCanBeNull())
				tagbuf.append("required=\"false\" ");
			else
				tagbuf.append("required=\"true\" ");
			//tagbuf.append("dataType=\""+Decide.decideNull(fieldWra.getDataType())+"\" ");
			//tagbuf.append("dictname=\""+Decide.decideNull(fieldWra.getDict())+"\" ");
			
			
			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}
}
