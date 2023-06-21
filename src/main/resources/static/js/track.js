let map = L.map('map').setView([51.505, -0.09], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

let content = document.getElementById('routeCoordinates').value;

const _xmlTrackPointToLatLng = (trkpoint) => {
    return [parseFloat(trkpoint.attributes.lat.nodeValue), parseFloat(trkpoint.attributes.lon.nodeValue)]
}

let gpxDom = (new DOMParser()).parseFromString(content, 'text/xml');

const trackPoints = Array.from(gpxDom.getElementsByTagName("trkpt"));

let latlngs = trackPoints.map((trkpnt) => _xmlTrackPointToLatLng(trkpnt));

let polyline = L.polyline(latlngs, {color: 'red'}).addTo(map);

map.fitBounds(polyline.getBounds());

function haversine(lat1, lon1, lat2, lon2) {
    Number.prototype.toRad = function() {
        return this * Math.PI / 180;
    }

    let r = 6371; // km
    let x1 = lat2 - lat1;
    let dLat = x1.toRad();
    let x2 = lon2 - lon1;
    let dLon = x2.toRad();
    let a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
    let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return r * c;
}

let totalDistance = 0;
let previosPoint;
for (let i = 0; i < latlngs.length; i++) {
    if (i === 0) {
        previosPoint = latlngs[i];
    } else {
        let currentPoint = latlngs[i];
        totalDistance += haversine(previosPoint[0], previosPoint[1], currentPoint[0], currentPoint[1]);
        previosPoint = currentPoint;
    }
}

document.getElementById('totalDistance').textContent = totalDistance.toFixed(2);