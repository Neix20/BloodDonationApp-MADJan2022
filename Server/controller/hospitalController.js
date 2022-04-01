// Import Library
const firebase = require("firebase-admin");
const serviceAccount = require("./../certificate/private-key.json");

// Initialize Firebase Credentials
if (!firebase.app.length) {
    firebase.initializeApp({
        credential: firebase.credential.cert(serviceAccount),
        databaseURL: "https://blooddonationmad-b0e0e-default-rtdb.asia-southeast1.firebasedatabase.app/"
    });
}