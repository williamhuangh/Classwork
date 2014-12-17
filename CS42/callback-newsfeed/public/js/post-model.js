(function(window, document, undefined) {
  var PostModel = {};

  var POSTS_URL= '/posts';
  var STATUS_OK = 200;

  /**
   * Loads all newsfeed posts from the server.
   *
   * Calls: callback(error, posts)
   *  error -- the error that occurred or null if no error occurred
   *  results -- an array of newsfeed posts
   */
  PostModel.loadAll = function(callback) {
    var request = new XMLHttpRequest();
    request.addEventListener('load', function(){
      if(request.status != STATUS_OK){
        callback(request.responseText);
      }
    });
    request.open('GET', POSTS_URL, false);
    request.send();
    callback(null, JSON.parse(request.responseText));
  };

  /* Adds the given post to the list of posts. The post must *not* have
   * an _id associated with it.
   *
   * Calls: callback(error, post)
   *  error -- the error that occurred or null if no error occurred
   *  post -- the post added, with an _id attribute
   */
  PostModel.add = function(post, callback) {
    var request = new XMLHttpRequest();
    request.addEventListener('load', function(){
      if(request.status != STATUS_OK){
        callback(request.responseText);
      }
    });
    request.open('POST', POSTS_URL, false);
    request.setRequestHeader('Content-type', 'application/json');
    request.send(JSON.stringify(post));
    callback(null, JSON.parse(request.responseText));
  };

  /* Removes the post with the given id.
   *
   * Calls: callback(error)
   *  error -- the error that occurred or null if no error occurred
   */
  PostModel.remove = function(id, callback) {
    var request = new XMLHttpRequest();
    request.addEventListener('load', function(){
      if (request.status != '200') {
        callback(request.responseText);
      }
    });
    var params = {
      id: id
    }
    request.open('POST', POSTS_URL + "/remove", false);
    request.setRequestHeader('Content-type', "application/json");
    //request.setRequestHeader('data', params);
    request.send(JSON.stringify({
          id: id
    }));
    callback(null);
  };

  /* Upvotes the post with the given id.
   *
   * Calls: callback(error, post)
   *  error -- the error that occurred or null if no error occurred
   *  post -- the updated post model
   */
  PostModel.upvote = function(id, callback) {
    var request = new XMLHttpRequest();
    request.addEventListener('load', function(){
      if (request.status != '200') {
        callback(request.responseText);
      }
    });
    request.open('POST', POSTS_URL + "/upvote", false);
    request.setRequestHeader('Content-type', 'application/json');
    request.send(JSON.stringify({
          id: id
    }));
    callback(null, JSON.parse(request.responseText));
  };

  window.PostModel = PostModel;
})(this, this.document);
