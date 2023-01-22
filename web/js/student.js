

var username;
function getUser() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 201) {
            // $("#ajaxContent1").html(createTableFromJSON(JSON.parse(xhr.responseText)));
            authenticate();
        } else if (xhr.status !== 201) {
            // $("#ajaxContent1").html("User not exists or incorrect password");
        }
    };
    username = document.getElementById("username").value;
    var data = $('#loginForm').serialize();
    console.log(data);
    xhr.open('GET', 'GetStudent?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function authenticate() {

    document.getElementById("loginDiv").style.display="none";
    document.getElementById("logoutOpt").style.display="block";
    document.getElementById("student_div").style.display="block";
}


function signup() {
    window.location = "signup_student.html";
}


function searchBook() {

    // let myForm = document.getElementById('bookSearchForm');
    // let formData = new FormData(myForm);
    // const data = {};
    // formData.forEach((value, key) => (data[key] = value));
    // var jsonData = JSON.stringify(data);

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
            
            createLibraryList(xhr.responseText);
            //$('#librariesFoundTable').html(createLibraryList(xhr.responseText));
            //document.getElementById('librariesFound').style.display = 'block';
        } else if (xhr.status === 403) {
            
        }
    }

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


function createLibraryList(jsonData) {
    
    var bookList = JSON.parse(jsonData);
   
    var mylat = x;
    var mylon = y;

    let origins = mylat + '%2C' + mylon;
    let destinations = bookList[0].lat + '%2C' + bookList[0].lon;

    for(var i = 1; i < bookList.length; i++) {
        destinations += '%3B' + bookList[i].lat + '%2C' + bookList[i].lon;
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
            for(var i = 0; i < bookList.length; i++) {
                bookList[i].distance = results.distances[0][i];
                bookList[i].duration = results.durations[0][i];
            }
            createClosestLibrariesList(bookList);
        }
    });
    xhr.open("GET", "https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?" + 'origins=' + origins + '&destinations=' + destinations);
    xhr.setRequestHeader("x-rapidapi-host", "trueway-matrix.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "d3da8ffb6emshf934200861c165cp134e9ajsn04cf19b7aa6a");
    xhr.send(data);
}


function createClosestLibrariesList(bookList) {

    console.log(bookList);

    bookList.sort((a, b) => (a.distance*a.duration > b.distance*b.duration) ? 1 : -1);

    var html = '';

    html += '<table class="table table-striped table-bordered table-hover table-sm">';
    html += '<thead class="thead-dark">';
    html += '<th>' + 'Library' + '</th>';
    html += '<th>' + 'Distance' + '</th>';
    html += '<th>' + 'Duration' + '</th>';
    html += '<th>' + 'Request' + '</th>';
    html += '<th>' + 'Return' + '</th>';
    html += '</thead>';
    html += '<tbody>';

    for(var i = 0; i < bookList.length; i++) {
        html += "<tr>";
        html += "<td>" + bookList[i].libraryname + "</td>";
        html += "<td>" + bookList[i].distance + "</td>";
        html += "<td>" + bookList[i].duration + "</td>";
        html += '<td><button type="button" class="btn btn-success" onclick="borrowBook(' + bookList[i].libraryid, bookList[i].isbn, username + ')">Borrow</button></td>';
        html += '<td></td>';
        html += "</tr>";
    }

    html += "</tbody></table>";
    $('#closestLibraries').html(html);
    document.getElementById('closestLibraries').style.display = 'block';
}


function borrowBook(libraryid, isbn, username) {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('AAAAAAAAAAA');
        } else if (xhr.status === 403) {
            
        }
    }

    xhr.open('GET', 'BorrowBooksForStudent?' + 'libraryid=' + libraryid + '&isbn=' + isbn + '&username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}