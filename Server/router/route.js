const express = require('express');
const path = require('path');
const router = express.Router();

// Get Request
router.get('/', (req, res) => {
    res.sendFile(`${__dirname}/public/index.html`);
});

module.exports = router;