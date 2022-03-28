const express = require('express');
const path = require('path');
const router = express.Router();

// Import Controller
const userController = require("./../controller/userController");

router.get('/getAllUser', userController.getAllUser);
router.get('/getUser', userController.getUser);
router.post('/addUser', userController.addUser);
router.post('/updateUser', userController.updateUser);
router.post('/deleteUser', userController.deleteUser);
router.post('/deleteAllUser', userController.deleteAllUser);

module.exports = router;