package com.yucheng.cmis.biz01line.fnc.master.component;


import java.sql.Connection;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.biz01line.fnc.master.agent.FncStatBaseAgent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatCfs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatIs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatSl;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatSoe;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
  /**
 *@Classname	FncStatBaseComponent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-8 上午10:13:12  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncStatBaseComponent extends CMISComponent{
	
	private FncConfTemplate fncConfTemplate;     //财务报表模板domain
	
	private FncStatBase fncStatBase;              //财务报表基表
	
	//private BizConfig bizConfig;
	
	/**
	 * 
	 * @param pfncStatBase   公司客户报表对象
	 * @return  flagInfo 信息编码
	 * @throws ComponentException
	 */
	public String addFncStatBase(FncStatBase pfncStatBase)throws ComponentException {
		
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");

		// 新增一条成员信息
		flagInfo = fncStatBaseAgent.addRecord(pfncStatBase);

		return flagInfo;
	};
	/**
	 * 
	 * @param fncStatBs
	 * @return
	 * @throws ComponentException
	 */
	public FncStatBs QueryFncStatBs(FncStatBs fncStatBs)throws ComponentException{
		FncStatBs tempBs = null;
		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
		tempBs = fncStatBaseAgent.QueryFncStatBs(fncStatBs,this.getConnection());
		return tempBs;
	}
		
	/**
	 * 
	 * @param fncStatCfs
	 * @return
	 * @throws ComponentException
	 */
	public FncStatCfs QueryFncStatCfs(FncStatCfs fncStatCfs)throws ComponentException{
		FncStatCfs tempCfs = null;
		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
		tempCfs = fncStatBaseAgent.QueryFncStatCfs(fncStatCfs,this.getConnection());
		return tempCfs;
		
	}
	/**
	 * 
	 * @param fncIndexRpt
	 * @return
	 * @throws ComponentException
	 */
	public FncIndexRpt QueryFncIndexRpt(FncIndexRpt fncIndexRpt)throws ComponentException{
		FncIndexRpt tempFi = null;
		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
		tempFi = fncStatBaseAgent.QueryFncIndexRpt(fncIndexRpt,this.getConnection());
		return tempFi;
		
	}
	/**
	 * 
	 * @param fncStatIs
	 * @return
	 * @throws ComponentException
	 */
	public FncStatIs QueryFncStatIs(FncStatIs fncStatIs)throws ComponentException{
		FncStatIs tempIs = null;
		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
		tempIs = fncStatBaseAgent.QueryFncStatIs(fncStatIs,this.getConnection());
		return tempIs;
		
	}
	/**
	 * 
	 * @param fncStatSl
	 * @return
	 * @throws ComponentException
	 */
	public FncStatSl QueryFncStatSl(FncStatSl fncStatSl)throws ComponentException{
		FncStatSl tempSl = null;
		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
		tempSl = fncStatBaseAgent.QueryFncStatSl(fncStatSl,this.getConnection());
		return tempSl;
		
	}
	/**
	 * 
	 * @param fncStatSoe
	 * @return
	 * @throws ComponentException
	 */
	public FncStatSoe QueryFncStatSoe(FncStatSoe fncStatSoe)throws ComponentException{
		FncStatSoe tempSoe = null;
		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
		tempSoe = fncStatBaseAgent.QueryFncStatSoe(fncStatSoe,this.getConnection());
		return tempSoe;
		
	}
	/**
	 * 
	 * @param fncStatBs
	 * @return
	 * @throws ComponentException
	 */
	public String addFncStatBs(FncStatBs fncStatBs,Connection conn)throws ComponentException {
		
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");

		// 新增一条成员信息
		flagInfo = fncStatBaseAgent.addFncStatBsRecord(fncStatBs,conn);

		return flagInfo;
	};
	/**
	 * 
	 * @param fncStatCfs
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String addFncStatCfs(FncStatCfs fncStatCfs,Connection conn)throws ComponentException {
		
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");

		// 新增一条成员信息
		flagInfo = fncStatBaseAgent.addFncStatCfsRecord(fncStatCfs,conn);

		return flagInfo;
	};
	/**
	 * 
	 * @param fncIndexRpt
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String addFncIndexRpt(FncIndexRpt fncIndexRpt,Connection conn)throws ComponentException {
		
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");

		// 新增一条成员信息
		flagInfo = fncStatBaseAgent.addFncIndexRptRecord(fncIndexRpt,conn);

		return flagInfo;
	};
	/**
	 * 
	 * @param fncStatIs
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String addFncStatIs(FncStatIs fncStatIs,Connection conn)throws ComponentException {
		
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");

		// 新增一条成员信息
		flagInfo = fncStatBaseAgent.addFncStatIsRecord(fncStatIs,conn);

		return flagInfo;
	};
	/**
	 * 添加财务简表信息
	 * @param fncStatSl
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String addFncStatSl(FncStatSl fncStatSl,Connection conn)throws ComponentException {
		
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");

		// 新增一条成员信息
		flagInfo = fncStatBaseAgent.addFncStatSlRecord(fncStatSl,conn);

		return flagInfo;
	};
	/**
	 * 
	 * @param fncStatSoe
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String addFncStatSoe(FncStatSoe fncStatSoe,Connection conn)throws ComponentException {
		
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");

		// 新增一条成员信息
		flagInfo = fncStatBaseAgent.addFncStatSoeRecord(fncStatSoe,conn);

		return flagInfo;
	};
	/**
	 * 
	 * @param pfncStatBase        公司客户报表对象
	 * @return       flagInfo 信息编码
	 * @throws ComponentException
	 */
	public String modifyFncStatBase (FncStatBase pfncStatBase)throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
		//通过代理类进行修改操作
		flagInfo = fncStatBaseAgent.updateRecord(pfncStatBase);
		return flagInfo;
	};
		
	/**
	 * 
	 * @param cusId  客户代码
	 * @param statPrdStyle   报表周期类型
	 * @param statPrd        报表期间
	 * @return   pfncStatBase      公司客户报表对象
	 * @throws ComponentException
	 */
	public FncStatBase findFncStatBase(String cusId,String statPrdStyle,String statPrd,String stat_style)throws ComponentException{
		FncStatBase pfncStatBase = new FncStatBase();

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
		//通过代理类进行查询操作
		pfncStatBase = fncStatBaseAgent.queryDetail(cusId,statPrdStyle,statPrd,stat_style);
		return pfncStatBase;
	};
	
	public FncStatBase findFncStatBase(FncStatBase pfncStatBase)throws ComponentException{
		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this.getAgentInstance("FncStatBase");
		//通过代理类进行查询操作
		pfncStatBase = fncStatBaseAgent.queryDetail(pfncStatBase);
		return pfncStatBase;
	}

	/**
	 * 
	 * @param cusId      客户代码
	 * @param statPrdStyle   报表周期类型
	 * @param statPrd      报表期间
	 * @return      flagInfo 信息编码
	 * @throws ComponentException
	 */
	public String removeFncStatBase(String cusId,String statPrdStyle,String statPrd)throws ComponentException{
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
				.getAgentInstance("FncStatBase");
        //通过代理类进行删除操作
		flagInfo = fncStatBaseAgent.deleteRecord(cusId,statPrdStyle,statPrd);
		return flagInfo;
	};
	
	/**
	 * 根据客户编号 获取对应的财务报表类型
	 * @param cus_id
	 * @return String
	 */
	public String findCusRepType(String cus_id)throws ComponentException{
		//String cus_fin_type = null;
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this.getAgentInstance("FncStatBase");
		
		CusBase cusBase = new CusBase();
		fncStatBaseAgent.findCMISDomainByKeyword(cusBase, PUBConstant.CUSBASE, cus_id);
		
		String cusType = cusBase.getCusType();
		String modelId = null;
//		if(cusType.startsWith("2")){
		if(!cusType.startsWith("Z")){
			modelId = PUBConstant.CUSCOM;
			CusCom cusCom = new CusCom();
			fncStatBaseAgent.findCMISDomainByKeyword(cusCom, modelId, cus_id);
			return cusCom.getComFinRepType();
		}
		//暂时注掉
//		else if(cusType.startsWith("3")){
//			modelId = PUBConstant.CUSSAMEORG;
//			CusSameOrg cusSame = new CusSameOrg();
//			fncStatBaseAgent.findCMISDomainByKeyword(cusSame, modelId, cus_id);
//			return cusSame.getComFinRepType();
//		}
		else{
			EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "该客户的客户类型不为对公和同业没有财务报表");
		}
		
		return null;
	
	}
	
	public FncConfTemplate findFncConfTemplate(String cus_fin_type)throws ComponentException{
		FncConfTemplate fncTemp = null;
		FncStatBaseAgent fncStatBaseAgent = (FncStatBaseAgent) this
		.getAgentInstance("FncStatBase");
		fncTemp = fncStatBaseAgent.findFncConfTemplate(cus_fin_type,this.getConnection());
		return fncTemp;
	}
	
	public FncConfTemplate getFncConfTemplate() {
		return fncConfTemplate;
	}
	public void setFncConfTemplate(FncConfTemplate fncConfTemplate) {
		this.fncConfTemplate = fncConfTemplate;
	}
	public FncStatBase getFncStatBase() {
		return fncStatBase;
	}
	public void setFncStatBase(FncStatBase fncStatBase) {
		this.fncStatBase = fncStatBase;
	}
	
	
}
