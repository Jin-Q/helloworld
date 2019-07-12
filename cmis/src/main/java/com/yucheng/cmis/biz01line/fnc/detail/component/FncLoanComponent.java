package com.yucheng.cmis.biz01line.fnc.detail.component;


import com.yucheng.cmis.biz01line.fnc.detail.agent.FncLoanAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;


/**
 * 主要借款明细的组件
 *@Classname	FncLoanComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 8, 2008 3:57:43 PM  
 *@Copyright  2008 yuchengtech
 *@Author 		gongjx
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncLoanComponent extends CMISComponent {

	/**
		 * 新增主要借款明细信息
		 * @param  fncLoan 主要借款明细信息
		 * @return	String   返回值说明
		 * @throws Exception
	 */
/*
 * 2012.6.16杨蕾注释
 * 	public String addFncLoan(FncLoan fncLoan) throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建代理类
		FncLoanAgent fncLoanAgent = (FncLoanAgent)this.getAgentInstance(PUBConstant.FNCLOAN);
		
		 * 查询数据库中是否存在相同记录，不存在新增，
		 * 存在 抛出异常提示用户
		 
		FncLoan fl = (FncLoan)this.findFncLoan(fncLoan);
		if(fl.getCusId() == null) {
			//设置添加时间
			fncLoan.setCrtDt(this.getCurDate());
//			设置登记机构
			fncLoan.setRegBchId(this.getUsrBchId());
			//设置登记人
			fncLoan.setCrtUsrId(this.getUsrId());
//			设置当前编辑人
			fncLoan.setEditUpdUsrId(this.getUsrId());
			//设置更新时间，首次添加的更新时间和添加时间一样
			fncLoan.setLastUpdTm(this.getCurTimestamp());
			//添加信息
			flagInfo = fncLoanAgent.addRecord(fncLoan);
		} else {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"你输入的信息已经存在！");
		}
		return flagInfo;
	};*/
	
	/**
		 * 修改主要借款明细信息
		 * @param fncLoan  主要借款明细信息
		 * @return	String   返回值说明
		 * @throws Exception
	 */
	/*
	*2012.6.16杨蕾注释
	*public String modifyFncLoan(FncLoan fncLoan) throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建主要借款明细信息的代理类
		FncLoanAgent fncLoanAgent = (FncLoanAgent)this.getAgentInstance(PUBConstant.FNCLOAN);
		//设置更新时间，首次添加的更新时间和添加时间一样
		fncLoan.setLastUpdTm(this.getCurTimestamp());
		//修改
		flagInfo = fncLoanAgent.updateRecord(fncLoan);
		return flagInfo;
	};
	 * 
	 */
	/**
		* 删除主要借款明细信息
		* @param  fncLoan  主要借款明细信息
		* @param  String cusId 客户代码
		* @param  String fncYm 年月
		* @param  String fncTyp 报表周期类型
		* @param  int seq 序号
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String removeFncLoan(String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建主要借款明细信息的代理类
		FncLoanAgent fncLoanAgent = (FncLoanAgent)this.getAgentInstance(PUBConstant.FNCLOAN);
		flagInfo = fncLoanAgent.deleteRecord(cusId, fncYm, fncTyp, seq);
		return flagInfo;
	};
	
	/*
	 * 2012.6.16 杨蕾注释
	 * public String removeFncLoan(FncLoan fncLoan) throws ComponentException{
	
		String flagInfo = PUBConstant.FAIL;
		//直接调用上面的删除方法
		flagInfo = this.removeFncLoan(fncLoan.getCusId(), fncLoan.getFncYm(), fncLoan.getFncTyp(), fncLoan.getSeq());
		return flagInfo;
	};
	 */
	/**
		* 查询主要借款明细详细信息
		* @param  fncLoan  主要借款明细信息
		* @param  String cusId 客户代码
		* @param  String fncYm 年月
		* @param  String fncTyp 报表周期类型
		* @param  int seq 序号
		* @return	FncLoan   返回查询到的主要借款明细详细信息
		* @throws Exception
	 */
	
/*	
 * 2012.6.16 杨蕾注释
 * public FncLoan findFncLoan(String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		FncLoan fncLoan = null;
		
		//创建代理类
		FncLoanAgent fncLoanAgent = (FncLoanAgent)this.getAgentInstance(PUBConstant.FNCLOAN);
		//查询
		fncLoan = fncLoanAgent.queryDetail(cusId, fncYm, fncTyp, seq);
		return fncLoan;
	};
	
	public FncLoan findFncLoan(FncLoan fncLoan) throws ComponentException{
		//直接调用上面的方法执行查询操作
		return this.findFncLoan(fncLoan.getCusId(), fncLoan.getFncYm(), fncLoan.getFncTyp(), fncLoan.getSeq());
	};*/
	
	
}






