package com.yucheng.cmis.biz01line.document.cybercusinfo.cybercusdocumentinfo;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;

/**
 * 删除文件
 *@author 
 *@time
 *@description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
 *@version v1.0 yezm
 *
 */
public class DeleteCyberCusDocumentInfoRecordOp extends CMISOperation {

	private final String modelId = "CyberCusDocumentInfo";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);
			
			IndexedCollection icoll = (IndexedCollection)context.getDataElement("CyberCusDocumentInfoList");
			for (Iterator iterator = icoll.iterator(); iterator.hasNext();) {
				KeyedCollection kcoll = (KeyedCollection) iterator.next();
				String tmpFile = (String)kcoll.getDataValue("file_path").toString().trim();
				tmpFile = new String(tmpFile.getBytes("ISO8859-1"),"UTF-8");
				String[]tmpFileList = tmpFile.split("@");
				String filePath2 = tmpFileList[0];
				String fileName = tmpFileList[1];
				
				FTPUtil util = new FTPUtil();
				boolean fg=util.deleteFileFtp(fileName,filePath2);   // 删除文件
				if (!fg) {
					context.addDataField("flag","fild");
					context.addDataField("message","删除文档失败，错误描述：找不到对应的文档上传记录！");
					return "0";
				}
				
			}
			
							
//			File file = new File(filepath);
//			if (file.isFile()) {
//				boolean fg=file.delete();   // 删除文件
//
//				//int count = dao.deleteByPk(modelId, pk1_value, connection);
//				if (!fg) {
//					context.addDataField("flag","fild");
//					context.addDataField("message","删除文档失败，错误描述：找不到对应的文档上传记录！");
//					return "0";
//				}
//				
//			} else {
//				context.addDataField("flag","fild");
//				context.addDataField("message","删除文档失败，错误描述：文件不存在");
//				return "0";
//			}
			
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
