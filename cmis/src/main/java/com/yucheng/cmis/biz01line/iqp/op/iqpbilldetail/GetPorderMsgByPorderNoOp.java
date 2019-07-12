package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetPorderMsgByPorderNoOp extends CMISOperation {
	private static final String modelId = "IqpBatchBillRel";
	private static final String batModel = "IqpBatchMng";
	private static final String accModel = "AccDrft";
	/** 处理逻辑分析：判断票据是否存在
	 *  1.不存在则返回，可以直接添加
	 *  2.票据存在
	 *  	2.1票据存在关联表中则不可以添加，提示所在批次编号中
	 *  	2.2票据不存在关联表中，则返回票据信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String porderno = "";
			String bill_type = "";
			String batch_no = "";
			if(context.containsKey("porderno")){
				porderno = (String)context.getDataValue("porderno");
				porderno = porderno.trim();
			}
			if(context.containsKey("bill_type")){
				bill_type = (String)context.getDataValue("bill_type");
			}
			if(context.containsKey("batch_no")){
				batch_no = (String)context.getDataValue("batch_no");
			}
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			/**获取批次业务类型*/
			KeyedCollection batKColl = dao.queryDetail(batModel, batch_no, connection);
			String biz_type = (String) batKColl.getDataValue("biz_type");
			//'01':'买入买断', '02':'买入返售', '03':'卖出卖断', '04':'卖出回购', '05':'内部转贴现', '06':'再贴现', '07':'直贴'
			/**如果为再贴现，转贴现则需要校验该票据是否做过内部转贴现*/
			if("03".equals(biz_type) || "04".equals(biz_type) || "06".equals(biz_type)){
				String condition = "";
				String msg = "";
				if("03".equals(biz_type)){
					condition = "where porder_no='"+porderno+"' and dscnt_type in('05','01')";//判断是否做过.05内部转贴现 '01':'买入买断'
					msg = "该票据未做过内部转贴现或买入买断,不允许做卖出卖断!";
				}else{
					condition = "where porder_no='"+porderno+"' and dscnt_type in('05','01','02')";//判断是否做过.05内部转贴现 '01':'买入买断', '02':'买入返售',
					msg = "该票据未做过内部转贴现或买入买断或买入返售,不允许做外部转贴现或再贴现!";
				}
				 
				IndexedCollection iCollAcc = dao.queryList(accModel, condition, connection);
				if(iCollAcc.size()<=0){
					context.addDataField("flag", "error");
					context.addDataField("msg", msg);
					context.put("bill_type", bill_type);
					context.addDataField("porder_no", "");
					context.addDataField("porder_curr", "");
					context.addDataField("drft_amt", "");
					context.addDataField("porder_addr", "");
					context.addDataField("is_ebill", "");
					context.addDataField("bill_isse_date", "");
					context.addDataField("porder_end_date", "");
					context.addDataField("utakeover_sign", "");
					context.addDataField("tcont_no", "");
					context.addDataField("tcont_amt", "");
					context.addDataField("tcont_content", "");
					context.addDataField("drwr_org_code", "");
					context.addDataField("isse_name", "");
					context.addDataField("daorg_no", "");
					context.addDataField("daorg_name", "");
					context.addDataField("daorg_acct", "");
					context.addDataField("pyee_name", "");
					context.addDataField("paorg_no", "");
					context.addDataField("paorg_name", "");
					context.addDataField("paorg_acct_no", "");
					context.addDataField("aaorg_type", "");
					context.addDataField("aaorg_no", "");
					context.addDataField("aaorg_name", "");
					context.addDataField("accptr_cmon_code", "");
					context.addDataField("aaorg_acct_no", "");
					context.addDataField("aorg_type", "");
					context.addDataField("aorg_no", "");
					context.addDataField("aorg_name", "");
					context.addDataField("status", "");
					return null;
				}
			}
			
			/** 通过汇票号码取得系统中汇票信息 */
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			KeyedCollection kcResult = cmisComponent.getPorderMsgByPorderNo(porderno);
			if(kcResult.getDataValue("code").equals("9999")){
				context.addDataField("flag", "9999");
				context.addDataField("msg", kcResult.getDataValue("msg"));
				/** 填充系统默认参数 */
				if(context.containsKey("bill_type")){
					context.setDataValue("bill_type", bill_type);
				}else {
					context.addDataField("bill_type", bill_type);
				}
				context.addDataField("porder_no", "");
				context.addDataField("porder_curr", "");
				context.addDataField("drft_amt", "");
				context.addDataField("porder_addr", "");
				context.addDataField("is_ebill", "");
				context.addDataField("bill_isse_date", "");
				context.addDataField("porder_end_date", "");
				context.addDataField("utakeover_sign", "");
				context.addDataField("tcont_no", "");
				context.addDataField("tcont_amt", "");
				context.addDataField("tcont_content", "");
				context.addDataField("drwr_org_code", "");
				context.addDataField("isse_name", "");
				context.addDataField("daorg_no", "");
				context.addDataField("daorg_name", "");
				context.addDataField("daorg_acct", "");
				context.addDataField("pyee_name", "");
				context.addDataField("paorg_no", "");
				context.addDataField("paorg_name", "");
				context.addDataField("paorg_acct_no", "");
				context.addDataField("aaorg_type", "");
				context.addDataField("aaorg_no", "");
				context.addDataField("aaorg_name", "");
				context.addDataField("accptr_cmon_code", "");
				context.addDataField("aaorg_acct_no", "");
				context.addDataField("aorg_type", "");
				context.addDataField("aorg_no", "");
				context.addDataField("aorg_name", "");
				context.addDataField("status", "");
			}else {
				List<String> list = new ArrayList<String>();
				list.add("batch_no");
				/** 效验汇票信息是否可用，即该票据是否被引用 */
				//内部转贴现,只能做一次
				String condtition = "";
				if("05".equals(biz_type)){
					condtition = " where porder_no = '"+porderno+"' and batch_no in (select batch_no from Iqp_Batch_Mng where status in('01','02','03') and biz_type='05')";
				}else{
					condtition = " where porder_no = '"+porderno+"' and batch_no in (select batch_no from Iqp_Batch_Mng where status not in('03','04'))";
				}
				
				KeyedCollection relKColl = dao.queryFirst(modelId, list, condtition, connection);
				if(relKColl.getDataValue("batch_no") != null && ((String)relKColl.getDataValue("batch_no")).length() > 0){
					/** 存在引用 */
					context.addDataField("flag", "failed");
					if("05".equals(biz_type)){
						context.addDataField("msg", "该笔票据已做过内部转贴现！");
					}else{
						context.addDataField("msg", "该笔票据已经在【"+relKColl.getDataValue("batch_no")+"】批次中被引用，业务未完结，此处不能引用！");	
					}
				}else {
					/** 不存在引用,判断票据类型是否一致 */
					KeyedCollection billKColl = dao.queryDetail("IqpBillDetail", porderno, connection);
					/**
					 * 检查票据状态，  1【登记】、【核销】状态才能做直贴、买入返售、买入买断
					 *               2【持有】才能做再贴现、内部转贴现、卖出回购、卖出卖断
					 */
					if(biz_type.equals("07")||biz_type.equals("02")||biz_type.equals("01")){
						String bill_status = (String)billKColl.getDataValue("status"); 
						if(!"01".equals(bill_status) && !"04".equals(bill_status)){
							context.addDataField("flag", "failed");
							context.addDataField("msg", "该笔票据状态不为【登记】或【核销】,不能使用！");
						}else{
							String billType = (String)billKColl.getDataValue("bill_type");
							if(billType.equals(bill_type)){
								context.addDataField("flag", "0000");
								context.addDataField("msg", "该笔票据可以使用！");
							}else {
								context.addDataField("flag", "failed");
								context.addDataField("msg", "该笔票据种类不符合批次不能使用！");
							}
						}
					}else if(biz_type.equals("03")||biz_type.equals("04")||biz_type.equals("05")||biz_type.equals("06")){
						String bill_status = (String)billKColl.getDataValue("status"); 
						if(!"02".equals(bill_status)){
							context.addDataField("flag", "failed");
							context.addDataField("msg", "该笔票据状态不为【持有】,不能使用！");
						}else{
							String billType = (String)billKColl.getDataValue("bill_type");
							if(billType.equals(bill_type)){
								context.addDataField("flag", "0000");
								context.addDataField("msg", "该笔票据可以使用！");
							}else {
								context.addDataField("flag", "failed");
								context.addDataField("msg", "该笔票据种类不符合批次不能使用！");
							}
						}
					}
					
				}
			}
			insertMsg2KColl(kcResult, context);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

	/**
	 * 将需要转换的KColl放入上下文中
	 * @param kColl
	 * @param context
	 * @throws Exception
	 */
	public void insertMsg2KColl(KeyedCollection kColl,Context context) throws Exception{
		for(int i=0;i<kColl.size();i++){
			DataElement element = (DataElement) kColl.getDataElement(i);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				if(context.containsKey(aField.getName())){
				}else {
					String value = "";
					if(aField.getValue() == null){
						value = "";
					}else {
						value = aField.getValue().toString();
					}
					context.addDataField(aField.getName(), value);
				}
				
			}
		}
	}
}
