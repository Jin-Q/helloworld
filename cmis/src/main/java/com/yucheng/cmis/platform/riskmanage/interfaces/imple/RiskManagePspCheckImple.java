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

public class RiskManagePspCheckImple implements RiskManageInterface{

	private final String modelId = "PspCheckTask";//贷后任务表
	private static final Logger logger = Logger.getLogger(RiskManagePspCheckImple.class);
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
			String cus_id = (String) appkColl.getDataValue("cus_id");
			String psp_cus_type = (String) appkColl.getDataValue("psp_cus_type");
			String check_freq = (String) appkColl.getDataValue("check_freq");//检查频率
			String batch_task_type = (String) appkColl.getDataValue("batch_task_type");
			if(check_freq == null){
				check_freq = " ";
			}
			if(batch_task_type == null){
				batch_task_type = " ";
			}
			/**modified by lisj 2015-1-14 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能 begin**/
			if("09".equals(task_type)){
				//检查该批量任务【检查时间】及【意见】内容
				Object MResult = (Object) SqlClient.queryFirst("checkPspBatchTaskInfoByTaskId", task_id, null, conn);
				if("0".equals(MResult.toString())){
					returnFlag = "不通过";
					returnInfo += "该批量任务未录入【检查时间】及【意见】内容";
					returnMap.put("OUT_是否通过", returnFlag);
					returnMap.put("OUT_提示信息", returnInfo);		
					return returnMap;
				}
				IndexedCollection SPBT = SqlClient.queryList4IColl("querySubPspBatchTaskListByTaskId", task_id, conn);
				if(SPBT!=null && SPBT.size()>0){
					if("02".equals(batch_task_type)){
						for(int i=0;i<SPBT.size();i++){
							KeyedCollection temp = (KeyedCollection) SPBT.get(i);
							String sub_task_id = (String) temp.getDataValue("sub_task_id");
							Object SResult = (Object) SqlClient.queryFirst("checkPspCheckTaskInfoByTaskId", sub_task_id, null, conn);
							if("0".equals(SResult.toString())){
								returnFlag = "不通过";
								returnInfo += "该批量任务下子任务编号：【"+sub_task_id+"】未录入【检查时间】及【意见】内容;";
							}
							if(returnInfo!=null && !"".equals(returnInfo) && returnInfo.length()> 300){
								returnInfo = returnInfo.substring(0, 264)+"...";
							}
						}
					}
					//子任务明细均有录入【检查时间】及【意见】内容
					if(returnFlag.equals("") && returnInfo.equals("")){
						returnFlag = "通过";
						returnInfo = "该批量任务下子任务的【检查时间】、【意见】均有录入，检查通过";
					}
				}else{
					returnFlag = "不通过";
					returnInfo = "该批量任务没有引入子任务明细，检查不通过";
						
				}
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);		
				return returnMap;
			}else{
				String condition = " where cus_id='"+cus_id+"' and prd_id in('100058','100057','100061')";
				IndexedCollection iColl = dao.queryList("AccLoan", condition, connection);
				
				IndexedCollection grtMortIColl = new IndexedCollection();//担保品信息
				IndexedCollection grtGuaranteeIColl = new IndexedCollection();//保证人信息
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "iqp");
				
				/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
				IndexedCollection TaskIColl = new IndexedCollection();//贷后信息
				TaskIColl = dao.queryList("PspCheckTask", "where task_create_date >= '2015-06-05' and task_id ='"+task_id+"'", connection);
				
				/**modified by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
				grtGuaranteeIColl = dao.queryList("PspGuarAnalyRel", "where grt_type='3' and task_id ='"+task_id+"'", connection);
				if(TaskIColl ==null || TaskIColl.size()<=0){
					grtGuaranteeIColl = service.getGrtListByCusId(cus_id, null, "3", dataSource);
				}
				grtMortIColl = dao.queryList("PspGuarAnalyRel", "where grt_type='4' and task_id ='"+task_id+"'", connection);
				if(TaskIColl ==null || TaskIColl.size()<=0){
					grtMortIColl = service.getGrtListByCusId(cus_id, null, "4", dataSource);
				}
				/**modified by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
				/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 end**/
				
				IndexedCollection bailIColl = SqlClient.queryList4IColl("custabByTaskID", task_id, conn);//(1)借款人分析
				IndexedCollection bailIColl1 = SqlClient.queryList4IColl("propertytabByTaskID", task_id, conn);//(2)固定资产贷款（项目融资）分析
				IndexedCollection bailIColl2 = SqlClient.queryList4IColl("propertyanalytabByTaskID", task_id, conn);//(3)资产状况调查
				IndexedCollection bailIColl3 = SqlClient.queryList4IColl("fnctabByTaskID", task_id, conn);//(4)财务分析
				IndexedCollection bailIColl4 = SqlClient.queryList4IColl("opertabByTaskID", task_id, conn);//(5)经营佐证信息
				IndexedCollection bailIColl5 = SqlClient.queryList4IColl("montabByTaskID", task_id, conn);//(6)监控信息
		//		IndexedCollection bailIColl6 = SqlClient.queryList4IColl("grttabByTaskID", task_id, conn);//(7)担保分析，分析担保品
				IndexedCollection bailIColl8 = SqlClient.queryList4IColl("grttabPersonByTaskID", task_id, conn);//(9)担保分析，分析保证人-个人
				IndexedCollection bailIColl9 = SqlClient.queryList4IColl("sigltabByTaskID", task_id, conn);//(10)单一法人风险预警
				IndexedCollection bailIColl10 = SqlClient.queryList4IColl("evttabByTaskID", task_id, conn);//(11)关注事件
				IndexedCollection bailIColl11 = SqlClient.queryList4IColl("alttabByTaskID", task_id, conn);//(12)预警信息
				IndexedCollection bailIColl7 = SqlClient.queryList4IColl("custabPersonByTaskID", task_id, conn);//(8)借款人分析-零售-经营性
				IndexedCollection bailIColl12 = SqlClient.queryList4IColl("custabPersonXFByTaskID", task_id, conn);//(13)借款人分析-零售-消费性
				IndexedCollection bailIColl13 = SqlClient.queryList4IColl("montabPersonByTaskID", task_id, conn);//(14)监控信息-个人客户
				//IndexedCollection bailIColl14 = SqlClient.queryList4IColl("alttabByTaskID", task_id, conn);//(15)成员检查信息
				IndexedCollection bailIColl15 = SqlClient.queryList4IColl("sigltabJTByTaskID", task_id, conn);//(16)集团风险预警
				IndexedCollection bailIColl16 = SqlClient.queryList4IColl("evttabJTByTaskID", task_id, conn);//(17)集团关注事件
				IndexedCollection bailIColl17 = SqlClient.queryList4IColl("custabHZByTaskID", task_id, conn);//(18)合作方分析
				IndexedCollection bailIColl18 = SqlClient.queryList4IColl("evltabByTaskID", task_id, conn);//(19)合作方综合评价
				IndexedCollection bailIColl19 = SqlClient.queryList4IColl("custabRZByTaskID", task_id, conn);//(20)融资担保公司分析
				IndexedCollection bailIColl20 = SqlClient.queryList4IColl("evltabDBByTaskID", task_id, conn);//(21)担保公司客户综合评价
				IndexedCollection bailIColl21 = SqlClient.queryList4IColl("grttabDGByTaskID", task_id, conn);//(22)担保分析中的分析保证人-对公客户、小微客户、集团客户
				IndexedCollection bailIColl6 = SqlClient.queryList4IColl("grttabByTaskID", task_id, conn);//(7)担保分析，分析担保品
				IndexedCollection bailIColl22 = SqlClient.queryList4IColl("water_charge_analyByTaskID", task_id, conn);//(23)零售客户水电费
				IndexedCollection bailIColl23 = SqlClient.queryList4IColl("opertabTaxByTaskID", task_id, conn);//(24)经营佐证中的税费明细校验
				
				IndexedCollection bailIColl24 = SqlClient.queryList4IColl("finatabByTaskID", task_id, conn);//(25)财务报表中的财务分析结论
				
				IndexedCollection bailIColl25 = SqlClient.queryList4IColl("opertabTaxSDFByTaskID", task_id, conn);//(26)经营佐证中的水电费明细
				IndexedCollection bailIColl26 = SqlClient.queryList4IColl("propertyanalytabGDZCByTaskID", task_id, conn);//(27)资产状况调查中的固定资产分析
				IndexedCollection bailIColl27 = SqlClient.queryList4IColl("opertabTaxByTaskID", task_id, conn);//(28)经营佐证中的税费明细校验
				IndexedCollection bailIColl28 = SqlClient.queryList4IColl("opertabTaxYHDZByTaskID", task_id, conn);//(29)经营佐证中的银行对账单
				IndexedCollection bailIColl29 = SqlClient.queryList4IColl("opertabTaxGZDZByTaskID", task_id, conn);//(30)经营佐证中的工资对账单
				IndexedCollection bailIColl30 = SqlClient.queryList4IColl("opertabTaxCRKByTaskID", task_id, conn);//(31)经营佐证中的出入库
				IndexedCollection bailIColl31 = SqlClient.queryList4IColl("opertabTaxDDMXByTaskID", task_id, conn);//(32)经营佐证中的订单明细
				
				IndexedCollection bailIColl32 = SqlClient.queryList4IColl("montabAddByTaskID", task_id, conn);//(33)监控信息新增
				IndexedCollection bailIColl33 = SqlClient.queryList4IColl("montabHKByTaskID", task_id, conn);//(34)监控信息还款来源信息
				
				IndexedCollection bailIColl34 = SqlClient.queryList4IColl("capusetabByTaskID", task_id, conn);//(35)首次检查，资金用途监控
				IndexedCollection bailIColl35 = SqlClient.queryList4IColl("resulttabByTaskID", task_id, conn);//(36)首次检查，检查情况(对公、小微、零售)
			//	IndexedCollection iCollGrp = SqlClient.queryList4IColl("queryGrpListForPsp", task_id, connection);//获取集团成员信息
				/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 start */
				IndexedCollection bailIColl36 = SqlClient.queryList4IColl("queryFncByTaskID", task_id, conn);//(36)小微、公司检查当前财报
				IndexedCollection bailIColl37 = SqlClient.queryList4IColl("queryPspPropertyAnalyByTaskID", task_id, conn);//(37)小微、公司检查经营佐证
				KeyedCollection kcoll_param = new KeyedCollection();
				/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
				IndexedCollection bailIColl38 = new IndexedCollection();
				if(TaskIColl ==null || TaskIColl.size()<=0){
					kcoll_param.put("cus_id", cus_id);
					kcoll_param.put("task_id", task_id);
					bailIColl38  = SqlClient.queryList4IColl("queryGrtGuaranteeFncMsgByTaskID", kcoll_param, connection);//(38)小微、公司检查担保企业财报
				}else{
					kcoll_param.put("task_id", task_id);
					bailIColl38  = SqlClient.queryList4IColl("queryGrtGuaranteeFncMsgByTaskID2", kcoll_param, connection);//(38)小微、公司检查担保企业财报
				}
				/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 end**/
				IndexedCollection bailIColl40 = SqlClient.queryList4IColl("queryPspVisitForJKRByTaskID", task_id, conn);//(40)小微、公司检查现场检查借款企业
				IndexedCollection bailIColl41 = SqlClient.queryList4IColl("queryPspVisitForGDZCByTaskID", task_id, conn);//(41)小微、公司检查现场检查固定资产
				IndexedCollection bailIColl42 = SqlClient.queryList4IColl("queryPspVisitForJCJLByTaskID", task_id, conn);//(42)零售、小微、公司检查现场检查现场检查结论
				IndexedCollection bailIColl43 = SqlClient.queryList4IColl("queryPspVisitForDZYByTaskID", task_id, conn);//(43)小微、公司检查现场检查现场抵（质）押品检查
				IndexedCollection bailIColl44 = SqlClient.queryList4IColl("queryPspVisitForDBQYByTaskID", task_id, conn);//(44)小微、公司检查现场检查现场担保企业检查
				
				IndexedCollection bailIColl50 = SqlClient.queryList4IColl("queryPspZXForQYGJGRByTaskID", task_id, conn);//(50)小微、公司检查征信检查借款企业关键个人征信检查 
				IndexedCollection bailIColl51 = SqlClient.queryList4IColl("queryPspZXForBZRByTaskID", task_id, conn);//(51)小微、公司检查征信检查保证人征信检查
				IndexedCollection bailIColl52 = SqlClient.queryList4IColl("queryPspZXForJKRByTaskID", task_id, conn);//(52)零售、小微、公司检查征信检查借款人检查
				
				/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 end */
				
				/* added by yangzy 2015/1/27  需求:XD141230092,零售贷后检查改造 start */
				IndexedCollection bailIColl53 = SqlClient.queryList4IColl("queryPspZXForBZRGRByTaskID", task_id, conn);//(51)零售征信检查保证人征信检查
				IndexedCollection bailIColl60 = SqlClient.queryList4IColl("queryPspVisitGrForJKRByTaskID", task_id, conn);//(40)零售检查现场检查借款人
				IndexedCollection bailIColl61 = SqlClient.queryList4IColl("queryPspVisitGrForDZYByTaskID", task_id, conn);//(43)零售现场检查现场抵（质）押品检查
				IndexedCollection bailIColl62 = SqlClient.queryList4IColl("queryPspVisitGrForDBQYByTaskID", task_id, conn);//(44)零售现场检查现场担保检查
				/* added by yangzy 2015/1/27  需求:XD141230092,零售贷后检查改造 end */
				/* added by yangzy 2015/4/23  零售贷后检查改造,个人经营性贷款经营佐证资料变更 start */
				IndexedCollection bailIColl63 = SqlClient.queryList4IColl("queryPspCheckTask4GRJYX", task_id, conn);//(44)零售现场检查现场担保检查
				/* added by yangzy 2015/1/23  零售贷后检查改造,个人经营性贷款经营佐证资料变更 end */
				if("01".equals(check_type)){//首次检查
					if("01".equals(task_type)){//对公客户
						if(bailIColl34==null||bailIColl34.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【资金用途监控】后再进行提交";
						}else if(bailIColl35==null||bailIColl35.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【检查情况】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else if("02".equals(task_type)){//小微客户
						if(bailIColl34==null||bailIColl34.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【资金用途监控】后再进行提交";
						}else if(bailIColl35==null||bailIColl35.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【检查情况】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else if("03".equals(task_type)){//零售客户
						if(bailIColl34==null||bailIColl34.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【资金用途监控】后再进行提交";
						}else if(bailIColl35==null||bailIColl35.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【检查情况】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else{
						returnFlag = "通过";
						returnInfo = "提交前必输项校验通过";
					}
				}else if("02".equals(check_type)){//定期检查
					if("01".equals(task_type)){//对公客户
						/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 start */
						if(!check_freq.equals("04") &&(bailIColl36==null||bailIColl36.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【客户财报】后再进行提交";
						}else
						if(!check_freq.equals("04") &&(bailIColl38!=null&&bailIColl38.size()>0)){
							returnFlag = "不通过";
							returnInfo = "请录入【客户担保企业财报】后再进行提交";
						}else
						/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 end */
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
						if(!check_freq.equals("04") &&(bailIColl==null||bailIColl.size()<1)){
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
							returnFlag = "不通过";
							returnInfo = "请录入【借款人分析】后再进行提交";
						}else if(iColl.size()>0&&(bailIColl1==null||bailIColl1.size()<1)){
							//context.put("proFlag", "Y");
							returnFlag = "不通过";
							returnInfo = "请录入【固定资产贷款（项目融资）分析】后再进行提交";
						}else if(bailIColl2==null||bailIColl2.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【资产状况调查】后再进行提交";
						}else if(!check_freq.equals("04") && (bailIColl3==null||bailIColl3.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【财务分析】后再进行提交";
						}else if(!check_freq.equals("04") && (bailIColl4==null||bailIColl4.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息】后再进行提交";
						}
						/* modified by yangzy 2014/1/7  需求:XD141222090,公司小微贷后检查改造 start */
						//else if(!check_freq.equals("04") && (bailIColl23==null||bailIColl23.size()<1)){
						//	returnFlag = "不通过";
						//	returnInfo = "请录入【经营佐证信息中的税费明细】后再进行提交";
						//}
						else if(!check_freq.equals("04") && (bailIColl37==null||bailIColl37.size()<3)){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息(至少检查3项)】后再进行提交";
						}
						/* modified by yangzy 2014/1/7  需求:XD141222090,公司小微贷后检查改造 end */
						else if(!check_freq.equals("04") && (bailIColl28==null||bailIColl28.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息中的银行对账单】后再进行提交";
						}
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
						else if(!check_freq.equals("04") && (bailIColl5==null||bailIColl5.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【监控信息】后再进行提交";
						}
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
						/* modified by yangzy 2014/10/17 贷后改造 begin*/
						else if(grtMortIColl.size()>0 && (bailIColl6==null||bailIColl6.size()<grtMortIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析担保品】后再进行提交";
								
						}else if(grtGuaranteeIColl.size()>0 && (bailIColl21==null||bailIColl21.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析保证人】后再进行提交";
						}
						/* modified by yangzy 2014/10/17 贷后改造 end*/
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
						else if(check_freq.equals("04") && (bailIColl9==null||bailIColl9.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【单一法人风险预警】后再进行提交";
						}else if(check_freq.equals("04") && (bailIColl10==null||bailIColl10.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【关注事件】后再进行提交";
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
						}else if(check_freq.equals("04") && (bailIColl11==null||bailIColl11.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						}
						/* modified by yangzy 2015/01/22  需求:XD141222090,公司小微贷后检查改造 start */
						else if(bailIColl40==null||bailIColl40.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的借款企业检查后再进行提交";
						}
						else if(iColl.size()>0&&(bailIColl41==null||bailIColl41.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的固定资产检查后再进行提交";
						}
						else if(bailIColl42==null||bailIColl42.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的检查结论后再进行提交";
						}
						else if(grtMortIColl.size()>0 && (bailIColl43==null||bailIColl43.size()<grtMortIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的抵（质）押品检查后再进行提交";
						}
						else if(grtGuaranteeIColl.size()>0 && (bailIColl44==null||bailIColl44.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的担保企业检查后再进行提交";
						}
						else if(bailIColl50==null||bailIColl50.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【征信检查】的借款企业关键个人检查后再进行提交";
						}
						else if(grtGuaranteeIColl.size()>0 && (bailIColl51==null||bailIColl51.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【征信检查】的保证人检查后再进行提交";
						}
						else if(bailIColl52==null||bailIColl52.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【征信检查】的检查借款人信息后再进行提交";
						}
						/* modified by yangzy 2015/01/22  需求:XD141222090,公司小微贷后检查改造 end */
						else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					
					}else if("02".equals(task_type)){//小微客户
						/** modified by yangzy 2015/07/13 小微全额不校验财报检查 start **/
						String full_flag = task_id.substring(0, 4);
						/** modified by yangzy 2015/07/13 小微全额不校验财报检查 end **/
						/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 start */
						if(!check_freq.equals("04") &&(bailIColl36==null||bailIColl36.size()<1)){
							/**modified by lisj 2015-1-28 需求编号【XD150123004】 小微部关于贷后检查模块的变更需求 begin**/
							/** modified by yangzy 2015/07/13 小微全额不校验财报检查 start **/
							//String full_flag = task_id.substring(0, 4);
							/** modified by yangzy 2015/07/13 小微全额不校验财报检查 end **/
							if(full_flag.equals("SQN9")){
								returnFlag = "通过";
								returnInfo = "全额抵押类业务不需要检查【客户财报】，检查通过";
							}else{
								returnFlag = "不通过";
								returnInfo = "请录入【客户财报】后再进行提交";
							}
							/**modified by lisj 2015-1-28 需求编号【XD150123004】 小微部关于贷后检查模块的变更需求 end**/
						}else
						if(!check_freq.equals("04") &&(bailIColl38!=null&&bailIColl38.size()>0)){
							returnFlag = "不通过";
							returnInfo = "请录入【客户担保企业财报】后再进行提交";
						}else
						/* added by yangzy 2015/1/7  需求:XD141222090,公司小微贷后检查改造 end */
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
						if(!check_freq.equals("04") &&(bailIColl==null||bailIColl.size()<1)){
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
							returnFlag = "不通过";
							returnInfo = "请录入【借款人分析】后再进行提交";
						}else if(iColl.size()>0&&(bailIColl1==null||bailIColl1.size()<1)){
							//context.put("proFlag", "Y");
							returnFlag = "不通过";
							returnInfo = "请录入【固定资产贷款（项目融资）分析】后再进行提交";
						}else if(bailIColl2==null||bailIColl2.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【资产状况调查】后再进行提交";
						/** modified by yangzy 2015/07/13 小微全额不校验财报检查 start **/
						}else if(!full_flag.equals("SQN9") && !check_freq.equals("04") && (bailIColl24==null||bailIColl24.size()<1)){
						/** modified by yangzy 2015/07/13 小微全额不校验财报检查 end **/
							returnFlag = "不通过";
							returnInfo = "请录入【财务报表中的财务分析结论】后再进行提交";
						}else if(!check_freq.equals("04") && (bailIColl4==null||bailIColl4.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息】后再进行提交";
						}
						/* modified by yangzy 2014/1/7  需求:XD141222090,公司小微贷后检查改造 start */
						//else if(!check_freq.equals("04") && (bailIColl23==null||bailIColl23.size()<1)){
						//	returnFlag = "不通过";
						//	returnInfo = "请录入【经营佐证信息中的税费明细】后再进行提交";
						//}
						else if(!check_freq.equals("04") && (bailIColl37==null||bailIColl37.size()<3)){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息(至少检查3项)】后再进行提交";
						}
						/* modified by yangzy 2014/1/7  需求:XD141222090,公司小微贷后检查改造 end */
						else if(!check_freq.equals("04") && (bailIColl28==null||bailIColl28.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息中的银行对账单】后再进行提交";
						}
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
						else if(!check_freq.equals("04") && (bailIColl5==null||bailIColl5.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【监控信息】后再进行提交";
						}
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
						/* modified by yangzy 2014/10/17 贷后改造 begin*/
						else if(grtMortIColl.size()>0 && (bailIColl6==null||bailIColl6.size()<grtMortIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析担保品】后再进行提交";
								
						}else if(grtGuaranteeIColl.size()>0 && (bailIColl21==null||bailIColl21.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析保证人】后再进行提交";
						}
						/* modified by yangzy 2014/10/17 贷后改造 end*/
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start */
						else if(check_freq.equals("04") && (bailIColl9==null||bailIColl9.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【单一法人风险预警】后再进行提交";
						}else if(check_freq.equals("04") && (bailIColl10==null||bailIColl10.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【关注事件】后再进行提交";
						/* modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end */
						}else if(check_freq.equals("04") && (bailIColl11==null||bailIColl11.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						} 
						/* modified by yangzy 2015/01/22  需求:XD141222090,公司小微贷后检查改造 start */
						else if(bailIColl40==null||bailIColl40.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的借款企业检查后再进行提交";
						}
						else if(iColl.size()>0&&(bailIColl41==null||bailIColl41.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的固定资产检查后再进行提交";
						}
						else if(bailIColl42==null||bailIColl42.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的检查结论后再进行提交";
						}
						else if(grtMortIColl.size()>0 && (bailIColl43==null||bailIColl43.size()<grtMortIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的抵（质）押品检查后再进行提交";
						}
						else if(grtGuaranteeIColl.size()>0 && (bailIColl44==null||bailIColl44.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的担保企业检查后再进行提交";
						}
						else if(bailIColl50==null||bailIColl50.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【征信检查】的借款企业关键个人检查后再进行提交";
						}
						else if(grtGuaranteeIColl.size()>0 && (bailIColl51==null||bailIColl51.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【征信检查】的保证人检查后再进行提交";
						}
						else if(bailIColl52==null||bailIColl52.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【征信检查】的检查借款人信息后再进行提交";
						}
						/* modified by yangzy 2015/01/22  需求:XD141222090,公司小微贷后检查改造 end */
						else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else if("03".equals(task_type)){//个人客户
						if(psp_cus_type.equals("001") && (bailIColl7==null||bailIColl7.size()<1)){//经营性
							returnFlag = "不通过";
							returnInfo = "请录入【借款人分析】后再进行提交";
						}else if(psp_cus_type.equals("002") && (bailIColl12==null||bailIColl12.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【借款人分析】后再进行提交";
						}
						/* modified by yangzy 2014/10/17 贷后改造 begin*/
						else if(grtMortIColl.size()>0 && (bailIColl6==null||bailIColl6.size()<grtMortIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析担保品】后再进行提交";
								
						}else if(grtGuaranteeIColl.size()>0 && (bailIColl8==null||bailIColl8.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析保证人】后再进行提交";
						}
						/* modified by yangzy 2014/10/17 贷后改造 end*/
						//else if(bailIColl13==null||bailIColl13.size()<1){
						//	returnFlag = "不通过";
						//	returnInfo = "请录入【监控信息】后再进行提交";
						//}
						/* added by yangzy 2015/4/23  零售贷后检查改造,个人经营性贷款经营佐证资料变更 start */
						//else if(!check_freq.equals("04")&&(psp_cus_type.equals("001") && (bailIColl22==null||bailIColl22.size()<1))){//水电费
						//	returnFlag = "不通过";
						//	returnInfo = "请录入【水电费明细】后再进行提交";
						//}
						/* added by yangzy 2015/4/23  零售贷后检查改造,个人经营性贷款经营佐证资料变更 end */
						else if(check_freq.equals("04") && (bailIColl11==null||bailIColl11.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						}
						/* modified by yangzy 2015/01/27  需求:XD141230092,零售贷后检查改造 start */
						/* added by yangzy 2015/4/23  零售贷后检查改造,个人经营性贷款经营佐证资料变更 start */
						else if(!check_freq.equals("04")&&(psp_cus_type.equals("001") && (bailIColl28==null||bailIColl28.size()<1))){//银行对账单
							returnFlag = "不通过";
							returnInfo = "请录入【银行对账单】后再进行提交";
						}
						
						else if(!check_freq.equals("04")&& (!psp_cus_type.equals("002")) && (bailIColl63==null||bailIColl63.size()<1) && (bailIColl37==null||bailIColl37.size()<2)){//2015-03-03 Edited by FCL
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息(至少检查2项)】后再进行提交";
						}
						/* added by yangzy 2015/4/23  零售贷后检查改造,个人经营性贷款经营佐证资料变更 end */
						else if(grtGuaranteeIColl.size()>0 && (bailIColl53==null||bailIColl53.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【征信检查】的保证人检查后再进行提交";
						}
						else if(bailIColl52==null||bailIColl52.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【征信检查】的检查借款人信息后再进行提交";
						}
						else if(bailIColl60==null||bailIColl60.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的借款人检查后再进行提交";
						}
						else if(bailIColl42==null||bailIColl42.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的检查结论后再进行提交";
						}
						else if(grtMortIColl.size()>0 && (bailIColl61==null||bailIColl61.size()<grtMortIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的抵（质）押品检查后再进行提交";
						}
						else if(grtGuaranteeIColl.size()>0 && (bailIColl62==null||bailIColl62.size()<grtGuaranteeIColl.size())){
							returnFlag = "不通过";
							returnInfo = "请录入【现场检查】的担保企业检查后再进行提交";
						}
						/* modified by yangzy 2015/01/27  需求:XD141230092,零售贷后检查改造 end */
						else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else if("05".equals(task_type)){//合作方客户
						if(bailIColl17==null||bailIColl17.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【合作方分析】后再进行提交";
						}else if(!check_freq.equals("04") && (bailIColl3==null||bailIColl3.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【财务分析】后再进行提交";
						}else if(bailIColl9==null||bailIColl9.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【单一法人风险预警】后再进行提交";
						}else if(bailIColl10==null||bailIColl10.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【关注事件】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
						
					}else if("06".equals(task_type)){//担保公司客户
						if(bailIColl19==null||bailIColl19.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【融资担保公司分析】后再进行提交";
						}else if(!check_freq.equals("04") && (bailIColl3==null||bailIColl3.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【财务分析】后再进行提交";
						}else if(check_freq.equals("04") && (bailIColl11==null||bailIColl11.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						}else if(bailIColl20==null||bailIColl20.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【综合评价】后再进行提交";
						}else if(bailIColl9==null||bailIColl9.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【单一法人风险预警】后再进行提交";
						}else if(bailIColl10==null||bailIColl10.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【关注事件】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else if("07".equals(task_type) || "08".equals(task_type)){
						//检查是否存在批量任务关联关系
						IndexedCollection PBTR = SqlClient.queryList4IColl("queryPspBatchTaskRelByTaskId", task_id, conn);		
						if(PBTR!=null && PBTR.size()>0){
							KeyedCollection temp = (KeyedCollection) PBTR.get(0);
							String major_task_id = (String) temp.getDataValue("major_task_id");
							returnFlag = "不通过";
							returnInfo = "该贷后任务已存在批量任务编号为【"+major_task_id+"】中，不允许单独提交，检查不通过";
						}else{
							returnFlag = "通过";
							returnInfo = "贷后任务【首次检查】、【专项检查】，不需检查";
						}
					}
				}else if("03".equals(check_type)){//专项检查
					if("01".equals(task_type)){//对公客户
						
						if(bailIColl==null||bailIColl.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【借款人分析】后再进行提交";
						}else if(iColl.size()>0&&(bailIColl1==null||bailIColl1.size()<1)){
							//context.put("proFlag", "Y");
							returnFlag = "不通过";
							returnInfo = "请录入【固定资产贷款（项目融资）分析】后再进行提交";
						}else if(bailIColl2==null||bailIColl2.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【资产状况调查】后再进行提交";
						}else if(bailIColl3==null||bailIColl3.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【财务分析】后再进行提交";
						}else if(bailIColl4==null||bailIColl4.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息】后再进行提交";
						}else if(bailIColl23==null||bailIColl23.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息中的税费明细】后再进行提交";
						}else if(bailIColl28==null||bailIColl28.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息中的银行对账单】后再进行提交";
						}else if(bailIColl5==null||bailIColl5.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【监控信息】后再进行提交";
						}else if(grtMortIColl.size()>0 && (bailIColl6==null||bailIColl6.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析担保品】后再进行提交";
								
						}else if(grtGuaranteeIColl.size()>0 && (bailIColl21==null||bailIColl21.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析保证人】后再进行提交";
						}else if(bailIColl11==null||bailIColl11.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					
					}else if("02".equals(task_type)){//小微客户
						//IndexedCollection bailIColl = SqlClient.queryList4IColl("custabByTaskID", task_id, conn);//(1)借款人分析
						if(bailIColl==null||bailIColl.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【借款人分析】后再进行提交";
						}else if(iColl.size()>0&&(bailIColl1==null||bailIColl1.size()<1)){
							//context.put("proFlag", "Y");
							returnFlag = "不通过";
							returnInfo = "请录入【固定资产贷款（项目融资）分析】后再进行提交";
						}else if(bailIColl2==null||bailIColl2.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【资产状况调查】后再进行提交";
						}else if(bailIColl4==null||bailIColl4.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息】后再进行提交";
						}else if(bailIColl23==null||bailIColl23.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息中的税费明细】后再进行提交";
						}else if(bailIColl28==null||bailIColl28.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息中的银行对账单】后再进行提交";
						}else if(bailIColl5==null||bailIColl5.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【监控信息】后再进行提交";
						}else if(grtMortIColl.size()>0 && (bailIColl6==null||bailIColl6.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析担保品】后再进行提交";
								
						}else if(grtGuaranteeIColl.size()>0 && (bailIColl21==null||bailIColl21.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析保证人】后再进行提交";
						}else if(bailIColl11==null||bailIColl11.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						} else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else if("03".equals(task_type)){//个人客户
						if(psp_cus_type.equals("001") && (bailIColl7==null||bailIColl7.size()<1)){//经营性
							returnFlag = "不通过";
							returnInfo = "请录入【借款人分析】后再进行提交";
						}else if(psp_cus_type.equals("002") && (bailIColl12==null||bailIColl12.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【借款人分析】后再进行提交";
						}else if(grtMortIColl.size()>0 && (bailIColl6==null||bailIColl6.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析担保品】后再进行提交";
								
						}else if(grtGuaranteeIColl.size()>0 && (bailIColl8==null||bailIColl8.size()<1)){
							returnFlag = "不通过";
							returnInfo = "请录入【担保分析，分析保证人】后再进行提交";
						}else if(bailIColl13==null||bailIColl13.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【监控信息】后再进行提交";
						}else if(psp_cus_type.equals("001") && (bailIColl4==null||bailIColl4.size()<1)){//水电费
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息】后再进行提交";
						}else if(bailIColl23==null||bailIColl23.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息中的税费明细】后再进行提交";
						}else if(bailIColl28==null||bailIColl28.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【经营佐证信息中的银行对账单】后再进行提交";
						}else if(bailIColl11==null||bailIColl11.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else if("05".equals(task_type)){//合作方客户
						if(bailIColl17==null||bailIColl17.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【合作方分析】后再进行提交";
						}else if(bailIColl3==null||bailIColl3.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【财务分析】后再进行提交";
						}else if(bailIColl18==null||bailIColl18.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【综合评价】后再进行提交";
						}else if(bailIColl11==null||bailIColl11.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
						
					}else if("06".equals(task_type)){//担保公司客户
						if(bailIColl19==null||bailIColl19.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【融资担保公司分析】后再进行提交";
						}else if(bailIColl3==null||bailIColl3.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【财务分析】后再进行提交";
						}else if(bailIColl11==null||bailIColl11.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【预警信息】后再进行提交";
						}else if(bailIColl20==null||bailIColl20.size()<1){
							returnFlag = "不通过";
							returnInfo = "请录入【综合评价】后再进行提交";
						}else{
							returnFlag = "通过";
							returnInfo = "提交前必输项校验通过";
						}
					}else if("07".equals(task_type) || "08".equals(task_type)){
						//检查是否存在批量任务关联关系
						IndexedCollection PBTR = SqlClient.queryList4IColl("queryPspBatchTaskRelByTaskId", task_id, conn);		
						if(PBTR!=null && PBTR.size()>0){
							KeyedCollection temp = (KeyedCollection) PBTR.get(0);
							String major_task_id = (String) temp.getDataValue("major_task_id");
							returnFlag = "不通过";
							returnInfo = "该贷后任务已存在批量任务编号为【"+major_task_id+"】中，不允许单独提交，检查不通过";
						}else{
							returnFlag = "通过";
							returnInfo = "贷后任务【首次检查】、【专项检查】，不需检查";
						}
					}
				}else{
					returnFlag = "通过";
					returnInfo = "贷后任务【首次检查】、【专项检查】，不需检查";
					returnMap.put("OUT_是否通过", returnFlag);
					returnMap.put("OUT_提示信息", returnInfo);
					return returnMap;
				}
			}
			/**modified by lisj 2015-1-14 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能 end**/
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
