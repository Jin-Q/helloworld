package com.yucheng.cmis.biz01line.lmt.op.jointguar.pubbailinfo_joint;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CkeckPubBailInfoOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PubBailInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String serno = null;
			String bail_acct_no = null;
			String cont_no = null;
			String condition = "";
			try {
				serno = (String)context.getDataValue("serno");
				bail_acct_no = context.getDataValue("bail_acct_no").toString().trim();//修复保证金账号空格校验问题
				cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {
				throw new EMPJDBCException("The values cannot be empty!");
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			HashMap<String,String> map = new HashMap<String,String>();
            if(!"".equals(cont_no) && cont_no!=null){
            	map.put("cont_no", cont_no);
    			map.put("bail_acct_no", bail_acct_no);
    			condition = "where cont_no='"+cont_no+"' and bail_acct_no='"+bail_acct_no+"'";
    			IndexedCollection iColl = new IndexedCollection();
    			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
    				condition += " and modify_rel_serno='"+modify_rel_serno+"'";
    				iColl = dao.queryList("PubBailInfoTmp", condition, connection);
    			}else{
    				iColl = dao.queryList(modelId, condition, connection);
    			}
    			
    			if(iColl.size()>0){
					context.addDataField("flag", "error");
				}else{
					context.addDataField("flag", "success");
				}
			}else{
				KeyedCollection kColl =new KeyedCollection();
				if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
					map.put("modify_rel_serno", modify_rel_serno);
					map.put("bail_acct_no", bail_acct_no);
					kColl = dao.queryDetail("PubBailInfoTmp", map, connection);
				}else{
					map.put("serno", serno);
					map.put("bail_acct_no", bail_acct_no);
					kColl = dao.queryDetail(modelId, map, connection);
				}				
				String ckeckSerno = (String)kColl.getDataValue("serno");
				if(!"".equals(ckeckSerno) &&  ckeckSerno!= null){
					context.addDataField("flag", "error");
				}else{
					context.addDataField("flag", "success");
				}
			}
            /**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
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
