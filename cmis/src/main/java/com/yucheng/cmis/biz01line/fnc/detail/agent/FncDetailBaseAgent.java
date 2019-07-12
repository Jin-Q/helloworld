package com.yucheng.cmis.biz01line.fnc.detail.agent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.fnc.detail.dao.FncDetailBaseDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 *@Classname	FncBaseDetailAgent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午03:13:35  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		xuyp	
 *@Description：
 *@Lastmodified 
 *@Author
 */

public class FncDetailBaseAgent extends CMISAgent{

	
	
	/**
	 * 查询前期报表明细---应付账款fnc_acc_payable
	 * @param conn
	 * @param cus_id
	 * @param ym
	 * @param dao
	 * @return
	 * @throws AgentException
	 */
	public IndexedCollection checkFncFront_FncAccPayable(Connection conn, String cus_id,String ym,TableModelDAO dao) throws AgentException{
		IndexedCollection iCol = null ;
		try {
            //TableModelDAO tDao = this.getTableModelDAO();
			//System.out.println("PK值："+this.getPK(ym, cus_id, conn));
            String condition = "where  pk ='"+this.getPK(ym, cus_id, conn)+"'";
             iCol = dao.queryList("FncAccPayable",condition, conn);
             System.out.println("##################################:old1");
            System.out.println("iCol:"+iCol);
            System.out.println("##################################:old2");
            if (iCol.isEmpty()) {
                return null;
            }
        
        } catch (Exception e) {
            throw new AgentException(e);
        }
        
        return iCol;
		
	}
	
	
	/**
	 * 将前期报表明细插入---应付账款fnc_acc_payable
	 * @param conn
	 * @param pk   现在插入的报表应该要取的pk值   
	 * @param dao
	 * @return
	 * @throws AgentException
	 */
	public boolean insertFncFront_FncAccPayable(Connection conn, String pk,IndexedCollection iCollNew,TableModelDAO dao) throws AgentException{
		boolean flag = false;
		for(int i=0;i<iCollNew.size();i++){
            	KeyedCollection kColl = (KeyedCollection)iCollNew.get(i);
            	String s = UUID.randomUUID().toString(); 
                //去掉“-”符号 
                String seq = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
            	try {
					kColl.setDataValue("seq", seq);
					kColl.setDataValue("pk", pk);
					dao.insert(kColl, conn);
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ObjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EMPJDBCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
		}
        System.out.println("##########################new 1");
        System.out.println(iCollNew);
        System.out.println("##########################new 2");
        return flag;
		
	}
	
	
	
	
	/**
	 * 根据客户id和月份确定该客户上一个月份的的报表的PK值
	 * @param pk
	 * @return
	 */
	public String getPK(String ym,String cus_id,Connection conn){
		String result = null;
		String ym_value = null;
		try{
			int year = Integer.parseInt(ym)-1;
			ym_value = String.valueOf(year);
			
		}catch(Exception e){
			System.out.println("年份有误!");			
		}
			
		try {
			String sql = "Select pk From Fnc_detail_base Where trim(fnc_ym)='"+ym_value+"'and trim(cus_id)='"+cus_id+"'";
			
			PreparedStatement s = conn.prepareStatement(sql);
			ResultSet rs = s.executeQuery();
			while(rs.next()){
				result = rs.getString("pk");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args){
		
		
		
	}

	/**
	 * 查询前期报表明细---应收账款fnc_acc_Receivable
	 * @param conn
	 * @param cus_id
	 * @param ym
	 * @param dao
	 * @return
	 * @throws AgentException 
	 * @throws AgentException
	 */
	public IndexedCollection checkFncFront_FncAccReceivable(
			Connection connection, String cus_id, String year, TableModelDAO dao) throws AgentException {
		IndexedCollection iCol = null ;
		try {
            //TableModelDAO tDao = this.getTableModelDAO();
			//System.out.println("PK值："+this.getPK(ym, cus_id, conn));
            String condition = "where  pk ='"+this.getPK(year, cus_id, connection)+"'";
             iCol = dao.queryList("FncAccReceivable",condition, connection);
/*             System.out.println("##################################:old1");
            System.out.println("iCol:"+iCol);
            System.out.println("##################################:old2");*/
            if (iCol.isEmpty()) {
                return null;
            }
        
        } catch (Exception e) {
            throw new AgentException(e);
        }
        
        return iCol;
	}


	/**
	 * 将前期报表明细插入---应付账款fnc_acc_Receivable
	 * @param conn
	 * @param pk   现在插入的报表应该要取的pk值   
	 * @param dao
	 * @return
	 * @throws AgentException
	 */
	public boolean insertFncFront_FncAccReceivable(Connection connection,
			String pk_value, IndexedCollection coll_new, TableModelDAO dao) {
		boolean flag = false;
		for(int i=0;i<coll_new.size();i++){
            	KeyedCollection kColl = (KeyedCollection)coll_new.get(i);
            	String s = UUID.randomUUID().toString(); 
                //去掉“-”符号 
                String seq = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
            	try {
					kColl.setDataValue("seq", seq);
					kColl.setDataValue("pk", pk_value);
					dao.insert(kColl, connection);
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ObjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EMPJDBCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
		}
/*		System.out.println("##########################new 1");
        System.out.println(coll_new);
        System.out.println("##########################new 2");*/
        return flag;
		
	}


	/**
	 * 查询前期报表明细---应付账款fnc_assure
	 * @param conn
	 * @param cus_id
	 * @param ym
	 * @param dao
	 * @return
	 * @throws AgentException 
	 * @throws AgentException
	 */
	public IndexedCollection checkFncFront_FncAssure(Connection connection,
			String cus_id, String year, TableModelDAO dao) throws AgentException {
		// TODO Auto-generated method stub
		IndexedCollection iCol = null ;
		try {
            //TableModelDAO tDao = this.getTableModelDAO();
			//System.out.println("PK值："+this.getPK(ym, cus_id, conn));
            String condition = "where  pk ='"+this.getPK(year, cus_id, connection)+"'";
             iCol = dao.queryList("FncAssure",condition, connection);
/*             System.out.println("##################################:old1");
            System.out.println("iCol:"+iCol);
            System.out.println("##################################:old2");*/
            if (iCol.isEmpty()) {
                return null;
            }
        
        } catch (Exception e) {
            throw new AgentException(e);
        }
        
        return iCol;
	}


	/**
	 * 将前期报表明细插入---应付账款fnc_assure
	 * @param conn
	 * @param pk   现在插入的报表应该要取的pk值   
	 * @param dao
	 * @return
	 * @throws AgentException
	 */
	public boolean insertFncFront_FncAssure(Connection connection,
			String pk_value, IndexedCollection coll_new, TableModelDAO dao) {
		boolean flag = false;
		for(int i=0;i<coll_new.size();i++){
            	KeyedCollection kColl = (KeyedCollection)coll_new.get(i);
            	String s = UUID.randomUUID().toString(); 
                //去掉“-”符号 
                String seq = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
            	try {
					kColl.setDataValue("seq", seq);
					kColl.setDataValue("pk", pk_value);
					dao.insert(kColl, connection);
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ObjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EMPJDBCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
		}
/*		System.out.println("##########################new 1");
        System.out.println(coll_new);
        System.out.println("##########################new 2");*/
        return flag;
		
	}


	/**
	 * 查询前期报表明细---固定资产fnc_FixedAsset
	 * @param conn
	 * @param cus_id
	 * @param ym
	 * @param dao
	 * @return
	 * @throws AgentException 
	 * @throws AgentException 
	 * @throws AgentException
	 */
	public IndexedCollection checkFncFront_FncFixedAsset(Connection connection,
			String cus_id, String year, TableModelDAO dao) throws AgentException {
		// TODO Auto-generated method stub
		IndexedCollection iCol = null ;
		try {
            //TableModelDAO tDao = this.getTableModelDAO();
			//System.out.println("PK值："+this.getPK(ym, cus_id, conn));
            String condition = "where  pk ='"+this.getPK(year, cus_id, connection)+"'";
             iCol = dao.queryList("FncFixedAsset",condition, connection);
/*             System.out.println("##################################:old1");
            System.out.println("iCol:"+iCol);
            System.out.println("##################################:old2");*/
            if (iCol.isEmpty()) {
                return null;
            }
        
        } catch (Exception e) {
            throw new AgentException(e);
        }
        
        return iCol;
	}


	/**
	 * 将前期报表明细插入---固定资产FncFixedAsset
	 * @param conn
	 * @param pk   现在插入的报表应该要取的pk值   
	 * @param dao
	 * @return
	 * @throws AgentException
	 */
	public boolean insertFncFront_FncFixedAsset(Connection connection,
			String pk_value, IndexedCollection coll_new, TableModelDAO dao) {
		boolean flag = false;
		for(int i=0;i<coll_new.size();i++){
            	KeyedCollection kColl = (KeyedCollection)coll_new.get(i);
            	String s = UUID.randomUUID().toString(); 
                //去掉“-”符号 
                String seq = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
            	try {
					kColl.setDataValue("seq", seq);
					kColl.setDataValue("pk", pk_value);
					dao.insert(kColl, connection);
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ObjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EMPJDBCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
		}
	/*	System.out.println("##########################new 1");
        System.out.println(coll_new);
        System.out.println("##########################new 2");*/
        return flag;
		
	}
	public String getFncDetailBasePk(String cusId,String ym){
		String pk = "";
		FncDetailBaseDao  dao = new FncDetailBaseDao();
		Connection conn=this.getConnection();
		pk = dao.getPkByCusIdAndYm(cusId,ym,conn);
		return pk;
	}
	public String insertBeforeMess(String beforePk,String thisPk,String modelId,TableModelDAO dao) throws EMPException{
		//从具体的明细表中查询数据（根据参数modelId判断是那张表）
		String info ="";
		IndexedCollection beforeICol = null ;
		IndexedCollection thisICol = null ;
		String seq = "";
		KeyedCollection removeKColl = null;
		KeyedCollection insertKColl = null;
		String newSeq = "";
		String beforeYm = "";
		String thisYm = "";
		Connection conn=this.getConnection();
        String condition = "where  pk ='"+beforePk+"'";
        beforeICol = dao.queryList(modelId,condition, conn);
        if (beforeICol.isEmpty()) {
        	info = "beforeEmpty";
        }else{
        	//查询本期的数据
        	  String conditionThis = "where  pk ='"+thisPk+"'";
        	  thisICol = dao.queryList(modelId,conditionThis, conn);
        	  for(int i=0;i<thisICol.size();i++){
        		  removeKColl = (KeyedCollection)thisICol.get(i);
        		  seq = (String)removeKColl.getDataValue("seq");
        		  int count=dao.deleteByPk(modelId, seq, conn);
        		  if(count!=1){
      				throw new EMPException("删除本期明细报表失败"+count+"���¼");
      			  }
        	   }
        	  //把上期报表信息导入本期中
        	  for(int j=0;j<beforeICol.size();j++){
        		  insertKColl = (KeyedCollection)beforeICol.get(j);
        		  String s = UUID.randomUUID().toString(); 
                  newSeq = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
          		  insertKColl.setDataValue("seq", newSeq);
          		  insertKColl.setDataValue("pk", thisPk);
          		  beforeYm = (String)insertKColl.getDataValue("fnc_ym");
	          		//如果是1一月份的话
	          	  String month = "";
	          	  int year = 0;
	          	  month = beforeYm.substring(4);
	          	  if(month.equals("12")){
	          			year = Integer.parseInt(beforeYm.substring(0, 4))+1;
	          			thisYm = String.valueOf(year)+"01";
	          	  }else{
	          			 year = Integer.parseInt(beforeYm)+1;
	          			 thisYm = String.valueOf(year);
	          	 }
          		  insertKColl.setDataValue("fnc_ym", thisYm);
    		      insertKColl.setName(modelId);
    		      dao.insert(insertKColl, conn);
        	  }
        	  info =CMISMessage.SUCCESS;
        }
		return info;
	}
	
	
}
