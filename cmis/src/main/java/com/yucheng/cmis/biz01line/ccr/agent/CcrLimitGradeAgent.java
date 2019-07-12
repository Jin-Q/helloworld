package com.yucheng.cmis.biz01line.ccr.agent;

import java.util.HashMap;

import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 *@Classname	CcrLimitGradeAgent.java
 *@Version 1.0	
 *@Since   1.0 	Jan 8, 2010 
 *@Copyright 	yuchengtech
 *@Author 		eric
 *@Description：
 *@Lastmodified 
 *@Author	    
 */
public class CcrLimitGradeAgent  extends CMISAgent{
	/**
	 * 根据 得到满分值
	 * @param groupNo 组编号
	 * @param indexNo 指标编号
	 * @param conn
	 * @return
	 */
	public String getFullScore(String groupNo,String indexNo) throws AgentException{
		String ind_std_score  = "0";
		String sql = "select ind_std_score from  ind_group_index where group_no='"+groupNo+"' and index_no='"+indexNo+"'";
		HashMap<String,String> hm = this.queryDataOfMapByCondition(sql);
		
		if(hm!=null){
			ind_std_score = hm.get("ind_std_score");
		}
		
		return ind_std_score;
		
	}
}
