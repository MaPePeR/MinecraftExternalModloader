Minecraft External Modloader
============================

This piece of Software allows you to manipulate the behaivior of Java-Programs by *exchanging* classes with your own modified ones.  
Which is especially used in **Minecraft-Modding** and this will be the intended use for this program, but it might be possible to use this for any other java programs as well.  
The normal way of exchanging classes is to open the .jar file with an archive program and replace .class files. If the jar is signed you also have to delete the META-INF folder.

**This Program will not replace any .class files in any jar file**, but will modify the way java loads classes to get the same result.

How to use with Minecraft
-------------------------
1. Install Mods
1. Download the minecraft.jar from http://minecraft.net/download ( *Show all Platforms -> Minecraft for Linux/Other -> minecraft.jar* )  
   **WARNING:** This is a different minecraft.jar then the one you can find in the bin folder of your Minecraft installation!
1. Place the MinecraftExternalModloader.jar next to this minecraft.jar (the launcher, not the one in bin!)
1. Start the MinecraftExternalModloader.jar by double-clicking or with `java -jar MinecraftExternalModloader.jar` on the commandline
   This will bring up the default Mojang Minecraft-Launcher which will then hopefully launch your modded Minecraft.

How to install Mods
-------------------
1. Create a folder named `mod` inside your Minecraft Installation Folder (`%APPDATA%\.minecraft` on Windows, `~/.minecraft` on Linux ...)
1. Extract the mod you want to install into that folder (The parts that normally would go into the minecraft.jar)

Experts
-------
The program contains two parts.  
`GenerateStartCommand` generates a command-line that has all the needed parameters to launch `StartMinecraft` which will then inject the `ReplacingClassLoader` to load mods.  
Run `java -jar MinecraftExternalModloader.jar -dummy`. To get the command-line that is generated to launch `StartMinecraft`.  
This will output something like  

<pre><code>javaw -Xmx1024m -Djava.library.path="/C:/Users/<em>YourName</em>/AppData/Roaming/.minecraft/natives" -Dsun.java2d.noddraw=true -Dsun.java2d.d3d=false -Dsun.java2d.opengl=false -Dsun.java2d.pmoffscreen=false -classpath [classpath ommitted] mapeper.minecraft.modloader.StartMinecraft <strong>file:/C:/Users/<em>YourName</em>/AppData/Roaming/.minecraft/mod/</strong></pre></code>

on Windows 7.  

**file:/C:/Users/<em>YourName</em>/AppData/Roaming/.minecraft/mod/** is the folder the mods will get loaded from. The Inner-Program will allow you to specify multiple URLs. Even http links to jar files might be possible but are not recommended.  
You can now create your personal batch/bash-Script to launch your mods.  
This will skip the first step of the Minecraft-Launching

Credits
-------
Thanks to the great Amazon-Kindle-Hacker *ixtab* who showed me the right technique in his [JBPatcher](http://www.mobileread.com/forums/showthread.php?t=175512) (Source Code on [bitbucket.org](https://bitbucket.org/ixtab/jbpatcher/src/08953dac405d?at=master)).  
Also thanks to the MCP-Guys for making Minecraft-Modding possible.