package com.yucheng.cmis.biz01line.fnc.detail.component;


import java.util.List;

import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.fnc.detail.agent.FncOthRecAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncOthReceive;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.ComponentException;
  /**
 *@Classname	FncOthRecComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 11:43:41 AM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncOthRecComponent extends CMISComponent{

	private	CusCom  cusCom;	
	private FncOthReceive fncDetOthRec;
	
	public String addFncDetOthRec(String cusId,String fncYm,String fcnTyp)throws ComponentException {
		return null;
	}
	
	/**
	 * 新增其它应收款信息
	 * @param pFncDetOthRec
	 * @return String
	 * @throws ComponentException
	 */
    public String addFncDetOthRec(FncOthReceive pFncDetOthRec)throws ComponentException {
            String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)
            
            //构建业务代理类
            FncOthRecAgent foRecAgent = (FncOthRecAgent)this.getAgentInstance("FncOthRec");
            
            /**
             * 查找该条信息是否已存在
             * 不存在新增，存在则 抛出异常提示用户
             */
    		String cusId = pFncDetOthRec.getCusId(); 
    		String fncYm = pFncDetOthRec.getFncYm();
    		String fncTyp =pFncDetOthRec.getFncTyp();
    		int seq = pFncDetOthRec.getSeq();
    		FncOthReceive fncAccRec = foRecAgent.queryDetail(cusId, fncYm, fncTyp, seq);
    		if(fncAccRec.getCusId() == null){
                //设置时间
    			pFncDetOthRec.setCrtDt(this.getCurDate()); 
    			//设置更新时间
    			pFncDetOthRec.setLastUpdTm(this.getCurTimestamp());
    			//设置操作人
    			pFncDetOthRec.setCrtUsrId(this.getUsrId());
                //新增一条信息
                flagInfo = foRecAgent.addRecord(pFncDetOthRec);
    		}else{
    			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"你输入的信息已经存在！");
    		}
                        
            return flagInfo;
	}
    /**
     * 修改其它应收款信息
     * @param pFncDetOthRec
     * @return String
     * @throws ComponentException
     */
    public String modifyFncDetOthRec(FncOthReceive pFncDetOthRec)throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT;
		
        //构建业务代理类
		FncOthRecAgent fncOthRecAgent = (FncOthRecAgent)this.getAgentInstance("FncOthRec");
        //设置更新时间
		pFncDetOthRec.setLastUpdTm(this.getCurTimestamp());
        //修改信息
		flagInfo = fncOthRecAgent.updateRecord(pFncDetOthRec);
		
		return flagInfo;
	}
    public String removeFncDetOthRec(FncOthReceive pFncDetOthRec)throws ComponentException {
		return null;
	}
    public String removeFncDetOthRec(String cusId,String fncYm,String fcnTyp)throws ComponentException {
		return null;
	}
    /**
     * 根据主键查找其它应收款信息
     * @param cusId
     * @param fncYm
     * @param fncTyp
     * @param seq
     * @return FncOthReceive
     * @throws ComponentException
     */
    public FncOthReceive findFncDetAccPay(String cusId,String fncYm,String fncTyp,int seq)throws ComponentException {
    	
    	FncOthReceive pfncOthReceive = new FncOthReceive(); 
    	
        //创建业务代理类
    	FncOthRecAgent fncOthRecAgent = (FncOthRecAgent)this.getAgentInstance("FncOthRec");
    	
        //通过代理类进行查看操作
    	pfncOthReceive = fncOthRecAgent.queryDetail(cusId,fncYm,fncTyp,seq);
    	
    	return pfncOthReceive;
	}
    public List findFncDetAccPay(String cusId)throws ComponentException {
		return null;
	}
    public List findFncDetAccPayByTyp(String cusId, String fcnTyp)throws ComponentException {
		return null;
	}
    public List findFncDetAccPayByYmAndTyp(String cusId,String fncYm,String fcnTyp)throws ComponentException {
		return null;
	}
	public CusCom getCusCom() {
		return cusCom;
	}
	public void setCusCom(CusCom cusCom) {
		this.cusCom = cusCom;
	}
	public FncOthReceive getFncDetOthRec() {
		return fncDetOthRec;
	}
	public void setFncDetOthRec(FncOthReceive fncDetOthRec) {
		this.fncDetOthRec = fncDetOthRec;
	}
	/**
	 * 根据主键删除其它应收款信息
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return String
	 * @throws ComponentException
	 */
	public String removeFncDetOthRec(String cusId,String fncYm,String fncTyp,int seq) throws ComponentException {
		// TODO Auto-generated method stub
	    String flagInfo = CMISMessage.DEFEAT;  //错误编码信息
	    
	    //创建业务代理类
		FncOthRecAgent fncOthRecAgent = (FncOthRecAgent)this.getAgentInstance("FncOthRec");
		
		 //通过代理类进行删除操作
		flagInfo = fncOthRecAgent.deleteRecord(cusId,fncYm,fncTyp,seq);
		
		return flagInfo;
		
	}
	
}
