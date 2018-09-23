# Current Version 
[![](https://jitpack.io/v/SharryChoo/SToolbar.svg)](https://jitpack.io/#SharryChoo/SToolbar)

# How to integration
### Step 1
Add it in your **root build.gradle** at the end of repositories
```
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```

### Step 2
Add it in your **module build.gradle** at the end of repositories
```
dependencies {
    ...
    implementation 'com.github.SharryChoo:SToolbar:+'
    // Dependencies
    implementation "com.google.android.material:material:$supportLibraryVersion"
}
```
