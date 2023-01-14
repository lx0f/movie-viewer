package nyp.sit.movieviewer.intermediate.entity

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable

@DynamoDBTable(tableName = "UserData")
data class UserData @JvmOverloads constructor(
    @DynamoDBHashKey
    var id: String = "",
    @DynamoDBAttribute(attributeName = "favMovie")
    var favouriteMovies: ArrayList<Movie> = arrayListOf()
)