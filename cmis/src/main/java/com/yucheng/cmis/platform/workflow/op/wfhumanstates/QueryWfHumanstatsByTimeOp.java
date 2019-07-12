package com.yucheng.cmis.platform.workflow.op.wfhumanstates;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * <p>根据前端录入的起止时间，查找是否有重叠的记录</p>
 * 如果申请类型不为空，则时间+申请类型不能有冲突配置；<br>
 * 如果申请类型为空，则时间不能有冲突配置；<br>
 * 1存在，0不存在
 * 
 * @author liuhw
 *
 */
public class QueryWfHumanstatsByTimeOp extends CMISOperation {
	
	private String modelId = "WfHumanstates";
	private String optypeUpdate = "update";
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		String beginTime, endTime, opType, appId, vicarioustype;
		try {
			beginTime = (String) context.getDataValue("WfHumanstates.begintime");
			endTime = (String) context.getDataValue("WfHumanstates.endtime");
			vicarioustype = (String) context.getDataValue("WfHumanstates.vicarioustype");
			opType = (String) context.getDataValue("optype");
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("表单数据不完整！");
		}
		
		Connection connection = null;
		try {
			String currentuserid = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " WHERE USERID='" + currentuserid + "' AND (('"
					+ beginTime + "' BETWEEN BEGINTIME AND ENDTIME ) OR ('" + endTime + "' BETWEEN BEGINTIME AND ENDTIME )" +
					" OR (BEGINTIME BETWEEN '" + beginTime + "' AND '" + endTime + "') " +
					"OR (ENDTIME BETWEEN '" + beginTime + "' AND '" + endTime + "')) ";
			if("1".equals(vicarioustype)) {  //0：默认指定 1：特殊指定
				appId = (String) context.getDataValue("WfHumanstates.appid");
				condition += " AND APPID='"+appId+"' ";
			}

			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			KeyedCollection kColl = new KeyedCollection();
			String result, retBeginTime, retEndTime;
			if(iColl.size() == 0) {
				result = "0";
				retBeginTime = "";
				retEndTime = "";
			} else {
				String pkey = null, pkeyQuery;
				try {
					pkey = (String) context.getDataValue("WfHumanstates.pkey");
				} catch (Exception e) {
					if(opType.equals(optypeUpdate)) {
						e.printStackTrace();
						throw new EMPException("The value of pkey is null!");
					}
				}
				if(iColl.size() == 1) {
					kColl = (KeyedCollection) iColl.get(0);
					if(opType.equals(optypeUpdate)) {
						pkeyQuery = (String) kColl.getDataValue("pkey");
						if(pkey.equals(pkeyQuery))
							result = "0";
						else
							result = "1";
					} else {
						result = "1";
					}
				} else {
					result = "1";
					for(int i=0; i<iColl.size(); i++) {
						KeyedCollection kCollT = (KeyedCollection) iColl.get(i);
						pkeyQuery = (String) kCollT.getDataValue("pkey");
						if(!pkeyQuery.equals(pkey)) {
							kColl = kCollT;
							break;
						}
					}
				}
				retBeginTime = (String) kColl.getDataValue("begintime");
				retEndTime = (String) kColl.getDataValue("endtime");
			}
			KeyedCollection kCollRes = new KeyedCollection("retKcoll");
			kCollRes.put("result", result);
			kCollRes.put("begintime", retBeginTime);
			kCollRes.put("endtime", retEndTime);
			this.putDataElement2Context(kCollRes, context);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e.getMessage());
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
