'use strict'

const express = require('express');
const app = express();
const server = require('http').Server(app);
const io = require('socket.io')(server);

import path from 'path';

// Port setting
const PORT = process.env.PORT || 3000;

// Static file setting
app.use('/public', express.static('public'));

/**
 * Routing
 */
app.get('/', function(req, res) {
  res.sendFile(path.resolve('public/views/index.html'));
});


// Listening setting
server.listen(PORT, function(){
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
