package com.yucheng.cmis.platform.organization.steammem.op;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteSTeamMemRecordOp extends CMISOperation {

	private final String modelId = "STeamMem";
	


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String mem_no = "";
		String memNo;
		int count = 0;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			mem_no = (String) context.getDataValue("mem_no");
			//批量删除
			String Mem[] = mem_no.split(",");
			for(int i=0;i<Mem.length;i++){
				memNo = Mem[i];
				count=dao.deleteByPk(modelId, memNo, connection);
			}
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag","success");
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
