package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpmemberapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusGrpInfoApplyCusGrpMemberApplyListOp extends CMISOperation {
		
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String serno_value = (String) context.getDataValue("CusGrpInfoApply.serno");
			if (serno_value == null) {
				throw new EMPException("parent primary key not found!");
			}

			String sql = "select a.* , b.cus_name ,b.cust_mgr as manager_id ,b.main_br_id as manager_br_id "
					+ " from Cus_Grp_Member_Apply a , cus_base b "
					+ " where a.cus_id = b.cus_id and a.serno = '"
					+ serno_value + "'  order by a.grp_corre_type asc , a.gen_type asc  ";
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			iColl.setName("CusGrpMemberApplyList");

			// 详细信息翻译时调用
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });

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
