

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

var lat;
var lon;
function getCoordinates() {

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var myCoordinates = JSON.parse(xhr.responseText);
            document.getElementById('lat').value(myCoordinates.lat);
            document.getElementById('lon').value(myCoordinates.lon);
            // console.log(myCoordinates, ' ', myCoordinates.lat, ' ', myCoordinates.lon); 
        } else if (xhr.status === 403) {
            return false;
        }
    };  

    xhr.open('GET', 'ReturnCoordinatesForStudent?' + 'username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function createLibraryList(jsonData) {

    console.log(jsonData);

    var bookList = JSON.parse(jsonData);
    getCoordinates();

    let mylat = document.getElementById('lat').value;
    let mylon = document.getElementById('lon').value;

    console.log(mylat, ' ', mylon);

    let origins = 'origins=';
    let destinations = 'destinations=';

    origins += lat + '&' + lon;

    for(var i = 0; i < bookList.length; i++) {
        destinations += i === bookList.length-1 ? bookList[i].lat + '&' + bookList[i].lon : bookList[i].lat + '&' + bookList[i].lon + '&';
    }


    const settings = {
        "async": true,
        "crossDomain": true,
        "url": "https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?" + origins + destinations, // + "units=km",
        "method": "GET",
        "headers": {
            "X-RapidAPI-Key": "d3da8ffb6emshf934200861c165cp134e9ajsn04cf19b7aa6a",
            "X-RapidAPI-Host": "trueway-matrix.p.rapidapi.com"
        }
    };
    
    $.ajax(settings).done(function (response) {
        console.log(response);
    });
}

