# Current Version 
[![](https://jitpack.io/v/SharryChoo/SToolbar.svg)](https://jitpack.io/#SharryChoo/SToolbar)
- "-x" 表示使用的是 jetpack androidx 的依赖
- 若使用 AppCompat 将 '-x' 后缀去除即可

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
Add it in your **module build.gradle** at the end of dependencies
```
dependencies {
    ...
    implementation 'com.github.SharryChoo:SToolbar:+'
    // Dependencies
    implementation "com.android.support:appcompat-v7:$supportLibraryVersion"
}
```
# How to use
https://www.jianshu.com/p/e91ed59ecdb4
