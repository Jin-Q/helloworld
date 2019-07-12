package com.yucheng.cmis.biz01line.ccr.op.ccrratdirect;

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
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCcrRatDirectRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CcrAppInfo";
	private final String modelId1 = "CcrAppDetail";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String currentUserId=(String)context.getDataValue("currentUserId");
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		String yyrq= (String) context.getDataValue(PUBConstant.OPENDAY);
		try{
			connection = this.getConnection(context);
			KeyedCollection kCollInfo = null;
			KeyedCollection kCollDetail = null;
			try {
				kCollInfo = (KeyedCollection)context.getDataElement(modelId);
				kCollDetail = (KeyedCollection)context.getDataElement(modelId1);
			} catch (Exception e) {}
		//	if(kColl == null || kColl.size() == 0)
		//		throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
		//	String cusId =(String) kColl.getDataValue("cus_id");
			//构建业务处理类
			CcrComponent ccrComponent = (CcrComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(
					CcrPubConstant.CCR_COMPONENT, context, connection);
			//取客户银监标准小企业的值
		//	CusComComponent  cusComComponent= (CusComComponent) ccrComponent.getOtherComponentInstance("CusCom");
		//	CusCom  cusCom =cusComComponent.getCusCom(cusId);
		//	String  yjpzFlag=cusCom.getYjbzFlag();
		//	if(yjpzFlag==null)  yjpzFlag="2";
			
		//	int iCount = ccrComponent.checkCusApp(cusId);
			
		//	if(iCount>0){
		//		throw new EMPException("该客户已经存在未办结的业务申请不能再次新增!");
		//	}
			
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			String reason_show = (String) kCollDetail.getDataValue("reason_show");
			if("0".equals(reason_show)){
				kCollDetail.setDataValue("reason_show0", "0");
			}else if("1".equals(reason_show)){
				kCollDetail.setDataValue("reason_show1", "1");
			}else if("2".equals(reason_show)){
				kCollDetail.setDataValue("reason_show2", "2");
			}else if("0,1".equals(reason_show)){
				kCollDetail.setDataValue("reason_show0", "0");
				kCollDetail.setDataValue("reason_show1", "1");
			}else if("0,2".equals(reason_show)){
				kCollDetail.setDataValue("reason_show0", "0");
				kCollDetail.setDataValue("reason_show2", "2");
			}else if("1,2".equals(reason_show)){
				kCollDetail.setDataValue("reason_show1", "1");
				kCollDetail.setDataValue("reason_show2", "2");
			}else if("0,1,2".equals(reason_show)){
				kCollDetail.setDataValue("reason_show0", "0");
				kCollDetail.setDataValue("reason_show1", "1");
				kCollDetail.setDataValue("reason_show2", "2");
			}
			
 			ComponentHelper cHelper =new ComponentHelper();
			CcrAppInfo ccrAddInfDomain=new CcrAppInfo();
			CcrAppDetail ccrAppDetail = new CcrAppDetail();
			//将新增信息传入Domain 带入Component中。
			cHelper.kcolTOdomain(ccrAddInfDomain, kCollInfo);
			
			//评级直接认定
			if(kCollDetail.containsKey("reason_show")){
				String[] reason_show_arr = reason_show.split(",");
				for(int i = 0;i<reason_show_arr.length;i++){
					if(reason_show_arr[i].equals("0")){
						if(kCollDetail.containsKey("reason_show0")){
							kCollDetail.setDataValue("reason_show0", "0");
						}else{
							kCollDetail.addDataField("reason_show0", "0");
						}
					}else if(reason_show_arr[i].equals("1")){
						if(kCollDetail.containsKey("reason_show1")){
							kCollDetail.setDataValue("reason_show1", "1");
						}else{
							kCollDetail.addDataField("reason_show1", "1");
						}
					}else if(reason_show_arr[i].equals("2")){
						if(kCollDetail.containsKey("reason_show1")){
							kCollDetail.setDataValue("reason_show2", "2");
						}else{
							kCollDetail.addDataField("reason_show2", "2");
						}
					}
				}
				kCollInfo.setDataValue("check_value", "1");
				context.addDataField("reportId", "ccr/zrscb1.raq");
			}else{
				kCollInfo.setDataValue("check_value", "2");
				context.addDataField("reportId", "ccr/zrscb2.raq");
			}
			cHelper.kcolTOdomain(ccrAppDetail, kCollDetail);
			//设置serno
			ccrAddInfDomain.setSerno(serno);
			ccrAppDetail.setSerno(serno);
			
			//设置评分人
	//		ccrAddInfDomain.setManagerId(currentUserId);
	//		ccrAddInfDomain.setManagerBrId((String)context.getDataValue("organNo"));
			ccrAddInfDomain.setInputId((String)context.getDataValue(PUBConstant.currentUserId));

			//设置申请发起日=营业日期。
			ccrAddInfDomain.setAppBeginDate(yyrq);
			//ccrAddInfDomain.setFinaBrId((String)context.getDataValue("ARTI_ORGANNO"));
			ccrAddInfDomain.setInputBrId((String)context.getDataValue("organNo"));
			
			
			
			//将 serno存入 context.供跳转使用。
			context.addDataField("serno", serno);
			//将 cus_id存入 context.供跳转使用。
			context.addDataField("cus_id", ccrAddInfDomain.getCusId());
			//进行新增操作
			flagInfo = ccrComponent.addAppRat(ccrAddInfDomain,ccrAppDetail);
			if(flagInfo == CMISMessage.DEFEAT){
				throw new CMISException(CMISMessage.MESSAGEDEFAULT,"新增失败!"); 
			}
		}catch (CMISException e) {
			// TODO Auto-generated catch block
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
