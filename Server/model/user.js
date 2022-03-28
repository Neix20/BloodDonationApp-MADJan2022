// Create Class
class User {
    constructor(id, email, password, name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    getId() {
        return this.id;
    }

    setId(id) {
        this.id = id;
    }

    getEmail() {
        return this.email;
    }

    setEmail(email) {
        this.email = email;
    }

    getPassword() {
        return this.password;
    }

    setPassword(password) {
        this.password = password;
    }

    getName() {
        return this.name;
    }

    setName(name) {
        this.name = name;
    }

    toString() {
        return `ID: ${this.id}\nEmail: ${this.email}\nPassword: ${this.password}\nName: ${this.name}`;
    }

    equals(user) {
        return this.toString() == user.toString();
    }
}

function userAdapter(json) {
    let id = json.id;
    let email = json.email;
    let name = json.name;
    let password = json.password;
    return new User(id, email, password, name);
}

module.exports = {
    User,
    userAdapter
}