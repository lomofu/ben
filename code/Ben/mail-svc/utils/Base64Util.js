const encodeText = function (origin) {
    return Buffer.from(origin).toString('base64');
};

const decodeText = function (str) {
    return Buffer.from(str, 'base64').toString();
};

module.exports.encodeText = encodeText;
module.exports.decodeText = decodeText;
