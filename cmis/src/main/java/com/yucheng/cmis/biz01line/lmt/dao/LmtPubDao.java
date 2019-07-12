package com.yucheng.cmis.biz01line.lmt.dao;

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

public class LmtPubDao extends CMISDao {
	/**
	 * 根据流水号更新授信申请基表的授信总额、循环额度、一次性额度
	 * @param serno 流水号
	 */
	public int updateLmtApplyAmt(String serno)throws DaoException {
		int count=0;
		try {
			KeyedCollection kColl4Query = new KeyedCollection();
			kColl4Query.addDataField("SERNO", serno);
			count = SqlClient.executeUpd("updateLmtApplyAmt", kColl4Query, null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据流水号更新授信金额出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据流水号更新授信申请基表的授信总额、循环额度、一次性额度
	 * @param serno 流水号
	 * @param lmt_type 授信类别（区分条线）
	 * @param lrisk_type 是否非低风险
	 */
	public int updateLmtApplyAmt(String serno,String lrisk_type)throws DaoException {
		int count=0;
		try {
			KeyedCollection kColl4Query = new KeyedCollection();
			kColl4Query.addDataField("SERNO", serno);
			//kColl4Query.addDataField("LMT_TYPE", lmt_type);
			kColl4Query.addDataField("LRISK_TYPE", lrisk_type);
			count = SqlClient.executeUpd("updateLmtApplyAmt2", kColl4Query, null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据流水号更新授信金额出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	
	/**
	 * 根据流水号更新个人授信申请基表的授信总额
	 * @param serno 流水号
	 */
	public int updateLmtAppIndivAmt(String serno)throws DaoException {
		int count=0;
		try {
			count = SqlClient.executeUpd("updateLmtAppIndivAmt", serno, null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据流水号更新授信金额出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据流水号更新集团授信申请基表的授信总额
	 * @param serno 流水号
	 */
	public int updateLmtGrpAppAmt(String serno)throws DaoException {
		int count=0;
		try {
			count = SqlClient.executeUpd("updateLmtGrpAppAmt", serno, null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据流水号更新授信金额出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据授信申请流水号，删除授信分项中对应的数据
	 * @param serno 流水号
	 */
	public int deleteLmtApplyDetailsBySerno(String serno)throws DaoException {
		int count=0;
		try {
			//删除分项与担保合同关系表数据
			count = SqlClient.executeUpd("deleteRLmtAppGuarCont", serno, null, null, this.getConnection());
			//删除申请对应的分项数据
			count = SqlClient.executeUpd("deleteLmtApplyDetails", serno, null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据流水号删除授信明细出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据集团授信申请流水号，删除成员授信申请中对应的数据
	 * @param serno 流水号
	 */
	public int deleteLmtGrpMemberAppBySerno(String serno)throws DaoException {
		int count=0;
		try {
			count = SqlClient.executeUpd("deleteLmtGrpMemberApp", serno, null, null, this.getConnection());
		} catch (Exception e) {
			throw new DaoException("根据流水号删除成员授信明细出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 根据授信流水号/协议编号查询循环额度、一次性额度
	 * @param serno 授信申请流水号
	 * @param tablename 查询表名
	 */
	public KeyedCollection selectLmtAppIndivAmt(String serno,String tablename)throws DaoException {
		KeyedCollection kColl = null;
		try {
			if("LMT_APP_DETAILS".equalsIgnoreCase(tablename)){
				kColl = (KeyedCollection)SqlClient.queryFirst("selectLmtAppIndivAmtByApp", serno, null, this.getConnection());
			}else{
				kColl = (KeyedCollection)SqlClient.queryFirst("selectLmtAppIndivAmtByAgr", serno, null, this.getConnection());
			}
		}catch (Exception e) {
			throw new DaoException("根据授信流水号/协议编号查询循环额度、一次性额度出错，错误原因："+e.getMessage());
		}
		return kColl;
	}
	
	/**
	 * 根据授信协议编号查询循环额度、一次性额度
	 * @param agr_no 授信协议号
	 */
	public KeyedCollection selectLmtAgrAmtByAgr(String agr_no)throws DaoException {
		KeyedCollection kColl = null;
		try {
			kColl = (KeyedCollection)SqlClient.queryFirst("selectLmtAgrAmtByAgr", agr_no, null, this.getConnection());
		}catch (Exception e) {
			throw new DaoException("根据授信协议编号查询循环额度、一次性额度出错，错误原因："+e.getMessage());
		}
		return kColl;
	}
	
	
	/**
	 *根据授信协议编号查询循环额度、一次性额度
	 * @param serno 个人授信申请流水号
	 * @param lmt_type 授信类别（区分条线）
	 */
	public KeyedCollection selectLmtAgrDetailsAmt(String serno)throws DaoException {
		KeyedCollection kColl = null;
		KeyedCollection parameter   = new KeyedCollection();
		parameter.put("AGR_NO_1", serno);
		//parameter.put("LMT_TYPE_1", lmt_type);
		parameter.put("AGR_NO_2", serno);
		//parameter.put("LMT_TYPE_2", lmt_type);
		parameter.put("AGR_NO_3", serno);
		//parameter.put("LMT_TYPE_3", lmt_type);
		parameter.put("AGR_NO_4", serno);
		//parameter.put("LMT_TYPE_4", lmt_type);
		try {
			kColl = (KeyedCollection)SqlClient.queryFirst("selectLmtAgrDetailsAmtByAgr", parameter, null, this.getConnection());
		}catch (Exception e) {
			throw new DaoException("根据授信流水号/协议编号查询循环额度、一次性额度出错，错误原因："+e.getMessage());
		}
		return kColl;
	}
	
	
	
	/**
	 * 根据合作方流水号，删除拟按揭设备信息
	 * @param serno 合作方业务流水号
	 */
	public int deleteLmtSchedEquip(String serno)throws DaoException {
		int count = 0;
		try {
			count = SqlClient.executeUpd("deleteLmtSchedEquip", serno, null, null,this.getConnection());
		}catch (Exception e) {
			throw new DaoException("根据合作方机械设备项目编号，删除拟按揭设备信息出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 授信变更时将原有台账台账与担保合同关系复制给新的授信申请
	 * @param limit_code 额度编号
	 * @param org_limit_code 原额度编号（台账编号）
	 * @param serno 当前业务流水号
	 */
	public int createRLmtAppGuarContRecord(String limit_code,String org_limit_code,String serno)throws DaoException {
		int count = 0;
		try {
			KeyedCollection kColl4Query = new KeyedCollection();
			kColl4Query.addDataField("LIMIT_CODE", org_limit_code);  //如果是变更通过原有流水号与担保合同挂接
			kColl4Query.addDataField("AGR_NO", serno);  //申请时将流水号也挂关系
			count = SqlClient.executeUpd("createRLmtAppGuarContRecord", org_limit_code, kColl4Query, null,this.getConnection());
		}catch (Exception e) {
			throw new DaoException("授信变更时将原有台账与担保合同关系复制给新的授信申请出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 变更保存时将原有授信台账复制到申请分项历史表
	 * @param agr_no 授信协议编号
	 * @param serno 变更业务流水号
	 * @param lrisk_type 低风险业务类型
	 * @param lmt_type 授信类别（区分授信条线）
	 */
	public int createLmtAppDetailsHisRecord(String agr_no,String serno,String lrisk_type)throws DaoException {
		int count = 0;
		try {
			KeyedCollection kColl4Query = new KeyedCollection();
			kColl4Query.addDataField("LRISK_TYPE", lrisk_type);
			kColl4Query.addDataField("AGR_NO", agr_no);
			
			KeyedCollection valueKcoll = new KeyedCollection();
			valueKcoll.addDataField("SERNO", serno);
			count = SqlClient.executeUpd("createLmtAppDetailsHisRecord", kColl4Query, valueKcoll, null,this.getConnection());
		}catch (Exception e) {
			throw new DaoException("变更保存时将原有授信台账复制到申请分项历史表出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 授信变更保存时将原有授信台账复制到申请分项
	 * @param agr_no 授信协议编号
	 * @param serno 变更业务流水号
	 */
	public int createLmtAppDetailsRecord(String agr_no,String serno)throws DaoException {
		int count = 0;
		try {
			count = SqlClient.executeUpd("createLmtAppDetailsRecord", agr_no, serno, null,this.getConnection());
		}catch (Exception e) {
			throw new DaoException("变更保存时将原有授信台账复制到申请分项表出错，错误原因："+e.getMessage());
		}
		return count;
	}
	
	/**
	 * 变更保存时将原有授信协议复制到申请历史表
	 * @param grp_agr_no 集团授信协议编号
	 * @param serno 变更业务流水号
	 */
	public int createLmtApplyRecord(String grp_agr_no,String serno)throws DaoException {
		int count = 0;
		try {
			count = SqlClient.executeUpd("createLmtApplyRecord", grp_agr_no, serno, null,this.getConnection());
		}catch (Exception e) {
			throw new DaoException("变更保存时将原有授信台账复制到申请分项历史表出错，错误原因："+e.getMessage());
		}
		return count;
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
	
	/**
	 * 行业授信专用无返回值的命名sql调用
	 * @param submitType 类型
	 * @param serno  流水号
 	 * @param agr_no  协议编号
	 * @return 
	 * @throws DaoException 
	 */
	public void doVirtualSubmit(String submitType, String serno , String agr_no) throws DaoException {
		try {
			if(submitType.equals("indus_crd_apply")||submitType.equals("indus_crd_change")){/* 行业授信申请、变更流程后处理 */
				if(submitType.equals("indus_crd_change")){/* 变更时先删除管理表中的旧记录 */
					SqlClient.executeUpd("deleteIndusAgr", agr_no, null, null, this.getConnection());
					SqlClient.executeUpd("deleteIndusListMana", agr_no, null, null, this.getConnection());
					SqlClient.executeUpd("deleteIndusAuthMana", agr_no, null, null, this.getConnection());
					//SqlClient.executeUpd("deleteAgrDetails", agr_no, null, null, this.getConnection());
				}
				SqlClient.executeUpd("dealIndusApp", serno, null, null, this.getConnection());//更新行业授信申请表中：结束日期
				SqlClient.executeUpd("dealListApp", serno, null, null, this.getConnection());//更新行业名单申请表：状态"待准入"-> "有效"
				SqlClient.executeUpd("dealAuthApp", serno, null, null, this.getConnection());//更新行业授权申请表：状态"登记"-> "有效"
				SqlClient.executeUpd("indusAppToAgr", serno, agr_no, null, this.getConnection());//申请过渡协议
				SqlClient.executeUpd("indusListToMana", serno, agr_no, null, this.getConnection());//名单过渡管理
				SqlClient.executeUpd("indusAuthToMana", serno, agr_no, null, this.getConnection());//授权过渡管理
				//SqlClient.executeUpd("detailsToMana", serno, agr_no, null, this.getConnection());//分项过渡台账
			}else if (submitType.equals("indus_apply_change")){	/* 行业授信变更处理 */
				SqlClient.executeUpd("indusListToApp", serno, agr_no, null, this.getConnection());//管理表过渡数据到名单申请表
				SqlClient.executeUpd("indusAuthToApp", serno, agr_no, null, this.getConnection());//管理表过渡数据到授权申请表
			}
		} catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}			
	}
	
	/**
	 * 行业授信专用带返回值的命名sql调用
	 * @param submitType 类型
	 * @param serno  流水号
	 * @return results结果
	 * @throws DaoException 
	 */
	public String getAgrno(String submitType, String serno) throws DaoException {
		Object results = "";
		try{
			if (submitType.equals("indusType")){/***** 行业分类校验 ******/
				results = (Object) SqlClient.queryFirst("checkIndusType", serno, null, this.getConnection());
			}else if (submitType.equals("indusList")){/***** 行业名单校验 ******/
				results = (Object) SqlClient.queryFirst("checkIndusList", serno, null, this.getConnection());
			}else if (submitType.equals("indusAgr")){/***** 行业协议校验 ******/
				results = (Object) SqlClient.queryFirst("checkIndusAgr", serno, null, this.getConnection());
			}else if (submitType.equals("listMana")){/***** 名单制管理校验 ******/
				results = (Object) SqlClient.queryFirst("checkListMana", serno, null, this.getConnection());
			}else if (submitType.equals("getAgrno")){/***** 根据流水号找借据编号 ******/
				results = (Object) SqlClient.queryFirst("getAgrnoBySerno", serno, null, this.getConnection());
			}else if (submitType.equals("LmtQuotaManager")){/***** 限额产品代码校验 ******/
				String[] value = serno.split(",");
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("code_id", value[0]);
				paramMap.put("serno", value[1]);
				
				results = (Object) SqlClient.queryFirst("prdIdCheck", paramMap, null, this.getConnection());
				if(results == null){
					results = "";
				}
			}else if (submitType.equals("checkDeleteIndusList")){/***** 行业名单删除校验 ******/
				results = (Object) SqlClient.queryFirst(submitType, serno, null, this.getConnection());
			}
		}catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}
		return results.toString();
	}
	
}
