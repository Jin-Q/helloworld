package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class GrtGuarListOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
            String limit_acc_no="";
            String limit_credit_no="";
            String guarType = "";
            String guar_cont_no_str = "";
            String guar_cont_no_credit_str = "";
            IndexedCollection ContiColl=new IndexedCollection();
            IndexedCollection ContiCollCredit=new IndexedCollection();
            IndexedCollection lmtIc=new IndexedCollection();
            IndexedCollection lmtCreditIc=new IndexedCollection();
            IndexedCollection iColl=new IndexedCollection("GrtGuarContList");
			try {
				limit_acc_no = (String)context.getDataValue("limit_acc_no");    
				limit_credit_no = (String)context.getDataValue("limit_credit_no");
				guarType = (String)context.getDataValue("guar_cont_type");     
			} catch (Exception e) {
				throw new Exception("数据异常，请联系后台管理员！");
			}  
			 
			if(limit_acc_no == null || "".equals(limit_acc_no)){
				limit_acc_no = "null";
			}
			if(limit_credit_no == null || "".equals(limit_credit_no)){
				limit_credit_no = "null";
			} 
			
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			int size = 100;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtService = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			lmtIc = lmtService.searchGuarContByLimitCode(limit_acc_no,pageInfo, dataSource);
			lmtCreditIc = lmtService.searchGuarContByLimitCode(limit_credit_no,pageInfo, dataSource);
			for(int i=0;i<lmtIc.size();i++){
				KeyedCollection kColl = (KeyedCollection)lmtIc.get(i);
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
				if(i < lmtIc.size()-1){
					guar_cont_no_str += "'"+guar_cont_no+"',";
				}else{
					guar_cont_no_str += "'"+guar_cont_no+"'";
				}
			}
			for(int i=0;i<lmtCreditIc.size();i++){
				KeyedCollection kColl = (KeyedCollection)lmtCreditIc.get(i);
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
				if(i < lmtCreditIc.size()-1){
					guar_cont_no_credit_str += "'"+guar_cont_no+"',";
				}else{
					guar_cont_no_credit_str += "'"+guar_cont_no+"'";
				}
			}
			
			
			
			
		    /**调用担保模块接口*/
		    if(!"".equals(guar_cont_no_str) && guar_cont_no_str != null){
		    	GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				ContiColl = service.getGuarContInfoList(guar_cont_no_str, pageInfo, this.getDataSource(context));
		    }
		    if(!"".equals(guar_cont_no_credit_str) && guar_cont_no_credit_str != null){
		    	GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
		    	ContiCollCredit = service.getGuarContInfoList(guar_cont_no_credit_str, pageInfo, this.getDataSource(context));
		    }
		    for(int i=0;i<ContiColl.size();i++){
		    	KeyedCollection kColl = (KeyedCollection)ContiColl.get(i);  
		    	String guar_cont_type = (String)kColl.getDataValue("guar_cont_type");
		    	String guar_cont_state = (String)kColl.getDataValue("guar_cont_state");
		    	if(guarType.equals(guar_cont_type) && "01".equals(guar_cont_state)){
		    		iColl.addDataElement(kColl);
		    	}
		    }
		    for(int i=0;i<ContiCollCredit.size();i++){
		    	KeyedCollection kColl = (KeyedCollection)ContiCollCredit.get(i);  
		    	String guar_cont_type = (String)kColl.getDataValue("guar_cont_type");
		    	String guar_cont_state = (String)kColl.getDataValue("guar_cont_state");
		    	if(guarType.equals(guar_cont_type) && "01".equals(guar_cont_state)){
		    		iColl.addDataElement(kColl);
		    	}
		    }
		    
			this.putDataElement2Context(iColl, context);

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
