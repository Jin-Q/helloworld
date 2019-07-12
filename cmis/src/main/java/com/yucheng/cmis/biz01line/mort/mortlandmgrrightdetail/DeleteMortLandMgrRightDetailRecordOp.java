package com.yucheng.cmis.biz01line.mort.mortlandmgrrightdetail;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * @Description:XD150714054 新增土地承包经营权押品类型
 * @author FChengLiang
 * @time:2015-8-19  上午10:07:15
 */
public class DeleteMortLandMgrRightDetailRecordOp extends CMISOperation {

	private final String modelId = "MortLandMgrRightDetail";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
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
