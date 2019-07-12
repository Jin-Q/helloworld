package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


//import java.sql.Connection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfStylesComponent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatBaseComponent;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.biz01line.fnc.master.domain.RptItemData;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteFncStatBaseRecordOp extends CMISOperation {

    private final String pk1 = "cus_id";
    private final String pk2 = "stat_prd_style";
    private final String pk3 = "stat_prd";
    private final String pk4 = "stat_style";
    private final String pk5 = "fnc_type";

    /**
     * 业务逻辑执行的具体实现方法
     */
    public String doExecute(Context context) throws EMPException {
        String flagInfo = CMISMessage.DEFEAT;
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            // 客户代码
            String cusId = (String) context.getDataValue(pk1); // pk1
            // 报表周期类型
            String statPrdStyle = (String) context.getDataValue(pk2); // pk2
            // 报表期间
            String statPrd = (String) context.getDataValue(pk3); // pk3

            String pk4_value = (String) context.getDataValue(pk4); // pk4
            //财报类型
            String fnc_type = (String) context.getDataValue(pk5);

            // 构件业务处理类
            FncStatBaseComponent fsbComponent = (FncStatBaseComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATBASE, context, connection);

            FncStatBase pfncStatBase = new FncStatBase();
            pfncStatBase.setCusId(cusId);
            pfncStatBase.setStatPrdStyle(statPrdStyle);
            pfncStatBase.setStatPrd(statPrd);
            pfncStatBase.setStatStyle(pk4_value);
            pfncStatBase.setFncType(fnc_type);
            /*
             * 对应条件的记录，并转换数据结构
             */
            // FncStatBase fsb = fsbComponent.findFncStatBase(cusId,
            // statPrdStyle,statPrd);
            FncStatBase fsb = fsbComponent.findFncStatBase(pfncStatBase);
            // 取得操作的表的ID
            String statBsStyleId = fsb.getStatBsStyleId();
            String statPlStyleId = fsb.getStatPlStyleId();
            String statCfStyleId = fsb.getStatCfStyleId();
            String statFiStyleId = fsb.getStatFiStyleId();
            String statSlStyleId = fsb.getStatSlStyleId();

            // 构件业务处理类
            FncConfStylesComponent fcsComponent = (FncConfStylesComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCCONFSTYLES, context, connection);

            FncConfStyles statBs = null;
            if(statBsStyleId!=null && !"".equals(statBsStyleId)){
            	statBs = fcsComponent.findFncConfStyles(statBsStyleId);
            }
            
            FncConfStyles statPl = null;
            if(statPlStyleId!=null && !"".equals(statPlStyleId)){
            	statPl = fcsComponent.findFncConfStyles(statPlStyleId);
            }
            
            FncConfStyles statCf = null;
            if(statCfStyleId!=null && !"".equals(statCfStyleId)){
            	statCf = fcsComponent.findFncConfStyles(statCfStyleId);
            }
            
            FncConfStyles statFi = null;
            if(statFiStyleId!=null && !"".equals(statFiStyleId)){
            	statFi = fcsComponent.findFncConfStyles(statFiStyleId);
            }
            
            FncConfStyles statSl = null;
            if(statSlStyleId!=null && !statSlStyleId.equals("")){
            	statSl = fcsComponent.findFncConfStyles(statSlStyleId);
            }

            List<FncConfStyles> list = new ArrayList<FncConfStyles>();
            if(statBs!=null && !"".equals(statBs)){
            	list.add(statBs);
            }
            if(statPl!=null && !"".equals(statPl)){
            	list.add(statPl);
            }
            if(statCf!=null && !"".equals(statCf)){
            	list.add(statCf);
            }
            if(statFi!=null && !"".equals(statFi)){
            	list.add(statFi);            	
            }
            if(statSl!=null && !statSl.equals("")){
            	list.add(statSl);
            }
            // 构件业务处理类
            FncStatCommonComponent fsCommonComponent = (FncStatCommonComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATCOMMON, context, connection);

            String statYear = statPrd.substring(0, 4);
            String statMonth = statPrd.substring(4);
            String itemId = null;

            for (int i = 0; i < list.size(); i++) {
                FncConfStyles fncConfStyles = list.get(i);
                String tableName = fncConfStyles.getFncName();
                // 数据列数
                int fncConfDataColumn = fncConfStyles.getFncConfDataCol();

                RptItemData riData = new RptItemData(cusId, tableName,
                        statPrdStyle, statYear, statMonth, itemId, pk4_value, fncConfDataColumn);

                // 删除记录
                TableModelDAO dao = this.getTableModelDAO(context);
                Map<String, String> pkMap = new HashMap<String, String>();
                pkMap.put(pk1, cusId);
                pkMap.put(pk2, statPrdStyle);
                pkMap.put(pk3, statPrd);
                pkMap.put(pk4, pk4_value);
                pkMap.put(pk5, fnc_type);
                int count = dao.deleteByPks(PUBConstant.FNCSTATBASE, pkMap, connection);
                if (count > 0) {
                    flagInfo = CMISMessage.SUCCESS;
                }

                if (flagInfo.equals(CMISMessage.SUCCESS)) {
                    flagInfo = fsCommonComponent.removeFncStat(riData);
                }
            }

            // 失败抛出错误提示信息
            if (flagInfo.equals(CMISMessage.DEFEAT)) {
                throw new CMISException(CMISMessage.MESSAGEDEFAULT, "删除信息失败，请重新操作!");
            }
            context.addDataField("flag", "success");

        } catch (CMISException e) {
            e.printStackTrace();
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
            String message = CMISMessageManager.getMessage(e);
            CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
            throw e;
        } catch (Exception e) {
            throw new EMPException(e);
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return flagInfo;
    }
}
