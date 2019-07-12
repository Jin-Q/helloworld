package com.yucheng.cmis.biz01line.upload.op;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddUploadFileInfoRecordOp extends CMISOperation {

	private final String modelId = "PubDocumentInfo";

	/**
	 * 上传文件
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = new KeyedCollection();

			kColl.setName(modelId);
			kColl = upLoad(context, kColl);// 上传文件
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String file_pk = CMISSequenceService4JXXD.querySequenceFromED("FILE", "all", connection, context);
			kColl.addDataField("file_pk", file_pk);
			kColl.addDataField("file_type", context.getDataValue("file_type").toString());// 业务类型
			kColl.put("serno",context.getDataValue("serno").toString());//业务流水
			kColl.addDataField("create_date", (String) context.getDataValue(PUBConstant.OPENDAY));
			kColl.addDataField("create_man", (String) context.getDataValue(PUBConstant.currentUserId));
			kColl.addDataField("create_org", (String) context.getDataValue(PUBConstant.loginorgid));
			dao.insert(kColl, connection);

			context.addDataField("message", "上传资源成功");
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "error");
			context.addDataField("message", "失败原因：" + e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

	public KeyedCollection upLoad(Context context, KeyedCollection kColl) throws EMPException {
		// 获取上传文件指定路径pFile
		String tempFilePath = "";
		try {
			tempFilePath = (String) context.getDataValue("pFile");
		} catch (Exception e) {
		}

		int post1 = tempFilePath.indexOf("tmpFileName=");
		int post2 = tempFilePath.indexOf("srcFileName=");
		// 服务端文件名
		String tempFileName = tempFilePath.substring(post1 + 12, post2).trim();
		String filename = tempFilePath.substring(post2 + 12);
		filename = filename.replaceAll(".*\\\\", "");
		File file1 = new File(tempFileName);

		kColl.addDataField("file_name", filename);
		kColl.addDataField("file_path", tempFileName);
		kColl.addDataField("file_size", file1.length() / 1024);
		kColl.addDataField("file_unit", "KB");
		return kColl;
	}

}
