
function cancel() {
    window.location = "librarian.html";
}


function RegisterNewLibrarian() {
        
    let myForm = document.getElementById('myForm');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);
    var xhr = new XMLHttpRequest();
    console.log(jsonData);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#ajaxContent').html("Successful Librarian Registration. Welcome!");
            setTimeout(function(){window.location = "librarian.html";}, 3000);
        } else if (xhr.status !== 200) {
            $('#ajaxContent').html("ERROR!");
        }
    };
    xhr.open('POST', 'RegisterLibrarian');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send(jsonData);
}