<%@page language="java" contentType="text/html; charset=UTF-8"%> 
<%@taglib uri="/WEB-INF/c-rt.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@taglib uri="/WEB-INF/ind.tld" prefix="ind" %>
<link rel="stylesheet" type="text/css" href="<emp:file fileName='styles/ccrTable.css'/>"/>

<script type="text/javascript">
//验证单选是否选中.
function judgeRadioChecked(obj)
{
   if (obj){
    if (obj.length!=undefined)
    {
     for(var i=0;i < obj.length;i++)
     {
     if (obj[i].checked) return true;
     }
    }
    else{
      if (obj.checked) return true;
    }
   }
   return false;
}
function checkRequired(){
//检查每个组中的指标是否有值.如果没有则警告并返回.
var item;
//检验组信用历史的指标
			item=document.getElementsByName('G2013203932.ST022$ST02201');
		if(!judgeRadioChecked(item)){
			alert("指标[担保机构信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203932.ST022$ST02202');
		if(!judgeRadioChecked(item)){
			alert("指标[个人信用（法人、实际负责人、主要股东）]未选择，请选择后提交。");
			return false;
		}
	//检验组基本情况的指标
			item=document.getElementsByName('G2013203933.ST022$ST02203');
		if(!judgeRadioChecked(item)){
			alert("指标[资质情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203933.ST022$ST02204');
		if(!judgeRadioChecked(item)){
			alert("指标[股东结构]未选择，请选择后提交。");
			return false;
		}
					item=document.getElementsByName('G2013203933.ST022$ST02206');
		if(!judgeRadioChecked(item)){
			alert("指标[对外投资]未选择，请选择后提交。");
			return false;
		}
	//检验组偿债能力的指标
			item=document.getElementsByName('G2013203934.ST022$ST02207');
		if(!judgeRadioChecked(item)){
			alert("指标[担保责任余额]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203934.ST022$ST02208');
		if(!judgeRadioChecked(item)){
			alert("指标[对单个被担保人提供的最高融资性担保责任余额]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203934.ST022$ST02209');
		if(!judgeRadioChecked(item)){
			alert("指标[对单个被担保人及其关联方提供的最高融资性担保责任余额]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203934.ST022$ST02210');
		if(!judgeRadioChecked(item)){
			alert("指标[对单个被担保人债券发行提供的担保责任余额]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203934.ST022$ST02211');
		if(!judgeRadioChecked(item)){
			alert("指标[不良贷款率]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203934.ST022$ST02212');
		if(!judgeRadioChecked(item)){
			alert("指标[担保代偿率]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203934.ST022$ST02213');
		if(!judgeRadioChecked(item)){
			alert("指标[平均代偿回收率]未选择，请选择后提交。");
			return false;
		}
	//检验组盈利能力的指标
		//检验组管理指标的指标
			item=document.getElementsByName('G2013203936.ST022$ST02216');
		if(!judgeRadioChecked(item)){
			alert("指标[制度建设]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203936.ST022$ST02217');
		if(!judgeRadioChecked(item)){
			alert("指标[人员结构]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203936.ST022$ST02218');
		if(!judgeRadioChecked(item)){
			alert("指标[系统建设]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203936.ST022$ST02219');
		if(!judgeRadioChecked(item)){
			alert("指标[未到期责任准备金]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203936.ST022$ST02220');
		if(!judgeRadioChecked(item)){
			alert("指标[担保赔偿准备金]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203936.ST022$ST02221');
		if(!judgeRadioChecked(item)){
			alert("指标[与其他银行合作情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203936.ST022$ST02226');
		if(!judgeRadioChecked(item)){
			alert("指标[与我行的合作情况]未选择，请选择后提交。");
			return false;
		}
	//检验组调整项的指标
			item=document.getElementsByName('G2013203937.ST022$ST02222');
		if(!judgeRadioChecked(item)){
			alert("指标[是否获得国家财政补贴]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203937.ST022$ST02223');
		if(!judgeRadioChecked(item)){
			alert("指标[财务报表是否经过会计事务所审计]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203937.ST022$ST02224');
		if(!judgeRadioChecked(item)){
			alert("指标[是否被相关监管部门、金融机构发出整改通知书或检查通报]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203937.ST022$ST02225');
		if(!judgeRadioChecked(item)){
			alert("指标[是否按我行管理要求提供相关资料]未选择，请选择后提交。");
			return false;
		}
		return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2013203932" groupName="信用历史" seqno="1">
				<ind:IndItemRadio indexNo="ST022$ST02201" indexName="担保机构信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良记录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="报告期内存在逾期信用记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02202" indexName="个人信用（法人、实际负责人、主要股东）" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良违约纪录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有违约记录6次（含）以下，但现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有违约记录7-11次（含），现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="报告期内有违约记录超过12次（含）以上的或有违约记录且有违约余额在3个月以内"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203933" groupName="基本情况" seqno="2">
				<ind:IndItemRadio indexNo="ST022$ST02203" indexName="资质情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02204" indexName="股东结构" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="国有控股担保机构"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="国有参股担保机构"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="民营商业性担保机构"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST022$ST02205" indexName="注册资本（亿）" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST022$ST02206" indexName="对外投资" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="低于20%(含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="20%以上"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203934" groupName="偿债能力" seqno="3">
				<ind:IndItemRadio indexNo="ST022$ST02207" indexName="担保责任余额" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="2-5倍（含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="5-7倍（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="7-9倍（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="2倍（含）以内或9-10倍（倍）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02208" indexName="对单个被担保人提供的最高融资性担保责任余额" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="3%-7%（含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="1%-3%（含）或7%-10%（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%以上"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02209" indexName="对单个被担保人及其关联方提供的最高融资性担保责任余额" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="5%-10%（含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="3%-5%（含）或10%-13%（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="3%（含）以内或13%-15%（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="15%以上"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02210" indexName="对单个被担保人债券发行提供的担保责任余额" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="10%-20%（含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="10%（含）以下"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="20%-30%（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="30%以上"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02211" indexName="不良贷款率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良贷款"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="低于5%（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="5%-15%（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="高于15%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02212" indexName="担保代偿率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无代偿额"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="低于1%（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="1%-3%（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="3%-5%（含）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="高于5%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02213" indexName="平均代偿回收率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无代偿额或全部收回"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="高于50%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="30%-50%（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="低于30%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203935" groupName="盈利能力" seqno="4">
    			<ind:IndItemText indexNo="ST022$ST02214" indexName="利润率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST022$ST02215" indexName="资产收益率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203936" groupName="管理指标" seqno="5">
				<ind:IndItemRadio indexNo="ST022$ST02216" indexName="制度建设" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="三项均有"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="缺项"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02217" indexName="人员结构" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="均有"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="缺项"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02218" indexName="系统建设" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="均有"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="缺项"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02219" indexName="未到期责任准备金" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="高于50%（含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="低于50%或未提取"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02220" indexName="担保赔偿准备金" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="累计提取达10%(含）以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="当年提取1%（含）以上"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="当年提取低于1%或未提取"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02221" indexName="与其他银行合作情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="3年以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="1-3年（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="1年以下"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="未合作"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02226" indexName="与我行的合作情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="3年以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="1-3年（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="1年以下"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="未合作"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203937" groupName="调整项" seqno="6">
				<ind:IndItemRadio indexNo="ST022$ST02222" indexName="是否获得国家财政补贴" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02223" indexName="财务报表是否经过会计事务所审计" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经会计事务所审计持无保留意见"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经会计事务所审计却持保留意见"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="未经审计"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02224" indexName="是否被相关监管部门、金融机构发出整改通知书或检查通报" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="是，目前已整改"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="是，且目前尚未整改"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST022$ST02225" indexName="是否按我行管理要求提供相关资料" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>


</ind:IndTableLayout>
