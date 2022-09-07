# aist

ais visa watcher.
It monitors **ais** us visa website for new available interview dates and send
you a notification if there are any new early dates via telegram channel

## How to build ?

1. First you need java>=14 and mvn
    1. Install [sdkman](https://sdkman.io/install)
    2. In the shell type (`sdk install java 17.0.4-amzn`) to install java 17
    3. In the shell type (`sdk install maven 3.8.6`) to install maven
2. Build the project `mvn clean package`
3. Run the
   program `-Dpassword=password -Demail=email -DbotToken=5790465153:AAGkaOMeJ3sd9xVXBr8HeV4wYVMs9OslhLY -DchatId=@aisVisa -Djdk.httpclient.allowRestrictedHeaders=host -jar target/aist.jar`
   where
    1. `-Dpassword` is your ais application password
    2. `-Demail` is your ais email
    3. `-DchatId` is your telegram channel's chat id(Don't forget to make you
       telegram channel public and assign your bot as an administrator of your
       channel )
    4. `-DbotToken` is your telegram bot token(Instruction on how to set
       up [telegram bot](https://core.telegram.org/bots/api))

## TODO

1. How it works
2. Why it's superior to other chrome based solutions
3. Make native image build


