# Read Me

## Building and installation
Build using `maven` by running the following command from the project folder.
> `mvn clean install`

After the build, service can be started by running the following command
> `java -jar <project_folder>/target/github-connector-0.9.jar`

## Using the service
By default, service runs on port `8099`.

To check the user login use `/users/{login}` endpoint.

Additionally there is a debug endpoint `/debug/users` available after starting the service with `dev` profile, which lists all the users along with their req. count.


