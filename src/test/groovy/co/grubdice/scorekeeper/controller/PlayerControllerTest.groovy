package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.mock.interceptor.MockFor
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class PlayerControllerTest {

    def mockPlayerDao
    def playerDoa

    @BeforeMethod
    public void setup() {
        mockPlayerDao = new MockFor(PlayerDao)
    }

    @Test
    public void testSavingANewPlayer() throws Exception {
        mockPlayerDao.demand.save { }

        PlayerController controller = createControllerForMock()
        controller.createPlayer(new Player(name: 'test name'))

        mockPlayerDao.verify playerDoa
    }

    PlayerController createControllerForMock() {
        playerDoa = mockPlayerDao.proxyInstance()
        return new PlayerController(playerDao: playerDoa)
    }
}
