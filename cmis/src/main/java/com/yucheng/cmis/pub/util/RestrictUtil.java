package com.yucheng.cmis.pub.util;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.base.CMISConstance;

public class RestrictUtil {
	/**
	 * POP框记录集权限控制封装
	 * <h1>记录及权限使用规则：返回前端pop框配置的URL中传递的参数</h1>
	 * @param connection 数据库连接
	 * @param context 上下文
	 * @return 传递的封装条件，示例（input_br_id = '0000'）
	 * @throws EMPException
	 */
	private static final String FLAG = "restrictUsed";//记录级权限使用标识（true,使用记级权限）
	private static final String FLAG_TAB = "restrict_tab";//记录级权限使用标识（true,使用记级权限）
	private static final String SELF_PARAM = "popRestriceParam";//记录级权限使用标识（true,使用记级权限）
	public static String getNewRestrictSelf(String modelId, Connection connection,Context context) throws EMPException {
		String restrictUsed = "";
		String conditionStr = " WHERE 1 = 1 ";
		if(context.containsKey(FLAG)){
			restrictUsed = (String)context.getDataValue(FLAG);
			if(restrictUsed.equals("true")){//使用记录级权限
				String popRestriceParam = "";
				/** 判断使用的记录级权限是资源配置下，还是自定义记录集权限，如果包含popRestriceParam则表示使用的是pop框，否则使用资源定义 */
				if(context.containsKey(SELF_PARAM)){
					popRestriceParam = (String)context.getDataValue(SELF_PARAM);
					if(popRestriceParam != null && popRestriceParam.trim().length() > 0){
						if(popRestriceParam.toLowerCase().indexOf("where") != -1){
							conditionStr = " and  ("+ popRestriceParam+") ";
						}else {
							conditionStr = (" WHERE ("+popRestriceParam+") ");
						}
					}
				}else {
					/** 暂时不适用与tab页签挂接的tab页签控制，如果需要控制需要在资源第一种添加参数控制 */
				}
			}
		}else {
			if(context.containsKey(FLAG_TAB)){
				/** 暂时不适用与tab页签挂接的tab页签控制，如果需要控制需要在资源第一种添加参数控制 */
			}else {
				RecordRestrict recordRestrict = (RecordRestrict)context.getService(CMISConstance.ATTR_RECORDRESTRICT);
				conditionStr = recordRestrict.judgeQueryRestrict(modelId, conditionStr, context, connection);
			}
		}
		if(conditionStr == null || conditionStr.trim().length() == 0){
			conditionStr = " WHERE 1 = 1 ";
		}
		return conditionStr;
	}
}
