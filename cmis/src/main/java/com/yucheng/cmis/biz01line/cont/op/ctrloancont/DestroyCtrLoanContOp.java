package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cont.component.IqpAppendTermsComponent;
import com.yucheng.cmis.biz01line.cont.pub.ContConstant;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DestroyCtrLoanContOp extends CMISOperation {
	private final String modelId = "CtrLoanCont";
	private final String modelIdGrtLoan = "GrtLoanRGur";
	private final String modelIdPvp = "PvpLoanApp";
	private final String modelIdAcc = "AccView";
	private final String modelIdAuthorize = "PvpAuthorize";

	private final String cont_no_name = "cont_no";
	
    
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cont_no_value = null;
			String guar_cont_no_str = "";
			String accStatus = null;
			String PvpAuthStatus = null;
			String bill_no = "";
			String bill_no_authorize = "";
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			DataSource datasource =(DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			 //是否出账
			/** modified by yangzy 2015/07/09 业务合同注销，判断在途业务增加【追回】状态 start **/
			 String conditionStr = "where cont_no='"+cont_no_value+"' and approve_status in('000','111','991','992','993')";//待发起，审批中，重办，打回，追回
			/** modified by yangzy 2015/07/09 业务合同注销，判断在途业务增加【追回】状态 start **/
			 IndexedCollection iCollPvp = dao.queryList(modelIdPvp, conditionStr, connection);
	         
	         
	         /**业务是否结清,查询台账视图,查询是否有在途出账
	          * 如果accStatus = "error" 则有业务未结清
	          * */
	         String conditionAccView = "where cont_no='"+cont_no_value+"'";
	         IndexedCollection iCollAcc = dao.queryList(modelIdAcc, conditionAccView, connection);
	         for(int i=0;i<iCollAcc.size();i++){
	        	 KeyedCollection kColl = (KeyedCollection)iCollAcc.get(i);
	        	 String status = (String)kColl.getDataValue("status");
	        	 /**  添加注销台账状态为8的合同     2014-08-19  王青 	 */
	        	 if(!"10".equals(status) && !"9".equals(status) && !"8".equals(status)){
	        		accStatus = "error";
	        		String bill = (String)kColl.getDataValue("bill_no");
	        		if(i < iCollAcc.size()-1){
	        			 bill_no += "'"+bill+"',";
	 				}else{
	 					bill_no += "'"+bill+"'";
	 				}
	        	 }
	         }
	         //判断授权是否未发送
	         //'00':'未授权', '01':'授权失败', '02':'已授权', '03':'授权已撤销', '04':'授权已确认', '05':'等待通知'
	         IndexedCollection iCollAuthorize = dao.queryList(modelIdAuthorize, conditionAccView, connection);
	         for(int i=0;i<iCollAuthorize.size();i++){
	        	 KeyedCollection kColl = (KeyedCollection)iCollAuthorize.get(i);
	        	 String status = (String)kColl.getDataValue("status");
	        	 if(!"03".equals(status) && !"04".equals(status)){
	        		PvpAuthStatus = "error";
	        		String bill = (String)kColl.getDataValue("bill_no");
	        		if(i < iCollAuthorize.size()-1){
	        			 bill_no_authorize += "'"+bill+"',";
	 				}else{
	 					bill_no_authorize += "'"+bill+"'";
	 				}
	        	 }
	         }
	         //判断条件1.如果存在在途的出账,不让注销
	         if(iCollPvp.size()>0 && iCollPvp != null){
	        	 context.put("flag", "Pvperror");
	        	 context.put("billNo", bill_no);
	        	 return "0";
	         }
	         //判断条件2.如果存在有效的台账不让注销
	         if(iCollAcc.size()>0 && iCollAcc != null && "error".equals(accStatus)){
	        	 context.put("flag", "accStatusError");
	        	 context.put("billNo", bill_no);
	        	 return "0";
	         }
	         //判断条件3.如果存在授权未发送
	         if("error".equals(PvpAuthStatus)){
	        	 context.put("flag", "pvpAuthorizeStatusError");
	        	 context.put("billNo", bill_no_authorize);
	        	 return "0";
	         }
	         /**-----------------------------------------------------------------------------*/
     	        String condition = "where cont_no='"+cont_no_value+"'";
				IndexedCollection iColl = dao.queryList(modelIdGrtLoan, condition, connection);
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
					guar_cont_no_str += "'"+guar_cont_no+"',";
				}
				if(guar_cont_no_str.length()>1){
					guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
					/**调用担保模块接口,跟新担保合同状态*/ 
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				    service.updateGrtGuarContSta(guar_cont_no_str,datasource, connection);
				}
				/**-----------------------------------------------------------------------------*/
				//更新业务担保合同状态
				IqpAppendTermsComponent cmisComponent = (IqpAppendTermsComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(ContConstant.IQPAPPENDTREMSCCMPONTENT, context, connection);
				cmisComponent.updateGrtLoanRGur(cont_no_value,datasource, connection);
				/**-----------------------------------------------------------------------------*/
				//跟新合同状态
				KeyedCollection kCollCont = dao.queryDetail(modelId, cont_no_value, connection);
				String cont_end_date = (String)kCollCont.getDataValue("cont_end_date");
				String open_day = (String)context.getDataValue("OPENDAY");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date contEndDate = sdf.parse(cont_end_date);
				Date openDay = sdf.parse(open_day);
				if(contEndDate.after(openDay)){
					kCollCont.put("cont_status", "500");//撤销操作把合同状态改为中止
					context.put("flag", "stopSuccess");
				}else{
					kCollCont.put("cont_status", "600");//撤销操作把合同状态改为注销
					context.put("flag", "success");
				}
				dao.update(kCollCont, connection);
				
				
				//增加贴现处理
				String prd_id = (String) kCollCont.getDataValue("prd_id");
				String serno = (String) kCollCont.getDataValue("serno");
				KeyedCollection kCollForBatch = null;
				if(prd_id.equals("300021") || prd_id.equals("300020")){//判断是否贴现业务
					String conditionstr = " where serno='"+serno+"'";
					kCollForBatch=dao.queryFirst("IqpBatchMng", null, conditionstr, connection);
					if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
						kCollForBatch.setDataValue("status", "03");//改回【已办结】状态
						dao.update(kCollForBatch, connection);
					}
				}
				/**-----------------------------------------------------------------------------*/
	            context.addDataField("billNo", bill_no);
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
