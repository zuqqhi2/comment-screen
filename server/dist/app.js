'use strict';

var _path = require('path');

var _path2 = _interopRequireDefault(_path);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var express = require('express');
var app = express();
var server = require('http').Server(app);
var io = require('socket.io')(server);

// Port setting
var PORT = process.env.PORT || 3000;

// Static file setting
app.use('/public', express.static('public'));

/**
 * Routing
 */
app.get('/', function (req, res) {
  res.sendFile(_path2.default.resolve('public/views/index.html'));
});
app.get('/test.html', function (req, res) {
  res.sendFile(_path2.default.resolve('public/views/test.html'));
});
app.get('/test2.html', function (req, res) {
  res.sendFile(_path2.default.resolve('public/views/test2.html'));
});

app.get("/env", function (req, res) {
  res.writeHead(200, { 'Content-Type': 'text/plain' });
  var all_env_vars = Object.keys(process.env).map(function (varname, index) {
    return varname + ": " + process.env[varname] + "\n";
  }).join("");
  res.end(all_env_vars);
});

// Listening setting
server.listen(PORT, function () {
  console.log('listening on *:', PORT);
});

/**
 * Socket.io logic
 */
io.sockets.on('connection', function (socket) {
  console.log('a user connected');

  //socket.emit('message_from_server', 'hello, world');
  socket.on('message_from_client', function (msg) {
    console.log('message:', msg);
  });

  socket.on('chat message', function (msg) {
    console.log('chat message:', msg);
    socket.broadcast.emit('chat message', msg);
    socket.emit('chat message', msg);
    socket.broadcast.emit('message_from_server', msg);
  });
});