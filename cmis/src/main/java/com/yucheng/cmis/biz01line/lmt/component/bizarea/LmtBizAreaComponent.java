package com.yucheng.cmis.biz01line.lmt.component.bizarea;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.dao.bizarea.LmtBizAreaDao;
import com.yucheng.cmis.biz01line.lmt.domain.jointguar.LmtAgrJointCoop;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDaoFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class LmtBizAreaComponent extends CMISComponent{
	/**
	 * LmtNameList转换为 LmtAgrDetails
	 * @return
	 * @throws Exception 
	 */
	public KeyedCollection save2Details(KeyedCollection kColl, Connection connection, Context context) throws Exception {
		KeyedCollection kc = new KeyedCollection("LmtAgrDetails");
		this.setConnection(connection);
		this.setContext(context);
		try {
//			String serno = (String)kColl.getDataValue("serno");  //业务编号
			String lmt_serno = "";  //授信协议编号   联保协议 号
			String sub_type = "02";   //分项类别: 商圈 
			String cus_id = (String)kColl.getDataValue("cus_id");   //客户码
			String limit_type = (String)kColl.getDataValue("limit_type"); //额度类型
			String limit_code = (String)kColl.getDataValue("limit_code"); //授信额度编号
			String limit_name = (String)kColl.getDataValue("limit_name"); //额度品种名称
			String prd_id = (String)kColl.getDataValue("prd_id"); //适用产品编号
			
			String cur_type = (String)kColl.getDataValue("cur_type"); //授信币种
			String crd_amt = (String)kColl.getDataValue("crd_amt"); //授信金额
			String guar_type = (String)kColl.getDataValue("guar_type"); //担保方式
			String term_type = (String)kColl.getDataValue("term_type"); //授信期限类型
			String term  = (String)kColl.getDataValue("term"); //授信期限
			
			//根据期限类型和期限  来计算开始日期  到期日期   
			String start_date  = (String)context.getDataValue(PUBConstant.OPENDAY); //授信开始日期   
			String end_date = LmtUtils.computeEndDate(start_date, term_type, term);
			String cus_type = ""; //客户类型
			String biz_area_no = "";//queryBizAreaNo(serno, connection,context);  //商圈编号
			
			kc.addDataField("lmt_serno", lmt_serno);
			kc.addDataField("sub_type", sub_type);
			kc.addDataField("cus_id", cus_id);
			kc.addDataField("limit_type", limit_type);
			kc.addDataField("limit_code", limit_code);
			kc.addDataField("limit_name", limit_name);
			kc.addDataField("prd_id", prd_id);
			kc.addDataField("cur_type", cur_type);
			kc.addDataField("crd_amt", crd_amt);
			kc.addDataField("guar_type", guar_type);
			kc.addDataField("term_type", term_type);
			kc.addDataField("term", term);
			kc.addDataField("start_date", start_date);
			kc.addDataField("end_date", end_date);
			kc.addDataField("cus_type", cus_type);
			kc.addDataField("biz_area_no", biz_area_no);
		} catch (Exception e) {
			throw e;
		}
		return kc;
	}

	/**
	 * 联保小组生成协议,并返回联保协议编号
	 */
	public String jointGuarApp2Agr(String serno,Connection connection,Context context,TableModelDAO dao) throws Exception{
		String modelId = "LmtAppJointCoop";
		String modelIdAgr = "LmtAgrJointCoop";
		String openDate = (String) context.getDataValue(PUBConstant.OPENDAY); 
		IndexedCollection ic = dao.queryList(modelIdAgr, " where serno='" + serno + "'", connection);
		if(ic.size() > 0 ){
			KeyedCollection kCollAgr = (KeyedCollection)ic.get(0);
			String agr_no = (String)kCollAgr.getDataValue("agr_no");//联保协议编号
			return agr_no;
		}
		KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
		
		LmtAgrJointCoop domainAgr = new LmtAgrJointCoop();
		ComponentHelper helper = new ComponentHelper();
		String term_type = (String)kColl.getDataValue("term_type");
		String term  = (String)kColl.getDataValue("term");
		kColl.remove("app_date");
		kColl.remove("over_date");
		kColl.remove("term_type");
		kColl.remove("term");
		kColl.remove("approve_status");
		domainAgr = (LmtAgrJointCoop) helper.kcolTOdomain(domainAgr, kColl);
		KeyedCollection kCollAgr = new KeyedCollection(modelIdAgr);
		kCollAgr = helper.domain2kcol(domainAgr, modelIdAgr);
		//起始日期  到期日期  协议状态
		kCollAgr.addDataField("start_date", openDate);
		String end_date = LmtUtils.computeEndDate(openDate, term_type, term);
		kCollAgr.addDataField("end_date", end_date);
		kCollAgr.addDataField("agr_flag","0");
		String agrNo = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
		kCollAgr.addDataField("agr_no",agrNo);
		kCollAgr.addDataField("agr_status","002");
		//add协议数据
		dao.insert(kCollAgr, connection);
		return agrNo;
//		connection.commit();
	}

	/**
	 * 根据客户码查询客户是否在圈商或联保小组内内
	 * @param cusId  客户码
	 * @throws Exception 
	 */
	public KeyedCollection queryJointAreaFlag(String cusId,Connection connection) throws Exception{
		KeyedCollection kColl = new KeyedCollection();
		LmtBizAreaDao lbaDao = (LmtBizAreaDao) CMISDaoFactory.getDaoFactoryInstance().getDaoInstance("BizArea");
		kColl = lbaDao.queryJointAreaFlag(cusId, connection);
		return kColl;
	}
}
