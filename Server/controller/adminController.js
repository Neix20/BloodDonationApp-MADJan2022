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

// Get List of Admin
async function getAllAdmin(req, res) {
    let adminDict = {};
    let adminRef = firebase.database().ref().child("admins/");
    let snap = await adminRef.once("value");
    snap.forEach(val => {
        let tmp_admin = val.val();
        adminDict[tmp_admin.id] = tmp_admin;
    });

    console.log("Retrieve List of Admin Data successfully!");
    return res.status(200).json({ data: adminDict });
}

// Get Admin
async function getAdmin(req, res) {
    let id = req.query.id;

    let adminRef = firebase.database().ref().child(`admins/${id}`);

    let admin = await adminRef.once("value");
    admin = admin.val();

    console.log("Retrieved Admin Data Successfully!");
    return res.status(200).json({ data: admin });
}

// Add Admin
async function addAdmin(req, res) {

    let adminRef = firebase.database().ref().child("admins/").push();

    let _id = adminRef.key,
        _email = req.body.email,
        _name = req.body.name,
        _password = req.body.password;

    await adminRef.set({
        id: _id,
        email: _email,
        password: _password,
        name: _name
    });

    console.log(`Admin ${_email} has been added successfully!`);
    return res.status(200).json({ data: `Admin ${_email} has been added successfully!` });
}

// Update Admin
async function updateAdmin(req, res) {

    let _id = req.body.id,
        _email = req.body.email,
        _name = req.body.name,
        _password = req.body.password;

    let adminRef = firebase.database().ref().child(`admins/${_id}`);

    await adminRef.set({
        id: _id,
        email: _email,
        password: _password,
        name: _name
    });

    console.log(`Admin ${_email} has been updated successfully!`);
    return res.status(200).json({ data: `Admin ${_email} has been updated successfully!` });
}

// Delete Admin
async function deleteAdmin(req, res) {
    let _id = req.body.id;

    let adminRef = firebase.database().ref().child(`admins/${_id}`);

    let admin = await adminRef.once("value");
    admin = admin.val();

    adminRef = adminRef.remove();

    console.log(`Admin ${admin.email} has been deleted successfully!`);
    return res.status(200).json({ data: `Admin ${admin.email} has been removed successfully!` });
}

// Delete All User
async function deleteAllAdmin(req, res) {
    let adminRef = firebase.database().ref().child("admins/").remove();
    console.log(`All Admin data has been removed successfully!`);
    return res.status(200).json({ data: `All Admin data has been removed successfully!` });
}

module.exports = {
    getAllAdmin,
    getAdmin,
    addAdmin,
    updateAdmin,
    deleteAdmin,
    deleteAllAdmin
}