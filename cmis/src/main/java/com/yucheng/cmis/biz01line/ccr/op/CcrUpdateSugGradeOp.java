package com.yucheng.cmis.biz01line.ccr.op;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.exception.AgentException;
/**
 * 异步保存<b>调整得分</b>,<b>调整后等级</b>,以及<b>调整后的最后得分</b>OP类
 * 	虽然OP叫UpdateSugGradeOp,但是其实保存的是调整得分以及调整后等级,以及调整后的最后得分.
	最后得分将在流程结束后根据流程中的调整得分 再次修改.
	建议等级已经失效.
 * @author Administrator
 *
 */
public class CcrUpdateSugGradeOp extends CMISOperation {

	String modelId = "CcrAppInfo";
	String modelId1 = "CcrAppDetail";
	String modelId2 = "LmtAppFinGuar";//融资性担保公司授信和评级一起走时，需要新增授信记录
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String result ="";
		String serno = "";
		String cusId="";
		String all_score="";
		BigDecimal auto_score = null;
		String auto_grade="";
		String adjusted_grade="";
		String reason="";
		String managerId="";
		String managerBrId="";
		String regdate="";
//		String reason="";
		String o_rating_unit ="";
		String o_rating_result ="";
		String o_rating_date = "";
		String bail_multi ="";//担保放大倍数
		String guar_type = "";//担保类型
		try{
			connection = this.getConnection(context);

			try {
				KeyedCollection ccrAppInfo = (KeyedCollection)context.getDataElement(modelId);
				KeyedCollection ccrAppDetail = (KeyedCollection)context.getDataElement(modelId1);
				if(ccrAppDetail.containsKey("is_authorize")){
					String is_authorize = (String) ccrAppDetail.getDataValue("is_authorize");
					if("1".equals(is_authorize.trim())){//如果存在和评级一起走的授信则对其进行新增
						KeyedCollection lmtAppFinGuar =(KeyedCollection)context.getDataElement(modelId2);
						String serno1 = (String) lmtAppFinGuar.getDataValue("serno");
						lmtAppFinGuar.setDataValue("manager_id",ccrAppInfo.getDataValue("manager_id"));//责任人
						lmtAppFinGuar.setDataValue("manager_br_id",ccrAppInfo.getDataValue("manager_br_id"));//责任机构
						lmtAppFinGuar.setDataValue("input_id",context.getDataValue("currentUserId"));//登记人
						lmtAppFinGuar.setDataValue("input_br_id",context.getDataValue("organNo"));//登记机构
						lmtAppFinGuar.setDataValue("input_date",ccrAppInfo.getDataValue("reg_date"));//登记日期
						TableModelDAO dao = this.getTableModelDAO(context);
						IndexedCollection ic = dao.queryList(modelId2, " where serno = '"+serno1+"'", connection);
						if(ic.size()==0){
							dao.insert(lmtAppFinGuar, connection);
						}else{
							dao.update(lmtAppFinGuar, connection);
						}
					}
				}
				serno = (String)ccrAppInfo.getDataValue("serno");
				cusId = (String)ccrAppInfo.getDataValue("cus_id");
//				all_score = (String)ccrAppDetail.getDataValue("all_score");
//				auto_score = BigDecimal.valueOf(Double.valueOf((String)ccrAppDetail.getDataValue("auto_score")));
//				auto_grade = (String)ccrAppDetail.getDataValue("auto_grade");
				adjusted_grade = (String) ccrAppDetail.getDataValue("adjusted_grade");
				reason = (String) ccrAppDetail.getDataValue("reason");
				managerId = (String)ccrAppInfo.getDataValue("manager_id");
				managerBrId = (String)ccrAppInfo.getDataValue("manager_br_id");
				regdate = (String)ccrAppInfo.getDataValue("reg_date");
				if(ccrAppDetail.containsKey("o_rating_unit")){
					o_rating_unit = (String) ccrAppDetail.getDataValue("o_rating_unit");
				}
				if(ccrAppDetail.containsKey("o_rating_result")){
					o_rating_result = (String) ccrAppDetail.getDataValue("o_rating_result");
				}
				if(ccrAppDetail.containsKey("o_rating_date")){
					o_rating_date = (String) ccrAppDetail.getDataValue("o_rating_date");
				}
				if(ccrAppDetail.containsKey("bail_multi")){
					bail_multi = (String) ccrAppDetail.getDataValue("bail_multi");
				}
				if(ccrAppDetail.containsKey("guar_type")){
					guar_type = (String) ccrAppDetail.getDataValue("guar_type");
				}
//				reason = (String)ccrAppInfo.getDataValue("reason");
			} catch (Exception e) {
				EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "Kcoll["+modelId+"] can't find in context");
				e.printStackTrace();
				throw e;
			}

			//构建业务处理类
			try{
				CcrAppInfo ccrAppInfo = new CcrAppInfo();
				CcrAppDetail ccrAppDetail = new CcrAppDetail();
				ccrAppInfo.setSerno(serno);
				ccrAppInfo.setManagerId(managerId);
				ccrAppInfo.setManagerBrId(managerBrId);
				ccrAppInfo.setRegDate(regdate);
				ccrAppInfo.setCusId(cusId);
				ccrAppDetail.setSerno(serno);
//				ccrAppDetail.setAllScore(all_score);
//				ccrAppDetail.setAutoScore(auto_score);
//				ccrAppDetail.setAutoGrade(auto_grade);
//				ccrAppDetail.setReason(reason);
				ccrAppDetail.setAdjustedGrade(adjusted_grade);
				ccrAppDetail.setReason(reason);
				ccrAppDetail.setORatingUnit(o_rating_unit);
				ccrAppDetail.setORatingResult(o_rating_result);
				ccrAppDetail.setORatingDate(o_rating_date);
				ccrAppDetail.setBailMulti(bail_multi);
				ccrAppDetail.setGuarType(guar_type);
				CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
				String cusGradeDt = context.getDataValue("OPENDAY").toString();
				//申请日期
				ccrAppInfo.setAppBeginDate(cusGradeDt);
				result = ccrComponent.updateCcrAppAll(ccrAppInfo,ccrAppDetail);
				context.addDataField("ccr_result", ""+result);
				context.addDataField("ccr_message","");
				
			}catch(AgentException e){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, e.toString());
				throw new CMISException(e);
			}
			return null;
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}

	}

}
