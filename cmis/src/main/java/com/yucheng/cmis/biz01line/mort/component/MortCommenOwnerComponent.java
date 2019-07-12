package com.yucheng.cmis.biz01line.mort.component;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.arp.agent.ArpPubAgent;
import com.yucheng.cmis.biz01line.mort.agent.MortCommenOwnerAgent;
import com.yucheng.cmis.biz01line.mort.dao.MortCommenOwnerDao;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.exception.DaoException;
import com.yucheng.cmis.pub.pubopera.PubOperaAgent;
import com.yucheng.cmis.pub.pubopera.PubOperaComponent;

public class MortCommenOwnerComponent extends CMISComponent {
	
	/**
	 * 根据押品编号关联查询客户表获取共有人信息
	 * @param guarantyNo 押品ID
	 * @return IndexedCollection
	 * @throws Exception 
	 */
	public IndexedCollection getCommenList(String guarantyNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.getCommenList(guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("获取共有人信息失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据押品编号关联查询权证信息表获取主权证信息
	 * @param guarantyNo 押品ID
	 * @return IndexedCollection
	 * @throws Exception 
	 */
	public KeyedCollection getMortGuarantyCertiInfoDetail(String guarantyNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.getMortGuarantyCertiInfoDetail(guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号关联查询权证信息表获取主权证信息失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据押品编号关联删除相关押品信息
	 * @param guarantyNo 押品ID
	 * @return int
	 * @throws Exception 
	 */
	public int deleteAllByGuarantyNo(String guarantyNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.deleteAllByGuarantyNo(guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号关联删除相关押品信息失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据押品编号关联删除货物与监管协议关系记录（货物登记模块）
	 * @param guarantyNo 押品ID
	 * @return int
	 * @throws Exception 
	 */
	public int deleteCarOverReByGuarantyNo(String guarantyNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.deleteCarOverReByGuarantyNo(guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号关联删除相关押品信息失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据押品编号查询抵质押品与担保合同的关联信息表
	 * @param guarantyNo 押品ID
	 * @return IndexedCollection
	 * @throws Exception 
	 */
	public IndexedCollection queryMortTabList(String guarantyContNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.queryMortTabList(guarantyContNo);
		} catch (Exception e) {
			throw new ComponentException("查询抵质押品与担保合同的关联信息表失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 货物登记模块（商链通）对货物与监管协议关系的新增
	 * @param kc 需要插入数据库的键值对
	 * @return
	 * @throws ComponentException
	 */
	public int insertIqpCargoOverseeReRecord(KeyedCollection kc) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.insertIqpCargoOverseeReRecord(kc);
		} catch (Exception e) {
			throw new ComponentException("货物与监管协议关系记录新增失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 货物登记模块（商链通）根据押品编号查询获取监管协议与货物的关系表获取监管协议编号
	 * @param guarantyContNo 押品编号
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryCarOverReRecordDetail(String guarantyNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.queryCarOverReRecordDetail(guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号查询获取监管协议与货物的关系表获取监管协议编号失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据押品编号获取押品下关联的货物库存总价值（商链通）
	 * @param guarantyContNo 押品编号
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryIdentyTotalInfo(String guarantyNo,String status) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.queryIdentyTotalInfo(guarantyNo,status);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号获取押品下关联的货物库存总价值失败，失败原因："+e.getMessage());
		}
	}
	
	/**
	 * 根据业务流水号获取押品下关联的货物库存总价值（商链通）
	 * @param guarantyContNo 押品编号
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryCargoReplListTotalInfo(String serno,String status) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.queryCargoReplListTotalInfo(serno,status);
		} catch (Exception e) {
			throw new ComponentException("根据业务流水号获取押品下关联的货物库存总价值失败，失败原因："+e.getMessage());
		}
	}
	
	/**
	 *更改非登记状态的押品状态（商链通）
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 01--登记02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int updateStatusBatch(String bfStatus,String status,String guarantyNo,Context context)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.updateStatusBatch(bfStatus,status,guarantyNo,context);
		} catch (Exception e) {
			throw new ComponentException("更改非登记状态的押品状态失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 批量更改押品关联下货物的状态（商链通）
	 * @param ic 需要做状态修改的货物编号集
	 * @param status 状态01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return
	 * @throws DaoException 
	 * @throws ComponentException
	 */
	public void updateStatusBatchCheck(String status,IndexedCollection iColl,Connection conn)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		cmisAgent.updateStatusBatchCheck(status,iColl,conn);
		} catch (Exception e) {
			throw new ComponentException("批量更改押品关联下货物的状态信息失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 批量更改押品关联下货物的状态（货物出入库清单表）
	 * @param ic 需要做状态修改的货物编号集
	 * @param status 状态01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return
	 * @throws DaoException 
	 * @throws ComponentException
	 */
	public void updateStatusBatchCheckRepl(String status,IndexedCollection iColl,Connection conn)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		cmisAgent.updateStatusBatchCheckRepl(status,iColl,conn);
		} catch (Exception e) {
			throw new ComponentException("批量更改押品关联下货物的状态信息失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据押品编号查询入库管理表中处于登记 状态的记录
	 * @param guarantyContNo 押品编号
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryRegiRecord(String guarantyNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.queryRegiRecord(guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号查询入库管理表中处于登记 状态的记录失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据押品编号查询出库管理表中处于登记 状态的记录
	 * @param guarantyContNo 押品编号
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public KeyedCollection queryRegiOutStorRecord(String guarantyNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.queryRegiOutStorRecord(guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号查询入库管理表中处于登记 状态的记录失败，失败原因："+e.getMessage());
		}
	}
	/**
	 *  获取押品记录（pop）
	 * @param conStr 查询条件（没有查询查询条件是 conStr 为1=1）
	 * @return res_value 
	 * @throws ComponentException
	 */
	public IndexedCollection getGuarantyInfoList(String conStr,PageInfo pageInfo,DataSource dataSource) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.getGuarantyInfoList(conStr,pageInfo,dataSource);
		} catch (Exception e) {
			throw new ComponentException("获取押品记录失败，失败原因："+e.getMessage());
		}
	
    }
	/**
	 * 根据押品编号查询处于登记状态的提货清单记录，根据返回的状态值判断需要做的操作是新增操作或者是修改操作
	 * @param guarantyContNo 押品编号 cargoId 货物编号,op_type 操作类型（1--货物置换，2--保证金提货，3--货物出库）
	 * @return result true--新增操作，false--修改操作 
	 * @throws ComponentException
	 */
	public KeyedCollection queryMortDelivList(String guarantyNo,String cargoId,String op_type) throws ComponentException{
		MortCommenOwnerAgent cmisAgent ;
		try {
			cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
			return cmisAgent.queryMortDelivList(guarantyNo,cargoId,op_type);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号查询处于登记状态的提货清单记录失败，失败原因："+e.getMessage());
		}
	
    }
	/**
	 *根据提货流水将处于出库待记账状态的提货清单记录更新为出库状态
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 05--出库待记账 03--出库
	 * @return  
	 * @throws ComponentException
	 */
	public int updateMortDelivList(String bfStatus,String status,String serno)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.updateMortDelivList(bfStatus,status,serno);
		} catch (Exception e) {
			throw new ComponentException("更改保证金提货清单的货物状态失败，失败原因："+e.getMessage());
		}
	}
	/**
	 *根据保证金提货流水更新其货物的在库数量和在库总价值
	 * @param serno 流水
	 * @return  
	 * @throws ComponentException
	 */
	public int updatemortCargoPledge(String serno)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.updatemortCargoPledge(serno);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号更新其货物的在库数量和在库总价值失败，失败原因："+e.getMessage());
		}
	}
	/**
	 *更改非登记状态的押品状态（商链通）
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 01--登记02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public void updateStatusBatchRepl(String bfStatus,IndexedCollection iColl,Connection conn)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		 cmisAgent.updateStatusBatchRepl(bfStatus,iColl,conn);
		} catch (Exception e) {
			throw new ComponentException("批量更改押品关联下货物的状态（货物置换清单）失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据押品编号获取押品下关联的货物库存总价值（货物置换清单）
	 * @param guarantyContNo 押品编号
	 * @param status 货物状态 01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @param oper 操作类型 1--初次入库，2--补货，3--出库，4--置出，5--置入，6--提货
	 * @return resultKc  
	 * @throws ComponentException
	 */
	public KeyedCollection queryIdentyTotalInfoRepl(String guarantyNo,String status,String oper) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.queryIdentyTotalInfoRepl(guarantyNo,status,oper);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号获取押品下关联的货物库存总价值失败，失败原因："+e.getMessage());
		}
	}
	/**
	 *批量更改押品关联下货物的状态（货物质押清单）
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 01--登记02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @param serno 业务编号 
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int updateStatusBatchCargoRepl(String bfStatus,String status,String guarantyNo,String serno)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.updateStatusBatchCargoRepl(bfStatus,status,guarantyNo,serno);
		} catch (Exception e) {
			throw new ComponentException("批量更改押品关联下货物的状态（货物质押清单）失败，失败原因："+e.getMessage());
		}
	}
	/**
	 *货物置换时，新增货物记录
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 01--登记02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int insertmortCargoPledge(String status,String guarantyNo)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.insertmortCargoPledge(status,guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("货物置换时，新增货物记录失败，失败原因："+e.getMessage());
		}
	}
	
	/**
	 *货物置换时，新增货物记录
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 01--登记02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int insertmortCargoPledge4ZH(String status,String serno)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.insertmortCargoPledge4ZH(status,serno);
		} catch (Exception e) {
			throw new ComponentException("货物置换时，新增货物记录失败，失败原因："+e.getMessage());
		}
	}
	
	/**
	 * 货物置换时，新增提货记录
	 * @param bfStatus 需要被更改的货物状态
	 * @param status 货物状态 01--登记02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int insertMortDelivList(String status,String guarantyNo)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.insertMortDelivList(status,guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("货物置换时，新增提货记录失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 货物动态出入库时，向提货清单表中存入货物的历史数据记录
	 * @param oper 操作类型（1--初次入库，2--补货，3--出库，4--置出，5--置入，6--提货） 
	 * @param status 货物状态 01--登记 02--入库 03--出库 04--入库待记账 05--出库待记账
	 * @return resultKc 
	 * @throws ComponentException
	 */
	public int insertMortDelivListByStatus(String serno,String oper,String status,String guarantyNo)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.insertMortDelivListByStatus(serno,oper,status,guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("货物动态出入库时，向提货清单表中存入货物的历史数据记录失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 删除保证金提货流水记录的同时删除提货清单中的数据（保证金流水只有登记状态时，可以进行删除）
	 * @param guarantyNo 押品ID
	 * @return int
	 * @throws Exception 
	 */
	public int deleteMortDelivListByGuarantyNo(String guarantyNo) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.deleteMortDelivListByGuarantyNo(guarantyNo);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号关联删除保证金提货流水记录的同时删除提货清单中的数据失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据出库申请业务编号删除出库申请明细信息
	 * @param serno 业务编号
	 * @return 
	 * @throws Exception
	 */
	public int deleteMortStorExwaDetailBySerno(String serno) throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
			return cmisAgent.deleteMortStorExwaDetailBySerno(serno);
		} catch (Exception e) {
			throw new ComponentException("根据押品编号关联删除保证金提货流水记录的同时删除提货清单中的数据失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model 表模型
	 * @param conditionFields  过滤条件键值对
	 * @return 执行删除记录条数
	 * @throws AgentException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields ) throws ComponentException {
		int count = 0;
		try {
			MortCommenOwnerAgent mortPubAgent = (MortCommenOwnerAgent)this.getAgentInstance("MortCommenOwnerAgent");
			count = mortPubAgent.deleteByField(model, conditionFields);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
	/**
	 * 补货入库时，保存操作，将申请流水号赋值给新增补货的货物
	 * @param sernoBf 业务流水号(更改之前)
	 * @param serno 业务流水号（更改之后）
	 * @param cargo_status 货物状态
	 * @param guaranty_no 押品编号
	 * @return  
	 * @throws ComponentException
	 */
	public int updateMortCargoReplList(String serno,String sernoBf,String cargo_status,String guaranty_no)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.updateMortCargoReplList(serno,sernoBf,cargo_status,guaranty_no);
		} catch (Exception e) {
			throw new ComponentException("补货入库时，保存操作，将申请流水号赋值给新增补货的货物失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 补货入库记账时，将数据插入货物清单表（结果表）
	 * @param oper 操作类型（1--初次入库，2--补货，3--出库，4--置出，5--置入，6--提货）
	 * @param sero 业务流水号
	 * @return 
	 * @throws ComponentException
	 */
	public int insertMortCargoPledgeBySerno(String serno,String oper)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.insertMortCargoPledgeBySerno(serno,oper);
		} catch (Exception e) {
			throw new ComponentException("补货入库记账时，将数据插入货物清单表（结果表）失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 提货或者货物置换时，更新提货清单中关联业务流水号字段
	 * @param re_serno 关联业务流水号
	 * @param op_type 操作类型（1--货物置换，2--保证金提货）
	 * @param guaranty_no 押品编号
	 * @return  
	 * @throws ComponentException
	 */
	public int updateMortDelivListOper(String re_serno,String guaranty_no,String op_type)throws ComponentException{
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		try {
		return cmisAgent.updateMortDelivListOper(re_serno,guaranty_no,op_type);
		} catch (Exception e) {
			throw new ComponentException("提货或者货物置换时，更新提货清单中关联业务流水号字段失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 根据表名和条件删除表中数据
	 * @param tableName
	 * @param condition
	 * @return
	 * @throws EMPException 
	 */
	public int deleteDateByTableAndCondition(String tableName, String condition) throws EMPException {
		// TODO Auto-generated method stub
		MortCommenOwnerAgent cmisAgent = (MortCommenOwnerAgent)this.getAgentInstance(MORTConstant.MORTCOMMENOWNERAGENT);
		return cmisAgent.deleteDateByTableAndCondition(tableName,condition);
	}
}
