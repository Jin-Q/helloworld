package com.yucheng.cmis.biz01line.cus.op.custrusteelst;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.ccr.dao.CcrAppInfoDao;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteCusTrusteeLstRecordOp extends CMISOperation {

	private final String modelId = "CusTrusteeLst";
	

	private final String serno_name = "serno";
	private final String cus_id_name = "cus_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
//		String flag = null;
		try{
			connection = this.getConnection(context);



			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			String scopeValue  = null;
            try {
                scopeValue  = (String)context.getDataValue("scope");
            } catch (Exception e) {}
            
            if("2".equals(scopeValue))
            {
                // 根据流水号删除全部托管客户。因为TableModelDAO接口不支持根据其中的一个主键删除数据，所以手工写sql语句删除
                CcrAppInfoDao dao = new CcrAppInfoDao();
                // 借用CcrAppInfoDao中的deleteScoreBySerno这个方法.删掉CUS_TRUSTEE_LST表中的该客户数据.
                dao.deleteScoreBySerno("CUS_TRUSTEE_LST", serno_value,
                        connection);
            }
            else
            {
			
                String cus_id_value = null;
                try
                {
                    cus_id_value = (String) context.getDataValue(cus_id_name);
                }
                catch (Exception e)
                {
                }
                if (cus_id_value == null || cus_id_value.length() == 0)
                    throw new EMPJDBCException("The value of pk[" + cus_id_name
                            + "] cannot be null!");

                TableModelDAO dao = this.getTableModelDAO(context);
                Map<String, String> pkMap = new HashMap<String, String>();
                pkMap.put("serno", serno_value);
                pkMap.put("cus_id", cus_id_value);
                int count = dao.deleteByPks(modelId, pkMap, connection);
                if (count != 1)
                {
                    throw new EMPException("Remove Failed! Records :" + count);
                }
            }			
			//flag = "删除成功";
		}catch (EMPException ee) {
			//flag = "删除失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		//context.addDataField("flag",flag);
		return "0";
	}
}
