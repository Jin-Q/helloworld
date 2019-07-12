package com.yucheng.cmis.biz01line.mort.cargo.mortcargorepl;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetMortCargoReplLeadRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoRepl";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		//押品编号
		String guaranty_no = "";
		//出质人客户码
		String cus_id = "";
		//存储货物与监管协议关系信息的kc
		KeyedCollection kcRe = null;
		try{
			connection = this.getConnection(context);
			//构建组件类
			MortCommenOwnerComponent mortRe = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			guaranty_no = (String) context.getDataValue("guaranty_no");
			//货物与监管协议关系记录详情
			kcRe = mortRe.queryCarOverReRecordDetail(guaranty_no);
			cus_id = (String) context.getDataValue("cus_id");
			
			//获取库存总金额
			KeyedCollection kc = mortRe.queryIdentyTotalInfo(guaranty_no,"02");
			BigDecimal storage_total = (BigDecimal) kc.getDataValue("identy_total");
			
			//获取该货物质押下的货物清单（在库或者出库待记账）
			int size = 2;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			//此次置换总价值
			kcRe.addDataField("this_repl_total","0");
			//出库待记账价值
			kcRe.addDataField("not_out_total","0");
			//库存总价值
			kcRe.addDataField("storage_total",storage_total);
			//置换后总价值
			kcRe.addDataField("after_repl_total",storage_total);
			kcRe.addDataField("not_to_total","0");//入库待记账
			kcRe.addDataField("cus_id",cus_id);
			kcRe.addDataField("oversee_agr_no",kcRe.getDataValue("agr_no"));
			
			kcRe.remove("agr_no");
			kcRe.setName(modelId);

			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			SystemTransUtils.dealName(kcRe, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			this.putDataElement2Context(kcRe, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
