package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class GetFirstPayDateOp extends CMISOperation {
private static final String CTRCONTMODEL = "CtrLoanCont";//合同
private static final String CTRCONTSUBMODEL = "CtrLoanContSub";//合同从表

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String contNo = "";
			if(context.containsKey("cont_no")){
				contNo = (String)context.getDataValue("cont_no");
			}
			if(contNo == null || contNo.trim().length() == 0){
				throw new EMPException("获取合同编号失败！");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			//KeyedCollection kColl = dao.queryAllDetail("CtrLoanCont", contNo, connection);
			KeyedCollection ctrContKColl =  dao.queryDetail(CTRCONTMODEL, contNo, connection); //合同申请主表
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, contNo, connection); //合同申请从表
			if(ctrContKColl == null || ctrContSubKColl == null){
				throw new EMPException("无对应的合同信息！");
			}
			String interest_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("interest_term"));//计息周期
			String repay_date = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_date"));//还款日
			String prd_id = TagUtil.replaceNull4String(ctrContKColl.getDataValue("prd_id"));//产品编码
			String intStlmntDt="21";//还款日， 默认21
			String nxtStlmntIntDt="";
			String date = context.getDataValue(PUBConstant.OPENDAY).toString();//当前营业日期
			nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, date, intStlmntDt);//对公下一结息日期
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String hxPrdId = service.getPrdBasicCLPM2LM(contNo, prd_id, context, connection);
			if("01010101".equals(hxPrdId)){//对公贷款
				nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, date, intStlmntDt);//对公下一结息日期
			}else{//对私贷款repay_date
				int len=repay_date.length();
				if(len==2){
					intStlmntDt = repay_date;
				}else{
					intStlmntDt = "0"+repay_date;
				}
				nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, date, intStlmntDt);
			}
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
			context.addDataField("first_pay_date", nxtStlmntIntDt);
		} catch (Exception e) {
			context.addDataField("flag", "error");
			context.addDataField("first_pay_date", "");
			context.addDataField("msg", "计算首次还款日失败："+e.getMessage());
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
