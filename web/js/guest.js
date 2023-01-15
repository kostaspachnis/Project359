




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