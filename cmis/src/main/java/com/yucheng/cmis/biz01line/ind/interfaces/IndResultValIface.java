package com.yucheng.cmis.biz01line.ind.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import com.yucheng.cmis.pub.exception.ComponentException;

public interface IndResultValIface {
	/**
	 * 将指标值插入指标结果表
	 * @param hs 插入的数据
	 * @return
	 * @throws ComponentException
	 */
	public int insertIndResultVal(HashMap<String,String> hs) throws ComponentException;
	
	/**
	 * 查询指标结果值是否存在
	 * @param serno 业务编号
	 * @param year  指标年份
	 * @param month 指标月份
	 * @param day 指标日 
	 * @param indexno 指标编号
	 * @return
	 * @throws ComponentException
	 */
	public String queryIndResultVal(String serno,int year,int month,int day,String indexno) throws ComponentException;
	
	/**
	 * 删除指标结果值表
	 * @param serno 业务编号
	 * @param year  指标年份
	 * @param month 指标月份
	 * @param day 指标日 
	 * @param indexno 指标编号
	 * @throws ComponentException
	 */
	public void deleteIndResultVal(String serno,int year,int month,int day,String indexno) throws ComponentException;
	
	/**
	 * 查询指标结果值是否存在
	 * @param serno 业务编号
	 * @param year  指标年份
	 * @param month 指标月份
	 * @param day 指标日 
	 * @param indexno 指标编号
	 * @return
	 * @throws ComponentException
	 */
	public String queryIndResultValByNo(String serno,String indexno) throws ComponentException;
	
	/**
	 * 删除指标结果值表
	 * @param serno 业务编号
	 * @param year  指标年份
	 * @param month 指标月份
	 * @param day 指标日 
	 * @param indexno 指标编号
	 * @throws ComponentException
	 */
	public void deleteIndResultValByNo(String serno,String indexno) throws ComponentException;
	
	
	/**
	 * 查询一个客户 该次评级的 所有定量指标的指标值
	 * @param serno 流水号
	 * @throws ComponentException
	 */
	public ArrayList<HashMap> queryIndResultValHm(String serno) throws ComponentException;

}
