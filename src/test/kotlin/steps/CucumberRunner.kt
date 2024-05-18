package steps

import br.com.fiap.postech.module
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/test/resources/features"],
)
class CucumberRunner {
    companion object {
        private lateinit var server: NettyApplicationEngine

        @BeforeClass
        @JvmStatic
        fun setUp() {
            server = embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            server.start(wait = false) // start the server asynchronously
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            server.stop(1000, 1000) // stop the server with a grace period
        }
    }
}

