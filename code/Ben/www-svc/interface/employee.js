const Mongoose = require("mongoose");

const employeeSchema = new Mongoose.Schema({
  email: {
    type: String,
    unique: true,
    required: [true, "Employee email required"]
  },
  name: {
    type: String,
    unique: true,
    minlength: 1,
    maxlength: 20,
    validate: {
      validator: function(value) {
        return (
          /^[a-zA-Z0-9_\u4e00-\u9fa5\s·]+$/.test(value) &&
          !/(^\_)|(\__)|(\_+$)/.test(value) &&
          !/^\d+\d+\d$/.test(value)
        );
      },
      message: "{VALUE} is not a valid fullName!"
    },
    required: [true, "Employee fullName required"]
  },
  gender: {
    type: Number,
    required: [true, "Employee gender required"]
  },
  phoneNumber: {
    type: String,
    minlength: 11,
    maxlength: 11,
    validate: {
      validator: function(value) {
        return /^1(3|4|5|7|8)\d{9}$/.test(value);
      },
      message: "{VALUE} is not a valid phone!"
    },
    required: [true, "Employee phone number required"]
  },
  companyId: {
    type: String,
    required: [true, "Employee companyName required"]
  },
  teamId: {
    type: String,
    required: [true, "Employee teamId required"]
  },
  token: {
    type: String,
    required: [true, "Employee token required"]
  },
  expireAt: {
    type: Date,
    index: {
      expireAfterSeconds: 0
    }
  }
});

const employee = Mongoose.model("employee", employeeSchema);

module.exports = employee;
