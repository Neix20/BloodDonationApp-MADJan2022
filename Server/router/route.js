const express = require('express');
const path = require('path');
const router = express.Router();

// User Controller
const userController = require("./../controller/userController");

router.get('/getAllUser', userController.getAllUser);
router.get('/getUser', userController.getUser);
router.post('/addUser', userController.addUser);
router.post('/updateUser', userController.updateUser);
router.post('/deleteUser', userController.deleteUser);
router.post('/deleteAllUser', userController.deleteAllUser);

// Admin Controller
const adminController = require("./../controller/adminController");

router.get('/getAllAdmin', adminController.getAllAdmin);
router.get('/getAdmin', adminController.getAdmin);
router.post('/addAdmin', adminController.addAdmin);
router.post('/updateAdmin', adminController.updateAdmin);
router.post('/deleteAdmin', adminController.deleteAdmin);
router.post('/deleteAllAdmin', adminController.deleteAllAdmin);

module.exports = router;