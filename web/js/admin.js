var booksperlibTable;

function authenticate() {
    
    var user = document.getElementById("username").value;
    var pass = document.getElementById("password").value;

    if(user === "admin" && pass === "admin12*") {
        getStudents();
        getLibrarians();
        getNoBooksPerLibrary();
        document.getElementById("loginDiv").style.display="none";
        document.getElementById("adminDiv").style.display="block";
    }
}

function createInnerUsersTableFromJSON(data) {
    var html = "";
    let yoho = data.toString();
    let help = yoho.split("}"); 
    
    for(var i=0; i<help.length-1; i++){
        html += "<tr>";
        let ok = help[i].concat("}");
        let ok1 = ok.slice(1);
        let helper = JSON.parse(ok1.toString());   
        for (const x in helper) {     
            var value = helper[x];
            html += "<td>" + value + "</td>";
        }
        html+="</tr>";
    }
    
    return html;
}


// Return the table with ALL students (Project Function)
function getStudents() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#studentsTable tr:last").after(createInnerUsersTableFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#studentsTable").html("Error!");
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
            $("#librariansTable tr:last").after(createInnerUsersTableFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#librariansTable").html("Error!");
        }
    };

    xhr.open('GET', 'GetLibrariansForAdmin?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function getNoBooksPerLibrary() {

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            booksperlibTable = Object.entries(JSON.parse(xhr.responseText));
            console.log(booksperlibTable);
        } else if (xhr.status !== 200) {
            $("#booksPerLibP").html("Error!");
        }
    };

    xhr.open('GET', 'GetBooksInEachLibraryForAdmin?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function drawChart() {
    // Define the chart to be drawn.
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Library');
    data.addColumn('number', 'Percentage');
    data.addRows(booksperlibTable);
    
    // Set chart options
    var options = {'title':'Books per Library', 'width':550, 'height':400};

    // Instantiate and draw the chart.
    var chart = new google.visualization.PieChart(document.getElementById ("booksPerLibDiv"));
    chart.draw(data, options);
}
