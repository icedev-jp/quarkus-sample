# Quarkus

This mockup is based on example/sample project from Quarkus project creator. I modified it enough to barely conform to the given spec.

I never used Quarkus before, so it was a little journey to get it working (I wasted way too much time trying to automate CSV unmarshalling, failed anyway) It is by no means polished. No unit tests either.

To start it locally in dev mode, type `mvn compile quarkus:dev`, all endpoints are mapped to `/csv`

send CSV file using POST as multipart/form-data named "csv"

GET/DELETE requests use param PRIMARY_KEY

I use H2, because I couldn't be bothered to set up a real database (like postgres) on my laptop. I also use `database.generation=drop-and-create` because why not