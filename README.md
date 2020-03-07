# Memo

라인 앱 챌린지 메모 앱



## Develop Environment

- Complier - **Android Studio**
- Language - **Kotlin**
- Compile SDK Version - **29**
- Optimized Device - **Galaxy s8+**
- Mininum SDK Version - **21**

## Main Used
 - Architecture - **MVVM**
	+ **Data Binding**
	+ **LiveData**
	+ **ViewModel**
	+ **Room**
- DI
	+ **Koin**

## Dependencies

* **Develop**


| Name                | Gradle                                        |
| ------------------- | --------------------------------------------- |
| Room                | androidx.room:room-runtime:2.2.3              |
|                     | androidx.room:room-compiler:2.2.3             |
| Koin                | org.koin:koin-androidx-scope:2.0.1            |
| Glide               | com.github.bumptech.glide:glide:4.9.0         |
| Lifecycle Extention | androidx.lifecycle:lifecycle-extensions:2.2.0 |

* **Test**

| Name              | Gradle                                           |
| ----------------- | ------------------------------------------------ |
| Junit 4           | junit:junit:4.12                                 |
|                   | android.arch.core:core-testing:1.1.1             |
|                   | androidx.test.ext:junit:1.1.1                    |
| Mockito to Kotlin | com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0 |
| Mockito           | org.mockito:mockito-core:2.25.0                  |
|                   | org.mockito:mockito-inline:2.21.0                |
| Koin Test         | org.koin:koin-test:2.0.1                         |


