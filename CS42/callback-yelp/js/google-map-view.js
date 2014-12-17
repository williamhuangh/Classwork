(function(window, document, undefined) {
  var GoogleMapView = {};
  
  // zoom level for Google Map
  var DEFAULT_ZOOM = 14;
  var STATUS_OK = 200;

  /* Renders a map for the given entry into the provided $map element. */
  GoogleMapView.render = function($map, entryData) {
    var mapsRequest = new XMLHttpRequest();
    mapsRequest.addEventListener('load', function(){
    	if(mapsRequest.status != STATUS_OK){
    		return;
    	}
    });
    mapsRequest.open('GET', "https://maps.googleapis.com/maps/api/geocode/json?address=" + entryData.address + "&key=AIzaSyCjqL0kFVn2K62jbdSACUloFfI2LkXfUKA&sensor=false", false);
    mapsRequest.send();
    var response = JSON.parse(mapsRequest.responseText);
    if (response.status == "ZERO_RESULTS") {
    	return;
    }
    var longitude = response.results[0].geometry.location.lng;
    var latitude = response.results[0].geometry.location.lat;
    var mapOptions = {
    	center: {lat: latitude, lng: longitude},
    	zoom: DEFAULT_ZOOM
    };
    var shownMap = new google.maps.Map($map[0], mapOptions);
    var marker = new google.maps.Marker({
    	position: {lat: latitude, lng: longitude},
    	map: shownMap,
    	title: "Location"
    });
  };
  
  window.GoogleMapView = GoogleMapView;
})(this, this.document);
