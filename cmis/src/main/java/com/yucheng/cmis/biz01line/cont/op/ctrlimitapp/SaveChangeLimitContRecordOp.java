package com.yucheng.cmis.biz01line.cont.op.ctrlimitapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 保存额度合同变更信息，迁移一份从表数据到从表临时表中
 * 需要迁移的表包括：合同占用授信明细表
 * @author QZCB
 *
 */
public class SaveChangeLimitContRecordOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = (KeyedCollection)context.getDataElement("CtrLimitApp");
			/** 新增一条业务申请数据 */
			String newSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			kColl.setDataValue("serno", newSerno);
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl.addDataField("app_amt", kColl.getDataValue("cont_amt"));
			kColl.addDataField("approve_status", "000");
			dao.insert(kColl, connection);
			/** 迁移合同占用授信明细数据到临时表中 */
			String contNo = (String)kColl.getDataValue("cont_no");
			IndexedCollection appIColl = dao.queryList("CtrLimitLmtRelTemp", null, " where limit_cont_no='"+contNo+"'", connection);
			if(appIColl != null && appIColl.size() > 0){
				UNIDGenerator unid = new UNIDGenerator();
				for(int i=0;i<appIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)appIColl.get(i);
					kc.setDataValue("pk_id", unid.getUNID());
					kc.setDataValue("limit_serno", newSerno);
					kc.setName("CtrLimitLmtRel");
					dao.insert(kc, connection);
				}
			}
			/** 复制客户经理绩效数据(插入新的业务流水号) */
			IndexedCollection cusMagIColl = dao.queryList("CusManager", "where cont_no='"+contNo+"'", connection);
			for(int i=0;i<cusMagIColl.size();i++){
				KeyedCollection CusMagkColl = (KeyedCollection)cusMagIColl.get(i);
				CusMagkColl.setDataValue("serno", newSerno);
				CusMagkColl.setDataValue("cont_no", "");
				dao.insert(CusMagkColl, connection);
			}
			
			
			context.addDataField("flag", "success");
			context.addDataField("newSerno", newSerno);
		} catch (Exception e) {
			context.addDataField("flag", "failed");
			context.addDataField("newSerno", "");
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
