package com.yucheng.cmis.biz01line.batch.op.iqpbatchmng;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class ImportIqpBatchPageOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		/**
		 * 票据批次引入操作处理逻辑：
		 * 1.根据录入的批次号进行判断是否存在该批次.
		 * 2.判断该批次状态是否可引用
		 * 3.通过流水号判断该笔业务是否已经存在批次信息。
		 */
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String batch_no = "";
			String serno = "";
			if(context.containsKey("batch_no")){
				batch_no = (String)context.getDataValue("batch_no");
			}
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryDetail("IqpBatchMng", batch_no, connection);
			if(kc != null && ((String)kc.getDataValue("batch_no"))!= null){
				/** 根据批次号状态判断该批次是否可引用 */
				String batchStatus = "";
				batchStatus = (String)kc.getDataValue("status");
				String batchSerno = (String)kc.getDataValue("serno");//从批次包中查询业务编号，如果不为空，说明该包已经被引用。
				if("".equals(batchSerno) || batchSerno == null){
					//通过本笔业务编号serno查询该笔业务是否已经引用其他的批次包。
					KeyedCollection iqpbathKcoll = dao.queryDetail("IqpBatchMng", serno, connection);
					String batchnoExists = (String)iqpbathKcoll.getDataValue("batch_no");
					//String ;
					
					/** 更新批次表中业务关联关系 */
					KeyedCollection upKColl = new KeyedCollection("IqpBatchMng");
					upKColl.addDataField("batch_no", batch_no);
					upKColl.addDataField("serno", serno);
					int count = dao.update(upKColl, connection);
					if(count != 1){
						context.addDataField("flag", "failed");
						context.addDataField("msg", "【"+batch_no+"】批次号关联现有业务失败");
					}else {
						context.addDataField("flag", "success");
						context.addDataField("msg", "关联成功");
					}
				}else {
					context.addDataField("flag", "failed");
					context.addDataField("msg", "【"+batch_no+"】批次号存在现有业务中，不能引用");
				}
			}else {
				context.addDataField("flag", "failed");
				context.addDataField("msg", "系统中不存在【"+batch_no+"】批次号");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
