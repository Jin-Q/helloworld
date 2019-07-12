package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBizFlowComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class ImportIqpBatchPageOp extends CMISOperation {

	private final String batModel = "IqpBatchMng"; //批次表模型
	private final String loanModel = "IqpLoanApp"; //业务申请表模型
	private final String discAppModel = "IqpDiscApp";//贴现申请从表模型
	
	@Override
	public String doExecute(Context context) throws EMPException {
		/**
		 * 票据批次引入场景：
		 * 1、在业务申请的直贴业务(银票、商票)中导入批次。
		 * 
		 * 票据批次引入操作处理逻辑：
		 * 1.根据录入的批次号进行判断是否存在该批次.
		 * 2.判断该批次状态是否可引用
		 * 3.通过流水号判断该笔业务是否已经存在批次信息。
		 * 4.校验批次的票据种类与业务中的票据种类一致。
		 * 
		 * 注意，以最新注释为准
		 * 目前修改为，票据明细维护批次信息，并不去更新业务信息（优点，操作集中在针对批次，不用再区分不同场景）
		 * 即：1、批次中操作，需要去维护4个表（批次表、关系表、票据表、收益表）
		 *     2、业务引用票据明细后，在主页面实时查询批次中的票据信息（包含票据总金额、利息、实付金额、票据数量等），在最后保存与提交流程时保存到业务表中
		 * add by zhaozq 2013-09-07
		 */
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String batch_no = "";
			String serno = "";
			if(context.containsKey("batch_no")){
				batch_no = (String)context.getDataValue("batch_no");
			}
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryDetail(batModel, batch_no, connection);
			if(kc != null && ((String)kc.getDataValue("batch_no"))!= null){
				/** 根据批次号状态判断该批次是否可引用 01:登记 02：审批中 03：已办结*/
				String batchStatus = (String)kc.getDataValue("status");
				if(!"01".equals(batchStatus)){
					context.addDataField("flag", "failed");
					context.addDataField("msg", "该批次状态不为【登记】状态，不能引入！");
					return null;
				}
				String batchSerno = (String)kc.getDataValue("serno");//从批次包中查询业务编号，如果不为空，说明该包已经被引用。
				String batchBilltype = (String)kc.getDataValue("bill_type");//批次的票据种类
				if("".equals(batchSerno) || batchSerno == null){
					//校验贴现申请从表中的票据种类是否和导入批次的票据种类一致。
				    KeyedCollection discKcoll = dao.queryAllDetail(discAppModel, serno, connection);
				    String discBilltype = (String)discKcoll.getDataValue("bill_type");
				    if("".equals(discBilltype) || discBilltype== null){//贴现信息中的票据种类信息不能为空。
				    	context.addDataField("flag", "failed");
						context.addDataField("msg", "获取不到【票据种类】,请先保存基本信息！");
						return null;
				    }else{
				    	if(!batchBilltype.equals(discBilltype)){
				    		context.addDataField("flag", "failed");
							context.addDataField("msg", "批次的票据种类和贴现信息中的票据种类不一致，不能引入该批次！");
							return null;
				    	}
				    }

					//通过业务流水号查询是否已经引入了其他批次，如果已引入批次，那么去除关联关系。
					KeyedCollection iqpbathKcoll = dao.queryFirst(batModel, null, " where serno='"+serno+"'", connection);
					if(!"".equals((String)iqpbathKcoll.getDataValue("batch_no")) && iqpbathKcoll.getDataValue("batch_no") != null){
						iqpbathKcoll.setDataValue("serno", "");
						iqpbathKcoll.setDataValue("status", "01");//改回【登记】状态
						dao.update(iqpbathKcoll, connection);
					}
					
					/** 更新批次表中业务关联关系 */
					KeyedCollection upKColl = new KeyedCollection(batModel);
					upKColl.addDataField("batch_no", batch_no);
					upKColl.addDataField("serno", serno);
					upKColl.addDataField("status", "02");//改为【已引用】状态
					int count = dao.update(upKColl, connection);
					if(count != 1){
						context.addDataField("flag", "failed");
						context.addDataField("msg", "【"+batch_no+"】批次号关联现有业务失败");
					}else {
						context.addDataField("flag", "success");
						context.addDataField("msg", "关联成功");
					}
					
					/**
					 * 当前只有在业务申请直贴产品中可引入批次，引入后需要更新贴现从表和业务申请主表信息。
					 * add by MQ 2013-08-26
					 * update by WS 2014-01-08
					 */
					IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
					                                           .getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
					iqpLoanAppComponent.updateIqpInfoByBillDetail("IqpLoanApp", serno, kc, dao);
					iqpLoanAppComponent.updateIqpLmtRel("IqpLoanApp",serno, kc, dao);
				}else {
					context.addDataField("flag", "failed");
					context.addDataField("msg", "【"+batch_no+"】批次号存在其他业务中，不能引用");
				}
			}else {
				context.addDataField("flag", "failed");
				context.addDataField("msg", "系统中不存在【"+batch_no+"】批次号");
			}
		} catch (Exception ee) {
			if(context.containsKey("flag")){
				context.setDataValue("flag", "failed");
				context.setDataValue("msg", ee.getCause().getMessage());
			}else{
				context.addDataField("flag", "failed");
				context.addDataField("msg", ee.getCause().getMessage());
			}
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
