# Fabric Example Mod

## Setup

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

### Install the Java Development Kit

Download and install the propper Java development kit (JDK) version from [AdoptOpenJDK](https://adoptium.net/en-GB/temurin/releases/).
To see what version of Java you need, check the [minecraft wiki](https://minecraft.wiki/w/Tutorials/Update_Java).
Currently, these are the latest java version requirements for minecraft:
- From Minecraft 1.17.1 and up: Java 16
- From Minecraft 1.18 and up: Java 17
- From Minecraft 1.20.5 and up: Java 21

### Install an IDE

Install an IDE of your choice. We recommend using [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/).

### Initialize a new project (optional)

If you want to start a new project, you can use the [FabricMC template mod generator](https://fabricmc.net/develop/template/).
Make sure you:
- give your mod a name,
- enter a mod ID,
- choose a unique package name,
- select the propper minecraft version,
- **check the `Data Generation` checkbox!**
- **uncheck the `Split client and common sources` checkbox!** (Not confirmed yet, but it's recommended by Kaupenjoe (When creating server mods, I would expect this should be checked))

Add the downloaded files to a git repo and open the project in your IDE.

Alternatively, you can use an existing project.

### Configure the IDE

If you open the project in IntelliJ IDEA, the project should be automatically configured.
After the configuration is done, follow the following steps:
- Go to `File` -> `Project Structure` -> `Project` -> `SDK` and select the proper JDK version.
- Go to `File` -> `Settings` -> `Build, Execution, Deployment` -> `Build Tools` -> `Gradle` and make sure that `Gradle JVM` is set to the `Project SDK`.
- In the project browser, click the options menu (three dots) and under `Tree Appearance`, make sure that `Flatten Packages` and `Compact Middle Packages` are unchecked. (This is a personal preference)

### Configure the project

I'll refer to the project root folder as `<ROOT>`.
The server code is located in the `src/main/java/net/<author>/<modid>` folder. In this guide, I'll use the `<BACKEND>` placeholder to refer to this folder.
The resources are located in the `src/main/resources` folder. In this guide, I'll use the `<RESOURCES>` placeholder to refer to this folder.

#### Configure a new project

- In the `<BACKEND>` folder, you'll find a class implementing the `ModInitializer` class. This is the main class of your mod.
  - Refactor the `MOD_ID` to your mod ID in snake case (all lowercase or numbers, words separated by underscores).
  - Refactor the class `ExampleMod` to your mod name in camel case (first letter of each word is capitalized, no underscores).
- If you've got an `ExampleModDataGenerator` class, refactor it to your mod name in camel case, suffixed with `DataGenerator`.
- In the `<RESOURCES>/assets/modid` folder, you'll find an icon for your mod. Replace this icon with your own icon or remove it.
- Open the `<RESOURCES>/fabric.mod.json` file.
  - Make sure that the `id` field is set to your mod ID.
  - Set the `name` field to your mod name.
  - Set the `description` field to a short description of your mod.
  - Add your mod's authors to the `authors` field.
  - Change your mod's license in the `license` field.
  - Set the `icon` field to the path of your mod's icon.
  - In the `entrypoints` field, make sure that the entrypoints are updated correctly to your previously refactored classes.
  - For the `depends` and `suggest` fields, refer to the [manage dependencies](#manage-dependencies) section.

### Manage your mod version

Your mod version can be changed in the `<ROOT>/gradle.properties` file using the `mod_version` property.
The version should specify your own version number, following the [Semantic Versioning](https://semver.org/) standard.
It's also recommended to specify the Minecraft version in the version number.

Use the following format to specify your mod version: `<major>.<minor>.<patch>-<minecraft_version>`.
- `<major>`: The major version of your mod.
- `<minor>`: The minor version of your mod.
- `<patch>`: The patch version of your mod.
- `<minecraft_version>`: The Minecraft version that your mod is compatible with. 

For example, if your mod is compatible with Minecraft 1.20.1, you could use the version `1.0.0-1.20.1`.

### Manage the Java, Minecraft, and Fabric versions

When changing the Java, Minecraft, or Fabric versions, you must update the versions in two separate files:
- The `<ROOT>/gradle.properties` file specifies the versions that will be used to compile your mod.
- The `<RESOURCES>/fabric.mod.json` file specifies the versions that your mod is compatible with.

#### Change the compilation targets

In the `<ROOT>/gradle.properties` file, defines some variables that will be used in the `<ROOT>/build.gradle` file.
- The `minecraft_version` property specifies the Minecraft version what will be used when compiling the mod.
- The `loader_version` property specifies the Fabric loader version what will be used when compiling the mod.
- The `yarn_mappings` property specifies the Yarn mappings version what will be used when compiling the mod.
  - These mappings are used to map the obfuscated Minecraft code to human-readable names.
- The `fabric_version` property specifies the Fabric API version what will be used when compiling the mod.

The recommended versions can be found using a fabric tool found [here](https://fabricmc.net/develop/).

You can add additional libraries or mods into the `build.gradle` and refer to a custom variable defined in the `gradle.properties` file.

#### Manage project dependencies

In the `<RESOURCES>/fabric.mod.json` file, you'll find a `depends` object.
This object defines what versions of which dependencies are required for your mod to run.
In other words, this object defines what versions **could** be used.
- The `fabricloader` dependency defines the version of the Fabric loader that is required to run your mod.
  A changelog of the Fabric loader can be found [here](https://github.com/FabricMC/fabric-loader/releases).
- The `minecraft` dependency defines the version of Minecraft that your mod is compatible with.
  A changelog of the Minecraft versions can be found [here](https://minecraft.fandom.com/wiki/Java_Edition_version_history).
- The `java` dependency defines the version of Java that your mod is compatible with.
  A changelog of the Java versions can be found [here](https://en.wikipedia.org/wiki/Java_version_history).
  Make sure that the Java version is [compatible](https://minecraft.wiki/w/Tutorials/Update_Java) with the Minecraft version.
- The `fabric-api` dependency defines the version of the Fabric API that your mod is compatible with.
  Check for compatibility on the [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?page=1&pageSize=20)
  or [Modrinth](https://modrinth.com/mod/fabric-api/versions#all-versions) page of the Fabric API.
- You can also add other dependencies that your mod requires to run.

When updating the dependencies, make sure to update your [compilation targets](#change-the-compilation-targets) as well.

##### Manage dependency versions

Versions are usually defined according to the [Semantic Versioning](https://semver.org/) standard.
You can use several operators to define the version:
- `*` matches any version.
- `=1.2.3` matches exactly version 1.2.3.
- `>=1.2.3` matches any version equal or greater than 1.2.3.
- `>1.2.3` matches any version greater than 1.2.3.
- `<=1.2.3` matches any version equal or less than 1.2.3.
- `<1.2.3` matches any version less than 1.2.3.
- `^1.2.3` matches any version equal or greater than 1.2.3 but less than 2.0.0 (major version is fixed).
- `~1.2.3` matches any version equal or greater than 1.2.3 but less than 1.3.0 (minor version is fixed).

Dependencies can be added to one of the following blocks:
- `dependencies` block: A failure to match causes a hard failure.
- `recommends` block: A failure to match causes a warning.
- `suggests` block: These dependencies are not matched and are primarily used as metadata.
- `conflicts` block: A successful match causes a warning.
- `breaks` block: A successful match causes a hard failure.

All this and more information can be found in the [Fabric documentation](https://fabricmc.net/wiki/documentation:fabric_mod_json_spec).

### Browse dependency source files

Before you can browse the source files of a dependency, you need to make sure you configure the source files in the `build.gradle` file.
After configuring, you can reload the project and generate the source files.
```shell
./gradlew --refresh-dependencies
./gradlew genSources
```

After generating the source files, you'll need to tell your IDE what source files to use.
In this example, we'll configure this in IntelliJ IDEA for the Minecraft source files.
- In your project browser, go to `<BACKEND>/mixin/ExampleMixin` and `Ctrl` + `Click` on the `MinecraftServer` class.
- You'll see a blue popup with a message telling you the sources could not be found.
- Click on the `Choose Sources...` button and choose the jar file ending with `-sources.jar`

In some source files, you'll see some errors. You can ignore those but you now have a much more representative view of the Minecraft source files.

### Start a minecraft server with your mod

All gradle commands can be found in the gradle tab, on the right side of your IDE.
Use the following gradle commando's to:
- run a server with your mod: Tasks -> fabric -> runServer
- run a client with your mod: Tasks -> fabric -> runClient
- build your mod: Tasks -> build -> build

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
