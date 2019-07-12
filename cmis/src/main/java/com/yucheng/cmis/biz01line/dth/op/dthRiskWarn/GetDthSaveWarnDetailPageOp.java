package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetDthSaveWarnDetailPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
			String value = context.getDataValue("value").toString();
			String values[] = value.split(",");	//拆分成manager_br_id和line_enname
			String sql = "select * from (  "
					+ "  select  a.cus_id,b.cus_name,'一般授信' as lmt_type ,a.crd_totl_amt,a.crd_cir_amt,a.crd_one_amt,b.cust_mgr, a.manager_br_id   "
					+ "  from lmt_apply a ,cus_base b where a.approve_status not in ('000','997','998') and a.cus_id = b.cus_id  union   "
					+ "  select l.cus_id,c.cus_name , (select cnname from s_dic where opttype = 'STD_ZB_COOP_TYPE' and enname = l.coop_type)lmt_type ,  "
					+ "  l.lmt_totl_amt,l.lmt_totl_amt,0,c.cust_mgr,l.manager_br_id  "
					+ "  from LMT_APP_JOINT_COOP l , cus_base c where l.approve_status not in ('000','997','998') and c.cus_id = l.cus_id   "
					+ "  )s where s.manager_br_id = '"+values[0]+"' and s.cus_id in (select cus_id from cus_base where belg_line = '"+values[1]+"')";
						
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);			
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
			iColl.setName("SaveWarnDetail");
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}

}