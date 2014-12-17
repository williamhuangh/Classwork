(function(window, document, undefined) {
  var Util = {};

  Util.getAllEntries = function(){
       var allEntries = null;
       EntryModel.loadAll(function(error, entries){
          if (error){
            console.log("Error: ", error);
          } else{
            allEntries = entries;
          }
       });
       return allEntries;
   }

   Util.getNewEntry = function(){
    var newEntry = {};
    newEntry.name = $("input[name='name']").val();
    newEntry.address = $("input[name='address']").val();
    newEntry.description = $('textarea').val();
    return newEntry;
   }

  window.Util = Util;
})(this, this.document);
