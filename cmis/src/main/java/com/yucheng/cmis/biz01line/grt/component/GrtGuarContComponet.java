package com.yucheng.cmis.biz01line.grt.component;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.grt.agent.GrtGuarContAgent;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.exception.DaoException;
import com.yucheng.cmis.util.TableModelUtil;

public class GrtGuarContComponet extends CMISComponent {
	private static final Logger logger = Logger.getLogger(IqpServiceInterfaceImple.class);
	
	//新增保证人的同时新增保证人与担保合同的关联信息
	public void insertGrtGuarantyRe(KeyedCollection kColl) throws ComponentException {
		try{
		GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
		cmisAgent.insertGrtGuarantyRe(kColl);
		} catch (Exception e) {
			throw new ComponentException("新增保证人与担保合同的关联信息失败"+e.getMessage());
		} 
	}
	//删除保证人的同时删除其与担保合同的关联记录
	public int deleteGrtGuarantyReGuar(String guarId) throws ComponentException {
		int count=0;
		try {
			GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
			count = cmisAgent.deleteGrtGuarantyReGuar(guarId);
		} catch (Exception e) {
			throw new ComponentException("删除保证人的同时删除其与担保合同的关联记录失败"+e.getMessage());
			}
			return count;
	}
	//删除一笔合同的同时删除其与保证人的关联记录
	public int deleteGrtGuarantyReCont(String guarContN0) throws ComponentException {
		int count=0;
		try {	
			GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
			count = cmisAgent.deleteGrtGuarantyReCont(guarContN0);
		} catch (Exception e) {
			throw new ComponentException("删除一笔合同的同时删除其与保证人的关联记录失败"+e.getMessage());
		}
			return count;
    }
	/**
	 * 通过合同编号获得其下关联下的担保信息
	 * @param guarContNo 合同编号
	 * @return KeyedCollection
	 */
    public IndexedCollection queryGrtGuarantyReGuarList(String guarContN0) throws ComponentException {
	    IndexedCollection returnKColl = new IndexedCollection();
	    try {	
		GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
		returnKColl = cmisAgent.queryGrtGuarantyReGuarList(guarContN0);
	    } catch (Exception e) {
			throw new ComponentException("通过合同编号获得其下关联下的担保信息失败"+e.getMessage());
		}
		return returnKColl;
    }
    /**
	 * 借助担保合同和保证人的关联关系表查询担保合同表和保证人信息表中所有相关的保证人信息和担保合同信息
	 * @param guarContNo
	 * @param  客户编号，担保合同编号
	 * @return IndexedCollection
	 * @throws Exception
	 */
    public IndexedCollection queryGrtGuaranteeAllList() throws ComponentException {
	    IndexedCollection returnKColl = new IndexedCollection();
	    try {	
			GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
			returnKColl = cmisAgent.queryGrtGuaranteeAllList();
	    } catch (Exception e) {
			throw new ComponentException("借助担保合同和保证人的关联关系表查询担保合同表和保证人信息表中所有相关的保证人信息和担保合同信息失败"+e.getMessage());
		}
		return returnKColl;
    }
    /**
	 * 通过担保合同编号和保证人客户编号通过关联关系表查出唯一相关的保证人信息和合同信息
	 * @param guarContNo
	 * @param cusId，guarContNo 客户编号，担保合同编号
	 * @return KeyedCollection
	 * @throws Exception
	 */
    public KeyedCollection queryGrtGuaranteeAllDetail(String guarContNo,String cusId) throws ComponentException {
    	KeyedCollection returnIColl = new KeyedCollection();
    	try {	
		GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
		returnIColl = cmisAgent.queryGrtGuaranteeAllDetail(guarContNo,cusId);
	    } catch (Exception e) {
			throw new ComponentException("通过担保合同编号和保证人客户编号通过关联关系表查出唯一相关的保证人信息和合同信息失败"+e.getMessage());
		}
		return returnIColl;
    }
	/**根据合同编号获取池编号（一个担保合同只能引用一个池作为担保品）*/
    public String getDrfpoNoByGuarContNo(String guarContNo) throws ComponentException {
    	String result ="";
    	try {	
		GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
		result = cmisAgent.getDrfpoNoByGuarContNo(guarContNo);
    	} catch (Exception e) {
 			throw new ComponentException("获取池编号失败"+e.getMessage());
 		}
		return result;
    }
    /**根据合同编号获取该合同下保证人担保金额的总额*/
    public String getGuarAmtByGuarContNo(String guarContNo) throws ComponentException {
    	String result ="";
    	try {	
		GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
		result = cmisAgent.getGuarAmtByGuarContNo(guarContNo);
    	} catch (Exception e) {
 			throw new ComponentException("根据合同编号获取该合同下保证人担保金额的总额失败"+e.getMessage());
 		}
		return result;
    }
    /**根据合同编号获取该合同下保证人担保金额的总额*/
    public String queryGrtGuarantyAmt(String guarContNo) throws ComponentException {
    	String result ="";
    	try {	
		GrtGuarContAgent cmisAgent = (GrtGuarContAgent)this.getAgentInstance("GrtGuarCont");
		result = cmisAgent.queryGrtGuarantyAmt(guarContNo);
    	} catch (Exception e) {
 			throw new ComponentException("根据合同编号["+guarContNo+"]获取该合同下保证人担保金额的总额失败"+e.getMessage());
 		}
		return result;
    }
    /**
	 * 通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数
	 * guar_cont_no  担保合同编号 
	 * @return IndexedCollection 
	 * @throws Exception 
	 */
	public int getGuarBailMultiple(String guar_cont_no,DataSource dataSource,Connection connection) throws Exception{
		logger.info("---------------通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数 开始---------------");
		IndexedCollection res_value = new IndexedCollection();
		KeyedCollection res = new KeyedCollection();
		int rel=0;
		try{
			String sql_select = "select cus_com.Guar_Bail_Multiple,cus_com.cus_id from cus_com cus_com,cus_base cus_base where cus_com.cus_id=cus_base.cus_id and cus_base.cus_type='A2' and cus_com.cus_id in(select grt.cus_id from grt_guarantee grt where grt.guar_id in(select grtRe.Guaranty_Id from grt_guaranty_re grtRe,grt_loan_r_gur grtLoan where grtRe.Guar_Cont_No=grtLoan.guar_cont_no and grtRe.Guar_Cont_No='"+guar_cont_no+"'))";
			res_value = TableModelUtil.buildPageData(null, dataSource, sql_select);
			if(res_value.size()>0){
				KeyedCollection relKColl = (KeyedCollection)res_value.get(0);
				String Guar_Bail_Multiple = (String)relKColl.getDataValue("guar_bail_multiple");
				String cus_id = (String)relKColl.getDataValue("cus_id");
				res.put("guar_cont_no", guar_cont_no);
				res.put("cus_id", cus_id);
			    rel = SqlClient.update("updateIqpAssSesMultiple", res, Guar_Bail_Multiple, null, connection);
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception("通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数!原因:"+e.getMessage());
		}
		logger.info("---------------通过担保合同编号查询此担保合同下保证人为担保公司时，担保公司的担保放大倍数结束---------------");
		return rel;
	}

}