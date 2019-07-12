package com.yucheng.cmis.biz01line.mort.mortguarantyevalvalue;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.psp.msi.PspServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryMortGuarantyEvalValueDetailOp  extends CMISOperation {
	
	private final String modelId = "MortGuarantyEvalValue";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no = "";
		try{
			connection = this.getConnection(context);
			guaranty_no = (String) context.getDataValue("guaranty_no");
			//拼接where条件
			String conStr = "where guaranty_no = '"+guaranty_no+"'";
			TableModelDAO dao = this.getTableModelDAO(context);
			//查询符合条件的押品基本信息
			IndexedCollection iColl = dao.queryList("MortGuarantyBaseInfo",conStr,connection);
			if(0!=iColl.size()){
				//已经存在押品基本信息，则将其放入context中
				KeyedCollection mortGuarantyBaseInfo = (KeyedCollection) iColl.get(0);
				mortGuarantyBaseInfo.setName("MortGuarantyBaseInfo");
				this.putDataElement2Context(mortGuarantyBaseInfo, context);
			}
			//查询符合条件的评估信息记录
			IndexedCollection ic = dao.queryList(modelId, conStr, connection);
			if(0==ic.size()){
				//不存在时，则为新增
				context.setDataValue("guaranty_no", guaranty_no);
			}else{
				//已经存在评估信息，则将其放入context中
				KeyedCollection kColl = (KeyedCollection) ic.get(0);
								
				String[] args=new String[] { "eval_org_name","eval_org_name" };
				String[] modelIds=new String[]{"CusOrgAppMng","CusOrgAppMng"};
				String[] modelForeign=new String[]{"cus_id","cus_id"};
				String[] fieldName=new String[]{"end_date","cus_name"};
				String[] resultName = new String[]{"eval_end_date","eval_name"};
				SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
				
				kColl.setName(modelId);
				this.putDataElement2Context(kColl, context);
			}
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PspServiceInterface service = (PspServiceInterface)serviceJndi.getModualServiceById("pspServices", "psp");
			IndexedCollection iC  = service.getPspGuarantyValueReevalList(guaranty_no, connection, context);
			iC.setName("PspGuarantyValueReevalList");
			this.putDataElement2Context(iC, context);
		}catch (EMPException ee) {
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
