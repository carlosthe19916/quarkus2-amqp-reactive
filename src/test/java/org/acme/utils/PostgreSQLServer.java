/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme.utils;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.HashMap;
import java.util.Map;

public class PostgreSQLServer implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> postgreSQL;

    @Override
    public Map<String, String> start() {
        postgreSQL = new GenericContainer<>("postgres:13.2")
                .withExposedPorts(5432)
                .withEnv("POSTGRES_DB", "my_db")
                .withEnv("POSTGRES_USER", "my_db_username")
                .withEnv("POSTGRES_PASSWORD", "my_db_password")
                .waitingFor(Wait.forLogMessage(".* database system is ready to accept .*", 1));
        postgreSQL.start();

        String host = postgreSQL.getHost();
        Integer port = postgreSQL.getMappedPort(5432);

        return new HashMap<>() {{
            put("quarkus.datasource.username", "my_db_username");
            put("quarkus.datasource.password", "my_db_password");
            put("quarkus.datasource.reactive.url", "vertx-reactive:postgresql://" + host + ":" + port + "/my_db");
        }};
    }

    @Override
    public void stop() {
        postgreSQL.stop();
    }

}
