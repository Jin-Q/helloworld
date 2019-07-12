package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class CcrAddAppOperation extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = this.getConnection(context);
		String flagInfo = CMISMessage.DEFEAT;//信息编码
        String serno="";//业务编码
        String modelno="";//模型编码
		String yyrq= (String) context.getDataValue(PUBConstant.OPENDAY);
		String currentUserId=(String)context.getDataValue("currentUserId");
		String organNo = (String)context.getDataValue("organNo");
		
        try{
			//构建业务处理类
			CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
			
		    //从context中取出sequenceService 
            serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "generate serno["+serno+"]");
			
			KeyedCollection ccrAddInfKcoll = (KeyedCollection)context.getDataElement(CcrPubConstant.CCR_ADDINF);
			modelno = (String) context.getDataValue("CcrAddInf.model_no");
			String cus_id = (String) context.getDataValue("CcrAddInf.cus_id");
			String flag = (String) context.getDataValue("CcrAddInf.flag");
			try{
				ccrAddInfKcoll.remove("cert_type");
			}catch(Exception e){
				
			}
			try{
				ccrAddInfKcoll.remove("cert_code");
			}catch(Exception e){
				
			}

			ComponentHelper cHelper =new ComponentHelper();
			CcrAppInfo ccrAddInfDomain=new CcrAppInfo();
			CcrAppDetail ccrAppDetail = new CcrAppDetail();
			//将新增信息传入Domain 带入Component中。
			cHelper.kcolTOdomain(ccrAddInfDomain, ccrAddInfKcoll);
			cHelper.kcolTOdomain(ccrAppDetail, ccrAddInfKcoll);
			//设置serno
			ccrAddInfDomain.setSerno(serno);
			ccrAppDetail.setSerno(serno);
			//设置modelno
			ccrAppDetail.setModelNo(modelno);
			//设置评分人
			ccrAddInfDomain.setInputId(currentUserId);
			ccrAddInfDomain.setInputBrId(organNo);
			try {
				if("5".equals(flag)){
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
					//从客户表获取相关信息
					CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
					ccrAppDetail.setLast_adjusted_grade(cus.getCusCrdGrade());//上次信用评级
					String eval_maturity = cus.getCusCrdDt();
					String lastYear = "";
					if(eval_maturity!=null && !"".equals(eval_maturity)){
						lastYear = Integer.toString((Integer.parseInt(eval_maturity.substring(0,4))-1))+eval_maturity.substring(4);
					}
					ccrAppDetail.setLat_app_end_date(lastYear);//上次评级时间	
				}
			} catch (Exception e) {
				EMPLog.log( this.getClass().getName(), EMPLog.INFO, 0, e.getMessage());
				throw new EMPException(e);
			}
			
			//设置申请发起日=营业日期。
			ccrAddInfDomain.setAppBeginDate(yyrq);
			//进行新增操作
			flagInfo = ccrComponent.addApp(ccrAddInfDomain,ccrAppDetail);
			
			if(flagInfo == CMISMessage.DEFEAT){
				throw new CMISException(CMISMessage.MESSAGEDEFAULT,"新增失败!"); 
			}
			//将 serno存入 context.供跳转使用。
			context.addDataField("serno", serno);
			//将 cus_id存入 context.供跳转使用。
			context.addDataField("cus_id", ccrAddInfDomain.getCusId());
			//将 model_no存入 context.供跳转使用。
			context.addDataField("model_no", ccrAppDetail.getModelNo());
			//将 存入 context.供跳转使用。
			context.addDataField("fnc_year", ccrAppDetail.getFncYear());
			//将 是否授信标志存入 context.供跳转不同的页面使用。
			context.addDataField("is_authorize", ccrAppDetail.getIs_authorize());
			context.addDataField("flag", "success");
		}catch (CMISException e) {
			String message = CMISMessageManager.getMessage(e);
			CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
            EMPLog.log( this.getClass().getName(), EMPLog.INFO, 0, e.toString());

			throw e;
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		
		return PUBConstant.SUCCESS;
	}
}
