/*
RESTFul Services by NodeJS
Author: KenHoang
Update: 22/01/2019
*/

var express = require('express');
var mysql = require('mysql');
var bodyParser = require('body-parser')
// Connect to MySQL
var conn = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'admin', // Default mysql password in XAMPP is blank
  database: 'FGManga' // Name of database you just import
});

// Create RESTFul
var app = express();
var publicDir = (__dirname + '/public/'); // Set static dir for display image local by url
app.use(express.static(publicDir));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// Get all banner
app.get("/banner", (req, res, next) => {
  conn.query('SELECT * FROM banner', function(error, result, fields) {
    conn.on('error', function(err) {
      console.log('[MY SQL ERROR]', err);
    });

    if (result && result.length) {
      res.end(JSON.stringify(result));
    } else {
      res.end(JSON.stringify("No banner here"));
    }
  })
});

// Get all comic
app.get("/comic", (req, res, next) => {
  conn.query('SELECT * FROM manga', function(error, result, fields) {
    conn.on('error', function(err) {
      console.log('[MY SQL ERROR]', err);
    });

    if (result && result.length) {
      res.end(JSON.stringify(result));
    } else {
      res.end(JSON.stringify("No comic here"));
    }
  })
});

// Get chapter by manga id
app.get("/chapter/:mangaid", (req, res, next) => {
  conn.query('SELECT * FROM chapter where MangaID=?', [req.params.mangaid], function(error, result, fields) {
    conn.on('error', function(err) {
      console.log('[MY SQL ERROR]', err);
    });

    if (result && result.length) {
      res.end(JSON.stringify(result));
    } else {
      res.end(JSON.stringify("No chapter here"));
    }
  })
});

// Get images by chapter id
app.get("/links/:chapterid", (req, res, next) => {
  conn.query('SELECT * FROM link where ChapterId=?', [req.params.chapterid], function(error, result, fields) {
    conn.on('error', function(err) {
      console.log('[MY SQL ERROR]', err);
    });

    if (result && result.length) {
      res.end(JSON.stringify(result));
    } else {
      res.end(JSON.stringify("No chapter here"));
    }
  })
});

// Start Server
app.listen(3000, ()=>{
  console.log("KenHoang Comic API running on port 3000");
})
