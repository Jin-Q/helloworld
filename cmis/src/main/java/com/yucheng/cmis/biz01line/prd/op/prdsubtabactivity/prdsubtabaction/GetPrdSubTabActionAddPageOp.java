package com.yucheng.cmis.biz01line.prd.op.prdsubtabactivity.prdsubtabaction;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetPrdSubTabActionAddPageOp extends CMISOperation {
	private static final String MAINACT = "MAIN_ACTION_TYPE";
	private static final String SUBACT = "SUB_ACTION_TYPE";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			/** 生成主键 */
			String pk = CMISSequenceService4JXXD.querySequenceFromDB("PK", "fromDate", connection, context);
			String mainid = (String)context.getDataValue("mainid");
			String subid = (String)context.getDataValue("subid");
			
			/** 通过主资源ID\从资源ID，封装生成前台需要的下拉框选择数据 */
			IndexedCollection mainIColl = new IndexedCollection();
			IndexedCollection subIColl = new IndexedCollection();
			
			PrdPolcySchemeComponent cmisComponent = (PrdPolcySchemeComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			mainIColl = cmisComponent.getActionByResourceId(mainid);
			mainIColl.setName(MAINACT);
			subIColl = cmisComponent.getActionByResourceId(subid);
			subIColl.setName(SUBACT);
			this.putDataElement2Context(this.addDicFiled(mainIColl), context);
			this.putDataElement2Context(this.addDicFiled(subIColl), context);
			context.addDataField("pkid", pk);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}
	/**
	 * 将IColl装换为需要的字典项值
	 * @param iColl
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection addDicFiled(IndexedCollection iColl) throws Exception {
		IndexedCollection dicIColl = new IndexedCollection(iColl.getName());
		
		for(int i=0;i<iColl.size();i++){
			KeyedCollection kc = (KeyedCollection)iColl.get(i);
			KeyedCollection resultKColl = new KeyedCollection();
			resultKColl.addDataField("enname", kc.getDataValue("actid"));
			resultKColl.addDataField("cnname", kc.getDataValue("descr"));
			dicIColl.add(resultKColl);
		}
		return dicIColl;
	}

}
