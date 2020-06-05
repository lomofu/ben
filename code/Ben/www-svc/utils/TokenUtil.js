const jwt = require("jsonwebtoken");
const TOKEN = require("../public/Token");
const config = require(`../config/env/${process.env.NODE_ENV}`);

const createToken = function(payload) {
  return jwt.sign({ data: payload }, TOKEN.SECRET, {
    expiresIn: config.mail_config.expire
  });
};

const parseToken = function(token) {
  return jwt.verify(token, TOKEN.SECRET);
};

module.exports.createToken = createToken;
module.exports.parseToken = parseToken;
