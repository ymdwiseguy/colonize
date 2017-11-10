const express = require('express');
const america = require('../../maps/america.json');

const app = express();

app.get('/maps/america', (req, res) => res.status(200).send(america));

app.listen(9090, function () {
    console.log('Col API, listening on port 9090!')
});
