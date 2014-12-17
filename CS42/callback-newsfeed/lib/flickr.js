var request = require('request');

var FLICKR_URL = 'https://api.flickr.com/services/rest/?';
var FLICKR_API_KEY = '3cffcc97867ea6aaf3d7fa2690f0ae10';
var STATUS_OK = 200;

/**
 * Queries Flickr for photos that match the given query.
 *
 * @param query -- the search query to send to Flickr
 *
 * Calls @param callback(error, results):
 *  error -- the error that occurred or null if no error
 *  results -- if error is null, contains the search results
 */
exports.search = function(query, callback) {
  	request.get({
     	url: FLICKR_URL + "api_key=" + FLICKR_API_KEY + "&text=" + query + "&method=flickr.photos.search&format=json&media=photos&sort=relevance&nojsoncallback=1",
  	}, function(error, response, body) {
   		if (response.statusCode !== 200) {
      		callback(new Exception("" + response.statusCode + ': Could not fetch url'));
   		} else if (error) {
   			callback(error);
   		} else {
   			var fetchResultArray = [];
      		var requestResults = JSON.parse(body).photos.photo;
      		//console.log(requestResults);
      		for(var i = 0; i < requestResults.length; i++){
       			fetchResultArray.push({
           			'title' : requestResults[i].title,
           			'source' : 'https://farm' +  requestResults[i].farm + '.staticflickr.com/' + requestResults[i].server + '/' + requestResults[i].id + '_' + requestResults[i].secret + '_z.jpg'
       			});
      		}
      		callback(null, fetchResultArray);
   		}
	});
};
