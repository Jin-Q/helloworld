<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%!
    private String replace(String str, String str1, String str2)
    {
        for(int pos = str.indexOf(str1); pos >= 0; pos = str.indexOf(str1))
            str = String.valueOf(new StringBuffer(str.substring(0, pos)).append(str2).append(str.substring(str1.length() + pos)));
        return str;
    }
%>
<%  
    //String gs =  request.getParameter("gs") ;
	String gs = (String)session.getAttribute("chkGs");
	//byte b[]=gs.getBytes("ISO-8859-1");
	//gs=new String(b); 

    String rptType =  request.getParameter("rptType") ;
	//gs = replace(gs,"$","+");
	String styleId=request.getParameter("style_id");
 
%>
<emp:page>
<html:html>
<head>
<title>hidden</title>
<style type="text/css">
._select {
border: 1px solid #b7b7b7;
text-align:left;
width:240px;
}
</style>

<jsp:include page="/include.jsp" />
<script type="text/javascript">

    var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
		changeItems();
		
	};
	function changeItems(){
	    var paramStr ="fnc_conf_typ=<%=rptType%>";    	
		if (paramStr != null) {
		var url = '<emp:url action="queryFncConfItemsListByCon.do"/>&'+paramStr;
		var handleSuccess = function(o){ EMPTools.unmask();	
				setCustomer(o);	
		};
		var handleFailure = function(o){ EMPTools.unmask();	
		};
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);	
		}
	
	};
		
	function setCustomer(o){
	
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr define error!"+e.message);
				return;
			}
            var FncConfItemsList=jsonstr.FncConfItemsList;
			var select = item_id._obj.element;	
			select.length=1;
			
			for(var i=0;i<FncConfItemsList.length;i++){
				select.options[i+1] = new Option();
				select.options[i+1].value = "["+FncConfItemsList[i].item_id+"]"+FncConfItemsList[i].item_name;
				select.options[i+1].text =  "["+FncConfItemsList[i].item_id+"]"+FncConfItemsList[i].item_name;
			}
		}
	}
	function killErrors() 
	{ 
	return true; 
	}   
	window.onerror = killErrors; 
</script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" onload="doOnLoad()">
<form name="form1" method="post" action="">
  <table width="550" border="0" cellpadding="0" cellspacing="1" bgcolor="#9E9F9F">
    <tr>
      <td width="475" valign="top" bgcolor="#FFFFFF"><br>
    
        <table width="99%" border="1" align="center" cellpadding="5" cellspacing="0">
          <tr valign="top"> 
              <td>引用指标</td>
               <td><emp:select id="item_id" label="项目编号" required="false" cssElementClass="_select"/></td>
          </tr>
        
          <tr> 
               <td colspan="2"><div align="center"> 
                <input name="b_insert" type="button" id="b_insert2" value="插入" onClick="insert()">
               </div></td>
          </tr>
        </table></td>
      <td width="167" valign="top" bgcolor="#FFFFFF"><table width="165" height="308" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td width="201" height="188"><table width="160" height="120" border="1" align="center" cellpadding="0" cellspacing="1">
                <tr align="center" bgcolor="#CCCCCC">
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('/')" align="center">/</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('*')" align="center">*</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('-')" align="center">-</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('+')" align="center">+</td>
                </tr>
                <tr align="center" bgcolor="#CCCCCC">
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('7')" align="center">7</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('8')" align="center">8</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('9')" align="center">9</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr(',')"  align="center">,</td>
                </tr>
                <tr align="center" bgcolor="#CCCCCC">
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('4')" align="center">4</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('5')" align="center">5</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('6')" align="center">6</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('%')" align="center">%</td>
                </tr>
                <tr align="center" bgcolor="#CCCCCC">
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('1')"  align="center">1</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('2')"  align="center">2</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('3')"  align="center">3</td>
                  <td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="" align="center">←</td>
                </tr>
                <tr align="center" bgcolor="#CCCCCC">
					<td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('0')" align="center">0</td>
					<td width="40" height="30" onMouseOver="mo()" onMouseOut="mu()" onClick="inputStr('.')" align="center">.</td>
					<td width="40" height="30" onMouseOver="mo()" nMouseOut="mu()" onClick="inputStr('(')" align="center">(</td>
					<td width="40" height="30" onMouseOver="mo()"onMouseOut="mu()" onClick="inputStr(')')" align="center">)</td>
				</tr></table>
			</td>
						</tr>
						<tr>
							<td>
								<p align="center">
									<input name="b_max" type="button" onClick="inputStr('MAX()')"
										value="MAX">
									<input name="b_min" type="button" id="b_min2"
										onClick="inputStr('MIN()')" value="MIN">
									<input name="b_int" type="button" id="b_int2"
										onClick="inputStr('INT()')" value="INT">
								</p>
							</td>
						</tr>
						<tr>
							<td>
								<div align="center">
									<input name="s_oper" type="button"
										onClick="inputStr('@OPER(==)')" value="=">
									<input name="s_oper" type="button"
										onClick="inputStr('@OPER(>)')" value=">">
									<input name="s_oper" type="button"
										onClick="inputStr('@OPER(>=)')" value="≥">
									<input name="s_oper" type="button"
										onClick="inputStr('@OPER(<)')"
										value="<">
				<input name="s_oper" type="button" onClick="inputStr('@OPER(<=)')" value="≤">
              </div></td>
          </tr>
          <tr>
            <td height="60"><div align="center">
                <input name="b_js" type="button" id="b_js" value="公式校验" onClick="doCheck()">
                <input name="b_clear" type="button" id="b_clear" value="清除公式" onClick="doClear()">
              </div></td>
          </tr>
        </table></td>
    </tr>
    <tr>
      <td colspan="2" valign="top" bgcolor="#FFFFFF"> &nbsp;
        <textarea name="gongshi" cols="90" rows="8" id="gongshi"><%=gs%></textarea>
      </td>
    </tr>
  </table>
<table width="515" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="505"><div align="center">
          <input type="button" name="Button" value="确定" onClick="doOk()">
          &nbsp;
          <input type="button" name="Submit2" value="返回" onClick="window.close()">
        </div></td>
    </tr>
  </table>
</form>
<script language="javascript">

//那个版本这个JS是带参数的，现在的版本中不需要参数
function insert()
{   
	var string;
  	string="{"+item_id._obj.element.value+"}";
	inputStr(string);
}

function inputStr(string)
{
	document.all["gongshi"].focus();
	//if(this.selectedIndex)
		document.selection.createRange().text=string;
	//else
	//	document.all["formula"].value=document.all["formula"].value+string;
}


function trim(str)
{
	return str.replace(/(^\s*)|(\s*$)/g, "");
}


function check()
{
	var formula=document.all["gongshi"].value;
	if(trim(formula)=="")
		return "公式不能为空";

	formula=replaceAll(formula,"@OPER","");	
	oper = getParam(formula,"(",")");
	formula=replaceAll(formula,"("+oper+")","");	

	formula=replaceAll(formula,"MAX","Math.max");
	formula=replaceAll(formula,"MIN","Math.min");
	formula=replaceAll(formula,"INT","Math.round");
	formula=replaceAll(formula,"@SUMSUB","");
	formula=replaceAll(formula,"@PARENT","");
	formula=replaceAll(formula,"@RANK","");
	formula=replaceAll(formula,"@ZONE","");
	formula=replaceAll(formula,"@SIBLINGS()","1");
	
	formula=replaceAll(formula,"@OPER","0");
	while(!getParam(formula,"{","}")=="")
	{
		var param=getParam(formula,"{","}");
		formula=replaceAll(formula,"{"+param+"}","@@");	
	}
	
	if(formula.indexOf("@@@@")>-1)
		return "指标间缺少运算符！";
		
	formula=replaceAll(formula,"@@","0");
	
	try{
		var result=eval(formula);
		return "";
	}catch(exception)
	{
		return "公式错误："+exception.description;
	}
}


 function replaceAll(str,str1,str2){
	while(str.indexOf(str1)>=0)
		str=str.replace(str1,str2);
    
    return str;
}
 
function getParam(str,leftFlag,rightFlag)
{
    var param="";
    while(str.indexOf(rightFlag) < str.indexOf(leftFlag))
    {
    	if(str.indexOf(rightFlag)<0)
    		break;
        str=str.substring(str.indexOf(rightFlag)+1);
    }

    if(str.indexOf(leftFlag)>=0 && str.indexOf(rightFlag)>=0)
    {
        var pos1=str.indexOf(leftFlag);
        var pos2=str.indexOf(rightFlag);
        param=str.substring(pos1+1,pos2);
    }
    return param;
}

function doCheck(){
  
	var result=check();
	if(result=="")
	{
		
		alert("公式正确");
	}
	else
		alert(result);
}
function doClear()
{
	document.all["gongshi"].value ="";
}

function doOk()
{
	var result=check();
	if(result=="")
	{
		window.returnValue=document.all["gongshi"].value;
		window.close ();
	}
	else
		alert(result);
}
function mo(){
  var e=window.event;
  var S=e.srcElement;
  S.style.color ="blue";
  S.className = "jsqbg";
  S.style.cursor= "hand";
}

function mu(){
  var e=window.event;
  var S=e.srcElement;
  S.className = "";
  S.style.color ="black";
}

  selectType(<%=rptType%>);
  
</script>

</body>
</html:html>
</emp:page>

