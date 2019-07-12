package com.yucheng.cmis.biz01line.prd.msi.msiimple;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 产品配置模块对外提供服务接口实现类
 * @author Pansq
 * @version V1.0
 */
public class PrdServiceInterfaceImple extends CMISModualService implements PrdServiceInterface {
	/**
	 * 通过产品编号获得产品配置信息
	 * @param connection
	 * @return List<PrdBasicinfo>
	 * @throws Exception
	 */
	public PrdBasicinfo getPrdBasicinfoList(String prdid, Connection connection) throws Exception {
		PrdBasicinfo prdBasicinfo = new PrdBasicinfo();
		try {
			KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("getPrdBasicinfoByPrdId4PrdBasicinfo", prdid, null, connection);
			ComponentHelper componetHelper = new ComponentHelper();
			componetHelper.kcolTOdomain(prdBasicinfo, kColl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prdBasicinfo;
	}
	/**
	 * 通过产品编号、表单KeyedCollection将KeyedCollection中数据通过映射关系插入目标表中
	 * @param prdId 产品编号
	 * @param mapType 映射种类【cont合同映射\pvp出账映射】
	 * @param kModel 原表模型封装成的KeyedCollection
	 * @param toTable 目标表
	 * @param connection 数据库连接
	 * @return Map<String,String>
	 * @throws Exception
	 */
	public KeyedCollection insertMsgByKModelFromPrdMap(String prdId, String mapType,
			KeyedCollection kModel, KeyedCollection toDefKColl, String toTable, Context context, Connection connection)
			throws Exception {
		KeyedCollection resultKColl = null;
		PrdPolcySchemeComponent pps = (PrdPolcySchemeComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
		resultKColl = pps.insertMsgByKModelFromPrdMap(prdId,mapType,kModel,toDefKColl,toTable,context,connection);
		return resultKColl;
	}
	
	/**
	 * 通过产品编号获得产品的适用机构列表信息
	 * @param prdid 产品编号
	 * @param connection 数据库连接
	 * @return IndexedCollection 
	 * @throws Exception
	 */
	public IndexedCollection getPrdApplyOrgByPrdId(String prdid,Connection connection) throws Exception {
		IndexedCollection returnIColl = null;
		returnIColl = SqlClient.queryList4IColl("getPrdApplyOrgByPrdId", prdid, connection);
		return returnIColl;
	}
	
	/**
	 * 通过业务品种、币种、期限类型获取利率信息
	 * @param prdId 业务品种
	 * @param currType 币种
	 * @param termM 期限
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getPrdRate(String prdId, String currType,int termM,Context context, Connection connection) throws Exception {
		KeyedCollection returnKColl = null;
		PrdPolcySchemeComponent pps = (PrdPolcySchemeComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
		returnKColl = pps.getRate(prdId, currType, termM, connection);
		/** 有限获取有产品的利率信息，不存在则项下取共用的配置，即产品编号默认为9999 */
		if(returnKColl == null || returnKColl.size() == 0){
			returnKColl = pps.getRate(PRDConstant.RATEALL, currType, termM, connection);
		}
		return returnKColl;
	}
	/**
	 * 通过币种、利率种类查询LIBOR牌告基准利率 
	 * @param currType 币种
	 * @param irType 利率种类
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getLiborRate(String currType, String irType,Context context, Connection connection) throws Exception {
		KeyedCollection returnKColl = null;
		PrdPolcySchemeComponent pps = (PrdPolcySchemeComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
		String openDay = (String)context.getDataValue("OPENDAY");
		openDay = DateUtils.getEndDate("003", openDay, -1);
		returnKColl = pps.getLiborRate(currType,openDay, irType, connection);
		return returnKColl;
	} 

}
