(function(window, document, undefined) {
  var NewsfeedView = {};

  /* Renders the newsfeed into the given $newsfeed element. */
  NewsfeedView.render = function($newsfeed) {
  
    var allNewsfeedPosts;
    PostModel.loadAll(function(error, entries){
      if(error){
        throw error;
      }
      allNewsfeedPosts = entries;
    });

    for(var i = 0; i < allNewsfeedPosts.length; i++){
      NewsfeedView.renderPost($newsfeed, allNewsfeedPosts[i], false);
    }

    $newsfeed.imagesLoaded(function() {
      $newsfeed.masonry({
        columnWidth: '.post',
        itemSelector: '.post'
      });
    });
  };

  /* Given post information, renders a post element into $newsfeed. */
  NewsfeedView.renderPost = function($newsfeed, post, updateMasonry) {
    
    var postTemplate = document.getElementById('newsfeed-post-template');
    var templates = {
     renderPostTemplate: Handlebars.compile(postTemplate.innerHTML)
    };
    renderingPost = templates.renderPostTemplate(post);
    var $post = $(renderingPost);
    $newsfeed.prepend($post);

    $post.find(".remove").click(function(){
      PostModel.remove(post._id, function(error){
        if(error){
          $(".error").html(error);
        } else{
          $newsfeed.masonry('remove', $post);
          $newsfeed.masonry();
        }
      });
    });
    
    $post.find(".upvote").click(function(){
      PostModel.upvote(post._id, function(error){
        if (error) {
          $(".error").html(error);
        } else{
          $post.find(".upvote-count").html(parseInt($post.find(".upvote-count").html()) + 1);
        }
      });
    });

    if (updateMasonry) {
      $newsfeed.imagesLoaded(function() {
        $newsfeed.masonry('prepended', $post);
      });
    }
  };

  window.NewsfeedView = NewsfeedView;
})(this, this.document);
