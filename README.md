## 맨땅에 안드로이드 : Room
안드로이드 클린 아키텍쳐를 알아가고 있습니다. 라이브러리 Room은 다음 [자료](https://blog.yena.io/studynote/2018/09/08/Android-Kotlin-Room.html)를 참고하여 구현했으며,  
지속적으로 업데이트 될 예정입니다.  

`최근 업데이트 : '22. 08. 05.`
<br/>
## 본론
### 1. Android Room이란?

SQLite 개체 매핑 라이브러리로, Jetpack 라이브러리의 아키텍쳐 권장사항에 해당하는 AAC(Android Architecture Components)에서 제공합니다.
### 2. 기능
⚠ 안드로이드에서는 SQLite 보다 Room 라이브러리 사용을 권장합니다. 

![](https://user-images.githubusercontent.com/86638578/182986978-8d28e412-aa90-4d54-81da-ff06b7db4e9f.png)

출처 : [Android Developers Page SQLite 문서](https://developer.android.com/training/data-storage/sqlite)

1. Room은 컴파일하는 시간에 SQL 유효성 검사를 수행한다.
2. 스키마가 바뀌었을 때 영향 받는 SQL 쿼리를 직접 바꾸지 않아도 된다.
3. 상용구 코드 없이 DB 객체를 자바 객체에 매핑한다.
4. LiveData, RxJava와 같이 작동할 수 있다.

### 3. Room 구성요소

1. `Entity` : DB에 들어갈 테이블을 Kotlin Class로 구현한 것이다. 데이터 모델 클래스라고도 한다.
2. `DAO` : Database Access Object의 약자이다. DB에 접근하여 insert, delete 등을 수행한다.
3. `DB` : Database holder를 포함한다. 앱에 영구 저장되는 데이터와 기본 연결을 위한 주 액세스 지점이다. RoomDatabase를 상속받는 추상 클래스로, 테이블과 버전을 정의한다.
