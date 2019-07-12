package com.yucheng.cmis.biz01line.ind.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.exception.ComponentException;

public interface IndModGrpIndIface {
	/**
	 * 查询模型下的所有组信息
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList<HashMap> queryModGrpList(String modelNo) throws ComponentException;
	/**
	 * 查询组下的所有指标信息
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList<HashMap> queryGrpIndexesList(String groupNo) throws ComponentException;
	
	/**
	 * 通过模型编号，获取相关组信息的方法
	 * <p>注意对接的表中的每个主要都要设置
	 * @param modelId	表模型ID	
	 * @param conditinonValues 要查询的模型条件
	 * @return	相应的查询对象
	 * @throws ComponentException
	 */
	public <CMISDomain>ArrayList queryIndGroupDomain( String conditionValues) throws ComponentException;
	
	/**
	 * 通过组编号，获取相关提标信息的方法
	 * <p>注意对接的表中的每个主要都要设置
	 * @param modelId	表模型ID	
	 * @param conditinonValues 要查询的模型条件
	 * @return	相应的查询对象
	 * @throws ComponentException
	 */
	public <CMISDomain>ArrayList queryIndGroupIndexDomain( String conditionValues) throws ComponentException;
	
	/**
	 * 查询指标信息
	 * @param index_no 指标编号
	 * @return 
	 * @throws AgentException
	 */
	
	public CMISDomain queryIndLibDetail(String index_no) throws ComponentException ;
	/**
	 * 根据模型编号查询该模型中所有的指标中所配置的财务指标编号列表
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList getFncIndexArray(String modelNo) throws ComponentException;
}
