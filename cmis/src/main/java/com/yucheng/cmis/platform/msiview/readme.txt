MSIView模块，通过注解机制自动查询系统中已经注册过的模块服务。
1、随着工程JAVA文件的增加，每次查询系统中的模块服务可能时间过长，已经实施的项目中测试过，需要5-7秒的时间。
	所以在系统启动的时候查询一次，保存到数据库中，每次查询可直接查询表，同时添加reLoad的功能

	
2、表结构：
	create table s_msi_class_view(
	    modual_id varchar2(32), --模块ID
	    modual_name varchar2(80), --模块名称
	    msi_class_id varchar2(32), --服务ID
	    msi_class_desc varchar2(200), --服务描述
	    msi_class_name varchar2(200), --服务类名
	    msi_class_example varchar2(200) --服务类调用示例
	 )
	
	create table s_msi_method_view(
	    msi_class_id varchar2(32), --服务ID
	    msi_method_id varchar2(32),--方法ID PK
	    msi_method_name varchar2(32), --方法名
	    msi_method_desc varchar2(200), --方法描述
	    msi_method_param_in varchar2(600),--输入参数
	    msi_method_param_out varchar2(600), --输入参数
	    msi_method_type varchar2(10),--方法类型
	    msi_method_example varchar2(200) --方法调用示例
    )