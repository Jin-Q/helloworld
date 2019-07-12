package com.yucheng.cmis.biz01line.fnc.op.fncstatcommon;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems4Query;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.Fnc4QueryComponent;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TimeUtil;
/**
 * 取报表的前期数据---获得期初数据
 * @author liuxin
 *
 */
public class GetInitDataOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		FncStatBase pfncStatBase = null;            //财报对象
		FncConfStyles pfncConfStyles = null;		//带有数据的样式对象
		Connection connection = null;
		try{
		    connection = this.getConnection(context);
			//构件业务处理类
			FncStatCommonComponent fCommonComponent = (FncStatCommonComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATCOMMON,context, connection);
			
			
			/**
			 * 从context中取出cusId(客户编号),statPrdStyle(报表周期类型),statPrd(报表期间 格式：YYYYMM)
			 */
			String cusId = (String)context.getDataValue("cus_id");
			if(cusId == null || cusId.length() == 0){
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的客户编号为空");
				return PUBConstant.FAIL;
			}
			String statPrdStyle = (String)context.getDataValue("stat_prd_style");
			if(statPrdStyle == null || statPrdStyle.length() == 0){
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表周期类型为空");
				return PUBConstant.FAIL;
			}
			String statPrd = (String)context.getDataValue("stat_prd");
			if(statPrd == null || statPrd.length() == 0){
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表期间类型为空");
				return PUBConstant.FAIL;
			}
			
			String fnc_conf_typ = (String)context.getDataValue("fnc_conf_typ");
			if(fnc_conf_typ == null || fnc_conf_typ.length() == 0){
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表种类为空");
				return PUBConstant.FAIL;
			}
			
			String stat_style = (String)context.getDataValue("stat_style");
			if(stat_style == null || stat_style.length() == 0){
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表口径为空");
				return PUBConstant.FAIL;
			}
			/**
			 * 获取到财报的对象,将财报对象放入到context中
			 */
			FncStatBase tempFB = new FncStatBase();
			tempFB.setCusId(cusId);
			tempFB.setStatPrdStyle(statPrdStyle);
			tempFB.setStatPrd(statPrd);
			tempFB.setStatStyle(stat_style);
			
			pfncStatBase = fCommonComponent.findOneFncStatBase(tempFB);
			if(pfncStatBase == null){
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的财报对象为空");
				return PUBConstant.FAIL;
			}
			context.addDataField("editFlag", "noedit");
			context.setDataValue("state_flg", pfncStatBase.getStateFlg());
			context.addDataField(CMISConstance.CMIS_FNCSTATBASE, pfncStatBase);
			
			List errList = null;
			context.addDataField("errList", errList);//校验是用
			
			/**
			 * 获取到标签样式对象(带数据的)的对象,将该标签样式对象对象放入到context中
			 */
			
			String  styleId = null;
			if("01".equals(fnc_conf_typ)){
				styleId = pfncStatBase.getStatBsStyleId();
			}else if("02".equals(fnc_conf_typ)){
				styleId = pfncStatBase.getStatPlStyleId();
			}else if("03".equals(fnc_conf_typ)){
				styleId = pfncStatBase.getStatCfStyleId();
			}else if("04".equals(fnc_conf_typ)){
				styleId = pfncStatBase.getStatFiStyleId();
			}else if("05".equals(fnc_conf_typ)){
				styleId = pfncStatBase.getStatSoeStyleId();
			}else if("06".equals(fnc_conf_typ)){
				styleId = pfncStatBase.getStatSlStyleId();
			}
			
			
			/**
			 * 从系统缓存中读取取得报表样式信息对象，得到其中的所有项目列表
			 */
			//FNCFactory fncFactory = (FNCFactory) context.getService(CMISConstance.ATTR_RPTSERVICE);
			FncConfStyles fcs = (FncConfStyles) FNCFactory.getFNCInstance(styleId);
			String tableName = fcs.getFncName();
			String fncConfTyp = fcs.getFncConfTyp();
			
			///////////新的做法：  开始//////////////////////////////////////////////////////////////////////////////
			try {
				pfncConfStyles = (FncConfStyles)fcs.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			List itemList = fCommonComponent.QueryItemsList(cusId, statPrdStyle, statPrd, styleId,tableName,fncConfTyp,stat_style);
			
			/**
			 * 取报表的前期数据---获得期初数据
			 */
//			if("01".equals(fncConfTyp)||"02".equals(fncConfTyp)){
//				List<FncConfItems4Query> fncList = null;
//				List itemIds = new ArrayList();
//				for(int i=0;i<itemList.size();i++){
//					FncConfDefFormat ff = (FncConfDefFormat)itemList.get(i);
//					itemIds.add(ff.getItemId());
//				}
//				Fnc4QueryComponent fnc4querycomponent =fCommonComponent.getFnc4QueryComponent();
//				boolean isFlag = fnc4querycomponent.isExistStatBase(cusId, "4", TimeUtil.getPeryyMMdd(statPrd+"01","4").substring(0, 4)+"12",stat_style);
//				if(isFlag){
//					fncList = fnc4querycomponent.getConfItems4BOP(cusId, itemIds, TimeUtil.getPeryyMMdd(statPrd+"01","4").substring(0, 4)+"1230");
//					if(fncList.size()==itemList.size()){
//						for(int j=0;j<itemList.size();j++){
//							
//							FncConfDefFormat fmt = (FncConfDefFormat)itemList.get(j);
//							for(int m=0;m<fncList.size();m++){
//								FncConfItems4Query fnc = (FncConfItems4Query)fncList.get(m);
//								if(fnc.getItemId().equals(fmt.getItemId())){
//									fmt.setData1(fnc.getData1());
//									break;
//								}
//							}
//							
//						}
//					}
//				
//				}
//			}
			pfncConfStyles.setItems(itemList);
			////////////结束/////////////////////////////////////////////////////////////////////////////////
			
			//原来的做法：
			//pfncConfStyles = fCommonComponent.findOneFncConfStyles(cusId, statPrdStyle, statPrd, styleId,tableName,fncConfTyp);
			
			if(pfncConfStyles == null){
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的标签样式对象(带数据的)的对象为空");
				return PUBConstant.FAIL;
			}
			context.setDataValue("style_id", pfncConfStyles.getStyleId());
			context.setDataValue("fnc_conf_data_col", pfncConfStyles.getFncConfDataCol()+"");
			context.setDataValue("fnc_name", pfncConfStyles.getFncName());
			context.setDataValue("fnc_conf_typ", pfncConfStyles.getFncConfTyp());

			context.removeDataElement(CMISConstance.CMIS_RPTSTYLE);
			context.addDataField(CMISConstance.CMIS_RPTSTYLE, pfncConfStyles);

			
			
		}catch(CMISException e){
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
			String message = CMISMessageManager.getMessage(e);
			CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
			throw e;
		}catch(Exception e){
	                throw new EMPException(e);
	        } finally {
	                if (connection != null)
	                        this.releaseConnection(context, connection);
	        }
		return PUBConstant.SUCCESS;
	}


}
