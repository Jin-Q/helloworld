package com.yucheng.cmis.biz01line.iqp.op.iqpactrecpomana;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetIqpActrecBondInPoPopPage extends CMISOperation {
	private final String modelId = "IqpActrecpoMana";
	public String doExecute(Context context) throws EMPException {

			Connection connection = null;
			try {
				connection = this.getConnection(context);

				KeyedCollection queryData = null;
				try {
					queryData = (KeyedCollection) context
							.getDataElement(this.modelId);
				} catch (Exception e) {
				}
//				String cus_id = context.getDataValue("cus_id").toString();
				String poType = (String) context.getDataValue("PO_TYPE");
				//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式  begin
				String conditionStr = TableModelUtil.getQueryCondition(
						this.modelId, queryData, context, false, false, false);
				if (conditionStr == null || "".equals(conditionStr)) {
					conditionStr = "where status='2' and po_type ='" + poType + "' ";
				} else {
					conditionStr = conditionStr + "and status='2' and po_type ='" + poType + "' ";
				}
				String cus_id = "";
				if(poType!=null&&"2".equals(poType)){
					if(context.containsKey("cus_id")&&context.getDataValue("cus_id")!=null&&!"".equals(context.getDataValue("cus_id"))){
						cus_id = (String) context.getDataValue("cus_id");
						conditionStr = conditionStr + " and cus_id = '"+cus_id+"' ";
					}
				}
				conditionStr += " order by po_no desc ";
				//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式  end
				int size = 10;
				PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",String.valueOf(size));
				TableModelDAO dao = this.getTableModelDAO(context);
				List<String> list = new ArrayList<String>();
				IndexedCollection iColl = dao.queryList(modelId, list,conditionStr, pageInfo, connection);
				iColl.setName(iColl.getName() + "List");
				
				String[] args = new String[] { "cus_id" };
				String[] modelIds = new String[] { "CusBase" };
				String[] modelForeign = new String[] { "cus_id" };
				String[] fieldName = new String[] { "cus_name" };
				SInfoUtils.addUSerName(iColl, new String[] { "input_id" });
				SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id" });
				// 详细信息翻译时调用
				SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD,context, modelIds, modelForeign, fieldName);

				this.putDataElement2Context(iColl, context);
				TableModelUtil.parsePageInfo(context, pageInfo);
				
			} catch (EMPException ee) {
				throw ee;
			} catch (Exception e) {
				throw new EMPException(e);
			} finally {
				if (connection != null)
					this.releaseConnection(context, connection);
			}
			return "0";
		}


}
