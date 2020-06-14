var mongoose = require('mongoose');

var UserSchema = mongoose.Schema({
	username: {
		type: String,
		index:true
	},
	password: {
		type: String
	},
	email: {
		type: String
	},
	phone: {
		type: Number
	}
});

var User = mongoose.model('customers', UserSchema);


var BookingSchema = mongoose.Schema({
	username: {
		type: String,
		index:true
	},
	source: {
		type: String
	},
	dest: {
		type: String
	},
	ticket_number: {
		type: Number
	},
	bus_number: {
		type: Number
	},
	number_of_persons: {
		type: Number
	},
	amount: {
		type: Number
	}
});

var Booking = mongoose.model('booking', BookingSchema);


module.exports = {
    booking : Booking,
    user : User
}



