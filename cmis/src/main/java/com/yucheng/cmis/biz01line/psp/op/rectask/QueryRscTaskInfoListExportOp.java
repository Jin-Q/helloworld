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
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRscTaskInfoListExportOp  extends CMISOperation {
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
			
			/***判断岗位****/
			String  duty = "";
			try {
				duty = (String) context.getDataValue("duty");
			} catch (Exception e) {
			}
			
			//定义查询参数map
			Map paramMap = new HashMap();
			String  dutyNo = "";
			if(context.containsKey("dutyNo")){
				dutyNo=(String) context.getDataValue("dutyNo");
			}
			
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
							}
						}else{
							paramMap.put("input_id", "000000000");//无数据清空列表
						}
					}
				}
			}
			if(!"".equals(duty)  && duty !=null && "mgr".equals(duty)){//未分类  客户经理
//				月末跑批生成的任务清单的分类状态为“未分类”；
				paramMap.put("risk_cls_status", new String[]{"01"});
				paramMap.put("doubt_status", "01");
			}else if (!"".equals(duty)  && duty !=null && "subHead".equals(duty)){//客户经理
				paramMap.put("risk_cls_status", new String[]{"02"});
				paramMap.put("doubt_status", "02");
			}else if (!"".equals(duty)  && duty !=null && "branchMgr".equals(duty)){//部门审核
				paramMap.put("risk_cls_status", new String[]{"03"});
				paramMap.put("doubt_status", "03");
			}else if (!"".equals(duty)  && duty !=null && "branchHead".equals(duty)){//风险经办
				paramMap.put("risk_cls_status", new String[]{"04"});
				paramMap.put("doubt_status", "04");
			}else if (!"".equals(duty)  && duty !=null && "headAcc".equals(duty)){//风险审核
				paramMap.put("risk_cls_status", new String[]{"05"});
				paramMap.put("doubt_status", "05");
			}
			//解析页面排序字段

			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);			 

			//取查询关键字为in的查询条件,这里没有用到，注释
			//EUIUtil.assembleSearchParamerter(context, "in", EUIUtil.QueryParamMatchType_Array, paramMap);
            int size = 15; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
//			//声明分页信息，默认读取10条记录
//			int pageSize = 10;
//			PageInfo pageInfo = EUIUtil.assemblePageInfo(context, pageSize);
//			
//			//通过组件服务实例化业务组件
//			RscTaskInfoComponent comp = (RscTaskInfoComponent)CMISFactory.getComponent(RscTaskInfoConstance.RSC_TASK_INFO_ID);	
//			
//			List<RscTaskInfo>  list = comp.queryRscTaskInfoList(pageInfo, paramMap, connection);
			iColl.setName(iColl.getName()+"List");
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
