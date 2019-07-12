package com.yucheng.cmis.platform.permission.domain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.yucheng.cmis.util.CMISJSONUtil;


public class CMISResource {
	
	//资源ID
	public String resourceId;
	
	//中文名称
	public String cnName;
	
	//系统主键
	public String systemid;
	
	//资源URL
	public String url;
	
	//上级资源ID
	public String parentId;
	
	//关联表名
	public String tableName;
	
	//关联工作流流程标识
	public String procId;
	
	//排序
	public String orderId;
	
	//备注
	public String memo;
	
	//资源图标
	public String icon;
	
	//所有子资源节点
	public List childs;
	
	//所有的操作
	public List acts;
	
	public String resourcelevel;
	
	
	public void addCMISResource(CMISResource resource){
		if(this.childs == null)
			this.childs = new LinkedList();
		this.childs.add(resource);
	}
	
	public void addCMISAct(CMISAct act){
		if(this.acts == null)
			this.acts = new LinkedList();
		this.acts.add(act);
	}
	
	public CMISAct getCMISAct(String actId){
		if(this.acts == null || this.acts.size() == 0)
			return null;
		for(int i=0;i<this.acts.size();i++){
			CMISAct act = (CMISAct)this.acts.get(i);
			if(actId.equals(act.actId))
				return act;
		}
		return null;
	}
	
	public CMISResource getChild(String resourceId){
		if(this.childs == null || this.childs.size() == 0)
			return null;
		for(int i=0;i<this.childs.size();i++){
			CMISResource child = (CMISResource)this.childs.get(i);
			if(resourceId.equals(child.resourceId)){
				return child;
			}
		}
		return null;
	}
	
	public String toString(int tabCount){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < tabCount; i++)
			buf.append("\t");
		buf.append("<CMISResource id=\""+this.resourceId+"\"");
		buf.append(" name=\""+this.cnName+"\"");
		if(this.parentId != null)
			buf.append(" parentId=\""+this.parentId+"\"");
		if(this.tableName != null)
			buf.append(" tableName=\""+this.tableName+"\"");
		if(this.procId != null)
			buf.append(" procId=\""+this.procId+"\"");
		if(this.orderId != null)
			buf.append(" orderId=\""+this.orderId+"\"");
		buf.append(" systemid=\""+this.systemid+"\"");
		buf.append(" url=\""+this.url+"\"");
		int actCount = 0;
		if(this.acts != null)
			actCount = this.acts.size();
		buf.append(" actCount=\""+actCount+"\"");
		buf.append(">\n");
		
		if(this.childs != null){
			for(int i=0;i<this.childs.size();i++){
				CMISResource resource = (CMISResource)this.childs.get(i);
				buf.append(resource.toString((tabCount+1)));
				buf.append("\n");
			}
		}
		for (int i = 0; i < tabCount; i++)
			buf.append("\t");
		buf.append("</CMISResource>");
		return buf.toString();
	}
	
	@Override
	public String toString(){
		return this.toString(0);
	}
	
	public String toJSONString(){
		StringBuffer buf = new StringBuffer();
		buf.append("{id:'").append(CMISJSONUtil.normalizeString(this.resourceId));
		buf.append("',label:'").append(CMISJSONUtil.normalizeString(this.cnName)).append("'");
		if(this.url != null)
			buf.append(",action:'").append(CMISJSONUtil.normalizeString(this.url)).append("'");
		if(this.acts != null){
			buf.append(",operations:[");
			Iterator i = this.acts.iterator();
	        boolean hasNext = i.hasNext();
	        while (hasNext) {
	            CMISAct act = (CMISAct)i.next();
	            hasNext = i.hasNext();
	            buf.append(act.toJSONString());
	            if (hasNext)
	                buf.append(",");
	        }
	        buf.append("]");
		}
		if(this.childs != null){
			
			buf.append(",children:[");
			Iterator child = this.childs.iterator();
	        boolean hasNext = child.hasNext();
	        while (hasNext) {
	        	CMISResource resource = (CMISResource)child.next();
	        	hasNext = child.hasNext();
	            buf.append(resource.toJSONString());
	            if (hasNext)
	                buf.append(",");
	        }
	        buf.append("]");
		}
		buf.append("}");
		
		return buf.toString();
	}
}
