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
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRscTaskInfoHisListOp extends CMISOperation {
	private final String modelId = "RscTaskInfoHis";

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
			//定义查询参数map
			Map paramMap = new HashMap();
			 
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);			 
			int size = 15; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			
			
			if(context.containsKey("export")&&((String)context.getDataValue("export")).equals("yes")){
				pageInfo = null;
			}
						//查询机构列表数据
			IndexedCollection list = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			
 
			
			
			list.setName(list.getName()+"List");
			String[] args=new String[] {"cus_id" };
			String[] modelIds=new String[]{"CusBase" };
			String[]modelForeign=new String[]{"cus_id" };
			String[] fieldName=new String[]{"cus_name" };
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(list, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			//将查询结构放入Context中以便前端获查询结结
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(list, new String[]{"input_id"});
			SInfoUtils.addSOrgName(list, new String[]{"input_br_id"});
			
			if(!list.isEmpty() && list !=null){					
				Map map = new HashMap();
				for(int i=0;i<list.size();i++){
					String cusMgr ="";//客户经理
					String subHead = "";//支行行长
					String branchMgr="";//分行售后经理
					String branchHead = "";//分行行长
					String headAcc = "";//受理岗
					String headApp = "";//审批岗
					String finallyApp = "";//已完成
					KeyedCollection kColl = (KeyedCollection)list.get(i);
					map.put("serno",kColl.getDataValue("serno"));
				    IndexedCollection subResult=(IndexedCollection) SqlClient.queryList4IColl("queryRscTaskInfoSubHisLike", map, connection);
					if(!subResult.isEmpty()&&subResult !=null){
						/**查询岗位对应的数据认定结果***/
						for(int j=0;j<subResult.size();j++){
							KeyedCollection sub = (KeyedCollection)subResult.get(i);
							if(!"".equals(sub.getDataValue("identy_duty")) && "S0403".equals(sub.getDataValue("identy_duty"))){//客户经理
								if(!"".equals(sub.getDataValue("class_adjust_rst")))
								cusMgr = (String) sub.getDataValue("class_adjust_rst");
							}else if(!"".equals(sub.getDataValue("identy_duty")) && "S0404".equals(sub.getDataValue("identy_duty"))){//支行 行长
								if(!"".equals(sub.getDataValue("class_adjust_rst")))
								subHead = (String) sub.getDataValue("class_adjust_rst");
							}else if(!"".equals(sub.getDataValue("identy_duty")) && "D0102".equals(sub.getDataValue("identy_duty"))){//分行行长
								if(!"".equals(sub.getDataValue("class_adjust_rst")))
								branchHead = (String) sub.getDataValue("class_adjust_rst");
							}else if(!"".equals(sub.getDataValue("identy_duty")) && "D0103".equals(sub.getDataValue("identy_duty"))){//总行受理
								if(!"".equals(sub.getDataValue("class_adjust_rst")))
								headAcc = (String) sub.getDataValue("class_adjust_rst");
								/*if(!"".equals(dutyFinally) && "Q0000".equals(dutyFinally)){//已完成
									if(!"".equals(sub.getClassAdjustRst()))
										finallyApp = sub.getClassAdjustRst();
									kColl.put("finally_class_rst", finallyApp);
								}*/
							}else if(!"".equals(sub.getDataValue("identy_duty")) && "Q0000".equals(sub.getDataValue("identy_duty"))){//分行行长
								if(!"".equals(sub.getDataValue("class_adjust_rst")))
									finallyApp = (String) sub.getDataValue("class_adjust_rst");
							}
							
						}
					}
					kColl.put("mgr_mgr_class_rst", cusMgr);
					kColl.put("subHead_class_rst", subHead); 
					kColl.put("branchHead_class_rst", branchHead);
					kColl.put("headAcc_class_rst", headAcc);
					kColl.put("headApp_class_rst", headApp);
					kColl.put("finally_class_rst", finallyApp);
					KeyedCollection RscTaskInfo = new KeyedCollection(); 
					list.set(i, RscTaskInfo);
				}
			}
			this.putDataElement2Context(list, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (EMPException ee) {
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
