


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

function authenticate() {

    document.getElementById("loginDiv").style.display="none";
    document.getElementById("logoutOpt").style.display="block";
    document.getElementById("librarian_div").style.display="block";
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
            $("#lib_books").after(createBooksTable(xhr.responseText));
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
            document.getElementById("newBookTitle").style.display="none";
            document.getElementById("newBookDiv").style.display="none";
            // getBooks(); PREPEI NA VALW KAI TO BOOK NA EMFANIZETAI STO TABLE
            const popover = new bootstrap.Popover.getOrCreateInstance(document.getElementById("insert_button"));
            popover.disable();
        } else if (xhr.status === 405) {
            const popover = new bootstrap.Popover.getOrCreateInstance(document.getElementById("insert_button"));
            popover.enable();
            popover.setContent({
                '.popover-header': 'Cannot insert book',
                '.popover-body': 'Book already exists'
            })
            popover.show();
        } else if (xhr.status !== 406) {
            const popover = new bootstrap.Popover.getOrCreateInstance(document.getElementById("insert_button"));
            popover.enable();
            popover.setContent({
                '.popover-header': 'Cannot insert book',
                '.popover-body': 'Book does not exist, please register a new book'
            })
            popover.show();
        } else if (xhr.status !== 200) {
            // $("#lib_books").html("Error!");
        }
    };
    // var data = $('#newBookForm').serialize();
    xhr.open('GET', 'SearchIfBookExistsLibrarian?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

<button type="button" class="btn btn-primary" data-bs-toggle="popover" data-bs-title="Popover title" data-bs-content="And here's some amazing content. It's very engaging. Right?">Click to toggle popover</button>