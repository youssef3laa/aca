export default class User {
    username = null;
    password = null;
    organization = 'mod';
    SAMLart = null;

    create(json){
        for (const [key, value] of Object.entries(json)) {
            if(key in this)  this[key] = value
        }
    }

    getUsername() {
      return this.username;
    }
    setUsername(username){
        this.username = username;
    }
    setPassword(password) {
      this.password = password;
    }
    setSAMLart(SAMLart){
        this.SAMLart = SAMLart;
    }
    getSAMLart() {
      return this.SAMLart;
    }
    toJson(){
        return  {
            username: this.username,
            password: this.password,
            organization: this.organization,
            SAMLart: this.SAMLart
        }
    }   
    toString(){
        return JSON.stringify( {
            username: this.username,
            password: this.password,
            organization: this.organization,
            SAMLart: this.SAMLart
        })
    }
}