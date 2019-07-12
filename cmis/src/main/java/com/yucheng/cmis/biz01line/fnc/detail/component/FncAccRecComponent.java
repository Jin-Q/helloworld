package com.yucheng.cmis.biz01line.fnc.detail.component;

import java.util.List;

import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.fnc.detail.agent.FncAccRecAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncAccReceivable;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.ComponentException;
  /**
 *@Classname	FncAccRecComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 11:42:12 AM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncAccRecComponent extends CMISComponent{

	private	CusCom  cusCom;	
	private FncAccReceivable fncDetAccRec;
	
	public String addFncDetAccRec(String cusId,String fncYm,String fcnTyp)throws ComponentException {
		return null;
	}
   /**
    * 修改应收账款信息
    * @param pFncDetAccRec
    * @return String 
    * @throws ComponentException
    */
    public String modifyFncDetAccRec(FncAccReceivable pFncDetAccRec)throws ComponentException {
    	String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
    	FncAccRecAgent fncAccRecAgent = (FncAccRecAgent) this
		.getAgentInstance("FncAccRec");
        //设置更新时间
    	pFncDetAccRec.setLastUpdTm(this.getCurTimestamp());
		// 通过代理类进行修改操作
		flagInfo = fncAccRecAgent.updateRecord(pFncDetAccRec);

		return flagInfo;
	};
    public String removeFncDetAccRec(FncAccReceivable pFncDetAccRec)throws ComponentException {
		return null;
	}
    /**
     * 通过主键删除应收账款信息
     * @param cusId
     * @param fncYm
     * @param fcnTyp
     * @param seq
     * @return String
     * @throws ComponentException
     */
    public String removeFncDetAccRec(String cusId,String fncYm,String fcnTyp,int seq)throws ComponentException {
    	String flagInfo = CMISMessage.DEFEAT;   //错误编码信息
    	
		// 创建业务代理类
		FncAccRecAgent fncAccRecAgent = (FncAccRecAgent) this
		    .getAgentInstance("FncAccRec");
		
		// 通过代理类进行删除操作
		flagInfo = fncAccRecAgent.deleteRecord(cusId,fncYm,fcnTyp,seq);
		return flagInfo;
	};
	/**
	 * 通过主键查询应收账款信息
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return FncAccReceivable
	 * @throws ComponentException
	 */
    public FncAccReceivable findFncDetAccRec(String cusId,String fncYm,String fncTyp,int seq)throws ComponentException {

		FncAccReceivable pfncAccReceivable = new FncAccReceivable();
		// 创建业务代理类
		FncAccRecAgent fncAccRecAgent = (FncAccRecAgent) this
		.getAgentInstance("FncAccRec");
		// 通过代理类进行查看操作
		pfncAccReceivable = fncAccRecAgent.queryDetail(cusId,fncYm, fncTyp, seq);

		return pfncAccReceivable;

	};
    public List findFncDetAccRec(String cusId)throws ComponentException {
		return null;
	}
    public List findFncDetAccRecByTyp(String cusId, String fcnTyp)throws ComponentException {
		return null;
	}
    public List findFncDetAccRecByYmAndTyp(String cusId,String fncYm,String fcnTyp)throws ComponentException {
		return null;
	}

	public CusCom getCusCom() {
		return cusCom;
	}
	public void setCusCom(CusCom cusCom) {
		this.cusCom = cusCom;
	}
	public FncAccReceivable getFncDetAccRec() {
		return fncDetAccRec;
	}
	public void setFncDetAccRec(FncAccReceivable fncDetAccRec) {
		this.fncDetAccRec = fncDetAccRec;
	}
	/**
	 * 新增应收账款信息
	 * @param domain
	 * @return String
	 * @throws ComponentException 
	 */
	public String addFncAccReceivable(FncAccReceivable pfncAccRec) throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncAccRecAgent fncAccRecAgent =(FncAccRecAgent)this.getAgentInstance("FncAccRec");
		
		/**
         * 查找该条信息是否已存在
         * 不存在新增，存在则 抛出异常提示用户
         */
		String cusId = pfncAccRec.getCusId(); 
		String fncYm = pfncAccRec.getFncYm();
		String fncTyp = pfncAccRec.getFncTyp();
		int seq = pfncAccRec.getSeq();
		FncAccReceivable pfncAccRecable = fncAccRecAgent.queryDetail(cusId, fncYm, fncTyp, seq);
		if(pfncAccRecable.getCusId() == null){
            //设置时间
			pfncAccRecable.setCrtDt(this.getCurDate()); 
			//设置更新时间
			pfncAccRecable.setLastUpdTm(this.getCurTimestamp());
			//设置操作人
			pfncAccRecable.setCrtUsrId(this.getUsrId());
		    // 新增一条信息
			flagInfo = fncAccRecAgent.addRecord(pfncAccRec);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"你输入的信息已经存在！");
		}
				
		return flagInfo;
	};
}
