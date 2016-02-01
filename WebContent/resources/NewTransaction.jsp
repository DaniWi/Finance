<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*,java.util.*,data.*"%>
<%
IDataHandler dataHandler = DataHandler.getInstance();
%>


<!-- onsubmit oder action, beides schon probiert, method="POST" bereits entfernt -->

<form id="transactionSubmit">
	<fieldset class="form-group">
    	<label for="fromAccount">Choose your account</label>
     	<select class="form-control" id="fromAccount" name="fromAccount" required>
	        <%
	    	for (Account account : dataHandler.getAllAccountFromUser((int)session.getAttribute("ID"))) {
	    		%><option value="<%= account.getId() %>"><%= account.getName() %></option>
	    	<% } %>
     	</select>
	</fieldset>
  	<fieldset class="form-group">
    	<label for="transferto">Transfer to</label>
      	<select multiple class="form-control" id="transferto" name="transferto" required>
	        <%
	    	for (User user : dataHandler.getAllUsers()) {
	    		for (Account account : dataHandler.getAllAccountFromUser(user.getId())) {
	    			%><option value="<%= account.getId() %>"><%= user.getName() + ": " + account.getName()%></option> <%
	    		}
	    		
	    	} %>
     	</select>
  	</fieldset>
	<fieldset class="form-group">
    	<label for="amount">Amount</label>       
       	<input type="number" step="0.01" class="form-control" id="amount" name="amount" placeholder="Amount in &euro;" required>
   	</fieldset>
   	<input type="hidden" id="userID" name="userID" value="<%= session.getAttribute("ID") %>">
   	<fieldset class="form-group">       
       	<button type="submit" class="btn btn-primary">Transfer cash</button>
   	</fieldset>
</form>