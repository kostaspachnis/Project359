


var username;
function getUser() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 202) {
            authenticate();
        } else if (xhr.status !== 202) {
            alert('Wrong username or password');
        }
    };
    username = document.getElementById("username").value;
    var data = $('#loginForm').serialize();
    console.log(data);
    xhr.open('GET', 'GetStudent?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function signup() {
    window.location = "signup_librarian.html";
}

function authenticate() {

    document.getElementById("loginDiv").style.display="none";
    document.getElementById("logoutOpt").style.display="block";
    document.getElementById("updateOpt").style.display="block";
    document.getElementById("librarian_div").style.display="block";
    document.getElementById("book_insert").style.display="block";
    getBooks();
}

function availability(btn) {
    
    var row = btn.parentNode.parentNode;
    var isbn = row.cells[0].innerHTML;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            if(btn.className === 'btn btn-danger') {
                btn.className = 'btn btn-success';
                row.cells[1].innerHTML = '<td>false</td>';
            } else {
                btn.className = 'btn btn-danger';
                row.cells[1].innerHTML = '<td>true</td>';
            }
        } else if (xhr.status !== 200) {
            //alert("Error!");
        }
    };

    const data = {};
    data["username"] = username;
    data["isbn"] = isbn;
    
    var jsondata = JSON.stringify(data);
    console.log(jsondata);

    xhr.open('POST', 'BookAvailabilityLibrarian');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send(jsondata);
}

function createBooksTable(data) {

    var books = JSON.parse(data);
    var html = '';

    if(books.length === 0) return;

    html += "<table class='table table-striped'>";
    html += "<thead>";
    html += "<tr>";
    html += "<th>ISBN</th>";
    html += "<th>Available</th>";
    html += "<th>Change Availability</th>";
    html += "</tr>";
    html += "</thead>";
    html += "<tbody>";

    for(var i = 0; i < books.length; i++) {
        html += "<tr>";
        html += "<td>" + books[i].isbn + "</td>";
        html += "<td>" + books[i].available + "</td>";
        if(books[i].available === 'true') {
            html += "<td><button type='button' class='btn btn-danger' onclick='availability(this);'>Change Availability</button></td>"; 
        } else {
            html += "<td><button type='button' class='btn btn-success' onclick='availability(this);'>Change Availability</button></td>";
        }
        html += "</tr>";
    }

    html += "</tbody>";
    html += "</table>";

    return html;
}


function getBooks() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(xhr.responseText);
            $("#lib_books").html(createBooksTable(xhr.responseText));
        } else if (xhr.status !== 200) {
             $("#lib_books").html("Error!");
        }
    };
    var data = $('#loginForm').serialize();
    xhr.open('GET', 'ReturnAvailableBooksLibrarian?' + data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function insertBook() {
    document.getElementById("newBookTitle").style.display="block";
    document.getElementById("newBookDiv").style.display="block";   
}

function insertCancel() {
    document.getElementById("newBookTitle").style.display="none";
    document.getElementById("newBookDiv").style.display="none";
}

function checkBook() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 201) {
            $("#messageModal").modal({show: true});
            $("#messageModalLabel").html("Success");
            $("#modalMessage").html("Book inserted in library");
            getBooks();
        } else if (xhr.readyState === 4 && xhr.status === 205) {
            $("#messageModal").modal({show: true});
            $("#messageModalLabel").html("Cannot insert book");
            $("#modalMessage").html("Book already exists");
        } else if (xhr.readyState === 4 && xhr.status === 206) {
            $("#messageModal").modal({show: true});
            $("#messageModalLabel").html("Cannot insert book");
            $("#modalMessage").html("Book does not exist");
        } else if (xhr.status !== 200) {
            // $("#lib_books").html("Error!");
        }
    };
    var isbn = document.getElementById("book_isbn").value;
    var data = "username="+username+"&book_isbn="+isbn;
    xhr.open('GET', 'SearchIfBookExistsLibrarian?' + data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function registerButton() {
    document.getElementById("newBookTitle").style.display="none";
    document.getElementById("newBookForm").style.display="none";
    document.getElementById("registerBookTitle").style.display="block";
    document.getElementById("registerBookForm").style.display="block";
}

function registerBook() {
    var isbn = document.getElementById("isbn").value;
    var title = document.getElementById("title").value;
    var authors = document.getElementById("authors").value;
    var genre = document.getElementById("genre").value;
    var pages = document.getElementById("pages").value;
    var pyear = document.getElementById("publicationyear").value;
    var url = document.getElementById("url").value;
    var photo = document.getElementById("photo").value;
    var sendata = "username="+username+"&isbn="+isbn+"&title="+title+"&authors="+authors+"&genre="+genre+"&pages="+pages+"&publicationyear="+pyear+"&url="+url+"&photo="+photo;
    var xhr = new XMLHttpRequest();
    
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#messageModal").modal({show: true});
            $('#registerMessage').html("Successful Book Insertion!");
            getBooks();
        } else if (xhr.readyState === 4 && xhr.status === 205) {
            $("#messageModal").modal({show: true});
            $('#registerMessage').html("Book already exists!");
        } else if (xhr.status !== 200) {
            // $('#ajaxContent').html("ERROR!");
        }
    };
    
    xhr.open('POST', 'InsertBookForLibrarian?' + sendata);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function registerCancel() {
    document.getElementById("registerBookTitle").style.display="none";
    document.getElementById("registerBookForm").style.display="none";
}

function getRequestBooks() {

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            createRequestBooksTable(xhr.responseText);
        } else if (xhr.status !== 200) {
            
        }
    };

    xhr.open('GET', 'RequestedForLibrarian?libname=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function getReturnBooks() {

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            createReturnBooksTable(xhr.responseText);
        } else if (xhr.status === 403) {
            
        }
    };

    xhr.open('GET', 'ReturnedForLibrarian?libname=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function createRequestBooksTable(books) {

    books = JSON.parse(books);
    var html = '';

    html += '<table class="table table-striped table-bordered table-hover table-sm">';
    html += '<thead class="thead-dark">';
    html += '<th>' + 'ISBN' + '</th>';
    html += '<th>' + 'Title' + '</th>';
    html += '<th>' + 'Authors' + '</th>';
    html += '<th>' + 'Photo' + '</th>';
    html += '<th>' + 'Accept' + '</th>';
    html += '</thead>';
    html += '<tbody>';

    for(var i = 0; i < books.length; i++) {
        html += "<tr>";
        html += "<td>" + books[i].isbn + "</td>";
        html += "<td>" + books[i].title + "</td>";
        html += "<td>" + books[i].authors + "</td>";
        html += '<td><img src="' + books[i].photo + '" alt="book photo" width="70" height="100"></td>'
        html += '<td><button type="button" class="btn btn-success" onclick="acceptRequest(' + books[i].isbn + ')">Accept Request</button></td>';
        html += "</tr>";
    }

    html += "</tbody></table>";
    $('#requestBooksTable').html(html);
    document.getElementById('requestBooksTable').style.display = 'block';
    var btn = $('#requestButton');
    btn.value = 'Hide Request Books';
    btn.attr('onclick', 'hideRequestBooks()');
}

function createReturnBooksTable(books) {
    
        books = JSON.parse(books);
        var html = '';
    
        html += '<table class="table table-striped table-bordered table-hover table-sm">';
        html += '<thead class="thead-dark">';
        html += '<th>' + 'ISBN' + '</th>';
        html += '<th>' + 'Title' + '</th>';
        html += '<th>' + 'Authors' + '</th>';
        html += '<th>' + 'Photo' + '</th>';
        html += '<th>' + 'Return' + '</th>';
        html += '</thead>';
        html += '<tbody>';
    
        for(var i = 0; i < books.length; i++) {
            html += "<tr>";
            html += "<td>" + books[i].isbn + "</td>";
            html += "<td>" + books[i].title + "</td>";
            html += "<td>" + books[i].authors + "</td>";
            html += '<td><img src="' + books[i].photo + '" alt="book photo" width="70" height="100"></td>'
            html += '<td><button type="button" class="btn btn-danger" onclick="acceptReturn(' + books[i].isbn + ');">Accept Return</button></td>';
            html += "</tr>";
        }
    
        html += "</tbody></table>";
        $('#returnBooksTable').html(html);
        document.getElementById('returnBooksTable').style.display = 'block';
        var btn = $('#returnButton');
        btn.value = 'Hide return Books';
        btn.attr('onclick', 'hideReturnBooks()');
}

function hideRequestBooks() {
    document.getElementById('requestBooksTable').style.display = 'none';
    var btn = $('#requestButton');
    btn.value = 'Requested Books';
    btn.attr('onclick', 'getRequestBooks()');
}

function hideReturnBooks() {
    document.getElementById('returnBooksTable').style.display = 'none';
    var btn = $('#returnButton');
    btn.value = 'Returned Books';
    btn.attr('onclick', 'getReturnBooks()');
}

function acceptRequest(isbn) {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            getRequestBooks();
        } else if (xhr.status !== 200) {
            
        }
    }

    xhr.open('POST', 'AcceptRequestedForLibrarian?isbn=' + isbn + '&libname=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function acceptReturn(isbn) {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            getReturnBooks();
        } else if (xhr.status !== 200) {
            
        }
    }

    xhr.open('POST', 'AcceptReturnedForLibrarian?isbn=' + isbn + '&libname=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function exportPdf() {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            
        } else if (xhr.status === 403) {
            
        }
    };

    xhr.open('GET', 'ActiveBorrowingForLibrarian?' + 'libname=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function showUpdateLibrarian() {
    document.getElementById('librarian_div').style.display = 'none';
    document.getElementById('book_insert').style.display = 'none';
    document.getElementById('updateLibrarianDiv').style.display = 'block';   
}

function cancelUpdateLibrarian() {
    document.getElementById('librarian_div').style.display = 'block';
    document.getElementById('book_insert').style.display = 'block';
    document.getElementById('updateLibrarianDiv').style.display = 'none';   
    document.getElementById('myForm').reset();
}

function updateLibrarian() {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            cancelUpdateLibrarian();
        } else if (xhr.status === 403) {
        }
    };

    var data = $('#myForm').serialize();

    xhr.open('POST', 'UpdateLibrarian?' + data + '&username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send(data);
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


function validate() {

    if(!passwordStrength()) {
        alert('Password is too weak!');
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