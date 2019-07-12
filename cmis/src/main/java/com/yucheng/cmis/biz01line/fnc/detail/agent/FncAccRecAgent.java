package com.yucheng.cmis.biz01line.fnc.detail.agent;


import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.detail.domain.FncAccReceivable;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
  /**
 *@Classname	FncAccRecAgent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 11:45:27 AM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncAccRecAgent extends CMISAgent{
    /**
    * 新增应收账款信息
    * @param domain
    * @return String
    * @throws AgentException
    */
	public String addRecord(FncAccReceivable domain) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//添加信息
		int count = this.insertCMISDomain(domain,
				PUBConstant.FNCACCRECEIVABLE); // 1成功  其他失败

		if (1 == count) {
			//表示成功
			flagInfo = CMISMessage.SUCCESS; //成功
		}
		return flagInfo;
	}
    /**
     * 根据主键删除应收账款信息
     * @param cusId
     * @param fncYm
     * @param fncTyp
     * @param seq
     * @return
     * @throws AgentException
     */
	 public String deleteRecord(String cusId, String fncYm, String fncTyp,int seq) throws AgentException {
		// TODO Auto-generated method stub
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		//把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		String seq_val = String.valueOf(seq);
		pk_values.put("seq", seq_val);
		
        //进行删除操作
		int count = this.removeCMISDomainByKeywords(PUBConstant.FNCACCRECEIVABLE, pk_values);
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}  	  	
		return flagInfo;
}
    /**
     * 根据主键查找应收账款信息
     * @param cusId
     * @param fncYm
     * @param fncTyp
     * @param seq
     * @return FncAccReceivable
     * @throws AgentException
     */
	public FncAccReceivable queryDetail(String cusId, String fncYm, String fncTyp, int seq) throws AgentException{
		// TODO Auto-generated method stub
		FncAccReceivable pfncAccReceivable = new FncAccReceivable();
		
        //把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		String seq_val = String.valueOf(seq);
		pk_values.put("seq", seq_val);
		
		//进行查询操作
		pfncAccReceivable  = (FncAccReceivable) this.findCMISDomainByKeywords(
				pfncAccReceivable , PUBConstant.FNCACCRECEIVABLE, pk_values);

		return pfncAccReceivable ;
	}
    /**
     * 修改应收账款信息
     * @param pfncDetAccRec
     * @return String
     * @throws AgentException
     */
	public String updateRecord(FncAccReceivable pfncDetAccRec) throws AgentException{
		// TODO Auto-generated method stub
		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//更新信息
		int count = this.modifyCMISDomain(pfncDetAccRec,
				PUBConstant.FNCACCRECEIVABLE);// 1成功  其他失败

		if (1 == count) {
			//成功
			flagInfo = CMISMessage.SUCCESS;
		}
		return flagInfo;
	}

}
