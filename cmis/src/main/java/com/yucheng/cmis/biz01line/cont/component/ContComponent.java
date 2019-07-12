package com.yucheng.cmis.biz01line.cont.component;

import java.sql.Connection;
import java.util.HashMap;

import sun.awt.UNIXToolkit;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 额度合同事务处理component
 * @author QZCB
 *
 */
public class ContComponent extends CMISComponent {

	/**
	 * 新增额度担保合同业务处理类
	 * @param appKColl 额度合同业务申请表单
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @param dao 表模型加载机制
	 * @throws Exception 异常
	 */
	public void addCtrLimitCont(KeyedCollection appKColl, Context context,
			Connection connection, TableModelDAO dao) throws Exception {
		try {
			String contNo = CMISSequenceService4JXXD.querySequenceFromDB("HT", "all", connection, context);
			if(contNo == null){
				throw new EMPException("生成额度合同编号【"+contNo+"】失败！");
			}
			/**--------------------合同主表数据插入--------------------*/
			KeyedCollection contKColl = new KeyedCollection();
			contKColl.addDataField("cont_no", contNo);
			contKColl.addDataField("serno", appKColl.getDataValue("serno"));
			//contKColl.addDataField("app_type", appKColl.getDataValue("app_type"));
			contKColl.addDataField("cus_id", appKColl.getDataValue("cus_id"));
			contKColl.addDataField("cur_type", appKColl.getDataValue("cur_type"));
			contKColl.addDataField("cont_amt", appKColl.getDataValue("app_amt"));
			contKColl.addDataField("start_date", appKColl.getDataValue("start_date"));
			contKColl.addDataField("end_date", appKColl.getDataValue("end_date"));
			contKColl.addDataField("cont_status", "100");//待签订
			contKColl.addDataField("manager_br_id", appKColl.getDataValue("manager_br_id"));
			contKColl.addDataField("input_id", appKColl.getDataValue("input_id"));
			contKColl.addDataField("input_br_id", appKColl.getDataValue("input_br_id"));
			contKColl.addDataField("input_date", appKColl.getDataValue("input_date"));
			
			contKColl.setName("CtrLimitCont");
			dao.insert(contKColl, connection);
			/**--------------------合同占用授信明细更新--------------------*/
			IndexedCollection relIColl = dao.queryList("CtrLimitLmtRel", null, " where limit_serno = '"+appKColl.getDataValue("serno")+"'", connection);
			if(relIColl != null && relIColl.size() > 0){
				UNIDGenerator unid = new UNIDGenerator();
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection relKColl = (KeyedCollection)relIColl.get(i);
					relKColl.setDataValue("pk_id", unid.getUNID());
					relKColl.setDataValue("limit_cont_no", contNo);
					relKColl.setName("CtrLimitLmtRelTemp");
					dao.insert(relKColl, connection);
				}
			}
			//SqlClient.executeUpd("updateCtrLimitLmtRelBySerno", appKColl.getDataValue("serno"), contNo, null, connection);
			/**--------------------客户经理绩效更新--------------------*/
			SqlClient.executeUpd("updateCusManagerBySerno", appKColl.getDataValue("serno"), contNo, null, connection);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
	}
	/**
	 * 变更额度担保合同业务处理类,
	 * 需要重新覆盖原先的合同信息，并且更新相关的从表信息
	 * @param appKColl 额度合同业务申请表单
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @param dao 表模型加载机制
	 * @throws Exception 异常
	 */
	public void updateCtrLimitCont(KeyedCollection appKColl, Context context,
			Connection connection, TableModelDAO dao) throws Exception {
		try {
			/**--------------------合同主表start--------------------*/
			String contNo = (String)appKColl.getDataValue("cont_no");
			KeyedCollection contKColl = new KeyedCollection();
			contKColl.put("cont_no", contNo);
			contKColl.put("serno", appKColl.getDataValue("serno"));
			//contKColl.put("app_type", appKColl.getDataValue("app_type"));
			contKColl.put("cus_id", appKColl.getDataValue("cus_id"));
			contKColl.put("cur_type", appKColl.getDataValue("cur_type"));
			contKColl.put("cont_amt", appKColl.getDataValue("app_amt"));
			contKColl.put("start_date", appKColl.getDataValue("start_date"));
			contKColl.put("end_date", appKColl.getDataValue("end_date"));
			contKColl.put("cont_status", "100");//改为未签订 
			contKColl.put("manager_br_id", appKColl.getDataValue("manager_br_id"));
			contKColl.put("input_id", appKColl.getDataValue("input_id"));
			contKColl.put("input_br_id", appKColl.getDataValue("input_br_id"));
			contKColl.put("input_date", appKColl.getDataValue("input_date"));
			
			contKColl.setName("CtrLimitCont");
			dao.update(contKColl, connection);
			/**--------------------原合同占用授信明细删除--------------------*/
			SqlClient.delete("deleteCtrLimitLmtRelByContNo", contNo, connection);
			/**--------------------插入新的合同占用授信明细--------------------*/
			IndexedCollection relIColl = dao.queryList("CtrLimitLmtRel", null, " where limit_serno = '"+appKColl.getDataValue("serno")+"'", connection);
			if(relIColl != null && relIColl.size() > 0){
				UNIDGenerator unid = new UNIDGenerator();
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection relKColl = (KeyedCollection)relIColl.get(i);
					relKColl.put("pk_id", unid.getUNID());
					relKColl.put("limit_cont_no", contNo);
					relKColl.setName("CtrLimitLmtRelTemp");
					dao.insert(relKColl, connection);
				}
			}
			/**--------------------变更合同占用授信明细删除(暂时不删除，放开即可删除)--------------------*/
			//SqlClient.delete("deleteCtrLimitLmtRelTempByContNo", contNo, connection);
			/**--------------------把原客户经理绩效合同号放空--------------------*/
			SqlClient.update("updateCusManagerByContNo", contNo, null, null, connection);
			/**--------------------把新客户经理绩效放入合同号--------------------*/
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("cont_no", contNo);
			map.put("serno", appKColl.getDataValue("serno").toString());
			SqlClient.update("updateCusManagerForSerno", map, contNo, null, connection);
			
			
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		
	}
	
	/**
	 * 合同签订，把保证金状态变为执行中
	 * @param cont_no 合同编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @param dao 表模型加载机制
	 * @throws Exception 异常
	 */
	public void updatePubBailInfo(String cont_no, Context context,
			Connection connection, TableModelDAO dao) throws Exception {
		try {
			/**--------------------合同签订，把保证金状态变为执行中start--------------------*/
			SqlClient.update("updatePubBailInfo", cont_no, null, null, connection);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
	}
	

}
