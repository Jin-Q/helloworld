package com.yucheng.cmis.biz01line.lmt.op.lmtagrfinguar;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.ConnectionManager;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class FinGuarUtils {
	
	public static void buildFinGuarAgrInfo(Context context, String serno) throws EMPException {
		try{
			Connection connection = ConnectionManager.getConnection((DataSource) context.getService(CMISConstance.ATTR_DATASOURCE));
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollApp = dao.queryDetail("LmtAppFinGuar", serno, connection);
			String cus_id = kCollApp.getDataValue("cus_id").toString();
			String openDay = (String)context.getDataValue("OPENDAY");//当前系统日期
			
			KeyedCollection kc = dao.queryFirst("LmtAgrFinGuar", null,"where cus_id='" + cus_id + "' and lmt_end_date >'"+openDay+"' and agr_status='002'", connection);
			if (kc.getDataValue("serno") == null||kc.getDataValue("serno").equals("")) {// 如果在协议表中查找不到数据则插入
				// 修改申请表中的“申请状态”和“办结时间”
				kCollApp.setDataValue("approve_status", "997");
				kCollApp.setDataValue("end_date", context.getDataValue("OPENDAY"));
				int i = dao.update(kCollApp, connection);
				if (i != 1) {
					throw new EMPException("update Failed! Record i: " + i);
				}
				int count = insertLmtAgrFinGuar(kCollApp, dao, connection, context);//生成一笔新的授信协议
				if (count != 1) {
					throw new EMPException("Insert Failed! Record Count: " + count);
				}
			} else {
				
				// 修改申请表中的“申请状态”和“办结时间”
				kCollApp.setDataValue("approve_status", "997");
				kCollApp.setDataValue("end_date", context .getDataValue("OPENDAY"));
				int i = dao.update(kCollApp, connection);
				if (i != 1) {
					throw new EMPException("update Failed! Record i: " + i);
				}
	
				int count = updateLmtAgrFinGuar(connection, kCollApp, kc, dao);//修改原有的授信协议
				if (count != 1) {
					throw new EMPException("Update Failed! Record Count: " + count);
				}
			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl4trade = new KeyedCollection();
//			kColl4trade.put("CLIENT_NO", cus_id);
//			kColl4trade.put("BUSS_SEQ_NO", "");
//			kColl4trade.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, context, connection);	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, context)){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch(Exception e){
			throw new EMPException();
//			throw new EMPException("融资担保授信流程审批报错，错误描述："+e.printStackTrace());
		}
	}

	/**   修改融资担保公司授信协议    */
	private static int updateLmtAgrFinGuar(Connection connection, KeyedCollection kCollApp, KeyedCollection kc, TableModelDAO dao) throws EMPException {
		// 把申请表里的数据添加到协议表中
		try{
			kc.setDataValue("serno", kCollApp.getDataValue("serno"));
			kc.setDataValue("fin_totl_limit", kCollApp.getDataValue("fin_totl_limit"));
			kc.setDataValue("fin_totl_spac", kCollApp.getDataValue("fin_totl_spac"));
			kc.setDataValue("single_quota", kCollApp.getDataValue("single_quota"));
			kc.setDataValue("share_range", kCollApp.getDataValue("share_range"));
			kc.setDataValue("lmt_term_type", kCollApp.getDataValue("lmt_term_type"));
			kc.setDataValue("term", kCollApp.getDataValue("term"));
			kc.setDataValue("guar_bail_multiple", kCollApp.getDataValue("guar_bail_multiple"));
			kc.setDataValue("guar_cls", kCollApp.getDataValue("guar_cls"));
			kc.setDataValue("eval_rst", kCollApp.getDataValue("eval_rst"));
			kc.setDataValue("input_br_id", kCollApp.getDataValue("input_br_id"));
			kc.setDataValue("input_id", kCollApp.getDataValue("input_id"));
			kc.setDataValue("manager_br_id", kCollApp.getDataValue("manager_br_id"));
			kc.setDataValue("manager_id", kCollApp.getDataValue("manager_id"));
			kc.setDataValue("input_date", kCollApp.getDataValue("input_date"));
			kc.setDataValue("lmt_start_date", kCollApp.getDataValue("end_date"));
			kc.setDataValue("lmt_end_date", LmtUtils.computeEndDate(kCollApp.getDataValue("end_date").toString(), 
			kCollApp.getDataValue("lmt_term_type").toString(), kCollApp.getDataValue("term").toString()));
			kc.setDataValue("agr_status", "002");
			return dao.update(kc, connection);
		}catch(Exception e){
			throw new EMPException("修改融资担保公司授信协议报错，错误描述："+e.getMessage());
		}
	}

	/**   生成融资担保公司授信协议    */
	private static int insertLmtAgrFinGuar(KeyedCollection kCollApp, TableModelDAO dao, Connection connection, Context context) throws EMPException {
		try{
			// 自动生成协议编号
			String agrNo = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR","all", connection, context);
			// 把申请表里的数据添加到协议表中
			KeyedCollection kCollAgr = new KeyedCollection("LmtAgrFinGuar");
			kCollAgr.addDataField("serno", kCollApp.getDataValue("serno"));
			kCollAgr.addDataField("cus_id", kCollApp.getDataValue("cus_id"));
			kCollAgr.addDataField("fin_cls", kCollApp.getDataValue("fin_cls"));
			kCollAgr.addDataField("fin_totl_limit", kCollApp.getDataValue("fin_totl_limit"));
			kCollAgr.addDataField("fin_totl_spac", kCollApp.getDataValue("fin_totl_spac"));
			kCollAgr.addDataField("single_quota", kCollApp.getDataValue("single_quota"));
			kCollAgr.addDataField("share_range", kCollApp.getDataValue("share_range"));
			kCollAgr.addDataField("lmt_term_type", kCollApp.getDataValue("lmt_term_type"));
			kCollAgr.addDataField("term", kCollApp.getDataValue("term"));
			kCollAgr.addDataField("guar_bail_multiple", kCollApp.getDataValue("guar_bail_multiple"));
			kCollAgr.addDataField("guar_cls", kCollApp.getDataValue("guar_cls"));
			kCollAgr.addDataField("eval_rst", kCollApp.getDataValue("eval_rst"));
			kCollAgr.addDataField("input_br_id", kCollApp.getDataValue("input_br_id"));
			kCollAgr.addDataField("input_id", kCollApp.getDataValue("input_id"));
			kCollAgr.addDataField("manager_br_id", kCollApp.getDataValue("manager_br_id"));
			kCollAgr.addDataField("manager_id", kCollApp.getDataValue("manager_id"));
			kCollAgr.addDataField("input_date", kCollApp.getDataValue("input_date"));
			kCollAgr.addDataField("lmt_start_date", kCollApp.getDataValue("end_date"));
			kCollAgr.addDataField("lmt_end_date", LmtUtils.computeEndDate(kCollApp.getDataValue("end_date").toString(), kCollApp.getDataValue(
					"lmt_term_type").toString(), kCollApp.getDataValue("term").toString()));
			kCollAgr.addDataField("agr_status", "002");
			kCollAgr.addDataField("agr_no", agrNo);
			int count = dao.insert(kCollAgr, connection);
			return count;
		}catch(Exception e){
			throw new EMPException("生成融资担保公司授信协议报错，错误描述："+e.getMessage());
		}
	}

	
}
