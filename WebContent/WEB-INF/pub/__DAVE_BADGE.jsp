<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:url var="assets" scope="page" value="/assets"></c:url> 

<div class="card card-user" style="border: 1px solid green; border-radius: 8px; border-bottom: 5px solid green;">
    <div class="card-image">
        <img src="${assets}/img/premeir-jet-1.jpg" alt="..." style="height: 130%;">
    </div>
    <div class="card-body">
        <div class="author">
            <a href="#">
                <img class="avatar border-gray" src="${assets}/img/tekzone.jpg" alt="...">
                <h5 class="title font-weight-bold" style="color: green; font-size: 1.8rem;">Developer Dave</h5>
            </a>
            <p class="description font-weight-bold d-none">
                Tekzone Kenya
            </p>
        </div>
        <p class="description text-center">
            Innovative solutions
            <br> for modern needs and 
            <br> superb productivity
        </p>
    </div>
    <hr>
    <div class="button-container mr-auto ml-auto">
        <button href="#" class="btn btn-simple btn-link btn-icon">
            <i class="fa fa-facebook-square"></i>
        </button>
        <button href="#" class="btn btn-simple btn-link btn-icon">
            <i class="fa fa-twitter"></i>
        </button>
        <button href="#" class="btn btn-simple btn-link btn-icon">
            <i class="fa fa-google-plus-square"></i>
        </button>
    </div>
</div>