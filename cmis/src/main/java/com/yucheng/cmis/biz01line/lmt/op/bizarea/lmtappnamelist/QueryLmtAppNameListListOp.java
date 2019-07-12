package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappnamelist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAppNameListListOp extends CMISOperation {

	private final String modelId = "LmtAppNameList";
	private final String modelIdAgr = "LmtAgrBizArea";
	private final String modelIdNameList = "LmtNameList";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnFlag = "";
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			String app_flag = null;
			String serno = "";
			String agr_no = null;
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			try {
				app_flag = (String)context.getDataValue("app_flag"); //0 : 入圈  1:退圈
			} catch (Exception e) {}
			try{
				serno = (String) context.getDataValue("serno"); //申请编号
				agr_no = (String)context.getDataValue("agr_no");  //协议编号
			} catch (Exception e) {
	            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "QueryLmtNameListListOp get serno error!");
			}
			if(app_flag==null||"".equals(app_flag)){
				throw new Exception("申请标识[app_flag]为空！");
			}
			if("1".equals(app_flag)){//退圈时查询圈商现有成员
				String condition = " where agr_no='"+agr_no+"' and cus_status='1'";
				IndexedCollection iColl = dao.queryList(modelIdNameList, condition, connection);
				iColl.setName(modelIdNameList+"List");
				String[] args=new String[] { "cus_id" };
				String[] modelIds=new String[]{"CusBase"};
				String[] modelForeign=new String[]{"cus_id"};
				String[] fieldName=new String[]{"cus_name"};
				//详细信息翻译时调用			
				SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
				this.putDataElement2Context(iColl, context);
				returnFlag = "back";
			}else{
				returnFlag = "join";
			}
			//取圈商协议信息放入context中，做后续校验
			if(!context.containsKey("single_max_amt")){
				KeyedCollection kCollAgr = dao.queryDetail(modelIdAgr, agr_no, connection);
				context.addDataField("single_max_amt", kCollAgr.getDataValue("single_max_amt"));
				context.addDataField("end_date", kCollAgr.getDataValue("end_date"));
				context.addDataField("biz_area_type", kCollAgr.getDataValue("biz_area_type"));
			}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if( "".equals(conditionStr )){
				conditionStr = " where serno = '" + serno + "'" + "order by serno desc";
			}else{
				conditionStr += " and serno = '" + serno + "'" + "order by serno desc";
			}
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			
			//得到客户码   从而获取客户名称,客户类别
			KeyedCollection kColl = null;
			String cus_id = null;
			//调用客户模块接口
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase cusBase = null;
			for(int i=0; i<iColl.size(); i++){
				kColl = (KeyedCollection)iColl.get(i);
				cus_id = (String) kColl.getDataValue("cus_id");
				cusBase = service.getCusBaseByCusId(cus_id, context, connection);
				kColl.addDataField("cus_name", cusBase.getCusName());
				kColl.addDataField("cus_type", cusBase.getCusType());
				iColl.remove(i);
				iColl.add(i, kColl);
			}
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnFlag;
	}
}
