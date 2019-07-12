<%@page import="java.net.URLDecoder"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%
    String skinId = request.getParameter("skinId"); 
%>
<title>welcome</title>
<link href="<emp:file fileName='styles/index/page.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='scripts/jTabView-tofishes-2.1/css/demo.css'/>" rel="stylesheet" type="text/css" />
<!--<link href="<emp:file fileName='styles/QZ_blue.css'/>" rel="stylesheet" type="text/css"/>-->

 <%if("01".equals(skinId)){ %>
<link href="<emp:file fileName='styles/QZ_blue.css'/>" rel="stylesheet" type="text/css"/>
<%}else if("02".equals(skinId)){ %>
<link href="<emp:file fileName='stylesGreen/QZ_blue.css'/>" rel="stylesheet" type="text/css"/>
<%}else if("03".equals(skinId)){ %>
<link href="<emp:file fileName='stylesPink/QZ_blue.css'/>" rel="stylesheet" type="text/css"/>
<%}else if("04".equals(skinId)){ %>
<link href="<emp:file fileName='stylesRed/QZ_blue.css'/>" rel="stylesheet" type="text/css"/>
<%}%>

<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/jTabView-tofishes-2.1/js/jTabView-tofishes-1.0.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/emp/pageUtil.js'/>" type="text/javascript" language="javascript"></script>

<!--快捷菜单展开-->
<!--  
<script>

function createShortcut(){
/*	var curMenu1=parent.curMenu1;
	var curMenu2=parent.curMenu2;
	var trees=parent.trees;
	var nodes=trees[curMenu2[curMenu1]].aNodes;
	var rt_content=document.getElementById('rt_content_p');
	var tempString="";
	for(var i=0;i<nodes.length;i++){
		 if( nodes[i]['url'] == undefined || nodes[i]['url'] == ''){}
		 else{
			 var name="";
			 var url="";
			 name=nodes[i]['name'];
			 url=nodes[i]['url'];
			 id=nodes[i]['id'];
			 //trees[curMenu2[curMenu1]].openTo(id,true,false);
			 tempString = tempString+"<a title=\'" + name + "\' href=\""+url+"\" onClick=\"parent.trees[parent.curMenu2[parent.curMenu1]].openTo(\'"+id+"\',true,false)\" class=\"xz_p1\">"+name+"</a>";
		 }
	}
		
	rt_content.innerHTML=tempString;
	*/
}

</script>-->



</head>
<body id="QZ_welcomeBody" >
<div class="page_welcome" style="border:none;background:none;" >
	<div id="welcome" class="page_welcome_main">
	<%
	    String menuName = request.getParameter("menuName"); 
		//menuName = new String(menuName.getBytes("ISO-8859-1"), "UTF-8");   
		menuName = URLDecoder.decode(menuName);
	%>	
	<!--  <div id="wrap" style="width:100%;height:620px;overflow:hidden;"> -->
		<!--  <div id="demo" style="width:100%;height:620px;overflow:hidden;padding:0;">
			<div id="demoBox" style="width:1200%;height:620px;">
				<ul id="demo1">
					<li><img src="images/00.jpg" width="438px" height="620px" alt="泉之道：人本，诚信，创新，奋进" title="泉之道：人本，诚信，创新，奋进"/></li>
					<li><img src="images/01.jpg" width="438px" height="620px" alt="人本：汇为本，汇为源" title="人本：汇为本，汇为源"/></li>
					<li><img src="images/02.jpg" width="438px" height="620px" alt="诚信：静以澄，静以润" title="诚信：静以澄，静以润"/></li>
					<li><img src="images/03.jpg" width="438px" height="620px" alt="创新：上而诵，上而活" title="创新：上而诵，上而活"/></li>
					<li><img src="images/04.jpg" width="438px" height="620px" alt="奋进：续且进，续且济" title="奋进：续且进，续且济"/></li>
				</ul>
				<ul id="demo2"></ul>
			</div>
		</div>
		
		
		<script type="text/javascript">
			        var speed = 40;
			        var demo2 = document.getElementById('demo2');
			        var demo1 = document.getElementById('demo1');
			        var demo = document.getElementById('demo');
			         demo2.innerHTML = demo1.innerHTML;
			        function Marquee() {
			           if (demo2.offsetWidth - demo.scrollLeft <= 0) {
			               demo.scrollLeft -= demo1.offsetWidth;
			                }
			           else {
			                demo.scrollLeft++;
			                }
			         }
			        var MyMar = setInterval(Marquee, speed);
			        demo.onmouseover = function() { clearInterval(MyMar) };
			        demo.onmouseout = function() { MyMar = setInterval(Marquee, speed) };
			       </script>
			       -->
	
	<!--  
	  <div id="jTabView">
	      <h3>
			<a class="curr jtab" href="#d1" title=""><em>快捷菜单</em></a>
			<a href="#d2" class="jtab"><em>即时消息</em></a>
		  </h3>
	      <div class="jtab-con">
		     <div class="xzzq_box">
				<div class="rt_content" >
					<p id="rt_content_p"> 				
					</p>
				</div>
			</div>
	      </div>
	   </div>
	   -->
	   
	    <!-- 
	      <div class="jtab-con">
		  	<div class="area_content">
				<div class="hyy_yhgglb">
			       <span>产品部内部消息公告：</span>
			    </div>
	    
			    <div class="hyy_fkgd">
			       <div id="demo" style="background: #ffffff; overflow: hidden; color: #0000ff;height: 200px">
			          <div id="demo1">
			                <ul>
			                     <li><a href="#">产品部支撑系线上线，欢迎使用。</a></li>
			                </ul>                                        
			           </div>
			           <div id="demo2">
			           </div>
			      </div>
			         <div class="hyy_gd">
			            <a href="#">更多>></a>
			         </div>
			     </div>
			</div>
	      </div> 
	      -->
	      
	      
	      
	      
		<!--  </div>-->
	  </div>
	</div>

<script type="text/javascript">
	 function resize(){
		 var height=document.body.clientHeight;
		 var welcome=document.getElementById('welcome');
		 welcome.style.height=height-20;
	}
	 
	 $(document).ready(function(){
		 resize();
		 EMPTools.addEvent(window,'resize',resize);
	 });
	 
</script>
<script type="text/javascript">
    $(function(){
		//$("#jTabView").jTabView({retain:1});
		//$("pre").each(function(i){
		//	eval($(this).html());
		//});
  	});
</script>
</body>
</html>