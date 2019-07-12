package com.yucheng.cmis.biz01line.fnc.detail.component;


import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.fnc.detail.agent.FncInvestmentAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncInvestment;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname	FncInvestmentComponent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午02:45:02  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：财务报表明细信息—主要长期投资明细业务逻辑处理类Component
 *@Lastmodified 
 *@Author
 */

public class FncInvestmentComponent extends CMISComponent{
	
	private CusCom cusCom;			//引用数据类型：获取客户对象基本信息
	private FncInvestment fncDetInvest ;		//引用数据类型：获取客户财务报表明细信息—主要长期投资明细

	/**
	 * 添加财务报表明细信息—主要长期投资明细
	 * @param fncInvestment 财务报表明细信息—主要长期投资明细
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 * @throws ComponentException
	 */
	public String addFncInvestment (FncInvestment fncInvestment)throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）
		
		//创建代理类
		FncInvestmentAgent fncAgent = (FncInvestmentAgent)this.getAgentInstance("FncInvestment");

		/*
		 * 查询数据库中是否存在相同记录，不存在新增，
		 * 存在 抛出异常提示用户
		 */
		FncInvestment fncInvest = this.findFncInvestment(fncInvestment.getCusId(), fncInvestment.getFncYm(), 
				fncInvestment.getFncTyp(), String.valueOf(fncInvestment.getSeq()));
		if(fncInvest.getCusId() == null){
			//设置操作员
			fncInvestment.setCrtUsrId(this.getUsrId());
			//设置时间
			fncInvestment.setCrtDt(this.getCurDate());
			//设置操作员机构
			fncInvestment.setRegBchId(this.getUsrBchId());
			//设置更新时间
			fncInvestment.setLastUpdTm(this.getCurTimestamp());
			//新增一条成员信息
			flag = fncAgent.addRecord(fncInvestment);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您输入的信息已经存在！");
		}
		return flag;
	}
	public String addFncInvestment (String cusId,String fncYm,String fcnTyp){
		return null;
	}
	/**
	 * 修改财务报表明细信息—主要长期投资明细
	 * @param fncInvestment
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 * @throws ComponentException
	 */
	public String modifyFncInvestment (FncInvestment fncInvestment)throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncInvestmentAgent cusAgent = (FncInvestmentAgent)this.getAgentInstance("FncInvestment");

		//设置更新时间
		fncInvestment.setLastUpdTm(this.getCurTimestamp());
		
		//修改一条信息
		flag = cusAgent.updateRecord(fncInvestment);

		return flag;
	}
	/**
	 * 删除财务报表明细信息—主要长期投资明细
	 * @param fncInvestment
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 * @throws ComponentException
	 */
	public String removeFncInvestment (FncInvestment fncInvestment)throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncInvestmentAgent fncAgent = (FncInvestmentAgent)this.getAgentInstance("FncInvestment");

		/*
		 * 查询数据库中是否存在相同记录，存在删除，
		 * 不存在 抛出异常提示用户已经被别人删除
		 */
		FncInvestment fncInvest = this.findFncInvestment(fncInvestment.getCusId(), fncInvestment.getFncYm(), 
				fncInvestment.getFncTyp(), String.valueOf(fncInvestment.getSeq()));
		if(fncInvest.getCusId() != null){
			//删除一条信息
			flag = fncAgent.deleteRecord(fncInvestment.getCusId(), fncInvestment.getFncYm(), 
					fncInvestment.getFncTyp(), String.valueOf(fncInvestment.getSeq()));
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您所操作的数据已经被删除,请刷新后重试！");
		}

		return flag;
	}
	public String removeFncInvestment (String cusId,String fncYm,String fncTyp,String seq) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncInvestmentAgent fncAgent = (FncInvestmentAgent)this.getAgentInstance("FncInvestment");

		/*
		 * 查询数据库中是否存在相同记录，存在删除，
		 * 不存在 抛出异常提示用户已经被别人删除
		 */
		FncInvestment fncInvest = this.findFncInvestment(cusId, fncYm, fncTyp, seq);
		if(fncInvest.getCusId() != null){
			//删除一条信息
			flag = fncAgent.deleteRecord(cusId, fncYm, fncTyp, seq);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您所操作的数据已经被删除,请刷新后重试！");
		}

		return flag;
	}
	/**
	 * 查询财务报表明细信息—主要长期投资明细
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return  fncInvestment财务报表明细信息—主要长期投资明细
	 * @throws ComponentException
	 */
	public FncInvestment findFncInvestment (String cusId,String fncYm,String fncTyp,String seq)throws ComponentException{
		
		FncInvestment fncInvest = new FncInvestment();

		//创建代理类
		FncInvestmentAgent fncAgent = (FncInvestmentAgent) this.getAgentInstance("FncInvestment");
			
		//通过代理类查找信息
		fncInvest = fncAgent.queryDetail(cusId, fncYm, fncTyp, seq);

		return fncInvest;
	}
}
