const webpack = require('webpack');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

const BUILD_DIR = path.resolve(__dirname, 'src/main/resources/static/assets/js');
const INDEX_TEMPLATE = path.resolve(__dirname, 'src/main/resources/index.html');
const APP_DIR = path.resolve(__dirname, 'src/main/resources/js');
const SRC = path.resolve(__dirname, 'src/main/resources/static');

const config = {
        entry: {
            vendor: ['react', 'react-dom'],
            // bundle: APP_DIR + '/app.jsx',
            game: APP_DIR + '/GameApp.jsx'
        },
        output: {
            path: BUILD_DIR,
            filename: '[name].js'
        },
        module: {
            rules: [
                {
                    test: /\.(js|jsx)$/,
                    include: APP_DIR,
                    use: [
                        'babel-loader'
                    ]
                },
                {
                    test: /\.css$/,
                    loader: ExtractTextPlugin.extract({
                        use: 'css-loader',
                    }),
                }
            ]
        },
        devServer: {
            contentBase: SRC,
            proxy: {
                '/maps/america': {
                    target: 'http://localhost:9090/',
                    secure: false,
                    changeOrigin: true
                }
            }
        },
        plugins: [
            new HtmlWebpackPlugin({
                template: INDEX_TEMPLATE,
            }),
            new ExtractTextPlugin('style.bundle.css'),
            new webpack.optimize.CommonsChunkPlugin({
                name: 'vendor',
                filename: 'vendor.js',
                minChunks: Infinity
            })
        ],

    }
;

module.exports = config;
