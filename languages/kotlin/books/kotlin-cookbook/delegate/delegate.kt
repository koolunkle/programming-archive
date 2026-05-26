import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/*
    코틀린 표준 라이브러리에는 lazy, nonNull, observable, vetoable 대리자 및 사용자 정의 대리자 등이 있다
    클래스 대리자를 통해 상속을 합서응로 대체할 수 있고, 속성 대리자를 통해 어떤 속성의 획득자와 설정자를
    다른 클래스에 있는 속성의 획득자와 설정자로 대체할 수 있다

    싱글톤 클래스, 팩토리 메소드, private 구현 클래스의 조합은 코틀린의 구현 패턴
    사용자 정의 대리자를 작성하려면 싱글톤 클래스와 팩토리 메소드, private 구현 클래스의 조합 패턴을 따르는 것을 고려
*/

/*
    대리자를 사용해서 합성 구현하기

    다른 클래스의 인스턴스가 포함된 클래스를 만들고 해당 클래스에 연산을 위임하고 싶은 경우
    연산을 위임할 메소드가 포함된 인터페이스를 만들고, 클래스에서 해당 인터페이스를 구현한 다음,
    by 키워드를 사용해 바깥쪽에 래퍼 클래스를 만든다
    (최신 객체 지향 디자인은 강한 결합 없이 기능을 추가할 때 상속보다는 합성을 선호한다)
*/

// 포함된 객체의 인터페이스와 클래스
interface Dialable {
    fun dial(number: String): String
}

class Phone : Dialable {

    override fun dial(number: String): String {
        return "Dialing $number..."
    }
}

interface Snappable {
    fun takePicture(): String
}

class Camera : Snappable {
    override fun takePicture() = "Taking picture..."
}

// SmartPhone 클래스를 포함된 객체에 위임
class SmartPhone(private val phone: Dialable = Phone(), private val camera: Snappable = Camera()) :
        Dialable by phone, Snappable by camera

/*
    lazy 대리자 사용하기

    어떤 속성이 필요할 때까지 해당 속성의 초기화를 지연시키고 싶은 경우
    코틀린 표준 라이브러리의 lazy 대리자를 사용
    lazy 함수는 최상위 함수인 반면 다른 대리자는 Delegates 인스턴스의 일부분이다
*/

// 기본, 스스로 동기화
// fun <T> lazy(initializer: () -> T): Lazy<T>

/*
    lazy 인스턴스가 다수의 스레드 간에 초기화를 동기화하는 방법을 명시
    mode의 기본값은 LazyThreadSafetyMode.SYNCRONIZED
    SYNCRONIZED: 오직 하나의 스레드만 Lazy 인스턴스를 초기화할 수 있게 락을 사용
    PUBLICATION: 초기화 함수가 여러 번 호출될 수 있지만 첫 번째 리턴값만 사용
    NONE: 락이 사용되지 않음
    fun <T> lazy(mode: LazyThreadSafetyMode, initializer: () -> T): Lazy<T>
*/

// 동기화 락을 위해 제공된 객체를 사용
// fun <T> lazy(lock: Any?, initializer: () -> T): Lazy<T>

/*
    값이 널이 될 수 없게 만들기

    처음 접근이 일어나기 전에 값이 초기화되지 않았다면 에외를 던지고 싶은 경우,
    notNull 함수를 이용하여 값이 설정되지 않았따면 예외를 던지는 대리자 제공

    notNull 대리자 동작 확인

    var shouldNotBeNull: String by Delegates.nonNull<String>()

    @Test
    fun `uninitialized value throws exception`() {
        assertThrows<IllegalStateException> { shouldNotBeNull }
    }

    @Test
    fun `intialize value then retrieve it`() {
        shouldNotBeNull = "Hello, World!"
        assertDoesNotThrow { shouldNotBeNull }
        assertEquals("Hello, World!", shouldNotBeNull)
    }
*/

// 표준 라이브러리의 notNull 구현
// Delegates는 싱글톤(class 대신 object)
object Delegates {
    // NotNullVar 클래스를 인스턴스화하는 팩토리 메소드
    fun <T : Any> notNull(): ReadWriteProperty<Any?, T> = NotNullVar()
}

private class NotNullVar<T : Any>() :
        ReadWriteProperty<Any?, T> { // ReadWriteProperty를 구현한 private 클래스
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
                ?: throw IllegalStateException(
                        "Property ${property.name} should be initialized before get"
                )
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

/*
    Observable과 vetoable 대리자 사용하기

    속성의 변경을 가로채서, 필요에 따라 변경을 거부하고 싶은 경우
    변경 감지에는 observable 함수를 사용하고, 변경의 적용 여부를 결정할 때는 vetoable 함수와 람다 사용

    observable 함수의 시그니처
    fun <T> observable(
        initialValue : T,
        onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
    ): ReadWriteProperty<Any?, T>

    vetoable 함수의 시그니처
    fun <T> vetoable(
        initialValue: T,
        onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit,
    ): ReadWriteProperty<Any?, T>
*/

// Delegate 내의 팩토리 함수 (ReadWriteProperty, ReadOnlyProperty, ObservableProperty)
object Delegatess {
    inline fun <T> observable(
            initialValue: T,
            crossinline onChange: (property: KProperty<*>, oldVale: T, newValue: T) -> Unit
    ): ReadWriteProperty<Any?, T> =
            object : ObservableProperty<T>(initialValue) {
                override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) =
                        onChange(property, oldValue, newValue)
            }

    inline fun <T> vetoable(
            initialValue: T,
            crossinline onChange: (property: KProperty<*>, oldVale: T, newValue: T) -> Boolean
    ): ReadWriteProperty<Any?, T> =
            object : ObservableProperty<T>(initialValue) {
                override fun beforeChange(
                        property: KProperty<*>,
                        oldValue: T,
                        newValue: T
                ): Boolean = onChange(property, oldValue, newValue)
            }
}

// 대리자를 제공하기 위해 사용되는 ObservableProperty 클래스
abstract class ObservableProperty<T>(initialValue: T) : ReadWriteProperty<Any?, T> {
    private var value = initialValue

    protected open fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean =
            true

    protected open fun afterChange(property: KProperty<*>, oldValue: T, newValue: T): Unit {}

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val oldValue = this.value
        if (!beforeChange(property, oldValue, value)) {
            return
        }
        this.value = value
        afterChange(property, oldValue, value)
    }
}

/*
    inline과 crossline

    inline 키워드는 컴파일러가 함수만 호출하는 완전히 새로운 객체를 생성하는 것이 아닌
    해당 호출 위치를 실제 소스 코드로 대체하도록 지시한다

    inline 함수는 지역 객체 또는 중첩 함수 같은 다른 컨텍스트에서 실행되어야 하는 파라미터로써 전달되는 람다이다
    이러한 '로컬이 아닌' 제어 흐름은 람다 내에서는 허용되지 않는다

    해당 예제에서는 ObservableProperty를 확장한 클래스 대신 observable 또는 vetoable와 관련해
    onChange 람다가 실행되기 때문에 crossline 제어자가 필요하다
*/

fun main() {
    // SmartPhone 클래스 테스트
    val smartPhone = SmartPhone(Phone(), Camera())
    println(smartPhone.dial("010-7153-7394"))
    println(smartPhone.takePicture())

    // 처음 접근하게 될 때까지 속성의 초기화를 기다림
    val ultimateAnswer: Int by lazy {
        println("computing the answer")
        42
    }

    // 구체적인 방법을 명시하지 않고 접근 전에 초기화가 필요함
    var shouldNotBeNull: String by Delegates.notNull<String>()
    // println(shouldNotBeNull)

    // observable과 vetoable 함수의 사용법
    var watched: Int by
            Delegates.observable(1) { prop, old, new ->
                println("${prop.name} changed from $old to $new")
            }

    watched *= 2
    watched *= 2
    println(watched)

    var checked: Int by
            Delegates.vetoable(0) { prop, old, new ->
                println("Trying to change ${prop.name} from $old to $new")
                new >= 0
            }
    checked = 10
    checked = -1
    checked = 5
    println(checked)
}
