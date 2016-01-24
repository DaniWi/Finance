var changeSite = require("./content.js").changeSite;

document.getElementById("AccountButton").addEventListener("click",function(){
    changeSite("http://localhost:8080/Finance/rest/user/accounts");
});
document.getElementById("NewTransactionButton").addEventListener("click", function(){
	changeSite("resources/NewTransaction.jsp");
});
document.getElementById("AllTransactionButton").addEventListener("click", function(){
	changeSite("http://localhost:8080/Finance/rest/user/transactions");
});
function transaction() {
	var userID = document.getElementById("userID").value;
	var fromAccount = document.getElementById("fromAccount").value;
	var toAccount = document.getElementById("transferto").value;
	var amount = document.getElementById("amount").value;
	transaction(userID, fromAccount, toAccount, amount);
}