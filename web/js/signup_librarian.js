
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

// ================================= Checks =================================

var uni;
var mail_flag;
var get;

function passwordToggle() {
    var x = document.getElementById("password");
    var y = document.getElementById("toggle");
    x.type === 'password' ? x.type = 'text' : x.type = 'password';
    y.className === 'fa-regular fa-eye' ? y.className = 'fa-solid fa-eye-slash' : y.className = 'fa-regular fa-eye';
}

function passwordStrength() {
    var password = document.getElementById('password').value;
    var strength = document.getElementById('strength');

    if (password.includes('helmepa') || password.includes('uoc') || password.includes('tuc')) {
        strength.style.color = 'red';
        strength.innerHTML = 'Weak';
        return false;
    }

    if (password.match(/[0-9]g/) != null && password.match(/[0-9]/g).length >= password.length / 2) {
        strength.style.color = 'red';
        strength.innerHTML = 'Weak';
        return false;
    }

    if (password.match(/[a-z]/g) && password.match(/[A-Z]/g) && password.match(/[^a-zA-Z\d]/g) && password.match(/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/g).length >= 2) {
        strength.style.color = 'green';
        strength.innerHTML = 'Strong';
    } else {
        strength.style.color = 'orange';
        strength.innerHTML = 'Medium';
    }

    return true;
}


function validate() {

    if(!passwordStrength()) {
        alert('Password is too weak!');
        return false;
    }

    return true;
}

var map = new OpenLayers.Map("map");
map.addLayer(new OpenLayers.Layer.OSM());
function map_show(x, y) {
    map = new OpenLayers.Map("map");
    map.addLayer(new OpenLayers.Layer.OSM());
    map.updateSize();

    document.getElementById('map').style.height = '300px';
    document.getElementById('map').style.width = '700px';

    var lonLat = new OpenLayers.LonLat(x, y)
          .transform(
            new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
          
    var zoom=16;

    var markers = new OpenLayers.Layer.Markers( "Markers" );
    map.addLayer(markers);
    
    markers.addMarker(new OpenLayers.Marker(lonLat));
    
    map.setCenter (lonLat, zoom);
}

function request() {
    const data = null;
    const country = document.getElementById('country').value;
    const city = document.getElementById('city').value;
    const address = document.getElementById('address').value;

    const full_address = country + ', ' + city + ', ' + address;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = false;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            if(JSON.parse(this.responseText).length > 0) {
                get = JSON.parse(this.responseText);
                document.getElementById('map-message').textContent = 'Found location';
                map_show(get[0].lon, get[0].lat);
            }
        } else {
            document.getElementById('map-message').textContent = 'Loading...';
        }
    });

    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q="+full_address+"&accept-language=en&polygon_threshold=0.0");
    xhr.setRequestHeader("X-RapidAPI-Key", "d3da8ffb6emshf934200861c165cp134e9ajsn04cf19b7aa6a");
    xhr.setRequestHeader("X-RapidAPI-Host", "forward-reverse-geocoding.p.rapidapi.com");

    xhr.send(data);
}