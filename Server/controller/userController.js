// Import Library
const { json } = require("express");
const firebase = require("firebase-admin");
const serviceAccount = require("./../certificate/private-key.json");
const UserModel = require("./../model/user");

// Initialize Firebase Credentials
firebase.initializeApp({
    credential: firebase.credential.cert(serviceAccount),
    databaseURL: "https://blooddonationmad-default-rtdb.asia-southeast1.firebasedatabase.app/"
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
    let id = req.query.id,
        userDict = {};

    let userRef = firebase.database().ref().child("users/");
    let snap = await userRef.once("value");
    snap.forEach(val => {
        let tmp_user = val.val();
        userDict[tmp_user.id] = tmp_user;
    });

    console.log("Retrieved User Data Successfully!");
    return res.status(200).json({ data: userDict[id] });
}

// Add User
async function addUser(req, res) {

    let userRef = firebase.database().ref().child("users/");
    userRef = userRef.push();

    let _id = userRef.key,
        _email = req.body.email,
        _name = req.body.name,
        _password = req.body.password;

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

    let userRef = firebase.database().ref().child("users/");

    let _id = req.body.id,
        _email = req.body.email,
        _name = req.body.name,
        _password = req.body.password;

    userRef = userRef.child(_id);

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
    let userRef = firebase.database().ref().child("users/");

    let _id = req.body.id;

    userRef = userRef.child(_id).remove();

    console.log(`User has been deleted successfully!`);
    return res.status(200).json({ data: `User has been removed successfully!` });
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