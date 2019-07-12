package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class FncStatOp extends CMISOperation {
//	private final String modelId = "FncStatBase";
//    private boolean updateCheck = false;
	@Override
	public String doExecute(Context context) throws EMPException {
		
//		Connection connection = this.getConnection(context);
		
		 //加记录级权限
//		if(this.updateCheck){
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//		}			
		/**
		 * 从context中取出cusId(客户编号),statPrdStyle(报表周期类型),statPrd(报表期间 格式：YYYYMM)
		 */
		String cusId = (String)context.getDataValue("cus_id");
		if(cusId == null || cusId.length() == 0){
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的客户编号为空");
			return PUBConstant.FAIL;
		}
		String statPrdStyle = (String)context.getDataValue("stat_prd_style");
		if(statPrdStyle == null || statPrdStyle.length() == 0){
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表周期类型为空");
			return PUBConstant.FAIL;
		}
		String statPrd = (String)context.getDataValue("stat_prd");
		if(statPrd == null || statPrd.length() == 0){
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表期间类型为空");
			return PUBConstant.FAIL;
		}
		String state_flg = (String)context.getDataValue("state_flg");
		if(state_flg == null || state_flg.length() == 0){
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表状态为空");
			return PUBConstant.FAIL;
		}
		String stat_style = (String)context.getDataValue("stat_style");
		if(stat_style == null || stat_style.length() == 0){
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表口径为空");
			return PUBConstant.FAIL;
		}
		String fnc_type = (String)context.getDataValue("fnc_type");
		if(fnc_type == null || fnc_type.length() == 0){
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表类型为空");
			return PUBConstant.FAIL;
		}
		
		return PUBConstant.SUCCESS;
	}

//	public void setUpdateCheck(boolean updateCheck) {
//		this.updateCheck = updateCheck;
//	}
	
}
