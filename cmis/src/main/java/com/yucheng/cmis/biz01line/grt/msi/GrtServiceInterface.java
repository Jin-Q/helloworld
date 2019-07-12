package com.yucheng.cmis.biz01line.grt.msi;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;

/**
 * 押品模块向外提供的接口
 * @author 肖迪
 */
@ModualService(modualId="grt",modualName="担保管理",serviceId="grtServices",serviceDesc="担保模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface")
public interface GrtServiceInterface {
	
	/**
	 * 根据标志的不同，返回不同的担保信息（抵质押物或保证人）
	 * @param guaranty_no_str 押品编号/保证人编号
	 * @param flag 标志位（用来区分押品编号/保证人编号）
	 * @return res_value 返回押品列表/保证人列表
	 */
	@MethodService(method="getGuarantyInfoList", desc="返回综合授信模块的担保信息",
			inParam={
				@MethodParam(paramName="guaranty_no_str",paramDesc="押品编号/保证人编号"),
				@MethodParam(paramName="flag",paramDesc="标志位（1--押品，2--保证人）"),
				@MethodParam(paramName="begin",paramDesc="数据分页的起始条数"),
				@MethodParam(paramName="end",paramDesc="数据分页的结束条数"),
				@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam={
				@MethodParam(paramName="res_value",paramDesc="返回押品列表或保证人列表")
			},
			methodType=MethodType.JAVA,
			example="getGuarantyInfoList(guaranty_no_str,flag,connection)")
	public IndexedCollection getGuarantyInfoList(String guaranty_no_str,String flag,PageInfo pageInfo,DataSource dataSource) throws EMPException;
	
	/**
	 * 展示授信的申请人名下所有可以被引入的押品信息
	 * @param cus_id 客户码
	 */
	@MethodService(method="queryMortGuarantyPopList.do", desc="展示授信的申请人名下所有可以被引入的押品信息",
			inParam={
				@MethodParam(paramName="cus_id",paramDesc="客户码"),
			},
			outParam={
				@MethodParam(paramName="cus_id",paramDesc="客户码"),
				@MethodParam(paramName="guaranty_no",paramDesc="押品编号"),
				@MethodParam(paramName="guaranty_name",paramDesc="押品名称"),
				@MethodParam(paramName="guaranty_cls",paramDesc="押品类别"),
				@MethodParam(paramName="guaranty_type",paramDesc="押品类型"),
				@MethodParam(paramName="guaranty_info_status",paramDesc="押品信息状态"),
				@MethodParam(paramName="manager_id",paramDesc="管理人"),
				@MethodParam(paramName="manager_br_id",paramDesc="管理机构"),
				@MethodParam(paramName="input_date",paramDesc="登记日期"),
			},
			example="queryMortGuarantyPopList.do?cus_id=****")
			public void queryMortGuarantyPopList(String cus_id) throws EMPException;
	
	/**
	 * 综合授信模块保证人信息查看功能
	 * @param guar_id 保证编码
	 */
	@MethodService(method="getGrtGuaranteeViewPage.do", desc="根据保证编号查看具体的保证人信息",
			inParam={
				@MethodParam(paramName="guar_id",paramDesc="保证编码"),
			},
			outParam={
			},
			example="getGrtGuaranteeViewPage.do?guar_id=****")
	public void getGrtGuaranteeViewPage(String guar_id) throws EMPException;
	
	/**
	 * 综合授信模块保证人信息引入功能
	 */
	@MethodService(method="queryGrtGuaranteePopList.do", desc="引入保证人信息",
			inParam={
			},
			outParam={
			},
			example="queryGrtGuaranteePopList.do")
	public void getGrtGuaranteeIntroPage() throws EMPException;
	
	/**
	 * 根据押品编号查看押品详情信息
	 * @param guaranty_no 押品编号
	 */
	@MethodService(method="getMortGuarantyBaseInfoViewPage.do", desc="根据押品编号显示押品详情信息",
			inParam={
				@MethodParam(paramName="guaranty_no",paramDesc="押品编号"),
			},
			outParam={
			},
			example="getMortGuarantyBaseInfoViewPage.do?guaranty_no=****")
	public void getMortGuarantyBaseInfoViewPage(String guaranty_no) throws EMPException;
	
	/**
	 * 业务办理模块最高额担保合同引入功能
	 * @param cus_id 客户码
	 */
	@MethodService(method="introGrtGuarContList.do", desc="引入最高额担保合同信息",
			inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码")
			},
			outParam={
			},
			example="introGrtGuarContList.do?cus_id=**")
	public void introGrtGuarContList() throws EMPException;
	
	/**
	 * 业务办理模块一般担保合同引入功能
	 * @param cus_id 客户码
	 */
	@MethodService(method="introYbGrtGuarContList.do", desc="引入一般担保合同信息",
			inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码")
			},
			outParam={
			},
			example="introYbGrtGuarContList.do?cus_id=**")
	public void introYbGrtGuarContList() throws EMPException;
	
	/**
	 * 业务办理模块一般担保合同维护功能
	 */
	@MethodService(method="queryYbGrtGuarContList.do", desc="维护一般担保合同信息",
			inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码")
			},
			outParam={
			},
			example="queryYbGrtGuarContList.do?cus_id=**")
	public void queryYbGrtGuarContList() throws EMPException;
	
	/**
	 * 业务办理模块一般/最高担保合同查看功能
	 * @param guar_cont_no 担保合同编号
	 * @return res_value 返回担保合同详细信息
	 */
	@MethodService(method="viewGuarContInfoDetail", desc="查看一般/最高额担保合同信息",
			inParam={
				@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
			},
			outParam={
				@MethodParam(paramName="res_value",paramDesc="返回担保信息详细信息")
			},
			methodType=MethodType.JAVA,
			example="viewGuarContInfoDetail(guar_cont_no,connection)")
	public KeyedCollection viewGuarContInfoDetail(String guar_cont_no,Connection connection,Context context) throws EMPException;
	
	/**
	 * 根据担保合同编号来获取业务相关的担保合同信息信息（业务办理模块担保信息）
	 * @param guar_cont_no_str 担保合同编号串
	 * @return res_value 返回所属客户编号下的担保合同信息
	 */
	@MethodService(method="getGuarContInfoList", desc="返回业务办理模块的担保信息列表",
			inParam={
				@MethodParam(paramName="guar_cont_no_str",paramDesc="担保合同编号串"),
				@MethodParam(paramName="pageInfo",paramDesc="调用此接口处的context中包含的信息--用来分页"),
				@MethodParam(paramName="dataSource",paramDesc="调用此接口处的context中包含的信息--获取数据源"),
			},
			outParam={
				@MethodParam(paramName="res_value",paramDesc="返回担保信息列表")
			},
			methodType=MethodType.JAVA,
			example="getGuarContInfoList(guaranty_no_str,pageInfo,dataSource)")
	public IndexedCollection getGuarContInfoList(String guar_cont_no_str,PageInfo pageInfo,DataSource dataSource) throws EMPException;
	
	/**
	 * 根据合同编号删除合同信息及其关联信息
	 * @param guar_cont_no 担保合同编号
	 * @return res_value 返回删除结果
	 */
	@MethodService(method="deleteGuarContInfo", desc="担保合同及其关联关系删除功能",
			inParam={
				@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号支持"),
			},
			outParam={
				@MethodParam(paramName="res_value",paramDesc="返回删除结果")
			},
			methodType=MethodType.JAVA,
			example="deleteGuarContInfo(guar_cont_no,connection,context)")  
	public int deleteGuarContInfo(String guar_cont_no,Connection connection,Context context) throws EMPException;
	
	/**
	 * 通过合同编号（支持多值），担保方式获取所需担保列表
	 * @param guarNoList 担保编号列表
	 * @param guarWay 担保方式
	 * @param pageInfo 分页对象
	 * @param dataSource 数据源
	 * @return IndexedCollection
	 * @throws Exception
	 */
	@MethodService(method="getGrtListByGuarNoListAndGuarWay",desc="通过合同编号（支持多值），担保方式获取所需担保列表",
			inParam={ 
			@MethodParam(paramName="guarNoList",paramDesc="担保编号列表"),
			@MethodParam(paramName="guarWay",paramDesc="担保方式"),
			@MethodParam(paramName="pageInfo",paramDesc="翻页信息"),
			@MethodParam(paramName="dataSource",paramDesc="数据库连接池")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="担保列表")) 
	public IndexedCollection getGrtListByGuarNoListAndGuarWay(String guarNoList, String guarWay, PageInfo pageInfo, DataSource dataSource) throws Exception;
	
	/**
	 * 根据保证人客户码获取保证人所有关联的合同编号
	 * @param cus_id 担保编号列表
	 * @param pageInfo 分页对象
	 * @param dataSource 数据源
	 * @return IndexedCollection
	 * @throws Exception
	 */
	@MethodService(method="getGuarContNoByGuarantyteeCusId",desc="根据保证人客户码获取保证人所有关联的合同编号",
			inParam={ 
			@MethodParam(paramName="cus_id",paramDesc="保证人客户码"),
			@MethodParam(paramName="pageInfo",paramDesc="翻页信息"),
			@MethodParam(paramName="dataSource",paramDesc="数据库连接池")
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="合同编号列表")) 
	public IndexedCollection getGuarContNoByGuarantyteeCusId(String cusId,Connection connection) throws Exception;
	
	/**
	 * 根据合同编号判断此合同是否可被删除
	 * @param guar_cont_no 合同编号
	 * @return boolean 此担保合同是否可被删除，false--有效或注销，不能删除，true--登记，可以删除
	 * @throws Exception
	 */
	@MethodService(method="getGuarContNoByGuarantyteeCusId",desc="根据合同编号判断此合同是否有效",
			inParam={ 
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="boolean",paramDesc="是否可被删除")) 
	public boolean getGuarContStatusByGuarContNo(String guarContNo,Connection connection) throws Exception;
	
	/**
	 * 根据合同编号更新担保状态
	 * @param guar_cont_no 合同编号字符串
	 * @return res_value
	 * @throws Exception
	 */
	@MethodService(method="updateGrtGuarContSta",desc="根据合同编号更新担保状态",
			inParam={ 
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="int",paramDesc="更新结果")) 
	public int updateGrtGuarContSta(String guarContNo,DataSource dataSource,Connection connection) throws Exception;
	
	/**
	 * 根据合同编号更新担保状态
	 * @param guar_cont_no 合同编号字符串
	 * @return res_value
	 * @throws Exception
	 */
	@MethodService(method="updateGrtGuarCont4RemoveCont",desc="根据合同编号更新担保状态",
			inParam={ 
			@MethodParam(paramName="guar_cont_no",paramDesc="担保合同编号"),
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="int",paramDesc="更新结果")) 
	public int updateGrtGuarCont4RemoveCont(String guarContNo,DataSource dataSource,Connection connection) throws Exception;
	
	/**
	 * 查询贷款客户下所有生效合同下的担保信息
	 * @param cus_id
	 * @param pageInfo（可传空）
	 * @param context
	 * @param connection
	 * @param grt_type（1-抵质押，2-保证，3-查询保证人客户信息（无重复），4-查询押品信息（无重复））
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getGrtListByCusId",desc="查询贷款客户下所有生效合同下的担保信息",
			inParam={ 
			@MethodParam(paramName="cus_id",paramDesc="客户编号"),
	}, 
	methodType=MethodType.JAVA,
	outParam=@MethodParam(paramName="iColl",paramDesc="担保信息列表")) 
	public IndexedCollection getGrtListByCusId(String cus_id,PageInfo pageInfo,String grt_type,DataSource dataSource) throws Exception;
}
