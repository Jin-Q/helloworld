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
//检验组规模的指标
			//检验组资本充足状况的指标
		//检验组资产质量状况的指标
				//检验组盈利状况 的指标
			//检验组流动性状况 的指标
				return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2012203923" groupName="规模" seqno="1">
    			<ind:IndItemText indexNo="ST015$ST01501" indexName="存款（亿元）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01502" indexName="本地存款市场占有率（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01503" indexName="资产（亿元）" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203924" groupName="资本充足状况" seqno="2">
    			<ind:IndItemText indexNo="ST015$ST01504" indexName="资本充足率（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01505" indexName="核心资本充足率（%）" readonly="false" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203925" groupName="资产质量状况" seqno="3">
    			<ind:IndItemText indexNo="ST015$ST01506" indexName="不良贷款率（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01507" indexName="单一最大客户贷款比例（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01508" indexName="全部关联度（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01509" indexName="贷款损失准备充足率（%）" readonly="false" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203926" groupName="盈利状况 " seqno="4">
    			<ind:IndItemText indexNo="ST015$ST01510" indexName="资产利润率（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01511" indexName="资本利润率（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01512" indexName="成本收入比率（%）" readonly="false" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203927" groupName="流动性状况 " seqno="5">
    			<ind:IndItemText indexNo="ST015$ST01513" indexName="流动性比例（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01514" indexName="核心负债依存度（%）" readonly="false" />
	

    			<ind:IndItemText indexNo="ST015$ST01515" indexName="流动性缺口率（%）" readonly="false" />
	

	</ind:IndGroup>


</ind:IndTableLayout>
