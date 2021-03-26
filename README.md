# Running

```sh
./gradlew run --args="foo bar baz" # logger options don't seem to work
```

```sh
./gradlew install
./gradlew distTar
tar -xf  build/distributions/brix-0.1.0-SNAPSHOT.tar
./brix-0.1.0-SNAPSHOT/bin/brix
```
