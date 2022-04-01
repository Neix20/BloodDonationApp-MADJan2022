// Import Library
const firebase = require("firebase-admin");
const serviceAccount = require("./../certificate/private-key.json");

// Initialize Firebase Credentials
if (!firebase.app.length) {
    firebase.initializeApp({
        credential: firebase.credential.cert(serviceAccount),
        databaseURL: "https://blooddonationmad-default-rtdb.asia-southeast1.firebasedatabase.app/"
    });
}