package com.yucheng.cmis.pub.domain;

import java.sql.Connection;
import java.util.ArrayList;

import com.ecc.emp.core.Context;

/**
 * 单个序列服务实体类
 *    
 * @Version bsbcmis
 * @author wuming 2012-8-31 
 * Description:
 */
public class CmisSequence {

	/**
	 * 序列配置id
	 */
    private String id;
    
    private int maxLen;
    
    
    private ArrayList<SeqTemDataSupport> list = new ArrayList<SeqTemDataSupport>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

	public ArrayList<SeqTemDataSupport> getList() {
		return list;
	}

	public void setList(ArrayList<SeqTemDataSupport> list) {
		this.list = list;
	}
	
	public void setSeqTemDataSupport(SeqTemDataSupport data){
		if(list == null)list = new ArrayList<SeqTemDataSupport>();
		list.add(data);
	}
    
	/**
	 * 获取序列服务生成的 格式化 序列号
	 * @param context
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public String getFormatSeqStr(Context context,Connection con)throws Exception{
		if(list == null || list.size() == 0){
			throw new Exception("错误:序列id["+this.id+"]为配置实际的格式化子项目");
		}
		StringBuilder sb = new StringBuilder();
		
		for(SeqTemDataSupport support:list){
			sb.append(support.getDataValue(this.id,context,con));
		}
		
		//如果配置了 该序列服务的最大长度 那么需要校验一次
		if(maxLen > 0 && sb.length()>maxLen ){
			throw new Exception("序列服务错误：生成的序列号["+sb.toString()+"]超出序列id["+this.id+"]规定的最大长度");
		}
		
		return sb.toString();
	}
}
