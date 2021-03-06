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

require('events').EventEmitter.defaultMaxListeners = 20;

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

// Get all category
app.get("/categories", (req, res, next) => {
  conn.query('SELECT * FROM category', function(error, result, fields) {
    conn.on('error', function(err) {
      console.log('[MY SQL ERROR]', err);
    });

    if (result && result.length) {
      res.end(JSON.stringify(result));
    } else {
      res.end(JSON.stringify("No category here"));
    }
  })
});


// Filter
app.post("/filter", (req, res, next) => {
  var post_data = req.body; // GET POST DATA from POST REQUEST
  var array = JSON.parse(post_data.data);
  var query = "SELECT * FROM manga WHERE ID IN (SELECT MangaID FROM mangacategory"; // Default query
  if (array.length > 0) {
    query += " GROUP BY MangaID";
    if (array.length == 1) // If user just submit 1 category
      query += " HAVING SUM (CASE WHEN CategoryID = " + array[0] + " THEN 1 ELSE 0 END) > 0)";
    else { // If user submit more than 1 category
      for (var i = 0; i < array.length; i++) {
        if (i == 0)
          query += " HAVING SUM (CASE WHEN CategoryID = " + array[i] + " THEN 1 ELSE 0 END) > 0 AND";
        else if (i == array.length - 1)
          query += " SUM (CASE WHEN CategoryID = " + array[i] + " THEN 1 ELSE 0 END) > 0)";
        else
          query += " SUM (CASE WHEN CategoryID = " + array[i] + " THEN 1 ELSE 0 END) > 0 AND";
      }
    }
  }
  conn.query(query, function(error, result, fields) {
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

// SEARCH MANGA BY NAME
app.post("/search", (req, res, next) => {
  var post_data = req.body; // GET POST DATA from POST REQUEST
  var name_search = post_data.search;
  var query = "SELECT * FROM manga WHERE Name LIKE '%" + name_search + "%'"; // Default query
  conn.query(query, function(error, result, fields) {
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

// Start Server
app.listen(3000, ()=>{
  console.log("KenHoang Comic API running on port 3000");
})
