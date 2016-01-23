exports.changeSite = function(name) {
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			document.getElementById("content").innerHTML = xhttp.responseText;
		}
	}
	xhttp.open("GET", "resources/"+name+".html", true);
	xhttp.send();
}