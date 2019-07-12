package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class CheckGrtGuarContOp extends CMISOperation {
	
	private final String modelId = "CtrLoanCont";
	private final String modelIdGrt = "GrtLoanRGur";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cont_no = null;
			String guar_cont_no_res = "";
			int res = 0;
			try {
				 cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {}
			if(cont_no == null || "".equals(cont_no))
				throw new EMPJDBCException("The values cont_no cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String condition = "where cont_no='"+cont_no+"'";
			IndexedCollection iCollGrt = dao.queryList(modelIdGrt, condition, connection);
			//把担保合同编号拼装成一个String
			String guar_cont_no_str ="";
			for(int i=0;i<iCollGrt.size();i++){
				KeyedCollection kColl = (KeyedCollection)iCollGrt.get(i);
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
				if(i==(iCollGrt.size()-1)){
					guar_cont_no_str += "'"+guar_cont_no+"'";
				}else{
					guar_cont_no_str += "'"+guar_cont_no+"',";
				}
			}
			if(!"".equals(guar_cont_no_str)){
				/**调用担保模块接口*/ 
				int size = 200;
				PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				IndexedCollection ContiColl = service.getGuarContInfoList(guar_cont_no_str, pageInfo, this.getDataSource(context));		
	            for(int i=0;i<ContiColl.size();i++){
	            	KeyedCollection kCollGrt = (KeyedCollection)ContiColl.get(i);
	            	String guar_cont_state = (String)kCollGrt.getDataValue("guar_cont_state");
	            	String guar_cont_type = (String)kCollGrt.getDataValue("guar_cont_type");
	            	String guar_cont_no = (String)kCollGrt.getDataValue("guar_cont_no");
	            	if("00".equals(guar_cont_type)){
	            		if( !"01".equals(guar_cont_state)){
	                		res +=1;//如果一般担保合同有非有效状态,记录加1
	                		guar_cont_no_res += "'"+guar_cont_no+"',";
	                	}
	            	}
	            }
			}
            if(res>0){
            	context.addDataField("flag", "error");
            	context.addDataField("mes", guar_cont_no_res);
            }else{
            	context.addDataField("flag", "success");
            	context.addDataField("mes", guar_cont_no_res);
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
