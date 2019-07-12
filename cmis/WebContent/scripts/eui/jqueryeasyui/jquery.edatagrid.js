/**
 * edatagrid - jQuery EasyUI
 * 
 * Licensed under the GPL:
 *   http://www.gnu.org/licenses/gpl.txt
 *
 * Copyright 2011 stworthy [ stworthy@gmail.com ] 
 * 
 * Dependencies:
 *   datagrid
 *   messager
 *
 * 修改说明：
 * <p>1、修改addRow方法，增加可编辑列表的列的默认值设置。zangys 2014-6-18</p>
 * <p>2、修改onClickCell方法，将opts.editing && opts.editIndex >= 0改为>=-1由双击触发编辑器改为单击触发
 * <p>3、修改编辑表格在编辑状态不能通过点击数字列选择其他行（针对点击数字列时不调用validateRow方法问题）。 add by zangys at 2014-10-10 12:54:33</p>
 * <p>4、onAfterEdit事件中数据保存成功后只更新row中存在的属性值，避免将执行结果等信息保存到row中，当再次请求后台时将执行结果等信息一并
 * 		 post到后台，后台在使用context.addDatafield(name,value)方法时报添加的name值已存在的错误。add by zangys at 2014-12-9 15:22:24
 * </p>
 * <p>5. 消费合并:_del中添加opts.onDestroy.call(dg[0], index, row);</p>
 * <p>6. onAfterEdit:可编辑表格，行数据保存异常时的提示信息更新为系统通用的EMP.alertException() update by WangYang at 2016-06-06 17:48
 * <p>7. 添加onAfterSave事件，定义执行保存后可执行的方法，用于处理后台返回的data信息，
 *       onAfterEdit方法中，判断如果用户定义了onAfterSave事件的方法，则会讲ajax方法执行后返回的信息返回到页面供用户使用 add by WangYang at 2016-07-27</p>
 * <p>8. 添加onAfterDestroy事件，定义执行删除操作后可执行的方法，用于处理后台返回的data信息，
 *       _del方法中，判断如果用户定义了onAfterDestroy事件的方法，则会讲ajax方法执行后返回的信息返回到页面供用户使用 add by WangYang at 2016-07-27</p>
 * <p>9. _del方法中在执行destroyUrl的.do方法时，将$.post换成$.ajax，应为$.post不会监控服务器报错的时候的异常 add by WangYang at 2016-07-28</p>
 * <p>10. 删除对象最后一个属性后的逗号，防止在IE7下报错 add by liucheng3 at 2016-08-19</p>
 */
(function($){
	var currTarget;
	$(function(){
		$(document).unbind('.edatagrid').bind('mousedown.edatagrid', function(e){
			var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
			if (p.length){
				if (p.hasClass('datagrid-view')){
					var dg = p.children('table');
					if (dg.length && currTarget != dg[0]){
						_save();
					}
				}
				return;
			}
			_save();
			
			function _save(){
				var dg = $(currTarget);
				if (dg.length){
					dg.edatagrid('saveRow');
					currTarget = undefined;
				}
			}
		});
	});
	
	function buildGrid(target){
		var opts = $.data(target, 'edatagrid').options;
		$(target).datagrid($.extend({}, opts, {
			onDblClickCell:function(index,field,value){
				if (opts.editing){
					$(this).edatagrid('editRow', index);
					focusEditor(field);
				}
				if (opts.onDblClickCell){
					opts.onDblClickCell.call(target, index, field, value);
				}
			},
			onClickCell:function(index,field,value){
				if (opts.editing && opts.editIndex >= -1){
					$(this).edatagrid('editRow', index);
					focusEditor(field);
				}
				if (opts.onClickCell){
					opts.onClickCell.call(target, index, field, value);
				}
			},
			onAfterEdit: function(index, row){
				opts.editIndex = -1;
				var url = row.isNewRecord ? opts.saveUrl : opts.updateUrl;
				if (url){
					$.ajax({
						url: url,
						type:'post',
						data:row,
						dataType:'text',
						success:function(jsonstr){
							//异常信息提示 add by wangbin 2014-4-14 14:46:09 
							try {
								// 如果定义了此事件，则使用此事件将ajax方法执行的结果信息返回到页面供用户处理 add by WangYang at 2016-07-27
								if(opts.onAfterSave){
									opts.onAfterSave.call(target, index, row, jsonstr);
								}
								// ----- end -------
								var data = eval("("+jsonstr+")");
							} catch(e) {
								//$.messager.alert('错误',jsonstr,'error');
								EMP.alertException(jsonstr);//update by WangYang at 2016-06-06 17:48
								$(target).edatagrid('cancelRow',index);
								$(target).edatagrid('selectRow',index);
								$(target).edatagrid('editRow',index);
								opts.onError.call(target, index, data);
								return;
							}
							if (data.isError && data.isError!='false'){
								$(target).edatagrid('cancelRow',index);
								$(target).edatagrid('selectRow',index);
								$(target).edatagrid('editRow',index);
								opts.onError.call(target, index, data);
								return;
							}
							data.isNewRecord = null;
							
							/**
							 * 只更新row中存在的属性值，避免将执行结果等信息保存到row中，当再次请求后台时将执行结果等信息一并
							 * post到后台，后台在使用context.addDatafield(name,value)方法时报添加的name值已存在的错误。
							 */
							var newdata = {}
							for(var rowcolumn in row){
								if(data[rowcolumn] != undefined){
									newdata[rowcolumn]=data[rowcolumn];
								}
							}
							$(target).datagrid('updateRow', {
								index: index,
								//row: data
								row: newdata//只更新row中存在的字段值
							});
							if (opts.tree){
								var idValue = row[opts.idField||'id'];
								var t = $(opts.tree);
								var node = t.tree('find', idValue);
								if (node){
									node.text = row[opts.treeTextField];
									t.tree('update', node);
								} else {
									var pnode = t.tree('find', row[opts.treeParentField]);
									t.tree('append', {
										parent: (pnode ? pnode.target : null),
										data: [{id:idValue,text:row[opts.treeTextField]}]
									});
								}
							}
							opts.onSave.call(target, index, row);
						}
					});
				} else {
					opts.onSave.call(target, index, row);
				}
				if (opts.onAfterEdit) opts.onAfterEdit.call(target, index, row);
			},
			onCancelEdit: function(index, row){
				opts.editIndex = -1;
				if (row.isNewRecord) {
					$(this).datagrid('deleteRow', index);
				}
				if (opts.onCancelEdit) opts.onCancelEdit.call(target, index, row);
			},
			onBeforeLoad: function(param){
				if (opts.onBeforeLoad.call(target, param) == false){return false}
//				$(this).datagrid('rejectChanges');
				$(this).edatagrid('cancelRow');
				if (opts.tree){
					var node = $(opts.tree).tree('getSelected');
					param[opts.treeParentField] = node ? node.id : undefined;
				}
			},
			onClickRow: function(rowIndex, rowData){
				var theEvent = window.event || arguments.callee.caller.arguments[0];
				var element =  theEvent.target || theEvent.srcElement;
				//在编辑状态禁止通过点击数字列单元格选择其他行
				if (($(element).hasClass("datagrid-td-rownumber") || $(element).hasClass('datagrid-cell-rownumber')) && opts.editing && opts.editIndex > -1 && rowIndex != opts.editIndex){
					setTimeout(function(){
						$(target).datagrid('selectRow', opts.editIndex);
					},0);
				}
				if(opts.onClickRow){
					opts.onClickRow.call(target, rowIndex, rowData)
				}
			}
		}));
		
		
		function focusEditor(field){
			var editor = $(target).datagrid('getEditor', {index:opts.editIndex,field:field});
			if (editor){
				editor.target.focus();
			} else {
				var editors = $(target).datagrid('getEditors', opts.editIndex);
				if (editors.length){
					editors[0].target.focus();
				}
			}
		}
		
		if (opts.tree){
			$(opts.tree).tree({
				url: opts.treeUrl,
				onClick: function(node){
					$(target).datagrid('load');
				},
				onDrop: function(dest,source,point){
					var targetId = $(this).tree('getNode', dest).id;
					$.ajax({
						url: opts.treeDndUrl,
						type:'post',
						data:{
							id:source.id,
							targetId:targetId,
							point:point
						},
						dataType:'json',
						success:function(){
							$(target).datagrid('load');
						}
					});
				}
			});
		}
	}
	
	$.fn.edatagrid = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.edatagrid.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'edatagrid');
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'edatagrid', {
					options: $.extend({}, $.fn.edatagrid.defaults, $.fn.edatagrid.parseOptions(this), options)
				});
			}
			buildGrid(this);
		});
	};
	
	$.fn.edatagrid.parseOptions = function(target){
		return $.extend({}, $.fn.datagrid.parseOptions(target), {
		});
	};
	
	$.fn.edatagrid.methods = {
		options: function(jq){
			var opts = $.data(jq[0], 'edatagrid').options;
			return opts;
		},
		enableEditing: function(jq){
			return jq.each(function(){
				var opts = $.data(this, 'edatagrid').options;
				opts.editing = true;
			});
		},
		disableEditing: function(jq){
			return jq.each(function(){
				var opts = $.data(this, 'edatagrid').options;
				opts.editing = false;
			});
		},
		editRow: function(jq, index){
			return jq.each(function(){
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				var editIndex = opts.editIndex;
				if (editIndex != index){
					if (dg.datagrid('validateRow', editIndex)){
						if (editIndex>=0){
							if (opts.onBeforeSave.call(this, editIndex) == false) {
								setTimeout(function(){
									dg.datagrid('selectRow', editIndex);
								},0);
								return;
							}
						}
						dg.datagrid('endEdit', editIndex);
						dg.datagrid('beginEdit', index);
						opts.editIndex = index;
						
						if (currTarget != this && $(currTarget).length){
							$(currTarget).edatagrid('saveRow');
							currTarget = undefined;
						}
						if (opts.autoSave){
							currTarget = this;
						}
						
						var rows = dg.datagrid('getRows');
						opts.onEdit.call(this, index, rows[index]);
					} else {
						setTimeout(function(){
							dg.datagrid('selectRow', editIndex);
						}, 0);
					}
				}
			});
		},
		addRow: function(jq, index){
			return jq.each(function(){
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				//////////////////
				var defaultValues = {};
				if(opts.defaultValues){
					$.extend(defaultValues,opts.defaultValues);
				}
				/////////////////
				if (opts.editIndex >= 0){
					if (!dg.datagrid('validateRow', opts.editIndex)){
						dg.datagrid('selectRow', opts.editIndex);
						return;
					}
					if (opts.onBeforeSave.call(this, opts.editIndex) == false){
						setTimeout(function(){
							dg.datagrid('selectRow', opts.editIndex);
						},0);
						return;
					}
					dg.datagrid('endEdit', opts.editIndex);
				}
				var rows = dg.datagrid('getRows');
				
				function _add(index, row){
					if (index == undefined){
						dg.datagrid('appendRow', row);
						opts.editIndex = rows.length - 1;
					} else {
						dg.datagrid('insertRow', {index:index,row:row});
						opts.editIndex = index;
					}
				}
				if (typeof index == 'object'){
					//增加默认值
					_add(index.index, $.extend(defaultValues,index.row, {isNewRecord:true}));
					//_add(index.index, $.extend(index.row, {isNewRecord:true}));
				} else {
					//增加默认值
					_add(index, $.extend(defaultValues,{isNewRecord:true}));
					//_add(index, {isNewRecord:true});
				}
				
//				if (index == undefined){
//					dg.datagrid('appendRow', {isNewRecord:true});
//					opts.editIndex = rows.length - 1;
//				} else {
//					dg.datagrid('insertRow', {
//						index: index,
//						row: {isNewRecord:true}
//					});
//					opts.editIndex = index;
//				}
				
				dg.datagrid('beginEdit', opts.editIndex);
				dg.datagrid('selectRow', opts.editIndex);
				
				if (opts.tree){
					var node = $(opts.tree).tree('getSelected');
					rows[opts.editIndex][opts.treeParentField] = (node ? node.id : 0);
				}
				
				opts.onAdd.call(this, opts.editIndex, rows[opts.editIndex]);
			});
		},
		saveRow: function(jq){
			return jq.each(function(){
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				if (opts.editIndex >= 0){
					if (opts.onBeforeSave.call(this, opts.editIndex) == false) {
						setTimeout(function(){
							dg.datagrid('selectRow', opts.editIndex);
						},0);
						return;
					}
					$(this).datagrid('endEdit', opts.editIndex);
				}
			});
		},
		cancelRow: function(jq){
			return jq.each(function(){
				var opts = $.data(this, 'edatagrid').options;
				if (opts.editIndex >= 0){
					$(this).datagrid('cancelEdit', opts.editIndex);
				}
			});
		},
		destroyRow: function(jq, index){
			return jq.each(function(){
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				
				var rows = [];
				if (index == undefined){
					rows = dg.datagrid('getSelections');
				} else {
					var rowIndexes = $.isArray(index) ? index : [index];
					for(var i=0; i<rowIndexes.length; i++){
						var row = opts.finder.getRow(this, rowIndexes[i]);
						if (row){
							rows.push(row);
						}
					}
				}
				
				if (!rows.length){
					$.messager.show({
						title: opts.destroyMsg.norecord.title,
						msg: opts.destroyMsg.norecord.msg
					});
					return;
				}
				
				$.messager.confirm(opts.destroyMsg.confirm.title,opts.destroyMsg.confirm.msg,function(r){
					if (r){
						for(var i=0; i<rows.length; i++){
							_del(rows[i]);
						}
						dg.datagrid('clearSelections');
					}
				});
				
				function _del(row){
					var index = dg.datagrid('getRowIndex', row);
					if (index == -1){return}
					if (row.isNewRecord){
						dg.datagrid('cancelEdit', index);
						opts.onDestroy.call(dg[0], index, row);
					} else {
						if (opts.destroyUrl){
							var idValue = row[opts.idField||'id'];
							$.ajax({
								url: opts.destroyUrl,
								type:'post',
								data:{id:idValue},
								dataType:'text',
								success:function(jsonstr){
									try{
										var index = dg.datagrid('getRowIndex', idValue);
										// 如果定义了此事件，则使用此事件将ajax方法执行的结果信息返回到页面供用户处理 add by WangYang at 2016-07-27
										if(opts.onAfterDestroy){
											opts.onAfterDestroy.call(dg[0], index, row, jsonstr);
										}
										var data = eval("("+jsonstr+")");
									} catch(e){
										dg.datagrid('selectRow', index);
										opts.onError.call(dg[0], index, data);
										return;
									}
									
									//--------- end ------
									if (data.isError){
										dg.datagrid('selectRow', index);
										opts.onError.call(dg[0], index, data);
										return;
									}
									if (opts.tree){
										dg.datagrid('reload');
										var t = $(opts.tree);
										var node = t.tree('find', idValue);
										if (node){
											t.tree('remove', node.target);
										}
									} else {
										dg.datagrid('cancelEdit', index);
										dg.datagrid('deleteRow', index);
									}
									opts.onDestroy.call(dg[0], index, row);
								}
							});
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
						} else {
							dg.datagrid('cancelEdit', index);
							dg.datagrid('deleteRow', index);
							opts.onDestroy.call(dg[0], index, row);
						}
					}
				}
			});
		}
	};
	
	$.fn.edatagrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		editing: true,
		editIndex: -1,
		destroyMsg:{
			norecord:{
				title:'Warning',
				msg:'No record is selected.'
			},
			confirm:{
				title:'Confirm',
				msg:'Are you sure you want to delete?'
			}
		},
//		destroyConfirmTitle: 'Confirm',
//		destroyConfirmMsg: 'Are you sure you want to delete?',
		
		autoSave: false,	// auto save the editing row when click out of datagrid
		url: null,	// return the datagrid data
		saveUrl: null,	// return the added row
		updateUrl: null,	// return the updated row
		destroyUrl: null,	// return {success:true}
		
		tree: null,		// the tree selector
		treeUrl: null,	// return tree data
		treeDndUrl: null,	// to process the drag and drop operation, return {success:true}
		treeTextField: 'name',
		treeParentField: 'parentId',
		
		onAdd: function(index, row){},
		onEdit: function(index, row){},
		onBeforeSave: function(index){},
		onSave: function(index, row){},
		onDestroy: function(index, row){},
		onError: function(index, row){},
		//add by WangYang at 2016-07-27 定义执行保存后可执行的方法，用于处理后台返回的data信息
		onAfterSave: function(index, row, data){},
		onAfterDestroy: function(index, row, data){}
	});
	
	////////////////////////////////
	$.parser.plugins.push('edatagrid');
})(jQuery);