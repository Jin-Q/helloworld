package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppAll;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.biz01line.ccr.domain.CcrGIndScore;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class CcrQueryAppOperation extends CMISOperation {
	private final String modelId = "CcrAppInfo";
	private final String serno_name = "serno";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "";
		try {
			connection = this.getConnection(context);

		/*	if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.put("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
		*/	
			String serno_value = null;
			try {
				serno_value = (String) context.getDataValue(serno_name);

			} catch (Exception e) {
				throw new EMPException("找不到业务编码\n" + e);
			}
			if (serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + serno_name
						+ "] cannot be null!");

			// 构建业务处理类
			try {
				TableModelDAO dao = this.getTableModelDAO(context);
				KeyedCollection ccrAppInfoKcoll = dao.queryFirst("CcrAppInfo", null, " where serno = '"+serno_value+"'", connection);
				KeyedCollection ccrAppDetailKcoll = dao.queryFirst("CcrAppDetail", null, " where serno = '"+serno_value+"'", connection);
				String cus_id = ccrAppInfoKcoll.getDataValue("cus_id").toString();
				
				CcrComponent ccrComponent = (CcrComponent) CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance(
								CcrPubConstant.CCR_COMPONENT, context, connection);
				CcrAppAll ccrAppAll = ccrComponent.loadScore(serno_value, cus_id);
				CcrAppInfo ccrAppInfo = ccrAppAll.getCcrAppInfo();
				CcrAppDetail ccrAppDetail = ccrAppAll.getCcrAppDetail();
				ArrayList ccrGIndScoreList = ccrAppAll.getCcrGIndScoreList();

				/**
				 *将指标的得分保存入 id 为 组名的kcoll中。 格式为 kcoll (-id:组名) -指标名 : 指标选项
				 * -指标名2 ： 指标选项2 -指标名3 ： 指标选项3 -指标名_score: 指标得分 -指标名2_score:
				 * 指标得分2 -指标名3_score: 指标得分3 -指标名_orgVal: 指标原始值 -指标名2_orgVal:
				 * 指标原始值2 -指标名3_orgVal: 指标原始值3 下方已弃用 -指标名_readonly: 指标是否只读
				 * -指标名2_readonly: 指标2是否只读 -指标名3_readonly: 指标3是否只读
				 * 
				 */
				Iterator indScoreIter = ccrGIndScoreList.iterator();
				String groupNoOld = "";
				
				/**
				 * 嘉兴项目组 需要在第一次显示页面的时候 取到定量指标 指标值
				 * 如果 原始值在  IND_RESULT_VAL 表中存在 用 该表中的值 替换 原始值
				 */

			/*	ArrayList<HashMap> arr = new ArrayList<HashMap>(); 
				arr = ccrComponent.loadIndResultVal(serno_value);  */
				
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
					groupKcoll.put(indexNo, indexValue);
					
					/**
					 * 嘉兴 项目组 加
					 */
				/*	if(arr!=null&&arr.size()!=0){
						for(int i=0; i<arr.size(); i++){
							HashMap<String,String> hm = arr.get(i);
							if(indexNo.equals(hm.get("index_no"))){
								indOrgVal = hm.get("index_value");
							}
						}
					}  */
					
					groupKcoll.put(indexNo + "_score", indexScore);
					groupKcoll.put(indexNo + "_orgVal", indOrgVal);
					// groupKcoll.put(indexNo+"_readonly",
					// indReadonly);

				}
				/*KeyedCollection ccrAppInfoKcoll = cHelper.domain2kcol(
						ccrAppInfo, CcrPubConstant.CCR_APPINFO);*/
				String app_end_date ="";
				flag = (String) ccrAppInfoKcoll.getDataValue("flag");
				app_end_date = (String) ccrAppInfoKcoll.getDataValue("app_end_date");
				if(null==app_end_date||"".equals(app_end_date)){
					app_end_date = (String) context.getDataValue("OPENDAY");
				}
				KeyedCollection ss = ccrComponent.GetDateGrade(cus_id, flag, app_end_date);
				
				//限制等级
				context.addDataElement(groupKcoll);// 把最后一个kcoll加入context
				
				// 需要时放开,将ccrMGroupScoreIcoll 转换格式后放入context
				// IndexedCollection ccrMGroupScoreIcoll
				// =(IndexedCollection)cHelper.domain2icol(ccrMGroupScoreList,
				// CcrPubConstant.CCR_MGROUPSCORE);
				// @todo 没制作转换格式部分
				// this.putDataElement2Context(ccrMGroupScoreIcoll, context);
				SInfoUtils.addUSerName(ccrAppInfoKcoll, new String[] { "manager_id" ,"input_id"});
				SInfoUtils.addSOrgName(ccrAppInfoKcoll, new String[] { "manager_br_id" ,"input_br_id"});
			//	SInfoUtils.addSOrgName(ccrAppInfoKcoll, new String[] { "fina_br_id" });
							
				this.putDataElement2Context(ccrAppInfoKcoll, context);
				this.putDataElement2Context(ccrAppDetailKcoll, context);
				
				//根据模型ID获取模型名称
				String modelNo = (String) ccrAppDetailKcoll.getDataValue("model_no");
				KeyedCollection model = dao.queryFirst("IndModel", null, " where model_no = '"+modelNo+"'", connection);
				context.put("model_name", model.getDataValue("model_name").toString());
				//调用客户模块接口
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				String lastGrade = "";//上次信用等级
				if(flag.equals("3")){
				//同业客户
					if(null==ss.getDataValue("adjusted_grade")||"".equals(ss.getDataValue("adjusted_grade"))){
					   lastGrade = "00";
					}else{
					   lastGrade = (String) ss.getDataValue("adjusted_grade");
					}
				KeyedCollection cusSameOrg = service.getCusSameOrgKcoll(cus_id, context, connection);
				context.put("cus_name",cusSameOrg.getDataValue("same_org_cnname"));//客户名称
				context.put("cus_type",cusSameOrg.getDataValue("same_org_type"));//客户类型CRD_GRADE
				context.put("cus_crd_grade",lastGrade);//上次信用评级
				context.put("com_crd_dt",ss.getDataValue("app_end_date"));//上次评级时间	
				}else if(flag.equals("4")){//融资性担保公司
					//从客户表获取相关信息
					if(null==ss.getDataValue("adjusted_grade")||"".equals(ss.getDataValue("adjusted_grade"))){
					   lastGrade = "Z";
					}else{
					   lastGrade = (String) ss.getDataValue("adjusted_grade");
					}
					CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
					context.put("cus_name", cus.getCusName());//客户名称
					context.put("cus_type", cus.getCusType());//客户类型
					context.put("cus_crd_grade",lastGrade);//上次信用评级
					context.put("com_crd_dt",ss.getDataValue("app_end_date"));//上次评级时间	
				}else{
					//从客户表获取相关信息
					if(null==ss.getDataValue("adjusted_grade")||"".equals(ss.getDataValue("adjusted_grade"))){
					   lastGrade = "00";
					}else{
					   lastGrade = (String) ss.getDataValue("adjusted_grade");
					}
					CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
					context.put("cus_name", cus.getCusName());//客户名称
					context.put("cus_type", cus.getCusType());//客户类型
					context.put("cus_crd_grade",lastGrade);//上次信用评级
					context.put("com_crd_dt",ss.getDataValue("app_end_date"));//上次评级时间	
				}
				//获取是否授信标志
				String is_authorize = (String) ccrAppDetailKcoll.getDataValue("is_authorize");
				if("1".equals(is_authorize)){
					KeyedCollection lmtAppFinGuarKc = dao.queryFirst("LmtAppFinGuar", null, " where serno = '"+serno_value+"'", connection);
					SInfoUtils.addUSerName(lmtAppFinGuarKc, new String[] { "manager_id" ,"input_id"});
					SInfoUtils.addSOrgName(lmtAppFinGuarKc, new String[] { "manager_br_id" ,"input_br_id"});
					this.putDataElement2Context(lmtAppFinGuarKc, context);
				}
				context.put("is_authorize", is_authorize);
				String stat_prd_style = (String) ccrAppDetailKcoll.getDataValue("stat_prd_style");
				if("2".equals(stat_prd_style)){
					context.put("fnc_type_name", "季报");
				}else if("4".equals(stat_prd_style)){
					context.put("fnc_type_name", "年报");
				}else{
					context.put("fnc_type_name", "");
				}
				context.put("flag",flag);
			} catch (AgentException e) {
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, e.toString());
				throw new CMISException(e);
			}

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}

		return "0";
	}

}
