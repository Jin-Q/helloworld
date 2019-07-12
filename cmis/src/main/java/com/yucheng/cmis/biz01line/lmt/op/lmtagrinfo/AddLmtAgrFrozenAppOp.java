package com.yucheng.cmis.biz01line.lmt.op.lmtagrinfo;


import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.biz01line.lmt.domain.LmtAgrDetails;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtAgrFrozenAppOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtApply";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String agrNo = "";
		String serno="";
		try{
			connection = this.getConnection(context);
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			serno = kColl.getDataValue("serno").toString();
			agrNo = kColl.getDataValue("agr_no").toString();
			LmtPubComponent lmtAgr = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			List<CMISDomain> lmtAgrDets = lmtAgr.queryLmtAgrDetailsListByLmtSerno(agrNo);
			for(int i=0;i<lmtAgrDets.size();i++){
				String lmtCode = CMISSequenceService4JXXD.querySequenceFromED("ED", "all", connection, context);
				LmtAgrDetails lmtAgrDet = (LmtAgrDetails)lmtAgrDets.get(i);
				String lmt_status = lmtAgrDet.getLmtStatus();
				if(!"30".equals(lmt_status)){//“终止”状态的台账不发起冻结
					KeyedCollection kColl4AppDet = new KeyedCollection("LmtAppDetails");
					kColl4AppDet.addDataField("serno", serno);
					kColl4AppDet.addDataField("crd_amt", lmtAgrDet.getCrdAmt());
					kColl4AppDet.addDataField("prd_id", lmtAgrDet.getPrdId());
					kColl4AppDet.addDataField("limit_code", lmtCode);
					kColl4AppDet.addDataField("limit_name", lmtAgrDet.getLimitName());
					kColl4AppDet.addDataField("limit_type", lmtAgrDet.getLimitType());
					kColl4AppDet.addDataField("sub_type", lmtAgrDet.getSubType());
					kColl4AppDet.addDataField("cur_type", "CNY");
					kColl4AppDet.addDataField("org_limit_code", lmtAgrDet.getLimitCode());
					//kColl4AppDet.addDataField("froze_amt", lmtAgrDet.getFrozeAmt());
					kColl4AppDet.addDataField("guar_type", lmtAgrDet.getGuarType());
					kColl4AppDet.addDataField("term", lmtAgrDet.getTerm());
					kColl4AppDet.addDataField("term_type", lmtAgrDet.getTermType());
					kColl4AppDet.addDataField("start_date", lmtAgrDet.getStartDate());
					kColl4AppDet.addDataField("end_date", lmtAgrDet.getEndDate());
					dao.insert(kColl4AppDet, connection);
				}
			}
			context.addDataField("flag", "succ");
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
