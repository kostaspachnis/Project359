

var username;
var borrowingStatus;
function getUser() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 201) {
            username = document.getElementById("username").value;
            authenticate();
        } else if (xhr.status !== 201) {

        }
    };
    
    var data = $('#loginForm').serialize();
    console.log(data);
    xhr.open('GET', 'GetStudent?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function authenticate() {

    getRequestedBooks();
    getBorrowedBooks();
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
            
            createLibraryList(xhr.responseText, isbn);
            //$('#librariesFoundTable').html(createLibraryList(xhr.responseText));
            //document.getElementById('librariesFound').style.display = 'block';
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
            $('#okay').html(xhr.responseText);
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
            console.log(xhr.responseText);
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
    html += '<th>' + 'Library' + '</th>';
    html += '<th>' + 'ISBN' + '</th>';
    html += '<th>' + 'Title' + '</th>';
    html += '<th>' + 'Author' + '</th>';
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
    var btn = $('#requestButton');
    btn.value = 'Hide Requested Books';
    btn.attr('onclick', 'hideRequestedBooks()');
}

function hideRequestedBooks() {
    document.getElementById('requestedBooksTable').style.display = 'none';
    var btn = $('#requestButton');
    btn.value = 'Show Requested Books';
    btn.attr('onclick', 'getRequestedBooks()');
}