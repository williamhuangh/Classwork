(function(window, document, undefined) {
  var EntryModel = {};

  var ENTRIES_URL = 'http://callbackjs.me:4155/entries';
  var STATUS_OK = 200;

  /* Loads all entries from the server.
   *
   * Calls: callback(error, entries)
   *  error -- the error that occurred or NULL if no error occurred
   *  entries -- an array of entries
   */
  EntryModel.loadAll = function(callback) {
    var request = new XMLHttpRequest();
    request.addEventListener('load', function(){
      if(request.status != STATUS_OK){
        callback(request.responseText);
      }
    });
    request.open('GET', ENTRIES_URL, false);
    request.send();
    callback(null, JSON.parse(request.responseText));
  };

  /* Adds the given entry to the list of entries. The entry must *not* have
   * an id associated with it.
   *
   * Calls: callback(error, entry)
   *  error -- the error that occurred or NULL if no error occurred
   *  entry -- the entry added, with an id attribute
   */
  EntryModel.add = function(entry, callback) {
    var request = new XMLHttpRequest();
    request.addEventListener('load', function(){
      if(request.status != STATUS_OK){
        callback(request.responseText);
      }
    });
    request.open('POST', ENTRIES_URL, false);
    request.setRequestHeader('Content-type', 'application/json');
    request.send(JSON.stringify(entry));
    callback(null, JSON.parse(request.responseText));
  };

  /* Updates the given entry. The entry must have an id attribute that
   * identifies it.
   *
   * Calls: callback(error)
   *  error -- the error that occurred or NULL if no error occurred
   */
  EntryModel.update = function(entry, callback) {
    var entryId = entry.id;
    var url = ENTRIES_URL + "/" + entryId;
    var request = new XMLHttpRequest();
    request.addEventListener('load', function(){
      if (request.status != '200') {
        callback(request.responseText);
      }
    });
    request.open('POST', url, false);
    request.setRequestHeader('Content-type', 'application/json');
    request.send(JSON.stringify(entry));
    callback(null, JSON.parse(request.responseText));
  };

  /* Deletes the entry with the given id.
   *
   * Calls: callback(error)
   *  error -- the error that occurred or NULL if no error occurred
   */
  EntryModel.remove = function(id, callback) {
    var url = ENTRIES_URL + "/" + id + "/delete";
    var request = new XMLHttpRequest();
    request.addEventListener('load', function(){
      if (request.status != '200') {
        callback(request.responseText);
      }
    });
    request.open('POST', url, false);
    request.setRequestHeader('Content-type', "application/json");
    request.send();
    callback(null);
  };

  window.EntryModel = EntryModel;
})(this, this.document);
