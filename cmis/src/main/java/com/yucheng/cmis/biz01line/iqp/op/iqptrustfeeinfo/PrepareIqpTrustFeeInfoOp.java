package com.yucheng.cmis.biz01line.iqp.op.iqptrustfeeinfo;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2014-12-29
*@description 需求编号：【XD141204082】关于信托贷款业务需求调整
*@version v1.0
*
 */
public class PrepareIqpTrustFeeInfoOp extends CMISOperation {


	private final String modelId = "IqpTrustFeeInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = (String) context.getDataValue("serno");
			TableModelDAO dao = this.getTableModelDAO(context);
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String conditionSelect ="select count(*) as count from iqp_trust_fee_info i where i.serno ='"+serno+"'";
			IndexedCollection temp=	TableModelUtil.buildPageData(null, dataSource, conditionSelect);
			KeyedCollection tempK = (KeyedCollection) temp.get(0);
			if(temp !=null && temp.size()>0 && !"0".equals(tempK.getDataValue("count"))){
				KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
				this.putDataElement2Context(kColl, context);
				return "exist";
			}else{
				context.addDataField("sernoStr", serno);
			}
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "inexistence";
	}

}
