exports.changeSite = function(path) {
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			document.getElementById("content").innerHTML = xhttp.responseText;
			if(path === "resources/NewTransaction.jsp"){
				document.getElementById("transactionSubmit").addEventListener("submit", function(event){
					event.preventDefault();
					console.log("transaction");
					var fromAccountID = document.getElementById("fromAccount").value;
					var toAccountID = document.getElementById("transferto").value;
					var amount = document.getElementById("amount").value; 
					var userID = document.getElementById("userID").value;
					transaction(userID, fromAccountID, toAccountID, amount);
				});
			}
		}
	}
	xhttp.open("GET", path, true);
	xhttp.send();
	document.getElementById("content").innerHTML = "Fetching Data ..."
}

transaction = function(userID, fromAccount, toAccount, amount) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			document.getElementById("content").innerHTML = xhttp.responseText
		}
	}
	var params = "userID=" + userID + "&fromAccount=" + fromAccount + "&toAccount=" + toAccount + "&amount=" + amount;
	xhttp.open("POST", "http://localhost:8080/Finance/rest/user/transaction/new", true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send(params);
	document.getElementById("content").innerHTML = "Processing Transaction ..."
}