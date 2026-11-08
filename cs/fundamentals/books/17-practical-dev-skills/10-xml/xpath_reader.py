import sys

from lxml import etree  # type: ignore


def read_xpath(tree, xpath):
    tags = tree.xpath(xpath)
    if tags and len(tags) > 0:
        return True, tags[0]
    else:
        return False, None


def open_xml_file(filename):
    try:
        with open(filename, encoding="UTF-8") as file:
            return etree.parse(file, parser=etree.XMLParser(encoding="utf-8"))
    except (OSError, etree.XMLSyntaxError) as e:
        print(f"XML 데이터를 파싱하는 데 실패했습니다. 사유={e}")


# message1.xml 파일은 같은 디렉터리에 있어야 한다.
xml_tree = open_xml_file("message1.xml")

if not xml_tree:
    # 더 이상 로직을 진행할 수 없으므로 종료한다.
    sys.exit(0)

# XPath 기반 데이터 접근

root_tree = xml_tree.getroot()
print(f"root={root_tree.tag}")

exist, number_t = read_xpath(xml_tree, "/message/number")

if not exist:
    # XPath가 존재하지 않는 경우 여기서 처리할 수 있다.
    sys.exit(0)

assert number_t is not None
print(f"number={number_t.text}")

_, pi_t = read_xpath(xml_tree, "/message/pi")
assert pi_t is not None
print(f"pi={pi_t.text}")

_, str_t = read_xpath(xml_tree, "/message/str")
assert str_t is not None
print(f"str={str_t.text}")

for attr in str_t.attrib:
    print(f"str attribute: {attr}={str_t.attrib[attr]}")

exist, null_t = read_xpath(xml_tree, "/message/null_tag")
assert null_t is not None
print(f"null_tag={null_t.text}")

_, object_t = read_xpath(xml_tree, "/message/object")
_, str2_t = read_xpath(object_t, "str2")
assert str2_t is not None
print(f"str2={str2_t.text}")

_, number2_t = read_xpath(object_t, "object2/number2")
assert number2_t is not None
print(f"number2={number2_t.text}")

_, num_array_t = read_xpath(xml_tree, "/message/num_array")
assert num_array_t is not None
for element in num_array_t.xpath("element"):
    print(f"element={element.text}")
    for attr in element.attrib:
        print(f"\telement attribute: {attr}={element.attrib[attr]}")

_, str_array_t = read_xpath(xml_tree, "/message/str_array")
assert str_array_t is not None
for element in str_array_t.xpath("element"):
    print(f"str element={element.text}")
