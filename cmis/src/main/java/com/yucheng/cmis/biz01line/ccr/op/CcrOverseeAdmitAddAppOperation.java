package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppAll;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.biz01line.ccr.domain.CcrGIndScore;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.PUBConstant;

public class CcrOverseeAdmitAddAppOperation extends CMISOperation {
	private final String modelId = "CcrAppInfo";
	public String doExecute(Context context) throws EMPException {
		Connection connection = this.getConnection(context);
		String flagInfo = CMISMessage.DEFEAT;//信息编码
        String modelno="";//模型编码
		String yyrq= (String) context.getDataValue(PUBConstant.OPENDAY);
		String sernoOversee = (String) context.getDataValue("serno");
		String cus_id = (String) context.getDataValue("oversee_org_id");
		String currentUserId=(String)context.getDataValue("currentUserId");
		String organNo = (String)context.getDataValue("organNo");
        try{//构建业务处理类
			CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);					        
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "generate serno["+sernoOversee+"]");
			
			//监管机构准入评级模型编号
			modelno = "M20130800060";
			CcrAppInfo ccrAddInfDomain=new CcrAppInfo();
			CcrAppDetail ccrAppDetail = new CcrAppDetail();
			//设置modelno
			ccrAppDetail.setModelNo(modelno);
			ccrAddInfDomain.setCusId(cus_id);
			//设置评分人
			ccrAddInfDomain.setInputId(currentUserId);
			ccrAddInfDomain.setInputBrId(organNo);
			//监管机构准入标准
			ccrAddInfDomain.setFlag("7");
			//设置审批状态
			ccrAddInfDomain.setApproveStatus("000");
			//设置申请发起日=营业日期。
			ccrAddInfDomain.setAppBeginDate(yyrq);
			
			KeyedCollection kcDetail =null;
			TableModelDAO dao = this.getTableModelDAO(context);
			String op = (String) context.getDataValue("op");
			if(!op.equals("view")){//进行新增操作
	        	String conStr = "where serno='"+sernoOversee+"' and flag='7'";
	        	IndexedCollection iColl = dao.queryList(modelId, conStr, connection);
				if((iColl.size()==0)){//从context中取出sequenceService 
					//设置serno
					ccrAddInfDomain.setSerno(sernoOversee);
					ccrAppDetail.setSerno(sernoOversee);
					flagInfo = ccrComponent.addApp(ccrAddInfDomain,ccrAppDetail);
					if(flagInfo == CMISMessage.DEFEAT){
						throw new CMISException(CMISMessage.MESSAGEDEFAULT,"新增失败!"); 
					}
				}
				kcDetail = dao.queryDetail("CcrAppDetail",sernoOversee, connection);
				doQuery(context,sernoOversee,cus_id,ccrComponent,connection);
			}else{//只查询操作
				kcDetail = dao.queryDetail("CcrAppDetail",sernoOversee, connection);
			    doQuery(context,sernoOversee,cus_id,ccrComponent,connection);
			}
			//将 serno存入 context.供跳转使用。
			context.setDataValue("serno", sernoOversee);
			//将 cus_id存入 context.供跳转使用。
			context.addDataField("cus_id",cus_id);
			//将 model_no存入 context.供跳转使用。
			context.addDataField("model_no",modelno);
			//将fnc_year 存入 context.供跳转使用。
			context.addDataField("fnc_year",kcDetail.getDataValue("fnc_year"));
			//将 fnc_month存入 context.供跳转使用。
			context.addDataField("fnc_month",kcDetail.getDataValue("fnc_month"));
			//将 stat_prd_style存入 context.供跳转使用。
			context.addDataField("stat_prd_style",kcDetail.getDataValue("stat_prd_style"));
			
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
	
	protected void doQuery(Context context,String serno,String cus_id,CcrComponent ccrComponent,Connection connection){
		KeyedCollection ccrAppInfoKcoll = null;
		KeyedCollection ccrAppDetailKcoll = null;
						        
		CcrAppAll ccrAppAll;
		try {
			TableModelDAO dao = this.getTableModelDAO(context);
			/**
			 *将指标的得分保存入 id 为 组名的kcoll中。 格式为 kcoll (-id:组名) -指标名 : 指标选项
			 * -指标名2 ： 指标选项2 -指标名3 ： 指标选项3 -指标名_score: 指标得分 -指标名2_score:
			 * 指标得分2 -指标名3_score: 指标得分3 -指标名_orgVal: 指标原始值 -指标名2_orgVal:
			 * 指标原始值2 -指标名3_orgVal: 指标原始值3 下方已弃用 -指标名_readonly: 指标是否只读
			 * -指标名2_readonly: 指标2是否只读 -指标名3_readonly: 指标3是否只读
			 * 
			 */
			ccrAppAll = ccrComponent.loadScore(serno, cus_id);
			CcrAppInfo ccrAppInfo = ccrAppAll.getCcrAppInfo();
			CcrAppDetail ccrAppDetail1 = ccrAppAll.getCcrAppDetail();
			ArrayList ccrGIndScoreList = ccrAppAll.getCcrGIndScoreList();
			Iterator indScoreIter = ccrGIndScoreList.iterator();
			String groupNoOld = "";
			
			KeyedCollection groupKcoll = null;// 组kcoll 第一次循环时实例化
			while (indScoreIter.hasNext()) {
				CcrGIndScore ccrGIndScore = (CcrGIndScore) indScoreIter.next();
				String groupNo = ccrGIndScore.getGroupNo();
				String indexScore = ccrGIndScore.getIndexScore();
				// String indReadonly =
				// ccrGIndScore.getGroupName();//用GroupName来保存是否只读。
				String indexNo = ccrGIndScore.getIndexNo();// 编号
				String indexValue = ccrGIndScore.getIndexValue();// 得分
				String indOrgVal = ccrGIndScore.getIndOrgVal();// 原始值
				if (!groupNo.equals(groupNoOld)) {
					if (groupKcoll != null) {
						context.addDataElement(groupKcoll);// 当组名称发生变化将老的组存入context,并实例化新的groupKcoll
					}
					groupKcoll = new KeyedCollection();
					groupKcoll.setId(groupNo);
					groupNoOld = groupNo;// 将当前新的groupNo存入old用作比较
				}
				groupKcoll.addDataField(indexNo, indexValue);
				
				groupKcoll.addDataField(indexNo + "_score", indexScore);
				groupKcoll.addDataField(indexNo + "_orgVal", indOrgVal);
			}
			context.addDataElement(groupKcoll);
			/*KeyedCollection ccrAppInfoKcoll = cHelper.domain2kcol(
					ccrAppInfo, CcrPubConstant.CCR_APPINFO);*/
			ccrAppInfoKcoll = dao.queryFirst("CcrAppInfo", null, " where serno = '"+serno+"'",connection);
			ccrAppDetailKcoll = dao.queryFirst("CcrAppDetail", null, " where serno = '"+serno+"'",connection);
			this.putDataElement2Context(ccrAppInfoKcoll, context);
			this.putDataElement2Context(ccrAppDetailKcoll, context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
