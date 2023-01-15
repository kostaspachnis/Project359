


function createBooksTablePerGenreFromJSON(data) {

    var i = 0;
    var books = JSON.parse(data);

    if(books.length === 0) return "No books found!";

    var genre = books[0].genre;
    var html = "<table class='table table-striped table-bordered table-hover' id='booksTable'>";
    html += "<thead><tr><th>ISBN</th><th>Title</th><th>Authors</th><th>URL</th><th>Photo</th><th>Pages</th><th>Year</th></tr></thead>";
    html += "<tbody>";

    for(var i = 0; i < books.length; i++) {
        if(books[i].genre === genre) {
            html += "<tr>";
            html += "<td>" + books[i].isbn + "</td>";
            html += "<td>" + books[i].title + "</td>";
            html += "<td>" + books[i].authors + "</td>";
            html += "<td>" + books[i].url + "</td>";
            html += "<td>" + books[i].photo + "</td>";
            html += "<td>" + books[i].pages + "</td>";
            html += "<td>" + books[i].publicationyear + "</td>";
            html += "</tr>";
        }
        else {

            html += "</tbody></table>";
            genre = books[i].genre;
            html += "<table class='table table-striped table-bordered table-hover' id='booksTable'>";
            html += "<thead><tr><th>ISBN</th><th>Title</th><th>Authors</th><th>URL</th><th>Photo</th><th>Pages</th><th>Year</th></tr></thead>";
            html += "<tbody>";
            html += "<tr>";
            html += "<tr>";
            html += "<td>" + books[i].isbn + "</td>";
            html += "<td>" + books[i].title + "</td>";
            html += "<td>" + books[i].authors + "</td>";
            html += "<td>" + books[i].url + "</td>";
            html += "<td>" + books[i].photo + "</td>";
            html += "<td>" + books[i].pages + "</td>";
            html += "<td>" + books[i].publicationyear + "</td>";
            html += "</tr>";
        }
    }

    html += "</tbody></table>";
    return html;
}


function getBooks() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("book_tables").style.display="block";
            $("#book_tables").html(createBooksTablePerGenreFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#book_tables").html("Error!");
        }
    };

    xhr.open('GET', 'GuestUserShowBooks?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}