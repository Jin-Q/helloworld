package com.yucheng.cmis.biz01line.prd.op.prdcatalog;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class ShowPrdTreeDetailsOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		//上下文转换传递
		String bizline = "";
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			if(context.containsKey("bizline")&&context.getDataValue("bizline")!=null){
				bizline = (String)context.getDataValue("bizline");
				if(bizline.length() == 0){
					TableModelDAO dao = this.getTableModelDAO(context);
					KeyedCollection kcoll = dao.queryDetail("CusBase", (String)context.getDataValue("cus_id"), connection);
					bizline = kcoll.getDataValue("belg_line").toString();
				}
			}else{
				bizline = "BL100,BL200,BL500";
			}
			context.put("bizline", bizline);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
