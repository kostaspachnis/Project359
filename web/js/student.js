


function signup() {
    window.location = "signup_student.html";
}


function searchBook() {

    let myForm = document.getElementById('bookSearchForm');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 202) {
            console.log("success");
        } else if (xhr.status !== 202) {
            console.log("error");
        }
    };

    // var data = $('#bookSearchForm').serialize();

    xhr.open('GET', 'FindBookForStudent?');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send(jsonData);
}