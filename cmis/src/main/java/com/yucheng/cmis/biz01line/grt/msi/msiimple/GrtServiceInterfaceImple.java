package com.yucheng.cmis.biz01line.grt.msi.msiimple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GrtServiceInterfaceImple extends CMISModualService implements GrtServiceInterface {
	private static final Logger logger = Logger.getLogger(GrtServiceInterfaceImple.class);

	/**
	 * 获取综合授信模块所需的押品信息和保证人信息
	 * @param guaranty_no_str 押品编号/保证人编号
	 * @param flag 标志位（用来区分押品编号/保证人编号）
	 * @return res_value 返回押品列表/保证人列表
	 */
	public IndexedCollection getGuarantyInfoList(String guaranty_no_str,String flag,PageInfo pageInfo,DataSource dataSource) throws EMPException{
		IndexedCollection res_value = null;
		KeyedCollection kc = new KeyedCollection();
		logger.info("---------------调用押品模块接口获取押品信息开始---------------");
		if(null==flag || (!"1".equals(flag) && !"2".equals(flag))){ //标志位为空时
			logger.error("调用押品模块接口获得相关押品信息错误，标志类型[flag]传入值："+flag+" 错误，正确取值：1--押品  2--保证人！");
			throw new EMPException("调用押品模块接口获得相关押品信息错误，标志类型[flag]传入值："+flag+" 错误，正确取值：1--押品  2--保证人！");
		}
		if(null == guaranty_no_str || "".equals(guaranty_no_str)){
			logger.error("调用押品模块接口获得相关押品信息错误，押品编号/保证人编号[guaranty_no_str]不能为空！");
			throw new EMPException("调用押品模块接口获得相关押品信息错误，押品编号/保证人编号[guaranty_no_str]不能为空！");
		}
		try {
			if("1".equals(flag)){  //获取抵质押物信息
				kc.addDataField("guaranty_no",guaranty_no_str);
				String sql_select =SqlClient.joinQuerySql("getLmtGuarantyInfoList",kc,null);
				res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
				//TableModelUtil.				
			}else{   //获取保证人信息
				//kc.addDataField("guaranty_no",guaranty_no_str);
				String sql_select =SqlClient.joinQuerySql("getLmtGuaranteeInfoList",guaranty_no_str,null);
				res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
			}
		}catch (Exception e) {
			logger.error("获取押品或保证人信息失败，错误描述："+e.getMessage());
			throw new EMPException("获取押品或保证人信息失败，错误描述："+e.getMessage());
		}
		logger.info("---------------调用押品模块接口获取押品信息结束---------------");
		return res_value;
	}
	/**
	 * 展示授信的申请人名下所有可以被引入的押品信息
	 * @param cus_id 客户码
	 */
	public void queryMortGuarantyPopList(String cus_id) throws EMPException{
		// TODO Auto-generated method stub
		
	}
	/**
	 * 综合授信模块保证人信息查看功能
	 * @param guar_id 保证编码
	 */
	public void getGrtGuaranteeViewPage(String guar_id) throws EMPException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 综合授信模块保证人信息引入功能
	 */
	public void getGrtGuaranteeIntroPage() throws EMPException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 根据押品编号查看押品详情信息
	 * @param guaranty_no 押品编号
	 */
	public void getMortGuarantyBaseInfoViewPage(String guaranty_no)
			throws EMPException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 业务办理模块最高额担保合同引入功能
	 * @param cus_id 客户码
	 */
	public void introGrtGuarContList() throws EMPException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 业务办理模块一般担保合同引入功能
	 * @param cus_id 客户码
	 */
	public void introYbGrtGuarContList() throws EMPException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 业务办理模块一般担保合同维护功能
	 */
	public void queryYbGrtGuarContList() throws EMPException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 业务办理模块一般/最高担保合同查看功能
	 * @param guar_cont_no 担保合同编号
	 * @return res_value 返回担保合同详细信息
	 */
	public KeyedCollection viewGuarContInfoDetail(String guar_cont_no,Connection connection,Context context) throws EMPException {
		KeyedCollection res_value = null;
		logger.info("---------------调用担保模块接口查看担保合同信息开始---------------");
		if(null == guar_cont_no || "".equals(guar_cont_no)){
			logger.error("调用担保模块接口获得相关担保合同信息错误，担保合同编号[guar_cont_no_str]不能为空！");
			throw new EMPException("调用担保模块接口获得相关担保合同信息错误，担保合同编号[guar_cont_no_str]不能为空！");
		}
		try {
			res_value =(KeyedCollection) SqlClient.queryFirst("viewGuarContInfoDetail", guar_cont_no, null,connection);
			res_value.setName("GrtGuarCont");
			//构建担保管理模块业务组件
			GrtGuarContComponet ggc = (GrtGuarContComponet) CMISComponentFactory.getComponentFactoryInstance().
			getComponentInstance("GrtGuarCont", context, connection);
			if(res_value.getDataValue("guar_model").equals("01")){//票据池
				
				//根据合同编号得到池编号
				String drfpoNo = ggc.getDrfpoNoByGuarContNo(guar_cont_no);
				//构建票据池组件类
				DpoDrfpoComponent dpoComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
				/**根据票据池编号获取票据池中处于在池状态的票据票面金额价值总额*/
				Double count = dpoComponent.getDrftAmtByDrfpoNo(drfpoNo,"01");
				res_value.addDataField("drfpo_no", drfpoNo);
				res_value.addDataField("bill_amt", count);
			}else if(res_value.getDataValue("guar_model").equals("02")){//应收账款池
				//根据合同编号得到应收账款池编号
				String poNo = ggc.getDrfpoNoByGuarContNo(guar_cont_no);
				IqpActrecBondComponent component = new IqpActrecBondComponent();
				String sAmt = component.getAllInvcAndBondAmt(poNo, connection).split("@")[2];
				res_value.addDataField("po_no",poNo);
				res_value.addDataField("bill_amt",sAmt);
				res_value.addDataField("poType","1");
			}else if(res_value.getDataValue("guar_model").equals("03")){//保理池
				//根据合同编号得到保理池编号
				String poNo = ggc.getDrfpoNoByGuarContNo(guar_cont_no);
				IqpActrecBondComponent component = new IqpActrecBondComponent();
				String sAmt = component.getAllInvcAndBondAmt(poNo, connection).split("@")[2];
				res_value.addDataField("po_no",poNo);
				res_value.addDataField("bill_amt",sAmt);
				res_value.addDataField("poType","2");
		  }else if(res_value.getDataValue("guar_model").equals("00")&&(res_value.getDataValue("guar_way").equals("02")||res_value.getDataValue("guar_way").equals("03"))){//保证人信息
				String guarAmt = ggc.getGuarAmtByGuarContNo(guar_cont_no);
				res_value.addDataField("bill_amt",guarAmt);
		  }else if(res_value.getDataValue("guar_model").equals("00")&&(res_value.getDataValue("guar_way").equals("01")||res_value.getDataValue("guar_way").equals("00"))){//担保品信息
			//获取押品编号，根据押品编号获得担保金额的总和
				String guarAmt = ggc.queryGrtGuarantyAmt(guar_cont_no);
				res_value.addDataField("bill_amt",guarAmt);
		  }else if(res_value.getDataValue("guar_model").equals("05")){//联保
				String agr_no = (String) res_value.getDataValue("agr_no");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				//获取总的授信额度
		    	BigDecimal total_amt = new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(agr_no, "03", connection));
		    	res_value.addDataField("bill_amt",total_amt);
		  }
		
		if(res_value.getDataValue("lmt_grt_flag").equals("1")){//是否授信项下授信（值为“是”时）
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				KeyedCollection resultKc = service.queryRLmtGuarContInfo(guar_cont_no,"",connection);
				if(resultKc != null){
					resultKc.addDataField("guar_amt",res_value.getDataValue("guar_amt"));
					resultKc.setName("RLmtGuar"); 
				}
		}
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(res_value, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(res_value, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(res_value, new String[] { "input_id" });
			SInfoUtils.addSOrgName(res_value, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(res_value, new String[] { "manager_id" });
			//this.putDataElement2Context(res_value, context);
		}catch (Exception e) {
			logger.error("获取担保合同信息失败，错误描述："+e.getMessage());
			throw new EMPException("获取查看担保合同信息失败，错误描述："+e.getMessage());
		}
		logger.info("---------------调用担保模块接口查看担保合同信息结束---------------");
		return res_value;
	}
	/**
	 * 根据担保合同编号来获取业务相关的担保合同信息信息（业务办理模块担保信息）
	 * @param guar_cont_no_str 担保合同编号串
	 * @return res_value 返回所属客户编号下的担保合同信息
	 */
	public IndexedCollection getGuarContInfoList(String guar_cont_no_str,PageInfo pageInfo, DataSource dataSource) throws EMPException {
		IndexedCollection res_value = null;
		logger.info("---------------调用担保模块接口获取担保合同信息开始---------------");
		if(null == guar_cont_no_str || "".equals(guar_cont_no_str)){
			logger.error("调用担保模块接口获得相关担保合同信息错误，担保合同编号[guar_cont_no_str]不能为空！");
			throw new EMPException("调用担保模块接口获得相关担保合同信息错误，担保合同编号[guar_cont_no_str]不能为空！");
		}  
		try {
			String sql_select =SqlClient.joinQuerySql("getIqpGuarContInfoList",guar_cont_no_str,null);
			res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		}catch (Exception e) {
			logger.error("获取担保合同信息失败，错误描述："+e.getMessage());
			throw new EMPException("获取担保合同信息失败，错误描述："+e.getMessage());
		}
		logger.info("---------------调用担保模块接口获取担保合同信息结束---------------");
		return res_value;
	}
	
	/**
	 * 根据合同编号删除合同表信息及其关联关系
	 * @param guar_cont_no 担保合同编号
	 * @return res_value 返回删除结果
	 */
	public int deleteGuarContInfo(String guar_cont_no,Connection connection,Context context) throws EMPException {
		int ret = -1;
	    logger.info("---------------调用担保模块接口 根据合同编号删除合同表信息及其关联关系 开始---------------");
		if(null == guar_cont_no || "".equals(guar_cont_no)){
			logger.error("调用担保模块接口 根据合同编号删除担保合同信息及其关联关系错误，担保合同编号[guar_cont_no]不能为空！");
			throw new EMPException("调用担保模块接口 根据合同编号删除担保合同信息及其关联关系错误，担保合同编号[guar_cont_no]不能为空！");
		} 
	     try {
	    	//以下顺序不能乱，先删除关系表再删除担保合同
	    	String del_grtguarantyre = SqlClient.joinQuerySql("deleteGrtGuarantyReCont", guar_cont_no, null);
	    	ret = SqlClient.deleteBySql(del_grtguarantyre, connection);
	    	//删除担保合同
	    	del_grtguarantyre = SqlClient.joinQuerySql("deleteGrtGuarContByContNo", guar_cont_no, null);
	    	ret = SqlClient.deleteBySql(del_grtguarantyre, connection);
			//SqlClient.executeUpd("deleteGrtGuarantyReCont", guar_cont_no, null, null, connection);
			//SqlClient.executeUpd("deleteGrtGuarContByContNo", guar_cont_no, null, null, connection);
		} catch (SQLException e) {
			logger.error("根据合同编号删除担保合同信息及其关联关系失败，错误描述："+e.getMessage());
			throw new EMPException("根据合同编号删除担保合同信息及其关联关系失败，错误描述："+e.getMessage());
		}
		logger.info("---------------调用担保模块接口 根据合同编号删除担保合同信息及其关联关系  结束---------------");
		return ret;  
	}
	/**
	 * 通过合同编号（支持多值），担保方式获取所需担保列表
	 * @param guarNoList 担保编号列表
	 * @param guarWay 担保方式
	 * @param pageInfo 分页对象
	 * @param dataSource 数据源
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getGrtListByGuarNoListAndGuarWay(String guarNoList, String guarWay, PageInfo pageInfo,DataSource dataSource) throws Exception {
		IndexedCollection returnIColl = null;
		try {
			Map param = new HashMap();
			param.put("guar_cont_no", guarNoList);
			param.put("guar_cont_type", guarWay);
			String sql_select = SqlClient.joinQuerySql("getGrtListByGuarNoListAndGuarWay", param, null);
			returnIColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		} catch (Exception e) {
			throw new EMPException("");
		}
		return returnIColl;
	}
	/**
	 * 根据保证人客户码获取保证人所有关联的合同编号
	 * @param cus_id 担保编号列表
	 * @param pageInfo 分页对象
	 * @param dataSource 数据源
	 * @return IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getGuarContNoByGuarantyteeCusId(String cusId,Connection connection) throws Exception {
		// TODO Auto-generated method stub
		IndexedCollection returnIColl = null;
		logger.info("---------------调用担保模块接口获取担保合同信息开始---------------");
		if(null == cusId || "".equals(cusId)){
			logger.error("调用担保模块接口根据保证人客户码获取保证人所有关联的合同编号错误，保证人客户码[cusId]不能为空！");
			throw new EMPException("调用担保模块接口根据保证人客户码获取保证人所有关联的合同编号错误，保证人客户码[cusId]不能为空！");
		}  
		try {
			returnIColl = SqlClient.queryList4IColl("getGuarContNoByGuarantyteeCusId", cusId, connection);
		} catch (Exception e) {
			logger.error("根据保证人客户码获取保证人所有关联的合同编号为空！");
			throw new EMPException("获取担保合同信息失败，错误描述："+e.getMessage());
		}
		logger.info("---------------调用担保模块接口获取担保合同信息结束---------------");
		return returnIColl;
	}
	/**
	 * 根据合同编号判断此合同是否可被删除
	 * @param guar_cont_no 合同编号
	 * @return boolean 此担保合同是否可被删除，false--有效或注销，不能删除，true--登记，可以删除
	 * @throws Exception
	 */
	public boolean getGuarContStatusByGuarContNo(String guarContNo,Connection connection) throws Exception {
		boolean result = false;
		logger.info("---------------调用担保模块接口根据合同编号判断此合同是否可被删除开始---------------");
		if(null == guarContNo || "".equals(guarContNo)){
			logger.error("调用担保模块接口根据合同编号判断此合同是否可被删除错误，担保合同编号[guarContNo]不能为空！");
			throw new EMPException("调用担保模块接口根据合同编号判断此合同是否可被删除错误，担保合同编号[guarContNo]不能为空！");
		}  
		try {
			KeyedCollection kc = (KeyedCollection) SqlClient.queryFirst("getGuarContStatusByGuarContNo", guarContNo, null, connection);
			String status = (String) kc.getDataValue("guar_cont_state");
			if("00".equals(status)){
				result=true;
			}else{
				result=false;
			}
		} catch (Exception e) {
			logger.error("根据合同编号判断此合同是否可被删除失败！");
			throw new EMPException("根据合同编号判断此合同是否可被删除失败，错误描述："+e.getMessage());
		}
		logger.info("---------------调用担保模块接口根据合同编号判断此合同是否可被删除结束---------------");
		return result;
	}
	/**
	 * 根据合同编号更新担保状态(合同撤销时)
	 * @param guar_cont_no 合同编号字符串
	 * @return res_value
	 * @throws Exception
	 */
	public int updateGrtGuarContSta(String guarContNo,DataSource dataSource,Connection connection) throws Exception {
		int res_value=0;
		logger.info("---------------根据合同编号更新担保状态 开始---------------");
		try {
			String sql_select =SqlClient.joinQuerySql("updateGrtGuarContSta",guarContNo,null);
			SqlClient.deleteBySql(sql_select, connection);
		} catch (Exception e) { 
			throw new Exception("更新担保状态失败!");
		}
		logger.info("---------------根据合同编号更新担保状态 结束---------------");
		return res_value;
	}
	/**
	 * 根据合同编号更新担保状态（合同作废时）
	 * @param guar_cont_no 合同编号字符串
	 * @return res_value
	 * @throws Exception
	 */
	public int updateGrtGuarCont4RemoveCont(String guarContNo,DataSource dataSource,Connection connection) throws Exception {
		int res_value=0;
		logger.info("---------------根据合同编号更新担保状态 开始---------------");
		try {
			String sql_select =SqlClient.joinQuerySql("updateGrtGuarCont4RemoveCont",guarContNo,null);
			SqlClient.deleteBySql(sql_select, connection);
		} catch (Exception e) { 
			throw new Exception("更新担保状态失败!");
		}
		logger.info("---------------根据合同编号更新担保状态 结束---------------");
		return res_value;
	}
	
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
	public IndexedCollection getGrtListByCusId(String cus_id,PageInfo pageInfo,String grt_type,DataSource dataSource) throws Exception {
		logger.info("---------------查询贷款客户下所有生效合同下的担保信息  开始---------------");
		IndexedCollection iColl = null;
		KeyedCollection kc = new KeyedCollection();
		String sqlstr = "";
		try {
			if(grt_type.equals("1")){
				sqlstr = "getGrtMortInfoListByCusId";
			}else if(grt_type.equals("2")){
				sqlstr = "getGrtGuaranteeInfoListByCusId";
			}else if(grt_type.equals("3")){
				sqlstr = "getGrtGuaranteeMsgListByCusId";
			}else if(grt_type.equals("4")){
				sqlstr = "getGuarantyBaseInfoListByCusId";
			}else{
				throw new EMPException("未知担保类型！");	
			}
			
			kc.addDataField("cus_id",cus_id);
			String sql_select =SqlClient.joinQuerySql(sqlstr,kc,null);
			iColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		}catch(Exception e){ 
			logger.error("查询贷款客户下所有担保信息，错误描述："+e.getMessage());
			throw new EMPException("查询贷款客户下所有担保信息，错误描述："+e.getMessage());	
		}
		return iColl;
	}
}
