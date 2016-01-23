var changeSite = require("./content.js").changeSite;

document.getElementById("AccountButton").addEventListener("click",function(){
    changeSite("Accounts");
});
document.getElementById("NewTransactionButton").addEventListener("click", function(){
    changeSite("NewTransaction");
});
document.getElementById("AllTransactionButton").addEventListener("click", function(){
    changeSite("Transactions");
});
document.getElementById("page_index").addEventListener("click", function(){
    changeSite("Accounts");
});
