package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.acc.accPub.AccConstant;
import com.yucheng.cmis.biz01line.acc.component.AccComponent;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.RestrictUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryAccListOp extends CMISOperation {


	private final String modelId = "AccView";  
	private final String modelIdCtr = "CtrLoanCont";  
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String cus_id=null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			/**取得客户码*/
			if(context.containsKey("cus_id")){
				cus_id = (String)context.getDataValue("cus_id");  
			}
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			
			int size = 15;    
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			/**调用担保合同模块接口通过客户码查询担保合同编号*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			GrtServiceInterface serviceGrt = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
	    	IndexedCollection iCollGrtGuarGur = serviceGrt.getGuarContNoByGuarantyteeCusId(cus_id, connection); 
	    	String guar_cont_no_str = "";
	    	//把担保合同编号拼装成一个String 
			for(int i=0;i<iCollGrtGuarGur.size();i++){ 
				KeyedCollection kColl = (KeyedCollection)iCollGrtGuarGur.get(i); 
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
				guar_cont_no_str += "'"+guar_cont_no+"',"; 
			}
			if(guar_cont_no_str.length()>1){
				guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
			}else{
				guar_cont_no_str="''";
			}
			if(conditionStr == null || "".equals(conditionStr)){
				conditionStr = "where cont_no in (select c.cont_no from grt_loan_r_gur b, ctr_loan_cont c where b.cont_no = c.cont_no and b.guar_cont_no in ("+guar_cont_no_str+") and c.CONT_STATUS='200') ";
			}else{
				conditionStr += "and cont_no in (select c.cont_no from grt_loan_r_gur b, ctr_loan_cont c where b.cont_no = c.cont_no and b.guar_cont_no in ("+guar_cont_no_str+") and c.CONT_STATUS='200') ";
			}  

			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iCollAcc = dao.queryList(modelId,null, conditionStr,pageInfo, connection);
	    	
	    	for(int i=0;i<iCollAcc.size();i++){
	    		KeyedCollection kColl = (KeyedCollection)iCollAcc.get(i);
	    		String cont_no = (String)kColl.getDataValue("cont_no");  
	    		KeyedCollection kCollCtr = dao.queryDetail(modelIdCtr, cont_no, connection);
	    		kColl.addDataField("assure_main", kCollCtr.getDataValue("assure_main"));
	    	} 
//	    	/**通过担保合同编号查询台账数据*/
//	    	AccComponent cmisComponent = (AccComponent)CMISComponentFactory.getComponentFactoryInstance()
//			.getComponentInstance(AccConstant.ACCCOMPONENT, context, connection);
//	    	IndexedCollection iColl = cmisComponent.getAcc(iCollAcc, connection); 
			
	    	iCollAcc.setName(iCollAcc.getName()+"List"); 
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iCollAcc, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			this.putDataElement2Context(iCollAcc, context); 
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
