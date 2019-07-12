package com.yucheng.cmis.biz01line.fnc.detail.component;

import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.fnc.detail.agent.FncFixedAssetAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncFixedAsset;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname	FncFixedAssetComponent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午02:45:18  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：财务报表明细信息—主要固定资产明细业务逻辑处理类Component
 *@Lastmodified 
 *@Author
 */

public class FncFixedAssetComponent extends CMISComponent{
	
	private CusCom cusCom ;			//引用数据类型：获取客户对象基本信息
	private FncFixedAsset fncDetAsset ;		//引用数据类型：获取客户财务报表明细信息—主要固定资产明细
	
	/**
	 * 添加财务报表明细信息—主要固定资产明细
	 * @param fncFixedAsset 财务报表明细信息—主要固定资产明细
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 */
	public String addFncFixedAsset (FncFixedAsset fncFixedAsset) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）
		
		//创建代理类
		FncFixedAssetAgent fncAgent = (FncFixedAssetAgent)this.getAgentInstance("FncFixedAsset");

		/*
		 * 查询数据库中是否存在相同记录，不存在新增，
		 * 存在 抛出异常提示用户
		 */
		FncFixedAsset fncFixed = this.findFncFixedAsset(fncFixedAsset.getCusId(), fncFixedAsset.getFncYm(), 
				fncFixedAsset.getFncTyp(), fncFixedAsset.getSeq());
		if(fncFixed.getCusId() == null){
			//设置操作员
			fncFixedAsset.setCrtUsrId(this.getUsrId());
			//设置时间
			fncFixedAsset.setCrtDt(this.getCurDate());
			//设置操作员机构
			fncFixedAsset.setRegBchId(this.getUsrBchId());
			//设置更新时间
			fncFixedAsset.setLastUpdTm(this.getCurTimestamp());
			//新增一条成员信息
			flag = fncAgent.addRecord(fncFixedAsset);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您输入的信息已经存在！");
		}
		return flag;
	}
	public String addFncFixedAsset (String cusId,String fncYm,String fncTyp){
		return null;
	}
	/**
	 * 修改财务报表明细信息—主要固定资产明细
	 * @param fncFixedAsset 财务报表明细信息—主要固定资产明细
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 * @throws ComponentException
	 */
	public String modifyFncFixedAsset (FncFixedAsset fncFixedAsset) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncFixedAssetAgent fncAgent = (FncFixedAssetAgent)this.getAgentInstance("FncFixedAsset");

		//设置更新时间
		fncFixedAsset.setLastUpdTm(this.getCurTimestamp());
		
		//修改一条信息
		flag = fncAgent.updateRecord(fncFixedAsset);

		return flag;
	}
	/**
	 * 删除财务报表明细信息—主要固定资产明细
	 * @param fncFixedAsset 财务报表明细信息—主要固定资产明细
	 * @return 信息编码flag默认"999999"失败,为"000000"的时候表示成功 
	 * @throws ComponentException
	 */
	public String removeFncFixedAsset (FncFixedAsset fncFixedAsset) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncFixedAssetAgent fncAgent = (FncFixedAssetAgent)this.getAgentInstance("FncFixedAsset");

		/*
		 * 查询数据库中是否存在相同记录，存在删除，
		 * 不存在 抛出异常提示用户已经被别人删除
		 */
		FncFixedAsset fncFixed = this.findFncFixedAsset(fncFixedAsset.getCusId(), fncFixedAsset.getFncYm(), 
				fncFixedAsset.getFncTyp(), fncFixedAsset.getSeq());
		if(fncFixed.getCusId() != null){
			//删除一条信息
			flag = fncAgent.deleteRecord(fncFixedAsset.getCusId(), fncFixedAsset.getFncYm(), 
					fncFixedAsset.getFncTyp(), fncFixedAsset.getSeq());
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您所操作的数据已经被删除,请刷新后重试！");
		}

		return flag;
	}
	public String removeFncFixedAsset (String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		
		String flag = CMISMessage.DEFEAT;////错误信息（默认失败）

		//创建代理类 
		FncFixedAssetAgent fncAgent = (FncFixedAssetAgent)this.getAgentInstance("FncFixedAsset");

		/*
		 * 查询数据库中是否存在相同记录，存在删除，
		 * 不存在 抛出异常提示用户已经被别人删除
		 */
		FncFixedAsset fncFixed = this.findFncFixedAsset(cusId, fncYm, fncTyp, seq);
		if(fncFixed.getCusId() != null){
			//删除一条信息
			flag = fncAgent.deleteRecord(cusId, fncYm, fncTyp, seq);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"您所操作的数据已经被删除,请刷新后重试！");
		}

		return flag;
	}
	/**
	 * 查询财务报表明细信息—主要固定资产明细
	 * @param cusId 客户代码
	 * @param fncYm 年月
	 * @param fncTyp 报表周期类型1
	 * @param seq 序号
	 * @return  FncFixedAsset 财务报表明细信息—主要固定资产明细
	 * @throws ComponentException
	 */
	public FncFixedAsset findFncFixedAsset (String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		
		FncFixedAsset fncFixed = new FncFixedAsset();

		//创建代理类
		FncFixedAssetAgent fncAgent = (FncFixedAssetAgent) this.getAgentInstance("FncFixedAsset");
			
		//通过代理类查找信息
		fncFixed = fncAgent.queryDetail(cusId, fncYm, fncTyp, seq);

		return fncFixed;
	}
}
