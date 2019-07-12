package com.yucheng.cmis.biz01line.document;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class DeletePubDocumentInfoRecordOp extends CMISOperation {

	private final String modelId = "PubDocumentInfo";
	private final String pk1_name = "file_pk";

	/**
	 * 删除文件
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);
			
			String pk1_value = null;
			try {
				pk1_value = (String) context.getDataValue(pk1_name);
			} catch (Exception e) {
			}
			if (pk1_value == null || pk1_value.length() == 0){
				context.addDataField("flag","fild");
				context.addDataField("message","删除文档失败，错误描述：传入的文档编号为空！");
				return "0";
			}

			String filepath = null;
			try {
				filepath = (String) context.getDataValue("file_path").toString().trim();
			} catch (Exception e) {
			}

			File file = new File(filepath);
			if (file.isFile()) {
				file.delete();   // 删除文件

				int count = dao.deleteByPk(modelId, pk1_value, connection);
				if (count != 1) {
					context.addDataField("flag","fild");
					context.addDataField("message","删除文档失败，错误描述：找不到对应的文档上传记录！");
					return "0";
				}
			} else {
				context.addDataField("flag","fild");
				context.addDataField("message","删除文档失败，错误描述：文件不存在");
				return "0";
			}
			
			context.addDataField("message", "删除文档成功");
			context.addDataField("flag", "success");
		}catch(Exception e){
			context.addDataField("flag","fild");
			context.addDataField("message","删除文档失败，错误描述："+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
