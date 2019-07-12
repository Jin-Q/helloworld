package com.yucheng.cmis.biz01line.cus.cusbase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

public class CusBaseDao extends CMISDao {

	/**
	 * 根据客户证件类型和证件号码获取客户基本的信息
	 * @param certCode
	 * @param certType
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public CusBase findCusBaseByCert(String certCode, String certType, Connection conn) throws SQLException{
		Statement stmt=null;
		ResultSet rs=null;
		CusBase cusBase = new CusBase();
		try {
			stmt = conn.createStatement();
			String sql = "select CUS_ID,CUS_NAME,CUS_SHORT_NAME,CUS_TYPE ,CERT_TYPE,CERT_CODE," +
					"OPEN_DATE,CUS_COUNTRY,LOAN_CARD_FLG,LOAN_CARD_ID,LOAN_CARD_PWD,LOAN_CARD_EFF_FLG," +
					"LOAN_CARD_AUDIT_DT,CUS_CRD_GRADE,CUS_CRD_DT,CUS_STATUS,MAIN_BR_ID,CUST_MGR,INPUT_ID,INPUT_BR_ID," +
					"INPUT_DATE,BELG_LINE,GUAR_CRD_GRADE from CUS_BASE " +
					"where CERT_TYPE='"+certType+"' and CERT_CODE='"+certCode+"'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cusBase.setCusId(rs.getString("CUS_ID"));
				cusBase.setCusName(rs.getString("CUS_NAME"));
				cusBase.setCusShortName(rs.getString("CUS_SHORT_NAME"));
				cusBase.setCusType(rs.getString("CUS_TYPE"));
				cusBase.setCertType(rs.getString("CERT_TYPE"));
				cusBase.setCertCode(rs.getString("CERT_CODE"));
				cusBase.setOpenDate(rs.getString("OPEN_DATE"));
				cusBase.setCusCountry(rs.getString("CUS_COUNTRY"));
				cusBase.setLoanCardFlg(rs.getString("LOAN_CARD_FLG"));
				cusBase.setLoanCardId(rs.getString("LOAN_CARD_ID"));
				cusBase.setLoanCardPwd(rs.getString("LOAN_CARD_PWD"));
				cusBase.setLoanCardEffFlg(rs.getString("LOAN_CARD_EFF_FLG"));
				cusBase.setLoanCardAuditDt(rs.getString("LOAN_CARD_AUDIT_DT"));
				cusBase.setCusCrdGrade(rs.getString("CUS_CRD_GRADE"));
				cusBase.setCusCrdDt(rs.getString("CUS_CRD_DT"));
				cusBase.setCusStatus(rs.getString("CUS_STATUS"));
				cusBase.setMainBrId(rs.getString("MAIN_BR_ID"));
				cusBase.setCustMgr(rs.getString("CUST_MGR"));
				cusBase.setInputId(rs.getString("INPUT_ID"));
				cusBase.setInputBrId(rs.getString("INPUT_BR_ID"));
				cusBase.setInputDate(rs.getString("INPUT_DATE"));
				cusBase.setBelgLine(rs.getString("BELG_LINE"));
				cusBase.setGuarCrdGrade(rs.getString("GUAR_CRD_GRADE"));
				
//				cusBase.setCertTypOther(rs.getString("CERT_TYP_OTHER"));
//				cusBase.setCertCodeOther(rs.getString("CERT_CODE_OTHER"));
//				cusBase.setCrtDateOther(rs.getString("CRT_DATE_OTHER"));
//				cusBase.setCrtEndDateOther(rs.getString("CRT_END_DATE_OTHER"));	
			}
		}  catch (SQLException e) {
			throw e;
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
				throw e;
			}
		}
		return cusBase;
	}
	
	/**
	 * 根据客户证件类型和证件号码获取客户基本的信息
	 * @param certCode
	 * @param certType
	 * @param conn
	 * @return
	 * @throws SQLException 
	 * @throws DuplicatedDataNameException 
	 * @throws InvalidArgumentException 
	 */
	public KeyedCollection findCusBaseByCert1(String certCode, String certType, Connection conn) throws SQLException, InvalidArgumentException, DuplicatedDataNameException{
		Statement stmt=null;
		ResultSet rs=null;
		KeyedCollection cusBase_kColl = new KeyedCollection();
		try {
			stmt = conn.createStatement();
//			String sql = "select CUS_ID,CUS_NAME,CUS_SHORT_NAME,CUS_TYPE ,CERT_TYPE,CERT_CODE," +
//					"OPEN_DATE,CUS_COUNTRY,LOAN_CARD_FLG,LOAN_CARD_ID,LOAN_CARD_PWD,LOAN_CARD_EFF_FLG," +
//					"LOAN_CARD_AUDIT_DT,CUS_CRD_GRADE,CUS_CRD_DT,CUS_STATUS,MAIN_BR_ID,CUST_MGR,INPUT_ID,INPUT_BR_ID," +
//					"INPUT_DATE,BELG_LINE,GUAR_CRD_GRADE from CUS_BASE CUS where" +
//					" (CERT_TYPE='"+certType+"' and CERT_CODE='"+certCode+"') OR (CUS_ID IN(SELECT CUS_ID FROM CUS_CERT_INFO WHERE CERT_TYP='"+certType+"' and CERT_CODE='"+certCode+"'))";
//			
			String sql = "select a.CUS_ID,a.CUS_NAME,a.CUS_SHORT_NAME,a.CUS_TYPE ,a.CERT_TYPE,a.CERT_CODE," +
			"a.OPEN_DATE,a.CUS_COUNTRY,a.LOAN_CARD_FLG,a.LOAN_CARD_ID,a.LOAN_CARD_PWD,a.LOAN_CARD_EFF_FLG," +
			"a.LOAN_CARD_AUDIT_DT,a.CUS_CRD_GRADE,a.CUS_CRD_DT,a.CUS_STATUS,a.MAIN_BR_ID,a.CUST_MGR,a.INPUT_ID,a.INPUT_BR_ID," +
			"a.INPUT_DATE,a.BELG_LINE,a.GUAR_CRD_GRADE,b.CERT_TYP CERT_TYP_OTHER,b.CERT_CODE CERT_CODE_OTHER," +
			"b.CRT_DATE CRT_DATE_OTHER,b.CRT_END_DATE CRT_END_DATE_OTHER,"+
			"c.INDIV_ID_EXP_DT,c.INDIV_ID_START_DT "+
			"from CUS_BASE a," +
			"CUS_CERT_INFO b," +
			"CUS_INDIV c"+
			" where a.CUS_ID = b.CUS_ID(+) and a.CUS_ID = c.CUS_ID(+)"+
			"and ((a.CERT_TYPE = '"+certType+"' and a.CERT_CODE = '"+certCode+"')"+
			"or (b.CERT_TYP = '"+certType+"' and b.CERT_CODE = '"+certCode+"'))";
			
//			" from CUS_BASE a left join CUS_CERT_INFO b on a.CUS_ID=b.CUS_ID and" +
//			" ((a.CERT_TYPE='"+certType+"' and a.CERT_CODE='"+certCode+"') OR (a.CUS_ID IN(SELECT CUS_ID FROM CUS_CERT_INFO WHERE CERT_TYP='"+certType+"' and CERT_CODE='"+certCode+"')))";
//			" left join CUS_COM c on a.CUS_ID=c.CUS_ID";
			
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
			cusBase_kColl.addDataField("cus_id", rs.getString("Cus_ID"));
			cusBase_kColl.addDataField("cus_name", rs.getString("Cus_NAME"));
			cusBase_kColl.addDataField("cus_short_name", rs.getString("CUS_SHORT_NAME"));
			cusBase_kColl.addDataField("cus_type", rs.getString("CUS_TYPE"));
			cusBase_kColl.addDataField("cert_type", rs.getString("CERT_TYPE"));
			cusBase_kColl.addDataField("cert_code", rs.getString("CERT_CODE"));
			cusBase_kColl.addDataField("open_date", rs.getString("OPEN_DATE"));
			cusBase_kColl.addDataField("cus_country", rs.getString("CUS_COUNTRY"));
			cusBase_kColl.addDataField("loan_card_flg", rs.getString("LOAN_CARD_FLG"));
			cusBase_kColl.addDataField("loan_card_id", rs.getString("LOAN_CARD_ID"));
			cusBase_kColl.addDataField("loan_card_pwd", rs.getString("LOAN_CARD_PWD"));
			cusBase_kColl.addDataField("loan_card_eff_flg", rs.getString("LOAN_CARD_EFF_FLG"));
			cusBase_kColl.addDataField("loan_card_audit_dt", rs.getString("LOAN_CARD_AUDIT_DT"));
			cusBase_kColl.addDataField("cus_crd_grade", rs.getString("CUS_CRD_GRADE"));
			cusBase_kColl.addDataField("cert_typ_other", rs.getString("CERT_TYP_OTHER"));
			cusBase_kColl.addDataField("cert_code_other", rs.getString("CERT_CODE_OTHER"));
			cusBase_kColl.addDataField("crt_date_other", rs.getString("CRT_DATE_OTHER"));
			cusBase_kColl.addDataField("crt_end_date_other", rs.getString("CRT_END_DATE_OTHER"));
			cusBase_kColl.addDataField("indiv_id_exp_dt", rs.getString("INDIV_ID_EXP_DT"));
			cusBase_kColl.addDataField("indiv_id_start_dt",rs.getString("INDIV_ID_START_DT"));
			
			
			
//			" a.CUS_ID=(select cus_id from CUS_BASE " +
//			" where a.CERT_TYPE='"+certType+"' and a.CERT_CODE='"+certCode+"')";
				
			}
		}  catch (SQLException e) {
			throw e;
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
				throw e;
			}
		}
		return cusBase_kColl;
	}
		
	/**
	 * 根据客户号获取一条客户的基本信息
	 * @param cusId
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public CusBase findCusBaseById(String cusId,  Connection conn) throws SQLException{
		Statement stmt=null;
		ResultSet rs=null;
		CusBase cusBase = null;
		try {
			stmt = conn.createStatement();
			String sql = "select CUS_ID,CUS_NAME,CUS_SHORT_NAME,CUS_TYPE ,CERT_TYPE,CERT_CODE," +
					"OPEN_DATE,CUS_COUNTRY,LOAN_CARD_FLG,LOAN_CARD_ID,LOAN_CARD_PWD,LOAN_CARD_EFF_FLG," +
					"LOAN_CARD_AUDIT_DT,CUS_CRD_GRADE,CUS_CRD_DT,CUS_STATUS,MAIN_BR_ID,CUST_MGR,INPUT_ID,INPUT_BR_ID," +
					"INPUT_DATE,BELG_LINE,GUAR_CRD_GRADE from CUS_BASE " +
					"where CUS_ID='"+cusId+"'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cusBase = new CusBase();
				cusBase.setCusId(rs.getString("CUS_ID"));
				cusBase.setCusName(rs.getString("CUS_NAME"));
				cusBase.setCusShortName(rs.getString("CUS_SHORT_NAME"));
				cusBase.setCusType(rs.getString("CUS_TYPE"));
				cusBase.setCertType(rs.getString("CERT_TYPE"));
				cusBase.setCertCode(rs.getString("CERT_CODE"));
				cusBase.setOpenDate(rs.getString("OPEN_DATE"));
				cusBase.setCusCountry(rs.getString("CUS_COUNTRY"));
				cusBase.setLoanCardFlg(rs.getString("LOAN_CARD_FLG"));
				cusBase.setLoanCardId(rs.getString("LOAN_CARD_ID"));
				cusBase.setLoanCardPwd(rs.getString("LOAN_CARD_PWD"));
				cusBase.setLoanCardEffFlg(rs.getString("LOAN_CARD_EFF_FLG"));
				cusBase.setLoanCardAuditDt(rs.getString("LOAN_CARD_AUDIT_DT"));
				cusBase.setCusCrdGrade(rs.getString("CUS_CRD_GRADE"));
				cusBase.setCusCrdDt(rs.getString("CUS_CRD_DT"));
				cusBase.setCusStatus(rs.getString("CUS_STATUS"));
				cusBase.setMainBrId(rs.getString("MAIN_BR_ID"));
				cusBase.setCustMgr(rs.getString("CUST_MGR"));
				cusBase.setInputId(rs.getString("INPUT_ID"));
				cusBase.setInputBrId(rs.getString("INPUT_BR_ID"));
				cusBase.setInputDate(rs.getString("INPUT_DATE"));
				cusBase.setBelgLine(rs.getString("BELG_LINE"));
				cusBase.setGuarCrdGrade(rs.getString("GUAR_CRD_GRADE"));
			}
		}  catch (SQLException e) {
			throw e;
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
				throw e;
			}
		}
		return cusBase;
	}
	
	/**
	 * 修改客户的主管客户经理      用于托管和移交修改主管客户经理的时候使用
	 * @param cusId 客户编号
	 * @param conn 链接
	 * @return intReturnMessage   执行是否成功信息
	 * @throws SQLException 
	 */
	public String  updateCusByCusId(String cusId,String receiverBrId,String cusType,String custMgr, String mainCusMgr, Connection conn) throws SQLException{
			
		  String intReturnMessage=CMISMessage.ADDDEFEAT;
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement("update cus_base set cust_mgr=? ,main_br_id=? where cus_id=? and cust_mgr=?");
				pstmt.setString(1, custMgr);
				pstmt.setString(2, receiverBrId);
				pstmt.setString(3, cusId);
				pstmt.setString(4, mainCusMgr);
				pstmt.executeUpdate();
				intReturnMessage=CMISMessage.ADDSUCCEESS;
			} catch (SQLException e) {
				throw e;
			} finally {
				try {
					if(pstmt != null) {
						pstmt.close();
						pstmt = null;
					}
					if(conn != null) {
						//conn.close();
						//conn = null;
					}
				} catch (SQLException e) {
					throw e;
				}
			}
			return intReturnMessage;
		}

	public boolean delCusTrusteeRecord(String consignorId, String trusteeId,
			Connection connection) throws SQLException {
		Map<String,String> map = new HashMap<String,String>();
		map.put("consignor_id", consignorId);
		map.put("trustee_id", trusteeId);
		return (SqlClient.delete("delCusTrustee", map, connection) == 1);
	}
	
	public void delSubmitRecord(String tableName, String serno) throws SQLException {
		Statement st = null;
		st = this.getConnection().createStatement();
		String sql = "";
		if(tableName.equals("CUS_ORG_APP")){ //评估机构认定流程后处理
			SqlClient.executeUpd("delCusOrgAppMng", serno, null, null, this.getConnection());
			SqlClient.executeUpd("delCusOrgInfoMng", serno, null, null, this.getConnection());
			SqlClient.executeUpd("submitCusOrgApp", serno, null, null, this.getConnection());
			SqlClient.executeUpd("submitCusOrgInfo", serno, null, null, this.getConnection());
		}else if(tableName.equals("CUS_GRP_INFO_APPLY")){ //集团认定流程后处理
			sql = "delete from CUS_GRP_MEMBER where grp_no = (select grp_no from CUS_GRP_INFO_APPLY where serno = '"+serno+"') ";
			st.executeUpdate(sql);
			sql = "delete from CUS_GRP_INFO where grp_no = (select grp_no from CUS_GRP_INFO_APPLY where serno = '"+serno+"') ";
			st.executeUpdate(sql);
			SqlClient.executeUpd("submitCusGrpMember", serno, null, null, this.getConnection());
			SqlClient.executeUpd("submitCusGrpInfo", serno, null, null, this.getConnection());
		}else if(tableName.equals("grpCognizChg")){ //集团认定变更处理
			String[] value =serno.split(","); //格式serno,grp_no
			SqlClient.executeUpd("changeCusGrpMember", value[1],  value[0], null, this.getConnection());
		}else if(tableName.equals("addGrpCognizMember")){ //集团认定将母公司加入成员表
			SqlClient.executeUpd("addGrpCognizMember", serno,  null, null, this.getConnection());
		}
	}
	
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model 表模型
	 * @param conditionFields  过滤条件键值对
	 * @return 执行删除记录条数
	 * @throws DaoException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields) throws DaoException {
	    PreparedStatement state = null;
	    TableModelLoader modelLoader = (TableModelLoader)this.getContext().getService(CMISConstance.ATTR_TABLEMODELLOADER);
        TableModel refModel = modelLoader.getTableModel(model);
      
	    int j;
	    try{
	        List<TableModelField> deleteFieldList = new ArrayList<TableModelField>();
	        String strSQL = null;
	     
	        StringBuffer strSQLBuf = new StringBuffer((new StringBuilder("DELETE FROM ")).append(refModel.getDbTableName()).append(" WHERE ").toString());
	        for(Iterator iterator = conditionFields.keySet().iterator(); iterator.hasNext();){
	            String fieldId = (String)iterator.next();
	            TableModelField modelField = refModel.getModelField(fieldId);
	            if(modelField != null){
	                deleteFieldList.add(modelField);
	                if(iterator.hasNext())
	                    strSQLBuf.append((new StringBuilder(String.valueOf(modelField.getColumnName()))).append(" = ? AND ").toString());
	                else
	                    strSQLBuf.append((new StringBuilder(String.valueOf(modelField.getColumnName()))).append(" = ? ").toString());
	            }
	        }
	
	        if(deleteFieldList.isEmpty())
	            throw new DaoException((new StringBuilder("删除TableModel[")).append(refModel.getId()).append("]未获取到字段信息").toString());
	        strSQL = strSQLBuf.toString();
	        Connection con = this.getConnection();  //得到数据库连接
	        state = con.prepareStatement(strSQL);
	        for(int i = 0; i < deleteFieldList.size(); i++){
	            TableModelField field = (TableModelField)deleteFieldList.get(i);
	            Object fieldValue = conditionFields.get(field.getId());
	            if(fieldValue != null){
	                EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder("Set condition's field [")).append(field.getColumnName()).append("]'s value = ").append(fieldValue).toString());
	                state.setObject(i + 1, fieldValue);
	            } else{
	                EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder("Set condition's field [")).append(field.getColumnName()).append("]'s value with null").toString());
	                state.setNull(i + 1, field.getColumnType());
	            }
	        }
	
	        int result = state.executeUpdate();
	        EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder(String.valueOf(result))).append(" records in tableModel [").append(refModel.getId()).append("] has been DELETE.").toString());
	        state.close();
	        j = result;
	    }
	    catch(SQLException se){
	        EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, (new StringBuilder("Failed to DELETE record in tableModel [")).append(refModel.getId()).append("] due to SQLException !").toString(), se);
	        throw new DaoException((new StringBuilder("Failed to DELETE record in tableModel [")).append(refModel.getId()).append("] due to SQLException !").toString());
	    }
	    if(state != null){
	        try{
	            state.close();
	        }
	        catch(SQLException sqlexception) { }
	    }
	    return j;
	}
	
}

