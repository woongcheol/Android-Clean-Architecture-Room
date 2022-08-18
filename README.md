## 맨땅에 안드로이드 : Room
안드로이드 클린 아키텍쳐를 알아가고 있습니다. 라이브러리 Room은 다음 [자료](https://blog.yena.io/studynote/2018/09/08/Android-Kotlin-Room.html)를 참고하여 구현했으며,  
지속적으로 업데이트 될 예정입니다.  

`최근 업데이트 : '22. 08. 10.`
<br/>
## 본론
### 1. Android Room이란?
SQLite 개체 매핑 라이브러리로, Jetpack 라이브러리의 아키텍쳐 권장사항에 해당하는 AAC(Android Architecture Components)에서 제공합니다.
### 2. 기능
⚠ 안드로이드에서는 SQLite 보다 Room 라이브러리 사용을 권장합니다. 

![](https://user-images.githubusercontent.com/86638578/182986978-8d28e412-aa90-4d54-81da-ff06b7db4e9f.png)

출처 : [Android SQLite Docs](https://developer.android.com/training/data-storage/sqlite)

- Room은 컴파일하는 시간에 SQL 유효성 검사를 수행한다.
- 스키마가 바뀌었을 때 영향 받는 SQL 쿼리를 직접 바꾸지 않아도 된다.
- 상용구 코드 없이 DB 객체를 자바 객체에 매핑한다.
- LiveData, RxJava와 같이 작동할 수 있다.

### 3. Room 구성요소
![image](https://user-images.githubusercontent.com/86638578/183840483-0ef7506e-0e57-4ee2-bf02-4a2406109a47.png)

출처 : [Android Room Docs](https://developer.android.com/training/data-storage/room?hl=ko)

- `Entity` : DB에 들어갈 테이블을 Kotlin Class로 구현한 것이다. 데이터 모델 클래스라고도 한다.
- `DAO` : Database Access Object의 약자이다. DB에 접근하여 insert, delete 등을 수행한다.
- `DB` : Database holder를 포함한다. 앱에 영구 저장되는 데이터와 기본 연결을 위한 주 액세스 지점이다. RoomDatabase를 상속받는 추상 클래스로, 테이블과 버전을 정의한다.

## 실습

### 1. 공통
- Room의 구성 요소들은 Clean Architecture의 `Data Layer`에 해당됩니다.
- 빌드 종속 항목 추가 : build.gradle(module)
```kotlin
def room_version = "2.4.3" // `22.8.10 기준

implementation "androidx.room:room-runtime:$room_version"
kapt "androidx.room:room-compiler:$room_version"
```

### 2. Entity
![image](https://user-images.githubusercontent.com/86638578/183840167-5c3724ec-d375-42d5-a6a2-2e31fddb12ce.png)
#### 다음은 [`docs`](https://developer.android.com/training/data-storage/room/defining-data?hl=ko) 복합 기본 키 정의까지 설명되었습니다.
- 엔터티를 구성하기 위해 데이터 클래스에 어노테이션 @Entity을 달아줍니다.  
- 엔터티에는 기본 키(@PrimaryKey)를 반드시 정의해야 하며 이를 포함한 하나 이상의 필드가 존재합니다.
- 클래스 이름은 DB 테이블 이름으로 사용되지만, tableName으로 지정할 수 있습니다.
- 마찬가지로 필드의 열 이름은 @ColumnIfo으로 지정할 수 있습니다.
- 복합키(Composite Key)는 다음처럼 지정할 수 있습니다.
```kotlin
@Entity(primaryKeys = ["firstName", "lastName"])
data class User(
    val firstName: String?,
    val lastName: String?
)
```
