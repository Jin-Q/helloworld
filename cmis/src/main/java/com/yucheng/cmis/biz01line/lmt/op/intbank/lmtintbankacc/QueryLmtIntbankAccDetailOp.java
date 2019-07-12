package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankacc;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtIntbankAccDetailOp extends CMISOperation {

	private final String modelId = "LmtIntbankAcc";
	private final String modelIdDetail= "LmtIntbankDetail";

	private final String agrno_name = "agr_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String agrno_value = null;
			try {
				agrno_value = (String)context.getDataValue(agrno_name);
			} catch (Exception e) {}
			if(agrno_value == null || agrno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+agrno_value+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, agrno_value, connection);
			//查询授信占用额度
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo(agrno_value, "02", connection, context);
			BigDecimal lmt_amt = BigDecimalUtil.replaceNull(kCollTemp.getDataValue("lmt_amt"));
			BigDecimal bal_amt = (BigDecimalUtil.replaceNull(kColl.getDataValue("lmt_amt")).subtract(lmt_amt)).subtract(BigDecimalUtil.replaceNull(kColl.getDataValue("froze_amt"))); 
			kColl.addDataField("bal_amt", bal_amt);
			
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			KeyedCollection kColl_cus =service.getCusSameOrgKcoll(kColl.getDataValue("cus_id").toString(), context, connection);
			String same_org_cnname = (String)kColl_cus.getDataValue("same_org_cnname");
			kColl.addDataField("same_org_cnname", same_org_cnname);
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"manager_id"});
			
			this.putDataElement2Context(kColl, context);			
			//对分项进行分页,取自授信使用明细数据
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelIdDetail);
			} catch (Exception e) {}
			String cus_id=(String)context.getDataValue("cus_id");
			String conditionStr = TableModelUtil.getQueryCondition(this.modelIdDetail, queryData, context, false, false, false);	
			conditionStr =conditionStr + "where cus_id='"+cus_id+"'"+"order by cus_id desc";
            int size = 10;

            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            IndexedCollection iColl = dao.queryList(modelIdDetail,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] { "cus_id" };
		    String[] modelIds=new String[]{"CusSameOrg"};
		    String[] modelForeign=new String[]{"cus_id"};
		    String[] fieldName=new String[]{"same_org_cnname"};
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
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
		return "0";
	}	
}
