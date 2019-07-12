package com.yucheng.cmis.biz01line.ind.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import com.yucheng.cmis.pub.exception.ComponentException;

public interface IndCalScoreIface {
	/**
	 * 取指标得分
	 * @param grpno  组别编号
	 * @param indexno 指标编号
	 * @param indexval  指标值
	 * @return
	 * @throws ComponentException
	 */
	public String getIndScore(String grpno,String indexno,String indexval) throws ComponentException;
	/**
	 * 取指标得分
	 * @param grpno  组别编号
	 * @param indexno 指标编号
	 * @param indexval  指标值
	 * @return
	 * @throws ComponentException
	 */
	public String getIndScore(String grpno,String indexno,String indexval,String orgVal) throws ComponentException;
	public String getIndScore(HashMap<String,String> hm) throws ComponentException;
	/**
	 * 取指标得分 带参数
	 * @param grpno  组别编号
	 * @param indexno 指标编号
	 * @param indexval  指标值
	 * @param para 指标算分参数
	 * @return
	 * @throws ComponentException
	 */
	public String getIndScore(String grpno,String indexno,String indexval,HashMap<String,String> para) throws ComponentException;
	/**
	 * 取组得分
	 * @param grpno 
	 * @param hm 组下各个指标的得分集
	 * @return
	 * @throws ComponentException
	 */
	public String getGrpScore(String grpno,HashMap<String,String> hm) throws ComponentException;
	
	/**
	 * 取模型得分
	 * @param modelno
	 * @param hm  模型下各个组的得分集
	 * @return
	 * @throws ComponentException
	 */
	public String getModelScore(String modelno,HashMap<String,String> hm,ArrayList list) throws ComponentException;
	
	/**
	 * 取指标值
	 * @param indexno
	 * @param hm
	 * @return
	 * @throws ComponentException
	 */
	public String getIndValue(String indexno,HashMap<String,String> hm) throws ComponentException;
	/**
	 * 取指标原始值以及原始值对应的选项值
	 * @param modelno
	 * @param indexno
	 * @param hm
	 * @return
	 * @throws ComponentException
	 */
	public HashMap<String,String> getIndOrgValAndOptVal(String modelno,String indexno,HashMap hm) throws ComponentException;
}