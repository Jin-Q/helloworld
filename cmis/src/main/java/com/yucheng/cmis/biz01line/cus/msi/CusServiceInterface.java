package com.yucheng.cmis.biz01line.cus.msi;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 客户模块对外提供服务接口
 * @author 
 * @version V1.0
 */
@ModualService(modualId="cus",modualName="客户模块",serviceId="cusServices",serviceDesc="客户模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface")
public interface CusServiceInterface {
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
	@MethodService(method="updateGrade",desc="评级结束更新客户评级信息",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="cusGrade",paramDesc="评级结果"),
				@MethodParam(paramName="cusGradeDt",paramDesc="评级日期"),
				@MethodParam(paramName="guarFlag",paramDesc="是否担保公司"),
				@MethodParam(paramName="guar_bail_multiple",paramDesc="保证金放大倍数"),
				@MethodParam(paramName="guar_cls",paramDesc="担保类别"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="修改结果"))
	public String updateGrade(String cusId, String cusGrade, String cusGradeDt, String guarFlag,String guar_bail_multiple,String guar_cls, Connection connection) throws Exception ;
	
	/**
	 * 审批结束更新客户所属条线
	 * @param cusId  	客户编号
	 * @param belgLine	所属条线
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	@MethodService(method="updateCusBelgLine",desc="审批结束更新客户所属条线",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="cusGrade",paramDesc="所属条线"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="修改结果"))
	public String updateCusBelgLine(String cusId, String belgLine, Connection connection) throws Exception ;
	
	
	/**
	 * 批量更新个人客户信用评级、评级日期
	 * @param indGrade	个人批量评级信息
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	@MethodService(method="updateIndivGrade",desc="个人批量评级更新客户信息",
			inParam={
				@MethodParam(paramName="indGrade",paramDesc="个人批量评级信息"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="结果 (1：成功)"))
	public String updateIndivGrade(IndexedCollection indGrade, Connection connection) throws Exception ;
	
	
	
	/**
	 *  通过客户号获取客户基表信息
	 * @param cusId	客户码
	 * @param connection 数据库连接
	 * @return CusBase 客户基本信息对象
	 * @throws Exception
	 */
	@MethodService(method="getCusBaseByCusId",desc="通过客户码获得客户基表信息",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="CusBase",paramDesc="客户基本信息对象"))
	public KeyedCollection getCusBaseByCusId(String cusId,  Connection connection) throws Exception ;
	
	
	/**
	 *  通过客户号获取客户基表信息
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return CusBase 客户基本信息对象
	 * @throws Exception
	 */
	@MethodService(method="getCusBaseByCusId",desc="通过客户码获得客户基表信息",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="context",paramDesc="context上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="CusBase",paramDesc="客户基本信息对象"))
	public CusBase getCusBaseByCusId(String cusId, Context context, Connection connection) throws Exception ;

	/**
	 *  通过客户号获取客户基表信息
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return CusBase 客户基本信息对象
	 * @throws Exception
	 */
	@MethodService(method="getCusGrpInfoByGrpNo",desc="通过集团客户码获得集团客户基表信息",
			inParam={
				@MethodParam(paramName="grpNo",paramDesc="集团客户码"),
				@MethodParam(paramName="context",paramDesc="context上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="CusGrpInfo",paramDesc="集团客户基本信息对象"))
	public CusGrpInfo getCusGrpInfoByGrpNo(String grpNo, Context context, Connection connection) throws Exception ;	
	
	/**
	 *  通过客户号获取对公客户详细信息
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return CusCom 客户基本信息对象
	 * @throws Exception
	 */
	@MethodService(method="getCusComByCusId",desc="通过客户号获取对公客户详细信息",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="context",paramDesc="context上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="CusCom",paramDesc="对公客户详细信息对象"))
	public CusCom getCusComByCusId(String cusId, Context context, Connection connection) throws Exception ;
	
	/**
	 *  通过客户号获取客户所属集团融资模式
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return grpFinanceType 客户所属集团融资模式
	 * @throws Exception 
	 */
	@MethodService(method="getGrpFinanceType",desc="通过客户号获取客户所属集团融资模式",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="context",paramDesc="context上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="grpFinanceType",paramDesc="客户所属集团融资模式"))
	public String getGrpFinanceType(String cusId, Context context, Connection connection) throws Exception;
	
	/**
	 *  通过客户号获取同业客户信息
	 * @param cusId	同业客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return KeyedCollection 同业客户kColl
	 * @throws Exception 
	 */
	@MethodService(method="getCusSameOrgKcoll",desc="通过客户号获取同业客户信息",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="同业客户码"),
				@MethodParam(paramName="context",paramDesc="context上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="同业客户kColl"))
	public KeyedCollection getCusSameOrgKcoll(String cusId, Context context, Connection connection) throws Exception;
	

	/**
	 * 通过对公客户号获取该对公客户法人客户码
	 * @param cusId 客户号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return cusIdRel 法人客户码
	 * @throws Exception
	 */
	@MethodService(method="getManagerByCusId",desc="通过对公客户号获取该对公客户法人客户码",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户号"),
				@MethodParam(paramName="context",paramDesc="context上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="cusIdRel",paramDesc="法人客户码"))
	public String getManagerByCusId(String cusId, Context context, Connection connection) throws Exception;

	/**
	 *  通过客户号获取该客户所在集团成员列表
	 * @param cusId	客户码
	 * @param context 上下文 
	 * @param connection 数据库连接
	 * @return IndexedCollection 集团成员客户iColl
	 * @throws Exception 
	 */
	@MethodService(method="getGrpMemberByCusId",desc="通过客户号获取该客户所在集团成员客户信息",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="context",paramDesc="context上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="iColl",paramDesc="集团成员客户iColl"))
	public IndexedCollection getGrpMemberByCusId(String cusId, Context context, Connection connection) throws Exception;

	/**
	 *  通过集团客户号获取集团成员列表
	 * @param grpNo	集团客户码
	 * @param connection 数据库连接
	 * @return IndexedCollection 集团成员客户iColl
	 * @throws Exception 
	 */
	@MethodService(method="getGrpMemberByGrpNo",desc="通过集团客户号获取集团成员列表",
			inParam={
				@MethodParam(paramName="grpNo",paramDesc="集团客户码"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="iColl",paramDesc="集团成员客户iColl"))
	public IndexedCollection getGrpMemberByGrpNo(String grpNo, Connection connection) throws Exception;
	
	/**
	 *  通过个人客户号查询该客户是否是经营性客户
	 * @param cusId	客户码
	 * @param connection 数据库连接
	 * @return String 是否是经营性客户
	 * @throws Exception 
	 */
	@MethodService(method="isCusIndivBusiness",desc="通过个人客户号查询该客户是否是经营性客户",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="isBusiness",paramDesc="是否是经营性客户"))
	public String isCusIndivBusiness(String cusId, Connection connection) throws Exception;
	
	
	/**
	 *  通过个人客户号查询该客户关联客户
	 * @param cusId	客户码
	 * @param cusRel 关联关系(1：配偶 2:父母 3:子女  9:兄弟姐妹)
	 * @param connection 数据库连接
	 * @return IndexedCollection 关联客户
	 * @throws Exception 
	 */
	@MethodService(method="getIndivSocRel",desc="通过个人客户号查询该客户关联客户",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
				@MethodParam(paramName="cusRel",paramDesc="关联关系"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="cusIdRel",paramDesc="关联客户码"))
	public IndexedCollection getIndivSocRel(String cusId, String cusRel, Connection connection) throws Exception;
}
