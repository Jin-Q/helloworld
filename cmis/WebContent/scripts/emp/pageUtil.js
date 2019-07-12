var EMP = {
	util : {},
	widget : {},
	data : {},
	field : {},
	type : {}
};

if (!EMP.util.Tools) {

	EMP.util.Tools = {
		/**
		 * 设置cookie 值 及生存期
		 * 
		 * @argument name 名字
		 * @argument value 值
		 * @argument expires 天 note： 为 0时 生存期为浏览器进程
		 * @argument path 存放路径 eg： set_cookie('name', 'user', 0, '/', '', '');
		 */
		setCookie : function(name, value, expires, path, domain, secure) {
			// set time, it's in milliseconds
			var today = new Date();
			today.setTime(today.getTime());
			expires = expires * 1000 * 60 * 60 * 24;
			var expires_date = new Date(today.getTime() + (expires));
			document.cookie = name
					+ "="
					+ escape(value)
					+ ((expires)
							? ";expires=" + expires_date.toGMTString()
							: "") + ((path) ? ";path=" + path : "")
					+ ((domain) ? ";domain=" + domain : "")
					+ ((secure) ? ";secure" : "");
		},
		/**
		 * 根据cookie 名 得到其 值
		 */
		getCookie : function(name) {
			var start = document.cookie.indexOf(name + "=");
			var len = start + name.length + 1;
			if ((!start) && (name != document.cookie.substring(0, name.length))) {
				return null;
			}
			if (start == -1)
				return null;
			var end = document.cookie.indexOf(";", len);
			if (end == -1)
				end = document.cookie.length;

			return unescape(document.cookie.substring(len, end));
		},
		/**
		 * 删除cookie 值
		 */
		delCookie : function(name, path, domain) {
			if (getCookie(name))
				document.cookie = name + "=" + ((path) ? ";path=" + path : "")
						+ ((domain) ? ";domain=" + domain : "")
						+ ";expires=Thu, 01-Jan-1970 00:00:01 GMT";
		},
		/**
		 * 用于显示调试信息(需要页面引入log.js。否则，该方法不起作用)
		 */
		log : function(type, level, msg, e) {

			if (EMP.util.Logger) {
				EMP.util.Logger.log(type, level, msg, e);
			} else {
				if (level == 4)
					alert(msg);
				if (e != null) {
					throw e;
				}
			}

		},

		/**
		 * 抛出指定名称、信息的异常
		 */
		error : function(name, message) {
			var err = new Error();
			err.name = name;
			err.message = message;
			throw (err);
		},

		/**
		 * JS方法返回的消息对象(通常用在校验中，具体属性的作用在不同的模块中可能有不同的含义):
		 * 
		 * @result：操作结果(true表示成功；false表示失败。缺省情况下表示成功)；
		 * @message：消息对象的具体内容(如失败时表示失败的信息。缺省情况下为空)；
		 * @level：消息的级别(0：只在标签的后面显示信息；1：将信息使用alert弹出。缺省情况下只显示)；
		 * @control：消息对于表单提交操作的控制作用(0：不可忽略；1：可忽略；2：不需要检查。缺省情况下是不可忽略的)； 目前用到消息对象的有两个地方：
		 *                                                             1、数据类型校验，其中message属性在成功时表示所需要的真实值或显示值，而在失败时表示失败的信息
		 *                                                             2、页面的业务校验接口，其中的message属性只在失败时表示失败信息，其它情况不处理
		 */
		message : function(result, message, level, control) {
			var obj = {};
			if (result == null) {
				result = true;
			}
			if (level == null || level != 0 && level != 1) {
				level = 0;
			}
			if (control == null || control != 0 && control != 1 && control != 2) {
				control = 0;
			}
			obj.result = result;
			obj.message = message;
			obj.level = level;
			obj.control = control;
			return obj;
		},

		/**
		 * 增加某个HTML元素的指定css样式
		 */
		addClass : function(el, className) {
			if (!el)
				return;
			if (el.className.indexOf(className) == -1) {
				el.className += " " + className;
			}
		},

		/**
		 * 删除某个HTML元素的指定css样式
		 */
		removeClass : function(el, className) {
			if (!el)
				return;
			if (el.className.indexOf(className) != -1) {
				var reg = new RegExp("\\b" + className + "\\b");
				var tmp = el.className.replace(reg, "");
				el.className = tmp.replace(" {2,}", " ");
			}
		},

		/**
		 * 添加某个HTML元素的指定事件
		 * 
		 * @el ：HTML对象
		 * @eventName ：事件名称(小写，如click、blur、change等)
		 * @method ：事件触发的方法(不是方法名称)
		 * @host ：事件指定的当前对象(即在事件所触发的方法中使用this所代表的对象)
		 */
		addEvent : function(el, eventName, method, host) {
			if (!el || !eventName || !method)
				return;
			if (!host)
				host = window;
			var args = new Array();
			for (var i = 4; i < arguments.length; i++) {
				args.push(arguments[i]);
			}
			if (EMP.util.Tools.Browser.ie) { // IE
				el.attachEvent("on" + eventName, function(event) {
							args.unshift(event); // 将event对象插到参数列表最前面
							try {
								var res = method.apply(host, args);
							} catch (e) {
								alert('Error: ' + e.message);
								res = false;
							}
							if (res == false) {
								event.cancelBubble = true;
								event.returnValue = false;
							}
						});
			} else { // Mozilla
				el.addEventListener(eventName, function(event) {
							args.unshift(event); // 将event对象插到参数列表最前面
							var res = method.apply(host, args);
							if (res == false) {
								event.preventDefault();
								event.stopPropagation();
							}
						}, false);
			}
		},

		/**
		 * 获得页面边缘与容器(比如窗口)之间的间距 返回值中的"x"属性 ：左边缘间距(横向) 返回值中的"y"属性 ：上边缘间距(纵向)
		 */
		getScrollPos : function() {
			var pos = {};
			if (EMP.util.Tools.Browser.ie) { // IE
				if (window.pageXOffset) {
					pos.x = window.pageXOffset;
					pos.y = window.pageYOffset;
				} else if (document.documentElement
						&& document.documentElement.scrollLeft) {
					pos.x = document.documentElement.scrollLeft;
					pos.y = document.documentElement.scrollTop;
				} else if (document.body) {
					pos.x = document.body.scrollLeft;
					pos.y = document.body.scrollTop;
				}
			} else { // Mozilla
				pos.x = document.body.scrollLeft;
				pos.y = document.body.scrollTop;
			}
			return pos;
		},

		/**
		 * 设置某个HTML元素的innerText
		 */
		setInnerText : function(el, text) {
			if (EMP.util.Tools.Browser.ie) {
				el.innerText = text;
			} else {
				if (el.hasChildNodes()) {
					el.innerHTML = "";
				}
				var textnode = document.createTextNode(text);
				el.appendChild(textnode);
			}
		},

		/**
		 * 获得HTML元素中包含的某个标签名称的指定id对象
		 */
		getElementById : function(el, id, tagName) {
			var els = el.getElementsByTagName(tagName);
			for (var i = 0; i < els.length; i++) {
				if (els[i].id && els[i].id == id) {
					return els[i];
				}
			}
			return null;
		},

		/**
		 * 得到某个对象中的所有属性(或方法)，只是个简单的方法，只能获得一个层次的数据，无法递归。
		 */
		seeObject : function(obj) {
			var s = "";
			for (var i in obj) {
				s += i + "=" + obj[i] + "; ";
			}
			return s;
		},

		/**
		 * 获得指定HTML元素中包含的所有指定标签名称的对象
		 */
		getChildrenByTagName : function(el, tagName) {
			var result = new Array();
			tagName = tagName.toLowerCase();
			for (var i = 0; i < el.childNodes.length; i++) {
				if (el.childNodes[i].tagName
						&& el.childNodes[i].tagName.toLowerCase() == tagName) {
					result.push(el.childNodes[i]);
				}
			}
			return result;
		},

		/**
		 * 得到url中指定参数的值
		 * 
		 * @param ：指定的参数
		 */
		getParam : function(url, param) {

			var s = url.indexOf("?");
			if (s == -1) {
				return null;
			}
			var pre = param + "=";
			var i = url.indexOf(pre, s);

			if (i == -1) {
				return null;
			} else {
				var e = url.indexOf("&", i + 1);
				if (e == -1) {
					return url.substring(i + pre.length);
				} else {
					return url.substring(i + pre.length, e);
				}
			}

		},

		/**
		 * 设置url中指定参数的值
		 * 
		 * @param ：指定的参数
		 * @value ：设置的值
		 */
		setParam : function(url, param, value) {

			if (url == null || param == null || value == null)
				return url;
			var s = url.indexOf("?");
			var noparam = false;
			if (s == -1) {
				url += "?";
				s = url.length;
				noparam = true;
			}
			var pre = param + "=";
			var i = url.indexOf(pre, s);
			if (i == -1) {
				if (noparam)
					return url + pre + value;
				else
					return url + "&" + pre + value;
			} else {
				var url1 = url.substring(0, i + pre.length);
				var e = url.indexOf("&", i + 1);
				if (e == -1) {
					return url1 + value;
				} else {
					return url1 + value + url.substring(e);
				}
			}

		},

		/**
		 * 获得当前弹出窗口的父窗口(同时兼容window.open和window.showModelDialog)
		 */
		getWindowOpener : function(winObj) {
			if (!winObj) {
				winObj = window;
			}
			var windowOpener = null;
			if (EMP.util.Tools.Browser.ie && EMP.util.Tools.useModelDialog) { // IE
				windowOpener = winObj.dialogArguments;
			} else {
				windowOpener = winObj.opener;
			}
			return windowOpener;
		},

		/**
		 * 打开当前页面的弹出窗口(同时兼容window.open和window.showModelDialog)
		 */
		openWindow : function(url, winId, popparam) {
			var subWindow = null;

			if (EMP.util.Tools.Browser.ie && EMP.util.Tools.useModelDialog) { // IE
				if (popparam == null)
					popparam = "dialogHeight:400, dialogWidth:800, dialogTop:120, dialogLeft:200, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no";
				subWindow = window.showModalDialog(url, window, popparam);
			} else {
				if (popparam == null)
					popparam = "height=400, width=800, top=120, left=200, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no";
				winId = "popwin_" + winId.replace("\.", "_");
				// edit by 张普(过滤多一个.)
				winId = "popwin_" + winId.replace("\.", "_");

				subWindow = window.open(url, winId, popparam);
			}

			return subWindow;
		},

		/**
		 * 检查数据长度，用于计算HTML本身无法判断的双字节字符长度 判断ASC2码256以上有几个
		 */
		getByteLength : function(str) {
			if (str == null)
				return 0;
			if (typeof(str) != 'string')
				str = str.toString();
			var length = str.length;
			var arr = str.match(/[^\x00-\xff]/ig);
			return length + (arr == null ? 0 : arr.length);
		},

		/**
		 * 将字符串前后空格去除
		 */
		trim : function(str) {
			if (str == null || str == "")
				return str;
			str = str.replace(/(^\s*)/g, "");
			str = str.replace(/(\s*$)/g, "");
			return str;
		},

		/**
		 * 获得工作流所需要的权限信息(只适用于工作流)
		 */
		getWorkFlowPermission : function() {
			var buttonpermission = page.buttonpermission;
			var parentWin = window;
			while (buttonpermission == null) {
				if (parentWin == EMP.util.Tools.topPage) {// 已经搜索到了最外围，则停止
					break;
				}

				// 继续找上一页面(或弹出框的父页面)
				if (EMP.util.Tools.getWindowOpener(parentWin)) {
					parentWin = EMP.util.Tools.getWindowOpener(parentWin);
					if (!parentWin)
						break;
				} else {
					parentWin = parentWin.parent;
				}
				var parentPage = parentWin.window.page;
				if (parentPage == null)
					break;
				buttonpermission = parentPage.buttonpermission;
			}
			return buttonpermission;
		},

		/**
		 * 对url进行规整并使用js的encodeURI进行编码
		 */
		encodeURI : function(url) {
			if (url) {
				var reg = new RegExp("(.*)\\?(.*)\\?(.*)");
				url = url.replace(reg, "$1?$2&$3");
				return encodeURI(url);
			}
		},
		/**
		 * 对url进行规整并使用js的encodeURI进行反编码
		 */
		decodeURI : function(url) {
			if (url) {
				return decodeURI(url);
			}
		},
		/**
		 * 添加遮罩效果
		 * 
		 * @argument msgtext 提示信息
		 * @argument time 显示时间 单位秒 默认15s
		 * @argument 显示页面
		 */
		mask : function(msgtext, time, page) {
			//alert("begin mask!");
			if (!msgtext) {
				msgtext = "正在操作,请稍候……";
			}
			if (!page){
				if(window.top)
					page = window.top;
				else
					return;
			}
			var maskTagert = page.document.getElementById("maskTagert");
			if(!maskTagert)
				return;
			var width = maskTagert.offsetWidth;
			var height = maskTagert.offsetHeight;
			var wait = page.document.getElementById("empMask");
			if (!wait) {
				wait = page.document.createElement("DIV");
				wait.style.top = 95;
				wait.style.left = 0;
				wait.style.bottom = 10;
				wait.style.width = "100%";
				wait.style.height = height;
				wait.style.display = "block";
				wait.id = "empMask";
				var msg = page.document.createElement("DIV");
				msg.id = "maskMsg";
				msg.style.top = height / 2;
				msg.style.left = width / 2;
				msg.className = "ext-el-mask-msg";
				msg.innerHTML = "<div style='color:blue;'><img src='images/wait.gif' style='vertical-align:middle;filter:alpha(opacity=100);'>"
						+ msgtext + "</div>";
				wait.className = "ext-el-mask";
				page.document.body.appendChild(msg);
				page.document.body.appendChild(wait);
			} else {
				wait.style.display = "block";
				wait.style.width = "100%";
				wait.style.height = height;
				wait.style.bottom = 10;
				var msg = page.document.getElementById("maskMsg");
				msg.innerHTML = "<div style='color:blue;'><img src='images/wait.gif' style='vertical-align:middle;filter:alpha(opacity=100);'>"
						+ msgtext + "</div>";
				msg.style.top = height / 2;
				msg.style.left = width / 2;
				msg.style.display = "block";
			}
			if (!time)
				time = 15 * 1000;
			//设置超时时间
			setTimeout("EMPTools.unmask();", time);
		},
		/**
		 * 移除遮罩
		 * 
		 * @argument page 操作页面
		 */
		unmask : function(page) {
			if (!page){
				if(window.top)
					page = window.top;
				else
					page=window;
			}
			var wait = page.document.getElementById("empMask");
			var msg = page.document.getElementById("maskMsg");
			if (wait)
				wait.style.display = "none";
			if (msg)
				msg.style.display = "none";
		},
		/**
		 * 在主页面上显示请稍候的标识
		 */
		setWait : function(windowObj) {
			if (!windowObj)
				windowObj = window.top;
			var wait = windowObj.document.getElementById("pleaseWaitDiv");
			if (!wait) {
				wait = windowObj.document.createElement("DIV");
				wait.id = "pleaseWaitDiv";
				wait.innerHTML = "<img src='images/wait.gif' style='vertical-align:middle;'>请稍候……";
				wait.style.position = "absolute";
				wait.style.top = "380px";
				wait.style.left = "520px";
				wait.style.backgroundColor = "#FFFFFF";
				wait.style.border = "solid 1px #000000";
				wait.style.padding = "10px 20px";
				wait.style.fontSize = "12px";
				windowObj.document.body.appendChild(wait);
			} else {
				wait.style.display = "block";
			}
		},

		/**
		 * 消除在主页面上显示的请稍候的标识
		 */
		removeWait : function(windowObj) {
			if (!windowObj)
				windowObj = window.top;
			var wait = windowObj.document.getElementById("pleaseWaitDiv");
			if (wait)
				wait.style.display = "none";
		},

		/**
		 * 表单做异步提交修改,例EMPTools.doAjaxUpdate('submitForm',CtrMrge1st,'updateCtrMrge1stRecord.do');
		 * 
		 * @formid 表单id，在这里用HTML的formid
		 * @kColl 页面的表单项的kColl
		 * @url 提交的action Url
		 * @check 需要在表单验证后进行的校验
		 */
		doAjaxUpdate : function(formid, kColl, url, check) {
			if (!kColl._checkAll()) {
				alert('表单信息还不完整，请补充');
				return;
			}

			if (check == null || check == undefined
					|| (typeof(check) == 'function' && check())) {
				var form = document.getElementById(formid);
				kColl._toForm(form);

				var callback = {
					success : function(returnMsg) {
						if (returnMsg == '0') {
							alert('保存成功');
						} else {
							alert(returnMsg);
						}
					},
					isJSON : true,
					form : form,
					handleError : true
				};

				EMPTools.ajaxRequest('POST', url, callback);
			}
		},
		
		/**
		 * 表单做异步提交修改,并调用回调函数（成功条件下）
		 * 
		 * @formid 表单id，在这里用HTML的formid
		 * @kColl 页面的表单项的kColl
		 * @url 提交的action Url
		 * @check 需要在表单验证后进行的校验
		 */
		doAjaxUpdateAndBack : function(formid, kColl, url, check,backFun) {
			if (!kColl._checkAll()) {
				alert('表单信息还不完整，请补充');
				return;
			}

			if (check == null || check == undefined
					|| (typeof(check) == 'function' && check())) {
				var form = document.getElementById(formid);
				kColl._toForm(form);

				var callback = {
					success : function(returnMsg) {
						if (returnMsg == '0') {
							backFun();
						} else {
							alert(returnMsg);
						}
					},
					isJSON : true,
					form : form,
					handleError : true
				};

				EMPTools.ajaxRequest('POST', url, callback);
			}
		},

		/**
		 * 发起ajax请求
		 * 
		 * @method ：请求的方法，主要是get或者是post两种
		 * @url ：请求的url(已经经过EMPTools.encodeURI之后的url，在该方法中不对url进行处理)
		 * @callback ：异步请求所必要的一些信息，主要有如下几个： var callback = { success
		 *           ：正常返回所要调用的方法，该方法有两个参数，一个是返回值；另一个是callback对象
		 *           callback既包含了当前对象中的内容，还包含了返回值的一些信息 handleError
		 *           ：是否自行处理异常信息，缺省不需要。异常信息指的是在返回页面中包含 <span
		 *           class='errorPage_errorContent'>字样的信息 isJSON
		 *           ：异步请求返回的是否是json字符串，如果是则转化成对象提供给success方法 errorMessage
		 *           ：出现异常时，异常的内容(该内容是在success方法中使用，不是用于设置的) }
		 */
		ajaxRequest : function(method, url, callback) {
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					var responseText = o.responseText;
					var callbackObj = o.argument.callback;

					// 是否自行处理异常信息(在未定义情况下，默认是由该方法进行处理)
					var handleError = callbackObj.handleError;
					if (handleError == null) {
						handleError = false;
					}

					// 对异常信息进行处理，如果页面中包括<span
					// class='errorPage_errorContent'>，则说明是错误页面
					var errorSpan = "<span class='errorPage_errorContent'>";
					var idx = responseText.indexOf(errorSpan);
					if (idx != -1) {
						var end_idx = responseText.indexOf("</span>", idx);
						if (end_idx == -1) {
							alert("错误页面编辑不完整!");
							return;
						}
						var errorContent = responseText.substring(idx
										+ errorSpan.length, end_idx);
						if (handleError == true) {
							callbackObj.errorMessage = errorContent;
							callbackObj.isJSON = false;
						} else {
							alert(errorContent);
							return;
						}
					}

					// 正常情况下，调用相关的返回方法进行处理
					var successFunc = callbackObj.success;
					if (successFunc) {
						var isJSON = callbackObj.isJSON;
						if (isJSON == null) { // 在未定义isJSON的情况下，默认返回的是JSON字符串
							isJSON = true;
						}
						if (isJSON == true) { // 如果是JSON字符串，则提前将字符串转换成JS对象
							try {
								var text = eval("(" + responseText + ")");
								responseText = text;
							} catch (e) {
								callbackObj.isJSON = false;
							}
						}

						try {
							if (typeof(successFunc) == 'string') {
								window[successFunc](responseText, callbackObj);
							} else {
								successFunc(responseText, callbackObj);
							}
						} catch (e) {
							alert("返回调用出错!" + e.message);
						}
					}
				}
			};

			var handleFailure = function(o) {
				var responseText = o.responseText;
				alert("网络异常：" + responseText);
			};

			var requestObj = {
				success : handleSuccess,
				failure : handleFailure,
				argument : {
					callback : callback
				}
			};

			var form = callback.form;
			var formdata = callback.formdata;
			if (form || formdata) {
				var data = formdata;
				if (form) {
					data = YAHOO.util.Connect.setForm(form);
				}
				var obj1 = YAHOO.util.Connect.asyncRequest(method, url,
						requestObj, data);
			} else {
				var obj2 = YAHOO.util.Connect.asyncRequest(method, url,
						requestObj);
			}
		},

		/**
		 * 前端页面最外围的框架
		 */
		topPage : window.top,

		/**
		 * 是否使用模式窗口(只在IE中起作用)
		 */
		useModelDialog : false,

		/**
		 * 分页条数最大设置
		 */
		PageQueryMaxLine : 50,

		/**
		 * 是否需要校验用户权限(如果不需要，则代表所有的按钮都拥有权限)
		 */
		haveToCheckButtonPermission : true,

		/**
		 * 对普通页面是否通过JS重新布局
		 */
		reGridLayout : true,

		Browser : {
			'name' : 'unknown',
			'version' : ''
		},
		Features : {},
		/** 实现Map结构(排序使用Pansq) */
		Map : function (){
		　　this.elements=new Array();
		　　this.size=function(){
		　　		return this.elements.length;
		　　}
		　　this.put=function(_key,_value){
		　　		this.elements.push({key:_key,value:_value});
		　　}
		　　this.remove=function(_key){
			　　var bln=false;
			　　try{
				　　for (i=0;i<this.elements.length;i++){
					　　if (this.elements[i].key==_key){
						　　this.elements.splice(i,1);
						　　return true;
				　　		}
				　　}
			　　}catch(e){
			　　		bln=false;
			　　}
		　　		return bln;
		　　}
		　　this.containsKey=function(_key){
		　　		var bln=false;
			　　try{
				　　for (i=0;i<this.elements.length;i++){ 
					　　if (this.elements[i].key==_key){
					　　		bln=true;
					　　}
				　　}
			　　}catch(e){
			　　		bln=false;
			　　}
			　　return bln;
		　　}
		　　
		　　this.get=function(_key){
			　　try{
				　　for (i=0;i<this.elements.length;i++){
					　　if (this.elements[i].key==_key){
					　　		return this.elements[i].value;
					　　}
				　　}
			　　}catch(e){
			　　		return null;
			　　}
		　　}
			this.keySet=function(){
				try{
					var _keys = new Array();
				　　for (i=0;i<this.elements.length;i++){
					　　_keys[i] = this.elements[i].key;
				　　}
					return _keys;
			　　}catch(e){
			　　		return null;
			　　}
			}
		}
	};

	EMP.util.Tools.Features.xhr = !!(window.XMLHttpRequest);
	EMP.util.Tools.Features.xpath = !!(document.evaluate);

	if (window.opera)
		EMP.util.Tools.Browser.name = 'opera';
	else if (window.ActiveXObject)
		EMP.util.Tools.Browser = {
			'name' : 'ie',
			'version' : (EMP.util.Tools.Features.xhr) ? 7 : 6
		};
	else if (!navigator.taintEnabled)
		EMP.util.Tools.Browser = {
			'name' : 'webkit',
			'version' : (EMP.util.Tools.Features.xpath) ? 420 : 419
		};
	else if (document.getBoxObjectFor != null)
		EMP.util.Tools.Browser.name = 'gecko';
	EMP.util.Tools.Browser[EMP.util.Tools.Browser.name] = EMP.util.Tools.Browser[EMP.util.Tools.Browser.name
			+ EMP.util.Tools.Browser.version] = true;

	/**
	 * 找到前端页面最外围的框架 */ 

	while (EMP.util.Tools.topPage) {
		var openWindow = EMP.util.Tools.getWindowOpener(EMP.util.Tools.topPage);
		if (openWindow) {
			EMP.util.Tools.topPage = openWindow.top;
		} else {
			// alert('未找到主布局页面！');
			break;
		}
	}
	// 页面载入后，去掉"请稍候"的标识(若当前是弹出窗口，则同时去掉父窗口的"请稍候"标识)
	EMP.util.Tools.removeWait();
	if (EMP.util.Tools.getWindowOpener()) {
		EMP.util.Tools.removeWait(EMP.util.Tools.getWindowOpener());
	}
};

var EMPTools = EMP.util.Tools;
/** 初始化数据列表字段类型的Map */
var EMPDataTableMap = new EMPTools.Map();
/** 初始化用于存储每个排序对象的Map */
var EMPDataTableSortMap = new EMPTools.Map();

if (!EMP.util.Page) {
	EMP.util.Page = function() {

	};
	EMP.util.Page.prototype.hide = function() {
		try {
			var html = document.getElementsByTagName('HTML')[0];
			html.style.display = 'none';
			// if (!this.opener)
			// topPage.setWait();
		} catch (e) {
			alert(e)
		}
	};

	EMP.util.Page.prototype.show = function() {
		try {
			var html = document.getElementsByTagName('HTML')[0];
			html.style.display = '';
			// if (!this.opener)
			// topPage.removeWait();
		} catch (e) {
			alert(e)
		}
	};

	EMP.util.Page.prototype.contentUpdated = function() {
		try {
			if (window.frameElement != null) {
				window.frameElement.fireEvent('onresize');
			}
		} catch (e) {
		}
	};

	EMP.util.Page.prototype.renderEmpObjects = function() {
		EMP.util.Tools.log('EMP.util.Page', 0,'====================================================================');
		EMP.util.Tools.log('EMP.util.Page', 0, '开始解析页面' + window.location+ '...');
		this.hide();// 在解析页面时先将页面内容隐藏
		try {
			var objectsDefine = page.objectsDefine;
			if (!objectsDefine)
				return;
			var beginTime = new Date();

			page.dataGroups = {};
			page.dataGroups._default = new EMP.widget.DataGroup();

			page.dataDics = objectsDefine.dataDics;

			// 处理单个数据对象
			var dataFields = objectsDefine.dataFields;
			this._renderDataFields(dataFields);
			// 处理列表对象
			var dataTables = objectsDefine.dataTables;
			this._renderDataTables(dataTables);
			// 处理标签页对象
			var relatedTabs = objectsDefine.relatedTabs;
			this._renderRelatedTabs(relatedTabs);
			// 处理联动下拉框对象
			var relatedSelects = objectsDefine.relatedSelects;
			this._renderRelatedSelects(relatedSelects);

			var endTime = new Date();
			EMP.util.Tools.log('EMP.util.Page', 0, '解析页面完成，共使用'
							+ (endTime - beginTime) + '毫秒.');

			this.renderButtons();
		} catch (e) {
			this.show();// 解析页面出错时，要将隐藏的页面显示出来以方便开发过程中的调试
			EMP.util.Tools.log('EMP.util.Page', 0, '解析页面出错!', e);
		}

		this.show();// 解析页面成功后，显示整个页面
	};

	EMP.util.Page.prototype._addToDataGroup = function(tag, obj) {

		while (tag.parentNode != null) {
			tag = tag.parentNode;
			if (tag.tagName == "DIV" && tag.className == "emp_group_div") {
				var id = tag.id;
				if (!page.dataGroups[id]) {
					page.dataGroups[id] = new EMP.widget.DataGroup(tag);
				}
				page.dataGroups[id].push(obj);
			}
		}
	};

	EMP.util.Page.prototype._renderDataFields = function(dataFields) {
		for (var i = 0; i < dataFields.length; i++) {
			EMP.util.Tools.log('EMP.util.Page', 0, '开始处理数据域[' + dataFields[i]
							+ ']...');
			var tag = document.getElementById("emp_field_" + dataFields[i]);

			if (tag != null && tag.getAttribute("rendered") == "false") { // 检查是否合法？
				EMP.util.Tools.log('EMP.util.Page', 0, '找到数据域[' + dataFields[i]
								+ ']所对应的注册标签.');

				var obj = EMP.util.Page.instField(tag);
				if (obj != null) {
					this.addData(window, "dataField", obj.dataName, obj);

					page.dataGroups._default.push(obj);
					this._addToDataGroup(obj.tag, obj);
				}
				tag.setAttribute("rendered", "true");
			}
		}
	};

	EMP.util.Page.prototype._renderDataTables = function(dataTables) {
		for (var i = 0; i < dataTables.length; i++) {
			EMP.util.Tools.log('EMP.util.Page', 0, '开始处理数据列表[' + dataTables[i]+ ']...');
			var tag = document.getElementById('emp_table_' + dataTables[i]+ '_table');
			if (tag != null && tag.getAttribute("rendered") == "false") { // 检查是否合法？
				EMP.util.Tools.log('EMP.util.Page', 0, '找到数据列表['+ dataTables[i] + ']所对应的注册标签，进行实例化...');
				try {
					var obj = new EMP.widget.DataTable();
					tag.empObj = obj;
					obj._parseParams(tag);
					obj.dataName = dataTables[i];
				} catch (e) {
					EMP.util.Tools.log('EMP.util.Page', 4, '实例化数据列表['+ dataTables[i] + ']时发生错误！' + e.message, e);
					obj = null;
				}
				if (obj != null) {
					var iColl = this.addData(window, "iColl", obj.dataName, obj);
					page.dataGroups._default.push(obj);
					this._addToDataGroup(obj.tag, obj);
					obj._regist();
				}
				tag.setAttribute("rendered", "true");
			}
			/** 初始化DataTable同时为每个DataTable实例化一个排序对象(clear:20130927) */
			var _types = EMPDataTableMap.get(dataTables[i]);
			if(_types != null && _types != undefined && _types != ''){
				var _sorObj = new obj.Gird(document.getElementById('emp_table_'+dataTables[i]+'_table'),_types);
				EMPDataTableSortMap.put('emp_table_'+dataTables[i]+'_table',_sorObj);
			}
		}
	};

	EMP.util.Page.instField = function(tag, isTable) {

		var fieldType = tag.getAttribute("type");
		if (EMP.field[fieldType] == null) {
			EMP.util.Tools.log('EMP.util.Page', 4, '没有类型为[EMP.field.'
							+ fieldType + ']的数据域实现！');
			return null;
		}

		try {
			var obj = new EMP.field[fieldType]();
			tag.empObj = obj;
			obj.container = new EMP.widget.DataFieldContainer(obj, isTable);
			obj._parseParams(tag);

			obj.dataName = tag.getAttribute("id").substring(10);// "emp_field_".length，将id中的emp_field_移除

			return obj;
		} catch (e) {
			EMP.util.Tools.log('EMP.util.Page', 4, '实例化数据域['
							+ tag.getAttribute("dataName") + ']时发生错误！'
							+ e.message, e);
			return null;
		}
	};

	EMP.util.Page.prototype.addData = function(parent, type, dataName, obj) {
		var names = dataName.split(".");
		var path = "";
		for (var i = 0; i < names.length - 1; i++) {
			path += names[i];
			if (parent[names[i]] == null) {
				var newKColl = new EMP.data.KColl();
				parent[names[i]] = newKColl;
				parent = newKColl;
			} else {
				if (parent[names[i]]._isKColl) {
					parent = parent[names[i]];
				} else {
					EMP.util.Tools.log('EMP.util.Page', 4, '已存在数据对象[' + path
									+ ']，并且它不是KColl，无法往其中添加数据域！');
				}
			}
			path += ".";
		}
		if (type == "dataField") {
			var dataField = new EMP.data.Field(obj);
			parent[names[names.length - 1]] = dataField;
			return dataField;
		} else if (type == "iColl") {
			var iColl = new EMP.data.IColl(obj);
			parent[names[names.length - 1]] = iColl;
			return iColl;
		}
	};

	/**
	 * 根据权限，决定页面中按钮的显示
	 */
	EMP.util.Page.prototype.renderButtons = function() {

		var buttonpermission = EMPTools.getWorkFlowPermission();
		var buttonList = document.getElementsByTagName("BUTTON");
		for (var i = 0; i < buttonList.length; i++) {
			var buttonObj = buttonList[i];
			var op = buttonObj.getAttribute("op");
			if (!op)
				continue;
			if (!EMPTools.haveToCheckButtonPermission) {
				buttonObj.style.display = "";
				continue;
			}
			if (buttonpermission && buttonpermission[op]) {// 优先检查是否属于工作流权限控制范围
				buttonObj.style.display = "";
			} else if (EMPTools.topPage.checkPermission) {
				// 如果页面强制决定了menuId，则由页面定义的menuId决定权限，否则由菜单上的menuId决定
				// (便于极其特殊的情况：一个页面在不同模块中要求拥有不同的权限)
				// alert(this.menuId+";"+op);
				var check = EMPTools.topPage.checkPermission(this.menuId, op);
				if (check)
					buttonObj.style.display = "";
			}
		}
	};

	/**
	 * 解析标签页
	 */
	EMP.util.Page.prototype._renderRelatedTabs = function(relatedTabs) {
		for (var i = 0; i < relatedTabs.length; i++) {
			EMP.util.Tools.log('EMP.util.Page', 0, '开始处理标签页[' + relatedTabs[i]
							+ ']...');
			var dataName = relatedTabs[i];
			var idx = dataName.indexOf(".");
			var tabGroupName = dataName.substring(0, idx);
			var tabName = dataName.substring(idx + 1);
			var tabGroup = window[tabGroupName];
			if (!tabGroup) {
				var tag = document.getElementById('relatedtabs_' + tabGroupName
						+ '_tabs');
				if (!tag)
					continue;

				tabGroup = new EMP.widget.RelatedTabGroup(tabGroupName);

				tabGroup._parseParams(tag);
				window[tabGroupName] = tabGroup;
			}
			tabGroup._addTab(tabName);
		}
	};

	/**
	 * 解析联动下拉框
	 */
	EMP.util.Page.prototype._renderRelatedSelects = function(
			relatedSelectGroups) {
		if (relatedSelectGroups == null)
			return;
		for (var i = 0; i < relatedSelectGroups.length; i++) {
			var group = relatedSelectGroups[i];
			var dataName = group.id;

			EMP.util.Tools.log('EMP.util.Page', 0, '开始处理[' + dataName
							+ ']联动下拉框分组...');

			var selects = group.selects;
			var selectGroup = window[dataName];
			if (!selectGroup) {
				selectGroup = new EMP.widget.RelatedSelectGroup(dataName,
						group.url, group.needCache);
				window[dataName] = selectGroup;
			}
			for (var j = 0; j < selects.length; j++) {
				var selectName = selects[j];
				EMP.util.Tools.log('EMP.util.Page', 0, '将[' + selectName
								+ ']注册到[' + dataName + ']联动下拉框分组中...');
				selectGroup.registRelatedSelect(selectName);
			}
			selectGroup.doInitSelectContent(selectGroup.firstSelect);

			EMP.util.Tools.log('EMP.util.Page', 0, '联动下拉框分组[' + dataName
							+ ']处理完成!');
		}
	};
}
