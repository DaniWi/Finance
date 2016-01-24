<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*,java.util.*,data.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Finance 4</title>
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet">
		
		<!-- Custom styles for this template -->
		<link href="css/sticky-footer-navbar.css" rel="stylesheet">
		
		<!-- Own css for item design etc -->
		<link rel="stylesheet" href="css/styles.css">
	</head>
	<body id="page_index">

		<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
			<div class="container">
			  <!-- Brand and toggle get grouped for better mobile display -->
			  <div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
						 <span class="sr-only">Toggle navigation</span>
						 <span class="icon-bar"></span>
						 <span class="icon-bar"></span>
						 <span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="/Finance/">Finance 4</a>
			  </div>

			  <!-- Collect the nav links, forms, and other content for toggling -->
			  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav navbar-right">
						 <% if(session.getAttribute("Username") == null) {%>
						 <li>
							  <a href="login.html" title="Login Page"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> Login </a>
						 </li>
						 <%} else { %>
						 <li>
						 	<a href="logout.jsp" title="Logout"><span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span> Logout (<%=session.getAttribute("Username") %>) </a>
						 </li>
						<% } %>
					</ul>
			  </div>
			  <!-- /.navbar-collapse -->
			</div>
			<!-- /.container -->
		</nav>
	
		<!-- Begin page content -->
		<div class="container">
		<% if(session.getAttribute("Username") == null) {%>
	  	<div class="page-header">
			<h1>Please login to Finace 4</h1>
 		</div>
		<%} else { %>
		<button id="AccountButton" class="btn btn-primary">Accounts</button>
		<button id="NewTransactionButton" class="btn btn-primary">New Transaction</button>
		<button id="AllTransactionButton" class="btn btn-primary">All Transaction</button>
		
  		<div class="page-header">
			<h1>Hello <%= session.getAttribute("Username") %>, this is Finance 4.</h1>
  		</div>
  		<div id="content">
		</div>
		<% } %>
		</div>

		<footer class="footer">
		  <div class="container">
			<p class="text-muted">&copy; 2016 Nocker/Witsch. All rights reserved.</p>
		  </div>
		</footer>
	
		<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<!-- Include all compiled plugins (below), or include individual files as needed -->
		<script src="js/bootstrap.min.js"></script>
		<script type="text/javascript" src="js/bundle.js" charset="utf-8"></script>
	</body>
</html>