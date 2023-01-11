


function createUsersTableFromJSON() {

    var html = "<table class=\"table table-striped\"><tr><th>username</th><th>firstname</th><th>lastname</th></tr>";

    html += getStudents();
    html += getLibrarians();

    html += "</table>";

    document.getElementById("usersTable").innerHTML = html;
}

function createInnerUsersTableFromJSON(data) {
    var html = "";
    let yoho= data.toString();
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
            document.getElementById("outputdiv").style.display="block";
            $("#outputdiv").html(createInnerUsersTableFromJSON((xhr.responseText)));
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