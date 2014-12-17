var soundcloud = require("../lib/soundcloud");
var youtube = require("../lib/youtube");
var flickr = require("../lib/flickr");
var Post = require("../models/post.js")

module.exports = function(app) {
  /* Renders the newsfeed landing page. */
  	app.get('/', function(request, response) {
    	response.render('index.html');
  	});

  	app.get('/search', function(request, response){
  		var topResultsArray = [];
  		var counter = 0;
  		soundcloud.search(request.query.query, function(error, resultsArray){
  			counter++;
  			if(!error && resultsArray.length > 0){
  				var currentResult = resultsArray[0];
  				currentResult.api = "soundcloud";
  				topResultsArray.push(currentResult);
  			} else if (error) {
  				throw error;
  			}
  			if (counter == 3) {
  				response.json(200, topResultsArray);
  			};
  		});

  		youtube.search(request.query.query, function(error, resultsArray){
  			counter++;
  			if(!error && resultsArray.length > 0){
  				var currentResult = resultsArray[0];
  				currentResult.api = "youtube";
  				topResultsArray.push(currentResult);
  			} else if (error) {
  				throw error;
  			}
  			if (counter == 3) {
  				response.json(200, topResultsArray);
  			};
  		});

  		flickr.search(request.query.query, function(error, resultsArray){
  			counter++;
  			if(!error && resultsArray.length > 0){
  				var currentResult = resultsArray[0];
  				currentResult.api = "flickr";
  				topResultsArray.push(currentResult);
  			} else if (error) {
  				throw error;
  			}
  			if (counter == 3) {
  				response.json(200, topResultsArray);
  			};
  		});
  	});

	app.get('/posts', function(request, response){
		Post.find(function(error, people){
			if(error){
				throw error;
			}
			response.json(people);
		});
	});

	app.post('/posts', function(request, response){
		if (request.body.api != undefined && request.body.source != undefined && request.body.title != undefined) {
			var newPost = new Post({
				api: request.body.api,
				source: request.body.source,
				title: request.body.title,
				upvotes: 0
			});
			newPost.save(function(error){
				if(error){
					throw error;
				}
			});
			response.json(newPost);
		}
	});

	app.post('/posts/remove', function(request, response){
		if (request.body.id != undefined) {
			Post.findByIdAndRemove(request.body.id, function(error){
				if (error) {
					throw error;
				}
			});
			response.json();
		}
	});

	app.post('/posts/upvote', function(request, response){
		if (request.body.id != undefined) {
			Post.findById(request.body.id, function(error, currentPost){
				if (error) {
					throw error;
				}
				currentPost.upvotes += 1;
				currentPost.save(function(error){
					if (error) {
						throw error;
					}
				})
				response.json(currentPost);
			})
		}
	});
};
