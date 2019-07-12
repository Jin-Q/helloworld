package com.yucheng.cmis.biz01line.cus.msi.msiimple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComManagerComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComManager;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 客户模块对外提供服务接口实现类
 * @author tangzf
 * @version V1.0
 */
public class CusServiceInterfaceImple extends CMISModualService implements CusServiceInterface {
	/**
	 * 更新客户信用评级、评级日期
	 * @param cusId	同业客户码
	 * @param cusGrade	信用等级
	 * @param cusGradeDt 评级日期
	 * @param guarFlag 是否融资性担保公司
	 * @param guar_bail_multiple 保证金放大倍数
	 * @param guar_cls 担保类别
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	public String updateGrade(String cusId, String cusGrade, String cusGradeDt, String guarFlag,String guar_bail_multiple,String guar_cls, Connection connection) throws Exception {
		try {
			Map<String, String> insertMap = new HashMap<String, String>();
			insertMap.put("grade", cusGrade);
			insertMap.put("gradeDt", cusGradeDt);
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "客户评级，流程审批通过逻辑处理【更新客户表中的评级信息 start】！", null);
			//1--对公客户，2--直接认定的对公客户，4--融资性担保公司，3--同业客户 ，5--个人客户
			if("4".equals(guarFlag)){
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "客户评级，流程审批通过逻辑处理【更新客户表中的评级信息 start 4--融资性担保公司】！", null);
				SqlClient.update("updateGuarCrdGrade",cusId,insertMap, null, connection);//更新cus_base表
				insertMap.clear();
				insertMap.put("guar_bail_multiple", guar_bail_multiple);
				insertMap.put("guar_cls", guar_cls);
				SqlClient.update("updateGuarCrdGradeCusCom",cusId,insertMap, null, connection);//更新cus_com表
			}else if("1".equals(guarFlag)||"5".equals(guarFlag)){
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "客户评级，流程审批通过逻辑处理【更新客户表中的评级信息 start 对公客户/个人客户】！", null);
				SqlClient.update("updateCusCrdGrade",cusId,insertMap, null, connection);
			}else if("2".equals(guarFlag)){
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "客户评级，流程审批通过逻辑处理【更新客户表中的评级信息 start 直接认定的对公客户】！", null);
				SqlClient.update("updateCusCrdGrade",cusId,insertMap, null, connection);
				SqlClient.update("updateStrCusCrd",cusId,cusGradeDt, null, connection);
			}else if("3".equals(guarFlag)){
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "客户评级，流程审批通过逻辑处理【更新客户表中的评级信息 start 同业客户】！", null);
				SqlClient.update("updateCusSameCrdGrade",cusId,insertMap, null, connection);
			}
		} catch (Exception e) {
			throw new EMPException("评级信息回写客户表失败，错误描述："+e.getMessage());
		}
		return "1";
	}

	/**
	 * 批量更新个人客户信用评级、评级日期
	 * @param indGrade	个人批量评级信息
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	public String updateIndivGrade(IndexedCollection indGrade, Connection connection) throws Exception {
		try {
			Map<String, String> insertMap = new HashMap<String, String>();
			
			if(indGrade.size()==0){
				throw new EMPException("传入个人评级信息列表为空");
			}

			for(int i=0;i<indGrade.size();i++){
//				Map<String, String> map = (HashMap<String, String>)indGrade.get(i);
				KeyedCollection map = (KeyedCollection) indGrade.get(i);
				String cusId = (String) map.getDataValue("cusid");
				insertMap.put("grade", (String)map.getDataValue("grade"));
				insertMap.put("gradeDt", (String)map.getDataValue("gradedt"));
				SqlClient.update("updateCusCrdGrade",cusId,insertMap, null, connection);
			}
		} catch (Exception e) {
			throw new EMPException("评级信息回写客户表失败，错误描述："+e.getMessage());
		}
		return "1";
	}
	
	/**
	 * 审批结束更新客户所属条线
	 * @param cusId  	客户编号
	 * @param belgLine	所属条线
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	public String updateCusBelgLine(String cusId, String belgLine, Connection connection) throws Exception {
		try {
			Map<String, String> insertMap = new HashMap<String, String>();
			insertMap.put("belgLine", belgLine);
			SqlClient.update("updateCusBelgLine",cusId,insertMap, null, connection);//更新cus_base表

		} catch (Exception e) {
			throw new EMPException("审批结束更新客户所属条线失败，错误描述："+e.getMessage());
		}
		return "1";
	}
	
	/**
	 *  通过客户号获取客户基表信息
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return CusBase 客户基本信息对象
	 * @throws Exception
	 */
	public CusBase getCusBaseByCusId(String cusId, Context context, Connection connection) throws Exception {
		CusBase cusBase = null;
		try {
			CusBaseComponent cbcomp = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE, context,connection);
			cusBase = cbcomp.getCusBase(cusId);
		} catch (Exception e) {
			throw new EMPException("通过客户号获取客户基表信息失败，错误描述："+e.getMessage());
		}
		return cusBase;
	}
	
	/**
	 *  通过客户号获取客户基表信息
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return CusBase 客户基本信息对象
	 * @throws Exception
	 */
	public CusGrpInfo getCusGrpInfoByGrpNo(String grpNo, Context context, Connection connection) throws Exception {
		CusGrpInfo cusGrpInfo = null;
		try {
			CusGrpInfoComponent cusGrpInfoComponent = (CusGrpInfoComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPINFO, context,connection);
			cusGrpInfo = cusGrpInfoComponent.getCusGrpInfoDomainByGrpNo(grpNo);
		} catch (Exception e) {
			throw new EMPException("通过客户号获取客户基表信息失败，错误描述："+e.getMessage());
		}
		return cusGrpInfo;
	}
	
	/**
	 *  通过客户号获取对公客户详细信息
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return CusBase 客户基本信息对象
	 * @throws Exception
	 */
	public CusCom getCusComByCusId(String cusId, Context context, Connection connection) throws Exception {
		CusCom cusCom = null;
		try {
			CusComComponent cccomp = (CusComComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOM, context,connection);
			cusCom = cccomp.getCusCom(cusId);
		} catch (Exception e) {
			throw new EMPException("通过客户号获取对公客户详细信息失败，错误描述："+e.getMessage());
		}
		return cusCom;
	}
	
	/**
	 *  通过客户号获取客户所属集团融资模式
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return grpFinanceType 客户所属集团融资模式
	 * @throws Exception 
	 */
	public String getGrpFinanceType(String cusId, Context context, Connection connection) throws Exception{
		String grpFinanceType = "";
		CusGrpInfoComponent cgiComp = (CusGrpInfoComponent)CMISComponentFactory
		.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPINFO, context,connection);
		
		CusGrpInfo cgi = cgiComp.getCusGrpInfoDomainByCusId(cusId);
		if(cgi!=null&&cgi.getGrpNo()!=null&&!"".equals(cgi.getGrpNo())){
			grpFinanceType = cgi.getGrpFinanceType();
		}
		return grpFinanceType;
	}
	
	/**
	 *  通过客户号获取同业客户信息
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return KeyedCollection 同业客户kColl
	 * @throws Exception 
	 */
	public KeyedCollection getCusSameOrgKcoll(String cusId, Context context, Connection connection) throws Exception{
		KeyedCollection sameOrgKcoll = new KeyedCollection("CusSameOrg");
		CusComComponent cusComComp = (CusComComponent)CMISComponentFactory
		.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOM, context,connection);
//		CusSameOrg cusSameOrg = cusComComp.getCusSameOrg(cusId);
//		if(cusSameOrg!=null&&cusSameOrg.getCusId()!=null&&!"".equals(cusSameOrg.getCusId())){
//			ComponentHelper helper = new ComponentHelper();
//			sameOrgKcoll = helper.domain2kcol(cusSameOrg, "CusSameOrg");
//		}
		sameOrgKcoll = cusComComp.getCusSameOrgkColl(cusId);
		return sameOrgKcoll;
	}

	/**
	 * 通过对公客户号获取该对公客户法人客户码
	 * @param cusId 客户码 
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return cusIdRel 法人客户码
	 * @throws Exception
	 */
	public String getManagerByCusId(String cusId,Context context, Connection connection) throws Exception {
		String cusIdRel = null;
		
		CusComManagerComponent cuscommanager = (CusComManagerComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(
                PUBConstant.CUSCOMMANAGER, context,connection);
		
		CusComManager cusComManager = cuscommanager.getCusComManager(cusId);
		if(cusComManager!=null&&cusComManager.getCusIdRel()!=null&&!"".equals(cusComManager)){
			cusIdRel = cusComManager.getCusIdRel();
		}
		return cusIdRel;
	}
	
	/**
	 *  通过客户号获取该客户所在集团成员列表
	 * @param cusId	客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return IndexedCollection 集团成员客户iColl
	 * @throws Exception 
	 */
	public IndexedCollection getGrpMemberByCusId(String cusId, Context context, Connection connection) throws Exception{
		IndexedCollection iCollMem = new IndexedCollection();

		CusGrpInfoComponent cgiComp = (CusGrpInfoComponent)CMISComponentFactory
		.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPINFO, context,connection);
		CusGrpInfo cgi = cgiComp.getCusGrpInfoDomainByCusId(cusId);
		if(cgi==null||cgi.getGrpNo()==null||"".equals(cgi.getGrpNo())){
			return null;
		}else{
			iCollMem = SqlClient.queryList4IColl("findCusGrpMemberByGrpNo", cgi.getGrpNo(), connection);
			return iCollMem;
		}
	}
	
	/**
	 *  通过集团客户号获取集团成员列表
	 * @param grpNo	集团客户码
	 * @param connection 数据库连接
	 * @return IndexedCollection 集团成员客户iColl
	 * @throws Exception 
	 */
	public IndexedCollection getGrpMemberByGrpNo(String grpNo, Connection connection) throws Exception{
		IndexedCollection iCollMem = new IndexedCollection();
		if(grpNo==null||"".equals(grpNo)){
			return null;
		}else{
			iCollMem = SqlClient.queryList4IColl("findCusGrpMemberByGrpNo", grpNo, connection);
			return iCollMem;
		}
	}
	
	/**
	 *  通过个人客户号查询该客户是否是经营性客户
	 * @param cusId	客户码
	 * @param connection 数据库连接
	 * @return String 是否是经营性客户
	 * @throws Exception 
	 */
	public String isCusIndivBusiness(String cusId, Connection connection) throws Exception{
		String returnFlag = "";
		BigDecimal isBusiness = (BigDecimal)SqlClient.queryFirst("isCusIndivBusiness", cusId, null, connection);
		if(isBusiness.compareTo(BigDecimal.ZERO)>0){
			returnFlag = "Y";
		}else{
			returnFlag = "N";
		}
		
		return returnFlag;
	}
	
	/**
	 *  通过个人客户号查询该客户关联客户
	 * @param cusId	客户码
	 * @param cusRel 关联关系
	 * @param connection 数据库连接
	 * @return IndexedCollection 关联客户
	 * @throws Exception 
	 */
	public IndexedCollection getIndivSocRel(String cusId, String cusRel, Connection connection) throws Exception{
		Map<String,String> param = new HashMap<String,String>();
		param.put("cus_id", cusId);
		param.put("cus_rel", cusRel);
		IndexedCollection iColl = SqlClient.queryList4IColl("queryCusIndivSocRel", param, connection);
		return iColl;
	}
	
	/**
	 *  通过客户号获取客户基表信息
	 * @param cusId	客户码
	 * @param connection 数据库连接
	 * @return CusBase 客户基本信息对象
	 * @throws Exception
	 */
	public KeyedCollection getCusBaseByCusId(String cusId, Connection connection) throws Exception {
		KeyedCollection cusBase = new KeyedCollection();
		try {
			cusBase = (KeyedCollection)SqlClient.queryFirst("queryExistsCus", cusId, null, connection);
		} catch (Exception e) {
			throw new EMPException("通过客户号获取客户基表信息失败，错误描述："+e.getMessage());
		}
		return cusBase;
	}
	
}
