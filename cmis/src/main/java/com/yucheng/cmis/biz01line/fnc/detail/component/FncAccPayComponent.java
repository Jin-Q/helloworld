package com.yucheng.cmis.biz01line.fnc.detail.component;


import java.util.List;

import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.fnc.detail.agent.FncAccPayAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncAccPayable;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
  /**
 *@Classname	FncAccPayComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 11:43:00 AM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncAccPayComponent extends CMISComponent{
	private	CusCom  cusCom;	
	private FncAccPayable fncDetAccPay;
	
	public String addFncDetAccPay (String cusId,String fncYm,String fcnTyp)throws ComponentException {
		return null;
	}
	/**
	 * 新增信息
	 * @param pFncDetAccPay
	 * @return String
	 * @throws ComponentException
	 */
    public String addFncDetAccPay (FncAccPayable pFncDetAccPay)throws ComponentException {
    	String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncAccPayAgent fncAccPayAgent = (FncAccPayAgent)this.getAgentInstance(PUBConstant.FNCACCPAYABLE);
		
        /**
         * 查找该条信息是否已存在
         * 不存在新增，存在则 抛出异常提示用户
         */
		String cusId = pFncDetAccPay.getCusId(); 
		String fncYm = pFncDetAccPay.getFncYm();
		String fncTyp = pFncDetAccPay.getFncTyp();
		int seq = pFncDetAccPay.getSeq();
		FncAccPayable pfncAccPayable = fncAccPayAgent.queryDetail(cusId, fncYm, fncTyp, seq);
		if(pfncAccPayable.getCusId() == null){
            //设置时间
			pFncDetAccPay.setCrtDt(this.getCurDate()); 
			//设置更新时间
			pFncDetAccPay.setLastUpdTm(this.getCurTimestamp());
			//设置操作人
			pFncDetAccPay.setCrtUsrId(this.getUsrId());
		    // 新增一条信息
		    flagInfo = fncAccPayAgent.addRecord(pFncDetAccPay);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"你输入的信息已经存在！");
		}
		return flagInfo;
	};
    /**
     * 修改信息
     * @param pFncDetAccPay
     * @return String
     * @throws ComponentException
     */
    public String modifyFncDetAccPay(FncAccPayable pFncDetAccPay)throws ComponentException {
    	String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncAccPayAgent fncAccPayAgent = (FncAccPayAgent) this
		    .getAgentInstance("FncAccPayable");
        //设置更新时间
		 pFncDetAccPay.setLastUpdTm(this.getCurTimestamp());
		// 通过代理类进行修改操作
		flagInfo = fncAccPayAgent.updateRecord(pFncDetAccPay);

		return flagInfo;
	};
    public String removeFncDetAccPay(FncAccPayable pFncDetAccPay)throws ComponentException {
		return null;
	}
    /**
     * 通过主键删除一条信息
     * @param cusId
     * @param fncYm
     * @param fcnTyp
     * @param seq
     * @return String
     * @throws ComponentException
     */
    public String removeFncDetAccPay(String cusId,String fncYm,String fcnTyp,int seq)
                     throws ComponentException {
    	String flagInfo = CMISMessage.DEFEAT;
    	
		// 创建业务代理类
		FncAccPayAgent fncAccPayAgent = (FncAccPayAgent) this
		          .getAgentInstance("FncAccPayable");
		
		// 通过代理类进行删除操作
		flagInfo = fncAccPayAgent.deleteRecord(cusId,fncYm,fcnTyp,seq);
		return flagInfo;
	};
	/**
	 * 通过主键查询一条信息
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return String
	 * @throws ComponentException
	 */
    public FncAccPayable findFncDetAccPay(String cusId,String fncYm,String fncTyp,int seq)throws ComponentException {
    	FncAccPayable pfncAccPay = new FncAccPayable();
    	
		// 创建业务代理类
    	FncAccPayAgent fncAccPayAgent = (FncAccPayAgent)this.getAgentInstance("FncAccPayable");
    	
		// 通过代理类进行查看操作
    	pfncAccPay = fncAccPayAgent.queryDetail(cusId,fncYm,fncTyp,seq);

    	return pfncAccPay;
	};
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
	public FncAccPayable getFncDetAccPay() {
		return fncDetAccPay;
	}
	public void setFncDetAccPay(FncAccPayable fncDetAccPay) {
		this.fncDetAccPay = fncDetAccPay;
	}
}
