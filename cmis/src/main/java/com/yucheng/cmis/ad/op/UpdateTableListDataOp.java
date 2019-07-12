package com.yucheng.cmis.ad.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

/**
 * 对列表的增删改操作
 * 
 * @author JackYu
 *
 */
public class UpdateTableListDataOp extends CMISOperation {

	public final String OPT_TYPE="optType";
	/**
	 * 新增记录数
	 */
	public int addCount = -1;
	/**
	 * 删除记录数
	 */
	public int deleteCount = -1;
	/**
	 * 更新记录数
	 */
	public int updateCount = -1;
	
	public String doExecute(Context context)throws EMPException {
		Connection connection = null;
		
		//重置记录数
		addCount = 0;
		deleteCount = 0;
		updateCount = 0;
		
		try{ 
			connection = this.getConnection(context);

			IndexedCollection iColl = null ;
			KeyedCollection kColl = null ;
			String modelIdName = null ;

			try{
				//取从JSP页面中modelId的参数值：表模型ID
				modelIdName = (String)context.getDataValue("modelId") ;
			}catch (Exception e) {} 
			if(modelIdName == null)
				throw new EMPJDBCException("无效表模型!");

			try {
				//可以取到LIST页面中的所有记录数
				iColl = (IndexedCollection)context.getDataElement(getDataElementName(modelIdName));
			} catch (Exception e) {}
			if(iColl == null || iColl.size() == 0)
				throw new EMPJDBCException(modelIdName+"'s data is empty!");

			TableModelDAO dao = this.getTableModelDAO(context);

			
			//遍历所有记录，根据optType标识来判断是新增、删除、更新或是不操作
			for(int i=0; i<iColl.size(); i++){
				kColl = (KeyedCollection)iColl.get(i) ;
				kColl.setName(modelIdName) ;
				String optType = (String)kColl.getDataValue(OPT_TYPE) ;
				
				//无操作
				if(optType.equals(PUBConstant.NONE)){
					continue ;
				}
				//新增操作
				else if(optType.equals(PUBConstant.ADD)){
					addDataRecord(context, modelIdName,dao, kColl, connection);
					addCount ++;
				}
				//删除操作
				else if(optType.equals(PUBConstant.DELETE)){
					deleteDataRecord(context, modelIdName, dao, kColl, connection);
					
					//这里不能将kColl移出iColl，否则计算iColl的size不正确==》这就要求，在做删除操作时需要刷新JSP页面
					//iColl.remove(kColl); 
					
					deleteCount ++;
				}
				//更新操作
				else{
					dao.update(kColl, connection);
					
					updateCount ++;
				}
			}
		}catch (EMPException ee) {
			addCount = 0;
			deleteCount = 0;
			updateCount = 0;
			
			throw ee;
		}catch (Exception e) {
			addCount = 0;
			deleteCount = 0;
			updateCount = 0;
			
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}

		return "0";
	}
	
	/**
	 * JSP页面数据结构名称:默认：表模型名+"List"
	 * @param modelIdName
	 * @return
	 */
	public String getDataElementName(String modelIdName){
		
		return modelIdName+"List";
	} 
	
	/**
	 * 新增操作
	 * 
	 * 子类可以重写该方法以满足具体业务逻辑
	 * @param context
	 * @param modelIdName
	 * @param dao
	 * @param kColl
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public boolean addDataRecord(Context context,String modelIdName,TableModelDAO dao, KeyedCollection kColl,Connection connection)throws Exception{
		try {
			dao.insert(kColl, connection);
			
			//新增后的记录，将其optType的标识去掉，以防重复新增
			kColl.setDataValue("optType", "");
		} catch (Exception e) {
			EMPLog.log("List update:", EMPLog.ERROR, 0, "add list data error-"+e.getMessage());
			throw e;
		}
		
		return true;
	}
	
	/**
	 * 删除操作
	 * 
	 * 子类可以重写该方法以满足具体业务逻辑
	 * @param context
	 * @param modelIdName
	 * @param dao
	 * @param kColl
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public boolean deleteDataRecord(Context context,String modelIdName,TableModelDAO dao, KeyedCollection kColl,Connection connection)throws Exception{
		try {
			Map pkMap = new HashMap();

			String[] pkColumns = this.getPks(context, modelIdName) ;

			for(int j=0; j<pkColumns.length; j++){
				String pkColumnName = (String)pkColumns[j] ;
				pkMap.put(pkColumnName, (String)kColl.getDataValue(pkColumnName)) ;
			}
			dao.deleteAllByPks(modelIdName, pkMap, connection);
		} catch (Exception e) {
			EMPLog.log("List update:", EMPLog.ERROR, 0, "delete list data error-"+e.getMessage());
			throw e;
		}
		
		return true;
	}
	
	
	/**
	 * 更新操作
	 * 
	 * 子类可以重写该方法以满足具体业务逻辑
	 * @param context
	 * @param modelIdName
	 * @param dao
	 * @param kColl
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public boolean updateDataRecord(Context context,String modelIdName,TableModelDAO dao, KeyedCollection kColl,Connection connection)throws Exception{
		try {
			dao.update(kColl, connection);
		} catch (Exception e) {
			EMPLog.log("List update:", EMPLog.ERROR, 0, "update list data error-"+e.getMessage());
			throw e;
		}
		
		return true;
	}
	
	/**
	 * 根据表模型，获取主键数组
	 * @param context
	 * @param modelId
	 * @return 主键数组
	 */
	public String[] getPks(Context context, String modelId){
		List listPk = new ArrayList() ;
		int i = 0 ;

		TableModelLoader model = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
		Iterator it = model.getTableModel(modelId).getModelFields().values().iterator();
		while (it.hasNext()) {
			TableModelField field = (TableModelField)it.next();
			if (field.isPK()) {
				listPk.add(field.getColumnName().toLowerCase()) ;
			}
		}
		return (String[])listPk.toArray(new String[0]) ;
	}

	/**
	 * @return 新增记录数
	 */
	public int getAddCount() {
		return addCount;
	}

	/**
	 * @param addCount 新增记录数
	 */
	public void setAddCount(int addCount) {
		this.addCount = addCount;
	}

	/**
	 * @return 删除记录数
	 */
	public int getDeleteCount() {
		return deleteCount;
	}

	/**
	 * @param deleteCount 删除记录数
	 */
	public void setDeleteCount(int deleteCount) {
		this.deleteCount = deleteCount;
	}

	/**
	 * @return 更新记录数
	 */
	public int getUpdateCount() {
		return updateCount;
	}

	/**
	 * @param updateCount 更新记录数
	 */
	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}
	
}
