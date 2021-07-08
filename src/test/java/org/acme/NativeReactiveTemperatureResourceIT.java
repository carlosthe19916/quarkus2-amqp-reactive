package org.acme;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeReactiveTemperatureResourceIT extends ReactiveTemperatureResourceTest {

    // Execute the same tests but in native mode.
}
