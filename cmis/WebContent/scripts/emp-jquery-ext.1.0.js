
/**
 * 组件基类
 */
(function($) {
	$.emp = $.emp || {};
	$.emp.ext = function() {
	};
	$.emp.createObject = function(object, el, options) {
		object.element = el;
		object.options = options;
		object.create();
	};
	$.emp.maxId = 0;
	$.extend($.emp.ext.prototype, {
				uiComplete : true,
				readOnly : false,
				/**
				 * 对象构造 并生成id
				 */
				create : function() {
					if (typeof(this.options.readOnly) != "undefined") {
						this.readOnly = this.options.readOnly;
					}
					if (this.element) {
						this.options = (!this.options) ? {} : this.options;
						var $element = $(this.element);
						var id = this.options.id;
						if (!id) {
							$.emp.maxId++;
							id = "emp-comp-" + $.emp.maxId;
							if (!$element.attr("id"))
								$element.attr("id", id);
							this.options.id = id;
						}
						this.createElement();
						/**
						 * ui构建完成
						 */
						this.init();

					}
				},
				onRender : function() {
				},
				/**
				 * 参数构造函数
				 */
				init : function() {
				},
				/**
				 * 析构函数
				 */
				destroy : function() {
				},
				/**
				 * 组件UI 构造函数
				 */
				createElement : function() {
					this.uiComplete = true;
				},
				equals : function(o) {
					return (o != null && o.element === this.element);
				},
				getId : function() {
					return $(element).attr("id");
				},
				getSerise : function() {
					++$.emp.maxId;
					return $.emp.maxId;
				}
			});
})($);

(function($) {
	/**
	 * 条件构造器
	 */
	$.emp.ShfCondition = function(el, o) {
		$.emp.createObject(this, el, o);
	};
	$.extend($.emp.ShfCondition.prototype, $.emp.ext.prototype, {
		swapClass : "swapClass",
		/**
		 * 外包元素tr
		 */
		wrapEl : null,
		/**
		 * 创建外包容器
		 */
		createWrapEl : function(label) {
			var id = "wrap_" + this.getSerise();
			var wrap = document.createElement("tr");
			$(wrap).attr("id", id);
			$(wrap).addClass(this.swapClass);
			$(this.element).append(wrap);
			if (label) {
				var td = document.createElement("td");
				td.style.padding = "0 0 0 73px";
				$(td).text(label);
				$(wrap).append(td);
			}
			return $(wrap);
		},
		selectEl : null,
		/**
		 * 创建选择元素
		 */
		createSelectEl : function() {
			if (this.wrapEl) {
				var td = document.createElement("td");
				this.wrapEl.append(td);
				var select = document.createElement("select");
				select.style.width = "150px";
				$(td).append(select);
				var option = document.createElement("option");
				option.selected = true;
				option.innerHTML = "----请选择业务分类----"
				$(select).html(option);
				return $(select);
			} else {
				return null;
			}
		},
		unitEl : null,
		/**
		 * 创建关系符号元素
		 */
		createUnitEl : function() {
			if (this.wrapEl) {
				var td = document.createElement("td");
				this.wrapEl.append(td);
				var select = document.createElement("select");
				select.style.width = "80px";
				$(td).append(select);
				return $(select);
			} else {
				return null;
			}
		},
		inputEl : null,
		inputType : "text",
		inputWrap : null,
		/** 创建输入 */
		createInputEl : function() {
			if (this.wrapEl) {
				var td = document.createElement("td");
				this.wrapEl.append(td);
				this.inputWrap = $(td);
				return this.createInputText();
			} else {
				return null;
			}
		},
		/**
		 * 条件输入文本形式
		 */
		createInputText : function() {
			var input = document.createElement("input");
			input.style.width = "150px";
			input.type = "text";
			input.disabled = this.readOnly;
			this.inputWrap.append(input);
			this.inputType = "text";
			var change = function(e) {
				var key = $(e.target).val().toString(), nkey = "";
				for (var i = 0; i < key.length; i++) {
					var lastInput = key.charAt(i);
					var flag = /\d|\./.test(lastInput);
					if (flag) {
						nkey += lastInput;
					}
				}
				$(e.target).val(nkey);
			}
			$(input).keyup(change);
			$(input).mouseup(change);
			return $(input);
		},
		/**
		 * 创建关联字段
		 */
		createLinkInput:function(){
			var o=this;
			var showInput = document.createElement("input");
			showInput.style.width = "150px";
			showInput.type = "text";
			showInput.readOnly = true;			
			showInput.value = o.linkName;
			this.inputWrap.append(showInput);
			var hidenInput = document.createElement("input");
			hidenInput.style.width = "0px";
			hidenInput.type = "hidden";			
			hidenInput.value = o.linkKey;
			o.inputType = "text";
			this.inputWrap.append(hidenInput);
			return $(hidenInput);
		},
		/**
		 * 创建字典项选择形式
		 */
		createInputChoose : function() {
			var select = document.createElement("select");
			select.style.width = "150px";
			this.inputWrap.append(select);
			this.inputType = "select";
			select.disabled = this.readOnly;
			return $(select);
		},
		/**
		 * 异步请求字典项
		 */
		ajaxGetDicList : function(url, callback) {
			this.inputEl.val();
			var o = this;
//			console.log(this.sql);
			$.getJSON(url + this.dicType, {sql: this.sql}, function(json) {
						var str = ""
						for (var i in json) {
							var iter = json[i];
							str += "<option value='" + iter.dicValue + "'>"
									+ iter.dicName + "</option>";
						}
						o.inputEl.html(str);
						if (typeof(callback) == "function")
							callback.call(o);
					});
		},
		buttonEl : null,
		/**
		 * 创建删除按钮
		 */
		createButtonEl : function() {
			if (this.wrapEl) {
				var td = document.createElement("td");
				this.wrapEl.append(td);
				var button = document.createElement("button");
				button.style.width = "80px";
				button.innerHTML = "删除";
				$(button).addClass("btn_mouseout");
				$(td).append(button);
				return $(button);
			} else {
				return null;
			}
		},
		errorEl : null,
		/** 创建错误提示EL */
		createErrorEl : function() {
			if (this.wrapEl) {
				var td = document.createElement("td");
				td.style.width = "150px";
				this.wrapEl.append(td);
				var label = document.createElement("label");
				label.style.color = "red";
				label.innerHTML = "*";
				$(td).append(label);
				return $(label);
			} else {
				return null;
			}
		},
		label : "规则条件",
		unitArray : [{
					name : "等于",
					value : "=="
				}, {
					name : "大于",
					value : ">"
				}, {
					name : "大于等于",
					value : ">="
				}, {
					name : "小于",
					value : "<"
				}, {
					name : "小于等于",
					value : "<="
				}, {
					name : "不等于",
					value : "!="
				}],
		/**
		 * 关系条件控制
		 */
		unitOption : function(type) {
			type = type.toString().toLowerCase();
			var select = [];
			switch (type) {
				case "choose" :
				case "string" :
				case "select" :
					select.push(0);
					select.push(5);
					break;
				case "undefined" :
				case "number" :
				default :
					select.push(0);
					select.push(1);
					select.push(2);
					select.push(3);
					select.push(4);
					select.push(5);
					break;
			}
			var ret = "";
			for (var i in select) {
				var o = this.unitArray[select[i]];
				ret += "<option value='" + o.value + "'>" + o.name
						+ "</option>";
			}
			return ret;
		},
		type : "number",
		dicType : "",
		selectOptions : null,
		json : null,
		sql:"",
		checkInputEl : function(callback) {
			var url = "shf/dicType=";		
			var o = this;
			var v = o.selectEl.find("option:selected").get(0);
			o.type = v.getAttribute("type");
			o.dicType = v.getAttribute("dicType");
			o.linkKey=v.getAttribute("linkKey");
			o.linkName=v.getAttribute("linkName");
			o.sql=v.getAttribute("sql");	
//			console.log(o.sql);
			if(o.sql!=""){
				o.dicType="sql";
				url="shf/sql=";	
			}
			if (o.dicType != "") {				
				if (o.inputType == "text") {
					o.inputWrap.children().remove();
					o.inputEl = o.createInputChoose.call(o);
				}
				o.ajaxGetDicList(url, callback);
			} else {				
				if (o.inputType == "select") {	
					o.inputWrap.children().remove();
					if(o.linkKey!=""){		
						o.inputEl = o.createLinkInput.call(o);			
					}else
						o.inputEl = o.createInputText.call(o);
				}else{
					if(o.linkKey!=""){		
						o.inputWrap.children().remove();
						o.inputEl = o.createLinkInput.call(o);		
					}else{
						o.inputWrap.children().remove();
						o.inputEl = o.createInputText.call(o);
					}
						
				}
				if (typeof(callback) == "function")
					callback.call(o);
			}
			if (typeof(callback) == "undefined")
				o.unitEl.html(o.unitOption(o.type));
		},
		pr : null,
		/**
		 * 参数初始化
		 */
		init : function() {
			var o = this;
			var url = "shf/dicType=";
			this.unitEl.html(this.unitOption(this.type));
			if (!this.readOnly) {
				this.selectEl.change(function() {
							o.checkInputEl.call(o);
						});
			}

			this.pr = this.options.pr;
			this.selectOptions = this.options.selectOptions;
			if (this.selectOptions.length > 0) {
				var str = "";
				for (var c in this.selectOptions) {
					var obj = this.selectOptions[c];
					var isShow = obj.isShow;
					if (isShow) {
						str += "<option value='" + obj.key + "' type='"
								+ obj.dataType + "' dicType='" + obj.dicType
								+ "'  linkKey='" + obj.linkKey
								+ "'  linkName='" + obj.linkName + "' sql=\""+obj.sql+"\">"
								+ obj.name + "</option>";
					}
				}
				this.selectEl.html(str);
				o.checkInputEl.call(o);
			} else {
				this.selectEl.html("<option value=''>----请选择业务分类----</option>");
			}
			if (!this.readOnly)
				this.buttonEl.click(function() {
							var count = new Number(o.pr._getValue());
							o.pr._setValue(count - 1);
							o.destroy.apply(o);
						});
			if (!this.readOnly)
				this.inputEl.keyup(function() {
							o.errorReset();
						});
			if (this.options.json != null) {
				this.json = this.options.json;
				this.setValue(this.json);
			}
		},
		setValue : function(json) {
			var o = this;
			var selectVal = json.key;
			var unitVal = json.operator;
			var value = json.value.replace(/"/ig, '');
			this.setSelectValue(selectVal);
			this.setUnitValue(unitVal);
			this.checkInputEl(function() {
						o.setInputValue.call(o, value);
					});
		},
		/** 设置条件 */
		setSelectValue : function(value) {
			var list = this.selectEl.children();
			for (var i = 0; i < list.length; i++) {
				var o = $(list[i]);
				if (o.val() == value) {
//					o.attr("selected", true);
					o[0].selected=true;
					this.type = $(o).attr("type").toString().toLowerCase();
				} else {
//					o.attr("selected", false);
					o[0].selected=false;
				}
			}
		},
		/** 设置条件关系* */
		setUnitValue : function(value) {
			// console.log(this.type);
			this.unitEl.html(this.unitOption(this.type));
			var list = this.unitEl.children();
			for (var i = 0; i < list.length; i++) {
				var o = $(list[i]);
				if (o.val() == value) {
					o[0].selected=true;
				} else {
					o[0].selected=false;
				}
			}
		},
		setInputValue : function(value) {
			if (this.inputType == "select") {
				var list = this.inputEl.children();
				var html="";
				for (var i = 0; i < list.length; i++) {
					var o = $(list[i]);				
					if (o.val() == value) {
						html+="<option value='"+o.val()+"' selected>"+o.text()+"</option>";
//						o[0].selected=true;
					} else {
						html+="<option value='"+o.val()+"'>"+o.text()+"</option>";
//						o[0].selected=false;
					}
				}
				this.inputEl.html(html);
			} else {
			//	console.log(value);
				this.inputEl.val(value);
			}
		},
		errorReset : function() {
			this.errorEl.text("*");
		},
		status : true,
		getStatus : function() {
			return this.status;
		},
		destroy : function() {
			this.wrapEl.remove();
			this.status = false;
		},
		configValue : function() {
			var flag = true;
			if (!this.selectEl.val()) {
				this.errorEl.text("请选择规则条件...");
				flag = false;
			} else if (!this.inputEl.val()) {
				this.errorEl.text("请输入规则条件...");
				flag = false;
			} else {
				this.errorReset();
			}
			return flag;
		},

		toShfObject : function() {
			var retObj;
			if (this.configValue()) {
				retObj = {};
				var value = this.inputEl.val();
				if (this.type.toString().toLowerCase() != "number")
					value = "\"" + value + "\"";
				retObj.value = value;
				retObj.key = this.selectEl.val();
				retObj.operator = this.unitEl.val();
			}
			return retObj;
		},
		/**
		 * UI构造函数
		 */
		createElement : function() {
			this.status = true;
			this.wrapEl = this.createWrapEl(this.label);
			this.selectEl = this.createSelectEl();
			this.selectEl.attr("disabled", this.readOnly);
			this.unitEl = this.createUnitEl();
			this.unitEl.attr("disabled", this.readOnly);
			this.inputEl = this.createInputEl();
			this.inputEl.attr("disabled", this.readOnly);
			if (!this.readOnly)
				this.buttonEl = this.createButtonEl();
			if (!this.readOnly)
				this.errorEl = this.createErrorEl();
		}
	});
})($);
/**
 * 条件构造器容器
 */
(function($) {
	$.emp.ShfCondition.Container = function(el, o) {
		$.emp.createObject(this, el, o);
	};
	$.extend($.emp.ShfCondition.Container.prototype, $.emp.ext.prototype, {
				swapClass : "",
				response : null,
				ajaxSelect : function(url) {
					var o = this;
					$.getJSON(url, {}, function(json) {
								o.response = json;
								o.dealWithJson(json);
								o.cleanCondition.call(o);

							});
				},
				buildCondition : function() {
					var json = $.evalJSON(this.jsonStr);
					for (var i = 0; i < json.length; i++) {
						this.addNewCondition(json[i]);
					}
				},
				dealWithJson : function(json) {
					this.selectOptions = [];
					for (var c in json) {
						var o = eval("json." + c);
						this.selectOptions.push(o);
					}
					// o.selectOptions=json;
				},
				cleanCondition : function() {
					for (var obj in this.shfArray) {
						this.shfArray[obj].destroy();
					}
					this.shfArray = [];
					if (this.jsonStr != "") {
						this.buildCondition.call(this);
					} else {
						this.addNewCondition();
					}

				},
				tableEl : null,
				createTableEl : function() {
					var id = "shf-container-" + this.getSerise();
					var wrap = document.createElement("table");
					$(wrap).attr("id", id);
					$(wrap).addClass(this.swapClass);
					$(this.element).append(wrap);
					return $(wrap);
				},
				tbodyEl : null,
				createTbodyEl : function() {
					if (this.tableEl) {
						var tbody = document.createElement("tbody");
						this.tableEl.append(tbody);
						return $(tbody);
					} else {
						return null;
					}
				},
				buttonEl : null,
				createButtonEl : function() {
					if (this.tableEl) {
						var tfoot = document.createElement("tfoot");
						var tr = document.createElement("tr");
						var td = document.createElement("td");
						tfoot.style.height = "30px";
						this.tableEl.append(tfoot);
						$(tfoot).append(tr);
						$(tr).append(td);
						var button = document.createElement("button");
						button.style.width = "80px";
						button.innerHTML = "添加新条件";
						$(button).addClass("btn_mouseout");
						$(td).append(button);
						return $(button);
					} else {
						return null;
					}
				},
				selectOptions : [],
				shfArray : [],
				addNewCondition : function(json) {
					var o = {
						selectOptions : this.selectOptions,
						json : json,
						readOnly : this.readOnly,
						pr : this.pr
					};
					var shf = new $.emp.ShfCondition(this.tbodyEl, o);
					this.shfArray.push(shf);
				},
				// 优先级对象
				pr : null,
				jsonStr : "",
				init : function() {
					var target = this.options.target;
					if (this.options.pro)
						this.pr = this.options.pro;
					var url = this.options.url;
					var o = this;
					this.addNewCondition();
					try {
						if (typeof(target) == "object") {
							target.change(function() {
										o.pr._setValue(1);
										o.ajaxSelect
												.call(o, url + target.val());
									});
						} else {
							this.jsonStr = this.options.jsonStr;
							o.ajaxSelect.call(o, url + target);

						}
					} catch (e) {
						alert(e);
					}
					if (!this.readOnly)
						this.buttonEl.click(function() {
									var count = new Number(o.pr._getValue());
									o.pr._setValue(count + 1);
									o.addNewCondition();
								});

				},
				createElement : function() {
					this.tableEl = this.createTableEl();
					this.tbodyEl = this.createTbodyEl();
					if (!this.readOnly)
						this.buttonEl = this.createButtonEl();

				},
				status : true,
				JSON : null,
				getStatus : function() {
					this.JSON = this.toJSON();
					return this.status;
				},
				/**
				 * 构造json对象
				 */
				toJSON : function() {
					var object = {};
					var json = [];
					for (var i in this.shfArray) {
						var obj = this.shfArray[i].toShfObject();
						if (typeof(obj) != 'undefined') {
							if (this.shfArray[i].getStatus()) {
								json.push(obj);
								this.status = true;
							}
						} else {
							this.status = false;
						}
					}
					object.data = json;
					return object;
				},
				/**
				 * 返回json String
				 */
				toJsonStr : function() {
					return $.toJSON(this.toJSON());
				}
			});
})($);
/**
 * 选择框组件
 */
(function($) {
	$.emp.ChooseListBox = function(el, o) {
		$.emp.createObject(this, el, o);
	};
	$.extend($.emp.ChooseListBox.prototype, $.emp.ext.prototype, {
				title : "选择框",
				wrapEl : null,
				titleEl : null,
				bodyEl : null,
				footEl : null,
				/**
				 * 创建外容器
				 */
				createWrapEl : function() {
					this.id = "wrap_" + this.getSerise();
					var table = document.createElement("fieldset");
					$(this.element).append(table);
					this.wrapEl = $(table);
					this.wrapEl.id = this.id;
					var thead = document.createElement("legend");
					this.wrapEl.append(thead);
					this.titleEl = $(thead);
					var tbody = document.createElement("div");
					this.bodyEl = $(tbody);
					this.wrapEl.append(tbody);
					var tfoot = document.createElement("div");
					this.footEl = $(tfoot);
					this.wrapEl.append(tfoot);
				},
				addBtnEl : null,
				/**
				 * 创建添加按钮
				 */
				createAddBtnEl : function() {
					var button = document.createElement("button");
					button.style.width = "45%";
					button.innerHTML = "添加";
					$(button).addClass("ui-state-default ui-corner-all");
					this.footEl.append(button);
					return $(button);
				},
				delBtnEl : null,
				createDelBtnEl : function() {
					var button = document.createElement("button");
					button.style.width = "45%";
					button.innerHTML = "删除";
					$(button).addClass("ui-state-default ui-corner-all");
					this.footEl.append(button);
					return $(button);
				},
				/**
				 * 检查是否存在重复项
				 */
				checkItem : function(o) {
					var flag = false;
					var list = this.bodyEl.children();
					for (var i = 0; i < list.length; i++) {
						var item = list.get(i);
						if ($(item).attr("value") == o.value) {
							flag = true;
						}
					}
					return flag;
				},
				/**
				 * 添加进入容器中
				 */
				addItem : function(json) {
					var list = [];
					for (var i in json) {
						var o = json[i];
						if (!this.checkItem(o)) {
							list.push(o);
						}
					}
					for (var n in list) {
						var o = list[n];
						this.bodyEl.append(this.createItemEl(o));
					}
				},
				radioCheck : function() {
					var list = this.bodyEl.children();
					for (var i = 0; i < list.length; i++) {
						var o = list[i];
						$($(o).children()[0]).attr("checked", false);
					}

				},
				checkType : "checkbox",
				/**
				 * 添加列表项
				 */
				createItemEl : function(o) {
					var item = document.createElement("div");
					var check = document.createElement("input");
					check.type = this.checkType;
					check.name = this.checkType + "_" + this.id;
					var ct = this;
					if (this.checkType == "radio") {
						// check.checked=true;
						var radioHandler = function(ck) {
							ct.radioCheck();
							// if (!ck.checked) {
							ck.attr("checked", true);
							// }

						};
						$(item).click(function() {
									radioHandler($(check));
								});
						$(check).click(function() {
									radioHandler($(check));
								});
						// console.log(check);
					} else {
						var checkHandler = function() {
							if (!$(check).attr("checked")) {
								$(check).attr("checked", true);
							} else {
								$(check).removeAttr("checked");
							}
						}
						$(item).click(function() {
									checkHandler();
								});
						$(check).click(function() {
									checkHandler();
								});
					}
					$(item).append(check);
					$(item).addClass("ux-mselect-item");
					$(item).append(o.name);
					$(item).attr("value", o.value);
					$(item).hover(function() {
								$(item).addClass("ux-mselect-hover");
							}, function() {
								$(item).toggleClass("ux-mselect-hover");
							});
					return $(item);
				},
				ctype : "select",// defaults
				url : "",
				/**
				 * 删除选择的列表项
				 */
				delItem : function() {
					var lists = this.getSelectItems();
					for (var i in lists) {
						lists[i].remove();
					}
				},
				getSelectItems : function() {
					var list = this.bodyEl.children(".ux-mselect-item");
					var lists = [];
					for (var i = 0; i < list.length; i++) {
						if ($($(list[i]).children()[0]).attr("checked")) {
							lists.push($(list[i]));
						}
					}
					return lists;
				},
				/**
				 * 获取选择的列表 返回json
				 */
				getSelectJson : function() {
					var list = this.getSelectItems();
					var json = [];
					for (var i = 0; i < list.length; i++) {
						var el = list[i];
						var o = {};
						o.name = el.text();
						o.value = el.attr("value");
						json.push(o);
					}

					return json;
				},
				/**
				 * 事件初始化
				 */
				init : function() {
					this.title = this.options.title;
					this.render();
					var o = this;
					if (this.ctype == "select") {
						this.addBtnEl.click(function() {
									o.mothed.call(o, o.params);
								});
						this.delBtnEl.click(function() {
									o.delItem.call(o);
								});
					} else if (this.ctype == "mulChoose") {
						this.loadItems(this.url);
						this.searchTextAction();
						this.searchEl.keyup(function() {
									o.filterHandler.call(o, o.searchEl.val());
								});
						this.searchEl.mousedown(function() {
									var key = o.searchEl.val();
									if (key == o.searchText) {
										o.searchEl.val("");
										o.searchEl.attr("style",
												"color:black;width:100%;");
									}
								});
						this.searchEl.mouseout(function() {
									o.searchTextAction.call(o);
								});
						// this.chooseEl.click(function(){
						// var json=o.getSelectJson();
						// o.target.addItem.call(o.target,json);
						// o.options.dialog.remove();
						// });
					} else if (this.ctype == "singleChoose") {
						this.ajaxGetData(this.url);
						this.selectEl.change(function() {
									o.ajaxAddItemsList.call(o);
								});
					}
				},
				// 过滤响应
				filterHandler : function(key) {
					var reg = new RegExp(key);
					var list = this.bodyEl.children();
					for (var i = 0; i < list.length; i++) {
						var iter = list[i];
						var name = $(iter).text();
						if (reg.test(name)) {
							$(iter).attr("style", "display:block");
						} else {
							$(iter).attr("style", "display:none");
						}
					}
				},
				/**
				 * 搜索输入
				 */
				searchEl : null,
				createSearchEl : function() {
					var input = document.createElement("input");
					input.type = "text";
					input.style.width = "100%";
					this.footEl.append(input);
					return $(input);
				},
				/**
				 * 返回选择结果
				 */
				chooseEl : null,
				createChooseEl : function() {
					var button = document.createElement("button");
					button.style.width = "100%";
					button.innerHTML = "返回选择结果";
					$(button).addClass("ui-state-default ui-corner-all");
					this.footEl.append(button);
					return $(button);
				},
				selectEl : null,
				/**
				 * 创建下拉选择路由框
				 */
				createSelectEl : function() {
					var select = document.createElement("select");
					var option = document.createElement("option");
					option.innerHTML = "-------请选择路由-------";
					$(select).append(option);
					select.style.width = "100%";
					this.footEl.append(select);
					return $(select);
				},
				/**
				 * 读取路由下拉列表
				 */
				ajaxGetData : function(url) {
					var o = this;
					var array = [];
					$.getJSON(url, {}, function(json) {
								for (var i in json) {
									var iter = json[i];
									var obj = {};
									obj.name = iter.dicName;
									obj.value = iter.dicValue;
									array.push(obj);
								}
								o.addDropList.call(o, array);
							});
				},
				clearItems : function() {
					this.bodyEl.children().remove();
				},
				addDropList : function(json) {
					var options = "";
					for (var i in json) {
						var o = json[i];
						options += "<option value='" + o.value + "'>" + o.name
								+ "</option>";
					}
					this.selectEl.html(options);
					this.ajaxAddItemsList.call(this);
				},
				/**
				 * 读取路由子项
				 */
				ajaxAddItemsList : function() {
					var value = this.selectEl.val();
					var o = this;
					var url = "shf/dicType=ZB_WFI_R_" + value;
					$.getJSON(url, {}, function(json) {
								var array = [];
								for (var i in json) {
									var iter = json[i];
									var obj = {};
									var name = [];
									obj.name = iter.dicName;
									obj.value = iter.dicValue;
									array.push(obj);
								}
								o.clearItems();
								o.addItem(array);
								o.makeDefaultSelete.call(o);
							});
				},
				makeDefaultSelete : function() {
					var list = this.bodyEl.children(".ux-mselect-item");
					$($(list[0]).children()[0]).attr("checked", true);
				},
				/**
				 * UI创建入口
				 */
				createElement : function() {
					this.createWrapEl();
					if (this.options.ctype) {
						this.ctype = this.options.ctype;
						this.target = this.options.target;
						this.url = this.options.url;
					} else {
						this.ctype = "select";
						this.mothed = this.options.mothed;
						this.params = this.options.params;
						this.params.target = this;
					}
					if (this.ctype == "select") {
						this.addBtnEl = this.createAddBtnEl();
						this.delBtnEl = this.createDelBtnEl();
					} else if (this.ctype == "mulChoose") {
						this.searchText = "请输入搜索内容...";
						this.wrapEl.attr("style", "width:100%;");
						this.searchEl = this.createSearchEl();
						// this.chooseEl=this.createChooseEl();
					} else if (this.ctype == "singleChoose") {
						this.selectEl = this.createSelectEl();
						this.checkType = "radio";
					}
				},
				getSingleStr : function() {
					var o = {};
					o.name = this.selectEl.val();
					o.value = this.getSelectJson();
					return $.toJSON(o);
				},
				getRouteObject : function() {
					var o = {};
					o.routeKeyName = this.selectEl.val();
					o.routeValue = this.getSelectJson()[0];
					return o;
				},
				getRoles : function() {
					var roles = [];
					var list = this.getAllJson();
					for (var i = 0; i < list.length; i++) {
						roles.push(list[i].value);
					}
					return roles.toString();
				},
				/**
				 * 获取列表项
				 */
				getAllJson : function() {
					var list = this.bodyEl.children("");
					var array = [];
					for (var i = 0; i < list.length; i++) {
						var el = list[i];
						var o = {};
						o.name = $(el).text();
						o.value = $(el).attr("value");
						array.push(o);
					}
					return array;
				},
				toJsonStr : function() {
					return $.toJSON(this.getAllJson());
				},
				searchText : "请输入搜索内容...",
				searchTextAction : function() {
					var key = this.searchEl.val();
					if (key == "") {
						this.searchEl.val(this.searchText);
						this.searchEl
								.attr("style", "color:#cfcfcf;width:100%;");
						return;
					}
					if (key == this.searchText) {
						this.searchEl
								.attr("style", "color:#cfcfcf;width:100%;");
					} else {
						this.searchEl.attr("style", "color:black;width:100%;");
					}
				},
				destroy : function() {
					this.wrapEl.remove();
				},
				/**
				 * 读取服务器返回项/读取岗位信息
				 */
				loadItems : function(url) {
					var o = this;
					var array = [];
					$.getJSON(url, {}, function(json) {
								for (var i in json) {
									var iter = json[i];
									var obj = {};
									obj.name = iter.roleName;
									obj.value = iter.roleCd;
									array.push(obj);
								}
								o.addItem(array);
							});
				},
				render : function() {
					this.titleEl.text(this.title);
					this.bodyEl.addClass("x-fieldset-body ux-mselect");
					this.footEl.addClass("x-fieldset-foot");
					this.wrapEl.addClass("x-fieldset");

				}
			});
})($);
(function($) {
	$.emp.EmpTable = function(el, o) {
		$.emp.createObject(this, el, o);
	};
	$.extend($.emp.EmpTable.prototype, $.emp.ext.prototype, {
		wrapEl : null,
		titleEl : null,
		bodyEl : null,
		buttonsEl : null,
		/** 创建容器 */
		createWrapEl : function() {
			var buttonsEl = document.createElement("div");
			this.buttonsEl = $(buttonsEl);
			this.buttonsEl.id = "buttons_" + this.id;
			this.buttonsEl.attr("align", "left");
			$(this.element).append(buttonsEl);
			this.id = "wrap-" + this.getSerise();
			var table = document.createElement("table");
			$(this.element).append(table);
			this.wrapEl = $(table);
			this.wrapEl.id = this.id;
			var thead = document.createElement("thead");
			var tr = document.createElement("tr");
			this.wrapEl.append(thead);
			$(thead).append(tr);
			this.titleEl = $(tr);
			var tbody = document.createElement("tbody");
			this.bodyEl = $(tbody);
			this.wrapEl.append(tbody);

		},
		createElement : function() {
			this.createWrapEl();
			if (!this.readOnly) {
				this.createButtons();
				this.createErrorEl();
			}
			this.createTitles();
			this.render();
		},
		createBodyItem : function(obj) {
			var ct = this, n = this.items.length;
			var tr = document.createElement("tr");
			tr.style.cursor = "pointer";
			var cla = obj.id % 2 + 1;
			$(tr).addClass("row" + cla);
			for (var i in obj) {
				var o = obj[i];
				var td = document.createElement("td");
				$(td).html(o);
				$(tr).append(td);
			}
			this.bodyEl.append(tr);
			$(tr).click(function() {
						var lists = ct.bodyEl.children(".selected");
						for (var m = 0; m < lists.length; m++) {
							var r = lists[m];
							$(r).toggleClass("selected")
						}
						$(tr).addClass("selected");
					});

		},
		i : 1,// 项目数 也作为索引 自增长！important
		/** 删除* */
		delItemHandler : function() {
			var item = this.bodyEl.children(".selected")[0];
			var id = $(item).children("td")[0].innerHTML;
			var itemss = [];
			for (var i in this.items) {
				var iter = this.items[i];
				if (iter.id != id) {
					itemss.push(iter);
				} else {
					iter = null;
				}
			}
			this.items = itemss;
			this.bodyEl.children(".selected").remove();
		},
		/** 创建表头* */
		createTitles : function() {
			var tds = document.createElement("td");
			var td1 = document.createElement("td");
			var td2 = document.createElement("td");
			tds.width = "5%";
			td1.width = "25%";
			td2.width = "65%";
			tds.innerHTML = "编号";
			td1.innerHTML = "选择路由";
			td2.innerHTML = "执行岗位";
			this.titleEl.append(tds);
			this.titleEl.append(td1);
			this.titleEl.append(td2);
		},
		addBtn : null,
		delBtn : null,
		createButtons : function() {
			var add = document.createElement("button");
			var del = document.createElement("button");
			add.style.width = "80px";
			add.style.float = "left";
			add.innerHTML = "添加";
			$(add).addClass("btn_mouseout");
			del.style.width = "80px";
			del.style.float = "left";
			del.innerHTML = "删除";
			$(del).addClass("btn_mouseout");
			$(this.buttonsEl).append(add);
			$(this.buttonsEl).append(del);
			this.addBtn = $(add);
			this.delBtn = $(del);
		},
		errorEl : null,
		createErrorEl : function() {
			var error = document.createElement("span");
			this.delBtn.after(error);
			this.errorEl = $(error);
			this.errorEl.addClass("terror");
		},
		items : [],
		addGroupRouteJson : function(groupJson, routeJson) {
			var o = {}, item = {}, groupStr = "", routeStr = "", roles = [], rolesName = [];
			var n = this.i;
			this.i++;

			for (var i in groupJson) {
				var obj1 = groupJson[i];
				roles.push(obj1.value);
				rolesName.push(obj1.name);
				groupStr += "<li class='lic' value='" + obj1.value + "'>"
						+ obj1.name + ",</li>";
			}
			if (groupStr == "") {
				groupStr = "<li class='lic' value='0'> 默认岗位,</li>";
			}
			item.roles = roles.toString();
			item.rolesName = rolesName.toString();
			o.id = n;
			var obj2 = routeJson.routeValue;
			routeStr += "<li class='li' value='" + obj2.value + "'>"
					+ obj2.name + "</li>";
			o.route = "<ul class='ulc' value='" + routeJson.routeKeyName + "'>"
					+ routeStr + "</ul>";

			o.group = "<ul class='ulc' value='group'>" + groupStr + "</ul>";
			item.id = n;
			item.routeKeyName = routeJson.routeKeyName;
			item.routeValue = obj2.value;
			item.routeName = obj2.name;
			this.items.push(item);

			this.createBodyItem(o);
		},
		getResultJson : function() {
			var json = {};
			json.data = this.items;
			return $.toJSON(json);
		},
		/** 检查完整性* */
		getStatus : function() {
			// alert(this.items.length);

			if (this.items.length == 0) {
				this.errorEl.text("请选择路由...");
				return false;
			} else {
				this.errorEl.text("");
				return true;
			}

		},
		json : null,
		loadItems : function(list) {
			for (var n = 0; n < list.length; n++) {
				var json = list[n];
				var rolesValueList = json.roles.split(",");
				var rolesNameList = json.rolesName.split(",");
				var groupList = [];
				for (var i = 0; i < rolesValueList.length; i++) {
					var obj = {};
					obj.value = rolesValueList[i];
					obj.name = rolesNameList[i];
					groupList.push(obj);
				}
				var routeJson = {};
				routeJson.routeKeyName = json.routeKeyName;
				routeJson.routeValue = {
					name : json.routeName,
					value : json.routeValue
				};
				this.addGroupRouteJson(groupList, routeJson);
			}
		},
		init : function() {
			var o = this;
			this.callback = this.options.callback;
			if (typeof(this.options.jsonStr) != "undefined") {
				this.json = $.evalJSON(this.options.jsonStr);
				this.loadItems(this.json);
			}
			// console.log(this.readOnly);
			if (!this.readOnly) {
				this.addBtn.click(function() {
							o.callback.call(o);
						});
				this.delBtn.click(function() {
							o.delItemHandler.call(o)
						});
			}
		},
		render : function() {
			this.wrapEl.addClass("emp_table display");
			this.titleEl.addClass("emp_table_title");
			if (!this.readOnly)
				this.buttonsEl.attr("style", "padding:1px 5px 2px 5px;");
		}
	});
})($);
/**
 * 规则条件接口函数
 */
$.fn.extend({
			shfCondition : function(o) {
				return this.each(function() {
							new $.emp.ShfCondition(this, o);
						});
			}
		});
/**
 * 规则条件容器接口函数
 */
$.fn.extend({
			shfContainer : function(o) {
				return new $.emp.ShfCondition.Container(this, o);

			}
		});
/**
 * 选择容器接口函数
 */
$.fn.extend({
			shfChooseBox : function(o) {
				return new $.emp.ChooseListBox(this, o);

			}
		});
/**
 * 选择table接口函数
 */
$.fn.extend({
			resultTable : function(o) {
				return new $.emp.EmpTable(this, o);

			}
		});
/**
 * JSON序列化工具类
 */
(function($) {
	function toIntegersAtLease(n)
	// Format integers to have at least two digits.
	{
		return n < 10 ? '0' + n : n;
	}

	Date.prototype.toJSON = function(date)
	// Yes, it polutes the Date namespace, but we'll allow it here, as
	// it's damned usefull.
	{
		return this.getUTCFullYear() + '-'
				+ toIntegersAtLease(this.getUTCMonth()) + '-'
				+ toIntegersAtLease(this.getUTCDate());
	};

	var escapeable = /["\\\x00-\x1f\x7f-\x9f]/g;
	var meta = { // table of character substitutions
		'\b' : '\\b',
		'\t' : '\\t',
		'\n' : '\\n',
		'\f' : '\\f',
		'\r' : '\\r',
		'"' : '\\"',
		'\\' : '\\\\'
	};

	$.quoteString = function(string)
	// Places quotes around a string, inteligently.
	// If the string contains no control characters, no quote characters, and no
	// backslash characters, then we can safely slap some quotes around it.
	// Otherwise we must also replace the offending characters with safe escape
	// sequences.
	{
		if (escapeable.test(string)) {
			return '"' + string.replace(escapeable, function(a) {
						var c = meta[a];
						if (typeof c === 'string') {
							return c;
						}
						c = a.charCodeAt();
						return '\\u00' + Math.floor(c / 16).toString(16)
								+ (c % 16).toString(16);
					}) + '"';
		}
		return '"' + string + '"';
	};

	$.toJSON = function(o, compact) {
		var type = typeof(o);

		if (type == "undefined")
			return "undefined";
		else if (type == "number" || type == "boolean")
			return o + "";
		else if (o === null)
			return "null";

		// Is it a string?
		if (type == "string") {
			return $.quoteString(o);
		}

		// Does it have a .toJSON function?
		if (type == "object" && typeof o.toJSON == "function")
			return o.toJSON(compact);

		// Is it an array?
		if (type != "function" && typeof(o.length) == "number") {
			var ret = [];
			for (var i = 0; i < o.length; i++) {
				ret.push($.toJSON(o[i], compact));
			}
			if (compact)
				return "[" + ret.join(",") + "]";
			else
				return "[" + ret.join(", ") + "]";
		}

		// If it's a function, we have to warn somebody!
		if (type == "function") {
			throw new TypeError("Unable to convert object of type 'function' to json.");
		}

		// It's probably an object, then.
		var ret = [];
		for (var k in o) {
			var name;
			type = typeof(k);

			if (type == "number")
				name = '"' + k + '"';
			else if (type == "string")
				name = $.quoteString(k);
			else
				continue; // skip non-string or number keys

			var val = $.toJSON(o[k], compact);
			if (typeof(val) != "string") {
				// skip non-serializable values
				continue;
			}

			if (compact)
				ret.push(name + ":" + val);
			else
				ret.push(name + ": " + val);
		}
		return "{" + ret.join(", ") + "}";
	};

	$.compactJSON = function(o) {
		return $.toJSON(o, true);
	};

	$.evalJSON = function(src)
	// Evals JSON that we know to be safe.
	{
		return eval("(" + src + ")");
	};

	$.secureEvalJSON = function(src)
	// Evals JSON in a way that is *more* secure.
	{
		var filtered = src;
		filtered = filtered.replace(/\\["\\\/bfnrtu]/g, '@');
		filtered = filtered
				.replace(
						/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
						']');
		filtered = filtered.replace(/(?:^|:|,)(?:\s*\[)+/g, '');

		if (/^[\],:{}\s]*$/.test(filtered))
			return eval("(" + src + ")");
		else
			throw new SyntaxError("Error parsing JSON, source is not valid.");
	};
})(jQuery);
