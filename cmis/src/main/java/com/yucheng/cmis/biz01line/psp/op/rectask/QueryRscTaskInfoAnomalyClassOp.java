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
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRscTaskInfoAnomalyClassOp extends CMISOperation {
	private final String modelId = "RscTaskInfo";

	@Override
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
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
                flag = (String) queryData.getDataValue("flag");
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
			/***是否有无疑义****/
			String  doubt = "";
			try {
				doubt = (String) queryData.getDataValue("doubt");
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
			/***判断岗位****/
			String  duty = "";
			try {
				duty = (String) queryData.getDataValue("duty");
			} catch (Exception e) {
			}
			if(NewStringUtils.isBlank(duty)){
				try {
					duty = (String) context.getDataValue("duty");
				} catch (Exception e) {
				}
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
							if("S0404".equals(s)){//二级支行售后复合岗
								duty="subHead";
								break;
							}else if("D0102".equals(s)){//总行分类认定岗
								duty="branchHead";
								break;
							}else if("D0103".equals(s)){//总行分类审核岗
								duty="headAcc";
								break;
							}else if("Q0000".equals(s)){//已完成
								duty="finally";
								break; 
							}
						}else{
							paramMap.put("input_id", "00000000000");//无数据清空列表
						}
					}
				}
			}
			
            int size = 15; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
//			IndexedCollection iColl = dao.queryList(exportRscTaskInfoAnomalyClassExcelData,null ,conditionStr,pageInfo,connection);
			
			if(context.containsKey("bs")&&(context.getDataValue("bs")).equals("export")){
				 pageInfo = null;					
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
				if(!"".equals(duty)  && duty !=null && "mgr".equals(duty)){//客户经理
//					月末跑批生成的任务清单的分类状态为“未分类”；
					paramMap.put("risk_cls_status", "01");
					doubt_status = "01";
				}else if (!"".equals(duty)  && duty !=null && "subHead".equals(duty)){//二级支行售后复合岗G0047
					paramMap.put("risk_cls_status", "02");
					doubt_status = "02";
				}else if (!"".equals(duty)  && duty !=null && "branchHead".equals(duty)){//一级支行（分行）复核岗G0064
					paramMap.put("risk_cls_status", "03");
					doubt_status = "03";
				}else if (!"".equals(duty)  && duty !=null && "headAcc".equals(duty)){//总行分类认定岗G0062
					paramMap.put("risk_cls_status", "04");
					doubt_status = "04";
					doubt = "not";//异常风险无记录及
					if(paramMap.containsKey("usr_cde")){
						paramMap.remove("usr_cde");
					}
					if(paramMap.containsKey("bch_cde")){
						paramMap.remove("bch_cde");
					}
				
				}else if (!"".equals(duty)  && duty !=null && "finally".equals(duty)){//总行分类审核岗G0063
					paramMap.put("risk_cls_status", "05");
					doubt_status = "05";
					doubt = "not";//异常风险无记录及
					if(paramMap.containsKey("usr_cde")){
						paramMap.remove("usr_cde");
					}
					if(paramMap.containsKey("bch_cde")){
						paramMap.remove("bch_cde");
					}
				
				} 
				
			}
			//判断是否有疑义
			if(!"".equals(doubt)  && doubt !=null && "yes".equals(doubt)){//有疑义
				paramMap.put("yesduty", "1");
				paramMap.put("doubt_status", doubt_status);
			}else if (!"".equals(doubt)  && doubt !=null && "not".equals(doubt)){//无疑义
				paramMap.put("noduty", "1");
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
			IndexedCollection iColl  = new IndexedCollection();
			IndexedCollection iCollForPageInfo = SqlClient.queryList4IColl("queryRscTaskInfoAnomalyClass", paramMap, 0, 0, connection);
			if(pageInfo!=null){
				pageInfo.setRecordSize(String.valueOf(iCollForPageInfo.size()));
				pageInfo.beginIdx=1+pageInfo.pageSize*(pageInfo.pageIdx-1);
				if(pageInfo.recordSize<pageInfo.pageSize*pageInfo.pageIdx){  
					 pageInfo.endIdx= pageInfo.recordSize;
				}else{
					pageInfo.endIdx= pageInfo.beginIdx+pageInfo.pageSize-1;
					 
				}
				
			  iColl = SqlClient.queryList4IColl("queryRscTaskInfoAnomalyClass", paramMap,pageInfo.beginIdx, pageInfo.endIdx, connection);

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
						String finallyApp = "";//审批岗
						KeyedCollection kColl = (KeyedCollection)iColl.get(i);
						map.put("serno", (String)kColl.getDataValue("serno"));
						IndexedCollection subResult= SqlClient.queryList4IColl("queryRscTaskInfoSubLike", map,null,0,0, connection);
						if(!subResult.isEmpty()&&subResult !=null){
							/**查询岗位对应的数据认定结果***/
							for(int j = 0; subResult.size()>j;j++){
								KeyedCollection sub = (KeyedCollection)subResult.get(j);
								if(!"".equals((String)sub.getDataValue("identy_duty")) && ("S0403".equals((String)sub.getDataValue("identy_duty")))){//客户经理
									if(!"".equals((String)sub.getDataValue("class_adjust_rst")))
									cusMgr = (String)sub.getDataValue("class_adjust_rst");
								}else if(!"".equals((String)sub.getDataValue("identy_duty")) && "S0404".equals((String)sub.getDataValue("identy_duty"))){//客户经理主管
									if(!"".equals((String)sub.getDataValue("class_adjust_rst")))
									subHead = (String)sub.getDataValue("class_adjust_rst");
								}else if(!"".equals((String)sub.getDataValue("identy_duty")) && "D0102".equals((String)sub.getDataValue("identy_duty"))){//风险部审查
									if(!"".equals((String)sub.getDataValue("class_adjust_rst")))
									branchHead = (String)sub.getDataValue("class_adjust_rst");
								}else if(!"".equals((String)sub.getDataValue("identy_duty")) && "D0103".equals((String)sub.getDataValue("identy_duty"))){//风险部总经理
									if(!"".equals((String)sub.getDataValue("class_adjust_rst")))
									headAcc = (String)sub.getDataValue("class_adjust_rst");
									/*if(!"".equals(dutyFinally) && "Q0000".equals(dutyFinally)){//已完成
										if(!"".equals(sub.getClassAdjustRst()))
										kColl.put("finally_class_rst", sub.getClassAdjustRst());
									}*/
								}else if(!"".equals((String)sub.getDataValue("identy_duty")) && "Q0000".equals((String)sub.getDataValue("identy_duty"))){//
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
			
			
			iColl.setName("RscTaskInfoThrList");
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
