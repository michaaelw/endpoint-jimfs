### Backend Coding Challenge
Directory Tree
<br>
<br>

#### Built With
* Java


### Getting Started
#### Prerequisites
To run, you need Java 17 on your machine.

The best way to do this if you're using a macOS is to run the following command:
```shell
curl -s "https://get.sdkman.io" | bash
```

Upon successful installation of sdkman, you can install java:
```shell
sdk install java 17.0.4-amzn
```

Now, make this java SDK version your default:
```shell
sdk default java 17.0.4-amzn
```

You can confirm that you have the correct version of java on your path with the following command:
```shell
java -version
```

The output should be something like:
```
openjdk version "17.0.4" 2022-07-19 LTS
OpenJDK Runtime Environment Corretto-17.0.4.8.1 (build 17.0.4+8-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.4.8.1 (build 17.0.4+8-LTS, mixed mode, sharing)
```

Download the challenge executable jar from the [bin](https://github.com/michaaelw/endpoint-jimfs/tree/master/bin) directory:

Go to the folder in which the resides and run the following command:
```shell
java -jar endpoint-jimfs-1.0.jar
```

To end the session, you can type **quit** or **exit**

