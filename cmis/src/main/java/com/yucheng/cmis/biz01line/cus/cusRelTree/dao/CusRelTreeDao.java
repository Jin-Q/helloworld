package com.yucheng.cmis.biz01line.cus.cusRelTree.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusTree;
import com.yucheng.cmis.biz01line.cus.cusRelTree.util.TreeUtil;
import com.yucheng.cmis.pub.CMISDao;

public class CusRelTreeDao extends CMISDao {
//	private static final double perc = 0.05;
	/**
	 * 取得企业关键人
	 * @param cusTree
	 * @return
	 * @throws EMPException
	 */
	public List<CusTree> getKeyCus(String cusId, Connection conn)
			throws EMPException {
		Statement stmt=null;
		ResultSet rs=null;
		CusTree tmpTree = new CusTree();
		List<CusTree> list = new ArrayList<CusTree>();
		String cusIdRel = "";
		try {
			stmt = conn.createStatement();
			//客户高管
			String sqlFromCusCom = "select s.cus_id_rel,t.cus_name,t.cus_type,com_mrg_typ from cus_com_manager s ,cus_base t" + 
						" where  s.cus_id = '"+cusId+"' and s.cus_id_rel=t.cus_id" ;
						//" s.com_mrg_cert_code = t.cert_code and s.com_mrg_cert_typ = t.cert_type";

			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sqlFromCusCom: " + sqlFromCusCom);
			rs = stmt.executeQuery(sqlFromCusCom);
			while (rs.next()) {
				tmpTree = new CusTree();
				cusIdRel = rs.getString("cus_id_rel");
				if(cusIdRel != null) {
					tmpTree.setNodeId(cusIdRel);
					tmpTree.setNodeName(rs.getString("cus_name"));
					String com_mrg_typ = rs.getString("com_mrg_typ");
					if("01".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(实际控制人)");
					else if("02".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(法人代表)");
					else if("03".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(总经理（厂长）)");
					else if("04".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(财务负责人)");
					else if("05".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(董事)");
					else if("06".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(主管业务副总经理)");
					else if("07".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(董事长)");
					else if("08".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(授权经办人)");
					else if("90".equals(com_mrg_typ))
						tmpTree.setNodeInfo("(其他)");
					tmpTree.setNodeType(TreeUtil.nodeTypeGg);
					tmpTree.setNodeAttribute("TreeCusIndiv");
					tmpTree.setNodeCusType(rs.getString("cus_type"));
					list.add(tmpTree);
				}
			}
		}  catch (SQLException e) {
			throw new EMPException(e);
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return list;
	}
	
	/**
	 * 取得关键人配偶、父母、子女、兄弟姐妹
	 * @param cusTree
	 * @return
	 * @throws EMPException 
	 */
	public List<CusTree> getRelCus(String cusId, Connection conn) throws EMPException
	{
		Statement stmt=null;
		ResultSet rs=null;
		List<CusTree> list = new ArrayList<CusTree>();
		CusTree tmpTree = null;
		try {
			stmt = conn.createStatement();
			//配偶
			String sqlFromCusIndiv = "select t.cus_id, t.cus_name, t.cus_type,indiv_cus_rel from cus_indiv_soc_rel s ,cus_base t"+
							" where s.cus_id = '"+cusId+"' and s.cus_id_rel=t.cus_id";
							//" s.indiv_rl_cert_code = t.cert_code and s.indiv_rel_cert_typ = t.cert_type ";	
								
			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sqlFromCusIndiv: " + sqlFromCusIndiv);
			rs = stmt.executeQuery(sqlFromCusIndiv);
			while (rs.next()) {
				tmpTree = new CusTree();
				tmpTree.setNodeId(rs.getString("cus_id"));
				tmpTree.setNodeName(rs.getString("cus_name"));
				String cusRelType = rs.getString("indiv_cus_rel");
				if("1".equals(cusRelType))
					tmpTree.setNodeInfo("(配偶)");
				else if ("2".equals(cusRelType))
					tmpTree.setNodeInfo("(父母)");
				else if ("3".equals(cusRelType))
					tmpTree.setNodeInfo("(子女)");
				else if ("4".equals(cusRelType))
					tmpTree.setNodeInfo("(其他血亲关系)");
				else if ("5".equals(cusRelType))
					tmpTree.setNodeInfo("(其他姻亲关系)");
				else if ("6".equals(cusRelType))
					tmpTree.setNodeInfo("(同事)");
				else if ("7".equals(cusRelType))
					tmpTree.setNodeInfo("(合伙人)");
				else if ("8".equals(cusRelType))
					tmpTree.setNodeInfo("(其他关系)");
				else if ("9".equals(cusRelType))
					tmpTree.setNodeInfo("(兄弟姐妹)");
				tmpTree.setNodeType(TreeUtil.nodeTypeQs);
//				tmpTree.setNodeAttribute(rs.getString("cus_type").startsWith("1") ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeAttribute(rs.getString("cus_type").startsWith("Z") ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeCusType(rs.getString("cus_type"));
				list.add(tmpTree);
			}
			
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return list;
	}
	
	/**
	 * 取得关键人所在的企业
	 * @param cusTree
	 * @return
	 * @throws EMPException 
	 */
	public List<CusTree> getKeyCom(String cusId, Connection conn) throws EMPException
	{
		Statement stmt=null;
		ResultSet rs=null;
		List<CusTree> list = new ArrayList<CusTree>();
		CusTree tmpTree = null;
		try {
			stmt = conn.createStatement();
//			String sqlForCert = "select cert_code, cert_type from cus_base where cus_id='" + cusId + "' and cus_type <> '250'";
//			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sql: " + sqlForCert);
//			String certId = "";
//			String certType = "";
//			rs = stmt.executeQuery(sqlForCert);
//			if(rs.next())
//			{
//				certId = rs.getString("cert_code");
//				certType = rs.getString("cert_type");
//			}
//			
//			stmt = conn.createStatement();
			
			String sql = "select cus_id, cus_type, cus_name from cus_base where cus_id in " +
					" (select cus_id from cus_com_manager where com_mrg_typ in ('01','02') and " +
					" cus_id_rel ='"+cusId+"') and cus_type <>'250'" ;
				//" com_mrg_cert_typ ='"+certType+"' and com_mrg_cert_code ='"+certId+"') and cus_type <>'250'" ;
			
			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sql: " + sql);
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				tmpTree = new CusTree();
				tmpTree.setNodeId(rs.getString("cus_id"));
				tmpTree.setNodeName(rs.getString("cus_name"));
				tmpTree.setNodeInfo("(经营企业)");
				tmpTree.setNodeType(TreeUtil.nodeTypeDwtz);
				tmpTree.setNodeAttribute("TreeCusCom");
				tmpTree.setNodeCusType(rs.getString("cus_type"));
				list.add(tmpTree);
			}
			
		}  catch (SQLException e) {
			throw new EMPException(e);
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return list;
	}
	
	/**
	 * 从资本构成信息查询到的主要投资企业和法人（控制50%以上股权或为第一大股东）
	 * @param cusTree
	 * @return
	 * @throws EMPException 
	 */
	public List<CusTree> getComAndIndivFrolistital(String cusId, Connection conn) throws EMPException
	{
		Statement stmt=null;
		ResultSet rs=null;
		List<CusTree> list = new ArrayList<CusTree>();
		CusTree tmpTree = null;
		try {
			stmt = conn.createStatement();
			String sql = "select t.cus_id, t.cus_name, invt_typ, t.cus_type,s.invt_perc from (select t.*,dense_rank() over(partition by t.cus_id order by t.invt_perc desc) rn from cus_com_rel_apital t where cus_id='"
				+ cusId + "') s, cus_base t where s.cus_id='"
				+ cusId + "' and s.cus_id_rel=t.cus_id" ;	//and s.invt_perc >= "+perc;//不控制比例
//			+ cusId + "' and s.cert_typ = t.cert_type and s.cert_code = t.cert_code and t.cus_type <> '250' and s.invt_perc >= "+perc;
			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sql: " + sql);
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				tmpTree = new CusTree();
				tmpTree.setNodeId(rs.getString("cus_id"));
				tmpTree.setNodeName(rs.getString("cus_name"));
				double invtPerc = rs.getDouble("invt_perc");
				if(invtPerc>=0.5)
					tmpTree.setNodeInfo("(资本构成——绝对控股)");
				else 
					tmpTree.setNodeInfo("(资本构成)");
				tmpTree.setNodeType(TreeUtil.nodeTypeZbgc);
				tmpTree.setNodeAttribute("1".equals(rs.getString("invt_typ")) ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeCusType(rs.getString("cus_type"));
				list.add(tmpTree);
			}
			
		}  catch (SQLException e) {
			throw new EMPException(e);
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return list;
	}
	
	/**
	 * 从对外投资信息查询到的企业(第一大股东)
	 * 先从对外投资信息里查到需要查看所有投资的企业
	 * 再从资本构成表里查投资的这些企业是否是第一大股东
	 * @param cusTree
	 * @return
	 * @throws EMPException 
	 */
	public List<CusTree> getComAndIndivFromInvest(String cusId, Connection conn) throws EMPException
	{
		Statement stmt=null;
		ResultSet rs=null;
		List<CusTree> list = new ArrayList<CusTree>();
		CusTree tmpTree = null;
		try {
			stmt = conn.createStatement();
			//查询组织机构代码 //原查询条件
//			String sql = "select n.* from (select cus_id_rel, cus_id,invt_perc from (select t.cus_id, t.cus_id_rel, t.invt_perc, DENSE_RANK() OVER(PARTITION BY t.cus_id ORDER BY t.invt_perc DESC) rn from cus_com_rel_apital t where cus_id in (select cus_id_rel from cus_com_rel_invest where cus_id = '"
//					+ cusId
//					+ "')) x where invt_perc >= "+perc+") m, cus_base n where m.cus_id_rel = '"
//					+ cusId + "' and m.cus_id = n.cus_id and n.cus_type <> '250'";
			
			String sql = "select n.* from (select cus_id_rel, cus_id from cus_com_rel_invest ) m, cus_base n where m.cus_id = '"
				+ cusId + "' and m.cus_id_rel = n.cus_id ";
			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sql: " + sql);
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				tmpTree = new CusTree();
				tmpTree.setNodeId(rs.getString("cus_id"));
				tmpTree.setNodeName(rs.getString("cus_name"));
				tmpTree.setNodeInfo("(对外投资)");
				tmpTree.setNodeType(TreeUtil.nodeTypeDwtz);
//				tmpTree.setNodeAttribute(rs.getString("cus_type").startsWith("1") ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeAttribute(rs.getString("cus_type").startsWith("Z") ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeCusType(rs.getString("cus_type"));
				list.add(tmpTree);
			}
			
		}  catch (SQLException e) {
			throw new EMPException(e);
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return list;
	}
	
	/**
	 * 从所有对公客户里查找同时被第三方控制的企业
	 * @param cusTree
	 * @return
	 * @throws EMPException 
	 */
/*	public List<CusTree> searchRelThirdCusList(String cusId, Connection conn) throws EMPException
	{
		Statement stmt=null;
		ResultSet rs=null;
		List<CusTree> list = new ArrayList<CusTree>();
		CusTree tmpTree = null;
		try {
			stmt = conn.createStatement();
			//最大投资方投资的另外的公司
			String sql1 = "select r.cus_id, r.cus_type, r.cus_name, rk from (select dense_rank() over(partition by cus_id order by invt_perc desc) rk, s.* from cus_com_rel_apital s) t, cus_base r where t.cus_id_rel in (select cus_id_rel from (select dense_rank() over(partition by cus_id order by invt_perc desc) rn, x.* from cus_com_rel_apital x where cus_id = '"
				+ cusId + "') y where rn = 1) and rk = 1 and t.cus_id <> '"
				+ cusId + "' and t.cus_id_rel=r.cus_id";
//			+ cusId + "' and r.cert_code = t.cert_code and r.cert_type = t.cert_typ and r.cus_type <> '250'";
			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sql1: " + sql1);
			//投资方的投资方
			String sql2 = "select r.cus_id, r.cus_type, r.cus_name from (select dense_rank() over (partition by cus_id order by invt_perc desc) rk, s.* from cus_com_rel_apital s) t, cus_base r where t.cus_id in (select cus_id_rel from (select dense_rank() over(partition by cus_id order by invt_perc desc) rn, x.* from cus_com_rel_apital x where cus_id = '"
				+ cusId + "') y where rn = 1) and rk =1 and r.cus_id = t.cus_id_rel ";
//			+ cusId + "') y where rn = 1) and rk =1 and r.cus_id = t.cus_id_rel and r.cert_type = t.cert_typ and r.cert_code = t.cert_code and r.cus_type <> '250'";
			rs = stmt.executeQuery(sql1);
			while(rs.next())
			{
				tmpTree = new CusTree();
				tmpTree.setNodeId(rs.getString("cus_id"));
				tmpTree.setNodeName(rs.getString("cus_name"));
				tmpTree.setNodeInfo("(第三方控股)");
				tmpTree.setNodeType(TreeUtil.nodeTypeDwtz);
//				tmpTree.setNodeAttribute(rs.getString("cus_type").startsWith("1") ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeAttribute(rs.getString("cus_type").startsWith("Z") ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeCusType(rs.getString("cus_type"));
				list.add(tmpTree);
			}
			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sql2: " + sql2);
			rs = stmt.executeQuery(sql2);
			while(rs.next())
			{
				tmpTree = new CusTree();
				tmpTree.setNodeId(rs.getString("cus_id"));
				tmpTree.setNodeName(rs.getString("cus_name"));
				tmpTree.setNodeInfo("(子公司对外投资)");
				tmpTree.setNodeType(TreeUtil.nodeTypeDwtz);
				//根据cus_type判断是否为对公客户	
//				tmpTree.setNodeAttribute(rs.getString("cus_type").startsWith("1") ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeAttribute(rs.getString("cus_type").startsWith("Z") ? "TreeCusIndiv" : "TreeCusCom");
				tmpTree.setNodeCusType(rs.getString("cus_type"));
				list.add(tmpTree);
			}
			
		}  catch (SQLException e) {
			throw new EMPException(e);
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return list;
	}*/
	/**
	 * 从资本构成信息查询到的主要投资企业和法人
	 * @param cusTree
	 * @return
	 * @throws EMPException 
	 */
	public List<String> getComRelCus(String cusId, Connection conn) throws EMPException
	{
		Statement stmt=null;
		ResultSet rs=null;
		List<String> list = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql = "select b.cus_id,DENSE_RANK() OVER(PARTITION BY t.cus_id ORDER BY t.invt_perc DESC,cus_id_rel ) rn" +
					" from cus_com_rel_apital t,cus_base b where t.cus_id_rel = b.cus_id  and t.cus_id = '" + cusId + "'";
			EMPLog.log("CusRel", EMPLog.WARNING, 0, System.currentTimeMillis() +  "***sql: " + sql);
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{			
				String Cus_id = (String)rs.getString(1);
				list.add(Cus_id);
			}
			
		}  catch (SQLException e) {
			throw new EMPException(e);
		} finally {
			try {
				
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return list;
	}
}
