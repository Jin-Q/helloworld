package com.yucheng.cmis.biz01line.ccr.msi.msiimple;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.msi.CcrServiceInterface;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 评级模块对外提供服务接口实现类
 * @author tangzf
 * @version V1.0
 */
public class CcrServiceInterfaceImple extends CMISModualService implements CcrServiceInterface {
	private static final Logger logger = Logger.getLogger(CcrServiceInterfaceImple.class);
	/**
	 * 根据授信申请流水号删除对应个人评级申请信息
	 * @param lmt_serno	个人授信申请流水号
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	public String deleteCcrAppByLmtSerno(String lmt_serno, Context context, Connection connection) throws Exception {
		try {
			//根据授信流水号查询个人评级申请流水号
			TableModelDAO dao = (TableModelDAO) context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String condition = " where lmt_serno='"+lmt_serno+"'";
			List<String> list = new ArrayList<String>();
			list.add("serno");
			IndexedCollection iColl = dao.queryList("CcrAppInfo", list, condition, connection);
			
			CcrComponent ccrComp = (CcrComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("CcrComponent", context,connection);
			//删除对应评级申请
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String serno = (String)kColl.getDataValue("serno");
				ccrComp.deleteApp(serno);
			}
			
		} catch (Exception e) {
			throw e;
		}
		return "1";
	}
	/**
	 * 根据授信申请流水号更新对应个人评级申请信息
	 * @param lmt_serno	个人授信申请流水号
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	public int updateCcrAppInfo(String lmt_serno,String status, Context context,Connection connection) throws Exception {
		// TODO Auto-generated method stub
         logger.info("--------------- 根据授信申请流水号更新对应个人评级申请信息   开始---------------");
         int result = 0;
         try {
			if(null==lmt_serno || "".equals(lmt_serno)){
				logger.error(" 根据授信申请流水号更新对应个人评级申请信息，授信流水号[lmt_serno]传入值："+lmt_serno+"不能为空！");
				throw new EMPException(" 根据授信申请流水号更新对应个人评级申请信息，授信流水号[lmt_serno]传入值："+lmt_serno+"不能为空！");
			}
			if(null==status || "".equals(status)){
				logger.error(" 根据授信申请流水号更新对应个人评级申请信息，流程结束标识[status]传入值"+status+" 错误，正确取值：997--通过，998--否决！");
				throw new EMPException(" 根据授信申请流水号更新对应个人评级申请信息，流程结束标识[status]传入值"+status+" 错误，正确取值：997--通过，998--否决！");
			}
			//当前日期（办结日期，开始日期）
			String current = context.getDataValue("OPENDAY").toString();
			//取当前日期推后一年的时间（到期日期）
			String nextYear = Integer.toString((Integer.parseInt(current.substring(0,4))+1))+current.substring(4);
			Map<String,String> map = new HashedMap();
			map.put("appEndDate",current );
			map.put("startDate",current);
			map.put("expiringDate",nextYear );
			map.put("status",status );
			result = SqlClient.update("updateCcrAppInfoByLmtNo", lmt_serno, map, null, connection);
			IndexedCollection resultIc = SqlClient.queryList4IColl("getCusIdByLmtNo",lmt_serno, connection);
			if(resultIc.size()>0 && "997".equalsIgnoreCase(status)){   //评级记录数大于0时才调用客户接口  并且是审批通过时才调用客户模块更新
				//调用客户模块接口将评级结果以及评级到期日日期更新到客户表中。
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				service.updateIndivGrade(resultIc, connection);
			}
 		} catch (Exception e) {
 			throw e;
 		}
 		 logger.info("--------------- 根据授信申请流水号更新对应个人评级申请信息   结束---------------");
		return result;
	}
	/**
	 * 根据个人评级申请流水号更新对应个人评级申请信息
	 * @param lmt_serno	个人评级申请流水号
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	public int updateCcrAppInfoIndiv(String serno, Context context,Connection connection) throws Exception {
		// TODO Auto-generated method stub
         logger.info("--------------- 根据个人评级申请流水号更新对应个人评级申请信息   开始---------------");
         int result = 0;
         try {
			if(null==serno || "".equals(serno)){
				logger.error(" 根据个人评级申请流水号更新对应个人评级申请信息，个人评级申请流水号[serno]传入值："+serno+"不能为空！");
				throw new EMPException(" 根据个人评级申请流水号更新对应个人评级申请信息，个人评级申请流水号[serno]传入值："+serno+"不能为空！");
			}
			//当前日期（办结日期，开始日期）
			String current = context.getDataValue("OPENDAY").toString();
			//取当前日期推后一年的时间（到期日期）
			String nextYear = Integer.toString((Integer.parseInt(current.substring(0,4))+1))+current.substring(4);
			Map<String,String> map = new HashedMap();
			map.put("appEndDate",current );
			map.put("startDate",current);
			map.put("expiringDate",nextYear );
			result = SqlClient.update("updateCcrAppInfoByserNo", serno, map, null, connection);
			IndexedCollection resultIc = SqlClient.queryList4IColl("getCusIdByserNo",serno, connection);
			//调用客户模块接口将评级结果以及评级到期日日期更新到客户表中。
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			service.updateIndivGrade(resultIc, connection);
 		} catch (Exception e) {
 			throw e;
 		}
 		 logger.info("--------------- 根据授信申请流水号更新对应个人评级申请信息   结束---------------");
		return result;
	}
	/**
	 * 根据客户码串获得其评级信息
	 * @param cus_id_str 客户码串
	 * @return 
	 * @throws Exception
	 */
	public IndexedCollection getCcrAppInfoByCusIdStr(IndexedCollection CusIdic,PageInfo pageInfo,DataSource dataSource,String condition) throws Exception {
		// TODO Auto-generated method stub
		IndexedCollection res_value = null;
		 logger.info("--------------- 根据客户码串获得其评级信息   开始---------------");
		 KeyedCollection kc = new KeyedCollection();
		 String cus_id_str="";
		 for(int i=0;i<CusIdic.size();i++){
			 kc=(KeyedCollection) CusIdic.get(i);
			 if(cus_id_str.equals("")){
				 cus_id_str = "'"+kc.getDataValue("cus_id")+"'";
			 }else{
				 cus_id_str+=",'"+kc.getDataValue("cus_id")+"'";
			 }
		 }
         try {
			if(null==cus_id_str || "".equals(cus_id_str)){
				cus_id_str="''";
			//	logger.error(" 根据客户码串获得其评级信息，客户码串[cus_id_str]传入值："+cus_id_str+"不能为空！");
			//	throw new EMPException("根据客户码串获得其评级信息，客户码串[cus_id_str]传入值："+cus_id_str+"不能为空！");
			}
			
			String sql_select =SqlClient.joinQuerySql("getCcrAppInfoByCusIdStr",cus_id_str,null);
			sql_select = "select * from ("+sql_select+")"+condition;
			res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		
         } catch (Exception e) {
  			throw e;
  		}
         logger.info("--------------- 根据客户码串获得其评级信息   结束---------------");
		return res_value;
	}

}
