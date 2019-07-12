package com.yucheng.cmis.biz01line.ccr.interfaces.impl;

import java.util.Map;

import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrRatDirect;
import com.yucheng.cmis.biz01line.ccr.interfaces.CcrApproveIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 分类模型得分实现类
 * @author Administrator
 *
 */
public class CcrApproveImpl extends CMISComponent implements CcrApproveIface {

	public String approvalApp(String serno,String cusId,Map param)
			throws ComponentException {
		// TODO Auto-generated method stub
		CcrComponent ccrComponent = (CcrComponent)
		this.getOtherComponentInstance(CcrPubConstant.CCR_COMPONENT);
		/*
		 * flag=1 对私  ,否则2对公
		 */
		String cusType = (String)param.get("cusType[@]");
		String flag = cusType.substring(0,1);
		String finalGrade = (String)param.get("finalGrade[@]");
		String adjustScore = (String)param.get("adjustScore[@]");
		if("1".equals(flag)){
			if(adjustScore==null||adjustScore.equals("")){
				throw new ComponentException("调整得分为空,此字段不能为空");
			}
		}else if("2".equals(flag)){
			if(finalGrade==null||finalGrade.equals("")){
				throw new ComponentException("调整等级为空,此字段不能为空");
			}
		}else{
			throw new ComponentException("客户类型为空,此字段不能为空");
		}
		
		if(cusId==null||cusId.equals("")){
			throw new ComponentException("客户码为空,此字段不能为空");
		}
//		ccrComponent.approvalApp(serno,cusId,finalGrade,adjustScore);
		return null;
	}


	
	/**
	 * 评级直接认定流程审批结束业务处理
	 * @param serno	业务流程号
	 * @param cusId	客户号
	 * @param param	流程传入参数
	 * @return
	 * @throws ComponentException
	 */
	public String CcrRatDirectApproval(String serno,String cusId,Map param) throws ComponentException{
		CcrComponent ccrComponent = (CcrComponent)
		this.getOtherComponentInstance(CcrPubConstant.CCR_COMPONENT);
		//取评级直接认定申请信息
		CcrRatDirect crd = ccrComponent.getCcrRatDirectAppInfoBySerno(serno);
		//更新客户信息中评级信息
//		ccrComponent.updateCusState(crd.getInputDate(), crd.getEndDate(), crd.getGrade(), crd.getCusId());
		
		return null;
	}
	
	/**xukaixi
	 * 评级直接认定流程审批结束业务处理
	 * @param serno	业务流程号
	 * @param cusId	客户号
	 * @param param	流程传入参数
	 * @return
	 * @throws ComponentException
	 */
	public String CcrRatDirectApproval(String serno) throws ComponentException{
		CcrComponent ccrComponent = (CcrComponent)
		this.getOtherComponentInstance(CcrPubConstant.CCR_COMPONENT);
		//取评级直接认定申请信息
		CcrRatDirect crd = ccrComponent.getCcrRatDirectAppInfoBySerno(serno);
		//更新客户信息中评级信息
//		ccrComponent.updateCusState(crd.getInputDate(), crd.getEndDate(), crd.getGrade(), crd.getCusId());
		
		return null;
	}

	public String approvalApp(String serno) throws ComponentException {
		// TODO Auto-generated method stub
		return null;
	}



	public String approvalBatchApp(String serno, Map param)
			throws ComponentException {
		// TODO Auto-generated method stub
		return null;
	}



}
