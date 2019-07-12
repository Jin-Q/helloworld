package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.echain.util.StringUtils;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.TimeUtil;

public class UpdateRscTaskInfoOp  extends CMISOperation {
	private final String modelId = "RscTaskInfo";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		boolean result = false;
		String flag = "failed";
		try {
			connection = this.getConnection(context);
			String type = "";
			/**区分提交和认定和回退和赋默认值操作**/
			try {
				type = (String)context.getDataValue("type");
			} catch (Exception e) {}
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
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
			String risk_cls_status ="";
			try {
				risk_cls_status = (String)context.getDataValue("risk_cls_status");
			} catch (Exception e) {}
			String[] conId = new String[]{"condition1","condition2","condition3","condition4","condition5","condition6","condition7","condition8","condition9","condition10","condition11","condition13","condition14"};
			String[] conIdSub  = new String[]{"condition1","condition2","condition3","condition4"};
			/***判断岗位****/
			 
			//获取当前登入用户的岗位
			//String  dutyNo=(String) context.get(CMISConstance.ATTR_DUTYNO_LIST);
			String  dutyNo = "";
			if(queryData!=null&&queryData.containsKey("dutyno")){
				dutyNo=(String) queryData.getDataValue("dutyno");
			}
			if(NewStringUtils.isBlank(dutyNo)){
				try {
					dutyNo = (String) context.getDataValue("dutyno");
				} catch (Exception e) {
				}
			}
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
//							}else if("Q0000".equals(s)){//总行分类审核岗
//								duty="finally";
//								break;
//							}
							//裕民的岗位都不一样。。。
							if("S0404".equals(s)){//客户经理主管
								duty="subHead";
								break;
							}else if("D0102".equals(s)){//风险管理部审查员
								duty="branchHead";
								break;
							}else if("D0103".equals(s)){//风险管理部总经理
								duty="headAcc";
								break;
							}else if("Q0000".equals(s)){//已完成
								duty="finally";
								break;
							}
						}
					}
				}
			}
 			KeyedCollection domainkColl = new KeyedCollection();
 			KeyedCollection subDomain = new KeyedCollection(); 
			try {
				domainkColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(domainkColl == null || domainkColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//通过组件服务实例化业务组件
			domainkColl.put("upd_id",(String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
			domainkColl.put("upd_br_id",(String) context.getDataValue(CMISConstance.ATTR_ORGID));
			domainkColl.put("upd_date",(String) context.getDataValue(CMISConstance.OPENDAY));
			//domain.setUpdDate(CmisUtil.getDateTime(context));
//			01	未分类
//			02	客户经理
//			03	一级支行（分行）复核岗
//			04	总行分类认定岗
//			05	总行分类审核岗
			//判断当前进入的岗位
			if("submit".equals(type)&& !"".equals(type)){//提交
				if(!"".equals(duty)  && duty !=null && "mgr".equals(duty)){//客户经理
	 
					KeyedCollection sOrgKColl = dao.queryFirst("SOrg", null, " where organno   ='"+(String)context.getDataValue(CMISConstance.ATTR_ORGID)+"'",connection);
					domainkColl.put("risk_cls_status","02");
					
					
				}else if (!"".equals(duty)  && duty !=null && "subHead".equals(duty)){//客户经理主管
					domainkColl.put("risk_cls_status","03");
				}else if (!"".equals(duty)  && duty !=null && "branchHead".equals(duty)){//风险管理员审核岗 
					domainkColl.put("risk_cls_status","04");
				}else if (!"".equals(duty)  && duty !=null && "headAcc".equals(duty)){//风险管理员总经理
					domainkColl.put("risk_cls_status","05");
				}else if (!"".equals(duty)  && duty !=null && "finally".equals(duty)){//已完成岗
					domainkColl.put("risk_cls_status","05");
				}else{
					//若都不是默认登录人为客户经理主管
					domainkColl.put("risk_cls_status","03");
				}
				if(!"".equals(duty)  && duty !=null && "mgr".equals(duty)){//有认定的全部提交
					//domain.setCusId((String)context.getDataValue("cus_id"));
					//result = comp.updateRscTaskInfoMgr(domain, connection);
					int index = 0;
					Map paramMap = new HashMap();
					paramMap.put("input_id", (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
					paramMap.put("risk_cls_status",risk_cls_status);
					paramMap.put("identy_duty", dutyNo);
					String dutyNo1 = (String) context.getDataValue("dutyNoList");
					if("mgr".equals(duty)){
						String[] dutyNoArray = dutyNo1.split(",");
						for(String s : dutyNoArray){
							if(!"".equals(s)&& s !=null){
								if(("S0404".equals(s)&&paramMap.containsKey("input_id"))){//客户经理主管
									//duty="ourOrg";
									paramMap.remove("input_id");
									paramMap.put("br_id",(String)context.getDataValue(CMISConstance.ATTR_ORGID)); 
									break;
								}
							}
						}
					}
					
					IndexedCollection usrIColl = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap,conId,0,0, connection);
					result=true;
					for(int i=0;usrIColl.size()>i;i++){
						KeyedCollection r = (KeyedCollection)usrIColl.get(i);
						HashMap taskInfoSub = new HashMap();
						taskInfoSub.put("serno",r.getDataValue("serno"));
						taskInfoSub.put("identy_duty",dutyNo); 
						KeyedCollection dataKColl = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", taskInfoSub, conIdSub, connection);
						if(dataKColl!=null&&NewStringUtils.isNotBlank((String)dataKColl.getDataValue("class_adjust_rst"))){
							domainkColl.put("serno",r.getDataValue("serno"));
							//更新数据 
							int updateResult = dao.update(domainkColl, connection);
						 	if(updateResult!=1){
						 		result = false;
								context.addDataField("flag","failed" );
								return "";
							}
							index += 1;
						}
					}
					if(index==0){
						flag="out";
					}
				}else{
					int index = 0;
					Map paramMap = new HashMap();
					if(!dutyNo.equals("D0102")&&!dutyNo.equals("D0103")&&!dutyNo.equals("Q0000")){ 
						paramMap.put("usr_cde",(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
						paramMap.put("bch_cde",(String)context.getDataValue(CMISConstance.ATTR_ORGID));
						paramMap.put("yesduty", "1");//控制去除异常风险分类列表数据有记录级
					}else{
						paramMap.put("noduty", "1");//控制去除异常风险分类列表数据无记录级
					}
					paramMap.put("identy_duty", dutyNo);
					paramMap.put("risk_cls_status",risk_cls_status);
					paramMap.put("nodoubt", "1");
					IndexedCollection usrList = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap,conId,0,0, connection);
					result = true;
					for(int i=0;usrList.size()>i;i++){
						KeyedCollection r = (KeyedCollection)usrList.get(i);
						Map taskInfoSub = new HashMap(); 
						taskInfoSub.put("serno",r.getDataValue("serno"));
						taskInfoSub.put("identy_duty",dutyNo); 
						KeyedCollection dataKColl = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", taskInfoSub, conIdSub, connection);
						if(dataKColl!=null&&NewStringUtils.isNotBlank((String)dataKColl.getDataValue("class_adjust_rst"))){
							domainkColl.put("serno",r.getDataValue("serno"));
							//更新数据 
							int updateResult = dao.update(domainkColl, connection);
						 	if(updateResult!=1){
						 		result = false;
								context.addDataField("flag","failed" );
								return "";
							}
							index += 1;
						 
							if("D0103".equals(dutyNo)||"Q0000".equals(dutyNo)){     
								updateAccLonaRiskClass(context, r, dutyNo);//更新借据
							}   
						}
					}
					paramMap.put("doubt", "1");
					paramMap.remove("nodoubt");
					IndexedCollection usrList1 = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap,conId,0,0, connection);
					result = true;
					for(int i=0;usrList1.size()>i;i++){
						KeyedCollection r = (KeyedCollection)usrList1.get(i);
						Map taskInfoSub = new HashMap(); 
						taskInfoSub.put("serno",r.getDataValue("serno"));
						taskInfoSub.put("identy_duty",dutyNo); 
						KeyedCollection dataKColl = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", taskInfoSub, conIdSub, connection);
						if(dataKColl!=null&&NewStringUtils.isNotBlank((String)dataKColl.getDataValue("class_adjust_rst"))){
							domainkColl.put("serno",r.getDataValue("serno"));
							//更新数据 
							int updateResult = dao.update(domainkColl, connection);
						 	if(updateResult!=1){
						 		result = false;
								context.addDataField("flag","failed" );
								return "";
							} 
							if("D0103".equals(dutyNo)||"Q0000".equals(dutyNo)){
								updateAccLonaRiskClass(context, r, dutyNo);//更新借据
							}
							index += 1;
						}
					}
					if(!dutyNo.equals("D0102")&&!dutyNo.equals("D0103")&&!dutyNo.equals("Q0000")){//总行都可以看到
						paramMap.put("doubt", "1");//异常风险有记录级
					}else{
						paramMap.put("nodoubt", "1");//异常风险无记录级
					}
					paramMap.put("identy_duty", dutyNo);
					IndexedCollection usrList2 = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap,conId,0,0, connection);
					result=true;
					for(int i=0;usrList2.size()>i;i++){
						KeyedCollection r = (KeyedCollection)usrList2.get(i);
						Map taskInfoSub = new HashMap(); 
						taskInfoSub.put("serno",r.getDataValue("serno"));
						taskInfoSub.put("identy_duty",dutyNo); 
						KeyedCollection dataKColl = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", taskInfoSub, conIdSub, connection);
						if(dataKColl!=null&&NewStringUtils.isNotBlank((String)dataKColl.getDataValue("class_adjust_rst"))){
							domainkColl.put("serno",r.getDataValue("serno"));
							//更新数据 
							int updateResult = dao.update(domainkColl, connection);
						 	if(updateResult!=1){
						 		result=false;
								context.addDataField("flag","failed" );
								return "";
							} 
							if("D0103".equals(dutyNo)||"Q0000".equals(dutyNo)){
								updateAccLonaRiskClass(context, r, dutyNo);//更新借据
							}
							index += 1;
						}
					}
					
					if(index==0){
						flag="out";
					}
				}
			}else if("fallBack".equals(type)&& !"".equals(type)){//回退
				if (!"".equals(duty)  && duty !=null && "subHead".equals(duty)){//未分类
					domainkColl.put("risk_cls_status","01");
				}else if (!"".equals(duty)  && duty !=null && "branchHead".equals(duty)){//客户经理主管
					domainkColl.put("risk_cls_status","02");
				}else if (!"".equals(duty)  && duty !=null && "headAcc".equals(duty)){//风险管理部审查岗
					domainkColl.put("risk_cls_status","03");
				}else{
					//若都不是默认登录人为客户经理主管
					domainkColl.put("risk_cls_status","01");
				}
				int index = 0;
				Map paramMap = new HashMap();
				String cus_id =  (String) context.getDataValue("cus_id");
				
				paramMap.put("identy_duty", dutyNo);
				if(!dutyNo.equals("D0102")&&!dutyNo.equals("D0103")){ 
					paramMap.put("usr_cde",(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
					paramMap.put("bch_cde",(String)context.getDataValue(CMISConstance.ATTR_ORGID));
					paramMap.put("yesduty", "1");//控制去除异常风险分类列表数据有记录级
				}else{
					paramMap.put("noduty", "1");//控制去除异常风险分类列表数据无记录级
				}
				paramMap.put("risk_cls_status",risk_cls_status);
				paramMap.put("nodoubt", "1");
				paramMap.put("cus_id",cus_id);
				IndexedCollection usrList2 = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap,conId,0,0, connection);
				result=true;
				for(int i=0;usrList2.size()>i;i++){
					KeyedCollection r = (KeyedCollection)usrList2.get(i);
					Map taskInfoSub = new HashMap(); 
					taskInfoSub.put("serno",r.getDataValue("serno"));
					taskInfoSub.put("identy_duty",dutyNo); 
					KeyedCollection dataKColl = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", taskInfoSub, conIdSub, connection);
					
					
					domainkColl.put("serno",r.getDataValue("serno"));
					//更新数据 
					int updateResult = dao.update(domainkColl, connection);
					if(updateResult!=1){
						result=false;
						context.addDataField("flag","failed");
						return "";
					}else{
						index += 1;
					}
					if(taskInfoSub!=null&&dataKColl!=null&&NewStringUtils.isNotBlank((String)dataKColl.getDataValue("class_adjust_rst"))){
					    SqlClient.delete("removeRscTaskInfoSub", taskInfoSub, connection); 
						
					}
				}
				paramMap.put("doubt", "1");
				paramMap.remove("nodoubt");
				IndexedCollection usrList3 = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap,conId,0,0, connection);
				result=true;
				for(int i=0;usrList3.size()>i;i++){
					KeyedCollection r = (KeyedCollection)usrList3.get(i);
					Map taskInfoSub = new HashMap(); 
					taskInfoSub.put("serno",r.getDataValue("serno"));
					taskInfoSub.put("identy_duty",dutyNo); 
					KeyedCollection dataKColl = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", taskInfoSub, conIdSub, connection);
					
//					if("branchMgr".equals(duty)){
//						
//						KeyedCollection sOrgKColl = dao.queryFirst("SOrg", null, " where organno   ='"+(String)context.getDataValue(CMISConstance.ATTR_ORGID)+"'",connection);
//						if("02".equals((String)sOrgKColl.getDataValue("organlevel"))){//没值
//							domainkColl.put("risk_cls_status","01");
//						}else{
//							domainkColl.put("risk_cls_status","02");
//						} 
//					}
					domainkColl.put("serno",r.getDataValue("serno"));
					//更新数据 
					int updateResult = dao.update(domainkColl, connection);
					if(updateResult!=1){
						result=false;
						context.addDataField("flag","failed");
						return "";
					}else{
						index += 1;
					}
					if(taskInfoSub!=null&&dataKColl!=null&&NewStringUtils.isNotBlank((String)dataKColl.getDataValue("class_adjust_rst"))){
					    SqlClient.delete("removeRscTaskInfoSub", taskInfoSub, connection); 
						
					}
				}
				if(!dutyNo.equals("D0102")&&!dutyNo.equals("D0103")){//总行都可以看到
					paramMap.put("doubt", "1");//异常风险有记录级
				}else{
					paramMap.put("nodoubt", "1");//异常风险无记录级
				}
				result=true;
				IndexedCollection usrList4 = SqlClient.queryList4IColl("queryRscTaskInfoAnomalyClass", paramMap,new String[]{"condition1","condition2","condition3","condition4","condition5","condition6","condition7","condition8","condition9","condition10"},0,0, connection);
				for(int i=0;usrList4.size()>i;i++){
					KeyedCollection r = (KeyedCollection)usrList4.get(i);
					Map taskInfoSub = new HashMap(); 
					taskInfoSub.put("serno",r.getDataValue("serno"));
					taskInfoSub.put("identy_duty",dutyNo); 
					KeyedCollection dataKColl = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", taskInfoSub, conIdSub, connection);
					
					if("branchMgr".equals(duty)){
						
						KeyedCollection sOrgKColl = dao.queryFirst("SOrg", null, " where organno   ='"+(String)context.getDataValue(CMISConstance.ATTR_ORGID)+"'",connection);
						if("02".equals((String)sOrgKColl.getDataValue("organlevel"))){//没值
							domainkColl.put("risk_cls_status","01");
						}else{
							domainkColl.put("risk_cls_status","02");
						} 
					}
					domainkColl.put("serno",r.getDataValue("serno"));
					//更新数据 
					int updateResult = dao.update(domainkColl, connection);
					if(updateResult!=1){
						result=false;
						context.addDataField("flag","failed");
						return "";
					}else{
						index += 1;
					}
					if(taskInfoSub!=null&&NewStringUtils.isNotBlank((String)dataKColl.getDataValue("class_adjust_rst"))){
					    SqlClient.delete("removeRscTaskInfoSub", taskInfoSub, connection); 
						
					}
				}
				
				if(index==0){
					flag="out";
				}
				
				
			}else if("assignment".equals(type)&& !"".equals(type)){//赋默认值操作
				if (!"".equals(duty)  && duty !=null && "subHead".equals(duty)){//二级支行售后复核岗
					domainkColl.put("risk_cls_status","02");
				}else if (!"".equals(duty)  && duty !=null && "branchHead".equals(duty)){//总行分类审核岗
					domainkColl.put("risk_cls_status","03");
				}else if (!"".equals(duty)  && duty !=null && "headAcc".equals(duty)){//总行审批岗
					domainkColl.put("risk_cls_status","04");
				}else if (!"".equals(duty)  && duty !=null && "finally".equals(duty)){//总行审批岗
					domainkColl.put("risk_cls_status","05");
				}else{
					//若都不是默认登录人为客户经理
					domainkColl.put("risk_cls_status","01");
				}
				result = assignment(context,(String)domainkColl.getDataValue("risk_cls_status"),dutyNo);
				
				
			}
			else{//认定
				Map map = new HashMap();
				/*map.put("serno", domain.getSerno());*/
				if(!"".equals(duty)  && duty !=null && "mgr".equals(duty)){//客户经理
					subDomain.put("identy_duty","S0403");
					map.put("identy_duty", "S0403");
				}else if (!"".equals(duty)  && duty !=null && "subHead".equals(duty)){//客户经理主管
					subDomain.put("identy_duty","S0404");
					map.put("identy_duty", "S0404");
				}else if (!"".equals(duty)  && duty !=null && "branchHead".equals(duty)){//风险管理部审查岗
					 
					subDomain.put("identy_duty", "D0102");
					map.put("identy_duty", "D0102");
				}else if (!"".equals(duty)  && duty !=null && "headAcc".equals(duty)){//风险管理总经理
					
					subDomain.put("identy_duty", "D0103");
					map.put("identy_duty", "D0103");
				}else if (!"".equals(duty)  && duty !=null && "finally".equals(duty)){//总行受理
					 
					subDomain.put("identy_duty", "Q0000");
					map.put("identy_duty", "Q0000");
				}else{
					//若都不是默认登录人为客户经理主管
					subDomain.put("identy_duty", "S0404");
					map.put("identy_duty", "S0404");
				}
				
				//更新同一客户的风险分类 同一岗位的认定信息
				Map syncMap = new HashMap();
				syncMap.put("cus_id", domainkColl.getDataValue("cus_id"));
				syncMap.put("remark", domainkColl.getDataValue("remark"));
				syncMap.put("class_rst", domainkColl.getDataValue("class_rst"));
				if(!duty.equals("headAcc")&&!duty.equals("branchHead")&&!duty.equals("finally")){//无记录及
					syncMap.put("usr_cde",(String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
					syncMap.put("bch_cde",(String) context.getDataValue(CMISConstance.ATTR_ORGID)); 
				}
				syncMap.put("identy_duty",subDomain.get("identy_duty"));
				
				//遍历所有条进行更新
				result=true;
				IndexedCollection usrList = SqlClient.queryList4IColl("queryRscTaskInfoSynchr", syncMap,new String[]{"condition1","condition2"},0,0, connection);
				for(int i = 0;i<usrList.size();i++){
					KeyedCollection r = (KeyedCollection)usrList.get(i);
					map.put("serno",r.getDataValue("serno")); 
					KeyedCollection subResult=(KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", map,conIdSub, connection);
					if(subResult!=null){//已有数据更新
						subResult.put("class_adjust_rst",domainkColl.getDataValue("class_rst"));
						subResult.put("class_date",TimeUtil.getDateTime(context)); 
						if(!"".equals(domainkColl.getDataValue("remark"))&& domainkColl.getDataValue("remark")!=null){
							subResult.put("remark",domainkColl.getDataValue("remark"));
						}
						subResult.setName("RscTaskInfoSub");
						dao.update(subResult, connection);
						
					}else{
						/**从表新增数据**/
						subDomain.put("class_adjust_rst",domainkColl.getDataValue("class_rst"));
						subDomain.put("pk_id",CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context));
						subDomain.put("class_date",TimeUtil.getDateTime(context)); 
						subDomain.put("serno",r.getDataValue("serno"));
						if(!"".equals(domainkColl.getDataValue("remark"))&& domainkColl.getDataValue("remark")!=null){
							subDomain.put("remark",domainkColl.getDataValue("remark"));
						}
						subDomain.setName("RscTaskInfoSub");
						dao.insert(subDomain, connection);
					}
				} 
				
				int count =  SqlClient.update("updateRscTaskInfoSynchr",syncMap , syncMap,null, connection);
				 
			}
			
			//将处理结果放入Context中以便前端获取
			if(result){
				context.addDataField("flag", "success");
			} else {
				context.addDataField("flag",flag );
			}
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
	
	
	/**
	 * 
	 * <p>
	 *	<h2>简述</h2>
	 *		<ol>赋默认值方法</ol>
	 *	<h2>功能描述</h2>
	 *		<ol>无</ol>
	 *	</p>
	 * @param context
	 * @param risk_cls_status
	 * @param duty
	 * @return
	 * @throws EMPException
	 */
	public boolean assignment(Context context,String risk_cls_status,String duty) throws EMPException {
		Connection connection = null;
		boolean result = true;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			Map paramMap = new HashMap();
			if(!duty.equals("D0102")&&!duty.equals("D0103")&&!duty.equals("Q0000")){
				paramMap.put("usr_cde",(String) context.getDataValue("currentUserId"));
				paramMap.put("bch_cde",(String) context.getDataValue("organNo")); 
			}
			paramMap.put("risk_cls_status",risk_cls_status );
			paramMap.put("identy_duty",duty );
			if(duty.equals("S0403")){//客户经理做赋值操作
				paramMap.remove("bch_cde");
				paramMap.remove("usr_cde");
				paramMap.put("input_id",(String) context.getDataValue("currentUserId"));
				
			}
			String dutyNo1 = (String) context.getDataValue("dutyNoList");
			if("mgr".equals(duty)){
				String[] dutyNoArray = dutyNo1.split(",");
				for(String s : dutyNoArray){
					if(!"".equals(s)&& s !=null){
						if("S0404".equals(s)&&paramMap.containsKey("input_id")){//二级支行售后复合岗
							//duty="ourOrg";
							paramMap.remove("input_id");
							paramMap.put("br_id",(String)context.getDataValue(CMISConstance.ATTR_ORGID)); 
							break;
						}
					}
				}
			}
			 
			IndexedCollection infoIColl = new IndexedCollection();
					//comp.queryRscTaskInfoList(null, paramMap, connection);
			 
			infoIColl = SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap, new String[]{"condition1","condition2","condition3","condition4","condition5","condition6","condition7","condition8","condition9","condition10","condition11","condition13","condition14"}, 0, 0, connection);
			for(int i=0;infoIColl.size()>i;i++){
				KeyedCollection infoKColl = (KeyedCollection)infoIColl.get(i);
				Map map  = new HashMap();
				map.put("serno",infoKColl.getDataValue("serno"));
				map.put("identy_duty", duty);
				KeyedCollection subResult=(KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", map,null, connection);
				if(subResult==null){
					map.remove("identy_duty");
					if("S0404".equals(duty)){//二级支行售后复合岗//客户经理主管
						map.put("identy_duty", "S0403");
					}else if("D0102".equals(duty)){//总行分类认定岗
						map.put("identy_duty", "S0404");
					}else if("D0103".equals(duty)){//总行分类审核岗
						map.put("identy_duty", "D0102");
					}else if("Q0000".equals(duty)){//总行分类审核岗
						map.put("identy_duty", "D0103");
					}
					if(duty.equals("S0403")){
						subResult = new KeyedCollection();
						subResult.put("class_adjust_rst", infoKColl.getDataValue("model_class_rst")); 
						subResult.put("remark", infoKColl.getDataValue("model_class_rea"));
						
					}else{
						subResult=(KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", map, new String[]{"condition1","condition2","condition3","condition4"},connection);
						
					}
					if(subResult!=null){
						/**从表新增数据**/
						KeyedCollection subDomain = new KeyedCollection();
						subDomain.put("class_adjust_rst", subResult.getDataValue("class_adjust_rst"));//分类结果
						subDomain.put("pk_id",CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context));
						subDomain.put("class_date",TimeUtil.getDateTime(context));
						subDomain.put("serno",infoKColl.getDataValue("serno"));
						subDomain.put("identy_duty",duty);
						if(!"".equals(subResult.getDataValue("remark"))&& subResult.getDataValue("remark")!=null){
							subDomain.put("remark",subResult.getDataValue("remark"));
						}
						subDomain.setName("RscTaskInfoSub");
						dao.insert(subDomain, connection);
						KeyedCollection info = new KeyedCollection();
						info.put("serno",infoKColl.getDataValue("serno"));	 
						info.put("class_rst",infoKColl.getDataValue("model_class_rst"));
						info.setName("RscTaskInfo");
						dao.update(info, connection);
					}
					
					
				}
			}
			
		} catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return result;
	}
	
	/**
	 * 
	 * <p>
	 *	<h2>简述</h2>
	 *		<ol>更新借据风险分类认定</ol>
	 *	<h2>功能描述</h2>
	 *		<ol>无</ol>
	 *	</p>
	 * @param context
	 * @param risk_cls_status
	 * @param duty
	 * @return
	 * @throws EMPException
	 */
	public boolean updateAccLonaRiskClass(Context context,KeyedCollection rscTaskInfo,String duty) throws EMPException {
		Connection connection = null;
		boolean result = true;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
//			EMPLog.log(CMISExtConstance.LOGGER_NAME_CMIS, EMPLog.INFO, 0,
//					"-----------借据编号"+rscTaskInfo.getBillNo()+"风险认定最后一个岗位提交更新 借据五级分类九级分类-----------");
			Map accParam = new HashMap();
			accParam.put("bill_no", (String)rscTaskInfo.getDataValue("bill_no"));
			KeyedCollection accLoan =  dao.queryDetail("AccLoan", accParam, connection);
			if(accLoan!=null&&NewStringUtils.isNotBlank((String)accLoan.getDataValue("bill_no"))){
				
			
			String[] conId = new String[]{"condition1","condition2","condition3","condition4"};
			Map rscmap = new HashMap();
			rscmap.put("serno",rscTaskInfo.getDataValue("serno"));
			rscmap.put("identy_duty",duty);
			KeyedCollection rscTaskInfoSub = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", rscmap, conId, connection);
			Map map = new HashMap();
			map.put("serno",rscTaskInfo.getDataValue("serno"));
			map.put("identy_duty", "Q0000");
			KeyedCollection subResult=(KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", map, conId, connection);
			if(subResult!=null){//已有数据更新
				subResult.put("class_adjust_rst",rscTaskInfoSub.getDataValue("class_adjust_rst"));
				subResult.put("class_date",TimeUtil.getDateTime(context));
				
				if(!"".equals(rscTaskInfo.getDataValue("remark"))&& rscTaskInfo.getDataValue("remark")!=null){
					subResult.put("remark",rscTaskInfo.getDataValue("remark")); 
				}
				dao.update(subResult, connection);
				
			}else{
				KeyedCollection subDomain = new KeyedCollection();
				/**从表新增数据**/
				subDomain.put("class_adjust_rst",rscTaskInfoSub.getDataValue("class_adjust_rst"));
				subDomain.put("pk_id",CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context));
				subDomain.put("class_date",TimeUtil.getDateTime(context));
				subDomain.put("identy_duty","Q0000");
				subDomain.put("serno",rscTaskInfo.getDataValue("serno"));
				if(!"".equals(rscTaskInfo.getDataValue("remark"))&& rscTaskInfo.getDataValue("remark")!=null){
					subDomain.put("remark",rscTaskInfo.getDataValue("remark"));
				}else{
					KeyedCollection infoSub = new KeyedCollection();
					infoSub.put("serno",rscTaskInfo.getDataValue("serno"));
					infoSub.put("identy_duty","D0103");
					infoSub = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike",infoSub,conId, connection);
					subDomain.put("remark",infoSub.getDataValue("remark"));
				}
				subDomain.setName("RscTaskInfoSub");
				dao.insert(subDomain, connection);
			}

		//	accLoan.put("nine_class",rscTaskInfoSub.getDataValue("class_adjust_rst"));
			
			switch (Integer.parseInt((String) rscTaskInfoSub.getDataValue("class_adjust_rst"))) {
			case 10://正常
				accLoan.put("five_class","10");
				break;
			case 15://正常-
				accLoan.put("five_class","10");
				break;
			case 20://关注
				accLoan.put("five_class","20");
				break;
			case 25://关注-
				accLoan.put("five_class","20");
				break;
			case 30://次级
				accLoan.put("five_class","30");
				break;
			case 35://次级-
				accLoan.put("five_class","30");
				break;
			case 40://可疑
				accLoan.put("five_class","40");
				break;
			case 45://可疑-
				accLoan.put("five_class","40");
				break;
			case 50://损失
				accLoan.put("five_class","50");
				break;
			
			default:
				break;
			}
			String openDate = (String)context.getDataValue(CMISConstance.OPENDAY);
			//accLoan.put("five_class_time",openDate);
		//	accLoan.put("nine_class_time",openDate);
			accLoan.setName("AccLoan");
			dao.update(accLoan, connection);
			}
			
		} catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return result;
	}
	

}
