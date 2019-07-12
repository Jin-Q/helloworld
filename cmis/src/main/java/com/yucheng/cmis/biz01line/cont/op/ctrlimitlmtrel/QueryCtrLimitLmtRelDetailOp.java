package com.yucheng.cmis.biz01line.cont.op.ctrlimitlmtrel;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryCtrLimitLmtRelDetailOp  extends CMISOperation {
	
	private final String modelId = "CtrLimitLmtRel";
	private final String pk_id_name = "pk_id";
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String pk_id_value = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");

			PageInfo pageInfo = new PageInfo();
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk_id_value, connection);
			String lmt_code_no = (String)kColl.getDataValue("lmt_code_no");
			/** 通过授信额度品种编号查询出授信模块的授信信息 */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtServiceInterface = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			IndexedCollection lmtIColl = lmtServiceInterface.queryLmtAgrDetailsByLimitCodeStr("'"+lmt_code_no+"'", pageInfo, dataSource);
			if(lmtIColl != null){
				KeyedCollection kc = (KeyedCollection)lmtIColl.get(0);
				kColl.addDataField("lmt_code_name", kc.getDataValue("limit_name"));
				kColl.addDataField("lmt_code_amt", kc.getDataValue("crd_amt"));
				kColl.addDataField("lmt_code_enable_amt", kc.getDataValue("enable_amt"));
				kColl.addDataField("lmt_type", kc.getDataValue("limit_type"));
			}
			String[] args=new String[] { "lmt_code_name" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			kColl.put("lmt_code_name", kColl.getDataValue("lmt_code_name_displayname"));
			this.putDataElement2Context(kColl, context);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
