package com.yucheng.cmis.biz01line.authorize.op.pvpauthorize;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.pvp.component.PvpBizFlowComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 *撤销出账授权
 */
public class CancelPvpAuthorizeOp extends CMISOperation {
	private final String modelId = "PvpAuthorize";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			String tran_serno = (String) context.getDataValue("tran_serno");
			KeyedCollection auKColl = dao.queryDetail(modelId, tran_serno, connection);
			String prd_id = (String) auKColl.getDataValue("prd_id");
			String tran_id = (String) auKColl.getDataValue("tran_id");
			String status = (String) auKColl.getDataValue("status");
			
			//只有已授权的授权才能调交易
			if("02".equals(status)){
				/**第一步，调用撤销交易*/
				KeyedCollection reqKColl = new KeyedCollection();
				reqKColl.addDataField("GEN_GL_NO", auKColl.getDataValue("authorize_no"));
				if("200024".equals(prd_id)){//银票
					reqKColl.addDataField("TRAN_TYPE", "ACCP");
				}else if("300020".equals(prd_id)||"300021".equals(prd_id)||"300022".equals(prd_id)||"300023".equals(prd_id)||"300024".equals(prd_id)){//贴现，包含直贴，转贴、再贴
					reqKColl.addDataField("TRAN_TYPE", "DISC");
				}else if("400021".equals(prd_id)){//境内保函
					reqKColl.addDataField("TRAN_TYPE", "GUTR");
				}else if("600020".equals(prd_id)){//资产转受让，目前确认传贷款类型
					reqKColl.addDataField("TRAN_TYPE", "LOAN");
				}else if("600021".equals(prd_id)){//资产流转，目前确认传贷款类型
					reqKColl.addDataField("TRAN_TYPE", "LOAN");
				}else if("600022".equals(prd_id)){//资产证券化，目前确认传贷款类型
					reqKColl.addDataField("TRAN_TYPE", "LOAN");
				}else{
					reqKColl.addDataField("TRAN_TYPE", "LOAN");//贷款（包含贸易融资）
				}
				reqKColl.addDataField("OLD_GEN_GL_NO", auKColl.getDataValue("authorize_no"));
				
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				KeyedCollection repKColl = null;
				try{
					repKColl = service.tradeSQCXYM(reqKColl, context, connection);
				}catch(Exception e){
					context.addDataField("flag", "failed");
					context.addDataField("retMsg", "ESB通讯接口【撤销授权】交易失败："+e.getMessage());
					return null;
				}
				/*if(!TagUtil.haveSuccess(repKColl, context)){
					//交易失败信息
					String retMsg = (String) repKColl.getDataValue("RET_MSG");
					context.addDataField("flag", "failed");
					context.addDataField("retMsg","ESB通讯接口【撤销授权】交易失败："+retMsg);
					return null;
				}*/
			}
			
			
			/**第二步，交易成功后，进行如下处理：（1）将授权信息状态更新为“出账撤销”（2）将对应的台账信息删除 */
			PvpBizFlowComponent pvpBizFlowComponent = (PvpBizFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("PvpBizFlowComponent", context, connection);
			if("0200200001102".equals(tran_id)){//展期
				pvpBizFlowComponent.doCancelAuthorizeForExtend(tran_serno);
			}
			if("200024".equals(prd_id)){
				pvpBizFlowComponent.doCancelAuthorizeForAccp(tran_serno);
			}else if("600020".equals(prd_id)){
				pvpBizFlowComponent.doCancelAuthorizeForAsset(tran_serno);
			}else if("600021".equals(prd_id)){
				pvpBizFlowComponent.doCancelAuthorizeForAssetTrans(tran_serno);
			}else if("600022".equals(prd_id)){
				pvpBizFlowComponent.doCancelAuthorizeForAssetPro(tran_serno);
			}else if("300020".equals(prd_id)||"300021".equals(prd_id)){
				pvpBizFlowComponent.doCancelAuthorizeForDisc(tran_serno);
			}else if("300022".equals(prd_id)||"300023".equals(prd_id)||"300024".equals(prd_id)){
				pvpBizFlowComponent.doCancelAuthorizeForRpddscnt(tran_serno);
			}else{
				pvpBizFlowComponent.doCancelAuthorizeForLoan(tran_serno);
			}
			
			context.addDataField("flag", "success");
			context.addDataField("retMsg","撤销成功");
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
