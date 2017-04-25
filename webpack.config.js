var webpack = require('webpack');
var path = require('path');

var BUILD_DIR = path.resolve(__dirname, 'src/main/resources/static/assets/js');
var APP_DIR = path.resolve(__dirname, 'src/main/resources/js');

var config = {
        entry: {
            vendor: ['react', 'react-dom'],
            bundle: APP_DIR + '/app.jsx',
            game: APP_DIR + '/GameApp.jsx'
        },
        output: {
            path: BUILD_DIR,
            filename: '[name].js'
        },
        module: {
            loaders: [
                {
                    test: /\.jsx?/,
                    include: APP_DIR,
                    loader: 'babel'
                }
            ]
        },
        plugins: [
            new webpack.optimize.CommonsChunkPlugin({
                name: 'vendor',
                filename: 'vendor.js',
                minChunks: Infinity
            })
        ]
    }
    ;

module.exports = config;
