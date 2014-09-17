The new_request function is re-usable. The new_request function allows you to pass in the string of a particular id of a text field and it will assign an xhrHandler for it. It sends a request with whatever value is inside the text field (of the id passed in) although the urls may need to be slighly modified.

The xhrHandler is reusable. It gets the response from the server and calls a function to manipulate the div that is needed to change (in this case, search_photos). This function call can easily be replaced by a different function call to do different things to show_div.

new_request and xhrHandler can easily be used to search up a date to show the photos that were uploaded before/after a certain date. An id of the text field is passed in and its date is stored in the "parameter" variable in new_request. The calculation logic is done by the server and the photos are passed back and stored in the "objects" array in xhrHandler.

new_request and xhrHandler can be used to search up and list users who have uploaded more than a certain number of photos. A number is passed in and stored as "parameter" in new_request. The calculation logic is done by the server-side and the users are passed into the 'objects' variable in xhrHandler.
