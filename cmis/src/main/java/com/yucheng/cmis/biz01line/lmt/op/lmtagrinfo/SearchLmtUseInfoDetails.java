package com.yucheng.cmis.biz01line.lmt.op.lmtagrinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class SearchLmtUseInfoDetails  extends CMISOperation {
	private final String modelId = "CtrLoanCont";
	private final String limit_arg_no = "agr_no";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String agr_no = "";
			String lmt_type = "1";  //授信类型，默认为单一法人授信
			if(context.containsKey(limit_arg_no) && null!=context.getDataValue(limit_arg_no) && !"".equals(context.getDataValue(limit_arg_no))){  //第三方
				lmt_type = "2";
				agr_no = (String)context.getDataValue(limit_arg_no);
			}else if(context.containsKey("limit_code") && null!=context.getDataValue("limit_code") && !"".equals(context.getDataValue("limit_code"))){  //单一法人授信
				lmt_type = "1";
				agr_no = (String)context.getDataValue("limit_code");
			}else if(context.containsKey("agr_no") && null!=context.getDataValue("agr_no") && !"".equals(context.getDataValue("agr_no"))){  //圈商
				lmt_type = "2";
				agr_no = (String)context.getDataValue("agr_no");
			}
			
			try {   
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {} 
			String conditionStr =TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "5000");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			IndexedCollection iCollCont = service.getHistoryContByLimitAccNo(lmt_type, agr_no, pageInfo,conditionStr, context, connection);
			IndexedCollection iCollIqp = service.getIqpByLimitAccNo(lmt_type, agr_no, pageInfo, context, connection);
			
			iCollCont.setName("LmtUseContList");
			iCollIqp.setName("LmtUseIqpList");
			this.putDataElement2Context(iCollCont, context);
			this.putDataElement2Context(iCollIqp, context);
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
