package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpbconcoopagr;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpBconCoopAgrDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpBconCoopAgr";
	

	private final String coop_agr_no_name = "coop_agr_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);	
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}		
			String coop_agr_no_value = null;
			try {
				coop_agr_no_value = (String)context.getDataValue(coop_agr_no_name);
			} catch (Exception e) {}
			if(coop_agr_no_value == null || coop_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+coop_agr_no_name+"] cannot be null!");

		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, coop_agr_no_value, connection);
			String psale_cont=(String)kColl.getDataValue("psale_cont");//购销合同
			KeyedCollection PCkColl=dao.queryAllDetail("IqpPsaleCont", psale_cont, connection);
			String mem_cus_id=(String)kColl.getDataValue("borrow_cus_id");//借款人客户号
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();	
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
			KeyedCollection lmtkColl=lmtservice.getLmtAgrInfoMsg(mem_cus_id, "05", connection);//通过客户码获取供应链协议信息
			String agr_no="";//获取授信协议编号
			if(lmtkColl.containsKey("agr_no"))
			{
				agr_no=(String)lmtkColl.getDataValue("agr_no");
			}	
			kColl.addDataField("lmt_agr_no", agr_no);//设置借款人客户码授信协议号
			String[] args=new String[] { "borrow_cus_id","manuf_cus_id","consign_cus_id"};
			String[] modelIds=new String[]{"CusBase","CusBase","CusBase"};
			String[] modelForeign=new String[]{"cus_id","cus_id","cus_id"};
			String[] fieldName=new String[]{"cus_name","cus_name","cus_name"};
			//详细信息翻译时调用			
            SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
            Map<String,String> map = new HashMap<String,String>();
            map.put("consign_addr", "STD_GB_AREA_ALL");//行政区划名称
            CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(PCkColl, context);//用于展示合同金额、开始日、到期日！
			
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
