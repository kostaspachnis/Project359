'use strict';

var isAdmin = 0; // This var will be used in order to know when we can call
                 // the admin functions
// Admin Login or Normal Login (Project Function)
function login() {
    username = document.getElementById("username_log").value;
    password = document.getElementById("password_log").value;
    if(username.toString() === "admin" && password.toString() === "admin12*") {
        document.getElementById("ajaxContent1").innerHTML = "Successful Login. Admin mode activated!";
        isAdmin = 1;
    }
    else {
        getUser();
    }
}

function book_api(){
    document.getElementById("ex4").style.display = "block";
    $("#ex4").load("ex4.html");
    document.getElementById("form_attr").style.display = "none";
    document.getElementById("login_attr").style.display = "none";
    document.getElementById("update_user_info").style.display = "none";
    document.getElementById("outputdiv").style.display = "none";
    document.getElementById("outputdive").style.display = "none";
    document.getElementById("welcome").style.display = "none";
    document.getElementById("xssattack").style.display = "none";
    document.getElementById("logged_in").style.display = "none";
}

function showXSS() {
    document.getElementById("update_user_info").style.display = "none";
    document.getElementById("outputdiv").style.display = "none";
    document.getElementById("outputdive").style.display = "none";
    document.getElementById("welcome").style.display = "none";
    document.getElementById("xssattack").style.display = "block";
}

function logOut(){
    document.getElementById("form_attr").style.display = "none";
    document.getElementById("login_attr").style.display = "none";
    document.getElementById("update_user_info").style.display = "none";
    document.getElementById("outputdiv").style.display = "none";
    document.getElementById("outputdive").style.display = "none";
    document.getElementById("welcome").style.display = "block";
    document.getElementById("xssattack").style.display = "none";
    document.getElementById("logged_in").style.display = "none";
    
}

function showUpdate() {
    document.getElementById("outputdive").innerHTML = "";
    document.getElementById("update_user_info").style.display = "block";
    document.getElementById("outputdiv").style.display = "none";
}

function showSignUp() {
    document.getElementById("form_attr").style.display = "block";
    document.getElementById("xssattack").style.display = "none";
    document.getElementById("outputdive").style.display = "none";
    document.getElementById("login_attr").style.display = "none";
    document.getElementById("welcome").style.display = "none";
}

function showLogin() {
    document.getElementById("form_attr").style.display = "none";
    document.getElementById("login_attr").style.display = "block";
    document.getElementById("welcome").style.display = "none";
    document.getElementById("outputdive").style.display = "none";
    document.getElementById("xssattack").style.display = "none";
}

function createTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    return html;

}

function createBooksTableFromJSON(data) {
    var html = "<table><tr><th>isbn</th><th>title</th><th>authors</th><th>genre</th><th>url</th><th>photo</th><th>pages</th><th>publicationyear</th></tr>";
    let yoho= data.toString();
    let help = yoho.split("}"); 
    
    for(var i=0; i<help.length-1; i++){
        html += "<tr>";
        console.log(help.length);
        let ok = help[i].concat("}");
        let ok1 = ok.slice(1);
        console.log(ok1);
        let helper = JSON.parse(ok1.toString());   
        for (const x in helper) {     
                var value = helper[x];
                html += "<td>" + value + "</td>";
            }
        html+="</tr>";
    }
    html += "</table>";
    return html;
}

// Create a table with ALL students or ALL librarians (Project Function)
function createUsersTableFromJSON(data) {
    var html = "<table><tr><th>username</th><th>firstname</th><th>lastname</th></tr>";
    let yoho= data.toString();
    let help = yoho.split("}"); 
    
    for(var i=0; i<help.length-1; i++){
        html += "<tr>";
        console.log(help.length);
        let ok = help[i].concat("}");
        let ok1 = ok.slice(1);
        console.log(ok1);
        let helper = JSON.parse(ok1.toString());   
        for (const x in helper) {     
                var value = helper[x];
                html += "<td>" + value + "</td>";
            }
        html+="</tr>";
    }
    html += "</table>";
    return html;
}

// Return the table with ALL students (Project Function)
function getStudents() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("outputdiv").style.display="block";
            $("#outputdiv").html(createUsersTableFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#ouputdiv").html("Error!");
        }
    };

    xhr.open('GET', 'GetStudentsForAdmin?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Return the table with ALL librarians (Project Function)
function getLibrarians() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("outputdiv").style.display="block";
            $("#outputdiv").html(createUsersTableFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#ouputdiv").html("Error!");
        }
    };

    xhr.open('GET', 'GetLibrariansForAdmin?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function getBooks() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("update_user_info").style.display = "none";
            document.getElementById("outputdiv").style.display="block";
            $("#outputdiv").html(createBooksTableFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#ouputdiv").html("Error!");
        }
    };

    xhr.open('GET', 'GetBooks?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Return the available books for the guest user (Project Function)
function getBooksGuest() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("update_user_info").style.display = "none";
            document.getElementById("outputdiv").style.display="block";
            $("#outputdiv").html(createBooksTableFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#ouputdiv").html("Error!");
        }
    };

    xhr.open('GET', 'GuestUserShowBooks?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Returns the logged in student or librarian (servlet name is wrong)
var name_global="";
function getUser() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#ajaxContent1").html(createTableFromJSON(JSON.parse(xhr.responseText)));
            document.getElementById("logged_in").style.display="block";
        } else if (xhr.status !== 200) {
             $("#ajaxContent1").html("User not exists or incorrect password");
        }
    };
    name_global = document.getElementById("username_log").value;
    var data = $('#loginForm').serialize();
    xhr.open('GET', 'GetStudent?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Update User (Project Function)
function updateUser() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("outputdive").style.display = "block";
            document.getElementById("outputdive").innerHTML = "Success.";
            $("#ajaxContent1").html(createTableFromJSON(JSON.parse(xhr.responseText)));
        } else if (xhr.status !== 200) {
            document.getElementById("outputdive").style.display = "block";
            document.getElementById("outputdive").innerHTML = "Failed."; 
        }
    };
    document.getElementById("username_new").value = name_global;
    var data = $('#newForm').serialize();
    console.log(data);
    xhr.open('POST', 'UpdateUser?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Update Librarian (Project Function)
function updateLibrarian() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("outputdive").style.display = "block";
            document.getElementById("outputdive").innerHTML = "Success.";
            $("#ajaxContent1").html(createTableFromJSON(JSON.parse(xhr.responseText)));
        } else if (xhr.status !== 200) {
            document.getElementById("outputdive").style.display = "block";
            document.getElementById("outputdive").innerHTML = "Failed."; 
        }
    };
    document.getElementById("username_new").value = name_global;
    var data = $('#newForm').serialize();
    console.log(data);
    xhr.open('POST', 'UpdateLibrarian?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Delete Student (Project Function)
function deleteStudent() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("outputdive").style.display = "block";
            document.getElementById("outputdive").innerHTML = "Success.";
        } else if (xhr.status !== 200) {
            document.getElementById("outputdive").style.display = "block";
            document.getElementById("outputdive").innerHTML = "Failed."; 
        }
    };
    var data = $('#newForm').serialize();
    console.log(data);
    xhr.open('POST', 'DeleteStudentForAdmin?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Delete Librarian (Project Function)
function deleteLibrarian() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("outputdive").style.display = "block";
            document.getElementById("outputdive").innerHTML = "Success.";
        } else if (xhr.status !== 200) {
            document.getElementById("outputdive").style.display = "block";
            document.getElementById("outputdive").innerHTML = "Failed."; 
        }
    };
    var data = $('#newForm').serialize();
    console.log(data);
    xhr.open('POST', 'DeleteLibrarianForAdmin?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Pie Chart for Books Genre (Project Function)
function booksGenreChart() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("update_user_info").style.display = "none";
            document.getElementById("outputdiv").style.display="block";
            $("#outputdiv").html(xhr.responseText);
        } else if (xhr.status !== 200) {
             $("#ouputdiv").html("Error!");
        }
    };

    xhr.open('GET', 'GetBooksGenreForAdmin?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Pie Chart for Student Type (Project Function)
function studentTypeChart() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("update_user_info").style.display = "none";
            document.getElementById("outputdiv").style.display="block";
            $("#outputdiv").html(xhr.responseText);
        } else if (xhr.status !== 200) {
             $("#ouputdiv").html("Error!");
        }
    };

    xhr.open('GET', 'GetStudentsTypeForAdmin?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Pie Chart for Books In Libraries (Project Function)
function booksInLibChart() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("update_user_info").style.display = "none";
            document.getElementById("outputdiv").style.display="block";
            $("#outputdiv").html(xhr.responseText);
        } else if (xhr.status !== 200) {
             $("#ouputdiv").html("Error!");
        }
    };

    xhr.open('GET', 'GetBooksInEachLibraryForAdmin?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

// Add new Book in the library (Project Function)
function InsertNewBook() {
    let bookForm = document.getElementById('bookForm');
    let formData = new FormData(bookForm);
    const data = {}
    formData.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);
    var xhr = new XMLHttpRequest();
    
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
                $('#ajaxContent').html("Successful Book Insertion!");
            } else if (xhr.status !== 200) {
                $('#ajaxContent').html("ERROR!");
            }
        };
    xhr.open('POST', 'InsertBookForLibrarian');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send(jsonData);
}

function RegisterNewStudent() {
    if(document.getElementById("user_type").value === "student"){
        let myForm = document.getElementById('myForm');
        let formData = new FormData(myForm);
        const data = {};
        formData.forEach((value, key) => (data[key] = value));
        var jsonData = JSON.stringify(data);
        var xhr = new XMLHttpRequest();
        console.log(jsonData);
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                $('#ajaxContent').html("Successful Student Registration. Welcome! ");
            } else if (xhr.status !== 200) {
                $('#ajaxContent').html("ERROR!");
            }
        };
        xhr.open('POST', 'RegisterStudent');
        xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
        xhr.send(jsonData);
    }
}

function RegisterNewLibrarian() {
    if(document.getElementById("user_type").value === "libman"){
        
        let myForm = document.getElementById('myForm');
        let formData = new FormData(myForm);
        const data = {};
        formData.forEach((value, key) => (data[key] = value));
        var jsonData = JSON.stringify(data);
        var xhr = new XMLHttpRequest();
        console.log(jsonData);
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                $('#ajaxContent').html("Successful Librarian Registration. Welcome! ");
            } else if (xhr.status !== 200) {
                $('#ajaxContent').html("ERROR!");
            }
        };
        xhr.open('POST', 'RegisterLibrarian');
        xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
        xhr.send(jsonData);
    }
}

function hideWho() {
    if(document.getElementById("user_type").value === "student") {
        hide_libman();
    }
    else if(document.getElementById("user_type").value === "libman") {
        hide_student();
    }
    console.log(document.getElementById("user_type").value);
}

function hide_student() {
    
    document.getElementById("student_attr").style.display = "none";
    document.getElementById("libman_attr").style.display = "block";
    
    document.getElementById("address").innerText= "Library Address:";
    
    whoIsIt = 1;
}

function hide_libman() {
    
    document.getElementById("libman_attr").style.display = "none";
    document.getElementById("student_attr").style.display = "block";
  
    document.getElementById("address").innerText= "Home Address:";
    
    whoIsIt = 0;
}

// REST API
function postBook () {
    let myForm = document.getElementById('post_books');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);
    var xhr = new XMLHttpRequest();
    console.log(jsonData);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#out1').html("Successful Book Post! ");
        } else if (xhr.status !== 200) {
            $('#out1').html("Unsuccessful Book Post!");
        }
    };
    xhr.open('POST', 'http://localhost:8080/ServletWithDatabaseConnection2022_2023/book_modification/book_modif/book/');
    xhr.setRequestHeader('Content-type','application/json');
    xhr.send(jsonData);
}

function getBook () {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const obj = JSON.parse(xhr.responseText);
            var i=1;
            document.getElementById("out2").innerHTML="";
            var count= Object.keys(obj).length;
            document.getElementById("output2").innerHTML="<h3>"+count+" Books</h3>";
            for(i=1; i<count; i++){
                document.getElementById("output2").innerHTML+=createTableFromJSON(obj[i],i);
            }
        } else if (xhr.status !== 200) {
            document.getElementById('out2').innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"+JSON.stringify(xhr.responseText);
        }
    };

    var genre=document.getElementById("genre2").value;
    var URL="http://localhost:8080/ServletWithDatabaseConnection2022_2023/book_modification/book_modif/books/"+genre;
    var from=document.getElementById("year1").value;
    var to=document.getElementById("year2").value;
    if(genre!==""){
        if(from !== "" && to === "")
            URL+="?fromYear="+from;
        else if(from === "" && to !== "")
            URL+="?toYear="+to;
        else if(from !== "" && to !== "")
            URL+="?fromYear="+from+"&toYear="+to;
    }	
    console.log(URL);
    xhr.open("GET", URL);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();

}

function updateBook () {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
           document.getElementById('out3').innerHTML=JSON.stringify(xhr.responseText);
            
        } else if (xhr.status !== 200) {
            document.getElementById('out3')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"+
					JSON.stringify(xhr.responseText);
 
        }
    };
    var isbn=document.getElementById("isbn3").value;
    var pages=document.getElementById("pages3").value;
    xhr.open('PUT', 'http://localhost:8080/ServletWithDatabaseConnection2022_2023/book_modification/book_modif/bookpages/'+isbn+"/"+pages);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function deleteBook () {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
           document.getElementById('out4').innerHTML=JSON.stringify(xhr.responseText);
            
        } else if (xhr.status !== 200) {
            document.getElementById('out4')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"+
					JSON.stringify(xhr.responseText);
 
        }
    };
    var isbn=document.getElementById("isbn4").value;
    xhr.open('DELETE', 'http://localhost:8080/ServletWithDatabaseConnection2022_2023/book_modification/book_modif/bookdeletion/'+isbn);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}