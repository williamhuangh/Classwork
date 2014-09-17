function new_request(text_id){
	var parameter = document.getElementById(text_id).value;
	var xhr = new XMLHttpRequest();
  	xhr.onreadystatechange = xhrHandler;
  	if(parameter){
  		xhr.open("GET", "/users/search?search_string=" + encodeURIComponent(parameter), true);
  	} else{
  		xhr.open("GET", "/users/search", true);
  	}
  	xhr.send();
}

function xhrHandler(){
	if(this.readyState != 4){
		return;
	}
	if(this.status != 200){
		return;
	}
	var objects = JSON.parse(this.responseText);
	var show_div = document.getElementById("search_photos");
	show_div.innerHTML = "";
	show_photos(objects, show_div);
}

function show_photos(photos, show_div){
	if(photos){
		for (var i = 0; i < photos.length; i++){
			//show_div.innerHTML += "<a href='/users/" + photos[i].user_id + ">";
			var a = document.createElement("a");
			a.href = "/users/" + photos[i].user_id + "#" + photos[i].id;
			var new_elem = document.createElement("img");
			new_elem.setAttribute("src", "/assets/" + photos[i].file_name);
			new_elem.style['max-width'] = "50px";
			new_elem.style['max-height'] = "50px";
			a.appendChild(new_elem);
			//new_elem.link("/users/" + photos[i].user_id);
			show_div.appendChild(a);
			var new_line = document.createElement("br");
			show_div.appendChild(new_line);
			//show_div.innerHTML += "</a>";
		}
	}
}