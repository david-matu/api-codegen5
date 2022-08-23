<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:url var="assets" scope="page" value="/assets"></c:url> 
<div class="sidebar" data-color="blue" data-image="${assets}/img/sidebar-4.jpg">
            <!--
        Tip 1: You can change the color of the sidebar using: data-color="purple | blue | green | orange | red"

        Tip 2: you can also add an image using data-image tag
    -->
            <div class="sidebar-wrapper" > <!-- style="background-color: green;"  -->
                <div class="logo text-center">
                	<img class="avatar border-gray w-50 h-50 rounded" src="${assets}/img/tekzone.jpg" alt="Tekzone">
                	<br>
                    <a href="${pageContext.request.contextPath }/" class="h3 font-weight-bold">
                        CodePower
                    </a>
                </div>
                <ul class="nav">
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath }/">
                            <i class="nc-icon nc-globe-2"></i>
                            <strong>Dashboard</strong>
                        </a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" disabled href="${pageContext.request.contextPath }/projects">
                            <i class="nc-icon nc-notes"></i>
                            <p>Projects</p>
                        </a>
                    </li>
                    
                    <%-- <c:forEach items="${projectList}" var="p">
                    <li class="nav-item active">
                        <a class="nav-link" href="${pageContext.request.contextPath }/projects/${p.projectID}">
                        	<i class="nc-icon nc-mobile"></i>
                            <p>${p.name }</p>
                        </a>
                    </li>
                    <li> <hr class="bg-white"> </li>
                    
                    </c:forEach> --%>                    
                    <li>
                    	<c:choose>
                    	<c:when test="${model != null }">
	                    	<label class="nav-link"><u>Code Sections</u></label>
	                        <a class="nav-link" href="#gen-class">
	                            <strong>Code: Model Class</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-crud-create">
	                            <strong>CRUD Create</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-crud-read">
	                            <strong>CRUD Read (Single)</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-crud-read-list">
	                            <strong>CRUD Read (List)</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-crud-update">
	                            <strong>CRUD Update</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-servlet-create">
	                            <strong>Servlet: Create</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-servlet-read">
	                            <strong>Servlet: Read /S</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-servlet-read-list">
	                            <strong>Servlet: Read /L</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-servlet-update">
	                            <strong>Servlet: Update</strong>
	                        </a>
                        <hr class="bg-white">
                        	<a class="nav-link" href="#gen-html-create">
	                            <strong>HTML: Create</strong>
	                        </a>
	                        <a class="nav-link" href="#gen-html-update">
	                            <strong>HTML: Update</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-html-read-single">
	                            <strong>HTML: Read /S</strong>
	                        </a>
	                        <a class="nav-link" href="#gen-html-read-list">
	                            <strong>HTML: Read /L</strong>
	                        </a>
                        </c:when>
                        </c:choose>
                    </li>
                    
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath }/b2c">
                            <i class="nc-icon nc-layers-3"></i>
                            <p>Developer Dave</p>
                        </a>
                        <hr class="bg-white">
                    </li>
                    
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath }/about">
                            <i class="nc-icon nc-refresh-02"></i>
                            <p>About Codegen</p>
                        </a>
                        <hr class="bg-white">
                    </li>
                    
                    <li>
                    	<jsp:include page="__DAVE_BADGE.jsp"></jsp:include>
                    </li>
                </ul>
            </div>
        </div>