function Tagger(elem, feedback, x_val, y_val, wid, ht){
	this.isMouseDown = false;
	this.element = document.getElementById(elem);
	this.feedback = document.getElementById(feedback);
	this.feedback.style.visibility = "hidden";
	this.x_val = document.getElementById(x_val);
	this.y_val = document.getElementById(y_val);
	this.wid = document.getElementById(wid);
	this.ht = document.getElementById(ht);
	var obj = this;
	this.element.onmousedown = function(event){
		obj.mouseDown(event);
	}
}

Tagger.prototype.mouseDown = function(event){
	event.preventDefault();
	var obj = this;
	this.feedback.style.visibility = "visible";
	this.x_val.value = event.pageX - this.element.offsetLeft;
	this.y_val.value = event.pageY - this.element.offsetTop;
	this.wid.value = 0;
	this.ht.value = 0;
	this.feedback.style.width = 0;
	this.feedback.style.height = 0;
	this.originalLeft = event.pageX - this.element.offsetLeft;
	this.originalTop = event.pageY - this.element.offsetTop;
	this.feedback.style.left = (event.pageX - this.element.offsetLeft) + "px";
	this.feedback.style.top = (event.pageY - this.element.offsetTop) + "px";
	this.isMouseDown = true;
	document.body.onmousemove = function(event){
		obj.mouseMove(event);
	}
	document.body.onmouseup = function(event){
		obj.mouseUp(event);
	}
}

Tagger.prototype.mouseMove = function(event){
	event.preventDefault();
	if(!this.isMouseDown){
		return;
	}
	// Div is slightly bigger than the image itself, so the 5 px on the bottom corrects for it.
	if(event.pageX - this.element.offsetLeft >= this.element.offsetWidth-3){
		this.wid.value = this.element.offsetWidth-3 - this.originalLeft;
		this.feedback.style.width = this.wid.value + "px";
	} else if(event.pageX - this.element.offsetLeft <= 0){
		this.wid.value = this.originalLeft - this.element.offsetLeft+5;
		this.feedback.style.width = this.wid.value + "px";
	} else if((event.pageX - this.element.offsetLeft) - this.originalLeft >= 0){
		this.wid.value = (event.pageX - this.element.offsetLeft) - this.originalLeft;
		this.feedback.style.width = this.wid.value + "px";
	} else if ((event.pageX - this.element.offsetLeft) - this.originalLeft < 0){
		this.feedback.style.left = (event.pageX - this.element.offsetLeft) + "px";
		this.wid.value = this.originalLeft - (event.pageX - this.element.offsetLeft);
		this.feedback.style.width = this.wid.value + "px";
	}
	if(event.pageY - this.element.offsetTop >= this.element.offsetHeight-5){
		this.ht.value = this.element.offsetHeight-5 - this.originalTop;
		this.feedback.style.height = this.ht.value + "px";
	} else if (event.pageY - this.element.offsetTop <= 0){
		this.ht.value = this.originalTop - this.element.offsetTop;
		this.feedback.style.height = this.ht.value + "px";
	} else if ((event.pageY - this.element.offsetTop) - this.originalTop >= 0){
		this.ht.value = (event.pageY - this.element.offsetTop) - this.originalTop;
		this.feedback.style.height = this.ht.value + "px";
	} else if ((event.pageY - this.element.offsetTop) - this.originalTop < 0){
		this.feedback.style.top = (event.pageY - this.element.offsetTop) + "px";
		this.ht.value = this.originalTop - (event.pageY - this.element.offsetTop);
		this.feedback.style.height = this.ht.value + "px";
	}
}

Tagger.prototype.mouseUp = function(event){
	event.preventDefault();
	this.isMouseDown = false;
}