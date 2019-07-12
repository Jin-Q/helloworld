package com.yucheng.cmis.biz01line.esb.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.msiimple.ESBServiceInterfaceImple;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * <p>处理逻辑：</p>
 * <p>生成还款方案</p>
 */
public class CreateRepayPlanOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection iqpkColl = new KeyedCollection();
		IndexedCollection bodyIColl = null;
		//String serno = "";
		String apply_amount = "";
		String repay_date = "";
		String apply_date ="";
		String termType = "";
		String term = "";
		String repay_type = "";
		String loan_type = "";
		String reality_ir_y = "";
		String repay_term = "";
		String repay_space = "";
		String is_term = "";
		String interest_term="";
		String prd_id="";
		context.addDataField("RET_CODE", "");
		context.addDataField("RET_MSG", "");
		String CONTRACT_EXPIRY_DATE = "";
		String repay_mode_type = "";
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/**判断是否是翻页*/
			if(context.containsKey("CONTRACT_EXPIRY_DATE")){
				  //serno = (String)context.getDataValue("serno");
				  apply_amount = (String)context.getDataValue("apply_amount");
				  repay_date = (String)context.getDataValue("repay_date");
				  apply_date = (String)context.getDataValue("apply_date");
				  CONTRACT_EXPIRY_DATE = (String)context.getDataValue("CONTRACT_EXPIRY_DATE");
				  loan_type = (String)context.getDataValue("loan_type");
				  reality_ir_y = (String)context.getDataValue("reality_ir_y");
				  repay_term = (String)context.getDataValue("repay_term");
				  repay_space = (String)context.getDataValue("repay_space");
				  repay_type = (String)context.getDataValue("repay_type");
				  repay_mode_type = (String)context.getDataValue("repay_mode_type");
				  is_term = (String)context.getDataValue("is_term");
				  interest_term = (String)context.getDataValue("interest_term");
				  prd_id = (String)context.getDataValue("prd_id");
			}else{
			     // serno = (String)context.getDataValue("serno");
			      apply_amount = (String)context.getDataValue("apply_amount");
			      repay_date = (String)context.getDataValue("repay_date");
			      apply_date =(String)context.getDataValue("apply_date");
			      termType = (String)context.getDataValue("term_type");
			      term = (String)context.getDataValue("apply_term");
			      repay_type = (String)context.getDataValue("repay_type");
			      loan_type = (String)context.getDataValue("loan_type");
			      reality_ir_y = (String)context.getDataValue("reality_ir_y");
			      repay_term = (String)context.getDataValue("repay_term");
			      repay_space = (String)context.getDataValue("repay_space");
			      is_term = (String)context.getDataValue("is_term");
			      interest_term = (String)context.getDataValue("interest_term");
			      prd_id = (String)context.getDataValue("prd_id");
			
			      /**计算合同到期日*/
			      String type = "";
			      if("001".equals(termType)){
			    	  type = "Y";
			      }else if("002".equals(termType)){
			    	  type = "M";
			      }else if("003".equals(termType)){
			    	  type = "D";
			      }
			      CONTRACT_EXPIRY_DATE = DateUtils.getAddDate(type, apply_date, Integer.parseInt(term));//合同到期日
			
			
			      /**取还款方式种类*/
			      KeyedCollection RepayKColl = dao.queryDetail("PrdRepayMode", repay_type, connection);
			      repay_mode_type = (String)RepayKColl.getDataValue("repay_mode_type");//还款方式种类
			}
			/**封装接口需要的数据*/
			//iqpkColl.addDataField("serno", serno); 
			iqpkColl.addDataField("apply_amount", apply_amount);//贷款金额
			iqpkColl.addDataField("repay_date", repay_date);
			iqpkColl.addDataField("apply_date", apply_date);//起始日（发放日）
			iqpkColl.addDataField("CONTRACT_EXPIRY_DATE", CONTRACT_EXPIRY_DATE);//结束日（到期日）
			iqpkColl.addDataField("loan_type", loan_type);
			iqpkColl.addDataField("reality_ir_y", reality_ir_y);//执行利率
			iqpkColl.addDataField("repay_term", repay_term);
			iqpkColl.addDataField("repay_space", repay_space);
			iqpkColl.addDataField("repay_type", repay_type);//还款方式
			iqpkColl.addDataField("repay_mode_type", repay_mode_type);
			iqpkColl.addDataField("is_term", is_term);
			iqpkColl.addDataField("interest_term", interest_term);//结息频率
			iqpkColl.addDataField("prd_id", prd_id);
			iqpkColl.setName("iqpLoanApp"); 
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			bodyIColl = getRepayPlanList(iqpkColl,dao,context,connection,pageInfo);
			
			//页面展示集合
			IndexedCollection iCollForPage = new IndexedCollection();//分页时，展示在页面上的记录集合
			iCollForPage.setName("RepayPlanList");
			for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
				if(bodyIColl.size()>i){
					   KeyedCollection kColl = (KeyedCollection)bodyIColl.get(i);
					   iCollForPage.addDataElement(kColl);
				}else{
					break;
				}
			}
			this.putDataElement2Context(iCollForPage, context);
			this.putDataElement2Context(iqpkColl, context); 
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}
	
	
	public IndexedCollection getRepayPlanList(KeyedCollection iqpkColl,TableModelDAO dao,Context context,Connection connection,PageInfo pageInfo) throws EMPException{
		KeyedCollection returnKColl = null;
		IndexedCollection bodyIColl = null;
		StringBuffer sb = new StringBuffer();//记录日志信息
	 try{
		 /** 废弃原有使用CompositeData数据结构的接口调用类，转为使用KeyedCollection 
		  *  modified by huangtao on 2019-03-14  */
//		ESBComponent esbComponent = (ESBComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(TradeConstance.ESBCOMPONENT, context, connection);
//		returnKColl = esbComponent.createRepayPlan(iqpkColl,dao,context,connection,pageInfo);
		ESBServiceInterface call = new ESBServiceInterfaceImple();
		returnKColl = call.tradeHKSS(iqpkColl, context, connection);
		if(returnKColl != null && returnKColl.size() > 0){ 
			/**取报文头信息*/
			KeyedCollection headKColl = (KeyedCollection)returnKColl.getDataElement("SYS_HEAD");
			String retCode =(String)((KeyedCollection)((IndexedCollection)headKColl.getDataElement("RetInfArry")).get(0)).getDataValue("RetCd");
			
			/**判断报文是否发送成功,并接受返回的数据*/
			if("000000".equals(retCode)){
				KeyedCollection bodyKColl = (KeyedCollection)returnKColl.getDataElement("BODY");
				bodyIColl = (IndexedCollection)bodyKColl.getDataElement("AcctRepymtDtlInfArry");//接收的总iColl
				bodyIColl.setName("RepayPlanList");
				String totalRowsR = String.valueOf(bodyIColl.size());//总条数 
				pageInfo.setRecordSize(totalRowsR);
			}else{
				String retMsg = (String)((KeyedCollection)((IndexedCollection)headKColl.getDataElement("RetInfArry")).get(0)).getDataValue("RetInf");
				throw new Exception("还款试算失败："+retMsg);
			}
		}
	 } catch (Exception e) {
	 		sb.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
	 		EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
	 		throw new EMPException(e.getMessage());
		}
		return bodyIColl;
	}

}
