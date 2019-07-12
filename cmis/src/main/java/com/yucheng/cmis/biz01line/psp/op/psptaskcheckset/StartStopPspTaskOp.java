package com.yucheng.cmis.biz01line.psp.op.psptaskcheckset;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class StartStopPspTaskOp extends CMISOperation {


	private final String modelId = "PspTaskCheckSet";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String flag = "success";
		try{
			connection = this.getConnection(context);
		    String serno = null;//任务编号
			String brands=null;//标志（start-启用，stop-停用）
			String task_status = null;//任务状态
			String check_mode = null;//检查类型
			String cus_type = null;//客户类型
			brands = (String) context.getDataValue("brands");
			serno = (String) context.getDataValue("serno");
			if(context.containsKey("check_mode")){
				check_mode = (String) context.getDataValue("check_mode");
			}
			if(context.containsKey("cus_type")){
				cus_type = (String) context.getDataValue("cus_type");
			}
			
			if("start".equals(brands)){//启用状态
				task_status="01";
			}else if("stop".equals(brands)){//停用状态
				task_status="02";
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = new KeyedCollection(modelId);
			IndexedCollection icoll = new IndexedCollection();
			if("00".equals(check_mode)){//首次检查
				if("start".equals(brands)){
					String condition = " where check_mode = '00' and cus_type = '"+cus_type+"' and task_status = '01' ";
					icoll =  dao.queryList(modelId, condition  , connection);
					if(icoll!=null&&icoll.size()>0){
						flag = "exist";
					}else{
						kc.addDataField("serno",serno);
						kc.addDataField("task_status",task_status);
						dao.update(kc, connection);
					}
				}else{
					kc.addDataField("serno",serno);
					kc.addDataField("task_status",task_status);
					dao.update(kc, connection);
				}
			}else{
				kc.addDataField("serno",serno);
				kc.addDataField("task_status",task_status);
				dao.update(kc, connection);
			}
			
		}catch (EMPException ee) {
			context.addDataField("flag","error");
			throw ee;
		} catch(Exception e){
			context.addDataField("flag","error");
			throw new EMPException(e);
		} finally {
			context.addDataField("flag",flag);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
