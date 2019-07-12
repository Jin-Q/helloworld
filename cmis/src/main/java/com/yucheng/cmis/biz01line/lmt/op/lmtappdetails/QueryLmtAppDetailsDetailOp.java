package com.yucheng.cmis.biz01line.lmt.op.lmtappdetails;

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
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAppDetailsDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAppDetails";
	private final String agrModelId = "LmtAgrDetails";
	private final String limit_code_name = "limit_code";
	
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
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
			
			SInfoUtils.getPrdPopName(kColl, "prd_id", connection);  //翻译产品
			
			//根据客户码查询客户所属条线
			if(context.containsKey("cus_id")){
				String belg_line = "BL100";  //默认公司
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				CusBase cusBase = service.getCusBaseByCusId(context.getDataValue("cus_id").toString(),context,connection);
				if(null!=cusBase && null!=cusBase.getCusId()){
					belg_line = cusBase.getBelgLine();
				}
				context.addDataField("BelgLine", belg_line);
				
				context.addDataField("action", "updateLmtAppDetailsRecord.do");  //修改的POP
			}
			
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "core_corp_cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
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
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			
			this.putDataElement2Context(kColl, context);
			
			/** 校验为冻结解冻时才处理冻结解冻业务    2013-10-30 唐顺岩   */
			if(context.containsKey("app_type") &&("03".equals(context.getDataValue("app_type")) || "04".equals(context.getDataValue("app_type")))){
				//增加授信台账已冻结金额显示  start 2013-10-29 add by zhaozq
				String org_limit_code = (String) kColl.getDataValue("org_limit_code");
				if(org_limit_code!=null&&!org_limit_code.equals("")){
					KeyedCollection agrKColl = dao.queryDetail(agrModelId, org_limit_code, connection);
					this.putDataElement2Context(agrKColl, context);
				}
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
