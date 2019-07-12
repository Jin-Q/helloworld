package com.yucheng.cmis.biz01line.iqp.op.riqpactrecrepay;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryRIqpActrecRepayList extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			/**modified by lisj 2015-2-2 需求编号【HS141110017】保理业务改造 begin**/
			String invc_no = (String) context.getDataValue("invc_no");
			String cont_no = (String) context.getDataValue("cont_no");
			String po_no = (String) context.getDataValue("po_no");  //池编号，通过池编号查询池关联回款保证金账户
			String buy_cus_id = (String) context.getDataValue("buy_cus_id");//买方客户编号
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection BADL = dao.queryList("IqpBailaccDetail", " where po_no = '" + po_no + "'", connection);
			IndexedCollection iColl_bail =new IndexedCollection();
			if(BADL!=null && BADL.size()>0){
				for(int i=0;i<BADL.size();i++){
					KeyedCollection temp = (KeyedCollection) BADL.get(i);
					String bail_acc_no = (String)temp.getDataValue("bail_acc_no");
					if(bail_acc_no==null || "".equals(bail_acc_no)){
						throw new EMPException("池["+po_no+"]未找到关联回款保证金账户，请确认！");
					}
				}
			}
			KeyedCollection kColl_ICBDL = new KeyedCollection();
			kColl_ICBDL.addDataField("po_no", po_no);
			kColl_ICBDL.addDataField("buy_cus_id", buy_cus_id);
			iColl_bail  = (IndexedCollection)SqlClient.queryList4IColl("queryIqpCoreBailDetailList", kColl_ICBDL, connection);
			iColl_bail.setName("IqpCoreBailDetailList");
			SInfoUtils.addSOrgName(iColl_bail, new String[] { "organno" });
			/**modified by lisj 2015-2-2 需求编号【HS141110017】保理业务改造 end**/
			KeyedCollection kcoll_param = new KeyedCollection();
			kcoll_param.addDataField("invc_no", invc_no);
			kcoll_param.addDataField("cont_no", cont_no);
			
			IndexedCollection iColl  = SqlClient.queryList4IColl("queryRIqpActrecRepayList", kcoll_param, connection);
			iColl.setName("RIqpActrecRepayList");
			
			SInfoUtils.addSOrgName(iColl, new String[] { "organno" });
			
			this.putDataElement2Context(iColl, context);
			this.putDataElement2Context(iColl_bail, context);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
