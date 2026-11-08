from lxml import etree  # type: ignore

message2 = {
    "number": 12345,
    "pi": 3.14,
    "str": "문자열 값",
    "null_key": None,
    "object": {"str2": "문자열 값2", "object2": {"number2": 12345}},
    "num_array": [1, 2, 3, 4, 5],
    "str_array": ["one", "two", "three", "four", "five"],
}


def to_xml(tree, dict_object):
    for key in dict_object:
        # key에 해당하는 서브 트리 객체를 생성한다.
        element = etree.SubElement(tree, key)
        value = dict_object[key]
        if value:
            # 키에 대한 값이 존재하는 경우 값의 타입을 확인한 후 처리한다.
            if type(value) is str:
                # dict 값이 단순 문자열인 경우 값만 추가한다.
                element.text = value
                # dict 값이 int, float인 경우 문자열로 변환하여 추가한다.
            elif type(value) in (int, float):
                element.text = str(value)
            elif type(value) is dict:
                # dict 값이 또 다른 dict 객체인 경우 이 함수를 재귀적으로 호출한다.
                to_xml(element, value)
            elif type(value) is list:
                # dict 값이 리스트인 경우 리스트를 순회하며 값을 추가한다.
                for v in value:
                    etree.SubElement(element, "element").text = str(v)
            else:
                # XML에서 지원하지 않는 타입 존재 시
                assert False
        else:
            # 키에 대한 값이 존재하지 않는 경우 키만 등록한다.
            pass


xml_tree = etree.Element("message")
to_xml(xml_tree, message2)

with open("message2.xml", "wb") as file:
    file.write(
        etree.tostring(
            xml_tree, xml_declaration=True, encoding="UTF-8", pretty_print=True
        )
    )
