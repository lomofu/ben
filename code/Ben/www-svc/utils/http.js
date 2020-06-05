const axios = require('axios');

axios.defaults.withCredentials = true;
axios.defaults.headers.post["Content-Type"] =
    "application/x-www-form-urlencoded";
const instance = axios.create({timeout: 10000, crossDomain: true});

module.exports = instance;
