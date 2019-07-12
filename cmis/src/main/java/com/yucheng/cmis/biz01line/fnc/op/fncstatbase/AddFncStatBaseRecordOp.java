package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatBaseComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatCfs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatIs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatSl;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatSoe;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

public class AddFncStatBaseRecordOp extends CMISOperation {

    /**
     * 业务逻辑执行的具体实现方法
     */
    public String doExecute(Context context) throws EMPException {
        String flagInfo = CMISMessage.DEFEAT;
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            FncStatBase domain = new FncStatBase();// 数据容器

            // 构件业务处理类
            FncStatBaseComponent thisComponent = (FncStatBaseComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATBASE, context, connection);

            /*
             * 从context取出数据对象
             */
            String isSmp = "";//简表标志
            KeyedCollection kColl = (KeyedCollection) context.getDataElement(PUBConstant.FNCSTATBASE);
            if(kColl.containsKey("isSmp")){
            	isSmp = (String)kColl.getDataValue("isSmp");
            }
            ComponentHelper componetHelper = new ComponentHelper();
            domain = (FncStatBase) componetHelper.kcolTOdomain(domain, kColl);

            // 获取客户财务信息
            String cus_id = domain.getCusId();
            //财报类型改为从前端获取 pizd 20190318
            //String cus_fin_type = thisComponent.findCusRepType(cus_id);
            String cus_fin_type = domain.getFncType();
            if(isSmp!=null && "isSmp".equals(isSmp)){//若含有简表标志则直接赋值财务简表
            	cus_fin_type = "PB0003";
            	domain.setFncType(cus_fin_type);
            }
                        
            //控制同一客户下同报表周期类型、报表期间、报表口径只能录入一条02或07版财报 pizd 2019.03.20
            if(PUBConstant.FNC_PB0001.equals(cus_fin_type) || PUBConstant.FNC_PB0005.equals(cus_fin_type)){
            	List<FncStatBase> listFncStatBase = (List<FncStatBase>) SqlClient.queryList("queryFncStatBase", domain, connection);
            	if(listFncStatBase!=null && listFncStatBase.size()>0){
            		context.addDataField("flag", "exist");
                    context.addDataField("cus_id", listFncStatBase.get(0).getCusId());
                    context.addDataField("stat_prd_style", listFncStatBase.get(0).getStatPrdStyle());
                    context.addDataField("stat_prd", listFncStatBase.get(0).getStatPrd());
                    context.addDataField("state_flg", listFncStatBase.get(0).getStateFlg());
                    context.addDataField("stat_style", listFncStatBase.get(0).getStatStyle());// 报表口径
                    context.put("fnc_type", listFncStatBase.get(0).getFncType());// 报表类型
                    return flagInfo;
            	}
            }
            // 判断是否已经存在该条信息
            FncStatBase tempFncStatBase = thisComponent.findFncStatBase(domain);
            String cus_idTemp = tempFncStatBase.getCusId();
            if (cus_idTemp == null) {
                
            	
            } else {
                context.addDataField("flag", "exist");
                context.addDataField("cus_id", tempFncStatBase.getCusId());
                context.addDataField("stat_prd_style", tempFncStatBase.getStatPrdStyle());
                context.addDataField("stat_prd", tempFncStatBase.getStatPrd());
                context.addDataField("state_flg", tempFncStatBase.getStateFlg());
                context.addDataField("stat_style", tempFncStatBase.getStatStyle());// 报表口径
                context.put("fnc_type", tempFncStatBase.getFncType());// 报表类型
                return flagInfo;
            }
            //
            String statEditUsr = (String) context.getDataValue("currentUserId");
            String regOrgId = (String) context.getDataValue("organNo");
            String regOptId = (String) context.getDataValue("currentUserId");
//            TimeUtil timeUtil = new TimeUtil();
//            String regDt = timeUtil.getCurDate();
            String regDt = (String) context.getDataValue("OPENDAY");
            domain.setLastUpdId(statEditUsr);
            domain.setInputBrId(regOrgId);
            domain.setInputId(regOptId);
            domain.setInputDate(regDt);
            String stateFlg = null;

			if(StringUtils.isBlank(cus_fin_type)){
				//throw new ComponentException("该客户的客户信息中还未选定对应的财务报表类型!");
				context.addDataField("flag", "该客户的客户信息中还未选定对应的财务报表类型!");
				context.addDataField("cus_id", domain.getCusId());
				context.addDataField("stat_prd_style", domain.getStatPrdStyle());
				context.addDataField("stat_prd", domain.getStatPrd());
				context.addDataField("state_flg", domain.getStateFlg());
				context.addDataField("stat_style", domain.getStatStyle());
				context.put("fnc_type", domain.getFncType());// 报表类型
				return "0";
			}
           /* CusCom cusCom = thisComponent.findCusFinById(cus_id);
            String cus_fin_type = cusCom.getComFinRepType();
            */
            //获取对应的财务报表类型
            
            // cus_fin_type = "PB0002";//客户对应的报表类型
            // if(cus_fin_type==null){
            // cus_fin_type = "PB0002";
            // }
            FncConfTemplate fncTemp = thisComponent.findFncConfTemplate(cus_fin_type);
            if (fncTemp != null) {
                domain.setStatBsStyleId(fncTemp.getFncBsStyleId());// 资产样式编号
                domain.setStatPlStyleId(fncTemp.getFncPlStyleId());// 损益表编号
                domain.setStatCfStyleId(fncTemp.getFncCfStyleId());// 现金流量表编号
                domain.setStatFiStyleId(fncTemp.getFncFiStyleId());// 财务指标表编号
                domain.setStatSoeStyleId(fncTemp.getFncRiStyleId());// 所有者权益变动表编号
                domain.setStatSlStyleId(fncTemp.getFncSmpStyleId());// 财务简表编号
                domain.setStyleId1(fncTemp.getFncStyleId1());// 保留1
                domain.setStyleId2(fncTemp.getFncStyleId2());// 保留2
            }
            boolean bs_flg = false;// 资产样式编号
            if (domain.getStatBsStyleId() == null || domain.getStatBsStyleId().length() == 0) {
                stateFlg = "9";
            } else {
                stateFlg = "0";
                bs_flg = true;
            }
            
            boolean pl_flg = false;// 损益表编号
            if (domain.getStatPlStyleId() == null || domain.getStatPlStyleId().length() == 0) {
                stateFlg = stateFlg + "9";
            } else {
                stateFlg = stateFlg + "0";
                pl_flg = true;
            }
            
            boolean cf_flg = false;// 现金流量表编号
            if (domain.getStatCfStyleId() == null || domain.getStatCfStyleId().length() == 0) {
                stateFlg = stateFlg + "9";
            } else {
                stateFlg = stateFlg + "0";
                cf_flg = true;
            }
            
            boolean fi_flg = false;// 财务指标表编号
            if (domain.getStatFiStyleId() == null || domain.getStatFiStyleId().length() == 0) {
                stateFlg = stateFlg + "9";
            } else {
                stateFlg = stateFlg + "0";
                fi_flg = true;
            }
           
            boolean soe_flg = false;// 所有者权益变动表编号
            if (domain.getStatSoeStyleId() == null || domain.getStatSoeStyleId().length() == 0) {
                stateFlg = stateFlg + "9";
            } else {
                stateFlg = stateFlg + "0";
                soe_flg = true;
            }

            boolean sl_flg = false;// 财务简表编号
            if (domain.getStatSlStyleId() == null || domain.getStatSlStyleId().length() == 0) {
                stateFlg = stateFlg + "9";
            } else {
                stateFlg = stateFlg + "0";
                sl_flg = true;
            }

            stateFlg = stateFlg + "990";
            domain.setStateFlg(stateFlg);

            // 新增一条记录信息(基表信息)
            flagInfo = thisComponent.addFncStatBase(domain);
            if (flagInfo.equals(CMISMessage.SUCCESS)) {

                // 处理资产负债表
                this.detailFncStatBs(bs_flg, domain, context, thisComponent);
                // 处理现金流量表
                this.detailFncStatCfs(cf_flg, domain, context, thisComponent);
                // 处理财务指标表
                this.detailFncIndexRpt(fi_flg, domain, context, thisComponent);
                // 处理损益表
                this.detailFncStatIs(pl_flg, domain, context, thisComponent);
                // 处理财务简表
                this.detailFncStatSl(sl_flg, domain, context, thisComponent);
                // 处理所有者权益变动表
                this.detailFncStatSoe(soe_flg, domain, context, thisComponent);
            }

            // 失败抛出错误提示信息
            if (flagInfo.equals(CMISMessage.DEFEAT)) {
                throw new CMISException(CMISMessage.MESSAGEDEFAULT, "新增信息失败，请重新操作!");
            }
            
            context.addDataField("flag", "noexist");
            context.addDataField("cus_id", domain.getCusId());
            context.addDataField("stat_prd_style", domain.getStatPrdStyle());
            context.addDataField("stat_prd", domain.getStatPrd());
            context.addDataField("state_flg", domain.getStateFlg());
            context.addDataField("stat_style", domain.getStatStyle());// 报表口径
            context.put("fnc_type", domain.getFncType());//报表类型
        } catch (CMISException e) {
            e.printStackTrace();
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
            String message = CMISMessageManager.getMessage(e);
            CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
            throw e;
        } catch (Exception e) {
        	EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "", e);
            throw new EMPException(e);
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return flagInfo;
    }

    /**
     * 处理资产负债表信息
     * 
     * @param bs_flg
     * @param domain
     * @param context
     * @param thisComponent
     * @throws EMPException
     */
    public void detailFncStatBs(boolean bs_flg, FncStatBase domain,
            Context context, FncStatBaseComponent thisComponent)
            throws EMPException {
        String flagInfo = CMISMessage.SUCCESS;
        if (bs_flg) {
            FncStatBs fncStatBs = new FncStatBs();
            fncStatBs.setCusId(domain.getCusId());
            String statYear = domain.getStatPrd().substring(0, 4);
            fncStatBs.setStatYear(statYear);
            fncStatBs.setStatStyle(domain.getStatStyle());

            /**
             * 从缓存中根据样式编号获取加载的信息
             */
            // FNCFactory fncFactory = (FNCFactory)
            // context.getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fncConfStyle = FNCFactory.getFNCInstance(domain
                    .getStatBsStyleId());
            List list = (List) fncConfStyle.getItems();
            for (int i = 0; i < list.size(); i++) {
                FncConfDefFormat fncFormat = (FncConfDefFormat) list.get(i);
                String statItemId = fncFormat.getItemId();
                fncStatBs.setStatItemId(statItemId);
                /**
                 * 先查询子表中是否有该数据存在，有不对其任何操作，否则，执行新增操作
                 */
                FncStatBs tempBs = thisComponent.QueryFncStatBs(fncStatBs);
                if (tempBs == null) {
                    // 新增字表的信息（资产负债表）
                    flagInfo = thisComponent.addFncStatBs(fncStatBs, this.getConnection(context));
                }
            }
        }
    }

    /**
     * 处理现金流量表
     * 
     * @param cf_flg
     * @param domain
     * @param context
     * @param thisComponent
     * @throws EMPException
     */
    public void detailFncStatCfs(boolean cf_flg, FncStatBase domain,
            Context context, FncStatBaseComponent thisComponent)
            throws EMPException {
        String flagInfo = CMISMessage.SUCCESS;
        if (cf_flg) {
            FncStatCfs fncStatCfs = new FncStatCfs();
            fncStatCfs.setCusId(domain.getCusId());
            String statYear = domain.getStatPrd().substring(0, 4);
            fncStatCfs.setStatYear(statYear);
            fncStatCfs.setStatStyle(domain.getStatStyle());
            /**
             * 从缓存中根据样式编号获取加载的信息
             */
            // FNCFactory fncFactory = (FNCFactory)
            // context.getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fncConfStyle = FNCFactory.getFNCInstance(domain.getStatCfStyleId());
            List list = (List) fncConfStyle.getItems();

            for (int i = 0; i < list.size(); i++) {
                FncConfDefFormat fncFormat = (FncConfDefFormat) list.get(i);
                String statItemId = fncFormat.getItemId();
                fncStatCfs.setStatItemId(statItemId);
                /**
                 * 先查询子表中是否有该数据存在，有不对其任何操作，否则，执行新增操作
                 */
                FncStatCfs tempCfs = thisComponent.QueryFncStatCfs(fncStatCfs);
                if (tempCfs == null) {
                    // 新增字表的信息（资产负债表）
                    flagInfo = thisComponent.addFncStatCfs(fncStatCfs, this.getConnection(context));
                }
            }
        }
    }

    /**
     * 处理财务指标表
     * 
     * @param fi_flg
     * @param domain
     * @param context
     * @param thisComponent
     * @throws EMPException
     */
    public void detailFncIndexRpt(boolean fi_flg, FncStatBase domain,
            Context context, FncStatBaseComponent thisComponent)
            throws EMPException {
        String flagInfo = CMISMessage.SUCCESS;

        if (fi_flg) {
            FncIndexRpt fncIndexRpt = new FncIndexRpt();
            fncIndexRpt.setCusId(domain.getCusId());
            String statYear = domain.getStatPrd().substring(0, 4);
            fncIndexRpt.setStatYear(statYear);
            fncIndexRpt.setStatStyle(domain.getStatStyle());
            /**
             * 从缓存中根据样式编号获取加载的信息
             */
            // FNCFactory fncFactory = (FNCFactory)
            // context.getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fncConfStyle = FNCFactory.getFNCInstance(domain.getStatFiStyleId());
            List list = (List) fncConfStyle.getItems();

            for (int i = 0; i < list.size(); i++) {
                FncConfDefFormat fncFormat = (FncConfDefFormat) list.get(i);
                String statItemId = fncFormat.getItemId();
                fncIndexRpt.setStatItemId(statItemId);
                /**
                 * 先查询子表中是否有该数据存在，有不对其任何操作，否则，执行新增操作
                 */
                FncIndexRpt tempFi = thisComponent.QueryFncIndexRpt(fncIndexRpt);
                if (tempFi == null) {
                    // 新增字表的信息（资产负债表）
                    flagInfo = thisComponent.addFncIndexRpt(fncIndexRpt, this.getConnection(context));
                }
            }
        }
    }

    /**
     * 处理损益表
     * 
     * @param pl_flg
     * @param domain
     * @param context
     * @param thisComponent
     * @throws EMPException
     */
    public void detailFncStatIs(boolean pl_flg, FncStatBase domain,
            Context context, FncStatBaseComponent thisComponent)
            throws EMPException {
        String flagInfo = CMISMessage.SUCCESS;
        if (pl_flg) {
            FncStatIs fncStatIs = new FncStatIs();
            fncStatIs.setCusId(domain.getCusId());
            String statYear = domain.getStatPrd().substring(0, 4);
            fncStatIs.setStatYear(statYear);
            fncStatIs.setStatStyle(domain.getStatStyle());
            /**
             * 从缓存中根据样式编号获取加载的信息
             */
            // FNCFactory fncFactory = (FNCFactory)
            // context.getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fncConfStyle = FNCFactory.getFNCInstance(domain.getStatPlStyleId());
            List list = (List) fncConfStyle.getItems();

            for (int i = 0; i < list.size(); i++) {
                FncConfDefFormat fncFormat = (FncConfDefFormat) list.get(i);
                String statItemId = fncFormat.getItemId();
                fncStatIs.setStatItemId(statItemId);

                /**
                 * 先查询子表中是否有该数据存在，有不对其任何操作，否则，执行新增操作
                 */
                FncStatIs tempIs = thisComponent.QueryFncStatIs(fncStatIs);
                if (tempIs == null) {
                    // 新增字表的信息（资产负债表）
                    flagInfo = thisComponent.addFncStatIs(fncStatIs, this.getConnection(context));
                }
            }
        }
    }

    /**
     * 处理财务简表
     * 
     * @param sl_flg
     * @param domain
     * @param context
     * @param thisComponent
     * @throws EMPException
     */
    public void detailFncStatSl(boolean sl_flg, FncStatBase domain,
            Context context, FncStatBaseComponent thisComponent)
            throws EMPException {
        String flagInfo = CMISMessage.SUCCESS;
        if (sl_flg) {
            FncStatSl fncStatSl = new FncStatSl();
            fncStatSl.setCusId(domain.getCusId());
            String statYear = domain.getStatPrd().substring(0, 4);
            fncStatSl.setStatYear(statYear);
            fncStatSl.setStatStyle(domain.getStatStyle());
            /**
             * 从缓存中根据样式编号获取加载的信息
             */
            // FNCFactory fncFactory = (FNCFactory)
            // context.getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fncConfStyle = FNCFactory.getFNCInstance(domain.getStatSlStyleId());
            List list = (List) fncConfStyle.getItems();

            for (int i = 0; i < list.size(); i++) {
                FncConfDefFormat fncFormat = (FncConfDefFormat) list.get(i);
                String statItemId = fncFormat.getItemId();
                fncStatSl.setStatItemId(statItemId);
                /**
                 * 先查询子表中是否有该数据存在，有不对其任何操作，否则，执行新增操作
                 */
                FncStatSl tempSl = thisComponent.QueryFncStatSl(fncStatSl);
                if (tempSl == null) {
                    // 新增字表的信息（资产负债表）
                    flagInfo = thisComponent.addFncStatSl(fncStatSl, this.getConnection(context));
                }
            }
        }
    }

    /**
     * 处理所有者权益变动表
     * 
     * @param soe_flg
     * @param domain
     * @param context
     * @param thisComponent
     * @throws EMPException
     */
    public void detailFncStatSoe(boolean soe_flg, FncStatBase domain,
            Context context, FncStatBaseComponent thisComponent)
            throws EMPException {
        String flagInfo = CMISMessage.SUCCESS;
        if (soe_flg) {
            FncStatSoe fncStatSoe = new FncStatSoe();
            fncStatSoe.setCusId(domain.getCusId());
            String statYear = domain.getStatPrd().substring(0, 4);
            fncStatSoe.setStatYear(statYear);
            fncStatSoe.setStatStyle(domain.getStatStyle());
            /**
             * 从缓存中根据样式编号获取加载的信息
             */
            // FNCFactory fncFactory = (FNCFactory)
            // context.getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fncConfStyle = FNCFactory.getFNCInstance(domain.getStatSoeStyleId());
            List list = (List) fncConfStyle.getItems();

            for (int i = 0; i < list.size(); i++) {
                FncConfDefFormat fncFormat = (FncConfDefFormat) list.get(i);
                String statItemId = fncFormat.getItemId();
                fncStatSoe.setStatItemId(statItemId);
                /**
                 * 先查询子表中是否有该数据存在，有不对其任何操作，否则，执行新增操作
                 */
                FncStatSoe tempSoe = thisComponent.QueryFncStatSoe(fncStatSoe);
                if (tempSoe == null) {
                    // 新增字表的信息（资产负债表）
                    flagInfo = thisComponent.addFncStatSoe(fncStatSoe, this.getConnection(context));
                }
            }
        }
    }
}
