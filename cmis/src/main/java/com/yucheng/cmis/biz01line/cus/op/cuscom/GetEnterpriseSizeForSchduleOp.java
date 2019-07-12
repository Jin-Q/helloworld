package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
 * 定时调度自动更新企业规模
 * @author LIQH
 * 描述：10大型 20中型 30小型 31微型 90其他
 * 
 */
public class GetEnterpriseSizeForSchduleOp extends CMISOperation {
	private final String modelId = "CusCom";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		connection = this.getConnection(context);
		try {
			String conditionStr =" where cus_id in (select cus_id from cus_base where cus_status = '20') ";
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr, connection);
			for(int i=0;i<iColl.size();i++){//循环遍历对公客户信息表，逐个测算企业规模
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String cus_id = (String) kColl.getDataValue("cus_id");
				if(cus_id == null){
					cus_id = "";
				}
				String com_cll_type = (String)kColl.getDataValue("com_cll_type");
				if(com_cll_type == null){
					com_cll_type = "";
                    continue;
				}
				String com_empStr  = (String)kColl.getDataValue("com_employee");
				int com_employee = 0;
				if(com_empStr == null || "".equals(com_empStr)){
					com_employee = 0;
				}else{
					com_employee = Integer.parseInt(com_empStr);
				}
				//调用规则
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ShuffleServiceInterface shuffleService = null;
				try {
					shuffleService = (ShuffleServiceInterface) serviceJndi .getModualServiceById("shuffleServices", "shuffle");
				} catch (Exception e) {
					EMPLog.log("shuffle", EMPLog.ERROR, 0, "getModualServiceById error!", e);
					throw new EMPException(e);
				}
				String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
				context.put("OPENDAY", openDay);
				Map<String,String> inputValueMap = new HashMap<String,String>();
				inputValueMap.put("IN_客户码", cus_id);
				inputValueMap.put("IN_行业编号", com_cll_type);
				inputValueMap.put("IN_从业人数", com_employee+"");
				openDay = (String)context.getDataValue(PUBConstant.OPENDAY);
				inputValueMap.put("IN_OPENDAY", openDay.replaceAll("-", "") );
				Map<String,String> resultMap = new HashMap<String,String>();
				resultMap = shuffleService.fireTargetRule("CUS_CON_SIZE_IDENTY","CUS_CON_SIZE_IDENTY",inputValueMap);
				String com_scale = (String)resultMap.get("OUT_企业规模");
				//根据测算出的企业规模信息更新到对公客户信息表中
				KeyedCollection cuscom = new KeyedCollection(this.modelId);
				cuscom.put("cus_id", cus_id);
				cuscom.put("com_scale", com_scale);
				dao.update(cuscom, connection);
			}
		} catch (Exception e) {
			EMPLog.log("GetEnterpriseSizeForSchduleOp", EMPLog.ERROR, 0, "定时调度自动更新企业规模出错，错误原因："+e.getCause().getMessage());
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
