
	var menuTree;	//存放该用户的菜单结构
	var operations;
	var menuNodes = {};		//存放所有节点映射以便查找
	var curMenu1;	//存放当前激活菜单项
	var curMenu2={};
	var trees={};
	var logInfo="";
	
	function getMenuData(sUrl){
		var divObj;
		var callback = {
		  success:handleSuccess,
		  failure:handleFailure,
		  argument: { divObj:divObj }
		}
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
	};


	/**
	  ajax http 请求的成功回调函数
	*/
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var result = eval("("+o.responseText+")");
				menuTree = result.menuTree;
				operations = result.operations;
			} catch(e) {
				alert("Parse menu define error!"+e);
				return;
			}
			
			//showMenuBar1();
			showMenuBar();
		}
	};	
	
	/** 显示菜单*/
	function showMenuBar(){
		var menu1div = document.getElementById("menu_lv1");
		catchMenu(menuTree);
		
		createFirstMenuBar(menu1div, menuTree);
		
		createTreeMenuBar(menuTree.children[0].children[0].id);
	}
	
	function catchMenu(pNode){
		if (pNode.children){
			for (var i in pNode.children) {
				var item = pNode.children[i];
			 
				menuNodes[item.id] = item;
			}
		}
	}
	
	/** 显示一级菜单 @TODO ： 现只针对信贷管理系统*/
	function createFirstMenuBar(htmlNode, pNode){
		var firstMenuId = "";
		if (pNode.children){
			var cmisroot = pNode.children[0];
			
			for (var i in cmisroot.children) {
				var item = cmisroot.children[i];
				menuNodes[item.id] = item;
				//alert('item  ' + item.label );
				//样例： <span id="m1_1" class="menu_on"><a href="#">我的工作台</a></span>
				var spanNode = document.createElement("SPAN");
				htmlNode.appendChild(spanNode);
				spanNode.id = item.id+"_span";
				if(i != 0){
				  spanNode.className = "menu_off";
				} else {
				  firstMenuId = item.id;
				  spanNode.className = "menu_on";
				}
				var textNode = document.createTextNode(item.label);
				var linkNode = document.createElement("A");
				linkNode.href="#";
				linkNode.id = item.id+"_a";
				linkNode.itemid = item.id;
				//alert(item.id);
				linkNode.onclick = Function("ml1('"+item.id+"')");
				spanNode.appendChild(linkNode);
				linkNode.appendChild(textNode);
			}
		}
		changeTableSize();   //一级菜单生成后，重新设置主内容页面的高度   
		return firstMenuId;
	}
	///不用
	function showMenuBar1() {
		var menu1div = document.getElementById("menu_lv1");
		createMenuBar(menu1div, menuTree, 1);
		showMenuBar2(menuTree.children[0].id);
	};
	
 
		
	function createMenuBar(htmlNode, pNode, level) {
		if (pNode.children)
		for (var i in pNode.children) {
			var item = pNode.children[i];

			menuNodes[item.id] = item;

			var spanNode = document.createElement("SPAN");
			spanNode.id = item.id+"_span";
			
			spanNode.className = "menu"+level+"_off";
				
			htmlNode.appendChild(spanNode);

			var textNode = document.createTextNode(item.label);
			var linkNode = document.createElement("A");
			linkNode.href="#";
			linkNode.id = item.id+"_a";
			linkNode.itemid = item.id;
			linkNode.onclick = Function("ml"+level+"('"+item.id+"')");
			spanNode.appendChild(linkNode);
			linkNode.appendChild(textNode);
			
		}
	};
	
	function createTreeMenuBar(firstMenuId) {
		
		if (firstMenuId == curMenu1) return;

		
		var curSubMenuDiv = document.getElementById(curMenu1+"_subMenu");
		if (curSubMenuDiv)
			curSubMenuDiv.style.display = "none";
		var curMenuSpan = document.getElementById(curMenu1+"_span");
		if (curMenuSpan)
			curMenuSpan.className="menu_off";
		var newMenuSpan = document.getElementById(firstMenuId+"_span");
		if (newMenuSpan)
			newMenuSpan.className="menu_on";		
				
		var subMenuDiv = document.getElementById(firstMenuId+"_subMenu");
		if (subMenuDiv) {
			subMenuDiv.style.display = "";
		} else {
			/**
			var menu2div = document.getElementById("menu_lv2");
			var divNode = document.createElement("DIV");
			divNode.id = firstMenuId+"_subMenu";
			menu2div.appendChild(divNode);
			createMenuBar(divNode, menuNodes[firstMenuId], 2);
			*/
			//alert(">>> " + firstMenuId);
			catchMenu(menuNodes[firstMenuId]);
		}
		try {

			//alert(">>>>> " +menuNodes[firstMenuId].id);
			if (!curMenu2[firstMenuId]) {
				//curMenu2[firstMenuId] = menuNodes[firstMenuId].children[0].id;
				curMenu2[firstMenuId] = menuNodes[firstMenuId].id;
			}
			
			showTree2(firstMenuId,curMenu2[firstMenuId]);
		
		}catch(e){}
		curMenu1 = firstMenuId;
		
	}
	
	function showTree2(lv1ItemId,lv2ItemId) {
		if (!lv1ItemId) lv1ItemId = curMenu1;
		if (lv2ItemId == curMenu2[curMenu1]) return;
		
		var curSubMenuDiv = document.getElementById(curMenu2[curMenu1]+"_subMenu");
		if (curSubMenuDiv)
			curSubMenuDiv.style.display = "none";
		var curMenuSpan = document.getElementById(curMenu2[curMenu1]+"_span");
		if (curMenuSpan)
			curMenuSpan.className="menu_off";
		var newMenuSpan = document.getElementById(lv2ItemId+"_span");
		if (newMenuSpan)
			newMenuSpan.className="menu_on";	
			
		var subMenuDiv = document.getElementById(lv2ItemId+"_subMenu");
		if (subMenuDiv) {			
			subMenuDiv.style.display = "";
		} else {		
			var menu3div = document.getElementById("Page_left_tree");
			var divNode = document.createElement("DIV");
			divNode.id = lv2ItemId+"_subMenu";
			menu3div.appendChild(divNode);
			
			try {
				var dtree = new dTree('trees.'+lv2ItemId);
				dtree.add(lv2ItemId+"_treenode",-1,menuNodes[lv2ItemId].label);
				//alert(lv2ItemId);
				createSubTree(lv2ItemId, menuNodes[lv2ItemId], dtree);
			} catch (e) {};
            //alert(dtree.toString());
			divNode.innerHTML = dtree.toString();
			trees[lv2ItemId] = dtree;
		}
		curMenu2[lv1ItemId] = lv2ItemId;
	};	
	
	
 
	
	function createSubTree(pid, pNode, dtree){
		var children = pNode.children;
		for( var i=0; i<children.length; i++) {
			var item = children[i];
			 
			var action = encodeURL(item.action);
			if (action)
				if (action.indexOf("?")>-1) {
					action+="&menuId="+item.id;
				} else {
					action+="?menuId="+item.id;
				}
			
			dtree.add(item.id+"_treenode",pid+"_treenode",item.label,action,'','infoframe');
			menuNodes[item.id] = item;
			if (item.children && item.children.length>0) {
				createSubTree(item.id, item, dtree);
			}
		}
	};
	
	/**
	  ajax http 请求的失败回调函数
	*/

	var handleFailure = function(o){
		if(o.responseText !== undefined){
			alert("error");
		}
	};
	
	//一级菜单事件
	function ml1(idStr) {
		createTreeMenuBar(idStr);
		var skinid = document.getElementById("skin").title;
		//我的工作台、行长驾驶舱，一级菜单点击跳转到自定义首页  2014-05-27 tsy
		if("wdgzt" == idStr || "President_Cockpit" == idStr){
			var emp_sid = document.getElementById("emp_sid").value;
			window.infoframe.location = "queryCustomHomePage.do?menuId=customHomePage&EMP_SID="+emp_sid;
		}else{
			var menu = menuNodes[idStr].label;
			menu = encodeURI(menu);
			//alert(menu);
			window.infoframe.location=encodeURL("welcome.jsp")+"&menuName="+menu+"&skinId="+skinid;
		}
		//var el=document.getElementById("Page_navigation");
		//if(el)
		//	el.innerHTML='';
		
		//document.getElementById("Page_navigation").innerHTML =logInfo;
	};
	
	//二级菜单事件。
	function ml2(idStr) {
		showTree(null,idStr);
	};
	
	function checkPermission(menuid, opid) {
		var item = operations[menuid];
		if (item){
			if(item[opid]){
				return true;
			}
		}
		return false;
	};
	