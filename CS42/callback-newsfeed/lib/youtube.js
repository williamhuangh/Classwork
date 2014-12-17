var request = require('request');

var YT_URL = 'https://www.googleapis.com/youtube/v3/search';
var YT_API_KEY = 'AIzaSyDDP01Gnj3-wfoqM59xQz6pryJQhmYWCt8';
var YT_EMBED_URL = 'http://www.youtube.com/embed/';

/**
 * Queries YouTube for tracks that match the given query
 * 
 * @param query - the search query to send to YouTube
 *
 * Calls @param callback(error, results):
 *  error -- the error that occurred or null if no error
 *  results -- if error is null, contains the search results
 */
exports.search = function(query, callback) {
  	request.get({
     	url: YT_URL + "?key=" + YT_API_KEY + "&q=" + query + "&part=snippet&type=video",
  	}, function(error, response, body) {
   		if (response.statusCode !== 200) {
      		callback(new Exception("" + response.statusCode + ': Could not fetch url'));
   		} else if (error) {
   			callback(error);
   		} else {
   			var fetchResultArray = [];
      		var requestResults = JSON.parse(body).items;
      		
      		for(var i = 0; i < requestResults.length; i++){
       			fetchResultArray.push({
           			'title' : requestResults[i].snippet.title,
           			'source' : YT_EMBED_URL +  requestResults[i].id.videoId
       			});
      		}
      		callback(null, fetchResultArray);
   		}
	});
};
