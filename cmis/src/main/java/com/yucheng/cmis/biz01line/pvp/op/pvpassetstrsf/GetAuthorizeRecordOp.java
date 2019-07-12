package com.yucheng.cmis.biz01line.pvp.op.pvpassetstrsf;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetAuthorizeRecordOp extends CMISOperation {
	private static final String PVPMODEL = "PvpLoanApp";
	private static final String AUTHMODEL = "PvpAuthorize";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try {
			connection = this.getConnection(context);
			String serno = "";
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			String condition = " where serno = '" + serno + "'";
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			/** 通过业务编号获取出账信息 */
			List list = new ArrayList();
			list.add("serno");
			list.add("prd_id");
			list.add("cus_id");
			list.add("cont_no");
			list.add("bill_no");
			list.add("pvp_amt");
			list.add("approve_status");
			KeyedCollection pvpKColl = dao.queryFirst(PVPMODEL, list, condition, connection);
			pvpKColl.setDataValue("approve_status", "2");
			dao.update(pvpKColl, connection);
			/** 生成出账授权信息 */
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, context);
			KeyedCollection authKColl = new KeyedCollection(AUTHMODEL);
			authKColl.addDataField("serno", authSerno);
			authKColl.addDataField("tran_serno", pvpKColl.getDataValue("serno"));
			authKColl.addDataField("prd_id", pvpKColl.getDataValue("prd_id"));
			authKColl.addDataField("cus_id", pvpKColl.getDataValue("cus_id"));
			authKColl.addDataField("cus_name", "");
			authKColl.addDataField("send_times", "0");
			authKColl.addDataField("cont_no", pvpKColl.getDataValue("cont_no"));
			authKColl.addDataField("bill_no", pvpKColl.getDataValue("bill_no"));
			authKColl.addDataField("tran_amt", pvpKColl.getDataValue("pvp_amt"));
			authKColl.addDataField("status", "00");//状态默认为登记
			
			dao.insert(authKColl, connection);
			//context.put("flag", "success");
			context.addDataField("flag", "success");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
