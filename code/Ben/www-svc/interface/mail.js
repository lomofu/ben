const Mongoose = require('mongoose');

const mailSchema = new Mongoose.Schema({
    email: {
        type: String,
        unique: true,
        required: true
    },
    expireAt: {
        type: Date,
        index: {
            expireAfterSeconds: 0
        }

    },
    token: {
        type: String,
        unique: true,
        required: true
    }
});


const mail = Mongoose.model('mail', mailSchema);

module.exports = mail;
