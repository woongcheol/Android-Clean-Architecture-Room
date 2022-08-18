`최근 업데이트 : '22. 08. 18.`

![img_2.png](https://resources.stdio.vn/content/article/luu-tru-du-lieu-voi-sqlite-trong-android/thumbnail-hd/blob-1605173321355@960x540.png)  
</br>

## 개요
#### ✔ 참고 자료를 바탕으로 작성했으나 일부 수정 및 삭제한 내용이 있습니다.
#### ✔ 다음 [자료](https://blog.yena.io/studynote/2018/09/08/Android-Kotlin-Room.html)를 참고하여 구현했으며, 지속적으로 업데이트 될 예정입니다.
#### ✔ 이외에도 [Clean Architecture Repo](https://github.com/woongcheol/Android-Clean-Architecture)에서 다양한 개념들을 확인하실 수 있습니다.
#### ✔ 오류 및 보완해야할 내용은 Contribution을 통해 기여부탁드리겠습니다🙇‍♂️
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
<br/>

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
#### 엔터티 정의
- 엔터티를 구성하기 위해 데이터 클래스에 어노테이션 @Entity을 달아줍니다.  
- 엔터티에는 기본 키(@PrimaryKey)를 반드시 정의해야 하며 이를 포함한 하나 이상의 필드가 존재합니다.
- 클래스 이름은 DB 테이블 이름으로 사용되지만, tableName으로 지정할 수 있습니다.
- 마찬가지로 필드의 열 이름은 @ColumnIfo으로 지정할 수 있습니다.
#### 복합키 지정
- 복합키(Composite Key)는 다음처럼 지정할 수 있습니다.
```kotlin
@Entity(primaryKeys = ["firstName", "lastName"])
data class User(
    val firstName: String?,
    val lastName: String?
)
```
#### 필드 무시
- 필드를 유지하지 않으려면 해당 필드에 @Ignore을 사용합니다.
```kotlin
@Entity
data class User(
    @PrimaryKey val id: Int,
    val firstName: String?,
    val lastName: String?,
    @Ignore val picture: Bitmap?
)
```
- 필드를 상속받는 경우 @Entity에서 ignoredColumns 속성을 사용합니다.
```kotlin
open class User {
var picture: Bitmap? = null
}

@Entity(ignoredColumns = ["picture"])
data class RemoteUser(
@PrimaryKey val id: Int,
val hasVpn: Boolean
) : User()
```
#### 테이블 검색 지원
- 테이블 검색 지원을 통해 테이블 내에서 세부 정보를 쉽게 검색합니다. `minSdkVersion 16 이상`
- FTS(전체 텍스트 검색)을 위해 FTS3 또는 FTS4를 사용합니다.
```kotlin
// Use `@Fts3` only if your app has strict disk space requirements or if you
// require compatibility with an older SQLite version.
@Fts4
@Entity(tableName = "users")
data class User(
    /* Specifying a primary key for an FTS-table-backed entity is optional, but
       if you include one, it must use this type and column name. */
    @PrimaryKey @ColumnInfo(name = "rowid") val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String?
)
```
- 테이블이 여러 언어로 된 컨텐츠를 지원할 경우 @Fts에서 languageId 속성을 사용합니다.
```kotlin
@Fts4(languageId = "lid")
@Entity(tableName = "users")
data class User(
    // ...
    @ColumnInfo(name = "lid") val languageId: Int
)
```
- 특정 열 색인을 통해 쿼리 속도를 높일 수 있습니다. 이를 위해 @Entity에서 indices 속성을 통해 색인에
포함하려는 열의 이름을 나열합니다.
```kotlin
@Entity(indices = [Index(value = ["last_name", "address"])])
data class User(
    @PrimaryKey val id: Int,
    val firstName: String?,
    val address: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @Ignore val picture: Bitmap?
)
```
- DB의 특정 필드나 필드 그룹이 고유해야할 경우 @Index의 unique 속성을 true로 설정하여 고유성 속성을 적용할 수 있습니다.
```kotlin
@Entity(indices = [Index(value = ["first_name", "last_name"],
        unique = true)])
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @Ignore var picture: Bitmap?
)
```