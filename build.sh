echo "Setup Script"
mvn clean install &&
mv target/Kukkura.jar dev/Kukkura.jar
echo "Moved Kukkura.jar in dev environment"