(function(window, document, undefined) {
  var EntryView = {};

  var entryTemplate = document.getElementById('entry-template');
  var templates = {
  	renderEntry: Handlebars.compile(entryTemplate.innerHTML)
  };

  /* Renders an entry into the given $entry element. Requires the object
   * representing the active entry (activeEntryData). If this object is null,
   * picks the first existing entry. If no entry exists, this view will display
   * the CreatingEntryView. */
  EntryView.render = function($entry, activeEntryData) {
    obj = {
      viewing: true,
      entries: [],
      activeEntryData: []
    }
    var allEntries = Util.getAllEntries();
    if (allEntries.length === 0) {
      CreatingEntryView.render(entry);
    }
    if (!activeEntryData) {
      activeEntryData = allEntries[0];
    }
    obj.entries = allEntries;
    obj.activeEntryData = activeEntryData;
    entry.innerHTML = templates.renderEntry(obj);

    $(".new").click(function(){
      CreatingEntryView.render(entry);
    });
    $(".edit").click(function(){
      EditingEntryView.render(entry, activeEntryData);
    });
    $(".delete").click(function(){
      EntryModel.remove(activeEntryData.id, function(error, entries){
        if(error){
          $(".error").html(error);
        } else{
          var newAllEntries = Util.getAllEntries();
          if (newAllEntries.length === 0) {
            CreatingEntryView.render(entry);
          } else{
            EntryView.render(entry, newAllEntries[0]);
          }
        }
      });
    });
    $("#selector").change(function(){
      var selected = $(this).find(":selected").val();
      for(var i = 0; i < allEntries.length; i++){
        var selectedActiveEntry = allEntries[i];
        if (selected == selectedActiveEntry.id) {
          EntryView.render(entry, selectedActiveEntry);
          break;
        }
      }
    });
    mapObj = $(".map");
    GoogleMapView.render(mapObj, activeEntryData);
  };

  window.EntryView = EntryView;
})(this, this.document);
