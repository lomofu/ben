const Mongoose = require('mongoose');

const accountSchema = new Mongoose.Schema({
    email: {
        type: String,
        unique: true,
        validate: {
            validator: function (value) {
                return /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(value);
            },
            message: '{VALUE} is not a valid email!'
        },
        required: [true, 'User email required']
    },
    name: {
        type: String,
        unique: true,
        minlength: 1,
        maxlength: 20,
        validate: {
            validator: function (value) {
                return /^[a-zA-Z0-9_\u4e00-\u9fa5\s·]+$/.test(value) && !/(^\_)|(\__)|(\_+$)/.test(value) && !/^\d+\d+\d$/.test(value);
            },
            message: '{VALUE} is not a valid fullName!'
        },
        required: [true, 'User fullName required']
    },
    gender: {
        type: Number,
        required: [true, 'User gender required']
    },
    phoneNumber: {
        type: String,
        minlength: 11,
        maxlength: 11,
        validate: {
            validator: function (value) {
                return /^1(3|4|5|7|8)\d{9}$/.test(value);

            },
            message: '{VALUE} is not a valid phone!'
        },
        required: [true, 'User phone number required']
    },
    password: {
        type: String,
        required: [true, 'User password required']
    },
    confirmPassword: {
        type: String,
        required: function (value) {
            return this.password === value;
        }
    },
    type: {
        type: Number,
        required: [true, 'User type required']
    },
    companyName: {
        type: String,
        minlength: 1,
        maxlength: 20,
        required: [true, 'User companyName required']
    },
    companyAddress: {
        type: String,
        maxlength: 100
    }

});

const account = Mongoose.model('account', accountSchema);

module.exports = account;
