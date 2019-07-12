package com.yucheng.cmis.biz01line.fnc.op.fncconfstyles;


import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfStylesComponent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCloneFncConfStyleRecordOp extends CMISOperation {
	
	private final String modelId = "FncConfDefFmt";
	/**
	 * ҵ���߼�ִ�еľ���ʵ�ַ���
	 */
	public String doExecute(Context context) throws EMPException {
		String flagInfo = CMISMessage.DEFEAT;
		Connection connection = null;
		try{
		    connection = this.getConnection(context);
			FncConfStyles domain = new FncConfStyles();
          
			FncConfStylesComponent thisComponent = (FncConfStylesComponent)CMISComponentFactory
			                                                                       
					.getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCCONFSTYLES,context, connection);

			KeyedCollection kColl = (KeyedCollection)context.getDataElement(PUBConstant.FNCCONFSTYLES);
			String styleID_YL = (String)kColl.getDataValue("styleId");//获得原来的样式编号
			ComponentHelper componetHelper = new ComponentHelper();
			domain = (FncConfStyles)componetHelper.kcolTOdomain(domain, kColl);
			flagInfo = thisComponent.addFncConfStyles(domain);
			
			/**
			 * 处理fnc_conf_def_fmt的数据
			 */
			List dataList = thisComponent.getFormats(styleID_YL);
			if(flagInfo.equals(CMISMessage.SUCCESS)){
				for(int i=0;i<dataList.size();i++){
					FncConfDefFormat domainTemp = (FncConfDefFormat)dataList.get(i);
					domainTemp.setStyleId(domain.getStyleId());
					flagInfo = thisComponent.addFncConfDefFormat(domainTemp);
				}
			}

			if(flagInfo.equals(CMISMessage.DEFEAT)){
				throw new CMISException(CMISMessage.MESSAGEDEFAULT,"");
			}
			
		}catch(CMISException e){
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
			String message = CMISMessageManager.getMessage(e);
			CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
			throw e;
		}  catch(Exception e){
	                throw new EMPException(e);
	        } finally {
	                if (connection != null)
	                        this.releaseConnection(context, connection);
	        }
		return flagInfo;
	}
	
}
