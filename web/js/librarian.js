


var username;
function getUser() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 202) {
            // $("#ajaxContent1").html(createTableFromJSON(JSON.parse(xhr.responseText)));
            authenticate();
        } else if (xhr.status !== 202) {
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
    document.getElementById('book_instert').style.display = 'none';
    document.getElementById('updateLibrarianDiv').style.display = 'block';   
}

function cancelUpdateLibrarian() {
    document.getElementById('librarian_div').style.display = 'block';
    document.getElementById('book_instert').style.display = 'block';
    document.getElementById('updateLibrarianDiv').style.display = 'none';   
    document.getElementById('myForm').reset();
}

function updateLibrarian() {

    xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            hideUpdateLibrarian();
        } else if (xhr.status === 403) {
        }
    };

    var data = $('#myForm').serialize();

    xhr.open('POST', 'UpdateLibrarian?' + data + '&username=' + username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send(data);
}