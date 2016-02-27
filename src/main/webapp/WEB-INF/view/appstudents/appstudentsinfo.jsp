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
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>assets/metronic/assets/global/plugins/bootstrap-markdown/css/bootstrap-markdown.min.css">
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
							</li> -->
							<!-- <li>
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
				<li>
					<a href="javascript:;">
					<i class="icon-settings"></i>
					<span class="title">系统服务</span>
					<span class="arrow "></span>
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
				<li class="active open">
					<a href="javascript:;">
					<i class="icon-graduation"></i>
					<span class="title">学生学级管理</span>
					<span class="arrow open"></span>
					</a>
					<ul class="sub-menu">
						<li class="active">
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
					<h1>VCE课程管理系统-学生信息管理</h1>
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
									<span class="caption-subject font-blue-hoki bold uppercase">学生信息</span>
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
								<form action="<%=basePath%>appstudentsinfomanagement/savestudentinfo.do" class="form-horizontal" Method="POST" id="frmsysstudentinfo">
									<div class="form-body">
										<div class="alert alert-danger display-hide">
											<button class="close" data-close="alert"></button>
											You have some form errors. Please check below.
										</div>
										<div class="alert alert-success display-hide">
											<button class="close" data-close="alert"></button>
											Your form validation is successful!
										</div>
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">用户名<span class="required">*</span></label>
													<div class="col-md-9">
														<input type="text" name="userName" placeholder="用户名" class="form-control" id="userName" value="${sysuser.userName }"/>
													</div>
												</div>
											</div>
											<!--/span-->
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">英文姓名<span class="required">*</span></label>
													<div class="col-md-9">
														<input type="text" name="englishName" placeholder="英文姓名" class="form-control" id="englishName" value="${sysuser.englishName }"/>
													</div>
												</div>
											</div>
											<!--/span-->
										</div>
										<!--/row-->
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">中文姓名<span class="required">*</span></label>
													<div class="col-md-9">
														<input type="text" name="chineseName" placeholder="中文姓名" class="form-control" id="chineseName" value="${sysuser.chineseName }"/>
													</div>
												</div>
											</div>
											<!--/span-->
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">中文姓名拼音</label>
													<div class="col-md-9">
														<input type="text" name="pinyin" placeholder="中文姓名拼音" class="form-control" id="pinyin" value="${sysuser.pinyin }"/>
													</div>
												</div>
											</div>
											<!--/span-->
										</div>
										<!--/row-->
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">移动电话<span class="required">*</span></label>
													<div class="col-md-9">
														<input type="text" name="mobilePhone" placeholder="移动电话" class="form-control" id="mobilePhone" value="${sysuser.mobilePhone }"/>
													</div>
												</div>
											</div>
											<!--/span-->
											<div class="col-md-6">
												<div class="form-group">
													<label class="col-md-3 control-label">Email地址<span class="required">*</span>
													</label>
													<div class="col-md-9">
														<div class="input-group">
															<span class="input-group-addon">
															<i class="fa fa-envelope"></i>
															</span>
															<input type="email" name="emailAddress" class="form-control" placeholder="Email地址" id="emailAddress" value="${sysuser.emailAddress }">
														</div>
													</div>
												</div>
											</div>
											<!--/span-->
										</div>
										<!--/row-->
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">家庭住址</label>
													<div class="col-md-9">
														<input type="text" name="homeAddress" placeholder="家庭住址" class="form-control" id="homeAddress" value="${sysuser.homeAddress }"/>
													</div>
												</div>
											</div>
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">家庭电话</label>
													<div class="col-md-9">
														<input type="text" name="homePhone" placeholder="家庭电话" class="form-control" id="homePhone" value="${sysuser.homePhone }"/>
													</div>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">日校名称</label>
													<div class="col-md-9">
														<input type="text" name="daySchool" placeholder="日校名称" class="form-control" id="daySchool" value="${sysuser.daySchool }"/>
													</div>
												</div>
											</div>
											<!--/span-->
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">日校年级</label>
													<div class="col-md-9">
														<input type="text" name="daySchoolGrade" placeholder="日校年级" class="form-control" id="daySchoolGrade" value="${sysuser.daySchoolGrade }"/>
													</div>
												</div>
											</div>
											<!--/span-->
										</div>
										<!--/row-->
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">在日校学习中文<span class="required">*</span></label>
													<div class="col-md-9">
														<select class="js-states form-control" id="propertyIsLearnChineseId" name="propertyIsLearnChineseId" disabled></select>
													</div>
												</div>
											</div>
											<!--/span-->
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3"></label>
													<div class="col-md-9">
													</div>
												</div>
											</div>
											<!--/span-->
										</div>
										<!--/row-->
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">校区<span class="required">*</span></label>
													<div class="col-md-9">
														<select class="js-states form-control" id="studentGroupId" name="studentGroupId" disabled></select>
													</div>
												</div>
											</div>
											<!--/span-->
											<div class="col-md-6" id="div_select2_parent">
												<div class="form-group" id="div_select2_destroy">
													<label class="control-label col-md-3">班级<span class="required">*</span></label>
													<div class="col-md-9">
														<select class="js-states form-control" id="studentGroupClassId" name="studentGroupClassId" disabled></select>
													</div>
												</div>
											</div>
											<!--/span-->
										</div>
									</div>
									<div class="form-actions">
										<div class="row">
											<div class="col-md-6">
												<div class="row">
													<div class="col-md-offset-3 col-md-9">
														<button type="submit" class="btn blue"><i class="fa fa-check"></i> 保存</button>
														<button type="button" class="btn default" onclick="javascript:history.back();">取消</button>
													</div>
												</div>
											</div>
											<div class="col-md-6">
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
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-validation/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/jquery-validation/js/additional-methods.min.js"></script>
<script type="text/javascript" src="<%=basePath%>assets/metronic/assets/global/plugins/bootbox/bootbox.min.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN SELECT2 -->
<script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js"></script>
<!-- END SELECT2 -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="<%=basePath%>assets/metronic/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/layout.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/layout4/scripts/demo.js" type="text/javascript"></script>
<script src="<%=basePath%>assets/metronic/assets/admin/pages/scripts/form-validation.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
var $isLearnChinese, $studentGroup, $studentGroupClass;
var isLearnChineseData, propertyIsLearnChineseId, studentGroupData, studentGroupClassData;
jQuery(document).ready(function() {    
   // initiate layout and plugins
   	Metronic.init(); // init metronic core components
	Layout.init(); // init current layout
	Demo.init(); // init demo features
   	//FormValidation.init();
	
	addValidation();
   
   if ($("#userId").val()&&$("#userId").val()!=""){
	   $("#userName").attr("readOnly",true);
	   $("#pinyin").attr("readOnly",true);
	   $("#homeAddress").attr("readOnly",true);
	   $("#homePhone").attr("readOnly",true);
	   $("#chineseName").attr("readOnly",true);
	   $("#englishName").attr("readOnly",true);
	   $("#mobilePhone").attr("readOnly",true);
	   $("#emailAddress").attr("readOnly",true);
	   
	   $("#daySchool").attr("readOnly",true);
	   $("#daySchoolGrade").attr("readOnly",true);
	   
	   $("#studentGroupId").attr("disabled",true);
	   $("#studentGroupClassId").attr("disabled",true);
	   
   }
   
 	//initiate select2
	$isLearnChinese=$("#propertyIsLearnChineseId");
	$studentGroup=$("#studentGroupId");
	$studentGroupClass=$("#studentGroupClassId");
	
	//changing property id here
	propertyIsLearnChineseId=5;
	
	$.ajax({
       type: "POST",
       async:false,
       contentType: "application/json; charset=utf-8",
       url: "<%=basePath%>appstudentsinfomanagement/findpropertyislearnchinese.do",
       data: "{'userId':'"+$("#userId").val()+"','propertyId':'"+propertyIsLearnChineseId+"'}",
       dataType: 'json',
       success: function(result) {
    	   isLearnChineseData=result.data;
       }
   });
	
	$isLearnChinese.select2({
		data:isLearnChineseData,
		placeholder: "是否在日校学习中文"
	});
	
	var flag=false;
	$.each(isLearnChineseData,function(idx,item){
		if (item.selected&&item.selected==="selected"){
			flag=true;
		}
	});
	
	if (!flag){
		$isLearnChinese.val(null).trigger("change");
		flag=false;
	}
	
	$.ajax({
       type: "POST",
       async:false,
       contentType: "application/json; charset=utf-8",
       url: "<%=basePath%>appstudentsinfomanagement/findstudentgroup.do",
       data: "{'userId':'"+$("#userId").val()+"'}",
       dataType: 'json',
       success: function(result) {
    	   studentGroupData=result.data;
       }
   });
	
	$studentGroup.select2({
		data:studentGroupData,
		placeholder: "选择学生所在校区"
	});
	
	//add select2 event listener
	$studentGroup.on("select2:select", function (e) { changeGroupClassData($studentGroup.val()); });
	
	
	$studentGroupClass.select2({
		//data:studentGroupClassData,
		placeholder: "选择学生所在班级"
	});
	
	$.each(studentGroupData,function(idx,item){
		if (item.selected&&item.selected==="selected"){
			flag=true;
			changeGroupClassData(item.id);
		}
	});
	if (!flag){
		$studentGroup.val(null).trigger("change");
		flag=false;
	}
	
   var result="${result}";
	try{
		if (result){
			showMessage(result);
		}
	}catch(error){
	}
});

function addValidation(){
	var form3 = $('#frmsysstudentinfo');
    var error3 = $('.alert-danger', form3);
    var success3 = $('.alert-success', form3);

    form3.validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block help-block-error', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        ignore: "", // validate all fields including form hidden input
        rules: {
        	userName:{
        		required:true
        	},
        	chineseName:{
        		required:true
        	},
        	englishName:{
        		required:true
        	},
        	emailAddress: {
                required: true,
                email: true
            },
            mobilePhone:{
            	required: true,
            	digits: true
            }
        },

        messages: { // custom messages for radio buttons and checkboxes
            membership: {
                required: "Please select a Membership type"
            },
            service: {
                required: "Please select  at least 2 types of Service",
                minlength: jQuery.validator.format("Please select  at least {0} types of Service")
            }
        },

        errorPlacement: function (error, element) { // render error placement for each input type
            if (element.parent(".input-group").size() > 0) {
                error.insertAfter(element.parent(".input-group"));
            } else {
                error.insertAfter(element); // for other inputs, just perform default behavior
            }
        },

        invalidHandler: function (event, validator) { //display error alert on form submit   
            success3.hide();
            error3.show();
            Metronic.scrollTo(error3, -200);
        },

        highlight: function (element) { // hightlight error inputs
           $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            $(element).closest('.form-group').removeClass('has-error'); // set error class to the control group
        },

        success: function (label) {
            label.closest('.form-group').removeClass('has-error'); // set success class to the control group
        },

        submitHandler: function (form) {
            success3.show();
            error3.hide();
            
            var canSubmit=true;
            if (!$("#propertyIsLearnChineseId").val()){
            	success3.hide();
                error3.show();
                Metronic.scrollTo(error3, -200);
                alert("请选择学生是否在日校学习中文");
                canSubmit=false;
            }
            if (!$("#studentGroupId").val()){
            	success3.hide();
                error3.show();
                Metronic.scrollTo(error3, -200);
                alert("请选择学生所在校区");
                canSubmit=false;
            }
            if (!$("#studentGroupClassId").val()){
            	success3.hide();
                error3.show();
                Metronic.scrollTo(error3, -200);
                alert("请选择学生所在班级");
                canSubmit=false;
            }
            if (canSubmit){
            	form[0].submit(); // submit the form
            }
        }
    });
}

function changeGroupClassData(gid){
	$("#div_select2_destroy").remove();
	$("#div_select2_parent").append('<div class="form-group" id="div_select2_destroy"><label class="control-label col-md-3">班级<span class="required">*</span></label><div class="col-md-9"><select class="js-states form-control" id="studentGroupClassId" name="studentGroupClassId"></select></div>');
	
	$.ajax({
       type: "POST",
       async:false,
       contentType: "application/json; charset=utf-8",
       url: "<%=basePath%>appstudentsinfomanagement/findstudentgroup.do",
       data: "{'userId':'"+$("#userId").val()+"','groupId':'"+gid+"'}",
       dataType: 'json',
       success: function(result) {
    	   studentGroupClassData=result.data;
       }
  	});
	
	$("#studentGroupClassId").select2({
		data:studentGroupClassData,
		placeholder: "选择学生所在班级"
	});
	
	var flag=false;
	$.each(studentGroupClassData,function(idx,item){
		if (item.selected&&item.selected==="selected"){
			flag=true;
		}
	});
	if (!flag){
		$("#studentGroupClassId").val(null).trigger("change");
	}
	
	$("#studentGroupClassId").attr("disabled", $("#studentGroupId").attr("disabled"));
}

function modifyInfo(){
   	$("#pinyin").attr("readOnly",false);
   	$("#homeAddress").attr("readOnly",false);
   	$("#homePhone").attr("readOnly",false);
   	$("#chineseName").attr("readOnly",false);
   	$("#englishName").attr("readOnly",false);
   	$("#mobilePhone").attr("readOnly",false);
   	$("#emailAddress").attr("readOnly",false);
   
   	$("#daySchool").attr("readOnly",false);
   	$("#daySchoolGrade").attr("readOnly",false);
   
   	$("#studentGroupId").attr("disabled",false);
   	$("#studentGroupClassId").attr("disabled",false);
	$("#propertyIsLearnChineseId").attr("disabled",false);
}

function deleteInfo(){
	if($("#deleteId").val()&&$("#deleteId").val()!=""){
		$("#frmdeleteinfo").submit();
	}
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