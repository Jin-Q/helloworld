package com.yucheng.cmis.biz01line.ccr.interfaces.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.interfaces.CcrApproveIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 分类模型得分实现类
 * @author Administrator
 *
 */
public class CcrBatchApproveImpl extends CMISComponent implements CcrApproveIface {

	public String approvalBatchApp(String serno,Map param)
			throws ComponentException {
		// TODO Auto-generated method stub
		
		CcrComponent ccrComponent = (CcrComponent)
		this.getOtherComponentInstance(CcrPubConstant.CCR_COMPONENT);
		ArrayList ccrAppDetailList=	ccrComponent.getCcrAppDetailList(serno);
		Iterator detailIter = ccrAppDetailList.iterator();
		while(detailIter.hasNext()){
			CcrAppDetail ccrAppDetail= (CcrAppDetail)detailIter.next();
		//	String cusId = ccrAppDetail.getCusId();
			/*String adjScoName = "adjustScore["+cusId+"]";
			String adjustScore = (String)param.get(adjScoName);
			ccrAppDetail.setAdjustScore(adjustScore);*/
		//	String finalGradeName = "finalGrade["+cusId+"]";
		//	String finalGrade = (String)param.get(finalGradeName);
	//		ccrAppDetail.setFinalGrade(finalGrade);
		}
		ccrComponent.approvalBatchApp(serno, ccrAppDetailList);
		return null;
	}

	public String approvalApp(String serno, String cusId, Map param)
			throws ComponentException {
		// TODO Auto-generated method stub
		return null;
	}

	public String CcrRatDirectApproval(String serno) throws ComponentException {
		// TODO Auto-generated method stub
		return null;
	}



}
