package nyp.sit.movieviewer.advanced.entity

data class User(
    var login_name: String? = null,
    var password: String? = null,
    var email: String? = null,
    var admin_number: String? = null,
    var pem_group: String? = null,
    var verified: Boolean = false,
    var id: String? = null,
)
