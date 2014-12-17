(function(window, document, undefined) {
  var CreatingEntryView = {};
  var entryTemplate = document.getElementById('entry-template');
  var templates = {
  	renderEntry: Handlebars.compile(entryTemplate.innerHTML)
  };
  /* Renders a view to allow the user to create an entry. Requires the $entry
   * element. */
  CreatingEntryView.render = function($entry) {
    obj = {
    	creating: true,
    	entries: null,
    	activeEntryData: null
    };
    entry.innerHTML = templates.renderEntry(obj);

    $(".add").click(function(){
    	var newEntry = Util.getNewEntry();
      EntryModel.add(newEntry, function(error, entries){
        if(error){
          $(".error").html(error);
        } else{
          EntryView.render(entry, entries);
        }
      });
    });
  };

  window.CreatingEntryView = CreatingEntryView;
})(this, this.document);
