package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverlst;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddCusHandoverLstRecordOp extends CMISOperation{

    // operation TableModel
    private final String modelId = "CusHandoverLst";

    /**
     * bussiness logic operation
     */
    public String doExecute(Context context) throws EMPException{
        Connection connection = null;
        String flag = "fail";
        try{
            connection = this.getConnection(context);

            KeyedCollection kCollToDb = new KeyedCollection(modelId);
            KeyedCollection kColl = null;
            IndexedCollection iColl = null;
            String handover_br_id = (String) context.getDataValue("handover_br_id");
            String handover_id = (String) context.getDataValue("handover_id");
            String handover_id_req = handover_id;
            String receiver_br_id = (String) context.getDataValue("receiver_br_id");
            String receiver_id = (String) context.getDataValue("receiver_id");
            String serno = (String) context.getDataValue("serno");
//            String handover_mode = (String) context.getDataValue("handover_mode");	//移交方式 	1.仅客户资料 2.客户与业务
            String handoverScope = (String) context.getDataValue("handover_scope"); // 移交范围	1.单个或多个 	2.所有
            
            kColl = new KeyedCollection();
            kColl.addDataField("handover_br_id", handover_br_id);
            kColl.addDataField("handover_id", handover_id);
            kColl.addDataField("receiver_br_id", receiver_br_id);
            kColl.addDataField("receiver_id", receiver_id);
            
            SInfoUtils.addSOrgName(kColl, new String[]{"handover_br_id", "receiver_br_id"});
            SInfoUtils.addUSerName(kColl, new String[]{"handover_id", "receiver_id"});
            
            handover_br_id = (String)kColl.getDataValue("handover_br_id_displayname");
            handover_id = (String)kColl.getDataValue("handover_id_displayname");
            receiver_br_id = (String)kColl.getDataValue("receiver_br_id_displayname");
            receiver_id = (String)kColl.getDataValue("receiver_id_displayname");
            
            // 申请流水号
            kCollToDb.addDataField("serno", serno);
            // 业务编码 暂时默认的为10-客户资料
            kCollToDb.addDataField("handover_type", "10");
            String detailPart =
                    "]的主管客户经理由[" + handover_br_id + "]的[" + handover_id + "]移交给["
                            + receiver_br_id + "]的[" + receiver_id + "]";

            TableModelDAO dao = this.getTableModelDAO(context);
            
            //先保存一下申请里的内容
            String cusHandoverApp = "CusHandoverApp";
            if(context.containsKey(cusHandoverApp)){
                kColl = (KeyedCollection)context.getDataElement(cusHandoverApp);
                String condition = "where serno='" + serno + "' ";
                IndexedCollection results = dao.queryList(cusHandoverApp, condition, connection);
                if(results!=null && results.size() == 0){
                    dao.insert(kColl, connection);
                }else{
                	dao.update(kColl, connection);
                }
            }
            
            //单个或多个客户
            if(handoverScope.equals("1")){
				try{
				iColl =(IndexedCollection) context.getDataElement("CusBaseList");
				}catch (Exception e){}
				  
				if (iColl == null || iColl.size() == 0){
				    iColl = new IndexedCollection();
				}
            }else if (handoverScope.equals("2")){ //按客户经理所有客户
            	CusBaseComponent cusBaseComponent =(CusBaseComponent) CMISComponentFactory.getComponentFactoryInstance()
            	.getComponentInstance(PUBConstant.CUSBASE,context,connection);
            	List<CMISDomain> cusBaseList = new ArrayList<CMISDomain>();
            	cusBaseList=cusBaseComponent.findCusListByCustMgr(handover_id_req);
            	ComponentHelper cHelper = new ComponentHelper();
	            iColl = cHelper.domain2icol(cusBaseList, "CusBase",CMISConstance.CMIS_LIST_IND);
            }
            for (int i = 0; i < iColl.size(); i++){
                kColl = (KeyedCollection) iColl.get(i);
                kCollToDb.put("cus_id", (String) kColl.getDataValue("cus_id"));
                // 业务移交说明
                if(kCollToDb.containsKey("business_detail")){
                	kCollToDb.removeDataElement("business_detail");
                    kCollToDb.addDataField("business_detail", "客户["
                            + (String) kColl.getDataValue("cus_name")
                            + detailPart);
                }else{
                	 kCollToDb.addDataField("business_detail", "客户["
                             + (String) kColl.getDataValue("cus_name")
                             + detailPart);
                }

                // 查询本次申请中是否已经添加过了该客户的明细
                IndexedCollection iColTmp = dao.queryList(modelId, " where serno='"+serno+"' and handover_type='10' and cus_id='"+(String) kColl.getDataValue("cus_id")+"'", connection);
               
                if (iColTmp == null || iColTmp.size() == 0){
                	//handoverpk
//                	kCollToDb.put("pk_id", CMISSequenceService4JXXD.querySequenceFromDB("handoverpk", "all", connection, context));
                    dao.insert(kCollToDb, connection);
                }
            }
            flag = "suc";
        } catch (EMPException ee) {
            throw ee;
        } catch (Exception e) {
            throw new EMPException(e);
        } finally {
            context.addDataField("flag", flag);
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return "0";

    }
}
