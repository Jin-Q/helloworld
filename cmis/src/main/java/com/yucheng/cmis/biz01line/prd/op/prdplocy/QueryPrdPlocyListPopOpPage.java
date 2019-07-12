package com.yucheng.cmis.biz01line.prd.op.prdplocy;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdPlocyListPopOpPage extends CMISOperation {
	private final String modelId = "PrdPlocy";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String conditionStr = "";
		IndexedCollection iCollPage = new IndexedCollection();
		try{
			connection = this.getConnection(context);
			String schemeId = (String)context.getDataValue("schemeId");

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			if(queryData!=null){
				String schemecode = (String)queryData.getDataValue("schemecode");
				String ifwarrant = (String)queryData.getDataValue("ifwarrant");
				String schemetype = (String)queryData.getDataValue("schemetype");
				String inputid = (String)queryData.getDataValue("inputid");

				conditionStr = "and schemecode like '%"+schemecode+"%' and ifwarrant like '%"+ifwarrant+"%' and schemetype like '%"+schemetype+"%' and inputid like '%"+inputid+"%'";
				
				}
					
//			conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			conditionStr = " and"+conditionStr.substring(6);
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			IndexedCollection iColl = ppsc.getPrdPlocyBySchemeId(schemeId,conditionStr);
			//IndexedCollection iColl = ppsc.getPlocyRelListBySchemeId(schemeId);
			iColl.setName("PrdPlocyList");
			//Pop框分页
			for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
				KeyedCollection kColl = new KeyedCollection();
				if(iColl.size()>0&&iColl.size()>i){
		    		kColl = (KeyedCollection) iColl.get(i);
		    		iCollPage.addDataElement(kColl);
		    	}				    			    	
		    }	
			iCollPage.setName("PrdPlocyList");
			this.putDataElement2Context(iCollPage, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
