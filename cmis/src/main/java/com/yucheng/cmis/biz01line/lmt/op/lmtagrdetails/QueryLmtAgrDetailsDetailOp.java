package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;


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
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAgrDetailsDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAgrDetails";

	private final String limit_code_name = "limit_code";
	
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnStr = "accPage";
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String limit_code_value = null;
			try {
				limit_code_value = (String)context.getDataValue(limit_code_name);
			} catch (Exception e) {}
			if(limit_code_value == null || limit_code_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+limit_code_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, limit_code_value, connection);
			
			String[] args=new String[] { "cus_id","core_corp_cus_id" };
			String[] modelIds=new String[]{"CusBase","CusBase"};
			String[] modelForeign=new String[]{"cus_id","cus_id"};
			String[] fieldName=new String[]{"cus_name","cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.getPrdPopName(kColl, "prd_id", connection);  //翻译产品
			
			//根据客户码查询客户所属条线--用于变更时选择产品及保存时判断
			String belg_line = "BL100";  //默认公司
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase cusBase = service.getCusBaseByCusId(kColl.getDataValue("cus_id").toString(),context,connection);
			if(null!=cusBase && null!=cusBase.getCusId()){
				belg_line = cusBase.getBelgLine();
			}
			context.addDataField("belg_line", belg_line);
			
			String crd_amt = kColl.getDataValue("crd_amt").toString();
			String limit_code = kColl.getDataValue("limit_code").toString();
			
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo(limit_code, "01", connection, context);
			String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
			double bal_amt = Double.parseDouble(crd_amt) - Double.parseDouble(lmt_amt);
			kColl.addDataField("bal_amt", bal_amt);
			
			/**翻译额度名称**/
			args=new String[] { "limit_name" };
			modelIds=new String[]{"PrdBasicinfo"};
			modelForeign=new String[]{"prdid"};
			fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			//翻译绿色信贷
			Map<String,String> map = new HashMap<String,String>();
			map.put("green_indus","STD_ZB_GREEN_INDUS");
			//树形菜单服务
			CMISTreeDicService service_tree = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service_tree);
			
			this.putDataElement2Context(kColl, context);
			//如果为单一法人或个人授信变更申请查看原有授信台账
			if(context.containsKey("type") && ("app".equalsIgnoreCase(context.getDataValue("type").toString()) || "indivApp".equalsIgnoreCase(context.getDataValue("type").toString()))){ 
				returnStr = "appPage";
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnStr;
	}
}
