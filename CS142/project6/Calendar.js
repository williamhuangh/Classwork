function Calendar(id){
	this.idAttribute = id;
};

Calendar.prototype.render = function(date) {
	var self = this;
	this.month = date.getMonth();
	date.setDate(1);
	var current_calendar = document.getElementById(this.idAttribute);
	var endMonth = false;
	var counter = 0;
	var includePrevMonth = false;
	// Setting up the headers of the calendar
	var str = "<table><tbody>"
	str += "<tr><th colspan = '7'>" + (date.getMonth()+1) + "/" + date.getFullYear() + "</th></tr>";
	str += "<tr><th>Su</th><th>Mo</th><th>Tu</th><th>We</th><th>Th</th><th>Fr</th><th>Sa</th></tr>";
	// Including some dates from the previous month
	if (date.getDay() != 0) {
		includePrevMonth = true;
		date.setDate(0);
		while(date.getDay() != 0){
			date.setDate(date.getDate()-1);
		}
	}
	while(!endMonth){
		str += "<tr>";
		var i = 0;
		while(i < 7){ // Every 7 days, go to a new line
			var day = date.getDay();
			if(this.month === date.getMonth()){
				str += "<td>" + date.getDate() + "</td>";
			} else{ // If these are dates not in the same month
				str += "<td class = 'dim'>" + date.getDate() + "</td>";
			}
			date.setDate(date.getDate()+1);
			if (date.getDate() === 1 && !endMonth) {
				if(!includePrevMonth || counter != 0){ // Break the loop with the next 1-date seen
					endMonth = true;
				} else{
					counter++;
				}
			}
			i++;
		}
		str += "</tr>";
	}
	// Setting up previous month numbers
	date.setMonth(date.getMonth()-1);
	var prev_date = date.getDate();
	var prev_month = date.	getMonth();
	var prev_year = date.getFullYear();
	if(prev_month === 0){
		prev_month = 12;
		prev_year--;
	}
	// Setting up next mont numbers
	date.setMonth(date.getMonth()+2);
	var next_date = date.getDate();
	var next_month = date.getMonth();
	var next_year = date.getFullYear();
	if(next_month === 0){
		next_month = 12;
		next_year--;
	}
	// Putting in html for the < and > buttons
	str += "<tr><td id = '" + this.idAttribute + "back" + "'>&lt;</td><td></td><td></td><td></td><td></td><td></td><td id = '" + this.idAttribute + "next" + "'>&gt;</td></tr>";
	str += "</tbody></table>";
	current_calendar.innerHTML = str;
	var less_than = this.idAttribute + "back";
	var go_back = document.getElementById(less_than);
	go_back.onclick = function(){ // render new date
		self.render(new Date(prev_year + "," + prev_month + "," + prev_date));
	}
	var greater_than = this.idAttribute + "next";
	var go_forward = document.getElementById(greater_than);
	go_forward.onclick = function(){ // render new date
		self.render(new Date(next_year + "," + next_month + "," + next_date));
	}
};