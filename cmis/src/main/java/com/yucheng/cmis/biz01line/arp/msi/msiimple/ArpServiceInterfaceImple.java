package com.yucheng.cmis.biz01line.arp.msi.msiimple;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.arp.msi.ArpServiceInterface;
import com.yucheng.cmis.pub.CMISModualService;

public class ArpServiceInterfaceImple extends CMISModualService implements
		ArpServiceInterface {
	private static final Logger logger = Logger.getLogger(ArpServiceInterfaceImple.class);
	/**
	 * 根据查询条件返回资产保全表信息iColl
	 * @param kColl (默认参数1.type查询类型 2.condition条件 3.tableName表名，其他按需扩展)
	 * @param context
	 * @param connection
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getArpIcollByCondition(KeyedCollection Kcoll,Context context,
			 Connection connection) throws Exception {
		logger.info("调用:根据查询条件返回资产保全表信息iColl 接口");		
		IndexedCollection iColl = new IndexedCollection();
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		
		Object type = Kcoll.getDataValue("type");
		if(type.equals("common")){ //type=common,普通查询,即只根据condition+tableName查出结果
			String tableName = Kcoll.getDataValue("tableName").toString();
			String condition = Kcoll.getDataValue("condition").toString();
			iColl = dao.queryList(tableName, condition, connection);
		}else if(type.equals("pageInfo")){ //带pageInfo的查询
			String tableName = Kcoll.getDataValue("tableName").toString();
			String condition = Kcoll.getDataValue("condition").toString();
			PageInfo pageInfo = (PageInfo) Kcoll.getDataValue("pageInfo");
			iColl = dao.queryList(tableName, null, condition, pageInfo, connection);
		}else if(type.equals("")){ //其他特殊查询，传入自定type标识，并自己处理
			
		}
		
		return iColl;
	}

	/**
	 * 根据查询条件返回资产保全表信息kColl
	 * @param kColl (默认参数1.type 2.condition 3.tableName，其他按需扩展)
	 * @param context
	 * @param connection
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getArpKcollByCondition(KeyedCollection Kcoll,Context context,
			 Connection connection) throws Exception {
		logger.info("调用:根据查询条件返回资产保全表信息kColl 接口");		
		KeyedCollection result_kColl = new KeyedCollection();
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		
		Object type = Kcoll.getDataValue("type");
		if(type.equals("common")){ //type=common,普通查询,即只根据condition+tableName查出结果
			String tableName = Kcoll.getDataValue("tableName").toString();
			String condition = Kcoll.getDataValue("condition").toString(); //这里的条件必须是主键
			result_kColl = dao.queryDetail(tableName, condition, connection);
		}else if(type.equals("")){ //其他特殊查询，传入自定type标识，并自己处理
			
		}
		
		return result_kColl;
	}

}