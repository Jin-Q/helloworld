package com.yucheng.cmis.biz01line.lmt.op.lmtappdetails;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryAdjustDistinctionDetailOp  extends CMISOperation {
	
	private final String modelIdApp = "LmtAppDetails";
	
	private final String modelIdAgr = "LmtAgrDetails";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//查询申请表中的数据
			String limit_code = (String) context.getDataValue("limit_code");
			KeyedCollection kcoll = dao.queryDetail(modelIdApp, limit_code, connection);
			SInfoUtils.getPrdPopName(kcoll, "limit_name", connection);  //翻译额度品种名称
			SInfoUtils.getPrdPopName(kcoll, "prd_id", connection);  //翻译产品
			
			this.putDataElement2Context(kcoll, context);
			
			//查询历史表中的数据
			List<String> list4Agr = new ArrayList<String>();
			list4Agr.add("limit_code");
			list4Agr.add("limit_name");
			list4Agr.add("sub_type");
			list4Agr.add("cur_type");
			list4Agr.add("crd_amt");
			list4Agr.add("limit_type");
			list4Agr.add("prd_id");
			list4Agr.add("is_pre_crd");
			list4Agr.add("guar_type");
			list4Agr.add("term_type");
			list4Agr.add("term");
			String org_limit_code = (String) kcoll.getDataValue("org_limit_code");
			String condition4Agr = "where limit_code='"+org_limit_code+"'";
			KeyedCollection kcollTemp = dao.queryFirst(modelIdAgr, list4Agr, condition4Agr, connection);
			
			SInfoUtils.getPrdPopName(kcollTemp, "limit_name", connection);  //翻译额度品种名称
			SInfoUtils.getPrdPopName(kcollTemp, "prd_id", connection);  //翻译产品
			this.putDataElement2Context(kcollTemp, context);
			
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