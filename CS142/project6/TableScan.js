function TableScan(){}

TableScan.sumColumn = function(id, str){
	var full_table = document.getElementById(id);
	if(full_table === null) return 0; // If table does not exist
	var table = full_table.rows;
	var first_row = table[0].cells;
	var found = false;
	var counter = 0;
	var total = 0;
	while(counter < first_row.length){ // Finding str in header of table
		if(first_row[counter].textContent === str){
			found = true;
			break;
		}
		counter++;
	}
	if(!found){ // If str not found
		return("0");
	} else{
		var row_counter = 1;
		while(row_counter < table.length){ // Summing all valid numbers
			var curr_row = table[row_counter].cells;
			if((curr_row[counter] != null) && !isNaN(curr_row[counter].textContent)){
				total += parseFloat(curr_row[counter].textContent);
			}
			row_counter++;
		}
		return(Math.round(total * 1e2)/1e2);
	}
}

// Function post gets called when the submit button is clicked
function post(){
	var arg_1 = document.getElementById("table_input").value;
	var arg_2 = document.getElementById("column_input").value;
	var display = TableScan.sumColumn(arg_1, arg_2);
	var replacing = document.getElementById("statement");
	replacing.innerHTML = "Column " + arg_2 + " in table " + arg_1 + " sums to " + display;
}
