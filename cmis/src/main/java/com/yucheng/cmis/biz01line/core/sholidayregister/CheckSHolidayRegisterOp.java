package com.yucheng.cmis.biz01line.core.sholidayregister;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class CheckSHolidayRegisterOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {

		Connection conn = null;
		try {
			conn = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String type = (String)context.getDataValue("type");
			String value = (String)context.getDataValue("value");

			if(type.equals("checkUser")){
      			String sql = "select a.actorno from s_holiday_register a , s_holiday_register b  "
						+ "  where a.actorno = b.actorno and a.rowid < b.rowid and a.status != '02' and b.status != '02' and a.actorno = '"+value+"'   "
						+ "  and ((a.begin_date between b.begin_date and b.plan_end_date) or (a.plan_end_date between b.begin_date and b.plan_end_date)  "
						+ "  or (b.begin_date between a.begin_date and a.plan_end_date) or (b.plan_end_date between a.begin_date and a.plan_end_date))";
      			IndexedCollection iColl = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
				if(iColl.size()!=0){
					context.addDataField("flag","fail");
				}else{
					context.addDataField("flag","success");
				}
			}else if(type.equals("oprant") || type.equals("lose")){
				KeyedCollection kColl = dao.queryAllDetail("SHolidayRegister", value, conn);
				kColl.setDataValue("status", type.equals("oprant")?"01":"02");
				kColl.setDataValue("real_end_date", type.equals("oprant")?"":context.getDataValue("OPENDAY"));
				int count=dao.update(kColl, conn);
				if(count!=1){
					context.addDataField("flag","fail");
				}else{
					context.addDataField("flag","success");
				}
			}
		}catch (EMPException ee) {
			context.addDataField("flag", "failue");
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "failue");
			e.printStackTrace();
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return "0";
	}
	
}