package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AsynException;

public class CalSumOfAmtMethodOp extends CMISOperation {
	private final String modelId = "RscTaskInfo";

	@Override
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		Connection connection = null;
		String conditionStr = "";
		try {
			connection = this.getConnection(context);
			/**贷款总金额**/
			String loanAmt = "";
			/**不良贷款总金额**/
			String loanBalance = "";
			
			/***判断岗位****/
			String  duty = "";
			try {
				duty = (String) context.getDataValue("duty");
			} catch (Exception e) {
			}
			String  bch_cde = "";
			try {
				bch_cde = (String) context.getDataValue("bch_cde");
			} catch (Exception e) {
			}
			String usr_cde="";
			try {
				usr_cde = (String) context.getDataValue("usr_cde");
			} catch (Exception e) {
			}
			
			//获取当前登入用户的岗位
			//String  dutyNo=(String) context.get(CMISConstance.ATTR_DUTYNO_LIST);
			String  dutyNo = "";
			if(context.containsKey("dutyno")){
				dutyNo=(String) context.getDataValue("dutyno");
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
							}else if("Q0000".equals(s)){//已完成
								duty="finally";
								break;
							}
							/*else if("G0020".equals(s)){//总行售后服务部审批岗
								duty="headApp";
								break;
							}*/
						}
					}
				}
			}

			//定义查询参数map
			Map paramMap = new HashMap();			
			//通过组件服务实例化业务组件
		 

			//判断当前进入的岗位
			if(!"".equals(duty)  && duty !=null && "mgr".equals(duty)){//客户经理
				paramMap.put("risk_cls_status", "01");//		月末跑批生成的任务清单的分类状态为“未分类”；
				paramMap.put("identy_duty", "S0403");
				if(bch_cde!=null&&bch_cde!=""){
				    paramMap.put("bch_cde", bch_cde);
				}
				if(usr_cde!=null&&usr_cde!=""){
					paramMap.put("usr_cde",usr_cde);
				}
			}else if (!"".equals(duty)  && duty !=null && "subHead".equals(duty)){//客户经理主管
				paramMap.put("risk_cls_status", "02");
				paramMap.put("identy_duty", "S0404");
				if(bch_cde!=null&&bch_cde!=""){
				    paramMap.put("bch_cde", bch_cde);
				}
				if(usr_cde!=null&&usr_cde!=""){
					paramMap.put("usr_cde",usr_cde);
				}
			}else if (!"".equals(duty)  && duty !=null && "branchHead".equals(duty)){//总行分类认定岗
				paramMap.clear();
				paramMap.put("risk_cls_status", "03");
				paramMap.put("identy_duty","D0102" );
			}else if (!"".equals(duty)  && duty !=null && "headAcc".equals(duty)){//总行分类审核岗
				paramMap.clear();
				paramMap.put("risk_cls_status", "04");
				paramMap.put("identy_duty", "D0103");
			}else if (!"".equals(duty)  && duty !=null && "finally".equals(duty)){//总行审批
				paramMap.clear();
				paramMap.put("risk_cls_status", "05");
				paramMap.put("identy_duty", "Q0000");
			}else{
				if(bch_cde!=null&&bch_cde!=""){
				    paramMap.put("bch_cde", bch_cde);
				}
				if(usr_cde!=null&&usr_cde!=""){
					paramMap.put("usr_cde",usr_cde);
				}
				//若都不是默认支行行长
				paramMap.put("risk_cls_status", "02");
			}
			
			
			//查询机构列表数据
		
			KeyedCollection domain =  (KeyedCollection) SqlClient.queryFirst("calSumOfAmtByClassRst", paramMap, new String[]{"condition1","condition2","condition3","condition4","condition5"}, connection);
			if(null != domain){
				/**放入前台以便展示**/
				context.put("loanAmt", domain.getDataValue("loanamt"));
				context.put("loanBalance", domain.getDataValue("loanbalance"));				
			}else{
				context.put("loanAmt", loanAmt);
				context.put("loanBalance", loanBalance);	
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

}
