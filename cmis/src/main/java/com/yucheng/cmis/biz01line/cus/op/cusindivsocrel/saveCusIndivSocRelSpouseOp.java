package com.yucheng.cmis.biz01line.cus.op.cusindivsocrel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivSocRel;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class saveCusIndivSocRelSpouseOp extends CMISOperation {

	// operation TableModel
	private final String modelId = "CusIndivSocRel";

	private final String cus_id_name = "cus_id";
	
	private boolean updateCheck = false;
	/**
	 * 根据context 中的 cus_id , weatherSpouse 来保存或修改\删除 \新增 model中的数据
	 */
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try {
			connection = this.getConnection(context);

			if (this.updateCheck) {
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}

			String cus_id_value = null;
			String cus_id_rel = null;
			try {
				cus_id_value = (String) context.getDataValue(cus_id_name);
				cus_id_rel = (String) context.getDataValue("cus_id_rel");
			} catch (Exception e) {
			}

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id", cus_id_value);
			pkMap.put("cus_id_rel", cus_id_rel);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);

			// 加载component 如果前面已经有实例从工厂中加载，请使用改实例的getComponent(comId)
			// 如cusBaseComponent.getComponent(comId)，以保证事务一致
			CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.CUSBASE, context,connection);

			// 标识证件类型、证件号码MAP，其中cert_type对应证件类型字段名，cert_code对应证件号码字段名
			Map<String,String> pkMaps = new HashMap<String,String>();
			pkMaps.put("cert_type", "indiv_rel_cert_typ");
			pkMaps.put("cert_code", "indiv_rl_cert_code");

			// 需要从客户信息中获取的字段mapping关系map
			Map <String,String>colMap = new HashMap<String,String>();
			colMap.put("indiv_rel_cus_name", "cus_name");
			colMap.put("indiv_rel_cert_typ", "cert_type");
			colMap.put("indiv_rl_cert_code", "cert_code");

			colMap.put("indiv_sex", "indiv_sex");
			colMap.put("indiv_rel_job", "indiv_occ");
			colMap.put("indiv_rel_duty", "indiv_com_job_ttl");

			colMap.put("indiv_rel_phn", "fphone");

			cusBaseComponent.getKCollCus(kColl, colMap, pkMaps);

			//SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
            //SInfoUtils.addUSerName(kColl, new String[]{"input_id", "last_upd_id"});
			
			this.putDataElement2Context(kColl, context);
			
			ComponentHelper cHelper = new ComponentHelper();
			CusIndivSocRel cusIndivSocRel = new CusIndivSocRel();
			cusIndivSocRel = (CusIndivSocRel) cHelper.kcolTOdomain(
					cusIndivSocRel, kColl);

//			CusIndivSocRelComponent cusIndivSocRelComponent = (CusIndivSocRelComponent) CMISComponentFactory
//					.getComponentFactoryInstance().getComponentInstance(
//							PUBConstant.CUSINDIVSOCREL, context, connection);

//			String strReturnMessage = cusIndivSocRelComponent
//					.checkExist(cusIndivSocRel);
//			String message = cusIndivSocRelComponent.checkExist(cus_id);
//			
//			if ("1".equals(weatherSpouse)) {
//				if (message.equals("y")) {
//					flag = "该客户已经存在配偶信息，修改信息！";
//					dao.update(kColl, connection);
//					cusIndivSocRelComponent.giveValueToCusIndivSocRel(cusIndivSocRel);
//				} else {
//					if (strReturnMessage.equals("no")) {
//						// 不存在，可以新增
//						dao.insert(kColl, connection);
//						cusIndivSocRelComponent
//								.giveValueToCusIndivSocRel(cusIndivSocRel);
//						flag = "新增信息";
//					} else if (strReturnMessage.equals("yes")) {
//						// 证件类型+证件号码+客户码已存在
//						flag = "该客户下的同一关联客户已存在，不能新增！";
//					}
//				}
//			} else {
//				if (message.equals("y")) {
//					flag = "删除";
//					Map map = new HashMap();
//					map.put("cus_id", cus_id);
//					map.put("indiv_rel_cert_typ", indiv_rel_cert_typ);
//					map.put("indiv_rl_cert_code", indiv_rl_cert_code);
//					dao.deleteByPks(modelId, map, connection);
//				}
//			}
		} catch (EMPException ee) {
			flag = "新增失败";
			EMPLog.log("CusIndivSocRel", EMPLog.ERROR, 0, "新增失败!", ee);
			throw ee;
		} catch (Exception e) {
			flag = "新增失败";
			EMPLog.log("CusIndivSocRel", EMPLog.ERROR, 0, "新增失败!", e);
			throw new EMPException(e);
		} finally {

			if (connection != null) {
				this.releaseConnection(context, connection);
			}
		}
		context.addDataField("flag", flag);
		return "0";
	}
}
