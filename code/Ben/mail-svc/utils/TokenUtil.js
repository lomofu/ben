const jwt = require('jsonwebtoken');
const TOKEN = require('../config/Token');

const createToken = function (payload) {
    return jwt.sign({data: payload}, TOKEN.SECRET);
};

const parseToken = function (token) {
    return jwt.verify(token, TOKEN.SECRET)
};

module.exports.createToken = createToken;
module.exports.parseToken = parseToken;
