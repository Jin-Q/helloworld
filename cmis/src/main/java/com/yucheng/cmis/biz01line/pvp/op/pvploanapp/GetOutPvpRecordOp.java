package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.pvp.component.PvpComponent;
import com.yucheng.cmis.biz01line.pvp.pvptools.PvpConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetOutPvpRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CtrLoanCont";
	
	/**
	 * bussiness logic operation  
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			IndexedCollection iColl = null;
			try {
				iColl = (IndexedCollection)context.getDataElement("CtrLoanContList"); 
			} catch (Exception e) {}
			if(iColl == null || iColl.size() == 0){
				throw new EMPJDBCException("The values cannot be empty!");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				//获取判断勾选项
				String checkBox =(String)kColl.getDataValue("box");
				if(checkBox.equals("on")){
					String cont_no = (String)kColl.getDataValue("cont_no");
					/** 插入挂起操作表、挂起操作明细表 */
					KeyedCollection hangKColl = dao.queryFirst("PvpHangUp", null, " where cont_no = '"+cont_no+"'", connection);
					String queryContNo = (String)hangKColl.getDataValue("cont_no");
					if(queryContNo != null && queryContNo.trim().length() > 0){
						KeyedCollection upHangKColl = new KeyedCollection("PvpHangUp");
						upHangKColl.addDataField("cont_no", cont_no);
						upHangKColl.addDataField("hang_status", "100");
						dao.update(upHangKColl, connection);
					}else {
						KeyedCollection inHangKColl = new KeyedCollection("PvpHangUp");
						inHangKColl.addDataField("cont_no", cont_no);
						inHangKColl.addDataField("hang_status", "100");
						dao.insert(inHangKColl, connection);
					}
					KeyedCollection inHangDetailKColl = new KeyedCollection("PvpHangUpDetail");
					UNIDGenerator unid = new UNIDGenerator();
					inHangDetailKColl.addDataField("hang_id", unid.getUNID());
					inHangDetailKColl.addDataField("cont_no", cont_no);
					inHangDetailKColl.addDataField("operation", "100");
					inHangDetailKColl.addDataField("input_date", context.getDataValue(CMISConstance.OPENDAY));
					inHangDetailKColl.addDataField("input_id", context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
					inHangDetailKColl.addDataField("input_br_id", context.getDataValue(CMISConstance.ATTR_ORGID));
					dao.insert(inHangDetailKColl, connection);
					
					/*KeyedCollection contkColl = dao.queryDetail(modelId, cont_no, connection);
					contkColl.setDataValue("cont_status","700");//合同状态700为挂起状态  
					dao.update(contkColl, connection);  */
				}
			}
			context.addDataField("flag", "success");
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
