package com.example.bb.backend

class User {
    // variables
    var name = ""
    var password = "" // its cool to be unsecure
    var logged_in = false
    var budgets = ArrayList<Budget>()
    var activeBud = 0

    var reports = ArrayList<Report>()
    var alertSetting = AlertSetting()

    // constructors
    constructor(name: String, password: String) {
        this.name = name
        this.password = password
    }

    fun login(entered: String): Boolean {
        if(password.compareTo(entered)==0)
            logged_in = true

        return logged_in
    }
    // adding/removing
    fun addBudget(b: Budget) {
        budgets.add(b)
    }

    // debug/toString
    fun showBudgets(): String {
        var str: String = this.toString()

        for(b in budgets) {
            str += "\n  $b"
        }

        return str
    }
    override fun toString(): String {
        return "$name: ........"
    }

    // aka static
    companion object {
        fun sample(): User {
            val u = User("Landon Moon", "password")
            val b = Budget.sample()

            u.addBudget(b)

            return u
        }

        fun users() : List<User>{
            var users = ArrayList<User>()

            users.add(sample())

            //adding an additional user
            var u = User("Parker Steach", "password")
            var b = Budget.sample()
            u.addBudget(b)
            users.add(u)

            u = User("Jeremiah Richard", "password")
            b = Budget.sample()
            u.addBudget(b)
            users.add(u)

            u = User("Juan Alcaraz", "password")
            b = Budget.sample()
            u.addBudget(b)
            users.add(u)


            return users
        }
    }
}