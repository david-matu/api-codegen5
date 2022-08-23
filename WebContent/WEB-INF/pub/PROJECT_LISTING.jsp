<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:url var="assets" scope="page" value="/assets"></c:url> 
<%-- <c:url var="assets" scope="page" value="http://d3b9eqxzrvw9g6.cloudfront.net"></c:url> --%> 

<!--
=========================================================
 Light Bootstrap Dashboard - v2.0.1
=========================================================

 Product Page: https://www.creative-tim.com/product/light-bootstrap-dashboard
 Copyright 2019 Creative Tim (https://www.creative-tim.com)
 Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard/blob/master/LICENSE)

 Coded by Creative Tim

=========================================================

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.  -->
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <link rel="apple-touch-icon" sizes="76x76" href="${assets}/img/apple-icon.png">
    <link rel="icon" type="image/png" href="${assets}/img/favicon.ico">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>Projects | CodePower ~ Dave</title>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
    <!--     Fonts and icons     -->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700,200" rel="stylesheet" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" />
    <!-- CSS Files -->
    <link href="${assets}/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${assets}/css/light-bootstrap-dashboard.css?v=2.0.0 " rel="stylesheet" />
    <!-- CSS Just for demo purpose, don't include it in your project -->
    <link href="${assets}/css/demo.css" rel="stylesheet" />
</head>

<body>
    <div class="wrapper">
        <jsp:include page="__sidebar.jsp"></jsp:include>
        
        <div class="main-panel">
            <!-- Navbar -->
            <jsp:include page="__navbar.jsp"></jsp:include>
            <!-- End Navbar -->
            
            <div class="content">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-12 mx-0 px-0 px-md-3">
                        	<h3 class="card shadow bg-primary text-white font-weight-bold p-2 mt-0">Projects</h3>                            
					        <c:forEach items="${projectList }" var="p">
					        	<div id="parent-${p.projectID }">
				                <a href="${pageContext.request.contextPath }/projects/${p.projectID}" class="list-group-item-action" aria-current="true">
				                  <div class="card">
					                  <div class="card-header d-md-flex w-100 justify-content-between p-md-3" style="background: forestgreen; color: #FFF;">
					                    <h3 class="mb-1 font-weight-bold">${p.name }</h3>
					                    <small class="date-created font-weight-bold" title="${p.dateCreated }">${p.dateCreated }</small>
					                  </div>
					                  <div class="card-body">
					                  <p class="mb-1">${p.description }</p>
					                  <small>Resident Database: <strong class="text-dark">${p.dbName }</strong></small>
					                  <a href="${pageContext.request.contextPath }/delete" data-asset-name="${p.name }" data-id="${p.projectID }" data-table="PROJECT" data-key="PROJECT_ID" class="delete-btn btn btn-sm btn-danger btn-fill">Delete</a>
					                  </div>
				                  </div>
				                </a>
				                </div>
			                </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            
            <jsp:include page="__footer.jsp"></jsp:include>
        </div>
    </div>
</body>
<!--   Core JS Files   -->
<script src="${assets}/js/core/jquery.3.2.1.min.js" type="text/javascript"></script>
<script src="${assets}/js/core/popper.min.js" type="text/javascript"></script>
<script src="${assets}/js/core/bootstrap.min.js" type="text/javascript"></script>
<!--  Plugin for Switches, full documentation here: http://www.jque.re/plugins/version3/bootstrap.switch/ -->
<script src="${assets}/js/plugins/bootstrap-switch.js"></script>
<!--  Google Maps Plugin    -->
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
<!--  Chartist Plugin  -->
<script src="${assets}/js/plugins/chartist.min.js"></script>
<!--  Notifications Plugin    -->
<script src="${assets}/js/plugins/bootstrap-notify.js"></script>
<!-- Control Center for Light Bootstrap Dashboard: scripts for the example pages etc -->
<script src="${assets}/js/light-bootstrap-dashboard.js?v=2.0.0 " type="text/javascript"></script>
<!-- Light Bootstrap Dashboard DEMO methods, don't include it in your project! -->
<script src="${assets}/js/jquery.timeago.js"></script>
<script src="${assets}/js/demo.js"></script>

<script>
	$(document).ready(function(){
		jQuery("small.date-created").timeago();
	})
</script>
</html>