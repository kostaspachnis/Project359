


function createUsersTableFromJSON() {

    document.getElementById("usersTableDiv").style.display="block";

    getStudents();
    getLibrarians();

    return html;
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
            $("#usersTable tr:last").after(createInnerUsersTableFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#usersTable").html("Error!");
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
            $("#usersTable tr:last").after(createInnerUsersTableFromJSON((xhr.responseText)));
        } else if (xhr.status !== 200) {
             $("#usersTable").html("Error!");
        }
    };

    xhr.open('GET', 'GetLibrariansForAdmin?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}