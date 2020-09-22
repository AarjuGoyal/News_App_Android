var express = require('express');
var request = require('request');

const googleTrends = require('google-trends-api');

app = express(),
port = process.env.PORT || 8000;

// const productRoutes = require('./api/prodcuts'); 

app.listen(port);

var nytime_api = "GgPSAzsdIxjDFlCpskEzUL5WG1e4BBCr";
var username = "aarju";
var nytimes_home = "https://api.nytimes.com/svc/topstories/v2/home.json?api-key="+nytime_api;

var nytimes_section = "https://api.nytimes.com/svc/topstories/v2/";
var nytimes_section_remianing = ".json?api-key=" + nytime_api;

var nytimes_article = 'https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=web_url:("';
var nyitmes_article_remianing = '")&api-key=' + nytime_api;

var guardian_api = "360d55ef-f616-4733-b330-a5bbebd68d61";
var guardian_home = "https://content.guardianapis.com/search?api-key="+ guardian_api + "&section=(sport|business|technology|politics)&show-blocks=all";

var guardian_section = "https://content.guardianapis.com/"
var guardian_section_remaining = "?api-key=" + guardian_api + "&show-blocks=all";

var guardian_article = "https://content.guardianapis.com/";
var guardian_article_remaining = "?api-key=" + guardian_api + "&show-blocks=all";

var ny_query_article_search = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=";
var ny_query_article_remaining = "&api-key=" + nytime_api;

var guardian_query_article_search = "https://content.guardianapis.com/search?q=";
var guardian_query_article_remaining = "&api-key=" + guardian_api + "&show-blocks=all";

var guardian_home_news = "https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=" + guardian_api;

app.get('/nytimes', (req, res) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.setHeader('Content-Type', 'application/json');
    // console.log(nytimes_home);
    request(nytimes_home, function(error, response, body) {
        var obj1 = JSON.parse(response.body);
    
        res.status(200).send({
            articles: obj1
        });
    });

    
});

app.get('/nytimesSection', (req, res) => {
    var section = req.query.section;
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    var request_api = nytimes_section + section + nytimes_section_remianing;
    request(request_api, function(error, response, body) {
        var obj1 = JSON.parse(response.body);
        
        res.status(200).send({
            articles: obj1
        });
    });

});

app.get('/nytimesArticleSearch', (req, res) => {
    var web_url = req.query.weburl;
    // console.log("Web url recieved is ",web_url);
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    console.log("Nytimes Article Search");
    var request_api = nytimes_article + web_url + nyitmes_article_remianing;

    request(request_api, function(error, response, body) {
        var obj1 = JSON.parse(response.body);
        // console.log(response.body)
        res.status(200).send({
            articles: obj1
        });
    });

});

app.get('/guardianTimesSection', (req, res) => {
    var section = req.query.section;
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.setHeader('Content-Type', 'application/json');
    var request_api = guardian_section + section + guardian_section_remaining;
    console.log("Guardian Times Section API");
    request(request_api, function(error, response, body) {
        var obj1 = JSON.parse(response.body);
        
        res.status(200).send({
            articles: obj1
        });
    });

});

app.get('/guardianTimes', (req, res) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.setHeader('Content-Type', 'application/json');
    console.log("Guardian Times home API");
    request(guardian_home, function(error, response, body) {
    // console.log(response.body)
    var obj1 = JSON.parse(response.body);
    
    res.status(200).send({
        articles: obj1
    });
});

    
});

app.get('/guardianArticleSearch', (req, res) => {
    var ArticleId = req.query.weburl; //id basically
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.setHeader('Content-Type', 'application/json');
    var request_api = guardian_article + ArticleId + guardian_article_remaining;
    console.log("Guardian Times Article search API");
    request(request_api, function(error, response, body) {
        var obj1 = JSON.parse(response.body);
        
        res.status(200).send({
            articles: obj1
        });
    });

});

app.get('/searcharticles', (req, res) => {
    var ArticleQuery = req.query.q; //id basically
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.setHeader('Content-Type', 'application/json');
    var request_api = ny_query_article_search + ArticleQuery + ny_query_article_remaining;
    // console.log("Guardian Times Article search API");
    request(request_api, function(error, response, body) {
        var obj1 = JSON.parse(response.body);
        
        res.status(200).send({
            articles: obj1
        });
    });

});

// New here
app.get('/searchguardianarticles', (req, res) => {
    var ArticleQuery = req.query.q; //id basically
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.setHeader('Content-Type', 'application/json');
    var request_api = guardian_query_article_search + ArticleQuery + guardian_query_article_remaining;
    // console.log("Guardian Times Article search API");
    request(request_api, function(error, response, body) {
        var obj1 = JSON.parse(response.body);
        
        res.status(200).send({
            articles: obj1
        });
    });

});


app.get('/guardiantimesHomeNews', (req, res) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.setHeader('Content-Type', 'application/json');
    console.log("Guardian Times home API");
    request(guardian_home_news, function(error, response, body) {
    // console.log(response.body)
    var obj1 = JSON.parse(response.body);
    
    res.status(200).send({
        articles: obj1
    });
});
    
});

app.get('/gettrend', (req, res) => {
    
    var TrendQuery = req.query.keyword; //id basically
    console.log("google trend api keyword is " + TrendQuery);
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.setHeader('Content-Type', 'application/json');
    
    // console.log("Guardian Times Article search API");
    // request(request_api, function(error, response, body) {
    //     var obj1 = JSON.parse(response.body);
        
    //     res.status(200).send({
    //         articles: obj1
    //     });
    // });
    var start_date = new Date(2019,6,1)

    googleTrends.interestOverTime({keyword: TrendQuery, startTime: start_date}, function(err, results)
    {
          if(err) console.error('there was an error!', err);
          else 
            {
                console.log('my sweet sweet results');
                var obj1 = JSON.parse(results);
                res.status(200).send({
                    trend: obj1
                });
            }
    })

});


console.log('RESTful API server started on port ' + port)