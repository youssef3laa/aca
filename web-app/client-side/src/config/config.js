var env = process.env.NODE_ENV || 'development'

console.log(process.env.NODE_ENV )

var config = {
    development: require('./development.js'),
    production: require('./production.js'),
    staging: require('./staging.js')
}
  
module.exports = config[env]