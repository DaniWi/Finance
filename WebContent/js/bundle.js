/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	var changeSite = __webpack_require__(1).changeSite;

	document.getElementById("AccountButton").addEventListener("click",function(){
	    changeSite("http://localhost:8080/Finance/rest/user/accounts");
	});
	document.getElementById("NewTransactionButton").addEventListener("click", function(){
		changeSite("resources/NewTransaction.jsp");
	});
	document.getElementById("AllTransactionButton").addEventListener("click", function(){
		changeSite("http://localhost:8080/Finance/rest/user/transactions");
	});

/***/ },
/* 1 */
/***/ function(module, exports) {

	exports.changeSite = function(path) {
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				document.getElementById("content").innerHTML = xhttp.responseText;
				if(path === "resources/NewTransaction.jsp"){
					document.getElementById("transactionSubmit").addEventListener("submit", function(event){
						event.preventDefault();
						console.log("transaction");
						var from = document.getElementById("fromAccount");
						var fromAccount = from.options[from.selectedIndex].value;
						var to = document.getElementById("transferto");
						var toAccount = to.options[to.selectedIndex].value;
						var amount = document.getElementById("amount").value; 
						var userID = document.getElementById("userID").value;
						transaction(userID, fromAccount, toAccount, amount);
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

/***/ }
/******/ ]);