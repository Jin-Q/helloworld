package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop.lmtcoopmachine;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateLmtCoopMachineRecordOp extends CMISOperation {

	private final String modelId = "LmtCoopMachine";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0){
				context.addDataField("msg", "未获取到合作方机械设备信息！");
				context.addDataField("flag","N");
				throw new Exception("未获取到合作方机械设备信息！");
			}
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				context.addDataField("msg", "合作方机械设备信息更新失败！");
				context.addDataField("flag","N");
				throw new Exception("合作方机械设备信息更新失败！");
			}
			
			//根据合作方流水号删除原有拟按揭设备信息
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			lmtComponent.deleteLmtSchedEquip(kColl.getDataValue("serno").toString());
			
			//得到拟按揭设备信息
			IndexedCollection iColl = (IndexedCollection)context.getDataElement("LmtSchedEquipList");
			if(null != iColl && iColl.size()>0){
				for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
					KeyedCollection kColl_equip = (KeyedCollection) iterator.next();
					kColl_equip.setName("LmtSchedEquip");
					dao.insert(kColl_equip, connection);
				}
			}
			
			context.addDataField("msg", "更新成功！");
			context.addDataField("flag","Y");
			
		} catch(Exception e){
			context.addDataField("msg", e.getMessage());
			context.addDataField("flag","N");
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
}
