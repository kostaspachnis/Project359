

var username;
var borrowingStatus;
function getUser() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 201) {
            username = document.getElementById("username").value;
            authenticate();
        } else if (xhr.status !== 201) {
            alert("Wrong username or password");
        }
    };
    
    var data = $('#loginForm').serialize();
    console.log(data);
    xhr.open('GET', 'GetStudent?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function authenticate() {

    document.getElementById("loginDiv").style.display="none";
    document.getElementById("logoutOpt").style.display="block";
    document.getElementById("updateOpt").style.display="block";
    document.getElementById("student_div").style.display="block";
    getNotifications();
}


function signup() {
    window.location = "signup_student.html";
}


function searchBook() {

    let genre = document.getElementById('search_genre').value;
    let title = document.getElementById('search_title').value;
    let author = document.getElementById('search_author').value;
    let fromPage = document.getElementById('fromPageNumber').value;
    let toPage = document.getElementById('toPageNumber').value;
    let fromYear = document.getElementById('fromYear').value;
    let toYear = document.getElementById('toYear').value;

    let data = 'genre=' + genre + '&title=' + title + '&author=' + author + '&fromPage=' + fromPage + '&toPage=' + toPage + '&fromYear=' + fromYear + '&toYear=' + toYear;

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#booksFoundTable').html("");
            $('#booksFoundTable').html(createBookListFromJSON(xhr.responseText));
            document.getElementById('booksFound').style.display = 'block';
        } else if (xhr.status === 403) {
            
        }
    };

    xhr.open('GET', 'FindBookForStudent?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}



function createBookListFromJSON(jsonData) {

    console.log(jsonData);

    var books = JSON.parse(jsonData);
    var html = '';

    if(books.length === 0) return;

    html += '<table class="table table-striped table-bordered table-hover table-sm">';
    html += '<thead class="thead-dark text-center">';
    html += '<th>' + 'ISBN' + '</th>';
    html += '<th>' + 'Title' + '</th>';
    html += '<th>' + 'Authors' + '</th>';
    html += '<th>' + 'URL' + '</th>';
    html += '<th>' + 'Photo' + '</th>';
    html += '<th>' + 'Pages' + '</th>';
    html += '<th>' + 'Publication Year' + '</th>';
    html += '<th>' + 'Request' + '</th>';
    html += '</thead>';
    html += '<tbody>';

    for(var i = 0; i < books.length; i++) {
        html += "<tr>";
        html += "<td>" + books[i].isbn + "</td>";
        html += "<td>" + books[i].title + "</td>";
        html += "<td>" + books[i].authors + "</td>";
        html += "<td>" + books[i].url + "</td>";
        html += '<td><img src="' + books[i].photo + '" alt="book photo" width="70" height="100"></td>';
        html += "<td>" + books[i].pages + "</td>";
        html += '<td>' + books[i].publicationyear + "</td>";
        html += '<td><button type="button" class="btn btn-success" onclick="requestBook(' + books[i].isbn + ')">Find Libraries</button></td>';
        html += "</tr>";
    }

    html += "</tbody></table>";
    return html;
}

function requestBook(isbn) {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(xhr.responseText);
            createLibraryList(xhr.responseText, isbn);
        } else if (xhr.status === 403) {
            
        }
    };

    xhr.open('GET', 'ShowLibrariesForStudent?' + 'isbn=' + isbn);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

var lat=0;
var lon=0;
var myCoordinates;
var Coordinates,x="",y="";
function getCoordinates() {

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            Coordinates = JSON.parse(xhr.responseText);
            x=Coordinates.lat;
            y=Coordinates.lon;
            console.log(x, ' ', y,"test");
        } else if (xhr.status === 403) {
            return false;
        }
    };  

    xhr.open('GET', 'ReturnCoordinatesForStudent?' + 'username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function createLibraryList(jsonData, isbn) {
    
    var libraryList = JSON.parse(jsonData);
   
    var mylat = x;
    var mylon = y;

    let origins = mylat + '%2C' + mylon;
    let destinations = libraryList[0].lat + '%2C' + libraryList[0].lon;

    for(var i = 1; i < libraryList.length; i++) {
        destinations += '%3B' + libraryList[i].lat + '%2C' + libraryList[i].lon;
    }

    console.log(origins);
    console.log(destinations);

    const data = null;
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", 
    function () {
        if (this.readyState === this.DONE) {
            results=JSON.parse(this.responseText);
            for(var i = 0; i < libraryList.length; i++) {
                libraryList[i].distance = results.distances[0][i];
                libraryList[i].duration = results.durations[0][i];
            }
            createClosestLibrariesList(libraryList, isbn);
        }
    });
    xhr.open("GET", "https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?" + 'origins=' + origins + '&destinations=' + destinations);
    xhr.setRequestHeader("x-rapidapi-host", "trueway-matrix.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "d3da8ffb6emshf934200861c165cp134e9ajsn04cf19b7aa6a");
    xhr.send(data);
}


function createClosestLibrariesList(libraryList, isbn) {

    console.log(libraryList);

    libraryList.sort((a, b) => (a.distance*a.duration > b.distance*b.duration) ? 1 : -1);

    var html = '';

    html += '<table class="table table-striped table-bordered table-hover table-sm">';
    html += '<thead class="thead-dark">';
    html += '<th>' + 'Library' + '</th>';
    html += '<th>' + 'Distance' + '</th>';
    html += '<th>' + 'Duration' + '</th>';
    html += '<th>' + 'Borrow' + '</th>';
    html += '</thead>';
    html += '<tbody>';

    for(var i = 0; i < libraryList.length; i++) {
        html += "<tr>";
        html += "<td>" + libraryList[i].libraryname + "</td>";
        html += "<td>" + libraryList[i].distance + "</td>";
        html += "<td>" + libraryList[i].duration + "</td>";
        html += '<td><button type="button" class="btn btn-success" onclick="borrowBook(' + libraryList[i].library_id + ', ' + isbn + '); this.disable=true">Borrow</button></td>';
        html += "</tr>";
    }

    html += "</tbody></table>";
    $('#closestLibrariesTable').html(html);
    document.getElementById('closestLibraries').style.display = 'block';
}


function borrowBook(libraryid, isbn) {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // add to requested books table
        } else if (xhr.status === 403) {
            
        }
    };

    xhr.open('POST', 'BorrowBookForStudent?' + 'libid=' + libraryid + '&isbn=' + isbn + '&username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function getRequestedBooks() {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            createRequestedBooksTable(xhr.responseText);
        } else if (xhr.status === 403) {
            
        }
    };

    xhr.open('GET', 'BorrowingStatusForStudent?' + 'username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function getBorrowedBooks() {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            createBorrowedBooksTable(xhr.responseText);
        } else if (xhr.status === 403) {
            
        }
    };

    xhr.open('GET', 'BorrowingStatusForStudent1?' + 'username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function createRequestedBooksTable(books) {

    books = JSON.parse(books);
    var html = '';

    html += '<table class="table table-striped table-bordered table-hover table-sm">';
    html += '<thead class="thead-dark">';
    html += '<th>' + 'ISBN' + '</th>';
    html += '<th>' + 'Title' + '</th>';
    html += '<th>' + 'Authors' + '</th>';
    html += '<th>' + 'Library' + '</th>';
    html += '<th>' + 'Photo' + '</th>';
    html += '</thead>';
    html += '<tbody>';

    for(var i = 0; i < books.length; i++) {
        html += "<tr>";
        html += "<td>" + books[i].isbn + "</td>";
        html += "<td>" + books[i].title + "</td>";
        html += "<td>" + books[i].authors + "</td>";
        html += "<td>" + books[i].library + "</td>";
        html += '<td><img src="' + books[i].photo + '" alt="book photo" width="70" height="100"></td>'
        html += "</tr>";
    }

    html += "</tbody></table>";
    $('#requestedBooksTable').html(html);
    document.getElementById('requestedBooksTable').style.display = 'block';
    var btn = $('#requestedButton');
    btn.value = 'Hide Requested Books';
    btn.attr('onclick', 'hideRequestedBooks()');
}

function createBorrowedBooksTable(books) {
    
        books = JSON.parse(books);
        var html = '';
    
        html += '<table class="table table-striped table-bordered table-hover table-sm">';
        html += '<thead class="thead-dark">';
        html += '<th>' + 'ISBN' + '</th>';
        html += '<th>' + 'Title' + '</th>';
        html += '<th>' + 'Authors' + '</th>';
        html += '<th>' + 'Library' + '</th>';
        html += '<th>' + 'Photo' + '</th>';
        html += '<th>' + 'Return' + '</th>';
        html += '</thead>';
        html += '<tbody>';
    
        for(var i = 0; i < books.length; i++) {
            let library = books[i].library;
            html += "<tr>";
            html += "<td>" + books[i].isbn + "</td>";
            html += "<td>" + books[i].title + "</td>";
            html += "<td>" + books[i].authors + "</td>";
            html += "<td>" + books[i].library + "</td>";
            html += '<td><img src="' + books[i].photo + '" alt="book photo" width="70" height="100"></td>';
            html += '<td><button type="button" class="btn btn-danger" onclick="returnBook(' + books[i].isbn + ', ' + '\'' + library + '\'' + ');">Return</button></td>';
            html += "</tr>";
        }
    
        html += "</tbody></table>";
        $('#borrowedBooksTable').html(html);
        document.getElementById('borrowedBooksTable').style.display = 'block';
        var btn = $('#borrowedButton');
        btn.value = 'Hide Borrowed Books';
        btn.attr('onclick', 'hideBorrowedBooks()');
}

function hideRequestedBooks() {
    document.getElementById('requestedBooksTable').style.display = 'none';
    var btn = $('#requestedButton');
    btn.value = 'Requested Books';
    btn.attr('onclick', 'getRequestedBooks()');
}

function hideBorrowedBooks() {
    document.getElementById('borrowedBooksTable').style.display = 'none';
    var btn = $('#borrowedButton');
    btn.value = 'Borrowed Books';
    btn.attr('onclick', 'getBorrowedBooks()');
}

function returnBook(isbn, libraryid) {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            getBorrowedBooks();
        } else if (xhr.status === 403) {
            
        }
    };

    xhr.open('POST', 'ReturnBookForStudent?' + 'libname=' + libraryid + '&isbn=' + isbn + '&username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function showComment() {
    document.getElementById('commentDiv').style.display = 'block';
    document.getElementById('commentButton').style.display = 'none';
}

function hideComment() {
    document.getElementById('commentDiv').style.display = 'none';
    document.getElementById('commentButton').style.display = 'block';
    document.getElementById('comment').value = '';
    document.getElementById('wrong_isbn').value = '';
}

function leaveComment(){

    let isbn = document.getElementById('comment_isbn').value;
    var comment = document.getElementById('comment').value;
    var rating = document.getElementById('rating').value;

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            hideComment();
        } else if (xhr.status === 403) {
            $('#wrong_isbn').html('Wrong ISBN, you have not borrowed this book');
        }
    };

    xhr.open('POST', 'AddReviewForStudent?' + 'username=' + username + '&review=' + comment + '&score=' + rating + '&isbn=' + isbn);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function showUpdateStudent() {
    document.getElementById('student_div').style.display = 'none';
    document.getElementById('updateStudentDiv').style.display = 'block';
}

function cancelUpdateStudent() {
    document.getElementById('student_div').style.display = 'block';
    document.getElementById('updateStudentDiv').style.display = 'none';
    document.getElementById('myForm').reset();
}

function updateStudent() {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            cancelUpdateStudent();
        } else if (xhr.status === 403) {
        }
    };

    var data = $('#myForm').serialize();

    xhr.open('POST', 'UpdateUser?' + data + '&username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function getNotifications() {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 201) {
            console.log(xhr.responseText);
            console.log('AAAAAAA');
            createNotificationsTable(xhr.responseText);
        } else if (xhr.status === 200) {
            console.log('BBBBBBB');
        } else if (xhr.status === 403) {
        }
    };

    xhr.open('GET', 'ThreeDaysNoticeForStudent?' + 'username=' + username);
    xhr.send();
}


function createNotificationsTable(books) {

    books = JSON.parse(books);

    var html = '';

    html += '<table class="table table-striped table-bordered table-hover table-sm">';
    html += '<thead class="thead-dark">';
    html += '<th>' + 'ISBN' + '</th>';
    html += '<th>' + 'Title' + '</th>';
    html += '</thead>';
    html += '<tbody>';

    for(var i = 0; i < books.length; i++) {
        html += '<tr class="table-danger">';
        html += "<td>" + books[i].isbn + "</td>";
        html += "<td>" + books[i].title + "</td>";
        html += "</tr>";
    }

    html += "</tbody></table>";
    $('#notificationsTable').html(html);
    document.getElementById('notifications').style.display = 'block';
}



// ================================= Checks =================================

var uni;
var mail_flag;
var get;

function passwordToggle() {
    var x = document.getElementById("password");
    var y = document.getElementById("toggle");
    x.type === 'password' ? x.type = 'text' : x.type = 'password';
    y.className === 'fa-regular fa-eye' ? y.className = 'fa-solid fa-eye-slash' : y.className = 'fa-regular fa-eye';
}

function passwordStrength() {
    var password = document.getElementById('password').value;
    var strength = document.getElementById('strength');

    if (password.includes('helmepa') || password.includes('uoc') || password.includes('tuc')) {
        strength.style.color = 'red';
        strength.innerHTML = 'Weak';
        return false;
    }

    if (password.match(/[0-9]g/) != null && password.match(/[0-9]/g).length >= password.length / 2) {
        strength.style.color = 'red';
        strength.innerHTML = 'Weak';
        return false;
    }

    if (password.match(/[a-z]/g) && password.match(/[A-Z]/g) && password.match(/[^a-zA-Z\d]/g) && password.match(/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/g).length >= 2) {
        strength.style.color = 'green';
        strength.innerHTML = 'Strong';
    } else {
        strength.style.color = 'orange';
        strength.innerHTML = 'Medium';
    }

    return true;
}


function passCheck() {

    var start = document.getElementById('student_id_from_date').value;
    var end = document.getElementById('student_id_to_date').value;

    if(start > end) {
        alert('Start date cannot be after end date!');
        return false;
    }

    if(end.getFullYear() - start.getFullYear() >= 7) {
        alert('You cannot have a period longer than 6 years!');
        return false;
    }

    return true;
}


function validate() {

    if(!passwordStrength()) {
        alert('Password is too weak!');
        return false;
    }

    if(!passCheck()) {
        return false;
    }

    return true;
}

var map = new OpenLayers.Map("map");
map.addLayer(new OpenLayers.Layer.OSM());
function map_show(x, y) {
    map = new OpenLayers.Map("map");
    map.addLayer(new OpenLayers.Layer.OSM());
    map.updateSize();

    document.getElementById('map').style.height = '300px';
    document.getElementById('map').style.width = '700px';

    var lonLat = new OpenLayers.LonLat(x, y)
          .transform(
            new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
          
    var zoom=16;

    var markers = new OpenLayers.Layer.Markers( "Markers" );
    map.addLayer(markers);
    
    markers.addMarker(new OpenLayers.Marker(lonLat));
    
    map.setCenter (lonLat, zoom);
}

function request() {
    const data = null;
    const country = document.getElementById('country').value;
    const city = document.getElementById('city').value;
    const address = document.getElementById('address').value;

    const full_address = country + ', ' + city + ', ' + address;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = false;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            if(JSON.parse(this.responseText).length > 0) {
                get = JSON.parse(this.responseText);
                document.getElementById('map-message').textContent = 'Found location';
                map_show(get[0].lon, get[0].lat);
            }
        } else {
            document.getElementById('map-message').textContent = 'Loading...';
        }
    });

    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q="+full_address+"&accept-language=en&polygon_threshold=0.0");
    xhr.setRequestHeader("X-RapidAPI-Key", "d3da8ffb6emshf934200861c165cp134e9ajsn04cf19b7aa6a");
    xhr.setRequestHeader("X-RapidAPI-Host", "forward-reverse-geocoding.p.rapidapi.com");

    xhr.send(data);
}