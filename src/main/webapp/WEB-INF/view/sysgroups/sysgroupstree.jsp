<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String userName=""+request.getSession().getAttribute("u_name");
	request.setAttribute("BasePath", basePath);
%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<title>New Chinese Language and Culture School</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta content="" name="description"/>
<meta content="" name="author"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="<%=basePath%>assets/jstree/dist/themes/default/style.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css"/>
<link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/jquery-multi-select/css/multi-select.css"/>
<!-- END PAGE LEVEL STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="<%=basePath%>assets/metronic/assets/global/css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>assets/metronic/assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>assets/metronic/assets/admin/layout4/css/layout.css" rel="stylesheet" type="text/css"/>
<link id="style_color" href="<%=basePath%>assets/metronic/assets/admin/layout4/css/themes/light.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>assets/metronic/assets/admin/layout4/css/custom.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="favicon.ico"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-fixed-mobile" and "page-footer-fixed-mobile" class to body element to force fixed header or footer in mobile devices -->
<!-- DOC: Apply "page-sidebar-closed" class to the body and "page-sidebar-menu-closed" class to the sidebar menu element to hide the sidebar by default -->
<!-- DOC: Apply "page-sidebar-hide" class to the body to make the sidebar completely hidden on toggle -->
<!-- DOC: Apply "page-sidebar-closed-hide-logo" class to the body element to make the logo hidden on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-hide" class to body element to completely hide the sidebar on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-fixed" class to have fixed sidebar -->
<!-- DOC: Apply "page-footer-fixed" class to the body element to have fixed footer -->
<!-- DOC: Apply "page-sidebar-reversed" class to put the sidebar on the right side -->
<!-- DOC: Apply "page-full-width" class to the body element to have full width page without the sidebar menu -->
<body class="page-header-fixed page-sidebar-closed-hide-logo ">
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
	<!-- BEGIN HEADER INNER -->
	<div class="page-header-inner">
		<!-- BEGIN LOGO -->
		<div class="page-logo">
			<a href="#">
			<img src="<%=basePath%>assets/image/logo_word_1.png" alt="logo" class="logo-default"/>
			</a>
			<div class="menu-toggler sidebar-toggler">
				<!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
			</div>
		</div>
		<!-- END LOGO -->
		<!-- BEGIN RESPONSIVE MENU TOGGLER -->
		<a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
		</a>
		<!-- END RESPONSIVE MENU TOGGLER -->
		<!-- BEGIN PAGE ACTIONS -->
		<!-- DOC: Remove "hide" class to enable the page header actions -->
		<!-- END PAGE ACTIONS -->
		<!-- BEGIN PAGE TOP -->
		<div class="page-top">
			<!-- BEGIN HEADER SEARCH BOX -->
			<!-- END HEADER SEARCH BOX -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<li class="separator hide">
					</li>
					<!-- BEGIN NOTIFICATION DROPDOWN -->
					<!-- END NOTIFICATION DROPDOWN -->
					<li class="separator hide">
					</li>
					<!-- BEGIN INBOX DROPDOWN -->
					<!-- END INBOX DROPDOWN -->
					<li class="separator hide">
					</li>
					<!-- BEGIN TODO DROPDOWN -->
					<!-- END TODO DROPDOWN -->
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
					<li class="dropdown dropdown-user dropdown-dark">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
						<span class="username username-hide-on-mobile">
						<%=userName %> </span>
						<!-- DOC: Do not remove below empty space(&nbsp;) as its purposely used -->
						<img alt="" class="img-circle" src="<%=basePath%>assets/image/logo_image_only_1.png"/>
						</a>
						<ul class="dropdown-menu dropdown-menu-default">
							<!-- <li>
								<a href="extra_profile.html">
								<i class="icon-user"></i> My Profile </a>
							</li>
							<li>
								<a href="page_calendar.html">
								<i class="icon-calendar"></i> My Calendar </a>
							</li> -->
							<li>
								<a href="<%=basePath%>sysnotificationmanagement/sysnotificationdetail.do">
								<i class="icon-envelope-open"></i> My Inbox 
								</a>
							</li>
							<!-- <li>
								<a href="page_todo.html">
								<i class="icon-rocket"></i> My Tasks <span class="badge badge-success">
								7 </span>
								</a>
							</li> -->
							<li class="divider">
							</li>
							<!-- <li>
								<a href="extra_lock.html">
								<i class="icon-lock"></i> Lock Screen </a>
							</li> -->
							<li>
								<a href="javascript:dologout();">
								<i class="icon-key"></i> Log Out </a>
							</li>
						</ul>
					</li>
					<!-- END USER LOGIN DROPDOWN -->
				</ul>
			</div>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END PAGE TOP -->
	</div>
	<!-- END HEADER INNER -->
</div>
<!-- END HEADER -->
<div class="clearfix">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
	<!-- BEGIN SIDEBAR -->
	<div class="page-sidebar-wrapper">
		<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
		<!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
		<div class="page-sidebar navbar-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->
			<!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
			<!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
			<!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
			<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
			<!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
			<!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
			<ul class="page-sidebar-menu " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
				<li class="start ">
					<a href="<%=basePath%>sysnotificationmanagement/sysnotificationdetail.do">
					<i class="icon-home"></i>
					<span class="title">我的通知</span>
					</a>
				</li>
				<c:if test="${_sys_privilege eq 1 || _sys_privilege eq 2 }">
				<li class="active open">
					<a href="javascript:;">
					<i class="icon-settings"></i>
					<span class="title">系统服务</span>
					<span class="arrow open"></span>
					</a>
					<ul class="sub-menu">
						<li class="">
							<a href="<%=basePath%>sysusersmanagement/sysuserslist.do">
							<i class="icon-user"></i>
							<span class="title">用户管理</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>sysrolesmanagement/sysroleslist.do">
							<i class="icon-user-following"></i>
							<span class="title">角色管理</span>
							</a>
						</li>
						<li class="active">
							<a href="<%=basePath%>sysgroupsmanagement/sysgroupstree.do">
							<i class="icon-users"></i>
							<span class="title">群组管理</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>syspropertiesmanagement/syspropertiestree.do">
							<i class="icon-docs"></i>
							<span class="title">系统属性管理</span>
							</a>
						</li>
					</ul>
				</li>
				</c:if>
				<c:if test="${_sys_privilege eq 1 || _sys_privilege eq 2 || _sys_privilege eq 3 || _sys_privilege eq 4}">
				<li>
					<a href="javascript:;">
					<i class="icon-graduation"></i>
					<span class="title">学生学级管理</span>
					<span class="arrow "></span>
					</a>
					<ul class="sub-menu">
						<li class="">
							<a href="<%=basePath%>appstudentsinfomanagement/appstudentsinfolist.do">
							<i class="icon-pencil"></i>
							<span class="title">学生信息管理</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>appstudentsinfomanagement/appstudentsscorelist.do">
							<i class="icon-bar-chart"></i>
							<span class="title">学生成绩管理</span>
							</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="javascript:;">
					<i class="icon-book-open"></i>
					<span class="title">学生作业管理</span>
					<span class="arrow "></span>
					</a>
					<ul class="sub-menu">
						<li class="">
							<a href="<%=basePath%>appassignmenttutormanagement/downloadassignmentlist.do">
							<i class="icon-action-redo"></i>
							<span class="title">下载学生作业</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>appassignmenttutormanagement/downloadappointmentassignmentlist.do">
							<i class="icon-login"></i>
							<span class="title">下载代审作业</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>appassignmenttutormanagement/uploadassignmentlist.do">
							<i class="icon-action-undo"></i>
							<span class="title">上传已审作业</span>
							</a>
						</li>
						<li class="">
							<a href="<%=basePath%>appassignmenttutormanagement/uploadappointmentassignmentlist.do">
							<i class="icon-logout"></i>
							<span class="title">上传代审作业</span>
							</a>
						</li>
					</ul>
				</li>
				<li class="">
					<a href="<%=basePath%>sysnotificationmanagement/sysnotificationlist.do">
					<i class="icon-envelope-letter"></i>
					<span class="title">通知管理</span>
					</a>
				</li>
				</c:if>
				<li class="">
					<a href="<%=basePath%>sysusersmanagement/sysuserspersonal.do">
					<i class="icon-user-follow"></i>
					<span class="title">个人信息</span>
					</a>
				</li>
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
	</div>
	<!-- END SIDEBAR -->
	<!-- BEGIN CONTENT -->
	<div class="page-content-wrapper">
		<div class="page-content">
			<!-- BEGIN SAMPLE PORTLET CONFIGURATION MODAL FORM-->
			<div class="modal fade" id="portlet-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
							<h4 class="modal-title">Modal title</h4>
						</div>
						<div class="modal-body">
							 Widget settings form goes here
						</div>
						<div class="modal-footer">
							<button type="button" class="btn blue">保存</button>
							<button type="button" class="btn default" data-dismiss="modal">取消</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
			<!-- END SAMPLE PORTLET CONFIGURATION MODAL FORM-->
			<!-- BEGIN PAGE HEADER-->
			<!-- BEGIN PAGE HEAD -->
			<div class="page-head">
				<!-- BEGIN PAGE TITLE -->
				<div class="page-title">
					<h1>VCE课程管理系统-群组管理</h1>
				</div>
				<!-- END PAGE TITLE -->
				<!-- BEGIN PAGE TOOLBAR -->
				<!-- END PAGE TOOLBAR -->
			</div>
			<!-- END PAGE HEAD -->
			<!-- BEGIN PAGE BREADCRUMB -->
			<!-- END PAGE BREADCRUMB -->
			<!-- END PAGE HEADER-->
			<!-- BEGIN PAGE CONTENT-->
			<div class="row">
				<div class="col-md-6 col-sm-12">
					<!-- <div class="note note-danger note-shadow">
						<p>
							 NOTE: The below datatable is not connected to a real database so the filter and sorting is just simulated for demo purposes only.
						</p>
					</div> -->
					<!-- Begin: life time stats -->
					<div class="portlet light">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-user font-blue-sharp"></i>
								<span class="caption-subject font-green-sharp bold uppercase">群组信息</span>
								<span class="caption-helper"></span>
							</div>
							<div class="actions">
								<div class="btn-group">
									<a class="btn btn-default btn-circle" href="javascript:;" data-toggle="dropdown">
									<i class="fa fa-share"></i>
									<span class="hidden-480">
									Tools </span>
									<i class="fa fa-angle-down"></i>
									</a>
									<ul class="dropdown-menu pull-right">
										<li>
											<a href="javascript:deleteGroup();">
											删除选择的群组 </a>
										</li>
										<li>
											<a href="javascript:showPrivilege();">
												 群组授权
											</a>
										</li>
										<!-- <li>
											<a href="javascript:;">
											Export to XML </a>
										</li>
										<li class="divider">
										</li>
										<li>
											<a href="javascript:;">
											Print Invoices </a>
										</li> -->
									</ul>
								</div>
							</div>
						</div>
						<div class="portlet-body">
							<div id="tree_2" class="tree-demo">
							</div>
						</div>
					</div>
					<!-- End: life time stats -->
				</div>
				<div class="col-md-6 col-sm-12">
					<!-- BEGIN EXAMPLE TABLE PORTLET-->
					<div class="tabbable tabbable-custom tabbable-noborder tabbable-reversed">
						<div class="portlet light bordered form-fit">
							<div class="portlet-title">
								<div class="caption">
									<i class="icon-user font-blue-hoki"></i>
									<span class="caption-subject font-blue-hoki bold uppercase">群组信息</span>
								</div>
								<div class="actions">
									<a class="btn btn-circle btn-icon-only btn-default" href="javascript:createGroup();">
									<i class="icon-cloud-upload"></i>
									</a>
									<a class="btn btn-circle btn-icon-only btn-default" href="javascript:modifyGroup();">
									<i class="icon-wrench"></i>
									</a>
								</div>
							</div>
							<div class="portlet-body form">
								<!-- BEGIN FORM-->
								<form action="<%=basePath%>sysgroupsmanagement/savesysgroups.do" Method="POST" class="form-horizontal form-bordered form-label-stripped" id="frmsysgroupinfo">
									<div class="form-body">
										<div class="form-group">
											<label class="control-label col-md-3">上级群组<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="text" name="parentName" placeholder="上级群组" class="form-control" id="parentName" readOnly="readonly"/>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">群组名称<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="text" name="groupName" placeholder="群组名称" class="form-control" id="groupName"  readOnly="readonly"/>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">群组类别</label>
											<div class="col-md-9">
												<input type="checkbox" onChange="javascript:changeCategory()" class="make-switch" data-on-text="班级" data-off-text="校区" name="groupCategory" id="groupCategory">
											</div>
										</div>
										<div class="form-group" id="div_category_class_1">
											<label class="control-label col-md-3">上课日期</label>
											<div class="col-md-9">
												<select class="js-states form-control" id="propertyDateInfo" name="propertyDateInfo" disabled></select>
											</div>
										</div>
										<div class="form-group" id="div_category_class_2">
											<label class="control-label col-md-3">上课班级</label>
											<div class="col-md-9">
												<select class="js-states form-control" id="propertyClassInfo" name="propertyClassInfo" disabled></select>
											</div>
										</div>
										<div class="form-group" id="div_category_class_3">
											<label class="control-label col-md-3">上课时间</label>
											<div class="col-md-9">
												<input type="text" name="gropuTimeInfo" placeholder="上课时间" class="form-control" id="gropuTimeInfo"  readOnly="readonly"/>
											</div>
										</div>
									</div>
									<div class="form-actions">
										<div class="row">
											<div class="col-md-offset-3 col-md-9">
												<button type="button" class="btn blue" id="btnSubmit" onclick="javascript:saveGroup();"><i class="fa fa-check"></i> 保存</button>
												<button type="button" class="btn default" onclick="javascript:doCancelAction()" id="btnCancel">取消</button>
											</div>
										</div>
									</div>
									<input type="hidden" id="groupId" name="groupId" value="${sysgroup.id }" />
									<input type="hidden" id="groupParentId" name="groupParentId" value="${sysgroup.groupParentId }" />
									<input type="hidden" id="parentIds" name="parentIds" value="${parentIds }" />
									<input type="hidden" id="operation_status" name="operation_status" value="${operation_status }" />
								</form>
								<!-- END FORM-->
							</div>
						</div>
					</div>
					<!-- END EXAMPLE TABLE PORTLET-->
				</div>
				<!-- modal -->
				<div class="modal fade" id="ajax" role="basic" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="row">
								<div class="col-md-12">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
										<h4 class="modal-title">选择用户</h4>
									</div>
									<div class="modal-body">
										<div class="scroller" style="height:500px" data-always-visible="1" data-rail-visible1="1">
											<div class="row" id="div_multi_select_parent">
												<div class="col-md-6" id="div_multi_select">
													<select multiple="multiple" class="multi-select" id="sys_users_roles" name="sys_users_roles">
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" data-dismiss="modal" class="btn default">取消</button>
										<button type="button" class="btn blue" onclick="javascript:saveSelected()">保存</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- modal end -->
			</div>
			<!-- END PAGE CONTENT-->
		</div>
	</div>
	<!-- END CONTENT -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/respond.min.js"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/excanvas.min.js"></script> 
<![endif]-->
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="<%=basePath%>assets/jstree/dist/jstree.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootbox/bootbox.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-multi-select/js/jquery.multi-select.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN SELECT2 -->
<script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js"></script>
<!-- END SELECT2 -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="<%=basePath%>assets/metronic/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/layout.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/demo.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/pages/scripts/ui-alert-dialog-api.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
	 var $dateInfo, $classInfo,dateData, classData;
     jQuery(document).ready(function() {
        Metronic.init(); // init metronic core components
        Layout.init(); // init current layout
		Demo.init(); // init demo features
		
		$("#div_category_class_1").hide();
		$("#div_category_class_2").hide();
		$("#div_category_class_3").hide();
		
		$("#groupName").attr('readonly', true);
		
		$("#groupCategory").bootstrapSwitch('readonly', '[readonly]');
		
		//initiate select2
		var propertyDateId, propertyClassId;
		$dateInfo=$("#propertyDateInfo");
		$classInfo=$("#propertyClassInfo");
		
		//changing property id here
		propertyDateId=3;
		propertyClassId=6;
		
		$.ajax({
	        type: "POST",
	        async:false,
	        contentType: "application/json; charset=utf-8",
	        url: "<%=basePath%>sysgroupsmanagement/findpropertydateinfo.do",
	        data: "{'groupId':'"+$("#groupId").val()+"','propertyId':'"+propertyDateId+"'}",
	        dataType: 'json',
	        success: function(result) {
	        	dateData=result.data;
	        }
	    });
		
		
		$.ajax({
	        type: "POST",
	        async:false,
	        contentType: "application/json; charset=utf-8",
	        url: "<%=basePath%>sysgroupsmanagement/findpropertyclassinfo.do",
	        data: "{'groupId':'"+$("#groupId").val()+"','propertyId':'"+propertyClassId+"'}",
	        dataType: 'json',
	        success: function(result) {
	        	classData=result.data;
	        }
	    });
		
		
		//init tree
		$(function () {
			$('#tree_2').jstree({ 
				'core' : {
					"multiple" : false,
				    'data' : {
						contentType: "application/json; charset=utf-8",
				        url: "<%=basePath%>sysgroupsmanagement/inittreedata.do",
				        dataType: 'json'
				    }
				},
				"checkbox" : {
					"keep_selected_style" : true,
					"three_state":false,
					"whole_node":false,
					"tie_selection":false,
					"cascade":"down+undetermined"
			    },
				"types" : {
	                "default" : {
	                    "icon" : "fa fa-folder icon-state-warning icon-lg"
	                },
	                "file" : {
	                    "icon" : "fa fa-file icon-state-warning icon-lg"
	                }
	            },
	            "plugins": ["types","checkbox"]
			});
		});
		
		//add event listener
		$('#tree_2').on("changed.jstree", function (e, data) {
		  	/* alert(data.selected);
			var i, j, r = [];
		    for(i = 0, j = data.selected.length; i < j; i++) {
		      r.push(data.instance.get_node(data.selected[i]).text);
		    }
		    alert(r); */
		    /*
		    $("#parentName").val($('#tree_2').jstree(true).get_node(data.instance.get_parent(data.selected)).text);
		    $("#groupName").val(data.instance.get_node(data.selected).text);
		    $("#groupId").val(data.selected);
		    $("#groupParentId").val(data.instance.get_parent(data.selected));
		    */
		    
		    $("#groupId").val(data.selected);
		    $("#parentName").val($('#tree_2').jstree(true).get_node(data.instance.get_parent(data.selected)).text);
		    $("#groupName").attr('readonly', true);
		    $("#groupParentId").val(data.instance.get_parent(data.selected));
		    
			$.ajax({
		        type: "POST",
		        async:false,
		        contentType: "application/json; charset=utf-8",
		        url: "<%=basePath%>sysgroupsmanagement/findgroupinfo.do",
		        data: "{'groupId':'"+$("#groupId").val()+"'}",
		        dataType: 'json',
		        success: function(result) {
		        	$("#groupCategory").bootstrapSwitch('readonly', '');
		        	
		        	$("#groupName").val(result.data.groupName);
		        	var gcategory=result.data.groupCategory;
		        	
		        	if (gcategory==0){
		        		//$("#div_category_class").hide();
		        		if ($("#groupCategory").bootstrapSwitch("state")){
		        			$("#groupCategory").bootstrapSwitch("state",false);
		        		}
		        	}else if(gcategory&&gcategory==1){
		        		//$("#div_category_class").show();
		        		$("#groupCategory").bootstrapSwitch("state",true);
		        		
		        		$dateInfo.val(result.data.groupDateInfo).trigger("change");
		        		$classInfo.val(result.data.groupClassInfo).trigger("change");
		        		$("#gropuTimeInfo").val(result.data.groupTimeInfo);
		        	}
		        	
		        	$("#propertyDateInfo").attr("disabled",true);
		    		$("#propertyClassInfo").attr("disabled",true);
		    		$("#gropuTimeInfo").attr("readonly",true);
		        }
		    });
			
			$("#groupCategory").bootstrapSwitch('readonly', '[readonly]');
		});
		
		//init multi select & modal
		$('#ajax').on('show.bs.modal', function (){
			$("#div_multi_select").remove();
			$("#div_multi_select_parent").append('<div class="col-md-6" id="div_multi_select"><select multiple="multiple" class="multi-select" id="sys_users_roles" name="sys_users_roles"></select></div>');
			var sysRoleUsers;
			$.ajax({
		        type: "POST",
		        async:false,
		        contentType: "application/json; charset=utf-8",
		        url: "<%=basePath%>sysgroupsmanagement/findsysusers.do",
		        data: "{'groupId':'"+$("#groupId").val()+"'}",
		        dataType: 'json',
		        success: function(result) {
		        	sysRoleUsers=result.data;
		        }
		    });
			$('#sys_users_roles').multiSelect();
			$('#sys_users_roles').multiSelect('addOption', sysRoleUsers);
			
			var arr=[];
			$.each(sysRoleUsers,function(idx,item){
				if (item.isSelected){
					arr.push(""+item.value);
				}
			});
			$('#sys_users_roles').multiSelect('select', arr);
		});
		
		var result="${result}";
		try{
			if (result){
				showMessage(result);
			}
		}catch(error){
		}
	});
     
    function changeCategory(){
    	if($("#groupCategory").attr("checked")){
    		//$("#div_category_class_1").show();
    		//$("#div_category_class_2").show();
    		//$("#div_category_class_3").show();
    		classInfoShow();
    		
    		$("#propertyDateInfo").attr("disabled",false);
    		$("#propertyClassInfo").attr("disabled",false);
    		$("#gropuTimeInfo").attr("readonly",false);
    		
    		$dateInfo.val(null).trigger("change");
    		$classInfo.val(null).trigger("change");
    	}else{
    		//$("#div_category_class_1").hide();
    		//$("#div_category_class_2").hide();
    		//$("#div_category_class_3").hide();
    		classInfoHide();
    	}
    }
     
   	//refresh tree
	function refreshTree(node){
		if (node){
			$('#tree_2').jstree(true).load_node(node);
		}else{
			$('#tree_2').jstree(true).refresh();
		}
	}
   	
	//get tree instance
	function getInstanceOfTree(tree){
		var treeInstance;
		if (tree){
			treeInstance=$('#'+tree).jstree(true);
		}else{
			treeInstance=$('#tree_2').jstree(true);
		}
		return treeInstance;
	}
	
	//create group button action
	function createGroup(){
		$("#groupName").attr('readonly', false);
		$("#groupCategory").bootstrapSwitch('readonly', '');
		
		var ref=getInstanceOfTree(),selection=ref.get_selected();
		
		$("#parentName").val(ref.get_node(selection).text);
	    $("#groupName").val("");
	    $("#groupId").val(null);
	    $("#groupParentId").val(selection);
	    
	    $dateInfo.val(null).trigger("change");
		$classInfo.val(null).trigger("change");
		$("#gropuTimeInfo").val("");
	}
	
	//modify group button action
	function modifyGroup() {
		var ref=getInstanceOfTree();
		if (!ref.get_selected()){
			showMessage("Hitting text to select option that you want to modify.");
			return;
		}
		$("#groupName").attr('readonly', false);
		//$("#groupCategory").bootstrapSwitch('readonly', '');
		$("#propertyDateInfo").attr("disabled",false);
		$("#propertyClassInfo").attr("disabled",false);
		$("#gropuTimeInfo").attr("readonly",false);
	}
	
	//delete group button action
	function deleteGroup() {
		var ref=getInstanceOfTree();
		if(ref.get_checked()==""){
			showMessage("请选择要删除的群组信息");
			return;
		}
		bootbox.confirm("<font size='3'>您选择的群组信息会被删除，此操作<font color='red'>不可恢复</font>，请确认</font>", function (result){
			if (result==true){
				$.ajax({
					type : "POST",
					async : false,
					contentType : "application/json; charset=utf-8",
					url : "<%=basePath%>sysgroupsmanagement/deletegroups.do",
			        data: "{'groupIds':'"+ref.get_checked()+"'}",
			        dataType: 'json',
			        success: function(result) {
			        	if (result.status==1){
			        		try{
			        			cleanAllFields();
			        		}catch(error){
			        		}
			   			 	refreshTree();
			        	}
			        	showMessage(result.data);
			        }
			    });
			}
		});
	}
	
	function cleanAllFields(){
		$("#parentName").val("");
	    $("#groupName").val("");
	    $("#groupId").val(null);
	    $("#groupParentId").val(null);
	    
	    //$("#div_category_class_1").hide();
		//$("#div_category_class_2").hide();
		//$("#div_category_class_3").hide();
	    classInfoHide();
	}
	
	function doCancelAction(){
		//getInstanceOfTree().select_node("#"+$("#groupId").val(),'false','false');
		var tmpId=$("#groupId").val()?$("#groupId").val():$("#groupParentId").val();
		getInstanceOfTree().deselect_node("#"+tmpId,false,false);
		getInstanceOfTree().select_node("#"+tmpId,false,false);
	}

	//check field and do save action
	function saveGroup() {
		var flag = true;

		if (!$("#groupName").val()) {
			constructAlertMessage("请输入群组名称!");
			flag = false;
		}

		if (!flag) {
			showMessage();
			return;
		}

		saveGroupInfo();
	}
	
	//save group information
	function saveGroupInfo(){
		var id, parentId, groupName, groupCategory, propertyDateInfo=null, propertyClassInfo=null, gropuTimeInfo=null;
		
		id = $("#groupId").val();
		parentId = $("#groupParentId").val();
		groupName = $("#groupName").val();
		
		if ($("#groupCategory").attr("checked")){
			groupCategory=1;
			propertyDateInfo=$("#propertyDateInfo").val();
			propertyClassInfo=$("#propertyClassInfo").val();
			gropuTimeInfo=$("#gropuTimeInfo").val();
		}else{
			groupCategory=0;
		}
		

		$.ajax({
			type : "POST",
			async : false,
			contentType : "application/json; charset=utf-8",
			url : "<%=basePath%>sysgroupsmanagement/savesysgroups.do",
	        data: "{'groupId':'"+id+"' , 'groupParentId':'"+parentId+"' , 'groupName':'"+groupName
	        	+"' , 'groupCategory':'"+groupCategory+"' , 'propertyDateInfo':'"+propertyDateInfo
	        	+"' , 'propertyClassInfo':'"+propertyClassInfo+"' , 'gropuTimeInfo':'"+gropuTimeInfo+"'}",
	        dataType: 'json',
	        success: function(result) {
	        	if (result.status==1){
	   			 	$("#groupName").attr('readonly',true);
	   			 	refreshTree();
	   			 	//openTreeNode();
	   			 	
	   				classInfoHide();
	   			 	$("#groupCategory").bootstrapSwitch('readonly', '[readonly]');
	        	}
	        	showMessage(result.data);
	        }
	    });
	}
	
	function openTreeNode(node){
		var ref=getInstanceOfTree();
		if (node){
			operationNode=node;
		}else{
			operationNode=ref.get_selected();
		}
		if (!ref.is_open(operationNode)){
			ref.open_node(operationNode,false,0);
		}
	}
	
	//save privilege
	function saveSelected(){
   	 $.ajax({
			type : "POST",
			async : false,
			contentType : "application/json; charset=utf-8",
			url : "<%=basePath%>sysgroupsmanagement/savegroupuserrelationship.do",
	        data: "{'groupId':'"+$("#groupId").val()+"' , 'userIds':'"+$("#sys_users_roles").val()+"'}",
	        dataType: 'json',
	        success: function(result) {
	        	if (result.status==1){
	        		$("#ajax").modal('hide');
	        	}
	        	showMessage(result.data);
	        }
	    });
    }
    
    function showPrivilege(){
   	 if ($("#groupId").val()){
   		 $("#ajax").modal('show');
   	 }else{
   		 showMessage("请先选择群组!");
   	 }
   	 
    }
    
    function classInfoHide(){
    	$("#div_category_class_1").hide();
		$("#div_category_class_2").hide();
		$("#div_category_class_3").hide();
		
		$dateInfo.select2("destroy");
		$classInfo.select2("destroy");
    }
    
    function classInfoShow(){
    	$("#div_category_class_1").show();
		$("#div_category_class_2").show();
		$("#div_category_class_3").show();
		
    	$dateInfo.select2({
			data:dateData,
			placeholder: "选择上课日期"
		});
		$classInfo.select2({
			data:classData,
			placeholder: "选择班型"
		});
    	
    }
    
    function dologout(){
 		 bootbox.confirm("<font size='3'>您即将退出系统，请确认</font>", function (result){
 				if (result==true){
 					window.location="<%=basePath%>sysusersmanagement/userlogout.do";
 				}
 			});
    }

	var message;
	function constructAlertMessage(msg){
		if (!message){
			message=msg;
		}else{
			message+="<br/>"+msg;
		}
	}
	
	function showMessage(msg) {
		if (msg) {
			message = msg;
		}
		bootbox.alert("<font size='4'>"+message+"</font>"); 
		/* alert(message); */
		message="";
	}
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>