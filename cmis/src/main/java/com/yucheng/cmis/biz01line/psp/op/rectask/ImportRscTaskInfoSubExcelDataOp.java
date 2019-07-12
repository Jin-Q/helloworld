package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.ExcelRead;
import com.yucheng.cmis.pub.util.ExcelVO;

public class ImportRscTaskInfoSubExcelDataOp extends CMISOperation {
	private final String modelId = "RscTaskInfo";

	@Override
	public String doExecute(Context context) throws EMPException {
		 Connection connection = null;
	        try {
	            // 获取request对象
	            HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);

	            // 针对html4判断文件是否超出设定大小
	            String flag = (String) request.getAttribute("SizeLimitExceededException");
	            if (flag != null && "1".equals(flag)) {
	                context.addDataField("status", "SizeLimitExceededException");
	                return "";
	            }
	            boolean result = false;
	            connection = this.getConnection(context);
	            // 将公共文件上传接口处理来的上传文件信息得到
	            IndexedCollection fileUploadInfos = (IndexedCollection) request.getAttribute("uploadSuccessFileList");
	            // FileUploadInfo[] fileUploadInfos = (FileUploadInfo[])obj;
	            TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
	            if (!fileUploadInfos.isEmpty()) {
	                KeyedCollection fileUploadInfo = (KeyedCollection) fileUploadInfos.get(0);
	                if (fileUploadInfo != null) {
	                    String filePath = (String) fileUploadInfo.getDataValue("file_path");
	                    // 读取Excel文件
	                    ExcelVO evo = ExcelRead.readExcel(filePath);
	                    KeyedCollection domain = new KeyedCollection();
	               //     IndexedCollection pojoFromExcelVo = (IndexedCollection)ExcelUtil.getPojoFromExcelVo(context, evo, domain);

	               //     SqlClient.insert("batchInsertRscTaskInfoSub", pojoFromExcelVo.toArray(), connection);
	                    result = true;
	                }
	            }
	            if (result) {
	                context.addDataField("status", "success");
	                 context.addDataField("message", "上传读取成功！");
	             //   context.addDataField("message", "SYS0000011");
	                context.addDataField("params", "");
	            } else {
	                context.addDataField("status", "failed");
	                 context.addDataField("message", "上传读取失败！");
	              //  context.addDataField("message", "SYS0000012");
	                context.addDataField("params", "");
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
