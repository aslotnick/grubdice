package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.config.HibernateConfig
import co.grubdice.scorekeeper.config.PropertyFileLoader
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
class ScoreDaoTest extends AbstractTransactionalTestNGSpringContextTests{

    @Autowired
    ScoreDao scoreDao

    @Autowired
    PlayerDao playerDao

    @Autowired
    GameDao gameDao

    def player1
    def player2
    def player3

    @BeforeMethod
    public void setup() {
        player1 = new Player("player 1")
        player2 = new Player("player 2")
        player3 = new Player("player 3")
    }

    @Test
    public void testGettingAScoreBoard() throws Exception {
        gameDao.save(getNewGame(player1, player2, player3))
        gameDao.save(getNewGame(player1, player2, player3))
        gameDao.save(getNewGame(player1, player2, player3))

        def playerBoard = scoreDao.findByPlayer(player1)
        assertThat(playerBoard).hasSize(3)
    }

    static private Game getNewGame(Player... placeOrder) {
        def game = new Game(postingDate: DateTime.now(), type: GameType.LEAGUE, players: 3)
        placeOrder.eachWithIndex { player, index ->
            game.results << new GameResult(game, player, placeOrder.size() - index - 1)
        }
        return game
    }
}
