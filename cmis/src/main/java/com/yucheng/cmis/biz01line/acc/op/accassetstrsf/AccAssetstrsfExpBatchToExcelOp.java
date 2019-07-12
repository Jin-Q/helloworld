package com.yucheng.cmis.biz01line.acc.op.accassetstrsf;

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
 * 资产转让台账生成excel
 *
 */
public class AccAssetstrsfExpBatchToExcelOp extends CMISOperation {
	
	private final String modelId = "AccAssetstrsf";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by bill_no desc";
			
		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			
			String[] args=new String[] {"cus_id","cont_no" ,"cont_no"};
			String[] modelIds=new String[]{"CusBase","CtrAssetstrsfCont","CtrAssetstrsfCont"};
			String[]modelForeign=new String[]{"cus_id","cont_no","cont_no"};
			String[] fieldName=new String[]{"cus_name","prd_id","serno"};
			String[] resultName=new String[]{"cus_id_displayname","prd_id","fount_serno"};
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
			
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);

			/** 组织机构、登记机构翻译 */
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
			
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
