package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFICallBackDisc;
import com.yucheng.cmis.platform.workflow.domain.WFINodeVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取已办节点及人员列表</p>
 * @author liuhw
 *
 */
public class GetTreatedNodeListOp extends CMISOperation {
	/**  modified wangj 2015/07/28 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/
	private final String modelId = "WfiCallBackDisc";
	/** modified wangj 2015/07/28 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/
	@Override
	public String doExecute(Context context) throws EMPException {

		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		String instanceId = null;
		String nodeId = null;
		String orgId = null;
		String currentUserId = null;
		try {
			connection = this.getConnection(context);
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID); 
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			nodeId = (String) context.getDataValue(WorkFlowConstance.ATTR_NODEID);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			List<WFINodeVO> list = wfi.getWFTreatedNodeList(instanceId, nodeId, currentUserId, orgId, connection);
			context.put(WorkFlowConstance.WF_NEXT_NODE_LIST, list);
			
			/**  added yangzy 2015/09/18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求  begin*/
			String prd_id = "";
			String is_back = "no";
			String wfSign4isBack =(String) context.getDataValue("wfSign");
			TableModelDAO dao4pvp = (TableModelDAO)this.getTableModelDAO(context);
			WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			KeyedCollection wfiJoin = wfiComponent.queryWfiJoin(instanceId);
			if(wfSign4isBack!=null&&"wfi_pvp_loan_app".equals(wfSign4isBack)){
				if(wfiJoin!=null&&wfiJoin.size()>0&&wfiJoin.getDataValue("prd_pk")!=null&&!"".equals(wfiJoin.getDataValue("prd_pk"))){
					prd_id = (String) wfiJoin.getDataValue("prd_pk");
					String prdstr = "100028,100029,100030,100031,100032,100033," +
							        "100034,100035,100036,100037,100038,100039," +
							        "100040,100042,100046,100047,100048,100049," +
							        "100050,100051,100052,100053,100054,100055," +
							        "100056,100057,100058,100059,100060,100061," +
							        "100062,100063,100065,100066,100067,100068," +
							        "100070,100071,100072,100076,100080,100081," +
							        "100082,100083,100084,100085,100086,100087," +
							        "100088,200024,400021";
					
					KeyedCollection kColl = dao4pvp.queryDetail("PvpLoanApp", (String) wfiJoin.getDataValue("pk_value"), connection);
					if(prd_id!=null && prdstr.indexOf(prd_id)>=0 ){
						if(kColl!=null&&kColl.getDataValue("cur_type")!=null&&!"".equals(kColl.getDataValue("cur_type"))&&"CNY".equals(kColl.getDataValue("cur_type"))){
							is_back = "yes";
						}
					}
				}
			}else if(wfSign4isBack!=null&&"wfi_pvp_extension".equals(wfSign4isBack)){
				KeyedCollection kColl = dao4pvp.queryDetail("IqpExtensionPvp", (String) wfiJoin.getDataValue("pk_value"), connection);
				if(kColl!=null&&kColl.getDataValue("fount_cur_type")!=null&&!"".equals(kColl.getDataValue("fount_cur_type"))&&"CNY".equals(kColl.getDataValue("fount_cur_type"))){
					is_back = "yes";
				}
			}
			context.put("is_back", is_back);
			/**  added yangzy 2015/09/18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求  end*/
			
			/**  modified wangj 2015/07/28 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/
			//流程标识：{贷款出账(wfi_pvp_loan_app)流程节点:放款审核岗(2_a6/2_a17)}、{展期出账(wfi_pvp_extension)流程节点：放款审核岗(90_a5)}  
			String wfSign =(String) context.getDataValue("wfSign");
			String isShowCBD="false";
			if(("wfi_pvp_loan_app".equals(wfSign)&&("2_a6".equals(nodeId)||"2_a17".equals(nodeId)))
					||("wfi_pvp_extension".equals(wfSign)&&"90_a5".equals(nodeId))){
				TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
				IndexedCollection iColl =dao.queryList(modelId, "where Wfi_Call_Back_Disc.IS_INUSE='1' order by Wfi_Call_Back_Disc.OR_NO ", connection);
				List<WFICallBackDisc> wfiCBNodes = new ArrayList<WFICallBackDisc>();
				if(iColl!=null&&iColl.size()>0){
					for(int i=0;i<iColl.size();i++){
						KeyedCollection kColl=(KeyedCollection) iColl.get(i);
						WFICallBackDisc wfiCBNode=new WFICallBackDisc();
						wfiCBNode.setPkId(kColl.getDataValue("pk_id")==null?"":kColl.getDataValue("pk_id").toString());
						wfiCBNode.setCbEnname(kColl.getDataValue("cb_enname")==null?"":kColl.getDataValue("cb_enname").toString());
						wfiCBNode.setCbCnname(kColl.getDataValue("cb_cnname")==null?"":kColl.getDataValue("cb_cnname").toString());
						wfiCBNode.setCbMemo(kColl.getDataValue("cb_memo")==null?"":kColl.getDataValue("cb_memo").toString());
						wfiCBNode.setAttr1(kColl.getDataValue("attr1")==null?"":kColl.getDataValue("attr1").toString());
						wfiCBNode.setAttr2(kColl.getDataValue("attr2")==null?"":kColl.getDataValue("attr2").toString());
						wfiCBNode.setAttr3(kColl.getDataValue("attr3")==null?"":kColl.getDataValue("attr3").toString());
						wfiCBNode.setIsInuse(kColl.getDataValue("is_inuse")==null?"":kColl.getDataValue("is_inuse").toString());
						wfiCBNode.setOrNo(kColl.getDataValue("or_no")==null?"":kColl.getDataValue("or_no").toString());
						wfiCBNodes.add(wfiCBNode);
					}
				}
				context.put("WfiCallBackDiscList", wfiCBNodes);
				isShowCBD="true";
			}
			context.put("isShowCBD", isShowCBD);
			/**  modified wangj 2015/07/28 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/
		} catch (Exception e) {
			EMPLog.log("GetTreatedNodeListOp", EMPLog.ERROR, EMPLog.ERROR, "获取已办节点及人员列表出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
