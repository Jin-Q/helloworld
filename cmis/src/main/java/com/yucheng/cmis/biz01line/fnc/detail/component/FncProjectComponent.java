package com.yucheng.cmis.biz01line.fnc.detail.component;


import com.yucheng.cmis.biz01line.fnc.detail.agent.FncProjectAgent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncOtherPayable;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncProject;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 在建工程明细组件
 *@Classname	FncProjectComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 8, 2008 6:54:59 PM  
 *@Copyright  2008 yuchengtech
 *@Author 		gongjx
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncProjectComponent extends CMISComponent {
	
	/**
		* 新增在建工程明细信息
		* @param  fncProject 在建工程明细信息
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String addFncProject(FncProject fncProject) throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建代理类
		FncProjectAgent fncProjectAgent = (FncProjectAgent)this.getAgentInstance(PUBConstant.FNCPROJECT);
		/*
		 * 查询数据库中是否存在相同记录，不存在新增，
		 * 存在 抛出异常提示用户
		 */
		FncProject fp = (FncProject)this.findFncProject(fncProject);
		if(fp.getCusId() == null) {
			//设置添加时间
			fncProject.setCrtDt(this.getCurDate());
			//设置登记机构
			fncProject.setRegBchId(this.getUsrBchId());
			//设置登记人
			fncProject.setCrtUsrId(this.getUsrId());
//			设置当前编辑人
			fncProject.setEditUpdUsrId(this.getUsrId());
			//设置更新时间，首次添加的更新时间和添加时间一样
			fncProject.setLastUpdTm(this.getCurTimestamp());
			//添加信息
			flagInfo = fncProjectAgent.addRecord(fncProject);
		} else {
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"你输入的信息已经存在！");
		}
		return flagInfo;
	};
	
	/**
		* 修改在建工程明细信息
		* @param  fncProject 在建工程明细信息
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String modifyFncProject(FncProject fncProject) throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建在建工程明细信息的代理类
		FncProjectAgent fncProjectAgent = (FncProjectAgent)this.getAgentInstance(PUBConstant.FNCPROJECT);
		//设置更新时间，首次添加的更新时间和添加时间一样
		fncProject.setLastUpdTm(this.getCurTimestamp());
		//修改
		flagInfo = fncProjectAgent.updateRecord(fncProject);
		return flagInfo;
	};
	
	/**
		* 删除在建工程明细信息
		* @param  fncProject 在建工程明细信息
		* @param  String cusId 客户代码
	    * @param  String fncYm 年月
	    * @param  String fncTyp 报表周期类型
	    * @param  int seq 序号
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String removeFncProject(String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		
		//创建在建工程明细信息的代理类
		FncProjectAgent fncProjectAgent = (FncProjectAgent)this.getAgentInstance(PUBConstant.FNCPROJECT);
		flagInfo = fncProjectAgent.deleteRecord(cusId, fncYm, fncTyp, seq);
		return flagInfo;
	};
	
	public String removeFncProject(FncOtherPayable fncProject) throws ComponentException{
		String flagInfo = PUBConstant.FAIL;
		//直接调用上面的删除方法
		flagInfo = this.removeFncProject(fncProject.getCusId(), fncProject.getFncYm(), fncProject.getFncTyp(), fncProject.getSeq());
		return flagInfo;
	};
	
	/**
	 * 查询在建工程明细
	 * @param fncProject  在建工程明细
	 * @param int seq  客户序号
	 * @param String cusId  客户代码
	 * @param String fncYm  年月
	 * @param String fcnTyp  报表周期类型
	 * @return	FncProject  在建工程明细
	 * @throws Exception
 */
	public FncProject findFncProject(String cusId,String fncYm,String fncTyp,int seq) throws ComponentException{
		FncProject fncProject = null;
		
		//创建在建工程明细信息的代理类
		FncProjectAgent fncProjectAgent = (FncProjectAgent)this.getAgentInstance(PUBConstant.FNCPROJECT);
		//查询
		fncProject = fncProjectAgent.queryDetail(cusId, fncYm, fncTyp, seq);
		return fncProject;
	};
	
	public FncProject findFncProject(FncProject fncProject) throws ComponentException{
		//直接调用上面的方法执行查询操作
		return this.findFncProject(fncProject.getCusId(), fncProject.getFncYm(), fncProject.getFncTyp(), fncProject.getSeq());
	};

}





