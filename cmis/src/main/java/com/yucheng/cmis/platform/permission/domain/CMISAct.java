package com.yucheng.cmis.platform.permission.domain;

import com.yucheng.cmis.util.CMISJSONUtil;

public class CMISAct {

	//资源ID
	public String resourceId;
	
	//操作ID
	public String actId;
	
	//描述
	public String descr;
	
	//标志
	public String flag;
	
	//提示确认信息
	public String confirmMsg;

	public String toString(int tabCount){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < tabCount; i++)
			buf.append("\t");
		
		buf.append("<CMISAct actId=\""+this.actId+"\"");
		buf.append(" resourceId=\""+this.resourceId+"\"");
		if(this.descr != null)
			buf.append(" descr=\""+this.descr+"\"");
		if(this.flag != null)
			buf.append(" flag=\""+this.flag+"\"");
		if(this.confirmMsg != null)
			buf.append(" confirmMsg=\""+this.confirmMsg+"\"");
		
		buf.append("/>");
		
		return buf.toString();
	}
	
	@Override
	public String toString(){
		return this.toString(0);
	}
	
	public String toJSONString(){
		StringBuffer buf = new StringBuffer();
		buf.append("{id:'").append(CMISJSONUtil.normalizeString(this.actId));
		buf.append("',label:'").append(CMISJSONUtil.normalizeString(this.descr));
		buf.append("'}");
		return buf.toString();
	}
}
