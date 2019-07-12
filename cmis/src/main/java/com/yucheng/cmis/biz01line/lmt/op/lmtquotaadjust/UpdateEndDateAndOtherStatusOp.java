package com.yucheng.cmis.biz01line.lmt.op.lmtquotaadjust;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;

public class UpdateEndDateAndOtherStatusOp extends CMISOperation {
	
	private final String modelId = "LmtQuotaAdjust";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			//1.获取到期日期
			String end_date = (String) context.getDataValue("end_date");
			//2.获取其他的日期  
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String Agr_No = (String) context.getDataValue("Agr_No");
			String condition = "where Fin_Agr_No='"+Agr_No+"'";
			
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			KeyedCollection kColl = (KeyedCollection)iColl.getDataElement().clone();
			String inure_date = (String) kColl.getDataValue("inure_date");
			
			//3.逻辑  1）.把到期日期 跟 其他日期比较
			TimeUtil.checkDate1BeforeDate2(end_date,inure_date);

			
			//2）.如果 获得的到期日期比 一条记录中的  开始日期  到期日期 只要有一个 小 则 未生效
			
			
			
			//3).全部大 则 生效
			
			
			
			context.addDataField("flag", "success");
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
