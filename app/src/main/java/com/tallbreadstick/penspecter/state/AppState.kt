package com.tallbreadstick.penspecter.state

class AppState(
    private var username: String?,
    private var email: String?
) {

    constructor() : this(null, null)

}