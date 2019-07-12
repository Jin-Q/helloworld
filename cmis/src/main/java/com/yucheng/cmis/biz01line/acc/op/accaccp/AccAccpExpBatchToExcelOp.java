package com.yucheng.cmis.biz01line.acc.op.accaccp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
 * 银承台账生成excel
 *
 */
public class AccAccpExpBatchToExcelOp extends CMISOperation {

	private final String modelId = "AccAccp";
	
public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
				
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,connection);
			iColl.setName(iColl.getName() + "List");
			
			/**添加客户条线字段  2014-08-07   邓亚辉*/
			String[] args=new String[] { "daorg_cusid","daorg_cusid","cont_no","prd_id"};
			String[] modelIds=new String[]{"CusBase","CusBase","CtrLoanCont","PrdBasicinfo"};
			String[] modelForeign=new String[]{"cus_id","cus_id","cont_no","prdid"};
			String[] fieldName=new String[]{"cus_name","belg_line","serno","prdname"};
			String[] resultName = new String[] { "daorg_cusid_displayname","belg_line","fount_serno","prd_id_displayname"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
			this.putDataElement2Context(iColl, context);
			
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
