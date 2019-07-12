package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.SessionException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class RiskManagePspCheckGrpImple implements RiskManageInterface{

	private final String modelId = "PspCheckTask";//贷后任务表
	private static final Logger logger = Logger.getLogger(RiskManagePspCheckGrpImple.class);
	/*贷后检查提交必入校验
	 * 注释1：
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			KeyedCollection appkColl = dao.queryDetail(modelId, serno, conn);
			String check_type = (String) appkColl.getDataValue("check_type");
			String task_type = (String) appkColl.getDataValue("task_type");
			String task_id = (String) appkColl.getDataValue("task_id");
			/* added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
			String check_freq = (String) appkColl.getDataValue("check_freq");//检查频率
			if(check_freq == null){
				check_freq = " ";
			}
			/* added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
			IndexedCollection bailIColl15 = SqlClient.queryList4IColl("sigltabJTByTaskID", task_id, conn);//(16)集团风险预警
			IndexedCollection bailIColl16 = SqlClient.queryList4IColl("evttabJTByTaskID", task_id, conn);//(17)集团关注事件
			
			IndexedCollection iColl = SqlClient.queryList4IColl("queryGrpAllListForPsp", task_id, connection);//获取集团成员信息
			/**add by lisj 2015-5-21 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
			String grpInfoFlag = (String) SqlClient.queryFirst("queryGrpInfoByTaskId", task_id, null, conn);
			/**add by lisj 2015-5-21 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
			if("02".equals(check_type) && "04".equals(task_type)){//定期检查的集团客户
				/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
				if(check_freq.equals("04") && (bailIColl15==null||bailIColl15.size()<1)){
						returnFlag = "不通过";
						returnInfo = "请录入【风险预警】后再进行提交";
					}else if(check_freq.equals("04") && (bailIColl16==null||bailIColl16.size()<1)){
						returnFlag = "不通过";
						returnInfo = "请录入【关注事件】后再进行提交";
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
					/**modified by lisj 2015-5-21 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
					}else if(grpInfoFlag ==null || "FORBID".equals(grpInfoFlag)){
						returnFlag = "不通过";
						returnInfo = "请录入【集团检查信息】后再进行提交";
					/**mofified by lisj 2015-5-21 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
					}else if(iColl != null && iColl.size()>0){//检查成员信息
						for(int i=0;i<iColl.size();i++){
							KeyedCollection kColl = (KeyedCollection) iColl.get(i);
							String task_id_Grp = (String) kColl.getDataValue("task_id");//集团下每个成员的task_id
						//	String condition2 = " where task_id='"+task_id_Grp+"' ";
							String cus_id_Grp = (String) kColl.getDataValue("cus_id");//集团下每个成员的cus_id
							String check_type2 = (String) kColl.getDataValue("check_type");
							String task_type2 = (String) kColl.getDataValue("task_type");
							
							String psp_cus_type2 = (String) kColl.getDataValue("psp_cus_type");
							String check_freq2 = (String) kColl.getDataValue("check_freq");
							if(check_freq2 == null){
								check_freq2 = " ";
							}
							String condition2 = " where cus_id='"+cus_id_Grp+"' and prd_id in('100058','100057','100061')";
							IndexedCollection iColl2 = dao.queryList("AccLoan", condition2, connection);
							
							IndexedCollection grtMortIColl2 = new IndexedCollection();//担保品信息
							IndexedCollection grtGuaranteeIColl2 = new IndexedCollection();//保证人信息
							CMISModualServiceFactory serviceJndi2 = CMISModualServiceFactory.getInstance();
							GrtServiceInterface service2 = (GrtServiceInterface)serviceJndi2.getModualServiceById("grtServices", "iqp");
							
							/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
							IndexedCollection TaskIColl = new IndexedCollection();//贷后信息
							TaskIColl = dao.queryList("PspCheckTask", "where task_create_date >= '2015-06-05' and task_id ='"+task_id_Grp+"'", connection);
							
							/**modified by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
							grtGuaranteeIColl2 = dao.queryList("PspGuarAnalyRel", "where grt_type='3' and task_id ='"+task_id_Grp+"'", connection);
							if(TaskIColl ==null || TaskIColl.size()<=0){
								grtGuaranteeIColl2 = service2.getGrtListByCusId(cus_id_Grp, null, "3", dataSource);
							}
							grtMortIColl2 = dao.queryList("PspGuarAnalyRel", "where grt_type='4' and task_id ='"+task_id_Grp+"'", connection);
							if(TaskIColl ==null || TaskIColl.size()<=0){
								grtMortIColl2 = service2.getGrtListByCusId(cus_id_Grp, null, "4", dataSource);
							}
							/**modified by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
							/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 end**/
							
							
							IndexedCollection bailIColl = SqlClient.queryList4IColl("custabByTaskID", task_id_Grp, conn);//(1)借款人分析
							IndexedCollection bailIColl1 = SqlClient.queryList4IColl("propertytabByTaskID", task_id_Grp, conn);//(2)固定资产贷款（项目融资）分析
							IndexedCollection bailIColl2 = SqlClient.queryList4IColl("propertyanalytabByTaskID", task_id_Grp, conn);//(3)资产状况调查
							IndexedCollection bailIColl3 = SqlClient.queryList4IColl("fnctabByTaskID", task_id_Grp, conn);//(4)财务分析
							IndexedCollection bailIColl4 = SqlClient.queryList4IColl("opertabByTaskID", task_id_Grp, conn);//(5)经营佐证信息
							IndexedCollection bailIColl5 = SqlClient.queryList4IColl("montabByTaskID", task_id_Grp, conn);//(6)监控信息
					//		IndexedCollection bailIColl6 = SqlClient.queryList4IColl("grttabByTaskID", task_id, conn);//(7)担保分析，分析担保品
							IndexedCollection bailIColl8 = SqlClient.queryList4IColl("grttabPersonByTaskID", task_id_Grp, conn);//(9)担保分析，分析保证人-个人
							IndexedCollection bailIColl9 = SqlClient.queryList4IColl("sigltabByTaskID", task_id_Grp, conn);//(10)单一法人风险预警
							IndexedCollection bailIColl10 = SqlClient.queryList4IColl("evttabByTaskID", task_id_Grp, conn);//(11)关注事件
							IndexedCollection bailIColl11 = SqlClient.queryList4IColl("alttabByTaskID", task_id_Grp, conn);//(12)预警信息
							IndexedCollection bailIColl7 = SqlClient.queryList4IColl("custabPersonByTaskID", task_id_Grp, conn);//(8)借款人分析-零售-经营性
							IndexedCollection bailIColl12 = SqlClient.queryList4IColl("custabPersonXFByTaskID", task_id_Grp, conn);//(13)借款人分析-零售-消费性
							IndexedCollection bailIColl13 = SqlClient.queryList4IColl("montabPersonByTaskID", task_id_Grp, conn);//(14)监控信息-个人客户
							//IndexedCollection bailIColl14 = SqlClient.queryList4IColl("alttabByTaskID", task_id, conn);//(15)成员检查信息
							IndexedCollection bailIColl17 = SqlClient.queryList4IColl("custabHZByTaskID", task_id_Grp, conn);//(18)合作方分析
							IndexedCollection bailIColl18 = SqlClient.queryList4IColl("evltabByTaskID", task_id_Grp, conn);//(19)合作方综合评价
							IndexedCollection bailIColl19 = SqlClient.queryList4IColl("custabRZByTaskID", task_id_Grp, conn);//(20)融资担保公司分析
							IndexedCollection bailIColl20 = SqlClient.queryList4IColl("evltabDBByTaskID", task_id_Grp, conn);//(21)担保公司客户综合评价
							IndexedCollection bailIColl21 = SqlClient.queryList4IColl("grttabDGByTaskID", task_id_Grp, conn);//(22)担保分析中的分析保证人-对公客户、小微客户、集团客户
							IndexedCollection bailIColl6 = SqlClient.queryList4IColl("grttabByTaskID", task_id_Grp, conn);//(7)担保分析，分析担保品
							IndexedCollection bailIColl22 = SqlClient.queryList4IColl("water_charge_analyByTaskID", task_id_Grp, conn);//(23)零售客户水电费
							/* modified by yangzy 2014/11/10 贷后集团检查校验改造 start */
							IndexedCollection bailIColl23 = SqlClient.queryList4IColl("opertabTaxByTaskID", task_id_Grp, conn);//(24)经营佐证中的税费明细校验
							IndexedCollection bailIColl24 = SqlClient.queryList4IColl("finatabByTaskID", task_id_Grp, conn);//(25)财务报表中的财务分析结论
							IndexedCollection bailIColl28 = SqlClient.queryList4IColl("opertabTaxYHDZByTaskID", task_id_Grp, conn);//(29)经营佐证中的银行对账单
							/* modified by yangzy 2014/11/10 贷后集团检查校验改造 end */
							
							/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 start */
							IndexedCollection bailIColl36 = SqlClient.queryList4IColl("queryFncByTaskID", task_id_Grp, conn);//(36)小微、公司检查当前财报
							IndexedCollection bailIColl37 = SqlClient.queryList4IColl("queryPspPropertyAnalyByTaskID", task_id_Grp, conn);//(37)零售、小微、公司检查经营佐证
							KeyedCollection kcoll_param = new KeyedCollection();
							
							/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
							IndexedCollection bailIColl38 = new IndexedCollection();
							if(TaskIColl ==null || TaskIColl.size()<=0){
								kcoll_param.put("cus_id", cus_id_Grp);
								kcoll_param.put("task_id", task_id_Grp);
								bailIColl38  = SqlClient.queryList4IColl("queryGrtGuaranteeFncMsgByTaskID", kcoll_param, connection);//(38)小微、公司检查担保企业财报
							}else{
								kcoll_param.put("task_id", task_id_Grp);
								bailIColl38  = SqlClient.queryList4IColl("queryGrtGuaranteeFncMsgByTaskID2", kcoll_param, connection);//(38)小微、公司检查担保企业财报
							}
							/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 end**/
							
							IndexedCollection bailIColl40 = SqlClient.queryList4IColl("queryPspVisitForJKRByTaskID", task_id_Grp, conn);//(40)小微、公司检查现场检查借款企业
							IndexedCollection bailIColl41 = SqlClient.queryList4IColl("queryPspVisitForGDZCByTaskID", task_id_Grp, conn);//(41)小微、公司检查现场检查固定资产
							IndexedCollection bailIColl42 = SqlClient.queryList4IColl("queryPspVisitForJCJLByTaskID", task_id_Grp, conn);//(42)零售、小微、公司检查现场检查现场检查结论
							IndexedCollection bailIColl43 = SqlClient.queryList4IColl("queryPspVisitForDZYByTaskID", task_id_Grp, conn);//(43)小微、公司检查现场检查现场抵（质）押品检查
							IndexedCollection bailIColl44 = SqlClient.queryList4IColl("queryPspVisitForDBQYByTaskID", task_id_Grp, conn);//(44)小微、公司检查现场检查现场担保企业检查
							
							IndexedCollection bailIColl50 = SqlClient.queryList4IColl("queryPspZXForQYGJGRByTaskID", task_id_Grp, conn);//(50)小微、公司检查征信检查借款企业关键个人征信检查 
							IndexedCollection bailIColl51 = SqlClient.queryList4IColl("queryPspZXForBZRByTaskID", task_id_Grp, conn);//(51)小微、公司检查征信检查保证人征信检查
							IndexedCollection bailIColl52 = SqlClient.queryList4IColl("queryPspZXForJKRByTaskID", task_id_Grp, conn);//(52)零售、小微、公司检查征信检查借款人检查
							/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 end */
							/* added by yangzy 2015/1/27  需求:XD141230092,零售贷后检查改造 start */
							IndexedCollection bailIColl53 = SqlClient.queryList4IColl("queryPspZXForBZRGRByTaskID", task_id_Grp, conn);//(51)零售征信检查保证人征信检查
							IndexedCollection bailIColl60 = SqlClient.queryList4IColl("queryPspVisitGrForJKRByTaskID", task_id_Grp, conn);//(40)零售检查现场检查借款人
							IndexedCollection bailIColl61 = SqlClient.queryList4IColl("queryPspVisitGrForDZYByTaskID", task_id_Grp, conn);//(43)零售现场检查现场抵（质）押品检查
							IndexedCollection bailIColl62 = SqlClient.queryList4IColl("queryPspVisitGrForDBQYByTaskID", task_id_Grp, conn);//(44)零售现场检查现场担保检查
							/* added by yangzy 2015/1/27  需求:XD141230092,零售贷后检查改造 end */
							
							if("02".equals(check_type2) && "01".equals(task_type2)){//集团成员为对公客户
								/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 start */
								if(!check_freq2.equals("04") &&(bailIColl36==null||bailIColl36.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入【客户财报】后再进行提交";
									break;
								}else
								if(!check_freq2.equals("04") &&(bailIColl38!=null&&bailIColl38.size()>0)){
									returnFlag = "不通过";
									returnInfo = "请录入【客户担保企业财报】后再进行提交";
									break;
								}else
								/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 end */
								/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
								if(!check_freq2.equals("04") &&(bailIColl==null||bailIColl.size()<1)){
								/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【借款人分析】后再进行提交";
									break;
								}else if(iColl2.size()>0&&(bailIColl1==null||bailIColl1.size()<1)){
									//context.put("proFlag", "Y");
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【固定资产贷款（项目融资）分析】后再进行提交";
									break;
								}else if(bailIColl2==null||bailIColl2.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【资产状况调查】后再进行提交";
									break;
								}else if(!check_freq2.equals("04") && (bailIColl3==null||bailIColl3.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【财务分析】后再进行提交";
									break;
								}else if(!check_freq2.equals("04") && (bailIColl4==null||bailIColl4.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息】后再进行提交";
									break;
								}
								/* modified by yangzy 2014/1/7  需求:XD141222090,公司小微贷后检查改造 start */
								//else if(!check_freq2.equals("04") && (bailIColl23==null||bailIColl23.size()<1)){
								//	returnFlag = "不通过";
								//	returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的税费明细】后再进行提交";
								//	break;
								//}
								else if(!check_freq2.equals("04") && (bailIColl37==null||bailIColl37.size()<3)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息(至少检查3项)】后再进行提交";
									break;
								}
								/* modified by yangzy 2014/1/7  需求:XD141222090,公司小微贷后检查改造 end */
								else if(!check_freq2.equals("04") && (bailIColl28==null||bailIColl28.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的银行对账单】后再进行提交";
									break;
								}
								/* modified by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 start */
								else if(!check_freq2.equals("04") &&(bailIColl5==null||bailIColl5.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【监控信息】后再进行提交";
									break;
								}
								
								else if(grtMortIColl2.size()>0 && (bailIColl6==null||bailIColl6.size()<grtMortIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析担保品】后再进行提交";
									break;
								}else if(grtGuaranteeIColl2.size()>0 && (bailIColl21==null||bailIColl21.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析保证人】后再进行提交";
									break;
								
								}else if(check_freq2.equals("04") && (bailIColl9==null||bailIColl9.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【单一法人风险预警】后再进行提交";
									break;
								}else if(check_freq2.equals("04") && (bailIColl10==null||bailIColl10.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【关注事件】后再进行提交";
									break;
								/* modified by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 end */
								}else if(check_freq2.equals("04") && (bailIColl11==null||bailIColl11.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								}
								/* modified by yangzy 2015/01/22  需求:XD141222090,公司小微贷后检查改造 start */
								else if(bailIColl40==null||bailIColl40.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的借款企业检查后再进行提交";
								}
								else if(iColl2.size()>0&&(bailIColl41==null||bailIColl41.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的固定资产检查后再进行提交";
								}
								else if(bailIColl42==null||bailIColl42.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的检查结论后再进行提交";
								}
								else if(grtMortIColl2.size()>0 && (bailIColl43==null||bailIColl43.size()<grtMortIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的抵（质）押品检查后再进行提交";
								}
								else if(grtGuaranteeIColl2.size()>0 && (bailIColl44==null||bailIColl44.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的担保企业检查后再进行提交";
								}
								else if(bailIColl50==null||bailIColl50.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【征信检查】的借款企业关键个人检查后再进行提交";
								}
								else if(grtGuaranteeIColl2.size()>0 && (bailIColl51==null||bailIColl51.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【征信检查】的保证人检查后再进行提交";
								}
								else if(bailIColl52==null||bailIColl52.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【征信检查】的检查借款人信息后再进行提交";
								}
								/* modified by yangzy 2015/01/22  需求:XD141222090,公司小微贷后检查改造 end */
								else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
							
							}else if("02".equals(check_type2) && "02".equals(task_type2)){//小微客户
								/** modified by yangzy 2015/07/13 小微全额不校验财报检查 start **/
								String full_flag = task_id_Grp.substring(0, 4);
								/** modified by yangzy 2015/07/13 小微全额不校验财报检查 end **/
								/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 start */
								if(!check_freq2.equals("04") &&(bailIColl36==null||bailIColl36.size()<1)){
									/**modified by lisj 2015-2-4 需求编号【XD150123004】 小微部关于贷后检查模块的变更需求 begin**/
									/** modified by yangzy 2015/07/13 小微全额不校验财报检查 start **/
									//String full_flag = task_id_Grp.substring(0, 4);
									/** modified by yangzy 2015/07/13 小微全额不校验财报检查 end **/
									if(!full_flag.equals("SQN9")){
										returnFlag = "不通过";
										returnInfo = "请录入【客户财报】后再进行提交";
										break;
									}
									/**modified by lisj 2015-2-4 需求编号【XD150123004】 小微部关于贷后检查模块的变更需求 end**/
								}else
								if(!check_freq2.equals("04") &&(bailIColl38!=null&&bailIColl38.size()>0)){
									returnFlag = "不通过";
									returnInfo = "请录入【客户担保企业财报】后再进行提交";
									break;
								}else
								/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 end */
								//IndexedCollection bailIColl = SqlClient.queryList4IColl("custabByTaskID", task_id, conn);//(1)借款人分析
								/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
								if(!check_freq2.equals("04") &&(bailIColl==null||bailIColl.size()<1)){
								/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【借款人分析】后再进行提交";
									break;
								}else if(iColl2.size()>0&&(bailIColl1==null||bailIColl1.size()<1)){
									//context.put("proFlag", "Y");
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【固定资产贷款（项目融资）分析】后再进行提交";
									break;
								}else if(bailIColl2==null||bailIColl2.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【资产状况调查】后再进行提交";
									break;
								/** modified by yangzy 2015/07/13 小微全额不校验财报检查 start **/	
								}else if(!full_flag.equals("SQN9") && !check_freq2.equals("04") && (bailIColl24==null||bailIColl24.size()<1)){
								/** modified by yangzy 2015/07/13 小微全额不校验财报检查 end **/
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【财务报表中的财务分析结论】后再进行提交";
									break;
								}else if(!check_freq2.equals("04") && (bailIColl4==null||bailIColl4.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息】后再进行提交";
									break;
								}
								/* modified by yangzy 2014/1/7  需求:XD141222090,公司小微贷后检查改造 start */
								//else if(!check_freq2.equals("04") && (bailIColl23==null||bailIColl23.size()<1)){
								//	returnFlag = "不通过";
								//	returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的税费明细】后再进行提交";
								//	break;
								//}
								else if(!check_freq2.equals("04") && (bailIColl37==null||bailIColl37.size()<3)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息(至少检查3项)】后再进行提交";
									break;
								}
								/* modified by yangzy 2014/1/7  需求:XD141222090,公司小微贷后检查改造 end */
								else if(!check_freq2.equals("04") && (bailIColl28==null||bailIColl28.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的银行对账单】后再进行提交";
									break;
								}
								/* modified by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 start */
								else if(!check_freq2.equals("04") && (bailIColl5==null||bailIColl5.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【监控信息】后再进行提交";
									break;
								}
								
								else if(grtMortIColl2.size()>0 && (bailIColl6==null||bailIColl6.size()<grtMortIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析担保品】后再进行提交";
									break;
								}else if(grtGuaranteeIColl2.size()>0 && (bailIColl21==null||bailIColl21.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析保证人】后再进行提交";
									break;
								
								}else if(check_freq2.equals("04") && (bailIColl9==null||bailIColl9.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【单一法人风险预警】后再进行提交";
									break;
								}else if(check_freq2.equals("04") && (bailIColl10==null||bailIColl10.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【关注事件】后再进行提交";
									break;
								/* modified by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 end */
								}else if(check_freq2.equals("04") && (bailIColl11==null||bailIColl11.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								} 
								/* modified by yangzy 2015/01/22  需求:XD141222090,公司小微贷后检查改造 start */
								else if(bailIColl40==null||bailIColl40.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的借款企业检查后再进行提交";
								}
								else if(iColl2.size()>0&&(bailIColl41==null||bailIColl41.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的固定资产检查后再进行提交";
								}
								else if(bailIColl42==null||bailIColl42.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的检查结论后再进行提交";
								}
								else if(grtMortIColl2.size()>0 && (bailIColl43==null||bailIColl43.size()<grtMortIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的抵（质）押品检查后再进行提交";
								}
								else if(grtGuaranteeIColl2.size()>0 && (bailIColl44==null||bailIColl44.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的担保企业检查后再进行提交";
								}
								else if(bailIColl50==null||bailIColl50.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【征信检查】的借款企业关键个人检查后再进行提交";
								}
								else if(grtGuaranteeIColl2.size()>0 && (bailIColl51==null||bailIColl51.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【征信检查】的保证人检查后再进行提交";
								}
								else if(bailIColl52==null||bailIColl52.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【征信检查】的检查借款人信息后再进行提交";
								}
								/* modified by yangzy 2015/01/22  需求:XD141222090,公司小微贷后检查改造 end */
								else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
							}else if("02".equals(check_type2) && "03".equals(task_type2)){//个人客户
								if(psp_cus_type2.equals("001") && (bailIColl7==null||bailIColl7.size()<1)){//经营性
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【借款人分析】后再进行提交";
									break;
								}else if(psp_cus_type2.equals("002") && (bailIColl12==null||bailIColl12.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【借款人分析】后再进行提交";
									break;
								}else if(grtMortIColl2.size()>0 && (bailIColl6==null||bailIColl6.size()<grtMortIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析担保品】后再进行提交";
									break;	
								}else if(grtGuaranteeIColl2.size()>0 && (bailIColl8==null||bailIColl8.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析保证人】后再进行提交";
									break;
								}
								//else if(bailIColl13==null||bailIColl13.size()<1){
								//	returnFlag = "不通过";
								//	returnInfo = "请录入"+ task_id_Grp+"的【监控信息】后再进行提交";
								//	break;
								//}
								else if(!check_freq2.equals("04") && (psp_cus_type2.equals("001") && (bailIColl22==null||bailIColl22.size()<1))){//水电费
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【水电费明细】后再进行提交";
									break;
								}else if(check_freq2.equals("04") && (bailIColl11==null||bailIColl11.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								}
								/* modified by yangzy 2015/01/27  需求:XD141230092,零售贷后检查改造 start */
								else if(!check_freq2.equals("04") && (bailIColl37==null||bailIColl37.size()<2)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息(至少检查3项)】后再进行提交";
									break;
								}
								else if(grtGuaranteeIColl2.size()>0 && (bailIColl53==null||bailIColl53.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【征信检查】的保证人检查后再进行提交";
								}
								else if(bailIColl52==null||bailIColl52.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【征信检查】的检查借款人信息后再进行提交";
								}
								else if(bailIColl60==null||bailIColl60.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的借款人检查后再进行提交";
								}
								else if(bailIColl42==null||bailIColl42.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的检查结论后再进行提交";
								}
								else if(grtMortIColl2.size()>0 && (bailIColl61==null||bailIColl61.size()<grtMortIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的抵（质）押品检查后再进行提交";
								}
								else if(grtGuaranteeIColl2.size()>0 && (bailIColl62==null||bailIColl62.size()<grtGuaranteeIColl2.size())){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【现场检查】的担保企业检查后再进行提交";
								}
								/* modified by yangzy 2015/01/27  需求:XD141230092,零售贷后检查改造 end */
								else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
							}else if("02".equals(check_type2) && "05".equals(task_type2)){//合作方客户
								if(bailIColl17==null||bailIColl17.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【合作方分析】后再进行提交";
									break;
								}else if(!check_freq2.equals("04") && (bailIColl3==null||bailIColl3.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【财务分析】后再进行提交";
									break;
								}else if(bailIColl9==null||bailIColl9.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【单一法人风险预警】后再进行提交";
									break;
								}else if(bailIColl10==null||bailIColl10.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【关注事件】后再进行提交";
									break;
								}else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
								
							}else if("02".equals(check_type2) && "06".equals(task_type2)){//担保公司客户
								if(bailIColl19==null||bailIColl19.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【融资担保公司分析】后再进行提交";
									break;
								}else if(check_freq2.equals("04") && (bailIColl3==null||bailIColl3.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【财务分析】后再进行提交";
									break;
								}else if(check_freq2.equals("04") && (bailIColl11==null||bailIColl11.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								}else if(bailIColl20==null||bailIColl20.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【综合评价】后再进行提交";
									break;
								}else if(bailIColl9==null||bailIColl9.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【单一法人风险预警】后再进行提交";
									break;
								}else if(bailIColl10==null||bailIColl10.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【关注事件】后再进行提交";
									break;
								}else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
							}else if("02".equals(check_type2) ){
								returnFlag = "通过";
								returnInfo = task_id_Grp+"贷后任务【首次检查】、【专项检查】，不需检查";
							}
						}
					}
					//added by yangzy 2015/09/25 贷后集团任务检查过滤已完成的成员任务 start
					else{
						returnFlag = "通过";
						returnInfo = "成员任务检查通过";
					}
					//added by yangzy 2015/09/25 贷后集团任务检查过滤已完成的成员任务 end
			}else if("03".equals(check_type) && "04".equals(task_type)){//专项检查的集团客户
				 if(iColl != null && iColl.size()>0){//检查成员信息
						for(int i=0;i<iColl.size();i++){
							KeyedCollection kColl = (KeyedCollection) iColl.get(i);
							String task_id_Grp = (String) kColl.getDataValue("task_id");//集团下每个成员的task_id
						//	String condition2 = " where task_id='"+task_id_Grp+"' ";
							String cus_id_Grp = (String) kColl.getDataValue("cus_id");//集团下每个成员的cus_id
							String check_type2 = (String) kColl.getDataValue("check_type");
							String task_type2 = (String) kColl.getDataValue("task_type");
							
							String psp_cus_type2 = (String) kColl.getDataValue("psp_cus_type");
							String check_freq2 = (String) kColl.getDataValue("check_freq");
							if(check_freq2 == null){
								check_freq2 = " ";
							}
							String condition2 = " where cus_id='"+cus_id_Grp+"' and prd_id in('100058','100057','100061')";
							IndexedCollection iColl2 = dao.queryList("AccLoan", condition2, connection);
							
							IndexedCollection grtMortIColl2 = new IndexedCollection();//担保品信息
							IndexedCollection grtGuaranteeIColl2 = new IndexedCollection();//保证人信息
							CMISModualServiceFactory serviceJndi2 = CMISModualServiceFactory.getInstance();
							GrtServiceInterface service2 = (GrtServiceInterface)serviceJndi2.getModualServiceById("grtServices", "iqp");
							grtGuaranteeIColl2 = service2.getGrtListByCusId(cus_id_Grp, null, "3", dataSource);
							grtMortIColl2 = service2.getGrtListByCusId(cus_id_Grp, null, "4", dataSource);
							
							
							IndexedCollection bailIColl = SqlClient.queryList4IColl("custabByTaskID", task_id_Grp, conn);//(1)借款人分析
							IndexedCollection bailIColl1 = SqlClient.queryList4IColl("propertytabByTaskID", task_id_Grp, conn);//(2)固定资产贷款（项目融资）分析
							IndexedCollection bailIColl2 = SqlClient.queryList4IColl("propertyanalytabByTaskID", task_id_Grp, conn);//(3)资产状况调查
							IndexedCollection bailIColl3 = SqlClient.queryList4IColl("fnctabByTaskID", task_id_Grp, conn);//(4)财务分析
							IndexedCollection bailIColl4 = SqlClient.queryList4IColl("opertabByTaskID", task_id_Grp, conn);//(5)经营佐证信息
							IndexedCollection bailIColl5 = SqlClient.queryList4IColl("montabByTaskID", task_id_Grp, conn);//(6)监控信息
					//		IndexedCollection bailIColl6 = SqlClient.queryList4IColl("grttabByTaskID", task_id, conn);//(7)担保分析，分析担保品
							IndexedCollection bailIColl8 = SqlClient.queryList4IColl("grttabPersonByTaskID", task_id_Grp, conn);//(9)担保分析，分析保证人-个人
							IndexedCollection bailIColl9 = SqlClient.queryList4IColl("sigltabByTaskID", task_id_Grp, conn);//(10)单一法人风险预警
							IndexedCollection bailIColl10 = SqlClient.queryList4IColl("evttabByTaskID", task_id_Grp, conn);//(11)关注事件
							IndexedCollection bailIColl11 = SqlClient.queryList4IColl("alttabByTaskID", task_id_Grp, conn);//(12)预警信息
							IndexedCollection bailIColl7 = SqlClient.queryList4IColl("custabPersonByTaskID", task_id_Grp, conn);//(8)借款人分析-零售-经营性
							IndexedCollection bailIColl12 = SqlClient.queryList4IColl("custabPersonXFByTaskID", task_id_Grp, conn);//(13)借款人分析-零售-消费性
							IndexedCollection bailIColl13 = SqlClient.queryList4IColl("montabPersonByTaskID", task_id_Grp, conn);//(14)监控信息-个人客户
							//IndexedCollection bailIColl14 = SqlClient.queryList4IColl("alttabByTaskID", task_id, conn);//(15)成员检查信息
							IndexedCollection bailIColl17 = SqlClient.queryList4IColl("custabHZByTaskID", task_id_Grp, conn);//(18)合作方分析
							IndexedCollection bailIColl18 = SqlClient.queryList4IColl("evltabByTaskID", task_id_Grp, conn);//(19)合作方综合评价
							IndexedCollection bailIColl19 = SqlClient.queryList4IColl("custabRZByTaskID", task_id_Grp, conn);//(20)融资担保公司分析
							IndexedCollection bailIColl20 = SqlClient.queryList4IColl("evltabDBByTaskID", task_id_Grp, conn);//(21)担保公司客户综合评价
							IndexedCollection bailIColl21 = SqlClient.queryList4IColl("grttabDGByTaskID", task_id_Grp, conn);//(22)担保分析中的分析保证人-对公客户、小微客户、集团客户
							IndexedCollection bailIColl6 = SqlClient.queryList4IColl("grttabByTaskID", task_id_Grp, conn);//(7)担保分析，分析担保品
							IndexedCollection bailIColl22 = SqlClient.queryList4IColl("water_charge_analyByTaskID", task_id_Grp, conn);//(23)零售客户水电费
							IndexedCollection bailIColl23 = SqlClient.queryList4IColl("opertabTaxByTaskID", task_id, conn);//(24)经营佐证中的税费明细校验
							IndexedCollection bailIColl28 = SqlClient.queryList4IColl("opertabTaxYHDZByTaskID", task_id, conn);//(29)经营佐证中的银行对账单
							if("03".equals(check_type2) && "01".equals(task_type2)){//集团成员为对公客户
								if(bailIColl==null||bailIColl.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【借款人分析】后再进行提交";
									break;
								}else if(iColl2.size()>0&&(bailIColl1==null||bailIColl1.size()<1)){
									//context.put("proFlag", "Y");
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【固定资产贷款（项目融资）分析】后再进行提交";
									break;
								}else if(bailIColl2==null||bailIColl2.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【资产状况调查】后再进行提交";
									break;
								}else if(bailIColl3==null||bailIColl3.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【财务分析】后再进行提交";
									break;
								}else if(bailIColl4==null||bailIColl4.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息】后再进行提交";
									break;
								}else if(bailIColl23==null||bailIColl23.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的税费明细】后再进行提交";
									break;
								}else if(bailIColl28==null||bailIColl28.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的银行对账单】后再进行提交";
								}else if(bailIColl5==null||bailIColl5.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【监控信息】后再进行提交";
									break;
								}else if(grtMortIColl2.size()>0 && (bailIColl6==null||bailIColl6.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析担保品】后再进行提交";
									break;	
								}else if(grtGuaranteeIColl2.size()>0 && (bailIColl21==null||bailIColl21.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析保证人】后再进行提交";
									break;
								}else if(bailIColl11==null||bailIColl11.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								}else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
							
							}else if("03".equals(check_type2) && "02".equals(task_type2)){//小微客户
								//IndexedCollection bailIColl = SqlClient.queryList4IColl("custabByTaskID", task_id, conn);//(1)借款人分析
								if(bailIColl==null||bailIColl.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【借款人分析】后再进行提交";
									break;
								}else if(iColl2.size()>0&&(bailIColl1==null||bailIColl1.size()<1)){
									//context.put("proFlag", "Y");
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【固定资产贷款（项目融资）分析】后再进行提交";
									break;
								}else if(bailIColl2==null||bailIColl2.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【资产状况调查】后再进行提交";
									break;
								}else if(bailIColl4==null||bailIColl4.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息】后再进行提交";
									break;
								}else if(bailIColl23==null||bailIColl23.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的税费明细】后再进行提交";
									break;
								}else if(bailIColl28==null||bailIColl28.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的银行对账单】后再进行提交";
								}else if(bailIColl5==null||bailIColl5.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【监控信息】后再进行提交";
									break;
								}else if(grtMortIColl2.size()>0 && (bailIColl6==null||bailIColl6.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析担保品】后再进行提交";
									break;	
								}else if(grtGuaranteeIColl2.size()>0 && (bailIColl21==null||bailIColl21.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析保证人】后再进行提交";
									break;
								}else if(bailIColl11==null||bailIColl11.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								} else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
							}else if("03".equals(check_type2) && "03".equals(task_type2)){//个人客户
								if(psp_cus_type2.equals("001") && (bailIColl7==null||bailIColl7.size()<1)){//经营性
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【借款人分析】后再进行提交";
									break;
								}else if(psp_cus_type2.equals("002") && (bailIColl12==null||bailIColl12.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【借款人分析】后再进行提交";
									break;
								}else if(grtMortIColl2.size()>0 && (bailIColl6==null||bailIColl6.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析担保品】后再进行提交";
									break;	
								}else if(grtGuaranteeIColl2.size()>0 && (bailIColl8==null||bailIColl8.size()<1)){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【担保分析，分析保证人】后再进行提交";
									break;
								}else if(bailIColl13==null||bailIColl13.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【监控信息】后再进行提交";
									break;
								}else if(psp_cus_type2.equals("001") && (bailIColl4==null||bailIColl4.size()<1)){//水电费
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息】后再进行提交";
									break;
								}else if(bailIColl23==null||bailIColl23.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的税费明细】后再进行提交";
									break;
								}else if(bailIColl28==null||bailIColl28.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【经营佐证信息中的银行对账单】后再进行提交";
								}else if(bailIColl11==null||bailIColl11.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								}else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
							}else if("03".equals(check_type2) && "05".equals(task_type2)){//合作方客户
								if(bailIColl17==null||bailIColl17.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【合作方分析】后再进行提交";
									break;
								}else if(bailIColl3==null||bailIColl3.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【财务分析】后再进行提交";
									break;
								}else if(bailIColl18==null||bailIColl18.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【综合评价】后再进行提交";
									break;
								}else if(bailIColl11==null||bailIColl11.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								}else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
								
							}else if("03".equals(check_type2) && "06".equals(task_type2)){//担保公司客户
								if(bailIColl19==null||bailIColl19.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【融资担保公司分析】后再进行提交";
									break;
								}else if(bailIColl3==null||bailIColl3.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【财务分析】后再进行提交";
									break;
								}else if(bailIColl11==null||bailIColl11.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【预警信息】后再进行提交";
									break;
								}else if(bailIColl20==null||bailIColl20.size()<1){
									returnFlag = "不通过";
									returnInfo = "请录入"+ task_id_Grp+"的【综合评价】后再进行提交";
									break;
								}else{
									returnFlag = "通过";
									returnInfo = "提交前必输项校验通过";
								}
							}else if("03".equals(check_type2) ){
								returnFlag = "通过";
								returnInfo = task_id_Grp+"贷后任务【首次检查】、【专项检查】，不需检查";
							}
						}
					}
					//added by yangzy 2015/09/25 贷后集团任务检查过滤已完成的成员任务 start
					else{
						returnFlag = "通过";
						returnInfo = "成员任务检查通过";
					}
					//added by yangzy 2015/09/25 贷后集团任务检查过滤已完成的成员任务 end
			}else{
				returnFlag = "通过";
				returnInfo = "贷后任务【首次检查】、【专项检查】，不需检查";
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}
			
			//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，调用规则检查结束", null);
			//logger.info("融资性担保公司授信额度及限额检查，调用规则检查结束");			
			returnMap.put("OUT_是否通过", returnFlag);
			returnMap.put("OUT_提示信息", returnInfo);
	
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "贷后任务必输检查失败！"+e.getMessage(), null);
			logger.error("贷后任务必输检查失败！"+e.getStackTrace());
			throw new EMPException(e);
		} finally {
			if (conn != null)
				this.releaseConnection(dataSource, conn);
		}
		
		return returnMap;
	}
	

	/**
	 * 获取数据库连接
	 * 
	 * @param context
	 * @param dataSource
	 * @return
	 * @throws EMPJDBCException
	 * @throws SessionException 
	 */
	private Connection getConnection(Context context, DataSource dataSource)
			throws EMPJDBCException, SessionException {
		if (dataSource == null)
			throw new SessionException("登陆超时，请重新登陆或联系管理员 !"
					+ this.toString());
		Connection connection = null;
		connection = ConnectionManager.getConnection(dataSource);
		
		EMPLog.log( EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Apply new connection from data source: "+dataSource+" success!");
		return connection;
	}
	
	/**
	 * 释放数据库连接
	 * 
	 * @param dataSource
	 * @param connection
	 * @throws EMPJDBCException
	 */
	private void releaseConnection(DataSource dataSource, Connection connection)
			throws EMPJDBCException {
		ConnectionManager.releaseConnection(dataSource, connection);
		EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Do release the connection from data source: " + dataSource + " success!");
	}
	
	
}
