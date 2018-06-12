# Marama View

This repository represents the view layer of the Marama-editor. This project uses [libGDX](https://libgdx.badlogicgames.com/) and depends on the [Marama-editor](https://github.com/chide-marama/marama-editor) repository written in Scala.

TODO: describe connection to marama-editor project (technical details).

###Travis
[![Build Status](https://travis-ci.org/LuneCoding/marama-view.png)](https://travis-ci.org/LuneCoding/marama-view)
## Install
This project uses [libGDX](https://libgdx.badlogicgames.com/). More info about the project structure is found [here](https://github.com/libgdx/libgdx/wiki/Project-Setup-Gradle#project-layout).

#### Intellij
- Clone the repository
- File -> Open -> build.gradle

#### Eclipse
- Clone the repository
- File -> Import -> General -> Existing Projects into Workspace

## Running
#### Desktop
- Locate DesktopLauncher.java inside desktop/src/com/marama/game/desktop/ 
- Create a run configuration
- Add '/android/assets' to the working directory (when using assets).
- Run the configuration

#### Android

- Install the [Android-SDK](https://developer.android.com/studio/)
- Make sure the project has access to the SDK path
  - Attach your SDK path to the ANDROID_HOME environment variable OR,
  - Add a local.properties file at the root of the project and add the following: sdk.dir=[path-to-your-SDK]
- run AndroidLauncher.java

#### iOS
Building for iOS is only available inside a OSX/MacOS environment. 
- Install RoboVM plugin and create a run configuration targeting IOSLauncher.java
- Remove the 'component name="FacetManager"' node from ios/ios.iml
- Run the RoboVM configuration

## Useful links

- [libGDX documentation](https://libgdx.badlogicgames.com/documentation/)
- [libGDX Wiki](https://github.com/libgdx/libgdx/wiki) 
- [libGDX API Docs](https://libgdx.badlogicgames.com/nightlies/docs/api/)
- [useful tutorials](https://xoppa.github.io/blog/basic-3d-using-libgdx/)
