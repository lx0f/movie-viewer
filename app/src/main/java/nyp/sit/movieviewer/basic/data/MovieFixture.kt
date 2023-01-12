package nyp.sit.movieviewer.basic.data

import kotlinx.coroutines.delay
import nyp.sit.movieviewer.basic.entity.Movie

class MovieFixture {
    companion object {
        private var moviesArrayList: ArrayList<Movie> = ArrayList()

        init {
            populateSimpleMovieItem()
        }

        suspend fun getAllMovies(): ArrayList<Movie> {
            // TODO: delete this
            delay(1500)
            return moviesArrayList
        }

        private fun populateSimpleMovieItem(): ArrayList<Movie> {

            moviesArrayList.add(
                Movie(
                    "The next installment in the franchise and the conclusion of the Star Wars sequel trilogy as well as the Skywalker Saga",
                    "December 20, 2019",
                    "English",
                    "Star Wars: The Rise of Skywalker (2019)"
                )
            )

            moviesArrayList.add(
                Movie(
                    "Elsa, Anna, Kristoff and Olaf head far into the forest to learn the truth about an ancient mystery of their kingdom.",
                    "November 22, 2019",
                    "English",
                    "Frozen II (2019)"
                )
            )

            moviesArrayList.add(
                Movie(
                    "In Jumanji: The Next Level, the gang is back but the game has changed. As they return to rescue one of their own, the players will have to brave parts unknown from arid deserts to snowy mountains, to escape the world's most dangerous game.",
                    "December 13, 2019",
                    "English",
                    "Jumanji: The Next Level"
                )
            )

            moviesArrayList.add(
                Movie(
                    "Ip Man 4 is an upcoming Hong Kong biographical martial arts film directed by Wilson Yip and produced by Raymond Wong. It is the fourth in the Ip Man film series based on the life of the Wing Chun grandmaster of the same name and features Donnie Yen reprising the role. The film began production in April 2018 and ended in July the same year.",
                    "December 25, 2019",
                    "English",
                    "Ip Man 4: The Finale"
                )
            )

            moviesArrayList.add(
                Movie(
                    "Super-assassin John Wick returns with a \$14 million price tag on his head and an army of bounty-hunting killers on his trail. After killing a member of the shadowy international assassin’s guild, the High Table, John Wick is excommunicado, but the world’s most ruthless hit men and women await his every turn.",
                    "August 23, 2019",
                    "English",
                    "John Wick: Chapter 3 - Parabellum "
                )
            )

            moviesArrayList.add(
                Movie(
                    "After faking his death, a tech billionaire recruits a team of international operatives for a bold and bloody mission to take down a brutal dictator.",
                    "December 13, 2019",
                    "English",
                    "6 Underground "
                )
            )

            moviesArrayList.add(
                Movie(
                    "After fighting his demons for decades, John Rambo now lives in peace on his family ranch in Arizona, but his rest is interrupted when Gabriela, the granddaughter of his housekeeper María, disappears after crossing the border into Mexico to meet her biological father. Rambo, who has become a true father figure for Gabriela over the years, undertakes a desperate and dangerous journey to find her.",
                    "December 17, 2019",
                    "English",
                    "Rambo: Last Blood "
                )
            )

            moviesArrayList.add(
                Movie(
                    "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
                    "August 13, 2019",
                    "English",
                    "Avengers: Endgame"
                )
            )

            moviesArrayList.add(
                Movie(
                    "Growing up can be a bumpy road, and it's no exception for Riley, who is uprooted from her Midwest life when her father starts a new job in San Francisco. Riley's guiding emotions— Joy, Fear, Anger, Disgust and Sadness—live in Headquarters, the control centre inside Riley's mind, where they help advise her through everyday life and tries to keep things positive, but the emotions conflict on how best to navigate a new city, house and school.",
                    "June 19, 2015",
                    "English",
                    "Inside Out"
                )
            )

            moviesArrayList.add(
                Movie(
                    "Young hobbit Frodo Baggins, after inheriting a mysterious ring from his uncle Bilbo, must leave his home in order to keep it from falling into the hands of its evil creator. Along the way, a fellowship is formed to protect the ringbearer and make sure that the ring arrives at its final destination: Mt. Doom, the only place where it can be destroyed.",
                    "December 19, 2001",
                    "English",
                    "The Lord of the Rings: The Fellowship of the Ring"
                )
            )
            return moviesArrayList
        }
    }
}