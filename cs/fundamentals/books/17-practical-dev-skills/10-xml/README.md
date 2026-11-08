# 10. XML

> XML은 태그로 데이터를 감싸 표현하는 마크업 언어다. JSON 이전에 웹 데이터 교환의 표준이었고, 지금도 오래된 API, 문서 형식, 자바 계열 설정 파일에서 쓴다.

## 핵심 요약

- XML은 `<tag>값</tag>` 형태로 데이터를 표현한다. 문자 인코딩을 파일 안에 선언할 수 있고, **스키마(XSD)로 형식을 엄격하게 검증** 할 수 있다.
- JSON·YAML보다 표현이 길다. 새로 설계한다면 데이터 교환은 JSON이나 프로토콜 버퍼, 설정은 YAML을 먼저 고려한다.
- 읽을 때는 구조를 알면 **XPath**, 구조를 모르면 **반복자** 로 순회한다. 대용량 파일은 `iterparse` 로 조금씩 읽는다.
- 외부에서 받은 XML은 **XXE 공격** 을 막도록 외부 엔티티 해석을 끄고 읽는다(`defusedxml`).

## 지금도 XML을 쓰는 곳

- **AWS 등 오래된 API**: S3, EC2 같은 AWS 초기 API는 응답을 XML로 준다. JSON이 널리 쓰이기 전에 만들어졌고, 호환성 때문에 지금도 유지된다.
- **문서 형식**: MS 오피스의 `docx`, `xlsx`, `pptx` 는 XML 파일 여러 개를 ZIP으로 압축한 것이다. SVG, RSS도 XML이다.
- **설정 파일**: 자바의 Maven(`pom.xml`), 안드로이드 레이아웃과 매니페스트, 예전 스프링 설정 등

## XML 구조

```xml
<?xml version="1.0" encoding="UTF-8"?>
<message>
    <number>12345</number>
    <str option1="1" option2="2">문자열 값</str>
    <null_tag />
    <object>
        <str2>문자열 값2</str2>
    </object>
    <num_array>
        <element>1</element>
        <element attribute="value">3</element>
    </num_array>
</message>
```

| 구성 요소 | 예 | 설명 |
| --- | --- | --- |
| XML 선언 | `<?xml version="1.0" encoding="UTF-8"?>` | 버전과 문자 인코딩. 파일 맨 앞에 둔다 |
| 요소(element) | `<number>12345</number>` | 시작 태그 + 내용 + 끝 태그 |
| 빈 요소 | `<null_tag />` | 내용이 없는 요소. JSON의 `null` 과 비슷하다 |
| 속성(attribute) | `option1="1"` | 요소에 붙는 부가 정보 |
| 루트 요소 | `<message>` | 문서에 **딱 하나** 만 있어야 한다 |

- 태그 이름은 대소문자를 구분하고, 모든 시작 태그에는 끝 태그가 있어야 한다. `<`, `&` 같은 문자는 `&lt;`, `&amp;` 로 이스케이프한다.

### 배열

- XML에는 배열 문법이 없다. 같은 이름의 요소를 여러 번 쓰면 배열로 취급한다.
- 배열 요소의 태그 이름(예제의 `element`)은 팀 안에서 하나로 정해 둔다. 사람마다 다르게 쓰면 읽는 코드도 제각각이 된다.

### 속성과 요소 중 무엇을 쓸까

- 정해진 규칙은 없지만, **데이터 자체는 요소로, 데이터를 설명하는 메타데이터는 속성으로** 표현하면 자연스럽다.

```xml
<!-- 단위(cm)는 값(100)을 설명하는 정보이므로 속성이 자연스럽다 -->
<length unit="cm">100</length>

<!-- 요소로만 표현할 수도 있다 -->
<length>
    <value>100</value>
    <unit>cm</unit>
</length>
```

### 문자 인코딩

- XML은 선언부에 인코딩을 적을 수 있어 EUC-KR 같은 인코딩도 쓸 수 있다. 파서는 BOM과 선언부의 첫 바이트를 보고 인코딩을 판별한다.
- 그래도 UTF-8로 통일하는 것이 가장 안전하다([1장](../01-text-encoding/README.md)).

## XML 읽기

- 파이썬 표준 라이브러리(`xml.etree.ElementTree`)도 있지만, 예제는 XPath를 온전히 지원하고 빠른 `lxml` 을 사용한다.
- JSON·YAML은 객체(`dict`)로 바로 바뀌지만, XML은 요소들이 이어진 **트리** 로 읽는다.

### 방법 1: XPath

- **XPath** 는 파일 경로처럼 요소의 위치를 지정하는 표준 문법이다. 구조를 알고 있을 때 원하는 값에 바로 접근할 수 있다.

| XPath | 의미 |
| --- | --- |
| `/message/number` | 루트 `message` 바로 아래의 `number` |
| `object/object2/number2` | 현재 요소 기준 상대 경로 |
| `//element` | 문서 어디에 있든 모든 `element` |
| `/message/num_array/element[@attribute='value']` | `attribute` 속성 값이 `value` 인 요소 |
| `/message/str/@option1` | `str` 요소의 `option1` 속성 값 |

```python
tree = etree.parse("message1.xml")
number = tree.xpath("/message/number")[0].text   # "12345"
for element in tree.xpath("/message/num_array/element"):
    print(element.text, element.attrib)
```

### 방법 2: 반복자

- 사용자가 보낸 데이터처럼 구조를 미리 알 수 없거나 구조가 자주 바뀐다면, 자식 요소를 반복자로 돌면서 재귀적으로 읽는다(`xpath_reader2.py`).

### 대용량 파일

- `etree.parse()` 는 파일 전체를 메모리에 올린다. 수백 MB 이상의 파일은 `etree.iterparse()` 로 필요한 요소만 처리하고 바로 메모리에서 지운다.

## XML 쓰기

- XML 구조는 `dict` 와 일대일로 대응하지 않아 JSON보다 만들기 까다롭다. 배열 요소 이름을 하나로 정하고 속성을 쓰지 않으면 `dict` 를 쉽게 XML로 바꿀 수 있다(`xml_writer.py`). 속성까지 표현하려면 클래스 같은 별도 구조가 필요하다.

```python
etree.tostring(root, xml_declaration=True, encoding="UTF-8", pretty_print=True)
```

- `xml_declaration=True`: 인코딩 선언을 항상 넣는다. 필수는 아니지만, 다른 시스템이 인코딩을 추측하지 않아도 된다.
- `pretty_print=True`: 사람이 읽는 설정 파일에는 켜고, 네트워크로 보내는 데이터에는 꺼서 크기를 줄인다.

## 보안: XXE 공격

- XML은 **엔티티** 라는 기능으로 외부 파일이나 URL의 내용을 문서에 끌어올 수 있다. 공격자가 이 기능을 악용한 XML을 보내면 서버의 파일(`/etc/passwd` 등)을 읽거나 내부망에 요청을 보낼 수 있다. 이것이 **XXE(XML External Entity)** 공격이다.
- 엔티티를 중첩해 메모리를 고갈시키는 공격(Billion Laughs)도 있다.
- 외부에서 받은 XML은 `defusedxml` 로 읽거나, `lxml` 이라면 `etree.XMLParser(resolve_entities=False, no_network=True)` 처럼 엔티티 해석과 네트워크 접근을 끄고 읽는다.

## 예제 코드

| 파일 | 내용 |
| --- | --- |
| `message1.xml` | 8장 `message1.json` 과 같은 내용을 XML로 표현(속성 포함) |
| `open_xml_file.py` | XML 파일을 읽어 그대로 출력 |
| `xpath_reader.py` | XPath로 값, 속성, 배열 요소 읽기 |
| `xpath_reader2.py` | 반복자로 트리 전체를 재귀적으로 순회 |
| `xml_writer.py` | `dict` 를 `message2.xml` 로 저장 |

```sh
cd 10-xml
../.venv/Scripts/python.exe xpath_reader.py
```

## 더 알아보기

- **XSD(XML Schema)**: 요소, 타입, 필수 여부를 정의하고 문서를 검증한다. `lxml.etree.XMLSchema` 로 사용할 수 있다.
- **네임스페이스**: 서로 다른 규격의 태그 이름이 겹치지 않도록 접두사(`xmlns:`)를 붙이는 기능. SOAP, SVG, 오피스 문서에서 자주 만난다.
- **XSLT**: XML을 다른 XML이나 HTML로 변환하는 언어

---

[← 이전: 09. YAML](../09-yaml/README.md) · [목차](../README.md) · [다음: 11. 프로토콜 버퍼 →](../11-protocol-buffer/README.md)
