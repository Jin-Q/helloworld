package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-8-6
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class CheckBizModifyRightOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String serno = "";//出账流水号
			String biz_mode= "";//loan 普通贷款业务 loanExt 贷款展期业务
			String cur_type="";//币种
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			if(context.containsKey("biz_mode")){
				biz_mode = (String)context.getDataValue("biz_mode");
			}
			if(context.containsKey("cur_type")){
				cur_type = (String)context.getDataValue("cur_type");
			}
			if(serno == null || serno.trim().length() == 0){
				throw new EMPException("获取出账流水号异常！");
			}
			if(biz_mode == null || biz_mode.trim().length() == 0){
				throw new EMPException("获取业务模式异常！");
			}
			if(cur_type == null || cur_type.trim().length() == 0){
				throw new EMPException("获取币种异常！");
			}
			//查询出账业务信息
			TableModelDAO dao  = this.getTableModelDAO(context);
			KeyedCollection temp  = new KeyedCollection();
			if("loan".equals(biz_mode)){
				temp = dao.queryDetail("PvpLoanApp", serno, connection);
								
			}else if("loanExt".equals(biz_mode)){
				temp  = dao.queryDetail("IqpExtensionPvp", serno, connection);
			}
			IndexedCollection PBMR = dao.queryList("PvpBizModifyRel", "where biz_serno='"+serno+"' and approve_status in('000','111','992','993')", connection);
			String approve_modify_right = (String) temp.getDataValue("approve_modify_right");//业务修改标志
			if(approve_modify_right ==null || "".equals(approve_modify_right)){
				approve_modify_right = "0";
			}
			if("1".equals(approve_modify_right)){
				if("CNY".equals(cur_type)){
					if(PBMR==null || PBMR.size()<=0){
						context.addDataField("flag", "allow");
					}else{
						String approve_status = (String) (((KeyedCollection) PBMR.get(0)).getDataValue("approve_status"));
						if("000".equals(approve_status)){
							context.addDataField("flag", "exist");
						}else{
							context.addDataField("flag", "forbidden");
						}
					}
				}else{
					context.addDataField("flag", "curTypeErr");
				}
			}else{
				context.addDataField("flag", "limited");
			}
				
			
		} catch (Exception e) {
			context.addDataField("flag", "failed");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return "0";
	}

}
