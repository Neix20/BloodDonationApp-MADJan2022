// Import Library
const firebase = require("firebase-admin");
const serviceAccount = require("./../certificate/private-key.json");

// Initialize Firebase Credentials
firebase.initializeApp({
    credential: firebase.credential.cert(serviceAccount),
    databaseURL: "https://blooddonationmad-b0e0e-default-rtdb.asia-southeast1.firebasedatabase.app/"
});

// Get List of User
async function getAllUser(req, res) {
    let userDict = {};
    let userRef = firebase.database().ref().child("users/");
    let snap = await userRef.once("value");
    snap.forEach(val => {
        let tmp_user = val.val();
        userDict[tmp_user.id] = tmp_user;
    });

    console.log("Retrieve List of User Data successfully!");
    return res.status(200).json({ data: userDict });
}

// Get User
async function getUser(req, res) {
    let id = req.query.id;

    let userRef = firebase.database().ref().child(`users/${id}`);

    let user = await userRef.once("value");
    user = user.val();

    console.log(user);

    console.log("Retrieved User Data Successfully!");
    return res.status(200).json({ data: user });
}

// Add User
async function addUser(req, res) {

    let userRef = firebase.database().ref().child("users/").push();

    let _id = userRef.key,
        _email = req.body.email,
        _name = req.body.name,
        _password = req.body.password;

    console.log(_email, _name, _password);

    await userRef.set({
        id: _id,
        email: _email,
        password: _password,
        name: _name
    });

    console.log(`User ${_email} has been added successfully!`);
    return res.status(200).json({ data: `User ${_email} has been added successfully!` });
}

// Update User
async function updateUser(req, res) {

    let _id = req.body.id,
        _email = req.body.email,
        _name = req.body.name,
        _password = req.body.password;

    let userRef = firebase.database().ref().child(`users/${_id}`);

    await userRef.set({
        id: _id,
        email: _email,
        password: _password,
        name: _name
    });

    console.log(`User ${_email} has been updated successfully!`);
    return res.status(200).json({ data: `User ${_email} has been updated successfully!` });
}

// Delete User
async function deleteUser(req, res) {
    let _id = req.body.id;

    let userRef = firebase.database().ref().child(`users/${_id}`);

    let user = await userRef.once("value");
    user = user.val();

    userRef = userRef.remove();

    console.log(`User ${user.email} has been deleted successfully!`);
    return res.status(200).json({ data: `User ${user.email} has been removed successfully!` });
}

// Delete All User
async function deleteAllUser(req, res) {
    let userRef = firebase.database().ref().child("users/").remove();
    console.log(`All User data has been removed successfully!`);
    return res.status(200).json({ data: `All User data has been removed successfully!` });
}

module.exports = {
    getAllUser,
    getUser,
    addUser,
    updateUser,
    deleteUser,
    deleteAllUser
}