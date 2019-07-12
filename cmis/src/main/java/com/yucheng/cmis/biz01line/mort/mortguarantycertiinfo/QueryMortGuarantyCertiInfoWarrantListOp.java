package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortGuarantyCertiInfoWarrantListOp extends CMISOperation {


	private final String modelId = "MortGuarantyCertiInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String warrant_type = (String) context.getDataValue("warrantTypeNoStr");
			String warrant_no = (String) context.getDataValue("warrantNoStr");
			String[]warrantTypeStr = warrant_type.split(",");
			String[]warrantNoStr = warrant_no.split(",");
			Map<String,String> map = new HashedMap();
			IndexedCollection iColl = new IndexedCollection();
			KeyedCollection kc = null;
			for(int i=0;i<warrantTypeStr.length;i++){
				map.put("warrant_type",warrantTypeStr[i]);
				map.put("warrant_no",warrantNoStr[i]);
				kc = dao.queryDetail(modelId, map, connection);
				iColl.add(kc);
			}
			iColl.setName("MortGuarantyCertiInfoList");
			SInfoUtils.addSOrgName(iColl, new String[] {"keep_org_no"});
			this.putDataElement2Context(iColl, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
		
	}

}
