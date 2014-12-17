(function(window, document, undefined) {
  var EditingEntryView = {};
  var editingEntryTemplate = document.getElementById('entry-template');
  var templates = {
  	renderEntry: Handlebars.compile(editingEntryTemplate.innerHTML)
  };

  /* Renders a view to allow the user to edit an entry. Requires the $entry
   * element and an object representing the active entry. */
  EditingEntryView.render = function($entry, activeEntryData) {
    var obj = {
    	editing: true,
    	entries: null,
    	activeEntryData: activeEntryData
    }
    entry.innerHTML = templates.renderEntry(obj);
    $(".update").click(function(){
    	var updatedEntry = Util.getNewEntry();
    	activeEntryData.name = updatedEntry.name;
    	activeEntryData.address = updatedEntry.address;
    	activeEntryData.description = updatedEntry.description;
    	EntryModel.update(activeEntryData, function(error, entries){
    		if(error){
    			$(".error").html(error);
    		} else{
    			EntryView.render(entry, activeEntryData);
    		}
    	});
    });
  };

  window.EditingEntryView = EditingEntryView;
})(this, this.document);
