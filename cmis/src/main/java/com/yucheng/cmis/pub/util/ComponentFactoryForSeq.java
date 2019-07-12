package com.yucheng.cmis.pub.util;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ecc.emp.component.factory.ComponentFactory;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.domain.CmisSequence;

/**
 * 解析序列文件的组件工厂类
 * 
 * @Version bsbcmis
 * @author wuming 2012-8-30 Description:
 */
public class ComponentFactoryForSeq extends ComponentFactory {

	/**
	 * 查找指定标签名的 nodelist 对象
	 * @param tagName
	 * @return
	 */
	public NodeList findElementNodeByTagName(String tagName) {

		return this.document.getElementsByTagName(tagName);
	}

	/**
	 * 根据指定的节点获取指定节点的实例化对象
	 *   该解析方法会自动注入属性以及子节点对象
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public Object getComponent(Node node) throws Exception{
		
		if (this.componentParser != null) {
			Object bean = this.componentParser.parseTheElement(this.document, node);
			return bean;
		}
		return null;
	}
	
	/**
	 * 根据指定的标签名解析当前文档的所有bean对象存放到传入的map中
	 * @param map
	 * @param tagName
	 */
	public void parseBeansByTagName(HashMap<String,CmisSequence> map,String tagName)throws Exception{
		
		NodeList nodeList = this.findElementNodeByTagName(tagName);
		
		if(nodeList.getLength() == 0){
			EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "根据标签名["+tagName+"]获取子节点数目为0");
		    return;
		}
		Node node;
		for(int i=0;i<nodeList.getLength();i++){
			node = nodeList.item(i);
			
			Object obj = this.getComponent(node);
			String id = null;
			if(obj instanceof CmisSequence){
				if(StringUtils.isBlank(((CmisSequence)obj).getId())){
					EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, node.getNodeName()+"未配置id属性，无法实例化");
					continue;
				}
				id = ((CmisSequence)obj).getId();
				if(map.containsKey(id)){
					EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "序列服务id["+id+"]已经在配置文件中存在！");
					continue;
				}
				
				map.put(id, (CmisSequence)obj);
			}else{
				EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, node.getNodeName()+"不是序列服务类CmisSequence对象");
				continue;
			}
		}
		
	}
}
