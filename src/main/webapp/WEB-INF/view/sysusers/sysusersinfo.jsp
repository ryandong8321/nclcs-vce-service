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
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.3.5
Version: 4.1.0
Author: KeenThemes
Website: http://www.keenthemes.com/
Contact: support@keenthemes.com
Follow: www.twitter.com/keenthemes
Like: www.facebook.com/keenthemes
Purchase: http://themeforest.net/item/metronic-responsive-admin-dashboard-template/4021469?ref=keenthemes
License: You must have a valid license purchased only from themeforest(the above link) in order to legally use the theme for your project.
-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<title>Metronic | Form Stuff - Form Layouts</title>
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
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/select2/select2.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-markdown/css/bootstrap-markdown.min.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css"/>
<!-- END PAGE LEVEL SCRIPTS -->
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
			<!-- DOC: Apply "search-form-expanded" right after the "search-form" class to have half expanded search box -->
			<!-- END HEADER SEARCH BOX -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<li class="separator hide">
					</li>
					<!-- BEGIN NOTIFICATION DROPDOWN -->
					<!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
					<!-- END NOTIFICATION DROPDOWN -->
					<li class="separator hide">
					</li>
					<!-- BEGIN INBOX DROPDOWN -->
					<!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
					<!-- END INBOX DROPDOWN -->
					<li class="separator hide">
					</li>
					<!-- BEGIN TODO DROPDOWN -->
					<!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
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
								<a href="<%=basePath%>sysusersmanagement/userlogout.do">
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
						<li class="active">
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
						<li class="">
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
							<button type="button" class="btn blue">Save changes</button>
							<button type="button" class="btn default" data-dismiss="modal">Close</button>
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
					<h1>VCE课程管理系统-用户管理</h1>
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
				<div class="col-md-12">
					<div class="tabbable tabbable-custom tabbable-noborder tabbable-reversed">
						<div class="portlet light bordered form-fit">
							<div class="portlet-title">
								<div class="caption">
									<i class="icon-user font-blue-hoki"></i>
									<span class="caption-subject font-blue-hoki bold uppercase">用户信息</span>
								</div>
								<div class="actions">
									<!-- <a class="btn btn-circle btn-icon-only btn-default" href="javascript:;">
									<i class="icon-cloud-upload"></i>
									</a> -->
									<a class="btn btn-circle btn-icon-only btn-default" href="javascript:modifyInfo();">
									<i class="icon-wrench"></i>
									</a>
									<a class="btn btn-circle btn-icon-only btn-default" href="javascript:deleteInfo();">
									<i class="icon-trash"></i>
									</a>
								</div>
							</div>
							<div class="portlet-body form">
								<!-- BEGIN FORM-->
								<form action="<%=basePath%>sysusersmanagement/savesysusers.do" Method="POST" class="form-horizontal form-bordered form-label-stripped" id="frmsysuserinfo">
									<div class="form-body">
										<div class="alert alert-danger display-hide">
											<button class="close" data-close="alert"></button>
											You have some form errors. Please check below.
										</div>
										<div class="alert alert-success display-hide">
											<button class="close" data-close="alert"></button>
											Your form validation is successful!
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">用户名<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="text" name="userName" placeholder="用户名" class="form-control" id="userName" value="${sysuser.userName }"/>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">密码<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="password" name="password" class="form-control" placeholder="密码" id="password" value="${sysuser.password }">
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">确认密码<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="password" name="confirmPWD" class="form-control" placeholder="确认密码" id="confirmPWD" value="${sysuser.password }">
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">中文姓名</label>
											<div class="col-md-9">
												<input type="text" name="chineseName" placeholder="中文姓名" class="form-control" id="chineseName" value="${sysuser.chineseName }"/>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">英文姓名</label>
											<div class="col-md-9">
												<input type="text" name="englishName" placeholder="英文姓名" class="form-control" id="englishName" value="${sysuser.englishName }"/>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3">移动电话<span class="required">*</span></label>
											<div class="col-md-9">
												<input type="text" name="mobilePhone" placeholder="移动电话" class="form-control" id="mobilePhone" value="${sysuser.mobilePhone }"/>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-3 control-label">Email地址<span class="required">*</span>
											</label>
											<div class="col-md-4">
												<div class="input-group">
													<span class="input-group-addon">
													<i class="fa fa-envelope"></i>
													</span>
													<input type="email" name="emailAddress" class="form-control" placeholder="Email地址" id="emailAddress" value="${sysuser.emailAddress }">
												</div>
											</div>
										</div>
									</div>
									<div class="form-actions">
										<div class="row">
											<div class="col-md-offset-3 col-md-9">
												<button type="submit" class="btn blue"><i class="fa fa-check"></i> 保存</button>
												<c:if test="${not empty sysuser.id}">
												<button id="btnChangePWD" type="button" class="btn blue" onclick="javascript:changePassword();"><i class="fa fa-gears"></i> 修改密码</button>
												</c:if>
												<button type="button" class="btn default" onclick="javascript:history.back();">取消</button>
											</div>
										</div>
									</div>
									<input type="hidden" name="userId" value="${sysuser.id }" id="userId" />
								</form>
								<form action="<%=basePath%>sysusersmanagement/deletesysusers.do" Method="POST" id="frmdeleteinfo">
									<input type="hidden" name="deleteId" value="${sysuser.id }" id="deleteId" />
								</form>
								<!-- END FORM-->
							</div>
						</div>
					</div>
				</div>
				<!-- modal -->
				<div class="modal fade" id="ajax" role="basic" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="row">
								<div class="col-md-12">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
										<h4 class="modal-title">密码修改</h4>
									</div>
									<div class="modal-body">
										<div class="scroller" style="height:200px" data-always-visible="1" data-rail-visible1="1">
											<div class="row">
												<div id="div_show_info" class="alert alert-danger display-hide">
												</div>
												<div class="form-group">
													<label class="control-label col-md-3">原始密码<span class="required">*</span></label>
													<div class="col-md-9">
														<input type="password" name="opassword" class="form-control" placeholder="密码" id="opassword" value="">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-3">新密码<span class="required">*</span></label>
													<div class="col-md-9">
														<input type="password" name="newPassword" class="form-control" placeholder="新密码" id="newPassword" value="">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-3">确认新密码<span class="required">*</span></label>
													<div class="col-md-9">
														<input type="password" name="confirmNewPWD" class="form-control" placeholder="确认新密码" id="confirmNewPWD" value="">
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" data-dismiss="modal" class="btn default">取消</button>
										<button type="button" class="btn blue" onclick="javascript:saveNewPWD()">保存</button>
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
<div class="page-footer">
	<div class="page-footer-inner">
	</div>
	<div class="scroll-to-top">
		<i class="icon-arrow-up"></i>
	</div>
</div>
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
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-validation/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-validation/js/additional-methods.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/select2/select2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-wysihtml5/wysihtml5-0.3.0.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-markdown/js/bootstrap-markdown.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-markdown/lib/markdown.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="<%=basePath%>assets/metronic/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/layout.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/demo.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/pages/scripts/form-samples.js"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/pages/scripts/form-validation.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
jQuery(document).ready(function() {    
   // initiate layout and plugins
   	Metronic.init(); // init metronic core components
	Layout.init(); // init current layout
	Demo.init(); // init demo features
  	FormSamples.init();
   	FormValidation.init();
   
   if ($("#userId").val()&&$("#userId").val()!=""){
	   $("#userName").attr("readOnly",true);
	   $("#password").attr("readOnly",true);
	   $("#confirmPWD").attr("readOnly",true);
	   $("#chineseName").attr("readOnly",true);
	   $("#englishName").attr("readOnly",true);
	   $("#mobilePhone").attr("readOnly",true);
	   $("#emailAddress").attr("readOnly",true);
   }
   
   if ($("#userId").val()==""){
	   $("#btnChangePWD".hide());
   }
   
   	$('#ajax').on('show.bs.modal', function (){
   		$("#div_show_info").hide();
   		$("#div_show_info").text("");
   		$("#opassword").val("");
   		$("#newPassword").val("");
   		$("#confirmNewPWD").val("");
	});
   
   var result="${result}";
	try{
		if (result){
			showMessage(result);
		}
	}catch(error){
	}
});

function modifyInfo(){
	$("#chineseName").attr("readOnly",false);
	$("#englishName").attr("readOnly",false);
	$("#mobilePhone").attr("readOnly",false);
	$("#emailAddress").attr("readOnly",false);
}

function deleteInfo(){
	if($("#deleteId").val()&&$("#deleteId").val()!=""){
		$("#frmdeleteinfo").submit();
	}
}

function changePassword(){
	 $("#ajax").modal('show');
}

function saveNewPWD(){
	$("#div_show_info").hide();
	if ($("#opassword").val()==""){
		$("#div_show_info").html("<span><font color='red'>请输入原始密码！</font></span>");
		$("#div_show_info").show();
		return;
	}
	if ($("#newPassword").val()==""){
		$("#div_show_info").text("请输入新密码！");
		$("#div_show_info").show();
		return;
	}else if($("#newPassword").val()!=$("#confirmNewPWD").val()){
		$("#div_show_info").text("确认新密码与新密码不同！");
		$("#div_show_info").show();
		return;
	}
	
	$.ajax({
		type : "POST",
		async : false,
		contentType : "application/json; charset=utf-8",
		url : "<%=basePath%>sysusersmanagement/changepassword.do",
		      data: "{'op':'"+$("#opassword").val()+"' , 'np':'"+$("#newPassword").val()+"','ud':'"+$("#userId").val()+"'}",
		      dataType: 'json',
		      success: function(result) {
		      	if (result.status==1){
		      		$("#ajax").modal('hide');
		      		alert(result.data);
		      	}else if (result.status==0){
		      		$("#div_show_info").text(result.data);
		    		$("#div_show_info").show();
		      	}
		      }
	});
}

var message;
function showMessage(msg){
	if (msg){
		message=msg;
	}
	bootbox.alert("<font size='4'>"+message+"</font>");
}
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>