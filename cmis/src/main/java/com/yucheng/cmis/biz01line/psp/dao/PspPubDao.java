package com.yucheng.cmis.biz01line.psp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

public class PspPubDao extends CMISDao {
	/**
	 * 贷后管理专用无返回值的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 * @return 
	 * @throws DaoException 
	 */
	public void delExecuteSql(String submitType, KeyedCollection kcoll) throws DaoException {
		try {
			
		} catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}
	}
	
	/**
	 * 贷后管理专用带返回值的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 * @throws DaoException 
	 */
	public KeyedCollection delReturnSql(String submitType, KeyedCollection kcoll) throws DaoException {
		Object results = "";
		try{
			/********************************** 贷后检查任务设置 *****************************************/
			if (submitType.equals("queryCusIdByComCllType")){ /***** 按照行业类型过滤出其有有效业务的客户 ******/
				results = (Object) SqlClient.queryList4IColl("queryCusIdByComCllType", kcoll.getDataValue("com_cll_type"), this.getConnection());
				kcoll.addDataField("results", results);
			}else if (submitType.equals("queryCusIdByCustMgr")){ /***** 根据主管客户经理获得此主管客户经理辖内存在有效业务的所有客户 ******/
				results = (Object) SqlClient.queryList4IColl("queryCusIdByCustMgr", kcoll.getDataValue("cust_mgr"), this.getConnection());
				kcoll.addDataField("results", results);
			}else if (submitType.equals("queryCusIdByMainBrId")){ /***** 根据客户主管机构获得此机构下存在有效业务的所有客户 ******/
				results = (Object) SqlClient.queryList4IColl("queryCusIdByMainBrId", kcoll.getDataValue("main_br_id"), this.getConnection());
				kcoll.addDataField("results", results);
			}else if (submitType.equals("queryCusIdByLoanAmt")){ /***** 根据金额区间过滤存在有效业务的所有客户 ******/
				results = (Object) SqlClient.queryList4IColl("queryCusIdByLoanAmt", kcoll, this.getConnection());
				kcoll.addDataField("results", results);
			}else if (submitType.equals("checkTrueByCusId")){ /***** 根据客户码判断此客户是否存在有效业务信息 ******/
				KeyedCollection result = (KeyedCollection) SqlClient.queryFirst("checkTrueByCusId", kcoll.getDataValue("cus_id"),null, this.getConnection());
				BigDecimal counts = (BigDecimal) result.getDataValue("counts");
				if(counts.compareTo(new BigDecimal("0"))>0){
					results=true;
				}else{
					results=false;
				}
				kcoll.addDataField("results", results);
				
			}
		}catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}
		return kcoll;
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
	        for(Iterator<String> iterator = conditionFields.keySet().iterator(); iterator.hasNext();){
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
	            throw new DaoException((new StringBuilder("Delete TableModel[")).append(refModel.getId()).append("]未获取到字段信息").toString());
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
