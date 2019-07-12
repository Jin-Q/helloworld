<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.data.IndexedCollection"%>
<%@ page import="com.ecc.emp.data.KeyedCollection"%>
<%@ page import="java.util.*"%>
<emp:page>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
IndexedCollection deptusericoll=(IndexedCollection)context.getDataElement("SDeptuserList");
IndexedCollection orgicoll=(IndexedCollection)context.getDataElement("SDeptList");

ArrayList orgSelected=new ArrayList();
ArrayList orgSelectedList=new ArrayList();
ArrayList orgList=new ArrayList();


for(int i=0;i<deptusericoll.size();i++)
{

	KeyedCollection deptuserkcoll=(KeyedCollection)deptusericoll.get(i);
	String organno=(String)deptuserkcoll.getDataValue("organno");
	
	orgSelected.add(organno);
}

for(int i=0;i<orgicoll.size();i++ )
{
	boolean isSelect=false;
	KeyedCollection orgkcoll=(KeyedCollection)orgicoll.get(i);
	if(orgkcoll!=null){
		String organnoList=(String)orgkcoll.getDataValue("organno");
		
		System.out.println("org Not Selected: "+organnoList);

			for(int j=0;j<orgSelected.size();j++)
			{
				
				if(organnoList.equals((String)orgSelected.get(j)))
				{
					isSelect=true;
					orgSelected.remove(j);
					orgSelectedList.add(orgkcoll);
					break;
				}
			}
			if (!isSelect)
			{
				
				orgList.add(orgkcoll);
			}
		}
	
}
for(int i=0;i<orgList.size();i++)
{
	KeyedCollection a =(KeyedCollection)orgList.get(i);
}
for(int i=0;i<orgSelectedList.size();i++)
{
	KeyedCollection a =(KeyedCollection)orgSelectedList.get(i);
}


%>
<html>
<head>
<link href="<ctp:file fileName='styles/default/common.css'/>" rel="stylesheet" type="text/css" />
<script language="Javascript">

var selectedOld = new Array();
var unselectedOld = new Array();
var selectedNew =new Array();

function preparedata()
{
//prepare data for select[organno]
<%for(int i=0;i<orgSelectedList.size();i++ )
{
%>var organnoOption<%=i%>=new Option();
organnoOption<%=i%>.value="<%=(String)((KeyedCollection)orgSelectedList.get(i)).getDataValue("organno")%>";

organnoOption<%=i%>.text="<%=(String)((KeyedCollection)orgSelectedList.get(i)).getDataValue("organname")%>";
document.form1.organno.options[<%=i%>]=organnoOption<%=i%>;
selectedOld[<%=i%>]="<%=(String)((KeyedCollection)orgSelectedList.get(i)).getDataValue("organno")%>";
<%}%>


//prepaer data for select[]
<%for(int i=0;i<orgList.size();i++ )
{
%>var unselectOption<%=i%>=new Option();
unselectOption<%=i%>.value="<%=(String)((KeyedCollection)orgList.get(i)).getDataValue("organno")%>";

unselectOption<%=i%>.text="<%=(String)((KeyedCollection)orgList.get(i)).getDataValue("organname")%>";
document.form1.unselectrole.options[<%=i%>]=unselectOption<%=i%>;
unselectedOld[<%=i%>]="<%=(String)((KeyedCollection)orgList.get(i)).getDataValue("organno")%>";
<%}%>
};

function move(side)
{
	var temp1 = new Array();
	var temp2 = new Array();
	var tempa = new Array();
	var tempb = new Array();
	var current1 = 0;
	var current2 = 0;
	var y=0;
	var attribute;

	//assign what select attribute treat as attribute1 and attribute2
	if (side == "in")
	{
		attribute1 = document.form1.organno;
		attribute2 = document.form1.unselectrole;
	}
	else
	{
		attribute1 = document.form1.unselectrole;
		attribute2 = document.form1.organno;
	}

	//fill an array with old values
	for (var i = 0; i < attribute2.length; i++)
	{
		y=current1++
		temp1[y] = attribute2.options[i].value;
		tempa[y] = attribute2.options[i].text;
	}

	//assign new values to arrays
	for (var i = 0; i < attribute1.length; i++)
	{
		if ( attribute1.options[i].selected )
		{
			y=current1++
			temp1[y] = attribute1.options[i].value;
			tempa[y] = attribute1.options[i].text;
		}
		else
		{
			y=current2++
			temp2[y] = attribute1.options[i].value;
			tempb[y] = attribute1.options[i].text;
		}
	}

	//generating new options
	for (var i = 0; i < temp1.length; i++)
	{
		attribute2.options[i] = new Option();
		attribute2.options[i].value = temp1[i];
		attribute2.options[i].text =  tempa[i];
	}

	//generating new options
	for (var i=temp2.length; i<attribute1.lenght; i++)
	{
		attribute1.options[i] = null;
	}
	attribute1.length = temp2.length;
	if (temp2.length>0)
	{
		for (var i = 0; i < temp2.length; i++)
		{
			attribute1.options[i] = new Option();
			attribute1.options[i].value = temp2[i];
			attribute1.options[i].text =  tempb[i];
		}
	}
	organno._setValue("");
	state._setValue("");
}
function see()
{
	organno._setValue("");
	state._setValue("");
	var attribute=document.form1.organno.options;
	

	for(var i=0; i<unselectedOld.length;i++)
	{	
		for(var j=0;j<attribute.length;j++)
			{
				organnonew=attribute[j].value;
				
				if(organnonew!=""&&organnonew!=null)
				{
						if(unselectedOld[i]==organnonew)
						{
							if(organno._getValue()=="")
							{
								organno._setValue(organnonew);
								state._setValue("true");
							}else{
								organno._setValue(organno._getValue()+","+organnonew);
								state._setValue(state._getValue()+",true");
							}					
						}
					
				}
			}
	}
	

	
	
	attribute=document.form1.unselectrole.options;
	for(var i=0; i<selectedOld.length;i++)
	{	
		for(var j=0;j<attribute.length;j++)
			{
				organnonew=attribute[j].value;
				
				if(organnonew!=""&&organnonew!=null)
				{
						if(selectedOld[i]==organnonew)
						{
							if(organno._getValue()=="")
							{
								organno._setValue(organnonew);
								state._setValue("false");
							}else{
								organno._setValue(organno._getValue()+","+organnonew);
								state._setValue(state._getValue()+",false");
							}	
						}
					
				}
			}
	}


	if(organno._getValue()!="" &&state._getValue()!="")
	{
		organno._toForm(form2);
		state._toForm(form2);
		actorno._toForm(form2);
		form2.submit();
	}else{
		alert("未作任何修改");
	}

}
function returnQueryList()
{
		//var url = '<emp:url action="querySUserList.do"/>&menuId=thgl';
		//window.location = url;
	window.close();
}
</script>
<jsp:include page="/include.jsp" flush="true"/>
</head>
<body onload="preparedata()">

<form name="form1" isLayoutContent="false" action="addroletouser" method="POST">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="45%" align="center" valign="top">
				<div align="left">				</div>
				<fieldset><legend align="center"><span class="titletext">已授机构</span></legend>
				<table width="89%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><span class="tdtext"><span class="titletext">机构名称:</span></span></td>
                  </tr>
                  <tr>
                    <td align="center"><br>
                      <select name="organno" multiple size="12" style="width:250"  onDblclick="move('in');">


</select>
                      <br>
                      <br></td></tr>
                </table>
				</fieldset>
				</td>
                <td width="10%" align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="center">
					<span class="lightgrayinput"><span class="even">
					<button name="save2" onclick="move('in');">>>></button>
					</span></span>                      <br>
                        <span class="lightgrayinput"><span class="even">
                        <button name="save22" onclick="move('out');"><<<</button>
                        </span></span></td></tr>
                </table></td>
                <td width="45%" align="center" valign="top">
				<div align="left">				</div>
                <fieldset><legend align="center"><span class="titletext">可授机构</span></legend>
                    <table width="89%" border="0" cellspacing="0" cellpadding="0">
                          <tr class="tdtext">
                            <td  align="right">&nbsp;</td>
                          </tr>
                          <tr class="tdtext">
                            <td  align="right"><br>
			<select name="unselectrole" multiple size="12" style="width:250" onDblclick="move('out');">

			
			</select>
			<br><br></td>
                          </tr>
                    </table>
                  </fieldset></td>
              </tr>
          </table>
<p align="center">
<button name="seee" onclick="see();">提交</button>&nbsp;
<button name="return" onclick="returnQueryList();">返回</button>

</p>

</form>   
<emp:form id="form2" name="form2" action="addDeptToUser.do" method="POST">
<emp:text id="state" label="state" hidden="true" />
<emp:text id="organno" label="organno" hidden="true" />
<emp:text id="actorno" label="actorno" hidden="true" />
</emp:form>
</body>
</html>
</emp:page>