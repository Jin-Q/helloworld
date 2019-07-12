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
//检验组经营者素质与资信的指标
			item=document.getElementsByName('G2012203881.ST012$ST01201');
		if(!judgeRadioChecked(item)){
			alert("指标[从业经验]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203881.ST012$ST01202');
		if(!judgeRadioChecked(item)){
			alert("指标[经营者素质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203881.ST012$ST01203');
		if(!judgeRadioChecked(item)){
			alert("指标[资产实力]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203881.ST012$ST01204');
		if(!judgeRadioChecked(item)){
			alert("指标[团队合作]未选择，请选择后提交。");
			return false;
		}
	//检验组经营发展状况的指标
			item=document.getElementsByName('G2012203882.ST012$ST01206');
		if(!judgeRadioChecked(item)){
			alert("指标[水/电/工资/纳税额增长率]未选择，请选择后提交。");
			return false;
		}
		//检验组生产经营情况 的指标
			item=document.getElementsByName('G2012203883.ST012$ST01207');
		if(!judgeRadioChecked(item)){
			alert("指标[产品及技术]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203883.ST012$ST01208');
		if(!judgeRadioChecked(item)){
			alert("指标[生产管理]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203883.ST012$ST01209');
		if(!judgeRadioChecked(item)){
			alert("指标[购销渠道]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203883.ST012$ST01210');
		if(!judgeRadioChecked(item)){
			alert("指标[市场竞争度]未选择，请选择后提交。");
			return false;
		}
	//检验组企业财务状况的指标
					item=document.getElementsByName('G2012203884.ST012$ST01213');
		if(!judgeRadioChecked(item)){
			alert("指标[短期融资余额与销售收入比]未选择，请选择后提交。");
			return false;
		}
	//检验组账户行为的指标
			item=document.getElementsByName('G2012203885.ST012$ST01214');
		if(!judgeRadioChecked(item)){
			alert("指标[账户性质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203885.ST012$ST01215');
		if(!judgeRadioChecked(item)){
			alert("指标[销售归行率]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203885.ST012$ST01216');
		if(!judgeRadioChecked(item)){
			alert("指标[平均存贷比]未选择，请选择后提交。");
			return false;
		}
	//检验组信用状况的指标
			item=document.getElementsByName('G2012203886.ST012$ST01217');
		if(!judgeRadioChecked(item)){
			alert("指标[小微企业业主（或主要股东）信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203886.ST012$ST01218');
		if(!judgeRadioChecked(item)){
			alert("指标[企业银行信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203886.ST012$ST01219');
		if(!judgeRadioChecked(item)){
			alert("指标[信用环境]未选择，请选择后提交。");
			return false;
		}
		return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2012203881" groupName="经营者素质与资信" seqno="1">
				<ind:IndItemRadio indexNo="ST012$ST01201" indexName="从业经验" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经营者行业经验≥6年"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经营者行业经验≥3年，<6年"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营者行业经验≥1，<3年"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01202" indexName="经营者素质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经营者谈吐诚实，思路清晰"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经营者谈吐本分，思路一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营者谈吐与实际明显不符，思路混乱，对企业未来发展无规划或规划混乱"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01203" indexName="资产实力" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="≥5"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="≥4,<5"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="≥3,<4"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="≥2,<3"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="<2"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01204" indexName="团队合作" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="主要经营团队合作时间超过3年，关系良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经营团队合作少于3年，不存在明显分歧"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营团队合作少于1年"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203882" groupName="经营发展状况" seqno="2">
				<ind:IndItemRadio indexNo="ST012$ST01206" indexName="水/电/工资/纳税额增长率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="比上年同期增加10%以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="比上年同期增加8%以上"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="比上年同期增加6%以上"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="比上年同期增加4%以上"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="比上年同期增加4%以下"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST012$ST01205" indexName="销售收入增长率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203883" groupName="生产经营情况 " seqno="3">
				<ind:IndItemRadio indexNo="ST012$ST01207" indexName="产品及技术" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="企业有独特工艺、专利或成本优势"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="生产无明显特色，但也无明显劣势"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="产品为低水平模仿，质量低下"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01208" indexName="生产管理" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="管理规范，经营稳健，生产流程科学，内部制度健全"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="管理一般，经营一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营方向多变或盲目扩张，生产、财务制度混乱"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01209" indexName="购销渠道" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="有独特、稳定的购销渠道，与上下游客户关系良好，或者有较大控制力"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="无明显购销渠道优势"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="无固定的购销渠道，完全受制于上下游企业"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01210" indexName="市场竞争度" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="市场进入门槛较高，且现有市场竞争不激烈"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="进入门槛一般，目前竞争程度也一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="进入门槛低，现有企业淘汰率高"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="指标值不达标不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203884" groupName="企业财务状况" seqno="4">
    			<ind:IndItemText indexNo="ST012$ST01211" indexName="资产负债率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST012$ST01212" indexName="利息保障倍数" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST012$ST01213" indexName="短期融资余额与销售收入比" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="≤50%"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc=">50%，≤55%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc=">55%，≤60%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc=">60%，≤70%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc=">70%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203885" groupName="账户行为" seqno="5">
				<ind:IndItemRadio indexNo="ST012$ST01214" indexName="账户性质" readonly="true">
							<ind:IndItemRadioOption indValue="0" indDesc="基本户"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般户"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01215" indexName="销售归行率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="≥70%"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="≥60%,<70%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="≥50%,<60%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="≥30%,<50%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="<30%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01216" indexName="平均存贷比" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="≥18%"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="≥12%,<18%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="≥8%,<12%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="≥4%,<8%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="<4%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203886" groupName="信用状况" seqno="6">
				<ind:IndItemRadio indexNo="ST012$ST01217" indexName="小微企业业主（或主要股东）信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="小微企业业主或主要股东个人偿债意愿强，信用记录良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="小微企业业主或主要股东个人偿债意愿一般，非主观恶意造成逾期信用记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="小微企业业主或主要股东个人偿债意愿低，有不良信用记录"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01218" indexName="企业银行信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="企业信用良好，无不良记录，银行贷款均为正常类"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="企业银行贷款中有关注类贷款，且企业无不良记录，本息无逾期记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="企业曾有本息逾期记录、不良信用记录"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST012$ST01219" indexName="信用环境" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况较好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况尚可"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况一般"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况较差"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>


</ind:IndTableLayout>
