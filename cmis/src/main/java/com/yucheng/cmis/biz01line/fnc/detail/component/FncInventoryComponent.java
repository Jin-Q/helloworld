package com.yucheng.cmis.biz01line.fnc.detail.component;


import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.fnc.detail.agent.FncInventoryAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncInventory;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname	FncInventoryComponent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午02:33:14  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：财务报表明细信息—主要存货明细业务逻辑处理类Component
 *@Lastmodified 
 *@Author
 */

public class FncInventoryComponent extends CMISComponent{
	
	private CusCom cusCom ;		//引用数据类型：获取客户对象基本信息
	private FncInventory	 fncDetInvty ;	//引用数据类型：获取客户财务报表明细信息—主要存货明细

	/**
	 * 添加财务报表明细信息—主要存货明细
	 * @param FncInventory 财务报表明细信息—主要存货明细
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 */
	public String addFncInventory (FncInventory fncInventory) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）
		
		//创建代理类
		FncInventoryAgent fncAgent = (FncInventoryAgent)this.getAgentInstance("FncInventory");

		/*
		 * 查询数据库中是否存在相同记录，不存在新增，
		 * 存在 抛出异常提示用户
		 */
		FncInventory fncInven = this.findFncInventory(fncInventory.getCusId(), fncInventory.getFncYm(), 
				fncInventory.getFncTyp(), fncInventory.getSeq());
		if(fncInven.getCusId() == null){
			//设置操作员
			fncInventory.setCrtUsrId(this.getUsrId());
			//设置时间
			fncInventory.setCrtDt(this.getCurDate());
			//设置操作员机构
			fncInventory.setRegBchId(this.getUsrBchId());
			//设置更新时间
			fncInventory.setLastUpdTm(this.getCurTimestamp());
			//新增一条成员信息
			flag = fncAgent.addRecord(fncInventory);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您输入的信息已经存在！");
		}
		return flag;
	}
	public String addFncInventory (String cusId,String fncYm,String fncTyp){
		return null;
	}
	/**
	 * 修改财务报表明细信息—主要存货明细
	 * @param FncInventory 财务报表明细信息—主要存货明细
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 * @throws ComponentException
	 */
	public String modifyFncInventory (FncInventory fncInventory) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncInventoryAgent fncAgent = (FncInventoryAgent)this.getAgentInstance("FncInventory");

		//设置更新时间
		fncInventory.setLastUpdTm(this.getCurTimestamp());
		
		//修改一条信息
		flag = fncAgent.updateRecord(fncInventory);

		return flag;
	}
	/**
	 * 删除财务报表明细信息—主要存货明细
	 * @param FncInventory 财务报表明细信息—主要存货明细
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 * @throws ComponentException
	 */
	public String removeFncInventory (FncInventory fncInventory) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncInventoryAgent fncAgent = (FncInventoryAgent)this.getAgentInstance("FncInventory");

		/*
		 * 查询数据库中是否存在相同记录，存在删除，
		 * 不存在 抛出异常提示用户已经被别人删除
		 */
		FncInventory fncInven = this.findFncInventory(fncInventory.getCusId(), fncInventory.getFncYm(), 
				fncInventory.getFncTyp(), fncInventory.getSeq());
		if(fncInven.getCusId() != null){
			//删除一条信息
			flag = fncAgent.deleteRecord(fncInventory.getCusId(), fncInventory.getFncYm(), 
					fncInventory.getFncTyp(), fncInventory.getSeq());
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您所操作的数据已经被删除,请刷新后重试！");
		}

		return flag;
	}
	public String removeFncInventory (String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncInventoryAgent fncAgent = (FncInventoryAgent)this.getAgentInstance("FncInventory");

		/*
		 * 查询数据库中是否存在相同记录，存在删除，
		 * 不存在 抛出异常提示用户已经被别人删除
		 */
		FncInventory fncInven = this.findFncInventory(cusId, fncYm, fncTyp, seq);
		if(fncInven.getCusId() != null){
			//删除一条信息
			flag = fncAgent.deleteRecord(cusId, fncYm, fncTyp, seq);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您所操作的数据已经被删除,请刷新后重试！");
		}

		return flag;
	}
	/**
	 * 查询财务报表明细信息—主要存货明细
	 * @param cusId 客户代码
	 * @param fncYm 年月
	 * @param fncTyp 报表周期类型1
	 * @param seq 序号
	 * @return  FncInventory 财务报表明细信息—主要存货明细
	 * @throws ComponentException
	 */
	public FncInventory findFncInventory (String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		
		FncInventory fncInven = new FncInventory();

		//创建代理类
		FncInventoryAgent fncAgent = (FncInventoryAgent) this.getAgentInstance("FncInventory");
			
		//通过代理类查找信息
		fncInven = fncAgent.queryDetail(cusId, fncYm, fncTyp, seq);

		return fncInven;
	}
}
