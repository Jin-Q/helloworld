package com.yucheng.cmis.pub.sequence;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.service.EMPService;
import com.yucheng.cmis.pub.sequence.template.AllSequenceTemplate;


public abstract class CMISSequenceService extends EMPService {
	//所有配置的模板
	private HashMap templates;
	
	public String getSequence(String aType, String owner, Context context, Connection connection) throws EMPException{
		if(aType == null || "".equals(aType))
			throw new EMPException("The aType should not be null for CMISSequenceService!");
		if(owner == null || "".equals(owner))
			throw new EMPException("The owner should not be null for CMISSequenceService!");
		
		CMISSequenceTemplate template = (CMISSequenceTemplate)this.templates.get(aType);
		if(template == null)
			return null;
		String sequence = this.querySequenceFromDB(aType, owner, connection);
		
		return template.format(owner, sequence, context);
	}
	
	abstract protected String querySequenceFromDB(String aType, String owner, Connection connection) throws EMPException;
	
	public void addCMISSequenceTemplate(CMISSequenceTemplate template){
		if(this.templates == null)
			this.templates = new HashMap();
 
		this.templates.put(template.getAType(), template);
	}
	
	/**
	 * 根据传入的前缀,长度获取标准的序列号
	 * @author 娄建成
	 * @param  fromType（fromDate：根据日期，fromOrg：根据机构）
	 * @param  beforeString（前缀）
	 * @param  length（长度）
	 * @param  context
	 * @param  connection
	 * @return
	 * @throws EMPException 
	 */
	public String getSequence(String beforeString, String fromType, int length,
			Context context) throws EMPException {
		if(beforeString == null || "".equals(beforeString))
			throw new EMPException("生成流水号发生错误，可能没有传入流水号前缀！");
		if(fromType == null || "".equals(fromType))
			throw new EMPException("生成流水号发生错误，可能没有传入格式化来源fromType！");
		
		AllSequenceTemplate template = new AllSequenceTemplate();
		if(template == null)
			return null;
		String sequence = template.querySequenceFromDB(beforeString, fromType, context);
		
		return template.format(beforeString,fromType, sequence,length, context);
	}
	
	
}
