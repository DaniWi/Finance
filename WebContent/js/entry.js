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