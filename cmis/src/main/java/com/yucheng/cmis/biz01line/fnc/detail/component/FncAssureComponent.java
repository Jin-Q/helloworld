package com.yucheng.cmis.biz01line.fnc.detail.component;

import com.yucheng.cmis.biz01line.fnc.detail.agent.FncAssureAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncAssure;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname	FncAssureComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 5:14:04 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：对外担保及表外业务明细业务处理类
 *@Lastmodified 
 *@Author
 */
public class FncAssureComponent  extends CMISComponent{
	/**
	 * 新增一条对外担保及表外业务明细信息
	 * @param pfncAssure	对外担保及表外业务明细信息
	 * @return	信息编码
	 * @throws ComponentException
	 */
	public String addFncAssure(FncAssure pFncAssure) throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建业务代理类
		FncAssureAgent fAssureAgent = (FncAssureAgent)
							this.getAgentInstance(PUBConstant.FNCASSURE);
		/*
		 * 查询数据库中是否存在相同记录，不存在新增，
		 * 存在 抛出异常提示用户
		 */
		
		FncAssure ccmFamily = this.findFncAssure(pFncAssure.getCusId(), pFncAssure.getFncYm(),
				pFncAssure.getFncTyp(),	pFncAssure.getSeq()+"");
		
	
		
		if(ccmFamily.getCusId() == null){
			//设置操作员
			pFncAssure.setCrtUsrId(this.getUsrId());
			//设置时间
			pFncAssure.setCrtDt(this.getCurDate());
			//设置更新操作员
			pFncAssure.setEditUpdUsrId(this.getUsrId());
			//设置操作员机构
			pFncAssure.setRegBchId(this.getUsrBchId());
			//设置更新时间
			pFncAssure.setLastUpdTm(this.getCurTimestamp());
			
			//新增一条信息
			
			flagInfo = fAssureAgent.addRecord(pFncAssure);
		}else{
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"你输入的信息已经存在！");
		}
		
			

		return flagInfo;
	};

	/**
	 * 对外担保及表外业务明细信息
	 * @param pfncAssure 对外担保及表外业务明细信息
	 * @return 信息编码
	 * @throws ComponentException
	 */
	public String modifyFncAssure(FncAssure pfncAssure) throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT;//信息编码
	
		//创建业务代理类
		FncAssureAgent ccMngFmlAgent = (FncAssureAgent)
							this.getAgentInstance(PUBConstant.FNCASSURE);
		//设置更新操作员
		pfncAssure.setEditUpdUsrId(this.getUsrId());
		//设置更新时间
		pfncAssure.setLastUpdTm(this.getCurTimestamp());

		//修改对外担保及表外业务明细信息
		flagInfo = ccMngFmlAgent.updateRecord(pfncAssure);

		return flagInfo;
	};
	
	/**
	 * 根据主键删除记录
	 * @param cusId 客户ID
	 * @param Fnc_Ym	年月	
	 * @param Fnc_Typ	报表周期类型
	 * @param seq	序号
	 * @return 信息编码
	 */
	public String removeFncAssure(String cusId, String fncYm, String fncTyp, String seq ) 
													throws ComponentException{
		
		String flagInfo = CMISMessage.DEFEAT;//信息编码

		//创建业务代理类
		FncAssureAgent fAssureAgent = (FncAssureAgent)
							this.getAgentInstance(PUBConstant.FNCASSURE);
			
		//主键值
		flagInfo = fAssureAgent.deleteRecord(cusId, fncYm, fncTyp, seq);

		return flagInfo;
		
	};


	/**
	 * 查询一条对外担保及表外业务明细信息通过主键
	 * @param cusId 客户ID
	 * @param Fnc_Ym	年月	
	 * @param Fnc_Typ	报表周期类型
	 * @param seq	序号
	 * @return
	 * @throws ComponentException
	 */
	public FncAssure findFncAssure(String cusId, String fncYm, String fncTyp, String seq )
													throws ComponentException{
		
		FncAssure fAssure = null;

		//		创建业务代理类
		FncAssureAgent fAssureAgent = (FncAssureAgent)
							this.getAgentInstance(PUBConstant.FNCASSURE);
			
		fAssure = fAssureAgent.queryDetail(cusId, fncYm, fncTyp, seq);

		return fAssure;
		
	};
	
}
