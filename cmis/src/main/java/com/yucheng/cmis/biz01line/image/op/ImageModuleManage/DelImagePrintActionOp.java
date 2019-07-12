package com.yucheng.cmis.biz01line.image.op.ImageModuleManage;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.image.op.pubAction.ImagePubFunction;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class DelImagePrintActionOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);			
			ImagePubFunction cmisOp = new ImagePubFunction();			
			String url = "";
			
			/*** 公共部分url的拼接 ***/
			url = cmisOp.delFirstPartUrl(url, context);
			url = url.replaceAll("SunIASRequestServlet", "Printbarcode");	//打印单独调用另外的url
			
			/*** 处理生成业务段url的参数 ***/
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("cus_id", context.getDataValue("cus_id"));	//客户编号
			kColl.addDataField("prd_stage", context.getDataValue("prd_stage"));	//业务阶段
			kColl.addDataField("prd_id", context.getDataValue("prd_id"));	//产品编号
			
			String serno = context.getDataValue("serno").toString();	
			if(serno.indexOf(",") >= 0){	//展期要同时传原业务编号和展期编号，中间以","隔开
				String[] sernos = serno.split(",");
				kColl.addDataField("serno", sernos[0]);	//调用url时取原业务编号
			}else{
				kColl.addDataField("serno", serno);	//一般情况直接取serno
			}
			
			String[] args = new String[] { "cus_id" };
			String[] modelIds = new String[] { "CusBase" };
			String[] modelForeign = new String[] { "cus_id" };
			String[] fieldName = new String[] { "belg_line" };
			String[] resultName = new String[] { "cus_type" };	//翻译客户条线
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			if(kColl.getDataValue("cus_type").equals("BL200")){
				kColl.setDataValue("cus_type", "BL100");
			}
			
			/*** 调用条码打印处理方法 ***/
			url = cmisOp.delSecondPrintUrl(url, kColl, context);
			
			context.addDataField("flag", PUBConstant.SUCCESS);
			context.addDataField("value", "");
			context.addDataField("resultUrl", url);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}

}