var fse = require('fs-extra');
var workingPaths = require("../package.json").workingPaths;

var staticPath = workingPaths.static;

fse.remove(staticPath + '**/*', function (err) {
    if (err) {
        return console.error(err);
    }
});
