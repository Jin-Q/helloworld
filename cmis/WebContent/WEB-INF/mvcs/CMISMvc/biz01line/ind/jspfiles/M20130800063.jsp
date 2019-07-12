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
//检验组信用状况的指标
			item=document.getElementsByName('G2013203946.ST024$ST02401');
		if(!judgeRadioChecked(item)){
			alert("指标[银行贷款资产状态]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203946.ST024$ST02402');
		if(!judgeRadioChecked(item)){
			alert("指标[商业信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203946.ST024$ST02403');
		if(!judgeRadioChecked(item)){
			alert("指标[个人信用]未选择，请选择后提交。");
			return false;
		}
	//检验组规模实力的指标
		//检验组偿债能力的指标
				item=document.getElementsByName('G2013203948.ST024$ST02410');
		if(!judgeRadioChecked(item)){
			alert("指标[流动比率]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203948.ST024$ST02412');
		if(!judgeRadioChecked(item)){
			alert("指标[利息保障倍数]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203948.ST024$ST02413');
		if(!judgeRadioChecked(item)){
			alert("指标[或有负债比率]未选择，请选择后提交。");
			return false;
		}
	//检验组经营效率的指标
		//检验组经营能力的指标
				item=document.getElementsByName('G2013203950.ST024$ST02428');
		if(!judgeRadioChecked(item)){
			alert("指标[收入计划完成率]未选择，请选择后提交。");
			return false;
		}
		//检验组经营管理的指标
			item=document.getElementsByName('G2013203951.ST024$ST02406');
		if(!judgeRadioChecked(item)){
			alert("指标[经营管理水平]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203951.ST024$ST02407');
		if(!judgeRadioChecked(item)){
			alert("指标[融资能力]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203951.ST024$ST02408');
		if(!judgeRadioChecked(item)){
			alert("指标[政策风险]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203951.ST024$ST02411');
		if(!judgeRadioChecked(item)){
			alert("指标[财政支持力度]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203951.ST024$ST02414');
		if(!judgeRadioChecked(item)){
			alert("指标[区域地位]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203951.ST024$ST02415');
		if(!judgeRadioChecked(item)){
			alert("指标[经费来源稳定性]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203951.ST024$ST02416');
		if(!judgeRadioChecked(item)){
			alert("指标[收入稳定性]未选择，请选择后提交。");
			return false;
		}
	//检验组综合评价的指标
			item=document.getElementsByName('G2013203952.ST024$ST02417');
		if(!judgeRadioChecked(item)){
			alert("指标[领导者素质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203952.ST024$ST02418');
		if(!judgeRadioChecked(item)){
			alert("指标[合作情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203952.ST024$ST02419');
		if(!judgeRadioChecked(item)){
			alert("指标[资金回笼率]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203952.ST024$ST02420');
		if(!judgeRadioChecked(item)){
			alert("指标[生产经营期限]未选择，请选择后提交。");
			return false;
		}
	//检验组修正/调整项的指标
			item=document.getElementsByName('G2013203954.ST024$ST02421');
		if(!judgeRadioChecked(item)){
			alert("指标[学校奖励情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203954.ST024$ST02423');
		if(!judgeRadioChecked(item)){
			alert("指标[财务报表是否经过会计事务所审计]未选择，请选择后提交。");
			return false;
		}
			return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2013203946" groupName="信用状况" seqno="1">
				<ind:IndItemRadio indexNo="ST024$ST02401" indexName="银行贷款资产状态" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无关注、次级、可疑、损失贷款"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有关注贷款"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有次级、可疑、损失之一"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02402" indexName="商业信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良记录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="报告期内存在逾期1个月（含）以内的信用记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="存在逾期1个月以上的信用记录；或败诉商业纠纷的；或有拖欠员工工资记录"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="保全资产类借新还旧贷款"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02403" indexName="个人信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良违约纪录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有违约记录6次（含）以下，但现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有违约记录7-11次（含），现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="报告期内有违约记录超过12次（含）以上的或有违约记录且有违约余额在3个月以内"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="报告期尚有违约余额且逾期3个月以上"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203947" groupName="规模实力" seqno="2">
    			<ind:IndItemText indexNo="ST024$ST02404" indexName="总资产(万元)" readonly="true" />
	

    			<ind:IndItemText indexNo="ST024$ST02405" indexName="总营业收入(万元)" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203948" groupName="偿债能力" seqno="3">
    			<ind:IndItemText indexNo="ST024$ST02409" indexName="资产负债率" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST024$ST02410" indexName="流动比率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="高于3(含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="2（含）-3"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="1.5（含）-2"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="1.2（含）-1.5"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="0.8（含）-1.2"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="低于0.8"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02412" indexName="利息保障倍数" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="高于20(含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="15（含）-20"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10（含）-15"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="5（含）-10"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="3（含）-5"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="低于3"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02413" indexName="或有负债比率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="没有或有负债"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="低于10%（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%-20%（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="20%-30%（含）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="30%-40%（含）"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="40%-50%（含）"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="高于50%"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203949" groupName="经营效率" seqno="4">
    			<ind:IndItemText indexNo="ST024$ST02424" indexName="收支结余率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST024$ST02425" indexName="总收入增长率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203950" groupName="经营能力" seqno="5">
    			<ind:IndItemText indexNo="ST024$ST02426" indexName="经费自给率" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST024$ST02428" indexName="收入计划完成率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="100%（含）以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="96%（含）-100%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="92%（含）-96%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="88%（含）-92%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="84%（含）-88%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="80%（含）-84%"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="小于80%"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST024$ST02429" indexName="经营收入占比" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203951" groupName="经营管理" seqno="6">
				<ind:IndItemRadio indexNo="ST024$ST02406" indexName="经营管理水平" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="各项均表现良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有2项以上表现良好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="表现一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02407" indexName="融资能力" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="融资灵活性强，有两家或两家以上的金融机构融资业务合作"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="融资灵活性一般，有1家金融机构融资业务合作"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="融资灵活性差或首次融资"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02408" indexName="政策风险" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="政策风险低"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="政策风险中"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="政策风险高"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02411" indexName="财政支持力度" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="支持力度大"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="支持力度中"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="支持力度小"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02414" indexName="区域地位" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="区域地位高"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="区域地位中"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="区域地位低"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02415" indexName="经费来源稳定性" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经费来源稳定性高"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经费来源稳定性中"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经费来源稳定性低"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02416" indexName="收入稳定性" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="收入稳定性高"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="收入稳定性中"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="收入稳定性低"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203952" groupName="综合评价" seqno="7">
				<ind:IndItemRadio indexNo="ST024$ST02417" indexName="领导者素质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="领导者有丰富的管理经验；近3年总资产或收入逐年扩大,业绩显著，有良好的社会声誉"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="其他"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02418" indexName="合作情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="与我行合作三年以上，有介绍新客户"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="与我行合作3年以上或有介绍新客户之一的"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="无"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02419" indexName="资金回笼率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="资金回笼率高于200%(含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="资金回笼率100%（含）-200%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="资金回笼率80%（含）-100%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="资金回笼率50%-80%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="资金回笼率低于50%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02420" indexName="生产经营期限" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="生产经营时间超过5年(含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="3年(含）-5年"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="低于3年"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203954" groupName="修正/调整项" seqno="8">
				<ind:IndItemRadio indexNo="ST024$ST02421" indexName="学校奖励情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="获得全国性荣誉称号"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="获得省级荣誉称号"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="获得市级荣誉称号"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST024$ST02423" indexName="财务报表是否经过会计事务所审计" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经会计事务所审计持无保留意见"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经会计事务所审计却持保留意见"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="未经审计"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST024$ST02427" indexName="其它可加减分因素（-3~3之间有效）" readonly="false" />
	

	</ind:IndGroup>


</ind:IndTableLayout>
