# highload-server
Run server with config httpd.conf:

git clone https://github.com/KrylovS/highload-server.git

docker build -t httpserver ./highload-server/

docker run -p 80:8080 -v /full/path/to/httpd.conf:/etc/httpd.conf:ro -v /full/path/to/http-test-suite:/var/www/html:ro --name httpserver -t httpserver
