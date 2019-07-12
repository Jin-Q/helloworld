package com.yucheng.cmis.biz01line.iqp.op.iqpassetstrsf;

import java.math.BigDecimal;
import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class CheckAssetInfoOp extends CMISOperation {

	private final String modelId = "IqpAsset";
	private final String relModelId = "IqpAssetRel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String asset_no = (String)context.getDataValue("asset_no");//资产包号
			String serno = (String)context.getDataValue("serno");//资产包号
			Double asset_total_amt = 0.00;
			Double asset_total_amt1 = 0.00;
			
			KeyedCollection kColl = dao.queryDetail(modelId, asset_no, connection);
			
			//校验资产包中是否录入资产清单信息
			String relCondition = " where asset_no = '"+asset_no+"'";
			IndexedCollection relIColl = dao.queryList(relModelId, relCondition, connection);
			
			//没有资产明细
			if(relIColl.size()==0){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "资产包中未添加资产清单，无法放入流程！");
				return null;
			}
			KeyedCollection askColl = dao.queryDetail("IqpAssetstrsf", serno, connection);
			if(kColl!=null){
				if(kColl.getDataValue("asset_total_amt")!=null&&!"".equals(kColl.getDataValue("asset_total_amt"))){
					String asset_total_amt_t =  kColl.getDataValue("asset_total_amt").toString();
					asset_total_amt = Double.valueOf(asset_total_amt_t);
				}
			}
			if(askColl!=null){
				if(askColl.getDataValue("asset_total_amt")!=null&&!"".equals(askColl.getDataValue("asset_total_amt"))){
					String asset_total_amt_t =  askColl.getDataValue("asset_total_amt").toString();
					asset_total_amt1 = Double.valueOf(asset_total_amt_t);
				}
			}
			if(BigDecimal.valueOf(asset_total_amt).compareTo(BigDecimal.valueOf(asset_total_amt1))!=0){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "申请资产总额与清单资产总额不一致，无法放入流程！");
				return null;
			}
			String takeover_type = (String) kColl.getDataValue("takeover_type");//转让方式
			/**modified by lisj 2014-12-4  修改资产转让转出校验状态获取的方法 begin**/
			String conditionStr ="";
			String acc_status="";
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			if(takeover_type!=null&&!takeover_type.equals("")){
				if(takeover_type.equals("01")||takeover_type.equals("02")){//转出（必须校验本行借据台账状态）
					for(int i=0;i<relIColl.size();i++){
						KeyedCollection relKColl = (KeyedCollection) relIColl.get(i);
						String bill_no = (String) relKColl.getDataValue("bill_no");						
						conditionStr ="select status from acc_view where " +
								"(table_model = 'AccLoan' and prd_id not in ('700020','700021') or table_model ='AccPad') and bill_no='"+bill_no+"'";
						IndexedCollection  accIColl = TableModelUtil.buildPageData(null, dataSource, conditionStr);
					    if(accIColl !=null && accIColl.size()>0){
					    	KeyedCollection temp = (KeyedCollection) accIColl.get(0);
					    	acc_status  = (String) temp.getDataValue("status");
					    }
						if(!acc_status.equals("1")){
							context.addDataField("flag", "failure");
							context.addDataField("msg", "借据号【"+bill_no+"】台账状态已不是【正常】状态，无法放入流程，请检查资产包！");
							return null;
						}
					}
				}
			}
			/**modified by lisj 2014-12-4  修改资产转让转出校验状态获取的方法  end**/
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "校验失败！");
			throw ee;
		}  catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
