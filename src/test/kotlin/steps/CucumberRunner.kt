package steps

import br.com.fiap.postech.module
import com.typesafe.config.ConfigFactory
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import io.ktor.server.config.*
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
            val testConfig = ConfigFactory.load("application-test.conf")
            server = embeddedServer(Netty,
                environment = applicationEngineEnvironment {
                config = HoconApplicationConfig(testConfig)
                module { module() }
                    connector {
                        port = 8080
                        host = "0.0.0.0"
                    }
            })
            server.start(wait = false)
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            server.stop(1000, 1000) // stop the server with a grace period
        }
    }
}
