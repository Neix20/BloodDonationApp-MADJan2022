const firebase = require("firebase-admin");
const serviceAccount = require("./certificate/private-key.json");

const UserModel = require("./model/user");

// Initialize Firebase Credentials
firebase.initializeApp({
    credential: firebase.credential.cert(serviceAccount),
    databaseURL: "https://blooddonationmad-default-rtdb.asia-southeast1.firebasedatabase.app/"
});

const ref = firebase.database().ref();

// Write List of Data

// userRef = ref.child(`users/${testUser.getName()}`);

userRef = ref.child("users/");

// userRef = userRef.push();
// let id = userRef.key;
// testUser = new UserModel.User(id, "txen2000@gmail.com", "arf11234", "Justin");
// userRef = userRef.set({
//     id: testUser.getId(),
//     email: testUser.getEmail(),
//     password: testUser.getPassword(),
//     name: testUser.getName()
// });

// Read Data
userRef.on("value", function(snapshot) {
    let userDict = snapshot.val(),
        userList = [];

    for (let user_id in userDict) {
        let user = userDict[user_id];
        user = UserModel.userAdapter(user);
        userList.push(user);
    }

    console.log(userList);
}, function(error) {
    console.log("Error: " + error.code);
});