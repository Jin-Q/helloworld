package com.yucheng.cmis.pub.ide.tagGenerator;

import com.ecc.emp.ide.table.FieldWrapper;
import com.ecc.ide.builder.jsp.JspTagGenerator;

public class ConditionTagGenerator implements JspTagGenerator {

	public String generateTag(FieldWrapper fieldWra) {
		if ("select".equals(fieldWra.getJSPTag()))
			return generateSelectTag(fieldWra);
		else if("date".equals(fieldWra.getJSPTag())){
			return generateDateTag(fieldWra);
		}else
			return generateTextTag(fieldWra);
	}
	 
	public String generateTextTag(FieldWrapper fieldWra) {
		StringBuffer tagbuf = new StringBuffer("<");
		try {
			if(fieldWra.interzoneCon){
				tagbuf.append("emp:textspace ");
			}else{
				tagbuf.append("emp:text ");
			}

			tagbuf.append("id=\"" + fieldWra.tableModelName+"."+Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");
			tagbuf.append(Decide.decideNull("dataType", (String)fieldWra.extAttrMap.get("dataType")));
			//tagbuf.append(Decide.decideNull("dictname", fieldWra.getDict()));
			
			if(fieldWra.interzoneCon){
				tagbuf.append("colSpan=\"2\"");
			}
			
			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}
	
	public String generateDateTag(FieldWrapper fieldWra){
		
		StringBuffer tagbuf = new StringBuffer("<");
		try {
			if(fieldWra.interzoneCon){
				tagbuf.append("emp:datespace ");
			}else{
				tagbuf.append("emp:date ");
			}

			String tableName = fieldWra.getTableModelName();

			tagbuf.append("id=\"" + tableName + "." + Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");

			if(fieldWra.interzoneCon){
				tagbuf.append("colSpan=\"2\"");
			}
			
			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}
	
	public String generateSelectTag(FieldWrapper fieldWra){
		StringBuffer tagbuf = new StringBuffer("<");
		try {
			tagbuf.append("emp:select ");

			tagbuf.append("id=\"" + fieldWra.tableModelName+"."+ Decide.decideNull(fieldWra.getId()) + "\" ");
			tagbuf.append("label=\"" + Decide.decideNull(fieldWra.getCnname()) + "\" ");
			tagbuf.append(Decide.decideNull("dictname", (String)fieldWra.extAttrMap.get("dictname")));

			tagbuf.append("/>");
			return tagbuf.toString();

		} catch (Exception e) {
		}
		return "<ERROR />";
	}

}
