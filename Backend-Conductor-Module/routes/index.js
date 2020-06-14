const express = require('express');
const app = express();
const url_redirect = require('url');


var Schema = require('../models/schema');

/*mongo shell
mongo ds141674.mlab.com:41674/covid_bus -u ram -p madhumitha123
*/

app.post('/book', (req,res) =>{
    var name = req.body.name;
    var source = req.body.source;
    var dest = req.body.dest;
    var ticket_number = req.body.ticket_number;
    var bus_number = req.body.bus_number;
    var number_of_persons = req.body.number_of_persons;
    var amount = req.body.amount;
    
    var book = Schema.booking({
        username:name,
        source:source,
        dest:dest,
        ticket_number:ticket_number,
        bus_number:bus_number,
        number_of_persons:number_of_persons,
        amount:amount,
    })

    Schema.booking.create(book, function(err, book) {
        if(err) res.send(err);  
        else
            res.send(book);
        });
    
});

app.get('/check', (req,res) =>{
    res.render('check',{
        query:"nil"
    });
});


app.get('/scan', (req,res) =>{
    res.render('scan-qr');
});

app.get('/booking-details', (req,res) =>{
    var ticket = req.query.ticket;
    
    Schema.booking.findOne({ticket_number: ticket}, function(err, user) {
        if(err) res.send(err);
        if(!user) {
            res.render('check',{
              query:"error"
          });
        }
          else{
            res.render('booking-details',{
                query:user
            })
        }
});

});

app.post('/check', (req,res) =>{
    

    Schema.booking.findOne({ticket_number: req.body.ticket}, function(err, user) {
        if(err) res.send(err);
        if(!user) {
            res.render('check',{
              query:"error"
          });
        }
          else{
            res.render('booking-details',{
                query:user
            })
        }        
     });
       
});

module.exports = app;