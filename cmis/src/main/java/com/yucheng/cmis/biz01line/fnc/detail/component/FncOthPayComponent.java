package com.yucheng.cmis.biz01line.fnc.detail.component;


import com.yucheng.cmis.biz01line.fnc.detail.agent.FncOthPayAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncOtherPayable;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 其它应付款明细组件
 *@Classname	FncOthPayComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 8, 2008 4:21:37 PM  
 *@Copyright  2008 yuchengtech
 *@Author 		gongjx
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncOthPayComponent extends CMISComponent {
	
	/**
		* 新增其它应付款明细对象
		* @param   fncOthPay  其它应付款明细对象
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String addFncOthPay(FncOtherPayable fncOthPay) throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建其它应付款明细的代理类
		FncOthPayAgent fncOthPayAgent = (FncOthPayAgent)this.getAgentInstance(PUBConstant.FNCOTHERPAYABLE);
		/*
		 * 查询数据库中是否存在相同记录，不存在新增，
		 * 存在 抛出异常提示用户
		 */
		FncOtherPayable fop = (FncOtherPayable)this.findFncOthPay(fncOthPay);
		if(fop.getCusId() == null) {
			//设置添加时间
			fncOthPay.setCrtDt(this.getCurDate());
			//设置登记机构
			fncOthPay.setRegBchId(this.getUsrBchId());
            //设置登记人
			fncOthPay.setCrtUsrId(this.getUsrId());
//			设置当前编辑人
			fncOthPay.setEditUpdUsrId(this.getUsrId());
			//设置更新时间，首次添加的更新时间和添加时间一样
			fncOthPay.setLastUpdTm(this.getCurTimestamp());
			//添加信息
			flagInfo = fncOthPayAgent.addRecord(fncOthPay);
		} else {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"你输入的信息已经存在！");
		}
		return flagInfo;
	};
	
	/**
		* 修改其它应付款明细对象
		* @param  fncOthPay 其它应付款明细对象
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String modifyFncOthPay(FncOtherPayable fncOthPay) throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建其它应付款明细的代理类
		FncOthPayAgent fncOthPayAgent = (FncOthPayAgent)this.getAgentInstance(PUBConstant.FNCOTHERPAYABLE);
		//设置更新时间，首次添加的更新时间和添加时间一样
		fncOthPay.setLastUpdTm(this.getCurTimestamp());
		//修改
		flagInfo = fncOthPayAgent.updateRecord(fncOthPay);
		return flagInfo;
	};
	
	/**
		* 删除其它应付款明细对象
		* @param  fncOthPay 其它应付款明细对象
		* @param  String cusId 客户代码
		* @param  String fncYm 年月
		* @param  String fncTyp 报表周期类型
		* @param  int seq 序号
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String removeFncOthPay(String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建其它应付款明细的代理类
		FncOthPayAgent fncOthPayAgent = (FncOthPayAgent)this.getAgentInstance(PUBConstant.FNCOTHERPAYABLE);
		flagInfo = fncOthPayAgent.deleteRecord(cusId, fncYm, fncTyp,seq);
		return flagInfo;
	};
	
	public String removeFncOthPay(FncOtherPayable fncOthPay) throws ComponentException{
		String flagInfo = PUBConstant.FAIL;
		//直接调用上面的删除方法
		flagInfo = this.removeFncOthPay(fncOthPay.getCusId(), fncOthPay.getFncYm(), fncOthPay.getFncTyp(),fncOthPay.getSeq());
		return flagInfo;
	};
	
	
	/**
		* 查询其它应付款明细对象
		* @param  fncOthPay 其它应付款明细对象
		* @param  String cusId 客户代码
		* @param  String fncYm 年月
		* @param  String fncTyp 报表周期类型
		* @param  int seq 序号
		* @return	FncOtherPayable   返回值说明
		* @throws Exception
	 */
	public FncOtherPayable findFncOthPay(String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		FncOtherPayable fncOthPay = null;
		
		//创建代理类
		FncOthPayAgent fncOthPayAgent = (FncOthPayAgent)this.getAgentInstance(PUBConstant.FNCOTHERPAYABLE);
		//查询
		fncOthPay = fncOthPayAgent.queryDetail(cusId, fncYm, fncTyp, seq);
		return fncOthPay;
	};
	
	public FncOtherPayable findFncOthPay(FncOtherPayable fncOthPay) throws ComponentException{
		//直接调用上面的方法执行查询操作
		return this.findFncOthPay(fncOthPay.getCusId(), fncOthPay.getFncYm(), fncOthPay.getFncTyp(), fncOthPay.getSeq());
	};
}







