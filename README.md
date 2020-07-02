# LiquidGalaxySimpleCMS 

<table><tr><td><img src="https://raw.githubusercontent.com/dfriveros11/LiquidGalaxySimpleCMS/develop/DemoSimpleCMSAndroid.gif" width="350" height="250"/></td><td><img src="https://raw.githubusercontent.com/dfriveros11/LiquidGalaxySimpleCMS/develop/DemoSimpleCMSLiquidGalaxy.gif" width="350" height="250"/></td></tr></table>


## Deploy

The first step is to download the project. (Remember you need to install the USB of OEM controllers of your tablet (windows). You can read more of the drivers here: [Drivers]( https://developer.android.com/studio/run/oem-usb) )

There are 3 forms to install the project.

### First

Connected the tablet to the computer, and then, go to the folder you download, open the terminal and write the following command: 

```
C:\\Downloads\SimpleCMS> gradlew installDebug
```

This will create the apk and installed to your tablet. 

You can read more in the following link: [Install the project in your tablet]( https://developer.android.com/studio/build/building-cmdline#DebugMode)

### Second

Open the project in Android Studio. Then, click on the tab build on the top of android studio, and click make the project. 
Finally, click on the tab Run on the top of android studio, and click on run.  You can follow the step in the following GIF: 

![](https://raw.githubusercontent.com/dfriveros11/LiquidGalaxySimpleCMS/develop/RunSimpleCMS.gif)

### Third 

Then, go to the folder you download, open the terminal and write the following command: 

```
C:\\Downloads\SimpleCMS> gradlew assembleDebug
```

This command will create an apk in the following route: 
project_name/module_name/build/outputs/apk/module_name-debug.apk

Finally you can install the apk to your tablet by using the adb install. (You need to have your tablet connect to your computer by USB). Open the terminal and run the following command: 

```
  C:\\Downloads\SimpleCMS>  adb -d install path/to/your_app.apk
```

You can read more in the following link: [Install the project in your tablet]( https://developer.android.com/studio/build/building-cmdline#DebugMode)


