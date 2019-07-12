package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdPlocyListPopOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		IndexedCollection iCollPage = new IndexedCollection();
		try{
			connection = this.getConnection(context);
			String schemeId = null;
			if(context.containsKey("schemeId")){
				schemeId = (String)context.getDataValue("schemeId");
			}else {
				throw new EMPException("获取关联政策方案ID失败！");
			}
			/**
			 * 获取政策关联的政策资料有效列表，排除已选列表
			 */
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			String conditionStr = "";
			IndexedCollection iColl = ppsc.getPrdPlocyBySchemeId(schemeId,conditionStr);
			//IndexedCollection iColl = ppsc.getPlocyRelListBySchemeId(schemeId);
				
			iColl.setName("PrdPlocyList");
			
			pageInfo.setRecordSize(String.valueOf(iColl.size()));
			//分页截取iColl   
			for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
				KeyedCollection kColl = new KeyedCollection();
				if(iColl.size()>0&&iColl.size()>i){
		    		kColl = (KeyedCollection) iColl.get(i);
		    		if(kColl!=null){
						iCollPage.addDataElement(kColl);
					}
		    	}	
				
		    }  
			iCollPage.setName("PrdPlocyList");
			this.putDataElement2Context(iCollPage, context);
			if(context.containsKey("schemeId")){
				context.setDataValue("schemeId", schemeId);
			} else{
				context.addDataField("schemeId", schemeId);
			}
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
