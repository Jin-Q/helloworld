package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRscTaskInfoListTowOp  extends CMISOperation {
	private final String modelId = "RscTaskInfo";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String conditionStr = "";
		try {
			connection = this.getConnection(context);
			// 页面查询类别，前台参数键名为"searchType"，参数值为"quickquery"表示搜索框快速查询 

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String  flag = "";
			try {
                                                                                                                                              				flag = (String) context.getDataValue("flag");
			} catch (Exception e) {
			}
			/***是否有无疑义****/
			String  doubt = "";
			try {
				doubt = (String) queryData.getDataValue("doubt");
			} catch (Exception e) {
			}
			/***判断岗位****/
			String  duty = "";
			try {
				duty = (String) queryData.getDataValue("duty");
			} catch (Exception e) {
			}
			String  bill_no = "";
			try {
				bill_no = (String) queryData.getDataValue("bill_no");
			} catch (Exception e) {
			}
			String  pre_class_rst = "";
			try {
				pre_class_rst = (String) queryData.getDataValue("pre_class_rst");
			} catch (Exception e) {
			}
			String  cus_id = "";
			try {
				cus_id = (String) queryData.getDataValue("cus_id");
			} catch (Exception e) {
			}
			String  usr_cde = "";
			try {
				usr_cde = (String) queryData.getDataValue("usr_cde");
			} catch (Exception e) {
			}
			String  bch_cde = "";
			try {
				bch_cde = (String) queryData.getDataValue("bch_cde");
			} catch (Exception e) {
			}
			//定义查询参数map
			Map paramMap = new HashMap();
			//获取当前登入用户的岗位
			String  dutyNo = "";
			String  dutyFinally = "";//选择已完成
			if(queryData!=null&&queryData.containsKey("dutyno")){
				dutyNo=(String) queryData.getDataValue("dutyno");
				paramMap.put("identy_duty", dutyNo);
			}
			
			paramMap.put("bch_cde", bch_cde);
			paramMap.put("usr_cde", usr_cde);
			if(!"mgr".equals(duty)){
				if(!"".equals(dutyNo) && dutyNo!=null){
					String[] dutyNoArray = dutyNo.split(",");
					for(String s : dutyNoArray){
						if(!"".equals(s)&& s !=null){
//							if("G0047".equals(s)){//二级支行售后复合岗
//								duty="subHead";
//								break;
//							}else if("G0064".equals(s)){//一级支行（分行）复核岗
//								duty="branchMgr";
//								break;
//							}else if("G0062".equals(s)){//总行分类认定岗
//								duty="branchHead";
//								break;
//							}else if("G0063".equals(s)){//总行分类审核岗
//								duty="headAcc";
//								break;
//							}else if("Q0000".equals(s)){//已完成
//								duty="finally";
//								 
//								break;
//							}
							if("S0404".equals(s)){//客户经理主管
								duty="subHead";
								break;
							}else if("D0102".equals(s)){//风险经办
								duty="branchHead";
								break;
							}else if("D0103".equals(s)){//风险审核
								duty="headAcc";
								break;
							}else if("Q0000".equals(s)){//已完成
								duty="finally";
								break; 
							}
						}else{
							paramMap.put("input_id", "000000000");//无数据清空列表
						}
					}
				}
			}else{
				paramMap.put("identy_duty", "S0403");
				dutyNo = (String) context.getDataValue("dutyNoList");
			}
			
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);			 
			int size = 15; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
	//		IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection); 
			if(context.containsKey("bs")&&(context.getDataValue("bs")).equals("export")){
				 pageInfo = null;					
			}
			
			//客户经理列表判断是否协办客户经理
			if("mgr".equals(duty)){
				String[] dutyNoArray = dutyNo.split(",");
				for(String s : dutyNoArray){
					if(!"".equals(s)&& s !=null){
						if("S0404".equals(s)&&paramMap.containsKey("input_id")){//二级支行售后复合岗//协办客户经理
							//duty="ourOrg";
							paramMap.remove("input_id");
							paramMap.put("br_id",(String)context.getDataValue(CMISConstance.ATTR_ORGID));
							break;
						}
					}
				}
			}
			
			/**声明分类结果供有疑义与无疑义查询**/
			String doubt_status = "";
			if(!"".equals(flag) && flag != null && "unDo".equals(flag)){
				//分类任务未完成统计 按机构查询明细
				paramMap.put("risk_cls_status", "01");
			}else{
//				01	未分类
//				02	支行认定（经理）
//				03	支行认定（行长）
//				04	分行认定（经理）
//				05	分行认定（行长）
//				06	总行认定（受理岗）
//				07	总行认定（审批岗）

				//判断当前进入的岗位
				if(!"".equals(duty)  && duty !=null && ("mgr".equals(duty))||("ourOrg".equals(duty))){//客户经理
//					月末跑批生成的任务清单的分类状态为“未分类”；
					paramMap.put("risk_cls_status", "01");
					doubt_status = "01";
					paramMap.put("yesduty", "1");//控制去除异常风险分类列表数据有记录级
				}else if (!"".equals(duty)  && duty !=null && "subHead".equals(duty)){//二级支行售后复合岗
					paramMap.put("risk_cls_status","02");
					doubt_status = "02";
					paramMap.put("yesduty", "1");//控制去除异常风险分类列表数据有记录级
				}else if (!"".equals(duty)  && duty !=null && "branchHead".equals(duty)){//总行分类认定岗
					paramMap.put("risk_cls_status", "03");
					doubt_status = "03";
					if(paramMap.containsKey("usr_cde")){
						paramMap.remove("usr_cde");
					}
					if(paramMap.containsKey("bch_cde")){
						paramMap.remove("bch_cde");
					}
				
					paramMap.put("noduty", "1");//控制去除异常风险分类列表数据无记录级
				}else if (!"".equals(duty)  && duty !=null && "headAcc".equals(duty)){//总行分类审核岗
					paramMap.put("risk_cls_status","04");
					doubt_status = "04";
					if(paramMap.containsKey("usr_cde")){
						paramMap.remove("usr_cde");
					}
					if(paramMap.containsKey("bch_cde")){
						paramMap.remove("bch_cde");
					}
				
					paramMap.put("noduty", "1");//控制去除异常风险分类列表数据无记录级
				}
				else if (!"".equals(duty)  && duty !=null && "finally".equals(duty)){//已完成
					paramMap.put("risk_cls_status", "05");
					doubt_status = "05";
					if(paramMap.containsKey("usr_cde")){
						paramMap.remove("usr_cde");
					}
					if(paramMap.containsKey("bch_cde")){
						paramMap.remove("bch_cde");
					}
					
					paramMap.put("noduty", "1");//控制去除异常风险分类列表数据无记录级
				}
				
			}
			//判断是否有疑义
			if(!"".equals(doubt)  && doubt !=null && "yes".equals(doubt)){//有疑义
				paramMap.put("doubt", "1");
				paramMap.put("doubt_status", doubt_status);
			}else if (!"".equals(doubt)  && doubt !=null && "not".equals(doubt)){//无疑义
				paramMap.put("nodoubt", "1");
				paramMap.put("doubt_status", doubt_status);
			}
			if(NewStringUtils.isNotBlank(bill_no)){
				paramMap.put("bill_no", bill_no);
			}
			if(NewStringUtils.isNotBlank(cus_id)){
				paramMap.put("cus_id", cus_id);
			}
			if(NewStringUtils.isNotBlank(pre_class_rst)){
				paramMap.put("pre_class_rst", pre_class_rst);
			}
			IndexedCollection iColl = new IndexedCollection();
			IndexedCollection iCollForPageInfo = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap, 0, 0, connection);
			if(pageInfo!=null){
				pageInfo.setRecordSize(String.valueOf(iCollForPageInfo.size()));
				pageInfo.beginIdx=1+pageInfo.pageSize*(pageInfo.pageIdx-1);
				if(pageInfo.recordSize<pageInfo.pageSize*pageInfo.pageIdx){  
					 pageInfo.endIdx= pageInfo.recordSize;
				}else{
					pageInfo.endIdx= pageInfo.beginIdx+pageInfo.pageSize-1;
					 
				}
			    iColl = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap,pageInfo.beginIdx, pageInfo.endIdx, connection);
			}else{
				iColl = iCollForPageInfo;
			}
			if(!"".equals(doubt)  && doubt !=null ){//非客户经理页面
				if(!iColl.isEmpty() && iColl !=null){					
					Map map = new HashMap();
					for(int i=0;i<iColl.size();i++){
						String cusMgr ="";//客户经理
						String subHead = "";//支行行长
						String branchMgr="";//分行售后经理
						String branchHead = "";//分行行长
						String headAcc = "";//受理岗
						String headApp = "";//审批岗
						String finallyApp = "";//已完成
						KeyedCollection kColl = (KeyedCollection)iColl.get(i);
						map.put("serno", kColl.getDataValue("serno"));
						IndexedCollection subResult=(IndexedCollection) SqlClient.queryList4IColl("queryRscTaskInfoSubLike", map, connection);
						if(!subResult.isEmpty()&&subResult !=null){
							/**查询岗位对应的数据认定结果***/
							for(int j=0;j<subResult.size();j++){
								KeyedCollection sub= (KeyedCollection)subResult.get(j);
								if(!"".equals(sub.getDataValue("identy_duty")) && "S0403".equals(sub.getDataValue("identy_duty"))){//客户经理
									if(!"".equals(sub.getDataValue("class_adjust_rst")))
									cusMgr = (String)sub.getDataValue("class_adjust_rst");
								}else if(!"".equals(sub.getDataValue("identy_duty")) && "S0404".equals(sub.getDataValue("identy_duty"))){//支行 行长
									if(!"".equals(sub.getDataValue("class_adjust_rst")))
									subHead = (String)sub.getDataValue("class_adjust_rst");
								}else if(!"".equals(sub.getDataValue("identy_duty")) && "D0102".equals(sub.getDataValue("identy_duty"))){//分行行长
									if(!"".equals((String)sub.getDataValue("class_adjust_rst")))
									branchHead = (String)sub.getDataValue("class_adjust_rst");
								}else if(!"".equals(sub.getDataValue("identy_duty")) && "D0103".equals(sub.getDataValue("identy_duty"))){//总行受理
									if(!"".equals((String)sub.getDataValue("class_adjust_rst")))
									headAcc = (String)sub.getDataValue("class_adjust_rst");
									/*if(!"".equals(dutyFinally) && "Q0000".equals(dutyFinally)){//已完成
										if(!"".equals(sub.getClassAdjustRst()))
											finallyApp = sub.getClassAdjustRst();
										kColl.put("finally_class_rst", finallyApp);
									}*/
								}else if(!"".equals(sub.getDataValue("identy_duty")) && "Q0000".equals(sub.getDataValue("identy_duty"))){//分行行长
									if(!"".equals((String)sub.getDataValue("class_adjust_rst")))
										finallyApp = (String)sub.getDataValue("class_adjust_rst");
								}
								
							}
						}
						kColl.put("mgr_mgr_class_rst", cusMgr);
						kColl.put("subHead_class_rst", subHead);
						kColl.put("branchMgr_class_rst", branchMgr);
						kColl.put("branchHead_class_rst", branchHead);
						kColl.put("headAcc_class_rst", headAcc);
						kColl.put("headApp_class_rst", headApp);
						kColl.put("finally_class_rst", finallyApp);
						iColl.set(i, kColl);
					}
				}
			}
			
            
		    
		 
				iColl.setName("RscTaskInfoTowList");
			 
			
			String[] args=new String[] {"cus_id" };
			String[] modelIds=new String[]{"CusBase" };
			String[]modelForeign=new String[]{"cus_id" };
			String[] fieldName=new String[]{"cus_name" };
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			//将查询结构放入Context中以便前端获查询结结
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			 
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
