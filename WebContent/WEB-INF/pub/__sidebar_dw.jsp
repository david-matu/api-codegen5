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
                        CodePower <br>
                        Dropwizard Code
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
	                        <a class="nav-link" href="#gen-entity">
	                            <strong>Entity</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-mapper">
	                            <strong>Mapper</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-dao">
	                            <strong>DAO</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-service">
	                            <strong>Service</strong>
	                        </a>
	                        
	                        <a class="nav-link" href="#gen-resource">
	                            <strong>Resource</strong>
	                        </a>
	                        
	                        
                        <hr class="bg-white">
                        	
	                        <a class="nav-link" href="#gen-html-read-list">
	                            <strong>Misc</strong>
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