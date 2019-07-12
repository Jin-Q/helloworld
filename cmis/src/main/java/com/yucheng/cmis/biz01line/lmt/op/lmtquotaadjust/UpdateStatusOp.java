package com.yucheng.cmis.biz01line.lmt.op.lmtquotaadjust;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;

public class UpdateStatusOp extends CMISOperation {


	private final String modelId = "LmtQuotaAdjustApp";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = null;
			KeyedCollection kCollTemp = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			String openday = context.getDataValue("OPENDAY").toString();
			TableModelDAO dao = this.getTableModelDAO(context);
			//获得当前选中的 对象
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String fin_agr_no = (String)kColl.getDataValue("fin_agr_no");
			String fix_inure_date = (String)kColl.getDataValue("inure_date");
			String fix_end_date = (String)kColl.getDataValue("end_date");
			//获取当前融资协议编号下最大的生效的到期日
			String conditionStr = "where fin_agr_no= '"+fin_agr_no+"' AND STATUS in ('1','3') and approve_status in ('000','111','992','993') order by end_date desc";
			kCollTemp = dao.queryFirst(modelId, null, conditionStr, connection);			
			if(kCollTemp!=null&&kCollTemp.size()>0&&kCollTemp.getDataValue("end_date")!=null){
				String end_date = kCollTemp.getDataValue("end_date").toString();
				String new_input_date = LmtUtils.computeEndDate(end_date, "003", "1");
				if(new_input_date.equals(fix_inure_date)){
					kColl.setDataValue("status", "3"); //置为有效
					dao.update(kColl, connection);  //更新
					context.addDataField("flag", "success");
				}else{
					context.addDataField("flag", "生效日期应为当前融资协议编号下最大的生效/待生效到期日的下一天！");
				}
			}else if(TimeUtil.checkDate1BeforeDate2(fix_end_date, openday)){
				context.addDataField("flag", "到期日期应大于当前营业日期！");
			}else{
				kColl.setDataValue("status", "3"); //置为有效
				dao.update(kColl, connection);  //更新
				context.addDataField("flag", "success");
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
