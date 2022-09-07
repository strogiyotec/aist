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
       up [telegram bot](https://www.google.com/search?q=telegram+bot&sxsrf=ALiCzsb_S7nL7gEec03gcmXLwIK-Chiqsg%3A1662561038849&source=hp&ei=DqsYY6nCMb6H0PEP9pWx6AY&iflsig=AJiK0e8AAAAAYxi5HtLBWzAI1z8m9T_NQbt3VUqRbmkR&oq=telegram+bot&gs_lcp=Cgdnd3Mtd2l6EAMYATIECCMQJzIOCAAQgAQQsQMQgwEQiwMyCAgAEIAEEIsDMggIABCABBCLAzIICAAQgAQQiwMyCAgAEIAEEIsDMggIABCABBCLAzIICAAQgAQQiwMyCAgAEIAEEIsDMggIABCABBCLAzoKCC4QxwEQ0QMQJzoFCAAQkQI6CwgAELEDEIMBEJECOgsIABCABBCxAxCDAToRCC4QgAQQsQMQgwEQxwEQ0QM6CwguEIAEELEDEIMBOggIABCxAxCDAToQCAAQgAQQhwIQsQMQgwEQFDoICC4QgAQQsQM6CAgAEIAEELEDOgUIABCABDoKCAAQgAQQhwIQFDoICAAQgAQQyQNQAFibD2DmHGgBcAB4AIABeYgB6wmSAQQxMC4zmAEAoAEBuAED&sclient=gws-wiz))

## TODO

1. How it works
2. Why it's superior to other chrome based solutions
3. Make native image build


